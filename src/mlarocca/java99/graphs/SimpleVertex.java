package mlarocca.java99.graphs;

import java.util.Optional;

class SimpleVertex<T> implements MutableVertex<T> {

  private String label;
  private Optional<T> value;

  public SimpleVertex(String label) {
    this.label = label;
    this.value = Optional.empty();
  }
  
  public SimpleVertex(String label, T value) {
    this.label = label;
    this.value = Optional.of(value);
  }
  
  public SimpleVertex(Vertex<T> u) {
    this.label = u.getLabel();
    this.value = u.getValue();
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
  public Object clone() {
    return value.map(v -> new SimpleVertex<T>(label, v))
      .orElse(new SimpleVertex<T>(label));
  }
  
  /**
   * Two vertices are equals (and hence have the same hashcode) iff they have
   * the same label.
   */
  @Override
  public int hashCode() {
    return label.hashCode();
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
   * determined by the vertex' hashcode (otherwise TreeMaps etc... could be inconsistent).
   */
  @Override
  public int compareTo(Vertex<T> other) {
    return this.hashCode() - other.hashCode();
  }

  @Override
  public boolean setValue(T newValue) {
    boolean updated = this.value.map(v -> !v.equals(newValue)).orElse(true);
    this.value = Optional.of(newValue);
    return updated;
  }
}
