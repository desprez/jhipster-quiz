package com.github.desprez.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Question getQuestionSample1() {
        return new Question()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .statement("statement1")
            .index(1)
            .correctOptionIndex(1);
    }

    public static Question getQuestionSample2() {
        return new Question()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .statement("statement2")
            .index(2)
            .correctOptionIndex(2);
    }

    public static Question getQuestionRandomSampleGenerator() {
        return new Question()
            .id(UUID.randomUUID())
            .statement(UUID.randomUUID().toString())
            .index(intCount.incrementAndGet())
            .correctOptionIndex(intCount.incrementAndGet());
    }
}
