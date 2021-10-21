package server;

import domain.RedisResponse;
import enums.ValueTypeEnum;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Sharable并不保证单例，只是当多个Pipeline使用同一Handler时不抛出异常
@ChannelHandler.Sharable
public class RedisServerHandler extends SimpleChannelInboundHandler<String[]> {
    private final static String GET = "get";
    private final static String SET = "set";

    private final Map<String, String> redisStore = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String[] msg) throws Exception {
        if (msg.length == 0) {
            return;
        }
        RedisResponse response = new RedisResponse();
        if (GET.equalsIgnoreCase(msg[0])) {
            // 处理get请求
            if (msg.length == 2) {
                String key = msg[1];
                response.setValueType(ValueTypeEnum.MULTI_LINE_STRING);
                response.setValue(redisStore.getOrDefault(key, null));
            } else {
                response.setValueType(ValueTypeEnum.ONE_LINE_MESSAGE);
                response.setValue("(error) ERR wrong number of arguments for 'get' command");
            }
        } else if (SET.equalsIgnoreCase(msg[0])) {
            // 处理set请求
            if (msg.length == 3) {
                String key = msg[1];
                String value = msg[2];
                redisStore.put(key, value);
                response.setValueType(ValueTypeEnum.ONE_LINE_STRING);
                response.setValue("OK");
            } else {
                response.setValueType(ValueTypeEnum.ONE_LINE_MESSAGE);
                if (msg.length < 3) {
                    response.setValue("(error) ERR wrong number of arguments for 'set' command");
                } else {
                    response.setValue("(error) ERR syntax error");
                }
            }
        } else {
            // 未知的命令
            response.setValueType(ValueTypeEnum.ONE_LINE_MESSAGE);
            StringBuilder sb = new StringBuilder();
            sb.append("(error) ERR unknown command `");
            sb.append(msg[0]);
            sb.append("`, with args beginning with:");
            for (int i = 1; i < msg.length; i++) {
                sb.append(" `").append(msg[i]).append("`,");
            }
            response.setValue(sb.toString());
        }
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
