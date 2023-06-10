package ketama.impl;

import ketama.Hasher;

public class HasherFnv1Impl implements Hasher {

    @Override
    public Long hash(String key) {
        int p = 16777619;
        long hash = 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        return Math.abs(hash);
    }
}
