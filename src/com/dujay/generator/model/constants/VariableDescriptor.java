package com.dujay.generator.model.constants;

public class VariableDescriptor extends Descriptor {

  public VariableDescriptor(String name, ClassDescriptor classDescriptor) {
    super(name, classDescriptor.getTypeString() + ";");
  }

}
