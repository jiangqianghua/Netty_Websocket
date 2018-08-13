package com.jqh.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import sun.util.resources.LocaleData;

/**
 * 处理消息的handler
 * TextWebSocketFrame，在netty中，用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户管理和记录所有channel
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        String content = textWebSocketFrame.text();
        System.out.println("接受到的消息:"+content);

        for (Channel channel:clients){
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息:]"+ content));
        }
        // 等同于for循环发送消息
        //clients.writeAndFlush(new TextWebSocketFrame("[服务器接受到消息:]"+ content))
    }

    /**
     * 打开链接，获取客户端channel，并且官方到ChannelGroup中
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
       // super.handlerAdded(ctx);
        clients.add(ctx.channel());
    }

    /**
     * 自动移除客户端不的channel
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
      //  super.handlerRemoved(ctx);
      //  clients.remove(ctx.channel()); // 可以不写，系统会自动移除
        System.out.println("handlerRemoved channel longText :"+ctx.channel().id().asLongText());
        System.out.println("handlerRemoved channel shortText :"+ctx.channel().id().asShortText());
    }
}
