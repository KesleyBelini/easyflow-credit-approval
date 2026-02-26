package com.easyflow.creditapproval.service;

import com.easyflow.creditapproval.dto.CreateCreditProposalRequest;
import com.easyflow.creditapproval.dto.CreditProposalResponse;
import com.easyflow.creditapproval.dto.TaskResponseDTO;
import com.easyflow.creditapproval.entity.CreditProposal;
import com.easyflow.creditapproval.entity.ProposalStatus;
import com.easyflow.creditapproval.exception.TaskNotFoundException;
import com.easyflow.creditapproval.mapper.CreditProposalMapper;
import com.easyflow.creditapproval.repository.CreditProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreditProposalService {
    private final TaskService taskService;
    private final CreditProposalRepository creditProposalRepository;
    private final CreditProposalMapper mapper;
    private final RuntimeService runtimeService;

    @Transactional
    public CreditProposalResponse save(CreateCreditProposalRequest dto) {
        CreditProposal entity = mapper.dtoRequestToEntity(dto);
        entity.setStatus(ProposalStatus.PENDING);
        creditProposalRepository.save(entity);

        log.info("DEBUG - age: {}, income: {}, value: {}",
                entity.getAge(), entity.getIncome(), entity.getValue());

        Map<String, Object>  processVariables = Map.of(
                "age", entity.getAge(),
                "income", entity.getIncome(),
                "value", entity.getValue(),
                "name", entity.getName()
        );

        var processDefinitionkey = "credit_proposal";
        var businesskey = entity.getId().toString();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionkey, businesskey, processVariables);
        String processInstanceId = processInstance.getId();
        entity.setProcessInstanceId(processInstanceId);

        log.info("[INFO] - Process Key ({}), Business Key ({}), Process Instance ID ('{}') ", processDefinitionkey,businesskey,processInstanceId);

        return mapper.entityToDtoResponse(entity);
    }

    public List<TaskResponseDTO> getPendingTasks() {
        return taskService.createTaskQuery()
                .processDefinitionKey("credit_proposal")
                .taskDefinitionKey("review_proposal")
                .list()
                .stream()
                .map(task -> {
                    String proposalId = runtimeService.createProcessInstanceQuery()
                            .processInstanceId(task.getProcessInstanceId())
                            .singleResult()
                            .getBusinessKey();
                    String name = (String) taskService.getVariable(task.getId(), "name");
                    Integer score = (Integer) taskService.getVariable(task.getId(), "score");
                    return new TaskResponseDTO(task.getId(), proposalId, name, score);
                })
                .toList();
    }

    public void approveTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }

        Map<String, Object> variables = Map.of("manualApproval", true);
        taskService.complete(taskId, variables);
        log.info("[SERVICE] Task {} approved", taskId);
    }

    public void rejectTask(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new TaskNotFoundException("Task not found: " + taskId);
        }

        Map<String, Object> variables = Map.of("manualApproval", false);
        taskService.complete(taskId, variables);
        log.info("[SERVICE] Task {} rejected", taskId);
    }
}
