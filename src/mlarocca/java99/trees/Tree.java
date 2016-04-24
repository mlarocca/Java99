package mlarocca.java99.trees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface Tree<T extends Comparable<? super T>> {
  static final String NEGATIVE_NUMBER_OF_NODES = "The number of nodes must be non-negative";

  //STATIC METHODS
  
  /**
   * Return a complete balanced tree with n nodes, all containing the key passed.
   * A complete balanced tree is a tree where the number of nodes in the left subtree
   * and the number of nodes in the right subtree at most differ by 1.
   * For n==0 it returns a Leaf.
   * 
   * @param n The number of nodes that the tree needs to contain
   * @param key The key to be inserted in all the nodes.
   * @return A new complete balanced tree
   * @throws IllegalArgumentException If the number of nodes passed is negative.
   */
  public static <R extends Comparable<R>> Tree<R> cBalanced(int n, R key) throws IllegalArgumentException {
    if (n == 0) {
      return new Leaf<>();
    } else if (n == 1) {
      return new Node<>(key);
    } else if (n > 1) {
      int r = (n - 1) / 2;
      return new Node<>(key, cBalanced(n - 1 - r, key), cBalanced(r, key));
    } else {
      throw new IllegalArgumentException(NEGATIVE_NUMBER_OF_NODES);
    }
  }
    
  /**
   * Return all complete balanced trees with n nodes.
   * 
   * @param n The number of nodes that the tree needs to contain
   * @param key The key to be inserted in all the nodes.
   * @return A new complete balanced tree
   * @throws IllegalArgumentException If the number of nodes passed is negative.
   */
  public static <R extends Comparable<? super R>> Set<Tree<R>> allCompleteBalancedTrees(
      int n,
      R key) throws IllegalArgumentException {
    Set<Tree<R>> result;
    
    if (n > 1) {
      int m = (n - 1) / 2;
      result = TreeInternal.generateSubTrees(m, n - 1 - m, key, (nodes, k) -> allCompleteBalancedTrees(nodes, k));
    } else if (n == 1) {
      Tree<R> node = new Node<>(key);
      result = new HashSet<>();
      result.add(node);
    } else if (n == 0) {
      Tree<R> node = new Leaf<>();
      result = new HashSet<>();
      result.add(node);
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_NUMER_OF_NODES_MESSAGE);
    }
    return result;
  }
  
  /**
   * Returns all symmetric completely balanced trees with exactly n nodes.
   * 
   * @param n The number of nodes that the tree needs to contain
   * @param key The key to be inserted in all the nodes.
   * @return A new complete balanced tree
   * @throws IllegalArgumentException If the number of nodes passed is negative.
   */
  public static <R extends Comparable<? super R>> Set<Tree<R>> allSymmetricBalancedTrees(
      int n,
      R key) throws IllegalArgumentException {
    return allCompleteBalancedTrees(n, key)
      .stream()
      .filter(Tree::hasSymmetricStructure)
      .collect(Collectors.toSet());
  }

  /**
   * Returns all height balanced trees with a given height.
   * For height == 0, returns a leaf.
   * 
   * Minimum number of nodes for such trees:
   * | h == 0 -> 0
   * | h == 1 -> 1
   * | h  > 1 -> 1 + minNN(h-1) + minNN(h-2)
   * 
   * @param height The height that the tree needs to have.
   * @param key The key to be inserted in all the nodes.
   * @return A new complete balanced tree
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static <R extends Comparable<? super R>> Set<Tree<R>> allHeightBalancedTrees(
      int height,
      R key) throws IllegalArgumentException {
    final Set<Tree<R>> result = new HashSet<>();
    if (height > 1) {
      final List<Tree<R>> taller = new ArrayList<>(allHeightBalancedTrees(height - 1, key));
      final Set<Tree<R>> shorter = allHeightBalancedTrees(height - 2, key);
      IntStream.range(0, taller.size()).forEach(i -> {
        Tree<R> tt1 = taller.get(i);
        result.add(new Node<>(key, tt1, tt1));
        shorter.stream().forEach(ts -> {
          result.add(new Node<>(key, tt1, ts));
          result.add(new Node<>(key, ts, tt1));
        });
        IntStream.range(i + 1, taller.size()).forEach(j -> {
          Tree<R> tt2 = taller.get(j);
          result.add(new Node<>(key, tt2, tt1));
          result.add(new Node<>(key, tt1, tt2));    
        });
      });
    } else if (height == 1) {
      Tree<R> node = new Node<>(key);
      result.add(node);
    } else if (height == 0) {
      Tree<R> node = new Leaf<>();
      result.add(node);
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
    return result;
  }
  
  /**
   * Minimum number of nodes for height balanced trees:
   * | h == 0 -> 0
   * | h == 1 -> 1
   * | h  > 1 -> 1 + minNN(h-1) + minNN(h-2)
   * 
   * @param height The height that the tree needs to have.
   * @return The minimum number of nodes in a height balanced tree with given height.
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static int minHbalNodes(int height) throws IllegalArgumentException {
    if (height > 1) {
      return 1 + minHbalNodes(height - 1) + minHbalNodes(height - 2);
    } else if (height == 1) {
      return 1;
    } else if (height == 0) {
      return 0;
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
  }

  /**
   * 
   * @param height The height that the tree needs to have.
   * @return The maximum number of nodes in a height balanced tree with given height.
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static int maxHbalNodes(int height) throws IllegalArgumentException {
    if (height >= 0) {
      return (int) Math.pow(2, height) - 1;
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
  }

  /**
   * Minimum height for height balanced trees with n nodes.
   * 
   * @param n The height that the tree needs to have.
   * @return The minimum number of nodes in a height balanced tree with given height.
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static int minHbalHeight(int n) throws IllegalArgumentException {
    if (n >= 0) {
      return (int) Math.ceil(Math.log10(n + 1) / Math.log10(2));
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_NUMER_OF_NODES_MESSAGE);
    }
  }

  /**
   * Max height for a height balanced tree with n nodes:
   * | n == 0 -> 0
   * | n == 1 -> 1
   * | n  > 1 -> 1 + minNN(h-1) + minNN(h-2)
   * 
   * @param n The height that the tree needs to have.
   * @return The maximum number of nodes in a height balanced tree with given height.
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static int maxHbalHeight(int n) throws IllegalArgumentException {
    if (n > 1) {
      int m = n - 1;
      int h1 = maxHbalHeight(m / 2);
      int k = minHbalNodes(h1);
      return 1 + Math.min(h1 + 1, maxHbalHeight(m - k));
    } else if (n == 1) {
      return 1;
    } else if (n == 0) {
      return 0;
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_NUMER_OF_NODES_MESSAGE);
    }
  }  
  
  /**
   * Returns all height balanced trees with a given height.
   * For height == 0, returns a leaf.
   * 
   * Minimum number of nodes for such trees:
   * | h == 0 -> 0
   * | h == 1 -> 1
   * | h  > 1 -> 1 + minNN(h-1) + minNN(h-2)
   * 
   * @param n The height that the tree needs to have.
   * @param key The key to be inserted in all the nodes.
   * @return A new complete balanced tree
   * @throws IllegalArgumentException If the height passed is negative.
   */
  public static <R extends Comparable<? super R>> Set<Tree<R>> allHeightBalancedTreesWithNodes(
      int n,
      R key) throws IllegalArgumentException {
    Set<Tree<R>> result;
    if (n > 1) {
      result = IntStream.rangeClosed(minHbalHeight(n), maxHbalHeight(n))
        .mapToObj(h -> allHeightBalancedTrees(h, key))
        .flatMap(s -> s.stream())
        .filter(t -> t.size() == n)
        .collect(Collectors.toSet());
    } else if (n == 1) {
      result = new HashSet<>();
      Tree<R> node = new Node<>(key);
      result.add(node);
    } else if (n == 0) {
      result = new HashSet<>();
      Tree<R> node = new Leaf<>();
      result.add(node);
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
    return result;
  }

  // INSTANCE METHODS
  
  public boolean isLeaf();
  
  public default boolean isNode() {
    return !isLeaf();
  }

  public int size();
  public int height();
  
  default boolean isEquals(Object other) {
    return other != null && 
      other.getClass().equals(this.getClass()) &&
      other.hashCode() == hashCode();
  }

  public T key() throws UnsupportedOperationException;
  public Tree<T> left() throws UnsupportedOperationException;
  public Tree<T> right() throws UnsupportedOperationException;

  public boolean hasSymmetricStructure();
  
  public boolean isLeafNode();
  public int leavesCount();
}


