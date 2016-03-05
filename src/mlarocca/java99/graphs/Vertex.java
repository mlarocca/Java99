package mlarocca.java99.graphs;

import java.util.Optional;

public interface Vertex<T> extends Cloneable, Comparable<Vertex<T>> {
  public String getLabel();
  public Optional<T> getValue();
  public Object clone();
}

interface MutableVertex<T> extends Vertex<T> {
  boolean setValue(T newValue);
}