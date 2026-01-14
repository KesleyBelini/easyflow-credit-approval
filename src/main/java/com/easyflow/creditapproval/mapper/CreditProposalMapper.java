package com.easyflow.creditapproval.mapper;

import com.easyflow.creditapproval.dto.CreateCreditProposalRequest;
import com.easyflow.creditapproval.dto.CreditProposalResponse;
import com.easyflow.creditapproval.entity.CreditProposal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditProposalMapper {

    @Mapping(source = "createdDate", target = "createdAt")
    CreditProposalResponse entityToDtoResponse(CreditProposal entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "processInstanceId", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "age", target = "age")
    @Mapping(source = "income", target = "income")
    @Mapping(source = "value", target = "value")
    CreditProposal dtoRequestToEntity(CreateCreditProposalRequest dtoRequest);
}
