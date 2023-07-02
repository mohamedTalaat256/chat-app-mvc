package com.mido.repository;


import com.mido.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Message, Long> {

    @Query(value = "select u.id, CONCAT(u.first_name ,' ', u.last_name) as name, u.email , u.last_name, u.image," +
            " COUNT(m.is_read) as unread" +
            " FROM users u LEFT JOIN messages m ON u.id = m.sender_id and m.is_read=0" +
            " and m.receiver_id =:userId" +
            " where u.id !=:userId" +
            " group by u.id, u.first_name, u.email, u.last_name, u.image ", nativeQuery = true)
    List<Object[]> getUsers( @Param("userId") Long userId);


    @Query(value = "select * FROM messages m " +
            "WHERE (m.receiver_id=:userIdInLoop OR m.sender_id=:userIdInLoop ) " +
            "AND" +
            " (m.sender_id=:userId OR m.receiver_id=:userId ) ORDER BY m.id DESC LIMIT 1", nativeQuery = true)
    List<Object[]> getMessage(@Param("userIdInLoop") Long userIdInLoop, @Param("userId") Long userId);



    @Modifying
    @Query(value = "update messages set is_read=1 " +
            "WHERE sender_id=:secondPersonId and receiver_id=:userId", nativeQuery = true)
    void updateUnreadToRead(@Param("userId") Long userId,@Param("secondPersonId") Long secondPersonId);


    @Modifying
    @Query(value = "update messages set is_read=1 " +
            "WHERE id=:id", nativeQuery = true)
    void updateOneMessageToSeen(@Param("id") Long id);


    List<Message> findBySenderIdIsAndReceiverIdIsOrReceiverIdIsAndSenderIdIsOrderByIdDesc(Long id1,  Long id2, Long id3,  Long id4);


}
