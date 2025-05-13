//package com.selfstudy.config;
//
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//public class MyWebSocketHandler extends TextWebSocketHandler {
//
//    @Override
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 处理客户端发送的消息
//        String payload = message.getPayload();
//        // 你可以在这里处理消息，或者向客户端发送回复
//        session.sendMessage(new TextMessage("Echo: " + payload));
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // 连接建立时的处理
//        System.out.println("WebSocket connection established: " + session.getId());
//    }
//}
