package com.dujay.jvm.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.dujay.jvm.bytes.ByteList;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.ConstantPoolBuilder;
import com.dujay.jvm.constants.enums.AccessFlag;
import com.dujay.jvm.methods.MethodPool;
import com.dujay.jvm.methods.builders.MethodPoolBuilder;

public class ClassFile extends File implements ByteList {
  private static final long serialVersionUID = 4178467766913681654L;
  
  private List<Byte> stream;
  private ConstantPool cpr;
  private MethodPool mp;
  private int accessFlags;
  
  public static final int JAVA8_MINOR = 0x0000;
  public static final int JAVA8_MAJOR = 0x0034;

  public ClassFile(String pathname, AccessFlag... flags) {
    super(pathname + ".class");
    stream = new ArrayList<Byte>();
    cpr = new ConstantPool();
    mp = new MethodPool();
    this.accessFlags = AccessFlag.mask(flags);
  }

  @Override
  public List<Byte> getBytes() {
    return stream;
  }
  
  public ConstantPool getConstantPool() {
    return cpr;
  }

  public MethodPool getMethodPool() {
    return mp;
  }

  public void setConstantPool(ConstantPool cpr) {
    this.cpr = cpr;
  }

  public void setMethodPool(MethodPool mp) {
    this.mp = mp;
  }
  
  public void writeMagicNumber() {
    this.u4(0xcafebabe);
  }
  
  public void writeVersion(int major, int minor) {
    this.u2(minor);
    this.u2(major);
  }
  
  public void writeConstantPool() throws IOException {
    this.append(cpr.generate());
  }
  
  public void writeAccessFlags() {
    this.u2(this.accessFlags);
  }
  
  public void writeClassIndices() {
    // The constant pool stores thisClass and superClass
    this.u2(cpr.getThisClass().getIndex());
    this.u2(cpr.getSuperClass().getIndex());
  }
  
  public void writeInterfaces() {
    // TODO Interface Structures
    this.u2(0);
  }
  
  public void writeFields() {
    // TODO Field Structures
    this.u2(0);
  }
  
  public void writeMethods() throws IOException {
    this.append(mp.generate());
  }
  
  public void writeAttributes() {
    // TODO Attribute Structures
    this.u2(0);
  }
  
  public void save() throws IOException {
    // write all the attributes to a byte stream
    logger.debug("headers");
    writeMagicNumber();
    writeVersion(ClassFile.JAVA8_MAJOR, ClassFile.JAVA8_MINOR);

    logger.debug("constant pool");
    writeConstantPool();

    logger.debug("class info");
    writeAccessFlags();
    writeClassIndices();

    logger.debug("interfaces");
    writeInterfaces();

    logger.debug("fields");
    writeFields();

    logger.debug("methods");
    writeMethods();

    logger.debug("attributes");
    writeAttributes();

    // write the stream out to the file
    IOException ex = null;
    FileOutputStream file = new FileOutputStream(this);
    try {
      this.writeTo(file);
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

  public ConstantPoolBuilder makeConstantPoolBuilder() {
    return new ConstantPoolBuilder(this);
  }

  public MethodPoolBuilder makeMethodPoolBuilder() {
    return new MethodPoolBuilder(this);
  }
}
