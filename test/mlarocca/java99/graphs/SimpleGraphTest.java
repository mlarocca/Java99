package mlarocca.java99.graphs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mlarocca.java99.graphs.data.MinDistanceResult;

public class SimpleGraphTest {
  private static final double DEFAULT_EDGE_WEIGHT = 0.0;
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
  private static SimpleEdge<Integer> eVUWeighted;
  private static SimpleEdge<Integer> eVW;
  private static SimpleEdge<Integer> eVWWeighted;
  private static SimpleEdge<Integer> eWZ;
  private static SimpleEdge<Integer> eWZWeighted;
  private static SimpleEdge<Integer> eZU;
  private static SimpleEdge<Integer> eZUWeighted;
  private static SimpleEdge<Integer> eWVWeighted;
  private static SimpleEdge<Integer> eZWWeighted;
  private static SimpleEdge<Integer> eUZWeighted;


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
  private static SimpleEdge<String> eIC;

  private static Graph<Integer> graph;
  private static Graph<Integer> pathGraph;
  private static Graph<Integer> cycleGraph;
  private static Graph<String> connectedGraph1;
  private static Graph<String> disconnectedGraph4;
  private static Graph<Integer> disconnectedGraph5;
  private static Graph<Integer> connectedGraph4;
  private static Graph<Integer> disconnectedGraph1;
  private static Graph<Integer> disconnectedGraph2;
  private static Graph<Integer> disconnectedGraph3;

  private static Graph<Integer> weightedGraph1;
  private static Graph<Integer> weightedUndirectedGraph;
  
  private static Comparator<Graph<?>> GraphComparatorByWeight = new Comparator<Graph<?>>() {
    @Override
    public int compare(Graph<?> g1, Graph<?> g2) {        
      Double w1 = g1.getEdges()
        .stream()
        .map(Edge::getWeight)
        .reduce((e1, e2) -> e1 + e2)
        .get();
      Double w2 = g2.getEdges()
          .stream()
          .map(Edge::getWeight)
          .reduce((e1, e2) -> e1 + e2)
          .get();
      return w1.compareTo(w2);
    }
  };
  
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
    eVUWeighted = new SimpleEdge<Integer>(v, u, 1);
    eVW = new SimpleEdge<Integer>(v, w);
    eVWWeighted = new SimpleEdge<Integer>(v, w, 4);
    eWVWeighted = new SimpleEdge<Integer>(w, v, 4);
    eWZ = new SimpleEdge<Integer>(w, z);
    eWZWeighted = new SimpleEdge<Integer>(w, z, 2.5);
    eZWWeighted = new SimpleEdge<Integer>(z, w, 2.5);
    eZU = new SimpleEdge<Integer>(z, u);
    eZUWeighted = new SimpleEdge<Integer>(z, u, 2);
    eUZWeighted = new SimpleEdge<Integer>(u, z, 2);

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
    eIC = new SimpleEdge<String>(i, c);
        
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
    connectedGraph1.addEdge(eIC);    
    
    connectedGraph4 = new SimpleGraph<>();
    connectedGraph4.addVertex(uLabel);

    disconnectedGraph1 = new SimpleGraph<>();
    disconnectedGraph1.addVertex(vLabel);
    disconnectedGraph1.addVertex(uLabel);
    disconnectedGraph1.addVertex(wLabel);
    disconnectedGraph1.addVertex(zLabel);
    
    disconnectedGraph1.addEdge(eUV);
    disconnectedGraph1.addEdge(eVW);  
    
    disconnectedGraph2 = new SimpleGraph<>();
    disconnectedGraph2.addVertex(vLabel);
    disconnectedGraph2.addVertex(uLabel);
  
    disconnectedGraph3 = new SimpleGraph<>();

    disconnectedGraph4 = new SimpleGraph<>();
    disconnectedGraph4.addVertex(a.getLabel());
    disconnectedGraph4.addVertex(b.getLabel());
    disconnectedGraph4.addVertex(c.getLabel());
    disconnectedGraph4.addVertex(d.getLabel());
    
    disconnectedGraph4.addEdge(eAB);
    disconnectedGraph4.addEdge(eCB);    
    disconnectedGraph4.addEdge(eAC);    
    disconnectedGraph4.addEdge(eDD);
    
