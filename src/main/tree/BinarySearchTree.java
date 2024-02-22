package tree;

import estrut.Tree;

public class BinarySearchTree implements Tree {

    private Node root;

    private class Node {
        int key;
        int height;
        Node left, right;

        Node(int key) {
            this.key = key;
            this.height = 1;
        }
    }

    @Override
    public boolean buscaElemento(int valor) {
        return search(root, valor);
    }

    private boolean search(Node node, int valor) {
        if (node == null)
            return false;
        if (valor == node.key)
            return true;
        if (valor < node.key)
            return search(node.left, valor);
        else
            return search(node.right, valor);
    }

    @Override
    public int minimo() {
        return findMin(root);
    }

    private int findMin(Node node) {
        if (node.left == null)
            return node.key;
        return findMin(node.left);
    }

    @Override
    public int maximo() {
        return findMax(root);
    }

    private int findMax(Node node) {
        if (node.right == null)
            return node.key;
        return findMax(node.right);
    }

    @Override
    public void insereElemento(int valor) {
        root = insert(root, valor);
    }

    private Node insert(Node node, int valor) {
        if (node == null)
            return new Node(valor);
        if (valor < node.key)
            node.left = insert(node.left, valor);
        else if (valor > node.key)
            node.right = insert(node.right, valor);
        else
            return node; // Chaves iguais não são permitidas

        // Atualiza a altura do nó atual
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Verifica e corrige o fator de balanceamento
        int balance = balanceFactor(node);

        // Casos de desbalanceamento e rotações
        if (balance > 1 && valor < node.left.key)
            return rotateRight(node);
        if (balance < -1 && valor > node.right.key)
            return rotateLeft(node);
        if (balance > 1 && valor > node.left.key) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && valor < node.right.key) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private int height(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private int balanceFactor(Node node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T = x.right;

        x.right = y;
        y.left = T;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T = y.left;

        y.left = x;
        x.right = T;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    @Override
    public void remove(int valor) {
        root = deleteNode(root, valor);
    }

    private Node deleteNode(Node node, int valor) {
        if (node == null)
            return node;

        if (valor < node.key)
            node.left = deleteNode(node.left, valor);
        else if (valor > node.key)
            node.right = deleteNode(node.right, valor);
        else {
            if (node.left == null || node.right == null) {
                Node temp = null;
                if (temp == node.left)
                    temp = node.right;
                else
                    temp = node.left;
                if (temp == null) {
                    temp = node;
                    node = null;
                } else
                    node = temp;
            } else {
                Node temp = minValueNode(node.right);
                node.key = temp.key;
                node.right = deleteNode(node.right, temp.key);
            }
        }
        if (node == null)
            return node;

        node.height = Math.max(height(node.left), height(node.right)) + 1;

        int balance = balanceFactor(node);

        if (balance > 1 && balanceFactor(node.left) >= 0)
            return rotateRight(node);
        if (balance > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && balanceFactor(node.right) <= 0)
            return rotateLeft(node);
        if (balance < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    @Override
    public int[] preOrdem() {
        return preOrderTraversal(root);
    }

    private int[] preOrderTraversal(Node node) {
        if (node == null)
            return new int[0];
        int[] result = new int[size(node)];
        preOrderTraversal(node, result, 0);
        return result;
    }

    private int preOrderTraversal(Node node, int[] array, int index) {
        if (node == null)
            return index;
        array[index++] = node.key;
        index = preOrderTraversal(node.left, array, index);
        index = preOrderTraversal(node.right, array, index);
        return index;
    }

    @Override
    public int[] emOrdem() {
        return inOrderTraversal(root);
    }

    private int[] inOrderTraversal(Node node) {
        if (node == null)
            return new int[0];
        int[] result = new int[size(node)];
        inOrderTraversal(node, result, 0);
        return result;
    }

    private int inOrderTraversal(Node node, int[] array, int index) {
        if (node == null)
            return index;
        index = inOrderTraversal(node.left, array, index);
        array[index++] = node.key;
        index = inOrderTraversal(node.right, array, index);
        return index;
    }

    @Override
    public int[] posOrdem() {
        return postOrderTraversal(root);
    }

    private int[] postOrderTraversal(Node node) {
        if (node == null)
            return new int[0];
        int[] result = new int[size(node)];
        postOrderTraversal(node, result, 0);
        return result;
    }

    private int postOrderTraversal(Node node, int[] array, int index) {
        if (node == null)
            return index;
        index = postOrderTraversal(node.left, array, index);
        index = postOrderTraversal(node.right, array, index);
        array[index++] = node.key;
        return index;
    }

    private int size(Node node) {
        if (node == null)
            return 0;
        return 1 + size(node.left) + size(node.right);
    }
}
