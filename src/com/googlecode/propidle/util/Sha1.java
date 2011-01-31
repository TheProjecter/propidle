package com.googlecode.propidle.util;

import static java.lang.String.format;
import java.security.MessageDigest;

/*
Based on http://www.anyexample.com/programming/java/java_simple_class_to_compute_sha_1_hash.xml
 */
public class Sha1 {

    public static String sha1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            md.update(text.getBytes("iso-8859-1"), 0, text.length());

            return convertToHex(md.digest());
        } catch (Exception e) {
            throw new ThisShouldNeverHappenException(format("Problem hashing %s", text), e);
        }
    }

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
