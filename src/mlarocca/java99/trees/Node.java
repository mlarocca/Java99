package mlarocca.java99.trees;

public class Node<T extends Comparable<T>> implements Tree<T> {

  private T _key;
  private Tree<T> _left;
  private Tree<T> _right;
  
  public Node(T key) throws NullPointerException {
    if (key == null) {
      throw new NullPointerException("key");
    }
    _key = key;
  }

  public Node(T key, Tree<T> left, Tree<T> right) {
    if (key == null || left == null || right == null) {
      throw new NullPointerException("key | left | right");
    }
    _left = left;
    _right = right;
  }

  @Override
  public T key() {
    return _key;
  }

  @Override
  public Tree<T> left() {
    return _left;
  }

  @Override
  public Tree<T> right() throws UnsupportedOperationException {
    return _right;
  }
  
  @Override
  public boolean isLeaf() throws UnsupportedOperationException {
    return false;
  }

  @Override
  public boolean isNode() {
    return !isLeaf();
  }

  @Override
  public String toString() {
    return String.format("T(%s, %s, %s)", key().toString(), left().toString(), right().toString());
  }
}
