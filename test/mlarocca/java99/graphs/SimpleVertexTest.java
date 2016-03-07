package mlarocca.java99.graphs;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleVertexTest {
  private static SimpleVertex<Integer> v;
  private static SimpleVertex<Integer> v1;
  private static SimpleVertex<Integer> u;
  
  private static SimpleVertex<Integer> uCopy;
  private static final String vLabel = "v";
  private static final String uLabel = "u";
  private static final Integer v1Value = 1;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    v = new SimpleVertex<>(vLabel);
    v1 = new SimpleVertex<>(vLabel, v1Value);
    u = new SimpleVertex<>(uLabel);
    uCopy = new SimpleVertex<>(uLabel);
  }

  @Test
  public void testGetLabel() {
   assertEquals(vLabel, v.getLabel());
   assertEquals(vLabel, v1.getLabel());
   assertEquals(uLabel, u.getLabel());
  }
  

  @Test
  public void testGetValue() {
   assertEquals(Optional.empty(), v.getValue());
   assertEquals(Optional.of(v1Value), v1.getValue());
  }
  
  @Test
  public void testEquality() {
    assertEquals(u, uCopy);
    assertEquals(v, v1);
    assertNotEquals(v, u);
  }

}
