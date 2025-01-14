package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


import model.Business.Business;
import model.CustomerManagement.CustomerDirectory;
import model.CustomerManagement.CustomerProfile;
import model.MarketModel.Channel;
import model.MarketModel.ChannelCatalog;
import model.MarketModel.Market;
import model.MarketModel.MarketCatalog;
import model.MarketModel.MarketChannelAssignment;
import model.OrderManagement.Order;
import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;
import model.ProductManagement.SolutionOffer;
import model.ProductManagement.SolutionOfferCatalog;



public class DigitalMarketingApplication {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void main(String[] args) {

        System.out.println(RED + "PART01-1. Populating Digital Marketing Business Model");
        System.out.println();
        Business jumpPenguin = new Business(RED + "jumpPenguin");
        System.out.println();

        // Load Markets
        String marketCatalogPath = "/workspaces/final-project-digital-marketing-ideal-jiwon-1/.devcontainer/Car_Products.csv";
        MarketCatalog marketCatalog = new MarketCatalog();
        marketCatalog.MarketFromCSV(marketCatalogPath);
        List<Market> allMarkets = marketCatalog.getMarketList();
        Set<String> uniqueMarketNames = new HashSet<>();
        for (Market market : marketCatalog.getMarketList()) {
            uniqueMarketNames.add(market.getName());

}
        System.out.println(GREEN + "Total Markets loaded: " + uniqueMarketNames.size());

        // Load Channels
        String channelCatalogPath = "/workspaces/final-project-digital-marketing-ideal-jiwon-1/.devcontainer/Channel.csv";
        ChannelCatalog channelCatalog = new ChannelCatalog();
        channelCatalog.ChannelFromCSV(channelCatalogPath);
        List<Channel> allChannels = channelCatalog.getChannelList();
        Set<String> uniqueChannelNames = new HashSet<>(allChannels.stream().map(Channel::getName).toList());
        //for (Channel channel : channelCatalog.getChannelList()) {
        System.out.println(YELLOW + "Total Channels loaded: " + uniqueChannelNames.size());
    
    // Generate Market-Channel Assignments
    List<MarketChannelAssignment> marketChannelAssignments =
           MarketChannelAssignment.generateMarketChannelAssignments(marketCatalog, channelCatalog);

    // Load Products
    String productFilePath = "/workspaces/final-project-digital-marketing-ideal-jiwon-1/.devcontainer/Car_Products.csv";
    ProductCatalog productCatalog = new ProductCatalog("ProductCatalog");
    productCatalog.loadProductsFromCSV(productFilePath, allMarkets);
    System.out.println(BLUE + "Products loaded for Markets:");

    for (Market market : allMarkets) {
        for (Product product : market.getProductList()) {
            System.out.printf("%-20s | %-20s | %-10d | %-10d | %-10d%n",
                    market.getName(),
                    product.getName(),
                    product.getCeilingPrice(),
                    product.getFloorPrice(),
                    product.getTargetPrice());
        }
    }
    // Load Customers
    String customerFilePath = "/workspaces/final-project-digital-marketing-ideal-jiwon-1/.devcontainer/Customer_Car.csv";
    CustomerDirectory customerDirectory = new CustomerDirectory();
    customerDirectory.loadCustomersFromCSV(customerFilePath);
    List<CustomerProfile> allCustomers = customerDirectory.getCustomerlist();
    System.out.println(GREEN + "Total Customers loaded: " + allCustomers.size());

