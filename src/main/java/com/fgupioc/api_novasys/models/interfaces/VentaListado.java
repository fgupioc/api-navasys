package com.fgupioc.api_novasys.models.interfaces;

public interface VentaListado {
    Long getId();
    String getUuid();
    String getCode();
    String getSaleDate();
    String getSaleTime();
    String getCustomer();
    String getDocumentType();
    String getDocumentNumber();
    String getPaymentMethod();
    String getPaymentMethodStyle();
    Double getTotal();
    String getStatus();
    String getStatusStyle();
    String getCashRegister();
}
