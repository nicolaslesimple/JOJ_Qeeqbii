package ch.epfl.sweng.qeeqbii.cancer.query;

import static java.lang.Double.MAX_VALUE;

public class RatcliffQueryCancerDB extends QueryCancerDataBase {

    private double threshold;

    public RatcliffQueryCancerDB() {
        super();
        threshold = 0.8;
    }

    public RatcliffQueryCancerDB(Double threshold) {
        super();
        if (threshold == null) {
            throw new IllegalArgumentException("Error: null threshold provided in RatcliffQueryCancerDB.\n");
        }
        this.threshold = threshold;
    }

    @Override
    protected double computeDistance(String queriedSubstance, String currentMatch) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Provided negative threshold to RatcliffQueryCancerDB");
        }
        RatcliffObershelpSimilarity ratcliffComputer = new RatcliffObershelpSimilarity();
        double distance = (double) ratcliffComputer.ratcliff(queriedSubstance, currentMatch);
        if (distance < threshold) {
            return MAX_VALUE;
        }
        return 1-distance;
    }
}
