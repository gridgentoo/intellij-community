/* The following code was generated by JFlex 1.7.0-SNAPSHOT tweaked for IntelliJ platform */

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
package com.intellij.spi.parsing;

import com.intellij.psi.JavaTokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.spi.parsing.SPITokenType;
import com.intellij.lexer.FlexLexer;

@SuppressWarnings({"ALL"})

/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-SNAPSHOT
 * from the specification file <tt>_SPILexer.flex</tt>
 */
class _SPILexer implements FlexLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [11, 6, 4]
   * Total runtime size is 13600 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[(ZZ_CMAP_Z[ch>>10]<<6)|((ch>>4)&0x3f)]<<4)|(ch&0xf)];
  }

  /* The ZZ_CMAP_Z table has 1088 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\2\11\1\12\1\13\6\14\1\15\23\14\1\16"+
    "\1\14\1\17\1\20\12\14\1\21\10\11\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1"+
    "\32\1\11\1\33\1\34\2\11\1\14\1\35\3\11\1\36\10\11\1\37\1\40\20\11\1\41\2\11"+
    "\1\42\5\11\1\43\4\11\1\44\1\45\4\11\51\14\1\46\3\14\1\47\1\50\4\14\1\51\12"+
    "\11\1\52\u0381\11");

  /* The ZZ_CMAP_Y table has 2752 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\4\1\6\2\1\1\7\1\10\1\11\1\12\1\11\1\12\34\11\1"+
    "\13\1\14\1\15\10\1\1\16\1\17\1\11\1\20\4\11\1\21\10\11\1\22\12\11\1\4\1\11"+
    "\1\23\1\4\1\11\1\24\4\1\1\11\1\6\1\25\2\1\2\11\1\6\1\1\1\26\1\4\5\11\1\27"+
    "\1\30\1\31\1\1\1\32\1\11\1\1\1\33\5\11\1\34\1\35\1\36\1\11\1\6\1\37\1\11\1"+
    "\40\1\41\1\1\1\11\1\42\4\1\1\11\1\43\4\1\1\44\2\11\1\45\1\1\1\46\1\47\1\4"+
    "\1\50\1\51\1\52\1\53\1\54\1\55\1\47\1\14\1\56\1\51\1\52\1\57\1\1\1\60\1\61"+
    "\1\62\1\63\1\20\1\52\1\64\1\1\1\65\1\47\1\66\1\67\1\51\1\52\1\64\1\1\1\55"+
    "\1\47\1\35\1\70\1\71\1\72\1\73\1\1\1\65\1\61\1\1\1\74\1\32\1\52\1\45\1\1\1"+
    "\75\1\47\1\1\1\74\1\32\1\52\1\76\1\1\1\54\1\47\1\77\1\74\1\32\1\11\1\100\1"+
    "\54\1\101\1\47\1\102\1\103\1\104\1\11\1\105\1\106\1\1\1\61\1\1\1\4\2\11\1"+
    "\107\1\106\1\3\2\1\1\110\1\111\1\112\1\113\1\114\1\115\2\1\1\65\1\1\1\3\1"+
    "\1\1\116\1\11\1\117\1\1\1\120\7\1\2\11\1\6\1\101\1\3\1\121\1\122\1\123\1\124"+
    "\1\3\2\11\1\125\2\11\1\126\24\11\1\127\1\130\2\11\1\127\2\11\1\131\1\132\1"+
    "\12\3\11\1\132\3\11\1\6\2\1\1\11\1\1\5\11\1\133\1\4\45\11\1\134\1\11\1\4\1"+
    "\6\4\11\1\6\1\135\1\136\1\14\1\11\1\14\1\11\1\14\1\136\1\65\3\11\1\137\1\1"+
    "\1\140\1\3\2\1\1\3\5\11\1\24\2\11\1\141\4\11\1\34\1\11\1\142\2\1\1\61\1\11"+
    "\1\143\1\43\2\11\1\144\1\11\1\73\1\3\2\1\1\11\1\106\3\11\1\43\2\1\2\3\1\145"+
    "\5\1\1\103\2\11\1\137\1\146\1\3\2\1\1\147\1\11\1\150\1\36\2\11\1\34\1\1\2"+
    "\11\1\137\1\1\1\151\1\36\1\11\1\143\6\1\1\152\1\153\14\11\4\1\21\11\1\133"+
    "\2\11\1\133\1\154\1\11\1\143\3\11\1\155\1\156\1\157\1\117\1\156\7\1\1\160"+
    "\1\1\1\117\6\1\1\161\1\162\1\163\1\164\1\165\3\1\1\166\147\1\2\11\1\142\2"+
    "\11\1\142\10\11\1\167\1\170\2\11\1\125\3\11\1\171\1\1\1\11\1\106\4\172\4\1"+
    "\1\101\35\1\1\173\2\1\1\174\1\4\4\11\1\175\1\4\4\11\1\126\1\103\1\11\1\143"+
    "\1\4\4\11\1\142\1\1\1\11\1\6\3\1\1\11\40\1\133\11\1\34\4\1\135\11\1\34\2\1"+
    "\10\11\1\117\4\1\2\11\1\143\20\11\1\117\1\11\1\176\1\1\2\11\1\142\1\101\1"+
    "\11\1\143\4\11\1\34\2\1\1\177\1\200\5\11\1\201\1\11\1\143\1\24\3\1\1\177\1"+
    "\202\1\11\1\25\1\1\3\11\1\137\1\200\2\11\1\137\1\1\1\3\1\1\1\203\1\36\1\11"+
    "\1\34\1\11\1\106\1\1\1\11\1\117\1\44\2\11\1\25\1\101\1\3\1\204\1\205\2\11"+
    "\1\42\1\1\1\206\1\3\1\11\1\207\3\11\1\210\1\211\1\212\1\6\1\62\1\213\1\214"+
    "\1\172\2\11\1\126\1\34\7\11\1\25\1\3\72\11\1\137\1\11\1\215\2\11\1\144\20"+
    "\1\26\11\1\143\6\11\1\73\2\1\1\106\1\216\1\52\1\217\1\220\6\11\1\14\1\1\1"+
    "\147\25\11\1\143\1\1\4\11\1\200\2\11\1\24\2\1\1\144\7\1\1\204\7\11\1\117\1"+
    "\1\1\3\1\4\1\6\1\4\1\6\1\221\4\11\1\142\1\222\1\223\2\1\1\224\1\11\1\12\1"+
    "\225\2\143\2\1\7\11\1\6\30\1\1\11\1\117\3\11\1\65\2\1\2\11\1\1\1\11\1\226"+
    "\2\11\1\34\1\11\1\143\2\11\1\227\3\1\11\11\1\143\1\3\5\1\2\11\1\24\3\11\1"+
    "\137\11\1\23\11\1\106\1\11\1\34\1\24\11\1\1\230\2\11\1\231\1\11\1\34\1\11"+
    "\1\106\1\11\1\142\4\1\1\11\1\232\1\11\1\34\1\11\1\73\4\1\3\11\1\233\4\1\1"+
    "\65\1\234\1\11\1\137\2\1\1\11\1\117\1\11\1\117\2\1\1\116\1\11\1\43\1\1\3\11"+
    "\1\34\1\11\1\34\1\11\1\25\1\11\1\14\6\1\4\11\1\42\3\1\3\11\1\25\3\11\1\25"+
    "\60\1\1\147\2\11\1\24\2\1\1\61\1\1\1\147\2\11\2\1\1\11\1\42\1\3\1\147\1\11"+
    "\1\106\1\61\1\1\2\11\1\235\1\147\2\11\1\25\1\236\1\237\2\1\1\11\1\20\1\144"+
    "\5\1\1\240\1\241\1\42\2\11\1\142\1\1\1\3\1\67\1\51\1\52\1\64\1\1\1\242\1\14"+
    "\21\1\3\11\1\1\1\243\1\3\12\1\2\11\1\142\2\1\1\244\2\1\3\11\1\1\1\245\1\3"+
    "\2\1\2\11\1\6\1\1\1\3\3\1\1\11\1\73\1\1\1\3\26\1\4\11\1\3\1\101\34\1\3\11"+
    "\1\42\20\1\71\11\1\73\16\1\14\11\1\137\53\1\2\11\1\142\75\1\44\11\1\106\33"+
    "\1\43\11\1\42\1\11\1\142\1\3\6\1\1\11\1\143\1\1\3\11\1\1\1\137\1\3\1\147\1"+
    "\246\1\11\67\1\4\11\1\43\1\65\3\1\1\147\6\1\1\14\77\1\6\11\1\6\1\117\1\42"+
    "\1\73\66\1\5\11\1\204\3\11\1\136\1\247\1\250\1\251\3\11\1\252\1\253\1\11\1"+
    "\254\1\255\1\32\24\11\1\256\1\11\1\32\1\126\1\11\1\126\1\11\1\204\1\11\1\204"+
    "\1\142\1\11\1\142\1\11\1\52\1\11\1\52\1\11\1\257\3\260\14\11\1\43\123\1\1"+
    "\251\1\11\1\261\1\262\1\263\1\264\1\265\1\266\1\267\1\144\1\270\1\144\24\1"+
    "\55\11\1\106\2\1\103\11\1\43\15\11\1\143\150\11\1\14\25\1\41\11\1\143\36\1");

  /* The ZZ_CMAP_A table has 2960 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\1\1\5\1\0\1\1\1\5\22\0\1\1\2\0\1\4\1\6\11\0\1\7\1\0\12\3\7\0\32\2"+
    "\4\0\14\2\17\0\1\2\12\0\1\2\4\0\1\2\5\0\27\2\1\0\12\2\4\0\14\2\16\0\5\2\7"+
    "\0\1\2\1\0\1\2\1\0\5\2\1\0\2\2\2\0\4\2\1\0\1\2\6\0\1\2\1\0\3\2\1\0\1\2\1\0"+
    "\4\2\1\0\23\2\1\0\13\2\10\0\15\2\2\0\1\2\6\0\10\2\10\0\3\2\15\0\12\3\4\0\6"+
    "\2\1\0\1\2\17\0\2\2\7\0\2\2\12\3\3\2\2\0\2\2\1\0\16\2\15\0\11\2\13\0\1\2\16"+
    "\0\12\3\6\2\4\0\2\2\4\0\1\2\5\0\6\2\4\0\1\2\11\0\1\2\3\0\1\2\7\0\11\2\7\0"+
    "\5\2\17\0\26\2\3\0\1\2\2\0\1\2\7\0\12\2\4\0\12\3\1\2\4\0\10\2\2\0\2\2\2\0"+
    "\26\2\1\0\7\2\1\0\1\2\3\0\4\2\3\0\1\2\20\0\1\2\15\0\2\2\1\0\1\2\5\0\6\2\4"+
    "\0\2\2\1\0\2\2\1\0\2\2\1\0\2\2\17\0\4\2\1\0\1\2\7\0\12\3\2\0\3\2\20\0\11\2"+
    "\1\0\2\2\1\0\2\2\1\0\5\2\3\0\1\2\2\0\1\2\30\0\1\2\13\0\10\2\2\0\1\2\3\0\1"+
    "\2\1\0\6\2\3\0\3\2\1\0\4\2\3\0\2\2\1\0\1\2\1\0\2\2\3\0\2\2\3\0\3\2\3\0\14"+
    "\2\13\0\10\2\1\0\2\2\10\0\3\2\5\0\4\2\1\0\5\2\3\0\1\2\3\0\2\2\15\0\13\2\2"+
    "\0\1\2\21\0\1\2\12\0\6\2\5\0\22\2\3\0\10\2\1\0\11\2\1\0\1\2\2\0\7\2\11\0\1"+
    "\2\1\0\2\2\15\0\2\2\1\0\1\2\2\0\2\2\1\0\1\2\2\0\1\2\6\0\4\2\1\0\7\2\1\0\3"+
    "\2\1\0\1\2\1\0\1\2\2\0\2\2\1\0\4\2\1\0\2\2\11\0\1\2\2\0\5\2\1\0\1\2\11\0\12"+
    "\3\2\0\14\2\1\0\24\2\13\0\5\2\3\0\6\2\4\0\4\2\3\0\1\2\3\0\2\2\7\0\3\2\4\0"+
    "\15\2\14\0\1\2\1\0\6\2\1\0\1\2\5\0\1\2\2\0\13\2\1\0\15\2\1\0\4\2\2\0\7\2\1"+
    "\0\1\2\1\0\4\2\2\0\1\2\1\0\4\2\2\0\7\2\1\0\1\2\1\0\4\2\2\0\16\2\2\0\6\2\2"+
    "\0\15\2\2\0\1\2\1\0\10\2\7\0\15\2\1\0\6\2\23\0\1\2\4\0\1\2\3\0\11\2\1\0\1"+
    "\2\5\0\17\2\1\0\16\2\2\0\14\2\13\0\1\2\15\0\7\2\7\0\16\2\15\0\2\2\12\3\3\0"+
    "\3\2\11\0\4\2\1\0\4\2\3\0\2\2\11\0\10\2\1\0\1\2\1\0\1\2\1\0\1\2\1\0\6\2\1"+
    "\0\7\2\1\0\1\2\3\0\3\2\1\0\7\2\3\0\4\2\2\0\6\2\5\0\1\2\15\0\1\2\2\0\1\2\4"+
    "\0\1\2\2\0\12\2\1\0\1\2\3\0\5\2\6\0\1\2\1\0\1\2\1\0\1\2\1\0\4\2\1\0\13\2\2"+
    "\0\4\2\5\0\5\2\4\0\1\2\4\0\2\2\13\0\5\2\6\0\4\2\3\0\2\2\14\0\10\2\7\0\10\2"+
    "\1\0\7\2\6\0\2\2\12\0\5\2\5\0\2\2\3\0\7\2\6\0\3\2\12\3\2\2\13\0\11\2\2\0\27"+
    "\2\2\0\7\2\1\0\3\2\1\0\4\2\1\0\4\2\2\0\6\2\3\0\1\2\1\0\1\2\2\0\5\2\1\0\12"+
    "\2\12\3\5\2\1\0\3\2\1\0\10\2\4\0\7\2\3\0\1\2\3\0\2\2\1\0\1\2\3\0\2\2\2\0\5"+
    "\2\2\0\1\2\1\0\1\2\30\0\3\2\3\0\6\2\2\0\6\2\2\0\6\2\11\0\7\2\4\0\5\2\3\0\5"+
    "\2\5\0\1\2\1\0\10\2\1\0\5\2\1\0\1\2\1\0\2\2\1\0\2\2\1\0\12\2\6\0\12\2\2\0"+
    "\6\2\2\0\6\2\2\0\6\2\2\0\3\2\3\0\14\2\1\0\16\2\1\0\2\2\1\0\2\2\1\0\10\2\6"+
    "\0\4\2\4\0\16\2\2\0\1\2\1\0\14\2\1\0\2\2\3\0\1\2\2\0\4\2\1\0\2\2\12\0\10\2"+
    "\6\0\6\2\1\0\3\2\1\0\12\2\3\0\1\2\12\0\4\2\13\0\12\3\1\2\1\0\1\2\3\0\7\2\1"+
    "\0\1\2\1\0\4\2\1\0\17\2\1\0\2\2\14\0\3\2\4\0\2\2\1\0\1\2\20\0\4\2\10\0\1\2"+
    "\13\0\10\2\5\0\3\2\2\0\1\2\2\0\2\2\2\0\4\2\1\0\14\2\1\0\1\2\1\0\7\2\1\0\21"+
    "\2\1\0\4\2\2\0\10\2\1\0\7\2\1\0\14\2\1\0\4\2\1\0\5\2\1\0\1\2\3\0\14\2\2\0"+
    "\13\2\1\0\10\2\2\0\22\3\1\0\2\2\1\0\1\2\2\0\1\2\1\0\12\2\1\0\4\2\1\0\1\2\1"+
    "\0\1\2\6\0\1\2\4\0\1\2\1\0\1\2\1\0\1\2\1\0\3\2\1\0\2\2\1\0\1\2\2\0\1\2\1\0"+
    "\1\2\1\0\1\2\1\0\1\2\1\0\1\2\1\0\2\2\1\0\1\2\2\0\4\2\1\0\7\2\1\0\4\2\1\0\4"+
    "\2\1\0\1\2\1\0\12\2\1\0\5\2\1\0\3\2\1\0\5\2\1\0\5\2");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\1\2\1\3\1\4\1\5\1\6";

  private static int [] zzUnpackAction() {
    int [] result = new int[7];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\10\0\20\0\30\0\40\0\10\0\10";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[7];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\2\1\5\1\3\1\6\1\7"+
    "\11\0\1\3\3\0\1\3\4\0\2\4\4\0\5\5"+
    "\1\0\2\5";

  private static int [] zzUnpackTrans() {
    int [] result = new int[40];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\3\1\2\11";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[7];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
  public _SPILexer() {
    this((java.io.Reader)null);
  }


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  _SPILexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return getTokenStart() + yylength();
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType advance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        return null;
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { return JavaTokenType.BAD_CHARACTER;
            }
          case 7: break;
          case 2: 
            { return JavaTokenType.WHITE_SPACE;
            }
          case 8: break;
          case 3: 
            { return SPITokenType.IDENTIFIER;
            }
          case 9: break;
          case 4: 
            { return JavaTokenType.END_OF_LINE_COMMENT;
            }
          case 10: break;
          case 5: 
            { return SPITokenType.DOLLAR;
            }
          case 11: break;
          case 6: 
            { return JavaTokenType.DOT;
            }
          case 12: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
