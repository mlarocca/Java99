package mlarocca.java99.trees;

public interface Tree<T extends Comparable<T>> {
  static final String NEGATIVE_NUMBER_OF_NODES = "The number of nodes must be non-negative";
  
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

}