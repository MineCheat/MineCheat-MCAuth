package kr.minecheat.mcauth.handler;

import kr.minecheat.mcauth.mcdata.Chat;
import kr.minecheat.mcauth.mcdata.ChatColor;
import kr.minecheat.mcauth.mcdata.ServerListPingResponse;
import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.packets.*;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerPingHandler extends PacketHandler {

    private PacketStatus00ServerListPing clientData;

    public ServerPingHandler(MinecraftPacketHandler nettyHandler) {
        super(nettyHandler);
    }

    @Override
    public void handlePacket(PacketHeader packetHeader) throws Exception {
        PacketData packetData = packetHeader.getData();


        if (packetData instanceof PacketStatus00ServerListPing) {
            if (!((PacketStatus00ServerListPing) packetData).isRequestPacket()) {
                clientData = (PacketStatus00ServerListPing) packetData;
            } else {
                if (clientData != null) {
                    PacketStatus00ServerListPingResponse p0slpr = new PacketStatus00ServerListPingResponse();

                    ServerListPingResponse slpr = new ServerListPingResponse();
                    slpr.setVersion(new ServerListPingResponse.VersionData("MineCheat-MCAuth", 47));
                    slpr.setPlayers(new ServerListPingResponse.PlayerListData.Builder()
                                            .setMax(999)
                                            .setOnline(1)
                                            .build());
                    slpr.setFavicon("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAE0lEQVR42mNk+P//PwMaYKSBIACCTQ73jQGOiQAAAABJRU5ErkJggg==");

                    slpr.setDescription(new Chat.Builder().setText(" \n").setExtra(Arrays.asList(
                            new Chat.Builder().setText("           MineCheat").setColor(ChatColor.LIGHT_GREEN).build(),
                            new Chat.Builder().setText(" 마인크래프트 계정 ").setColor(ChatColor.LIGHT_CYAN).build(),
                            new Chat.Builder().setText("인증").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText(" 서버").setColor(ChatColor.LIGHT_YELLOW).build()
                    )).build());
                    slpr.setModinfo(new ServerListPingResponse.ModInfo(new ArrayList<>(), "VANILLA"));

                    p0slpr.setResponse(slpr);
                    sendPacket(p0slpr);
                }
            }
        } else if (packetData instanceof PacketStatus01JustAPing) {
            sendPacket(packetData);
        }
    }
}
