package com.dujay.jvm.constants.structures.literals;

import com.dujay.jvm.constants.enums.ConstantTag;

public class LongNumberInfo extends LongLiteralInfo {
  
  private boolean isDouble = false;
  private long value;
  
  public LongNumberInfo(long value) {
    this.value = value;
  }
  
  public LongNumberInfo(double value) {
    this.isDouble = true;
    this.value = Double.doubleToLongBits(value);
  }

  @Override
  public int bytes() {
    return (int) (this.value >> 32);
  }

  @Override
  public int lowBytes() {
    return (int) (value & 0xffffffff);
  }

  @Override
  public int tag() {
    return isDouble ? ConstantTag.Double.tag() : ConstantTag.Long.tag();
  }

  @Override
  public String toString() {
    return String.format("LongInfo [value=%s, index=%s]", value, getIndex());
  }

}
