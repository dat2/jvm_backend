package com.dujay.generator.constants;

import com.dujay.generator.enums.ConstantTag;
import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class ClassInfoR extends ConstantInfo implements Element {
  
  private Utf8Info utf8;
  
  public ClassInfoR(Class<?> c) {
    this.utf8 = new Utf8Info(c.getTypeName().replace('.', '/'));
  }
  
  // allows for synthetic classes
  public ClassInfoR(String u) {
    this.utf8 = new Utf8Info(u);
  }
  
  public Utf8Info getUtf8() {
    return this.utf8;
  }

  public int getUtf8Index() {
    return utf8.getIndex();
  }
  
  @Override
  public int tag() {
    return ConstantTag.Class.tag();
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format("ClassInfo [nameIndex=%s, index=%s]", getUtf8Index(), getIndex());
  }

}
