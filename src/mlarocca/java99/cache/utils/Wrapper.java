package mlarocca.java99.cache.utils;

public class Wrapper<R> {
  private R value;    
  
  public Wrapper(R value) {
    setValue(value);
  }
  
  public R getValue() { 
    return value;
  }
  
  public void setValue(R value) {
    this.value = value;
  }
}