package ch.epfl.sweng.qeeqbii.open_food;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.util.HashMap;
import java.util.Map;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.qeeqbii.activities.BarcodeToProductActivity;

/**
 * Created by guillaume on 06/10/17.
 * Class allowing to make GET requests to openfood.
 * GET requests responses are cached in RecentlyScannedProducts.
 */

public class OpenFoodQuery extends AsyncTask<String, Void, Product> {

    private static final Map<String,String> errorCache = new HashMap<>();

    public static Map<String,String> getErrorCache() {
        return  errorCache;
    }

    private static class OpenFoodQueryException extends Exception {
        OpenFoodQueryException(String message) {
            super(message);
        }
    }

    // Make the GET request on an other thread.
    // Search first in the RecentlyScannedProducts if the product is already cached.
    // Returns a Product to the onPostExecution method.
    @Override
    protected Product doInBackground(String params[])
    {
        String barcode = params[0];
        if(RecentlyScannedProducts.contains(barcode))
        {
            return RecentlyScannedProducts.getProduct(barcode);
        }
        try {
            URL url = new URL("https://www.openfood.ch/api/v3/products?excludes=name_translations2Cimages&barcodes=" + barcode);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/vnd.api+json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("Authorization", "Token token=\"da68d406381c30aae6796d39d474265a\"");
            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            String str = "";
            int data = isw.read();
            for (int i = 0; i < 100; ++i) {
                char current = (char) data;
                data = isw.read();
                str += current;
            }
            int barcode_begin = str.indexOf("barcode") + 10;
            if (barcode_begin == 9) {
                throw new OpenFoodQueryException("Unusable data registered for this product.");
            }
            int barcode_end = str.indexOf('\"',barcode_begin);
            String barcode_found = str.substring(barcode_begin,barcode_end);
            if(!(barcode_found.equals(barcode)))
            {
                throw new OpenFoodQueryException("Barcode not found in the database.");
            }
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                str += current;
            }
            urlConnection.disconnect();

            System.out.println("Product barcode: /////////////////////////////////////" + barcode);
            Product product = (new HTTPRequestResponse(str, barcode).toProduct());

            RecentlyScannedProducts.add(barcode, product);
            return product;
        }
        catch(OpenFoodQueryException e)
        {
            errorCache.put(barcode, "ERROR: (openfood) " + e.getMessage());
            return null;
        }
        catch(java.io.IOException e)
        {
            errorCache.put(barcode, "ERROR: " + e.getMessage());
            return null;
        }


    }

    // This GetOrCreate can freeze the main thread if the barcode isn't in the cache.
    // If we do not want to save the product in the scanned history, we can give a null context in parameter.
    public static Product GetOrCreateProduct(String barcode, Context context) throws Exception
    {
        if(RecentlyScannedProducts.contains(barcode))
        {
            return RecentlyScannedProducts.getProduct(barcode);
        }

        final Context save_context = context;

        new OpenFoodQuery() {
            @Override
            protected void onPostExecute(Product product) {
                try{
                    SavedProductsDatabase.addProduct(product);
                    if (save_context != null) {
                        SavedProductsDatabase.save(save_context, "saved_products_database.json");
                    }
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }.execute(barcode).get(5, TimeUnit.SECONDS);

        if(RecentlyScannedProducts.contains(barcode))
        {
            return RecentlyScannedProducts.getProduct(barcode);
        } else {
            throw new Exception(errorCache.get(barcode));
        }
    }

    // Passing the barcode and a textview to this method makes the GET
    // request to openFood without freezing the main thread.
    public static void ShowProduct(final String barcode, TextView txt, Context context)
    {
        final TextView txt2 = txt;
        final String barcode2 = barcode;
        final Context save_context = context;
        new OpenFoodQuery() {
            @Override
            public void onPostExecute(Product product) {
                //TextView txt = (TextView) findViewById(R.id.product_details);
                try {
                    if (product == null)
                        throw new Exception(errorCache.get(barcode2));

                    //String s = product.getName();
                    //s += "\n\nIngredients: " + product.getIngredients();
                    //s += "\n\nQuantity: " + product.getQuantity();
                    //s += "\n\nNutrients: (per 100g)\n" + product.getNutrients();
                    String s = product.toString();
                    Log.d("STATE", "Product found: " + s);
                    txt2.setText(s);
                    try{
                        SavedProductsDatabase.addProduct(product);
                        if (save_context != null) {
                            SavedProductsDatabase.save(save_context, "saved_products_database.json");
                        }
                    } catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }

                } catch (Exception e) {
                    txt2.setText(e.getMessage());
                    BarcodeToProductActivity.requestAddProduct(barcode);
                }

            }
        }.execute(barcode);
    }

    public static boolean isCached(String barcode)
    {
        return (RecentlyScannedProducts.contains(barcode));
    }

    public static Product get(String barcode) throws OpenFoodQueryException
    {
        if (RecentlyScannedProducts.contains(barcode))
        {
            return RecentlyScannedProducts.getProduct(barcode);
        } else {
            throw new OpenFoodQueryException("ERROR: (OpenFoodQuery) : this barcode \"" + barcode + "\" is not cached.");
        }
    }
}
