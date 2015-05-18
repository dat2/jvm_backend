package com.dujay.generator.model.constants;

public class ClassDescriptor extends Descriptor {
  
  private String classString;
  
  public ClassDescriptor(Class<?> c) {
    super(c.getName(), Descriptor.getTypeString(c));
    classString = c.getTypeName().replace('.', '/');
  }
  
  public ClassDescriptor(Package pkg, String name) {
    super(name, pkg == null ? name : pkg.getName().replace('.', '/') + "/" + name);
    classString = pkg == null ? name : pkg.getName().replace('.', '/') + "/" + name;
  }

  public String getClassString() {
    return classString;
  }

}
