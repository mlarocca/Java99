package mlarocca.java99.trees;

public class LayoutBinaryNode<T extends Comparable<? super T>> extends Node<T> implements LayoutBinaryTree<T> {
  private int _x;
  private int _y;
  
  public LayoutBinaryNode(T key) throws NullPointerException {
    super(key, new LayoutBinaryNil<T>(), new LayoutBinaryNil<T>());
    _x = _y = 0;
  }

  public LayoutBinaryNode(T key, LayoutBinaryTree<T> left, LayoutBinaryTree<T> right) throws NullPointerException {
    super(key, left, right);
    _x = _y = 0;
  }

  public LayoutBinaryNode(T key, LayoutBinaryTree<T> left, LayoutBinaryTree<T> right, int x, int y) throws NullPointerException {
    super(key, left, right);
    _x = x;
    _y = y;
  }

  @Override
  public LayoutBinaryTree<T> left() throws UnsupportedOperationException {
    return (LayoutBinaryTree<T>)super.left();
  }

  @Override
  public LayoutBinaryTree<T> right() throws UnsupportedOperationException {
    return (LayoutBinaryTree<T>)super.right();
  }

  @Override
  public int x() {
    return _x;
  }

  @Override
  public int y() {
    return _y;
  }

  @Override
  public int hashCode() {
    return String.format("%d[%s]%d,%d@(%d,%d)", key().hashCode(), this.getClass(), left().hashCode(), right().hashCode(), x(), y()).hashCode();
  }
  
  @Override
  public String toString() {
    return String.format("%s@[x=%d,y=%d]", super.toString(), x(), y());
  }

}
