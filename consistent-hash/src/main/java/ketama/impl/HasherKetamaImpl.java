package ketama.impl;

import ketama.Hasher;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HasherKetamaImpl implements Hasher {

    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long hash(String key) {
        md5.reset();
        md5.update(key.getBytes());
        byte[] bk = md5.digest();

        long rv = (long) (bk[3] & 255) << 24 | (long) (bk[2] & 255) << 16 | (long) (bk[1] & 255) << 8 | (long) (bk[0] & 255);
        return rv & 4294967295L;
    }
}
