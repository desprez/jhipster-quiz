package com.github.desprez.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AttemptTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Attempt getAttemptSample1() {
        return new Attempt().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).score(1);
    }

    public static Attempt getAttemptSample2() {
        return new Attempt().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).score(2);
    }

    public static Attempt getAttemptRandomSampleGenerator() {
        return new Attempt().id(UUID.randomUUID()).score(intCount.incrementAndGet());
    }
}
