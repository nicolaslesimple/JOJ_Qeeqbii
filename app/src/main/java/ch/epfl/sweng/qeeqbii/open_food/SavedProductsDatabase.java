package ch.epfl.sweng.qeeqbii.open_food;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.qeeqbii.clustering.ClusterTypeSecondLevel;


/**
 * Created by guillaume on 13/11/17.
 * Manage the database of products scanned History.
 */

public class SavedProductsDatabase
{
    private static JSONObject saved_products_json = null;

    private static Map<String,Integer> dates_indices = new HashMap<>();

    private static Integer max_date_index;

    public static void load(Context context) throws IOException, JSONException
    {
        if (saved_products_json == null) {
            String[] filelist = context.fileList();
            Boolean exists = false;
            for (String file : filelist)
            {
                if ("saved_products_database.json".equals(file))
                    exists = true;
            }
            if (!exists) {
                context.openFileOutput("saved_products_database.json",0).close();
            }
            load(context.openFileInput("saved_products_database.json"));
        }
    }

    public static void load(InputStream inStream) throws IOException, JSONException
    {
        try {

            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            inStream.close();
            saved_products_json = new JSONObject(responseStrBuilder.toString());
            saved_products_json.getJSONArray("Dates");
            dates_indices = new HashMap<>();
        } catch(Exception e )
        {
            saved_products_json = new JSONObject().put("Dates",new JSONArray());
        }
    }

    public static Date[] getDates() throws JSONException, ParseException {
        JSONArray dates_arr = saved_products_json.getJSONArray("Dates");
        Date[] dates = new Date[dates_arr.length()];
        max_date_index = dates_arr.length() - 1;
        for (int i = 0; i < dates_arr.length(); ++i) {
            dates[i] = new Date(dates_arr.getJSONObject(i).getString("date"));
            dates_indices.put(dates[i].toString(), i);
        }
        return dates;
    }

    public static Product[] getProductsFromDate(Date date) throws JSONException, IOException
    {
        Integer index = dates_indices.get(date.toString());
        JSONArray products_json_array = saved_products_json.getJSONArray("Dates")
                .getJSONObject(index).getJSONArray("products");

        Product[] products = new Product[products_json_array.length()];
        for(int i = 0; i < products_json_array.length(); ++i)
        {
            JSONObject item = products_json_array.getJSONObject(i);
            products[i] = new Product(item.getString("name"), item.getString("quantity"), item.getString("ingredients"),
                    item.getString("nutrients"), item.getString("barcode"),
                    ClusterTypeSecondLevel.getClusterType(item.getString("cluster type")));
        }
        return products;

    }

    public static void addProduct(Product product) throws ParseException, JSONException {
        String date_of_the_day = (new Date()).toString();
        if (!dates_indices.containsKey(new Date().toString())) {
            max_date_index += 1;
            JSONObject new_json_object = new JSONObject();
            new_json_object.put("date", date_of_the_day);
            new_json_object.put("products", new JSONArray());
            saved_products_json.getJSONArray("Dates").put(max_date_index, new_json_object);
            dates_indices.put(new Date().toString(), max_date_index);
        }

        JSONArray json_today_products = saved_products_json.getJSONArray("Dates")
                .getJSONObject(dates_indices.get(new Date().toString())).getJSONArray("products");

        JSONObject new_product = new JSONObject();

        new_product.put("name", product.getName());
        new_product.put("barcode", product.getBarcode());
        new_product.put("ingredients", product.getIngredients());
        new_product.put("nutrients", product.getNutrients());
        new_product.put("quantity", product.getQuantity());
        new_product.put("cluster type", product.getCluster().toString());

        json_today_products.put(json_today_products.length(), new_product);
    }

    public static void save(Context context, String filename) throws IOException
    {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));
        outputStreamWriter.write(saved_products_json.toString());
        outputStreamWriter.close();
    }

    public static ArrayList<Product> getProductsBetweenTodayAndDate(Date date) {
        ArrayList<Product> products = new ArrayList<>();
        Date today_date = new Date();

        try {
            JSONArray dates_json_array = saved_products_json.getJSONArray("Dates");
            List<Integer> dates_in_between = new ArrayList<>();
            for (int i = 0; i < dates_json_array.length(); ++i)
            {
                Date this_date = new Date(dates_json_array.getJSONObject(i).getString("date"));
                if (this_date.isBefore(today_date) & this_date.isAfter(date))
                {
                    dates_in_between.add(dates_indices.get(this_date.toString()));
                }
            }


            for (Integer ind : dates_in_between) {
                JSONArray products_json_array = saved_products_json.getJSONArray("Dates")
                        .getJSONObject(ind).getJSONArray("products");


                for (int i = 0; i < products_json_array.length(); ++i) {
                    JSONObject item = products_json_array.getJSONObject(i);
                    products.add(new Product(item.getString("name"), item.getString("quantity"), item.getString("ingredients"),
                            item.getString("nutrients"), item.getString("barcode"), ClusterTypeSecondLevel
                            .getClusterType(item.getString("cluster type"))));
                }
            }
        } catch(JSONException e)
        {
            System.err.println(e.getMessage());
        }
        return products;
    }
}
