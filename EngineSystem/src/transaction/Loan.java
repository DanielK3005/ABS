package transaction;

import customer.Person;
import dto.NotificationDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Loan implements Cloneable{

    public enum Status{NEW,ACTIVE,PENDING,RISK,FINISHED}

    private String id;
    private Person owner;
    private Status status;
    private String category;
    private int capital;
    private int totalyaz; //the amount of yaz to close the deal
    private int timetopay; //number of yaz to pay
    private int activestatusyaz;//when the loan became active
    private int finishedyaz;
    List<Lender> lenderList;
    private int interest;
    List<NotificationDTO> Notifications;

    public Loan(String id,Person owner,String category,int amount, int totalyaz,int timetopay,int interest){
        this.id=id;
        this.owner=owner;
        this.status=Status.NEW;
        this.category=category;
        this.lenderList =new ArrayList<>();
        this.Notifications=new ArrayList<>();
        this.capital =amount;
        this.totalyaz=totalyaz;
        this.timetopay=timetopay;
        this.interest=interest;
        this.activestatusyaz=0;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Loan clone;
        clone=(Loan)super.clone();
        clone.setOwner(new Person(this.getOwner().getName(), this.getOwner().getBalance()));
        clone.lenderList =new ArrayList<>();
        for(Lender val: this.lenderList)
            clone.lenderList.add((Lender)val.clone());
        return clone;
    }
    public void setOwner(Person owner) {
        this.owner = owner;
    }
    public Person getOwner() {
        return owner;
    }

    public int getInterest() {
        return interest;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getTimetopay() {
        return timetopay;
    }

    public double getTotalAmountToPay(){
        double helper=this.capital*((double)this.interest/100);
        return this.capital+helper;
    }

    public Status getStatus() {
        return status;
    }

    public int getActivestatusyaz() {
        return activestatusyaz;
    }

    public String getCategory() {
        return category;
    }

    public int getTotalyaz() {
        return totalyaz;
    }

    public List<Lender> getLoanerList() {
        return lenderList;
    }
    public int getnextPaymentTime(){
        if(activestatusyaz==0)
            return 0;
        int paymentsize=lenderList.get(0).paymentlog.size();
        if(paymentsize==0)
            return  activestatusyaz+timetopay;
        else
            return timetopay+(lenderList.get(0).paymentlog.get(paymentsize-1).getCurrentyaz());
    }
    public double getFullDebt(){
        return this.lenderList.stream().mapToDouble(Lender::getDebt).sum();
    }

    public int getCapital() {
        return capital;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return Objects.equals(id, loan.id) && Objects.equals(owner, loan.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner);
    }

    public int getFinishedyaz() {
        return finishedyaz;
    }

    public void setFinishedyaz(int finishedyaz) {
        this.finishedyaz = finishedyaz;
    }
    public double AmountOfMoneyToMakeActive(){
        return (this.capital-this.calculateTotalLoanerMoney());
    }

    public void setNewLoaner(Lender lender) {
        this.lenderList.add(lender);
    }

    public double leftToPayForTheLoan(){
        return this.getTotalAmountToPay()-this.TotalPaidSoFarToLoaners();
    }

    public void updateStatus(int currentyaz) {
        if(!lenderList.isEmpty()) {
            if(status == Status.FINISHED)
                return;
            if (status == Status.NEW) {
                status = Status.PENDING;
            }
            if (capital == this.calculateTotalLoanerMoney() && status != Status.FINISHED) {
                if(activestatusyaz!=0) {
                    if (status == Status.RISK) {
                        if (this.getFullDebt()==0)
                            status = Status.ACTIVE;
                    }
                    if(currentyaz>this.getnextPaymentTime()){
                        status = Status.RISK;
                        addAllDebt(this.getnextPaymentTime());
                    }
                }else {
                    status = Status.ACTIVE;
                    activestatusyaz = currentyaz;
                    if(this.Notifications.size()==0)
                        this.Notifications.add(new NotificationDTO(this.id,this.getnextPaymentTime(),this.TotalOnePaymentToAllLoaners()));

                }
            }
            if(this.getFullDebt()!=0)
                status=Status.RISK;
            if(activestatusyaz!=0 && this.getTotalAmountToPay()==this.TotalPaidSoFarToLoaners()){
                status = Status.FINISHED;
                this.finishedyaz = currentyaz;
            }
        }
    }

    public void addAllDebt(int paytime){
        for (Lender i : this.getLoanerList()) {
            if(i.getDebt()<i.leftToPay()) {
                double oneintrest = i.getPayEveryYazPeriod() * ((double) this.getInterest() / 100);
                double onepay = i.getPayEveryYazPeriod() + oneintrest;
                i.setPaymentlog(new Payment(i.getPayEveryYazPeriod(), oneintrest, paytime, false));
                i.setDebt(i.getDebt() + onepay);
                this.Notifications.add(new NotificationDTO(this.id, this.getnextPaymentTime(), this.TotalOnePaymentToAllLoaners()));
            }
        }
    }

    public double TotalOnePaymentToAllLoaners(){
        double OneInterest,sum=0;
        for(Lender val: this.lenderList){
            OneInterest= val.getPayEveryYazPeriod()*((double) interest/100);
            sum=sum+ val.getPayEveryYazPeriod()+OneInterest;
        }
        return sum;
    }
    public double TotalPaidSoFarToLoaners(){
        return this.lenderList.stream().mapToDouble(x->x.getTotalPayback()).sum();
    }

    public boolean isAllPaymentsWentThrough(){
        boolean validpay=true;
        for(Lender val: lenderList) {
            for (Payment i : val.getPaymentlog())
                if (!i.isPaidSuccesfully())
                    validpay = false;
        }
        return validpay;
    }

    public double calculateTotalLoanerMoney()
    {
        return this.lenderList.stream().mapToDouble(Lender::getAmount).sum();
    }

    public List<NotificationDTO> getNotifications() {
        return Notifications;
    }

    public void setNotification(NotificationDTO notification) {
        Notifications.add(notification);
    }

    @Override
    public String toString() {
        return "Loan Details:\nName: "+this.id+"\nOwner: "+ this.owner.getName()+"\nCapital: "+this.capital+
                "\nStatus: "+this.status+ "\nCategory: "+this.category+"\nTotal yaz: "+this.totalyaz +
                "\nPay every "+ this.timetopay +" yaz" + "\nInterest per payment: "+ this.interest+ "%" + "\nTotal amount to pay back the loaners: "+ String.format("%,.1f",this.getTotalAmountToPay());
    }
}
