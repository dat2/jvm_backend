package com.dujay.generator.redesign.constants;

import com.dujay.generator.redesign.visitor.Element;

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
