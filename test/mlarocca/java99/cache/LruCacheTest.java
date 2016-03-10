package mlarocca.java99.cache;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import mlarocca.java99.cache.LruCache;

public class LruCacheTest {
  private static Map<String, Integer> p0 = new HashMap<>();  
  private static Map<String, Integer> p1 = new HashMap<>();  
  private static Map<String, Object> p2 = new HashMap<>();
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    p1.put("a", 1);
    p2.put("a", 1);
    p2.put("test", 1.0);
    p2.put("b", "1.0");      
    p2.put("c", "abc");
  }

  @Test
  public void testCreation() {
    LruCache cache = new LruCache(10);
    assertEquals(cache.isFull(), false);
  }

  @Test
  public void testSet() {
    //Empty parameters list
    LruCache cache = new LruCache(1);
    assertEquals(cache.isFull(), false);
    String methodName = "test";
    Integer value = 1;
    cache.set(methodName, p0, value);
    assertEquals(cache.get(methodName, p0), value);
    //Non-Empty parameters list
    cache = new LruCache(1);
    assertEquals(cache.isFull(), false);
    cache.set(methodName, p1, value);
    assertEquals(cache.get(methodName, p1), value);
    //Return null on miss (different params)
    assertEquals(cache.get(methodName, p0), (Object)null);    
  }
  
  @Test
  public void testIsFull() {
    LruCache cache = new LruCache(1);
    assertEquals(cache.isFull(), false);
    String methodName = "test";
    Integer value = 2;
    cache.set(methodName, p2, value);
    assertEquals(cache.isFull(), true);
  }

  @Test
  public void testSetOnFullCache() {
    //Different name
    LruCache cache = new LruCache(1);
    assertEquals(cache.isFull(), false);
    String methodName1 = "test1";
    String methodName2 = "test2";
    Integer value1 = 1;
    Integer value2 = 2;
    Double value3 = 3.0;
    cache.set(methodName1, p1, value1);
    cache.set(methodName2, p1, value2);
    assertEquals(cache.get(methodName1, p1), (Object)null);
    assertEquals(cache.get(methodName2, p1), value2);
    //Different params (and return type)
    cache.set(methodName2, p2, value3);
    assertEquals(cache.get(methodName2, p2), value3);
  }  

  @Test
  public void testGetShouldChangeEntriesPriority() throws InterruptedException {
    //Empty parameters list
    LruCache cache = new LruCache(2);
    String methodName1 = "test";
    String methodName2 = "method2";
    String methodName3 = "method3";
    Integer value1 = 1;
    String value2 = "v2";
    cache.set(methodName1, p0, value1);
    cache.set(methodName2, p0, value1);
    assertEquals(cache.isFull(), true);
    cache.get(methodName1, p0);
    Thread.sleep(10); //Need this to have a gap between fetching times
    cache.set(methodName3, p2, value2);
    assertEquals(cache.isFull(), true);
    assertEquals(cache.get(methodName1, p0), value1);  
    assertEquals(cache.get(methodName2, p0), (Object)null);
    assertEquals(cache.get(methodName3, p2), value2);
  }
  
  
  @Test
  public void testSetWithMultipleItems() throws InterruptedException {
    //Empty parameters list
    LruCache cache = new LruCache(3);
    assertEquals(cache.isFull(), false);
    String methodName1 = "test";
    String methodName2 = "method2";
    Integer value1 = 1;
    String value2 = "v2";
    cache.set(methodName1, p0, value1);
    assertEquals(cache.get(methodName1, p0), value1);
    //Non-Empty parameters list
    assertEquals(cache.isFull(), false);
    cache.set(methodName1, p1, value1);
    assertEquals(cache.get(methodName1, p0), value1);  
    assertEquals(cache.get(methodName1, p1), value1);
    cache.set(methodName2, p2, value2);
    assertEquals(cache.isFull(), true);
    assertEquals(cache.get(methodName1, p0), value1);
    Thread.sleep(10); //Need this to have a gap between fetching times
    assertEquals(cache.get(methodName1, p1), value1);
    Thread.sleep(10);
    assertEquals(cache.get(methodName2, p2), value2);
    cache.set(methodName2, p0, value1);
    assertEquals(cache.isFull(), true);
    assertEquals(cache.get(methodName1, p0), (Object)null);  
    assertEquals(cache.get(methodName1, p1), value1);
    System.out.println(p0);
    System.out.println(p2);
    assertEquals(cache.get(methodName2, p0), value1);    
    assertEquals(cache.get(methodName2, p2), value2);    
  }
}
