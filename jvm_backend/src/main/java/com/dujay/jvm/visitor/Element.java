package com.dujay.jvm.visitor;

/**
 * A visitable element class.
 * @author nick
 */
public interface Element {
  public <T> T accept(Visitor<T> visitor);
}
