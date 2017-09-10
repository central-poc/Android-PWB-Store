package cenergy.central.com.pwb_store.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napabhat on 10/25/2016 AD.
 */

public class ProductDetail implements IViewType, Parcelable {

    public static final Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {
        @Override
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        @Override
        public ProductDetail[] newArray(int size) {
            return new ProductDetail[size];
        }
    };

    private int viewTypeId;
    private int masterId;
    private String slug;
    private String brand;
    private String productName;
    private String bu;
    private ProductDetailImage productImageList;
    private double savePercent;
    private double originalPrice;
    private double price;
    private int quantity;
    private int minQuantity;
    private int maxQuantity;
    private String productCode;
    private String description;
    private String descriptionHtml;
    private String shippingReturn;
    private String shippingReturnHtml;
    private String deliveryDescription;
    private String deliveryHtml;
    private String warrantyDescription;
    private String warrantyHtml;
    private int stockAvailable;
    private TheOneCardProductDetail theOneCardDetailList;
    private List<ProductDetailPromotion> detailPromotionList;
    private ProductDetailOption productDetailOption;
    private SpecDao mSpecDao;

    public ProductDetail(String slug, String brand, String productName, String productCode, String bu, ProductDetailImage productDetailImages,
                         double savePercent, double originalPrice, double price, int quantity, int minQuantity, int maxQuantity, TheOneCardProductDetail theOneCardDetailList,
                         List<ProductDetailPromotion> detailPromotionList, ProductDetailOption productDetailOption, SpecDao specDao) {
        this.slug = slug;
        this.brand = brand;
        this.productName = productName;
        this.productCode = productCode;
        this.bu = bu;
        this.productImageList = productDetailImages;
        this.savePercent = savePercent;
        this.originalPrice = originalPrice;
        this.price = price;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.theOneCardDetailList = theOneCardDetailList;
        this.detailPromotionList = detailPromotionList;
        this.productDetailOption = productDetailOption;
        this.mSpecDao = specDao;
    }

    protected ProductDetail(Parcel in) {
        viewTypeId = in.readInt();
        masterId = in.readInt();
        slug = in.readString();
        brand = in.readString();
        productName = in.readString();
        productImageList = in.readParcelable(ProductDetailImage.class.getClassLoader());
        savePercent = in.readDouble();
        originalPrice = in.readDouble();
        price = in.readDouble();
        quantity = in.readInt();
        minQuantity = in.readInt();
        maxQuantity = in.readInt();
        productCode = in.readString();
        description = in.readString();
        descriptionHtml = in.readString();
        shippingReturn = in.readString();
        shippingReturnHtml = in.readString();
        deliveryDescription = in.readString();
        deliveryHtml = in.readString();
        warrantyDescription = in.readString();
        warrantyHtml = in.readString();
        stockAvailable = in.readInt();
        theOneCardDetailList = in.readParcelable(TheOneCardProductDetail.class.getClassLoader());
        detailPromotionList = in.createTypedArrayList(ProductDetailPromotion.CREATOR);
        productDetailOption = in.readParcelable(ProductDetailOption.class.getClassLoader());
        mSpecDao = in.readParcelable(SpecDao.class.getClassLoader());
    }

