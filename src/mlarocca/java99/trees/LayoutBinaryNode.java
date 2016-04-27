package mlarocca.java99.trees;

public class LayoutBinaryNode<T extends Comparable<? super T>> extends Node<T> implements LayoutBinaryTree<T> {
  private int _x;
  private int _y;
  
  public LayoutBinaryNode(T key) throws NullPointerException {
    super(key, new BinarySearchNil<T>(), new BinarySearchNil<T>());
    _x = _y = 0;
  }

  private LayoutBinaryNode(T key, LayoutBinaryNode<T> left, LayoutBinaryNode<T> right) throws NullPointerException {
    super(key, left, right);
    _x = _y = 0;
  }

  private LayoutBinaryNode(T key, LayoutBinaryNode<T> left, LayoutBinaryNode<T> right, int x, int y) throws NullPointerException {
    super(key, left, right);
    _x = x;
    _y = y;
  }

  @Override
  public LayoutBinaryNode<T> left() throws UnsupportedOperationException {
    return (LayoutBinaryNode<T>)super.left();
  }

  @Override
  public LayoutBinaryNode<T> right() throws UnsupportedOperationException {
    return (LayoutBinaryNode<T>)super.right();
  }

  @Override
  public int x() {
    return _x;
  }

  @Override
  public int y() {
    return _y;
  }

}
