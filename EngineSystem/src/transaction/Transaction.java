package transaction;

public class Transaction {
    public enum TransactionType{INCOME,WITHDRAW};
    private int yaztime;
    private TransactionType type;
    private double amount;
    private double moneyBefore;
    private double getMoneyAfter;

    public Transaction(int yaztime, TransactionType type, double amount, double moneyBefore, double getMoneyAfter) {
        this.yaztime = yaztime;
        this.type = type;
        this.amount = amount;
        this.moneyBefore = moneyBefore;
        this.getMoneyAfter = getMoneyAfter;
    }

    public int getYaztime() {
        return yaztime;
    }

    public void setYaztime(int yaztime) {
        this.yaztime = yaztime;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMoneyBefore() {
        return moneyBefore;
    }

    public void setMoneyBefore(double moneyBefore) {
        this.moneyBefore = moneyBefore;
    }

    public double getGetMoneyAfter() {
        return getMoneyAfter;
    }

    public void setGetMoneyAfter(double getMoneyAfter) {
        this.getMoneyAfter = getMoneyAfter;
    }
}
