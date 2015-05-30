package com.dujay.jvm.constants.structures.literals;

import com.dujay.jvm.constants.enums.ConstantTag;
import com.dujay.jvm.constants.structures.Utf8Info;

public class StringInfo extends LiteralInfo {
  
  private Utf8Info utf8;

  public StringInfo(String string) {
    this.setUtf8(new Utf8Info(string));
  }

  @Override
  public int tag() {
    return ConstantTag.String.tag();
  }

  public Utf8Info getUtf8() {
    return utf8;
  }

  public void setUtf8(Utf8Info utf8) {
    this.utf8 = utf8;
  }

  @Override
  public int bytes() {
    return utf8.getIndex();
  }

  @Override
  public int numBytes() {
    return 2;
  }

  @Override
  public String toString() {
    return String.format("StringInfo [string=%s, index=%s]", utf8.getString(), getIndex());
  }

}
