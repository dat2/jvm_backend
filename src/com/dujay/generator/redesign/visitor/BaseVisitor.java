package com.dujay.generator.redesign.visitor;

import com.dujay.generator.constants.ClassInfoR;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.MemberRefInfo;
import com.dujay.generator.constants.NameAndTypeInfo;
import com.dujay.generator.constants.StringInfo;
import com.dujay.generator.constants.Utf8Info;

/**
 * A simple base visitor class that lets users implement some methods, rather than all.
 * @author nick
 *
 * @param <T>
 */
public class BaseVisitor<T> implements Visitor<T> {

  @Override
  public T visit(ClassInfoR c) {
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

}
