package com.dennistjahyadi.cashless.Utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Denn on 10/5/2017.
 */

public class Encryption {

    private static String code = "^Aj7";
    private static String alphabet = "ghijklmnopqrstuvwxyz";

    public static String encrypt(String textt) {

        char[] aaa = textt.toCharArray();
        String[] hexaString = new String[aaa.length];
        for (int i = 0; i < aaa.length; i++) {
            hexaString[i] = convertStringToHex(String.valueOf(aaa[i]));
        }
        Random r = new Random();

        String randomcode = "";
        for (int i = 0; i < 4; i++) {
            randomcode += alphabet.charAt(r.nextInt(alphabet.length()));
        }

        String encrypted = randomcode;


        for (String hex : hexaString) {
            encrypted = encrypted + "" + multiply(hex) + alphabet.charAt(r.nextInt(alphabet.length()));
        }

        return encrypted;
    }

    public static String decrypt(String textt) {

        for (char alph : alphabet.toCharArray()) {
            textt = textt.replace(alph + "", " ");

        }
        textt = textt.trim();

        String[] aaa = textt.split(" ");
        String[] hexaString = new String[aaa.length];

        for (int i = 0; i < aaa.length; i++) {
            hexaString[i] = aaa[i];
        }
        String decrypted = "";

        try {
            for (String hex : hexaString) {
                decrypted = decrypted + "" + convertHexToString(divide(hex));
            }
        } catch (Exception ex) {
            return "0";
        }

        return decrypted;

    }

    private static String multiply(String hexa) {
        String salt = convertStringToHex(code);
        long value = Long.parseLong(salt, 16);
        long value2 = Long.parseLong(hexa, 16);

        long answer = value2 * value;

        hexa = Long.toHexString(answer);
        return hexa;
    }

    private static String divide(String hexa) {
        String asd = convertStringToHex(code);
        long value = Long.parseLong(asd, 16);
        long value2 = Long.parseLong(hexa, 16);

        long answer = value2 / value;

        hexa = Long.toHexString(answer);
        return hexa;
    }

    private static String convertStringToHex(String arg) {
        return String.format("%1x", new BigInteger(1, arg.getBytes()));
    }

    private static String convertHexToString(String hex) {

        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        //49204c6f7665204a617661 split into two characters 49, 20, 4c...
        for (int i = 0; i < hex.length() - 1; i += 2) {

            //grab the hex in pairs
            String output = hex.substring(i, (i + 2));
            //convert hex to decimal
            long decimal = Long.parseLong(output, 16);
            //convert the decimal to character
            sb.append((char) decimal);

            temp.append(decimal);
        }
        // System.out.println("Decimal : " + temp.toString());

        return sb.toString();
    }
}
