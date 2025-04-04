import java.util.List;


/*
    Klasa implementujaca model klasyfikacji wieloklasowej

 */
public class Model {
    private Perceptron[] perceptrons;
    private int epochs;
    private static final int FEATURE_SIZE = 26;

    public Model(double alpha, double theta, int epochs, int classes) {
        this.epochs = epochs;
        perceptrons = new Perceptron[classes];
        for(int i = 0; i < classes; i++) {
            perceptrons[i] = new Perceptron(FEATURE_SIZE, alpha);
            perceptrons[i].setTheta(theta);
        }
    }



    /*
    Trenowanie modelu na zbiorze danych treningowych
     */
    public void fit(List<DataPoint> trainingData) {
        //Trening kazdego perceptronu
        for (int i = 0; i < perceptrons.length; i++) {
            final int currentClass = i;


            List<DataPoint> binaryData = trainingData.stream()
                    .map(dp -> new DataPoint(
                            dp.getFeatures(),
                            dp.getLanguage(),
                            dp.getLanguageIndex() == currentClass ? 1 : 0))
                    .collect(java.util.stream.Collectors.toList());

            // Trenowanie pojedynczego perceptronu
            System.out.println("Trenowanie perceptronu dla klasy " + i);
            perceptrons[i].learn(binaryData, epochs);
        }
    }

    /*
    Predykcja klasy
     */
    public int predict(double[] features) {
        double maxOutput = Double.NEGATIVE_INFINITY;
        int predictedClass = -1;

        for (int i = 0; i < perceptrons.length; i++) {
            // Oblicz wartość net dla perceptronu i uwzględnij próg
            double output = perceptrons[i].computeNet(features);
            if (output > maxOutput) {
                maxOutput = output;
                predictedClass = i;
            }
        }

        return predictedClass;
    }

    public void setEpochs(int epochs) {
        this.epochs = epochs;
    }
}