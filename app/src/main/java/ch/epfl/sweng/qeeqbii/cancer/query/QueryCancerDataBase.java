package ch.epfl.sweng.qeeqbii.cancer.query;

import java.util.Set;

import ch.epfl.sweng.qeeqbii.cancer.CancerDataBase;
import ch.epfl.sweng.qeeqbii.cancer.CancerSubstance;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NullInputException;
import edu.gatech.gtri.bktree.BkTreeSearcher;


import static java.lang.Double.MAX_VALUE;


abstract class QueryCancerDataBase {

    private int maxLevenshteinDistance = 10;

    QueryCancerDataBase() {
        maxLevenshteinDistance = 10;
    }

    QueryCancerDataBase(int maxLevenshteinDistance) {
        if (maxLevenshteinDistance < 0) {
            throw new IllegalArgumentException("Provided negative maxLevenshteinDistance when querying the Cancer DB.\n");
        }
        this.maxLevenshteinDistance = maxLevenshteinDistance;
    }


    // private method that uses the bkTree in order to exclude matches that are too far according to the Levenshtein
    // distance.
    private Set<BkTreeSearcher.Match<? extends String>> levenshteinMatchFilter(String queried_substance)
            throws NotOpenFileException, NullInputException {
        if (!CancerDataBase.isRead()) {
            throw new NotOpenFileException("Read the carcinogenic database before trying to query it.\n");
        }
        if (queried_substance == null) {
            throw new NullInputException("Provided null string to perfectMatchQuery CancerDataBase's method.\n");
        }

        BkTreeSearcher<String> searcher = new BkTreeSearcher<>(CancerDataBase.getBkTree());
        return searcher.search(queried_substance, maxLevenshteinDistance);
    }


    // abstract method that is overriden by either LevenshteinQueryCancerDB or RatcliffQueryCancerDB
    // It must return +infinity if the distance is greater than the threshold
    abstract protected double computeDistance(String queriedSubstance, String currentMatch);


    public CancerSubstance query(String queriedSubstance)
            throws  NotOpenFileException, NullInputException {
        // We apply a Levenshtein filter to restrict the number of matches
        Set<BkTreeSearcher.Match<? extends String>> matches = levenshteinMatchFilter(queriedSubstance);
        // We go through all the matches with distance less or equal than max_distance and select the match
        // that involves the least distance
        // initialize minDist to +infinity
        double minDist = MAX_VALUE;
        double currentDist;
        String currentMatchName;
        String keptMatch = null;
        for (BkTreeSearcher.Match<? extends String> match : matches) {
            currentMatchName = match.getMatch();
            // abstract method that is overriden by either LevenshteinQueryCancerDB or RatcliffQueryCancerDB
            currentDist = computeDistance(queriedSubstance, currentMatchName);
            if (currentDist < minDist) {
                keptMatch = currentMatchName;
                minDist = currentDist;
            }
        }

        // We check that there is indeed a match with distance less than max_distance and if not we output
        // an empty CancerSubstance
        CancerSubstance output_substance = new CancerSubstance();
        if (keptMatch == null) {
            // Output the substance as an empty CancerSubstance (Query failed)
            return output_substance;
        }
        output_substance = CancerDataBase.getSubstanceByName(keptMatch);
        return output_substance;
    }
}
