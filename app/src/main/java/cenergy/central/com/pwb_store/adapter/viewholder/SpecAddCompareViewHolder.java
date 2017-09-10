package cenergy.central.com.pwb_store.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import cenergy.central.com.pwb_store.R;

/**
 * Created by napabhat on 8/4/2017 AD.
 */

public class SpecAddCompareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @BindView(R.id.card_view_add_compare)
    CardView mCardView;

    public SpecAddCompareViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setViewHolder(){
        mCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
