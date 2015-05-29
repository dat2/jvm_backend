package com.dujay.jvm.visitor;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.bytes.ByteStreamWriter;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.StringInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.MethodPool;

public class ByteArrayVisitor implements Visitor<ByteArrayOutputStream>, ByteStreamWriter {
  
  private ByteArrayOutputStream stream;
  
  public ByteArrayVisitor() {
    stream = new ByteArrayOutputStream();
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return stream;
  }

  @Override
  public ByteArrayOutputStream visit(Collection<? extends Element> es) {
    for(Element e : es) {
      e.accept(this);
    }
    return getStream();
  }
  
  @Override
  public ByteArrayOutputStream visit(ClassInfo c) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(NameAndTypeInfo nt) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(MemberRefInfo m) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(Utf8Info u) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(StringInfo i) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(ConstantPool cp) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(MethodInfo mi) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(MethodPool mp) {
    return getStream();
  }

  @Override
  public ByteArrayOutputStream visit(CodeAttribute ca) {
    return getStream();
  }
}
