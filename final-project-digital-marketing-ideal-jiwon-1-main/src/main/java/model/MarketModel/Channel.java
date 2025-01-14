/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.MarketModel;

import java.util.Objects;

/**
 *
 * @author kal bugrara
 */
public class Channel {
    String name;

    public Channel(String n) {
        this.name = n;
    }
    public String getName() {
        return name;
    }
    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Channel channel = (Channel) o;
    return name.equals(channel.name); // Compare relevant fields
}
    @Override
    public String toString() {
        return  "\n channel :" + name ;
             // ", Product Catalog: " + productcatalog;
    }
@Override
    public int hashCode() {
        return Objects.hash(name); // Hash relevant fields
    }
}
