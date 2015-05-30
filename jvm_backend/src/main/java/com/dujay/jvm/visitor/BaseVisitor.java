package com.dujay.jvm.visitor;

import java.util.Collection;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.constants.structures.literals.LongLiteralInfo;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.MethodPool;

/**
 * A simple base visitor class that lets users implement some methods, rather than all.
 * @author nick
 *
 * @param <T>
 */
public class BaseVisitor<T> implements Visitor<T> {

  @Override
  public T visit(Collection<? extends Element> ms) {
    return null;
  }

  @Override
  public T visit(ClassInfo c) {
    return null;
  }

  @Override
  public T visit(NameAndTypeInfo nt) {
    return null;
  }

  @Override
  public T visit(MemberRefInfo m) {
    return null;
  }

  @Override
  public T visit(Utf8Info u) {
    return null;
  }

  @Override
  public T visit(LiteralInfo i) {
    return null;
  }

  @Override
  public T visit(LongLiteralInfo i) {
    return null;
  }
  
  @Override
  public T visit(ConstantPool cp) {
    return null;
  }

  @Override
  public T visit(MethodInfo mi) {
    return null;
  }

  @Override
  public T visit(MethodPool mp) {
    return null;
  }

  @Override
  public T visit(CodeAttribute ca) {
    return null;
  }

}
