package mlarocca.java99.trees;

public interface Tree<T extends Comparable<T>> {
  public boolean isLeaf();
  public boolean isNode();
  
  public T key() throws UnsupportedOperationException;
  public Tree<T> left() throws UnsupportedOperationException;
  public Tree<T> right() throws UnsupportedOperationException;
}
