package ch.epfl.sweng.qeeqbii.clustering;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.custom_exceptions.BadlyFormatedFile;
import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

public class ClusterClassifier {

    private static final Map<String, NutrientVector > mapCenters = new HashMap<>();

    private static NutrientVector mean;
    private static NutrientVector std;

    // nutrientKeys is important because it allows to keep track of the order in which the nutrients
    // are specified in the cluster_nutrient_center.csv file
    private static final ArrayList<String> nutrientKeys = new ArrayList<>();

    private static boolean openState = false;

    public static void readClusterNutrientCentersFile(Context context) throws NotOpenFileException, BadlyFormatedFile {
        if (!NutrientNameConverter.isRead()) {
            throw new NotOpenFileException("Open the nutrient_name_converter.csv file before reading the" +
                    "cluster_nutrient_centers.csv file.");
        }
        if (!openState) {
            Resources resources = context.getResources();
            InputStream inStream;
            if (TestChecker.isRunningTest("ch.epfl.sweng.qeeqbii.clustering.ClusterClassifierTest")) {
                inStream = resources.openRawResource(R.raw.cluster_nutrient_centers_test);
            }
            else {
                inStream = resources.openRawResource(R.raw.cluster_nutrient_centers);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));
            try {
                // Step over the header and get the list of nutrients available
                String line = reader.readLine();
                String[] headerArray = line.split(",");
                // We slice headerArray to only account for nutrient names
                headerArray = Arrays.copyOfRange(headerArray, 1, headerArray.length);
                String standardKey;
                for (String currentKey : headerArray) {
                    standardKey = NutrientNameConverter.convertToStandardName(currentKey);
                    nutrientKeys.add(standardKey);
                }

                boolean foundMean = false;
                boolean foundStd = false;
                while ((line = reader.readLine()) != null) {
                    String[] elements = line.split(",");

                    List<String> valuesList = Arrays.asList(Arrays.copyOfRange(elements, 1, elements.length));
                    if (elements[0].equals("MEAN")) {
                        mean = new NutrientVector();
                        for (int i = 0; i < nutrientKeys.size(); i++) {
                            double nutrientValue = Double.parseDouble(valuesList.get(i));
                            mean.setComponent(nutrientKeys.get(i), nutrientValue);
                        }
                        foundMean = true;
                    }
                    else if (elements[0].equals("STD")) {
                        std = new NutrientVector();
                        for (int i = 0; i < nutrientKeys.size(); i++) {
                            double nutrientValue = Double.parseDouble(valuesList.get(i));
                            std.setComponent(nutrientKeys.get(i), nutrientValue);
                        }
                        foundStd = true;
                    }
                    else {
                        // We get the category name and we take care of only taking the second category name
                        // In the csv the names are formated this way:
                        // /FIRST_CATEGORY/SECOND_CATEGORY -> parsing: ["", "FIRST_CATEGORY", "SECOND_CATEGORY"]
                        String[] parsed_element = elements[0].split("/");
                        String category = parsed_element[2];
                        // Check that category is defined in
                        if (!Arrays.asList(ClusterTypeSecondLevel.values()).contains(ClusterTypeSecondLevel.valueOf(category))) {
                            throw new BadlyFormatedFile("Mismatch between categories specified in cluster_nutrient_centers.csv" +
                                    " and the ClusterTypeSecondLevel enum type\n" +
                                    category + " found in cluster_nutrient_centers.csv but not in ClusterTypeSecondLevel");
                        }

                        NutrientVector nutrientVector = new NutrientVector();

                        for (int i = 0; i < nutrientKeys.size(); i++) {
                            double nutrientValue = Double.parseDouble(valuesList.get(i));
                            nutrientVector.setComponent(nutrientKeys.get(i), nutrientValue);
                        }
                        mapCenters.put(category, nutrientVector);
                    }
                }
                if (!(foundMean && foundStd)) {
                    throw new BadlyFormatedFile("The cluster_nutrient_centers.csv file do not contain" +
                            "either the MEAN or the STD line.");
                }
                openState = true;

            } catch (IOException|IllegalNutrientKeyException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    public static ArrayList<ComparableCluster> getClusterTypeFromNutrients(NutrientVector queriedVector) throws NotOpenFileException {
        // First we need to find the closest nutrient vector to the queried nutrientVector
        // To do so we will use the euclidean norm
        if (!openState) {
            throw new NotOpenFileException("Open the cluster_nutrient_centers.csv file before trying to classify" +
                    "a product using it NutrientVector.");
        }

        NutrientVector normalizedQueriedVector = normalizationWRTClusterCenters(queriedVector);

        double currentDistance;
        NutrientVector currentClusterCenter;
        ComparableCluster comparableCluster;

        // Creation of a minHeap that is able to
        PriorityQueue<ComparableCluster> minHeap = new PriorityQueue<>(10, new ComparableCluster());

        for (ClusterTypeSecondLevel cluster : ClusterTypeSecondLevel.values()) {
            // cluster (toString) "Bonbon et chewing gums"
            // We get the cluster name and we get the corresponding clusterCenter (NutrientVector) from mapCenters
            if (mapCenters.containsKey(cluster.name())) {
                currentClusterCenter = mapCenters.get(cluster.name());
                currentDistance = normalizedQueriedVector.computeDistance(currentClusterCenter);

                comparableCluster = new ComparableCluster(cluster, currentDistance);
                minHeap.add(comparableCluster);
            }
        }

        // Get the ComparableClusters that were the closest to the normalizedQueriedVector
        int nbBestClusters = 10;
        ArrayList<ComparableCluster> topClusters = new ArrayList<>();
        boolean emptyHeap = false;
        for (int i = 0; i < nbBestClusters && !emptyHeap; i++) {
            try {
                ComparableCluster currentTopCluster = minHeap.remove();
                topClusters.add(currentTopCluster);
            }
            catch (NoSuchElementException e) {
                emptyHeap = true;
            }
        }

        return topClusters;
    }








    public static boolean isRead() {
        return openState;
    }

    private static NutrientVector normalizationWRTClusterCenters(NutrientVector toNormalize)
            throws NotOpenFileException {
        return (toNormalize.diff(mean)).componentWiseDivision(std);
    }


    public static void clear() {
        mapCenters.clear();
        mean = null;
        std = null;
        nutrientKeys.clear();
        openState = false;
    }


}
