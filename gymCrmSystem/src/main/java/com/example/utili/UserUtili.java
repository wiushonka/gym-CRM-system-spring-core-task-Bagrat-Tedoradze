package com.example.utili;

import org.jetbrains.annotations.NotNull;
import java.security.SecureRandom;

public class UserUtili {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();

    private UserUtili(){}

    public static String generateUsername(String firstName, String lastName, Integer serialNum){
        String username = firstName + "." + lastName;
        if(serialNum != null){
            username += serialNum;
        }
        return username;
    }

    public static @NotNull String generatePassword(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<10;++i){
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }
}
