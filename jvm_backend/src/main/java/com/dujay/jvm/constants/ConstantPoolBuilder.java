package com.dujay.jvm.constants;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.constants.structures.literals.NumberInfo;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.constants.structures.literals.LongNumberInfo;
import com.dujay.jvm.constants.structures.literals.StringInfo;
import com.dujay.jvm.file.ClassFile;

public class ConstantPoolBuilder {
  
  private Logger logger = (Logger) LoggerFactory.getLogger("cpbuilder");
  
  private ClassFile owner;
  private ConstantPool cpr;
  
  public ConstantPoolBuilder(ClassFile cls) {
    this.owner = cls;
    this.cpr = new ConstantPool();
    this.owner.setConstantPool(this.cpr);
  }
  
  private ClassInfo makeClass(String ciName, Class<?> clazz) {
    ClassInfo ci = new ClassInfo(clazz);
    cpr.put(ciName, ci);
    logger.debug("Adding class: " + ci.toString());
    
    return ci;
  }
  
  private ClassInfo makeClass(String ciName, String clazz) {
    ClassInfo ci = new ClassInfo(clazz);
    cpr.put(ciName, ci);
    logger.debug("Adding class: " + ci.toString());
    
    return ci;
  }
  
  public ConstantPoolBuilder clazz(String ciName, Class<?> clazz) {
    cpr.add(makeClass(ciName, clazz));
    return this;
  }
  
  public ConstantPoolBuilder clazz(String ciName, String clazz) {
    cpr.add(makeClass(ciName, clazz));
    return this;
  }

  public ConstantPoolBuilder thisClass(String clazz) {
    cpr.setThisClass(new ClassInfo(clazz));
    return this;
  }


  public ConstantPoolBuilder thisClass(Class<?> clazz) {
    cpr.setThisClass(new ClassInfo(clazz));
    return this;
  }
  

  public ConstantPoolBuilder superClass(String clazz) {
    cpr.setSuperClass(new ClassInfo(clazz));
    return this;
  }


  public ConstantPoolBuilder superClass(Class<?> clazz) {
    cpr.setSuperClass(new ClassInfo(clazz));
    return this;
  }
  
  public ClassInfo getThisClass() {
    return cpr.getClassInfo("this").get();
  }
  
  public ClassInfo getSuperClass() {
    return cpr.getClassInfo("super").get();
  }
  
  private ConstantPoolBuilder literal(String key, LiteralInfo value, String type) {
    cpr.put(key, value);
    logger.debug("Adding " + type + " literal: " + value.toString());
    return this;
  }
  
  public ConstantPoolBuilder literal(String key, String value) {
    return literal(key, new StringInfo(value), "String");
  }
  
  public ConstantPoolBuilder literal(String key, int value) {
    return literal(key, new NumberInfo(value), "Integer");
  }

  public ConstantPoolBuilder literal(String key, float value) {
    return literal(key, new NumberInfo(value), "Float");
  }

  public ConstantPoolBuilder literal(String key, long value) {
    return literal(key, new LongNumberInfo(value), "Long");
  }
  
  public ConstantPoolBuilder literal(String key, double value) {
    return literal(key, new LongNumberInfo(value), "Double");
  }
  
  public ConstantPoolBuilder utf8(String utf8) {
    Utf8Info uUtf = new Utf8Info(utf8);
    cpr.add(uUtf);
    logger.debug("Adding Utf8: " + uUtf.toString());
    return this;
  }
  
  public ConstantPoolBuilder attribute(String aName) {
    this.utf8(aName);
    return this;
  }

  public ConstantPoolBuilder source(String source) {
    cpr.addSource(source);
    return this;
  }
  
  public ConstantPoolBuilder nameAndType(String ntName, String name, String type) {
    NameAndTypeInfo nt = new NameAndTypeInfo(new Utf8Info(name), cpr.addDescriptor(name, type));
    cpr.put(ntName, nt);

    logger.debug("Adding NameAndType: " + nt.toString());
    
    return this;
  }
  
  private ConstantPoolBuilder makeMemberRef(MemberRefInfo.MemberRefType type, String mName, String ciName, String ntName) {
    MemberRefInfo member = new MemberRefInfo(
        type, cpr.getClassInfo(ciName).get(), cpr.getNameAndType(ntName).get());
    logger.debug("Adding Member: " + member.toString());
    
    cpr.put(mName, member);
    
    return this;
  }

  public ConstantPoolBuilder method(String mName, String ciName, String ntName) {
    logger.debug("Adding Method: " + mName);
    return makeMemberRef(MemberRefInfo.MemberRefType.MethodRef, mName, ciName, ntName);
  }

  public ConstantPoolBuilder field(String mName, String ciName, String ntName) {
    logger.debug("Adding Field: " + mName);
    return makeMemberRef(MemberRefInfo.MemberRefType.FieldRef, mName, ciName, ntName);
  }

  public ConstantPoolBuilder index() {
    cpr.setIndices();
    return this;
  }

  public ConstantPool build() {
    return cpr;
  }

}
