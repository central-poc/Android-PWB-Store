package cenergy.central.com.pwb_store.adapter.viewholder;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.manager.Contextor;
import cenergy.central.com.pwb_store.view.PowerBuyTextView;

/**
 * Created by napabhat on 7/5/2017 AD.
 */

public class TextBannerViewHolder extends RecyclerView.ViewHolder{

    CardView mCardView;
    PowerBuyTextView mTextView;

    public TextBannerViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.txt_view);
        mCardView = itemView.findViewById(R.id.card_view);
    }

    public void setViewHolder(String bannerTitle, boolean enableMargin){
        mTextView.setText(bannerTitle);
        if (enableMargin){
            CardView.LayoutParams params = new CardView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            int mContentMargin = (int) Contextor.getInstance().getContext().getResources().getDimension(R.dimen.recycler_view_content_horizontal_padding);
            params.setMargins(0, (int) Contextor.getInstance().getContext().getResources().getDimension(R.dimen.textBannerTopMargin), 0, 0);
            params.setMarginStart(mContentMargin);
            params.setMarginEnd(mContentMargin);

            mCardView.setLayoutParams(params);
        }
    }
}
