package Controllers;

import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class HistoryController {

    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public HistoryController() {
    }

    public int create(History history) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into history(historyProduct,historyCustomer,historyProductUnit,historyProductPrice)values(?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, history.getHistoryProduct());
            pst.setString(2, history.getHistoryCustomer());
            pst.setInt(3, history.getHistoryProductUnit());
            pst.setInt(4, history.getHistoryProductPrice());
            save = pst.executeUpdate();
        } catch (Exception e) {
//            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(History history) {
        int update = 0;
        connection = getConnection();
        String sql = "update history set historyProduct=?,historyCustomer=?,historyProductUnit=?,historyProductPrice=?,historyDate=now()where historyID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, history.getHistoryProduct());
            pst.setString(2, history.getHistoryCustomer());
            pst.setInt(3, history.getHistoryProductUnit());
            pst.setInt(4, history.getHistoryProductPrice());
            pst.setInt(5, history.getHistoryID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(History history) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from  history  where historyID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, history.getHistoryID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<History> displayHistoryInfo() {
        ArrayList<History> list = new ArrayList<>();       
String sql = "select history.historyID as ID,customer.CustomerName as Customer_Name,"
        + "product.productName as Product_Name,history.historyProductUnit as Unit,"
        + "product.productPrice as Price_Per_Unit,(productPrice*historyProductUnit)as Total,"
        + "(productPrice*historyProductUnit)as Balance,history.historyDate as Date from history,"
        + "customer,product where history.historyProduct=product.productID"
        + " and history.historyCustomer=customer.customerID order by historyId desc";
        connection = getConnection();
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                History history = new History();
                history.setHistoryID(resultSet.getInt("historyID"));
                history.setHistoryProduct(resultSet.getString("historyProduct"));
                history.setHistoryCustomer(resultSet.getString("historyCustomer"));
                history.setHistoryProductUnit(resultSet.getInt("productUnit"));
                history.setHistoryDate(resultSet.getString("historyDate"));
                list.add(history);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }
}
