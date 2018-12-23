package ch.epfl.sweng.qeeqbii.open_food;

import android.graphics.Color;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.clustering.ClusterClassifier;
import ch.epfl.sweng.qeeqbii.clustering.ClusterType;
import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;
import ch.epfl.sweng.qeeqbii.clustering.ComparableCluster;
import ch.epfl.sweng.qeeqbii.clustering.NutrientNameConverter;
import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.ProductException;
import ch.epfl.sweng.qeeqbii.clustering.NutrientVector;

public class Product implements Serializable{

    private String mName = "";
    private String mQuantity = "";
    private String mIngredients = "";
    private String mNutrients = "";
    private String[] mParsedIngredients = null;
    private Map<String, Double> mParsedNutrients = null;
    private ClusterType mType = ClusterTypeSecondLevel.UNDETERMINED;
    private ArrayList<ComparableCluster> bestClusters = null;
    private String mBarcode = "";
//    private Boolean mIsChecked = false;
//    private float mOpacity = 1f;
    private NutrientVector nutrientVector = null;

    public Product() {}
    
    public Product(String name, String quantity, String ingredients, String nutrients, String barcode, ClusterType type)
    {
        mName = name;
        mQuantity = quantity;
        mIngredients = ingredients;
        mNutrients = nutrients;
        mBarcode = barcode;
//        mIsChecked = false;
//        mOpacity = 1f;
        mType = type;

        if (mType == null)
        {
            try {
                nutrientVector = new NutrientVector(getParsedNutrients());

                // If the type is not defined (type == null) then we call ClusterClassifier's function
                // to get a cluster
                // If getParsedNutrients were to throw an exception we catch the exception, we define an empty
                // NutrientVector and we assign it an UNDEFINED cluster enum type

                if (type == null) {
                    bestClusters = ClusterClassifier.getClusterTypeFromNutrients(nutrientVector);
                    mType = bestClusters.get(0).getCluster();
                }
            } catch (ProductException|NotOpenFileException e)
            {
                System.err.println(e.getMessage());
                nutrientVector = null;
                if (type == null) {
                    mType = ClusterTypeSecondLevel.UNDETERMINED;
                }
                else {
                    mType = type;
                }
            }
        }
/*
        // Instantiating nutrientVector and defining cluster type
        try {
            nutrientVector = new NutrientVector(getParsedNutrients());

            // If the type is not defined (type == null) then we call ClusterClassifier's function
            // to get a cluster
            // If getParsedNutrients were to throw an exception we catch the exception, we define an empty
            // NutrientVector and we assign it an UNDEFINED cluster enum type
            if (type == null) {
                bestClusters = ClusterClassifier.getClusterTypeFromNutrients(nutrientVector);
                mType = bestClusters.get(0).getCluster();
            }
            else {
                mType = type;
            }
        }
        catch (ProductException|NotOpenFileException e) {
            System.err.println(e.getMessage());
            nutrientVector = null;
            if (type == null) {
                mType = ClusterTypeSecondLevel.UNDETERMINED;
            }
            else {
                mType = type;
            }
        }
        */
    }

    public String getName()
    {
        return mName;
    }

    public String getQuantity()
    {
        return mQuantity;
    }

    public String getIngredients()
    {
        return mIngredients;
    }

    public String getNutrients()
    {
        return mNutrients;
    }

    public String getBarcode() { return mBarcode; }

    public int getImageId() { return mType.getImageId(); }

    public ClusterType getCluster() { return mType; }

    public String[] getParsedIngredients() throws ProductException
    // Returns an array of string. Each entry of the array corresponds to an ingredient.
    {
        if(mParsedIngredients != null)
        {
            return mParsedIngredients;
        }

        if (mIngredients.matches("") | mIngredients.matches("Ingredients Not Found")) {
            throw new ProductException("Ingredient list is empty for this product: unable to execute the parsing operation.");
        }

        mParsedIngredients = mIngredients.split(",");

        return mParsedIngredients;
    }

