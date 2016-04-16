package mlarocca.java99.trees;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class BinarySearchTreeTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Test
  public void testEquals() {
    assertEquals(new BinarySearchNode<Integer>(1), new BinarySearchNode<>(1));
    assertEquals(new BinarySearchNode<Integer>(1).addKey(2), new BinarySearchNode<>(1).addKey(2));
    //Not equals to base trees with the same keys
    assertNotEquals(new Node<Integer>(1), new BinarySearchNode<>(1));
  }

  @Test
  public void testAddKey() {
    assertEquals((Integer)2, new BinarySearchNode<>(1).addKey(2).right().key());
    assertEquals((Integer)(-1), new BinarySearchNode<>(1).addKey(-1).left().key());
    assertEquals((Integer)(0), new BinarySearchNode<>(1).addKey(-1).addKey(0).left().right().key());
  }

  @Test
  public void testAddKeyBalanced() {
    //RANDOM TEST
    //Adding the same key should result in a left children 50% of the times
    int left = 0;
    int right = 0;
    final int total = 10000;
    final String key = "a";
    final BinarySearchNode<String> keyNode = new BinarySearchNode<String>(key);
    while(left + right < total) {
      BinarySearchTree<String> bst = new BinarySearchNode<String>(key);
      bst = bst.addKey(key);
      if (bst.left().equals(keyNode)) {
        left += 1;
      } else if (bst.right().equals(keyNode)) {
        right += 1;
      } else {
        throw new Error("Adding value error");
      }
    }
    assertTrue(Math.abs(right - left) / (double) total <= 100.0 / total);
  }

}
