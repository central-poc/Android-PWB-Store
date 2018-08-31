package cenergy.central.com.pwb_store.realm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.Date;
import java.util.List;

import cenergy.central.com.pwb_store.model.AddCompare;
import cenergy.central.com.pwb_store.model.Brand;
import cenergy.central.com.pwb_store.model.CacheCartItem;
import cenergy.central.com.pwb_store.model.CachedEndpoint;
import cenergy.central.com.pwb_store.model.Category;
import cenergy.central.com.pwb_store.model.CompareProduct;
import cenergy.central.com.pwb_store.model.Product;
import cenergy.central.com.pwb_store.model.UserInformation;
import cenergy.central.com.pwb_store.model.response.OrderResponse;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by napabhat on 9/13/2017 AD.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController with(Context context) {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm != null ? realm : Realm.getDefaultInstance();
    }

    public AddCompare getCompare(String id) {
        return realm.where(AddCompare.class).equalTo("productId", id).findFirst();
    }

    public long getCount() {
        long count = realm.where(AddCompare.class).count();
        return count;
    }

    //find all objects in the AppCompare.class
    public RealmResults<AddCompare> getCompares() {

        //return realm.where(AddCompare.class).findAll();
        return realm.where(AddCompare.class).findAllSorted("productSku", Sort.DESCENDING);
    }

    public RealmResults<AddCompare> deletedCompare(final String productSku) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AddCompare> result = realm.where(AddCompare.class).equalTo("productSku", productSku).findAllSorted("productSku", Sort.DESCENDING);
                result.deleteAllFromRealm();
            }
        });

        return realm.where(AddCompare.class).findAll();
    }

    // region category
    public void saveCategory(final Category category) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                // delete category
                realm.delete(Category.class);
                // store category
                realm.insertOrUpdate(category);
            }
        });
    }

    public Category getCategory() {
        Realm realm = getRealm();
        return realm.where(Category.class).findFirst();
    }
    // endregion

    // region compare product
    public void saveCompareProduct(final Product product, final DatabaseListener listener) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                CompareProduct compareProduct = CompareProduct.asCompareProduct(product);
                realm.copyToRealmOrUpdate(compareProduct);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                if (listener != null) {
                    listener.onSuccessfully();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                if (listener != null) {
                    listener.onFailure(error);
                }
            }
        });
    }

    public List<CompareProduct> deleteCompareProduct(final String sku) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<CompareProduct> realmCompareProducts = realm.where(CompareProduct.class).equalTo(CompareProduct.FIELD_SKU, sku).findAll();
                realmCompareProducts.deleteAllFromRealm();
            }
        });

        return getCompareProducts();
    }

    public List<CompareProduct> getCompareProducts() {
        Realm realm = getRealm();
        RealmResults<CompareProduct> realmCompareProducts = realm.where(CompareProduct.class).sort(CompareProduct.FIELD_SKU, Sort.DESCENDING).findAll();
        return realmCompareProducts == null ? null : realm.copyFromRealm(realmCompareProducts);
    }

    public void deleteAllCompareProduct() {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(CompareProduct.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public CompareProduct getCompareProduct(String sku) {
        return realm.where(CompareProduct.class).equalTo(CompareProduct.FIELD_SKU, sku).findFirst();
    }
    // endregion

    // region cart item
    public void saveCartItem(final CacheCartItem cacheCartItem) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(cacheCartItem);
            }
        });
    }

    public void saveCartItem(final CacheCartItem cacheCartItem, final DatabaseListener listener) {
        Realm realm = getRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(cacheCartItem);
            }
        }, new Realm.Transaction.OnSuccess() {

            @Override
            public void onSuccess() {
                if (listener != null) {
                    Log.d("Database", "stored cart item");
                    listener.onSuccessfully();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                if (listener != null) {
                    listener.onFailure(error);
                }
            }
        });
    }

    public List<CacheCartItem> deleteCartItem(final Long itemId) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<CacheCartItem> realmCompareProducts = realm.where(CacheCartItem.class).equalTo(
                        CacheCartItem.FIELD_ID, itemId).findAll();
                realmCompareProducts.deleteAllFromRealm();
            }
        });

        return getCacheCartItems();
    }

    public List<CacheCartItem> getCacheCartItems() {
        Realm realm = getRealm();
        RealmResults<CacheCartItem> realmCartItems = realm.where(CacheCartItem.class).sort(CacheCartItem.FIELD_ID, Sort.DESCENDING).findAll();
        return realmCartItems == null ? null : realm.copyFromRealm(realmCartItems);
    }

    public CacheCartItem getCacheCartItem(Long itemId) {
        CacheCartItem realmCartItem = realm.where(CacheCartItem.class).equalTo(CacheCartItem.FIELD_ID, itemId).findFirst();
        return realmCartItem == null ? null : realm.copyFromRealm(realmCartItem);
    }

    public void deleteAllCacheCartItem() {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(CacheCartItem.class).findAll().deleteAllFromRealm();
            }
        });
    }
    // endregion

    // region Order
    public void saveOrderResponse(final OrderResponse orderResponse) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(orderResponse);
            }
        });
    }

    public List<OrderResponse> deleteOrderResponse(final String orderId) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<OrderResponse> realmOrderResponses = realm.where(OrderResponse.class).equalTo(
                        OrderResponse.FIELD_ORDER_ID, orderId).findAll();
                realmOrderResponses.deleteAllFromRealm();
            }
        });

        return getOrderResponses();
    }

    public List<OrderResponse> getOrderResponses() {
        Realm realm = getRealm();
        RealmResults<OrderResponse> realmOrderResponses = realm.where(OrderResponse.class).sort(OrderResponse.FIELD_ORDER_ID, Sort.DESCENDING).findAll();
        return realmOrderResponses == null ? null : realm.copyFromRealm(realmOrderResponses);
    }

    public OrderResponse getOrderResponse(String orderId) {
        OrderResponse realmOrderResponse = realm.where(OrderResponse.class).equalTo(OrderResponse.FIELD_ORDER_ID, orderId).findFirst();
        return realmOrderResponse == null ? null : realm.copyFromRealm(realmOrderResponse);
    }

    public void deleteAllOrderResponse() {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(OrderResponse.class).findAll().deleteAllFromRealm();
            }
        });
    }
    // endregion

    // region brands
    public void saveBands(final Brand brand) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(brand);
            }
        });
    }

    public Brand getBrand(Long orderId) {
        Brand realmBrand = realm.where(Brand.class).equalTo(Brand.FIELD_BRAN_ID, orderId).findFirst();
        return realmBrand == null ? null : realm.copyFromRealm(realmBrand);
    }

    public void deleteBrans() {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Brand.class).findAll().deleteAllFromRealm();
            }
        });
    }
    // endregion

    // region user
    public void  saveUserInformation(final UserInformation userInformation) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.copyToRealmOrUpdate(userInformation);
            }
        });
    }

    public UserInformation getUserInformation() {
        UserInformation realmUser = realm.where(UserInformation.class).findFirst();
        return realmUser == null ? null : realm.copyFromRealm(realmUser);
    }

    public void deleteUserInformation() {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(UserInformation.class).findAll().deleteAllFromRealm();
            }
        });
    }
    // endregion

    // region caching
    public void updateCachedEndpoint(String endpoint) {
        final CachedEndpoint cachedEndpoint = new CachedEndpoint();
        cachedEndpoint.setEndpoint(endpoint);
        cachedEndpoint.setLastUpdated(new Date());

        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(cachedEndpoint);
            }
        });
    }

    public void clearCachedEndpoint(final String endpoint) {
        Realm realm = getRealm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(CachedEndpoint.class).like("endpoint", endpoint).findAll().deleteAllFromRealm();
            }
        });
    }

    public boolean hasFreshlyCachedEndpoint(String endpoint) {
        return hasFreshlyCachedEndpoint(endpoint, 1);
    }

    private boolean hasFreshlyCachedEndpoint(String endpoint, int hours) {
        Realm realm = getRealm();
        CachedEndpoint cachedEndpoint = realm.where(CachedEndpoint.class).equalTo("endpoint", endpoint).findFirst();
        return cachedEndpoint != null && cachedEndpoint.getLastUpdated().after(new Date(System.currentTimeMillis() - (hours * 60 * 60 * 1000)));
    }
    // end region

    public void logout() {
        deleteAllCacheCartItem();
        deleteAllCompareProduct();
    }
}