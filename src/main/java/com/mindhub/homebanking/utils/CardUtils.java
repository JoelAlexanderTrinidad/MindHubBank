package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils(){}

    public static short getCcvNumber() {
        return createCvv();
    }

    public static String getCardNumber() {
        return createCardNumber();
    }

    public static short createCvv(){

        Random rand = new Random();
        int cvv = rand.nextInt(1000);
        String cvvStr = String.format("%03d", cvv);

        return Short.parseShort(cvvStr);
    }

    public static String createCardNumber(){
        String num1 = createRandomNumber();
        String num2 = createRandomNumber();
        String num3 = createRandomNumber();
        String num4 = createRandomNumber();

        return num1 + "-" + num2 + "-" + num3 + "-" + num4;
    }

    public static String createRandomNumber(){

        Random rand = new Random();
        int num = rand.nextInt(10000);

        return String.format("%04d", num);
    }
}


