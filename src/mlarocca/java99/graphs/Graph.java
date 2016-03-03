package mlarocca.java99.graphs;

import java.util.List;

public interface Graph<T> {

  public List<Vertex<T>> getVertices();
  public List<Edge> getEdge();
  
  public List<Vertex<T>> bfs(Vertex<T> source);
  public List<Vertex<T>> dfs(Vertex<T> source);
  public List<Vertex<T>> dfs();
  
}
