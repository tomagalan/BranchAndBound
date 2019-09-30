public class Item implements Comparable<Item>{

    private int id;
    private double weight, value;

    Item(int id, double weight, double value) {
        this.id = id; this.weight = weight; this.value = value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int compareTo(Item o) {
        return Double.compare(o.value / o.weight, value / weight);
    }

    double weight() {
        return weight;
    }

    double value() {
        return value;
    }

    int id() { return id;}

    public String toString() {
        return id + " : " + value + "/" + weight;
    }
}
