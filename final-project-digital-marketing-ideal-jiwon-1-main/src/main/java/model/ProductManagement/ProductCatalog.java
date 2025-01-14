package model.ProductManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.MarketModel.Channel;
import model.MarketModel.Market;
import model.OrderManagement.OrderItem;

/**
 *
 * @author kal bugrara
 */
public class ProductCatalog {

    String type;
    ArrayList<Product> products; // list of products initially empty

    public ProductCatalog(String n) {
        type = n;
        products = new ArrayList<>(); // Initialize the product list
    }

    /**
     * Load products from a CSV file and link them to their corresponding markets.
     *
     * @param filePath   The path to the CSV file.
     * @param marketList List of markets to match products with.
     */
    public void loadProductsFromCSV(String filePath, List<Market> marketList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Skip the header line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Parse product details
                String name = values[1].trim();
                String marketName = values[0].trim();
                int floorPrice = Integer.parseInt(values[3].trim());
                int ceilingPrice = Integer.parseInt(values[2].trim());
                int targetPrice = Integer.parseInt(values[4].trim());

                // Find the market by name
                Market market = marketList.stream()
                        .filter(m -> m.getName().equalsIgnoreCase(marketName))
                        .findFirst()
                        .orElse(null);
                
                if (market != null) { 
                    // Add product to the catalog and link it to the market
                Product product = newProduct(name, floorPrice, ceilingPrice, targetPrice, market);
                product.setMarket(market);
                products.add(product);
                market.getProductList().add(product);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading products from CSV: " + e.getMessage());
        }
    }

    /**
     * Create a new product and add it to the catalog.
     *
     * @param n       Product name
     * @param fp      Floor price
     * @param cp      Ceiling price
     * @param tp      Target price
     * @param market  The market this product belongs to
     * @return The newly created product
     */
    public Product newProduct(String n, int fp, int cp, int tp, Market market) {
        Product p = new Product(n, fp, cp, tp);
        products.add(p);
        return p;
    }

    /**
     * Generate a product performance report based on a sorting rule.
     *
     * @param sortingRule Rule to sort products (e.g., by sales, profit)
     * @return A ProductsReport object
     */
    public ProductsReport generateProductPerformanceReport(String sortingRule) {
        ProductsReport productsReport = new ProductsReport(sortingRule);

        for (Product p : products) {
            ProductSummary ps = new ProductSummary(p);
            productsReport.addProductSummary(ps);
        }
        return productsReport;
    }

    /**
     * Get the list of all products in this catalog.
     *
     * @return List of products
     */
    public ArrayList<Product> getProductList() {
        return products;
    }

    /**
     * Pick a random product from the catalog.
     *
     * @return A random product or null if the catalog is empty
     */
    public Product pickRandomProduct() {
        if (products.isEmpty())
            return null;
        Random r = new Random();
        return products.get(r.nextInt(products.size()));
    }

    /**
     * Print a brief summary of the catalog.
     */
    public void printShortInfo() {
        System.out.println("There are " + products.size() + " products in this catalog");
    }

    /**
     * Calculate the total sales volume for a given market and channel.
     *
     * @param market  The market to filter by
     * @param channel The channel to filter by
     * @return Total sales volume for the specified market and channel
     */
    public int getSalesVolumeByMarketAndChannel(Market market, Channel channel) {
        int totalVolume = 0;
    
        for (Product product : products) {
            for (OrderItem orderItem : product.getOrderItems()) {
                // Retrieve the market and channel from MarketChannelAssignment
                Market assignedMarket = orderItem.marketChannelAssignment.getMarket();
                Channel assignedChannel = orderItem.marketChannelAssignment.getChannel();
    
                if (market.equals(assignedMarket) && channel.equals(assignedChannel)) {
                    totalVolume += orderItem.getQuantity();
                }
            }
        }
        return totalVolume;
    }
    // using for Market profitability Report
    public List<Product> getLowSalesProducts(int threshold) {
        List<Product> lowSalesProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getTotalQuantity() < threshold) {
                lowSalesProducts.add(product);
            }
        }
        return lowSalesProducts;
    }
    
}