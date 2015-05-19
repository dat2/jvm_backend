package com.dujay.generator.model.constants;

public class MemberDescriptor extends Descriptor {

  private ClassInfo ownerClass;
  
  // hijacking other descriptors
  public MemberDescriptor(ClassInfo classDescriptor, Descriptor d) {
    super(d.getName(), d.getTypeString());
    this.ownerClass = classDescriptor;
  }

  public ClassInfo getOwnerClass() {
    return ownerClass;
  }
}
