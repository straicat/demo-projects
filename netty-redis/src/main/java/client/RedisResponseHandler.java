package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

public class RedisResponseHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("\"" + msg + "\"");
        InetSocketAddress address = (InetSocketAddress) ctx.channel().attr(AttributeKey.valueOf("address")).get();
        System.out.printf("%s:%d> ", address.getHostString(), address.getPort());
    }
}
