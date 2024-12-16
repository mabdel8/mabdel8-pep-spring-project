package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account){
        try {
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (Exception e){
            if (e.getMessage().equals("Username already exists")){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        try {
            Account loggedAccount = accountService.login(account);
            return ResponseEntity.ok(loggedAccount);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message msg){
        try {
            Message addedMessage = messageService.createMessage(msg);
            return ResponseEntity.ok(addedMessage);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable Integer messageId){
        return messageService.getMessageById(messageId).orElse(null);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable Integer accountId){
        return messageService.getMessagesByAccountId(accountId);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable Integer messageId){
        int deleted = messageService.deleteMessageById(messageId);
        if (deleted == 1){
            return ResponseEntity.ok(deleted);
        }
        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable int messageId, @RequestBody Map<String, String> reqBody){
        String newText = reqBody.get("messageText");
        try {
            int rowsUpdated = messageService.updateMessage(messageId, newText);
            if(rowsUpdated == 1) {
                return ResponseEntity.ok(rowsUpdated);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
