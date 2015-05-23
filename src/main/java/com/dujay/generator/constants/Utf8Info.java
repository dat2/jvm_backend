package com.dujay.generator.constants;

import com.dujay.generator.enums.ConstantTag;
import com.dujay.generator.visitor.Element;
import com.dujay.generator.visitor.Visitor;

public class Utf8Info extends ConstantInfo implements Element {
  
  private String utf8;
  
  public Utf8Info(String utf8) {
    this.utf8 = utf8;
  }

  @Override
  public int tag() {
    return ConstantTag.Utf8.tag();
  }

  public int length() {
    return utf8.length();
  }

  public byte[] getBytes() {
    return utf8.getBytes();
  }
  
  public String getString() {
    return utf8;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format("Utf8Info [utf8=%s, index=%s]", utf8, getIndex());
  }
}
