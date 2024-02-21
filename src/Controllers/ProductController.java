package Controllers;

import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProductController{

    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public ProductController() {
    }

    public int create(Product product) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into product(productName,productUnit,productPrice,productDate)values(?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, product.getProductNature());
            pst.setInt(2, product.getProductUnit());
            pst.setInt(3, product.getProductPrice());
            pst.setString(4,product.getProductDate());
            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(Product product) {
        int update = 0;
        connection = getConnection();
        String sql = "update product set productName=?,productUnit=?,productPrice=?,productDate=? where productID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, product.getProductNature());
            pst.setInt(2, product.getProductUnit());
            pst.setInt(3, product.getProductPrice());
              pst.setString(4,product.getProductDate());
            pst.setInt(5, product.getProductID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(Product product) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from  product  where productID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, product.getProductID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<Product> displayProductInfo() {
        ArrayList<Product> list = new ArrayList<>();
        String sql = "select * from product order by productID desc";
        connection = getConnection();
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductID(resultSet.getInt("productID"));
                product.setProductNature(resultSet.getString("productName"));
                product.setProductUnit(resultSet.getInt("productUnit"));
                product.setProductPrice(resultSet.getInt("productPrice"));
                product.setProductDate(resultSet.getString("productDate"));
                list.add(product);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }
}
