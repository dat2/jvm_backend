package com.dujay.generator.constants;

import java.util.HashMap;
import java.util.Map;

import com.dujay.generator.constants.structures.ClassInfo;
import com.dujay.generator.constants.structures.MemberRefInfo;
import com.dujay.generator.constants.structures.NameAndTypeInfo;
import com.dujay.generator.constants.structures.StringInfo;
import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.file.ClassFile;

public class ConstantPoolBuilder {
  private ClassFile owner;
  private ConstantPool cpr;
  
  private Map<String, NameAndTypeInfo> ntMap;
  private Map<String, ClassInfo> classMap;
  
  public ConstantPoolBuilder(ClassFile cls) {
    this.owner = cls;
    this.cpr = new ConstantPool();
    this.ntMap = new HashMap<String, NameAndTypeInfo>();
    this.classMap = new HashMap<String, ClassInfo>();
  }
  
  public ConstantPoolBuilder literal(String string) {
    cpr.add(new StringInfo(string));
    return this;
  }
  
  public ConstantPoolBuilder utf8(String utf8) {
    cpr.add(new Utf8Info(utf8));
    return this;
  }

  public ConstantPoolBuilder source(String source) {
    cpr.addSource(source);
    return this;
  }
  
  public ConstantPoolBuilder nameAndType(String ntName, String name, String type) {
    Utf8Info uName = new Utf8Info(name);
    Utf8Info uType = cpr.addDescriptor(name, type);
    NameAndTypeInfo nt = new NameAndTypeInfo(uName, uType);
    this.ntMap.put(ntName, nt);
    
    cpr.add(uName);
    cpr.add(uType);
    cpr.add(nt);
    
    return this;
  }

  private ClassInfo makeClass(String ciName, Class<?> clazz) {
    ClassInfo ci = new ClassInfo(clazz);
    this.classMap.put(ciName, ci);
    
    return ci;
  }
  
  private ClassInfo makeClass(String ciName, String clazz) {
    ClassInfo ci = new ClassInfo(clazz);
    this.classMap.put(ciName, ci);
    
    return ci;
  }
  
  public ConstantPoolBuilder cClass(String ciName, Class<?> clazz) {
    
    cpr.add(makeClass(ciName, clazz));
  
    return this;
  }
  
  public ConstantPoolBuilder cClass(String ciName, String clazz) {
    
    cpr.add(makeClass(ciName, clazz));
    
    return this;
  }

  public ConstantPoolBuilder thisClass(String clazz) {
    
    cpr.setThisClass(makeClass("this", clazz));
    
    return this;
  }


  public ConstantPoolBuilder thisClass(Class<?> clazz) {
    
    cpr.setThisClass(makeClass("this", clazz));
    
    return this;
  }
  

  public ConstantPoolBuilder superClass(String clazz) {
    
    cpr.setThisClass(makeClass("super", clazz));
    
    return this;
  }


  public ConstantPoolBuilder superClass(Class<?> clazz) {
    
    cpr.setSuperClass(makeClass("super", clazz));
    
    return this;
  }
  
  public ClassInfo getThisClass() {
    return this.classMap.get("this");
  }
  
  public ClassInfo getSuperClass() {
    return this.classMap.get("this");
  }
  
  private ConstantPoolBuilder memberRef(MemberRefInfo.MemberRefType type, String ciName, String ntName) {
    MemberRefInfo member = new MemberRefInfo(
        type, classMap.get(ciName), ntMap.get(ntName));
    
    cpr.add(member);
    
    return this;
  }

  public ConstantPoolBuilder method(String ciName, String ntName) {
    return memberRef(MemberRefInfo.MemberRefType.MethodRef, ciName, ntName);
  }

  public ConstantPoolBuilder field(String ciName, String ntName) {
    return memberRef(MemberRefInfo.MemberRefType.FieldRef, ciName, ntName);
  }

  public ConstantPoolBuilder index() {
    cpr.setIndices();
    return this;
  }

  public ConstantPool build() {
    this.owner.setConstantPool(this.cpr);
    
    return cpr;
  }
}
