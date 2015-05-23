package com.dujay.generator.enums;

public enum ConstantTag {
  
  Class(7),
  Fieldref(9),
  Methodref(10),
  InterfaceMethodref(11),
  String(8),
  Integer(3),
  Float(4),
  Long(5),
  Double(6),
  NameAndType(12),
  Utf8(1),
  MethodHandle(15),
  MethodType(16),
  InvokeDynamic(18);

  private int tag;
  ConstantTag(int flag) {
    this.tag = flag;
  }
  
  public int tag() {
    return tag;
  }
}
