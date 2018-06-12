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
package com.intellij.ide.util;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yole
 */
public abstract class PsiNavigationSupport {
  public static PsiNavigationSupport getInstance() {
    return ServiceManager.getService(PsiNavigationSupport.class);
  }

  @Nullable
  public abstract Navigatable getDescriptor(@NotNull PsiElement element);

  @NotNull
  public abstract Navigatable createNavigatable(@NotNull Project project, @NotNull VirtualFile vFile, int offset);

  public abstract boolean canNavigate(@NotNull PsiElement element);
  public abstract void navigateToDirectory(@NotNull PsiDirectory psiDirectory, boolean requestFocus);
}
