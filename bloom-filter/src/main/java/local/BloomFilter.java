package local;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class BloomFilter {

    private int n, m, k;
    private double p;
    private BitSet bits;

    public BloomFilter(int n, double p) {
        this.n = n;
        this.p = p;

        m = (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
        k = Math.max(1, (int) ((double) m / n * Math.log(2)));
        bits = new BitSet(m);
    }

    public void add(String key) {
        long hash = Hashing.murmur3_128().hashString(key, StandardCharsets.UTF_8).asLong();
        int h1 = (int) hash;
        int h2 = (int) (hash >>> 32);

        for (int i = 1; i <= k; i++) {
            int h = h1 + i * h2;
            if (h < 0) {
                h = ~h;
            }
            bits.set(h % m);
        }
    }

    public boolean contains(String key) {
        long hash = Hashing.murmur3_128().hashString(key, StandardCharsets.UTF_8).asLong();
        int h1 = (int) hash;
        int h2 = (int) (hash >>> 32);

        for (int i = 1; i <= k; i++) {
            int h = h1 + i * h2;
            if (h < 0) {
                h = ~h;
            }
            if (!bits.get(h % m)) {
                return false;
            }
        }
        return true;
    }
}
