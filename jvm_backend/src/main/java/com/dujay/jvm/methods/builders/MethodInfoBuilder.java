package com.dujay.jvm.methods.builders;

import com.dujay.jvm.attributes.builders.CodeAttributeBuilder;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.enums.AccessFlag;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.MethodPool;

public class MethodInfoBuilder {
  
  private MethodPoolBuilder mpb;
  private MethodPool mp;
  private ConstantPool cpr;
  private MethodInfo mi;
  private CodeAttributeBuilder cab;
  
  public MethodInfoBuilder(MethodPoolBuilder mpb, MethodPool mp, ConstantPool cpr) {
    this.mpb = mpb;
    this.mp = mp;
    this.cpr = cpr;
  }
  
  public MethodInfoBuilder define(String ntName, int maxStack, int maxLocals, AccessFlag... flags) {
    NameAndTypeInfo nt = cpr.getNameAndType(ntName).get();
    mi = new MethodInfo(nt.getName(), nt.getType(), flags);
    cab = new CodeAttributeBuilder(this, mi, cpr, maxStack, maxLocals);
    
    return this;
  }
  
  public CodeAttributeBuilder beginCode() {
    return cab;
  }

  public MethodPoolBuilder endMethod() {
    this.mp.add(this.mi);
    
    return this.mpb;
  }
}
