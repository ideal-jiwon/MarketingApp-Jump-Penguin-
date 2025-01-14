/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import model.OrderManagement.OrderItem;
import model.ProductManagement.ProductCatalog;


public class MarketChannelAssignment {
    Market market;
    Channel channel;
    double nLessThan100;
    double nLessThan1000;
    double nLessThan10000;

    public MarketChannelAssignment(Market m, Channel c) {
        this.market = m;
        this.channel = c;
    }
    private static List<MarketChannelAssignment> assignments = new ArrayList<>();

    public static List<MarketChannelAssignment> getAssignments() {
        return assignments;
    }
    public double calculateAdvertisingCost(int numberOfOrders) {
        if (numberOfOrders < 100) {
            return 3 * numberOfOrders;
        } else if (numberOfOrders >= 100 && numberOfOrders < 1000) {
            return 1.5 * numberOfOrders;
        } else { // numberOfOrders >= 1000
            return 1 * numberOfOrders;
        }
    }

    // Getters and setters for nLessThan100, nLessThan1000, nLessThan10000
    public void setnLessThan100(double nLessThan100) {
        this.nLessThan100 = nLessThan100;
    }

    public void setnLessThan1000(double nLessThan1000) {
        this.nLessThan1000 = nLessThan1000;
    }

    public void setnLessThan10000(double nLessThan10000) {
        this.nLessThan10000 = nLessThan10000;
    }
    // Public getter for market
    public Market getMarket() {
        return market;
    }

    // Public getter for channel
    public Channel getChannel() {
        return channel;
    }


    public static List<MarketChannelAssignment> loadAssignmentsFromCSV(
            String filePath, MarketCatalog marketCatalog, ChannelCatalog channelCatalog) {

        List<MarketChannelAssignment> assignments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String marketId = values[0].trim();
                String channelId = values[1].trim();
                double nLessThan100 = Double.parseDouble(values[2].trim());
                double nLessThan1000 = Double.parseDouble(values[3].trim());
                double nLessThan10000 = Double.parseDouble(values[4].trim());

                Market market = marketCatalog.findMarket(marketId);
                Channel channel = channelCatalog.findChannel(channelId);

                if (market != null && channel != null) {
                    MarketChannelAssignment assignment = new MarketChannelAssignment(market, channel);
                    assignment.setnLessThan100(nLessThan100);
                    assignment.setnLessThan1000(nLessThan1000);
                    assignment.setnLessThan10000(nLessThan10000);
                    assignments.add(assignment);
                } else {
                    System.err.println("Error: Market or Channel not found for line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading assignments from CSV: " + e.getMessage());
        }

        return assignments;
    }
    public static List<MarketChannelAssignment> generateMarketChannelAssignments(
            MarketCatalog marketCatalog, ChannelCatalog channelCatalog) {
        
        assignments.clear();

        List<MarketChannelAssignment> assignments = new ArrayList<>();
        
        for (Market market : marketCatalog.getMarketList()) {
            for (Channel channel : channelCatalog.getChannelList()) {
                Random random = new Random();
                if (random.nextBoolean()) {
                MarketChannelAssignment assignment = new MarketChannelAssignment(market, channel);
                assignment.setnLessThan100(2 + random.nextDouble() * 2); // Base of 2, randomized up to 4
                assignment.setnLessThan1000(1 + random.nextDouble() * 1.5); // Base of 1, randomized up to 2.5
                assignment.setnLessThan10000(0.5 + random.nextDouble() * 1);
                assignments.add(assignment);
            }
        }
    }
        return assignments;
    }
    public static MarketChannelAssignment findMarketChannelAssignment(Market market, Channel channel,
                                                                   List<MarketChannelAssignment> assignments) {
    for (MarketChannelAssignment assignment : assignments) {
        if (assignment.market.equals(market) && assignment.channel.equals(channel)) {
            return assignment;
        }
    }
    return null;
}   
public static void displayAdvertisingCostBreakdown(
        ProductCatalog productCatalog,
        List<MarketChannelAssignment> assignments,
        List<Market> markets,
        List<Channel> channels) {

    Map<String, Map<String, Double>> advertisingCosts = new HashMap<>();
    Map<String, Double> marketTotals = new HashMap<>();
    Map<String, Double> channelTotals = new HashMap<>();
    double grandTotal = 0.0;

    // Calculate advertising costs
    for (Channel channel : channels) {
        String channelName = channel.getName(); // Group by channel name
        Map<String, Double> marketCosts = advertisingCosts.getOrDefault(channelName, new HashMap<>());

        for (Market market : markets) {
            String marketName = market.getName(); // Group by market name

            // Retrieve the total sales volume for this market and channel
            int salesVolume = productCatalog.getSalesVolumeByMarketAndChannel(market, channel);
            MarketChannelAssignment assignment = findMarketChannelAssignment(market, channel, assignments);

            double cost = 0.0;
            if (assignment != null) {
                cost = assignment.calculateAdvertisingCost(salesVolume);
            }

            // Aggregate costs by market and channel
            marketCosts.put(marketName, marketCosts.getOrDefault(marketName, 0.0) + cost);
            marketTotals.put(marketName, marketTotals.getOrDefault(marketName, 0.0) + cost);
            channelTotals.put(channelName, channelTotals.getOrDefault(channelName, 0.0) + cost);
            grandTotal += cost;
        }

        advertisingCosts.put(channelName, marketCosts);
    }

    // Format numbers as currency
    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);

    // Print column headers for markets
    System.out.println( "\nAdvertising Cost Breakdown:");
    System.out.printf("%-15s", "Channel/Market");
    for (String marketName : marketTotals.keySet()) {
        System.out.printf("%-15s", marketName);
    }
    System.out.printf("%-15s%n", "Total");

    // Print rows for each channel
    for (String channelName : channelTotals.keySet()) {
        System.out.printf("%-15s", channelName);
        Map<String, Double> marketCosts = advertisingCosts.get(channelName);

        for (String marketName : marketTotals.keySet()) {
            double cost = marketCosts.getOrDefault(marketName, 0.0);
            System.out.printf("%-15s", currencyFormatter.format(cost));
        }
        System.out.printf("%-15s%n", currencyFormatter.format(channelTotals.get(channelName)));
    }

    // Print totals row for each market
    System.out.printf("%-15s", "Total");
    for (String marketName : marketTotals.keySet()) {
        System.out.printf("%-15s", currencyFormatter.format(marketTotals.get(marketName)));
    }
    System.out.printf("%-15s%n", currencyFormatter.format(grandTotal));

    // Print grand total
    System.out.printf("%-15s%-15s%n", "Grand total", currencyFormatter.format(grandTotal));
}

    public static double calculateAdvertisingCostForMarket(Market market, int numberOfOrders) {
        if (numberOfOrders < 100) {
            return 3 * numberOfOrders;
        } else if (numberOfOrders >= 100 && numberOfOrders < 1000) {
            return 1.5 * numberOfOrders;
        } else {
            return 1 * numberOfOrders;
        }
}

}