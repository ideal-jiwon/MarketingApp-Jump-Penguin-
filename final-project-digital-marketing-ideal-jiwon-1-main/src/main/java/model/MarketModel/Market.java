/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.ArrayList;
import java.util.Objects;

import model.ProductManagement.Product;
import model.ProductManagement.ProductCatalog;

/**
 *
 * @author kal bugrara
 */
public class Market {
  String name;
  //ArrayList<SolutionOffer> so;
  //ArrayList<MarketChannelAssignment> channels;
  //ArrayList<String> characteristics;
  //ArrayList<Market> submarkets;
  ProductCatalog productCatalog;
  //int size;

  public Market(String n) {
    this.name = n;
    this.productCatalog = new ProductCatalog("ProductCatalog");
    //characteristics = new ArrayList<String>();
    //characteristics.add(s);
  }
  public String getName() {
    return name;
}
  public ArrayList<Product> getProductList() { 
    return productCatalog.getProductList();
}
  public ArrayList<Product> getProductCatalog(){
    return productCatalog.getProductList();
}

@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Market market = (Market) o;
    return name.equals(market.name); // Compare relevant fields
}
@Override
    public String toString() {
        return "\n Market Name: " + name ;
             // ", Product Catalog: " + productcatalog;
    }
@Override
    public int hashCode() {
        return Objects.hash(name); // Hash relevant fields
    }

  }

