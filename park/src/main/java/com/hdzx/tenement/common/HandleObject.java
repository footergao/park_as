package com.hdzx.tenement.common;

public class HandleObject
{
    private boolean isSucess = true;
    
    private Object object =  null;

    public boolean isSucess()
    {
        return isSucess;
    }

    public void setSucess(boolean isSucess)
    {
        this.isSucess = isSucess;
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object object)
    {
        this.object = object;
    }
}
