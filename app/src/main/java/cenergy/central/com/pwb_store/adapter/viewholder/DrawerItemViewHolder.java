package cenergy.central.com.pwb_store.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cenergy.central.com.pwb_store.R;
import cenergy.central.com.pwb_store.manager.bus.event.DrawItemBus;
import cenergy.central.com.pwb_store.model.DrawerItem;

/**
 * Created by napabhat on 6/29/2017 AD.
 */

public class DrawerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public DrawerItemViewHolder(View itemView) {
        super(itemView);
    }

    public void setViewHolder(DrawerItem drawerItem) {
        ((TextView)itemView.findViewById(R.id.title)).setText(drawerItem.getTitle());
        itemView.setTag(drawerItem);
        itemView.setOnClickListener(this);
    }

//    public void setViewHolder(Category category) {
//        mTitle.setText(category.getDepartmentNameEN());
//        itemView.setTag(category);
//        itemView.setOnClickListener(this);
//    }

    @Override
    public void onClick(View view) {
        DrawerItem drawerItem = (DrawerItem) itemView.getTag();
        EventBus.getDefault().post(new DrawItemBus(drawerItem));
    }
}
