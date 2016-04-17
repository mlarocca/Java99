package mlarocca.java99.trees;

import java.util.Arrays;

public class Node<T extends Comparable<? super T>> implements TreeInternal<T> {

  private T _key;
  private Tree<T> _left;
  private Tree<T> _right;
  
  public Node(T key) throws NullPointerException {
    if (key == null) {
      throw new NullPointerException("key");
    }
    _key = key;
    _left = new Leaf<>();
    _right = new Leaf<>();
  }

  public Node(T key, Tree<T> left, Tree<T> right) {
    this(key);
    if (left == null || right == null) {
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
  public String toString() {
    return String.format("T(%s, %s, %s)", key().toString(), left().toString(), right().toString());
  }
  
  @Override
  public boolean equals(Object other) {
    return isEquals(other);
  }

  @Override
  public int hashCode() {
    return Arrays.asList(key().hashCode(), left().hashCode(), right().hashCode()).hashCode();
  }

  @Override
  public boolean hasSymmetricStructure() {
    return TreeInternal.haveMirrorStructure(left(), right());
  }
}
