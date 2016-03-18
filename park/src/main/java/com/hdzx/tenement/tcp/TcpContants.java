package com.hdzx.tenement.tcp;

/**
 * 
 * @author Jesley
 *
 */
public final class TcpContants
{
    public static byte[] SESSION_ID = null;

    public static volatile boolean isSessionValid = false;

    public static final int SOCKET_CONNECTION_TIMEOUT_SEC = 5000;

    public static final int SOCKET_TIMEOUT_SEC = 5000;

    public static final String PACKAGE_EOF = "@#@#@#@#";

    // public static final String SERVER_DOMAIN = "127.0.0.1";
    //
    // public static final int SERVER_PORT = 2000;

    public static String SERVER_DOMAIN = "127.0.0.1";

    public static int SERVER_PORT = 2000;
    
    public static final String APPID = "BAS-0512-0001";

    public static final String VERSION = "1.0";
    
    public static final String OS = "and";
    
    public static final String CHARACTER_ENCODING = "UTF-8";
    
    public static final String DATA_KEY_ENTITY = "entity";
    
    public static final String DATA_KEY_RESPONSE_TEMPLATE = "response_template";
    
    public static final String TCP_RESPONSE_BROADCAST = "action.com.hdzx.tenement.tcp_response_broadcast";
    
    public static final String TCP_RESPONSE_BROADCAST_DIRTY = "action.com.hdzx.tenement.tcp_response_broadcast.dirty";
    
    public static enum PROTOCOL_CONTEN_KEY
    {
        head, 
        body
    }

    public static enum REQUEST_TERMINAL_HEAD
    {
        appid,
        sessionid, 
        version, 
        os,
        type,
        seq,
        operation,
        msg,
        appver,
        uuid
    }
    
    public static enum REQUEST_TERMINAL_BODY
    {
        ticket,
        data
    }
    
    public static enum RESPONSE_TERMINAL_HEAD
    {
        appid,
        type, 
        seq,
        code,
        msg
    }
    
    public static enum RESPONSE_TERMINAL_BODY
    {
        data
    }
    
    public static interface OperationCode
    {
        public static final String CODE_0x00HB = "0x00HB";
    }
    
    public static interface ResponseCode
    {
        public static final String CODE_0x00D0 = "0x00D0";
        public static final String CODE_0x00D1 = "0x00D1";
        public static final String CODE_0x00AO = "0x00AO";
    }
}
