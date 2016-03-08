package mlarocca.java99.graphs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mlarocca.java99.graphs.data.MinDistanceResult;

public class SimpleGraphTest {
  private static final String vLabel = "v";
  private static final String wLabel = "w";
  private static final String uLabel = "u";
  private static final String zLabel = "z";
  private static final Integer wValue = 1;

  private static SimpleVertex<Integer> v;
  private static SimpleVertex<Integer> w;
  private static SimpleVertex<Integer> u;
  private static SimpleVertex<Integer> z;
  
  private static SimpleVertex<String> a;
  private static SimpleVertex<String> b;
  private static SimpleVertex<String> c;
  private static SimpleVertex<String> d;
  private static SimpleVertex<String> e;
  private static SimpleVertex<String> f;
  private static SimpleVertex<String> g;
  private static SimpleVertex<String> h;
  private static SimpleVertex<String> i;
  
  private static SimpleEdge<Integer> eUV;
  private static SimpleEdge<Integer> eUVWeighted;
  private static SimpleEdge<Integer> eVU;
  private static SimpleEdge<Integer> eVW;
  private static SimpleEdge<Integer> eWZ;
  private static SimpleEdge<Integer> eZU;

  private static SimpleEdge<String> eAB;
  private static SimpleEdge<String> eAC;
  private static SimpleEdge<String> eCA;
  private static SimpleEdge<String> eCB;
  private static SimpleEdge<String> eCD;
  private static SimpleEdge<String> eDD;
  private static SimpleEdge<String> eFC;
  private static SimpleEdge<String> eCH;
  private static SimpleEdge<String> eDE;
  private static SimpleEdge<String> eDF;
  private static SimpleEdge<String> eIE;
  private static SimpleEdge<String> eFG;
  private static SimpleEdge<String> eGI;
  private static SimpleEdge<String> eHI;

  private static Graph<Integer> graph;
  private static Graph<Integer> pathGraph;
  private static Graph<Integer> cycleGraph;
  private static Graph<String> connectedGraph1;
  private static Graph<String> connectedGraph2;
  private static Graph<Integer> connectedGraph3;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    v = new SimpleVertex<>(vLabel);
    w = new SimpleVertex<>(wLabel, wValue);
    u = new SimpleVertex<>(uLabel);
    z = new SimpleVertex<>(zLabel);

    a = new SimpleVertex<>("a", "1");
    b = new SimpleVertex<>("b");    
    c = new SimpleVertex<>("c", "3");
    d = new SimpleVertex<>("d");
    e = new SimpleVertex<>("e");
    f = new SimpleVertex<>("f");
    g = new SimpleVertex<>("g");
    h = new SimpleVertex<>("h");
    i = new SimpleVertex<>("i");

    eUV = new SimpleEdge<Integer>(u, v);
    eUVWeighted = new SimpleEdge<Integer>(u, v, 1);
    eVU = new SimpleEdge<Integer>(v, u);
    eVW = new SimpleEdge<Integer>(v, w);
    eWZ = new SimpleEdge<Integer>(w, z);
    eZU = new SimpleEdge<Integer>(z, u);

    eAB = new SimpleEdge<String>(a, b);
    eAC = new SimpleEdge<String>(a, c);
    eCA = new SimpleEdge<String>(c, a);
    eCB = new SimpleEdge<String>(c, b);
    eCD = new SimpleEdge<String>(c, d);
    eDD = new SimpleEdge<String>(d, d);
    eFC = new SimpleEdge<String>(f, c);
    eCH = new SimpleEdge<String>(c, h);
    eDE = new SimpleEdge<String>(d, e);
    eDF = new SimpleEdge<String>(d, f);
    eIE = new SimpleEdge<String>(i, e);
    eFG = new SimpleEdge<String>(f, g);
    eGI = new SimpleEdge<String>(g, i);
    eHI = new SimpleEdge<String>(h, i);
        
    pathGraph = new SimpleGraph<>();
    pathGraph.addVertex(vLabel);
    pathGraph.addVertex(wLabel);
    pathGraph.addVertex(uLabel);
    pathGraph.addEdge(eUV);
    pathGraph.addEdge(eVW);
    
