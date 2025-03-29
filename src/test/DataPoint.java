package test;

public class DataPoint {
    private final double[] features;    // Will store normalized letter frequencies
    private final String language;      // Language label
    private final int languageIndex;    // Numerical index for the language

    public DataPoint(double[] features, String language, int languageIndex) {
        this.features = features.clone();
        this.language = language;
        this.languageIndex = languageIndex;
    }

    // Dodać konstruktor dla binarnej klasyfikacji
    public DataPoint(double[] features, int expectedOutput) {
        this.features = features.clone();
        this.language = String.valueOf(expectedOutput);
        this.languageIndex = expectedOutput;
    }

    // Dodać metodę getExpectedOutput
    public double getExpectedOutput() {
        return languageIndex;
    }

    public double[] getFeatures() {
        return features.clone();
    }

    public String getLanguage() {
        return language;
    }

    public int getLanguageIndex() {
        return languageIndex;
    }

    public int getDimension() {
        return features.length;
    }
}