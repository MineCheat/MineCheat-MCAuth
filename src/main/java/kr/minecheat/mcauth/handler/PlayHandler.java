package kr.minecheat.mcauth.handler;

import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.packets.PacketHeader;
import kr.minecheat.mcauth.packets.PacketPlay00KeepAlive;

import java.util.TimerTask;

public class PlayHandler extends PacketHandler {
    public PlayHandler(MinecraftPacketHandler nettyHandler) {
        super(nettyHandler);
    }

    long willSendKeepAlive;
    long willReceiveKeepAliveBefore;
    boolean recievedKeepAlive = false;

    @Override
    public void handlePacket(PacketHeader packetHeader) throws Exception {

    }

    public void initiate() {
        willSendKeepAlive = System.currentTimeMillis() + 15;
        willReceiveKeepAliveBefore = System.currentTimeMillis() + 30;
        Server.getKeepAlivetimer().schedule(new KeepAliveTask(), 1000, 1000);
    }

    public class KeepAliveTask extends TimerTask {

        @Override
        public void run() {
            try {
                if (!recievedKeepAlive && willReceiveKeepAliveBefore < System.currentTimeMillis()) {
                    nettyHandler.disconnect("disconnect.timeout");
                    return;
                }
                if (willSendKeepAlive < System.currentTimeMillis()) {
                    sendPacket(new PacketPlay00KeepAlive(Server.getRandom().nextInt()));
                    willSendKeepAlive = System.currentTimeMillis() + 15;
                    willReceiveKeepAliveBefore = System.currentTimeMillis() + 30;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
