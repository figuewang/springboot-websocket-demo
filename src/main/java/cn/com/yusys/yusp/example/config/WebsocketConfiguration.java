package cn.com.yusys.yusp.example.config;

import java.util.LinkedList;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import cn.com.yusys.yusp.example.config.websocket.User;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册端点，发布或者订阅消息的时候需要连接此端点
     * addEndpoint websocket的端点，客户端需要注册这个端点进行链接
     * setAllowedOrigins 非必须，*表示允许其他域进行连接，跨域
     * withSockJS 允许客户端利用sockjs进行浏览器兼容性处理
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socketserver")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    /**
     * 配置消息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 设置服务器广播消息的基础路径
        registry.enableSimpleBroker("/queue");
    }   
  
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                	Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
    	            if (raw instanceof Map) {
    	                Object name = ((Map) raw).get("login");
    	                if (name instanceof LinkedList) {
    	                    // 设置当前访问器的认证用户
    	                	String userName = ((LinkedList) name).get(0).toString();
    	                	System.out.println("regist userName:"+userName);
    	                    accessor.setUser(new User(userName));
    	                }
    	            }
                }
                return message;
            }
        });
    }
}