package com.easyflow.creditapproval.dto;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "CreateCreditProposalRequest", description = "Payload para criar uma nova proposta de crédito")
public record CreateCreditProposalRequest(
        @Schema(description = "Nome do cliente", example = "Ana Alta Renda")
        @NotBlank
        String name,

        @Schema(description = "Idade do cliente", example = "45", minimum = "1")
        @NotNull @Positive
        Integer age,

        @Schema(description = "Renda mensal do cliente", example = "12000.00", minimum = "0.01")
        @NotNull @Positive
        BigDecimal income,

        @Schema(description = "Valor solicitado no crédito", example = "5000.00", minimum = "0.01")
        @NotNull @Positive
        BigDecimal value
) {}
