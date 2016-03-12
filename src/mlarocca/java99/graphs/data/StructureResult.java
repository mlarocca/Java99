package mlarocca.java99.graphs.data;

import java.util.Map;

import mlarocca.java99.graphs.Vertex;

public interface StructureResult<T> {
  public Boolean isAcyclic();
  public Map<Vertex<T>, Integer> exitTimes();
}
