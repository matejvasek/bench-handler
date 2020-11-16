package cz.xvasek.bench.handler.impl.mocks;

import java.security.SecureRandom;
import java.util.Arrays;

public class MockSecureRandom extends SecureRandom {
    @Override
    public void nextBytes(byte[] bytes) {
        Arrays.fill(bytes, (byte) 0xff);
        return;
    }
}