    cycleGraph = new SimpleGraph<>();
    cycleGraph.addVertex(vLabel);
    cycleGraph.addVertex(uLabel);
    cycleGraph.addVertex(wLabel);
    cycleGraph.addVertex(zLabel);
    
    cycleGraph.addEdge(eUV);
    cycleGraph.addEdge(eVW);    
    cycleGraph.addEdge(eWZ);    
    cycleGraph.addEdge(eZU);    

    connectedGraph1 = new SimpleGraph<>();
    connectedGraph1.addVertex(a.getLabel());
    connectedGraph1.addVertex(b.getLabel());
    connectedGraph1.addVertex(c.getLabel());
    connectedGraph1.addVertex(d.getLabel());
    connectedGraph1.addVertex(e.getLabel());
    connectedGraph1.addVertex(f.getLabel());
    connectedGraph1.addVertex(g.getLabel());
    connectedGraph1.addVertex(h.getLabel());
    connectedGraph1.addVertex(i.getLabel());
    
    connectedGraph1.addEdge(eCA);
    connectedGraph1.addEdge(eCB);    
    connectedGraph1.addEdge(eCD);    
    connectedGraph1.addEdge(eFC);    
    connectedGraph1.addEdge(eCH);    
    connectedGraph1.addEdge(eDE);    
    connectedGraph1.addEdge(eDF);    
    connectedGraph1.addEdge(eIE);    
    connectedGraph1.addEdge(eFG);    
    connectedGraph1.addEdge(eGI);    
    connectedGraph1.addEdge(eHI);
    
    connectedGraph2 = new SimpleGraph<>();
    connectedGraph2.addVertex(a.getLabel());
    connectedGraph2.addVertex(b.getLabel());
    connectedGraph2.addVertex(c.getLabel());
    connectedGraph2.addVertex(d.getLabel());
    
    connectedGraph2.addEdge(eAB);
    connectedGraph2.addEdge(eCB);    
    connectedGraph2.addEdge(eAC);    
    connectedGraph2.addEdge(eDD);

    connectedGraph3 = new SimpleGraph<>();
    connectedGraph3.addVertex(vLabel);
    connectedGraph3.addVertex(uLabel);
    connectedGraph3.addVertex(wLabel);
    connectedGraph3.addVertex(zLabel);
    
