package com.dujay.generator.model.methods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.dujay.generator.model.AccessFlag;
import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.writers.ByteCodeWriter;
import com.dujay.generator.writers.ByteStreamWriter;
import com.dujay.generator.writers.WriteableByteStream;

public class Method implements WriteableByteStream {

  private String name;
  private int access;
  
  private ByteArrayOutputStream bytes;
  
  private CodeAttribute code;
  
  public Method(String name, int maxStack, int maxLocals, AccessFlag... flags) {
    this.name = name;
    this.access = AccessFlag.mask(flags);
    this.bytes = new ByteArrayOutputStream();
    
    this.code = new CodeAttribute(maxStack, maxLocals);
  }
  
  public ByteCodeWriter getByteCodeWriter() {
    return code.getByteCodeWriter();
  }
  
  public void prepareStream(ConstantPool constants) throws IOException {
    ByteStreamWriter writer = getStreamWriter();
    writer.u2(access);
    
    int methodIndex = constants.getMethodIndex(name);
    writer.u2(methodIndex);
    writer.u2(methodIndex + 1);
    
    // attributes
    writer.u2(1);
    
    // code attribute
    code.prepareStream(constants);
    writer.appendStream(code);
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return bytes;
  }
}
