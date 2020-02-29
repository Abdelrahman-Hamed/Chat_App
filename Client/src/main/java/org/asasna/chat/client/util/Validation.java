package org.asasna.chat.client.util;

import java.util.regex.Pattern;

public class Validation {

    public static boolean validatePhoneNumber(String phoneNumber){
        return Pattern.matches("01[0-9]{9}", phoneNumber);
    }
    /////Aya/////////////
    public static boolean validateEmail(String email){
        return Pattern.matches("[a-zA-Z_0-9._%+-]+@[a-zA-Z_0-9.-]+\\.[a-zA-Z]{2,}",email);
    }
    public static boolean validateLoginPassword(String password){
        return Pattern.matches("^[a-zA-Z0-9?><;,{}_+=!@#$%^&*|']*$", password);
    }
    public static boolean validatePassword(String password){
        return Pattern.matches("^[a-zA-Z0-9?><;,{}_+.=!@#$%^&*|']*$",password);
    }
    public static boolean validateConfiermedPassword(String password,String confirmedpassword){
        return confirmedpassword.equals(password)? true: false ;
    }
    ///////////end///////////////
}
