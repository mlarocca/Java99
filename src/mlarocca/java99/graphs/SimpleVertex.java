package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class SimpleVertex<T> implements MutableVertex<T>, Comparable<SimpleVertex<T>> {

  private String label;
  private Optional<T> value;
  private List<Edge<T>> adj;
  

  public SimpleVertex(String label) {
    this.label = label;
    this.adj = new ArrayList<>();
    this.value = Optional.empty();
  }
  
  public SimpleVertex(String label, T value) {
    this(label);
    this.value = Optional.of(value);
  }
  
  public SimpleVertex(String label, List<Edge<T>> adj) {
    this(label);
    adj.forEach(e -> this.adj.add(e));
  }

  public SimpleVertex(String label, T value, List<Edge<T>> adj) {
    this(label, value);
    adj.forEach(e -> this.adj.add(e));
  }
  
  @Override
  public String getLabel() {
    return label;
  }

  @Override
  public Optional<T> getValue() {
    return value;
  }

  @Override
  public List<Vertex<? extends T>> getNeighbours() {
    return adj.parallelStream().map(e -> e.getDestination()).collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Edge<T>> getAdjList() {
    return adj.parallelStream().map(e -> (Edge<T>)e.clone()).collect(Collectors.toList());
  }

  @Override
  public boolean addOutgoingEdge(MutableVertex<T> destination, double weight) {
    Optional<Edge<T>> maybeEdge = adj.stream().filter(e -> e.getDestination().equals(destination)).findAny();
    return maybeEdge.map(e -> {
      adj.remove(e);
      adj.add(new SimpleEdge<T>(this, destination, weight));
      return true;
    }).orElseGet(new Supplier<Boolean>() {
      @Override
      public Boolean get() {
        adj.add(new SimpleEdge<T>(SimpleVertex.this, destination, weight));
        return false;
      }
      
    });
  }

  @Override
  public Object clone() {
    return value.map(v -> new SimpleVertex<T>(label, v, adj))
      .orElse(new SimpleVertex<T>(label, adj));
  }
  
  /**
   * Two vertices are equals (and hence have the same hashcode) iff they have
   * the same label AND the same value.
   */
  @Override
  public int hashCode() {
    List<Object> params = new ArrayList<>();
    params.add(label);
    params.add(value);
    return params.hashCode();
  }
  
  @Override
  public boolean equals(Object other) {
    if (other == null || other.getClass() != this.getClass()) {
      return false;
    }
    return this.hashCode() == other.hashCode();
  }
  
  /**
   * To be consistent with equality method, the order of the vertex will be
   * determined by the labels' hashcode (otherwise TreeMaps etc... could be inconsistent).
   */
  @Override
  public int compareTo(SimpleVertex<T> other) {
    return this.hashCode() - other.hashCode();
  }
}
