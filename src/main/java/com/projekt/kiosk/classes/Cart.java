package com.projekt.kiosk.classes;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    public float price;
    public List<Product> products;

    public Cart() {
        this.price = 0;
        this.products = new ArrayList<Product>();

    }

    public void removeProduct(int id){
        if (id <0 || id >= products.size()) return;
        this.products.remove(id);
        calculatePrice();
    }
    public void calculatePrice(){
        this.price = 0;
        for(Product p : this.products){
            this.price += p.price;
        }
    }
    public void addProduct(Product product){
        this.products.add(product);
        calculatePrice();
    }



}
