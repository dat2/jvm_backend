package com.dujay.jvm.bytes;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ByteStreamWriter {
  
  final Logger logger = LoggerFactory.getLogger("bytestream");

  default public void writeInts(int... ints) {
    List<Integer> intsList = new ArrayList<Integer>();
    for (int i : ints) {
      intsList.add(i);
      getStream().write(i);
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

  default public void u2(int x) {
    writeInts(x >> 8, x);
  }

  default public void u4(int x) {
    writeInts(x >> 24, x >> 16, x >> 8, x);
  }
  
  public ByteArrayOutputStream getStream();
}
