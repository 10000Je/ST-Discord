package com.stuudent.discord.utils;

public class AuthCodeManager {

    public String getAuthCode() {
        StringBuilder authBuild = new StringBuilder();
        for(int i=0; i<6; i++) {
            int random = (int) Math.floor(Math.random()*10);
            authBuild.append(random);
        }
        return authBuild.toString();
    }

}
