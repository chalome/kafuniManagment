package MainForm;

import Charts.ModelData;
import Controllers.CustomerController;
import Controllers.ExpenseControllor;
import Controllers.HistoryController;
import Controllers.IncomeControllor;
import Controllers.ProductController;
import Controllers.WorkerControllor;
import static DatabaseConnection.ConnectionDatabase.getConnection;
import Login.SplashScreen;
import Models.Customer;
import Models.Expense;
import Models.History;
import Models.Income;
import Models.Product;
import Models.Worker;
import chalome.chart.ModelChart;
import chalome.dynamicjasper.template.PageFormat;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme;
import com.raven.card.ModelCard;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.exception.DRException;
import noticeboard.ModelNoticeBoard;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author Dell
 */
public class MainForm extends javax.swing.JFrame {

    private PageFormat pageFormat = new PageFormat(PageType.A4, 0, 0, PageOrientation.PORTRAIT);
    private Animator animatorLogin;
    private Animator animatorBody;
    private boolean signIn;

    public MainForm() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/icons/icon.png")).getImage());
        setTitle("Kafuni Enterprise");
        displayProductInfo();
        displayCustomerInfo();
        displayWorkerInfo();
        comboDisplay();
        displayIncomeInfo();
        displayComboBoxWorker();
        history();
        initNoticeBoard();
        chart.setTitle("Chart Data");
        chart.addLegend("Amount", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Cost", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Profit", Color.decode("#0099F7"), Color.decode("#F11712"));
//        test();
        setData();
        initData();
        getContentPane().setBackground(new Color(245, 245, 245));
        TimingTarget targetLogin = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (signIn) {
                    background1.setAnimate(fraction);
                } else {
                    background1.setAnimate(1f - fraction);
                }
            }

            @Override
            public void end() {
                if (signIn) {
                    panelLogin.setVisible(false);
                    background1.setShowPaint(true);
                    panelBody.setAlpha(0);
                    panelBody.setVisible(true);
                    animatorBody.start();
                } else {
                    enableLogin(true);
                    txtUser.grabFocus();
                }
            }
        };
        TimingTarget targetBody = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                if (signIn) {
                    panelBody.setAlpha(fraction);
                } else {
                    panelBody.setAlpha(1f - fraction);
                }
            }

            @Override
            public void end() {
                if (signIn == false) {
                    panelBody.setVisible(false);
                    background1.setShowPaint(false);
                    background1.setAnimate(1);
                    panelLogin.setVisible(true);
                    animatorLogin.start();
                }
            }
        };
        animatorLogin = new Animator(1500, targetLogin);
        animatorBody = new Animator(500, targetBody);
        animatorLogin.setResolution(0);
        animatorBody.setResolution(0);
        jScrollPane1.getViewport().setOpaque(false);
        jScrollPane1.setViewportBorder(null);
    }

    private void enableLogin(boolean action) {
        txtUser.setEditable(action);
        txtPass.setEditable(action);
        cmdSignIn.setEnabled(action);
    }

    public void clearLogin() {
        txtUser.setText("");
        txtPass.setText("");
        txtUser.setHelperText("");
        txtPass.setHelperText("");
    }

    private void setData() {
        try {
            List<ModelData> lists = new ArrayList<>();
            Connection connection = DatabaseConnection.ConnectionDatabase.getConnection();
            String sql = " select product.productName,sum(productUnit) as TotaL_Unit ,productPrice,(productUnit*productprice),sum(history.historyproductunit)as Amount,historyproductprice,(historyproductunit*historyproductprice)as Cost,((historyProductunit*historyproductprice)-(productunit*productprice))as Profit from product,history where history.historyproduct=product.productID  group by productID";
            PreparedStatement p = connection.prepareStatement(sql);
            ResultSet r = p.executeQuery();
            while (r.next()) {
                String product = r.getString("productName");
                double amount = r.getDouble("Amount");
                double cost = r.getDouble("Cost");
                double profit = r.getDouble("Profit");
                lists.add(new ModelData(product, amount, cost, profit));
            }
            r.close();
            p.close();
            //  Add Data to chart
            for (int i = lists.size() - 1; i >= 0; i--) {
                ModelData d = lists.get(i);
                chart.addData(new ModelChart(d.getMonth(), new double[]{d.getAmount(), d.getCost(), d.getProfit()}));
            }
            //  Start to show data with animation
            chart.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        card1.setData(new ModelCard("Income", "$ 60,000", "Total Income"));
        card2.setData(new ModelCard("Expense", "$ 60,000", "Total Expense"));
        card3.setData(new ModelCard("Profit", "$ 60,000", "Total Profit"));
        card4.setData(new ModelCard("Cost", "$ 60,000", "Total Cost"));
    }

    private void test() {
        chart.clear();
        chart.addData(new ModelChart("January", new double[]{500, 50, 100}));
        chart.addData(new ModelChart("February", new double[]{600, 300, 150}));
        chart.addData(new ModelChart("March", new double[]{200, 50, 900}));
        chart.addData(new ModelChart("April", new double[]{480, 700, 100}));
        chart.addData(new ModelChart("May", new double[]{350, 540, 500}));
        chart.addData(new ModelChart("June", new double[]{450, 800, 100}));
        chart.start();
    }

    public void displayProductInfo() {
        ProductController pc = new ProductController();

        String header[] = {"ProductID", "Nature", "Unit", "Unit Price", "Date"};
        Object data[][] = new Object[pc.displayProductInfo().size()][5];
        int i = 0;

        for (Product product : pc.displayProductInfo()) {
            data[i][0] = product.getProductID();
            data[i][1] = product.getProductNature();
            data[i][2] = product.getProductUnit();
            data[i][3] = product.getProductPrice();
            data[i][4] = product.getProductDate();
            i++;

        }
        productTable.setModel(new DefaultTableModel(data, header));
    }

    public void displayWorkerInfo() {
        WorkerControllor pc = new WorkerControllor();

        String header[] = {"ID", "FisrtName", "LastName", "Adresse", "Phone", "Salary", "Date"};
        Object data[][] = new Object[pc.displayWorkerInfo().size()][7];
        int i = 0;

        for (Worker worker : pc.displayWorkerInfo()) {
            data[i][0] = worker.getWorkerID();
            data[i][1] = worker.getWorkerFirstName();
            data[i][2] = worker.getWorkerLastName();
            data[i][3] = worker.getWorkerAdress();
            data[i][4] = worker.getWorkerPhone();
            data[i][5] = worker.getWorkerSalary();
            data[i][6] = worker.getWorkerDate();
            i++;

        }
        workerTable.setModel(new DefaultTableModel(data, header));
    }

    public void displayCustomerInfo() {
        CustomerController controller = new CustomerController();

        String header[] = {"CustomerID", "Name", "Adresse", "Phone Number", "Date"};
        Object data[][] = new Object[controller.displayCustomerInfo().size()][5];
        int i = 0;

        for (Customer customer : controller.displayCustomerInfo()) {
            data[i][0] = customer.getCustomerID();
            data[i][1] = customer.getCustomerName();
            data[i][2] = customer.getCustomerAdress();
            data[i][3] = customer.getCustomerPhone();
            data[i][4] = customer.getCustomerDate();
            i++;

        }
        customerTable.setModel(new DefaultTableModel(data, header));
    }

    private void displayIncomeInfo() {
        IncomeControllor controller = new IncomeControllor();

        String header[] = {"IncomeID", "Source", "Amount", "Date"};
        Object data[][] = new Object[controller.displayIncomeInfo().size()][5];
        int i = 0;

        for (Income income : controller.displayIncomeInfo()) {
            data[i][0] = income.getIncomeID();
            data[i][1] = income.getIncomeSource();
            data[i][2] = income.getIncomeAmount();
            data[i][3] = income.getIncomeDate();
            i++;

        }
        incomeTable.setModel(new DefaultTableModel(data, header));

    }

    private void history() {
        String header[] = {"HistoryID", "CustomerName", "ProductName", "Unit", "Unit Price", "Total", "Balance"};
        String sql = "select history.historyID as ID,customer.CustomerName as Customer_Name,"
                + "product.productName as Product_Name,history.historyProductUnit as Unit,"
                + "history.historyProductPrice,(historyProductPrice*historyProductUnit)as Total,"
                + "(historyProductPrice*historyProductUnit)as Balance,history.historyDate as Date from history,"
                + "customer,product where history.historyProduct=product.productID"
                + " and history.historyCustomer=customer.customerID order by historyId desc";
        String show[] = new String[7];
        Connection connection = getConnection();
        DefaultTableModel model = new DefaultTableModel(null, header);
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                show[0] = resultSet.getString("ID");
                show[1] = resultSet.getString("Customer_Name");
                show[2] = resultSet.getString("Product_Name");
                show[3] = resultSet.getString("Unit");
                show[4] = resultSet.getString("historyProductPrice");
                show[5] = resultSet.getString("Total");
                show[6] = resultSet.getString("Balance");
                model.addRow(show);
                historyTable.setModel(model);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void reset(JTextField n1, JTextField n2, JTextField n3, JDateChooser n4) {
        n1.setText("");
        n2.setText("");
        n3.setText("");
        n4.setDate(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background1 = new com.raven.swing.Background();
        panelLogin = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPass = new Login.UserLogin.PasswordField();
        txtUser = new Login.UserLogin.TextField();
        cmdSignIn = new com.raven.swing.Button();
        cmdSignInUp = new com.raven.swing.Button();
        jLabel6 = new javax.swing.JLabel();
        panelBody = new com.raven.swing.PanelTransparent();
        materialTabbed1 = new SwingComponents.MaterialTabbed();
        productPanel = new javax.swing.JPanel();
        pname = new SwingComponents.TextField();
        punit = new SwingComponents.TextField();
        pprice = new SwingComponents.TextField();
        pdate = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new SwingComponents.TableDark();
        jLabel5 = new javax.swing.JLabel();
        AddProduct = new SwingComponents.ButtonGradient();
        buttonGradient6 = new SwingComponents.ButtonGradient();
        updateProduct = new SwingComponents.ButtonGradient();
        resetProduct = new SwingComponents.ButtonGradient();
        customerPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        customerTable = new SwingComponents.TableDark();
        customerName = new SwingComponents.TextField();
        jLabel2 = new javax.swing.JLabel();
        customerAdress = new SwingComponents.TextField();
        customerPhone = new SwingComponents.TextField();
        customerDate = new com.toedter.calendar.JDateChooser();
        updateCustomer = new SwingComponents.ButtonGradient();
        AddCustomer = new SwingComponents.ButtonGradient();
        deleteCustomer = new SwingComponents.ButtonGradient();
        resetCustomer = new SwingComponents.ButtonGradient();
        historyPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        historyTable = new SwingComponents.TableDark();
        customerCombo = new SwingComponents.Combobox();
        productNatureCombo = new SwingComponents.Combobox();
        pricecombo = new SwingComponents.TextField();
        unitCombo = new SwingComponents.TextField();
        historyAdd = new SwingComponents.BorderGradientButton();
        updatejistory = new SwingComponents.BorderGradientButton();
        borderGradientButton3 = new SwingComponents.BorderGradientButton();
        deletehistory = new SwingComponents.BorderGradientButton();
        jLabel3 = new javax.swing.JLabel();
        refresh = new SwingComponents.BorderGradientButton();
        refresh1 = new SwingComponents.BorderGradientButton();
        chartsPanel = new javax.swing.JPanel();
        panelShadow1 = new chalome.panel.PanelShadow();
        chart = new chalome.chart.CurveLineChart();
        card1 = new chalome.chart.card.Card();
        card2 = new chalome.chart.card.Card();
        card3 = new chalome.chart.card.Card();
        card4 = new chalome.chart.card.Card();
        cmdSetupPage = new SwingComponents.BorderGradientButton();
        cmdPrintImage = new SwingComponents.BorderGradientButton();
        cmdPrintPdf = new SwingComponents.BorderGradientButton();
        cmdPrintWord = new SwingComponents.BorderGradientButton();
        cmdPrintExcel = new SwingComponents.BorderGradientButton();
        cmdPrintViewPage = new SwingComponents.BorderGradientButton();
        customerRadio = new SwingComponents.RadioButtonCustom();
        productRadio = new SwingComponents.RadioButtonCustom();
        historyRadio = new SwingComponents.RadioButtonCustom();
        incomePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        incomeSource = new SwingComponents.TextField();
        incomeAmount = new SwingComponents.TextField();
        incomeDate = new com.toedter.calendar.JDateChooser();
        AddIncome = new SwingComponents.ButtonGradient();
        updateIncome = new SwingComponents.ButtonGradient();
        resetIncome = new SwingComponents.ButtonGradient();
        deleteIncome = new SwingComponents.ButtonGradient();
        jScrollPane5 = new javax.swing.JScrollPane();
        incomeTable = new SwingComponents.TableDark();
        worker = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        workerFName = new SwingComponents.TextField();
        workerLname = new SwingComponents.TextField();
        workerPhone = new SwingComponents.TextField();
        workerDate = new com.toedter.calendar.JDateChooser();
        AddWorker = new SwingComponents.ButtonGradient();
        updateWorker = new SwingComponents.ButtonGradient();
        deleteWorker = new SwingComponents.ButtonGradient();
        resetWorker = new SwingComponents.ButtonGradient();
        jScrollPane4 = new javax.swing.JScrollPane();
        workerTable = new SwingComponents.TableDark();
        workerSalary = new SwingComponents.TextField();
        workerAdress = new SwingComponents.TextField();
        ExpensePanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        expenseReason = new SwingComponents.TextField();
        exepenseAmount = new SwingComponents.TextField();
        expensedate = new com.toedter.calendar.JDateChooser();
        savebtn = new SwingComponents.ButtonGradient();
        upadatebtn = new SwingComponents.ButtonGradient();
        deletebtn = new SwingComponents.ButtonGradient();
        resetbtn = new SwingComponents.ButtonGradient();
        expensedetails = new SwingComponents.TextAreaScroll();
        expenseDetail = new SwingComponents.TextArea();
        expenseWorker = new SwingComponents.Combobox();
        noticeBoard = new noticeboard.Notice_Board();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background1.setLayout(new java.awt.CardLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Login Page");

        txtPass.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtPass.setLabelText("Password");

        txtUser.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtUser.setLabelText("User Name");

        cmdSignIn.setBackground(new java.awt.Color(157, 153, 255));
        cmdSignIn.setText("Sign In");
        cmdSignIn.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmdSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSignInActionPerformed(evt);
            }
        });

        cmdSignInUp.setBackground(new java.awt.Color(157, 153, 255));
        cmdSignInUp.setText("Create new Account");
        cmdSignInUp.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmdSignInUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSignInUpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(txtPass, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(txtUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cmdSignIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cmdSignInUp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cmdSignIn, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cmdSignInUp, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye_hide.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel6MouseExited(evt);
            }
        });

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                .addContainerGap(350, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addGap(291, 291, 291))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(281, 281, 281))
        );

        background1.add(panelLogin, "card2");

        materialTabbed1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        pname.setBackground(new java.awt.Color(44, 44, 59));
        pname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pname.setLabelText("Product nature");

        punit.setBackground(new java.awt.Color(44, 44, 59));
        punit.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        punit.setLabelText("Unit");

        pprice.setBackground(new java.awt.Color(44, 44, 59));
        pprice.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        pprice.setLabelText("Price per Unit");

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productTableMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(productTable);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Add Product");

        AddProduct.setText("Add Product");
        AddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddProductActionPerformed(evt);
            }
        });

        buttonGradient6.setText("Delete Product");
        buttonGradient6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGradient6ActionPerformed(evt);
            }
        });

        updateProduct.setText("Update Product");
        updateProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateProductActionPerformed(evt);
            }
        });

        resetProduct.setText("Reset Fields");
        resetProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productPanelLayout = new javax.swing.GroupLayout(productPanel);
        productPanel.setLayout(productPanelLayout);
        productPanelLayout.setHorizontalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, productPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(pprice, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(punit, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pname, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pdate, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productPanelLayout.createSequentialGroup()
                                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(AddProduct, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                    .addComponent(buttonGradient6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                    .addComponent(resetProduct, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
        );
        productPanelLayout.setVerticalGroup(
            productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productPanelLayout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(punit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(pprice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(pdate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(productPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonGradient6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 133, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        materialTabbed1.addTab("Product", productPanel);

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        customerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                customerTableMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(customerTable);

        customerName.setBackground(new java.awt.Color(44, 44, 59));
        customerName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customerName.setLabelText("Customer Name");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Add Customer");

        customerAdress.setBackground(new java.awt.Color(44, 44, 59));
        customerAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customerAdress.setLabelText("Customer Adresse");

        customerPhone.setBackground(new java.awt.Color(44, 44, 59));
        customerPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customerPhone.setLabelText("Customer PhoneNumber");

        customerDate.setBackground(new java.awt.Color(44, 44, 59));

        updateCustomer.setText("Update Customer");
        updateCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCustomerActionPerformed(evt);
            }
        });

        AddCustomer.setText("Add Customer");
        AddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddCustomerActionPerformed(evt);
            }
        });

        deleteCustomer.setText("Delete Customer");
        deleteCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerActionPerformed(evt);
            }
        });

        resetCustomer.setText("Reset Fields");
        resetCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetCustomerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerPanelLayout = new javax.swing.GroupLayout(customerPanel);
        customerPanel.setLayout(customerPanelLayout);
        customerPanelLayout.setHorizontalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(customerPhone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerAdress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPanelLayout.createSequentialGroup()
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(deleteCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(AddCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(60, 60, 60)
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(resetCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(updateCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
        );
        customerPanelLayout.setVerticalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(customerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(customerAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(customerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(customerDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 133, Short.MAX_VALUE)))
                .addContainerGap())
        );

        materialTabbed1.addTab("Customer", customerPanel);

        historyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                historyTableMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(historyTable);

        customerCombo.setBackground(new java.awt.Color(44, 44, 59));
        customerCombo.setLabeText("Customer");
        customerCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customerComboMouseClicked(evt);
            }
        });

        productNatureCombo.setBackground(new java.awt.Color(44, 44, 59));
        productNatureCombo.setLabeText("Product nature");
        productNatureCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                productNatureComboItemStateChanged(evt);
            }
        });
        productNatureCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productNatureComboMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                productNatureComboMouseExited(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                productNatureComboMouseReleased(evt);
            }
        });

        pricecombo.setBackground(new java.awt.Color(44, 44, 59));
        pricecombo.setLabelText("Price Per Unit");

        unitCombo.setBackground(new java.awt.Color(44, 44, 59));
        unitCombo.setLabelText("Unit");

        historyAdd.setText("Add To Table");
        historyAdd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        historyAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historyAddActionPerformed(evt);
            }
        });

        updatejistory.setText("Update Data");
        updatejistory.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        updatejistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatejistoryActionPerformed(evt);
            }
        });

        borderGradientButton3.setText("Print Table");
        borderGradientButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        deletehistory.setText("Delete Data");
        deletehistory.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deletehistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletehistoryActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("History of Product");

        refresh.setText("Refresh");
        refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshActionPerformed(evt);
            }
        });

        refresh1.setText("Refresh");
        refresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historyPanelLayout = new javax.swing.GroupLayout(historyPanel);
        historyPanel.setLayout(historyPanelLayout);
        historyPanelLayout.setHorizontalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyPanelLayout.createSequentialGroup()
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addGroup(historyPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(189, 189, 189)
                                .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(70, 70, 70)
                                .addComponent(refresh1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(historyPanelLayout.createSequentialGroup()
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productNatureCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78)
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pricecombo, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(updatejistory, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(historyAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(70, 70, 70)
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deletehistory, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(borderGradientButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26))))
        );
        historyPanelLayout.setVerticalGroup(
            historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historyPanelLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(historyPanelLayout.createSequentialGroup()
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(historyPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(customerCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(unitCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(historyPanelLayout.createSequentialGroup()
                                .addComponent(refresh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(historyAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(historyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(productNatureCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pricecombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(updatejistory, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(historyPanelLayout.createSequentialGroup()
                        .addComponent(refresh1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(borderGradientButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(deletehistory, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        materialTabbed1.addTab("History", historyPanel);

        panelShadow1.setBackground(new java.awt.Color(34, 59, 69));
        panelShadow1.setColorGradient(new java.awt.Color(17, 38, 47));
        panelShadow1.setRadius(10);

        chart.setBackground(new java.awt.Color(34, 59, 69));
        chart.setForeground(new java.awt.Color(237, 237, 237));
        chart.setFillColor(true);

        javax.swing.GroupLayout panelShadow1Layout = new javax.swing.GroupLayout(panelShadow1);
        panelShadow1.setLayout(panelShadow1Layout);
        panelShadow1Layout.setHorizontalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelShadow1Layout.setVerticalGroup(
            panelShadow1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelShadow1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        card1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/income.png"))); // NOI18N

        card2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/profit.png"))); // NOI18N

        card3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cost.png"))); // NOI18N

        card4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/expense.png"))); // NOI18N

        cmdSetupPage.setText("Setup Page");
        cmdSetupPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSetupPageActionPerformed(evt);
            }
        });

        cmdPrintImage.setText("Print To Image");
        cmdPrintImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintImageActionPerformed(evt);
            }
        });

        cmdPrintPdf.setText("Print To Pdf");
        cmdPrintPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintPdfActionPerformed(evt);
            }
        });

        cmdPrintWord.setText("Print To Word");
        cmdPrintWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintWordActionPerformed(evt);
            }
        });

        cmdPrintExcel.setText("Print To Excel");
        cmdPrintExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintExcelActionPerformed(evt);
            }
        });

        cmdPrintViewPage.setText("View The Page");
        cmdPrintViewPage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdPrintViewPageActionPerformed(evt);
            }
        });

        customerRadio.setBackground(new java.awt.Color(44, 44, 59));
        customerRadio.setText("Customer");

        productRadio.setBackground(new java.awt.Color(44, 44, 59));
        productRadio.setText("Product");

        historyRadio.setBackground(new java.awt.Color(44, 44, 59));
        historyRadio.setText("History");

        javax.swing.GroupLayout chartsPanelLayout = new javax.swing.GroupLayout(chartsPanel);
        chartsPanel.setLayout(chartsPanelLayout);
        chartsPanelLayout.setHorizontalGroup(
            chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelShadow1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(chartsPanelLayout.createSequentialGroup()
                        .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(chartsPanelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(customerRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(historyRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chartsPanelLayout.createSequentialGroup()
                                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmdSetupPage, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmdPrintPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmdPrintImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdPrintWord, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmdPrintExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cmdPrintViewPage, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(chartsPanelLayout.createSequentialGroup()
                                .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                                .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18))
        );
        chartsPanelLayout.setVerticalGroup(
            chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chartsPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(panelShadow1, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(card1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chartsPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmdPrintImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdSetupPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmdPrintExcel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(chartsPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(customerRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(historyRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chartsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cmdPrintPdf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdPrintWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmdPrintViewPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(productRadio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Chart", chartsPanel);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Add Income");

        incomeSource.setBackground(new java.awt.Color(44, 44, 59));
        incomeSource.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        incomeSource.setLabelText("Source");

        incomeAmount.setBackground(new java.awt.Color(44, 44, 59));
        incomeAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        incomeAmount.setLabelText("Amount");

        incomeDate.setBackground(new java.awt.Color(44, 44, 59));

        AddIncome.setText("Add");
        AddIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddIncomeActionPerformed(evt);
            }
        });

        updateIncome.setText("Update ");
        updateIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateIncomeActionPerformed(evt);
            }
        });

        resetIncome.setText("Reset Fields");
        resetIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetIncomeActionPerformed(evt);
            }
        });

        deleteIncome.setText("Delete");
        deleteIncome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteIncomeActionPerformed(evt);
            }
        });

        incomeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        incomeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                incomeTableMouseReleased(evt);
            }
        });
        jScrollPane5.setViewportView(incomeTable);

        javax.swing.GroupLayout incomePanelLayout = new javax.swing.GroupLayout(incomePanel);
        incomePanel.setLayout(incomePanelLayout);
        incomePanelLayout.setHorizontalGroup(
            incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(incomePanelLayout.createSequentialGroup()
                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(incomePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, incomePanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(incomeAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incomeSource, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incomeDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, incomePanelLayout.createSequentialGroup()
                                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(deleteIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(AddIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(resetIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(updateIncome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE))
        );
        incomePanelLayout.setVerticalGroup(
            incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(incomePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5)
                    .addGroup(incomePanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(incomeSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(incomeAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(63, 63, 63)
                        .addComponent(incomeDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteIncome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 133, Short.MAX_VALUE)))
                .addContainerGap())
        );

        materialTabbed1.addTab("Income", incomePanel);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Add Worker");

        workerFName.setBackground(new java.awt.Color(44, 44, 59));
        workerFName.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        workerFName.setLabelText("Worker First Name");

        workerLname.setBackground(new java.awt.Color(44, 44, 59));
        workerLname.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        workerLname.setLabelText("Worker Last Name");

        workerPhone.setBackground(new java.awt.Color(44, 44, 59));
        workerPhone.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        workerPhone.setLabelText("Phone Number");

        AddWorker.setText("Add Worker");
        AddWorker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddWorkerActionPerformed(evt);
            }
        });

        updateWorker.setText("Update Worker");
        updateWorker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateWorkerActionPerformed(evt);
            }
        });

        deleteWorker.setText("Delete Worker");
        deleteWorker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteWorkerActionPerformed(evt);
            }
        });

        resetWorker.setText("Reset Fields");
        resetWorker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetWorkerActionPerformed(evt);
            }
        });

        workerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        workerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                workerTableMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(workerTable);

        workerSalary.setBackground(new java.awt.Color(44, 44, 59));
        workerSalary.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        workerSalary.setLabelText("Salary");

        workerAdress.setBackground(new java.awt.Color(44, 44, 59));
        workerAdress.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        workerAdress.setLabelText("Adress");

        javax.swing.GroupLayout workerLayout = new javax.swing.GroupLayout(worker);
        worker.setLayout(workerLayout);
        workerLayout.setHorizontalGroup(
            workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workerLayout.createSequentialGroup()
                .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, workerLayout.createSequentialGroup()
                        .addContainerGap(20, Short.MAX_VALUE)
                        .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(workerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workerLname, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workerFName, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workerDate, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(workerLayout.createSequentialGroup()
                                .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(AddWorker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(deleteWorker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(updateWorker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(resetWorker, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(workerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workerSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(workerAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE))
        );
        workerLayout.setVerticalGroup(
            workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(workerLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(workerLayout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addContainerGap())
                    .addGroup(workerLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(workerFName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(workerLname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(workerAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(workerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(workerSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(workerDate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(updateWorker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddWorker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(workerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetWorker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteWorker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(68, 68, 68))))
        );

        materialTabbed1.addTab("Workers", worker);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Add an expense");

        expenseReason.setBackground(new java.awt.Color(44, 44, 59));
        expenseReason.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        expenseReason.setLabelText("Reason");

        exepenseAmount.setBackground(new java.awt.Color(44, 44, 59));
        exepenseAmount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        exepenseAmount.setLabelText("Amount of money");

        savebtn.setText("Save");
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });

        upadatebtn.setText("Update ");
        upadatebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upadatebtnActionPerformed(evt);
            }
        });

        deletebtn.setText("Delete ");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        resetbtn.setText("Reset Fields");
        resetbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetbtnActionPerformed(evt);
            }
        });

        expensedetails.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        expensedetails.setLabelText("Details");

        expenseDetail.setColumns(20);
        expenseDetail.setRows(5);
        expensedetails.setViewportView(expenseDetail);

        expenseWorker.setBackground(new java.awt.Color(44, 44, 59));
        expenseWorker.setLabeText("Who ?");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Notice Board For Expenses");

        javax.swing.GroupLayout ExpensePanelLayout = new javax.swing.GroupLayout(ExpensePanel);
        ExpensePanel.setLayout(ExpensePanelLayout);
        ExpensePanelLayout.setHorizontalGroup(
            ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExpensePanelLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(666, 666, 666))
            .addGroup(ExpensePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expensedate, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(exepenseAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(expenseReason, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ExpensePanelLayout.createSequentialGroup()
                            .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(savebtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                                .addComponent(deletebtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                            .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(upadatebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                                .addComponent(resetbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(expensedetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(expenseWorker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(noticeBoard, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(89, 89, 89))
        );
        ExpensePanelLayout.setVerticalGroup(
            ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ExpensePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ExpensePanelLayout.createSequentialGroup()
                        .addComponent(expenseReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(exepenseAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(expenseWorker, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(expensedetails, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(expensedate, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(upadatebtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(savebtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(ExpensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(resetbtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ExpensePanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(noticeBoard, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        materialTabbed1.addTab("Expense", ExpensePanel);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(materialTabbed1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        background1.add(panelBody, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(background1, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cmdSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSignInActionPerformed
        if (!animatorLogin.isRunning()) {
            signIn = true;
            String sql = "select * from user where username=? and password=?";
            Connection connection = getConnection();
            String user = txtUser.getText().trim();
            boolean action = true;
            String pass = String.valueOf(txtPass.getPassword());
            try {
                PreparedStatement pst = connection.prepareStatement(sql);
                pst.setString(1, user);
                pst.setString(2, pass);
                ResultSet res = pst.executeQuery();
                while (res.next()) {
                    action = true;
                    animatorLogin.start();
                    enableLogin(false);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user.equals("")) {
                txtUser.setHelperText("Please input user name");
                txtUser.grabFocus();
                action = false;
            }
            if (pass.equals("")) {
                txtPass.setHelperText("Please input password");
                if (action) {
                    txtPass.grabFocus();
                }
                action = false;
            }
//            if (action) {
//                animatorLogin.start();
//                enableLogin(false);
//            }

        }
    }//GEN-LAST:event_cmdSignInActionPerformed

    private void productTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productTableMouseReleased
        table(productTable, pname, punit, pprice, pdate);
    }//GEN-LAST:event_productTableMouseReleased

    private void AddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddProductActionPerformed
        int save = 0;
        Product product = new Product();
        ProductController controller = new ProductController();
        if (pname.getText().trim().isEmpty()
                || punit.getText().trim().isEmpty()
                || pprice.getText().trim().isEmpty()
                || ((JTextField) pdate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            product.setProductNature(pname.getText());
            product.setProductUnit(Integer.parseInt(punit.getText()));
            product.setProductPrice(Integer.parseInt(pprice.getText()));
            product.setProductDate(((JTextField) pdate.getDateEditor().getUiComponent()).getText());
            save = controller.create(product);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayProductInfo();
                comboDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddProductActionPerformed

    private void buttonGradient6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGradient6ActionPerformed
        int ligne = productTable.getSelectedRow();
        int id = Integer.parseInt(productTable.getValueAt(ligne, 0).toString());
        int delete = 0;
        Product product = new Product();
        ProductController controller = new ProductController();
        product.setProductID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(product);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayProductInfo();
                table(productTable, pname, punit, pprice, pdate);
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_buttonGradient6ActionPerformed

    private void updateProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateProductActionPerformed
        int i = -1;
        while (i < 0) {
            String id = JOptionPane.showInputDialog(this, "Enter the ProductID");
            if (id.length() > 0) {
                i++;
                int update = 0;
                Product product = new Product();
                ProductController controller = new ProductController();
                product.setProductNature(pname.getText());
                product.setProductUnit(Integer.parseInt(punit.getText()));
                product.setProductPrice(Integer.parseInt(pprice.getText()));
                product.setProductDate(((JTextField) pdate.getDateEditor().getUiComponent()).getText());
                product.setProductID(Integer.parseInt(id));
                int y = JOptionPane.showConfirmDialog(this, "Do you really wanna change this infos?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (y == JOptionPane.OK_OPTION) {
                    update = controller.update(product);

                    if (update == 1) {
                        JOptionPane.showMessageDialog(this, "Successfully changed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        displayProductInfo();
                        table(productTable, pname, punit, pprice, pdate);
                    } else {
                        JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }//GEN-LAST:event_updateProductActionPerformed

    private void resetProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetProductActionPerformed
        reset(pname, punit, pprice, pdate);
    }//GEN-LAST:event_resetProductActionPerformed

    private void customerTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerTableMouseReleased
        table(customerTable, customerName, customerAdress, customerPhone, customerDate);
    }//GEN-LAST:event_customerTableMouseReleased

    private void updateCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCustomerActionPerformed
        int i = -1;
        while (i < 0) {
            String id = JOptionPane.showInputDialog(this, "Enter the ProductID");
            if (id.length() > 0) {
                i++;
                int update = 0;
                Customer customer = new Customer();
                CustomerController controller = new CustomerController();
                customer.setCustomerName(customerName.getText());
                customer.setCustomerAdress(customerAdress.getText());
                customer.setCustomerPhone(customerPhone.getText());
                customer.setCustomerDate(((JTextField) customerDate.getDateEditor().getUiComponent()).getText());
                customer.setCustomerID(Integer.parseInt(id));
                int y = JOptionPane.showConfirmDialog(null, "Do you really wanna change this infos?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (y == JOptionPane.OK_OPTION) {
                    update = controller.update(customer);
                    if (update == 1) {
                        JOptionPane.showMessageDialog(null, "Successfully changed!");
                        displayCustomerInfo();
                        table(customerTable, customerName, customerAdress, customerPhone, customerDate);
                    } else {
                        JOptionPane.showMessageDialog(null, "Action failed");
                    }
                }
            }
        }
    }//GEN-LAST:event_updateCustomerActionPerformed

    private void AddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddCustomerActionPerformed
        Customer customer = new Customer();
        CustomerController controller = new CustomerController();
        int save = 0;
        if (customerName.getText().trim().isEmpty()
                || customerAdress.getText().trim().isEmpty()
                || customerPhone.getText().trim().isEmpty()
                || ((JTextField) customerDate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            customer.setCustomerName(customerName.getText());
            customer.setCustomerAdress(customerAdress.getText());
            customer.setCustomerPhone(customerPhone.getText());
            customer.setCustomerDate(((JTextField) customerDate.getDateEditor().getUiComponent()).getText());
            save = controller.create(customer);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayCustomerInfo();
                comboDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddCustomerActionPerformed

    private void deleteCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerActionPerformed
        int ligne = customerTable.getSelectedRow();
        int id = Integer.parseInt(customerTable.getValueAt(ligne, 0).toString());
        int delete = 0;
        Customer customer = new Customer();
        CustomerController controller = new CustomerController();
        customer.setCustomerID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(customer);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayCustomerInfo();
                table(customerTable, customerName, customerAdress, customerPhone, customerDate);

            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteCustomerActionPerformed

    private void resetCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetCustomerActionPerformed
        reset(customerName, customerAdress, customerPhone, customerDate);
    }//GEN-LAST:event_resetCustomerActionPerformed

    private void historyTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyTableMouseReleased
        table(historyTable, customerCombo, productNatureCombo, unitCombo, pricecombo);
    }//GEN-LAST:event_historyTableMouseReleased

    private void customerComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customerComboMouseClicked

    }//GEN-LAST:event_customerComboMouseClicked

    private void productNatureComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_productNatureComboItemStateChanged
        Connection connection = DatabaseConnection.ConnectionDatabase.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select productPrice from "
                    + "product where productName=?");
            pst.setString(1, productNatureCombo.getSelectedItem().toString());
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Product product = new Product();
                product.setProductNature(res.getString("productPrice"));
                String price = product.getProductNature();
                pricecombo.setLabelText("Prix d'achat etait: " + price);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_productNatureComboItemStateChanged

    private void productNatureComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productNatureComboMouseClicked

    }//GEN-LAST:event_productNatureComboMouseClicked

    private void productNatureComboMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productNatureComboMouseExited

    }//GEN-LAST:event_productNatureComboMouseExited

    private void productNatureComboMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productNatureComboMouseReleased

    }//GEN-LAST:event_productNatureComboMouseReleased

    private void historyAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historyAddActionPerformed
        History history = new History();
        HistoryController controller = new HistoryController();
        int save = 0;
        if (unitCombo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "unit fiels is required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            history.setHistoryProduct(returnID(productNatureCombo, "product", "productID", "productName"));
            history.setHistoryCustomer(returnID(customerCombo, "customer", "customerID", "customerName"));
            history.setHistoryProductUnit(Integer.parseInt(unitCombo.getText()));
            history.setHistoryProductPrice(Integer.parseInt(pricecombo.getText()));
            save = controller.create(history);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                history();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_historyAddActionPerformed

    private void updatejistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatejistoryActionPerformed
        int update = 0;
        int ligne = historyTable.getSelectedRow();
        int id = Integer.parseInt(historyTable.getValueAt(ligne, 0).toString());
        History history = new History();
        HistoryController controller = new HistoryController();
        history.setHistoryProduct(returnID(productNatureCombo, "product", "productID", "productName"));
        history.setHistoryCustomer(returnID(customerCombo, "customer", "customerID", "customerName"));
        history.setHistoryProductUnit(Integer.parseInt(unitCombo.getText()));
        history.setHistoryProductPrice(Integer.parseInt(pricecombo.getText()));
        history.setHistoryID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna change this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            update = controller.update(history);

            if (update == 1) {
                JOptionPane.showMessageDialog(this, "Successfully changed!", "Success", JOptionPane.INFORMATION_MESSAGE);
                history();
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_updatejistoryActionPerformed

    private void deletehistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletehistoryActionPerformed
        int ligne = historyTable.getSelectedRow();
        int id = Integer.parseInt(historyTable.getValueAt(ligne, 0).toString());
        int delete = 0;
        History history = new History();
        HistoryController controller = new HistoryController();
        history.setHistoryID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(history);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                history();
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletehistoryActionPerformed

    private void refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshActionPerformed

        comboDisplay();
    }//GEN-LAST:event_refreshActionPerformed

    private void refresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refresh1ActionPerformed

    private void cmdSetupPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSetupPageActionPerformed
        setUpPage();
    }//GEN-LAST:event_cmdSetupPageActionPerformed

    private void cmdPrintImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintImageActionPerformed
        if (customerRadio.isSelected()) {
            printToImage(customerTable);
        } else if (productRadio.isSelected()) {
            printToImage(productTable);
        } else if (historyRadio.isSelected()) {
            printToImage(historyTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please Choose what to print", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cmdPrintImageActionPerformed

    private void cmdPrintPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintPdfActionPerformed
        if (customerRadio.isSelected()) {
            printToPdf(customerTable);
        } else if (productRadio.isSelected()) {
            printToPdf(productTable);
        } else if (historyRadio.isSelected()) {
            printToPdf(historyTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please Choose what to print", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cmdPrintPdfActionPerformed

    private void cmdPrintWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintWordActionPerformed
        if (customerRadio.isSelected()) {
            printToWord(customerTable);
        } else if (productRadio.isSelected()) {
            printToWord(productTable);
        } else if (historyRadio.isSelected()) {
            printToWord(historyTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please Choose what to print", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cmdPrintWordActionPerformed

    private void cmdPrintExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintExcelActionPerformed
        if (customerRadio.isSelected()) {
            printToExcel(customerTable);
        } else if (productRadio.isSelected()) {
            printToExcel(productTable);
        } else if (historyRadio.isSelected()) {
            printToExcel(historyTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please Choose what to print", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_cmdPrintExcelActionPerformed

    private void cmdPrintViewPageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdPrintViewPageActionPerformed
        try {
            new PrintTable().printTable(customerTable.getModel(), "History Report", pageFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_cmdPrintViewPageActionPerformed

    private void AddWorkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddWorkerActionPerformed
        Worker worker = new Worker();
        WorkerControllor controller = new WorkerControllor();
        int save = 0;
        if (workerFName.getText().trim().isEmpty()
                || workerLname.getText().trim().isEmpty()
                || workerAdress.getText().trim().isEmpty()
                || workerPhone.getText().trim().isEmpty()
                || workerSalary.getText().trim().isEmpty()
                || ((JTextField) workerDate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            worker.setWorkerFirstName(workerFName.getText());
            worker.setWorkerLastName(workerLname.getText());
            worker.setWorkerAdress(workerAdress.getText());
            worker.setWorkerPhone(workerPhone.getText());
            worker.setWorkerSalary(Integer.parseInt(workerSalary.getText()));
            worker.setWorkerDate(((JTextField) workerDate.getDateEditor().getUiComponent()).getText());
            save = controller.create(worker);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayWorkerInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddWorkerActionPerformed

    private void updateWorkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateWorkerActionPerformed
        int i = -1;
        while (i < 0) {
            String id = JOptionPane.showInputDialog(this, "Enter the ProductID");
            if (id.length() > 0) {
                i++;
                int update = 0;
                Worker worker = new Worker();
                WorkerControllor controller = new WorkerControllor();
                worker.setWorkerFirstName(workerFName.getText());
                worker.setWorkerLastName(workerLname.getText());
                worker.setWorkerAdress(workerAdress.getText());
                worker.setWorkerPhone(workerPhone.getText());
                worker.setWorkerSalary(Integer.parseInt(workerSalary.getText()));
                worker.setWorkerDate(((JTextField) workerDate.getDateEditor().getUiComponent()).getText());
                worker.setWorkerID(Integer.parseInt(id));
                int y = JOptionPane.showConfirmDialog(null, "Do you really wanna change this infos?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (y == JOptionPane.OK_OPTION) {
                    update = controller.update(worker);
                    if (update == 1) {
                        JOptionPane.showMessageDialog(null, "Successfully changed!");
                        displayWorkerInfo();
                    } else {
                        JOptionPane.showMessageDialog(null, "Action failed");
                    }
                }
            }
        }
    }//GEN-LAST:event_updateWorkerActionPerformed

    private void deleteWorkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteWorkerActionPerformed
        int ligne = workerTable.getSelectedRow();
        int id = Integer.parseInt(workerTable.getValueAt(ligne, 0).toString());
        int delete = 0;
        Worker worker = new Worker();
        WorkerControllor controller = new WorkerControllor();
        worker.setWorkerID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(worker);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayWorkerInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteWorkerActionPerformed

    private void resetWorkerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetWorkerActionPerformed
        workerFName.setText("");
        workerLname.setText("");
        workerAdress.setText("");
        workerPhone.setText("");
        workerSalary.setText("");
        workerDate.setDate(null);
    }//GEN-LAST:event_resetWorkerActionPerformed

    private void workerTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_workerTableMouseReleased
        int ligne = workerTable.getSelectedRow();
        workerFName.setText(workerTable.getValueAt(ligne, 1).toString());
        workerLname.setText(workerTable.getValueAt(ligne, 2).toString());
        workerAdress.setText(workerTable.getValueAt(ligne, 3).toString());
        workerPhone.setText(workerTable.getValueAt(ligne, 4).toString());
        workerSalary.setText(workerTable.getValueAt(ligne, 5).toString());
        JTextField date = (JTextField) workerDate.getDateEditor().getUiComponent();
        date.setText(workerTable.getValueAt(ligne, 6).toString());
    }//GEN-LAST:event_workerTableMouseReleased

    private void cmdSignInUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdSignInUpActionPerformed
        new SignUp(this, false).setVisible(true);
    }//GEN-LAST:event_cmdSignInUpActionPerformed

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye.png"))); // NOI18N
        txtPass.setEchoChar((char) 0);

    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseExited
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye_hide.png"))); // NOI18N
        txtPass.setEchoChar('*');
    }//GEN-LAST:event_jLabel6MouseExited

    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        Expense expense = new Expense();
        ExpenseControllor controller = new ExpenseControllor();
        int save = 0;
        if (expenseReason.getText().trim().isEmpty()
                || exepenseAmount.getText().trim().isEmpty()
                || expenseWorker.getSelectedItem().toString().trim().isEmpty()
                || expenseDetail.getText().trim().isEmpty()
                || ((JTextField) expensedate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            expense.setExpenseReason(expenseReason.getText());
            expense.setExpenseAmount(Integer.parseInt(exepenseAmount.getText()));
            expense.setExpenseWorker(returnID(expenseWorker, "worker", "workerID", "concat(workerFname,\" \",workerLname)"));
            expense.setExepenseDetails(expenseDetail.getText());
            expense.setExpenseDate(((JTextField) expensedate.getDateEditor().getUiComponent()).getText());
            save = controller.create(expense);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                initNoticeBoard();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_savebtnActionPerformed

    private void upadatebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upadatebtnActionPerformed
        int i = -1;
        while (i < 0) {
            String id = JOptionPane.showInputDialog(this, "Enter the ProductID");
            if (id.length() > 0) {
                i++;
                int update = 0;
                Expense expense = new Expense();
                ExpenseControllor controller = new ExpenseControllor();
                expense.setExpenseReason(expenseReason.getText());
                expense.setExpenseAmount(Integer.parseInt(exepenseAmount.getText()));
                expense.setExpenseWorker(expenseWorker.getSelectedItem().toString());
                expense.setExepenseDetails(expenseDetail.getText());
                expense.setExpenseDate(((JTextField) expensedate.getDateEditor().getUiComponent()).getText());
                expense.setExpenseID(Integer.parseInt(id));
                int y = JOptionPane.showConfirmDialog(null, "Do you really wanna change this infos?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (y == JOptionPane.OK_OPTION) {
                    update = controller.update(expense);
                    if (update == 1) {
                        JOptionPane.showMessageDialog(null, "Successfully changed!");
                        initNoticeBoard();
                    } else {
                        JOptionPane.showMessageDialog(null, "Action failed");
                    }
                }
            }
        }
    }//GEN-LAST:event_upadatebtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        int delete = 0;
        Expense expense = new Expense();
        ExpenseControllor controller = new ExpenseControllor();
//        expense.setExpenseID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(expense);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
//                displayExpenseInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void resetbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_resetbtnActionPerformed

    private void AddIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddIncomeActionPerformed
        Income income = new Income();
        IncomeControllor controller = new IncomeControllor();
        int save = 0;
        if (incomeSource.getText().trim().isEmpty()
                || incomeAmount.getText().trim().isEmpty()
                || ((JTextField) incomeDate.getDateEditor().getUiComponent()).getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            income.setIncomeSource(incomeSource.getText());
            income.setIncomeAmount(Integer.parseInt(incomeAmount.getText()));
            income.setIncomeDate(((JTextField) incomeDate.getDateEditor().getUiComponent()).getText());
            save = controller.create(income);
            if (save == 1) {
                JOptionPane.showMessageDialog(this, "Well done!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayIncomeInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Failed", "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_AddIncomeActionPerformed

    private void updateIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateIncomeActionPerformed
        int i = -1;
        while (i < 0) {
            String id = JOptionPane.showInputDialog(this, "Enter the ProductID");
            if (id.length() > 0) {
                i++;
                int update = 0;
                Income income = new Income();
                IncomeControllor controller = new IncomeControllor();
                income.setIncomeSource(incomeSource.getText());
                income.setIncomeAmount(Integer.parseInt(incomeAmount.getText()));
                income.setIncomeDate(((JTextField) incomeDate.getDateEditor().getUiComponent()).getText());
                income.setIncomeID(Integer.parseInt(id));
                int y = JOptionPane.showConfirmDialog(null, "Do you really wanna change this infos?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (y == JOptionPane.OK_OPTION) {
                    update = controller.update(income);
                    if (update == 1) {
                        JOptionPane.showMessageDialog(null, "Successfully changed!");
                        displayIncomeInfo();
                    } else {
                        JOptionPane.showMessageDialog(null, "Action failed");
                    }
                }
            }
        }
    }//GEN-LAST:event_updateIncomeActionPerformed

    private void resetIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetIncomeActionPerformed
        incomeSource.setText("");
        incomeAmount.setText("");
        incomeDate.setDate(null);
    }//GEN-LAST:event_resetIncomeActionPerformed

    private void deleteIncomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteIncomeActionPerformed
        int ligne = incomeTable.getSelectedRow();
        int id = Integer.parseInt(incomeTable.getValueAt(ligne, 0).toString());
        int delete = 0;
        Income income = new Income();
        IncomeControllor controller = new IncomeControllor();
        income.setIncomeID(id);
        int y = JOptionPane.showConfirmDialog(this, "Do you really wanna delete this infos?",
                "Confirmation", JOptionPane.YES_NO_OPTION);
        if (y == JOptionPane.OK_OPTION) {
            delete = controller.delete(income);
            if (delete == 1) {
                JOptionPane.showMessageDialog(this, "Successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayIncomeInfo();
            } else {
                JOptionPane.showMessageDialog(this, "Action failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_deleteIncomeActionPerformed

    private void incomeTableMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_incomeTableMouseReleased
        int ligne = workerTable.getSelectedRow();
        workerFName.setText(workerTable.getValueAt(ligne, 1).toString());
        workerLname.setText(workerTable.getValueAt(ligne, 2).toString());
        workerAdress.setText(workerTable.getValueAt(ligne, 3).toString());
        workerPhone.setText(workerTable.getValueAt(ligne, 4).toString());
        workerSalary.setText(workerTable.getValueAt(ligne, 5).toString());
        JTextField date = (JTextField) workerDate.getDateEditor().getUiComponent();
        date.setText(workerTable.getValueAt(ligne, 6).toString());
    }//GEN-LAST:event_incomeTableMouseReleased
    public void setUpPage() {
        PageConfigDialog dia = new PageConfigDialog(this, true);
        dia.setPageFormat(pageFormat);
        dia.setVisible(true);
        if (dia.getPageFormat() != null) {
            pageFormat = dia.getPageFormat();
        }
    }

    public void printToImage(JTable table) {
        File file = showFileChooser();
        if (file != null) {
            try {
                File f = new PrintTable().printTable(table.getModel(), "History Report", pageFormat).exportToImage(file);
                Desktop.getDesktop().open(f);
            } catch (IOException | DRException e) {
                e.printStackTrace();
            }
        }
    }

    public void printToWord(JTable table) {
        File file = showFileChooser();
        if (file != null) {
            try {
                File f = new PrintTable().printTable(table.getModel(), "History Report", pageFormat).exportToWord(file);
                Desktop.getDesktop().open(f);
            } catch (IOException | DRException e) {
                e.printStackTrace();
            }
        }
    }

    public void printToExcel(JTable table) {
        File file = showFileChooser();
        if (file != null) {
            try {
                File f = new PrintTable().printTable(table.getModel(), "History Report", pageFormat).exportToExcel(file);
                Desktop.getDesktop().open(f);
            } catch (IOException | DRException e) {
                e.printStackTrace();
            }
        }
    }

    public void printToPdf(JTable table) {
        File file = showFileChooser();
        if (file != null) {
            try {
                File f = new PrintTable().printTable(table.getModel(), "History Report", pageFormat).exportToPdf(file);
                Desktop.getDesktop().open(f);
            } catch (IOException | DRException e) {
                e.printStackTrace();
            }
        }
    }

    public void table(JTable table, JTextField t1, JTextField t2, JTextField t3, JDateChooser dateChooser) {
        int ligne = table.getSelectedRow();
        t1.setText(table.getValueAt(ligne, 1).toString());
        t2.setText(table.getValueAt(ligne, 2).toString());
        t3.setText(table.getValueAt(ligne, 3).toString());
        JTextField date = (JTextField) dateChooser.getDateEditor().getUiComponent();
        date.setText(table.getValueAt(ligne, 4).toString());
    }

    public void table(JTable table, JComboBox comboBox1, JComboBox comboBox2, JTextField t1, JTextField t2) {
        int ligne = table.getSelectedRow();
        comboBox1.setSelectedItem(table.getValueAt(ligne, 1).toString());
        comboBox2.setSelectedItem(table.getValueAt(ligne, 2).toString());
        t1.setText(table.getValueAt(ligne, 3).toString());
        t2.setText(table.getValueAt(ligne, 4).toString());
    }

    public void comboDisplay() {
        List<Customer> customerList = new CustomerController().displayCustomerInfo();
        for (Customer customer : customerList) {
            customerCombo.addItem(customer);
        }
        List<Product> productList = new ProductController().displayProductInfo();
        for (Product product : productList) {
            productNatureCombo.addItem(product);
        }
    }

    public void displayComboBoxWorker() {
        Connection connection = DatabaseConnection.ConnectionDatabase.getConnection();
        String sql = "select concat(workerFname,\" \",workerLname)as workerName from worker";
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                expenseWorker.addItem(res.getString("workerName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String returnID(JComboBox<Integer> combo, String tableName, String param, String key) {
        Connection connection = DatabaseConnection.ConnectionDatabase.getConnection();
        String price = "";
        try {
            PreparedStatement pst = connection.prepareStatement("select " + param + " from "
                    + tableName + " where " + key + "=?");
            pst.setString(1, combo.getSelectedItem().toString());
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Product product = new Product();
                product.setProductNature(res.getString(param));
                price = product.getProductNature();
                return price;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    private File showFileChooser() {
        JFileChooser ch = new JFileChooser();
        int opt = ch.showSaveDialog(this);
        if (opt == JFileChooser.APPROVE_OPTION) {
            return ch.getSelectedFile();
        } else {
            return null;
        }
    }

    private void initNoticeBoard() {
        Color[] colors = {new Color(94, 49, 238), new Color(218, 49, 238), new Color(32, 171, 43), new Color(50, 93, 215), new Color(27, 188, 204), new Color(238, 46, 57)};
        Connection connection = DatabaseConnection.ConnectionDatabase.getConnection();
        Random random = new Random();
        int colorIndex = random.nextInt(colors.length) + 1;
        String sql = "select expense.expenseID,expense.expenseReason,expense.expenseAmount,concat(workerFname,\" \",workerLname) as fullName,expense.expenseDetails,expense.expenseDate from expense,worker where expense.expenseWorker=worker.workerID order by expenseID desc";
//        noticeBoard.addDate("04/10/2021");
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(94, 49, 238), "Hidemode", "Now", "Sets the hide mode for the component. If the hide mode has been specified in the This hide mode can be overridden by the component constraint."));
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(218, 49, 238), "Tag", "2h ago", "Tags the component with metadata name that can be used by the layout engine. The tag can be used to explain for the layout manager what the components is showing, such as an OK or Cancel button."));
//        noticeBoard.addDate("03/10/2021");
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(32, 171, 43), "Further Reading", "12:30 PM", "There are more information to digest regarding MigLayout. The resources are all available at www.migcomponents.com"));
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(50, 93, 215), "Span", "10:30 AM", "Spans the current cell (merges) over a number of cells. Practically this means that this cell and the count number of cells will be treated as one cell and the component can use the space that all these cells have."));
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(27, 188, 204), "Skip ", "9:00 AM", "Skips a number of cells in the flow. This is used to jump over a number of cells before the next free cell is looked for. The skipping is done before this component is put in a cell and thus this cells is affected by it. \"count\" defaults to 1 if not specified."));
//        noticeBoard.addNoticeBoard(new ModelNoticeBoard(new Color(238, 46, 57), "Push", "7:15 AM", "Makes the row and/or column that the component is residing in grow with \"weight\". This can be used instead of having a \"grow\" keyword in the column/row constraints."));
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                noticeBoard.addDate("Reason for expense: " + res.getString("expenseReason") + ",Amount of money : " + res.getString("expenseAmount"));
                noticeBoard.addNoticeBoard(new ModelNoticeBoard(colors[colorIndex], " By " + res.getString("fullName"), res.getString("expenseDate"), res.getString("expenseDetails")));
                noticeBoard.scrollToTop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FlatDarkPurpleIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                SplashScreen sc = new SplashScreen(null, true);
                sc.setVisible(true);
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private SwingComponents.ButtonGradient AddCustomer;
    private SwingComponents.ButtonGradient AddIncome;
    private SwingComponents.ButtonGradient AddProduct;
    private SwingComponents.ButtonGradient AddWorker;
    private javax.swing.JPanel ExpensePanel;
    private com.raven.swing.Background background1;
    private SwingComponents.BorderGradientButton borderGradientButton3;
    private SwingComponents.ButtonGradient buttonGradient6;
    private chalome.chart.card.Card card1;
    private chalome.chart.card.Card card2;
    private chalome.chart.card.Card card3;
    private chalome.chart.card.Card card4;
    private chalome.chart.CurveLineChart chart;
    private javax.swing.JPanel chartsPanel;
    private SwingComponents.BorderGradientButton cmdPrintExcel;
    private SwingComponents.BorderGradientButton cmdPrintImage;
    private SwingComponents.BorderGradientButton cmdPrintPdf;
    private SwingComponents.BorderGradientButton cmdPrintViewPage;
    private SwingComponents.BorderGradientButton cmdPrintWord;
    private SwingComponents.BorderGradientButton cmdSetupPage;
    private com.raven.swing.Button cmdSignIn;
    private com.raven.swing.Button cmdSignInUp;
    private SwingComponents.TextField customerAdress;
    private SwingComponents.Combobox customerCombo;
    private com.toedter.calendar.JDateChooser customerDate;
    private SwingComponents.TextField customerName;
    private javax.swing.JPanel customerPanel;
    private SwingComponents.TextField customerPhone;
    private SwingComponents.RadioButtonCustom customerRadio;
    private SwingComponents.TableDark customerTable;
    private SwingComponents.ButtonGradient deleteCustomer;
    private SwingComponents.ButtonGradient deleteIncome;
    private SwingComponents.ButtonGradient deleteWorker;
    private SwingComponents.ButtonGradient deletebtn;
    private SwingComponents.BorderGradientButton deletehistory;
    private SwingComponents.TextField exepenseAmount;
    private SwingComponents.TextArea expenseDetail;
    private SwingComponents.TextField expenseReason;
    private SwingComponents.Combobox expenseWorker;
    private com.toedter.calendar.JDateChooser expensedate;
    private SwingComponents.TextAreaScroll expensedetails;
    private SwingComponents.BorderGradientButton historyAdd;
    private javax.swing.JPanel historyPanel;
    private SwingComponents.RadioButtonCustom historyRadio;
    private SwingComponents.TableDark historyTable;
    private SwingComponents.TextField incomeAmount;
    private com.toedter.calendar.JDateChooser incomeDate;
    private javax.swing.JPanel incomePanel;
    private SwingComponents.TextField incomeSource;
    private SwingComponents.TableDark incomeTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private SwingComponents.MaterialTabbed materialTabbed1;
    private noticeboard.Notice_Board noticeBoard;
    private com.raven.swing.PanelTransparent panelBody;
    private javax.swing.JPanel panelLogin;
    private chalome.panel.PanelShadow panelShadow1;
    private com.toedter.calendar.JDateChooser pdate;
    private SwingComponents.TextField pname;
    private SwingComponents.TextField pprice;
    private SwingComponents.TextField pricecombo;
    private SwingComponents.Combobox productNatureCombo;
    private javax.swing.JPanel productPanel;
    private SwingComponents.RadioButtonCustom productRadio;
    private SwingComponents.TableDark productTable;
    private SwingComponents.TextField punit;
    private SwingComponents.BorderGradientButton refresh;
    private SwingComponents.BorderGradientButton refresh1;
    private SwingComponents.ButtonGradient resetCustomer;
    private SwingComponents.ButtonGradient resetIncome;
    private SwingComponents.ButtonGradient resetProduct;
    private SwingComponents.ButtonGradient resetWorker;
    private SwingComponents.ButtonGradient resetbtn;
    private SwingComponents.ButtonGradient savebtn;
    private Login.UserLogin.PasswordField txtPass;
    private Login.UserLogin.TextField txtUser;
    private SwingComponents.TextField unitCombo;
    private SwingComponents.ButtonGradient upadatebtn;
    private SwingComponents.ButtonGradient updateCustomer;
    private SwingComponents.ButtonGradient updateIncome;
    private SwingComponents.ButtonGradient updateProduct;
    private SwingComponents.ButtonGradient updateWorker;
    private SwingComponents.BorderGradientButton updatejistory;
    private javax.swing.JPanel worker;
    private SwingComponents.TextField workerAdress;
    private com.toedter.calendar.JDateChooser workerDate;
    private SwingComponents.TextField workerFName;
    private SwingComponents.TextField workerLname;
    private SwingComponents.TextField workerPhone;
    private SwingComponents.TextField workerSalary;
    private SwingComponents.TableDark workerTable;
    // End of variables declaration//GEN-END:variables
}
