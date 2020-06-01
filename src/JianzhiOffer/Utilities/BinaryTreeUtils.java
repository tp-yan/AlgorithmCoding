package JianzhiOffer.Utilities;


import JianzhiOffer.Chapter02.DataStruct.Q07ConstructBinaryTree;

public class BinaryTreeUtils {
    public static void PrintTreeNode(Q07ConstructBinaryTree.BinaryTreeNode pNode) {
        if (pNode != null) {
            System.out.printf("value of this node is: %d\n", pNode.value);

            if (pNode.leftNode != null)
                System.out.printf("value of its left child is: %d.\n", pNode.leftNode.value);
            else
                System.out.printf("left child is null.\n");
            if (pNode.rightNode != null)
                System.out.printf("value of its right child is: %d.\n", pNode.rightNode.value);
            else
                System.out.printf("right child is null.\n");
        } else {
            System.out.printf("this node is null.\n");
        }

        System.out.printf("\n");
    }

    public static void PrintTree(Q07ConstructBinaryTree.BinaryTreeNode pRoot) {
        PrintTreeNode(pRoot);

        if (pRoot != null) {
            if (pRoot.leftNode != null)
                PrintTree(pRoot.leftNode);

            if (pRoot.rightNode != null)
                PrintTree(pRoot.rightNode);
        }
    }
}
