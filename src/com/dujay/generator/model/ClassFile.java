package com.dujay.generator.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dujay.generator.model.constants.ConstantPool;
import com.dujay.generator.model.methods.MethodPool;
import com.dujay.generator.writers.ByteStreamWriter;
import com.dujay.generator.writers.WriteableByteStream;

public class ClassFile extends File implements WriteableByteStream {
  
  private static final long serialVersionUID = 7377138959076812107L;
  
  public static final int JAVA8_MINOR = 0x0000;
  public static final int JAVA8_MAJOR = 0x0034;
  
  private ByteArrayOutputStream output;
  private ByteStreamWriter writer;
  
  public ClassFile(String filename) {
    super(filename);
    
    output = new ByteArrayOutputStream();
    writer = getStreamWriter();
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return output;
  }
  
  public void writeMagicNumber() {
    writer.u4(0xCAFEBABE);
  }
  
  public void writeVersion(int major, int minor) {
    writer.u2(minor);
    writer.u2(major);
  }
  
  public void writeConstantPool(ConstantPool pool) throws IOException {
    writer.u2(pool.length());
    pool.writeToStream(writer.getStream()); 
  }
  
  public void writeAccessFlags(AccessFlag... flags) {
    writer.u2(AccessFlag.mask(flags));
  }
  
  public void writeThisClass(int idx) {
    writer.u2(idx);
  }
  
  public void writeSuperClass(int idx) {
    writer.u2(idx);
  }
  
  public void writeInterfaces() {
    writer.u2(0);
  }
  
  public void writeFields() {
    writer.u2(0);
  }
  
  public void writeMethods(MethodPool methods) throws IOException {
    writer.u2(methods.length());
    methods.writeToStream(writer.getStream());
  }
  
  public void writeAttributes(ConstantPool constants) {
    writer.u2(1);
    
    // Source File Attribute
    writer.u2(constants.getSourceAttributeIndex());
    writer.u4(2);
    writer.u2(constants.getSourceIndex());
  }
  
  public void saveToFile() throws FileNotFoundException, IOException {
    this.writeToStream(new FileOutputStream(this));
  }
}
