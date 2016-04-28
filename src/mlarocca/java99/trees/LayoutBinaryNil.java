package mlarocca.java99.trees;

class LayoutBinaryNil<T extends Comparable<? super T>> extends Nil<T> implements LayoutBinaryTree<T> {

  @Override
  public LayoutBinaryTree<T> left() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public LayoutBinaryTree<T> right() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public int x() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Nil Marker has no position");
  }

  @Override
  public int y() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Nil Marker has no position");
  }
}
