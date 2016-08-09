/*
 * Copyright 2000-2016 JetBrains s.r.o.
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
package git4idea;

import git4idea.branch.GitBranchUtil;
import git4idea.repo.GitRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Ilya Zinoviev
 */
public final class GitSymbolicRef extends GitLocalBranch {

  @NotNull private final GitLocalBranch referencedBranch;

  public GitSymbolicRef(@NotNull String name, @NotNull GitLocalBranch branch) {
    super(name);
    referencedBranch = branch;
  }

  @NotNull
  public GitLocalBranch getReferencedBranch() {
    return referencedBranch;
  }

  @Override
  public String toString() {
    return getName() + "(" + GitBranchUtil.stripRefsPrefix(referencedBranch.toString()) + ")";
  }

  @Nullable
  @Override
  public final GitRemoteBranch findTrackedBranch(@NotNull GitRepository repository) {
    return referencedBranch.findTrackedBranch(repository);
  }

  @NotNull
  public String getPrintableName() {
    return super.getName() + "(" + referencedBranch.getName() + ")";
  }
}
