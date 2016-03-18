package com.hdzx.tenement.http.protocol;

import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.utils.Contants;
import com.hdzx.tenement.utils.Contants.CryptoTyepEnum;
import com.hdzx.tenement.utils.HttpUtil;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author Jesley
 *
 */
public class HttpRequestEntity
{
	private RequestContentTemplate requestContentTemplate = null;
	
	private String host = Contants.SERVER_HOST;
	
	private String path = null;
	
	private String method = null;
	
	private String requestCode = null;
	
	private Object userData = null;
	
	private String requestEncoding = HttpUtil.CHARACTER_ENCODING;
	
	private String responseEncoding = HttpUtil.CHARACTER_ENCODING;
	
	private Map<String, String> httpHeader = null;
	
	private RequestParams requestParams = null;
	
	private boolean hasData = true;
	
	private CryptoTyepEnum responseDecryptoType = CryptoTyepEnum.aes;
	
	public HttpRequestEntity()
	{
	    addHttpHeader("Content-Type", "application/json");
	}
	
	public HttpRequestEntity(RequestContentTemplate requestContentTemplate,
            String host, String path)
    {
	    this();
	    this.requestContentTemplate = requestContentTemplate;
	    this.host = host;
	    this.path = path;
    }
	
	public HttpRequestEntity(boolean isRequestTicket, Map<String, Object> head, Map<String, Object> data, String path, String requestCode)
	{
	    this();
		requestContentTemplate = new RequestContentTemplate();
		requestContentTemplate.setRequestTicket(isRequestTicket);
		requestContentTemplate.appendHead(head);
		requestContentTemplate.appendData(data);
		this.requestCode = requestCode;
		this.path = path;
	}
	
	public HttpRequestEntity(boolean isRequestTicket, Map<String, Object> head, Map<String, Object> data, String path)
	{
	    this();
		requestContentTemplate = new RequestContentTemplate();
		requestContentTemplate.setRequestTicket(isRequestTicket);
		requestContentTemplate.appendHead(head);
		requestContentTemplate.appendData(data);
		this.path = path;
	}
	
	public HttpRequestEntity(Map<String, Object> head, Map<String, Object> data, String path)
	{
	    this();
		requestContentTemplate = new RequestContentTemplate();
		requestContentTemplate.appendHead(head);
		requestContentTemplate.appendData(data);
		this.path = path;
	}

	public RequestContentTemplate getRequestContentTemplate()
	{
		return requestContentTemplate;
	}

	public void setRequestContentTemplate(
	        RequestContentTemplate requestContentTemplate)
	{
		this.requestContentTemplate = requestContentTemplate;
	}

	public String getHost()
	{
		return host;
	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getRequestEncoding()
	{
		return requestEncoding;
	}

	public void setRequestEncoding(String requestEncoding)
	{
		this.requestEncoding = requestEncoding;
	}

	public String getResponseEncoding()
	{
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding)
	{
		this.responseEncoding = responseEncoding;
	}
	
	public String getRequestUrl()
	{
		String url = host + "";
		if (path != null)
		{
			if (path.startsWith("/")) {
				url = url + path;
			}
			else 
			{
				url = url + "/" + path;
			}
		}

		url = url + ";jsessionid=" + UserSession.getInstance().getSessionId();
		return url;
	}
	
	public String getContent()
	{
		if (requestContentTemplate != null)
		{
			return requestContentTemplate.getContent();
		}
		else
		{
			return null;
		}
	}

	public String getRequestCode()
	{
		return requestCode;
	}

	public void setRequestCode(String requestCode)
	{
		this.requestCode = requestCode;
	}

	public Object getUserData()
    {
        return userData;
    }

    public void setUserData(Object userData)
    {
        this.userData = userData;
    }

    public Map<String, String> getHttpHeader()
	{
		return httpHeader;
	}

	public void setHttpHeader(Map<String, String> httpHeader)
	{
		this.httpHeader = httpHeader;
	}
	
	public void addHttpHeader(String name, String value)
	{
		if (name != null && !"".equals(name.trim()))
		{
			if (httpHeader == null)
			{
				httpHeader = new HashMap<String, String>();
			}
			
			httpHeader.put(name, value);
		}
	}

    public CryptoTyepEnum getResponseDecryptoType()
    {
        return responseDecryptoType;
    }

    public void setResponseDecryptoType(CryptoTyepEnum responseDecryptoType)
    {
        this.responseDecryptoType = responseDecryptoType;
    }

    public RequestParams getRequestParams()
    {
        return requestParams;
    }

    public void setRequestParams(RequestParams requestParams)
    {
        this.requestParams = requestParams;
    }

    public boolean isHasData()
    {
        return hasData;
    }

    public void setHasData(boolean hasData)
    {
        this.hasData = hasData;
    }
} 
