import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Vector<Double> vectorX = new Vector<>();
        vectorX.add(1.0);
        vectorX.add(2.0);
        vectorX.add(2.0);


        vectorX = normalizeVector(vectorX);
        for (int i = 0; i < vectorX.size(); i++) {
            System.out.println(vectorX.get(i));
        }


    }

    public static Vector<Double> normalizeVector(Vector<Double> vector) {

        Vector<Double> tempVector = new Vector<>();
        double sum = 0;
        for (int i = 0; i < vector.size(); i++) {
            sum += Math.pow(vector.get(i), 2);
        }

        for (Double val : vector) {
            tempVector.add(val / Math.sqrt(sum));
        }
        return tempVector;
    }
}