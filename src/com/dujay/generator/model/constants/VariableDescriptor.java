package com.dujay.generator.model.constants;

public class VariableDescriptor extends Descriptor {

  public VariableDescriptor(String name, ClassInfo classDescriptor) {
    super(name, classDescriptor.getTypeString() + ";");
  }

}
