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