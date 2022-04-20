package com.example.springreact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@org.springframework.stereotype.Repository
public class Repository {
    @Autowired
    JdbcTemplate db;
    private boolean validate(User u){
        return validateusername(u) && validateEmail(u);
    }
    private boolean validateusername(User u){
        String sql = "SELECT COUNT(*) FROM users WHERE username=?";
        int number = db.queryForObject(sql, Integer.class, u.getUsername());
        return number == 0;
    }
    private boolean validateEmail(User u){
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        int number = db.queryForObject(sql, Integer.class, u.getEmail());
        return number == 0;
    }
    public boolean register(User u){
        String password = encryptedpassword(u.getPassword());
        if(validate(u)){
            String sql = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
            db.update(sql,u.getUsername(),password,u.getEmail());
            return true;
        }
        else return false;
    }
    private String encryptedpassword(String password){
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        return bCrypt.encode(password);
    }

    public boolean login(String username, String password){
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String sql = "SELECT * FROM users WHERE username=?";
        User check = new User(username,password, "");
        if(!validateusername(check)) {
            User user = db.queryForObject(sql, BeanPropertyRowMapper.newInstance(User.class), username);
            return bCrypt.matches(password, user.getPassword());
        }
        else return false;
    }

}