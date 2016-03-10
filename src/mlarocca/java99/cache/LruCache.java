package mlarocca.java99.cache;

import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class LruCache extends AbstractCache {

  private int _maxCacheSize;
  
  /**
   * Constructor.
   * 
   * @param _maxCacheSize How many elements the cache can hold at the same time.
   */
  public LruCache(Integer maxCacheSize) {
    if (maxCacheSize <= 0) {
      throw new IllegalArgumentException("Cache size MUST be a positive integer");
    }
    _maxCacheSize = maxCacheSize;
    cache = new PriorityQueue<>(maxCacheSize(), comparator);
  }
  
  @Override
  protected BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> getComparator() {
    return CacheEntry.DefaultComparator;
  }

  @Override
  protected Integer maxCacheSize() {
    return _maxCacheSize;
  }
}
