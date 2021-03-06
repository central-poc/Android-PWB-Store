package cenergy.central.com.pwb_store.manager.bus.event;

import android.view.View;

import cenergy.central.com.pwb_store.model.CompareProduct;
import cenergy.central.com.pwb_store.model.ProductCompareList;

/**
 * Created by napabhat on 9/19/2017 AD.
 */

public class CompareDetailBus {

    private ProductCompareList mProductCompareList;
    private CompareProduct compareProduct;
    private boolean isOpen;
    private View mView;

    public CompareDetailBus(ProductCompareList productList, boolean isOpen, View view){
        this.mProductCompareList = productList;
        this.isOpen = isOpen;
        this.mView = view;
    }

    public CompareDetailBus(CompareProduct compareProduct, boolean isOpen, View view){
        this.compareProduct = compareProduct;
        this.isOpen = isOpen;
        this.mView = view;
    }

    public CompareProduct getCompareProduct() {
        return compareProduct;
    }

    public ProductCompareList getProductCompareList() {
        return mProductCompareList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public View getView() {
        return mView;
    }
}
