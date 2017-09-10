package cenergy.central.com.pwb_store.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.manager.bus.event.SpecDaoBus;
import cenergy.central.com.pwb_store.model.SpecDao;
import cenergy.central.com.pwb_store.view.PowerBuyWrapAbleGridView;

/**
 * Created by napabhat on 7/18/2017 AD.
 */

public class ProductSpecViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.layout_spec)
    LinearLayout mSpec;

    public ProductSpecViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setViewHolder(SpecDao specDao){
        mSpec.setTag(specDao);
        mSpec.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SpecDao specDao = (SpecDao) itemView.getTag();
        EventBus.getDefault().post(new SpecDaoBus(v, specDao));
    }
}
