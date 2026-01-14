package com.easyflow.creditapproval.workflow;

import com.easyflow.creditapproval.entity.CreditProposal;
import com.easyflow.creditapproval.repository.CreditProposalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component("calculateScoreDelegate")
@Slf4j
@RequiredArgsConstructor
public class CalculateScoreDelegate implements JavaDelegate {
    private static final int BASE_SCORE = 600;
    private final CreditProposalRepository repository;

    @Override
    public void execute(DelegateExecution execution) {
        Integer age = getInteger(execution, "age");
        BigDecimal income = getBigDecimal(execution, "income");
        BigDecimal value = getBigDecimal(execution, "value");

        int score = BASE_SCORE;
        if (income != null) {
            if (income.compareTo(new BigDecimal("6000")) >= 0) {
                score += 100;
            } else if (income.compareTo(new BigDecimal("3000")) >= 0 && income.compareTo(new  BigDecimal("6000")) < 0) {
                score += 50;
            } else if (income.compareTo(new  BigDecimal("2000")) < 0) {
                score -= 80;
            }
        }

        if (age != null) {
            if (age >= 25 && age <= 60) {
                score += 30;
            } else if (age < 21) {
                score -= 50;
            }
        }

        if (income != null && value != null) {
            BigDecimal lowRiskLimit = income.multiply(new BigDecimal("2"));
            if (value.compareTo(lowRiskLimit) <= 0) {
                score += 50; // Bônus de bom pagador/baixo risco
            }

            BigDecimal limit = income.multiply(new BigDecimal("6"));
            if (value.compareTo(limit) > 0) {
                score -= 60;
            }
        }

        score = Math.max(0, Math.min(1000, score));
        log.info("[WORKFLOW] Final score: {}", score);
        execution.setVariable("score", score);

        String businesskey = execution.getProcessBusinessKey();
        Optional<CreditProposal> entity = repository.findById(Long.valueOf(businesskey));

        if (entity.isPresent()) {
            CreditProposal creditProposal = entity.get();
            creditProposal.setScore(score);
            repository.save(creditProposal);
            log.info("[WORKFLOW] Score {} saved for proposal ID: {}", score, businesskey);
        } else {
            log.error("[WORKFLOW] Proposal not found for ID: {}", businesskey);
            throw new BpmnError("PROPOSAL_NOT_FOUND", "Proposal ID " + businesskey + " not found");
        }

    }

    private BigDecimal getBigDecimal(DelegateExecution execution, String varName) {
        Object value = execution.getVariable(varName);
        return switch (value) {
            case null -> null;
            case BigDecimal bd -> bd;
            case Number n -> BigDecimal.valueOf(n.doubleValue());
            case String s -> new BigDecimal(s);
            default ->
                    throw new IllegalArgumentException("Variable '" + varName + "' is not a number: " + value.getClass());
        };
    }

    private Integer getInteger(DelegateExecution execution, String varName) {
        Object value = execution.getVariable(varName);
        return switch (value) {
            case null -> null;
            case Integer i -> i;
            case Number n -> n.intValue();
            case String s -> Integer.valueOf(s);
            default ->
                    throw new IllegalArgumentException("Variable '" + varName + "' is not an integer: " + value.getClass());
        };
    }
}
