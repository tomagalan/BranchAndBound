import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SacADos {

    public static double bestValueGlobal = 0;
    public static ArrayList<Integer> selectedObjectsGlobal = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        File file = new File("sac0.txt");
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

        // Enlever les commentaires pour un parcourt complet de l'arbre
        //System.out.println("Valeur optimale : " + branchAndBound(objects, 0, totalWeight));

        // Parcours optimisé des solutions
        branchAndBoundOpti(objects, 0, totalWeight, 0, new ArrayList<Integer>());
        System.out.println("Valeur optimale : " + bestValueGlobal);
        System.out.println("Objets sélectionnés : " + selectedObjectsGlobal);
    }

    /* Algorithme glouton, retourne une borne supérieure de la meilleure valeur
    que l'on peut obtenir d'un sac */
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

    /* Parcours de l'arbre simple */
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

    /* Parcours de l'arbre optimisé avec utilisation de l'algorithme glouton à chaque noeud pour éviter
    l'exploration de branches inutiles */
    private static void branchAndBoundOpti(ArrayList<Obj> objects, int index, double remaining, double bestValue, ArrayList<Integer> selectedObjects) {

        if(index >= objects.size()) {
            if (bestValue >= bestValueGlobal){
                bestValueGlobal =  bestValue;
                selectedObjectsGlobal = new ArrayList<>(selectedObjects);
            }
            return;
        }

        if(glouton(objects, index, remaining) + bestValue <= bestValueGlobal) return;

        if(objects.get(index).weight() <= remaining) {
            selectedObjects.add(objects.get(index).id());
            branchAndBoundOpti(objects, index + 1, remaining - objects.get(index).weight(), bestValue + objects.get(index).value(), selectedObjects);
            selectedObjects.remove(selectedObjects.size() - 1);
        }
        branchAndBoundOpti(objects, index + 1, remaining, bestValue, selectedObjects);

        return;
    }
}
