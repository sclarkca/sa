package com.mr.sa.utils.pwd;

import java.util.Random;

/**
 * @author hongk
 * 随机生成8位密码
 */
public class MakeRandomPasswordUtil {

    private static String regx = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

    //随机生成 8 位由大写字母,小写字母,数字和特殊符号组成的密码
    public static String makeRandomPassword() {
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_-+={}[]/?,.\"<>\\|:;\'`".toCharArray();  //L20170131 update by hong.k
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int x = 0; x < 8; ++x) {
            sb.append(charr[random.nextInt(charr.length)]);
        }
        String randomPassword = sb.toString();
        if (checkPassword(randomPassword)) {
            return randomPassword;
        } else {
            randomPassword = makeRandomPassword();
        }
        return randomPassword;
    }

    //验证密码是否由大写字母,小写字母,数字和特殊符号组成
    public static boolean checkPassword(String strPass) {
        if (strPass.matches(regx)) {
            return true;
        }
        return false;
    }

}
