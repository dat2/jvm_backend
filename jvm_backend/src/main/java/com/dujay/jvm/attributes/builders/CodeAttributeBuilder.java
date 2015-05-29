package com.dujay.jvm.attributes.builders;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.LiteralInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.builders.MethodInfoBuilder;

public class CodeAttributeBuilder {
  
  private MethodInfoBuilder mib;
  private ConstantPool cpr;
  private MethodInfo mi;
  private CodeAttribute ca;

  public CodeAttributeBuilder(MethodInfoBuilder methodInfoBuilder, MethodInfo mi, ConstantPool cpr, int maxStack, int maxLocals) {
    this.mib = methodInfoBuilder;
    this.cpr = cpr;
    this.mi = mi;
    this.ca = new CodeAttribute(cpr, maxStack, maxLocals);
  }
  
  public CodeAttributeBuilder ldc(String lit) {
    LiteralInfo info = cpr.getLiteral(lit).get();
    ca.u1(0x12);
    ca.u1(info.getIndex());
    return this;
  }
  
  public CodeAttributeBuilder aload_0() {
    ca.u1(0x2a);
    return this;
  }
  
  public CodeAttributeBuilder vreturn() {
    ca.u1(0xb1);
    return this;
  }
  
  private CodeAttributeBuilder writeMemberIndex(String memberName) {
    MemberRefInfo mr = cpr.getMemberRefInfo(memberName).get();
    ca.u2(mr.getIndex());
    
    return this;
  }
  
  public CodeAttributeBuilder getstatic(String memberName) {
    ca.u1(0xb2);
    return writeMemberIndex(memberName);
  }

  public CodeAttributeBuilder invokevirtual(String memberName) {
    ca.u1(0xb6);
    return writeMemberIndex(memberName);
  }
  
  public CodeAttributeBuilder invokespecial(String memberName) {
    ca.u1(0xb7);
    return writeMemberIndex(memberName);
  }

  public MethodInfoBuilder endCode() {
    this.mi.add(this.ca);
    return mib;
  }

}
