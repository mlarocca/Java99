package mlarocca.java99.trees;

public interface LayoutBinaryTree<T extends Comparable<? super T>> extends Tree<T> {
    
    @Override
    public LayoutBinaryTree<T> left() throws UnsupportedOperationException;
    @Override
    public LayoutBinaryTree<T> right() throws UnsupportedOperationException;
    
    public int x();
    public int y();
}
