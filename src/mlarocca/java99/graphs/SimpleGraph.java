package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import mlarocca.java99.graphs.data.MinDistanceResult;

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
  public boolean hasVertex(String label) {
    return labelToVertex.containsKey(label);
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
    
    List<Edge<T>> adj = getEdgesFrom(source);

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
  public List<Vertex<T>> getNeighbours(Vertex<T> v) throws IllegalArgumentException {
    return getEdgesFrom(v)
      .parallelStream()
      .map(e -> e.getDestination())
      .collect(Collectors.toList());
  }

  @Override
  public List<Edge<T>> getEdgesFrom(Vertex<T> v) throws IllegalArgumentException {
    if (!adjList.containsKey(v)) {
      throw new IllegalArgumentException("Vertex not in graph");
    }
    return adjList.get(v);
  }

  @Override
  public List<Edge<T>> getEdgesTo(Vertex<T> v) throws IllegalArgumentException {
    List<Edge<T>> edgesToV = getEdges()
      .stream()
      .filter(e -> e.getDestination().equals(v))
      .collect(Collectors.toList());
    Collections.sort(edgesToV);
    return edgesToV;
  }
  
  @Override
  public String toString() {
    Map<Vertex<T>, HashSet<Vertex<T>>> neighbours = getVertices()
      .stream()
      .collect(Collectors.toMap(Function.identity(),
          v -> new HashSet<Vertex<T>>(this.getNeighbours(v))));

    Set<String> edges = getVertices().stream()
      .map(v -> {
        List<Edge<T>> outgoing = this.getEdgesFrom(v);
        List<Edge<T>> ingoing = this.getEdgesTo(v);
        Set<String> result = new LinkedHashSet<String>();
        if (outgoing.isEmpty() && ingoing.isEmpty()) {
           result.add(v.getLabel());
        } else {
          outgoing.forEach(e -> {
            Vertex<T> u = e.getDestination();
            Vertex<T> src;
            Vertex<T> dst;
            String edge;
            if (neighbours.get(u).contains(v)) {
              edge = "-";
              if (u.compareTo(v) < 0) {
                src = u;
                dst = v;
              } else {
                src = v;
                dst = u;
              }
            } else {
              edge = ">";
              src = v;
              dst = u;
            }
            result.add(String.format("%s %s %s", src, edge, dst));
          });
        }
        return result;
      })
      .reduce((set, subset) -> {
        set.addAll(subset);
        return set;
      }).orElse(new HashSet<>());
    return String.format("[%s]", String.join(",", edges));
  }

  /**
   * 
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public Map<Vertex<T>, Integer> dfs() {
    Map<Vertex<T>, Integer> entryTimes = new HashMap<>();
    Map<Vertex<T>, Integer> exitTimes = new HashMap<>();
    int time = 0;
    for (Vertex<T> v: getVertices()) {
      if (!entryTimes.containsKey(v)) {
        entryTimes.put(v, time);
        time = 1 + dfs(v, entryTimes, exitTimes);
      }
    }
    return exitTimes;
  }
  
  /**
   * 
   * @param source
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public Map<Vertex<T>, Integer> dfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException {
    Map<Vertex<T>, Integer> entryTimes = new HashMap<>();
    Map<Vertex<T>, Integer> exitTimes = new HashMap<>();
    entryTimes.put(source,  0);
    dfs(source, entryTimes, exitTimes);
    return exitTimes;
  }
  
  protected int dfs(Vertex<T> u, Map<Vertex<T>, Integer> entryTimes, Map<Vertex<T>, Integer> exitTimes) {
    int time = entryTimes.get(u);
    
    for (Vertex<T> v: getNeighbours(u)) {
      if (!entryTimes.containsKey(v)) {
        entryTimes.put(v, time);
        time = dfs(v, entryTimes, exitTimes);        
      }
    }
    exitTimes.put(u, time + 1);
    return time + 1;
  }
  
  /**
   * 
   * @param source
   * @param target
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public List<Vertex<T>> dfs(Vertex<T> source, Vertex<T> target) throws NullPointerException, IllegalArgumentException {
    if (!hasVertex(target.getLabel())) {
      throw new IllegalArgumentException("Target vertex doesn't belong to the graph");
    }
    return dfs(source, target, new HashSet<>());
  }

  protected List<Vertex<T>> dfs(Vertex<T> source, Vertex<T> target, Set<Vertex<T>> visited) {  
    List<Vertex<T>> path = null;
    visited.add(source);
    if (source.equals(target)) {
      path = new LinkedList<>();
      path.add(source);
    } else {
      for (Vertex<T> v: getNeighbours(source)) {
        if (!visited.contains(v)) {
          path = dfs(v, target, visited);
          if (path != null) {
            path.add(0, source);
            break;
          }
        }
      }      
    }
    return path;
  }
  
  /**
   * 
   * @param source
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */  
  @Override
  public MinDistanceResult<T> bfs(Vertex<T> source) {
    return AStar(source, v -> false, e -> 1.0, v -> 0.0);
  }
  
  /**
   * 
   * @param source
   * @param target
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public MinDistanceResult<T> bfs(Vertex<T> source, Vertex<T> target) {
    if (!hasVertex(target.getLabel())) {
      throw new IllegalArgumentException("Target vertex doesn't belong to the graph");
    }
    
    MinDistanceResult<T> result = AStar(source, v -> v.equals(target), e -> 1.0, v -> 0.0);
    return addPathToResult(source, target, result);
  }
  
  /**
   * 
   * @param source
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */  
  @Override
  public MinDistanceResult<T> dijkstra(Vertex<T> source) {
    return AStar(source, v -> false, e -> e.getWeight(), v -> 0.0);
  }

  /**
   * 
   * @param source
   * @param target
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */  
  @Override
  public MinDistanceResult<T> dijkstra(Vertex<T> source, Vertex<T> target) {
    MinDistanceResult<T> result = AStar(source, v -> v.equals(target), e -> e.getWeight(), v -> 0.0);
    return addPathToResult(source, target, result);
  }

  /**
   * 
   * @param source
   * @param target
   * @param heuristic
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public MinDistanceResult<T> AStar(Vertex<T> source, Vertex<T> target, Function<Vertex<T>, Double> heuristic) {
    MinDistanceResult<T> result = AStar(source, v -> v.equals(target), e -> e.getWeight(), heuristic);
    return addPathToResult(source, target, result);
  }

  /**
   * 
   * @param source
   * @param goalFound
   * @param distance
   * @param heuristic
   * @return
   */
  protected MinDistanceResult<T> AStar(
      Vertex<T> source, Predicate<Vertex<T>> goalFound, 
      Function<Edge<T>, Double> distance, 
      Function<Vertex<T>, Double> heuristic) {
    if (!hasVertex(source.getLabel())) {
      throw new IllegalArgumentException("Source vertex doesn't belong to the graph");
    }
    
    int n = this.getVertices().size();
    Map<Vertex<T>, Vertex<T>> predecessors = new HashMap<>(n);
    Set<Vertex<T>> visited = new HashSet<>(n);
    Map<Vertex<T>, Double> distances = new HashMap<>(n);
    Queue<Vertex<T>> queue = initMinDistanceUtilities(n, source, predecessors, visited, distances, heuristic);
    
    while (!queue.isEmpty()) {
      Vertex<T> v = queue.remove();
      visited.add(v);
      if (goalFound.test(v)) {
        break;
      }
      //Invariant: v is contained in distances at this point
      double dV = distances.get(v);
      for (Edge<T> e: getEdgesFrom(v)) {
        checkEdgeAndAddNodeToQueue(queue, e, dV, visited, distances, predecessors, distance);
      }
    }
    
    return wrapMinDistanceResults(predecessors, distances, null);
  }
  
  public void checkEdgeAndAddNodeToQueue(
      Queue<Vertex<T>> queue,
      Edge<T> e,
      Double dV,
      Set<Vertex<T>> visited,
      Map<Vertex<T>, Double> distances,
      Map<Vertex<T>, Vertex<T>> predecessors,
      Function<Edge<T>, Double> distance) {
    Vertex<T> u = e.getDestination();
    Vertex<T> v = e.getSource();
    if (!visited.contains(u)) {
      double dU = dV + distance.apply(e);
      if (distances.getOrDefault(u, Double.POSITIVE_INFINITY) > dU) {
        distances.put(u,  dU);
        predecessors.put(u, v);
        queue.remove(u);
        queue.add(u);
      }        
    }
  }
  
  private Queue<Vertex<T>> initMinDistanceUtilities(
      int n,
      Vertex<T> source,
      Map<Vertex<T>, Vertex<T>> predecessors,
      Set<Vertex<T>> visited,
      Map<Vertex<T>, Double> distances,
      Function<Vertex<T>, Double> heuristic) {
    predecessors.clear();
    predecessors.put(source, null);
    visited.clear();
    distances.clear();
    distances.put(source, 0.0);
    //Use closures to provide an order for the priority queue
    Comparator<Vertex<T>> vertexOrder = new Comparator<Vertex<T>>() {
      @Override
      public int compare(Vertex<T> o1, Vertex<T> o2) {
        Double d1 = distances.getOrDefault(o1, Double.POSITIVE_INFINITY) + heuristic.apply(o1);
        Double d2 = distances.getOrDefault(o2, Double.POSITIVE_INFINITY) + heuristic.apply(o2);
        return d1.compareTo(d2);
      }
    };
    Queue<Vertex<T>> queue = new PriorityQueue<>(vertexOrder);
    queue.add(source);
    return queue;
  }
  
  protected MinDistanceResult<T> wrapMinDistanceResults(
      final Map<Vertex<T>, Vertex<T>> predecessors,
      final Map<Vertex<T>, Double> distances,
      final List<Vertex<T>> path) {
    return new MinDistanceResult<T>() {
      @Override
      public Map<Vertex<T>, Vertex<T>> predecessors() {
        return predecessors;
      }

      @Override
      public Map<Vertex<T>, Double> distances() {
        return distances;
      }

      @Override
      public List<Vertex<T>> path() {
        return path;
      }
    };
  }
  
  protected  MinDistanceResult<T> addPathToResult(
      Vertex<T> source, Vertex<T> target,  
      MinDistanceResult<T> result) {
    return new MinDistanceResult<T>() {
      final List<Vertex<T>> path = buildPath(source, target, result.predecessors());
      
      @Override
      public Map<Vertex<T>, Vertex<T>> predecessors() {
        return result.predecessors();
      }

      @Override
      public Map<Vertex<T>, Double> distances() {
        return result.distances();
      }

      @Override
      public List<Vertex<T>> path() {
        return path;
      }
    };
  }
  
  protected List<Vertex<T>> buildPath(
      Vertex<T> source,
      Vertex<T> target,
      Map<Vertex<T>, Vertex<T>> predecessors) {
    List<Vertex<T>> result = new ArrayList<Vertex<T>>(predecessors.size());    
    Vertex<T> current = target;
    while (current != null && !source.equals(current)) {
      result.add(current);
      current = predecessors.get(current);
    }
    if (source.equals(current)) {
      result.add(source);
      Collections.reverse(result);
    } else {
      result = null;
    }
    return result;
  }

  @Override
  public Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target)
      throws NullPointerException, IllegalArgumentException {
    return allAcyclicPaths(source, target, new ArrayList<>());
  }

  protected Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target, List<Vertex<T>> path) {  
    Set<List<Vertex<T>>> result = new HashSet<>();
    List<Vertex<T>> newPath = new ArrayList<>(path);
    newPath.add(source);
    Set<Vertex<T>> visited = new HashSet<>(newPath);
    if (!hasVertex(target.getLabel())) {
      throw new IllegalArgumentException("Target vertex doesn't belong to the graph");
    }

    if (source.equals(target)) {
      result.add(newPath);
    } else {
      for (Vertex<T> v: getNeighbours(source)) {
        if (!visited.contains(v)) {
          result.addAll(allAcyclicPaths(v, target, newPath));
        }
      }      
    }
    return result;
  }

}
