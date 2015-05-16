package com.dujay.generator.codegen;

import java.io.IOException;

// http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
public interface IClassFileGenerator {

  /* **********************************
   * Required methods *********************************
   */
  public void setFilename(String filename);

  public void writeToFile() throws IOException;

  public void writeBytes(int opcode, byte... args);

  public void writeBytes(int[] opcodes, byte... args);

  /* **********************************
   * Java Class File information *********************************
   */
  default public void u1(int x) {
    writeBytes((byte) x);
  }

  default public void u2(int x) {
    writeBytes(new int[] { x >> 8, x });
  }

  default public void u4(int x) {
    writeBytes(new int[] { x >> 24, x >> 16, x >> 8, x });
  }

  default public void magicNumber() {
    u4(0xcafebabe);
  }

  default public void java8version() {
    u2(0);
    u2(0x0034); // Java 8
  }

  /* **********************************
   * Constant Pool structures *********************************
   */
  default public void constClass(int idx) {
    u1(ConstantType.Class.tag());
    u2(idx);
  }

  default public void constString(int idx) {
    u1(ConstantType.String.tag());
    u2(idx);
  }

  default public void constMemberRef(int memberType, int classIdx,
      int nameTypeIdx) {
    u1(memberType);
    u2(classIdx);
    u2(nameTypeIdx);
  }

  default public void constFieldRef(int classIdx, int nameTypeIdx) {
    constMemberRef(ConstantType.Fieldref.tag(), classIdx, nameTypeIdx);
  }

  default public void constMethodRef(int classIdx, int nameTypeIdx) {
    constMemberRef(ConstantType.Methodref.tag(), classIdx, nameTypeIdx);
  }

  default public void constInterfaceMethodRef(int classIdx, int nameTypeIdx) {
    constMemberRef(ConstantType.InterfaceMethodref.tag(), classIdx, nameTypeIdx);
  }

  default public void constNumber(int numberType, int bytes) {
    u1(numberType);
    u4(bytes);
  }

  default public void constInt(int val) {
    constNumber(ConstantType.Integer.tag(), val);
  }

  default public void constFloat(float val) {
    constNumber(ConstantType.Integer.tag(), Float.floatToIntBits(val));
  }

  default public void constLongNumber(int numberType, long bytes) {
    u1(numberType);
    u4((int) (bytes >> 32));
    u4((int) (bytes));
  }

  default public void constLong(long val) {
    constLongNumber(ConstantType.Long.tag(), val);
  }

  default public void constDouble(double val) {
    constLongNumber(ConstantType.Double.tag(), Double.doubleToLongBits(val));
  }

  default public void constNameAndType(int nameIdx, int descriptorIdx) {
    u1(ConstantType.NameAndType.tag());
    u2(nameIdx);
    u2(descriptorIdx);
  }

  default public void constUtf8(String utf) {
    u1(ConstantType.Utf8.tag());
    u2(utf.length());
    for (byte b : utf.getBytes()) {
      u1(b);
    }
  }

  default public void constMethodHandle(int refKind, int refIdx) {
    u1(ConstantType.MethodHandle.tag());
    u1(refKind);
    u2(refIdx);
  }

  default public void constMethodType(int descriptorIdx) {
    u1(ConstantType.MethodType.tag());
    u2(descriptorIdx);
  }

  default public void constInvokeDynamic(int bootstrapMethodIdx,
      int nameAndTypeIdx) {
    u1(ConstantType.InvokeDynamic.tag());
    u2(bootstrapMethodIdx);
    u2(nameAndTypeIdx);
  }

  default public void accessFlags(AccessFlag... flags) {
    u2(AccessFlag.mask(flags));
  }

  default public void thisClass(int idx) {
    u2(idx);
  }

  default public void superClass(int idx) {
    u2(idx);
  }

  /* **********************************
   * JVM OpCodes ********************************
   */
  default public void nop() {
    writeBytes(0);
  }

  default public void iconst_0() {
    writeBytes(0x03);
  }

  default public void iconst_1() {
    writeBytes(0x04);
  }

  default public void istore_1() {
    writeBytes(0x3c);
  }

  default public void aload_0() {
    writeBytes(0x2a);
  }

  default public void vreturn() {
    writeBytes(0xb1);
  }

  default public void invokedynamic(byte... args) {
    writeBytes(0xba, args);
  }

  default public void invokespecial(byte... args) {
    writeBytes(0xb7, args);
  }

  default public void breakpoint() {
    writeBytes(0xca);
  }

  default public void impdep1() {
    writeBytes(0xfe);
  }

  default public void impdep2() {
    writeBytes(0xff);
  }

}
