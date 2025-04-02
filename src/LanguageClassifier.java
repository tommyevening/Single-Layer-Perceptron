import java.io.File;
import java.io.IOException;
import java.util.*;

public class LanguageClassifier {
    private final Map<String, Integer> languageToIndex;
    private final List<String> languages;
    private final Model model;
    private static final int FEATURE_SIZE = 26;


    public LanguageClassifier(String trainingDataPath, double alpha, double theta) {
        this.languages = detectLanguages(trainingDataPath);
        this.languageToIndex = createLanguageMapping();

        System.out.println("Wykryte języki:");
        for (String lang : languages) {
            System.out.println("- " + lang);
        }

        // Inicjalizacja modelu z parametrami
        this.model = new Model(
                alpha,           // współczynnik uczenia
                theta,          // próg aktywacji
                1,              // epochs (zostanie ustawione w metodzie train)
                languages.size() // liczba klas (języków)
        );
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

        Collections.sort(detectedLanguages);
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
        model.fit(trainingData);
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
        int[][] confusionMatrix = new int[languages.size()][languages.size()];

        for (DataPoint dp : testData) {
            int predicted = model.predict(dp.getFeatures());
            confusionMatrix[dp.getLanguageIndex()][predicted]++;

            if (predicted == dp.getLanguageIndex()) {
                correct++;
            }
        }

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
        int predictedIndex = model.predict(features);
        return languages.get(predictedIndex);
    }

    public List<String> getLanguages() {
        return new ArrayList<>(languages);
    }
}