package ui;

import model.MarketModel.ChannelCatalog;
import model.MarketModel.MarketCatalog;
import model.MarketModel.MarketChannelAssignment;
import model.ProductManagement.ProductCatalog;
import model.ProductManagement.SolutionOfferCatalog;


import java.util.List;
import java.util.Scanner;

public class DigitalMarketingReportsInterface {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";

    public static void displayReports(
            MarketCatalog marketCatalog,
            ChannelCatalog channelCatalog,
            ProductCatalog productCatalog,
            SolutionOfferCatalog solutionOfferCatalog,
            List<MarketChannelAssignment> marketChannelAssignments
    ) {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println(YELLOW + "\n--- Digital Marketing Reports Menu ---");
                System.out.println(GREEN + "1. Advertising Efficiency Report");
                System.out.println(RED + "2. Channel Profitability Report");
                System.out.println(BLUE + "3. Market Profitability Report");
                System.out.println(GREEN + "4. Exit");
                System.out.print("Enter your choice: ");

                // Check if input is an integer
                if (!scanner.hasNextInt()) {
                    System.out.println(RED + "Invalid input. Please enter a number between 1 and 4.");
                    scanner.next(); // Consume invalid input
                    continue;
                }

                int choice = scanner.nextInt();
                scanner.nextLine();

                // Process the choice
                switch (choice) {
                    case 1:
                        System.out.println("\n--- Advertising Efficiency Report ---");
                        marketCatalog.generateAdvertisingEfficiencyReport(
                                marketChannelAssignments, solutionOfferCatalog, productCatalog);
                        break;

                    case 2:
                        System.out.println("\n--- Channel Profitability Report ---");
                        channelCatalog.generateChannelProfitabilityReport(
                                marketChannelAssignments, productCatalog);
                        break;

                    case 3:
                        System.out.println("\n--- Market Profitability Report ---");
                        DigitalMarketingApplication.generateComparisonReport(
                            marketCatalog,
                            productCatalog,
                            solutionOfferCatalog,
                            channelCatalog,
                            marketChannelAssignments);
                        break;

                    case 4:
                        System.out.println(GREEN + "Exit. Thank you!");
                        scanner.close();
                        return;

                    default:
                        System.out.println(RED + "Invalid choice. Please enter a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println(RED + "An unexpected error occurred: " + e.getMessage());
                scanner.nextLine(); // Consume the remaining input
            }
        }
    }
}







