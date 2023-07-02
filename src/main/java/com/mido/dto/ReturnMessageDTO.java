package com.mido.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnMessageDTO {
    private Long id;
    private Long lastMessageId;
    private String name;
    private String email;
    private String image;

    private Long unread;

    private String lastMessage;
    private String lastMessageDate;




    public static Comparator<ReturnMessageDTO> lastMessageIdOrder = new Comparator<ReturnMessageDTO>() {

        // Method
        public int compare(ReturnMessageDTO m1, ReturnMessageDTO m2) {

            int id1 = Math.toIntExact(m1.getLastMessageId());
            int id2 = Math.toIntExact(m2.getLastMessageId());

            // For ascending order
            return id2 - id1;

        }
    };



}
