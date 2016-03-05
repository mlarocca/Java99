package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SimpleGraph<T> implements Graph<T> {

  private ConcurrentMap<SimpleVertex<T>, List<Edge<T>>> adjList;
  private ConcurrentMap<String, SimpleVertex<T>> labelToVertex;

  //Lazily computed list of vertices (MUST be set to null when a vertex is added)
  private List<Vertex<T>> vertices;
  //Lazily computed list of edges (MUST be set to null when an edge is added)
  private List<Edge<T>> edges;
  
  public SimpleGraph() {
    adjList = new ConcurrentHashMap<>();
    labelToVertex = new ConcurrentHashMap<>();
    vertices = null;
    edges = null;
  }
  
  @Override
  public List<Vertex<T>> getVertices() {
    if (vertices == null) {
      vertices = new ArrayList<>(adjList.keySet());
      Collections.sort(vertices);
    }
    return vertices;
  }

  @Override
  public List<Edge<T>> getEdges() {
    if (edges == null) {
      edges = adjList.values()
        .stream()
        .flatMap(e -> e.stream())
        .collect(Collectors.toList());
      Collections.sort(edges);
    }
    return edges;
  }


  @Override
  public synchronized Vertex<T> addVertex(String label) throws IllegalArgumentException {
    if (labelToVertex.containsKey(label)) {
      throw new IllegalArgumentException();
    }
    SimpleVertex<T> v = new SimpleVertex<>(label);
    //Reset the lazily computed list
    vertices = null;
    //Ad a new adjacency list
    adjList.put(v, new ArrayList<>());
    labelToVertex.put(label, v);
    return v;
  }

  @Override
  public synchronized Vertex<T> addVertex(String label, T value) throws IllegalArgumentException {
    SimpleVertex<T> v = (SimpleVertex<T>) addVertex(label);
    v.setValue(value);
    return v;
  }

  @Override
  public Optional<Vertex<T>> getVertex(String label) {
    Vertex<T> v = labelToVertex.get(label);
    return v == null ? Optional.empty() : Optional.of(v);
  }

  @Override
  public synchronized Edge<T> addEdge(Edge<T> e) throws NullPointerException, IllegalArgumentException {
    if (e == null) {
      throw new NullPointerException("Edge in input is null");      
    }
    Vertex<T> source = e.getSource();
    Vertex<T> destination = e.getDestination();
    
    if (!(adjList.containsKey(source) && adjList.containsKey(destination))) {
      throw new IllegalArgumentException("Both source and destination must be in the graph");
    }
    
    //Reset the lazy list of edges
    edges = null;
    
    //Makes sure it's a SimpleEdge (or a sub type of a SimpleEdge)
    SimpleEdge<T> newEdge;
    if (e instanceof SimpleEdge) {
      newEdge = (SimpleEdge<T>) e;
    } else {
      newEdge = new SimpleEdge<T>(e);
    }
    
    List<Edge<T>> adj = getAdjList(source);

    //Check if an edge between these vertices is already in the graph
    Optional<Edge<T>> maybeEdge = adj.stream()
      .filter(edge -> edge.getDestination()
      .equals(destination))
      .findAny();
    
    return maybeEdge.map(oldEdge -> {
      //Replace the old edge
      adj.remove(oldEdge);
      adj.add(newEdge);
      return newEdge;
    }).orElseGet(new Supplier<SimpleEdge<T>>() {
      //Just add the new edge to the list
      @Override
      public SimpleEdge<T> get() {
        adj.add(newEdge);
        return newEdge;
      }
    });
  }

  @Override
  public synchronized Edge<T> addEdge(Vertex<T> source, Vertex<T> destination) throws IllegalArgumentException {
    if (source == null || destination == null) {
      throw new NullPointerException("Source and destination must be valid vertices");
    }
    return addEdge(new SimpleEdge<T>(source, destination));
  }

  @Override
  public synchronized Edge<T> addEdge(Vertex<T> source, Vertex<T> destination, double weight) throws NullPointerException, IllegalArgumentException {
    if (source == null || destination == null) {
      throw new NullPointerException("Source and destination must be valid vertices");
    }
    return addEdge(new SimpleEdge<T>(source, destination, weight));
  }
  
  @Override
  public List<Vertex<? extends T>> getNeighbours(Vertex<T> v) throws IllegalArgumentException {
    return getAdjList(v)
      .parallelStream()
      .map(e -> e.getDestination())
      .collect(Collectors.toList());
  }

  @Override
  public List<Edge<T>> getAdjList(Vertex<T> v) throws IllegalArgumentException {
    if (!adjList.containsKey(v)) {
      throw new IllegalArgumentException("Vertex not in graph");
    }
    return adjList.get(v);
  }
  
  @Override
  public List<Vertex<T>> bfs(Vertex<T> source) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Vertex<T>> dfs(Vertex<T> source) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Vertex<T>> dfs() {
    // TODO Auto-generated method stub
    return null;
  }
}
