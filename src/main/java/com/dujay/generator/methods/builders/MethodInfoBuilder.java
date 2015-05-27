package com.dujay.generator.methods.builders;

import com.dujay.generator.attributes.builders.CodeAttributeBuilder;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.structures.NameAndTypeInfo;
import com.dujay.generator.enums.AccessFlag;
import com.dujay.generator.methods.MethodInfo;
import com.dujay.generator.methods.MethodPool;

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
