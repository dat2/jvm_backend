package com.dujay.jvm.methods.builders;

import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.file.ClassFile;
import com.dujay.jvm.methods.MethodPool;

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
