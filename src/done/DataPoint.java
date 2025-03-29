package done;

public class DataPoint {
    private final double[] features;
    private final int expectedOutput;

    public DataPoint(double[] features, int expectedOutput) {
        this.features = features.clone();
        this.expectedOutput = expectedOutput;
    }

    public double[] getFeatures() {
        return features.clone();
    }

    public int getExpectedOutput() {
        return expectedOutput;
    }

    public int getDimension() {
        return features.length;
    }
}