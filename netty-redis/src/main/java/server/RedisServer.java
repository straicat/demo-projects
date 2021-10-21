package server;

import codec.RedisServerCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class RedisServer {
    private final InetSocketAddress address;

    public RedisServer(String host, int port) {
        this.address = new InetSocketAddress(host, port);
    }

    public static void main(String[] args) {
        int port = 6379;
        new RedisServer("127.0.0.1", port).start();
    }

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        RedisServerHandler serverHandler = new RedisServerHandler();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RedisServerCodec());
                        // 使用单例，这样才能共享存储的数据
                        ch.pipeline().addLast(serverHandler);
                    }
                });

        try {
            bootstrap.bind(address).sync();
            System.out.println("Netty redis server start at " + address.getHostString() + ":" + address.getPort());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
