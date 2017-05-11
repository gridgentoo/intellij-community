package com.intellij.dupLocator;

import com.intellij.psi.PsiElement;
import com.intellij.util.containers.HashSet;

import java.util.TreeSet;

public interface _DupInfo {
  TreeSet<Integer> getPatterns();

  int getHeight(Integer pattern);

  int getDensity(Integer pattern);

  HashSet<PsiElement> getOccurencies(Integer pattern);

  String toString(Integer pattern);
}