     // Create Orders
     System.out.println(GREEN + "Creating Random Orders");
     Random random = new Random();
     for (CustomerProfile customer : allCustomers) {
         int numberOfOrders = random.nextInt(3) + 1; // 1-3 orders
         for (int i = 0; i < numberOfOrders; i++) {
             Order order = new Order(customer);

             Market market = allMarkets.get(random.nextInt(allMarkets.size()));
             Channel channel = allChannels.get(random.nextInt(allChannels.size()));
             MarketChannelAssignment assignment =
                     MarketChannelAssignment.findMarketChannelAssignment(market, channel, marketChannelAssignments);

             if (assignment == null) continue;

             int numberOfItems = random.nextInt(3) + 1; // 1-5 items
             for (int j = 0; j < numberOfItems; j++) {
                 Product randomProduct = productCatalog.getProductList()
                         .get(random.nextInt(productCatalog.getProductList().size()));
                 int price = randomProduct.getTargetPrice();
                 int quantity = random.nextInt(3) + 1;

                 order.newOrderItem(randomProduct, price, quantity, assignment);
                 // Increment sales for the product
                 randomProduct.addSales(quantity);

                 // Increment sales volume for debugging
                 int salesVolume = randomProduct.getSalesVolume();
                 System.out.println("Market: " + market.getName() +
                    " Channel: " + channel.getName() +
                    " Product: " + randomProduct.getName() +
                    " Sales Volume: " + salesVolume);
             }
             customer.addCustomerOrder(order);
         }
     }
     // Display Advertising Cost Breakdown
     System.out.println(RED);
     MarketChannelAssignment.displayAdvertisingCostBreakdown(
        productCatalog,
        marketChannelAssignments,
        allMarkets,
        allChannels);

     // Load Solution Offers
     System.out.println(YELLOW+"Solution Offers loading --------------------------");
     String solutionOfferPath = "/workspaces/final-project-digital-marketing-ideal-jiwon-1/.devcontainer/Solution_Offer.csv";
     System.out.println("Loading solutions from: " + solutionOfferPath);
     SolutionOfferCatalog solutionOfferCatalog = new SolutionOfferCatalog(marketChannelAssignments);
     solutionOfferCatalog.loadSolutionsFromCSV(solutionOfferPath, solutionOfferCatalog.getSolutionOffers(), productCatalog);

     // Display loaded solution offers
     List<SolutionOffer> solutionOffers = solutionOfferCatalog.getSolutionOffers();
     System.out.println(YELLOW + "Total Solutions loaded: " + solutionOffers.size());
     for (SolutionOffer solution : solutionOffers) {
         System.out.println("Market-Channel: " + solution.getMarketChannelComb().getMarket().getName() +
                            " | Price: " + solution.getPrice());
         System.out.println("| Details: " + solution.getDetails());
     }

     // Create Market Profitability Report 
        System.out.println(GREEN);

    // Retrieve products with sales below the threshold
        List<Product> lowSalesProducts = productCatalog.getLowSalesProducts(250);

    // Use a Set to ensure uniqueness
        Set<Product> uniqueLowSalesProducts = new HashSet<>(lowSalesProducts);
        System.out.println("The list of the products require Solution Offer with low sales");
        System.out.println("Products with sales below threshold (250): " + uniqueLowSalesProducts.size());
        for (Product product : uniqueLowSalesProducts) {
            Market market = product.getMarket();
            SolutionOffer solutionOffer = solutionOfferCatalog.getSolutionOfferForMarket(market);
        
            if (solutionOffer != null) {
               System.out.println("Triggering Solution Offer for Product: " + product.getName());
               System.out.println("Solution Offer Details: " + solutionOffer.getDetails() + " | Price: " + solutionOffer.getPrice());
            }
        }
        generateComparisonReport(marketCatalog, productCatalog, solutionOfferCatalog, channelCatalog,marketChannelAssignments);
        DigitalMarketingAppMenu.displayAppMenu(marketCatalog, channelCatalog, solutionOfferCatalog, productCatalog,marketChannelAssignments);
        DigitalMarketingReportsInterface.displayReports(marketCatalog, channelCatalog, productCatalog,
                solutionOfferCatalog, marketChannelAssignments);
        
