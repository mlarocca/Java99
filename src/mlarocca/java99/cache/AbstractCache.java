package mlarocca.java99.cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiFunction;

public abstract class AbstractCache implements Cache {

  /**
   * The cache queue: head of the queue should be the next element to remove.
   */
  protected Queue<CacheEntry<?>> cache;
  /**
   * Stores references to the `CacheEntry`s grouped by name, to improve fetch time.
   * While a search on the priority queue would be linear, using this structure makes
   * it O(1) checking for an existing element.
   */
  private Map<String, Map<Integer, CacheEntry<?>>> cacheByMethod = new HashMap<>();
  /**
   * A function that compares two `CacheEntry`s. This will be provided in subclasses
   * as part of the Template pattern widely applied in this class' algorithms.
   */
  protected static final CacheComparator comparator = null;
  /**
   * Return the CacheEntry-comparator set for the actual subclasses.
   * This method is called in the algorithm templates below.
   * 
   * @return The comparator.
   */
  protected abstract BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> getComparator();
  /**
   * Return the maximum number of elements that can be stored in the cache 
   * (i.e. in the priority queue).
   * This method is called in the algorithm templates below.
   * 
   * @return The max cache size.
   */
  protected abstract Integer maxCacheSize();
  
  protected <T> CacheEntry<T> createCacheEntry(String methodName, Map<String, ?> params) {
    return new CacheEntry<T>(methodName, params, null, getComparator());
  }

  @Override
  public boolean isFull() {
    return cache.size() == maxCacheSize();
  }
  
  @Override
  public <T> T get(String methodName, Map<String, ?> params) {
    T result = null;
    CacheEntry<T> entry = getEntry(methodName, params);
    if (entry != null) {
      result = entry.getValue();
      updateEntryPriority(entry);
    }
    return result;
  }
  
  @Override
  public <T> boolean set(String methodName, Map<String, ?> params, T value) {
    CacheEntry<T> entry = preventDuplicates(methodName, params, value);
    
    if (entry == null) {
      entry = new CacheEntry<T>(methodName, params, value, getComparator());
    }
    
    addEntryToCache(entry);
    return isFull();
  }

  /**
   * Refresh the priority of an element.
   * INVARIANT: entry != null and cache.contains(entry)
   * 
   * Unfortunately we have no choice other than removing and inserting the entry back
   * into the queue to have the priorities updated. This will make running time O(n).
   * 
   * @param entry The entry whose priority needs to be updated.
   */
  private void updateEntryPriority(CacheEntry<?> entry) {
    cache.remove(entry);
    cache.add(entry);
  }
  
  private <T> CacheEntry<T> preventDuplicates(String methodName, Map<String, ?> params, T value) {
    CacheEntry<T> entry = getEntry(methodName, params);
    if (entry != null) {
      if (entry.getValue(true) != value) {
        //We update the entry to store the most recent value
        entry.updateValue(value);
      }
      //Unfortunately we have no choice other than removing and inserting the entry back
      //into the queue to have the priorities updated
      cache.remove(entry);
      cacheByMethod.get(methodName).remove(entry);
    }
    return entry;
  }

  private <T> boolean removeOldestEntry() {
    boolean removed = false;
    if (isFull()) {
      //Cache is full, must delete the oldest element
      CacheEntry<?> oldest = cache.remove();
      cacheByMethod.get(oldest.getMethodName()).remove(oldest.hashCode());
      removed = true;
    }
    return removed;
  }
  
  private <T> void addEntryToCache(CacheEntry<T> newEntry) {
    String methodName = newEntry.getMethodName();
    removeOldestEntry();
    
    cache.add(newEntry);
    if (!cacheByMethod.containsKey(methodName)) {
      cacheByMethod.put(methodName, new HashMap<>());
    }
    cacheByMethod.get(methodName).put(newEntry.hashCode(), newEntry);
  }
  
  
  @SuppressWarnings("unchecked")
  private <T> CacheEntry<T> getEntry(String methodName, Map<String, ?> params) {
    CacheEntry<T> result = null;
    if (cacheByMethod.containsKey(methodName)) {
      CacheEntry<Object> tmp = createCacheEntry(methodName, params);
      Map<Integer, CacheEntry<?>> entries = cacheByMethod.get(methodName);
      result = (CacheEntry<T>) entries.get(tmp.hashCode());
    }
    return result;
  }
    
  private class CacheComparator implements Comparator<CacheEntry<?>> {
    @Override
    public int compare(CacheEntry<?> x, CacheEntry<?> y) {
      return getComparator().apply(x, y);
    }
  }
  
  /**
   * Invalidate the cache, removing all the entries stored.
   */
  @Override
  public void clear() {
    cache.clear();
    cacheByMethod.clear();
  }
}
