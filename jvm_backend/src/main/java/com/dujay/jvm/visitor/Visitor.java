package com.dujay.jvm.visitor;

import java.util.Collection;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.StringInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.MethodPool;

public interface Visitor<T> {

  public T visit(Collection<? extends Element> ms);
  
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
