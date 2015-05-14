package com.dujay.generator.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClassFileGenerator implements IClassFileGenerator {

  private ArrayList<Byte> bytes;
  private File file = null;

  public SimpleClassFileGenerator() {
    bytes = new ArrayList<Byte>();
  }

  public void setFilename(String filename) {
    this.file = new File(filename);
  }

  public void writeToFile() throws IOException {
    if(file == null) {
      return;
    }
    
    List<String> hexBytes =
        bytes.stream()
          .map(x -> String.format("%01x", x))
          .collect(Collectors.toList());
    System.out.println(hexBytes);
    
    // TODO write to file
  }


  public void assembly(short opcode, byte... args) {
    bytes.add((byte)(opcode >> 8));
    if((byte)opcode != 0) {
      bytes.add((byte)opcode);
    }
    
    for( byte b : args ) {
      bytes.add(b);
    }
  }
}
