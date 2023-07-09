package com.example.erismapp.models;

public class RetirementBenefitModel {

    private final String employeeName;
    private final String benefitType;
    private final double contributionAmount;
    private final String contributionFrequency;
    private final String retirementPlan;
    private final String benefitStartDate;
    private final String benefitEndDate;
    private int retirementBenefitId;

    public RetirementBenefitModel(
            int retirementBenefitId, String employeeName, String benefitType,
            double contributionAmount, String contributionFrequency, String retirementPlan,
            String benefitStartDate, String benefitEndDate
    ) {
        this.retirementBenefitId = retirementBenefitId;
        this.employeeName = employeeName;
        this.benefitType = benefitType;
        this.contributionAmount = contributionAmount;
        this.contributionFrequency = contributionFrequency;
        this.retirementPlan = retirementPlan;
        this.benefitStartDate = benefitStartDate;
        this.benefitEndDate = benefitEndDate;
    }

    public RetirementBenefitModel(
            String employeeName, String benefitType,
            double contributionAmount, String contributionFrequency, String retirementPlan,
            String benefitStartDate, String benefitEndDate
    ) {
        this.employeeName = employeeName;
        this.benefitType = benefitType;
        this.contributionAmount = contributionAmount;
        this.contributionFrequency = contributionFrequency;
        this.retirementPlan = retirementPlan;
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

    public String getRetirementPlan() {
        return retirementPlan;
    }

    @Override
    public String toString() {
        return "RetirementBenefitModel{" +
                "retirementBenefitId=" + retirementBenefitId +
                ", employeeName='" + employeeName + '\'' +
                ", benefitType='" + benefitType + '\'' +
                ", contributionAmount=" + contributionAmount +
                ", contributionFrequency='" + contributionFrequency + '\'' +
                ", benefitStartDate='" + benefitStartDate + '\'' +
                ", benefitEndDate='" + benefitEndDate + '\'' +
                '}';
    }
}
