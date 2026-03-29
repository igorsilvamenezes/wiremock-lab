package com.lab.wiremock.model;

import java.math.BigDecimal;

public class TransferRequest {

    private String sourceAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private String description;

    public TransferRequest() {}

    public TransferRequest( String sourceAccount, String destinationAccount,
                            BigDecimal amount, String description) {
        this.sourceAccount      = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount             = amount;
        this.description        = description;
    }

    public String getSourceAccount() {
        return sourceAccount; 
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount; 
    }

    public String getDestinationAccount() { 
        return destinationAccount; 
    }

    public void setDestinationAccount(String destinationAccount) { 
        this.destinationAccount = destinationAccount; 
    }
    
    public BigDecimal getAmount() { 
        return amount; 
    }

    public void setAmount(BigDecimal amount) { 
        this.amount = amount; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }
}