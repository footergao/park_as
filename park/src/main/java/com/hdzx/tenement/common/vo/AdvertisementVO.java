package com.hdzx.tenement.common.vo;

import java.util.List;

public class AdvertisementVO
{
    private String type = null;
    
    private String showTime = null;
    
    private int baseId = 0;
    
    private List<AdvertisementImage> image = null;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getShowTime()
    {
        return showTime;
    }

    public void setShowTime(String showTime)
    {
        this.showTime = showTime;
    }


    public List<AdvertisementImage> getImage()
    {
        return image;
    }

    public void setImage(List<AdvertisementImage> image)
    {
        this.image = image;
    }

	public int getBaseId() {
		return baseId;
	}

	public void setBaseId(int baseId) {
		this.baseId = baseId;
	}
}
