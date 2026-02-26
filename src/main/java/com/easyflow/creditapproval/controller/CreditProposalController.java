package com.easyflow.creditapproval.controller;

import com.easyflow.creditapproval.dto.CreateCreditProposalRequest;
import com.easyflow.creditapproval.dto.CreditProposalResponse;
import com.easyflow.creditapproval.dto.TaskResponseDTO;
import com.easyflow.creditapproval.exception.ApiError;
import com.easyflow.creditapproval.service.CreditProposalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Credit Proposals", description = "Criação de proposta e ações manuais (tasks) do fluxo BPM")
@RestController
@RequestMapping("/credit-proposals")
@RequiredArgsConstructor
public class CreditProposalController {
    private final CreditProposalService service;

    @Operation(
            summary = "Cria uma proposta de crédito",
            description = "Cria a proposta (status inicial PENDING), inicia o processo 'credit_proposal' no Camunda e retorna os dados persistidos."
    )
    @ApiResponse(responseCode = "201", description = "Proposta criada e processo iniciado",
            content = @Content(schema = @Schema(implementation = CreditProposalResponse.class)))
    @ApiResponse(responseCode = "400", description = "Payload inválido (validações Bean Validation)", content = @Content)
    @PostMapping
    public ResponseEntity<CreditProposalResponse> create(@Valid @RequestBody CreateCreditProposalRequest dto) {
        var response = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Lista tarefas pendentes de análise manual",
            description = "Retorna as User Tasks ativas ('review_proposal') aguardando decisão. " +
                    "Use o taskId retornado para aprovar/rejeitar.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class))))
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDTO>> listTasks() {
        return ResponseEntity.ok(service.getPendingTasks());
    }

    @Operation(summary = "Aprova uma tarefa manual",
            description = "Completa a task com manualApproval=true. O fluxo segue para aprovação e persiste status APPROVED.")
    @ApiResponse(responseCode = "204", description = "Task completada com sucesso")
    @ApiResponse(responseCode = "404", description = "Task não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Task já completada ou em conflito",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @PostMapping("/tasks/{taskId}/approve")
    public ResponseEntity<Void> approve(@PathVariable String taskId) {
        service.approveTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rejeita uma tarefa manual",
            description = "Completa a task com manualApproval=false. O fluxo segue para rejeição e persiste status REJECTED.")
    @ApiResponse(responseCode = "204", description = "Task completada com sucesso")
    @ApiResponse(responseCode = "404", description = "Task não encontrada",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "409", description = "Task já completada ou em conflito",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @PostMapping("/tasks/{taskId}/reject")
    public ResponseEntity<Void> reject(@PathVariable String taskId) {
        service.rejectTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
