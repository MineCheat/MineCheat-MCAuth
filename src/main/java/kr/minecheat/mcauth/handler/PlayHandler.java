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

    private long willSendKeepAlive;
    private long willReceiveKeepAliveBefore;
    private boolean recievedKeepAlive = false;
    private PacketPlay00KeepAlive lastKeepAlive;

    @Override
    public void handlePacket(PacketHeader packetHeader) throws Exception {
        if (packetHeader.getData() instanceof PacketPlay00KeepAlive) {
            if (lastKeepAlive != null && lastKeepAlive.getRandomId() == ((PacketPlay00KeepAlive) packetHeader.getData()).getRandomId()) {
                recievedKeepAlive = true;
            } else if (lastKeepAlive == null) {
                recievedKeepAlive = true;
            }
        }
    }

    public void initiate() {
        willSendKeepAlive = System.currentTimeMillis() + 15000;
        willReceiveKeepAliveBefore = System.currentTimeMillis() + 30000;
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
                    sendPacket(lastKeepAlive = new PacketPlay00KeepAlive(Server.getRandom().nextInt()));
                    willSendKeepAlive = System.currentTimeMillis() + 15000;
                    willReceiveKeepAliveBefore = System.currentTimeMillis() + 30000;
                    recievedKeepAlive = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
