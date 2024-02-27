package com.github.desprez.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizzTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Quizz getQuizzSample1() {
        return new Quizz()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .title("title1")
            .description("description1")
            .maxAnswerTime(1)
            .attempsLimit(1)
            .questionCount(1);
    }

    public static Quizz getQuizzSample2() {
        return new Quizz()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .title("title2")
            .description("description2")
            .maxAnswerTime(2)
            .attempsLimit(2)
            .questionCount(2);
    }

    public static Quizz getQuizzRandomSampleGenerator() {
        return new Quizz()
            .id(UUID.randomUUID())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .maxAnswerTime(intCount.incrementAndGet())
            .attempsLimit(intCount.incrementAndGet())
            .questionCount(intCount.incrementAndGet());
    }
}
