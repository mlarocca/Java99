package mlarocca.java99.graphs;

import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mlarocca.java99.cache.utils.Wrapper;
import mlarocca.java99.graphs.data.MinDistanceResult;
import mlarocca.java99.graphs.data.StructureResult;

public class SimpleGraph<T> implements GraphInternal<T> {

  private static final String UNDIRECTED_EDGE_SYMBOL = "-";
  private static final String DIRECTED_EDGE_SYMBOL = ">";
  
  private static final String VERTEX_REGEX = "[^\\s-><\\[\\]/]+";
  private static final String NUMBER_REGEX = "\\s*(\\d+(?:\\.\\d+)?)\\s*";
  private static final Pattern DIRECTED_EDGE_PATTERN = Pattern.compile("^(" + VERTEX_REGEX + ")\\s*>\\s*(" + VERTEX_REGEX + ")(?:\\s*/" + NUMBER_REGEX + ")?$");
  private static final Pattern UNDIRECTED_EDGE_PATTERN = Pattern.compile("^(" + VERTEX_REGEX + ")\\s*-\\s*(" + VERTEX_REGEX + ")(?:\\s*/" + NUMBER_REGEX + ")?$");
  private static final Pattern VERTEX_PATTERN = Pattern.compile("^" + VERTEX_REGEX + "$");
  
  private static Supplier<IllegalArgumentException> VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER = 
    new Supplier<IllegalArgumentException>() {
  
      @Override
      public IllegalArgumentException get() {
        return new IllegalArgumentException("Vertex not in graph");
      }
      
    };
    
  /**
   * Compare vertices based on their degree (v1 < v2 <=> d1 > d2)
   */
  private Comparator<Vertex<T>> VERTEX_COMPARATOR_BY_DEGREE = new Comparator<Vertex<T>>() {
    @Override
    public int compare(Vertex<T> v1, Vertex<T> v2) {
      return (inDegree(v2) + outDegree(v2)) - (inDegree(v1) + outDegree(v1));
    }
  };
  
  private static <T> boolean addVertexFromString(String vertexStr, Graph<T> graph) {
    Matcher vertexMatcher = VERTEX_PATTERN.matcher(vertexStr);
    if (vertexMatcher.matches()) {
      return graph.getOrAddVertex(vertexStr) != null;
    }
    return false;
  }
  
  private static <T> boolean addEdgeFromMatcher(Matcher matcher, boolean undirectedEdge, Graph<T> graph) {
    String v1Label = matcher.group(1);
    String v2Label = matcher.group(2);
    Vertex<T> v1 = graph.getOrAddVertex(v1Label);
    Vertex<T> v2 = graph.getOrAddVertex(v2Label);
    boolean updated;
    Double weight;
    
    try {
      weight = new Double(matcher.group(3));
    } catch (NullPointerException npe) {
      weight = null;
    } catch (NumberFormatException nfe) {
      return false;
    }
    
    if (weight != null) {
      updated = graph.addEdge(v1, v2, weight) != null;
      if (undirectedEdge) {
        updated = (graph.addEdge(v2, v1, weight) != null) && updated;
      }
    } else {
      updated = graph.addEdge(v1, v2) != null;
      if (undirectedEdge) {
        updated = (graph.addEdge(v2, v1) != null) && updated;
      }

    }
    return updated;
  }

  private static <T> boolean addEdgeFromString(String edgeStr, Graph<T> graph) {
    Matcher directedEdgeMatcher = DIRECTED_EDGE_PATTERN.matcher(edgeStr);
    Matcher undirectedEdgeMatcher = UNDIRECTED_EDGE_PATTERN.matcher(edgeStr);
    if (directedEdgeMatcher.matches()) {
      return addEdgeFromMatcher(directedEdgeMatcher, false, graph);
    } else if (undirectedEdgeMatcher.matches()) {
      return addEdgeFromMatcher(undirectedEdgeMatcher, true, graph);  
    } else {
      return false;
    }
    
  }
  
