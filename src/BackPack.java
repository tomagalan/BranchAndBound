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

        // Remove annotation to number items according to their ratio
        /*for(int i =0; i < items.size(); i++) {
            items.get(i).setId(i);
        }*/

        // Complete search
        // System.out.println("Optimum value: " + branchAndBound(items, 0, totalWeight));

        // Optimized search
        System.out.print("Intermediate steps for a better solution: ");
        branchAndBoundOptimized(items, 0, totalWeight, 0, new ArrayList<>());

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

    /** Simple search for the optimum value of a bag
     *
     * @param items List of potential items to put in the bag
     * @param index Index of the current considered item
     * @param remaining Remaining space (weight) in the bag
     * @return The optimum value we can get from the bag
     */
    private static double branchAndBound(ArrayList<Item> items, int index, double remaining) {

        double rightValue, leftValue = 0;

        if(index >= items.size()) return 0;

        if(items.get(index).weight() <= remaining) {
            leftValue = items.get(index).value() + branchAndBound(items, index + 1, remaining - items.get(index).weight());
        }
        rightValue = branchAndBound(items, index + 1, remaining);

        return Math.max(leftValue, rightValue);
    }

    /** Optimized search for the best value of a bag (using glutton in order to avoid exploring useless solutions)
     *
     * @param items List of potential items to put in the bag
     * @param index Index of the current considered item
     * @param remaining Remaining space (weight) in the bag
     * @param bestValue Best value found during the current run
     * @param selectedItems Current list of selected items (corresponding to the current best value)
     */
    private static void branchAndBoundOptimized(ArrayList<Item> items, int index, double remaining, double bestValue, ArrayList<Integer> selectedItems) {

        if(index >= items.size()) {
            if (bestValue >= bestValueGlobal){
                System.out.print((int) bestValueGlobal + " ");
                bestValueGlobal =  bestValue;
                selectedItemsGlobal = new ArrayList<>(selectedItems);
            }
            return;
        }

        if(glutton(items, index, remaining) + bestValue <= bestValueGlobal) {
            cutNumber ++;
            return;
        }

        if(items.get(index).weight() <= remaining) {
            selectedItems.add(items.get(index).id());
            branchAndBoundOptimized(items, index + 1, remaining - items.get(index).weight(), bestValue + items.get(index).value(), selectedItems);
            selectedItems.remove(selectedItems.size() - 1);
        }
        branchAndBoundOptimized(items, index + 1, remaining, bestValue, selectedItems);
    }
}
