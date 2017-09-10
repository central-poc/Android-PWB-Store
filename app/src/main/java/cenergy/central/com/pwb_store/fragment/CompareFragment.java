package cenergy.central.com.pwb_store.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.adapter.CompareProductAdapter;
import cenergy.central.com.pwb_store.adapter.decoration.SpacesItemDecoration;
import cenergy.central.com.pwb_store.model.CompareDao;
import cenergy.central.com.pwb_store.model.CompareDetail;
import cenergy.central.com.pwb_store.model.CompareDetailItem;
import cenergy.central.com.pwb_store.model.CompareList;
import cenergy.central.com.pwb_store.model.ProductList;

/**
 * Created by napabhat on 7/6/2017 AD.
 */

public class CompareFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    //Data Member
    private CompareProductAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private CompareList mCompareList;
    private CompareDao mCompareDao;

    public CompareFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static CompareFragment newInstance() {
        CompareFragment fragment = new CompareFragment();
        Bundle args = new Bundle();
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
        View rootView = inflater.inflate(R.layout.fragment_compare, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        mockData();
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ButterKnife.bind(this, rootView);

        mAdapter = new CompareProductAdapter(getContext());
        mLayoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setSpanSizeLookup(mAdapter.getSpanSize());
        mAdapter.setCompare(mCompareList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

    }

    private void mockData(){
        List<ProductList> mProductListList = new ArrayList<>();
        mProductListList.add(new ProductList("1111","http://www.mx7.com/i/004/aOc9VL.png","iPhone SE","หัวใจหลักของ iPhone SE ก็คือชิพ A9 ซึ่งเป็น\n" +
                "ชิพอันล้ำสมัยแบบเดียวกับที่ใช้ใน iPhone 6s",16500,12600));
        mProductListList.add(new ProductList("2222","http://www.mx7.com/i/0e5/oFp5mm.png","iPhone SE","หัวใจหลักของ iPhone SE ก็คือชิพ A9 ซึ่งเป็น\n" +
                "ชิพอันล้ำสมัยแบบเดียวกับที่ใช้ใน iPhone 6s",16500,12600));
        mProductListList.add(new ProductList("2345","http://www.mx7.com/i/1cb/TL8yVR.png","Lenovo","Lenovo Yoga 720 เป็นแล็ปท็อป Windows 10 มีสองโมเดลคือ 13 นิ้ว (น้ำหนัก 1.3 กิโลกรัม)",35000,30000));
        mProductListList.add(new ProductList("1122","http://www.mx7.com/i/215/WAY0dD.png","EPSON","เครื่องพิมพ์มัลติฟังก์ชั่นอิงค์เจ็ท Print/ Copy/ Scan/ Fax(With ADF)",9490,9000));

        List<CompareDetailItem> compareDetailItems1 = new ArrayList<>();
        compareDetailItems1.add(new CompareDetailItem("-"));
        compareDetailItems1.add(new CompareDetailItem("Yes"));
        compareDetailItems1.add(new CompareDetailItem("Yes"));
        compareDetailItems1.add(new CompareDetailItem("Yes"));

        List<CompareDetailItem> compareDetailItems2 = new ArrayList<>();
        compareDetailItems2.add(new CompareDetailItem("Yes"));
        compareDetailItems2.add(new CompareDetailItem("A9 CHIP WITH 64BIT"));
        compareDetailItems2.add(new CompareDetailItem("Yes"));
        compareDetailItems2.add(new CompareDetailItem("Yes"));

        List<CompareDetailItem> compareDetailItems3 = new ArrayList<>();
        compareDetailItems3.add(new CompareDetailItem("-"));
        compareDetailItems3.add(new CompareDetailItem("-"));
        compareDetailItems3.add(new CompareDetailItem("Yes"));
        compareDetailItems3.add(new CompareDetailItem("Yes"));

        List<CompareDetailItem> compareDetailItems4 = new ArrayList<>();
        compareDetailItems4.add(new CompareDetailItem("-"));
        compareDetailItems4.add(new CompareDetailItem("Yes"));
        compareDetailItems4.add(new CompareDetailItem("Yes"));
        compareDetailItems4.add(new CompareDetailItem("Yes"));

        List<CompareDetailItem> compareDetailItems5 = new ArrayList<>();
        compareDetailItems5.add(new CompareDetailItem("1"));
        compareDetailItems5.add(new CompareDetailItem("2"));
        compareDetailItems5.add(new CompareDetailItem("Yes"));
        compareDetailItems5.add(new CompareDetailItem("Yes"));

        List<CompareDetail> compareDetails = new ArrayList<>();
        compareDetails.add(new CompareDetail("Bluetooth", compareDetailItems1));
        compareDetails.add(new CompareDetail("CPU", compareDetailItems2));
        compareDetails.add(new CompareDetail("Touch Screen", compareDetailItems3));
        compareDetails.add(new CompareDetail("Photo booth", compareDetailItems4));
        compareDetails.add(new CompareDetail("Warranty(Year)", compareDetailItems5));

        mCompareDao = new CompareDao(5, compareDetails);
        mCompareList = new CompareList(4, mProductListList, mCompareDao);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
