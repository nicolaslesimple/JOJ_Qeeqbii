package ch.epfl.sweng.qeeqbii.open_food;

import java.io.IOException;


/**
 * Created by guillaume on 09/10/17.
 *
 */

class HTTPRequestResponse {
    private String resp_body = "";

    private String mBarcode = "";

    HTTPRequestResponse(String str, String barcode) throws IOException
    {
        // Normally this error should not happen anymore
        if (str.charAt(0) != '{') throw new IOException(str);
        // To change to either OpenFoodQueryException or HTTPRequestException
        if ((str.substring(0,10).contains("[]"))) throw new IOException("Request gave an empty field, the barcode seems not to be present in the database.");
        resp_body = str;
        mBarcode = barcode;
    }

    private String getProductName(String language) {
        int names_ind = resp_body.indexOf("display_name_translations\":");
        if (names_ind == -1) return "Name Not Found";
        int language_ind = resp_body.indexOf("\"" + language + "\"", names_ind);
        int two_dots_ind = resp_body.indexOf(':', language_ind);

        return resp_body.substring(two_dots_ind + 2, resp_body.indexOf('\"', two_dots_ind + 2)).replace("\\n", "\n").replace("\\r", "\r");
    }

    private String getProductIngredients(String language) {
        int ingredients_ind = resp_body.indexOf("ingredients_translations\":");
        if (ingredients_ind == -1) {
            return "Ingredients Not Found";
        }
        int language_ind = resp_body.indexOf("\"" + language + "\"", ingredients_ind);
        int two_dots_ind = resp_body.indexOf(':', language_ind);

        return resp_body.substring(two_dots_ind + 2, resp_body.indexOf('\"', two_dots_ind + 2)).replace("\\n", "\n").replace("\\r", "\r");
        //We have to check if the replace is worth it
    }


    private String getProductNutrients(String language) {
        //System.out.println(resp_body);
        String str = "";
        int nutrients_ind = resp_body.indexOf("nutrients\":");
        if (nutrients_ind == -1) {
            return "Nutrients Not Found";
        } else {
            int current_index = nutrients_ind + 10;
            while (resp_body.indexOf("{", current_index) < resp_body.indexOf("}", current_index)) {
                if (resp_body.indexOf("}", current_index) - resp_body.indexOf("{", current_index) == 1)
                    return "Empty nutrients";
                //current_index = resp_body.indexOf("\"",current_index)
                //str += resp_body.substring(current_index+1,resp_body.indexOf("\"",current_index));
                current_index = resp_body.indexOf("\"" + language + "\"", current_index);
                current_index = resp_body.indexOf(':', current_index);
                str += resp_body.substring(current_index + 2, resp_body.indexOf("\"", current_index + 3)) + ": ";
                current_index = resp_body.indexOf("\"unit\"", current_index) + 6;
                String unit = resp_body.substring(resp_body.indexOf("\"", current_index) + 1, resp_body.indexOf("\"", current_index + 3));
                current_index = resp_body.indexOf("\"per_hundred\"", current_index) + 14;
                str += resp_body.substring(current_index, resp_body.indexOf(",", current_index)) + unit + "\n";
                current_index = resp_body.indexOf("},", current_index) + 2;
            }
            return str.replace("\\n", "\n").replace("\\r", "\r");
        }

    }

    private String getProductQuantity() {
        int quantity_ind = resp_body.indexOf("\"quantity\":");
        if (quantity_ind == -1) return "Quantity Not Found";
        String quantity = resp_body.substring(quantity_ind + 11, resp_body.indexOf(',', quantity_ind));
        int unit_ind = resp_body.indexOf("\"unit\"", quantity_ind);
        String unit = resp_body.substring(unit_ind + 8, resp_body.indexOf(',', unit_ind) - 1);

        return (quantity + unit).replace("\\n", "\n").replace("\\r", "\r");

    }

    //Return a product from the HTTPRequestResponse
    Product toProduct(String language)
    {
        return new Product(getProductName(language), getProductQuantity(), getProductIngredients(language),
                getProductNutrients(language), mBarcode, null);
    }

    // By default, we get the french version
    Product toProduct()
    {
        return toProduct("fr");
    }
}
