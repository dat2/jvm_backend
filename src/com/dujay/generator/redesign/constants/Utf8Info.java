package com.dujay.generator.redesign.constants;

import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class Utf8Info extends ConstantInfo implements Element {
  
  private String utf8;
  
  public Utf8Info(String utf8) {
    this.utf8 = utf8;
  }

  @Override
  public int tag() {
    return InfoTag.Utf8.tag();
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
