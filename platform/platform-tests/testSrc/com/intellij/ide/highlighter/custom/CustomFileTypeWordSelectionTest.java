/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package com.intellij.ide.highlighter.custom;

import com.intellij.testFramework.PlatformTestCase;
import com.intellij.testFramework.fixtures.CodeInsightTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * @author yole
 */
public class CustomFileTypeWordSelectionTest extends LightPlatformCodeInsightFixtureTestCase {
  public CustomFileTypeWordSelectionTest() {
    PlatformTestCase.initPlatformLangPrefix();
  }

  public void testCustomFileTypeBraces() {
    doTest();
  }

  public void testCustomFileTypeSkipMatchedPair() {
    doTest();
  }

  @Override
  protected String getBasePath() {
    return "/platform/platform-tests/testData/selectWord/";
  }

  private void doTest() {
    CodeInsightTestUtil.doWordSelectionTestOnDirectory(myFixture, getTestName(true), "cs");
  }
}
