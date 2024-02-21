/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Chalome
 */
public class Expense {
    private int expenseID;
    private String expenseReason;
    private int expenseAmount;
    private String expenseWorker;
    private String exepenseDetails;

    public Expense() {
    }
    
    

    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseReason() {
        return expenseReason;
    }

    public void setExpenseReason(String expesneReason) {
        this.expenseReason = expesneReason;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseWorker() {
        return expenseWorker;
    }

    public void setExpenseWorker(String expenseWorker) {
        this.expenseWorker = expenseWorker;
    }

    public String getExepenseDetails() {
        return exepenseDetails;
    }

    public void setExepenseDetails(String exepenseDetails) {
        this.exepenseDetails = exepenseDetails;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }
    private String expenseDate;
    
    
}
