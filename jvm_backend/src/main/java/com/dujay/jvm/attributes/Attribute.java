package com.dujay.jvm.attributes;

import com.dujay.jvm.constants.structures.Utf8Info;
import com.dujay.jvm.visitor.Element;

public abstract class Attribute implements Element {
  private Utf8Info attributeName;
  
  public Attribute(Utf8Info attributeName) {
    this.attributeName = attributeName;
  }

  public Utf8Info getAttributeName() {
    return attributeName;
  }
}
