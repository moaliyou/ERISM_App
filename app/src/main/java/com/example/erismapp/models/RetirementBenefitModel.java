package com.example.erismapp.models;

public class RetirementBenefitModel {

    private final int employeeId;
    private String employeeName;
    private String benefitType;
    private int planId;
    private double contributionAmount;
    private String contributionFrequency;
    private String benefitStartDate;
    private String benefitEndDate;
    private int retirementBenefitId;

    public RetirementBenefitModel(
            int employeeId,
            String employeeName
    ) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }

    public RetirementBenefitModel(
            int retirementBenefitId,
            int employeeId,
            String benefitType,
            double contributionAmount,
            String contributionFrequency,
            int planId,
            String benefitStartDate,
            String benefitEndDate

    ) {
        this.retirementBenefitId = retirementBenefitId;
        this.employeeId = employeeId;
        this.benefitType = benefitType;
        this.contributionAmount = contributionAmount;
        this.contributionFrequency = contributionFrequency;
        this.planId = planId;
        this.benefitStartDate = benefitStartDate;
        this.benefitEndDate = benefitEndDate;
    }

    public int getRetirementBenefitId() {
        return retirementBenefitId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getBenefitType() {
        return benefitType;
    }

    public double getContributionAmount() {
        return contributionAmount;
    }

    public String getContributionFrequency() {
        return contributionFrequency;
    }

    public String getBenefitStartDate() {
        return benefitStartDate;
    }

    public String getBenefitEndDate() {
        return benefitEndDate;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getPlanId() {
        return planId;
    }

}
