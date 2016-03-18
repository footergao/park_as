package com.hdzx.tenement.community.vo;

import android.view.View;

import java.io.Serializable;
import java.util.List;

public class ServiceBean implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Integer id = null;
    
    private String serviceName = null;
    
    private String serviceSort = null;
    
    private String serviceCompType = null;
    
    private String serviceHasChild = null;
    
    private String serviceStatus = null;
    
    private String serviceCreator = null;
    
    private String serviceType = null;
    
    private String pid = null;
    
    private String deep = null;
    
    private String serviceIcon = null;
    
    private List<ServiceBean> children = null;
    
    private String mappingKey = null;
    
    private transient View viewHolder = null;
    
    private transient Object value = null;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getServiceSort()
    {
        return serviceSort;
    }

    public void setServiceSort(String serviceSort)
    {
        this.serviceSort = serviceSort;
    }

    public String getServiceCompType()
    {
        return serviceCompType;
    }

    public void setServiceCompType(String serviceCompType)
    {
        this.serviceCompType = serviceCompType;
    }

    public String getServiceHasChild()
    {
        return serviceHasChild;
    }

    public void setServiceHasChild(String serviceHasChild)
    {
        this.serviceHasChild = serviceHasChild;
    }

    public String getServiceStatus()
    {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus)
    {
        this.serviceStatus = serviceStatus;
    }

    public String getServiceCreator()
    {
        return serviceCreator;
    }

    public void setServiceCreator(String serviceCreator)
    {
        this.serviceCreator = serviceCreator;
    }

    public String getServiceType()
    {
        return serviceType;
    }

    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public String getPid()
    {
        return pid;
    }

    public void setPid(String pid)
    {
        this.pid = pid;
    }

    public String getDeep()
    {
        return deep;
    }

    public void setDeep(String deep)
    {
        this.deep = deep;
    }

    public List<ServiceBean> getChildren()
    {
        return children;
    }

    public void setChildren(List<ServiceBean> children)
    {
        this.children = children;
    }

    public String getMappingKey()
    {
        return mappingKey;
    }

    public void setMappingKey(String mappingKey)
    {
        this.mappingKey = mappingKey;
    }

    public String getServiceIcon()
    {
        return serviceIcon;
    }

    public void setServiceIcon(String serviceIcon)
    {
        this.serviceIcon = serviceIcon;
    }

    public View getViewHolder()
    {
        return viewHolder;
    }

    public void setViewHolder(View viewHolder)
    {
        this.viewHolder = viewHolder;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }
}
