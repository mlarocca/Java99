package mlarocca.java99.graphs;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import mlarocca.java99.graphs.data.MinDistanceResult;

public interface Graph<T> {

  public List<Vertex<T>> getVertices();
  public List<Edge<T>> getEdges();
  
  public Vertex<T> addVertex(String label) throws IllegalArgumentException;
  public Vertex<T> addVertex(String label, T value) throws IllegalArgumentException;

  public Optional<Vertex<T>> getVertex(String label);
  public boolean hasVertex(String label);
  public boolean hasVertex(Vertex<T> v);

  public List<Vertex<T>> getNeighbours(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public List<Edge<T>> getEdgesFrom(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public List<Edge<T>> getEdgesTo(Vertex<T> v) throws NullPointerException, IllegalArgumentException;
  public Optional<Edge<T>> getEdgeBetween(Vertex<T> v, Vertex<T> u) throws NullPointerException, IllegalArgumentException;

  public Edge<T> addEdge(Edge<T> e) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination, double weight) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination) throws IllegalArgumentException;  
 
  public Map<Vertex<T>, Integer> dfs();
  public Map<Vertex<T>, Integer> dfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public List<Vertex<T>> dfs(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;

  public MinDistanceResult<T> bfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> bfs(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> dijkstra(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> dijkstra(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public MinDistanceResult<T> AStar(Vertex<T> source, Vertex<T> target, Function<Vertex<T>, Double> heuristic) throws NullPointerException, IllegalArgumentException;

  public boolean isConnected();
  public boolean isAcyclic();
  public boolean isTree();
  
  public Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException;
  public Set<List<Vertex<T>>> allCycles(Vertex<T> source) throws NullPointerException, IllegalArgumentException;
  public Set<Graph<T>> allSpanningTrees();
}
