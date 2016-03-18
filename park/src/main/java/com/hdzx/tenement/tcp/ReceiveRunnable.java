package com.hdzx.tenement.tcp;

import com.hdzx.tenement.common.HandleObject;
import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.protocol.TcpReceiveTamplate;
import com.hdzx.tenement.tcp.protocol.TcpSendEntity;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

/**
 * 
 * @author Jesley
 *
 */
public class ReceiveRunnable implements Runnable
{
    public static final int RECEIVE_BUFFER_LEN = 1024 * 8;
    
    private boolean isFirst = true;
    
    private int currSearchIndex = 0;
    
    private ByteBuffer recByteBuffer = ByteBuffer.allocate(RECEIVE_BUFFER_LEN);
    
    private byte[] eofBytes = null;
    
    public ReceiveRunnable()
    {
        try
        {
            eofBytes = TcpContants.PACKAGE_EOF.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run()
    {
        SocketChannel channel = null;
        int index = -1;
        
        while (!Thread.currentThread().isInterrupted())
        {
            channel = SocketClient.getInstance().getChannel();
            if (channel != null && SocketClient.getInstance().isConnected())
            {
                try
                {
                    while ((index = channel.read(recByteBuffer)) != -1)
                    {
                        if (index > 0)
                        {
                            processData();
                        }
                    }
                    
                    if (index == -1)
                    {
                        SocketClient.getInstance().setConnected(false);
                    }
                }
                catch (SocketException e)
                {
                    System.out.println("read, SocketException");
                    SocketClient.getInstance().setConnected(false);
                    e.printStackTrace();
                }
                catch (ClosedChannelException e)
                {
                    e.printStackTrace();
                    break;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("ReceiveRunnable:close");
                SocketClient.getInstance().close();
                if (isFirst)
                {
                    isFirst = false;
                    SocketClient.getInstance().connect();
                }
                else
                {
                    try
                    {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                        break;
                    }
                    SocketClient.getInstance().connect();
                }
            }
        }
    
    }
    
    private void processData()
    {
        if (recByteBuffer.hasArray())
        {
            byte[] data = recByteBuffer.array();
            int position = recByteBuffer.position();
            HandleObject handleObject = findEOF(data, currSearchIndex, position);
            int findIndex = ((Integer) handleObject.getObject()).intValue();
            int begin = 0;
            String text = null;
            TcpReceiveTamplate tamplate = null;
            TcpSendEntity entity = null;
            
            try
            {
                while(handleObject.isSucess())
                {
                    if (begin < findIndex)
                    {
                        if (UserSession.getInstance().isValidAesKey())
                        {
                            text = new String(data, begin, (findIndex - begin), "UTF-8");
                            System.out.println("http:receive msg=" + text);
                            
                            try
                            {
                                tamplate = new TcpReceiveTamplate();
                                tamplate.parseContent4Json(text);
                                if (tamplate.getErrorMsg() == null)
                                {
                                    if (tamplate.isAnsewer())
                                    {
                                        entity = SocketClient.getInstance().removeEntityFromWaitAnswer(tamplate.getSeq());
                                        SocketClient.getInstance().sendRunMessageOnMain(entity, tamplate);
                                    }
                                    else
                                    {
                                        SocketClient.getInstance().sendBroadcastMessage(tamplate);
                                    }
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    
                    currSearchIndex = findIndex + eofBytes.length;
                    begin = currSearchIndex;
                    handleObject = findEOF(data, currSearchIndex, position);
                    findIndex = ((Integer) handleObject.getObject()).intValue();
                }
                
                currSearchIndex = findIndex - eofBytes.length;
                if (currSearchIndex < 0)
                {
                    currSearchIndex = 0;
                }
                
                if (currSearchIndex > 0)
                {
                    cleanAndCopyValidData(data, currSearchIndex, position);
                }
            }
            catch (UnsupportedEncodingException e)
            {
                clearData();
                e.printStackTrace();
            }
        }
    }
    
    private void clearData()
    {
        recByteBuffer.clear();
        currSearchIndex = 0;
    }
    
    private void cleanAndCopyValidData(byte[] data, int currIndex, int position)
    {
        clearData();
        int eofLength = eofBytes.length;
        int index = currIndex + eofLength;
        if (data != null && index < position)
        {
            recByteBuffer.put(data, index, position - index);
        }

    }
    
    private HandleObject findEOF(byte[] orgBytes, int startIndex, int endIndex)
    {
        HandleObject handleObject = new HandleObject();
        int i = 0;
        boolean isFound = false;
        int eofLength = eofBytes.length;
        
        while ((startIndex + eofLength - 1) < endIndex)
        {
            i = 0;
            while (i < eofLength)
            {
                isFound = true;
                if (eofBytes[i] != orgBytes[startIndex + i])
                {
                    isFound = false;
                    break;
                }
                
                i++;
            }
            
            if (isFound)
            {
                break;
            }
            
            startIndex++;
        }
        
        handleObject.setSucess(isFound == true);
        handleObject.setObject(Integer.valueOf(startIndex));
        
        return handleObject;
    }

}
