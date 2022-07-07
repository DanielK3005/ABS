package engine;

import customer.Person;
import database.DataBase;
import dto.LoanDTO;
import dto.PersonDTO;
import exception.*;
import transaction.Loan;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface EngineFunctions {

    void setLoan(Loan loan);

    void LoadFileDataToEngine() throws NullPointerException;

    Person FindPersonInCustomerList(String name);

    List<String> getCategories();

    List<Loan> getLoans();

    List<LoanDTO> SearchMatchedLoans(PersonDTO p,DataBase data,Integer interest,List<String> categorys,Integer minYaz,Integer maxOpen);

    void MakeAssignment(PersonDTO borrower,double amount,List<LoanDTO> picked,Integer ownerShip, int currentyaz) throws LastLoanException;

    void UpdateLoansStatus(int currentyaz);

    boolean MakeAwithdraw(PersonDTO pick,int amount,int currentyaz) throws MoreThanYouGotException;

    void loadFiletoDescriptor(File in) throws JAXBException, IOException;

    void CheckValidDataFile() throws CategoryMismatchException, LoanPersonMismatchException, NotDividedCorrectlyException, DuplicatePersonException;

    void isFileLoaded() throws NullPointerException;

    Stream<LoanDTO> InterestFilter(Integer interest, Stream<LoanDTO> add);
    Stream<LoanDTO>  minYazFilter(Integer minYaz,Stream<LoanDTO> add);
    Stream<LoanDTO>  maxOpenFilter(Integer maxOpen,Stream<LoanDTO> add);
    Stream<LoanDTO> CategoryFilter(List<String> categories,Stream<LoanDTO> add);

    void PaymentForActive(LoanDTO activeDTO,int currentyaz) throws NotEnoughMoneyException;
    void PaymentForRisk(LoanDTO riskDTO,Double amount,int currentyaz) throws ExtraPaymentMoneyException;
    void PayAll(LoanDTO loanDTO,int currentyaz) throws NotEnoughMoneyException;

    boolean InsertIncome(PersonDTO p, int amount, int currentyaz);

    DataBase getDataBaseDTO();

    List<PersonDTO> getCustomerDTO();
}
