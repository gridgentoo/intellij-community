/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package git4idea.branch;

import com.intellij.dvcs.ui.CompareBranchesDialog;
import com.intellij.dvcs.util.CommitCompareInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.util.containers.ContainerUtil;
import git4idea.GitCommit;
import git4idea.GitExecutionException;
import git4idea.GitLocalBranch;
import git4idea.GitUtil;
import git4idea.changes.GitChangeUtils;
import git4idea.commands.Git;
import git4idea.config.GitVcsSettings;
import git4idea.history.GitHistoryUtils;
import git4idea.rebase.GitRebaseUtils;
import git4idea.repo.GitRepository;
import git4idea.ui.branch.GitCompareBranchesHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Executes the logic of git branch operations.
 * All operations are run in the current thread.
 * All UI interaction is done via the {@link GitBranchUiHandler} passed to the constructor.
 */
public final class GitBranchWorker {

  private static final Logger LOG = Logger.getInstance(GitBranchWorker.class);

  @NotNull private final Project myProject;
  @NotNull private final Git myGit;
  @NotNull private final GitBranchUiHandler myUiHandler;

  public GitBranchWorker(@NotNull Project project, @NotNull Git git, @NotNull GitBranchUiHandler uiHandler) {
    myProject = project;
    myGit = git;
    myUiHandler = uiHandler;
  }
  
  public void checkoutNewBranch(@NotNull final String name, @NotNull List<GitRepository> repositories) {
    updateInfo(repositories);
    repositories = ContainerUtil.filter(repositories, repository -> {
      GitLocalBranch currentBranch = repository.getCurrentBranch();
      return currentBranch == null || !currentBranch.getName().equals(name);
    });
    if (!repositories.isEmpty()) {
      new GitCheckoutNewBranchOperation(myProject, myGit, myUiHandler, repositories, name).execute();
    }
    else {
      LOG.error("Creating new branch the same as current in all repositories: " + name);
    }
  }

  public void createBranch(@NotNull String name, @NotNull Map<GitRepository, String> startPoints) {
    updateInfo(startPoints.keySet());
    new GitCreateBranchOperation(myProject, myGit, myUiHandler, name, startPoints).execute();
  }

  public void createNewTag(@NotNull final String name, @NotNull final String reference, @NotNull final List<GitRepository> repositories) {
    for (GitRepository repository : repositories) {
      myGit.createNewTag(repository, name, null, reference);
      repository.getRepositoryFiles().refresh();
    }
  }

  public void checkoutNewBranchStartingFrom(@NotNull String newBranchName, @NotNull String startPoint,
                                            @NotNull List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitCheckoutOperation(myProject, myGit, myUiHandler, repositories, startPoint, false, true, newBranchName).execute();
  }

  public void checkout(@NotNull final String reference, boolean detach, @NotNull List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitCheckoutOperation(myProject, myGit, myUiHandler, repositories, reference, detach, false, null).execute();
  }


  public void deleteBranch(@NotNull final String branchName, @NotNull final List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitDeleteBranchOperation(myProject, myGit, myUiHandler, repositories, branchName).execute();
  }

  public void deleteRemoteBranch(@NotNull final String branchName, @NotNull final List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitDeleteRemoteBranchOperation(myProject, myGit, myUiHandler, repositories, branchName).execute();
  }

  public void merge(@NotNull final String branchName, @NotNull final GitBrancher.DeleteOnMergeOption deleteOnMerge,
                    @NotNull final List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitMergeOperation(myProject, myGit, myUiHandler, repositories, branchName, deleteOnMerge).execute();
  }

  public void rebase(@NotNull List<GitRepository> repositories, @NotNull String branchName) {
    updateInfo(repositories);
    GitRebaseUtils.rebase(myProject, repositories, new GitRebaseParams(branchName), myUiHandler.getProgressIndicator());
  }

  public void rebaseOnCurrent(@NotNull List<GitRepository> repositories, @NotNull String branchName) {
    updateInfo(repositories);
    GitRebaseUtils.rebase(myProject, repositories, new GitRebaseParams(branchName, null, "HEAD", false, false),
                          myUiHandler.getProgressIndicator());
  }

  public void renameBranch(@NotNull String currentName, @NotNull String newName, @NotNull List<GitRepository> repositories) {
    updateInfo(repositories);
    new GitRenameBranchOperation(myProject, myGit, myUiHandler, currentName, newName, repositories).execute();
  }

  public void compare(@NotNull final String branchName, @NotNull final List<GitRepository> repositories,
                      @NotNull final GitRepository selectedRepository) {
    final CommitCompareInfo myCompareInfo = loadCommitsToCompare(repositories, branchName);
    if (myCompareInfo == null) {
      LOG.error("The task to get compare info didn't finish. Repositories: \n" + repositories + "\nbranch name: " + branchName);
      return;
    }
    ApplicationManager.getApplication().invokeLater(
      () -> displayCompareDialog(branchName, GitBranchUtil.getCurrentBranchOrRev(repositories), myCompareInfo, selectedRepository));
  }

  private CommitCompareInfo loadCommitsToCompare(List<GitRepository> repositories, String branchName) {
    CommitCompareInfo compareInfo = new CommitCompareInfo();
    for (GitRepository repository : repositories) {
      loadCommitsToCompare(repository, branchName, compareInfo);
      compareInfo.put(repository, loadTotalDiff(repository, branchName));
    }
    return compareInfo;
  }

  @NotNull
  private static Collection<Change> loadTotalDiff(@NotNull GitRepository repository, @NotNull String branchName) {
    try {
      // return git diff between current working directory and branchName: working dir should be displayed as a 'left' one (base)
      return GitChangeUtils.getDiffWithWorkingDir(repository.getProject(), repository.getRoot(), branchName, null, true);
    }
    catch (VcsException e) {
      // we treat it as critical and report an error
      throw new GitExecutionException("Couldn't get [git diff " + branchName + "] on repository [" + repository.getRoot() + "]", e);
    }
  }

  private void loadCommitsToCompare(@NotNull GitRepository repository, @NotNull final String branchName,
                                                       @NotNull CommitCompareInfo compareInfo) {
    final List<GitCommit> headToBranch;
    final List<GitCommit> branchToHead;
    try {
      headToBranch = GitHistoryUtils.history(myProject, repository.getRoot(), ".." + branchName);
      branchToHead = GitHistoryUtils.history(myProject, repository.getRoot(), branchName + "..");
    }
    catch (VcsException e) {
      // we treat it as critical and report an error
      throw new GitExecutionException("Couldn't get [git log .." + branchName + "] on repository [" + repository.getRoot() + "]", e);
    }
    compareInfo.put(repository, headToBranch, branchToHead);
  }
  
  private void displayCompareDialog(@NotNull String branchName, @NotNull String currentBranch, @NotNull CommitCompareInfo compareInfo,
                                    @NotNull GitRepository selectedRepository) {
    if (compareInfo.isEmpty()) {
      Messages.showInfoMessage(myProject, String.format("<html>There are no changes between <code>%s</code> and <code>%s</code></html>",
                                                        currentBranch, branchName), "No Changes Detected");
    }
    else {
      new CompareBranchesDialog(new GitCompareBranchesHelper(myProject),
                                branchName, currentBranch, compareInfo, selectedRepository, false).show();
    }
  }

  private static void updateInfo(@NotNull Collection<GitRepository> repositories) {
    for (GitRepository repository : repositories) {
      repository.update();
    }
  }

}
