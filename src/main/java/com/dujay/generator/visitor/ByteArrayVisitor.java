package com.dujay.generator.visitor;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.dujay.generator.attributes.CodeAttribute;
import com.dujay.generator.bytes.ByteStreamWriter;
import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.structures.ClassInfo;
import com.dujay.generator.constants.structures.MemberRefInfo;
import com.dujay.generator.constants.structures.NameAndTypeInfo;
import com.dujay.generator.constants.structures.StringInfo;
import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.methods.MethodInfo;
import com.dujay.generator.methods.MethodPool;

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
  public ByteArrayOutputStream visit(List<? extends Element> es) {
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
