package com.dujay.generator.visitor;

/**
 * A visitable element class.
 * @author nick
 */
public interface Element {
  public <T> T accept(Visitor<T> visitor);
}