    public Map<String, Double> getParsedNutrients() throws  ProductException {
    // Returns a map binding a nutrient to its quantity.
    // Key entered in the map can be e.g. "Sel (g)" or "Sucres (g)"
    // The quantity is returned as a double.

        if (mParsedNutrients != null) {
            return mParsedNutrients;
        }

        if (mNutrients.equals("") || mNutrients.equals("Nutrients Not Found") || mNutrients.equals("Empty nutrients")) {
            throw new ProductException("Nutrient list is empty for this product: unable to execute the parsing operation.");
        }

        Map<String, Double> nutrient_map = new HashMap<>();
        try {
            String[] parsed_nutrients = mNutrients.split("\\n");
            String standardKey;
            for (String nut : parsed_nutrients) {
                int two_dots_index = nut.indexOf(':');
                String key = nut.substring(0, two_dots_index);
                int search_alpha = two_dots_index + 1;

                while (!Character.isLetter(nut.charAt(search_alpha))) {
                    search_alpha += 1;
                }

                Double value = Double.parseDouble(nut.substring(two_dots_index + 2, search_alpha));
                String unit = nut.substring(search_alpha, nut.length());

                if (!key.contains("(" + unit + ")")) {
                    key = key + " (" + unit + ")";
                }

                // Conversion of the key in its standardKey form using NutrientNameConverter (if possible)
                try {
                    standardKey = NutrientNameConverter.convertToStandardName(key);
                    nutrient_map.put(standardKey, value);
                }

                // Here what we do is that if the key specified in the nutrient list is not recognized in the
                // nutrient_name_converter.csv file then we add it anyway to the parsedNutrients list
                // However if the nutrient is specified as "Glucide (g)" which clearly refers to "Glucides (g)"
                // then the program won't understand and it will set a value of 0.0 to the "Glucides (g)" entry
                // of the product's NutrientVector, whatever value is assigned to "Glucide (g)"
                // One of the possible ways to deal with it is to write "Glucide (g)" as a potential translation
                // of "Glucides (g)" in nutrient_name_converter.csv
                catch (IllegalNutrientKeyException e) {
                    System.err.println(e.getMessage());
                    nutrient_map.put(key, value);
                }
            }
        }
        catch (Exception e) {
            //System.err.println(e.getMessage());
            throw new ProductException(e.getMessage() +
                    "\nThe formatting of the Product's nutrient list is not good." +
                    "\nCheck the opening of nutrient_name_converter.csv." +
                    "\nCheck also that all the necessary nutrient keys are specified in that file.");
        }

        mParsedNutrients = nutrient_map;
        return nutrient_map;
    }

    @Override
    public String toString() {
        String s = getName();
        s += "\n\nIngredients: " + getIngredients();
        s += "\n\nQuantity: " + getQuantity();
        s += "\n\nNutrients: (per 100g)\n" + getNutrients();
        s += "\n\nCluster: " + getCluster().toString();

        if (bestClusters != null) {
            s += "\n\nBest clusters: ";
            for (ComparableCluster item : bestClusters) {
                s += item.getCluster().toString() + ", " + item.getDistance() + "\n";
            }
        }
        return s;
    }

    public void setParsedIngredients(String[] parsedIngredients) {
        this.mParsedIngredients = parsedIngredients;
    }

    public int getStickerColor()
    {
        try {

            double sum = 0;

            double energy = getParsedNutrients().get("Énergie (kCal)") / 2000;
            double fats = getParsedNutrients().get("Matières grasses (g)") / 70;
            double sugar= getParsedNutrients().get("Sucres (g)") / 55;
            double salt = getParsedNutrients().get("Sel (g)") / 5;

            sum += energy + fats + sugar + salt;

            if (sum < 0.25 && sugar > 0.1 && salt > 0.1)
                return Color.GREEN;

            if (sum < 0.5 && sugar > 0.5 && salt > 0.15)
                return Color.YELLOW;

            if (sum < 0.75 && sugar > 0.19)
                return Color.parseColor("#ffA500");

            return Color.RED;


        } catch (ProductException e)
        {
            return Color.WHITE;
        }
    }


//    public boolean isChecked() {
//        return mIsChecked;
//    }
//
//    public void setChecked(Boolean isChecked) {
//        mIsChecked = isChecked;
//    }
//
//    public float getOpacity () {
//        return mOpacity;
//    }
//
//    public void setOpacity (float opacity){
//        mOpacity = opacity;
//    }
}
