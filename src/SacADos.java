import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SacADos {

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

        System.out.println("Borne sup : " + glouton(objects, totalWeight));
    }

    private static double glouton(ArrayList<Obj> objects, double totalWeight) {

        double remaining = totalWeight;
        double upperBound = 0;

        for(Obj object : objects) {
            if(object.weight() <= remaining) {
                upperBound += object.value();
                remaining -= object.weight();
            }
            else {
                upperBound += remaining / object.weight() * object.value();
                break;
            }
        }
        return upperBound;
    }
}
