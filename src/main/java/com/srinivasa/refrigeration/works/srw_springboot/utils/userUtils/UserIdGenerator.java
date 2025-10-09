package com.srinivasa.refrigeration.works.srw_springboot.utils.userUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserIdGenerator {
    public static String generateUniqueId(String phoneNumber) {
        String first2 = phoneNumber.substring(0, 2);
        String last2 = phoneNumber.substring(phoneNumber.length() - 2);

        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));

        return first2 + dateTime + last2;
    }
}