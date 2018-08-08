package cenergy.central.com.pwb_store.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class ProductFilterSubHeader extends RealmObject implements IViewType, Parcelable {
    public static final Creator<ProductFilterSubHeader> CREATOR = new Creator<ProductFilterSubHeader>() {
        @Override
        public ProductFilterSubHeader createFromParcel(Parcel in) {
            return new ProductFilterSubHeader(in);
        }

        @Override
        public ProductFilterSubHeader[] newArray(int size) {
            return new ProductFilterSubHeader[size];
        }
    };
    private static final String TAG = "ProductFilterSubHeader";
    @Ignore
    private int viewTypeId;
    private String id;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String urlName;
    private String type;
    @SerializedName("children_data")
    @Expose
    private RealmList<ProductFilterItem> mProductFilterItemList;
    private boolean isExpanded;

    public ProductFilterSubHeader() {
    }

    public ProductFilterSubHeader(ProductFilterSubHeader productFilterSubHeader) {
        this.id = productFilterSubHeader.getId();
        this.name = productFilterSubHeader.getName();
        this.type = "single";
        this.urlName = productFilterSubHeader.getUrlName();

        List<ProductFilterItem> productFilterItemList = new ArrayList<>();
        if (productFilterSubHeader.isProductFilterItemListAvailable())
            for (ProductFilterItem productFilterItem :
                    productFilterSubHeader.getProductFilterItemList()) {
                productFilterItemList.add(new ProductFilterItem(productFilterItem));
            }

        this.mProductFilterItemList.clear();
        this.mProductFilterItemList.addAll(productFilterItemList);
        this.isExpanded = false;
    }

    protected ProductFilterSubHeader(Parcel in) {
        viewTypeId = in.readInt();
        id = in.readString();
        name = in.readString();
        type = in.readString();
        isExpanded = in.readByte() != 0;
        urlName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewTypeId);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeTypedList(mProductFilterItemList);
        dest.writeByte((byte) (isExpanded ? 1 : 0));
        dest.writeString(urlName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int getViewTypeId() {
        return viewTypeId;
    }

    @Override
    public void setViewTypeId(int id) {
        this.viewTypeId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSortBy() {
        return name.equalsIgnoreCase("เรียงสินค้าตาม");
    }

    public boolean isMultipleType() {
        return type.equalsIgnoreCase("multiple");
    }

    public List<ProductFilterItem> getProductFilterItemList() {
        return mProductFilterItemList;
    }

    public void setProductFilterItemList(RealmList<ProductFilterItem> productFilterItemList) {
        this.mProductFilterItemList = productFilterItemList;
    }

    public boolean isProductFilterItemListAvailable() {
        return mProductFilterItemList != null && !mProductFilterItemList.isEmpty();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }


    public void clearAllSelectedFilterOptions() {
        for (ProductFilterItem productFilterItem :
                mProductFilterItemList) {
            productFilterItem.setSelected(false);
        }
    }

    public void replaceSortHeader(ProductFilterSubHeader productFilterSubHeader, boolean isPreserveSelection) {
        this.id = productFilterSubHeader.getId();
        this.name = productFilterSubHeader.getName();
        this.level = productFilterSubHeader.getLevel();
        this.type = productFilterSubHeader.getType();
        if (isPreserveSelection) {
            for (ProductFilterItem productFilterItem :
                    mProductFilterItemList) {
                for (ProductFilterItem newProductFilterItem :
                        productFilterSubHeader.getProductFilterItemList()) {
                    if (productFilterItem.getSlug().equalsIgnoreCase(newProductFilterItem.getSlug())) {
                        newProductFilterItem.setSelected(productFilterItem.isSelected());
                        break;
                    }
                }
            }
        }

        this.mProductFilterItemList.clear();
        this.mProductFilterItemList.addAll(productFilterSubHeader.getProductFilterItemList());
    }

    public void replaceExisting(ProductFilterSubHeader loadedProductFilterSubHeader, boolean isPreserveSelection) {
        this.id = loadedProductFilterSubHeader.getId();
        this.name = loadedProductFilterSubHeader.getName();
        this.level = loadedProductFilterSubHeader.getLevel();
        this.type = loadedProductFilterSubHeader.getType();

        if (isPreserveSelection) {
            for (ProductFilterItem productFilterItem :
                    mProductFilterItemList) {
                for (ProductFilterItem newProductFilterItem :
                        loadedProductFilterSubHeader.getProductFilterItemList()) {
                    if (productFilterItem.getId().equalsIgnoreCase(newProductFilterItem.getId())) {
                        newProductFilterItem.setSelected(productFilterItem.isSelected());
                        break;
                    }
                }
            }
        }

        this.mProductFilterItemList.clear();
        this.mProductFilterItemList.addAll(loadedProductFilterSubHeader.getProductFilterItemList());
    }

    public String getSelectedProductFilterOptionIfAvailable() {
        StringBuilder stringBuilder = new StringBuilder();

        for (ProductFilterItem productFilterItem :
                mProductFilterItemList) {
            if (productFilterItem.isSelected()) {
                stringBuilder.append(productFilterItem.getId());
                stringBuilder.append(",");
            }
        }

        String resultString = stringBuilder.toString();

        if (TextUtils.isEmpty(resultString)) {
            return "";
        }

        return resultString.substring(0, resultString.length() - 1);
    }
}