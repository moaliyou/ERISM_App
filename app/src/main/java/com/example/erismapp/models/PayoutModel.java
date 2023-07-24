package com.example.erismapp.models;

public class PayoutModel {

    private int payoutId;
    private String employeeName;
    private String benefitType;
    private double payoutAmount;
    private String payoutDate;

    public PayoutModel(
            int payoutId,
            String employeeName,
            String benefitType,
            double payoutAmount,
            String payoutDate
    ) {
        this.payoutId = payoutId;
        this.employeeName = employeeName;
        this.benefitType = benefitType;
        this.payoutAmount = payoutAmount;
        this.payoutDate = payoutDate;
    }

    public int getPayoutId() {
        return payoutId;
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
