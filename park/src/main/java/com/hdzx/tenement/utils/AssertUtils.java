package com.hdzx.tenement.utils;

/**
 * 
 * @author Jesley
 *
 */
public class AssertUtils
{
	public static void asserts(boolean expression, String failedMessage)
	{
		if (!(expression))
		{
			throw new AssertionError(failedMessage);
		}
	}
}
