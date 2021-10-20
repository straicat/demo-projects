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
        String prompt = (String) ctx.channel().attr(AttributeKey.valueOf("prompt")).get();
        System.out.print(prompt);
    }
}
