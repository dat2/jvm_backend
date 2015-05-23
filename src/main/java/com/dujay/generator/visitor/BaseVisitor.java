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

/**
 * A simple base visitor class that lets users implement some methods, rather than all.
 * @author nick
 *
 * @param <T>
 */
public class BaseVisitor<T> implements Visitor<T> {

  @Override
  public T visit(ClassInfo c) {
    return null;
  }

  @Override
  public T visit(List<? extends Element> ms) {
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
  public T visit(StringInfo i) {
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
