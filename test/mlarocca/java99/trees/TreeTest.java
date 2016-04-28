package mlarocca.java99.trees;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

public class TreeTest {
  private static Tree<Integer> t1;
  private static Node<Integer> t2;
  private static Node<Integer> t3;
  private static Tree<Integer> nil;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    t1 = new Node<>(1, new Node<>(2), new Node<>(3));
    t2 = new Node<>(1, new Node<>(2), new Node<>(3));
    t3 = new Node<>(1, new Node<>(2, new Node<>(3), new Node<>(4)), new Node<>(5));
    nil = new Nil<>();
  }
  
  @Test
  public void testIsLeaf() {
    assertTrue(nil.isNil());
    assertFalse(t1.isNil());
  }
  
  @Test
  public void testIsNode() {
    assertFalse(nil.isNode());
    assertTrue(t1.isNode());
  }
  
  @Test
  public void testSize() {
    assertEquals(0, nil.size());
    assertEquals(1, new Node<>(1).size());
    assertEquals(2, new Node<>(1, new Node<>(2), nil).size());
    assertEquals(3, t1.size());
    assertEquals(7, new Node<>('x', new Node<>('a', new Node<>('4'), new Node<>('4')), new Node<>('b', new Node<>('4'), new Node<>('4'))).size());
  }

  @Test
  public void testHeight() {
    assertEquals(0, nil.height());
    assertEquals(1, new Node<>(1).height());
    assertEquals(2, new Node<>(1, new Node<>(2), nil).height());
    assertEquals(2, t1.height());
    assertEquals(3, new Node<>('x', new Node<>('a', new Node<>('4'), new Node<>('4')), new Node<>('b', new Node<>('4'), new Node<>('4'))).height());
  }
  
  @Test
  public void testLeafEquality() {
    assertEquals(new Nil<Character>(), new Nil<Character>());
    assertEquals(new Nil<Integer>(), new Nil<Double>());
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
    assertEquals(new Node<String>("12"), new Node<String>("12", new Nil<>(), new Nil<>()));
    //Deeper trees
    assertEquals(t1, t2);
    Tree<Integer> t3 = new Node<>(1, new Node<>(2, new Node<>(4), new Nil<>()), new Node<>(3));
    assertNotEquals(t1, t3);
  }
  
  @Test
  public void testTreeEquality() {
    String key = "1123432";
    Nil<String> leaf = new Nil<>();
    Tree<String> singleton = new Node<>(key);
    
    assertNotEquals(new Nil<Integer>(), new Node<Integer>(12));
   
    List<Tree<String>> list = Arrays.asList(
        singleton,
        new Node<>(key, singleton, singleton),
        new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, singleton, singleton)),
        new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, singleton, singleton)),
        new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, singleton, singleton)),
        new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, leaf, singleton)),
        new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, singleton, leaf)),
        new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, leaf, singleton)),
        new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, singleton, leaf)),
        new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, leaf, singleton)),
        new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, singleton, leaf)),
        new Node<>(key, new Node<>(key, singleton, singleton), singleton),
        new Node<>(key, new Node<>(key, leaf, singleton), singleton),
        new Node<>(key, new Node<>(key, singleton, leaf), singleton),     
        new Node<>(key, singleton, new Node<>(key, singleton, singleton)),
        new Node<>(key, singleton, new Node<>(key, leaf, singleton)),
        new Node<>(key, singleton, new Node<>(key, singleton, leaf)));
    //A list of trees without duplicates should have the same size of the set created from it.
    assertEquals(list.size(), new HashSet<>(list).size());
  }
  
  @Test
  public void testCBalanced() {
    Tree<Integer> t4 = new Node<>(1, new Node<>(1, new Node<>(1), new Nil<>()), new Node<>(1));
    assertEquals(t4, Tree.cBalanced(4, 1));
    assertEquals(t4.left(), Tree.cBalanced(2, 1));
    assertEquals(t4.right(), Tree.cBalanced(1, 1));
    assertEquals(new Nil<>(), Tree.cBalanced(0, "x"));
  }

  @Test(expected = IllegalArgumentException.class) 
  public void testCBalancedFailure() {
    Tree.cBalanced(-1, 'c');
  }
  
  @Test
  public void testAllCompleteBalancedTrees() {
    Character key = 'x';
    Node<Character> singleton = new Node<>(key);
    Nil<Character> leaf = new Nil<>();    
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
    assertTrue(nil.hasSymmetricStructure());
    Tree<Integer> symmetricTree1 = new Node<>(1, t1, t1);
    Tree<Integer> symmetricTree2 = new Node<>(1, new Node<>(1, new Node<>(1), nil), new Node<>(1, nil, new Node<>(1)));
    Tree<Integer> symmetricTree3 = new Node<>(1, new Node<>(1, new Node<>(1, nil, t1), nil), new Node<>(1, nil, new Node<>(1, t1, nil)));
    Tree<Integer> asymmetricTree1 = new Node<>(1, new Node<>(1, new Node<>(1), nil), new Node<>(1));
    Tree<Integer> asymmetricTree3 = new Node<>(1, new Node<>(1, new Node<>(1, nil, t1), nil), new Node<>(1, nil, new Node<>(1, nil, t1)));
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
    Nil<Integer> leaf = new Nil<>();    
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
  
  @Test
  public void testAllHeightBalancedTrees() {
    Integer key = 45;
    Node<Integer> singleton = new Node<>(key);
    Nil<Integer> leaf = new Nil<>();    
    Set<Tree<Integer>> set = Tree.allHeightBalancedTrees(0, key);
    Set<Tree<Integer>> expectedSet = new HashSet<>(Arrays.asList(leaf));
    assertEquals(expectedSet, set);
    
    set = Tree.allHeightBalancedTrees(1, key);
    expectedSet = new HashSet<>(Arrays.asList(singleton));
    assertEquals(expectedSet, set);
    
    set = Tree.allHeightBalancedTrees(2, key);
    expectedSet = new HashSet<>(Arrays.asList(
      new Node<>(key, singleton, singleton),
      new Node<>(key, leaf, singleton),
      new Node<>(key, singleton, leaf)));
    assertEquals(expectedSet, set);
    
    set = Tree.allHeightBalancedTrees(3, key);
    expectedSet = new HashSet<>(Arrays.asList(
      new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, singleton, singleton)),
      new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, singleton, singleton)),
      new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, singleton, singleton)),
      new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, leaf, singleton)),
      new Node<>(key, new Node<>(key, singleton, singleton), new Node<>(key, singleton, leaf)),
      new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, leaf, singleton)),
      new Node<>(key, new Node<>(key, leaf, singleton), new Node<>(key, singleton, leaf)),
      new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, leaf, singleton)),
      new Node<>(key, new Node<>(key, singleton, leaf), new Node<>(key, singleton, leaf)),
      new Node<>(key, new Node<>(key, singleton, singleton), singleton),
      new Node<>(key, new Node<>(key, leaf, singleton), singleton),
      new Node<>(key, new Node<>(key, singleton, leaf), singleton),     
      new Node<>(key, singleton, new Node<>(key, singleton, singleton)),
      new Node<>(key, singleton, new Node<>(key, leaf, singleton)),
      new Node<>(key, singleton, new Node<>(key, singleton, leaf))));
    assertEquals(expectedSet, set);
  }

  @Test(expected = IllegalArgumentException.class) 
  public void testAllHeightBalancedTreesFailure() {
    Tree.allHeightBalancedTrees(-10, 'x');
  }
  
  @Test
  public void testMinHbalNodes() {
    assertEquals(Tree.minHbalNodes(0), 0);
    assertEquals(Tree.minHbalNodes(1), 1);
    assertEquals(Tree.minHbalNodes(2), 2);
    assertEquals(Tree.minHbalNodes(3), 4);
    assertEquals(Tree.minHbalNodes(4), 7);
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testMinHbalNodesFailure() {
    Tree.minHbalNodes(Integer.MIN_VALUE);
  }
  
  @Test
  public void testMaxHbalNodes() {
    assertEquals(Tree.maxHbalNodes(0), 0);
    assertEquals(Tree.maxHbalNodes(1), 1);
    assertEquals(Tree.maxHbalNodes(2), 3);
    assertEquals(Tree.maxHbalNodes(3), 7);
    assertEquals(Tree.maxHbalNodes(4), 15);    
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testMaxHbalNodesFailure() {
    Tree.maxHbalNodes(-100);
  }

  @Test
  public void testMinHbalHeight() {
    assertEquals(Tree.minHbalHeight(0), 0);
    assertEquals(Tree.minHbalHeight(1), 1);
    assertEquals(Tree.minHbalHeight(2), 2);
    assertEquals(Tree.minHbalHeight(3), 2);
    assertEquals(Tree.minHbalHeight(4), 3);
    assertEquals(Tree.minHbalHeight(5), 3);
    assertEquals(Tree.minHbalHeight(6), 3);
    assertEquals(Tree.minHbalHeight(7), 3);
    assertEquals(Tree.minHbalHeight(8), 4);
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testMinHbalHeightFailure() {
    Tree.minHbalHeight(-1);
  }
  
  @Test
  public void testMaxHbalHeight() {
    assertEquals(0, Tree.maxHbalHeight(0));
    assertEquals(1, Tree.maxHbalHeight(1));
    assertEquals(2, Tree.maxHbalHeight(2));
    assertEquals(2, Tree.maxHbalHeight(3));
    assertEquals(3, Tree.maxHbalHeight(4));    
    assertEquals(3, Tree.maxHbalHeight(5));    
    assertEquals(3, Tree.maxHbalHeight(6));    
    assertEquals(4, Tree.maxHbalHeight(7));    
    assertEquals(4, Tree.maxHbalHeight(8));    
    assertEquals(4, Tree.maxHbalHeight(9));    
    assertEquals(4, Tree.maxHbalHeight(10));    
    assertEquals(4, Tree.maxHbalHeight(11));    
    assertEquals(5, Tree.maxHbalHeight(12));    
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testMaxHbalHeightFailure() {
    Tree.maxHbalHeight(-100);
  }
  
  @Test
  public void testAllHeightBalancedTreesWithNodes() {
    Set<Tree<Integer>> expected;
    Integer key = 1;
    expected = new HashSet<>(Arrays.asList(nil));
    assertEquals(expected, Tree.allHeightBalancedTreesWithNodes(0, key));
    expected = new HashSet<>(Arrays.asList(new Node<>(key)));
    assertEquals(expected, Tree.allHeightBalancedTreesWithNodes(1, key));
    expected = new HashSet<>(Arrays.asList(
        new Node<>(key, new Node<>(key), nil),
        new Node<>(key, nil, new Node<>(key))));
    assertEquals(expected, Tree.allHeightBalancedTreesWithNodes(2, key));
    expected = new HashSet<>(Arrays.asList(
        new Node<>(key, new Node<>(key), new Node<>(key))));
    assertEquals(expected, Tree.allHeightBalancedTreesWithNodes(3, key));

    expected = new HashSet<>(Arrays.asList(new Node<>(key, new Node<>(key, new Node<>(key), nil), new Node<>(key)),
        new Node<>(key, new Node<>(key, nil, new Node<>(key)), new Node<>(key)),
        new Node<>(key, new Node<>(key), new Node<>(key, new Node<>(key), nil)),
        new Node<>(key, new Node<>(key), new Node<>(key, nil, new Node<>(key)))));
    assertEquals(expected, Tree.allHeightBalancedTreesWithNodes(4, key));

    //assertEquals(1553, Tree.allHeightBalancedTreesWithNodes(15, key).size()); 
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testAllHeightBalancedTreesWithNodesFailure() {
    Tree.allHeightBalancedTreesWithNodes(-10, 3);
  }
  
  @Test
  public void testLeavesCount() {
    assertEquals(0, nil.leavesCount());
    assertEquals(1, new Node<Integer>(11).leavesCount());
    assertEquals(1, new Node<Integer>(11, new Node<Integer>(22), nil).leavesCount());
    assertEquals(1, new Node<Integer>(11, nil, new Node<Integer>(22)).leavesCount());
    assertEquals(2, t1.leavesCount());
    assertEquals(4, new Node<Integer>(0, t1, t1).leavesCount());
  }
  
  @Test
  public void testPreOrder() {
    List<Tree<Integer>> expected = Arrays.asList();
    assertEquals(expected, nil.preOrder());
    
    List<Integer> expectedKeys = Arrays.asList(1, 2, 3, 4, 5);
    assertEquals(expectedKeys, t3.preOrder().stream().map(Tree::key).collect(Collectors.toList()));
  }

  @Test
  public void testInOrder() {
    List<Tree<Integer>> expected = Arrays.asList();
    assertEquals(expected, nil.inOrder());
    
    List<Integer> expectedKeys = Arrays.asList(3, 2, 4, 1, 5);
    assertEquals(expectedKeys, t3.inOrder().stream().map(Tree::key).collect(Collectors.toList()));
  }

  @Test
  public void testPostOrder() {
    List<Tree<Integer>> expected = Arrays.asList();
    assertEquals(expected, nil.postOrder());
    
    List<Integer> expectedKeys = Arrays.asList(3, 4, 2, 5, 1);
    assertEquals(expectedKeys, t3.postOrder().stream().map(Tree::key).collect(Collectors.toList()));
  }
  
  @Test
  public void testLeavesKeysList() {
    assertEquals(Arrays.asList(), nil.leavesList());
    
    Tree<Character> t = new Node<>('a', new Node<>('b'), new Node<>('c', new Node<>('d'), new Node<>('e')));
    List<Character> expected = Arrays.asList('b', 'd', 'e');
    assertEquals(expected, t.leavesKeysList());
  }
  
  @Test
  public void testInternalNodesKeysList() {
    assertEquals(Arrays.asList(), nil.internalNodesList());

    Tree<Character> t = new Node<>('a', new Node<>('b'), new Node<>('c', new Node<>('d'), new Node<>('e')));
    List<Character> expected = Arrays.asList('a', 'c');
    assertEquals(expected, t.internalNodesKeysList());
  }
  
  @Test
  public void testNodesKeysAtLevel() {
    List<Integer> expected = Arrays.asList();
    assertEquals(Arrays.asList(), nil.nodesAtHeight(0));
    assertEquals(Arrays.asList(), nil.nodesAtHeight(1));
    assertEquals(Arrays.asList(), nil.nodesAtHeight(2));

    Tree<Integer> t = new Node<Integer>(0, t1, t1);
    expected = Arrays.asList(0);
    assertEquals(expected, t.nodesKeysAtHeight(0));
    
    expected = Arrays.asList(1, 1);
    assertEquals(expected, t.nodesKeysAtHeight(1));
    
    expected = Arrays.asList(2, 3, 2, 3);
    assertEquals(expected, t.nodesKeysAtHeight(2));

    expected = Arrays.asList();
    assertEquals(expected, t.nodesKeysAtHeight(3));
  }  

  @Test(expected = IllegalArgumentException.class)
  public void testNodesKeysAtLevelFailure() {
    t1.nodesAtHeight(-1);
  }

  @Test
  public void testCompleteBinaryTree() {
    Tree<Integer> expected;
    Integer key = 1;
    
    expected = nil;
    assertEquals(expected, Tree.completeBinaryTree(0, key));
    
    expected = new Node<>(key);
    assertEquals(expected, Tree.completeBinaryTree(1, key));
    
    expected = new Node<>(key, new Node<>(key), nil);
    assertEquals(expected, Tree.completeBinaryTree(2, key));

    expected = new Node<>(key, new Node<>(key), new Node<>(key));
    assertEquals(expected, Tree.completeBinaryTree(3, key));

    expected = new Node<>(key, new Node<>(key, new Node<>(key), nil), new Node<>(key));
    assertEquals(expected, Tree.completeBinaryTree(4, key));

    expected = new Node<>(key, new Node<>(key, new Node<>(key), new Node<>(key)), new Node<>(key));
    assertEquals(expected, Tree.completeBinaryTree(5, key));

    expected = new Node<>(key, new Node<>(key, new Node<>(key), new Node<>(key)), new Node<>(key, new Node<>(key), nil));
    assertEquals(expected, Tree.completeBinaryTree(6, key));

    expected = new Node<>(key, new Node<>(key, new Node<>(key), new Node<>(key)), new Node<>(key, new Node<>(key), new Node<>(key)));
    assertEquals(expected, Tree.completeBinaryTree(7, key));

    expected = new Node<>(key, new Node<>(key, new Node<>(key, new Node<>(key), new Node<>(key)), new Node<>(key, new Node<>(key), nil)), new Node<>(key, new Node<>(key), new Node<>(key)));
    assertEquals(expected, Tree.completeBinaryTree(10, key));
  }
  
  @Test(expected = IllegalArgumentException.class) 
  public void testCompleteBinaryTreeFailure() {
    Tree.completeBinaryTree(-10, "xyz");
  }
  
  @Test
  public void testLayoutBinaryTree() {
    assertEquals(nil.layoutBinaryTree(), new LayoutBinaryNil<>());
    assertNotEquals(t1.layoutBinaryTree(), new LayoutBinaryNil<>());

    LayoutBinaryNil<Integer> lbnil = new LayoutBinaryNil<>();
    LayoutBinaryTree<Integer> expected = new LayoutBinaryNode<>(
      t1.key(), 
      new LayoutBinaryNode<Integer>(t1.left().key(), lbnil, lbnil, 1, 2),
      new LayoutBinaryNode<Integer>(t1.right().key(), lbnil, lbnil, 3, 2),
      2,
      1);
    
    assertEquals(expected, t1.layoutBinaryTree());
    
    BinarySearchTree<Character> t = BinarySearchTree.fromList(Arrays.asList('n','k','m','c','a','h','g','e','u','p','s','q'));
    
    LayoutBinaryNil<Character> lbnilC = new LayoutBinaryNil<>();
    LayoutBinaryTree<Character> expectedC = new LayoutBinaryNode<>(
        'n', 
        new LayoutBinaryNode<Character>('k', 
            new LayoutBinaryNode<Character>('c',
              new LayoutBinaryNode<Character>('a', lbnilC, lbnilC, 1, 4),
              new LayoutBinaryNode<Character>('h',
                new LayoutBinaryNode<Character>('g',
                  new LayoutBinaryNode<Character>('e', lbnilC, lbnilC, 3, 6),
                  lbnilC,
                  4,
                  5),
                lbnilC,
                5,
                4),
              2, 3),
            new LayoutBinaryNode<Character>('m', lbnilC, lbnilC, 7, 3),
            6, 2),
        new LayoutBinaryNode<Character>('u',
          new LayoutBinaryNode<Character>('p',
            lbnilC,
            new LayoutBinaryNode<Character>('s',
              new LayoutBinaryNode<Character>('q',lbnilC, lbnilC, 10, 5),
              lbnilC,
              11,
              4),
            9,
            3),
          lbnilC,
          12,
          2),
        8,
        1);
    
    assertEquals(expectedC, t.layoutBinaryTree());
  }
}
