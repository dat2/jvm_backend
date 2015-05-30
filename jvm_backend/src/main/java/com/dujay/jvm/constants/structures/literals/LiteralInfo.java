package com.dujay.jvm.constants.structures.literals;

import com.dujay.jvm.constants.structures.ConstantInfo;
import com.dujay.jvm.visitor.Visitor;

public abstract class LiteralInfo extends ConstantInfo {

  public abstract int bytes();
  
  public abstract int numBytes();

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visit(this);
  }
}
