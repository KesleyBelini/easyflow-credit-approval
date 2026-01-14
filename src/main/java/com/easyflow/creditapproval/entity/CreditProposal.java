package com.easyflow.creditapproval.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name ="credit_proposal")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name ="age")
    private Integer age;

    @Column(name="income")
    private BigDecimal income;

    @Column(name ="value")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    @Column(name ="status")
    private ProposalStatus status;

    @Column(name ="score")
    private Integer score;

    @Column(name ="process_instance_id")
    private String processInstanceId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;
}
