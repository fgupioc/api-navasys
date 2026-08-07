package com.fgupioc.api_novasys.models.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ITicketCierreCaja {
    LocalDateTime getCreatedAt();
    String getCashRegisterName();
    String getCashRegisterDescription();
    String getOpenedAt();
    String getClosedAt();
    String getHoras();
    String getUserName();
    String getUserDocument();
    String getUserRole();
    String getTurno();
    Integer getSalesCount();
    BigDecimal getTotalPaymentsAmount();
    BigDecimal getOpeningAmount();
    BigDecimal getExecutiveCashAmount();
    BigDecimal getCashInAmount();
    BigDecimal getCashOutAmount();
    BigDecimal getExpectedCashAmount();
    BigDecimal getCountedCashAmount();
    BigDecimal getCashDifferenceAmount();
    String getNotes();
    String getReportGeneratedAt();
}
