public class Obj implements Comparable<Obj>{

    private int id;
    private double weight, value;

    public Obj(int id, double weight, double value) {
        this.id = id; this.weight = weight; this.value = value;
    }

    public int compareTo(Obj o) {
        if (value / weight < o.value / o.weight) return 1;
        if(value / weight ==  o.value / o.weight) return 0;
        return -1;
    }

    public double weight() {
        return weight;
    }

    public double value() {
        return value;
    }

    public String toString() {
        return id + " : " + value + "/" + weight;
    }
}
