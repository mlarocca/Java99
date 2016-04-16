package mlarocca.java99.trees;

public interface BinarySearchTree<T extends Comparable<T>> extends Tree<T> {
  public BinarySearchTree<T> addKey(T key);
  
  @Override
  public BinarySearchTree<T> left() throws UnsupportedOperationException;
  @Override
  public BinarySearchTree<T> right() throws UnsupportedOperationException;
}
