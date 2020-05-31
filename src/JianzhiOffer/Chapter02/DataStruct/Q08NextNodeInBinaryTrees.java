package JianzhiOffer.Chapter02.DataStruct;

/*
// 面试题8：二叉树的下一个结点
// 题目：给定一棵二叉树和其中的一个结点，如何找出中序遍历顺序的下一个结点？
// 树中的结点除了有两个分别指向左右子结点的指针以外，还有一个指向父结点的指针。
解：
(1)若当前节点有右子树，那么下一个节点是右子树的最左边的节点
(2)若当前节点有父节点且是父节点的左节点，那么下一个节点就是父节点
(3)若不是前2者，则当前节点是右节点且无右子树，向上遍历当前节点的某个(直接或间接)父节点A，且A是其父节点B的左节点，那么下一个节点就是A的父节点B。
 */
public class Q08NextNodeInBinaryTrees {
    private static class BinaryTreeNode {
        int value;
        BinaryTreeNode parent;
        BinaryTreeNode left;
        BinaryTreeNode right;

        public BinaryTreeNode(int value) {
            this.value = value;
        }
    }


    /**
     * 获取当前节点在中序遍历中的下一个节点
     *
     * @param pNode
     */
    public static BinaryTreeNode getNextNode(BinaryTreeNode pNode) {
        if (pNode == null) return null;
        // 只有一个节点
        if (pNode.parent == pNode.left && pNode.left == pNode.right && pNode.right == null) return null;
        // 情况(1)
        if (pNode.right != null) {
            BinaryTreeNode curNode = pNode.right;
            while (curNode.left != null)
                curNode = pNode.left;
            return curNode;
        } else {
            BinaryTreeNode curNode = pNode;
            BinaryTreeNode parentNode = pNode.parent;
            while (parentNode != null && curNode != parentNode.left) {// 情况(2) 当 curNode == parentNode.left时退出
                // 情况(3) 当 parentNode == null || curNode == parentNode.left 时退出
                curNode = parentNode;
                parentNode = parentNode.parent;
            }
            return parentNode;
        }
    }

    public static void main(String[] args) {

    }
}
