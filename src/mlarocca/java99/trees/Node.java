package mlarocca.java99.trees;

public class Node<T extends Comparable<? super T>> implements TreeInternal<T> {

  private T _key;
  private Tree<T> _left;
  private Tree<T> _right;
  private Integer _size;
  private Integer _height;
  
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
  
  /**
   * Copy constructor.
   * 
   * @param original The node to copy from.
   */
  public Node(Node<T> original) {
    this(original.key(), original.left(), original.right());
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
  public int size() {
    //Lazy evaluate _size. This only works as long as Nodes are immutable.
    if (_size == null) {
      _size = 1 + left().size() + right().size();
    }
    return _size;
  }
  
  @Override
  public int height() {
    //Lazy evaluate _size. This only works as long as Nodes are immutable.
    if (_height == null) {
      _height = 1 + Math.max(left().height(), right().height());
    }
    return _height;
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
    return String.format("%d[%s]%d,%d", key().hashCode(), this.getClass(), left().hashCode(), right().hashCode()).hashCode();
  }

  @Override
  public boolean hasSymmetricStructure() {
    return TreeInternal.haveMirrorStructure(left(), right());
  }
  
  @Override
  public boolean isLeafNode() {
    return left().isLeaf() && right().isLeaf();
  }
  
  @Override
  public int leavesCount() {
    return isLeafNode() ? 1 : left().leavesCount() + right().leavesCount();
  }
}
