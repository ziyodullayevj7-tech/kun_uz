package jahongir.kun_uz.util;

import java.util.Random;

public class RandomUtil {
    private static final Random random = new Random();

    public static int fiveDigit(){
        return 10000 + random.nextInt(90000);
    }
}
