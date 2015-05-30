package com.dujay.jvm.constants.structures.literals;


public abstract class LongLiteralInfo extends LiteralInfo {

  public abstract int lowBytes();

  @Override
  public int numBytes() {
    return 8;
  }
}
