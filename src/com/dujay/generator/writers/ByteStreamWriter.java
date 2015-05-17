package com.dujay.generator.writers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ByteStreamWriter implements WriteableByteStream {
  
  private ByteArrayOutputStream bytes;
  
  public ByteStreamWriter(ByteArrayOutputStream out) {
    this.bytes = out;
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

  public void appendStream(WriteableByteStream writeStream)
      throws IOException {
      writeStream.writeToStream(bytes);
  }

  @Override
  public ByteArrayOutputStream getStream() {
    return bytes;
  }
}
