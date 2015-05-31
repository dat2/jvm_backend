package com.dujay.jvm.attributes.builders;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ConstantInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.builders.MethodInfoBuilder;

public class CodeAttributeBuilder {
  
  private class PatchAddress  {
    public ConstantInfo constant;
    public int addr;
    public BiConsumer<Integer,Integer> func;
    
    public PatchAddress(ConstantInfo constant, int addr, BiConsumer<Integer,Integer> func) {
      this.constant = constant;
      this.addr = addr;
      this.func = func;
    }
    
    public void apply() {
      func.accept(constant.getIndex(), addr);
    }
  }
  
  private Logger logger = (Logger) LoggerFactory.getLogger("cabuilder");
  
  private MethodInfoBuilder mib;
  private ConstantPool cpr;
  private MethodInfo mi;
  private CodeAttribute ca;
  
  private List<PatchAddress> constantPatchAddresses;

  public CodeAttributeBuilder(MethodInfoBuilder methodInfoBuilder, MethodInfo mi, ConstantPool cpr, int maxStack, int maxLocals) {
    this.mib = methodInfoBuilder;
    this.cpr = cpr;
    this.mi = mi;
    this.ca = new CodeAttribute(cpr, maxStack, maxLocals);
    this.constantPatchAddresses = new ArrayList<PatchAddress>();
  }
  
  private void addPatchAddress(ConstantInfo c, int idx, BiConsumer<Integer,Integer> func) {
    this.constantPatchAddresses.add(new PatchAddress(c, idx, func));
  }
  
  public CodeAttributeBuilder ldc(String lit) {
    logger.debug("ldc");
    LiteralInfo info = cpr.getLiteral(lit).get();
    ca.u1(0x12);
    ca.u1(0);
    this.addPatchAddress(info, ca.currentIndex(), ca::u1);
    return this;
  }

  public CodeAttributeBuilder ldc2_w(String lit) {
    logger.debug("ldc2_w");
    LiteralInfo info = cpr.getLiteral(lit).get();
    ca.u1(0x14);
    ca.u2(0);
    this.addPatchAddress(info, ca.currentIndex() - 1, ca::u2);
    return this;
  }
  
  public CodeAttributeBuilder aload_0() {
    logger.debug("aload_0");
    ca.u1(0x2a);
    return this;
  }
  
  public CodeAttributeBuilder vreturn() {
    logger.debug("vreturn");
    ca.u1(0xb1);
    return this;
  }
  
  private CodeAttributeBuilder writeMemberIndex(String memberName) {
    logger.debug("writeMemberIndex");
    MemberRefInfo mr = cpr.getMemberRefInfo(memberName).get();
    ca.u2(0);
    this.addPatchAddress(mr, ca.currentIndex() - 1, ca::u2);
    
    return this;
  }
  
  public CodeAttributeBuilder getstatic(Field field) {
    return getstatic(ConstantPool.uniqueFieldName(field));
  }
  
  public CodeAttributeBuilder getstatic(String field) {
    logger.debug("getstatic");
    ca.u1(0xb2);
    return writeMemberIndex(field);
  }
  
  public CodeAttributeBuilder invokevirtual(Method method) {
    return invokevirtual(ConstantPool.uniqueMethodName(method));
  }

  public CodeAttributeBuilder invokevirtual(String memberName) {
    logger.debug("invokevirtual");
    ca.u1(0xb6);
    return writeMemberIndex(memberName);
  }
  
  public CodeAttributeBuilder invokespecial(Method method) {
    return invokespecial(ConstantPool.uniqueMethodName(method));
  }

  public CodeAttributeBuilder invokespecial(Constructor<?> c) {
    return invokespecial(ConstantPool.uniqueConstructorName(c));
  }
  
  public CodeAttributeBuilder invokespecial(String memberName) {
    logger.debug("invokespecial");
    ca.u1(0xb7);
    return writeMemberIndex(memberName);
  }
  
  public MethodInfoBuilder build() {
    return this
        .patch()
        .endCode();
  }
  
  public CodeAttributeBuilder patch() {
    logger.debug("Patching");
    this.constantPatchAddresses.stream()
      .forEach(x -> x.apply());
    logger.debug(ca.print());
    
    return this;
  }

  public MethodInfoBuilder endCode() {
    logger.debug("endCode");
    
    this.mi.add(this.ca);
    return mib;
  }

}
