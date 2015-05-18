package com.dujay.generator.model.constants;

public class MemberDescriptor extends Descriptor {

  private ClassDescriptor ownerClass;
  
  // hijacking other descriptors
  public MemberDescriptor(ClassDescriptor classDescriptor, Descriptor d) {
    super(d.getName(), d.getTypeString());
    this.ownerClass = classDescriptor;
  }

  public ClassDescriptor getOwnerClass() {
    return ownerClass;
  }
}
