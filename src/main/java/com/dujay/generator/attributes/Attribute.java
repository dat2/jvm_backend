package com.dujay.generator.attributes;

import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.visitor.Element;

public abstract class Attribute implements Element {
  private Utf8Info attributeName;
  
  public Attribute(Utf8Info attributeName) {
    this.attributeName = attributeName;
  }

  public Utf8Info getAttributeName() {
    return attributeName;
  }
}
