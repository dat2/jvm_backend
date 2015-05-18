package com.dujay.generator.model.constants;

import java.util.Arrays;
import java.util.List;

public class MethodDescriptor extends MemberDescriptor {
  private List<Class<?>> args;
  private Class<?> returnType;
  
  public MethodDescriptor(ClassDescriptor ownerClass, String name, Class<?> returnType, Class<?>... args) {
    super(ownerClass, new Descriptor(name, ""));
    this.args = Arrays.asList(args);
    this.returnType = returnType;
  }
  
  @Override
  public String getTypeString() {
    StringBuilder rtn = new StringBuilder("");
    rtn.append("(");
    for(Class<?> c : args) {
      rtn.append(Descriptor.getTypeString(c));
    }
    if(args.size() > 0) {
      rtn.append(";");
    }
    rtn.append(")");
    rtn.append(Descriptor.getTypeString(returnType));
    
    return rtn.toString();
  }
  
}
