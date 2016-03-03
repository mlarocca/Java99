package mlarocca.java99.graphs;

class SimpleEdge<T> implements MutableEdge<T> {

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

}
