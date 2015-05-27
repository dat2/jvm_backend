package com.dujay.generator.methods.builders;

import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.file.ClassFile;
import com.dujay.generator.methods.MethodPool;

public class MethodPoolBuilder {

  private ClassFile cls;
  private ConstantPool cpr;
  private MethodPool mp;
  
  public MethodPoolBuilder(ClassFile cls) {
    this.cls = cls;
    this.mp = new MethodPool();
    this.cpr = this.cls.getConstantPool();
  }
  
  public MethodInfoBuilder beginMethod() {
    return new MethodInfoBuilder(this, mp, cpr);
  }
  
  public MethodPool build() {
    cls.setMethodPool(this.mp);
    
    return mp;
  }
}
