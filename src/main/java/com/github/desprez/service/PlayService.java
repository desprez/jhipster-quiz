package com.github.desprez.service;

import com.github.desprez.service.dto.AttemptDTO;
import java.util.UUID;

public interface PlayService {
    AttemptDTO start(UUID quizId);

    AttemptDTO evaluate(AttemptDTO attemptDTO);
}
