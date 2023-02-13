package main.searches;

public class Tree<E> {
    public static class Node<E> {
        private Node parent;
        private Node left;
        private Node right;
        private E data;

        public Node(E data) {
            this.data = data;
        }

        public E getData() {
            return data;
        }

        public void setData(E data) {
            this.data = data;
        }

        /**
         * Sets the left child of this node.
         *
         * @param other The new left child of this node.
         * @return The old left child, or null if there was none.
         */
        public Node<E> setLeft(Node<E> other) {
            Node<E> ret = this.left;
            this.left.parent = null;
            this.left = other;
            other.parent.left = null;
            other.parent = this;
            return ret;
        }

        /**
         * Sets the right child of this node.
         *
         * @param other The new right child of this node.
         * @return The old right child, or null if there was none.
         */
        public Node<E> setRight(Node<E> other) {
            Node<E> ret = this.right;
            this.right.parent = null;
            this.right = other;
            other.parent.right = null;
            other.parent = this;
            return ret;
        }
    }

    private Node<E> root;

    public Tree() {
        this.root = null;
    }

    public Tree(E rootData) {
        this.root = new Node<>(rootData);
    }

    /**
     * Returns the tree's root.
     *
     * @return The tree's root, or null if there is none (the tree is empty).
     */
    public Node<E> getRoot() {
        return root;
    }
}