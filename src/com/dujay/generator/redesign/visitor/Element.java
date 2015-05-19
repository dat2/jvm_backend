package com.dujay.generator.redesign.visitor;

/**
 * A visitable element class.
 * @author nick
 */
public interface Element {
  public <T> T accept(Visitor<T> visitor);
}
