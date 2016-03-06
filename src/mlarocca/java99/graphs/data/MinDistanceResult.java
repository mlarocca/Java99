package mlarocca.java99.graphs.data;

import java.util.List;
import java.util.Map;

import mlarocca.java99.graphs.Vertex;

public interface MinDistanceResult<T> {
  public Map<Vertex<T>, Vertex<T>> predecessors();
  public Map<Vertex<T>, Double> distances();
  public List<Vertex<T>> path();
}
