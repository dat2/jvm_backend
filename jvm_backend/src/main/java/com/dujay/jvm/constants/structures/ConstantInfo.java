package com.dujay.jvm.constants.structures;

import com.dujay.jvm.visitor.Element;

public abstract class ConstantInfo implements Element {
  public abstract int tag();
  
  private int index;
  
  public int getIndex() {
    return index;
  }
  public void setIndex(int index){
    this.index = index;
  }
}
