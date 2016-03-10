package mlarocca.java99.cache;

import java.util.Map;

public interface Cache {
  public <T> T get(String methodName, Map<String, ?> params);
  public <T> boolean set(String methodName, Map<String, ?> params, T value);
  public boolean isFull();
  public void clear();
}
