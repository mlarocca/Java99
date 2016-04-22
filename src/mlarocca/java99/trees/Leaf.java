package mlarocca.java99.trees;

class Leaf<T extends Comparable<? super T>> implements TreeInternal<T> {
  
  @Override
  public boolean isLeaf() {
    return true;
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
  public int size() {
    return 0;
  }

  @Override
  public int height() {
    return 0;
  }
  
  @Override
  public String toString() {
    return ".";
  }
  
  @Override
  public boolean equals(Object other) {
    return isEquals(other);
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
  
  @Override
  public boolean hasSymmetricStructure() {
    return true;
  }
}
