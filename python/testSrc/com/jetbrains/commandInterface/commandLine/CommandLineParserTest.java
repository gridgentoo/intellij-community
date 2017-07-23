/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.jetbrains.commandInterface.commandLine;


import com.intellij.testFramework.ParsingTestCase;
import com.jetbrains.commandInterface.commandLine.psi.CommandLineArgument;
import com.jetbrains.commandInterface.commandLine.psi.CommandLineFile;
import org.junit.Assert;


/**
 * Tests command line parser
 *
 * @author Ilya.Kazakevich
 */
public final class CommandLineParserTest extends ParsingTestCase {
  public CommandLineParserTest() {
    super("", CommandLineFileType.EXTENSION, true, new CommandLineParserDefinition());
  }

  @Override
  protected String getTestDataPath() {
    return CommandTestTools.TEST_PATH;
  }

  public void testSpaces() throws Exception {
    doTest(true);
    final CommandLineFile commandLineFile = (CommandLineFile)myFile;
    Assert.assertEquals("Bad argument value", "spam and eggs", commandLineFile.getArguments().iterator().next().getValueNoQuotes());
    final CommandLineArgument optionArgument = commandLineFile.getOptions().iterator().next().findArgument();
    Assert.assertNotNull("No option argument found", optionArgument);
    Assert.assertEquals("Bad option argument value", "ketchup", optionArgument.getValueNoQuotes());
  }

  /**
   * Should be ok
   */
  public void testCommandLine() throws Exception {
    doTest(true);
  }

  /**
   * Should have a lot of errors
   */
  public void testJunk() throws Exception {
    doTest(true);
  }

  /**
   * Should have error because option ends with "="
   */
  public void testOptionNoValueJunk() throws Exception {
    doTest(true);
  }
}
