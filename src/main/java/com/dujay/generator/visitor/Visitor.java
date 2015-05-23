package com.dujay.generator.visitor;

import java.util.List;

import com.dujay.generator.attributes.CodeAttribute;
import com.dujay.generator.constants.ClassInfo;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.MemberRefInfo;
import com.dujay.generator.constants.NameAndTypeInfo;
import com.dujay.generator.constants.StringInfo;
import com.dujay.generator.constants.Utf8Info;
import com.dujay.generator.methods.MethodInfo;
import com.dujay.generator.methods.MethodPool;

public interface Visitor<T> {

  public T visit(List<? extends Element> ms);
  
  // constant structures
  public T visit(ClassInfo c);
  public T visit(MemberRefInfo m);
  public T visit(StringInfo i);
//  public T visit(NumberInfo n);
//  public T visit(LongNumberInfo ln);
  public T visit(NameAndTypeInfo nt);
  public T visit(Utf8Info u);
//  public T visit(MethodHandleInfo mh);
//  public T visit(MethodTypeInfo mt);
//  public T visit(InvokeDynamicInfo id);
  public T visit(ConstantPool cp);
  
  // method structures
  public T visit(MethodInfo mi);
  public T visit(MethodPool mp);
  
  // attribute structures
  public T visit(CodeAttribute ca);

  default public T visit(Element element) {
    throw new UnsupportedOperationException("Unimplemented for type: " + element.getClass().getTypeName());
  }
}
