package mlarocca.java99.trees;

public final class Leaf<T extends Comparable<T>> implements Tree<T> {
  
  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public boolean isNode() {
    return !isLeaf();
  }

  @Override
  public T key() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public Tree<T> left() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public Tree<T> right() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("Leaves have no children");
  }

  @Override
  public String toString() {
    return ".";
  }
  
}
