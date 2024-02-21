
package Models;
public class History {
    private int historyID;
    private String historyProduct;
    private String historyCustomer;
    private int HistoryProductUnit;
    private int historyProductPrice;
    private String historyDate;

    public History() {
    }
    
    

    public int getHistoryID() {
        return historyID;
    }

    public void setHistoryID(int historyID) {
        this.historyID = historyID;
    }

    public String getHistoryProduct() {
        return historyProduct;
    }

    public void setHistoryProduct(String historyProduct) {
        this.historyProduct = historyProduct;
    }

    public String getHistoryCustomer() {
        return historyCustomer;
    }

    public void setHistoryCustomer(String historyCustomer) {
        this.historyCustomer = historyCustomer;
    }

    public int getHistoryProductUnit() {
        return HistoryProductUnit;
    }

    public void setHistoryProductUnit(int HistoryProductUnit) {
        this.HistoryProductUnit = HistoryProductUnit;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    /**
     * @return the historyProductPrice
     */
    public int getHistoryProductPrice() {
        return historyProductPrice;
    }

    /**
     * @param historyProductPrice the historyProductPrice to set
     */
    public void setHistoryProductPrice(int historyProductPrice) {
        this.historyProductPrice = historyProductPrice;
    }

    
    
}
