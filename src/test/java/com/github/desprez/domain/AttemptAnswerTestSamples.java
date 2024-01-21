package com.github.desprez.domain;

import java.util.UUID;

public class AttemptAnswerTestSamples {

    public static AttemptAnswer getAttemptAnswerSample1() {
        return new AttemptAnswer().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static AttemptAnswer getAttemptAnswerSample2() {
        return new AttemptAnswer().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static AttemptAnswer getAttemptAnswerRandomSampleGenerator() {
        return new AttemptAnswer().id(UUID.randomUUID());
    }
}
