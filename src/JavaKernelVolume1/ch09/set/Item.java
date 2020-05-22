package JavaKernelVolume1.ch09.set;

import java.util.Objects;

public class Item implements Comparable<Item> {
    private int partNumber;
    private String description;

    @Override
    public int compareTo(Item o) {// 先按照partNumber排序
        int diff = Integer.compare(partNumber, o.partNumber);
        return diff == 0 ? description.compareTo(o.description) : diff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return partNumber == item.partNumber &&
                Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partNumber, description);
    }

    @Override
    public String toString() {
        return "Item{" +
                "partNumber=" + partNumber +
                ", description='" + description + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public Item( String description,int partNumber) {
        this.partNumber = partNumber;
        this.description = description;
    }
}
