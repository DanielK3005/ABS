package customerWindow.components.scramble;

import database.DataBase;
import dto.LoanDTO;
import dto.PersonDTO;
import engine.EngineFunctions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import transaction.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrambleTask extends Task<ObservableList<LoanDTO>> {

    private EngineFunctions Model;
    private PersonDTO PickedPerson;
    private DataBase dataDTO;
    private Integer interestPick;
    List<String> pickedCategories;
    Integer minYaz;
    Integer maxOpen;

    public ScrambleTask(EngineFunctions model, PersonDTO pickedPerson, DataBase dataDTO, Integer interestPick, List<String> pickedCategories, Integer minYaz, Integer maxOpen) {
        Model = model;
        PickedPerson = pickedPerson;
        this.dataDTO = dataDTO;
        this.interestPick = interestPick;
        this.pickedCategories = pickedCategories;
        this.minYaz = minYaz;
        this.maxOpen = maxOpen;
    }

    @Override
    protected ObservableList<LoanDTO> call() throws Exception {
        List<LoanDTO> loansDTO=dataDTO.getLoansDTO();
        Stream<LoanDTO> s=null;
        updateProgress(0,5);
        Thread.sleep(350);
        s=loansDTO.stream().filter(x->(x.getStatus()== Loan.Status.NEW) || (x.getStatus()== Loan.Status.PENDING));
        s=s.filter(x->!x.getOwner().getName().equals(PickedPerson.getName()));
        updateProgress(1,5);
        Thread.sleep(250);
        s=Model.CategoryFilter(pickedCategories,s);
        updateProgress(2,5);
        Thread.sleep(250);
        s=Model.InterestFilter(interestPick,s);
        updateProgress(3,5);
        Thread.sleep(250);
        s=Model.minYazFilter(minYaz,s);
        updateProgress(4,5);
        Thread.sleep(250);
        s=Model.maxOpenFilter(maxOpen,s);
        updateProgress(5,5);
        Thread.sleep(350);
        return FXCollections.observableArrayList(s.collect(Collectors.toList()));
    }
}
