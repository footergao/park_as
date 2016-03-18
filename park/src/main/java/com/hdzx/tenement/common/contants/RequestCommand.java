package com.hdzx.tenement.common.contants;

/**
 * 
 * @author Jesley
 *
 */
public interface RequestCommand
{
	public static enum BUS_COMMAND
	{
		BUS_GET_PASSENGER("/busticket/service/CW2112"),
		BUS_EDIT_PASSENGER("/busticket/service/CW2113"),
		BUS_EDIT_PASSENGER2("/csp-gate-zjgdubbo/service/CW2113"),
		BUS_DELETE_PASSENGER("/busticket/service/CW2114"),
		BUS_SUBMIT_ORDER("/csp-gate-zjgdubbo/service/CW2104"),
		BUS_NEW_SUBMIT_ORDER("/busticket/service/CW2122"),
		BUS_ORDER_TN("/busticket/service/CW2116"),
		BUS_GET_ORDER_DETAIL("/csp-gate-zjgdubbo/service/CW2108"),
		BUS_GET_ORDER_PASSENGER("/busticket/service/CW2115"),
		END("this end.");
		
		private String value;

		private BUS_COMMAND(String name)
		{
			this.value = name;
		}

		public String getValue()
		{
			return value;
		}

		public String getCode()
		{
			return this.name();
		}

		public static String getValue(String code)
		{
			for (BUS_COMMAND item : BUS_COMMAND.values())
			{
				if (item.getCode().equals(code))
				{
					return item.getValue();
				}
			}

			return code;
		}

		@Override
		public String toString()
		{
			return this.name();
		}
	}
}
