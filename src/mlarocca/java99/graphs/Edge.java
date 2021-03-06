package mlarocca.java99.graphs;

public interface Edge<T> extends Cloneable, Comparable<Edge<T>> {
  public Vertex<T> getSource();
  public Vertex<T> getDestination();
  public double getWeight();
  public Object clone();
  public Edge<T> inverse();
}

interface MutableEdge<T> extends Edge<T> {
  
  @Override
  public MutableVertex<T> getSource();
  @Override
  public MutableVertex<T> getDestination();
}
