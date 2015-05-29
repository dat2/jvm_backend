package com.dujay.jvm.attributes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dujay.jvm.bytes.ByteStreamWriter;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.visitor.Visitor;

public class CodeAttribute extends Attribute implements ByteStreamWriter {
  
  private ByteArrayOutputStream codeStream;
  
  private int maxStack;
  private int maxLocals;
  private List<Attribute> attributes;
  
  // TODO exceptions
  
  // TODO attributes

  public CodeAttribute(ConstantPool cpr, int maxStack, int maxLocals) {
    super(cpr.getUtf8("Code").get());
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
    this.codeStream = new ByteArrayOutputStream();
    this.attributes = new ArrayList<Attribute>();
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return codeStream;
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
        "CodeAttribute [codeLength=%s bytes, maxStack=%s, maxLocals=%s, attributes=%s]", this.codeStream.size(), maxStack,
        maxLocals, attributes);
  }

}