  public static <T> Graph<T> fromString(String str) throws IllegalArgumentException {
    Graph<T> graph = new SimpleGraph<>();
    str = str.trim();
    if (!str.startsWith("[") || !str.endsWith("]")) {
      throw new IllegalArgumentException("Parse error: Graph must be enclosed in square brackets");
    }
    
    String[] parts = str.substring(1, str.length() - 1).split(",");
    
    for (String element : parts) {
      element = element.trim();
      if (!addVertexFromString(element, graph) && !addEdgeFromString(element, graph)) {
        throw new IllegalArgumentException(String.format("Parse error: %s can be neither parsed to a valid vertex nor to a valid edge", element));
      }
    }
    
    return graph;
  }
  
  @SuppressWarnings("unused")
  private Comparator<Edge<T>> ByWeightEdgeComparator = new Comparator<Edge<T>>() {
    @Override
    public int compare(Edge<T> o1, Edge<T> o2) {
      return ((Double)o1.getWeight()).compareTo((Double)o2.getWeight());
    }
  };
  
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
  public int size() {
    return getVertices().size();
  }

  @Override
  public int edgesSize() {
    return getEdges().size();
  }
  
  @Override
  public List<Edge<T>> getEdgesFrom(Vertex<T> v) throws IllegalArgumentException {
    if (!adjList.containsKey(v)) {
      throw VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER.get();
    }
    return adjList.get(v);
  }

  @Override
  public List<Edge<T>> getEdgesTo(Vertex<T> v) throws IllegalArgumentException {
    if (!adjList.containsKey(v)) {
      throw VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER.get();
    }
    
    List<Edge<T>> edgesToV = getEdges()
      .stream()
      .filter(e -> e.getDestination().equals(v))
      .collect(Collectors.toList());
    Collections.sort(edgesToV);
    return edgesToV;
  }

  @Override
  public Optional<Edge<T>> getEdgeBetween(Vertex<T> v, Vertex<T> u)
      throws NullPointerException, IllegalArgumentException {
    if (!hasVertex(v) || !hasVertex(u)) {
      throw new IllegalArgumentException();
    }
    return getEdgesFrom(v).stream()
      .filter(e -> e.getDestination().equals(u))
      .findFirst();
  }
  
  @Override
  public int inDegree(Vertex<T> v) throws NullPointerException, IllegalArgumentException {
    return getEdgesTo(v).size();
  }
  
  @Override
  public int inDegree(String label) throws NullPointerException, IllegalArgumentException {
    return getVertex(label).map(v -> inDegree(v)).orElseThrow(VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER);
  }

  @Override
  public int outDegree(Vertex<T> v) throws NullPointerException, IllegalArgumentException {
    return getEdgesFrom(v).size();
  }
  
