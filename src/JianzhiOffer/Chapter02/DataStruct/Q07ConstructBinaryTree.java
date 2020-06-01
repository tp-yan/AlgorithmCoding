package JianzhiOffer.Chapter02.DataStruct;

import JianzhiOffer.Utilities.BinaryTreeUtils;

import java.util.Arrays;

/*
// 面试题7：重建二叉树
// 题目：输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输
// 入的前序遍历和中序遍历的结果中都不含重复的数字。例如输入前序遍历序列{1,
// 2, 4, 7, 3, 5, 6, 8}和中序遍历序列{4, 7, 2, 1, 5, 3, 8, 6}，则重建出
// 图2.6所示的二叉树并输出它的头结点。
 */
public class Q07ConstructBinaryTree {
    /**
     * @param preorder 前序遍历
     * @param inorder  中序遍历
     * @return
     * @throws Exception
     */
    public static BinaryTreeNode construct(int[] preorder, int[] inorder) throws Exception {
        if (preorder == null || inorder == null || preorder.length < 1 || inorder.length < 1) return null;
        if (preorder.length != inorder.length) throw new Exception("Invalid Input");
        return constructCore(preorder, 0, preorder.length - 1,
                inorder, 0, inorder.length - 1);

    }

    /**
     * 对前序和中序数组只会读取不会修改
     *
     * @param preorder
     * @param preLeft  要处理的前序数组左界（包含）
     * @param preRight 要处理的前序数组由界（包含）
     * @param inorder
     * @param inLeft   要处理的中序数组左界（包含）
     * @param inRight  要处理的中序数组左界（包含）
     * @return 返回根结点
     */
    public static BinaryTreeNode constructCore(int[] preorder, int preLeft, int preRight,
                                               int[] inorder, int inLeft, int inRight) throws Exception {
        // 1.先创建根结点
        int rootValue = preorder[preLeft]; // 前序第一个元素为根结点
        BinaryTreeNode root = new BinaryTreeNode(rootValue);
        root.leftNode = null;
        root.rightNode = null;

        // 递归结束条件
        if (preLeft == preRight) {// 当只有一个节点时，前序和中序必须完全一致.
            if (inLeft == inRight && preorder[preLeft] == inorder[inLeft]) return root; // 没有子树则直接返回
            else throw new Exception("Invalid input");
        }
        // 2.找到根结点在中序数组中的位置
        int rootIndex = inLeft;
        while (rootIndex <= inRight && inorder[rootIndex] != rootValue) rootIndex++;
        if (rootIndex > inRight) // 在中序中没有找到根结点
            throw new Exception("Invalid input.");
        // 3.划分左右子树
        int leftLength = rootIndex - inLeft; // 左子树节点个数
        int preSplit = preLeft + leftLength; // 前序数组中左右子树的划分点，
        if (leftLength > 0) {// 左子树有节点，递归构造左子树
            root.leftNode = constructCore(preorder, preLeft + 1, preSplit,
                    inorder, inLeft, rootIndex - 1);
        }
        if (leftLength < preRight - preLeft) {// ==> preSplit < preRight。右子树有节点，同理递归
            root.rightNode = constructCore(preorder, preSplit + 1, preRight,
                    inorder, rootIndex + 1, inRight);
        }
        return root;
    }

    public static void main(String[] args) {
        Test1();
        Test2();
        Test3();
        Test4();
        Test5();
        Test6();
        Test7();
    }

    public static class BinaryTreeNode {
        public int value;
        public BinaryTreeNode leftNode;
        public BinaryTreeNode rightNode;

        public BinaryTreeNode(int value) {
            this.value = value;
        }
    }


    // ====================测试代码====================
    static void Test(String testName, int[] preorder, int[] inorder, int length) {
        if (testName != null)
            System.out.printf("%s begins:\n", testName);

        System.out.printf("The preorder sequence is: ");
        for (int i = 0; i < length; ++i)
            System.out.printf("%d ", preorder[i]);
        System.out.printf("\n");

        System.out.printf("The inorder sequence is: ");
        for (int i = 0; i < length; ++i)
            System.out.printf("%d ", inorder[i]);
        System.out.printf("\n");

        BinaryTreeNode root = null;
        try {
            root = construct(preorder, inorder);
            BinaryTreeUtils.PrintTree(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 普通二叉树
//              1
//           /     \
//          2       3
//         /       / \
//        4       5   6
//         \         /
//          7       8
    static void Test1() {
        int length = 8;
        int[] preorder = {1, 2, 4, 7, 3, 5, 6, 8};
        int[] inorder = {4, 7, 2, 1, 5, 3, 8, 6};

        Test("Test1", preorder, inorder, length);
    }

    // 所有结点都没有右子结点
//            1
//           /
//          2
//         /
//        3
//       /
//      4
//     /
//    5
    static void Test2() {
        int length = 5;
        int[] preorder = {1, 2, 3, 4, 5};
        int[] inorder = {5, 4, 3, 2, 1};

        Test("Test2", preorder, inorder, length);
    }

    // 所有结点都没有左子结点
//            1
//             \
//              2
//               \
//                3
//                 \
//                  4
//                   \
//                    5
    static void Test3() {
        int length = 5;
        int[] preorder = {1, 2, 3, 4, 5};
        int[] inorder = {1, 2, 3, 4, 5};

        Test("Test3", preorder, inorder, length);
    }

    // 树中只有一个结点
    static void Test4() {
        int length = 1;
        int[] preorder = {1};
        int[] inorder = {1};

        Test("Test4", preorder, inorder, length);
    }

    // 完全二叉树
//              1
//           /     \
//          2       3
//         / \     / \
//        4   5   6   7
    static void Test5() {
        int length = 7;
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 5, 1, 6, 3, 7};

        Test("Test5", preorder, inorder, length);
    }

    // 输入空指针
    static void Test6() {
        Test("Test6", null, null, 0);
    }

    // 输入的两个序列不匹配
    static void Test7() {
        int length = 7;
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 8, 1, 6, 3, 7};

        Test("Test7: for unmatched input", preorder, inorder, length);
    }

}
