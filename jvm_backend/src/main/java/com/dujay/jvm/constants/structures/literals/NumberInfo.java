package com.dujay.jvm.constants.structures.literals;

import com.dujay.jvm.constants.enums.ConstantTag;

public class NumberInfo extends LiteralInfo {
  
  private boolean isFloat = false;
  private int value;
  
  public NumberInfo(int value) {
    this.value = value;
  }
  
  public NumberInfo(float value) {
    this.isFloat = true;
    this.value = Float.floatToIntBits(value);
  }

  @Override
  public int bytes() {
    return this.value;
  }

  @Override
  public int numBytes() {
    return 4;
  }
  
  @Override
  public int tag() {
    return isFloat ? ConstantTag.Float.tag() : ConstantTag.Integer.tag();
  }

  @Override
  public String toString() {
    return String.format("IntegerInfo [value=%s, index=%s]", value, getIndex());
  }

}
