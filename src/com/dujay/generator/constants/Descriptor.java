package com.dujay.generator.constants;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Descriptor {

  public static String fieldDescriptor(Class<?> c) {
    // void classes
    if(c == Void.class) {
      return "V";
    }
    // primitive classes
    if(c.isPrimitive()) {
      if(c == Long.class) {
        return "J";
      } else if(c == Boolean.class) {
        return "Z";
      }
      // even though the first character is uppercase, we want to make sure
      return "" + c.getSimpleName().toUpperCase().charAt(0);
    }
    // array classes
    if(c.isArray()) {
      return c.getName().replace('.', '/'); // its already formatted for us in the way we want
    }
    // general classes
    return "L" + c.getTypeName().replace('.', '/') + ";";
  }
  
  public static String methodDescriptor(Class<?> returnClass, Class<?>... parameters) {
    
    String parameterString = Arrays.asList(parameters).stream()
        .map(Descriptor::fieldDescriptor)
        .collect(Collectors.joining());
    
    return "(" + parameterString + ")" + Descriptor.fieldDescriptor(returnClass);
  }
  
}
