public class Model {
    private Perceptron[] perceptrons;
    private int epochs;

    public Model(double alpha, double theta, int epochs, int classes) {
        this.epochs = epochs;
        perceptrons = new Perceptron[classes];
        for(int i = 0; i < classes; i++) {
            perceptrons[i] = new Perceptron(26, alpha); // 26 to FEATURE_SIZE
            perceptrons[i].setAlpha(alpha);
            perceptrons[i].setTheta(theta);
        }
    }

    public void fit(double[][] inputs, int[] outputs) {
        // Trenowanie każdego perceptronu
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                // Dla każdej klasy
                for (int j = 0; j < perceptrons.length; j++) {
                    // Przygotowanie danych dla perceptronu (one-vs-all)
                    int target = (outputs[i] == j) ? 1 : 0;

                    // Utworzenie DataPoint dla pojedynczego perceptronu
                    DataPoint dataPoint = new DataPoint(inputs[i], "", target);

                    // Trenowanie pojedynczego perceptronu
                    perceptrons[j].train(dataPoint);
                }
            }
        }
    }

    public int[] predict(double[] vector) {
        int[] decisions = new int[perceptrons.length];

        // Każdy perceptron podejmuje decyzję
        for (int i = 0; i < perceptrons.length; i++) {
            decisions[i] = perceptrons[i].classify(vector);
        }

        return decisions;
    }
}