package com.hdzx.tenement.http.protocol;

/**
 * Content Builder
 * 
 * @author Jesley
 * 
 */
public interface AppContentBuilder
{
	/**
	 * construct header of protocol
	 */
	public void buildHeader();

	/**
	 * construct body of protocol
	 */
	public void buildBody();

	/**
	 * get content of protocol
	 * 
	 * @return
	 */
	public String getContent();
}
