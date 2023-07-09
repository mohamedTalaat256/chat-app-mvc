package com.mido.controller;


import com.mido.dto.MessageNotificationDto;
import com.mido.model.Message;
import com.mido.model.User;
import com.mido.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
public class SocketChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private-message")
    public MessageNotificationDto recMessage(@Payload Message message){

        User sender = userService.findById(message.getSenderId()).get();
        String senderName = sender.getFirstName()+ " "+ sender.getLastName();
        String senderImage = sender.getImage();

        MessageNotificationDto notification = new MessageNotificationDto();
        notification.setId(message.getId());
        notification.setMessage(message.getMessage());
        notification.setReceiverId(message.getReceiverId());
        notification.setSenderId(message.getSenderId());
        notification.setCreatedDate(message.getCreatedDate());
        notification.setMedia(message.getMedia());
        notification.setSenderName(senderName);
        notification.setSenderImage(senderImage);

        simpMessagingTemplate.convertAndSendToUser(message.getReceiverId().toString(),"/private",notification);


        return notification;
    }

    @MessageMapping("/status-message")
    public Message recMessageSeenStatus(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getSenderId().toString(),"/private-status",message);
        return message;
    }


}
