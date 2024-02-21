package Controllers;

import Actions.Actions;
import Models.Customer;
import static DatabaseConnection.ConnectionDatabase.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.JOptionPane;

public class CustomerController {

    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public CustomerController() {
    }

    public int create(Customer customer) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into customer(customerName,customerAdresse,customerPhone,CustomerDate)values(?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, customer.getCustomerName());
            pst.setString(2, customer.getCustomerAdress());
            pst.setString(3, customer.getCustomerPhone());
            pst.setString(4,  customer.getCustomerDate());
            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(Customer customer) {
        int update = 0;
        connection = getConnection();
        String sql = "update customer set customerName=?,customerAdresse=?,customerPhone=?,CustomerDate=? where customerID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, customer.getCustomerName());
            pst.setString(2, customer.getCustomerAdress());
            pst.setString(3, customer.getCustomerPhone());
            pst.setString(4, customer.getCustomerDate());
            pst.setInt(5, customer.getCustomerID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(Customer customer) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from customer where customerID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, customer.getCustomerID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<Customer> displayCustomerInfo() {
        ArrayList<Customer> list = new ArrayList<>();
        connection = getConnection();
        String sql = "SELECT * FROM customer order by customerID desc";
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerID(resultSet.getInt("customerID"));
                customer.setCustomerName(resultSet.getString("customerName"));
                customer.setCustomerAdress(resultSet.getString("customerAdresse"));
                customer.setCustomerPhone(resultSet.getString("customerPhone"));
                customer.setCustomerDate(resultSet.getString("customerDate"));
                list.add(customer);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return list;
    }
   
}
