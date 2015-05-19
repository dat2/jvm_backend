package com.dujay.generator.redesign.visitor;

import com.dujay.generator.redesign.constants.ClassInfoR;
import com.dujay.generator.redesign.constants.ConstantPoolR;
import com.dujay.generator.redesign.constants.MemberRefInfo;
import com.dujay.generator.redesign.constants.NameAndTypeInfo;
import com.dujay.generator.redesign.constants.Utf8Info;

public interface Visitor<T> {
  
  // constant structures
  public T visit(ClassInfoR c);
  public T visit(MemberRefInfo m);
//  public T visit(StringInfo i);
//  public T visit(NumberInfo n);
//  public T visit(LongNumberInfo ln);
  public T visit(NameAndTypeInfo nt);
  public T visit(Utf8Info u);
//  public T visit(MethodHandleInfo mh);
//  public T visit(MethodTypeInfo mt);
//  public T visit(InvokeDynamicInfo id);
  public T visit(ConstantPoolR cp);

  default public void visit(Element element) {
    System.out.println("Unimplemented for type: " + element.getClass().getTypeName());
  }
}
