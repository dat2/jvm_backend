package com.dujay.generator.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dujay.generator.bytes.ByteStreamWriter;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.enums.AccessFlag;
import com.dujay.generator.methods.MethodPool;

public class ClassFile extends File implements ByteStreamWriter {
  private static final long serialVersionUID = 4178467766913681654L;
  
  private ByteArrayOutputStream stream;
  private ConstantPool cpr;
  private MethodPool mp;
  
  public static final int JAVA8_MINOR = 0x0000;
  public static final int JAVA8_MAJOR = 0x0034;

  public ClassFile(String pathname) {
    super(pathname + ".class");
    stream = new ByteArrayOutputStream();
    cpr = new ConstantPool();
    mp = new MethodPool();
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return stream;
  }
  
  public ConstantPool getConstantPool() {
    return cpr;
  }

  public MethodPool getMethodPool() {
    return mp;
  }
  
  public void writeMagicNumber() {
    this.u4(0xcafebabe);
  }
  
  public void writeVersion(int major, int minor) {
    this.u2(minor);
    this.u2(major);
  }
  
  public void writeConstantPool() throws IOException {
    ByteArrayOutputStream out = cpr.generate();
    out.writeTo(getStream());
  }
  
  public void writeAccessFlags(AccessFlag... flags) {
    this.u2(AccessFlag.mask(flags));
  }
  
  public void writeClassIndices() {
    this.u2(cpr.getThisClass().getIndex());
    this.u2(cpr.getSuperClass().getIndex());
  }
  
  public void writeInterfaces() {
    // TODO writeinterfaces
    this.u2(0);
  }
  
  public void writeFields() {
    // TODO writefields
    this.u2(0);
  }
  
  public void writeMethods() throws IOException {
    ByteArrayOutputStream out = mp.generate();
    out.writeTo(getStream());
  }
  
  public void writeAttributes() {
    // TODO writeattributes
    this.u2(0);
  }
  
  public void save() throws IOException {
    IOException ex = null;
    FileOutputStream file = new FileOutputStream(this);
    try {
      getStream().writeTo(file);
    } catch(IOException e) {
      ex = e;
    } finally {
      file.close();
      // re throw, after closing the file
      if(ex != null) {
        throw ex;
      }
    }
  }
}
