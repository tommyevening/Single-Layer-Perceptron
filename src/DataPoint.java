public class DataPoint {
    private final double[] features;
    private final String language;
    private final int languageIndex;

    public DataPoint(double[] features, String language, int languageIndex) {
        this.features = features.clone();
        this.language = language;
        this.languageIndex = languageIndex;
    }

    public DataPoint(double[] features, int expectedOutput) {
        this.features = features.clone();
        this.language = String.valueOf(expectedOutput);
        this.languageIndex = expectedOutput;
    }

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