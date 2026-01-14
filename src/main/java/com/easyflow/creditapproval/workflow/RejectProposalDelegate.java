package com.easyflow.creditapproval.workflow;

import com.easyflow.creditapproval.entity.ProposalStatus;
import com.easyflow.creditapproval.repository.CreditProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rejectProposalDelegate")
@RequiredArgsConstructor
public class RejectProposalDelegate implements JavaDelegate {

    private final CreditProposalRepository repository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String businessKey = execution.getProcessBusinessKey();
        log.info("[WORKFLOW] Finalizing process: Rejecting proposal ID {}", businessKey);

        repository.findById(Long.valueOf(businessKey)).ifPresent(proposal -> {
            proposal.setStatus(ProposalStatus.REJECTED);
            repository.save(proposal);
        });
    }
}
