package com.dujay.generator.redesign.constants;

public abstract class ConstantInfo {
  public abstract int tag();
  
  private int index;
  
  public int getIndex() {
    return index;
  }
  public void setIndex(int index){
    this.index = index;
  }
}
