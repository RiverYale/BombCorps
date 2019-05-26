package com.bombcorps.game.controller;

import com.bombcorps.game.model.Ai;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class NetController {
    private ArrayList<Room> roomList;
    private ArrayList<Player> playerList;
    private ArrayList<Ai> aiList;
    private Player me;
    private boolean isStop;

    //网络行为协议
    private static final int REFRESH_ROOM = 10;   //刷新房间
    private static final int ENTER_ROOM = 11;     //加入房间
    private static final int QUIT_ROOM = 12;      //退出房间
    private static final int CHOOSE_HERO = 13;    //选择英雄
    private static final int CHOOSE_MAP = 14;     //选择地图
    private static final int CHOOSE_TEAM = 15;    //选择队伍
    private static final int ADD_AI = 16;         //添加AI
    private static final int PREPARE = 17;        //准备
    private static final int START = 18;          //开始
    private static final int ROUND_START = 19;    //回合开始
    private static final int ROUND_OVER = 21;     //回合结束
    private static final int SET_BONUS = 22;      //产生宝箱
    private static final int OPERATIONS = 23;     //英雄操作
    private static final int QUIT_GAME = 24;      //退出游戏

    public static final int PORT_SEND = 2430;     //发送端口
    public static final int PORT_RECEIVE = 2420;  //接收端口

    public NetController(Player me) {
        roomList = new ArrayList<Room>();
        playerList = new ArrayList<Player>();
        aiList = new ArrayList<Ai>();
        this.me = me;
    }

    public void init() {
        roomList.clear();
        playerList.clear();
        aiList.clear();
    }

    public void sendCMD(Message msg) {
        (new UdpSend(msg)).start();
    }

    //发送消息
    class UdpSend extends Thread {
        Message msg = null;

        UdpSend(Message msg) {
            this.msg = msg;
        }

        public void run() {
            try {
                byte[] data = toByteArray(msg);
                DatagramSocket ds = new DatagramSocket(PORT_SEND);
                DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(""), PORT_SEND);
                packet.setData(data);
                ds.send(packet);
                ds.close();
            } catch (Exception e) {}
        }
    }

    // 接收消息
    public void openReceiveMsgThread() {
        isStop = false;
        (new UdpReceive()).start();
    }

    // 停止接收消息
    public void stopReceiveMsgThread() {
        isStop = true;
    }

    public class UdpReceive extends Thread {
        Message msg = null;

        public void run() {
            // 消息循环
            while (!isStop) {
                try {
                    DatagramSocket ds = new DatagramSocket(PORT_RECEIVE);
                    byte[] data = new byte[1024 * 32];
                    DatagramPacket dp = new DatagramPacket(data, data.length);
                    dp.setData(data);
                    ds.receive(dp);
                    byte[] data2 = new byte[dp.getLength()];

                    // 得到接收的数据
                    System.arraycopy(data, 0, data2, 0, data2.length);
                    Message msg = (Message) toObject(data2);
                    ds.close();

                    // 解析消息
                    parse(msg);
                } catch (Exception e) {}
            }

        }
    }

    public void parse(Message msg) {

    }

    // 对象封装成消息
    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    // 消息解析成对象
    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}
