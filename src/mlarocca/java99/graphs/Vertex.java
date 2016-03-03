package mlarocca.java99.graphs;

import java.util.List;
import java.util.Optional;

public interface Vertex<T> extends Cloneable {
  public String getLabel();
  public Optional<T> getValue();
  public List<Vertex<? extends T>> getNeighbours();
  public List<Edge<T>> getAdjList();
  public Object clone();
}

interface MutableVertex<T> extends Vertex<T> {
  boolean addOutgoingEdge(MutableVertex<T> destination, double weight);
}