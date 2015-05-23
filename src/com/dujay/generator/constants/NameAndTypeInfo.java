package com.dujay.generator.constants;

import com.dujay.generator.enums.ConstantTag;
import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class NameAndTypeInfo extends ConstantInfo implements Element {
  
  private Utf8Info utf8;
  private Utf8Info type;
  
  public NameAndTypeInfo(Utf8Info name, Utf8Info type) {
    this.utf8 = name;
    this.type = type;
  }
  
  public Utf8Info getName() {
    return this.utf8;
  }
  
  public Utf8Info getType() {
    return this.type;
  }
  
  public int getNameIndex() {
    return this.utf8.getIndex();
  }
  
  public int getTypeIndex() {
    return this.type.getIndex();
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
    return String.format("NameAndTypeInfo [nameIndex=%s, typeIndex=%s, index=%s]", 
        getNameIndex(), getTypeIndex(), getIndex());
  }

}
