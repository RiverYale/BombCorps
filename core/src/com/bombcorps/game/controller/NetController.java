package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class NetController {
    private ArrayList<Room> roomList;
    private ArrayList<Player> playerList;
    private ArrayList<Ai> aiList;
    private WorldController world;
    private boolean bool_stop;
    private DirctGame game;

    //网络行为协议
    private static final int REFRESH_ROOM = 9;      //刷新房间
    private static final int RE_REFRESH_ROOM = 10;  //回复刷新房间
    private static final int ENTER_ROOM = 11;       //加入房间
    private static final int QUIT_ROOM = 12;        //退出房间
    private static final int CHOOSE_HERO = 13;      //选择英雄
    private static final int CHOOSE_MAP = 14;       //选择地图
    private static final int CHOOSE_TEAM = 15;      //选择队伍
    private static final int ADD_AI = 16;           //添加AI
    private static final int PREPARE = 17;          //准备
    private static final int START = 18;            //开始
    private static final int ROUND_START = 19;      //回合开始
    private static final int ROUND_OVER = 21;       //回合结束
    private static final int SET_BONUS = 22;        //产生宝箱
    private static final int OPERATIONS = 23;       //英雄操作
    private static final int QUIT_GAME = 24;        //退出游戏

    private static final int PORT_SEND = 65520;     //发送端口
    private static final int PORT_RECEIVE = 65530;  //接收端口

    public NetController() {
        roomList = new ArrayList<Room>();
        playerList = new ArrayList<Player>();
        aiList = new ArrayList<Ai>();
    }

    public void bindWorldController(WorldController world) {
        this.world = world;
    }

    public void bindGame(DirctGame game) {
        this.game = game;
    }

    public void init() {
        roomList.clear();
        playerList.clear();
        aiList.clear();
    }

    // 得到广播ip, 192.168.0.255之类的格式
    public static String getBroadCastIP() {
        String ip = getLocalHostIp().substring(0, getLocalHostIp().lastIndexOf(".") + 1) + "255";
        return ip;
    }

    // 获取本机IP
    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每个网络接口绑定的全部ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每个接口绑定的全部ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && (ip instanceof Inet4Address)) {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            System.out.print("获取本地IP失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public void sendCMD(Message msg) {
        (new UdpSend(msg)).start();
    }

    //发送消息
    class UdpSend extends Thread {
        Message msg;
        UdpSend(Message msg) {
            this.msg = msg;
        }
        public void run() {
            try {
                byte[] data = toByteArray(msg);
                DatagramSocket ds = new DatagramSocket(PORT_SEND);
                DatagramPacket packet = new DatagramPacket(data, data.length, msg.getToIp(), PORT_RECEIVE);
                packet.setData(data);
                ds.send(packet);
                ds.close();
            } catch(Exception e) {}
        }
    }

    // 接收消息
    public void openReceiveMsgThread() {
        bool_stop = false;
        (new UdpReceive()).start();
    }

    // 停止接收消息
    public void stopReceiveMsgThread() {
        bool_stop = true;
    }

    public class UdpReceive extends Thread {
        Message msg;
        public void run() {
            // 消息循环
            while (!bool_stop) {
                try {
                    DatagramSocket ds = new DatagramSocket(PORT_RECEIVE);
                    byte[] data = new byte[1024 * 32];
                    DatagramPacket dp = new DatagramPacket(data, data.length);
                    ds.receive(dp);
                    if(InetAddress.getLocalHost().getHostAddress().equals(dp.getAddress().getHostAddress())){
                        continue;
                    }
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

    public void parse(Message msg) {
        Gdx.app.log("zc", ""+ msg.getMsg());
        Message m;
        switch(msg.getMsg()){
            case REFRESH_ROOM:
                if(game.hasRoom()){
                    m = new Message(RE_REFRESH_ROOM);
                    m.setToIp(msg.getFromIp());
                    m.setFromIp(getLocalHostIp());
                    m.setRoom(game.getRoom());
                    sendCMD(m);
                }
                break;
            case RE_REFRESH_ROOM:
                roomList.add(msg.getRoom());
                break;
            case ENTER_ROOM:
                if(game.inRoom()){
                    if(game.hasRoom()){
                        if(!game.getRoom().isFull()){
                            m = new Message(ENTER_ROOM);
                            msg.setFromIp(getLocalHostIp());
                            for(Player p : game.getRoom().getPlayers()){
                                msg.setToIp(p.getIp());
                                sendCMD(m);
                            }
                        }
                    }else{
                        game.getRoom().addPlayer(msg.getPlayer());
                    }
                }
                break;
            case QUIT_ROOM:
                game.getRoom().removePlayer(msg.getPlayer());
                break;
            case CHOOSE_HERO:
                game.getRoom().updatePlayer(msg.getPlayer());
                break;
            case CHOOSE_MAP:
                game.getRoom().updateMap(msg.getMap());
                break;
            case CHOOSE_TEAM:
                game.getRoom().updatePlayer(msg.getPlayer());
                break;
            case ADD_AI:
                game.getRoom().addAi();
                break;
            case PREPARE:
                game.getRoom().updatePlayer(msg.getPlayer());
                break;
            case START:
                game.startGame();
                break;
            case ROUND_START:
                world.startNextRound();
                break;
            case ROUND_OVER:
                m = new Message(ROUND_START);
                m.setFromIp(getLocalHostIp());
                for(Player p : world.getPlayers()){
                    m.setToIp(p.getIp());
                    sendCMD(m);
                }
                world.startNextRound();
                break;
            case SET_BONUS:
                world.setBonus(msg.getBonus());
                break;
            case OPERATIONS:
                world.playerOperate(msg);
                break;
            case QUIT_GAME:
                if(world.getWorldIp().equals(msg.getFromIp())){
                    world.errorStop();
                }else{
                    world.playerQuit(msg);
                }
                break;
        }
    }
}
