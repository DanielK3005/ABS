package engine;

import customer.Person;
import database.DataBase;
import dto.*;
import exception.*;
import schema.AbsCustomer;
import schema.AbsDescriptor;
import schema.AbsLoan;
import transaction.Lender;
import transaction.Loan;
import transaction.Payment;
import transaction.Transaction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Engine implements EngineFunctions {

    public static final String XML_STRUCTURE="schema";

    AbsDescriptor fileobj;
    List<Person> customers;
    List<String> categories;
    List<Loan> loans;


    public Engine(){
        this.customers=new ArrayList<>();
        this.loans=new ArrayList<>();
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public List<Loan> getLoans() {
        return loans;
    }

    @Override
    public void UpdateLoansStatus(int currentyaz) {
        this.loans.forEach(x->x.updateStatus(currentyaz));
    }

    @Override
    public List<LoanDTO> SearchMatchedLoans(PersonDTO p,DataBase data,Integer interest,List<String> categorys,Integer minYaz,Integer maxOpen) {
        List<LoanDTO> copy=data.getLoansDTO();
        Stream<LoanDTO> s=null;
        s=copy.stream().filter(x->(x.getStatus()== Loan.Status.NEW) || (x.getStatus()== Loan.Status.PENDING));
        s=s.filter(x->!x.getOwner().getName().equals(p.getName()));
        s=CategoryFilter(categorys,s);
        s=InterestFilter(interest,s);
        s=minYazFilter(minYaz,s);
        s=maxOpenFilter(maxOpen,s);
        return s.collect(Collectors.toList());
    }

    @Override
    public Stream<LoanDTO>  InterestFilter(Integer interest,Stream<LoanDTO> add){
        if(interest!=null){
            add=add.filter(x->x.getInterest()>interest);
        }
        return add;
    }
    @Override
    public Stream<LoanDTO>  minYazFilter(Integer minYaz,Stream<LoanDTO> add){
        if(minYaz!=null){
            add=add.filter(x->x.getTotalYaz()>minYaz);
        }
        return add;
    }
    @Override
    public Stream<LoanDTO>  maxOpenFilter(Integer maxOpen,Stream<LoanDTO> add){
        if(maxOpen!=null){
            add=add.filter(x->(x.getOwner().getTotalSumLoans()-x.getOwner().getTotalFinishedLoans())<maxOpen);
        }
        return add;
    }
    @Override
    public Stream<LoanDTO> CategoryFilter(List<String> categories,Stream<LoanDTO> add){
        if(categories.size()>0){
            add=add.filter(x->categories.contains(x.getCategory()));
        }
        return add;
    }

    @Override
    public boolean MakeAwithdraw(PersonDTO pick,int amount,int currentyaz) throws MoreThanYouGotException {
        Person match=this.customers.stream().filter(x->x.getName().equalsIgnoreCase(pick.getName())).findAny().orElse(null);
       if (match!=null){
           match.Withdraw(amount, currentyaz);
           return true;
       }else
           return false;
    }
    @Override
    public boolean InsertIncome(PersonDTO p, int amount, int currentyaz){
        Person match=this.customers.stream().filter(x->x.getName().equalsIgnoreCase(p.getName())).findAny().orElse(null);
        if (match!=null){
            match.Income(amount, currentyaz);
            return true;
        }else
            return false;
    }

    @Override
    public void setLoan(Loan loan) {
        this.loans.add(loan);
    }

    public List<Person> getCustomers() {
        return customers;
    }

    @Override
    public void loadFiletoDescriptor(File in) throws JAXBException, IOException, NullPointerException {
            InputStream inputStream = new FileInputStream(in);
            JAXBContext dc = JAXBContext.newInstance(XML_STRUCTURE);
            Unmarshaller u = dc.createUnmarshaller();
            fileobj = (AbsDescriptor) u.unmarshal(inputStream);
            inputStream.close();
            isFileLoaded();
    }
    @Override
    public void CheckValidDataFile() throws CategoryMismatchException, LoanPersonMismatchException, NotDividedCorrectlyException, DuplicatePersonException {
        CheckCategories();
        CheckLoanPersons();
        CheckLoanNumbers();
        CheckCustomersList();
    }

    private void CheckCustomersList() throws DuplicatePersonException {
        for (AbsCustomer val : fileobj.getAbsCustomers().getAbsCustomer()) {
            if (Collections.frequency(fileobj.getAbsCustomers().getAbsCustomer(), val.getName()) > 1) {
                throw new DuplicatePersonException(val.getName());
            }
        }
    }

    private void CheckLoanNumbers() throws NotDividedCorrectlyException {
        for (AbsLoan val : fileobj.getAbsLoans().getAbsLoan()) {
            if ((val.getAbsTotalYazTime() % val.getAbsPaysEveryYaz()) != 0) {
                throw new NotDividedCorrectlyException(val.getId(),val.getAbsTotalYazTime(),val.getAbsPaysEveryYaz());
            }
        }
    }

    private void CheckLoanPersons() throws LoanPersonMismatchException {
        for (AbsLoan val : fileobj.getAbsLoans().getAbsLoan()) {
            boolean found = false;
            for (AbsCustomer v : fileobj.getAbsCustomers().getAbsCustomer()) {
                if (val.getAbsOwner().equalsIgnoreCase(v.getName()))
                    found = true;
            }
            if (found == false) {
                throw new LoanPersonMismatchException(val.getAbsOwner(),val.getId());
            }
        }
    }

    private void CheckCategories() throws CategoryMismatchException {
        for (AbsLoan val : fileobj.getAbsLoans().getAbsLoan()) {
            if (!(fileobj.getAbsCategories().getAbsCategory().contains(val.getAbsCategory()))) {
                throw new CategoryMismatchException(val.getAbsOwner(),val.getAbsCategory());
            }
        }
    }
    @Override
    public void isFileLoaded() throws NullPointerException{
        if(fileobj==null)
            throw new NullPointerException();
    }

    @Override
    public void MakeAssignment(PersonDTO loaner, double amount, List<LoanDTO> picked, Integer ownerShip ,int currentyaz) throws NullPointerException, LastLoanException {
        double paydivided = amount / picked.size(); //not divided by zero because picked isnt empty
        Person current = FindPersonInCustomerList(loaner.getName());
        List<LoanDTO> sorted = picked.stream().sorted(Comparator.comparingDouble(LoanDTO::getAmountOfMoneyToMakeActive)).collect(Collectors.toList());
        if (current == null)
            throw new NullPointerException();
        for (int i = 0; i < sorted.size(); i++) {
            for (Loan loan : this.loans) {
                double makeactive = loan.AmountOfMoneyToMakeActive();
                if (sorted.get(i).getId().equalsIgnoreCase(loan.getId())) {
                    Lender exist = loan.getLoanerList().stream().filter(x -> x.getOwner().equals(current)).findAny().orElse(null);
                    if (makeactive < paydivided) {
                        amount=MakeAssignmentHelper(amount,current,exist,ownerShip,currentyaz,loan,makeactive);
                            if (i < sorted.size() - 1)
                                paydivided = amount / (sorted.size() - (i + 1));
                            else if (amount > 0) {
                                this.UpdateLoansStatus(currentyaz);
                                throw new LastLoanException(paydivided, makeactive, loan);
                            }
                        }else {
                        amount=MakeAssignmentHelper(amount,current,exist,ownerShip,currentyaz,loan,paydivided);
                    }
                    loan.updateStatus(currentyaz);
                    }
                }
            }
    }

    private double MakeAssignmentHelper(double amount,Person current,Lender exist,Integer ownerShip,int currentyaz, Loan loan, double amountToAssign){
        if (exist == null) {
            Lender newloaner = new Lender(current, amountToAssign, loan.getInterest());
            newloaner.setPayEveryYazPeriod(loan.getTotalyaz(), loan.getTimetopay());
            newloaner.setOwnershipPercent(loan.getCapital());
            current.setAction(currentyaz, Transaction.TransactionType.WITHDRAW, amountToAssign, current.getBalance(), current.getBalance() - amountToAssign);
            current.setBalance(current.getBalance() - amountToAssign);
            if (newloaner.getOwnershipPercent() <= ownerShip)
                loan.setNewLoaner(newloaner);
            else {
                newloaner.setAmount(loan.getCapital() * ((double) ownerShip / 100));
                amountToAssign = newloaner.getAmount();
                loan.setNewLoaner(newloaner);
            }
            amount = amount - amountToAssign;
        } else {
            if (exist.getOwnershipPercent() <= ownerShip) {
                Double diff = ownerShip - exist.getOwnershipPercent();
                Double addMoney = loan.getCapital() * (diff / 100);
                if (addMoney <= amountToAssign) {
                    exist.setAmount(exist.getAmount() + addMoney);
                    current.setAction(currentyaz, Transaction.TransactionType.WITHDRAW, addMoney, current.getBalance(), current.getBalance() - addMoney);
                    current.setBalance(current.getBalance() - addMoney);
                    amount = amount - addMoney;
                } else {
                    exist.setAmount(exist.getAmount() + amountToAssign);
                    current.setAction(currentyaz, Transaction.TransactionType.WITHDRAW, amountToAssign, current.getBalance(), current.getBalance() - amountToAssign);
                    current.setBalance(current.getBalance() - amountToAssign);
                    amount = amount - amountToAssign;
                }
            }
        }
        return amount;
    }
    @Override
    public void PaymentForActive(LoanDTO activeDTO,int currentyaz) throws NotEnoughMoneyException {
        Loan pay=this.loans.stream().filter(x->x.getId().equalsIgnoreCase(activeDTO.getId())).findAny().get();
        if(currentyaz<= pay.getnextPaymentTime()) {
            if (pay.TotalOnePaymentToAllLoaners() <= pay.getOwner().getBalance()) {
                for (Lender i : pay.getLoanerList()) {
                    if (i.getTotalPayback() < i.getTotalAmountToPay()) {
                        double oneintrest = i.getPayEveryYazPeriod() * ((double) pay.getInterest() / 100);
                        double onepay = i.getPayEveryYazPeriod() + oneintrest;
                        i.setPaymentlog(new Payment(i.getPayEveryYazPeriod(), oneintrest, currentyaz, true));
                        i.setTotalPayback(i.getTotalPayback() + onepay);
                        pay.setNotification(new NotificationDTO(pay.getId(), pay.getnextPaymentTime(), pay.TotalOnePaymentToAllLoaners()));
                        pay.getOwner().setAction(currentyaz, Transaction.TransactionType.WITHDRAW, onepay, pay.getOwner().getBalance(), pay.getOwner().getBalance() - onepay);
                        pay.getOwner().setBalance(pay.getOwner().getBalance() - onepay);
                        i.getOwner().setAction(currentyaz, Transaction.TransactionType.INCOME, onepay, i.getOwner().getBalance(), i.getOwner().getBalance() + onepay);
                        i.getOwner().setBalance(i.getOwner().getBalance() + onepay);
                    }
                }
                }
                else{
                    if(currentyaz== pay.getnextPaymentTime()) {
                        pay.addAllDebt(currentyaz);
                    }
                    pay.updateStatus(currentyaz);
                    throw new NotEnoughMoneyException(pay.getId(), pay.getStatus().name(),pay.TotalOnePaymentToAllLoaners(),pay.getOwner().getBalance());
                }
        }
        pay.updateStatus(currentyaz);
    }
    @Override
    public void PaymentForRisk(LoanDTO riskDTO,Double amount,int currentyaz) throws ExtraPaymentMoneyException {
        Loan pay=this.loans.stream().filter(x->x.getId().equalsIgnoreCase(riskDTO.getId())).findAny().get();
        for (Lender a : pay.getLoanerList()) {
            if(amount==0.0)
                break;
            if (amount >= a.getDebt()) {
                a.setTotalPayback(a.getTotalPayback() + a.getDebt());
                double onepayment= a.getDebt()/(1+((double)pay.getInterest()/100));
                double intrestamount= onepayment*((double)pay.getInterest()/100);
                a.setPaymentlog(new Payment(onepayment,intrestamount,currentyaz,true));
                pay.getOwner().setAction(currentyaz, Transaction.TransactionType.WITHDRAW, a.getDebt(), pay.getOwner().getBalance(), pay.getOwner().getBalance() - a.getDebt());
                pay.getOwner().setBalance(pay.getOwner().getBalance() - a.getDebt());
                a.getOwner().setAction(currentyaz, Transaction.TransactionType.INCOME, a.getDebt(), a.getOwner().getBalance(), a.getOwner().getBalance() + a.getDebt());
                a.getOwner().setBalance(a.getOwner().getBalance() + a.getDebt());
                amount=amount-a.getDebt();
                a.setDebt(0);
            } else {
                a.setTotalPayback(a.getTotalPayback() + amount);
                double onepayment= amount/(1+((double)pay.getInterest()/100));
                double intrestamount= onepayment*((double)pay.getInterest()/100);
                a.setPaymentlog(new Payment(onepayment,intrestamount,currentyaz,true));
                pay.getOwner().setAction(currentyaz, Transaction.TransactionType.WITHDRAW, amount, pay.getOwner().getBalance(), pay.getOwner().getBalance() - amount);
                pay.getOwner().setBalance(pay.getOwner().getBalance() - amount);
                a.getOwner().setAction(currentyaz, Transaction.TransactionType.INCOME, amount, a.getOwner().getBalance(), a.getOwner().getBalance() + amount);
                a.getOwner().setBalance(a.getOwner().getBalance() + amount);
                a.setDebt(a.getDebt()-amount);
                amount=0.0;
            }
        }
        if(pay.getFullDebt()==0&& pay.TotalPaidSoFarToLoaners()<pay.getTotalAmountToPay())
            pay.setNotification(new NotificationDTO(pay.getId(),pay.getnextPaymentTime(),pay.TotalOnePaymentToAllLoaners()));
        else
            pay.setNotification(new NotificationDTO(pay.getId(),pay.getnextPaymentTime(),pay.getFullDebt()));
        if(amount>0.0){
            pay.updateStatus(currentyaz);
            throw new ExtraPaymentMoneyException(pay.getId(),amount,riskDTO.getFullDebt());
        }
        pay.updateStatus(currentyaz);
    }
    @Override
    public void PayAll(LoanDTO loanDTO,int currentyaz) throws NotEnoughMoneyException {
        Loan data=this.loans.stream().filter(x->x.getId().equalsIgnoreCase(loanDTO.getId())).findAny().get();
        if(data.leftToPayForTheLoan()<=data.getOwner().getBalance()){
            data.getOwner().setAction(currentyaz, Transaction.TransactionType.WITHDRAW,data.leftToPayForTheLoan(),data.getOwner().getBalance(),data.getOwner().getBalance()-data.leftToPayForTheLoan());
            data.getOwner().setBalance(data.getOwner().getBalance() - data.leftToPayForTheLoan());
            for (Lender val: data.getLoanerList()){
                val.getOwner().setAction(currentyaz, Transaction.TransactionType.INCOME, val.leftToPay(),val.getOwner().getBalance(),val.getOwner().getBalance()+ val.leftToPay());
                val.getOwner().setBalance(val.getOwner().getBalance() + val.leftToPay());
                double onepayment= val.leftToPay()/(1+((double)data.getInterest()/100));
                double intrestamount= onepayment*((double)data.getInterest()/100);
                val.setPaymentlog(new Payment(onepayment, intrestamount,currentyaz,true));
                val.setTotalPayback(val.getTotalPayback()+ val.leftToPay());
                val.setDebt(0.0);
            }
            data.updateStatus(currentyaz);
        }else {
             data.updateStatus(currentyaz);
            throw new NotEnoughMoneyException(data.getId(), data.getStatus().name(),data.leftToPayForTheLoan(),data.getOwner().getBalance());
        }
    }

    @Override
    public void LoadFileDataToEngine() throws NullPointerException{
        if(fileobj==null)
            throw new NullPointerException();
        this.categories=new ArrayList<>(fileobj.getAbsCategories().getAbsCategory());
        this.customers=new ArrayList<>();
        this.loans=new ArrayList<>();
        for(AbsCustomer val: fileobj.getAbsCustomers().getAbsCustomer()){
            this.customers.add(new Person(val.getName(), val.getAbsBalance()));
        }
        for(AbsLoan v: fileobj.getAbsLoans().getAbsLoan()){
            Person owner=FindPersonInCustomerList(v.getAbsOwner());
            this.loans.add(new Loan(v.getId(),owner,v.getAbsCategory(),v.getAbsCapital(),v.getAbsTotalYazTime(),v.getAbsPaysEveryYaz(),v.getAbsIntristPerPayment()));

        }
    }
    @Override
    //the file is checked so all the persons in the loan are in the customers list, this method must find one.
    public Person FindPersonInCustomerList(String name) {
        for(Person val:this.customers){
            if(val.getName().equals(name))
                return val;
        }
        return null;
    }
    @Override
    public List<PersonDTO> getCustomerDTO(){
        List<PersonDTO> customersDTO=new ArrayList<>();
        for(Person val:this.customers)
        {
            PersonDTO customer=new PersonDTO(val.getName(), val.getBalance(), val.getActions());
            Integer[] TotalLoans=val.getTotalStatusCustomerLoans(this.loans);
            customer.setTotalNewLoans(TotalLoans[0]); //New Status  total in the first index
            customer.setTotalPendingLoans(TotalLoans[1]); //Pending Status total in the second index
            customer.setTotalActiveLoans(TotalLoans[2]); //Active Status total in the third index
            customer.setTotalRiskLoans(TotalLoans[3]); //Risk Status total in the forth index
            customer.setTotalFinishedLoans(TotalLoans[4]); //Finished Status total in the fifth index
            customer.setTotalSumLoans(Arrays.stream(TotalLoans).mapToInt(Integer::intValue).sum());
            Integer[] TotalLenders= val.getTotalStatusCustomerLenders(this.loans);
            customer.setTotalNewLenders(TotalLenders[0]);
            customer.setTotalPendingLenders(TotalLenders[1]);
            customer.setTotalActiveLenders(TotalLenders[2]);
            customer.setTotalRiskLenders(TotalLenders[3]);
            customer.setTotalFinishedLenders(TotalLenders[4]);
            customersDTO.add(customer);

        }
        return customersDTO;
    }

    @Override
    public DataBase getDataBaseDTO(){
        List<PersonDTO> customerDTO = getCustomerDTO();
        List<LoanDTO> loansDTO = new ArrayList<>();
        for (Loan val : this.loans) {
            LoanDTO loanDTO = new LoanDTO(val.getId(), val.getStatus(), val.getCategory(), val.getCapital(), val.getTotalyaz(), val.getTimetopay(), val.getActivestatusyaz(), val.getFinishedyaz(), val.getInterest(),val.getNotifications());
            loansDTO.add(loanDTO);
            for (PersonDTO p : customerDTO) {
                if (val.getOwner().getName().equals(p.getName()))
                    loanDTO.setOwner(p);
            }
            loanDTO.setAmountOfMoneyToMakeActive(val.AmountOfMoneyToMakeActive());
            loanDTO.setFullDebt(val.getFullDebt());
            loanDTO.setTotalLoanerMoney(val.calculateTotalLoanerMoney());
            loanDTO.setTotalOnePaymentToAll(val.TotalOnePaymentToAllLoaners());
            loanDTO.setTotalPaidSoFar(val.TotalPaidSoFarToLoaners());
            loanDTO.setTotalAmountToPay(val.getTotalAmountToPay());
            loanDTO.setNextPaymentYaz(val.getnextPaymentTime());
            for (Lender value : val.getLoanerList()) {
                LenderDTO lenderDTO = new LenderDTO(value.getAmount(), value.getDebt(), value.getPayEveryYazPeriod(),loanDTO.getOwner());
                loanDTO.setLoanerList(lenderDTO);
                for (PersonDTO p : customerDTO) {
                    if (value.getOwner().getName().equalsIgnoreCase(p.getName())) {
                        lenderDTO.setOwner(p);
                    }
                }
                lenderDTO.setTotalInterest(value.TotalInterest());
                lenderDTO.setTotalPayBack(value.getTotalPayback());
                lenderDTO.setLeftToPay(value.leftToPay());
                lenderDTO.setTotalInterestPaidSoFar(value.TotalInterestPaidSoFar());
                lenderDTO.setOwnershipPercent(value.getOwnershipPercent());
                for (Payment pay : value.getPaymentlog())
                    lenderDTO.setPaymentlog(new PaymentDTO(pay.getCurrentyaz(), pay.getInterestOnePay(), pay.getOnePayment(), pay.isPaidSuccesfully(), pay.getTotalOnePayment()));
            }
        }
        for (PersonDTO val: customerDTO){
            val.setLoansAndLendersList(loansDTO);
        }
        return new DataBase(customerDTO,loansDTO);
    }
}
