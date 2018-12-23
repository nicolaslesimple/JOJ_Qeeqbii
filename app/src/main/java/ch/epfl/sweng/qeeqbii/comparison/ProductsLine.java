package ch.epfl.sweng.qeeqbii.comparison;

/**
 * Created by sergei on 11/30/17.
 */


// This class holds a single line (by single nutrient) in the ProductComparison activity
public class ProductsLine {
    // nutrient
    public String criteria;

    // value for product 1
    public Double value1;

    // value for product 2
    public Double value2;

    public ProductsLine(String criteria_, Double val1, Double val2) {
        criteria = criteria_;
        value1 = val1;
        value2 = val2;
    }

    public ProductsLine copy() {
        return new ProductsLine(criteria, value1, value2);
    }
}