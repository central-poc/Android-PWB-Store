package cenergy.central.com.pwb_store.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.activity.AvaliableStoreActivity;
import cenergy.central.com.pwb_store.activity.WebViewActivity;
import cenergy.central.com.pwb_store.adapter.ProductDetailAdapter;
import cenergy.central.com.pwb_store.adapter.decoration.ProductGridSpacesItemDecoration;
import cenergy.central.com.pwb_store.manager.bus.event.BookTimeBus;
import cenergy.central.com.pwb_store.manager.bus.event.ProductBus;
import cenergy.central.com.pwb_store.manager.bus.event.ProductDetailOptionItemBus;
import cenergy.central.com.pwb_store.manager.bus.event.StoreAvaliableBus;
import cenergy.central.com.pwb_store.manager.bus.event.UpdateBageBus;
import cenergy.central.com.pwb_store.model.AddCompare;
import cenergy.central.com.pwb_store.model.ProductDetail;
import cenergy.central.com.pwb_store.model.ProductDetailImageItem;
import cenergy.central.com.pwb_store.model.ProductDetailOptionItem;
import cenergy.central.com.pwb_store.model.Recommend;
import cenergy.central.com.pwb_store.realm.RealmController;
import cenergy.central.com.pwb_store.utils.DialogUtils;
import io.realm.Realm;

/**
 * Created by napabhat on 7/6/2017 AD.
 */

public class ProductDetailFragment extends Fragment {
    private static final String TAG = ProductDetailFragment.class.getSimpleName();
    private static final String ARG_PRODUCT_DETAIL = "ARG_PRODUCT_DETAIL";
    private static final String ARG_RECOMMEND = "ARG_RECOMMEND";
    private static final String ARG_PRODUCT_REALM = "ARG_PRODUCT_REALM";

    //View Members
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    //Data Member
    private ProductDetailAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private ProductDetail mProductDetail;
    private Recommend mRecommend;
    private ProductDetail mRealmProductDetail;
    // Realm
    private Realm mRealm;
    private Handler mHandler;
    private Runnable runOnUiThread;
    private AddCompare addCompare;
    private long count;
    private ProgressDialog mProgressDialog;

    public ProductDetailFragment() {
        super();
    }

    @Subscribe
    public void onEvent(ProductDetailOptionItemBus productDetailOptionItemBus) {
        ProductDetailOptionItem productDetailOptionItem = productDetailOptionItemBus.getProductDetailOptionItem();
        mProductDetail.getProductDetailOption().setSelectedProductDetailOptionItem(productDetailOptionItem);
        mProductDetail.replaceProduct(productDetailOptionItem);
        mAdapter.updateProductDetailOption(mProductDetail, productDetailOptionItem, productDetailOptionItemBus.getAdapterPosition());
    }

    @Subscribe
    public void onEvent(StoreAvaliableBus storeAvaliableBus){
        Intent intent = new Intent(getContext(), AvaliableStoreActivity.class);
        //intent.putExtra(AvaliableStoreActivity.ARG_PRODUCT_ID, storeAvaliableBus.getProductId());
        ActivityCompat.startActivity(getContext(), intent,
                ActivityOptionsCompat
                        .makeScaleUpAnimation(storeAvaliableBus.getView(), 0, 0, storeAvaliableBus.getView().getWidth(), storeAvaliableBus.getView().getHeight())
                        .toBundle());
    }

    @Subscribe
    public void onEvent(BookTimeBus bookTimeBus){
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.ARG_WEB_URL, getContext().getResources().getString(R.string.hdl_url));
        intent.putExtra(WebViewActivity.ARG_MODE, WebViewFragment.MODE_URL);
        intent.putExtra(WebViewActivity.ARG_TITLE, "Web");

