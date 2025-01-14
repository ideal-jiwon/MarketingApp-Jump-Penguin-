/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;
import model.ProductManagement.SolutionOffer;
import model.ProductManagement.SolutionOfferCatalog;

/**
 *
 * @author kal bugrara
 */
public class MarketCatalog {
    private List<Market> marketList;
    
        public MarketCatalog(){
            this.marketList = new ArrayList<>();
        }
        public Market newMarket(String name){
            Market market = new Market(name);
            marketList.add(market);
            return market;
        
        }
        public Market findMarket(String marketID) {
            for (Market market : marketList) {
                if (market.getName().equals(marketID)) {
                    return market;
                }
            }
            return null;
        }
        public List<Market> getMarketList() {
            return marketList;
        }
        
        public void setMarketList(List<Market> marketList) {
            this.marketList = marketList;
        }

        // Method to load suppliers from a CSV file
        public void MarketFromCSV(String filePath) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
    
                // Skip the header line
                br.readLine();
    
                // Read and process each line
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
    
                    // Extract market data
                    String marketName = values[0].trim(); 
    
                    // Create a new Market and set its ProductCatalog
                    Market market = new Market(marketName);
                    //market.setProductCatalog(new ProductCatalog(productCatalogType));
    
                    // Add the market to the list
                    marketList.add(market);
            }
        } catch (IOException e) {
            System.err.println("Error loading markets from CSV: " + e.getMessage());
        }
    }
    public void generateAdvertisingEfficiencyReport(
    List<MarketChannelAssignment> assignments,
    SolutionOfferCatalog solutionOfferCatalog,
    ProductCatalog productCatalog
) {
    System.out.println("\nAdvertising Efficiency Report");
    System.out.printf("%-15s | %-15s | %-25s | %-15s | %-15s | %-15s%n",
        "Solution Offer-------", "Channel", "Channel", "Ad Cost", "Sales Revenue", "Ad Efficiency (%)");

    // Grouping by Solution Offer -> Market -> Channel
    Map<String, Map<String, Map<String, double[]>>> reportData = new HashMap<>();


    for (Market market : this.getMarketList()) {
        for (Channel channel : assignments.stream()
                                          .filter(a -> a.getMarket().equals(market))
                                          .map(MarketChannelAssignment::getChannel)
                                          .distinct()
                                          .toList()) {
            for (SolutionOffer solutionOffer : solutionOfferCatalog.getSolutionOffersForMarket(market)) {
                int totalOrders = 0;
                int totalSalesRevenue = 0;

                // Calculate total sales and orders for this solution offer
                for (Product product : productCatalog.getProductList()) {
                    if (product.getMarket().equals(market)) {
                        totalOrders += product.getTotalQuantity();
                        totalSalesRevenue += product.getSalesVolume();
                    }
                }

                // Get advertising cost from MarketChannelAssignment
                MarketChannelAssignment assignment = MarketChannelAssignment.findMarketChannelAssignment(
                    market, channel, assignments);
                double advertisingCost = assignment != null ? 
                    assignment.calculateAdvertisingCost(totalOrders) : 0;

                // Create a unique key for grouping
                //String key = market.getName() + "|" + channel.getName() + "|" + solutionOffer.getDetails();

                // Organize data in the nested map
                reportData.putIfAbsent(solutionOffer.getDetails(), new HashMap<>());
                Map<String, Map<String, double[]>> marketData = reportData.get(solutionOffer.getDetails());

                marketData.putIfAbsent(market.getName(), new HashMap<>());
                Map<String, double[]> channelData = marketData.get(market.getName());

                channelData.put(channel.getName(), new double[] { advertisingCost, totalSalesRevenue });
            }
        }
    }


    // Print the results
    for (String solutionOffer : reportData.keySet()) {
        for (String market : reportData.get(solutionOffer).keySet()) {
            for (String channel : reportData.get(solutionOffer).get(market).keySet()) {
                double[] values = reportData.get(solutionOffer).get(market).get(channel);
                double advertisingCost = values[0];
                int salesRevenue = (int) values[1];
                double adEfficiency = advertisingCost > 0 ? (salesRevenue / advertisingCost) * 100 : 0;

                System.out.printf("%-25s | %-15s | %-15s | %-15.2f | %-15d | %-15.2f%n",
                    solutionOffer, market, channel, advertisingCost, salesRevenue, adEfficiency);
            }
        }
    }
}

// Helper class for grouping and accumulation
class AdvertisingData {
    private String market;
    private String channel;
    private String solutionOffer;
    private double advertisingCost;
    private int salesRevenue;

    public AdvertisingData(String market, String channel, String solutionOffer) {
        this.market = market;
        this.channel = channel;
        this.solutionOffer = solutionOffer;
        this.advertisingCost = 0;
        this.salesRevenue = 0;
    }

    public void addAdvertisingCost(double cost) {
        this.advertisingCost += cost;
    }

    public void addSalesRevenue(int revenue) {
        this.salesRevenue += revenue;
    }

    public String getMarket() {
        return market;
    }

    public String getChannel() {
        return channel;
    }

    public String getSolutionOffer() {
        return solutionOffer;
    }

    public double getAdvertisingCost() {
        return advertisingCost;
    }

    public int getSalesRevenue() {
        return salesRevenue;
    }
}
public List<String> getMarketNames() {
    List<String> marketNames = new ArrayList<>();
    for (Market market : marketList) {
        marketNames.add(market.getName());
    }
    return marketNames;
}
}

