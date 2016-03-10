package mlarocca.java99.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Class CacheEntry models a cache entry storing the result of a call to a method.
 * CacheEntry-s can be compared to each other, and by default they are ordered
 * by the least recently used.
 * 
 * @param <T>
 */
public class CacheEntry<T> implements Comparable<CacheEntry<?>>{
  private String methodName;
  private Map<String, ?> params;
  private T value;
  @SuppressWarnings("unused")
  private Date createdAt;
  private Date lastFetched;
  private int fetchedNbr;

  public String getMethodName() {
    return methodName;
  }

  public Map<String, ?> getParams() {
    return params;
  }

  public T getValue() {
    return getValue(false);
  }
  
  /**
   * Overloaded version, for internal use only: it will allow us to retrieve the
   * value and update the priority of the item.
   * 
   * @param skipPriorityUpdate
   * @return
   */
  protected T getValue(boolean skipPriorityUpdate) {
    if (!skipPriorityUpdate) {
      lastFetched = new Date();
      fetchedNbr += 1;
    }
    return value;
  }
  
  protected void updateValue(T newValue) {
    createdAt = lastFetched = new Date();
    value = newValue;
  }
  
  private BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> comparator;
  protected static final BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> DefaultComparator =
      (cache1, cache2) -> { 
        return cache1.lastFetched.compareTo(cache2.lastFetched);
      };
  
  /**
   * Overloaded Constructor. Uses the default value for the comparator, which is a 
   * comparator that sort entries based on the last time they were fetched.
   * 
   * @param _methodName The name of the method whose result is cached by this entry.
   * @param _params The parameters with which the method was called.
   * @param _value The value returned by the method call.
   */
  public CacheEntry(String _methodName, Map<String, ?> _params, T _value) {
    this(_methodName, _params, _value, DefaultComparator);
  }

  /**
   * Constructor.
   * 
   * @param _methodName The name of the method whose result is cached by this entry.
   * @param _params The parameters with which the method was called.
   * @param _value The value returned by the method call.
   * @param _comparator A function that compares two CacheEntry-s.
   */
  public CacheEntry(
      String _methodName,
      Map<String, ?> _params,
      T _value,
      BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> _comparator) {
    methodName = _methodName;
    params = new HashMap<>(_params);  //shallow copy
    value = _value;                   //shallow copy
    comparator = _comparator;
    if (comparator == null) {
      throw new IllegalArgumentException("Comparator can't be null - omit it to use default");
    }
    createdAt = lastFetched = new Date();
  }  
  
  /**
   * Compare this Object to any other object.
   */
  @Override
  public boolean equals(Object other) {
    Boolean eq = other != null && other.getClass() == this.getClass();
    @SuppressWarnings("unchecked")
    CacheEntry<T> otherEntry = (CacheEntry<T>) other;
    if (eq) {
      Map<String, ?> otherParams = otherEntry.params;
      return methodName.equals(otherEntry.getMethodName()) &&
          params.keySet()
            .stream()
            .allMatch(key -> {
              return otherParams.containsKey(key) &&
                  otherParams.get(key).equals(params.get(key));
            });
    }
    return eq;
  }
  
  /**
   * Override compareTo using Strategy pattern: the comparator will be
   * composed-in into to the class.
   */
  @Override
  public int compareTo(CacheEntry<?> other) {
    return comparator.apply(this, other);
  }
  
  /**
   * We need to override this method in order to be able to store and retrieve
   * CacheEntry-s to/from Maps and Sets.
   */
  @Override
  public int hashCode() {
    Map<String, Integer> hash = new HashMap<>(1);
    hash.put(getMethodName(), getParams().hashCode());
    return hash.hashCode();        
  }
}
