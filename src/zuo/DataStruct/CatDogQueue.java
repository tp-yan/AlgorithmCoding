package zuo.DataStruct;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 实现一种狗猫队列的结构，要求如下：
 * 用户可以调用add方法将cat类或dog类的实例放入队列中；
 * 用户可以调用pollAll方法，将队列中所有的实例按照进队列的先后顺序依次弹出；
 * 用户可以调用pollDog方法，将队列中dog类的实例按照进队列的先后顺序依次弹出；
 * 用户可以调用pollCat方法，将队列中cat类的实例按照进队列的先后顺序依次弹出；
 * 用户可以调用isEmpty方法，检查队列中是否还有dog或cat的实例；
 * 用户可以调用isDogEmpty方法，检查队列中是否有dog类的实例；
 * 用户可以调用isCatEmpty方法，检查队列中是否有cat类的实例。
 * <p>
 * 求解：将dog和cat分别依次放在dog queue 和cat queue，概念上这2个队列形成一个队列PetQueue，将每个pet打上一个时间戳rank代表其加入
 * PetQueue的先后顺序，当要让PetQueue队首元素出列时，将dog queue 和cat queue中队首元素rank最小的出队即可
 */
public class CatDogQueue {
    public static class Pet {
        private String type;

        public Pet(String type) {
            this.type = type;
        }

        public String getPetType() {
            return this.type;
        }
    }

    public static class Dog extends Pet {
        public Dog() {
            super("dog");
        }
    }

    public static class Cat extends Pet {
        public Cat() {
            super("cat");
        }
    }

    /**
     * 因为pet class 没有rank这个属性，而在工程上往往使用库中的class是无法修改源码再编译的，且修改源码会对其他代码可能造成影，故这里
     * 使用组合的方法，将 Pet 组合在一个新类，再添加一个 rank属性
     */
    public static class PetInQueue {
        private Pet pet;
        private int rank; // 相当于时间戳

        public PetInQueue(Pet pet, int rank) {
            this.pet = pet;
            this.rank = rank;
        }

        public Pet getPet() {
            return pet;
        }

        public int getRank() {
            return rank;
        }

        public String getPetType() {
            return pet.getPetType();
        }
    }


    /**
     * 宠物队列，由 cat队列和dog队列实现
     */
    public static class PetQueue {
        private Queue<PetInQueue> catQueue;
        private Queue<PetInQueue> dogQueue;
        private int rank;

        public PetQueue() {
            this.catQueue = new LinkedList<>();
            this.dogQueue = new LinkedList<>();
            this.rank = 0;
        }

        public void add(Pet pet) {
            if (pet.getPetType().equals("dog")) {
                dogQueue.add(new PetInQueue(pet, rank++));
            } else if (pet.getPetType().equals("cat")) {
                catQueue.add(new PetInQueue(pet, rank++));
            } else {
                throw new RuntimeException("err, not cat or dog!");
            }
        }

        // 将队列中所有的实例按照进队列的先后顺序依次弹出，一次弹一个
        public Pet pollAll() {
            if (!catQueue.isEmpty() && !dogQueue.isEmpty()) {
                if (catQueue.peek().getRank() < dogQueue.peek().getRank()) {
                    return catQueue.poll().getPet();
                } else {
                    return dogQueue.poll().getPet();
                }
            } else if (!catQueue.isEmpty()) {
                return catQueue.poll().getPet();
            } else if (!dogQueue.isEmpty()) {
                return dogQueue.poll().getPet();
            } else {
                throw new RuntimeException("err, queue is empty");
            }
        }

        // 将队列中dog类的实例按照进队列的先后顺序依次弹出
        public Dog pollDog() {
            if (!dogQueue.isEmpty()) {
                return (Dog) dogQueue.poll().getPet();
            } else {
                throw new RuntimeException("Dog queue is empty");
            }
        }

        // 将队列中cat类的实例按照进队列的先后顺序依次弹出
        public Cat pollCat() {
            if (!catQueue.isEmpty()) {
                return (Cat) catQueue.poll().getPet();
            } else {
                throw new RuntimeException("Cat queue is empty");
            }
        }

        // 检查队列中是否还有dog或cat的实例；
        public boolean isEmpty() {
            return dogQueue.isEmpty() && catQueue.isEmpty();
        }

        // 检查队列中是否有dog类的实例；
        public boolean isDogEmpty() {
            return dogQueue.isEmpty();
        }

        // 检查队列中是否有cat类的实例；
        public boolean isCatEmpty() {
            return catQueue.isEmpty();
        }
    }


    public static void main(String[] args) {
        PetQueue test = new PetQueue();

        Pet dog1 = new Dog();
        Pet cat1 = new Cat();
        Pet dog2 = new Dog();
        Pet cat2 = new Cat();
        Pet dog3 = new Dog();
        Pet cat3 = new Cat();

        test.add(dog1);
        test.add(cat1);
        test.add(dog2);
        test.add(cat2);
        test.add(dog3);
        test.add(cat3);

        test.add(dog1);
        test.add(cat1);
        test.add(dog2);
        test.add(cat2);
        test.add(dog3);
        test.add(cat3);

        test.add(dog1);
        test.add(cat1);
        test.add(dog2);
        test.add(cat2);
        test.add(dog3);
        test.add(cat3);
        while (!test.isDogEmpty()) {
            System.out.println(test.pollDog().getPetType());
        }
        while (!test.isEmpty()) {
            System.out.println(test.pollAll().getPetType());
        }
    }
}