interface TreeInternal<T extends Comparable<? super T>> extends Tree<T> {
  public static <R extends Comparable<? super R>> boolean haveMirrorStructure(
      Tree<R> t1,
      Tree<R> t2) {
    Stack<Tree<R>> stack1 = new Stack<>();
    Stack<Tree<R>> stack2 = new Stack<>();
    stack1.add(t1);
    stack2.add(t2);
    while (!stack1.isEmpty()) {
      Tree<R> tree1 = stack1.pop();
      Tree<R> tree2 = stack2.pop();
      if (tree1.isLeaf() != tree2.isLeaf()) {
        return false;
      }
      if (tree1.isNode()) {
        stack1.add(tree1.left());
        stack2.add(tree2.right());
        stack1.add(tree1.right());
        stack2.add(tree2.left());
      }
    }
    return true;
  }
  
  public static String NEGATIVE_NUMER_OF_NODES_MESSAGE = "Number of nodes must be non negative";
  public static String NEGATIVE_HEIGHT_MESSAGE = "Height must be non negative";
  
  public static Supplier<IllegalArgumentException> NEGATIVE_NUMER_OF_NODES = 
    new Supplier<IllegalArgumentException>() {
  
      @Override
      public IllegalArgumentException get() {
        return new IllegalArgumentException(NEGATIVE_NUMER_OF_NODES_MESSAGE);
      }
    };
  
  public static <R extends Comparable<? super R>> Set<Tree<R>> generateSubTrees(int left, int right, R key, BiFunction<Integer, R, Set<Tree<R>>> recursiveBuilder) {
    Tree<R> node;
    Set<Tree<R>> res = new HashSet<>();
    List<Tree<R>> subResultLeft;
    List<Tree<R>> subResultRight;
    if (left == right) {
      subResultLeft = subResultRight = new ArrayList<>(recursiveBuilder.apply(left, key));
    } else {
      subResultLeft = new ArrayList<>(recursiveBuilder.apply(left, key)); 
      subResultRight = new ArrayList<>(recursiveBuilder.apply(right, key));        
    }
    for (int i = 0, kL = subResultLeft.size(); i < kL; i++) {
      for (int j = 0, kR = subResultRight.size(); j < kR; j++) {
        node = new Node<>(key, subResultLeft.get(i), subResultRight.get(j));
        res.add(node);
        if (!subResultLeft.get(i).equals(subResultRight.get(j))) {
          node = new Node<>(key, subResultRight.get(j), subResultLeft.get(i));
          res.add(node);
        }
      }
    }
    return res;
  };
}