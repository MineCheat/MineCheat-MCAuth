package kr.minecheat.mcauth.handler;

import kr.minecheat.mcauth.Server;
import kr.minecheat.mcauth.database.IntegrationTokenData;
import kr.minecheat.mcauth.database.TokenDAO;
import kr.minecheat.mcauth.mcdata.*;
import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.packets.*;
import kr.minecheat.mcauth.utils.EncryptionUtils;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.TimerTask;

public class PlayHandler extends PacketHandler {
    public PlayHandler(MinecraftPacketHandler nettyHandler) {
        super(nettyHandler);
    }

    private long willSendKeepAlive;
    private long willReceiveKeepAliveBefore;
    private boolean recievedKeepAlive = false;
    private PacketPlay00KeepAlive lastKeepAlive;

    private enum IntegrationState {
        RETRIEVING_DATA, WAIT_FOR_APPROVAL, SETTING_DATA, ERROR
    }
    private IntegrationState current;

    private KeepAliveTask keepAliveTask;

    private IntegrationTokenData itd;

    private static final Chat welcome = new Chat.Builder().setText("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n").setExtra(Arrays.asList(
            new Chat.Builder().setText("■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
            new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
            new Chat.Builder().setText("인증서버에 오신 것을 ").setColor(ChatColor.LIGHT_WHITE).build(),
            new Chat.Builder().setText("환영합니다! ").setColor(ChatColor.LIGHT_YELLOW).build(),
            new Chat.Builder().setText("■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
            new Chat.Builder().setText("\n\n ▶ 데이터베이스에서 데이터를 로딩할 동안 잠시만 기다려주세요").setColor(ChatColor.LIGHT_GRAY).build()
    )).build();

    public void errorSave(Exception e) {
        e.printStackTrace();
        Chat c2 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                new Chat.Builder().setText("● 앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText("데이터를 저장하던중에 오류가 발생하여 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                new Chat.Builder().setText("\n ●").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText(" 재 접속하여 다시 인증을 진행해주세요!").setColor(ChatColor.LIGHT_GRAY).build(),
                new Chat.Builder().setText("\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText(" 오류 내역: ").setColor(ChatColor.LIGHT_GRAY).setExtra(Arrays.asList(
                        new Chat.Builder().setText(e.getClass().getName() + ": " + e.getMessage())
                                .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, new Chat.Builder().setText("복사하기").setColor(ChatColor.LIGHT_WHITE).build()))
                                .setClickEvent(new ChatClickEvent(ChatClickEvent.Action.REPLACE_CHATBOX,e.getClass().getName() + ": " + e.getMessage()))
                                .setColor(ChatColor.DARK_RED).build()
                )).build(),
                new Chat.Builder().setText("\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText(" 10").setColor(ChatColor.LIGHT_YELLOW).build(),
                new Chat.Builder().setText("초 뒤 킥됩니다").setColor(ChatColor.LIGHT_WHITE).build()
        )).build();
        try {
            sendPacket(new PacketPlayOut02Chat(c2, ChatPosition.SYSTEM_MESSAGE));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e2) {
            e.printStackTrace();
        }
        Chat c3 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                new Chat.Builder().setText("앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText("데이터를 저장하던중에 오류가 발생하여 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_RED).build(),
                new Chat.Builder().setText(" 재 접속하여 인증을 다시 진행해주세요!").setColor(ChatColor.LIGHT_GRAY).build()
        )).build();
        nettyHandler.disconnect(c3);
        return;
    }

    public void disapprove() {
        current = IntegrationState.SETTING_DATA;
        Chat c = new Chat.Builder().setText(" \n\n\n").setExtra(Arrays.asList(
                new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■\n\n").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                new Chat.Builder().setText("데이터베이스에 데이터 쓰는 중...\n").setColor(ChatColor.LIGHT_GRAY).build()
        )).build();
        try {
            sendPacket(new PacketPlayOut02Chat(c, ChatPosition.SYSTEM_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Server.getThreadPool().submit(() -> {
            try {
                TokenDAO.deleteToken(itd);
                Chat success = new Chat.Builder().setText("\n").setExtra(Arrays.asList(
                        new Chat.Builder().setText(" ▶ ").setColor(ChatColor.LIGHT_GRAY).build(),
                        new Chat.Builder().setText("정상적").setColor(ChatColor.LIGHT_GREEN).build(),
                        new Chat.Builder().setText("으로 ").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText("비허가").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 하였습니다\n\n").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                        new Chat.Builder().setText("10").setColor(ChatColor.LIGHT_YELLOW).build(),
                        new Chat.Builder().setText("초 뒤 킥됩니다\n\n").setColor(ChatColor.LIGHT_WHITE).build()
                )).build();
                sendPacket(new PacketPlayOut02Chat(success, ChatPosition.SYSTEM_MESSAGE));
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Chat c2 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                        new Chat.Builder().setText("■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                        new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                        new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                        new Chat.Builder().setText("■■■■■■■■■■■\n\n").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                        new Chat.Builder().setText("정상적").setColor(ChatColor.LIGHT_GREEN).build(),
                        new Chat.Builder().setText("으로 ").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText("비허가").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 하였습니다\n\n").setColor(ChatColor.LIGHT_WHITE).build()
                )).build();
                nettyHandler.disconnect(c2);
                return;
            } catch (Exception e) {
                errorSave(e);
                return;
            }

        });
    }

    public void approve() {
        current = IntegrationState.SETTING_DATA;
        Chat c = new Chat.Builder().setText(" \n\n\n").setExtra(Arrays.asList(
                new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■\n\n").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                new Chat.Builder().setText("데이터베이스에 데이터 쓰는 중...\n").setColor(ChatColor.LIGHT_GRAY).build()
        )).build();
        try {
            sendPacket(new PacketPlayOut02Chat(c, ChatPosition.SYSTEM_MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String tokenB = EncryptionUtils.generateRandomString(16);
        itd.setMinecraftUUID(nettyHandler.getUserData().getUid());
        itd.setTokenB(tokenB);

        try {
            TokenDAO.updateUUIDAndTokenBByTokenData(itd);

            Chat c2 = new Chat.Builder().setText(" \n").setExtra(Arrays.asList(
                    new Chat.Builder().setText(" ▶ ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("아래 ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText("URL").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText(" 로 접속하여, 인증을 마무리해주세요!\n\n").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("URL: ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("http://www.minecheat.kr:3000/mcAuth/tokenB?tokenB="+tokenB)
                            .setColor(ChatColor.LIGHT_YELLOW)
                            .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, new Chat.Builder().setText("클릭하여 인증 마무리하기").setColor(ChatColor.LIGHT_WHITE).build()))
                            .setClickEvent(new ChatClickEvent(ChatClickEvent.Action.OPEN_URL, "http://www.minecheat.kr:3000/mcAuth/tokenB?tokenB="+tokenB)).build(),
                    new Chat.Builder().setText("\n\n                                ").build(),
                    new Chat.Builder().setText("[  복사  ]").setColor(ChatColor.LIGHT_GREEN).setBold(true)
                            .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, new Chat.Builder().setText("클릭하여 URL 복사하기").setColor(ChatColor.LIGHT_WHITE).build()))
                            .setClickEvent(new ChatClickEvent(ChatClickEvent.Action.REPLACE_CHATBOX, "http://www.minecheat.kr:3000/mcAuth/tokenB?tokenB="+tokenB)).build(),
                    new Chat.Builder().setText("\n\n").build(),
                    new Chat.Builder().setText(" ●").setColor(ChatColor.LIGHT_RED).build(),
                    new Chat.Builder().setText(" 30").setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("초 뒤 킥됩니다").setColor(ChatColor.LIGHT_WHITE).build()
            )).build();

            try {
                sendPacket(new PacketPlayOut02Chat(c2, ChatPosition.SYSTEM_MESSAGE));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(30000);

            Chat c3 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                    new Chat.Builder().setText("■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                    new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("■■■■■■■■■■■\n\n").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                    new Chat.Builder().setText("정상적").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("으로 ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText("허가").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText(" 하였습니다\n\n").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText("인증 URL: ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("http://www.minecheat.kr:3000/mcAuth/tokenB?tokenB="+tokenB)
                            .setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_GREEN).build()
            )).build();
            nettyHandler.disconnect(c3);
            return;
        } catch (Exception e) {
            errorSave(e);
        }
    }

    @Override
    public void handlePacket(PacketHeader packetHeader) throws Exception {
        if (packetHeader.getData() instanceof PacketPlay00KeepAlive) {
            if (lastKeepAlive != null && lastKeepAlive.getRandomId() == ((PacketPlay00KeepAlive) packetHeader.getData()).getRandomId()) {
                recievedKeepAlive = true;
            } else if (lastKeepAlive == null) {
                recievedKeepAlive = true;
            }
        } else if (packetHeader.getData() instanceof PacketPlayIn01ChatMessage) {
            String chat = ((PacketPlayIn01ChatMessage) packetHeader.getData()).getChat();
            if (IntegrationState.WAIT_FOR_APPROVAL == current) {
                if ("/disapprove".equalsIgnoreCase(chat)) {
                    disapprove();
                } else if ("/approve".equalsIgnoreCase(chat)){
                    approve();
                }
            }
        }
    }

    public void initiate() throws Exception {
        willSendKeepAlive = System.currentTimeMillis() + 15000;
        willReceiveKeepAliveBefore = System.currentTimeMillis() + 30000;
        Server.getKeepAlivetimer().schedule(keepAliveTask = new KeepAliveTask(), 1000, 1000);

        sendPacket(new PacketPlayOut01JoinGame(16, GameMode.SURVIVAL, Dimension.OVERWORLD, Difficulty.PEACEFUL, 20, LevelType.FLAT, false));
        sendPacket(new PacketPlayOut3FPluginChannel("MC|Brand","MineCheat"));
        sendPacket(new PacketPlayOut41ServerDifficulty(Difficulty.PEACEFUL));
        sendPacket(new PacketPlayOut05SpawnPosition(new Location(8, 32, 8)));
        sendPacket(new PacketPlayOut39PlayerAbilities((byte) PlayerAbilities.INVULNERABLE, 0, 0.1f));
        sendPacket(new PacketPlayOut09HeldItemChange((byte) 0));
        PlayerListItem item = new PlayerListItem.Builder()
                                        .setAction(PlayerListItem.Action.ADD)
                                        .addActionValue(new PlayerListItem.ActionValue.AddPlayer(nettyHandler.getUserData().getUid(), nettyHandler.getUserData().getUsername(), nettyHandler.getUserData().getProperties(), GameMode.SURVIVAL, 10, null)).build();
        sendPacket(new PacketPlayOut38PlayerListItem(item));
        sendPacket(new PacketPlayOut38PlayerListItem(item));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut03TimeUpdate(6000,6000));
        sendPacket(new PacketPlayOut26MapChunkBulkButDeveloperIsLazyAndRefusingToImplement());
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut08PlayerPositionAndLook(8, 32, 8, 0, 90, (byte) 0));
        sendPacket(new PacketPlayOut02Chat(welcome, ChatPosition.SYSTEM_MESSAGE));

        current = IntegrationState.RETRIEVING_DATA;
        Server.getThreadPool().submit(() -> {
            String url = nettyHandler.getUserData().getServerURL();
            int spaceIndex = url.indexOf(0);
            if (spaceIndex != -1) url = url.substring(0, spaceIndex);
            int dotIndex = url.indexOf(".auth.minecheat.kr");

            if (dotIndex == -1 || !url.substring(dotIndex, url.length()).equalsIgnoreCase(".auth.minecheat.kr")) {
                this.current = IntegrationState.ERROR;
                Chat c = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                        new Chat.Builder().setText("앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText("유효하지 않은 주소로 접속하셔서 진행 할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText("\n공식").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                        new Chat.Builder().setText("사이트를 통해 접속해주세요").setColor(ChatColor.LIGHT_WHITE).build()
                )).build();
                nettyHandler.disconnect(c);
                return;
            }

            String tokenA = url.substring(0, dotIndex);
            try {
                itd = TokenDAO.getTokenDataByTokenA(tokenA);
                if (itd == null || itd.getTokenB() != null) {
                    this.current = IntegrationState.ERROR;
                    Chat c = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                            new Chat.Builder().setText("앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText("유효하지 않은 주소로 접속하셔서 진행 할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                            new Chat.Builder().setText("\n공식").setColor(ChatColor.LIGHT_WHITE).build(),
                            new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                            new Chat.Builder().setText("사이트를 통해 접속해주세요").setColor(ChatColor.LIGHT_WHITE).build()
                    )).build();
                    nettyHandler.disconnect(c);
                    return;
                }

                if (itd.getExpiry_date().getTime() <= new Date().getTime()) {
                    this.current = IntegrationState.ERROR;
                    Chat c = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                            new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                            new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                            new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                            new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                            new Chat.Builder().setText("\n\n\n ● 앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText("인증 토큰이 만료되어 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                            new Chat.Builder().setText("\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                            new Chat.Builder().setText("웹사이트에서 인증을 다시 시도해주세요").setColor(ChatColor.LIGHT_WHITE).build(),
                            new Chat.Builder().setText("\n\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText(" 10").setColor(ChatColor.LIGHT_YELLOW).build(),
                            new Chat.Builder().setText("초 뒤 킥됩니다").setColor(ChatColor.LIGHT_WHITE).build()
                    )).build();
                    try { sendPacket(new PacketPlayOut02Chat(c, ChatPosition.SYSTEM_MESSAGE)); } catch (Exception ex) {}
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Chat c2 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                            new Chat.Builder().setText("앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText("인증 토큰이 만료되어 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                            new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_RED).build(),
                            new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                            new Chat.Builder().setText("웹사이트에서 인증을 다시 시도해주세요").setColor(ChatColor.LIGHT_WHITE).build()
                    )).build();
                    nettyHandler.disconnect(c2);
                    return;
                }
            } catch (Exception e) {
                this.current = IntegrationState.ERROR;
                        e.printStackTrace();
                Chat c = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                        new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                        new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                        new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                        new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                        new Chat.Builder().setText("\n\n\n ● 앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText("데이터를 로딩하던중에 오류가 발생하여 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText("\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 재 접속시 오류가 해결될수도 있어요!").setColor(ChatColor.LIGHT_GRAY).build(),
                        new Chat.Builder().setText("\n\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 오류 내역: ").setColor(ChatColor.LIGHT_GRAY).setExtra(Arrays.asList(
                                new Chat.Builder().setText(e.getClass().getName() + ": " + e.getMessage())
                                        .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT, new Chat.Builder().setText("복사하기").setColor(ChatColor.LIGHT_WHITE).build()))
                                        .setClickEvent(new ChatClickEvent(ChatClickEvent.Action.REPLACE_CHATBOX,e.getClass().getName() + ": " + e.getMessage()))
                                        .setColor(ChatColor.DARK_RED).build()
                        )).build(),
                        new Chat.Builder().setText("\n\n\n ●").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 10").setColor(ChatColor.LIGHT_YELLOW).build(),
                        new Chat.Builder().setText("초 뒤 킥됩니다").setColor(ChatColor.LIGHT_WHITE).build()
                )).build();
                try { sendPacket(new PacketPlayOut02Chat(c, ChatPosition.SYSTEM_MESSAGE)); } catch (Exception ex) {}
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                Chat c2 = new Chat.Builder().setText(" ").setExtra(Arrays.asList(
                        new Chat.Builder().setText("앗 이런, ").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText("데이터를 로딩하던중에 오류가 발생하여 인증을 진행할 수 없어요! ").setColor(ChatColor.LIGHT_WHITE).build(),
                        new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_RED).build(),
                        new Chat.Builder().setText(" 재 접속시 오류가 해결될수도 있어요!").setColor(ChatColor.LIGHT_GRAY).build()
                )).build();
                nettyHandler.disconnect(c2);
                return;
            }

            Chat confirm = new Chat.Builder().setText("                                      \n").setExtra(Arrays.asList(
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("확실합니까?\n\n").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("아이디: ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(itd.getUsername()).setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("닉네임: ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(itd.getNickname()).setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("\n").setColor(ChatColor.LIGHT_GRAY).build()
            )).build();

            Chat c = new Chat.Builder().setText(" \n").setExtra(Arrays.asList(
                    new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                    new Chat.Builder().setText(" MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("인증 ").setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("■■■■■■■■■■■■■■■■■■■■■■\n\n").setColor(ChatColor.LIGHT_GRAY).setBold(true).build(),
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("본인").setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("이 맞으신가요?\n\n").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(" ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("아이디: ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(itd.getUsername()).setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("\n ● ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("MineCheat ").setColor(ChatColor.LIGHT_GREEN).build(),
                    new Chat.Builder().setText("닉네임: ").setColor(ChatColor.LIGHT_WHITE).build(),
                    new Chat.Builder().setText(itd.getNickname()).setColor(ChatColor.LIGHT_YELLOW).build(),
                    new Chat.Builder().setText("\n\n").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("             ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("[본인이 아닙니다]").setColor(ChatColor.LIGHT_RED)
                            .setBold(true)
                            .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT,
                                    new Chat.Builder().setText("     [본인이 아닙니다]\n").setColor(ChatColor.LIGHT_RED).setExtra(Arrays.asList(confirm)).build()
                            )).setClickEvent(new ChatClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/disapprove")).build(),
                    new Chat.Builder().setText("                ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("[본인이 맞습니다]").setColor(ChatColor.LIGHT_GREEN)
                            .setBold(true)
                            .setHoverEvent(new ChatHoverEvent(ChatHoverEvent.Action.SHOW_TEXT,
                                    new Chat.Builder().setText("     [본인이 맞습니다]\n").setColor(ChatColor.LIGHT_GREEN).setExtra(Arrays.asList(confirm)).build()
                            )).setClickEvent(new ChatClickEvent(ChatClickEvent.Action.RUN_COMMAND, "/approve")).build(),
                    new Chat.Builder().setText("             ").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText("\n\n").setColor(ChatColor.LIGHT_GRAY).build(),
                    new Chat.Builder().setText(" ● 위의 '본인이 맞습니다' 클릭시, 해당 MineCheat 계정에 현제 로그인된 마인크래프트 계정이 등록되는 것에 동의합니다\n").setColor(ChatColor.LIGHT_GRAY).build()
            )).build();
            this.current = IntegrationState.WAIT_FOR_APPROVAL;
            try { sendPacket(new PacketPlayOut02Chat(c, ChatPosition.SYSTEM_MESSAGE)); } catch (Exception ex) {}
        });
    }

    public void cancelKeepAlive() {
        if (keepAliveTask != null)
            keepAliveTask.cancel();
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
                    willSendKeepAlive = System.currentTimeMillis() + 10000;
                    willReceiveKeepAliveBefore = System.currentTimeMillis() + 30000;
                    recievedKeepAlive = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
