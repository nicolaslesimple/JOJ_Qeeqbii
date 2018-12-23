package ch.epfl.sweng.qeeqbii.clustering;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.custom_exceptions.IllegalNutrientKeyException;
import ch.epfl.sweng.qeeqbii.custom_exceptions.NotOpenFileException;


public class NutrientVector implements Cloneable, Serializable {

    private Map<String,Double> nutrientMap;

    public NutrientVector() throws NotOpenFileException {
        if (!NutrientNameConverter.isRead()) {
            throw new NotOpenFileException("Read the nutrient_name_converter.csv file before creating" +
                    "NutrientVector objects");
        }
        nutrientMap = new HashMap<>();
        for (String standardKey : NutrientNameConverter.getStandardNutrientNames()) {
            this.nutrientMap.put(standardKey, 0.0);
        }
    }

    // Copy constructor
    public NutrientVector(NutrientVector toCopy) throws NotOpenFileException {
        try {
            this.nutrientMap = new HashMap<>();
            double copiedValue;
            for (String standardKey : NutrientNameConverter.getStandardNutrientNames()) {
                copiedValue = toCopy.getComponent(standardKey);
                this.nutrientMap.put(standardKey, copiedValue);
            }
        }
        catch (IllegalNutrientKeyException e) {
            System.err.println("Invalid key used in NutrientVector's copy constructor");
        }
    }

    // Create a Nutrient vector from getParsedNutrient vector of the Product class
    public NutrientVector(Map<String, Double> inputNutrientMap) throws NotOpenFileException {
        nutrientMap = new HashMap<>();
        // Conversion of the getParsedNutrients Map format in the NutrientVector format
        for (String nutrientName : NutrientNameConverter.getStandardNutrientNames()) {
            if (inputNutrientMap.containsKey(nutrientName)) {
                nutrientMap.put(nutrientName, inputNutrientMap.get(nutrientName));
            }
            else {
                nutrientMap.put(nutrientName, 0.0);
            }
        }

    }

    public void setComponent(String nutrientName, double newValue) throws IllegalNutrientKeyException {
        try {
            if (!NutrientNameConverter.getStandardNutrientNames().contains(nutrientName)) {
                throw new IllegalNutrientKeyException("Trying to set a component of a NutrientVector object using" +
                        "an invalid nutrient name as key");
            }
            nutrientMap.put(nutrientName, newValue);
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }
    }

    public int getDim() {
        return nutrientMap.size();
    }


    public double getComponent(String key) throws IllegalNutrientKeyException {
        try {
            if (!NutrientNameConverter.getStandardNutrientNames().contains(key)) {
                throw new IllegalNutrientKeyException("Invalid nutrient key used to access NutrientVector component");
            }
        }
        catch (NotOpenFileException e) {
            System.err.println(e.getMessage());
        }

        return nutrientMap.get(key);
    }


    public NutrientVector diff(NutrientVector substractVector) {

        if (this.getDim() != substractVector.getDim()) {
            throw new IllegalArgumentException("Substracting NutrientVector does not have the right dimension.\n");
        }

        try {
            NutrientVector resultVector = new NutrientVector();
            double newValue;
            for (String nutrientName : NutrientNameConverter.getStandardNutrientNames()) {
                newValue = this.getComponent(nutrientName) - substractVector.getComponent(nutrientName);
                resultVector.setComponent(nutrientName, newValue);
            }
            return resultVector;

        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    private double computeNorm() {
        double sum = 0;
        for (double nutrientValue : nutrientMap.values()) {
            sum += Math.pow(nutrientValue, 2);
        }
        return Math.sqrt(sum);
    }

    public double computeDistance(NutrientVector queriedVector) throws NotOpenFileException{
        return this.diff(queriedVector).computeNorm();
    }



    public NutrientVector componentWiseDivision(NutrientVector divisor) {
        // Be careful to check division by 0
        try {
            NutrientVector result = new NutrientVector();
            double divisorValue;
            double currentValue;
            for (String standardKey : NutrientNameConverter.getStandardNutrientNames()) {
                divisorValue = divisor.getComponent(standardKey);
                if (divisorValue != 0.0) {
                    currentValue = this.getComponent(standardKey) / divisorValue;
                }
                else {
                    currentValue = Double.POSITIVE_INFINITY;
                }
                result.setComponent(standardKey, currentValue);
            }
            return result;
        }
        catch (NotOpenFileException|IllegalNutrientKeyException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }


    public String toString() {
        String output = "Dimension: " + nutrientMap.size() + "\n";
        for (String key : nutrientMap.keySet()) {
            output += key;
            output += ": ";
            output += nutrientMap.get(key);
            output += "\n";
        }
        return output;
    }


}
