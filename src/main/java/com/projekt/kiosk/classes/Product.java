package com.projekt.kiosk.classes;

public class Product {
    public String name;
    public float price;
    public Ingredient[] ingredients;
    public Extra[] possibleExtras;

    public Product(String name, float price, Ingredient[] ingredients, Extra[] possibleExtras) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.possibleExtras = possibleExtras;
    }



}
