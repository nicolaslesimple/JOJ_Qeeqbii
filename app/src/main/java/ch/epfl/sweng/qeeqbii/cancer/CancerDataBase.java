package ch.epfl.sweng.qeeqbii.cancer;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;
import ch.epfl.sweng.qeeqbii.R;
import edu.gatech.gtri.bktree.Metric;
import edu.gatech.gtri.bktree.MutableBkTree;
import info.debatty.java.stringsimilarity.Levenshtein;



public class CancerDataBase {

    // ATTRIBUTES
    static private final List<CancerSubstance> substanceList = new ArrayList<>();
    static private final HashMap<String, CancerSubstance> substanceMap = new HashMap<>();
    // mopen_state takes value 0 if no file have been read and takes value 1 if readCSVfile() have
    // been called and succeeded in reading a CSV file
    static private boolean openState = false;
    // Definition of the hamming distance that will be used to query the database
    // The Levenshtein distance is chosen here
    static private final Metric<String> hammingLevenshtein = new Metric<String>() {
        @Override
        public int distance(String x, String y) {
            Levenshtein levenshtein = new Levenshtein();
            return (int) Math.round(levenshtein.distance(x, y));
        }
    };
    static private final MutableBkTree<String> bkTree = new MutableBkTree<>(hammingLevenshtein);



    // Method that reads a CSVFile
    public static void readCSVFile(Context context) {
        if (!openState) {

            Resources resources = context.getResources();
            InputStream inStream = resources.openRawResource(R.raw.clean_classification_iarc_french);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));

            String line = "";

            try {
                // Step over the headers
                reader.readLine();
                while ((line = reader.readLine()) != null) {
                    //split by ','
                    String[] tokens = line.split(",");

                    //read the data
                    CancerSubstance new_substance = new CancerSubstance();

                    // Filling of new_substance's attributes
                    new_substance.setmId(Integer.parseInt(tokens[0]));
                    new_substance.setmAgent(tokens[1]); //Here we can add the fact that the dataBase might have leaks
                    new_substance.setmGroup(tokens[2]);

                    // Filling of BK-Tree with the Agent name
                    bkTree.add(tokens[1]);

                    //Note if we want to import an int or a double we have to write:
                    //sample.setmGroup(Double.parseDouble(tokens[x]));
                    //sample.setmGroup(Integer.parseInt(tokens[x]));
                    substanceList.add(new_substance); //adds the sample to the table with all the data
                    substanceMap.put(new_substance.getmAgent(), new_substance);
                }
            } catch (IOException error) {
                Log.wtf("MainActivity", "CustomExeptions reading the data file in the .csv file" + line, error);
                error.printStackTrace();
            }

            // Change the mopen_state to value 1 because the file is read
            openState = true;
        }
    }

    // Method that outputs a string containing a summary of all the substances and their categorization
    public static String sendOutputReadyToPrint() throws NotOpenFileException {
        if (openState) {
            String output = " id\tAgent\t\tGroup\n";
            for (CancerSubstance element : substanceList) {
                output += element.toString();
                output += "\n";
            }
            return output;
        } else {
            throw new NotOpenFileException("Read the carcinogenic database before trying to print it.\n");
        }
    }

    // Setters and getters

    public static boolean isRead() {
        return openState;
    }

    public static CancerSubstance getSubstanceByName(String substanceName) {
        CancerSubstance substanceOutput = substanceMap.get(substanceName);
        // If the queried substance is not in the Cancer DataBase, return an empty CancerSubstance
        if (substanceOutput == null) {
            return new CancerSubstance();
        }
        return substanceOutput;
    }

    public static MutableBkTree<String> getBkTree() {
        return bkTree;
    }

}




