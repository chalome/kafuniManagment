package Models;

import java.util.Date;

public class Product {

    private int productID;
    private String productNature;
    private int productUnit;
    private int productPrice;
    private String productDate;

    public Product(int productID, String productNature, int productUnit, int productPrice) {
        this.productID = productID;
        this.productNature = productNature;
        this.productUnit = productUnit;
        this.productPrice = productPrice;
    }

    public Product(String productNature, int productUnit, int productPrice,String productDate) {
        this.productNature = productNature;
        this.productUnit = productUnit;
        this.productPrice = productPrice;
        this.productDate=productDate;
    }

    public Product() {
    }

    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductNature() {
        return productNature;
    }

    public void setProductNature(String productNature) {
        this.productNature = productNature;
    }

    public int getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(int productUnit) {
        this.productUnit = productUnit;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return productNature; 
    }
    

}
