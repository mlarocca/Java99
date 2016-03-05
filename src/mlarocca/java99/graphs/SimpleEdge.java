package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.List;

class SimpleEdge<T> implements MutableEdge<T>, Comparable<SimpleEdge<T>>{

  private MutableVertex<T> source;
  private MutableVertex<T> destination;
  private double weight;
  
  public SimpleEdge(MutableVertex<T> source, MutableVertex<T> destination) {
    this(source, destination, 0);
  }
  
  public SimpleEdge(MutableVertex<T> source, MutableVertex<T> destination, double weight) {
    if (source == null || destination == null) {
      throw new IllegalArgumentException("Vertices can't be null");
    }
    this.source = source;
    this.destination = destination;
    this.weight = weight;
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
  public int compareTo(SimpleEdge<T> other) {
    return this.hashCode() - other.hashCode();
  }

}