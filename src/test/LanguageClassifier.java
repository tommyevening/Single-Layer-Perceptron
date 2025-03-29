package test;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LanguageClassifier {
    private final Map<String, Integer> languageToIndex;
    private final List<String> languages;
    private final MultiClassPerceptron classifier;
    private static final int FEATURE_SIZE = 26; // liczba liter w alfabecie

    public LanguageClassifier(String trainingDataPath) {
        // Automatyczne wykrywanie języków z katalogów
        this.languages = detectLanguages(trainingDataPath);
        this.languageToIndex = createLanguageMapping();

        System.out.println("Wykryte języki:");
        for (String lang : languages) {
            System.out.println("- " + lang);
        }

        // Inicjalizacja perceptronu z wykrytą liczbą klas
        this.classifier = new MultiClassPerceptron(languages.size(), FEATURE_SIZE, 0.01);
    }

    private List<String> detectLanguages(String directoryPath) {
        File directory = new File(directoryPath);
        List<String> detectedLanguages = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        detectedLanguages.add(file.getName());
                    }
                }
            }
        }

        if (detectedLanguages.isEmpty()) {
            throw new IllegalStateException("Nie znaleziono katalogów z danymi językowymi w: " + directoryPath);
        }

        Collections.sort(detectedLanguages); // dla spójnej kolejności
        return detectedLanguages;
    }

    private Map<String, Integer> createLanguageMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < languages.size(); i++) {
            mapping.put(languages.get(i), i);
        }
        return mapping;
    }

    public void train(String trainingDataPath, int epochs) {
        List<DataPoint> trainingData = loadData(trainingDataPath);
        classifier.train(trainingData, epochs);
    }

    public double test(String testDataPath) {
        List<DataPoint> testData = loadData(testDataPath);
        return evaluateAccuracy(testData);
    }

    private List<DataPoint> loadData(String directoryPath) {
        List<DataPoint> data = new ArrayList<>();
        File directory = new File(directoryPath);

        for (String language : languages) {
            File languageDir = new File(directory, language);
            if (!languageDir.exists() || !languageDir.isDirectory()) {
                continue;
            }

            File[] files = languageDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
            if (files == null) continue;

            for (File file : files) {
                try {
                    String text = TextProcessor.readTextFile(file.getPath());
                    double[] features = TextProcessor.processText(text);
                    int languageIndex = languageToIndex.get(language);
                    data.add(new DataPoint(features, language, languageIndex));
                } catch (IOException e) {
                    System.err.println("Błąd podczas wczytywania pliku: " + file.getName());
                }
            }
        }

        return data;
    }

    private double evaluateAccuracy(List<DataPoint> testData) {
        int correct = 0;
        int total = testData.size();

        // Macierz pomyłek dla szczegółowej analizy
        int[][] confusionMatrix = new int[languages.size()][languages.size()];

        for (DataPoint dp : testData) {
            int predicted = classifier.classify(dp.getFeatures());
            confusionMatrix[dp.getLanguageIndex()][predicted]++;

            if (predicted == dp.getLanguageIndex()) {
                correct++;
            }
        }

        // Wyświetlenie szczegółowych statystyk
        System.out.println("\nMacierz pomyłek:");
        System.out.print("Rzecz./Pred.\t");
        for (String lang : languages) {
            System.out.print(lang + "\t");
        }
        System.out.println();

        for (int i = 0; i < languages.size(); i++) {
            System.out.print(languages.get(i) + "\t\t");
            for (int j = 0; j < languages.size(); j++) {
                System.out.print(confusionMatrix[i][j] + "\t");
            }
            System.out.println();
        }

        return (double) correct / total;
    }

    public String classifyText(String text) {
        double[] features = TextProcessor.processText(text);
        int predictedIndex = classifier.classify(features);
        return languages.get(predictedIndex);
    }

    public List<String> getLanguages() {
        return new ArrayList<>(languages);
    }
}