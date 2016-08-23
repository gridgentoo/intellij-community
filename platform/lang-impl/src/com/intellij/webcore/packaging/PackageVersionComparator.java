package com.intellij.webcore.packaging;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PackageVersionComparator implements Comparator<String> {
  public static final Comparator<String> VERSION_COMPARATOR = new PackageVersionComparator();

  @Override
  public int compare(String version1, String version2) {
    final List<String> vs1 = parse(version1);
    final List<String> vs2 = parse(version2);
    int result = 0;
    for (int i = 0; i < vs1.size() && i < vs2.size(); i++) {
      result = vs1.get(i).compareTo(vs2.get(i));
      if (result != 0) {
        break;
      }
    }
    if (result == 0) {
      return vs1.size() - vs2.size();
    }
    return result;
  }

  @Nullable
  private static String replace(@NotNull String s) {
    final Map<String, String> sub = ImmutableMap.of("pre", "c",
                                                    "preview", "c",
                                                    "rc", "c",
                                                    "dev", "@");
    final String tmp = sub.get(s);
    if (tmp != null) {
      s = tmp;
    }
    if (s.equals(".") || s.equals("-")) {
      return null;
    }
    if (s.matches("[0-9]+")) {
      try {
        final long value = Long.parseLong(s);
        return String.format("%08d", value);
      }
      catch (NumberFormatException e) {
        return s;
      }
    }
    return "*" + s;
  }

  @NotNull
  private List<String> parse(@Nullable String s) {
    // Version parsing from pkg_resources ensures that all the "pre", "alpha", "rc", etc. are sorted correctly
    if (s == null) {
      return Collections.emptyList();
    }
    final Pattern COMPONENT_RE = Pattern.compile("\\d+|[a-z]+|\\.|-|.+");
    final List<String> results = new ArrayList<>();
    final Matcher matcher = COMPONENT_RE.matcher(s);
    while (matcher.find()) {
      final String component = replace(matcher.group());
      if (component == null) {
        continue;
      }
      results.add(component);
    }
    for (int i = results.size() - 1; i > 0; i--) {
      if ("00000000".equals(results.get(i))) {
        results.remove(i);
      }
      else {
        break;
      }
    }
    results.add("*final");
    return results;
  }
}
