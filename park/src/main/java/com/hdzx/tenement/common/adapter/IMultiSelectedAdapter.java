package com.hdzx.tenement.common.adapter;

import java.util.List;
import java.util.Set;

public interface IMultiSelectedAdapter<T>
{
	public Set<Integer> getSelectedIndexes();
	
	public void clearSelectedIndexes();
	
	public void addSelectedIndex(int selectedIndex);
	
	public void togleSelectedIndex(int selectedIndex);
	
	public List<T> getSelectedItem();
}
