package mlarocca.java99.trees;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

public class LayoutBinaryTreeTest {
  
  @Test
  public void testEquals() {
    assertEquals(new LayoutBinaryNode<Integer>(1), new LayoutBinaryNode<>(1));
    
    //Checking tree data not referential equality
    assertEquals(
      new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>()),
      new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>()));

    //Not equals if position is different
    assertNotEquals(
      new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>(), 1, 1),
      new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>()));
    assertNotEquals(
        new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>(), 1, 1),
        new LayoutBinaryNode<Integer>(1, new LayoutBinaryNode<Integer>(2), new LayoutBinaryNil<>(), 2, 1));
    
    //Not equals to base trees with the same keys
    assertNotEquals(new Node<Integer>(1), new LayoutBinaryNode<>(1));
    assertNotEquals(new Nil<Integer>(), new LayoutBinaryNil<>());
  }
}
