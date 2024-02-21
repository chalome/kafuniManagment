
package Controllers;

import static DatabaseConnection.ConnectionDatabase.getConnection;
import Models.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class WorkerControllor {
    private static Connection connection;
    private PreparedStatement pst;
    private ResultSet resultSet;

    public WorkerControllor() {
    }

    public int create(Worker worker) {
        int save = 0;
        connection = getConnection();
        String sql = "insert into worker(workerFname,workerLname,workerAdress,workerPhone,workerSalary,workerDate)values(?,?,?,?,?,?)";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, worker.getWorkerFirstName());
            pst.setString(2, worker.getWorkerLastName());
            pst.setString(3, worker.getWorkerAdress());
            pst.setString(4,worker.getWorkerPhone());
            pst.setInt(5, worker.getWorkerSalary());
            pst.setString(6,worker.getWorkerDate());
            save = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return save;
    }

    public int update(Worker worker) {
        int update = 0;
        connection = getConnection();
        String sql = "update worker set workerFname=?,workerLname=?,workerAdress=?,workerPhone=?,workerSalary=?,workerDate=? where workerID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, worker.getWorkerFirstName());
            pst.setString(2, worker.getWorkerLastName());
            pst.setString(3, worker.getWorkerAdress());
            pst.setString(4,worker.getWorkerPhone());
            pst.setInt(5, worker.getWorkerSalary());
            pst.setString(6,worker.getWorkerDate());
            pst.setInt(7, worker.getWorkerID());
            update = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return update;
    }

    public int delete(Worker worker) {
        int delete = 0;
        connection = getConnection();
        String sql = "delete from  worker  where workerID=?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, worker.getWorkerID());
            delete = pst.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return delete;
    }

    public ArrayList<Worker> displayWorkerInfo() {
        ArrayList<Worker> list = new ArrayList<>();
        String sql = "select * from worker order by workerID desc";
        connection = getConnection();
        try {
            pst = connection.prepareStatement(sql);
            resultSet = pst.executeQuery();
            while (resultSet.next()) {
                Worker worker = new Worker();
                worker.setWorkerID(resultSet.getInt("workerID"));
                worker.setWorkerFirstName(resultSet.getString("workerFname"));
                worker.setWorkerLastName(resultSet.getString("workerLname"));
                worker.setWorkerAdress(resultSet.getString("workerAdress"));
                worker.setWorkerPhone(resultSet.getString("workerPhone"));
                worker.setWorkerSalary(resultSet.getInt("workerSalary"));
                worker.setWorkerDate(resultSet.getString("workerDate"));
                list.add(worker);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return list;
    }
}
