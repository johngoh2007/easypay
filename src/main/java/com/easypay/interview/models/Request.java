package com.easypay.interview.models;

import java.time.LocalDate;

public class Request {
    private Long amount;
    private SubscriptionTypes type;
    private String dayOf;
    private String startDate;
    private String endDate;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public SubscriptionTypes getType() {
        return type;
    }

    public void setType(SubscriptionTypes type) {
        this.type = type;
    }

    public String getDayOf() {
        return dayOf;
    }

    public void setDayOf(String dayOf) {
        this.dayOf = dayOf;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Request{" +
                "amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", dayOf='" + dayOf + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
