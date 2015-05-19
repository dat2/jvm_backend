package com.dujay.generator.redesign.constants;

import com.dujay.generator.redesign.visitor.Element;
import com.dujay.generator.redesign.visitor.Visitor;

public class MemberRefInfo extends ConstantInfo implements Element {
  
  public static enum MemberRefType {
    FieldRef(InfoTag.Fieldref),
    MethodRef(InfoTag.Methodref),
    InterfaceMethodRef(InfoTag.InterfaceMethodref);
    
    private InfoTag tag;
    
    MemberRefType(InfoTag tag) {
      this.tag = tag;
    }
    
    public int tag() {
      return this.tag.tag();
    }
  }
  
  private MemberRefType memberRefType;
  private ClassInfoR ownerClass;
  private NameAndTypeInfo nameAndType;
  
  public MemberRefInfo(MemberRefType type, ClassInfoR ownerClass, NameAndTypeInfo nameAndType) {
    this.memberRefType = type;
    this.ownerClass = ownerClass;
    this.nameAndType = nameAndType;
  }

  @Override
  public int tag() {
    return this.memberRefType.tag();
  }

  public int getOwnerClassIndex() {
    return ownerClass.getIndex();
  }

  public int getNameAndTypeIndex() {
    return nameAndType.getIndex();
  }

  public ClassInfoR getOwnerClass() {
    return ownerClass;
  }
  
  public NameAndTypeInfo getNameAndType() {
    return nameAndType;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public String toString() {
    return String.format(
        "MemberRefInfo [memberRefType=%s, ownerClassIndex=%s, nameAndTypeIndex=%s, index=%s]",
        memberRefType, ownerClass.getIndex(),
        nameAndType.getIndex(), getIndex());
  }

}
