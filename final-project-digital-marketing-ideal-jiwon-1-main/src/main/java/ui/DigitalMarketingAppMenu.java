package ui;

import model.MarketModel.*;
import model.ProductManagement.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class DigitalMarketingAppMenu {

    public static void displayAppMenu(
            MarketCatalog marketCatalog,
            ChannelCatalog channelCatalog,
            SolutionOfferCatalog solutionOfferCatalog,
            ProductCatalog productCatalog,
            List<MarketChannelAssignment> marketChannelAssignments
            ) {
        
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- APP MENU ---");
            System.out.println("\nWould you like to choose a Market or a Channel?");
            System.out.println("1. Pick Market Choices");
            System.out.println("2. Pick Channel Choices");
            System.out.println("3. Proceed to Reports");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            //int choice = scanner.nextInt();
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next(); // Consume invalid input
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    handleMarketChoice(marketCatalog, solutionOfferCatalog, scanner);
                    break;

                case 2:
                    handleChannelChoice(channelCatalog, solutionOfferCatalog, scanner);
                    break;

                case 3:
                    System.out.println("Proceeding to Reports...");
                    DigitalMarketingReportsInterface.displayReports( // Call the reports interface
                        marketCatalog,
                        channelCatalog,
                        productCatalog,
                        solutionOfferCatalog,
                        marketChannelAssignments
                );
                    return;
                case 4:
                    System.out.println("Exit.Thank you using the app:)");
                    scanner.close();
                    return;

                default:
                  System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleMarketChoice(MarketCatalog marketCatalog, SolutionOfferCatalog solutionOfferCatalog, Scanner scanner) {
        System.out.println("\nAre you looking for selling cars and advertising in different markets?");
        System.out.println("We are offering services to 3 markets: South Asia, North America, and South America.");

        // Display available markets
        Set<String> uniqueMarketNames = new HashSet<>(marketCatalog.getMarketNames()); 
        //List<String> marketNames = marketCatalog.getMarketNames();
        System.out.println("\nAvailable Markets:");
        int i = 1;
        for (String marketName : uniqueMarketNames) {
            System.out.println(i + ". " + marketName);
            i++;
    }
        //for (int i = 0; i < uniqueMarketNames.size(); i++) {
        //    System.out.println((i + 1) + ". " + uniqueMarketNames.get(i));
        //}

        System.out.print("\nPick a market from the list: ");
        int marketChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (marketChoice < 1 || marketChoice > uniqueMarketNames.size()) { // Use the size of the set here
            System.out.println("Invalid market choice. Returning to menu.");
            return;
        }
        List<String> uniqueMarketNamesList = new ArrayList<>(uniqueMarketNames); 
        String selectedMarket = uniqueMarketNamesList.get(marketChoice - 1); 
        System.out.println("\nYou selected Market: " + selectedMarket);
        //String selectedMarket = marketNames.get(marketChoice - 1);
        //System.out.println("\nYou selected Market: " + selectedMarket);

        // Display solution offers for the selected market
        displaySolutionOffers(solutionOfferCatalog, scanner);
    }

    private static void handleChannelChoice(ChannelCatalog channelCatalog, SolutionOfferCatalog solutionOfferCatalog, Scanner scanner) {
        System.out.println("\nWe are experts in advertising.");
        System.out.println("We offer 4 options for channels: Cold Emails, Social Media, TV Commercials, Websites.");

        // Display available channels
        Set<String> uniqueChannelNames = new HashSet<>();
        for (Channel channel : channelCatalog.getChannelList()) {
            uniqueChannelNames.add(channel.getName());
        }
        //List<Channel> channels = channelCatalog.getChannelList();
        System.out.println("\nAvailable Channels:");
        //for (int i = 0; i < channels.size(); i++) {
        //    System.out.println((i + 1) + ". " + channels.get(i));
        //}
        int i = 1;
        for (String channelName : uniqueChannelNames) {
            System.out.println(i + ". " + channelName);
            i++;
    }

        System.out.print("\nPick a channel from the list: ");
        int channelChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (channelChoice < 1 || channelChoice > uniqueChannelNames.size()) {
            System.out.println("Invalid channel choice. Returning to menu.");
            return;
        }
        List<String> uniqueChannelNamesList = new ArrayList<>(uniqueChannelNames);
        Channel selectedChannel = channelCatalog.findChannel(uniqueChannelNamesList.get(channelChoice - 1));
        //Channel selectedChannel = channels.get(channelChoice - 1);
        String selectedChannelName = selectedChannel.getName();
        System.out.println("\nYou selected Channel: " + selectedChannelName);

        // Display solution offers for the selected channel
        displaySolutionOffers(solutionOfferCatalog, scanner);
    }

    private static void displaySolutionOffers(SolutionOfferCatalog solutionOfferCatalog, Scanner scanner) {
        List<SolutionOffer> solutionOffers = solutionOfferCatalog.getSolutionOffers();

        System.out.println("\nAvailable Solution Offers:");
        for (int i = 0; i < solutionOffers.size(); i++) {
            SolutionOffer offer = solutionOffers.get(i);
            System.out.println((i + 1) + ". " + offer.getDetails() + " | Price: " + offer.getPrice());
        }

        System.out.print("\nEnter the number of the solution offer you'd like to purchase (or 0 to skip): ");
        int solutionChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (solutionChoice == 0) {
            System.out.println("No solution offer selected. Returning to menu.");
        } else if (solutionChoice < 1 || solutionChoice > solutionOffers.size()) {
            System.out.println("Invalid choice. Returning to menu.");
        } else {
            SolutionOffer selectedOffer = solutionOffers.get(solutionChoice - 1);
            System.out.println("\nOrder Confirmation:");
            System.out.println("You selected Solution Offer: " + selectedOffer.getDetails());
            System.out.println("Price: " + selectedOffer.getPrice());
            System.out.println("Thank you for your purchase!");
        }
    }
}