    connectedGraph3.addEdge(eUV);
    connectedGraph3.addEdge(eVU);    
    connectedGraph3.addEdge(eVW);    
  }
  
  @Before
  public void setUpBefore() throws Exception {    
    graph = new SimpleGraph<>();
  }
  
  @Test
  public void testCreation() {
    assertEquals(new ArrayList<Edge<Integer>>(), graph.getEdges());
    assertEquals(new ArrayList<Vertex<Integer>>(), graph.getVertices());
  }

  @Test
  public void testAddAndGetVertex() {
    assertEquals(Arrays.asList(), graph.getVertices());
    graph.addVertex(vLabel);
    assertEquals(Optional.of(v), graph.getVertex(vLabel));
    assertEquals(Arrays.asList(v), graph.getVertices());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddExistingVertex() {
    graph.addVertex(vLabel);
    graph.addVertex(vLabel);
  }  
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddEdgeWithInvalidSource() {
    graph.addEdge(eUV);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAddEdgeWithInvalidDestination() {
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
  }  
  
  @Test(expected = NullPointerException.class)
  public void testAddNullEdge() {
    graph.addEdge(null);
  }  
  
  @Test
  public void testAddAndGetEdge() {
    assertEquals(Arrays.asList(), graph.getEdges());
    graph.addVertex(vLabel);
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
    graph.addEdge(eVU);

    assertEquals(Arrays.asList(eUV, eVU), graph.getEdges());
  }
  
  @Test
  public void testAddExistingEdge() {
    assertEquals(Arrays.asList(), graph.getEdges());
    assertNotEquals((Double)eUVWeighted.getWeight(), (Double)eUV.getWeight());
    
    graph.addVertex(vLabel);
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
    graph.addEdge(eUVWeighted);
    assertEquals((Double)eUVWeighted.getWeight(), (Double)graph.getEdges().get(0).getWeight());
  }  

  @Test
  public void testGetAdjList() {
    assertEquals(Arrays.asList(), graph.getEdges());
    graph.addVertex(vLabel);
    graph.addVertex(wLabel);
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
    graph.addEdge(eVU);
    graph.addEdge(eVW);

    assertEquals(Arrays.asList(eVU, eVW), graph.getEdgesFrom(v));
  }

  @Test
  public void testGetNeighbours() {
    assertEquals(Arrays.asList(), graph.getEdges());
    graph.addVertex(vLabel);
    graph.addVertex(wLabel);
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
    graph.addEdge(eVU);
    graph.addEdge(eVW);

    assertEquals(Arrays.asList(u, w), graph.getNeighbours(v));
  }
  
  @Test
  public void testGetEdgesTo() {
    assertEquals(Arrays.asList(), graph.getEdges());
    graph.addVertex(vLabel);
    graph.addVertex(wLabel);
    graph.addVertex(uLabel);
    graph.addEdge(eUV);
    graph.addEdge(eVU);
    graph.addEdge(eVW);

    assertEquals(Arrays.asList(eVU), graph.getEdgesTo(u));
  }
  
  @Test
  public void testDfs() {
    Map<Vertex<String>, Integer> exitTimes = connectedGraph1.dfs();
    assertEquals((Integer)1, exitTimes.get(a));
    assertEquals((Integer)3, exitTimes.get(b));
    assertEquals((Integer)5, exitTimes.get(e));
    assertEquals((Integer)6, exitTimes.get(i));
    assertEquals((Integer)7, exitTimes.get(g));
    assertEquals((Integer)8, exitTimes.get(f));
    assertEquals((Integer)9, exitTimes.get(d));
    assertEquals((Integer)10, exitTimes.get(h));
    assertEquals((Integer)11, exitTimes.get(c));
  }

  @Test
  public void testDfsFromVertex() {
    Map<Vertex<String>, Integer> exitTimes = connectedGraph1.dfs(c);
    assertEquals((Integer)1, exitTimes.get(a));
    assertEquals((Integer)2, exitTimes.get(b));
    assertEquals((Integer)3, exitTimes.get(e));
    assertEquals((Integer)4, exitTimes.get(i));
    assertEquals((Integer)5, exitTimes.get(g));
    assertEquals((Integer)6, exitTimes.get(f));
    assertEquals((Integer)7, exitTimes.get(d));
    assertEquals((Integer)8, exitTimes.get(h));
    assertEquals((Integer)9, exitTimes.get(c));
    
    exitTimes = connectedGraph1.dfs(a);
    assertEquals((Integer)1, exitTimes.get(a));
    assertEquals(null, exitTimes.get(b));
    assertEquals(null, exitTimes.get(e));
    assertEquals(null, exitTimes.get(i));
    assertEquals(null, exitTimes.get(g));
    assertEquals(null, exitTimes.get(f));
    assertEquals(null, exitTimes.get(d));
    assertEquals(null, exitTimes.get(h));
    assertEquals(null, exitTimes.get(c));    
  }
  
  @Test
  public void testDfsFromVertexCycle() {
    Map<Vertex<Integer>, Integer> exitTimes = cycleGraph.dfs(u);
    assertEquals((Integer)1, exitTimes.get(z));
    assertEquals((Integer)2, exitTimes.get(w));
    assertEquals((Integer)3, exitTimes.get(v));
    assertEquals((Integer)4, exitTimes.get(u));
    
    exitTimes = cycleGraph.dfs(v);
    assertEquals((Integer)1, exitTimes.get(u));
    assertEquals((Integer)2, exitTimes.get(z));
    assertEquals((Integer)3, exitTimes.get(w));
    assertEquals((Integer)4, exitTimes.get(v));   
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDfsFromInvalidVertex() {
    cycleGraph.dfs(new SimpleVertex<>("banana"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testDfsFromNullVertex() {
    cycleGraph.dfs(null);
  }
  
  @Test
  public void testDfsFromVertexToTarget() {
    List<Vertex<String>> path = connectedGraph1.dfs(c, e);
    assertEquals(Arrays.asList(c, d, e), path);  
  }
  
  @Test
  public void testDfsFromVertexToTargetCycle() {
    List<Vertex<Integer>> path = cycleGraph.dfs(u, z);
    assertEquals(Arrays.asList(u, v, w, z), path);  

    path = cycleGraph.dfs(v, u);
    assertEquals(Arrays.asList(v, w, z, u), path);  
 
    path = cycleGraph.dfs(w, u);
    assertEquals(Arrays.asList(w, z, u), path);  
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testDfsFromInvalidVertexToTarget() {
    cycleGraph.dfs(new SimpleVertex<>("banana"), v);
  }
  
  @Test(expected = NullPointerException.class)
  public void testDfsFromNullVertexToTarget() {
    cycleGraph.dfs(null, v);
  } 
  
  @Test(expected = IllegalArgumentException.class)
  public void testDfsFromVertexToInvalidTarget() {
    cycleGraph.dfs(u, new SimpleVertex<>("banana"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testDfsFromVertexToNullTarget() {
    cycleGraph.dfs(u, null);
  }
  

  @Test
  public void testBfsFromVertex() {
    MinDistanceResult<String> result = connectedGraph1.bfs(c);
    
    assertEquals((Double)0.0, result.distances().get(c));  
    assertEquals((Double)1.0, result.distances().get(a));  
    assertEquals((Double)1.0, result.distances().get(b));  
    assertEquals((Double)1.0, result.distances().get(d));  
    assertEquals((Double)1.0, result.distances().get(h));  
    assertEquals((Double)2.0, result.distances().get(e));  
    assertEquals((Double)2.0, result.distances().get(f));
    assertEquals((Double)2.0, result.distances().get(i));
    
    assertEquals(null, result.predecessors().get(c));  
    assertEquals(c, result.predecessors().get(a));  
    assertEquals(c, result.predecessors().get(b));  
    assertEquals(c, result.predecessors().get(d));  
    assertEquals(c, result.predecessors().get(h));  
    assertEquals(d, result.predecessors().get(e));  
    assertEquals(d, result.predecessors().get(f));
    assertEquals(h, result.predecessors().get(i));
    
    result = connectedGraph1.bfs(a);
    assertEquals((Double)0.0, result.distances().get(a));  
    assertEquals(null, result.distances().get(b));  
    assertEquals(null, result.distances().get(d));  
    assertEquals(null, result.distances().get(h));  
    assertEquals(null, result.distances().get(e));  
    assertEquals(null, result.distances().get(f));
    assertEquals(null, result.distances().get(i));
    
    assertEquals(null, result.predecessors().get(c));  
    assertEquals(null, result.predecessors().get(a));  
    assertEquals(null, result.predecessors().get(b));  
    assertEquals(null, result.predecessors().get(d));  
    assertEquals(null, result.predecessors().get(h));  
    assertEquals(null, result.predecessors().get(e));  
    assertEquals(null, result.predecessors().get(f));
    assertEquals(null, result.predecessors().get(i));
  }
  
  @Test
  public void testBfsFromVertexCycle() {
    MinDistanceResult<Integer> result = cycleGraph.bfs(u);
    
    assertEquals((Double)1.0, result.distances().get(v));  
    assertEquals((Double)2.0, result.distances().get(w));  
    assertEquals((Double)3.0, result.distances().get(z));
    
    assertEquals(null, result.predecessors().get(u));  
    assertEquals(u, result.predecessors().get(v));  
    assertEquals(v, result.predecessors().get(w));  
    assertEquals(w, result.predecessors().get(z));  
    
    assertEquals(null, result.path());  

    result = cycleGraph.bfs(v);
    
    assertEquals((Double)1.0, result.distances().get(w));  
    assertEquals((Double)2.0, result.distances().get(z));  
    assertEquals((Double)3.0, result.distances().get(u));
    
    assertEquals(null, result.predecessors().get(v));  
    assertEquals(v, result.predecessors().get(w));  
    assertEquals(w, result.predecessors().get(z));  
    assertEquals(z, result.predecessors().get(u));
    
    assertEquals(null, result.path());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBfsFromInvalidVertex() {
    cycleGraph.bfs(new SimpleVertex<>("banana"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testBfsFromNullVertex() {
    cycleGraph.bfs(null);
  }
  
  @Test
  public void testBfsFromVertexToTarget() {
    MinDistanceResult<String> result = connectedGraph1.bfs(c, e);
    assertEquals((Double)2.0, result.distances().get(e));  
    assertEquals(Arrays.asList(c, d, e), result.path());  
  }
  
  @Test
  public void testBfsFromVertexToTargetCycle() {
    MinDistanceResult<Integer> result = cycleGraph.bfs(u, z);

    assertEquals((Double)1.0, result.distances().get(v));  
    assertEquals((Double)2.0, result.distances().get(w));  
    assertEquals((Double)3.0, result.distances().get(z));
    
    assertEquals(null, result.predecessors().get(u));  
    assertEquals(u, result.predecessors().get(v));  
    assertEquals(v, result.predecessors().get(w));  
    assertEquals(w, result.predecessors().get(z));  
    
    assertEquals(Arrays.asList(u, v, w, z), result.path());  

    result = cycleGraph.bfs(v, z);
    
    assertEquals((Double)1.0, result.distances().get(w));  
    assertEquals((Double)2.0, result.distances().get(z));  
    
    assertEquals(null, result.predecessors().get(v));  
    assertEquals(v, result.predecessors().get(w));  
    assertEquals(w, result.predecessors().get(z));  
    
    assertEquals(Arrays.asList(v, w, z), result.path());  
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testBfsFromInvalidVertexToTarget() {
    cycleGraph.bfs(new SimpleVertex<>("banana"), v);
  }
  
  @Test(expected = NullPointerException.class)
  public void testBfsFromNullVertexToTarget() {
    cycleGraph.bfs(null, v);
  } 
  
  @Test(expected = IllegalArgumentException.class)
  public void testBfsFromVertexToInvalidTarget() {
    cycleGraph.bfs(u, new SimpleVertex<>("banana"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testBfsFromVertexToNullTarget() {
    cycleGraph.bfs(u, null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAllAcyclicPathsFromInvalidVertex() {
    cycleGraph.allAcyclicPaths(new SimpleVertex<>("banana"), v);
  }
  
  @Test(expected = NullPointerException.class)
  public void testAllAcyclicPathsFromNullVertex() {
    cycleGraph.allAcyclicPaths(null, v);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAllAcyclicPathsToInvalidVertex() {
    cycleGraph.allAcyclicPaths(v, new SimpleVertex<>("banana"));
  }
  
  @Test(expected = NullPointerException.class)
  public void testAllAcyclicPathsToNullVertex() {
    cycleGraph.allAcyclicPaths(v, null);
  }
  
  @Test
  public void testAllAcyclicPaths() {
    Set<List<Vertex<String>>> expectedResult = new HashSet<>();
    expectedResult.add(Arrays.asList(a, b));
    expectedResult.add(Arrays.asList(a, c, b));
    assertEquals(expectedResult, connectedGraph2.allAcyclicPaths(a, b));  

    expectedResult.clear();
    expectedResult.add(Arrays.asList(a, c));
    assertEquals(expectedResult, connectedGraph2.allAcyclicPaths(a, c));  

    Set<List<Vertex<String>>> expectedResultEmpty = new HashSet<>();
    assertEquals(expectedResultEmpty, connectedGraph2.allAcyclicPaths(a, d));  
    assertEquals(expectedResultEmpty, connectedGraph2.allAcyclicPaths(b, a));  
  }
  
  @Test
  public void testAllAcyclicPathsOnACycle() {
    Set<List<Vertex<Integer>>> expectedResult = new HashSet<>();
    expectedResult.add(Arrays.asList(u, v, w));
    assertEquals(expectedResult, cycleGraph.allAcyclicPaths(u, w));  

    expectedResult.clear();
    expectedResult.add(Arrays.asList(w, z, u));
    assertEquals(expectedResult, cycleGraph.allAcyclicPaths(w, u));  
  }
  
  @Test
  public void testToString() {
    assertEquals("[u > v, v > w1, w > z, z > u]", cycleGraph.toString());
    assertEquals("[u - v, v > w1, z]", connectedGraph3.toString());
  }
}
