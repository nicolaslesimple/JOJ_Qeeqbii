package ch.epfl.sweng.qeeqbii.cancer.query;

import info.debatty.java.stringsimilarity.Levenshtein;

public class LevenshteinQueryCancerDB extends QueryCancerDataBase {

    public LevenshteinQueryCancerDB() {
        super();
    }

    public LevenshteinQueryCancerDB(int maxLevenshteinDistance) {
        super(maxLevenshteinDistance);
    }

    @Override
    protected double computeDistance(String queriedSubstance, String currentMatch) {
        Levenshtein levenshtein = new Levenshtein();
        return levenshtein.distance(queriedSubstance, currentMatch);
    }
}
