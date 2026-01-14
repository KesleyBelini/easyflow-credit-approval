package com.easyflow.creditapproval.repository;

import com.easyflow.creditapproval.entity.CreditProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditProposalRepository extends JpaRepository<CreditProposal, Long> {
}
