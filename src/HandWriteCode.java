import algorithm.MyUtils;

import java.util.Arrays;

public class HandWriteCode {
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 1.冒泡排序：依次比较相邻元素
    // 最好==最差==O(N^2)
    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        for (int end = arr.length - 1; end > 0; end--) {
            for (int j = 0; j < end; j++) {
                if (arr[j] > arr[j + 1])
                    swap(arr, j, j + 1);
            }
        }
    }

    // 2.选择排序：每次选择剩余数组的最小/大值放到前面
    // 最好==最差==O(N^2)
    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        // 选择的最大/最小值存放位置
        for (int index = 0; index < arr.length - 1; index++) {
            int minIndex = index; // 保存本次遍历最小元素位置
            for (int j = index + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex])
                    minIndex = j;
            }
            swap(arr, index, minIndex); // 将最小元素交换到指定位置
        }
    }

    // 3.插入排序：规定左边的数组是有序，判断下一个待排元素插入到有序数组部分的哪个位置
    // 最好：O(N)，最差：O(N^2)
    public static void insertionSort(int[] arr) {
        if (arr == null || arr.length < 2) return;
        for (int i = 1; i < arr.length; i++) {// i:指示剩余未排序数组的开头元素
            // j+1:指向待排元素的插入位置，这里通过寻找插入位置时就移动元素来进行插入。j:就是需要向前比较的元素位置
            // 同时条件 arr[j] > arr[j+1]：保证了找到最终插入位置就立即结束
            for (int j = i - 1; j >= 0 && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }

    }

    // 三大核心排序，时间复杂度都是 O(NlogN)
    // 4.归并排序!
    // 空间复杂度：O(N)
    // 递归：利用栈数据结构实现。递归调用：共享同一个处理流程，只是处理数据不同。
    // 时间复杂度： T(N) = 2T(N/2) + O(N) 。O(N)：合并操作耗时
    static class MergeSort {
        public static void mergeSort(int[] arr) {
            if (arr == null || arr.length < 2) return;
            sortProcess(arr, 0, arr.length - 1);
        }

        // 递归过程
        public static void sortProcess(int[] arr, int l, int r) {
            if (l == r) return; // 只有一个元素，不用排序
            int mid = l + (r - l) >> 2; // 保证不会溢出，(l+r)/2也许求和会溢出
            sortProcess(arr, l, mid);
            sortProcess(arr, mid + 1, r);
            merge(arr, l, mid, r);
        }

        // 合并2段有序子数组
        public static void merge(int[] arr, int l, int m, int r) {
            int[] tmp = new int[r - l + 1]; // 暂存合并后的数组
            // 辅助数组指针，左右数组指针
            int index = 0;
            int pL = l;
            int pR = m + 1;

            while (pL <= m && pR <= r) {
                tmp[index++] = arr[pL] < arr[pR] ? arr[pL++] : arr[pR++];
            }
            // 最后总有一个指针越界
            while (pL <= m) {
                tmp[index++] = arr[pL++];
            }
            while (pR <= r) {
                tmp[index++] = arr[pR++];
            }
            // 覆盖那段数组
            for (int i = 0; i < tmp.length; i++) {
                arr[l + i] = tmp[i];
            }
        }

        // 4.1 使用循环实现非递归版的归并排序
        public static void mergeUseCycle(int[] arr) {
            if (arr == null || arr.length < 2) return;
            int k = 2; // 每相邻k个元素外排即合并
            while (true) {
                for (int left = 0; left < arr.length; ) {
                    if (arr.length - left <= k / 2) // 剩下的数组只有一个子数组，无需合并，如 k = 2 剩余：[1]。k=4，剩余：[2,3]等
                        break;
                    int right = left + k - 1;
                    int mid = left + (right - left) / 2;
                    if (right >= arr.length) {// 说明此段元素不够k个， k=4,剩余：[1,2,-1]，两个有序子数组:[1,2]，[-1]
                        right = arr.length - 1;
                        mid = left + k / 2 - 1;
                    }
                    merge(arr, left, mid, right);
                    left += k;
                }
                if (k > arr.length)
                    break;
                k *= 2;
            }
        }

        // 4.2 归并排序思想扩展：
        /* (1) 求最小和：概念转换。求每个元素之前比它小的元素之和的和。等价于：求每个元素A其右边比它大的元素个数n的乘积累加： A*n + B*m + ...
        算法加速基础：在合并时求右边子数组中大于左边子数组中的某个数A的元素个数时，因为子数组是有序的，若右子数组中某个元素B大于A，则B之后的元素也必定大于A，且已知B的索引和右数组右边界，那么 右边界-B索引+1 就得到了所有大于A的元素个数！
        
        */
        public static int smallSum(int[] arr) {
            if (arr == null || arr.length < 2) return 0;
            return sumSortProcess(arr, 0, arr.length - 1);
        }

        public static int sumSortProcess(int[] arr, int l, int r) {
            if (l == r) return 0;
            int mid = l + (r - l) / 2;
            return sumSortProcess(arr, l, mid) // 子数组得到的小和+合并2个子数组产生的小和
                    + sumSortProcess(arr, mid + 1, r)
                    + sumMerge(arr, l, mid, r);
        }

        public static int sumMerge(int[] arr, int l, int mid, int r) {
            int[] tmp = new int[r - l + 1];
            int index = 0;
            int pL = l;
            int pR = mid + 1;
            int sum = 0;

            while (pL <= l && pR <= r) {
                // 比原始归并排序合并，只增加了这行代码
                sum += arr[pL] < arr[pR] ? (r - pR + 1) * arr[pL] : 0; // 根据索引快速计算个数
                tmp[index++] = arr[pL] < arr[pR] ? arr[pL++] : arr[pR++];
            }
            while (pL <= mid) {
                tmp[index++] = arr[pL++];
            }
            while (pR <= r) {
                tmp[index++] = arr[pR++];
            }
            for (int i = 0; i < tmp.length; i++) {
                arr[l + i] = tmp[i];
            }
            return sum;
        }

        // (2) 求逆序对个数：与“最小和”同理。相当于在合并时，求右边数组小于左边数组某个元素的个数再累加。实际上就是求每个元素之前比它大的个数之和
        /* 比如序列 4 3 1 0 5 中的逆序对有：[4,3],[4,1],[4,0] [3,1] [3,0] [1,0] */
        public static int reversePairCount(int[] arr) {
            if (arr == null || arr.length < 2) return 0;
            return reverseSortProcess(arr, 0, arr.length - 1);
        }

        public static int reverseSortProcess(int[] arr, int l, int r) {
            if (l == r) return 0;
            int mid = l + (r - l) / 2;
            return reverseSortProcess(arr, l, mid)
                    + reverseSortProcess(arr, mid + 1, r)
                    + reverseMerge(arr, l, mid, r);
        }

        public static int reverseMerge(int[] arr, int l, int mid, int r) {
            int[] tmp = new int[r - l + 1];
            int index = 0;
            int pL = l;
            int pR = mid + 1;
            int count = 0;

            while (pL <= mid && pR <= r) {
                // 记住：一定要比较小值，因为遇到小值就被插入到辅助数组了！！！
                // 一旦右边的某个数A小于左边数B，则左数组中A之后的元素都比B大
                count += arr[pR] < arr[pL] ? (mid - pL + 1) : 0; // 根据索引快速计算个数
                tmp[index++] = arr[pL] < arr[pR] ? arr[pL++] : arr[pR++];
            }
            while (pL <= mid) {
                tmp[index++] = arr[pL++];
            }
            while (pR <= r) {
                tmp[index++] = arr[pR++];
            }
            for (int i = 0; i < tmp.length; i++) {
                arr[l + i] = tmp[i];
            }
            return count;
        }
    }


    // 5.快速排序!
    /* 空间复杂度：O(logN)
     * 经典快排：以最后一个数A作为划分元素，小于A的数放左边，大于A的数放右边，A在中间。
     * 一次划分：时间O(N),空间O(1)。只需确定一个小于等于区(用一个索引pointer指示其右边界)，小于等于区里的元素 <= 划分元素A，每当遍历索引index找到一个比A小于等于的数B，就将B与小于等于之后的第一个数交换
     * 然后小于等于区索引pointer++，在pointer与index之间的数一定大于划分元素A
     * 空间复杂度：O(logN)：每次划分后都要记住划分点位置，然后再进行左递归，这个位置必须左递归完了，才能进行右递归。
     * 所以每次递归都要记住这个位置，相当于将左数组进行不精确的二分，还记住划分点，最差就是O(N)。
     * */
    static class QuickSort {
        public static void quickSortShell(int[] arr) {
            if (arr == null || arr.length < 2) return;
            quickSort(arr, 0, arr.length - 1);
        }

        public static void quickSort(int[] arr, int l, int r) {
            if (l < r) {// 递归结束：l >= r
                // 随机选择划分元素，交换后再经典快排
                swap(arr, l + (int) (Math.random() * (r - l + 1)), r);
                // 使用荷兰国旗划分对于划分元素有多个时，效率更高
                int[] bounds = netherlandsPartition(arr, l, r); // 返回相等区域边界
                // 小于区、大于区再递归快排
                quickSort(arr, l, bounds[0] - 1);
                quickSort(arr, bounds[1] + 1, r);
            }
        }

        // 数组的一次划分：使用数组最后一个作为划分元素，返回划分后划分元素的位置
        public static int partition(int[] arr, int l, int r) {
            int less = l - 1; // 只用指示小于等于区的右边界即可
            while (l <= r) { // 以 l 作为遍历指针
                if (arr[l] <= arr[r]) {
                    swap(arr, ++less, l++); // 将当前指示元素与小于等于区的后一个素交换，然后右边界右移
                } else l++; // 若大于划分元素，则指针直接后移
            }
            return less;
        }

        // 5.1 快排扩展题：荷兰国旗问题：以末尾元素作为划分元素，使得数组：[小于区，等于划分元素区，大于区]，但各段不保证有序！
        // 需要小于区和大于区指针
        public static int[] netherlandsPartition(int[] arr, int l, int r) {
            int less = l - 1;
            int more = r; // 将末尾划分元素直接划入大于区，最后再与大于区首元素交换位置。这样保证划分元素是等于区的最后一个。

            while (l < more) {// l作为遍历指针，当遇到大于区边界时停止
                if (arr[l] < arr[r]) {// 小于划分元素。末尾划分元素一直不动，直到最后调整
                    swap(arr, ++less, l++); // 与小于区后元素交换，同时遍历指针后移
                } else if (arr[l] > arr[r]) {// 大于划分元素
                    swap(arr, --more, l); // 与大于前面一个交换，遍历指针不移动！因为从后面换到前面的元素 不知其与划分元素的大小
                } else l++; // 等于时，直接后移
            }
            swap(arr, more, r); // 将末尾划分元素与大于区首元素交换，然后大于区左边界后移
            return new int[]{less + 1, more}; // 返回等于区边界
        }
    }

// 6.堆排序!

    /**
     * 空间复杂度：O(1)
     * 堆结构：概念上是完全二叉树，一定是从上往下从左往右依次添加节点。而**实现堆的真实结构是数组**。通过数组下标越界限定树结构。
     * - 已知父节点索引i，子节点索引：左2*i+1、右2*i+2
     * - 已知子节点索引i，其父节点索引：(i-1)/2
     * - 大顶堆：每棵树的最大值位于根结点，子节点一定小于等于父节点，每棵子树也是，而兄弟节点大小关系不定。小顶堆：类似的堆顶为最小值。
     * <p>
     * 建立大顶堆：从首元素开始将数组元素依次插入到堆的末尾，然后从下往上调整堆，将新加节点不断与父节点比较，直到没有节点交换值(即符合大顶堆结构)或到堆顶停止。
     * 大顶堆可能是无序的。故构建大顶堆后再需要再调整：将堆顶元素与堆最后一个交换后，堆大小减一，相当于排除顶堆元素(实质就将最大元素放到数组末尾了)。然后再从堆顶开始从上往下调整堆，若子节点比父节点大就交换，直到堆底或没有节点交换值。当堆大小减为0时，数组就是有序的了。
     * 堆排序：
     */
    static class HeapSort {

        public static void heapSort(int[] arr) {
            if (arr == null || arr.length < 2) return;
            // 1.建立大顶堆
            for (int i = 0; i < arr.length; i++) {
                heapInsert(arr, i);
            }
            // 2.不断去掉堆顶元素排序
            int heapSize = arr.length; // 指示堆大小
            swap(arr, 0, --heapSize); // 堆顶元素与最后一个元素交换，同时堆大小减一
            while (heapSize > 0) {
                heapify(arr, 0, heapSize); // 调整后还是大顶堆
                swap(arr, 0, --heapSize); // 重复直到堆为0
            }
        }

        /**
         * 新节点插入堆，然后从下往上调整为大顶堆
         *
         * @param arr
         * @param index 插入节点索引
         */
        public static void heapInsert(int[] arr, int index) {
            while (arr[index] > arr[(index - 1) / 2]) {// 插入节点比父节点大，则交换
                swap(arr, index, (index - 1) / 2);
                index = (index - 1) / 2; // index指向父节点
            }
        }

        /**
         * 堆调整：某个元素不符合大顶堆结构，从上往下调整
         *
         * @param arr
         * @param index    元素索引
         * @param heapSize 堆大小
         */
        public static void heapify(int[] arr, int index, int heapSize) {
            int left = 2 * index + 1;
            while (left < heapSize) {
                // 较大子节点索引
                int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
                if (arr[largest] > arr[index]) {// 子节点比父节点大
                    swap(arr, largest, index);
                    index = largest;
                    left = 2 * index + 1; // 左子节点
                }else break; // 当2个子节点都比父节点小时，已是大顶堆结构
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = MyUtils.generateRandomArray(20, 20);
        int[] arrCopy = arr.clone();
        System.out.println("before netherlandsPartition:");
        System.out.println(Arrays.toString(arr));

        int[] pos = QuickSort.netherlandsPartition(arr, 0, arr.length - 1);
        System.out.println("after netherlandsPartition:");
        System.out.println(Arrays.toString(arr));
        System.out.println("pos:" + Arrays.toString(pos));
        QuickSort.quickSortShell(arrCopy);
        System.out.println(Arrays.toString(arrCopy));

        int[] arr2 = MyUtils.generateRandomArray(20, 20);
        System.out.println("arr2 before:" + Arrays.toString(arr2));
        HeapSort.heapSort(arr2);
        System.out.println("arr2 after:" + Arrays.toString(arr2));
    }
}
