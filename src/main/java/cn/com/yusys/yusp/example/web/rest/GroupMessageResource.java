package cn.com.yusys.yusp.example.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import cn.com.yusys.yusp.example.web.dto.Message;

@Controller
public class GroupMessageResource {	
	
	@Autowired
	private SimpMessagingTemplate messageSend;
	
	@MessageMapping("/sendall")
    public void say(Message message) throws Exception {
		messageSend.convertAndSend("/queue/sendall", message);
    }
}
