package kr.minecheat.mcauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kr.minecheat.mcauth.jackson.MCCharacterEscapes;
import kr.minecheat.mcauth.netty.MinecraftPacketHandler;
import kr.minecheat.mcauth.netty.PacketHandlerInitializer;
import kr.minecheat.mcauth.packetIO.PacketDataIOProvider;
import kr.minecheat.mcauth.packetIO.impls.PacketDataIOProviderImpl;
import kr.minecheat.mcauth.utils.EncryptionUtils;
import lombok.Getter;
import lombok.Setter;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    @Getter
    private static final PacketDataIOProvider dataIOProvider = new PacketDataIOProviderImpl();
    @Getter
    private static final ObjectMapper mapper = new ObjectMapper();
    @Getter
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    @Getter
    private static final Timer keepAlivetimer = new Timer();
    @Getter
    private static final Random random = new Random();
    @Getter
    private static HikariDataSource dataSource;

    @Getter
    private static KeyPair serverKeys;

    private int port;

    public Server(int port) throws NoSuchAlgorithmException {
        this.port = port;
        serverKeys = EncryptionUtils.generate1024RSAKey();
        HikariConfig config = new HikariConfig("hikariConfig.properties");
        dataSource = new HikariDataSource(config);
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new PacketHandlerInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync(); // (7)

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        mapper.getJsonFactory().setCharacterEscapes(new MCCharacterEscapes());

        int port = 25565;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new Server(port).run();
    }
}
