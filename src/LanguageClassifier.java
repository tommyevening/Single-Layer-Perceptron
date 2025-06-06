import java.io.File;
import java.io.IOException;
import java.util.*;

/*
Klasa odpoweidzialna za klsyfikowanie teksu do odpowiedniego języka
 */
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

        this.model = new Model(
                alpha,
                theta,
                1,
                languages.size()
        );
    }

    /*
    Detektowanie języków na podstawie katalogów z danymi
     */
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


    /*
    Tworzenie mapowania języków na indeksy
     */
    private Map<String, Integer> createLanguageMapping() {
        Map<String, Integer> mapping = new HashMap<>();
        for (int i = 0; i < languages.size(); i++) {
            mapping.put(languages.get(i), i);
        }
        return mapping;
    }

    /*
    Trenowanie modelu na podstawie danych uczących
     */
    public void train(String trainingDataPath, int epochs) {
        List<DataPoint> trainingData = loadData(trainingDataPath);
        model.setEpochs(epochs);
        model.fit(trainingData);
    }

    /*
    Testowanie modelu na podstawie danych testowych
     */
    public double test(String testDataPath) {
        List<DataPoint> testData = loadData(testDataPath);
        int correct = 0;
        int total = testData.size();

        for (DataPoint dp : testData) {
            int predicted = model.predict(dp.getFeatures());
            if (predicted == dp.getLanguageIndex()) {
                correct++;
            }
        }

        return (double) correct / total;
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

    /*
    Klasyfikuj tekst do odpowiedniego języka
     */
    public String classifyText(String text) {
        double[] features = TextProcessor.processText(text);
        int predictedIndex = model.predict(features);
        return languages.get(predictedIndex);
    }

    public List<String> getLanguages() {
        return new ArrayList<>(languages);
    }
}