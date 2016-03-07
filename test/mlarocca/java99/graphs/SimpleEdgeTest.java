package mlarocca.java99.graphs;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleEdgeTest {
  
  private static final String vLabel = "v";
  private static final String uLabel = "u";
  private static final Integer v1Value = 1;

  private static SimpleVertex<Integer> v;
  private static SimpleVertex<Integer> v1;
  private static SimpleVertex<Integer> u;
  
  private static SimpleEdge<Integer> eUV;
  private static SimpleEdge<Integer> eUVCopy;
  private static SimpleEdge<Integer> eUVWeighted;
  private static SimpleEdge<Integer> eVU;
  private static SimpleEdge<Integer> eVV1;
  private static SimpleEdge<Integer> eV1V;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    v = new SimpleVertex<>(vLabel);
    v1 = new SimpleVertex<>(vLabel, v1Value);
    u = new SimpleVertex<>(uLabel);
    
    eUV = new SimpleEdge<Integer>(u, v);
    eUVCopy = new SimpleEdge<Integer>(u, v);
    eUVWeighted = new SimpleEdge<Integer>(u, v, 1);
    eVU = new SimpleEdge<Integer>(v, u);
    eVV1 = new SimpleEdge<Integer>(v, v1);
    eV1V = new SimpleEdge<Integer>(v1, v);
  }

  @Test
  public void testGetSource() {
    assertEquals(u, eUV.getSource());
    assertEquals(u, eUVCopy.getSource());
  }

  @Test
  public void testGetDestination() {
    assertEquals(v, eV1V.getDestination());
    assertEquals(v1, eVV1.getDestination());
  }
  
  @Test
  public void testGetWeight() {
    assertEquals((Double)0.0, (Double)eUV.getWeight());
    assertEquals((Double)1.0, (Double)eUVWeighted.getWeight());
  }
  
  @Test
  public void testEquals() {
    assertEquals(eUV, eUVCopy);
    assertEquals(eV1V, eVV1);
    //Weight doesn't matter
    assertEquals(eUV, eUVWeighted);
    assertNotEquals(eUV, eVU);
    assertNotEquals(eUV, eVV1);
  }

}
