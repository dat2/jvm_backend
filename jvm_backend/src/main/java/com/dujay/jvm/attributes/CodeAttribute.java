package com.dujay.jvm.attributes;

import java.util.ArrayList;
import java.util.List;

import com.dujay.jvm.bytes.ByteList;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.visitor.Visitor;

public class CodeAttribute extends Attribute implements ByteList {
  
  private List<Byte> bytes;
  
  private int maxStack;
  private int maxLocals;
  private List<Attribute> attributes;
  
  // TODO exceptions
  
  // TODO attributes

  public CodeAttribute(ConstantPool cpr, int maxStack, int maxLocals) {
    super(cpr.getUtf8("Code").get());
    this.bytes = new ArrayList<Byte>();
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
    this.attributes = new ArrayList<Attribute>();
  }

  public List<Attribute> getAttributes() {
    return attributes;
  }

  public int getMaxStack() {
    return this.maxStack;
  }

  public int getMaxLocals() {
    return this.maxLocals;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }
  
  @Override
  public String toString() {
    return String.format(
        "CodeAttribute [code=%s, maxStack=%s, maxLocals=%s, attributes=%s]", getBytes(), maxStack,
        maxLocals, attributes);
  }

  public Integer currentIndex() {
    return getBytes().size() - 1;
  }

  @Override
  public List<Byte> getBytes() {
    return bytes;
  }
}
