package ketama.impl;

import ketama.Hasher;

import java.util.zip.CRC32;

public class HasherCrcImpl implements Hasher {

    private static CRC32 crc32 = new CRC32();

    @Override
    public Long hash(String key) {
        crc32.reset();
        crc32.update(key.getBytes());
        return crc32.getValue();
    }
}
