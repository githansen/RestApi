package com.example.springreact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

//RestController
@RestController
public class Controller extends Thread{
    public boolean innlogget = false;
    @Autowired
    private Repository rep;

    @CrossOrigin
    @PostMapping("/Register")
    public boolean registrer(@RequestBody Map<String, Object> payLoad){
        User u = new User((String)payLoad.get("username"), (String)payLoad.get("password"), (String)payLoad.get("email"));
        return rep.register(u);
    }
    @CrossOrigin
    @PostMapping("/Login")
    public boolean login(@RequestBody Map<String, Object> payLoad) throws InterruptedException {
        String username = (String)payLoad.get("username");
        String password = (String)payLoad.get("password");
        User u = new User((String)payLoad.get("username"), (String)payLoad.get("password"), "");
        if(rep.login(username, password)){
            innlogget = true;
            Thread tråd = new Thread(() -> {
                while(true) {
                    try {
                        TimeUnit.MINUTES.sleep(10);
                        innlogget = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            tråd.start();
            return true;
        }
        else return false;
    }
    @CrossOrigin
    @GetMapping("/Loggedin")
    public boolean checklog(){
        return innlogget;
    }
}