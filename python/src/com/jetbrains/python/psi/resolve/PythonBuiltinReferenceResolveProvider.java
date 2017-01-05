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
package com.jetbrains.python.psi.resolve;

import com.intellij.psi.PsiElement;
import com.jetbrains.python.PyNames;
import com.jetbrains.python.psi.AccessDirection;
import com.jetbrains.python.psi.PyFile;
import com.jetbrains.python.psi.PyQualifiedExpression;
import com.jetbrains.python.psi.PyUtil;
import com.jetbrains.python.psi.impl.PyBuiltinCache;
import com.jetbrains.python.psi.impl.PyPsiUtils;
import com.jetbrains.python.psi.impl.references.PyReferenceImpl;
import com.jetbrains.python.psi.types.TypeEvalContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * User : ktisha
 */
public class PythonBuiltinReferenceResolveProvider implements PyReferenceResolveProvider {

  @NotNull
  @Override
  public List<RatedResolveResult> resolveName(@NotNull PyQualifiedExpression element, @NotNull TypeEvalContext context) {
    final String referencedName = element.getReferencedName();
    if (referencedName == null) {
      return Collections.emptyList();
    }

    final List<RatedResolveResult> result = new ArrayList<>();
    final PyBuiltinCache builtinCache = PyBuiltinCache.getInstance(PyPsiUtils.getRealContext(element));

    // resolve to module __doc__
    if (PyNames.DOC.equals(referencedName)) {
      result.addAll(
        Optional
          .ofNullable(builtinCache.getObjectType())
          .map(type -> type.resolveMember(referencedName, element, AccessDirection.of(element),
                                          PyResolveContext.noImplicits().withTypeEvalContext(context)))
          .orElse(Collections.emptyList())
      );
    }

    // ...as a builtin symbol
    if (!PyUtil.isClassPrivateName(referencedName)) {
      result.addAll(
        Optional
          .ofNullable(builtinCache.getBuiltinsFile())
          .map(builtinsFile -> resolveNameInBuiltins(referencedName, builtinsFile))
          .map(resultElement -> new ImportedResolveResult(resultElement, PyReferenceImpl.getRate(resultElement, context), null))
          .map(Collections::singletonList)
          .orElse(Collections.emptyList())
      );
    }

    return result;
  }

  @Nullable
  private static PsiElement resolveNameInBuiltins(@NotNull String referencedName, @NotNull PyFile builtinsFile) {
    final PsiElement resultElement = builtinsFile.getElementNamed(referencedName);

    if (resultElement == null && "__builtins__".equals(referencedName)) {
      return builtinsFile; // resolve __builtins__ reference
    }

    return resultElement;
  }
}
