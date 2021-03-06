package com.bombcorps.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.bombcorps.game.model.Bonus;
import com.bombcorps.game.model.Message;
import com.bombcorps.game.model.Player;
import com.bombcorps.game.model.Room;
import com.bombcorps.game.view.DirectedGame;
import com.bombcorps.game.view.RoomScreen;

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
import java.util.ArrayList;
import java.util.Enumeration;

public class NetController {
    private ArrayList<Room> roomList;
    private WorldController world;
    private boolean bool_stop;
    private DirectedGame game;
    private static String localHost = getLocalHost();

    //网络行为协议
    private static final int REFRESH_ROOM = 9;      //刷新房间
    private static final int RE_REFRESH_ROOM = 10;  //回复刷新房间
    private static final int ENTER_ROOM = 11;       //加入房间
    private static final int QUIT_ROOM = 12;        //退出房间
    private static final int UPDATE_PLAYER = 13;    //退出房间
    private static final int CHOOSE_MAP = 14;       //选择地图
    private static final int ADD_AI = 16;           //添加AI
    private static final int START = 18;            //开始
    private static final int ROUND_START = 19;      //回合开始
    private static final int ROUND_OVER = 21;       //回合结束
    private static final int OPERATIONS = 23;       //英雄操作
    private static final int QUIT_GAME = 24;        //退出游戏

    private static final int PORT_SEND = 65520;     //发送端口
    private static final int PORT_RECEIVE = 65530;  //接收端口

    public NetController() {
        roomList = new ArrayList<Room>();
    }

    public void bindWorldController(WorldController world) {
        this.world = world;
    }

    public void bindGame(DirectedGame game) {
        this.game = game;
    }

    // 得到广播ip, 192.168.0.255之类的格式
    private static String getBroadCastIP() {
        String ip = getLocalHostIp().substring(0, getLocalHostIp().lastIndexOf(".") + 1) + "255";
        return ip;
    }

    public static String getLocalHostIp() {
        return localHost;
    }

    // 获取本机IP
    private static String getLocalHost() {
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
        } catch (Exception e) {
            System.out.print("获取本地IP失败");
            e.printStackTrace();
        }
        return ipaddress;
    }

    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    private void sendCMD(Message msg) {
        Gdx.app.log(getLocalHostIp()+" to", msg.getToIp());
        (new UdpSend(msg)).start();
    }

    //发送消息
    public class UdpSend extends Thread {
        Message msg;
        UdpSend(Message msg) {
            this.msg = msg;
        }
        public void run() {
            try {
                byte[] data = toByteArray(msg);
                DatagramSocket ds = new DatagramSocket(PORT_SEND);
                DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(msg.getToIp()), PORT_RECEIVE);
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
            try {
                DatagramSocket ds = new DatagramSocket(PORT_RECEIVE);
                ds.setReceiveBufferSize(1024 * 128);
                byte[] data = new byte[1024 * 128];
                DatagramPacket dp = new DatagramPacket(data, data.length);
                ds.receive(dp);
                ds.close();
                if(bool_stop){
                    return;
                }
                openReceiveMsgThread();
                byte[] data2 = new byte[dp.getLength()];
                // 得到接收的数据
                System.arraycopy(data, 0, data2, 0, data2.length);
                Message msg = (Message) toObject(data2);
                ds.close();
                //若是自己则不解析
                if(getLocalHostIp().equals(dp.getAddress().getHostAddress())){
                    return;
                }
                // 解析消息
                parse(msg);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }

    // 对象封装成消息
    private byte[] toByteArray(Object obj) {
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
    private Object toObject(byte[] bytes) {
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

    private void parse(Message msg) {
        Message m;
        switch(msg.getMsg()){
            case REFRESH_ROOM:
                if(game.hasRoom()){
                    m = new Message(RE_REFRESH_ROOM);
                    m.setToIp(msg.getFromIp());
                    m.setRoom(game.getRoom());
                    sendCMD(m);
                }
                break;
            case RE_REFRESH_ROOM:
                roomList.add(msg.getRoom());
                game.updateLobbyScreen();
                break;
            case ENTER_ROOM:
                if(game.inRoom()){
                    if(game.hasRoom()){
                        if(!game.getRoom().isFull()){
                            broadcastInRoom(msg);
                            game.getRoom().addPlayer(msg.getTargetPlayer());
                        }
                    }else{
                        game.getRoom().addPlayer(msg.getTargetPlayer());
                    }
                }
                game.updateRoomScreen();
                break;
            case QUIT_ROOM:
                if(game.getRoom().getOwnerIp().equals(msg.getFromIp())){
                    game.errorQuit();
                }else{
                    game.getRoom().removePlayer(msg.getTargetPlayer());
                }
                game.updateRoomScreen();
                break;
            case UPDATE_PLAYER:
                game.getRoom().updatePlayer(msg.getTargetPlayer());
                game.updateRoomScreen();
                break;
            case CHOOSE_MAP:
                game.getRoom().setMapName(msg.getMap());
                game.updateRoomScreen();
                break;
            case ADD_AI:
                game.getRoom().addAi();
                game.updateRoomScreen();
                break;
            case START:
                RoomScreen.b_loadGame = true;
                break;
            case ROUND_START:
                world.startNextRound(msg.getBonus());
                break;
            case ROUND_OVER:
                m = new Message(ROUND_START);
                boolean hasBonus = MathUtils.random(9) > 6; // 30%的几率
                Bonus b = null;
                if(hasBonus){
                    b = world.spawnBonus();
                }
                m.setBonus(b);
                broadcastInRoom(m);
                world.startNextRound(b);
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

    public void refreshRoom() {
        roomList.clear();
        game.updateLobbyScreen();
        Message m = new Message(REFRESH_ROOM);
        m.setToIp(getBroadCastIP());
        sendCMD(m);
    }

    public void enterRoom(String roomIp, Player me) {
        Message m = new Message(ENTER_ROOM);
        m.setToIp(roomIp);
        m.setTargetPlayer(me);
        sendCMD(m);
    }

    private void broadcastInRoom(Message m) {
        for (Player p : game.getRoom().getPlayerManager().getAllPlayerList()) {
            if(p.getIp().equals(getLocalHostIp())){
                continue;
            }
            m.setToIp(p.getIp());
            sendCMD(m);
        }
    }

    public void quitRoom(Player me) {
        Message m = new Message(QUIT_ROOM);
        m.setTargetPlayer(me);
        broadcastInRoom(m);
    }

    public void updatePlayer(Player me) {
        Message m = new Message(UPDATE_PLAYER);
        m.setTargetPlayer(me);
        broadcastInRoom(m);
    }

    public void chooseMap(String mapName) {
        Message m = new Message(CHOOSE_MAP);
        m.setMap(mapName);
        broadcastInRoom(m);
    }

    public void startGame() {
        Message m = new Message(START);
        broadcastInRoom(m);
    }

    public void roundStart(Bonus b) {
        Message m = new Message(ROUND_START);
        m.setBonus(b);
        broadcastInRoom(m);
    }

    public void roundOver() {
        Message m = new Message(ROUND_OVER);
        m.setToIp(game.getRoom().getOwnerIp());
        sendCMD(m);
    }

    public void operate(int op,  float targetX, float tapX, float tapY, boolean doubleHit){
        Message m = new Message(OPERATIONS);
        m.setOp(op, targetX, tapX, tapY, doubleHit);
        broadcastInRoom(m);
    }

    public void quitGame(Player me) {
        Message m = new Message(QUIT_GAME);
        m.setTargetPlayer(me);
        broadcastInRoom(m);
    }
}
