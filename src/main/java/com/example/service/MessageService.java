package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AccountRepository accountRepository;
    


    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message createMessage(Message msg) throws Exception {
        if (msg.getMessageText().isBlank() || msg.getMessageText().length() > 255) {
            throw new Exception("Message text cannot be blank or greater than 255");
        }
        if (!accountRepository.existsById(msg.getPostedBy())) {
            throw new Exception("invalid user");
        }
        return messageRepository.save(msg);
    }

    public Optional<Message> getMessageById(Integer msgId){
        return messageRepository.findById(msgId);
    }

    public List<Message> getMessagesByAccountId(Integer accountId){
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null){
            //return empty list
            return List.of();
        }
        return messageRepository.findAllByPostedBy(account.getAccountId());
    }

    public int deleteMessageById(Integer messageId) {
        if (messageRepository.existsById(messageId)){
            messageRepository.deleteById(messageId);
            return 1;
        }
        return 0;
    }

    public int updateMessage(int messageId, String msgText) throws Exception {
        if (msgText.isBlank() || msgText.length() > 255){
            throw new Exception("Invalid message");
        }

        Optional<Message> msg = messageRepository.findById(messageId);
        if(msg.isPresent()){
            Message retrievedMsg = msg.get();
            retrievedMsg.setMessageText(msgText);
            messageRepository.save(retrievedMsg);
            return 1;
        }
        
        throw new Exception("Id not found");
    }
}
