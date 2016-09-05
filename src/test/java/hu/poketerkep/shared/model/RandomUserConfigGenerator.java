package hu.poketerkep.shared.model;


import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RandomUserConfigGenerator {
    private static Random random = new Random();

    public static UserConfig generateWorking() {

        String userName = new BigInteger(130, random).toString(32);
        long lastUsed = Instant.now().minus(Duration.ofHours(random.nextInt(100))).toEpochMilli();

        return new UserConfig(userName, lastUsed, false);
    }

    public static Collection<UserConfig> generateN(int n) {
        ArrayList<UserConfig> userConfigs = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            userConfigs.add(generateWorking());
        }

        return userConfigs;
    }
}
