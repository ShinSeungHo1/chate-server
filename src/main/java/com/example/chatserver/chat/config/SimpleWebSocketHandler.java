package com.example.chatserver.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// connect로 websocket 연결요청이 들어왔을 때 이를 처리해줄 클래스
@Slf4j
@Component
public class SimpleWebSocketHandler extends TextWebSocketHandler {

    // 연결된 session관리를 할건데 : 스레드 safe 한 set을 사용 할것!!!!
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    // 사용자 연결 정보를 등록
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("Connected : " + session.getId());
    }

    // 사용자에게 메시지를 보내는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("received message : " + payload);
        for(WebSocketSession s : sessions) {
            if(s.isOpen()) {
                s.sendMessage(new TextMessage(payload));
            }
        }
    }

    // 연결이 끊기면 메모리에서 삭제
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("disconnected!!!!!");
    }

}
