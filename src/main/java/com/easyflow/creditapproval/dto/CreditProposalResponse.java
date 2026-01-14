package com.easyflow.creditapproval.dto;

import com.easyflow.creditapproval.entity.ProposalStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "CreditProposalResponse", description = "Representação de uma proposta de crédito retornada pela API")
public record CreditProposalResponse(
        @Schema(description = "ID da proposta", example = "12")
        Long id,

        @Schema(description = "Nome do cliente", example = "Ana Alta Renda")
        String name,

        @Schema(description = "Renda mensal do cliente", example = "12000.00")
        BigDecimal income,

        @Schema(description = "Valor solicitado no crédito", example = "5000.00")
        BigDecimal value,

        @Schema(description = "Status atual da proposta", example = "PENDING")
        ProposalStatus status,

        @Schema(description = "Score calculado no workflow", example = "730")
        Integer score,

        @Schema(description = "ID da instância do processo no Camunda", example = "4c552e70-f14a-11f0-92ce-be4c0763554a")
        String processInstanceId,

        @Schema(description = "Data/hora de criação da proposta", example = "2026-01-14T10:09:45.898187")
        LocalDateTime createdAt
) {}
