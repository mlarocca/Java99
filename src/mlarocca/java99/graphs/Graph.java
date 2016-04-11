package mlarocca.java99.graphs;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import mlarocca.java99.graphs.data.MinDistanceResult;
import mlarocca.java99.graphs.data.StructureResult;

public interface Graph<T> {
  public interface SetVertexRef<R> extends Supplier<Set<Vertex<R>>> {}
  public interface ListVertexRef<R> extends Supplier<List<Vertex<R>>> {}
  
  public List<Vertex<T>> getVertices();
  public List<Edge<T>> getEdges();

  public int size();
  public int edgesSize();
  
  public Vertex<T> addVertex(String label) throws IllegalArgumentException;
  public Vertex<T> addVertex(String label, T value) throws IllegalArgumentException;
  public Vertex<T> addVertex(Vertex<T> v) throws IllegalArgumentException;
  public Vertex<T> getOrAddVertex(String label);
  public Vertex<T> getOrAddVertex(String label, Optional<T> value);
  
  public Optional<Vertex<T>> getVertex(String label);
  public boolean hasVertex(String label);
  public boolean hasVertex(Vertex<T> v);

  public List<Vertex<T>> getNeighbours(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public List<Edge<T>> getEdgesFrom(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public List<Edge<T>> getEdgesTo(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public Optional<Edge<T>> getEdgeBetween(Vertex<T> v, Vertex<T> u) throws NullPointerException, IllegalArgumentException;

  public int inDegree(Vertex<T> v);
  public int inDegree(String label);
  public int outDegree(Vertex<T> v);
  public int outDegree(String label);
  
  public Edge<T> addEdge(Edge<T> e) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination, double weight) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination) throws IllegalArgumentException;  
 
  public StructureResult<T> dfs();
  public StructureResult<T> dfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public List<Vertex<T>> topologicalOrder();

  public List<Vertex<T>> dfs(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  
  public MinDistanceResult<T> bfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> bfs(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> dijkstra(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> dijkstra(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> AStar(Vertex<T> source, Vertex<T> target, Function<Vertex<T>, Double> heuristic) throws NullPointerException, IllegalArgumentException;

  public Graph<T> prim() throws UnsupportedOperationException;

  public boolean isConnected();
  public boolean isAcyclic();
  public boolean isTree();
  public boolean isUndirected();
  
  public Graph<T> inverse();
  
  public Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public Set<List<Vertex<T>>> allCycles(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public Set<Graph<T>> allSpanningTrees();
  
  public <R> boolean isIsomorphicTo(Graph<R> other);
  
  public List<Vertex<T>> verticesByDegree();
  
  public List<Vertex<T>> verticesByDepthFrom(String label);
  public List<Vertex<T>> verticesByDepthFrom(Vertex<T> v);
  
  public Graph<T> subGraph(Set<String> subset) throws IllegalArgumentException, NullPointerException;
  public Graph<T> subGraph(List<String> subset) throws IllegalArgumentException, NullPointerException;
  public Graph<T> subGraph(SetVertexRef<T> subset) throws IllegalArgumentException, NullPointerException;
  public Graph<T> subGraph(ListVertexRef<T> subset) throws IllegalArgumentException, NullPointerException;

  public Set<Graph<T>> connectedComponents() throws UnsupportedOperationException;
  public Set<Graph<T>> stronglyConnectedComponents();
}

//Interface for package only methods
interface GraphInternal<T> extends Graph<T> {
  public Vertex<T> getOrAddVertex(String label);
}
