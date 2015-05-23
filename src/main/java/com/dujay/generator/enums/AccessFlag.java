package com.dujay.generator.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum AccessFlag {
  PUBLIC(0x0001),
  FINAL(0x0010),
  SUPER(0x0020),
  INTERFACE(0x0200),
  ABSTRACT(0x0400),
  SYNTHETIC(0x1000),
  ANNOTATION(0x2000),
  ENUM(0x4000),

  // methods only
  PRIVATE(0x0002),
  PROTECTED(0x0004),
  STATIC(0x0008),
  SYNCHRONIZED(0x0020),
  BRIDGE(0x0040),
  VARARGS(0x0080),
  NATIVE(0x0100),
  STRICT(0x0800);

  private int mask;

  AccessFlag(int mask) {
    this.mask = mask;
  }

  public static int mask(AccessFlag... flags) {
    int r = 0;
    for (AccessFlag flag : flags) {
      r |= flag.mask;
    }
    return r;
  }

  public static String maskedToString(final int masked) {
    return Arrays.asList(AccessFlag.values())
        .stream()
        .filter(x -> (x.mask & masked) != 0)
        .map(AccessFlag::toString)
        .collect(Collectors.joining(", "));
  }
}
