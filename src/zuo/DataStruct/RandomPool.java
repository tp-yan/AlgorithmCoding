package zuo.DataStruct;

import java.util.HashMap;

/**
 * 设计RandomPool结构
 * 【题目】
 * 设计一种结构，在该结构中有如下三个功能：
 * insert(key)：将某个key加入到该结构，做到不重复加入。
 * delete(key)：将原本在结构中的某个key移除。
 * getRandom()：等概率随机返回结构中的任何一个key。
 * 【要求】
 * Insert、delete和getRandom方法的时间复杂度都是O(1)。
 * <p>
 * 解：通过2个HashMap实现，一个HashMap存<K,Integer> 另一个存<Integer，K>，2个一一对应
 */
public class RandomPool {
    public static class Pool<K> {
        private HashMap<K, Integer> keyIndexMap;
        private HashMap<Integer, K> indexKeyMap;
        private int size;

        public Pool() {
            keyIndexMap = new HashMap<>();
            indexKeyMap = new HashMap<>();
            size = 0;
        }

        public void insert(K key) {
            if (!keyIndexMap.containsKey(key)) {
                keyIndexMap.put(key, size); // 索引index从0开始
                indexKeyMap.put(size++, key);
            }
        }

        public void delete(K key) {
            if (keyIndexMap.containsKey(key)) {
                int deleteIndex = keyIndexMap.get(key); // 要删除key对应的index
                int lastIndex = --size; // 最后一个index，size减一
                K lastKey = indexKeyMap.get(lastIndex);
                keyIndexMap.put(lastKey, deleteIndex);// 将keyIndexMap的最后一个key的value改为被删除key的value
                indexKeyMap.put(deleteIndex, lastKey);
                // 删除的是 keyIndexMap的key和indexKeyMap的最后一个index，保持了indexKeyMap的index是连续的
                keyIndexMap.remove(key);
                indexKeyMap.remove(lastIndex);
            }
        }

        public K getRandom() {
            if (size == 0) {
                return null;
            }
            int randomIndex = (int) (Math.random() * size);
            return indexKeyMap.get(randomIndex);
        }
    }

    public static void main(String[] args) {
        Pool<String> pool = new Pool<String>();
        pool.insert("zhang");
        pool.insert("san");
        pool.insert("wu");
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
        System.out.println(pool.getRandom());
    }
}
