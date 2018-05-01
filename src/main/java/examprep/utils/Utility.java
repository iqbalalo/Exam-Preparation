package examprep.utils;

import java.util.Base64;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Utility {

    public Utility() {
    }

    public static String byteTo64EncodedString(byte[] byteArray) {
        String result = Base64.getEncoder().encodeToString(byteArray);
        return result;
    }

    //check duplicated value
    public static boolean checkDuplicated(String[] sValueTemp) {

        Set<String> sValueSet = new HashSet<>();
        for (String i : sValueTemp) {
            if (!sValueSet.add(i)) {
                return true;
            }
        }
        return false;
    }

    public static int randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
