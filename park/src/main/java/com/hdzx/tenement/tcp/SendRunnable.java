package com.hdzx.tenement.tcp;

import com.hdzx.tenement.common.UserSession;
import com.hdzx.tenement.tcp.protocol.TcpSendEntity;
import com.hdzx.tenement.tcp.protocol.TcpSendTemplate;

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
public class SendRunnable implements Runnable
{
    private TcpSendEntity entity = null;

    @Override
    public void run()
    {
        SocketChannel channel = null;
        TcpSendTemplate sendTemplate = null;
        String sendText = null;
        int retry = 0;
        
        while (true && !Thread.currentThread().isInterrupted())
        {
            if (UserSession.getInstance().isValidAesKey())
            {
                channel = SocketClient.getInstance().getChannel();
                if (channel != null && SocketClient.getInstance().isConnected())
                {
                    if (entity == null)
                    {
                        entity = SocketClient.getInstance().pollSendTcpEntity();
                    }
                    else
                    {
                        retry = entity.countRetry();
                        if (retry < 0)
                        {
                            entity = SocketClient.getInstance().pollSendTcpEntity();
                        }
                    }
                    
                    if (entity != null)
                    {
                        try
                        {
                            sendTemplate = entity.getSendTemplate();
                            sendText = sendTemplate.getContent() + TcpContants.PACKAGE_EOF;
                            System.out.println("tcp:send,msg=" + sendText);
                            ByteBuffer byteBuffer = ByteBuffer.wrap(sendText.getBytes(entity.getEncoding()));
                            channel.write(byteBuffer);
                            if (sendTemplate.isServerCallback() && entity.getTcpCallback() != null)
                            {
                                SocketClient.getInstance().putWaitAnswer(entity);
                            }
                            entity = null;
                        }
                        catch (SocketException e)
                        {
                            SocketClient.getInstance().setConnected(false);
                            e.printStackTrace();
                        }
                        catch (ClosedChannelException e)
                        {
                            e.printStackTrace();
                            break;
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            entity = null;
                            e.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    //connect();
                }
            }
            
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
