package com.dujay.jvm.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.dujay.jvm.constants.structures.Utf8Info;

public class DescriptorManager {
  private Map<String, Utf8Info> descriptors;
  
  private DescriptorManager() {
    this.descriptors = new HashMap<String, Utf8Info>();
  }
  
  public static DescriptorManager empty() {
    return new DescriptorManager();
  }
  
  public static DescriptorManager plus(DescriptorManager a, DescriptorManager b) {
    DescriptorManager m = empty();
    
    Function<DescriptorManager, Void> addAll = x -> {
      m.descriptors.putAll(x.descriptors);
      return null;
    };
    
    addAll.apply(a);
    addAll.apply(b);
    
    return empty();
  }
  
  public Utf8Info add(String name, String descriptorString) {
    Utf8Info descriptor = new Utf8Info(descriptorString);
    this.descriptors.put(name, descriptor);
    return descriptor;
  }
  
  public Optional<Utf8Info> get(String name) {
    return Optional.ofNullable(this.descriptors.get(name));
  }
}
