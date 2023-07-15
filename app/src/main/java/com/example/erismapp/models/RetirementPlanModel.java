package com.example.erismapp.models;

public class RetirementPlanModel {

    private final String planName;
    private final String planType;
    private final double employerContributionRate;
    private final double employeeContributionRate;
    private final int vestingPeriod;
    private final double maxContributionLimit;
    private final double minContributionLimit;

    public RetirementPlanModel(
            String planName,
            String planType,
            double employerContributionRate,
            double employeeContributionRate,
            int vestingPeriod,
            double maxContributionLimit,
            double minContributionLimit
    ) {
        this.planName = planName;
        this.planType = planType;
        this.employerContributionRate = employerContributionRate;
        this.employeeContributionRate = employeeContributionRate;
        this.vestingPeriod = vestingPeriod;
        this.maxContributionLimit = maxContributionLimit;
        this.minContributionLimit = minContributionLimit;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanType() {
        return planType;
    }

    public double getEmployerContributionRate() {
        return employerContributionRate;
    }

    public double getEmployeeContributionRate() {
        return employeeContributionRate;
    }

    public int getVestingPeriod() {
        return vestingPeriod;
    }

    public double getMaxContributionLimit() {
        return maxContributionLimit;
    }

    public double getMinContributionLimit() {
        return minContributionLimit;
    }
}
