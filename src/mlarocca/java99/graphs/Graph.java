package mlarocca.java99.graphs;

import java.util.List;
import java.util.Optional;

public interface Graph<T> {

  public List<Vertex<T>> getVertices();
  public List<Edge<T>> getEdge();
  
  public void addVertex(String label) throws IllegalArgumentException;
  public void addVertex(String label, T value) throws IllegalArgumentException;

  public Vertex<T> getVertex(String label, Optional<T> value);
  
  public void addEdge(Vertex<T> source, Vertex<T> destination) throws IllegalArgumentException;  
  
  public List<Vertex<T>> bfs(Vertex<T> source);
  public List<Vertex<T>> dfs(Vertex<T> source);
  public List<Vertex<T>> dfs();
  
}
