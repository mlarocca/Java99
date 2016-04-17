package mlarocca.java99.trees;

public interface BinarySearchTree<T extends Comparable<? super T>> extends Tree<T> {
  public BinarySearchTree<T> addKey(T key);
  
  @Override
  public BinarySearchTree<T> left() throws UnsupportedOperationException;
  @Override
  public BinarySearchTree<T> right() throws UnsupportedOperationException;
  
  public static <R extends Comparable<? super R>> BinarySearchTree<R> fromList(Iterable<R> list) {
    BinarySearchTree<R> tree = new BinarySearchLeaf<R>();
    for (R key: list) {
      tree = tree.addKey(key);
    }
    return tree;
  }
}
