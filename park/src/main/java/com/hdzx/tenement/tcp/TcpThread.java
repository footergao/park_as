package com.hdzx.tenement.tcp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.hdzx.tenement.utils.Contants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by anchendong on 15/6/30.
 */
public class TcpThread implements Runnable {

    //socket对象
    private Socket socket;
    //socket 输出流
    OutputStream output;
    //socket 输入流
    InputStream input;

    Context context;

    String TAG = this.getClass().getName();


    /**
     * TCP线程构造函数  TCP线程启动一定是在APP主界面开始运行时启动
     *
     * @param context app主界面context
     */
    public TcpThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress(Contants.tcpIP,Contants.tcpPort), 3000);
            output = socket.getOutputStream();
            input = socket.getInputStream();
        } catch (IOException e) {
            Log.v(TAG, "TCP-socket首次连接失败,连接关闭,开始重连!");
        }
        while (true) {
            //判断socket是否断开
            if (socket.isConnected()){
                if (!socket.isClosed()) {
                    //查询消息队列中是否存在需要发送的同步消息
                    TcpEntity tcpEntity = TcpBase.msgSetQueue.poll();
                    if (tcpEntity != null) {
                        try {
                            output.write(tcpEntity.getSendMsg());
                            output.flush();
                            int r = -1;
                            byte[] by = new byte[1024];
                            while ((r = input.read(by)) != -1) {
                                tcpEntity.getTcpCallbackInterface().tcpCallback(by);
                                break;
                            }
                            if (r==-1){
                                output.close();
                                input.close();
                                socket.close();
                            }else{
                                continue;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    int listenr = -1;
                    byte[] listenByte = new byte[1024];
                    try {
                        while ((listenr = input.read(listenByte)) != -1) {
                            //发送广播
                            Intent intent = new Intent();  //Itent就是我们要发送的内容
                            intent.putExtra("recmsg", listenByte);
                            intent.setAction("listentcp");   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                            context.sendBroadcast(intent);
                        }
                        //不阻塞且listenr=-1时，表示服务主动断开连接
                        output.close();
                        input.close();
                        socket.close();
                    } catch (IOException e) {
                        Log.v(TAG, "TCP-socket未监听到服务器推送信息");
                    }
                } else {
                    //连接成功过，但socket断开
                    reconnect();
                }
            }else{
                //没有连接成功过
                reconnect();
            }

        }
    }

    /**
     * 服务重连
     */
    private void reconnect() {
        try {
            Thread.sleep(2000);
            socket = new Socket();
            socket.setSoTimeout(2000);
            socket.connect(new InetSocketAddress(Contants.tcpIP, Contants.tcpPort), 3000);
            output = socket.getOutputStream();
            input = socket.getInputStream();
        } catch (IOException e) {
            Log.v(TAG, "TCP-socket重连失败");
        } catch (InterruptedException e) {
            Log.v(TAG, "TCP-socket等待重连异常");
        }
    }
}
