package com.hdzx.tenement.common.adapter;

public interface ISingleSelectedAdapter<T>
{
	public int getSelectedIndex();
	
	public void setSelectedIndex(int selectedIndex);
	
	public T getSelectedItem();
}
