package com.dujay.jvm.visitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dujay.jvm.attributes.CodeAttribute;
import com.dujay.jvm.bytes.ByteList;
import com.dujay.jvm.constants.ConstantPool;
import com.dujay.jvm.constants.structures.ClassInfo;
import com.dujay.jvm.constants.structures.MemberRefInfo;
import com.dujay.jvm.constants.structures.NameAndTypeInfo;
import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.constants.structures.literals.LiteralInfo;
import com.dujay.jvm.constants.structures.literals.LongLiteralInfo;
import com.dujay.jvm.methods.MethodInfo;
import com.dujay.jvm.methods.MethodPool;

public class ByteArrayVisitor implements Visitor<List<Byte>>, ByteList {
  
  private List<Byte> stream;
  
  public ByteArrayVisitor() {
    stream = new ArrayList<Byte>();
  }

  @Override
  public List<Byte> getBytes() {
    return stream;
  }

  @Override
  public List<Byte> visit(Collection<? extends Element> es) {
    for(Element e : es) {
      
      // This gets longs / doubles working properly
      if(e == null) {
        continue;
      }
      e.accept(this);
    }
    return getBytes();
  }
  
  @Override
  public List<Byte> visit(ClassInfo c) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(NameAndTypeInfo nt) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(MemberRefInfo m) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(Utf8Info u) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(LiteralInfo i) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(LongLiteralInfo i) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(ConstantPool cp) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(MethodInfo mi) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(MethodPool mp) {
    return getBytes();
  }

  @Override
  public List<Byte> visit(CodeAttribute ca) {
    return getBytes();
  }
}
