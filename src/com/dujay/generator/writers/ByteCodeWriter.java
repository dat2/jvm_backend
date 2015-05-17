package com.dujay.generator.writers;


public class ByteCodeWriter {

  private ByteStreamWriter writer;

  public ByteCodeWriter(ByteStreamWriter w) {
    writer = w;
  }

  public void nop() {
    writer.u1(0);
  }

  public void ldc(int constantIndex) {
    writer.u1(0x12);
    writer.u1(constantIndex);
  }

  public void iconst_0() {
    writer.u1(0x03);
  }

  public void iconst_1() {
    writer.u1(0x04);
  }

  public void istore_1() {
    writer.u1(0x3c);
  }

  public void aload_0() {
    writer.u1(0x2a);
  }

  public void vreturn() {
    writer.u1(0xb1);
  }

  public void getstatic(int memberRefIndex) {
    writer.u1(0xb2);
    writer.u2(memberRefIndex);
  }

  public void invokedynamic(byte... args) {
    writer.u1(0xba);
    for(byte b : args) {
      writer.u1(b);
    }
  }

  public void invokevirtual(int index, byte... args) {
    writer.u1(0xb6);
    writer.u2(index);
    for(byte b : args) {
      writer.u1(b);
    }
  }
  
  public void invokespecial(int index) {
    writer.u1(0xb7);
    writer.u2(index);
  }

  public void breakpoint() {
    writer.u1(0xca);
  }

  public void impdep1() {
    writer.u1(0xfe);
  }

  public void impdep2() {
    writer.u1(0xff);
  }
}
