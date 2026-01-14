package com.easyflow.creditapproval.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ApiError", description = "Resposta padrão de erro da API")
public record ApiError(
        @Schema(example = "404") int status,
        @Schema(example = "TASK_NOT_FOUND") String code,
        @Schema(example = "Task not found: abc123") String message,
        @Schema(example = "2026-01-14T12:30:45.123") LocalDateTime timestamp
) {}