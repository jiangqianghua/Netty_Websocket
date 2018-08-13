package com.jqh.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitialzer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // websocket是基于http协议，需要http编解码器
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 对httpmessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        // 几乎在netty的编程中，都会使用此handler
        pipeline.addLast(new HttpObjectAggregator(1024));

        // websocket服务器处理协议，用户指定给客户端访问连接的路由 /ws
        // 本handler会帮你处理一写繁重复杂的事情
        //会帮你处理握手，handshaking（close，ping , pong）
        // 对于websocket来说。都是以frames进行传输，不同的数据类型对应frame也不同
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new ChatHandler());
    }
}
