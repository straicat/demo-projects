package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.Optional;

public class RedisResponseHandler extends SimpleChannelInboundHandler<Optional<String>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Optional<String> msg) throws Exception {
        if (msg.isPresent()) {
            System.out.println("\"" + msg.get() + "\"");
        } else {
            System.out.println("(nil)");
        }
        InetSocketAddress address = (InetSocketAddress) ctx.channel().attr(AttributeKey.valueOf("address")).get();
        System.out.printf("%s:%d> ", address.getHostString(), address.getPort());
    }
}
