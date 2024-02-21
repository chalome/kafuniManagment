/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;
import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.Income;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.JOptionPane;
/**
 *
 * @author Chalome
 */
public class IncomeControllor {
    
    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public IncomeControllor() {
    }
    
     public int create(Income income) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into income(incomeSource,incomeAmount,incomeDate)values(?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, income.getIncomeSource());
            pst.setInt(2, income.getIncomeAmount());
            pst.setString(3, income.getIncomeDate());
            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(Income income) {
        int update = 0;
        connection = getConnection();
        String sql = "update income set incomeSource=?,incomeAmount=?,incomeDate=? where incomeID=?";
        try {
            pst = connection.prepareStatement(sql);
             pst.setString(1, income.getIncomeSource());
            pst.setInt(2, income.getIncomeAmount());
            pst.setString(3, income.getIncomeDate());
            pst.setInt(4, income.getIncomeID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(Income income) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from income where incomeID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, income.getIncomeID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<Income> displayIncomeInfo() {
        ArrayList<Income> list = new ArrayList<>();
        connection = getConnection();
        String sql = "SELECT * FROM income order by incomeID desc";
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Income income = new Income();
                income.setIncomeID(resultSet.getInt("incomeID"));
                income.setIncomeSource(resultSet.getString("incomeSource"));
                income.setIncomeAmount(Integer.parseInt(resultSet.getString("incomeAmount")));
                income.setIncomeDate(resultSet.getString("incomeDate"));
                list.add(income);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return list;
    }
   
}
