/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.CustomerManagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Business.Business;
import model.Personnel.Person;

/**
 *
 * @author kal bugrara
 */
public class CustomerDirectory {

    ArrayList<CustomerProfile> customerlist;

    public CustomerDirectory() {

        customerlist = new ArrayList<CustomerProfile>();

    }
    public List<CustomerProfile> getCustomerlist(){
        return customerlist;
    }

    public CustomerProfile newCustomerProfile(Person p) {

        CustomerProfile sp = new CustomerProfile(p, this);
        customerlist.add(sp);
        return sp;
    }

    public CustomerProfile findCustomer(String id) {

        for (CustomerProfile sp : customerlist) {

            if (sp.isMatch(id)) {
                return sp;
            }
        }
        return null; // not found after going through the whole list
    }
    public void loadCustomersFromCSV(String filePath) {
      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
          String line;

          // Skip the header line
          br.readLine();

          // Read and process each line
          while ((line = br.readLine()) != null) {
              String[] values = line.split(",");

              // Extract supplier data
              String customerName = values[1].trim(); 

              // Create a new Customer 
              Person person = new Person(customerName);
              // add a new customers to customerprofile
              CustomerProfile customerProfile = newCustomerProfile(person);
          }
      } catch (IOException e) {
          System.err.println("Error Message with customers " + e.getMessage());
      }
}

    public CustomersReport generatCustomerPerformanceReport() {
        CustomersReport customersreport = new CustomersReport();

        for (CustomerProfile cp : customerlist) {

            CustomerSummary cs = new CustomerSummary(cp);
            customersreport.addCustomerSummary(cs);
        }
        return customersreport;
    }

    public CustomerProfile pickRandomCustomer() {
        if (customerlist.size() == 0)
            return null;
        Random r = new Random();
        int randomIndex = r.nextInt(customerlist.size());
        return customerlist.get(randomIndex);
    }

    public void printShortInfo() {
        System.out.println("Checking what's inside the Customer directory.");
        System.out.println("There are " + customerlist.size() + " customers.");
    }
}
