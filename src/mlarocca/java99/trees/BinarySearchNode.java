package mlarocca.java99.trees;

import java.util.Random;

public class BinarySearchNode<T extends Comparable<T>> extends Node<T> implements BinarySearchTree<T> {

  private static final Random random = new Random();
  
  public BinarySearchNode(T key) throws NullPointerException {
    super(key, new BinarySearchLeaf<T>(), new BinarySearchLeaf<T>());
  }

  private BinarySearchNode(T key, BinarySearchTree<T> left, BinarySearchTree<T> right) throws NullPointerException {
    super(key, left, right);
  }
  
  @Override
  public BinarySearchTree<T> left() throws UnsupportedOperationException {
    return (BinarySearchTree<T>)super.left();
  }

  @Override
  public BinarySearchTree<T> right() throws UnsupportedOperationException {
    return (BinarySearchTree<T>)super.right();
  }

  @Override
  public synchronized BinarySearchTree<T> addKey(T key) {
    T currentKey = key();
    if (key.compareTo(currentKey) < 0 ||
        (key.compareTo(currentKey) == 0 && random.nextBoolean())) {
      return new BinarySearchNode<T>(currentKey, left().addKey(key), right());
    } else {
      return new BinarySearchNode<T>(currentKey, left(), right().addKey(key));
    }
  }
}
