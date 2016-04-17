package mlarocca.java99.trees;

import java.util.Stack;

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
}