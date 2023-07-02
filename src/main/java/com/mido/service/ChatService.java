package com.mido.service;

import com.mido.dto.ReturnMessageDTO;
import com.mido.dto.ReturnUserDto;
import com.mido.model.Message;
import com.mido.repository.ChatRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@Transactional
public class ChatService {

    @Autowired
    private ChatRepo chatRepo;

    public List<ReturnMessageDTO> findAllByUserId(Long userId){
        List<Object[]> users = chatRepo.getUsers( userId);
        List<ReturnUserDto> usersList = new ArrayList<>();
        for (Object[] obj :users) {
            usersList.add(new ReturnUserDto(
                    (Long) obj[0],
                    (String) obj[1],
                    (String) obj[2],
                    (String) obj[3],
                    (String) obj[4],
                    (Long) obj[5]
            ));
        }

        List<ReturnMessageDTO> messageDTOList = new ArrayList<>();

        for (int i = 0; i < usersList.size(); i++) {

            String lastMessage = "no message Yet";
            String lastMessageDate = "";
            Long lastMessageId = 0L;

            if(!chatRepo.getMessage(usersList.get(i).getId(), userId).isEmpty()){
                Object[] obj = chatRepo.getMessage(usersList.get(i).getId(), userId).get(0);

                if(obj.length >0 ){
                    lastMessageId = (Long) obj[0];
                    lastMessageDate = (String) obj[1];
                    lastMessage = (String) obj[2];
                    messageDTOList.add(new ReturnMessageDTO(
                            usersList.get(i).getId(),
                            lastMessageId,
                            usersList.get(i).getFullName(),
                            usersList.get(i).getEmail(),
                            usersList.get(i).getImage(),
                            usersList.get(i).getUnread(),
                            lastMessage,
                            lastMessageDate
                    ));
                }
            }else{
                messageDTOList.add(new ReturnMessageDTO(
                        usersList.get(i).getId(),
                        lastMessageId,
                        usersList.get(i).getFullName(),
                        usersList.get(i).getEmail(),
                        usersList.get(i).getImage(),
                        usersList.get(i).getUnread(),
                        lastMessage,
                        lastMessageDate));
            }
        }
        Collections.sort(messageDTOList,
                ReturnMessageDTO.lastMessageIdOrder);

        return  messageDTOList;

    }

    public Message saveMessage(Message message){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        message.setCreatedDate(dtf.format(now));
        return chatRepo.save(message);
    }

    public List<Message> getChatMessages(Long userId, Long secondPersonId){
        chatRepo.updateUnreadToRead(userId, secondPersonId);
        return chatRepo.findBySenderIdIsAndReceiverIdIsOrReceiverIdIsAndSenderIdIsOrderByIdDesc(userId, secondPersonId, userId, secondPersonId);
    }


    public Long updateMessageIsReadToSeen(Long id){
        chatRepo.updateOneMessageToSeen(id);
        return id;
    }
}