    public void replaceProduct(ProductDetailOptionItem productDetailOptionItem) {
        this.productCode = productDetailOptionItem.getProductId();
        this.productName = productDetailOptionItem.getProductName();
        this.price = productDetailOptionItem.getPrice();
        this.originalPrice = productDetailOptionItem.getOriginalPrice();
        this.stockAvailable = productDetailOptionItem.getStockAvailable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewTypeId);
        dest.writeInt(masterId);
        dest.writeString(slug);
        dest.writeString(brand);
        dest.writeString(productName);
        dest.writeParcelable(productImageList, flags);
        dest.writeDouble(savePercent);
        dest.writeDouble(originalPrice);
        dest.writeDouble(price);
        dest.writeInt(quantity);
        dest.writeInt(minQuantity);
        dest.writeInt(maxQuantity);
        dest.writeString(productCode);
        dest.writeString(description);
        dest.writeString(descriptionHtml);
        dest.writeString(shippingReturn);
        dest.writeString(shippingReturnHtml);
        dest.writeString(deliveryDescription);
        dest.writeString(deliveryHtml);
        dest.writeString(warrantyDescription);
        dest.writeString(warrantyHtml);
        dest.writeInt(stockAvailable);
        dest.writeParcelable(theOneCardDetailList, flags);
        dest.writeList(detailPromotionList);
        dest.writeParcelable(productDetailOption, flags);
        dest.writeParcelable(mSpecDao, flags);
    }

    @Override
    public int getViewTypeId() {
        return viewTypeId;
    }

    @Override
    public void setViewTypeId(int id) {
        this.viewTypeId = id;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public ProductDetailImage getProductImageList() {
        if (productImageList == null) {
            List<ProductDetailImageItem> productDetailImageItems = new ArrayList<>();
            productDetailImageItems.add(new ProductDetailImageItem("1", ""));
            productDetailImageItems.add(new ProductDetailImageItem("2", ""));
            productDetailImageItems.add(new ProductDetailImageItem("3", ""));
            productDetailImageItems.add(new ProductDetailImageItem("4", ""));
            productDetailImageItems.add(new ProductDetailImageItem("5", ""));
            productDetailImageItems.add(new ProductDetailImageItem("6", ""));

            productImageList = new ProductDetailImage(4, productDetailImageItems);
        }
        return productImageList;
    }

    public void setProductImageList(ProductDetailImage productImageList) {
        this.productImageList = productImageList;
    }

    public double getSavePercent() {
        return savePercent;
    }

    public void setSavePercent(double savePercent) {
        this.savePercent = savePercent;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionHtml() {
        return descriptionHtml;
    }

    public void setDescriptionHtml(String descriptionHtml) {
        this.descriptionHtml = descriptionHtml;
    }

    public String getShippingReturn() {
        return shippingReturn;
    }

    public void setShippingReturn(String shippingReturn) {
        this.shippingReturn = shippingReturn;
    }

    public String getShippingReturnHtml() {
        return shippingReturnHtml;
    }

    public void setShippingReturnHtml(String shippingReturnHtml) {
        this.shippingReturnHtml = shippingReturnHtml;
    }

    public String getDeliveryDescription() {
        return deliveryDescription;
    }

    public void setDeliveryDescription(String deliveryDescription) {
        this.deliveryDescription = deliveryDescription;
    }

    public String getDeliveryHtml() {
        return deliveryHtml;
    }

    public void setDeliveryHtml(String deliveryHtml) {
        this.deliveryHtml = deliveryHtml;
    }

    public String getWarrantyDescription() {
        return warrantyDescription;
    }

    public void setWarrantyDescription(String warrantyDescription) {
        this.warrantyDescription = warrantyDescription;
    }

    public String getWarrantyHtml() {
        return warrantyHtml;
    }

    public void setWarrantyHtml(String warrantyHtml) {
        this.warrantyHtml = warrantyHtml;
    }

    public int getStockAvailable() {
        return stockAvailable;
    }

    public void setStockAvailable(int stockAvailable) {
        this.stockAvailable = stockAvailable;
    }

    public TheOneCardProductDetail getTheOneCardDetailList() {
        return new TheOneCardProductDetail(7000, 2500);
    }

    public void setTheOneCardDetailList(TheOneCardProductDetail theOneCardDetailList) {
        this.theOneCardDetailList = theOneCardDetailList;
    }

    public List<ProductDetailPromotion> getDetailPromotionList() {

        List<ProductDetailPromotion> productDetailPromotionList = new ArrayList<>();
        productDetailPromotionList.add(new ProductDetailPromotion("1", "http://www.uppic.org/image-4E3E_57D7C9D3.jpg"));
        productDetailPromotionList.add(new ProductDetailPromotion("2", "http://www.uppic.org/image-4391_57D7C9D3.jpg"));
        productDetailPromotionList.add(new ProductDetailPromotion("3", "http://www.uppic.org/image-4E3E_57D7C9D3.jpg"));
        return detailPromotionList = productDetailPromotionList;
    }

    public void setDetailPromotionList(List<ProductDetailPromotion> detailPromotionList) {
        this.detailPromotionList = detailPromotionList;
    }

    public ProductDetailOption getProductDetailOption() {
        return productDetailOption;
    }

    public void setProductDetailOption(ProductDetailOption productDetailOption) {
        this.productDetailOption = productDetailOption;
    }

    public SpecDao getSpecDao() {
        return mSpecDao;
    }

    public void setSpecDao(SpecDao specDao) {
        mSpecDao = specDao;
    }
}
