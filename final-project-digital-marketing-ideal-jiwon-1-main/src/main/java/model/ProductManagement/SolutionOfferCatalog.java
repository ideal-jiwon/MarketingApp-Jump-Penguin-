/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.ProductManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.MarketModel.Market;
import model.MarketModel.MarketChannelAssignment;

/**
 * Manages the catalog of solution offers.
 */
public class SolutionOfferCatalog {
    private ArrayList<SolutionOffer> solutionOffers;
    private List<MarketChannelAssignment> marketChannelAssignments;

    public SolutionOfferCatalog(List<MarketChannelAssignment> marketChannelAssignments) {
        this.solutionOffers = new ArrayList<>();
        this.marketChannelAssignments = marketChannelAssignments;
    }

    public void loadSolutionsFromCSV(String filePath, List<SolutionOffer> solutionOfferList, ProductCatalog productCatalog) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length < 4) {
                    System.err.println("Skipping invalid row: " + line);
                    continue;
                }

                try {
                    String marketName = values[0].trim();
                    String details = values[1].trim(); // Read the details field
                    int price = Integer.parseInt(values[3].trim()); // Parse price as an integer

                    MarketChannelAssignment marketChannelAssignment = findMarketChannelAssignmentByName(marketName);
                    if (marketChannelAssignment != null) {
                        SolutionOffer solutionOffer = new SolutionOffer(marketChannelAssignment,details);
                        solutionOffer.setPrice(price);
                        solutionOfferList.add(solutionOffer);

                        // Associate products with the solution
                        //for (Product product : productCatalog.getProductList()) {
                        //    if (product.getMarket().getName().equalsIgnoreCase(marketName)) {
                         //       solutionOffer.addProduct(product);
                         //   }
                        //}
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Skipping row due to invalid price: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading solution offers from CSV: " + e.getMessage());
        }
    }

    private MarketChannelAssignment findMarketChannelAssignmentByName(String marketName) {
        for (MarketChannelAssignment assignment : marketChannelAssignments) {
            if (assignment.getMarket().getName().equalsIgnoreCase(marketName)) {
                return assignment;
            }
        }
        return null;
    }

    public ArrayList<SolutionOffer> getSolutionOffers() {
        return solutionOffers;
    }
    public SolutionOffer getSolutionOfferForMarket(Market market) {
    for (SolutionOffer solution : solutionOffers) {
        if (solution.getMarketChannelComb().getMarket().equals(market)) {
            return solution;
        }
    }
    return null; // No solution found for the market
}
public List<SolutionOffer> getSolutionOffersForMarket(Market market) {
    List<SolutionOffer> marketSpecificOffers = new ArrayList<>();

    for (SolutionOffer solutionOffer : solutionOffers) {
        if (solutionOffer.getMarketChannelComb().getMarket().equals(market)) {
            marketSpecificOffers.add(solutionOffer);
        }
    }

    return marketSpecificOffers;
}
    public List<SolutionOffer> getSolutionOffersForMarketAndChannel(String marketName, String channelName) {
        List<SolutionOffer> marketChannelSpecificOffers = new ArrayList<>();

        for (SolutionOffer solutionOffer : solutionOffers) {
          MarketChannelAssignment assignment = solutionOffer.getMarketChannelComb();
          if (assignment.getMarket().getName().equalsIgnoreCase(marketName) &&
             assignment.getChannel().getName().equalsIgnoreCase(channelName)) {
             marketChannelSpecificOffers.add(solutionOffer);
        }
    }

    return marketChannelSpecificOffers;
}

}
