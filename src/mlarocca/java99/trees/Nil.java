package mlarocca.java99.trees;

import java.util.ArrayList;
import java.util.List;

class Nil<T extends Comparable<? super T>> implements TreeInternal<T> {
  
  @Override
  public boolean isNil() {
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
  
  @Override
  public boolean isLeafNode() {
    return false;
  }
  
  @Override
  public int leavesCount() {
    return 0;
  }
  
  @Override
  public List<Tree<T>> preOrder() {
    return new ArrayList<>();
  }
  
  @Override
  public List<Tree<T>> postOrder() {
    return new ArrayList<>();
  }
  
  @Override
  public List<Tree<T>> inOrder() {
    return new ArrayList<>();
  }

  @Override
  public List<Tree<T>> leavesList() {
    return new ArrayList<>();
  }
  
  @Override
  public List<T> leavesKeysList() {
    return new ArrayList<>();
  }
  

  @Override
  public List<Tree<T>> internalNodesList() {
    return new ArrayList<>();
  }
  
  @Override
  public List<T> internalNodesKeysList() {
    return new ArrayList<>();
  }
  
  @Override
  public List<Tree<T>> nodesAtHeight(int h) throws IllegalArgumentException {
    if (h < 0) {
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
    return new ArrayList<>();
  }

  @Override
  public List<T> nodesKeysAtHeight(int h) throws IllegalArgumentException {
    if (h < 0) {
      throw new IllegalArgumentException(TreeInternal.NEGATIVE_HEIGHT_MESSAGE);
    }
    return new ArrayList<>();
  }

  @Override
  public LayoutBinaryTree<T> layoutBinaryTree() {
    return new LayoutBinaryNil<>();
  }

}
