package com.dujay.generator.constants.visitors;

import java.util.Collection;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

import com.dujay.generator.constants.ConstantPool;
import com.dujay.generator.constants.structures.ClassInfo;
import com.dujay.generator.constants.structures.ConstantInfo;
import com.dujay.generator.constants.structures.MemberRefInfo;
import com.dujay.generator.constants.structures.NameAndTypeInfo;
import com.dujay.generator.constants.structures.StringInfo;
import com.dujay.generator.constants.structures.Utf8Info;
import com.dujay.generator.visitor.BaseVisitor;
import com.dujay.generator.visitor.Element;

public class IndexVisitor extends BaseVisitor<Stream<? extends ConstantInfo>> {

  private static final Logger logger = (Logger) LoggerFactory.getLogger("indexer");
  private int index;

  private Stream<? extends ConstantInfo> unit(ConstantInfo c) {
    c.setIndex(index++);
    logger.debug("Set index of: " + c.toString());
    return Stream.of(c);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(Collection<? extends Element> ms) {
    return ms.stream().flatMap(c -> c.accept(this));
  }

  @Override
  public Stream<? extends ConstantInfo> visit(ClassInfo c) {
    return unit(c);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(NameAndTypeInfo nt) {
    return unit(nt);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(MemberRefInfo m) {
    return unit(m);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(Utf8Info u) {
    return unit(u);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(StringInfo i) {
    return unit(i);
  }

  @Override
  public Stream<? extends ConstantInfo> visit(ConstantPool cp) {

    // set indices of names before writing this , and super class
    ClassInfo thisClass = cp.getThisClass();
    Utf8Info thisClassName = thisClass.getName();
    ClassInfo superClass = cp.getSuperClass();
    Utf8Info superClassName = superClass.getName();

    thisClass.setIndex(1);
    thisClassName.setIndex(2);
    superClass.setIndex(3);
    superClassName.setIndex(4);
    index = 5;

    Stream<ConstantInfo> firstConstants = Stream.of(thisClass, thisClassName,
        superClass, superClassName);
    
    Stream.of(thisClass, thisClassName, superClass, superClassName)
      .forEach(x -> logger.debug("Set index of: " + x.toString()));
    
    return 
      Stream.concat(
          firstConstants,
          // concat all constant info lists, and then call accept on them all
          Stream.of(cp.getUtf8s(), cp.getStrings(), cp.getClasses(), cp.getNamesAndTypes(), cp.getMembers())
          .flatMap(Collection::stream)
          .flatMap(x -> x.accept(this))
      );
  }
}
