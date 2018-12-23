package ch.epfl.sweng.qeeqbii.open_food;

/**
 * Created by guillaume on 01/11/17.
 * A repertory of all recently scan products.
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class RecentlyScannedProducts {

    private static Map<String, Product> mProductMap = new HashMap<>();
    private static LinkedList<String> mBarcodeList = new LinkedList<>();

    // Returns whether a product has been recently scanned or not.
    public static Boolean contains(String barcode)
    {
        return mProductMap.containsKey(barcode);
    }

    public static LinkedList<String> getBarcodeList()
    {
        return mBarcodeList;
    }

    public static Product getProduct(String barcode)
    {
        if (contains(barcode))
        {
            return mProductMap.get(barcode);
        }
        else
        {
            return new Product();
        }
    }


    public static void add(String barcode, Product product)
    {
        mProductMap.put(barcode,product);
        mBarcodeList.addLast(barcode);
    }

    public static void clear() {
        mProductMap.clear();
        mBarcodeList.clear();
    }

    public Iterator<Product> iterator() {
        return new ProductIterator();
    }


    private class ProductIterator implements Iterator<Product> {
        int current_index = 0;

        @Override
        public boolean hasNext() {
            return (current_index < mBarcodeList.size());
        }

        @Override
        public Product next() {
            String barcode = mBarcodeList.get(current_index);
            return mProductMap.get(barcode);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove method of" +
                    "RecentlyScannedProducts' iterator is not implemented");
        }
    }


}
