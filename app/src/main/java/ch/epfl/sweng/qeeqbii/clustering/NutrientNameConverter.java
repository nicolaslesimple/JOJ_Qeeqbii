package ch.epfl.sweng.qeeqbii.clustering;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.qeeqbii.R;
import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;

public class NutrientNameConverter {

    private static Map<String, String> nameConversionMap;

    private static boolean openState = false;

    public static void readCSVFile(Context context) {
        if (!openState) {
            Resources resources = context.getResources();
            InputStream inStream = resources.openRawResource(R.raw.nutrient_name_converter);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, Charset.forName("UTF-8")));

            nameConversionMap = new HashMap<>();
            try {
                // step over the header
                String line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] elements = line.split(",");
                    String standardName = elements[0];

                    for (int i = 1; i < elements.length; i++) {
                        nameConversionMap.put(elements[i], standardName);
                    }
                }
                openState = true;
            }
            catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }



    public static String convertToStandardName(String queriedNutrientName)
            throws NotOpenFileException, IllegalNutrientKeyException {
        if (!openState) {
            throw new NotOpenFileException("Open the nutrient_name_converter.csv file" +
                    " before using NutrientNameConverter.getStandardName()");
        }
        String answer = nameConversionMap.get(queriedNutrientName);
        if (answer == null) {
            throw new IllegalNutrientKeyException("The nutrient key provided to NutrientNameConverter" +
                    " is not referrenced\n");
        }
        return answer;
    }


    public static boolean isRead() {
        return openState;
    }


    public static Set<String> getStandardNutrientNames() throws NotOpenFileException {
        if (!openState) {
            throw new NotOpenFileException("Open the nutrient_name_converter.csv file before trying" +
                    "to access the standard nutrient keys");
        }
        return new HashSet<>(nameConversionMap.values());
    }

    public static void clear() {
        nameConversionMap.clear();
        openState = false;
    }

}