    disconnectedGraph5 = new SimpleGraph<>();
    disconnectedGraph5.addVertex(vLabel);
    disconnectedGraph5.addVertex(uLabel);
    disconnectedGraph5.addVertex(wLabel);
    disconnectedGraph5.addVertex(zLabel);
    
    disconnectedGraph5.addEdge(eUV);
    disconnectedGraph5.addEdge(eVU);    
    disconnectedGraph5.addEdge(eVW);
    
    weightedGraph1 = new SimpleGraph<>();
    weightedGraph1.addVertex(vLabel);
    weightedGraph1.addVertex(wLabel);
    weightedGraph1.addVertex(uLabel);
    weightedGraph1.addVertex(zLabel);
    weightedGraph1.addEdge(eUVWeighted);
    weightedGraph1.addEdge(eVWWeighted);
    weightedGraph1.addEdge(eWZWeighted);
    weightedGraph1.addEdge(eZUWeighted);
    
    weightedUndirectedGraph = new SimpleGraph<>();
    weightedUndirectedGraph.addVertex(vLabel);
    weightedUndirectedGraph.addVertex(wLabel);
    weightedUndirectedGraph.addVertex(uLabel);
    weightedUndirectedGraph.addVertex(zLabel);
    weightedUndirectedGraph.addEdge(eUVWeighted);
    weightedUndirectedGraph.addEdge(eVUWeighted);
    weightedUndirectedGraph.addEdge(eVWWeighted);
    weightedUndirectedGraph.addEdge(eWVWeighted);
    weightedUndirectedGraph.addEdge(eWZWeighted);
    weightedUndirectedGraph.addEdge(eZWWeighted);
    weightedUndirectedGraph.addEdge(eZUWeighted);
    weightedUndirectedGraph.addEdge(eUZWeighted);        
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

  private static <T> void testEdge(Graph<T> g, String sourceLabel, String destLabel, double weight, boolean undirected) {
    assertTrue(g.hasVertex(sourceLabel));
    assertTrue(g.hasVertex(destLabel));
    Vertex<T> vA = g.getVertex(sourceLabel).get();
    Vertex<T> vB = g.getVertex(destLabel).get();
    assertTrue(g.getNeighbours(vA).contains(vB));
    assertTrue(g.getEdgesFrom(vA)
      .stream()
      .anyMatch(e -> e.getDestination().equals(vB) && e.getWeight() == weight));
    
    assertEquals(undirected, g.getNeighbours(vB).contains(vA));
  }
 
  private static <T> void testEdge(Graph<T> g, String sourceLabel, String destLabel, boolean undirected) {
    testEdge(g, sourceLabel, destLabel, DEFAULT_EDGE_WEIGHT, undirected);
  }
  
