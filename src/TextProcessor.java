import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextProcessor {
    private static final int ALPHABET_SIZE = 26;

    public static double[] processText(String text) {
        double[] features = new double[26];
        text = text.toLowerCase();

        // Zliczanie wystąpień liter
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                features[c - 'a']++;
            }
        }

        // Normalizacja przy użyciu normy Euklidesowej
        double sumOfSquares = 0;
        for (double feature : features) {
            sumOfSquares += feature * feature;
        }

        // Obliczanie pierwiastka z sumy kwadratów (norma Euklidesowa)
        double euclideanNorm = Math.sqrt(sumOfSquares);

        // Normalizacja wektora (jeśli norma jest większa od 0)
        if (euclideanNorm > 0) {
            for (int i = 0; i < features.length; i++) {
                features[i] /= euclideanNorm;
            }
        }

        return features;
    }

    public static String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}