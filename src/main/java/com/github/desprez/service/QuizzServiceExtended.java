package com.github.desprez.service;

import java.util.UUID;

/**
 * QuizzService extension
 */
public interface QuizzServiceExtended extends QuizzService {
    void publishQuiz(UUID id);
}
