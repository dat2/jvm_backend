package com.dujay.generator.model.methods;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.writers.ByteCodeWriter;
import com.dujay.generator.writers.ByteStreamWriter;
import com.dujay.generator.writers.WriteableByteStream;

public class CodeAttribute implements WriteableByteStream {
  
  private ByteArrayOutputStream bytes;
  private ByteArrayOutputStream code;
  
  private int maxStack;
  private int maxLocals;
  
  // line number table
  
  // local variable table
  
  // stack map table
  
  public CodeAttribute(int maxStack, int maxLocals) {
    bytes = new ByteArrayOutputStream();
    code = new ByteArrayOutputStream();
    
    this.maxStack = maxStack;
    this.maxLocals = maxLocals;
  }
  
  public void prepareStream(ConstantPool constants) throws IOException {
    ByteStreamWriter writer = getStreamWriter();
    
    int codeLength = code.size();
    int exceptionsLength = 0;
    int attributesLength = 0;
    int totalAttributeLength = 2+2+4+codeLength+2+exceptionsLength+2+attributesLength;
    
    writer.u2(constants.getUtf8Index("Code"));
    writer.u4(totalAttributeLength);
    
    writer.u2(maxStack);
    writer.u2(maxLocals);
    
    // code
    writer.u4(codeLength);
    code.writeTo(writer.getStream());
    
    // TODO exceptions
    writer.u2(exceptionsLength);
    // TODO attributes
    writer.u2(attributesLength);
  }
  
  public ByteCodeWriter getByteCodeWriter() {
    return new ByteCodeWriter(new ByteStreamWriter(code));
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return bytes;
  }
}
