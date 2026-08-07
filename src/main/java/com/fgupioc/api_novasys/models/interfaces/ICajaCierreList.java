package com.fgupioc.api_novasys.models.interfaces;

import java.time.LocalDateTime;

public interface ICajaCierreList {
    Long getId();
    Double getOpeningAmount();
    Double getCashDifferenceAmount();
    String getCashRegisterName();
    LocalDateTime getClosedAt();
    Double getClosingAmount();
    String getStatus();
    Double getTotalPaymentsAmount();
    String getUserLastName();
    String getUserName();
}
