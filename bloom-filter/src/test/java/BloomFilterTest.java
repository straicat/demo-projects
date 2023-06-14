import local.BloomFilter;
import org.junit.Test;

public class BloomFilterTest {

    @Test
    public void test() {
        int size = 1000000;
        BloomFilter bf = new BloomFilter(size, 0.03);
        for (int i = 0; i < size; i++) {
            bf.add(String.valueOf(i));
        }
        int cnt = 0;
        for (int i = size; i < 2 * size; i++) {
            if (bf.contains(String.valueOf(i))) {
                cnt++;
            }
        }
        System.out.println("误判率：" + (double) cnt / size);
    }
}
