package com.dujay.jvm.constants.structures;

import com.dujay.jvm.constants.enums.ConstantTag;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class ClassInfo extends ConstantInfo implements Element {
  
  private Utf8Info utf8;
  
  public ClassInfo(Class<?> c) {
    this.utf8 = new Utf8Info(c.getTypeName().replace('.', '/'));
  }
  
  // allows for synthetic classes
  public ClassInfo(String u) {
    this.utf8 = new Utf8Info(u);
  }
  
  public Utf8Info getName() {
    return this.utf8;
  }

  public int getNameIndex() {
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
    return String.format("ClassInfo [name=%s, index=%s]", getName().getString(), getIndex());
  }

}
