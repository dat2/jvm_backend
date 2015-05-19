package com.dujay.generator.model.constants;

public class ClassInfo extends Descriptor {
  
  private String classString;
  
  public ClassInfo(Class<?> c) {
    super(c.getName(), Descriptor.getTypeString(c));
    classString = c.getTypeName().replace('.', '/');
  }
  
  public ClassInfo(Package pkg, String name) {
    super(name, pkg == null ? name : pkg.getName().replace('.', '/') + "/" + name);
    classString = pkg == null ? name : pkg.getName().replace('.', '/') + "/" + name;
  }

  public String getTypeString() {
    return classString;
  }

}
