package com.dujay.jvm.constants.structures;

import com.dujay.jvm.constants.enums.ConstantTag;
import com.dujay.jvm.visitor.Element;
import com.dujay.jvm.visitor.Visitor;

public class MemberRefInfo extends ConstantInfo implements Element {
  
  public static enum MemberRefType {
    FieldRef(ConstantTag.Fieldref),
    MethodRef(ConstantTag.Methodref),
    InterfaceMethodRef(ConstantTag.InterfaceMethodref);
    
    private ConstantTag tag;
    
    MemberRefType(ConstantTag tag) {
      this.tag = tag;
    }
    
    public int tag() {
      return this.tag.tag();
    }
  }
  
  private MemberRefType memberRefType;
  private ClassInfo ownerClass;
  private NameAndTypeInfo nameAndType;
  
  public MemberRefInfo(MemberRefType type, ClassInfo ownerClass, NameAndTypeInfo nameAndType) {
    this.memberRefType = type;
    this.ownerClass = ownerClass;
    this.nameAndType = nameAndType;
  }

  public ClassInfo getOwnerClass() {
    return ownerClass;
  }
  
  public NameAndTypeInfo getNameAndType() {
    return nameAndType;
  }

  @Override
  public int tag() {
    return this.memberRefType.tag();
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format(
        "MemberRefInfo [memberRefType=%s, ownerClass=%s, nameAndTypeIndex=%s, index=%s]",
        memberRefType, ownerClass.getName().getString(),
        nameAndType.getIndex(), getIndex());
  }

}
