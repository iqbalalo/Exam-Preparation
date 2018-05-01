package examprep.utils;

import java.util.Random;

public class RandomString {

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch) {
            tmp.append(ch);
        }
        for (char ch = 'a'; ch <= 'z'; ++ch) {
            tmp.append(ch);
        }
        for (char ch = 'A'; ch <= 'Z'; ++ch) {
            tmp.append(ch);
        }
        symbols = tmp.toString().toCharArray();
    }

    public static String getRandomString(int length) {

        Random r = new Random();
        final char[] buf = new char[length];

        for (int i = 0; i < length; i++) {
            buf[i] = symbols[r.nextInt(symbols.length)];
        }
        return new String(buf);
    }

}
