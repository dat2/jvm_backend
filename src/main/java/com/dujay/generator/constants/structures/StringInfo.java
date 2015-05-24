package com.dujay.generator.constants.structures;

import com.dujay.generator.enums.ConstantTag;
import com.dujay.generator.visitor.Visitor;

public class StringInfo extends ConstantInfo {
  
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
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format("StringInfo [string=%s, index=%s]", utf8.getString(), getIndex());
  }

}
