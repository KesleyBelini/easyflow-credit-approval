package com.easyflow.creditapproval.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "TaskResponseDTO", description = "Tarefa pendente de decisão manual (User Task) no Camunda")
public record TaskResponseDTO(
        @Schema(description = "ID da task no Camunda (use para aprovar/rejeitar)", example = "a1b2c3d4-e5f6-...")
        String taskId,

        @Schema(description = "ID da proposta (business key do processo)", example = "13")
        String proposalId,

        @Schema(description = "Nome do cliente", example = "João Médio")
        String clientName,

        @Schema(description = "Score atual da proposta", example = "680")
        Integer score
) {}
