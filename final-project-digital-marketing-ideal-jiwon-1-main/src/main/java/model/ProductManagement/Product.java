/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;
import java.util.List;

import model.MarketModel.Market;
import model.OrderManagement.OrderItem;

/**
 *
 * @author kal bugrara
 */
public class Product {
    private String name;
    private int floorPrice;
    private int ceilingPrice;
    private int targetPrice;
    private float salesVolume; //Total Sales Quantity
    private Market market;
    ArrayList<OrderItem> orderitems;

    public Product(String n, int fp, int cp, int tp) {
        name = n;
        floorPrice = fp;
        ceilingPrice = cp;
        targetPrice = tp;
        salesVolume = 0;
        orderitems = new ArrayList<>();
        this.market = market;

    }

    public Product updateProduct(int fp, int cp, int tp) {
        floorPrice = fp;
        ceilingPrice = cp;
        targetPrice = tp;
        return this; // returns itself
    }

    public int getTargetPrice() {
        return targetPrice;
    }

    public void addOrderItem(OrderItem oi) {
        orderitems.add(oi);
    }
    public List<OrderItem> getOrderItems() {
        return orderitems;
    }
    public Market getMarket() {
        return market;
    }
    
    public void setMarket(Market market) {
        this.market = market;
    }
    
    // Number of item sales above target
    public int getNumberOfProductSalesAboveTarget() {
        int sum = 0;
        for (OrderItem oi : orderitems) {
            if (oi.isActualAboveTarget() == true)
                sum = sum + 1;
        }
        return sum;
    }

    public int getNumberOfProductSalesBelowTarget() {
        int sum = 0;
        for (OrderItem oi : orderitems) {
            
            if (oi.isActualBelowTarget() == true)
                sum = sum + 1;
        }
        return sum;
    }

    public boolean isProductAlwaysAboveTarget() {

        for (OrderItem oi : orderitems) {
            if (oi.isActualAboveTarget() == false)
                return false; //
        }
        return true;
    }
    // calculates the revenues gained or lost (in relation to the target)
    // For example, if target is at $2000 and actual is $2500 then revenue gained
    // is $500 above the expected target. If the actual is $1800 then the lose will
    // be $200
    // Add all these difference to get the total including wins and loses

    public int getOrderPricePerformance() {
        int sum = 0;
        for (OrderItem oi : orderitems) {
            sum = sum + oi.calculatePricePerformance(); // positive and negative values
        }
        return sum;
    }

    public int getSalesVolume() {
        int sum = 0;
        for (OrderItem oi : orderitems) {
            sum = sum + oi.getOrderItemTotal(); // positive and negative values
        }
        return sum;
    }

    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (OrderItem oi : orderitems) {
            totalQuantity = totalQuantity + oi.getQuantity();
        }
        return totalQuantity;
    }

    public float getAveragePrice() {
        if (getTotalQuantity() == 0)
            return 0;
        return (float) getSalesVolume() / getTotalQuantity();
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getFloorPrice() {
        return this.floorPrice;
    }

    public int getCeilingPrice() {
        return ceilingPrice;
    }
    public void addSales(float sales) {
        this.salesVolume += sales;
    }
}
