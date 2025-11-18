package com.tasya.smartbudget;
public class Transaction {

    public boolean isIncome;
    public String day;
    public String month;
    public String category;
    public String amount;

    public Transaction(boolean isIncome, String day, String month, String category, String amount) {
        this.isIncome = isIncome;
        this.day = day;
        this.month = month;
        this.category = category;
        this.amount = amount;
    }
}
