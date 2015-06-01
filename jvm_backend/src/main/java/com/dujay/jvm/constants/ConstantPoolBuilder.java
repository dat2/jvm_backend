package com.dujay.jvm.constants;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

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
    Optional<ClassInfo> oci = cpr.getClassInfo(ciName);
    if(!oci.isPresent()) {
      ClassInfo ci = new ClassInfo(clazz);
      cpr.put(ciName, ci);
      logger.debug("Adding class: " + ci.toString());
      return ci;
    } else {
      return oci.get();
    }
  }

  public ConstantPoolBuilder clazz(Class<?> clazz) {
    // TODO check if class is anonymous
    cpr.add(makeClass(ConstantPool.uniqueClassName(clazz), clazz));
    return this;
  }

  public ConstantPoolBuilder clazz(String ciName, String clazz) {
    cpr.add(makeClass(ciName + ".class", clazz));
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
    logger.debug("Adding " + type + " literal (" + key + "): "
        + value.toString());
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

  public ConstantPoolBuilder literal(String key, boolean b) {
    return null;
  }

  private ConstantPoolBuilder utf8(String utf8) {
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

  private ConstantPoolBuilder nameAndType(String ntName, String name,
      String type) {
    NameAndTypeInfo nt = new NameAndTypeInfo(new Utf8Info(name),
        cpr.addDescriptor(name, type));
    cpr.put(ntName, nt);

    logger.debug("Adding NameAndType (" + ntName + "): " + nt.toString());

    return this;
  }

  private ConstantPoolBuilder nameAndType(String ntName, String name,
      Class<?> rType, Class<?>... paramTypes) {
    return nameAndType(ntName, name,
        Descriptor.methodDescriptor(rType, paramTypes));
  }

  private ConstantPoolBuilder nameAndType(Field f) {
    return nameAndType(ConstantPool.uniqueNTName(f), f.getName(),
        Descriptor.fieldDescriptor(f));
  }

  private ConstantPoolBuilder nameAndType(Method m) {
    return nameAndType(ConstantPool.uniqueNTName(m), m.getName(),
        Descriptor.methodDescriptor(m.getReturnType(), m.getParameterTypes()));
  }

  private ConstantPoolBuilder makeMemberRef(MemberRefInfo.MemberRefType type,
      String mName, String ciName, String ntName) {
    Optional<ClassInfo> ci = cpr.getClassInfo(ciName);
    Optional<NameAndTypeInfo> nti = cpr.getNameAndType(ntName);

    MemberRefInfo member = new MemberRefInfo(type, ci.get(), nti.get());
    logger.debug("Adding " + type + " (" + mName + "): " + member.toString());

    cpr.put(mName, member);

    return this;
  }

  private ConstantPoolBuilder method(String mName, String ciName, String ntName) {
    return makeMemberRef(MemberRefInfo.MemberRefType.MethodRef, mName, ciName,
        ntName);
  }

  public ConstantPoolBuilder method(Method m) {
    this.nameAndType(m);
    return method(ConstantPool.uniqueMethodName(m),
        ConstantPool.uniqueClassName(m.getDeclaringClass()),
        ConstantPool.uniqueNTName(m));
  }
  
  public ConstantPoolBuilder method(String ciName, String methodName, Class<?> rType, Class<?>... pTypes) {
    String mName = ConstantPool.uniqueMethodName(methodName, rType, pTypes);
    String ntName = ConstantPool.uniqueNTName(ciName, methodName);
    this.nameAndType(ntName, methodName, rType, pTypes);
    
    return method(mName,ciName,ntName);
  }

  public ConstantPoolBuilder method(Class<?> c, String methodName, Class<?> rType, Class<?>... pTypes) {
    return method(ConstantPool.uniqueClassName(c),methodName,rType,pTypes);
  }

  public ConstantPoolBuilder constructor(String ciName,
      Constructor<?> constructor) {
    String mName = ConstantPool.uniqueConstructorName(constructor);
    String ntName = ConstantPool.uniqueNTName(constructor);

    if (!this.cpr.getNameAndType(ntName).isPresent()) {
      this.nameAndType(ntName, "<init>", Void.class,
          constructor.getParameterTypes());
    }

    logger.debug("Adding Constructor: " + mName);

    return makeMemberRef(MemberRefInfo.MemberRefType.MethodRef, mName, ciName,
        ntName);
  }

  public ConstantPoolBuilder constructor(Constructor<?> constructor) {
    return this.constructor(
        ConstantPool.uniqueClassName(constructor.getDeclaringClass()),
        constructor);
  }

  public ConstantPoolBuilder constructor(String ciName, Class<?>... paramTypes) {
    String mName = ciName + ".<init>";
    String ntName = ConstantPool.uniqueConstructorNTName(paramTypes);

    if (!this.cpr.getNameAndType(ntName).isPresent()) {
      this.nameAndType(ntName, "<init>", Void.class, paramTypes);
    }

    logger.debug("Adding Constructor: " + mName);

    return makeMemberRef(MemberRefInfo.MemberRefType.MethodRef, mName, ciName,
        ntName);
  }

  public ConstantPoolBuilder field(String fName, String ciName, String ntName) {
    return makeMemberRef(MemberRefInfo.MemberRefType.FieldRef, fName, ciName,
        ntName);
  }

  public ConstantPoolBuilder field(Field f) {
    String fName = ConstantPool.uniqueFieldName(f);
    String ciName = ConstantPool.uniqueClassName(f.getDeclaringClass());
    String ntName = ConstantPool.uniqueNTName(f);

    this.nameAndType(f);
    return field(fName, ciName, ntName);
  }

  public ConstantPoolBuilder index() {
    cpr.setIndices();
    return this;
  }

  public ConstantPool build() {
    return cpr;
  }
}
