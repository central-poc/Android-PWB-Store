package cenergy.central.com.pwb_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.model.ShippingItem;
import cenergy.central.com.pwb_store.view.PowerBuyTextView;

/**
 * Created by napabhat on 9/22/2017 AD.
 */

public class ShippingCalendarAdapter extends BaseAdapter {
    private Context mContext;
    private List<ShippingItem> mShippingItems;

    public ShippingCalendarAdapter(Context context, List<ShippingItem> shippingItems) {
        this.mContext = context;
        this.mShippingItems = shippingItems;
    }

    @Override
    public int getCount() {
        return mShippingItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ShippingItem shippingItem = mShippingItems.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.list_item_calendar_text, null);

            final PowerBuyTextView nameTextView = convertView.findViewById(R.id.txt_calendar);

            final ViewHolder viewHolder = new ViewHolder(nameTextView);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        if (shippingItem.getDescription().equalsIgnoreCase(mContext.getString(R.string.full))) {
            viewHolder.nameTextView.setText(shippingItem.getDescription());
            viewHolder.nameTextView.setTextColor(convertView.getResources().getColor(R.color.dangerColor));
        } else if (shippingItem.getDescription().equalsIgnoreCase("-")){
            viewHolder.nameTextView.setText(shippingItem.getDescription());
            viewHolder.nameTextView.setTextColor(convertView.getResources().getColor(R.color.grayTextColor));
        } else {
            viewHolder.nameTextView.setText(mContext.getString(R.string.free));
            viewHolder.nameTextView.setTextColor(convertView.getResources().getColor(R.color.inStockColor));
        }
        return convertView;
    }

    public void setShippingItems(List<ShippingItem> shippingItems) {
        this.mShippingItems = shippingItems;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private final PowerBuyTextView nameTextView;

        public ViewHolder(PowerBuyTextView nameTextView) {
            this.nameTextView = nameTextView;
        }
    }
}