  @Test
  public void testFromStringVertex() {
    Graph<?> g = SimpleGraph.fromString("[a]");
    assertTrue(g.hasVertex("a"));
    g = SimpleGraph.fromString("[ a  ]");
    assertTrue(g.hasVertex("a"));
    g = SimpleGraph.fromString("[ a , bi ,  www1 ]");
    assertTrue(g.hasVertex("a"));
    assertTrue(g.hasVertex("bi"));
    assertTrue(g.hasVertex("www1"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromIllegalStringGraph() {
    SimpleGraph.fromString("a]");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromIllegalStringVertex() {
    SimpleGraph.fromString("[a b]");
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
    Graph<String> g = SimpleGraph.fromString("[a>b, b-c, d-e, d>b, d-d, e, f-a, f>f]");
    Set<Vertex<String>> expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("b", "f"))));
    assertEquals(expected, g.getNeighbours(g.getOrAddVertex("a")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("c"))));
    assertEquals(expected, g.getNeighbours(g.getOrAddVertex("b")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("b", "e"))));
    assertEquals(expected, g.getNeighbours(g.getOrAddVertex("d")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("a"))));
    assertEquals(expected, g.getNeighbours(g.getOrAddVertex("f")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("a"))));
    assertTrue(g.getNeighbours(g.getOrAddVertex("")).isEmpty());
  }
  

  @Test
  public void testGetAdjacentVertices() {
    Graph<String> g = SimpleGraph.fromString("[a>b, b-c, d-e, d>b, d-d, f-a, f>f]");
    Set<Vertex<String>> expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("b", "f"))));
    assertEquals(expected, g.getAdjacentVertices(g.getOrAddVertex("a")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("a", "c", "d"))));
    assertEquals(expected, g.getAdjacentVertices(g.getOrAddVertex("b")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("b", "e"))));
    assertEquals(expected, g.getAdjacentVertices(g.getOrAddVertex("d")));

    expected = new HashSet<Vertex<String>>(
        (listLabels2ListVertex(g, Arrays.asList("a"))));
    assertEquals(expected, g.getAdjacentVertices(g.getOrAddVertex("f")));

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

  @Test(expected = NullPointerException.class)
  public void testInDegreeNullVertex() {
    graph.inDegree((Vertex<Integer>)null);
  }

  @Test(expected = NullPointerException.class)
  public void testInDegreeNullLabel() {
    graph.inDegree((String)null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInDegreeInvalidVertex() {
    graph.inDegree(v);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInDegreeInvalidLabel() {
    graph.inDegree("xyz");
  }
    
  @Test
  public void testInDegreeString() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b, a-c, b>d, e>f]");
    assertEquals(2, g.inDegree("a"));
    assertEquals(1, g.inDegree("b"));
    assertEquals(1, g.inDegree("c"));
    assertEquals(1, g.inDegree("d"));
    assertEquals(0, g.inDegree("e"));
    assertEquals(1, g.inDegree("f"));
  }

  @Test
  public void testInDegreeVertex() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b, a-c, b>d, e>f]");
    assertEquals(2, g.inDegree(g.getVertex("a").get()));
    assertEquals(1, g.inDegree(g.getVertex("b").get()));
    assertEquals(1, g.inDegree(g.getVertex("c").get()));
    assertEquals(1, g.inDegree(g.getVertex("d").get()));
    assertEquals(0, g.inDegree(g.getVertex("e").get()));
    assertEquals(1, g.inDegree(g.getVertex("f").get()));
  }

  @Test(expected = NullPointerException.class)
  public void testOutDegreeNullVertex() {
    graph.outDegree((Vertex<Integer>) null);
  }

  @Test(expected = NullPointerException.class)
  public void testOutDegreeNullLabel() {
    graph.outDegree((String) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutDegreeInvalidVertex() {
    graph.outDegree(v);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutDegreeInvalidLabel() {
    graph.outDegree("xyz");
  }

  @Test
  public void testOutDegreeString() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b, a-c, b>d, e>f]");
    assertEquals(2, g.outDegree("a"));
    assertEquals(2, g.outDegree("b"));
    assertEquals(1, g.outDegree("c"));
    assertEquals(0, g.outDegree("d"));
    assertEquals(1, g.outDegree("e"));
    assertEquals(0, g.outDegree("f"));
  }

  @Test
  public void testOutDegreeVertex() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b, a-c, b>d, e>f]");
    assertEquals(2, g.outDegree(g.getVertex("a").get()));
    assertEquals(2, g.outDegree(g.getVertex("b").get()));
    assertEquals(1, g.outDegree(g.getVertex("c").get()));
    assertEquals(0, g.outDegree(g.getVertex("d").get()));
    assertEquals(1, g.outDegree(g.getVertex("e").get()));
    assertEquals(0, g.outDegree(g.getVertex("f").get()));
  }

  @Test
  public void testFromStringDirectedEdge() {
    Graph<Integer> g = SimpleGraph.fromString("[a>b]");
    testEdge(g, "a", "b", false);
    g = SimpleGraph.fromString("[A > bi ]");
    testEdge(g, "A", "bi", false);
    g = SimpleGraph.fromString("[ A > bi , cii > DD,    z>    w9]");
    testEdge(g, "A", "bi", false);
    testEdge(g, "cii", "DD", false);
    testEdge(g, "z", "w9", false);
  }
  
  @Test
  public void testFromStringDirectedWeightedEdge() {
    Graph<Integer> g = SimpleGraph.fromString("[a>b/1]");
    testEdge(g, "a", "b", 1, false);
    g = SimpleGraph.fromString("[A > bi /  2.0 ]");
    testEdge(g, "A", "bi", 2.0, false);
    g = SimpleGraph.fromString(" [ A > bi/03.1 , cii > DD/1234.56,    z>    w9  / 3.14158  ]  ");
    testEdge(g, "A", "bi", 3.1, false);
    testEdge(g, "cii", "DD", 1234.56, false);
    testEdge(g, "z", "w9", 3.14158, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromIllegalStringDirectedEdge() {
    SimpleGraph.fromString("[a - b>c]");
  }

  @Test
  public void testFromStringUndirectedEdge() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b]");
    testEdge(g, "a", "b", true);
    g = SimpleGraph.fromString("[A - b$i ]");
    testEdge(g, "A", "b$i", true);
    g = SimpleGraph.fromString("[ A - bi , 0cii - _DD,    z -    w9]");
    testEdge(g, "A", "bi", true);
    testEdge(g, "0cii", "_DD", true);
    testEdge(g, "z", "w9", true);
  }

  @Test
  public void testFromStringUndirectedWeightedEdge() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b/10]");
    testEdge(g, "a", "b", 10, true);
    g = SimpleGraph.fromString("[A - b$i / 2.5  ]");
    testEdge(g, "A", "b$i", 2.5, true);
    g = SimpleGraph.fromString("[ A - bi /3 , 0cii - _DD/123.12,    z -    w9 /  12]");
    testEdge(g, "A", "bi", 3, true);
    testEdge(g, "0cii", "_DD", 123.12, true);
    testEdge(g, "z", "w9", 12, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromIllegalStringUndirectedEdge() {
    SimpleGraph.fromString("[a > b-c]");
  }

  @Test
  public void testFromString() {
    Graph<Integer> g = SimpleGraph.fromString("[a-b, c, d > e, a> e, b > d, d > b]");
    testEdge(g, "a", "b", true);
    testEdge(g, "d", "e", false);
    testEdge(g, "a", "e", false);
    testEdge(g, "d", "b", true);
    assertTrue(g.hasVertex("c"));
    assertEquals(g.getNeighbours(g.getVertex("c").get()), new HashSet<>(Arrays.asList()));
  }
  
  
  @Test
  public void testToString() {
    assertEquals("[u > v, v > w1, w > z, z > u]", cycleGraph.toString());
    assertEquals("[u - v, v > w1, z]", disconnectedGraph5.toString());
    disconnectedGraph5.addEdge(eUVWeighted);
    assertEquals("[u > v/1.0, v > u, v > w1, z]", disconnectedGraph5.toString());
    disconnectedGraph5.addEdge(eVUWeighted);
    assertEquals("[u - v/1.0, v > w1, z]", disconnectedGraph5.toString());
    //undo changes to the graph
    disconnectedGraph5.addEdge(eUV);
    disconnectedGraph5.addEdge(eVU);
  }

  @Test
  public void testDfs() {
    Map<Vertex<String>, Integer> exitTimes = connectedGraph1.dfs().exitTimes();
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
    Map<Vertex<String>, Integer> exitTimes = connectedGraph1.dfs(c).exitTimes();
    assertEquals((Integer)1, exitTimes.get(a));
    assertEquals((Integer)2, exitTimes.get(b));
    assertEquals((Integer)3, exitTimes.get(e));
    assertEquals((Integer)4, exitTimes.get(i));
    assertEquals((Integer)5, exitTimes.get(g));
    assertEquals((Integer)6, exitTimes.get(f));
    assertEquals((Integer)7, exitTimes.get(d));
    assertEquals((Integer)8, exitTimes.get(h));
    assertEquals((Integer)9, exitTimes.get(c));
    
    exitTimes = connectedGraph1.dfs(a).exitTimes();
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
    Map<Vertex<Integer>, Integer> exitTimes = cycleGraph.dfs(u).exitTimes();
    assertEquals((Integer)1, exitTimes.get(z));
    assertEquals((Integer)2, exitTimes.get(w));
    assertEquals((Integer)3, exitTimes.get(v));
    assertEquals((Integer)4, exitTimes.get(u));
    
    exitTimes = cycleGraph.dfs(v).exitTimes();
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
    assertEquals(expectedResult, disconnectedGraph4.allAcyclicPaths(a, b));  

    expectedResult.clear();
    expectedResult.add(Arrays.asList(a, c));
    assertEquals(expectedResult, disconnectedGraph4.allAcyclicPaths(a, c));  

    Set<List<Vertex<String>>> expectedResultEmpty = new HashSet<>();
    assertEquals(expectedResultEmpty, disconnectedGraph4.allAcyclicPaths(a, d));  
    assertEquals(expectedResultEmpty, disconnectedGraph4.allAcyclicPaths(b, a));  
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
  public void testAllCycles() {
    Set<List<Vertex<String>>> expectedResult = new HashSet<>();
    expectedResult.add(Arrays.asList(c, d, f, c));
    expectedResult.add(Arrays.asList(c, h, i, c));
    expectedResult.add(Arrays.asList(c, d, f, g, i, c));
    assertEquals(expectedResult, connectedGraph1.allCycles(c));  

    expectedResult.clear();
    expectedResult.add(Arrays.asList(f, c, d, f));
    expectedResult.add(Arrays.asList(f, g, i, c, d, f));
    assertEquals(expectedResult, connectedGraph1.allCycles(f));  

    Set<List<Vertex<String>>> expectedResultEmpty = new HashSet<>();
    assertEquals(expectedResultEmpty, connectedGraph1.allCycles(a));  
    assertEquals(expectedResultEmpty, connectedGraph1.allCycles(b));  
  }
  
  
  @Test
  public void testAllCyclesOnACycle() {
    Set<List<Vertex<Integer>>> expectedResult = new HashSet<>();
    expectedResult.add(Arrays.asList(u, v, w, z, u));
    assertEquals(expectedResult, cycleGraph.allCycles(u));
    expectedResult.clear();
    expectedResult.add(Arrays.asList(w, z, u, v, w));
    assertEquals(expectedResult, cycleGraph.allCycles(w));  
  }
  
  @Test
  public void isConnected() {
    assertTrue(pathGraph.isConnected());
    assertTrue(cycleGraph.isConnected());
    assertTrue(connectedGraph1.isConnected());
    //True for a graph with a single vertex
    assertTrue(connectedGraph4.isConnected());
    assertFalse(disconnectedGraph1.isConnected());
    //False for a graph with 2 vertices and no edge
    assertFalse(disconnectedGraph2.isConnected());
    //False for empty graph
    assertFalse(disconnectedGraph3.isConnected());
    assertFalse(disconnectedGraph4.isConnected());
    assertFalse(disconnectedGraph5.isConnected());
  }

  @Test
  public void isAcyclic() {
    assertTrue(pathGraph.isAcyclic());
    assertFalse(cycleGraph.isAcyclic());
    assertFalse(connectedGraph1.isAcyclic());
    //True for a graph with a single vertex
    assertTrue(connectedGraph4.isAcyclic());
    assertTrue(disconnectedGraph1.isAcyclic());
    //True for a graph with 2 vertices and no edge
    assertTrue(disconnectedGraph2.isAcyclic());
    //True for empty graph
    assertTrue(disconnectedGraph3.isAcyclic());
    assertTrue(disconnectedGraph4.isAcyclic());
    assertFalse(disconnectedGraph5.isAcyclic());
  }

  @Test
  public void isTree() {
    assertTrue(pathGraph.isTree());
    assertFalse(cycleGraph.isTree());
    assertFalse(connectedGraph1.isTree());
    //False for a graph with a single vertex
    assertTrue(connectedGraph4.isTree());
    assertFalse(disconnectedGraph1.isTree());
    //False for a graph with 2 vertices and no edge
    assertFalse(disconnectedGraph2.isTree());
    //False for empty graph
    assertFalse(disconnectedGraph3.isTree());
    assertFalse(disconnectedGraph4.isTree());
    assertFalse(disconnectedGraph5.isTree());
  }
  
  @Test
  public void isSpanningTree() {
    Set<Graph<Integer>> expectedResult = new HashSet<>();
    expectedResult.add(pathGraph);
    //A path is a tree
    assertEquals(expectedResult, pathGraph.allSpanningTrees());
    
    expectedResult.clear();
    //Every path in the cycle is a tree
    
    Graph<Integer> g = new SimpleGraph<Integer>();
    g.addVertex(u);
    g.addVertex(v);
    g.addVertex(w);
    g.addVertex(z);
    g.addEdge(eUV);
    g.addEdge(eVW);
    g.addEdge(eWZ);
    
    expectedResult.add(g);

    g = new SimpleGraph<Integer>();
    g.addVertex(u);
    g.addVertex(v);
    g.addVertex(w);
    g.addVertex(z);
    g.addEdge(eVW);
    g.addEdge(eWZ);
    g.addEdge(eZU);
    
    expectedResult.add(g);
   
    g = new SimpleGraph<Integer>();
    g.addVertex(u);
    g.addVertex(v);
    g.addVertex(w);
    g.addVertex(z);
    g.addEdge(eZU);
    g.addEdge(eUV);
    g.addEdge(eVW);
    
    expectedResult.add(g);

    g = new SimpleGraph<Integer>();
    g.addVertex(u);
    g.addVertex(v);
    g.addVertex(w);
    g.addVertex(z);
    g.addEdge(eUV);
    g.addEdge(eWZ);
    g.addEdge(eZU);
    
    expectedResult.add(g);

    assertEquals(expectedResult, cycleGraph.allSpanningTrees());
  }
  
  @Test
  public void testPrimUndirected() {
    //Prim doesn't work on certain directed Graphs
    Graph<Integer> expectedResult = weightedUndirectedGraph.allSpanningTrees().stream().min(GraphComparatorByWeight).get();
    assertEquals(expectedResult, weightedUndirectedGraph.prim());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testPrimDirected() {
    //Prim doesn't work on certain directed Graphs
    weightedGraph1.prim();
  }
  
  @Test
  public void testIsIsomorphic() {
    //Simple vertex
    assertTrue(SimpleGraph.fromString("[a-b]").isIsomorphicTo(SimpleGraph.fromString("[f-g]")));
    //Labels don't matter
    assertTrue(SimpleGraph.fromString("[a>b]").isIsomorphicTo(SimpleGraph.fromString("[b>a]")));
    
    //directed VS undirected
    assertFalse(SimpleGraph.fromString("[a-b]").isIsomorphicTo(SimpleGraph.fromString("[f>g]")));
    assertFalse(SimpleGraph.fromString("[a>b]").isIsomorphicTo(SimpleGraph.fromString("[f-g]")));
    assertFalse(SimpleGraph.fromString("[a>b]").isIsomorphicTo(SimpleGraph.fromString("[a-bg]")));
    assertFalse(SimpleGraph.fromString("[a>b, b-c]").isIsomorphicTo(SimpleGraph.fromString("[a>c]")));

    //Complex Graph
    assertTrue(SimpleGraph.fromString("[a>b, b-c]").isIsomorphicTo(SimpleGraph.fromString("[a>c, c-d]")));
    //Weight doesn't matter
    assertTrue(SimpleGraph.fromString("[a>b/2, b-c]").isIsomorphicTo(SimpleGraph.fromString("[a>c/44, c-d]")));
  }

  private <T> List<Vertex<T>> listLabels2ListVertex(Graph<T> g, List<String> list) {
    return list.stream()
      .map(v -> g.getVertex(v).get())
      .collect(Collectors.toList());
  }
  
  @Test
  public void testVerticesByDegree() {
    Graph<String> g = SimpleGraph.fromString("[a>b, b-c, f]");
    List<Vertex<String>> expectedResult =
      listLabels2ListVertex(g, Arrays.asList("b", "c", "a", "f"));

    assertEquals(expectedResult, g.verticesByDegree());
  }
  
  @Test(expected = NullPointerException.class)
  public void testVerticesByDepthFromNullVertex() {
    graph.verticesByDepthFrom((Vertex<Integer>)null);
  }
  
  @Test(expected = NullPointerException.class)
  public void testVerticesByDepthFromNullString() {
    graph.verticesByDepthFrom((Vertex<Integer>)null);
  }
  
  
  @Test(expected = IllegalArgumentException.class)
  public void testVerticesByDepthFromIllegalVertex() {
    graph.verticesByDepthFrom(v);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testVerticesByDepthFromIllegalString() {
    graph.verticesByDepthFrom(vLabel);
  }
  
  @Test
  public void testVerticesByDepthFrom() {
    Graph<Double> g = SimpleGraph.fromString("[a-b, b-c, e, a-c, a-d]");
    List<Vertex<Double>> expectedResult =
      listLabels2ListVertex(g, Arrays.asList("c", "b", "a", "d"));

    assertEquals(expectedResult, g.verticesByDepthFrom("d"));
  }
  
  @Test
  public void testIsUndirected() {
    assertTrue(SimpleGraph.fromString("[a-b, b-c, e, a-c, a-d]").isUndirected());
    assertTrue(SimpleGraph.fromString("[a-b]").isUndirected());
    assertTrue(SimpleGraph.fromString("[a]").isUndirected());
    assertTrue(SimpleGraph.fromString("[a>b, b>a]").isUndirected());
    assertFalse(SimpleGraph.fromString("[a>b]").isUndirected());
    assertFalse(SimpleGraph.fromString("[a>b, b-c]").isUndirected());
    assertFalse(SimpleGraph.fromString("[a>b/1, b>a/2]").isUndirected());
  }
  
  @Test
  public void testSubgraph() {
    Graph<Double> g = SimpleGraph.fromString("[a-b, b-c, e, a-c, a>d, c>f]");
    Set<Edge<Double>> gEdges = new HashSet<>(g.getEdges());
    //Simple subset
    Graph<Double> subG1 = g.subGraph(new HashSet<>(Arrays.asList("a")));
    assertEquals(1, subG1.size());
    assertEquals(0, subG1.edgesSize());
    assertTrue(subG1.hasVertex("a"));
    //More complex subset
    Graph<Double> subG2 = g.subGraph(new HashSet<>(Arrays.asList("a", "b", "c")));
    assertEquals(3, subG2.size());
    assertEquals(6, subG2.edgesSize());
    assertTrue(subG2.hasVertex("a"));
    assertTrue(subG2.hasVertex("b"));
    assertTrue(subG2.hasVertex("c"));
    for (Edge<Double> e : subG2.getEdges()) {
      gEdges.contains(e);
    }
    //List<String> overloading won't freak out for duplicates
    Graph<Double> subG3 = g.subGraph(Arrays.asList("a", "a", "b", "a", "c", "b"));
    assertEquals(3, subG3.size());
    assertEquals(6, subG3.edgesSize());
    assertTrue(subG3.hasVertex("a"));
    assertTrue(subG3.hasVertex("b"));
    assertTrue(subG3.hasVertex("c"));
    for (Edge<Double> e : subG3.getEdges()) {
      gEdges.contains(e);
    }
    //Directed vs Undirected
    Graph<Double> subG4 = g.subGraph(Arrays.asList("c", "f", "a", "d", "c", "d"));
    assertEquals(4, subG4.size());
    assertEquals(4, subG4.edgesSize());
    assertTrue(subG4.hasVertex("a"));
    assertTrue(subG4.hasVertex("c"));
    assertTrue(subG4.hasVertex("d"));
    assertTrue(subG4.hasVertex("f"));
    for (Edge<Double> e : subG4.getEdges()) {
      gEdges.contains(e);
    }
  }
  
  @Test
  public void testInverse() {
    assertEquals(SimpleGraph.fromString("[a-b, b-c, e, a-c, d>a, f>c]"),
      SimpleGraph.fromString("[a-b, b-c, e, a-c, a>d, c>f]").inverse());

    Graph<Integer> g1 = SimpleGraph.fromString("[a-b, b-c]");
    assertEquals(g1, g1.inverse());

    Graph<Integer> g2 = SimpleGraph.fromString("[a>b/7, b>c/3.1415]");
    Graph<Integer> g2I = SimpleGraph.fromString("[b>a/7, c>b/3.1415]");
    assertEquals(g2I, g2.inverse());
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testConnectedComponentsDirected() {
    SimpleGraph.fromString("[b>a/7, c>b/3.1415]").connectedComponents();
  }
  
  @Test
  public void connectedComponents() {
    //Empty Graph
    Graph<Long> g0 = new SimpleGraph<>();
    assertTrue(g0.connectedComponents().isEmpty());
    
    //Single vertex graph
    Graph<Long> g1 = SimpleGraph.fromString("[vertex]");
    assertTrue(g1.connectedComponents().contains(g1));
    
    //Disconnected graph
    Graph<Byte> g2 = SimpleGraph.fromString("[a-b, b-c, e, a-c, d-f, f-g]");
    Set<Graph<Byte>> expectedResult2 = new HashSet<>(Arrays.asList(
          SimpleGraph.fromString("[a-b, b-c, a-c]"),
          SimpleGraph.fromString("[e]"),
          SimpleGraph.fromString("[d-f, f-g]")));
    assertEquals(expectedResult2, g2.connectedComponents());

    //Connected graph
    Graph<Byte> g3 = SimpleGraph.fromString("[a-b, b-c, c-d, d-f, f-g]");
    Set<Graph<Byte>> expectedResult3 = new HashSet<>(Arrays.asList(g3));
    assertEquals(expectedResult3, g3.connectedComponents());  
  }

  @Test
  public void stronglyConnectedComponents() {
    //Empty Graph
    Graph<Long> g0 = new SimpleGraph<>();
    assertTrue(g0.stronglyConnectedComponents().isEmpty());
    
    //Single vertex graph
    Graph<Long> g1 = SimpleGraph.fromString("[vertex]");
    assertTrue(g1.stronglyConnectedComponents().contains(g1));
    
    //Disconnected graph
    Graph<Byte> g2 = SimpleGraph.fromString("[a-b, b-c, e, a-c, d-f, f-g]");
    Set<Graph<Byte>> expectedResult2 = new HashSet<>(Arrays.asList(
          SimpleGraph.fromString("[a-b, b-c, a-c]"),
          SimpleGraph.fromString("[e]"),
          SimpleGraph.fromString("[d-f, f-g]")));
    assertEquals(expectedResult2, g2.stronglyConnectedComponents());

    //Disconnected directed graph
    Graph<Byte> g3 = SimpleGraph.fromString("[a>b, b>c, e, c>a, d>f, f>g, g>d]");
    Set<Graph<Byte>> expectedResult3 = new HashSet<>(Arrays.asList(
          SimpleGraph.fromString("[a>b, b>c, c>a]"),
          SimpleGraph.fromString("[e]"),
          SimpleGraph.fromString("[d>f, f>g, g>d]")));
    assertEquals(expectedResult3, g3.stronglyConnectedComponents());

    //Connected undirected graph
    Graph<Byte> g4 = SimpleGraph.fromString("[a-b, b-c, c-d, d-f, f-g]");
    Set<Graph<Byte>> expectedResult4 = new HashSet<>(Arrays.asList(g4));
    assertEquals(expectedResult4, g4.stronglyConnectedComponents());  

    //Connected undirected graph
    Graph<Byte> g5 = SimpleGraph.fromString("[a>b, b>c, c>d, d>f, f-c, c>a]");
    Set<Graph<Byte>> expectedResult5 = new HashSet<>(Arrays.asList(g5));
    assertEquals(expectedResult5, g5.stronglyConnectedComponents());  
  }
  
  @Test
  public void testIsBipartite() {
    assertTrue(new SimpleGraph<String>().isBipartite());
    assertTrue(SimpleGraph.fromString("[a]").isBipartite());
    assertTrue(SimpleGraph.fromString("[a-b]").isBipartite());
    assertTrue(SimpleGraph.fromString("[a>b]").isBipartite());
    assertTrue(SimpleGraph.fromString("[a>b, b>c, c>d, d>f]").isBipartite());
    assertFalse(SimpleGraph.fromString("[a>b, b>c, c>d, d>f, f-c, c>a]").isBipartite());
  }
  
  @Test
  public void testVertexColoring() {
    Graph<Object> g = SimpleGraph.fromString("[a-b, b-c, a-c, a-d]");
    Map<Vertex<Object>, Byte> coloring = g.vertexColoring();
    assertEquals((Byte)((byte)0), coloring.get(g.getVertex("a").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("b").get()));
    assertEquals((Byte)((byte)2), coloring.get(g.getVertex("c").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("d").get()));

    g = SimpleGraph.fromString("[a-b, a-h, b-d, c-d, d-i, d-k, e-f, e-k, f-g, g-h, g-k, h-i, h-j, h-k, i-j, j-k]");
    coloring = g.vertexColoring();
    assertEquals((Byte)((byte)0), coloring.get(g.getVertex("h").get()));
    assertEquals((Byte)((byte)0), coloring.get(g.getVertex("d").get()));
    assertEquals((Byte)((byte)0), coloring.get(g.getVertex("e").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("k").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("i").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("a").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("f").get()));
    assertEquals((Byte)((byte)1), coloring.get(g.getVertex("c").get()));
    assertEquals((Byte)((byte)2), coloring.get(g.getVertex("g").get()));
    assertEquals((Byte)((byte)2), coloring.get(g.getVertex("j").get()));
    assertEquals((Byte)((byte)2), coloring.get(g.getVertex("b").get()));
  }
}
