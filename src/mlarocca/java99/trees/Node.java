package mlarocca.java99.trees;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    _left = new Nil<>();
    _right = new Nil<>();
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
  public boolean isNil() throws UnsupportedOperationException {
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
    return left().isNil() && right().isNil();
  }
  
  @Override
  public int leavesCount() {
    return isLeafNode() ? 1 : left().leavesCount() + right().leavesCount();
  }
  
  @Override
  public List<Tree<T>> preOrder() {
    List<Tree<T>> result = new ArrayList<>(Arrays.asList(this));
    result.addAll(left().preOrder());
    result.addAll(right().preOrder());
    return result;
  }

  
  @Override
  public List<Tree<T>> postOrder() {
    List<Tree<T>> result = left().postOrder();
    result.addAll(right().postOrder());
    result.add(this);
    return result;
  }
  
  @Override
  public List<Tree<T>> inOrder() {
    List<Tree<T>> result = left().inOrder();
    result.add(this);
    result.addAll(right().inOrder());
    return result;
  }

  @Override
  public List<Tree<T>> leavesList() {
    return preOrder().stream().filter(n -> n.isLeafNode()).collect(Collectors.toList());
  }
  
  @Override
  public List<T> leavesKeysList() {
    return leavesList().stream().map(Tree::key).collect(Collectors.toList());
  }


  @Override
  public List<Tree<T>> internalNodesList() {
    return preOrder().stream().filter(n -> !n.isLeafNode()).collect(Collectors.toList());
  }
  
  @Override
  public List<T> internalNodesKeysList() {
    return internalNodesList().stream().map(Tree::key).collect(Collectors.toList());
  }
  
  @Override
  public List<Tree<T>> nodesAtHeight(int h) throws IllegalArgumentException {
    List<Tree<T>> result;
    if (h > 0) {
      result = left().nodesAtHeight(h - 1);
      result.addAll(right().nodesAtHeight(h - 1));
    } else if (h == 0) {
      result = new ArrayList<>();
      result.add(this);
    } else {
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
    return result;
  }
  
  @Override
  public List<T> nodesKeysAtHeight(int h) throws IllegalArgumentException {
    return nodesAtHeight(h).stream().map(Tree::key).collect(Collectors.toList());
  }

  @Override
  public LayoutBinaryTree<T> layoutBinaryTree() {
    return layoutBinaryTree(0, 0);
  }
  
  private LayoutBinaryTree<T> layoutBinaryTree(int x, int y) {
    int nodeX = x + left().size() + 1;
    LayoutBinaryTree<T> leftLT;
    LayoutBinaryTree<T> rightLT;
    leftLT = left().isNil() ? new LayoutBinaryNil<>() : ((Node<T>)left()).layoutBinaryTree(x, y + 1);
    rightLT = right().isNil() ? new LayoutBinaryNil<>() : ((Node<T>)right()).layoutBinaryTree(nodeX, y + 1);
    return new LayoutBinaryNode<T>(key(), leftLT, rightLT, nodeX, y + 1);
  }

}
