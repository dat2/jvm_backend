package com.dujay.generator.redesign.visitor;

import java.io.ByteArrayOutputStream;

import com.dujay.generator.redesign.constants.ClassInfoR;
import com.dujay.generator.redesign.constants.ConstantPoolR;
import com.dujay.generator.redesign.constants.MemberRefInfo;
import com.dujay.generator.redesign.constants.NameAndTypeInfo;
import com.dujay.generator.redesign.constants.StringInfo;
import com.dujay.generator.redesign.constants.Utf8Info;

public class ByteArrayVisitor implements Visitor<ByteArrayOutputStream> {
  
  private ByteArrayOutputStream bytes;
  
  public ByteArrayVisitor() {
    bytes = new ByteArrayOutputStream();
  }

  private void writeInts(int... ints) {
    for (int i : ints) {
      bytes.write(i);
    }
  }

  public void u1(int x) {
    writeInts(x);
  }

  public void u2(int x) {
    writeInts(x >> 8, x);
  }

  public void u4(int x) {
    writeInts(x >> 24, x >> 16, x >> 8, x);
  }
  
  public ByteArrayOutputStream getBytes() {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(ClassInfoR c) {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(NameAndTypeInfo nt) {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(MemberRefInfo m) {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(Utf8Info u) {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(StringInfo i) {
    return bytes;
  }

  @Override
  public ByteArrayOutputStream visit(ConstantPoolR cp) {
    return bytes;
  }
}
