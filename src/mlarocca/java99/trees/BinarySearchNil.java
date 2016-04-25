package mlarocca.java99.trees;

class BinarySearchNil<T extends Comparable<? super T>> extends Nil<T> implements BinarySearchTree<T> {

  @Override
  public BinarySearchTree<T> addKey(T key) {
    return new BinarySearchNode<T>(key);
  }

  @Override
  public BinarySearchTree<T> left() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public BinarySearchTree<T> right() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }
}
