import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SacADos {

    public static double bestValueGlobal = 0;

    public static void main(String[] args) throws Exception {

        File file = new File("sacADos.txt");
        Scanner sc = new Scanner(file);

        ArrayList<Obj> objects = new ArrayList<>();

        double totalWeight = Integer.parseInt(sc.nextLine());
        int id = 1;

        while (sc.hasNextLine()) {
            double weight = Integer.parseInt(sc.next());
            double value = Integer.parseInt(sc.next());
            objects.add(new Obj(id, weight, value));
            id ++;
        }

        Collections.sort(objects);

        /*System.out.println("Valeur optimale : " + branchAndBound(objects, 0, totalWeight));*/
        branchAndBound2(objects, 0, totalWeight, 0);
        System.out.println("Valeur optimale : " + bestValueGlobal);
    }

    private static double glouton(ArrayList<Obj> objects, int index, double remaining) {

        double upperBound = 0;

        for(int i = index; i< objects.size(); i++) {
            if(objects.get(i).weight() <= remaining) {
                upperBound += objects.get(i).value();
                remaining -= objects.get(i).weight();
            }
            else {
                upperBound += (remaining / objects.get(i).weight()) * objects.get(i).value();
                break;
            }
        }
        return upperBound;
    }

    private static double branchAndBound(ArrayList<Obj> objects, int index, double remaining) {

        double rightValue, leftValue = 0;

        if(index >= objects.size()) return 0;

        if(objects.get(index).weight() <= remaining) {
            leftValue = objects.get(index).value() + branchAndBound(objects, index + 1, remaining - objects.get(index).weight());
        }
        rightValue = branchAndBound(objects, index + 1, remaining);

        if(leftValue >= rightValue) return leftValue;
        return rightValue;
    }

    private static void branchAndBound2(ArrayList<Obj> objects, int index, double remaining, double bestValue) {

        if(index >= objects.size()) {
            if (bestValue >= bestValueGlobal) bestValueGlobal =  bestValue;
            return;
        }

        if(glouton(objects, index, remaining) + bestValue <= bestValueGlobal) return;

        if(objects.get(index).weight() <= remaining) {
            branchAndBound2(objects, index + 1, remaining - objects.get(index).weight(), bestValue + objects.get(index).value());
        }
        branchAndBound2(objects, index + 1, remaining, bestValue);

        return;
    }
}
