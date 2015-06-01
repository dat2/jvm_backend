package com.dujay.jvm.constants.structures;

import com.dujay.jvm.constants.enums.ConstantTag;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class NameAndTypeInfo extends ConstantInfo implements Element {
  
  private Utf8Info name;
  private Utf8Info type;
  
  public NameAndTypeInfo(Utf8Info name, Utf8Info type) {
    this.name = name;
    this.type = type;
  }
  
  public Utf8Info getName() {
    return this.name;
  }
  
  public Utf8Info getType() {
    return this.type;
  }

  public void setName(Utf8Info name) {
    this.name =  name;
  }
  
  public void setType(Utf8Info type) {
    this.type = type;
  }

  @Override
  public int tag() {
    return ConstantTag.NameAndType.tag();
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format("NameAndTypeInfo [name=%s, type=%s, index=%s]", 
        getName().getString(), getType().getString(), getIndex());
  }


}
