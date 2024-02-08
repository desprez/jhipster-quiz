package com.github.desprez.service;

import com.github.desprez.service.dto.QuizzDTO;
import java.util.UUID;

/**
 * QuizzService extension
 */
public interface QuizzServiceExtended {
    void publishQuiz(UUID id);
}
