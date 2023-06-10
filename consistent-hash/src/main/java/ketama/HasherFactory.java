package ketama;

import ketama.impl.HasherCrcImpl;
import ketama.impl.HasherFnv1Impl;
import ketama.impl.HasherKetamaImpl;

public class HasherFactory {

    public static Hasher getHasher(HashEnum h) {
        switch (h) {
            case CRC32_HASH: return new HasherCrcImpl();
            case FNV1_32_HASH: return new HasherFnv1Impl();
            case KETAMA_HASH: return new HasherKetamaImpl();
            default: return null;
        }
    }
}
