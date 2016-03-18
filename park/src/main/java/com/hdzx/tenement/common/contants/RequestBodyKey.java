package com.hdzx.tenement.common.contants;

/**
 * 
 * @author Jesley
 *
 */
public interface RequestBodyKey
{
	public static enum BUS_KEY
	{
		//获取乘客列表
		name,
		curpage,
		
		//保存乘车人
		passengerid,
		gender,
		birth,
		country,
		cardtype,
		cardnum,
		passengertype,
		phonenumber,
		telephone,
		email,
		address,
		zipcode,
		
		//删除乘客
		passengerids,
		
		//提交订单
		passengerinfo,
		startTime,
		startDate,
		bookingmobile,
		bookingname,
		halfTicketNum,
		fullTicketNum,
		isbuyinsurance,
		userid,
		offstationcode,
		shift,
		halfprice,
		fullprice,
		
		//new
		passengerlist,
		price,
		startStation,
		terminalStation,

		
		//支付
		order_no,
		order_amt,
		order_date,
		order_time,
		
		//订单详情
		orderid,
	}
}
