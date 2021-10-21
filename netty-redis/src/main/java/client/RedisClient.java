package client;

import codec.RedisRequestEncoder;
import codec.RedisResponseDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;


public class RedisClient {
    private final InetSocketAddress address;

    public RedisClient(String host, int port) {
        address = new InetSocketAddress(host, port);
    }

    public static void main(String[] args) {
        int port = 6379;
        RedisClient client = new RedisClient("127.0.0.1", port);
        client.start();
    }

    public void start() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        String prompt = String.format("%s:%d> ", address.getHostString(), address.getPort());
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .attr(AttributeKey.newInstance("prompt"), prompt)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RedisRequestEncoder());
                        ch.pipeline().addLast(new RedisResponseDecoder());
                        ch.pipeline().addLast(new RedisClientResponseHandler());
                        ch.pipeline().addLast(new RedisClientRequestHandler());
                    }
                });

        try {
            // 连接Redis服务器
            Channel channel = bootstrap.connect(address).sync().channel();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(prompt);
            ChannelFuture future = null;
            while (true) {
                String s = in.readLine();
                if (s == null || "quit".equalsIgnoreCase(s = s.trim())) {
                    break;
                }
                if (s.length() == 0) {
                    System.out.print(prompt);
                    continue;
                }
                future = channel.writeAndFlush(s);
            }
            if (future != null) {
                future.sync();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
