/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class ExpenseControllor {
    
    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public ExpenseControllor() {
    }
     public int create(Expense expense) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into expense(expenseReason,expenseAmount,expenseWorker,expenseDetails,expenseDate)values(?,?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, expense.getExpenseReason());
            pst.setInt(2, expense.getExpenseAmount());
            pst.setString(3, expense.getExpenseWorker());
            pst.setString(4,expense.getExepenseDetails());
            pst.setString(5, expense.getExpenseDate());
            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(Expense expense) {
        int update = 0;
        connection = getConnection();
        String sql = "update worker set expenseReason=?,expenseAmount=?,expenseWorker=?,expenseDetails=?,expenseDate=? where expenseID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst = connection.prepareStatement(sql);
            pst.setString(1, expense.getExpenseReason());
            pst.setInt(2, expense.getExpenseAmount());
            pst.setString(3, expense.getExpenseWorker());
            pst.setString(4,expense.getExepenseDetails());
            pst.setString(5, expense.getExpenseDate());
            pst.setInt(6, expense.getExpenseID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(Expense expense) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from  expense  where expenseID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, expense.getExpenseID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<Expense> displayExpenseInfo() {
        ArrayList<Expense> list = new ArrayList<>();
        String sql = "select * from expense order by expenseID desc";
        connection = getConnection();
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Expense exepense = new Expense();
                exepense.setExpenseID(resultSet.getInt("expenseID"));
                exepense.setExpenseReason(resultSet.getString("expenseReason"));
                exepense.setExpenseAmount(resultSet.getInt("expenseAmount"));
                exepense.setExpenseWorker(resultSet.getString("expenseWorker"));
                exepense.setExepenseDetails(resultSet.getString("expenseDetails"));
                exepense.setExpenseDate(resultSet.getString("expenseDate"));
                list.add(exepense);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }
}
