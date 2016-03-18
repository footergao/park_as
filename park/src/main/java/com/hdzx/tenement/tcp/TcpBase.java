package com.hdzx.tenement.tcp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by anchendong on 15/6/30.
 */
public class TcpBase {
    //消息发送队列
    public static Queue<TcpEntity> msgSetQueue=new ConcurrentLinkedQueue<TcpEntity>();
}
