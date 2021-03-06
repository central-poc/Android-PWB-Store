package cenergy.central.com.pwb_store.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by napabhat on 10/26/2016 AD.
 */

public class ReviewDetailText implements IViewType, Parcelable {
    public static final int MODE_DESCRIPTION = 0;

    public static final Creator<ReviewDetailText> CREATOR = new Creator<ReviewDetailText>() {
        @Override
        public ReviewDetailText createFromParcel(Parcel in) {
            return new ReviewDetailText(in);
        }

        @Override
        public ReviewDetailText[] newArray(int size) {
            return new ReviewDetailText[size];
        }
    };
    private int viewTypeId;
    //private String title;
    private String html;
    private int mode;

    public ReviewDetailText(String html, int mode) {
        //this.title = title;
        this.html = html;
        this.mode = mode;
    }

    protected ReviewDetailText(Parcel in) {
        viewTypeId = in.readInt();
        //title = in.readString();
        mode = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(viewTypeId);
        //dest.writeString(title);
        dest.writeString(html);
        dest.writeInt(mode);
    }

    @Override
    public int getViewTypeId() {
        return viewTypeId;
    }

    @Override
    public void setViewTypeId(int id) {
        this.viewTypeId = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