        ActivityCompat.startActivity(getContext(), intent,
                ActivityOptionsCompat
                        .makeScaleUpAnimation(bookTimeBus.getView(), 0, 0, bookTimeBus.getView().getWidth(), bookTimeBus.getView().getHeight())
                        .toBundle());
    }

    @Subscribe
    public void onEvent(ProductBus productBus){
        mRealmProductDetail = productBus.getProductDetail();
        showProgressDialog();
        long count = RealmController.with(this).getCount();
        Log.d(TAG, "" + count);
//        if (mRealmProductDetail.getStockAvailable() == 0){
//            mProgressDialog.dismiss();
//            showAlertDialog(getContext().getResources().getString(R.string.alert_stock));
//        }else {
            if (count >= 4){
                mProgressDialog.dismiss();
                showAlertDialog(getContext().getResources().getString(R.string.alert_count));
            }else {
                String id = checkPrimary();
                if (id.equalsIgnoreCase(mRealmProductDetail.getProductCode())){
                    mProgressDialog.dismiss();
                    showAlertDialog(getContext().getResources().getString(R.string.alert_compare)
                            + "" + mRealmProductDetail.getProductName() + "" +
                            getContext().getResources().getString(R.string.alert_compare_yes));
                }else {
                    insertCompare();
                }
            }
    //    }
    }



    @SuppressWarnings("unused")
    public static ProductDetailFragment newInstance(ProductDetail productDetail, Recommend recommend) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUCT_DETAIL, productDetail);
        args.putParcelable(ARG_RECOMMEND, recommend);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        if (getArguments() != null) {
            //productId = getArguments().getString(ARG_PRODUCT_ID);
            mProductDetail = getArguments().getParcelable(ARG_PRODUCT_DETAIL);
            mRecommend = getArguments().getParcelable(ARG_RECOMMEND);
        }
        mHandler = new Handler();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ButterKnife.bind(this, rootView);

        //get realm instance
        this.mRealm = RealmController.with(this).getRealm();
        //mRealm = Realm.getDefaultInstance();

        mAdapter = new ProductDetailAdapter(getContext(), getChildFragmentManager());
        mLayoutManager = new GridLayoutManager(getContext(), 7, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSpanSizeLookup(mAdapter.getSpanSize());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new ProductGridSpacesItemDecoration(getContext(), R.dimen.product_item_spacing));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setProductDetail(mProductDetail);

        mAdapter.setProductRecommend(mRecommend);

    }

    private void insertCompare() {

        if (runOnUiThread != null) {
            mHandler.removeCallbacks(runOnUiThread);
        }

        runOnUiThread = new Runnable() {
            @Override
            public void run() {
                mRealm.executeTransactionAsync(
                        new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (mRealmProductDetail != null){
                                    addCompare = new AddCompare();

                                    addCompare.setProductId(mRealmProductDetail.getProductCode());
                                    addCompare.setProductName(mRealmProductDetail.getProductName());
                                    addCompare.setProductNameEN(mRealmProductDetail.getProductNameEN());
                                    addCompare.setUrlName(mRealmProductDetail.getUrlName());
                                    addCompare.setUrlNameEN(mRealmProductDetail.getUrlNameEN());
                                    addCompare.setDescription(mRealmProductDetail.getDescription());
                                    addCompare.setDescriptionEN(mRealmProductDetail.getDescriptionEN());
                                    addCompare.setReview(mRealmProductDetail.getReview());
                                    addCompare.setReviewEN(mRealmProductDetail.getReviewEN());
                                    addCompare.setBarcode(mRealmProductDetail.getBarcode());
                                    addCompare.setNumOfImage(mRealmProductDetail.getNumOfImage());
                                    addCompare.setCanInstallment(mRealmProductDetail.isCanInstallment());
                                    addCompare.setT1cPoint(mRealmProductDetail.getT1cPoint());
                                    addCompare.setDepartmentId(mRealmProductDetail.getDepartmentId());
                                    addCompare.setBrandId(mRealmProductDetail.getBrandId());
                                    addCompare.setBrand(mRealmProductDetail.getBrand());
                                    addCompare.setBrandEN(mRealmProductDetail.getBrandEN());
                                    for (ProductDetailImageItem productDetailImageItem :
                                            mRealmProductDetail.getProductDetailImageItems()) {
                                        addCompare.setUrl(productDetailImageItem.getImgUrl());
                                    }
                                    addCompare.setOriginalPrice(mRealmProductDetail.getOriginalPrice());
                                    addCompare.setPrice(mRealmProductDetail.getPrice());
                                    addCompare.setStoreId(mRealmProductDetail.getStoreId());
                                    addCompare.setStockAvailable(mRealmProductDetail.getStockAvailable());
                                }

                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                mRealm.beginTransaction();
                                //mRealm.copyToRealm(addCompare);
                                mRealm.copyToRealmOrUpdate(addCompare);
                                mRealm.commitTransaction();
                                EventBus.getDefault().post(new UpdateBageBus(true));
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), "Generate compare complete.", Toast.LENGTH_SHORT).show();

                            }

                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                                mProgressDialog.dismiss();
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        };

        mHandler.postDelayed(runOnUiThread, 1000);

    }

    private void clearCompare() {
        mRealm.beginTransaction();
        mRealm.delete(AddCompare.class);
        mRealm.commitTransaction();
    }

    private String checkPrimary(){
        String result;
        String id = mRealmProductDetail.getProductCode();
        addCompare = RealmController.with(this).getCompare(id);
        if (addCompare == null){
            result = "";
        }else {
            result = addCompare.getProductId();
        }

        return result;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        //mRealm.close();
        super.onDetach();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
        outState.putParcelable(ARG_PRODUCT_DETAIL, mProductDetail);
        outState.putParcelable(ARG_RECOMMEND, mRecommend);
        outState.putParcelable(ARG_PRODUCT_REALM, mRealmProductDetail);
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
        mProductDetail = savedInstanceState.getParcelable(ARG_PRODUCT_DETAIL);
        mRecommend = savedInstanceState.getParcelable(ARG_RECOMMEND);
        mRealmProductDetail = savedInstanceState.getParcelable(ARG_PRODUCT_REALM);
    }

    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        builder.show();
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = DialogUtils.createProgressDialog(getContext());
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

}
