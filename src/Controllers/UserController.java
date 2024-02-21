
package Controllers;

import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class UserController implements Actions.Actions{
    private Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public UserController() {
    }

    @Override
    public int create(User user) {
        int save=0;
       connection = getConnection();
        String sql = "insert into user(userName,password)values(?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());

            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    @Override
    public int update(User user) {
        int update=0;
        connection = getConnection();
        String sql = "update user set userName=?,password=? where userID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, user.getUserName());
            pst.setString(2, user.getPassword());
            pst.setInt(3, user.getUserID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update; }

    @Override
    public int delete(User user) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from  user  where userID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, user.getUserID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete; }

    @Override
    public ArrayList<User> displayUsers() {
        ArrayList<User> list = new ArrayList<>();
        String sql = "select * from user order by userID desc";
        connection = getConnection();
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setUserID(resultSet.getInt("productID"));
                user.setUserName(resultSet.getString("productName"));
                user.setPassword(resultSet.getString("productUnit"));
                list.add(user);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;}
    
    
}
