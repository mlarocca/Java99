package mlarocca.java99.trees;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TreeTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Test
  public void testLeafEquality() {
    assertEquals(new Leaf<Character>(), new Leaf<Character>());
    assertEquals(new Leaf<Integer>(), new Leaf<Double>());
  }
  
  @Test
  public void testNodeEquality() {
    //Nodes with no children
    assertEquals(new Node<String>("12"), new Node<String>("12"));
    assertEquals(new Node<Integer>(12), new Node<Integer>(12));
    assertNotEquals(new Node<Integer>(1), new Node<Integer>(2));
    assertNotEquals(new Node<String>("12"), new Node<Integer>(12));
    assertEquals(new Node<Integer>(12), new Node<Byte>((byte)12));
    //Explicitly created Leaves
    assertEquals(new Node<String>("12"), new Node<String>("12", new Leaf<>(), new Leaf<>()));
    //Deeper trees
    Tree<Integer> t1 = new Node<>(1, new Node<>(2), new Node<>(3));
    Node<Integer> t2 = new Node<>(1, new Node<>(2), new Node<>(3));
    assertEquals(t1, t2);
    Tree<Integer> t3 = new Node<>(1, new Node<>(2, new Node<>(4), new Leaf<>()), new Node<>(3));
    assertNotEquals(t1, t3);
  }
  
  @Test
  public void testTreeEquality() {
    assertNotEquals(new Leaf<Integer>(), new Node<Integer>(12));
  }

}
