/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.OrderManagement;

import model.MarketModel.MarketChannelAssignment;
import model.ProductManagement.Product;
import model.ProductManagement.*;

/**
 *
 * @author kal bugrara
 */
public class OrderItem {

    Order order;
    Product selectedproduct;
    int actualPrice;
    int quantity;
    public MarketChannelAssignment marketChannelAssignment;

    public OrderItem(Product p, int paidprice, int q, Order o, MarketChannelAssignment mca) {
        selectedproduct = p;
        p.addOrderItem(this); // make sure product links back to the item
        quantity = q;
        this.actualPrice = paidprice;
        order = o;
        this.marketChannelAssignment = mca;
    }
    public MarketChannelAssignment getMarketChannelAssignment() {
        return marketChannelAssignment;
    }
    public int getOrderItemTotal() {
        return selectedproduct.getFloorPrice() * quantity;
    }

    // The following calculates what the price gain would have been if products were
    // sold at target price
    public int getOrderItemTargetTotal() {
        return selectedproduct.getTargetPrice() * quantity;
    }

    // returns positive if seller is making higher margin than target
    // returns negative if seller is making lower margin than target
    // otherwise zero meaning neutral
    public int calculatePricePerformance() {
        return (actualPrice - selectedproduct.getTargetPrice()) * quantity;
    }

    public boolean isActualAboveTarget() {
        if (actualPrice > selectedproduct.getTargetPrice()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isActualBelowTarget() {
        if (actualPrice < selectedproduct.getTargetPrice()) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isActualATTarget() {
        if (actualPrice == selectedproduct.getTargetPrice()) {
            return true;
        } else {
            return false;
        }

    }

    public Product getSelectedProduct() {
        return selectedproduct;
    }

    public int getActualPrice() {
        return actualPrice;

    }

    public int getQuantity() {
        return quantity;
    }

}
