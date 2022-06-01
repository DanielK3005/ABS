package database;

import dto.LoanDTO;
import dto.PersonDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import transaction.Loan;

import java.util.List;
import java.util.Observable;

public class DataBase {
    List<PersonDTO> customersDTO;
    List<LoanDTO> loansDTO;

    public DataBase(List<PersonDTO> customersDTO, List<LoanDTO> loansDTO) {
        this.customersDTO = customersDTO;
        this.loansDTO = loansDTO;
    }

    public List<PersonDTO> getCustomersDTO() {
        return customersDTO;
    }

    public void setCustomersDTO(List<PersonDTO> customersDTO) {
        this.customersDTO = customersDTO;
    }

    public List<LoanDTO> getLoansDTO() {
        return loansDTO;
    }

    public void setLoansDTO(List<LoanDTO> loansDTO) {
        this.loansDTO = loansDTO;
    }

    public boolean StatusEqualLoans(List<Loan> loans){
        for(LoanDTO val: this.loansDTO){
            Loan compare= loans.stream().filter(x->x.getId().equalsIgnoreCase(val.getId())).findAny().get();
            if(val.getStatus()!=compare.getStatus())
                return false;
        }
        return true;
    }
}
