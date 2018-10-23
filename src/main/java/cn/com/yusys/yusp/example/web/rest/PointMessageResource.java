package cn.com.yusys.yusp.example.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cn.com.yusys.yusp.example.web.dto.Message;

@Controller
public class PointMessageResource {
	
	@Autowired
	private SimpMessagingTemplate messageSend;
	
	@MessageMapping("/point2point")
	public void sendToUser(Message message,SimpMessageHeaderAccessor headerAccessor) {
		System.out.println(headerAccessor.getUser().getName()+" to "+message.getTo()+":"+message.getMessage());
		messageSend.convertAndSendToUser(message.getTo(), "/queue/point2point", message.getMessage());
	}
}
