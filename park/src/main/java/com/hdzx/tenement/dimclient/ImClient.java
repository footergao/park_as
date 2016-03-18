package com.hdzx.tenement.dimclient;

import android.util.Log;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by anchendong on 15/6/16.
 */
public class ImClient {

    //服务名
    final private static String SERVICENAME = "dev-vm-1";
    //服务IP
    final private static String SERVICEIP = "192.168.1.243";
    //服务端口
    final private static int SERVICEPORT = 5222;
    //xmpp连接
    private static AbstractXMPPConnection connection = null;

    public static AbstractXMPPConnection getConnection(){
        return connection;
    }

    /**
     * 初始化IM，创建连接对象
     */
    public static void initIm() {
        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                //.setUsernameAndPassword(userName, passWord)
                //.setDebuggerEnabled(true)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .setServiceName(SERVICENAME)
                .setHost(SERVICEIP)
                .setPort(SERVICEPORT)
                .setSendPresence(true)//离线模式
                .build();

        connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param passWord
     */
    public static void loginIm(String userName, String passWord) {
        try {
            connection.login(userName, passWord);

            if (connection!=null){
                Roster roster = Roster.getInstanceFor(connection);
                Log.i("smack-entry","===="+roster.getGroupCount());

                Collection<RosterEntry> entries = roster.getEntries();
                for (RosterEntry entry : entries) {
                    Log.i("smack-entry",entry.toString());
                    System.out.println(entry);
                }
            }

        } catch (SmackException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个用户会话对象
     *
     * @param username
     * @return
     */
    public static Chat creatImChat(String username) {
        return ChatManager.getInstanceFor(connection).createChat(username + "@" + SERVICENAME);
    }

    /**
     * 对指定用户发送字符串信息
     *
     * @param chat
     * @param msg
     */
    public static void sendImMessageString(Chat chat, String msg) {
        try {
            chat.sendMessage(msg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
    }


}