  @Override
  public int outDegree(String label) throws NullPointerException, IllegalArgumentException {
    return getVertex(label).map(v -> outDegree(v)).orElseThrow(VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER);
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
  public synchronized Vertex<T> addVertex(Vertex<T> v) throws IllegalArgumentException {
    return v.getValue()
      .map(value -> addVertex(v.getLabel(), value))
      .orElseGet(new Supplier<Vertex<T>>() {

        @Override
        public Vertex<T> get() {
          return addVertex(v.getLabel());
        }
      });
  }

  @Override
  public Vertex<T> getOrAddVertex(String label) {
    return getOrAddVertex(label, Optional.empty());
  }

  @Override
  public Vertex<T> getOrAddVertex(String label, Optional<T> maybeValue) {
    return getVertex(label).orElseGet(new Supplier<Vertex<T>>() {
      @Override
      public Vertex<T> get() {
        return maybeValue
          .map(value -> addVertex(label, value))
          .orElse(addVertex(label));
      }
    });
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
  public boolean hasVertex(Vertex<T> v) {
    return hasVertex(v.getLabel());
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
          //Vertex with no in-going or out-going edges
          result.add(v.getLabel());
        } else {
          outgoing.forEach(e -> {
            Vertex<T> u = e.getDestination();
            Vertex<T> src;
            Vertex<T> dst;
            String edge;
            String weight = e.getWeight() != 0.0 ? "/" + e.getWeight() : "";
            
            if (neighbours.get(u).contains(v) && 
                e.getWeight() == getEdgeBetween(u, v).get().getWeight()) {
              edge = UNDIRECTED_EDGE_SYMBOL;
              if (u.compareTo(v) < 0) {
                src = u;
                dst = v;
              } else {
                src = v;
                dst = u;
              }
            } else {
              edge = DIRECTED_EDGE_SYMBOL;
              src = v;
              dst = u;
            }
            
            result.add(String.format("%s %s %s%s", src, edge, dst, weight));
          });
        }
        return result;
      })
      .reduce((set, subset) -> {
        set.addAll(subset);
        return set;
      }).orElse(new HashSet<>());
    return String.format("[%s]", String.join(", ", edges));
  }

  /**
   * 
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public StructureResult<T> dfs() {
    Map<Vertex<T>, Integer> entryTimes = new HashMap<>();
    Map<Vertex<T>, Integer> exitTimes = new HashMap<>();
    int time = 0;
    Wrapper<Boolean> isAcyclic = new Wrapper<>(true);
    for (Vertex<T> v: getVertices()) {
      if (!entryTimes.containsKey(v)) {
        entryTimes.put(v, time);
        time = 1 + dfs(v, entryTimes, exitTimes, isAcyclic);
      }
    }
    
    return new StructureResult<T>() {
      @Override
      public Boolean isAcyclic() {
        return isAcyclic.getValue();
      }

      @Override
      public Map<Vertex<T>, Integer> exitTimes() {
        return exitTimes;
      }
    };
  }
  
  /**
   * 
   * @param source
   * @return
   * @throws NullPointerException
   * @throws IllegalArgumentException
   */
  @Override
  public StructureResult<T> dfs(Vertex<T> source) throws NullPointerException, IllegalArgumentException {
    Map<Vertex<T>, Integer> entryTimes = new HashMap<>();
    Map<Vertex<T>, Integer> exitTimes = new HashMap<>();
    final Wrapper<Boolean> isAcyclic = new Wrapper<>(true);
    entryTimes.put(source,  0);
    dfs(source, entryTimes, exitTimes, isAcyclic);
    return new StructureResult<T>() {
      @Override
      public Boolean isAcyclic() {
        return isAcyclic.getValue();
      }

      @Override
      public Map<Vertex<T>, Integer> exitTimes() {
        return exitTimes;
      }
    };
  }
  
  protected int dfs(Vertex<T> u, Map<Vertex<T>, Integer> entryTimes, Map<Vertex<T>, Integer> exitTimes, Wrapper<Boolean> isAcyclic) {
    int time = entryTimes.get(u);
    
    for (Vertex<T> v: getNeighbours(u)) {
      if (!entryTimes.containsKey(v)) {
        entryTimes.put(v, time);
        time = dfs(v, entryTimes, exitTimes, isAcyclic);        
      } else if (!v.equals(u) && !exitTimes.containsKey(v)) {
        isAcyclic.setValue(false);
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
  
  public List<Vertex<T>> topologicalOrder() {
    StructureResult<T> result = dfs();
    Map<Vertex<T>, Integer> exitTimes = result.exitTimes();
    
    Comparator<Vertex<T>> topologicalComparator = new Comparator<Vertex<T>>() {
      @Override
      public int compare(Vertex<T> o1, Vertex<T> o2) {
        //Descending order
        return exitTimes.get(o2).compareTo(exitTimes.get(o1));
      }
    };
    
    return getVertices()
      .stream()
      .sorted(topologicalComparator)
      .collect(Collectors.toList());
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
  
  private void checkEdgeAndAddNodeToQueue(
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
  
  private MinDistanceResult<T> wrapMinDistanceResults(
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
  
  private  MinDistanceResult<T> addPathToResult(
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
  
  private List<Vertex<T>> buildPath(
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
  public Graph<T> prim() throws UnsupportedOperationException {
    if (!isUndirected()) {
      throw new UnsupportedOperationException("Prim is defined for undirected graphs only");
    }
    
    Graph<T> mst = new SimpleGraph<T>();
    List<Vertex<T>> topOrder = topologicalOrder();
    if (topOrder.isEmpty() || !this.isConnected()) {
      return null;
    }
    Vertex<T> root = topOrder.get(0);
    
    Map<Vertex<T>, Double> distances = new HashMap<>(getVertices().size());    
    Map<Vertex<T>, Edge<T>> edgeTo = new HashMap<>(getVertices().size());    
    Comparator<Vertex<T>> PrimComparator = new Comparator<Vertex<T>>() {
      @Override
      public int compare(Vertex<T> v1, Vertex<T> v2) {
        Double d1 = distances.getOrDefault(v1, Double.POSITIVE_INFINITY);
        Double d2 = distances.getOrDefault(v2, Double.POSITIVE_INFINITY);
            
        return d1.compareTo(d2);
      }
    };
    distances.put(root, 0.0);
    
    Queue<Vertex<T>> queue = new PriorityQueue<>(PrimComparator);
    queue.add(root);
    while (!queue.isEmpty()) {
      Vertex<T> v = queue.remove();
      if (!mst.hasVertex(v)) {
        mst.addVertex(v);
        if (!v.equals(root)) {
          mst.addEdge(edgeTo.get(v));
        }
        
        getEdgesFrom(v)
          .stream()
          .forEach(e -> {
            Vertex<T> u = e.getDestination();
            if (distances.getOrDefault(u, Double.POSITIVE_INFINITY) > e.getWeight()) {
              distances.put(u, e.getWeight());
              edgeTo.put(u, e);
              queue.remove(u);  //This is linear in the size of the queue :/ No other way to update it!
              queue.add(u);
            }
          });
      }
    }
    return mst;
  }

  @Override
  public Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target)
      throws NullPointerException, IllegalArgumentException {
    if (!hasVertex(target.getLabel())) {
      throw new IllegalArgumentException("Target vertex doesn't belong to the graph");
    }
    return allAcyclicPaths(source, target, new ArrayList<>());
  }

  protected Set<List<Vertex<T>>> allAcyclicPaths(Vertex<T> source, Vertex<T> target, List<Vertex<T>> path) {  
    Set<List<Vertex<T>>> result = new HashSet<>();
    List<Vertex<T>> newPath = new ArrayList<>(path);
    newPath.add(source);
    Set<Vertex<T>> visited = new HashSet<>(newPath);

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

  @Override
  public Set<List<Vertex<T>>> allCycles(Vertex<T> source)
      throws NullPointerException, IllegalArgumentException {
    if (!hasVertex(source.getLabel())) {
      throw new IllegalArgumentException("Source vertex doesn't belong to the graph");
    }
    return allCycles(source, source, Arrays.asList(source));
  }

  protected Set<List<Vertex<T>>> allCycles(Vertex<T> source, Vertex<T> v, List<Vertex<T>> path)
      throws NullPointerException, IllegalArgumentException {
    Set<Vertex<T>> visited = new HashSet<>(path);
    Set<List<Vertex<T>>> results = new HashSet<>();
    
    for (Vertex<T> u: getNeighbours(v)) {
      if (u.equals(source)) {
        List<Vertex<T>> newPath = new ArrayList<>(path);
        newPath.add(u);
        results.add(newPath);
      } else if (!visited.contains(u)) {
        List<Vertex<T>> newPath = new ArrayList<>(path);
        newPath.add(u);
        results.addAll(allCycles(source, u, newPath));
      }
    }
    return results;
  }
  
  public Set<Graph<T>> allSpanningTrees() {
    return allSpanningTrees(getVertices().size(), new HashSet<>(getEdges()), new HashSet<>());
  }
  
  protected Set<Graph<T>> allSpanningTrees(
      int verticesNumber,
      Set<Edge<T>> maybeTree, 
      Set<Set<Edge<T>>> testedSets) {
    
    testedSets.add(maybeTree);

    Set<Graph<T>> spanningTrees = new HashSet<>();
    List<Vertex<T>> vertices = getVertices();
    if (maybeTree.size() >= verticesNumber - 1) {
      Graph<T> graph = new SimpleGraph<T>();
      vertices.stream()
        .forEach(v -> graph.addVertex(v));
      maybeTree.stream()
        .forEach(edge -> {
          graph.addEdge(edge);
        });
      if (graph.isTree()) {
        spanningTrees.add(graph);
        //A spanning tree has exactly |V| - 1 edges, so removing more edges from this tree will result in no spanning tree
      } else if (graph.isConnected()) {
        maybeTree.forEach(e -> {
          Set<Edge<T>> edgeSet = new HashSet<>(maybeTree);
          edgeSet.remove(e);
          if (!testedSets.contains(edgeSet)) {
            spanningTrees.addAll(allSpanningTrees(verticesNumber, edgeSet, testedSets));          
          }
        });
      }
      //If the graph isn't connected, then removing more edges won't make it a tree...
      
    }
    return spanningTrees;
  }

  @Override
  public boolean isConnected() {
    boolean connected;
    List<Vertex<T>> topologicallyOrderedVertices = topologicalOrder();
    if (topologicallyOrderedVertices.isEmpty()) {
      connected = false;
    } else {
      StructureResult<T> result = dfs(topologicallyOrderedVertices.get(0));
      connected = result.exitTimes().keySet().containsAll(getVertices());
    }
    return connected;
  }

  @Override
  public boolean isAcyclic() {
    return dfs().isAcyclic();
  }

  /**
   * An undirected graph is a tree iff it is acyclic and connected.
   * A directed graph needs also to have exactly 1 less edge than the number of vertices,
   * cause otherwise could have a subgraph like [a > b, c > a, c > b].
   */
  @Override
  public boolean isTree() {
    return isAcyclic() && isConnected() && (getEdges().size() == getVertices().size() - 1);
  }
  
  @Override
  public int hashCode() {
    List<Integer> hashCodes = Stream.concat(
      getVertices().parallelStream().map(Vertex<T>::hashCode),
      getEdges().parallelStream().map(Edge<T>::hashCode))
        .collect(Collectors.toList());
    return hashCodes.hashCode();
  }

  @Override
  public boolean equals(Object otherGraph) {
    return otherGraph != null && 
      otherGraph.getClass().equals(this.getClass()) &&
      otherGraph.hashCode() == this.hashCode();  
  }
  
  private static class Degree implements Comparable<Degree> {
    private int _inDegree;
    private int _outDegree;
    
    public Degree(int inDegree, int outDegree) {
      _inDegree = inDegree;
      _outDegree = outDegree;
    }
    
    @SuppressWarnings("unused")
    public int inDegree() {
      return _inDegree;
    }
    
    @SuppressWarnings("unused")
    public int outDegree() {
      return _outDegree;
    }
    
    @Override
    public int hashCode() {
      return Arrays.asList(_inDegree, _outDegree).hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
      return (other != null && 
          other.getClass() == this.getClass() && 
          other.hashCode() == this.hashCode());
    }

    @Override
    public int compareTo(Degree other) {
      return this.hashCode() - other.hashCode();
    }
  }
  
  private static <R> Map<Degree, List<Vertex<R>>> groupVerticesByDegree(Graph<R> graph) {
    Function<Vertex<R>, Degree> vertexToDegree = v -> {
      int inDegree = graph.getEdgesTo(v).size();
      int outDegree = graph.getEdgesFrom(v).size();
      return new Degree(inDegree, outDegree);
    };
    
    return graph.getVertices()
      .stream()
      .collect(Collectors.groupingBy(vertexToDegree));
  }

  private static <R, S> boolean checkGroupedVerticesIsomorphicCompatibility(
      Map<Degree, List<Vertex<R>>> groupedVertices1,
      Map<Degree, List<Vertex<S>>> groupedVertices2) {
    return groupedVertices1.keySet()
      .stream()
      .allMatch(degree1 -> {
        return groupedVertices2.containsKey(degree1) && 
          groupedVertices1.get(degree1).size() == groupedVertices2.get(degree1).size();
     });
  }
  
  /**
   * For a given vertex relabeling v2 -> v1,  in a given isomorphism relabeling,
   * checks that the assignment is legal, i.e. that for every outgoing edge from v2
   * to v in graph2, there is an outgoing edge from v1 to v' in graph1, such that
   * v -> v' is compatible with current relabeling.
   * 
   * @param graph1 The first graph to be checked for isomorphism.
   * @param graph2 Second graph to check.
   * @param v1 New graph1's vertex added to the relabeling.
   * @param v2 New graph2's vertex added to the relabeling.
   * @param relabeling Current relabeling rules, including v2 -> v1.
   * @return True, iff all the conditions above are met.
   */
  private static <R, S> boolean  checkRelabelingAssignment(
      Graph<R> graph1,
      Graph<S> graph2,
      Vertex<R> v1,
      Vertex<S> v2,
      Map<Vertex<S>, Vertex<R>> relabeling) {
     
    Set<Vertex<R>> outNeighours1 = new HashSet<>(graph1.getNeighbours(v1));
    Set<Vertex<S>> outNeighours2 = new HashSet<>(graph2.getNeighbours(v2));
    boolean match = outNeighours2.stream().allMatch(v -> {
      return !relabeling.containsKey(v) || outNeighours1.contains(relabeling.get(v));
    });
    if (match) {
      Set<Vertex<R>> inNeighours1 = graph1.getEdgesTo(v1).stream().map(Edge::getSource).collect(Collectors.toSet());
      Set<Vertex<S>> inNeighours2 = graph2.getEdgesTo(v2).stream().map(Edge::getSource).collect(Collectors.toSet());
      match = inNeighours2.stream().allMatch(v -> {
        return !relabeling.containsKey(v) || inNeighours1.contains(relabeling.get(v));
      });
    }
    
    return match;
  }
  
  private static <R, S> boolean checkAllPossibleRelabeling(
      Graph<R> graph1,
      Graph<S> graph2,
      Map<Degree, List<Vertex<R>>> groupedVertices1,
      Map<Degree, List<Vertex<S>>> groupedVertices2,
      Map<Vertex<S>, Vertex<R>> relabeling,
      Queue<Degree> degreesLeft) {
    
    if (degreesLeft.isEmpty()) {
      return true;
    }
    
    Degree next = degreesLeft.remove();
    Set<Vertex<R>> g1VerticesAvailable = new HashSet<>(groupedVertices1.get(next));
    g1VerticesAvailable.removeAll(relabeling.values());
    
    return groupedVertices2.get(next).stream().anyMatch(v2 -> {
      return g1VerticesAvailable.stream().anyMatch(v1 -> {
        Map<Vertex<S>, Vertex<R>> relabelingRecursive = new HashMap<>(relabeling);
        relabelingRecursive.put(v2, v1);
        if (checkRelabelingAssignment(graph1, graph2, v1, v2, relabelingRecursive)) {
          return checkAllPossibleRelabeling(graph1, graph2, groupedVertices1, groupedVertices2, relabelingRecursive, degreesLeft);
        } else {
          return false;
        }
      });
    }); 
  }
  
  
  @Override
  public <S> boolean isIsomorphicTo(Graph<S> other) {
    List<Vertex<S>> otherVertices = other.getVertices();

    //Two graphs can be isomorphic only if:
    
    //#1. They have the same number of vertices and edges
    if (this.getVertices().size() != otherVertices.size() ||
        this.getEdges().size() != other.getEdges().size()) {
      return false;
    }
    
    Map<Degree, List<Vertex<T>>> groupedVertices1 = groupVerticesByDegree(this);
    Map<Degree, List<Vertex<S>>> groupedVertices2 = groupVerticesByDegree(other);
    
    //#2. A vertex can only be renamed into one with the same degree, so there must be the same number of vertices with a given (in,out)-degree in both graphs.
    if (!checkGroupedVerticesIsomorphicCompatibility(groupedVertices1, groupedVertices2)) {
      return false;
    }
    
    //#3. Try out all possible renaming from vertices in the same group
    //    (i.e. same degree) until it finds one
    return checkAllPossibleRelabeling(this, other, groupedVertices1, groupedVertices2, new HashMap<Vertex<S>, Vertex<T>>(), new PriorityQueue<Degree>(groupedVertices1.keySet()));
  }

  @Override
  public List<Vertex<T>> verticesByDegree() {
    List<Vertex<T>> vertices = new ArrayList<Vertex<T>>(getVertices());
    vertices.sort(VERTEX_COMPARATOR_BY_DEGREE);
    return vertices;
  }

  @Override
  public List<Vertex<T>> verticesByDepthFrom(String label) throws NullPointerException, IllegalArgumentException {
    return getVertex(label).map(v -> verticesByDepthFrom(v)).orElseThrow(VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER);
  }

  @Override
  public List<Vertex<T>> verticesByDepthFrom(Vertex<T> v)  throws NullPointerException, IllegalArgumentException {
    if (!hasVertex(v)) {
      throw VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER.get();
    }

    List<Vertex<T>> reversePath = verticesByDepthFrom(v, new HashSet<Vertex<T>>(),  new ArrayList<Vertex<T>>());
    Collections.reverse(reversePath);
    return reversePath;
  }
  
  private List<Vertex<T>> verticesByDepthFrom(Vertex<T> v, Set<Vertex<T>> visited, List<Vertex<T>> path) {
    visited.add(v);
    path.add(v);
    for (Vertex<T> u : getNeighbours(v)) {
      if (!visited.contains(u)) {
        verticesByDepthFrom(u, visited, path);
      }
    }
    return path;
  }

  /**
   * Check if a graph is directed or undirected.
   * We use the same representation for both, with undirected edges represented as
   * a pair of directed edges.
   * 
   * @return True iff the graph in undirected, i.e. for every edge u -> v, there is an
   *         edge v -> u with the same weight. 
   */
  @Override
  public boolean isUndirected() {
    //For all vertices...
    return getVertices().stream().allMatch(v -> {
      final Set<Vertex<T>> vNeighbours = new HashSet<>(getNeighbours(v));

      //For all in-going edges...
      return getEdgesTo(v).stream()
        .allMatch(e -> {
          Vertex<T> u = e.getSource();
          //There is an outgoing edge to the same vertex, with the same weight
          return v.equals(u) || 
              (vNeighbours.contains(u) &&
              getEdgeBetween(v, u).get().getWeight() == e.getWeight());
        });
    });
  }

  @Override
  public Graph<T> subGraph(ListVertexRef<T> subset) throws IllegalArgumentException, NullPointerException {
    return subGraph(subset.get().stream().map(Vertex::getLabel).collect(Collectors.toSet()));
  }
  
  @Override
  public Graph<T> subGraph(SetVertexRef<T> subset) throws IllegalArgumentException, NullPointerException {
    return subGraph(subset.get().stream().map(Vertex::getLabel).collect(Collectors.toSet()));
  }
  
  @Override
  public Graph<T> subGraph(List<String> subset) throws IllegalArgumentException, NullPointerException {
    return subGraph(new HashSet<String>(subset));
  }
  
  @Override
  public Graph<T> subGraph(Set<String> subset) throws IllegalArgumentException, NullPointerException {
    Graph<T> newGraph = new SimpleGraph<T>();
    
    subset.stream().forEach(label -> {
      Vertex<T> v = getVertex(label).orElseThrow(VERTEX_NOT_IN_GRAPH_EXCEPTION_SUPPLIER);
      Vertex<T> newV = newGraph.getOrAddVertex(label, v.getValue());
      
      getEdgesFrom(v).stream().forEach(e -> {
        Vertex<T> u = e.getDestination();
        if (subset.contains(u.getLabel())) {
          Vertex<T> newU = newGraph.getOrAddVertex(u.getLabel(), u.getValue());
          newGraph.addEdge(newV, newU, e.getWeight());
        }
      });
    });
    return newGraph;
  }
  
  @Override
  public Graph<T> inverse() {
    Graph<T> inverseGraph = new SimpleGraph<T>();
    getVertices().forEach(v -> inverseGraph.addVertex(v));
    getEdges().forEach(e -> inverseGraph.addEdge(e.inverse()));
    return inverseGraph;
  }

  /** 
   * Common code for finding connected components (directed graphs) or strongly
   * connected components (undirected graphs).
   * The difference is that, in the former case any order will do for the vertices,
   * in the latter, vertices must be in topological order for the inverse graph.
   * 
   * @param sortedVertices
   * @return A set of graphs: the connected/strongly connected components for this graph.
   */
  private Set<Graph<T>> findComponents(List<Vertex<T>> sortedVertices) {
    Set<Vertex<T>> availableVertices = new HashSet<>(sortedVertices);
    Set<Graph<T>> result = new HashSet<>();
    
    for (Vertex<T> v : getVertices()) {
      if (availableVertices.contains(v)) {
        Set<Vertex<T>> connectedComponent = dfs(v).exitTimes().keySet();
        result.add(subGraph(() -> connectedComponent));
        availableVertices.removeAll(connectedComponent);
      }
    }
    
    return result;
  }
  
  @Override
  public Set<Graph<T>> connectedComponents() throws UnsupportedOperationException {
    if (!isUndirected()) {
      throw new UnsupportedOperationException("For directed graphs, check Strongly Connected Components");
    }
    
    return findComponents(getVertices());
  }

  @Override
  public Set<Graph<T>> stronglyConnectedComponents() {
    if (isUndirected()) {
      //Connected and Strongly connected components are the same in an undirected graph
      //But it's a lot more efficient 
      return connectedComponents();
    }
    return findComponents(inverse().topologicalOrder());
  }
  
}
