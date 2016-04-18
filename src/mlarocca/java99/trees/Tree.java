package mlarocca.java99.trees;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Tree<T extends Comparable<? super T>> {
  static final String NEGATIVE_NUMBER_OF_NODES = "The number of nodes must be non-negative";

  /**
   * Return a complete balanced tree with n nodes, all containing the key passed.
   * A complete balanced tree is a tree where the number of nodes in the left subtree
   * and the number of nodes in the right subtree at most differ by 1.
   * 
   * @param n The number of nodes that the tree needs to contain
   * @return A new complete balanced tree
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
    
  public static <R extends Comparable<? super R>> Set<Tree<R>> allCompletelyBalanced(
      int n,
      R key) throws IllegalArgumentException {
    Set<Tree<R>> result;
    BiFunction<Integer, Integer, Set<Tree<R>>> generateSubTrees = (left, right) -> {
      Tree<R> node;
      Set<Tree<R>> res = new HashSet<>();
      List<Tree<R>> subResultLeft;
      List<Tree<R>> subResultRight;
      if (left == right) {
        subResultLeft = subResultRight = new ArrayList<>(Tree.allCompletelyBalanced(left, key));
      } else {
        subResultLeft = new ArrayList<>(Tree.allCompletelyBalanced(left, key)); 
        subResultRight = new ArrayList<>(Tree.allCompletelyBalanced(right, key));        
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
    
    if (n > 1) {
      int m = (n - 1) / 2;
      result = generateSubTrees.apply(m, n - 1 - m);
    } else if (n == 1) {
      Tree<R> node = new Node<>(key);
      result = new HashSet<>();
      result.add(node);
    } else if (n == 0) {
      Tree<R> node = new Node<>(key);
      result = new HashSet<>();
      node = new Leaf<>(); 
      result.add(node);
    } else {  // n < 0
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_NUMBER_OF_NODES);
    }
    return result;
  }
  
  public boolean isLeaf();
  
  public default boolean isNode() {
    return !isLeaf();
  }

  default boolean isEquals(Object other) {
    return other != null && 
      other.getClass().equals(this.getClass()) &&
      other.hashCode() == hashCode();
  }

  public T key() throws UnsupportedOperationException;
  public Tree<T> left() throws UnsupportedOperationException;
  public Tree<T> right() throws UnsupportedOperationException;

  public boolean hasSymmetricStructure();
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
  
  public static Supplier<IllegalArgumentException> NEGATIVE_NUMER_OF_NODES = 
    new Supplier<IllegalArgumentException>() {
  
      @Override
      public IllegalArgumentException get() {
        return new IllegalArgumentException("Number of nodes  must be non negative");
      }
      
    };
}