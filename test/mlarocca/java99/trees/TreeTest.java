package mlarocca.java99.trees;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class TreeTest {
  private static Tree<Integer> t1;
  private static Node<Integer> t2;
  private static Tree<Integer> leaf;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    t1 = new Node<>(1, new Node<>(2), new Node<>(3));
    t2 = new Node<>(1, new Node<>(2), new Node<>(3));
    leaf = new Leaf<>();
  }
  
  @Test
  public void testIsLeaf() {
    assertTrue(leaf.isLeaf());
    assertFalse(t1.isLeaf());
  }
  
  @Test
  public void testIsNode() {
    assertFalse(leaf.isNode());
    assertTrue(t1.isNode());
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
    assertEquals(t1, t2);
    Tree<Integer> t3 = new Node<>(1, new Node<>(2, new Node<>(4), new Leaf<>()), new Node<>(3));
    assertNotEquals(t1, t3);
  }
  
  @Test
  public void testTreeEquality() {
    assertNotEquals(new Leaf<Integer>(), new Node<Integer>(12));
  }
  
  @Test
  public void testCBalanced() {
    Tree<Integer> t4 = new Node<>(1, new Node<>(1, new Node<>(1), new Leaf<>()), new Node<>(1));
    assertEquals(t4, Tree.cBalanced(4, 1));
    assertEquals(t4.left(), Tree.cBalanced(2, 1));
    assertEquals(t4.right(), Tree.cBalanced(1, 1));
    assertEquals(new Leaf<>(), Tree.cBalanced(0, "x"));
  }

  @Test(expected = IllegalArgumentException.class) 
  public void testCBalancedFailure() {
    Tree.cBalanced(-1, 'c');
  }
  
  @Test
  public void testAllCompleteBalancedTrees() {
    Character key = 'x';
    Node<Character> singleton = new Node<>(key);
    Leaf<Character> leaf = new Leaf<>();    
    Set<Tree<Character>> set = Tree.allCompleteBalancedTrees(0, 'f');
    Set<Tree<Character>> expectedSet = new HashSet<>(Arrays.asList(leaf));
    assertEquals(expectedSet, set);
    
    set = Tree.allCompleteBalancedTrees(1, key);
    expectedSet = new HashSet<>(Arrays.asList(new Node<>(singleton)));
    assertEquals(expectedSet, set);
    
    set = Tree.allCompleteBalancedTrees(2, key);
    expectedSet = new HashSet<>(Arrays.asList(new Node<>(key, singleton, leaf), new Node<>(key, leaf, singleton)));
    assertEquals(expectedSet, set);
    
    set = Tree.allCompleteBalancedTrees(3, key);
    expectedSet = new HashSet<>(Arrays.asList(new Node<>(key, singleton, singleton)));
    assertEquals(expectedSet, set);
    
    set = Tree.allCompleteBalancedTrees(4, key);
    expectedSet = new HashSet<>(Arrays.asList(
        new Node<>(key, singleton, new Node<>(key, singleton, leaf)),
        new Node<>(key, singleton, new Node<>(key, leaf, singleton)),
        new Node<>(key, new Node<>(key, singleton, leaf), singleton),
        new Node<>(key, new Node<>(key, leaf, singleton), singleton)));
    assertEquals(expectedSet, set);
  }

  @Test(expected = IllegalArgumentException.class) 
  public void testAllCompleteBalancedTreesFailure() {
    Tree.allCompleteBalancedTrees(-1, 'x');
  }
  
  @Test
  public void testHasSymmetricStructure() {
    assertTrue(t1.hasSymmetricStructure());
    assertTrue(t2.hasSymmetricStructure());
    assertTrue(leaf.hasSymmetricStructure());
    Tree<Integer> symmetricTree1 = new Node<>(1, t1, t1);
    Tree<Integer> symmetricTree2 = new Node<>(1, new Node<>(1, new Node<>(1), leaf), new Node<>(1, leaf, new Node<>(1)));
    Tree<Integer> symmetricTree3 = new Node<>(1, new Node<>(1, new Node<>(1, leaf, t1), leaf), new Node<>(1, leaf, new Node<>(1, t1, leaf)));
    Tree<Integer> asymmetricTree1 = new Node<>(1, new Node<>(1, new Node<>(1), leaf), new Node<>(1));
    Tree<Integer> asymmetricTree3 = new Node<>(1, new Node<>(1, new Node<>(1, leaf, t1), leaf), new Node<>(1, leaf, new Node<>(1, leaf, t1)));
    assertTrue(symmetricTree1.hasSymmetricStructure());
    assertTrue(symmetricTree2.hasSymmetricStructure());
    assertTrue(symmetricTree3.hasSymmetricStructure());
    assertFalse(asymmetricTree1.hasSymmetricStructure());
    assertFalse(asymmetricTree3.hasSymmetricStructure());
  }
  
  @Test
  public void testAllSymmetricBalancedTrees() {
    Integer key = 45;
    Node<Integer> singleton = new Node<>(key);
    Leaf<Integer> leaf = new Leaf<>();    
    Set<Tree<Integer>> set = Tree.allSymmetricBalancedTrees(0, key);
    Set<Tree<Integer>> expectedSet = new HashSet<>(Arrays.asList(leaf));
    assertEquals(expectedSet, set);
    
    set = Tree.allSymmetricBalancedTrees(1, key);
    expectedSet = new HashSet<>(Arrays.asList(new Node<>(singleton)));
    assertEquals(expectedSet, set);
    
    set = Tree.allSymmetricBalancedTrees(2, key);
    expectedSet = new HashSet<>();
    assertEquals(expectedSet, set);
    
    set = Tree.allSymmetricBalancedTrees(3, key);
    expectedSet = new HashSet<>(Arrays.asList(new Node<>(key, singleton, singleton)));
    assertEquals(expectedSet, set);
    
    set = Tree.allSymmetricBalancedTrees(4, key);
    expectedSet = new HashSet<>();
    assertEquals(expectedSet, set);    

    set = Tree.allSymmetricBalancedTrees(5, 45);
    expectedSet = new HashSet<>(Arrays.asList(
      new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, singleton, leaf)),
      new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, leaf, singleton))));
    assertEquals(expectedSet, set);
  }

  @Test(expected = IllegalArgumentException.class) 
  public void testAllSymmetricBalancedTreesFailure() {
    Tree.allSymmetricBalancedTrees(-10, 'x');
  }
}
