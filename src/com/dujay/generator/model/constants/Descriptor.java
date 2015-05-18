package com.dujay.generator.model.constants;

public class Descriptor {
  
  private String typeString;
  private String name;
  
  public Descriptor(String name, String typeString) {
    this.name = name;
    this.typeString = typeString;
  }
  
  public Descriptor(String name, ClassDescriptor classType) {
    this.name = name;
    this.typeString = classType.getTypeString();
  }

  public static String getTypeString(Class<?> c) {
    if(c.equals(Void.class)) {
      // V is void
      return "V";
    }
    if(c.isPrimitive()) {
      // uppercased first character
      return "" + (c.getName().charAt(0));
    }

    String typename = c.getTypeName().replace('.', '/');
    
    // arrays require the "[" in front of the "L"
    if(c.isArray()) {
      return "[L" + typename.substring(0, typename.length() - 2);
    }
    
    // normal classes just need an L
    return "L" + typename;
  }

  public String getTypeString() {
    return typeString;
  }
  
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    String name = getName();
    String type = getTypeString();
    
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    String name = getName();
    String type = getTypeString();
    
    Descriptor other = (Descriptor) obj;
    return name.equals(other.getName()) && type.equals(other.getTypeString());
  }
  
  @Override
  public String toString() {
    return String.format("%s %s", getTypeString(), getName());
  }
}
