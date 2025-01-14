/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.util.ArrayList;

import model.MarketModel.MarketChannelAssignment;

/**
 * Represents a solution offer that bundles products for a specific market and channel.
 */
public class SolutionOffer {
    private String details;
    private ArrayList<Product> products; // List of products in this solution
    private int price;                   // Solution price
    private MarketChannelAssignment marketChannelComb; // Associated market-channel combination

    public SolutionOffer(MarketChannelAssignment marketChannelComb, String details) {
        this.marketChannelComb = marketChannelComb;
        this.details = details;
        this.products = new ArrayList<>();
    }

    /**
     * Add a product to the solution offer.
     * 
     * @param product The product to add.
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Set the price of the solution offer.
     * 
     * @param price The price to set.
     */
    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getPrice() {
        return price;
    }

    public MarketChannelAssignment getMarketChannelComb() {
        return marketChannelComb;
    }
    public String getDetails() {
        return details;
    }
    @Override
    public String toString() {
        return "Details: " + details + ", Price: " + price;
    }
}