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
package org.jetbrains.plugins.gradle.codeInsight

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.Ref
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import icons.ExternalSystemIcons
import icons.GradleIcons
import org.jetbrains.plugins.gradle.service.resolve.GradleExtensionsContributor
import org.jetbrains.plugins.gradle.service.resolve.GradleExtensionsContributor.Companion.getDocumentation
import org.jetbrains.plugins.groovy.dsl.holders.NonCodeMembersHolder
import org.jetbrains.plugins.groovy.lang.lexer.GroovyTokenTypes
import org.jetbrains.plugins.groovy.lang.psi.api.statements.expressions.GrReferenceExpression
import org.jetbrains.plugins.groovy.lang.psi.impl.synthetic.GrLightVariable

/**
 * @author Vladislav.Soroka
 * @since 12/12/2016
 */
class PropertiesTasksCompletionContributor : AbstractGradleCompletionContributor() {
  class PropertiesTasksCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(params: CompletionParameters,
                                context: ProcessingContext,
                                result: CompletionResultSet) {
      val position = params.position

      val prevSibling = position.prevSibling
      if (prevSibling is ASTNode && prevSibling.elementType == GroovyTokenTypes.mDOT) return

      val extensionsData = GradleExtensionsContributor.getExtensionsFor(position) ?: return
      for (gradleProp in extensionsData.findAllProperties()) {
        val docRef = Ref.create<String>()
        val propVar = object : GrLightVariable(position.manager, gradleProp.name, gradleProp.typeFqn, position) {
          override fun getNavigationElement(): PsiElement {
            val navigationElement = super.getNavigationElement()
            navigationElement.putUserData(NonCodeMembersHolder.DOCUMENTATION, docRef.get())
            return navigationElement
          }
        }
        docRef.set(getDocumentation(gradleProp, propVar))
        val elementBuilder = LookupElementBuilder.create(propVar, gradleProp.name)
          .withIcon(AllIcons.Nodes.Property)
          .withPresentableText(gradleProp.name)
          .withTailText("  via ext", true)
          .withTypeText(propVar.type.presentableText, GradleIcons.Gradle, false)
        result.addElement(elementBuilder)
      }

      for (gradleTask in extensionsData.tasks) {
        val docRef = Ref.create<String>()
        val taskVar = object : GrLightVariable(position.manager, gradleTask.name, gradleTask.typeFqn, position) {
          override fun getNavigationElement(): PsiElement {
            val navigationElement = super.getNavigationElement()
            navigationElement.putUserData(NonCodeMembersHolder.DOCUMENTATION, docRef.get())
            return navigationElement
          }
        }
        docRef.set(getDocumentation(gradleTask, taskVar))
        val elementBuilder = LookupElementBuilder.create(taskVar, gradleTask.name)
          .withIcon(ExternalSystemIcons.Task)
          .withPresentableText(gradleTask.name)
          .withTailText("  task", true)
          .withTypeText(taskVar.type.presentableText)
        result.addElement(elementBuilder)
      }
    }
  }

  init {
    extend(CompletionType.BASIC, PATTERN, PropertiesTasksCompletionProvider())
  }

  companion object {
    private val PATTERN = psiElement().and(GRADLE_FILE_PATTERN).withParent(GrReferenceExpression::class.java)
  }
}