package com.mido.controller.api;


import com.mido.dto.ReturnUserDto;
import com.mido.model.Message;
import com.mido.service.ActiveUserStore;
import com.mido.service.ChatService;
import com.mido.service.UserService;
import com.mido.utility.AuthData;
import com.mido.utility.StorageUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;
    @Autowired
    ActiveUserStore activeUserStore;


    @GetMapping("get_users_chats")
    public ResponseEntity<Object> getUsersChats(){


        chatService.findAllByUserId(AuthData.getCurrentUserId());

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("users", chatService.findAllByUserId(AuthData.getCurrentUserId()));
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }


    @GetMapping("active_users")
    public ResponseEntity<Object> getActiveUsers(){

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("active_users", activeUserStore.getUsers());
        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }


    @GetMapping("get_chat_messages/{secondPersonId}")
    public ResponseEntity<Object> getChatMessages(@PathVariable("secondPersonId") Long secondPersonId){

        List<Message> messages = chatService.getChatMessages(AuthData.getCurrentUserId(), secondPersonId);
        ReturnUserDto returnUserDto = userService.findReturnUserById(secondPersonId);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", messages);
        map.put("user_details", returnUserDto);


        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    @PostMapping("update_all_to_seen")
    public ResponseEntity<Object> updateAllToSeen(@RequestParam(name = "secondPersonId") Long secondPersonId){

        String messages = chatService.updateAllToSeen(AuthData.getCurrentUserId(), secondPersonId);
        ReturnUserDto returnUserDto = userService.findReturnUserById(secondPersonId);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("messages", messages);
        map.put("user_details", returnUserDto);


        return new ResponseEntity<Object>(map, HttpStatus.OK);
    }

    @PostMapping("send_chat_messages")
    public Message sendChatMessages(
            @RequestParam("message") String message,
            @RequestParam("receiverId") Long receiverId,
            @RequestParam(name ="media", required = false) MultipartFile imageFile) throws IOException {


        Message m = new Message();

        if(imageFile != null){
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
            m.setMedia(fileName);


            String uploadDirectory = "src/main/media/";
            StorageUtl.saveFile(uploadDirectory, fileName, imageFile);
        }



        m.setMessage(message);
        m.setSenderId(AuthData.getCurrentUserId());
        m.setReceiverId(receiverId);
        m.setIsRead(0);

        return chatService.saveMessage(m);
    }

    @PostMapping("update_message_is_read_to_seen")
    public Long updateMessageIsReadToSeen(@RequestParam(name = "id") Long id){

        return chatService.updateMessageIsReadToSeen(id);
    }




}
