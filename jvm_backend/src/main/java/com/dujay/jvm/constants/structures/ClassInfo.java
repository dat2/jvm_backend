package com.dujay.jvm.constants.structures;

import com.dujay.jvm.constants.enums.ConstantTag;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class ClassInfo extends ConstantInfo implements Element {
  
  private Utf8Info name;
  
  public ClassInfo(Class<?> c) {
    this.name = new Utf8Info(c.getTypeName().replace('.', '/'));
  }
  
  // allows for synthetic classes
  public ClassInfo(String u) {
    this.name = new Utf8Info(u);
  }
  
  public Utf8Info getName() {
    return this.name;
  }

  public void setName(Utf8Info name) {
    this.name = name;
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
    return String.format("ClassInfo [name=%s, index=%s]", getName().getString(), getIndex());
  }

}
