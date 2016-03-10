package mlarocca.java99.cache;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.junit.BeforeClass;
import org.junit.Test;

import mlarocca.java99.cache.CacheEntry;

public class CacheEntryTest {
  private static Map<String, Integer> p1 = new HashMap<>();  
  private static Map<String, Integer> p2 = new HashMap<>();
  private static Map<String, Double> p3 = new HashMap<>();
  private static Map<String, String> p4 = new HashMap<>();
  private static Integer tmpArray1[] = {1, 2 ,3}; 
  private static Integer tmpArray2[] = {1, 2 ,3}; 
      
  private static List<Integer> tmpList1 = new ArrayList<>();
  private static List<Integer> tmpList2 = new ArrayList<>();
    
  private static Map<String, Object> p5 = new HashMap<>();  
  private static Map<String, Object> p6 = new HashMap<>();

  private static Map<String, Object> p7 = new HashMap<>(p5);  
  private static Map<String, Object> p8 = new HashMap<>(p6);

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    p1.clear();
    p2.clear();
    p3.clear();
    p4.clear();
    tmpList1.clear(); 
    tmpList2.clear(); 
    
    p1.put("a", 1);
    p2.put("a", 1);
    p3.put("a", 1.0);
    p4.put("b", "1.0");      
    p4.put("c", "abc");    
    tmpList1.add(1); 
    tmpList1.add(2); 
    tmpList2.add(1); 
    tmpList2.add(2); 
    p5.put("list", tmpList1);
    p6.put("list", tmpList2);
    p7.put("array", tmpArray1);
    p8.put("array", tmpArray2); 
  }

  @Test
  public void testEquality() {
    //Same name, same (empty) list of parameters
    assertEquals(
        new CacheEntry<Integer>("test", new HashMap<String, String>(), 1),
        new CacheEntry<Integer>("test", new HashMap<String, String>(), 1));
    //The result value doesn't matter for equality
    assertEquals(
        new CacheEntry<Integer>("test", new HashMap<String, String>(), 1),
        new CacheEntry<Double>("test", new HashMap<String, String>(), 44.0));
    //Same name, same (non-empty) list of parameters
    assertEquals(
        new CacheEntry<Integer>("test", p1, 1),
        new CacheEntry<Integer>("test", p1, 1));

    assertEquals(
        new CacheEntry<String>("test", p1, "1"),
        new CacheEntry<String>("test", p2, "132"));

    assertEquals(
        new CacheEntry<String>("test", p1, "121"),
        new CacheEntry<String>("test", p2, "anbc"));

    assertEquals(
        new CacheEntry<String>("test", p5, "121"),
        new CacheEntry<String>("test", p6, "anbc"));

    //Type of the parameters matters
    assertNotEquals(
        new CacheEntry<String>("test", p1, "121"),
        new CacheEntry<String>("test", p3, "anbc"));
    //Different names
    assertNotEquals(
        new CacheEntry<String>("test1", p1, "121"),
        new CacheEntry<String>("test2", p1, "anbc"));
    //Different parameters lists
    assertNotEquals(
        new CacheEntry<String>("test", p3, "121"),
        new CacheEntry<String>("test", p4, "anbc"));

    assertNotEquals(
        new CacheEntry<String>("test", p6, "121"),
        new CacheEntry<String>("test", p4, "anbc"));

    //Arrays are compared by reference :/
    assertNotEquals(
        new CacheEntry<String>("test", p7, "121"),
        new CacheEntry<String>("test", p8, "anbc"));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testDefaultComparator() throws InterruptedException {
    CacheEntry e1;
    CacheEntry e2;
    
    //No access: The only thing that matters is which is created first 
    e1 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1);
    Thread.sleep(10);
    e2 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1);
    
    assertTrue(e1.compareTo(e2) < 0);
    assertFalse(e1.compareTo(e2) >= 0);
 
    //The result value doesn't matter for equality

    e1 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1);
    Thread.sleep(10);
    e2 = new CacheEntry<Double>("test", new HashMap<String, String>(), 44.0);
    
    assertTrue(e1.compareTo(e2) < 0);

    //Same name, same (non-empty) list of parameters
    e1 = new CacheEntry<Integer>("test", p1, 1);
    Thread.sleep(10);
    e2 = new CacheEntry<Integer>("test", p1, 1);

    assertFalse(e1.compareTo(e2) >= 0);

    //Different names don't matter
    e1 = new CacheEntry<String>("test1", p1, "121");
    Thread.sleep(10);
    e2 = new CacheEntry<String>("test2", p1, "anbc");

    assertTrue(e1.compareTo(e2) < 0);
  }

  @Test
  public void testDefaultComparatorAfterAccess() throws InterruptedException {
    CacheEntry<Integer> e1 = new CacheEntry<>("test", new HashMap<String, String>(), 1);
    Thread.sleep(10);
    CacheEntry<Double> e2 = new CacheEntry<Double>("test", new HashMap<String, String>(), 44.0);
    
    assertTrue(e1.compareTo(e2) < 0);
    Thread.sleep(10);    
    e1.getValue();
    assertTrue(e1.compareTo(e2) > 0);

    Thread.sleep(10);    
    e2.getValue();
    assertTrue(e1.compareTo(e2) < 0);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testCustomComparator() throws InterruptedException {
    BiFunction<CacheEntry<?>, CacheEntry<?>, Integer> nameComparator = 
        (entry1, entry2) -> {
          return entry1.getMethodName().compareTo(entry2.getMethodName());
        };
        
    CacheEntry e1;
    CacheEntry e2;
    
    //No access: The only thing that matters is which is created first 
    e1 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1, nameComparator);
    e2 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1, nameComparator);
    
    assertTrue(e1.compareTo(e2) == 0);

    e1 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1, nameComparator);
    e2 = new CacheEntry<Integer>("test", new HashMap<String, String>(), 1, nameComparator);

    assertTrue(e1.compareTo(e2) == 0);
 
    //Same name, same (non-empty) list of parameters
    e1 = new CacheEntry<Integer>("test", p1, 1, nameComparator);
    e2 = new CacheEntry<Integer>("test", p1, 1, nameComparator);

    assertTrue(e1.compareTo(e2) == 0);

    //Different names DOES matter
    e1 = new CacheEntry<String>("test1", p1, "121", nameComparator);
    e2 = new CacheEntry<String>("test2", p1, "anbc", nameComparator);

    assertTrue(e1.compareTo(e2) < 0);
    assertFalse(e1.compareTo(e2) >= 0);
  }  
}
