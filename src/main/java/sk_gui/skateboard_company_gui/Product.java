package sk_gui.skateboard_company_gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents a product in the inventory tracking system. It includes the functionality of associating parts with the product.
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * This method constructs a new Product object with the following values.
     * @param id The ID of the product.
     * @param name The name of the product.
     * @param price The price of the product.
     * @param stock The inventory of the product.
     * @param min The minimum of the product.
     * @param max The maximum of the product.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * This method returns the ID of the product.
     * @return the id of a product.
     */
    public int getId() {
        return id;
    }

    /**
     * This method sets the ID of the product to the given value.
     * @param id the id of a product to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This method returns the name of the product.
     * @return the name of a product.
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the product to the given string.
     * @param name the name of a product to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the price of the product.
     * @return the price of a product.
     */
    public double getPrice() {
        return price;
    }

    /**
     * This method sets the price of the product to the given value.
     * @param price the price of a product to set.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * This method returns the current inventory of the product.
     * @return the inventory of a product.
     */
    public int getStock() {
        return stock;
    }

    /**
     * This method sets the iventory of the product.
     * @param stock the inventory of a product to set.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * This method returns the minimum of the product.
     * @return the min of a product.
     */
    public int getMin() {
        return min;
    }

    /**
     * This method sets the minimum of the product.
     * @param min the min  of a product to set.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * This method returns the maximum of the product.
     * @return the max of a product.
     */
    public int getMax() {
        return max;
    }

    /**
     * This method sets the maximum of the product.
     * @param max the max of a product. to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * This method adds a specified part the list of associated parts.
     * @param part the part that will be added to the list of associated parts.
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    /**
     * This method deletes a selected associated part from the associated parts list.
     * @param selectedAssociatedPart the associated part that will be deleted.
     * @return true if the selected associated part was deleted successfully.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * This method returns a list of all parts associated with this product.
     * @return the observable list of the associated parts for this product.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }
}

