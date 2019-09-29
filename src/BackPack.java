import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BackPack {

    private static double bestValueGlobal = 0;
    private static ArrayList<Integer> selectedItemsGlobal = new ArrayList<>();
    private static int cutNumber = 0;

    public static void main(String[] args) throws Exception {

        File file = new File("bag4.txt");
        Scanner sc = new Scanner(file);

        ArrayList<Item> items = new ArrayList<>();

        double totalWeight = Integer.parseInt(sc.nextLine());
        int id = 1;

        while (sc.hasNextLine()) {
            double weight = Integer.parseInt(sc.next());
            double value = Integer.parseInt(sc.next());
            items.add(new Item(id, weight, value));
            id ++;
        }

        Collections.sort(items);

        // Complete search
        // System.out.println("Optimum value: " + branchAndBound(items, 0, totalWeight));

        // Optimized search
        System.out.print("Intermediate steps for a better solution: ");
        branchAndBoundOpti(items, 0, totalWeight, 0, new ArrayList<>());

        Collections.sort(selectedItemsGlobal);
        System.out.println("\nOptimum value : " + (int) bestValueGlobal);
        System.out.println("Selected items : " + selectedItemsGlobal);
        System.out.println("Number of cuts realized with glutton : " + cutNumber);
    }

    /** Returns an upper bound of the optimum value of a bag
     *
     * @param items List of potential items to put in the bag
     * @param index Index of the first item to consider
     * @param remaining Remaining space (weight) in the bag
     * @return An upper bound of the optimum value we can get from the bag
     */
    private static double glutton(ArrayList<Item> items, int index, double remaining) {

        double upperBound = 0;

        for(int i = index; i< items.size(); i++) {
            if(items.get(i).weight() <= remaining) {
                upperBound += items.get(i).value();
                remaining -= items.get(i).weight();
            }
            else {
                upperBound += (remaining / items.get(i).weight()) * items.get(i).value();
                break;
            }
        }
        return upperBound;
    }

    /* Simple search for the optimum value of a bag */
    private static double branchAndBound(ArrayList<Item> objects, int index, double remaining) {

        double rightValue, leftValue = 0;

        if(index >= objects.size()) return 0;

        if(objects.get(index).weight() <= remaining) {
            leftValue = objects.get(index).value() + branchAndBound(objects, index + 1, remaining - objects.get(index).weight());
        }
        rightValue = branchAndBound(objects, index + 1, remaining);

        return Math.max(leftValue, rightValue);
    }

    /* Optimized search for the best value of a bag (using glutton in order to avoid exploring useless solutions) */
    private static void branchAndBoundOpti(ArrayList<Item> objects, int index, double remaining, double bestValue, ArrayList<Integer> selectedObjects) {

        if(index >= objects.size()) {
            if (bestValue >= bestValueGlobal){
                System.out.print((int) bestValueGlobal + " ");
                bestValueGlobal =  bestValue;
                selectedItemsGlobal = new ArrayList<>(selectedObjects);
            }
            return;
        }

        if(glutton(objects, index, remaining) + bestValue <= bestValueGlobal) {
            cutNumber ++;
            return;
        }

        if(objects.get(index).weight() <= remaining) {
            selectedObjects.add(objects.get(index).id());
            branchAndBoundOpti(objects, index + 1, remaining - objects.get(index).weight(), bestValue + objects.get(index).value(), selectedObjects);
            selectedObjects.remove(selectedObjects.size() - 1);
        }
        branchAndBoundOpti(objects, index + 1, remaining, bestValue, selectedObjects);
    }
}
