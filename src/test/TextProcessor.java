package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextProcessor {
    private static final int ALPHABET_SIZE = 26;

    public static double[] processText(String text) {
        double[] letterFrequencies = new double[ALPHABET_SIZE];
        int totalLetters = 0;

        // Konwersja na małe litery i zliczanie
        text = text.toLowerCase();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                letterFrequencies[c - 'a']++;
                totalLetters++;
            }
        }

        // Normalizacja normą euklidesową
        if (totalLetters > 0) {
            double norm = 0;
            for (double freq : letterFrequencies) {
                norm += freq * freq;
            }
            norm = Math.sqrt(norm);

            for (int i = 0; i < ALPHABET_SIZE; i++) {
                letterFrequencies[i] /= norm;
            }
        }

        return letterFrequencies;
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