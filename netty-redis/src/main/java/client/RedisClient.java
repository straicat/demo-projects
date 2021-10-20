package client;

import codec.RedisResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class RedisClient {
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private Channel channel;

    public RedisClient(InetSocketAddress address) {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RedisResponseDecoder());
                            ch.pipeline().addLast(new RedisClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(address).sync();
            channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void get(String key) {
        channel.writeAndFlush(Unpooled.copiedBuffer("GET " + key + "\n", StandardCharsets.UTF_8));
    }

    public void close() {
        if (channel != null) {
            channel.close();
        }
        group.shutdownGracefully();
    }

    public static void main(String[] args) {
        RedisClient client = new RedisClient(new InetSocketAddress("127.0.0.1", 6379));
        client.get("test");
        client.close();
    }
}
