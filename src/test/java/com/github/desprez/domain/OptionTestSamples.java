package com.github.desprez.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class OptionTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Option getOptionSample1() {
        return new Option().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).statement("statement1").index(1);
    }

    public static Option getOptionSample2() {
        return new Option().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).statement("statement2").index(2);
    }

    public static Option getOptionRandomSampleGenerator() {
        return new Option().id(UUID.randomUUID()).statement(UUID.randomUUID().toString()).index(intCount.incrementAndGet());
    }
}
