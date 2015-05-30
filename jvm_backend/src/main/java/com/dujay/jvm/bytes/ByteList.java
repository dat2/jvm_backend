package com.dujay.jvm.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ByteList {
  
  final Logger logger = LoggerFactory.getLogger("bytelist");

  default public void writeInts(int... ints) {
    List<Integer> intsList = new ArrayList<Integer>();
    for (int i : ints) {
      intsList.add(i);
      getBytes().add(new Integer(i).byteValue());
    }
    
    // print out nicely each byte of the integer being written
    logger.debug(
        intsList
          .stream()
          .map(x -> x.intValue() & 0xff)
          .map(Integer::toHexString)
          .map(x -> String.format("%2s", x).replace(' ', '0'))
          .collect(Collectors.joining(" "))
    );
  }

  default public void u1(int x) {
    writeInts(x);
  }
  
  default public void u1(int x, int patchAddr) {
    this.patch(patchAddr, x);
  }

  default public void u2(int x) {
    writeInts(x >> 8, x);
  }
  
  default public void u2(int x, int patchAddr) {
    this.patch(patchAddr, x >> 8);
    this.patch(patchAddr + 1, x);
  }

  default public void u4(int x) {
    writeInts(x >> 24, x >> 16, x >> 8, x);
  }
  
  default public void u4(int x, int patchAddr) {
    this.patch(patchAddr, x >> 24);
    this.patch(patchAddr + 1, x >> 16);
    this.patch(patchAddr + 2, x >> 8);
    this.patch(patchAddr + 3, x);
  }
  
  default public void patch(int index, int value) {
    getBytes().set(index, new Integer(value).byteValue());
  }
  
  default public void append(List<Byte> list) {
    getBytes().addAll(list);
  }
  
  default public void append(ByteList list) {
    this.append(list.getBytes());
  }
  
  default public void writeTo(OutputStream o) throws IOException {
    List<Byte> bytes = getBytes();
    byte[] arr = new byte[bytes.size()];
    for(int i = 0; i < bytes.size(); i++) {
      arr[i] = bytes.get(i);
    }
    o.write(arr);
  }
  
  default public String print() {
    List<String> str = getBytes().stream()
        .map(x -> x.intValue() & 0xff)
        .map(Integer::toHexString)
        .map(x -> String.format("%2s", x).replace(' ', '0'))
        .collect(Collectors.toList());
    
    return str.toString();
  }
  
  public List<Byte> getBytes();
}
