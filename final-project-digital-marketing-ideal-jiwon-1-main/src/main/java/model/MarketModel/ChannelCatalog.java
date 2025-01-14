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

import model.OrderManagement.OrderItem;
import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;

/**
 *
 * @author kal bugrara
 */
public class ChannelCatalog {
    private List<Channel> channelList;
    
        public ChannelCatalog(){
            this.channelList = new ArrayList<>();
        }
        public Channel newChannel(String name){
            Channel channel = new Channel(name);
            channelList.add(channel);
            return channel;
        }
        public Channel findChannel(String channelID) {
            for (Channel channel : channelList) {
                if (channel.getName().equals(channelID)) {
                    return channel;
                }
            }
            return null;
        }
        public List<Channel> getChannelList() {
            return channelList;
        }
        
        public void setChannelList(List<Channel> channelList) {
            this.channelList = channelList;
        }
        // Method to load suppliers from a CSV file
        public void ChannelFromCSV(String filePath) {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
    
                // Skip the header line
                br.readLine();
    
                // Read and process each line
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
    
                    // Extract market data
                    String channelName = values[1].trim(); 
    
                    // Create a new Market and set its ProductCatalog
                    Channel channel = new Channel(channelName);
                    //market.setProductCatalog(new ProductCatalog(productCatalogType));
    
                    // Add the market to the list
                    channelList.add(channel);
            }
        } catch (IOException e) {
            System.err.println("Error loading markets from CSV: " + e.getMessage());
        }
    }
    public void generateChannelProfitabilityReport(
        List<MarketChannelAssignment> assignments,
        ProductCatalog productCatalog
    ) {
        System.out.println("\nChannel Profitability Report");
        System.out.printf("%-15s | %-15s | %-10s | %-10s | %-10s  | %-15s%n",
             "Channel", "Sold Qty", "Revenue", "Ad Cost", "Profit", "Profit %");
        // Store channel data for ranking
        List<Map<String, Object>> channelData = new ArrayList<>();
        // Remove duplicates by using a Set
        Set<String> uniqueChannels = new HashSet<>();
        
        int totalQuantity = 0;
        int totalRevenue = 0;
        double totalAdCost = 0;

        for (Channel channel : channelList) {
            if (uniqueChannels.contains(channel.getName())) {
                continue; // Skip duplicate channel entries
            }
            uniqueChannels.add(channel.getName());
    
            //int totalQuantity = 0;
            //int totalRevenue = 0;
            //double totalAdCost = 0;

            for (MarketChannelAssignment assignment : assignments) {
                if (assignment.getChannel().equals(channel)) {
                    Market market = assignment.getMarket();
    
                    // Loop through products associated with the market
                for (Product product : productCatalog.getProductList()) {
                    if (product.getMarket().equals(market)) {
                        int quantity = product.getTotalQuantity();
                        double revenue = product.getSalesVolume(); 
                        double adCost = assignment.calculateAdvertisingCost(quantity);

                        totalQuantity += quantity;
                        totalRevenue += revenue;
                        totalAdCost += adCost;
                    }
                }
            }
        }
        // Calculate profit and profitability
        double profit = totalRevenue - totalAdCost;
        double profitability = (totalAdCost != 0) ? (profit / totalAdCost) * 100 : 0;

        // Add channel data to list for ranking
        Map<String, Object> channelMetrics = new HashMap<>();
        channelMetrics.put("channel", channel.getName());
        channelMetrics.put("quantity", totalQuantity);
        channelMetrics.put("revenue", totalRevenue);
        channelMetrics.put("adCost", totalAdCost);
        channelMetrics.put("profit", profit);
        channelMetrics.put("profitability", profitability);
        channelData.add(channelMetrics);
    }

    // Sort by profitability in descending order
    channelData.sort((a, b) -> Double.compare((double) b.get("profitability"), (double) a.get("profitability")));

    // Print sorted channel data
    int rank = 1;
    for (Map<String, Object> data : channelData) {
        System.out.printf("%-15s | %-10d | %-10d | %-10.2f | %-10.2f | %-10.2f%n",
            rank + ". " + data.get("channel"),
            (int) data.get("quantity"),
            (int) data.get("revenue"),
            (double) data.get("adCost"),
            (double) data.get("profit"),
            (double) data.get("profitability"));
        rank++;
    }
}
}

     
