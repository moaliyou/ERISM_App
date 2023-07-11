package com.example.erismapp.models;

public class PayoutModel {

    private final String employeeName;
    private final String benefitType;
    private final double payoutAmount;
    private final String payoutDate;

    public PayoutModel(String employeeName, String benefitType, double payoutAmount, String payoutDate) {
        this.employeeName = employeeName;
        this.benefitType = benefitType;
        this.payoutAmount = payoutAmount;
        this.payoutDate = payoutDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public double getPayoutAmount() {
        return payoutAmount;
    }

    public String getPayoutDate() {
        return payoutDate;
    }
}
