package com.dujay.generator.codegen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SimpleClassFileGenerator implements IClassFileGenerator {

  private ByteArrayOutputStream bytes;
  private File file = null;

  public SimpleClassFileGenerator() {
    bytes = new ByteArrayOutputStream();
  }

  public void setFilename(String filename) {
    this.file = new File(filename);
  }

  public void writeToFile() throws IOException {
    if(file == null) {
      return;
    }
    
    bytes.writeTo(new FileOutputStream(file));
  }

  @Override
  public void writeBytes(int opcode, byte... args) {
    bytes.write(opcode);
    
    try {
      bytes.write(args);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeBytes(int[] opcodes, byte... args) {
    for(int i : opcodes) {
      bytes.write(i);
    }

    try {
      bytes.write(args);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
