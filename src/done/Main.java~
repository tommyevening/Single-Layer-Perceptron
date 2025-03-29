package done;

import java.io.*;
import java.util.*;

public class Main {
    private static final String TRAIN_PATH = "assets/trainData.txt";
    private static final String TEST_PATH = "assets/testData.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static Perceptron trainedPerceptron = null;

    public static void main() {
        try {
            System.out.println("Perceptron");
            System.out.print("Podaj nazwę pierwszej klasy: \nIris-setosa");
            String class1 = scanner.nextLine();

            System.out.print("Podaj nazwę drugiej klasy: \nIris-versicolor");
            String class2 = scanner.nextLine();

            List<DataPoint> trainData = loadDataFromFile(TRAIN_PATH, class1, class2);
            List<DataPoint> testData = loadDataFromFile(TEST_PATH, class1, class2);

            if (trainData.isEmpty() || testData.isEmpty()) {
                throw new IllegalStateException("Brak danych dla podanych klas");
            }

            while (true) {
                System.out.println("\n1. Trenuj model");
                System.out.println("2. Testuj model");
                System.out.println("3. Klasyfikuj wektor");
                System.out.println("4. Wyjście");
                System.out.print("Wybór: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        trainedPerceptron = trainModel(trainData);
                        break;
                    case 2:
                        if (trainedPerceptron == null) {
                            System.out.println("Najpierw wytrenuj model (opcja 1)");
                        } else {
                            testModel(trainedPerceptron, testData);
                        }
                        break;
                    case 3:
                        if (trainedPerceptron == null) {
                            System.out.println("Najpierw wytrenuj model (opcja 1)");
                        } else {
                            classifyNewVector(trainData, trainedPerceptron, class1, class2);
                        }
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Błędny wybór");
                }
            }
        } catch (Exception e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }

    private static Perceptron trainModel(List<DataPoint> trainData) {
        System.out.print("Współczynnik uczenia (0.01-1.0): ");
        double alpha = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Próg decyzyjny (0-10): ");
        int theta = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Liczba epok (10-1000): ");
        int epochs = scanner.nextInt();
        scanner.nextLine();

        int dimension = trainData.getFirst().getFeatures().length;
        Perceptron perceptron = new Perceptron(dimension, alpha);
        perceptron.setTheta(theta);

        Collections.shuffle(trainData, new Random());
        perceptron.learn(trainData, epochs);

        System.out.println("Model został wytrenowany");
        return perceptron;
    }

    private static void testModel(Perceptron perceptron, List<DataPoint> testData) {
        double accuracy = calculateAccuracy(perceptron, testData);
        System.out.printf("Dokładność modelu: %.2f%%%n", accuracy * 100);
    }

    private static double calculateAccuracy(Perceptron perceptron, List<DataPoint> testData) {
        int correct = 0;
        for (DataPoint point : testData) {
            if ((int) perceptron.compute(point) == point.getExpectedOutput()) {
                correct++;
            }
        }
        return (double) correct / testData.size();
    }

    private static void classifyNewVector(List<DataPoint> trainData, Perceptron perceptron, String class1, String class2) {
        int dimension = trainData.getFirst().getFeatures().length;

        while (true) {
            System.out.println("\nWprowadź wektor (wpisz 'exit' aby wyjść)");
            double[] features = new double[dimension];

            try {
                for (int i = 0; i < dimension; i++) {
                    System.out.printf("Podaj cechę %d z %d: ", i + 1, dimension);
                    String input = scanner.nextLine();

                    if (input.equalsIgnoreCase("exit")) {
                        return;
                    }

                    input = input.replace(",", ".");
                    features[i] = Double.parseDouble(input);
                }

                DataPoint newPoint = new DataPoint(features, 0);
                int result = (int) perceptron.compute(newPoint);
                System.out.println("\nKlasa: " + (result == 1 ? class1 : class2));

                System.out.println("\nCzy chcesz sklasyfikować kolejny wektor? (tak/nie)");
                String answer = scanner.nextLine();
                if (!answer.toLowerCase().startsWith("t")) {
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Błędne dane - wprowadź liczbę");
            }
        }
    }

    private static List<DataPoint> loadDataFromFile(String filename, String class1, String class2) {
        List<DataPoint> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                String className = elements[elements.length - 1];

                if (!className.equals(class1) && !className.equals(class2)) {
                    continue;
                }

                double[] features = new double[elements.length - 1];
                for (int i = 0; i < elements.length - 1; i++) {
                    features[i] = Double.parseDouble(elements[i]);
                }

                int expectedOutput = className.equals(class1) ? 1 : 0;
                dataList.add(new DataPoint(features, expectedOutput));
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd odczytu pliku: " + filename);
        }
        return dataList;
    }
}