        //Scanner scanner = new Scanner(System.in);
        //DigitalMarketingReportsInterface.displayReports(
        //marketCatalog,
        //channelCatalog,
        //productCatalog,
        //solutionOfferCatalog,
        //marketChannelAssignments
//);
    }
        public static void generateComparisonReport(
            MarketCatalog marketCatalog, 
            ProductCatalog productCatalog, 
            SolutionOfferCatalog solutionOfferCatalog,
            ChannelCatalog channelCatalog, 
            List<MarketChannelAssignment> marketChannelAssignments) {
        
                System.out.println(YELLOW + "\n--- Market Profitability Report ---");
                System.out.println(YELLOW + "\n--- Sales Revenue : Car retail Price / Ad Cost : ad cost * quantity / Profit : (Car retail Price - ad cost) ---");
                System.out.println(YELLOW+"\n--- Comparison of Same Products in Different Markets and Solution Offers ---");
                System.out.println(YELLOW+"");
                System.out.printf("%-20s | %-15s | %-25s | %-15s | %-15s | %-15s |%-15s%n",
                "Product", "Market", "Solution Offer", "Sales Revenue", "Ad Cost", "SolutionOfferCost","Profit");

        Map<String, List<ComparisonData>> productComparisonMap = new HashMap<>();
        
        for (Market market : marketCatalog.getMarketList()) {
            for (Product product : productCatalog.getProductList()) {
                SolutionOffer solutionOffer = solutionOfferCatalog.getSolutionOfferForMarket(market);
                if (solutionOffer == null) continue;
                // Retrieve metrics
                int salesRevenue = product.getSalesVolume();
                int totalQuantity = product.getTotalQuantity();
                double advertisingCost = MarketChannelAssignment.calculateAdvertisingCostForMarket(market, totalQuantity);
                double solutionOfferCost = solutionOffer.getPrice() * totalQuantity;
                double profit = salesRevenue - advertisingCost - solutionOfferCost;

                ComparisonData data = new ComparisonData(
                    product.getName(), market.getName(), solutionOffer.getDetails(), 
                    salesRevenue, advertisingCost, solutionOfferCost, profit);

                // Group by product name
                productComparisonMap.computeIfAbsent(product.getName(), k -> new ArrayList<>()).add(data);
        }
    }
                // Print the comparison results
                Set<String> uniqueEntries = new HashSet<>(); 
                productComparisonMap.keySet().stream().sorted().forEach(productName -> {
                    System.out.println("\nProduct: " + productName);
                    productComparisonMap.get(productName).stream()
                            .sorted((d1, d2) -> d1.getMarket().compareTo(d2.getMarket())) // Sort by market name
                            .forEach(data -> {
                                // Create a unique key for each entry
                                String uniqueKey = productName + "|" + data.getMarket() + "|" + data.getSolutionOffer();
                                if (!uniqueEntries.contains(uniqueKey)) {
                                    // If the entry is unique, add it to the set and print it
                                    uniqueEntries.add(uniqueKey);
                                    System.out.printf("%-20s | %-15s | %-25s | %-15d | %-15.2f | %-15s |%-15.2f%n",
                                        data.getProduct(), data.getMarket(), data.getSolutionOffer(),
                                        data.getSalesRevenue(), data.getAdvertisingCost(), data.getSolutionOfferCost(),data.getProfit());
                                }
                            });
                });
        System.out.println(YELLOW);
        channelCatalog.generateChannelProfitabilityReport(marketChannelAssignments, productCatalog);
        
        System.out.println(RED);
        marketCatalog.generateAdvertisingEfficiencyReport(
            marketChannelAssignments,
            solutionOfferCatalog,
            productCatalog
);
        System.out.println(GREEN);
        DigitalMarketingAppMenu.displayAppMenu(
            marketCatalog,
            channelCatalog,
            solutionOfferCatalog,
            productCatalog,
            marketChannelAssignments
);  
            
        //System.out.println(BLUE);           
        //DigitalMarketingReportsInterface.displayReports(
        //    marketCatalog,
        //    channelCatalog,
        //    productCatalog,
        //    solutionOfferCatalog,
        //    marketChannelAssignments
       // );
    }         
        public static class ComparisonData {
        private String product;
        private String market;
        private String solutionOffer;
        private int salesRevenue;
        private double advertisingCost;
        private double solutionOfferCost;
        private double profit;


    public ComparisonData(String product, String market, String solutionOffer, int salesRevenue, double advertisingCost, double solutionOfferCost, double profit) {
        this.product = product;
        this.market = market;
        this.solutionOffer = solutionOffer;
        this.salesRevenue = salesRevenue;
        this.advertisingCost = advertisingCost;
        this.solutionOfferCost = solutionOfferCost;
        this.profit = profit;
    }

    public String getProduct() {
        return product;
    }

    public String getMarket() {
        return market;
    }

    public String getSolutionOffer() {
        return solutionOffer;
    }

    public int getSalesRevenue() {
        return salesRevenue;
    }

    public double getAdvertisingCost() {
        return advertisingCost;
    }

    public double getProfit() {
        return profit;
    }
    public double getSolutionOfferCost(){
        return solutionOfferCost;
    }
}
    }        






