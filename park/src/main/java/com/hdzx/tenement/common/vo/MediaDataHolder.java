package com.hdzx.tenement.common.vo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.MEDIA_TYPE;

public class MediaDataHolder implements Parcelable
{
    private String text = null;

    private Contants.MEDIA_TYPE type = null;

    private Uri uri = null;
    
    private Uri big_uri = null;

    public Uri getBig_uri() {
		return big_uri;
	}

	public void setBig_uri(Uri big_uri) {
		this.big_uri = big_uri;
	}

	private boolean isDelete = false;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Contants.MEDIA_TYPE getType()
    {
        return type;
    }

    public void setType(Contants.MEDIA_TYPE type)
    {
        this.type = type;
    }

    public Uri getUri()
    {
        return uri;
    }

    public void setUri(Uri uri)
    {
        this.uri = uri;
    }

    public boolean isDelete()
    {
        return isDelete;
    }

    public void setDelete(boolean isDelete)
    {
        this.isDelete = isDelete;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(text);
        dest.writeValue(type);
        dest.writeParcelable(uri, 0);
        dest.writeValue(isDelete);
    }

    public static final Parcelable.Creator<MediaDataHolder> CREATOR = new Creator<MediaDataHolder>()
    {
        @Override
        public MediaDataHolder[] newArray(int size)
        {
            return new MediaDataHolder[size];
        }

        @Override
        public MediaDataHolder createFromParcel(Parcel in)
        {
            MediaDataHolder instance = new MediaDataHolder();
            instance.setText(in.readString());
            instance.setType((MEDIA_TYPE) in.readValue(null));
            instance.setUri((Uri) in.readParcelable(null));
            instance.setDelete((Boolean) in.readValue(null));
            
            return instance;
        }
    };
}
