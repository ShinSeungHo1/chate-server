package com.example.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final SimpleWebSocketHandler simpleWebSocketHandler;

    public WebSocketConfig(SimpleWebSocketHandler simpleWebSocketHandler) {
        this.simpleWebSocketHandler = simpleWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/connect" url로 websocket 연결 요청이 들어오면, 핸들러 클래스가 처리
        // 채팅 하려면 사용자 a, b, c가 있다 해보자
        // 사용자 a가 connect 요청(HTTP 요청은 아님)을 서버에 주면 handler가 처리해 주겠다라고 입력한거
        // 서버에는 A에대한 정보를 메모리에 등록 해놓는다.. 물론 B, C도 마찬가지다
        // A가 Message를 보내주면 서버는 메모리에 A, B, C 목록을 가지고 있기 때문에, A, B, C에 Message를 쏴준다!!
        // 중요한건 메모리에  A, B, C를 등록하고 A, B, C 목록에 Message 를 쏴주는 걸 Handler에서 해준다.
        // 지금 connect 연결 요청은 Http 요청이 아니기 때문에 원래라면 우리가 만든 filter에 걸려 authentication 객체를 만들어야 하는데
        // 거기에 걸리지 않음. 그러기 때문에 우리는 /connect로 들어오는 요청은 제외하라는 세팅을 해줘야 한다.
        // 1. token filter에서 제외
        // 2. web socket 관련 cors 설정
        registry.addHandler(simpleWebSocketHandler, "/connect")
                // securityConfig에서의 cors 예외는 http요청에 대한 예외, 따라서 websocket 프로토콜에 대한 요청에 대해서는 별도의 cors 설정 필요
                .setAllowedOrigins("http://localhost:3000");
    }
}
