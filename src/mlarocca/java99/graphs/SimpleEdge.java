package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.List;

class SimpleEdge<T> implements MutableEdge<T> {

  private MutableVertex<T> source;
  private MutableVertex<T> destination;
  private double weight;
  
  public SimpleEdge(Vertex<T> source, Vertex<T> destination) {
    this(source, destination, 0);
  }
  
  public SimpleEdge(Vertex<T> source2, Vertex<T> destination2, double weight) {
    if (source2 == null || destination2 == null) {
      throw new IllegalArgumentException("Vertices can't be null");
    }
    this.source = new SimpleVertex<T>(source2);
    this.destination = new SimpleVertex<T>(destination2);
    this.weight = weight;
  }
  
  public SimpleEdge(Edge<T> e) {
    this(e.getSource(), e.getDestination(), e.getWeight());
  }
 
  @Override
  public double getWeight() {
    return weight;
  }

  @Override
  public MutableVertex<T> getSource() {
    return source;
  }

  @Override
  public MutableVertex<T> getDestination() {
    return destination;
  }

  public Object clone() {
    return new SimpleEdge<T>(getSource(), getDestination(), getWeight());
  }

  /**
   * For simple graphs, two edges are the same (and hence have the same hashcode) 
   * if they have the same source and destination.
   */
  @Override
  public int hashCode() {
    List<Object> params = new ArrayList<>();
    params.add(getSource());
    params.add(getDestination());
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
   * To be consistent with equality, edges' order will be determined by their hashCode
   * (otherwise TreeMaps, TreeSets etc... could provide unexpected results).
   */
  @Override
  public int compareTo(Edge<T> other) {
    return this.hashCode() - other.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s -> %s [%s]", getSource(), getDestination(), getWeight());
  }

  @Override
  public Edge<T> inverse() {
    return new SimpleEdge<T>(getDestination(), getSource(), getWeight());
  }
}