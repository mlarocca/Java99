package mlarocca.java99.graphs;

import java.util.List;
import java.util.Optional;

public interface Graph<T> {

  public List<Vertex<T>> getVertices();
  public List<Edge<T>> getEdges();
  
  public Vertex<T> addVertex(String label) throws IllegalArgumentException;
  public Vertex<T> addVertex(String label, T value) throws IllegalArgumentException;

  public Optional<Vertex<T>> getVertex(String label);

  public List<Vertex<? extends T>> getNeighbours(Vertex<T> v);
  public List<Edge<T>> getAdjList(Vertex<T> v);

  public Edge<T> addEdge(Edge<T> e) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination, double weight) throws IllegalArgumentException;  
  public Edge<T> addEdge(Vertex<T> source, Vertex<T> destination) throws IllegalArgumentException;  

  public List<Vertex<T>> bfs(Vertex<T> source);
  public List<Vertex<T>> dfs(Vertex<T> source);
  public List<Vertex<T>> dfs();
  
}
