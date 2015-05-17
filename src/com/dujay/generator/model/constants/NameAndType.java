package com.dujay.generator.model.constants;

public class NameAndType {

  private String name;
  private String type;
  
  public NameAndType(String name, String type) {
    this.name = name == null ? "" : name;
    this.type = type == null ? "" : type;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NameAndType other = (NameAndType) obj;
    return name.equals(other.name) && type.equals(other.type);
  }
}
