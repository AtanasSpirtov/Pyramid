package com.example.pyramid.model;


import com.example.pyramid.model.enums.bstEnums.BonusStatus;

import java.time.LocalDateTime;

import javax.persistence.Entity;

@Entity
public class BonusReport extends _BaseEntity {

    BonusStatus status;
    String statusDetails;
    LocalDateTime processTime;

    public BonusStatus getStatus() {
        return status;
    }

    public void setStatus(BonusStatus status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public LocalDateTime getProcessTime() {
        return processTime;
    }

    public void setProcessTime(LocalDateTime processTime) {
        this.processTime = processTime;
    }

}
