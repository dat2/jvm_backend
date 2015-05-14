package com.dujay.generator.model;

import java.io.IOException;

// http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings
public interface IClassFileGenerator {

  /* **********************************
   * Required methods
   * **********************************/
  public void setFilename(String filename);

  public void writeToFile() throws IOException;

  public void assembly(short opcode, byte... args);

  /* **********************************
   * Java Class File information
   * **********************************/
  default public void magicNumber() {
    assembly((short) 0xcafe);
    assembly((short) 0xbabe);
  }
  
  default public void java8version() {
    assembly((short) 0);
    assembly((short) 0);
    assembly((short) 0x0034);
  }
  
  /* **********************************
   * Java OpCodes
   * *********************************/
  default public void nop() {
    assembly((short) 0);
  }

  default public void invokedynamic(byte... args) {
    assembly((short) 0xba00, args);
  }

  default public void breakpoint() {
    assembly((short) 0xca00);
  }

  default public void impdep1() {
    assembly((short) 0xfe00);
  }

  default public void impdep2() {
    assembly((short) 0xff00);
  }
}
