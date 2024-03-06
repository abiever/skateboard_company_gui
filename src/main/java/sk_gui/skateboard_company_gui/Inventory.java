package sk_gui.skateboard_company_gui;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The Inventory class sets up the main components of the overall Inventory Management System. The Inventory class also contains all of the methods for interacting with the system and also intializes the system with parts and products in its tables.
 * <p><b>
 * FUTURE_ENHANCEMENT
 * </b></p>
 * As a future enhancement to this inventory tracking system, something that would be most convenient for users would be a button for 'Removing All Associated Parts'. This enhancement could be added to both the 'Add' and 'Modify' sections dealing with products, but would be most ideal when modifying a product's details. At this point in time, it's not too laborious to remove one or two associated parts. However, the potential inefficiency of using such a system to remove dozens of associated parts one at a time can't be overstated.
 */
public class Inventory extends Application {

    private static TableView<Part> partsTable;
    private static ObservableList<Part> allParts;

    private static TableView<Product> productsTable;
    private static ObservableList<Product> allProducts;

    /**
     * This method readies the UI components and initializes the appropriate tables with parts and products.
     * @param stage The primary stage unto which the entire application's scene is set
     */
    @Override
    public void start(Stage stage) {
        // Create border panes to contain components
        BorderPane mainPane = new BorderPane();
        BorderPane partsBorderPane = new BorderPane();
        BorderPane productsBorderPane = new BorderPane();

        // Sets the desired dimensions and style of the parts pane
        //partsBorderPane.setPrefSize(400, 200);
        partsBorderPane.setPadding(new Insets(10));
        //borderPane.setMargin(borderPane, new Insets(30)); //does this actually do anything??
        partsBorderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // Sets the desired dimensions and style of the products pane
        //productsBorderPane.setPrefSize(400, 200);
        productsBorderPane.setPadding(new Insets(10));
        productsBorderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");

        // Sets the desired dimensions and style of the main container pane
        //mainPane.setPrefSize(450, 250);
        //mainPane.setStyle("-fx-border-color: black; -fx-border-width: 3px;");

        // Create the parts table columns and then set their desired width to match title text content width if needed
        TableColumn<Part, String> partsIdCol = new TableColumn<>("Part ID");
        partsIdCol.prefWidthProperty().set(100);
        TableColumn<Part, String> partsNameCol = new TableColumn<>("Part Name");
        partsNameCol.prefWidthProperty().set(100);
        TableColumn<Part, String> partsInvCol = new TableColumn<>("Inventory Level");
        partsInvCol.prefWidthProperty().set(150);
        TableColumn<Part, Double> partsPriceCol = new TableColumn<>("Price per Unit");
        partsPriceCol.prefWidthProperty().set(150);

        // Set the cell value factories for each column in parts table
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsPriceCol.setCellFactory(column -> {
            //This was INCREDIBLY hard to figure out for some reason, but the doubles are FINALLY defaulting to 2 trailing zeros
            TableCell<Part, Double> cell = new TableCell<Part, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        // Create the parts table and add the columns to it
        partsTable = new TableView<>();
        partsTable.getColumns().addAll(partsIdCol, partsNameCol, partsInvCol, partsPriceCol);
        Label defaultTableLabel = new Label("No content in table. Please add some parts.");
        partsTable.setPlaceholder(defaultTableLabel);

        //Removes unnecessary empty 5th column from parts tableview
        //for some reason 'table.getColumns().remove(4)' would cause the build to fail; below method works though
        partsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create the products table columns and then set their desired width to match title text content width if needed
        TableColumn<Product, String> productsIdCol = new TableColumn<>("Product ID");
        productsIdCol.prefWidthProperty().set(100);
        TableColumn<Product, String> productsNameCol = new TableColumn<>("Product Name");
        productsNameCol.prefWidthProperty().set(100);
        TableColumn<Product, String> productsInvCol = new TableColumn<>("Inventory Level");
        productsInvCol.prefWidthProperty().set(150);
        TableColumn<Product, Double> productsPriceCol = new TableColumn<>("Price per Unit");
        productsPriceCol.prefWidthProperty().set(150);

        // Set the cell value factories for each column in products table
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsPriceCol.setCellFactory(column -> {
            //sets price column to 2 trailing zeroes
            TableCell<Product, Double> cell = new TableCell<Product, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        // Create the products table and add columns to it
        productsTable = new TableView<>();
        productsTable.getColumns().addAll(productsIdCol, productsNameCol, productsInvCol, productsPriceCol);
        //Label emptyProductsLabel = new Label("NO SUCH PRODUCT EXISTS");
        //productsTable.setPlaceholder(emptyProductsLabel);

        //Removes unnecessary empty 5th column from parts tableview
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create the buttons needed for main UI components
        Button addPart = new Button("Add");
        Button modifyPart = new Button("Modify");
        Button deletePart = new Button("Delete");
        Button addProduct = new Button("Add");
        Button modifyProduct = new Button("Modify");
        Button deleteProduct = new Button("Delete");
        Button exitButton = new Button("Exit");

        // Create the search boxes & apply logic for search
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by Part ID or Name");
        partsSearchBox.prefWidthProperty().set(210);
        partsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+")) {
                // Call the method that filters by part ID
                lookupPart(Integer.parseInt(newValue));
            } else {
                // Call the method that filters by part name
                lookupPart(newValue);
            }
        });

        TextField productsSearchBox = new TextField();
        productsSearchBox.setPromptText("Search by Product ID or Name");
        productsSearchBox.prefWidthProperty().set(210);
        productsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+")) {
                lookupProduct(Integer.parseInt(newValue));
            } else {
                lookupProduct(newValue);
            }
        });

        // Set the button event handlers
        addPart.setOnAction(event -> addPart(null));
        modifyPart.setOnAction(event -> updatePart(0, null));
        deletePart.setOnAction(event -> deletePart(null));
        addProduct.setOnAction(event -> addProduct(null));
        modifyProduct.setOnAction(event -> updateProduct(0, null));
        deleteProduct.setOnAction(event -> deleteProduct(null));
        exitButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText("Are you sure you want to exit?");
            alert.setContentText("Click OK to close this application, or Cancel to stay.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Platform.exit();
            }
        });


        // Create an HBox layout manager for parts table and add buttons to it
        HBox partsButtonBox = new HBox();
        HBox.setHgrow(partsSearchBox, Priority.NEVER);
        HBox.setMargin(partsSearchBox, new Insets(0, 0, 0, 150));
        partsButtonBox.getChildren().addAll(addPart, modifyPart, deletePart, partsSearchBox);

        // Create an HBox layout manager for products table and add buttons to it
        HBox productsButtonBox = new HBox();
        HBox.setHgrow(productsSearchBox, Priority.NEVER);
        HBox.setMargin(productsSearchBox, new Insets(0, 0, 0, 150));
        productsButtonBox.getChildren().addAll(addProduct, modifyProduct, deleteProduct, productsSearchBox);

        // Create a VBox layout manager for parts table and add the table and buttonBox to it
        VBox partsVbox = new VBox();
        partsVbox.getChildren().addAll(partsTable, partsButtonBox);

        // Create a VBox layout manager for products table and add the table and buttonBox to it
        VBox productsVbox = new VBox();
        productsVbox.getChildren().addAll(productsTable, productsButtonBox);

        //Add the necessary components to their respective border panes and set their positioning
        partsBorderPane.setCenter(partsTable); // this is actually needed to keep the partsTable appropriately aligned
        partsBorderPane.setBottom(partsVbox);
        Label partsLabel = new Label("PARTS");
        partsLabel.setStyle("-fx-font-weight: bold;"); //this "bold" style doesn't seem to be applying?
        partsBorderPane.setTop(partsLabel);

        productsBorderPane.setCenter(productsTable);
        productsBorderPane.setBottom(productsVbox);
        Label productsLabel = new Label("PRODUCTS");
        productsLabel.setStyle("-fx-font-weight: bold;");
        productsBorderPane.setTop(productsLabel);
        mainPane.setLeft(partsBorderPane);
        mainPane.setRight(productsBorderPane);
        mainPane.setBottom(exitButton);

        // Create the scene and set it as the content of the stage, as well as desired min/max dimensions
        Scene scene = new Scene(mainPane);
        stage.setMinWidth(1110);
        stage.setMinHeight(600);
        stage.setMaxWidth(1410);
        stage.setMaxHeight(600);
        stage.setScene(scene);
        stage.setTitle("Inventory Management System");
        stage.show();

        // Initialize the items list for the parts table
        allParts = FXCollections.observableArrayList();

        allParts.add(new InHouse(IDGenerator.generateID(), "Independent 168mm Trucks", 34.00, 12, 1, 20, 4325));
        allParts.add(new InHouse(IDGenerator.generateID(), "SpitFire Wheels", 47.00, 24, 3, 30, 122));
        partsTable.setItems(allParts);
        allParts.add(new Outsourced(IDGenerator.generateID(), "REDS Bearings", 16.00, 11, 5, 30, "Bones Bearings"));
        allParts.add(new Outsourced(IDGenerator.generateID(), "Aloof 8.25 Deck", 76.00, 3, 2, 10, "Evisen Skateboards"));
        partsTable.setItems(allParts);
        allParts.add(new Outsourced(IDGenerator.generateID(), "Ninja Bushings - Black", 6.75, 13, 5, 20, "Ninja"));
        partsTable.setItems(allParts);
        allParts.add(new InHouse(IDGenerator.generateID(), "OJ SuperJuice 78A Wheels", 43.25, 4, 3, 10, 9390));
        allParts.add(new InHouse(IDGenerator.generateID(), "StreetKroozerz 82A Wheels", 53.00, 10, 3, 10, 9380));
        allParts.add(new Outsourced(IDGenerator.generateID(), "Beachy Pintail 9.75 Deck", 86.99, 3, 1, 5, "Globe"));
        allParts.add(new Outsourced(IDGenerator.generateID(), "WaterBorne SurfSkate Trucks", 116.50, 3, 1, 6, "WaterBorne"));
        partsTable.setItems(allParts);

        // Initialize the items list in the Products Table
        allProducts = FXCollections.observableArrayList();

        allProducts.add(new Product(IDGenerator.generateID(),"Globe 'Big Kahuna' Cruiser", 317.00, 1, 1, 3));
        allProducts.add(new Product(IDGenerator.generateID(), "Evisen 'Aloof' Complete", 158.00, 2, 1, 5));
        allProducts.add(new Product(IDGenerator.generateID(), "Element 'Fireside' Complete", 140.00, 4, 1, 5));
        allProducts.add(new Product(IDGenerator.generateID(), "Santa Cruz 'SoCal' SurfSkate", 277.00, 1, 1, 5));
        productsTable.setItems(allProducts);
    }

    /**
     * This method adds a new part to the inventory system. It calls a separate method that controls a dialog box where the user can type in the new part's details. After clicking save, the part is added to the appropriate ObservableList and is displayed in the Parts Table.
     * @param newPart A Part object that will be added to the inventory.
     */
    public static void addPart(Part newPart) {
        // Creates a new item and adds it to the table
        newPart = new InHouse(IDGenerator.generateID(), "", 0, 0, 0, 0, 0);
        addPartDialog dialog = new addPartDialog();
        Optional<Part> result = dialog.showAndWait();

        // If the user clicked "Save", add the new item to the table
        result.ifPresent(NewPart -> {
            allParts.add(NewPart);
            partsTable.setItems(allParts);
        });
    }

    /**
     * This method adds a new product to the inventory system. It calls a separate method that controls a dialog box where the user can type in the new product's details. After clicking save, the product is added to the appropriate ObservableList and is displayed in the Products Table.
     * @param newProduct A Product object that will be added to the inventory.
     */
    public static void addProduct(Product newProduct) {
        newProduct = new Product(IDGenerator.generateID(), "", 0, 0, 0, 0);
        addProductDialog dialog = new addProductDialog();
        Optional<Product> result = dialog.showAndWait();

        // If the user clicked "Save", add the new item to the table
        result.ifPresent(returnedNewProduct -> {
            allProducts.add(returnedNewProduct);
            productsTable.setItems(allProducts);
        });
    }

    /**
     * This method updates a selected part's data. It calls a separate method that controls a dialog box where the user can edit the part's data fields which are automatically populated with the selected part's data. Upon saving, the selected part is replaced with the updated part.
     * @param index The index of the selected part.
     * @param selectedPart The part that is selected and then updated by the user.
     * <p><b>
     * RUNTIME_ERROR
     * </b></p>
     * In this method, I was having an issue where the 'index' argument was causing this error: "Local variable index defined in an enclosing scope must be final or effectively final". This issue was happening because I was attempting to modify the value of the index variable within the updatePart() method, which is the enclosing scope. In order to fix this error, after much trial and error, I discovered I could simply pass the value of the index variable to a new one (in this case, 'selectedIndex') and complete the rest of the logic with this variable instead.
     */
    public static void updatePart(int index, Part selectedPart) {
        //SOLVED: I also needed to create a more general 'index' selector
        index = partsTable.getSelectionModel().getSelectedIndex();

        // Get the selected item from the table and open the edit dialog
        selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null && selectedPart instanceof InHouse) {
            // Show the edit dialog
            InHouse inHousePart = (InHouse) selectedPart;
            PartUpdateDialog dialog = new PartUpdateDialog(inHousePart);
            int selectedIndex = index;
            Optional<Part> result = dialog.showAndWait();
            result.ifPresent(updatedPart -> {
                // Replace the selected item with the updated item
                //index = partsTable.getItems().indexOf(inHousePart);
                //ISSUE SOLVED: needed to create 'selectedIndex' to overcome 'effectively final' issue
                partsTable.getItems().set(selectedIndex, updatedPart);
            });
        } else if (selectedPart != null && selectedPart instanceof Outsourced) {
            // Show the edit dialog
            Outsourced outsourcedPart = (Outsourced) selectedPart;
            PartUpdateDialog dialog = new PartUpdateDialog(outsourcedPart);
            int selectedIndex = index;
            Optional<Part> result = dialog.showAndWait();
            result.ifPresent(updatedPart -> {
                // Replace the selected item with the updated item
                //index = partsTable.getItems().indexOf(outsourcedPart);
                partsTable.getItems().set(selectedIndex, updatedPart);
            });
        } else if (selectedPart == null) {
            Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
            nothingSelectedDialog.setTitle("Nothing Selected");
            nothingSelectedDialog.setHeaderText("You must select a part.");
            nothingSelectedDialog.setContentText("In order to edit a part's data, please select one from the table to continue.");
            nothingSelectedDialog.showAndWait();
        }
    }

    /**
     * This method updates a selected product's data. It calls a separate method that controls a dialog box where the user can edit the product's data fields which are automatically populated with the selected product's data. Upon saving, the selected product is replaced with the updated product.
     * @param index The index of the selected product.
     * @param selectedProduct The product that is selected and then updated by the user.
     */
    public static void updateProduct(int index, Product selectedProduct) {
        // Get the selected item from the table and open the edit dialog
        index = productsTable.getSelectionModel().getSelectedIndex();
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            // Show the edit dialog
            ProductUpdateDialog dialog = new ProductUpdateDialog(selectedProduct);
            int selectedIndex = index;
            Optional<Product> result = dialog.showAndWait();
            result.ifPresent(updatedProduct -> {
                // Replace the selected item with the updated item
                //int index = productsTable.getItems().indexOf(selectedProduct);
                productsTable.getItems().set(selectedIndex, updatedProduct);
            });
        }
        if (selectedProduct == null) {
            Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
            nothingSelectedDialog.setTitle("Nothing Selected");
            nothingSelectedDialog.setHeaderText("You must select a product.");
            nothingSelectedDialog.setContentText("In order to edit a product's data, please select one from the table to continue.");
            nothingSelectedDialog.showAndWait();
        }
    }

    /**
     * This method deletes a selected part if it exists and will display an error message if nothing is selected.
     * @param selectedPart The part to be deleted.
     * @return true The Boolean value that is returned.
     */
    public static boolean deletePart(Part selectedPart) {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Are you sure you want to delete this part?");
            confirmDialog.setContentText("This action cannot be undone.");
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                allParts.remove(selectedPart);
            }
        }
        if (selectedPart == null) {
            Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
            nothingSelectedDialog.setTitle("Nothing Selected");
            nothingSelectedDialog.setHeaderText("You must select a part!");
            nothingSelectedDialog.setContentText("In order to delete any part, please first select one from the appropriate table.");
            nothingSelectedDialog.showAndWait();
        }
        return true;
    }

    /**
     * This method deletes a selected product if it exists and will display an error message if nothing is selected.
     * @param selectedProduct The product to be deleted.
     * @return true The boolean value that is returned.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        // Get the selected item from the table and remove it
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null && selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Deletion");
            confirmDialog.setHeaderText("Are you sure you want to delete this product?");
            confirmDialog.setContentText("This action cannot be undone.");
            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                allProducts.remove(selectedProduct);
            }
        }
        if (selectedProduct == null) {
            Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
            nothingSelectedDialog.setTitle("Nothing Selected");
            nothingSelectedDialog.setHeaderText("You must select a product!");
            nothingSelectedDialog.setContentText("In order to delete any product, please first select one from the appropriate table.");
            nothingSelectedDialog.showAndWait();
        }
        if (!selectedProduct.getAllAssociatedParts().isEmpty()) {
            Alert cannotDeleteDialog = new Alert(Alert.AlertType.INFORMATION);
            cannotDeleteDialog.setTitle("WARNING!");
            cannotDeleteDialog.setHeaderText("Cannot delete this product!");
            cannotDeleteDialog.setContentText("A product that currently has any associated parts cannot\nbe deleted from the inventory. Please remove all associated\nparts first, then try again.");
            cannotDeleteDialog.showAndWait();
        }
        return true;
    }

    /**
     * This method will look up a part in the inventory by part ID number and then display the matching part in its respective table. If no such part is found, the table itself will display a message stating that the part in question does not exist.
     * @param partID The ID of the part being looked up.
     * @return The matching Part object, or null if no matching Part is found.
     */
    public static Part lookupPart(int partID) {
        if (partID == 0) {
            partsTable.setItems(allParts);
        }
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(String.valueOf(partID))) {
                filteredParts.add(part);
            }
        }
        partsTable.setItems(filteredParts);
        if (filteredParts.isEmpty()) {
            Label noSuchPartLabel = new Label("NO SUCH PART EXISTS");
            partsTable.setPlaceholder(noSuchPartLabel);
        }
        return null;
    }

    /**
     * This method will look up a product in the inventory by product ID number and then display the matching product in its respective table. If no such product is found, the table itself will display a message stating that the product in question does not exist.
     * @param productID The ID of the product being looked up.
     * @return The matching Product object, or null if no matching Product is found.
     */
    public static Product lookupProduct(int productID) {
        if (productID == 0) {
            productsTable.setItems(allProducts);
        }
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (String.valueOf(product.getId()).contains(String.valueOf(productID))) {
                filteredProducts.add(product);
            }
        }
        productsTable.setItems(filteredProducts);
        if (filteredProducts.isEmpty()) {
            Label noSuchPartLabel = new Label("NO SUCH PRODUCT EXISTS");
            productsTable.setPlaceholder(noSuchPartLabel);
        }
        return null;
    }

    /**
     * This method looks up a part by part name and then displays the matching part in its respective table. If no such part is found, the table itself will display a message stating that the part in question does not exist.
     * @param partName The name of the part being looked up.
     * @return An ObservableList of Part objects that match the provided name. Returns an empty list if no matching parts are found.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        if (partName.isEmpty()) {
            partsTable.setItems(allParts);
        }
        ObservableList<Part> filteredParts = FXCollections.observableArrayList();
        for (Part part : allParts) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                filteredParts.add(part);
            }
        }
        partsTable.setItems(filteredParts);
        if (filteredParts.isEmpty()) {
            Label noSuchPartLabel = new Label("NO SUCH PART EXISTS");
            partsTable.setPlaceholder(noSuchPartLabel);
        }
        return filteredParts;
    }

    /**
     * This method looks up a product by product name and then displays the matching product in its respective table. If no such product is found, the table itself will display a message stating that the product in question does not exist.
     * @param productName The name of the product being looked up.
     * @return An ObservableList of Product objects that match the provided name. Returns an empty list if no matching products are found.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        if (productName.isEmpty()) {
            productsTable.setItems(allProducts);
        }
        ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        productsTable.setItems(filteredProducts);
        if (filteredProducts.isEmpty()) {
            Label noSuchPartLabel = new Label("NO SUCH PRODUCT EXISTS");
            productsTable.setPlaceholder(noSuchPartLabel);
        }
        return filteredProducts;
    }

    /**
     * This method returns an ObservableList containing all Part objects within the inventory.
     * @return The Observable List containing all parts in the inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method returns an ObservableList containing all Product objects within the inventory.
     * @return The Observable List containing all products in the inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * This method is the main entry point for the JavaFX application and starts the application.
     * @param args The command line arguments for launching the application.
     * <p><b>
     * JAVADOC FOLDER INCLUDED AT ".\GUIMockUp\dist\javadoc"
     * </b></p>
     */
    public static void main(String[] args) {
        launch(args);
    }
}

/**
 * This class is used to generate random and unique IDs within a specified range.
 */
class IDGenerator {
    private static final int minID = 1000;
    private static final int maxID = 9999;
    private static ArrayList<Integer> usedIDs = new ArrayList<Integer>();
    private static Random random = new Random();

    /**
     * This method generates a random but unique ID that can be used to identify parts or products.
     * @return The unique ID.
     */
    public static int generateID() {
        int id = random.nextInt(maxID - minID + 1) + minID;
        while (usedIDs.contains(id)) {
            id = random.nextInt(maxID - minID + 1) + minID;
            usedIDs.add(id);
        }
        return id;
    }
}

/**
 * This class creates a dialog window where the user can complete the fields to create a new InHouse or Outsourced part object and save it to the inventory.
 */
class addPartDialog extends Dialog<Part> {
    private final TextField IDField;
    private final TextField nameField;
    private final TextField priceField;
    private final TextField inventoryField;
    private final TextField partOriginField;
    private final TextField minField;
    private final TextField maxField;

    /**
     * This method creates the dialog window that the user completes to create a new InHouse or Outsourced part and add it to the inventory.
     */
    public addPartDialog() {

        // Set the dialog title and header text
        setTitle("Add Part");
        setHeaderText("Please choose either 'In-House' or 'Outsourced',\nthen provide the necessary information.\n*All fields are required.");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Create the radio buttons and toggle group
        RadioButton inHouseButton = new RadioButton("In-House");
        RadioButton outsourcedButton = new RadioButton("Outsourced");
        ToggleGroup typeGroup = new ToggleGroup();

        // Set the toggle group for the radio buttons
        inHouseButton.setToggleGroup(typeGroup);
        outsourcedButton.setToggleGroup(typeGroup);
        inHouseButton.setSelected(true); // Makes In-House default

        IDField = new TextField();
        Label IDLabel = new Label("Part ID:");
        IDField.setEditable(false);
        IDField.setDisable(true);
        IDField.setPromptText("ID Auto Generated");

        nameField = new TextField();
        Label nameLabel = new Label("Part Name:");

        priceField = new TextField();
        Label priceLabel = new Label("Price per Unit:");

        inventoryField = new TextField();
        Label inventoryLabel = new Label("Inventory Level:");

        partOriginField = new TextField();
        Label partOriginLabel = new Label("Machine ID:");
        // Add the listener to the toggle group to detect changes in the selected button
        typeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == outsourcedButton) {
                partOriginLabel.setText("Company Name:");
            } else {
                partOriginLabel.setText("Machine ID:");
            }
        });

        minField = new TextField();
        Label minLabel = new Label("Min:");

        maxField = new TextField();
        Label maxLabel = new Label("Max:");

        // Create a GridPane layout manager and add the fields and labels to it
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //This order is a bit different than the above, but matches the UI from Task Overview
        grid.addRow(0, new Label("Part Origin:"), inHouseButton, outsourcedButton);
        grid.addRow(1, IDLabel, IDField);
        grid.addRow(2, nameLabel, nameField);
        grid.addRow(3, priceLabel, priceField);
        grid.addRow(4, inventoryLabel, inventoryField);
        grid.addRow(5, minLabel, minField);
        grid.addRow(6, maxLabel, maxField);
        grid.addRow(7, partOriginLabel, partOriginField);

        // Enable/disable the save button depending on whether any fields are empty
        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // Likely unnecessary now that the ID is hard-coded
        /* IDField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        }); */

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        inventoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        partOriginField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the dialog content to the grid
        getDialogPane().setContent(grid);

        // Request focus on the nameField by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to an item when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String partType = ((RadioButton)typeGroup.getSelectedToggle()).getText();
                //Part newPart = null;
                // Check if any of the fields are empty
                if (nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                        inventoryField.getText().isEmpty() || partOriginField.getText().isEmpty() ||
                        minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                    // Display an alert dialog informing the user that all fields are required
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("All fields are required");
                    alert.setContentText("Please fill in all fields before saving.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(minField.getText()) > Integer.parseInt(maxField.getText())) {
                    minField.clear();
                    minField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Minimum must be less than Maximum.");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(inventoryField.getText()) < Integer.parseInt(minField.getText()) || Integer.parseInt(inventoryField.getText()) > Integer.parseInt(maxField.getText())) {
                    inventoryField.clear();
                    inventoryField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Inventory amount must be between Maximum and Minimum");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (!priceField.getText().matches("^\\d+(\\.\\d{1,2})?$")) {
                    // Creates error if price field does receive an appropriate decimal number
                    priceField.clear();
                    priceField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter a valid decimal number to represent price. e.x. '1.27'");
                    alert.showAndWait();
                }
                if (partType.equals("In-House")) {
                    // Creates error if fields that should only accept integers have anything else in their input
                    if (!inventoryField.getText().matches("^\\d+$") || !minField.getText().matches("^\\d+$") ||
                            !maxField.getText().matches("^\\d+$") || !partOriginField.getText().matches("^\\d+$")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for In-House Part");
                        alert.setContentText("Please enter a valid integer for fields Inventory, Min, Max, and Machine ID.");
                        alert.showAndWait();
                    }
                }
                if (partType.equals("Outsourced")) {
                    // Creates error if fields that should only accept integers have anything else in their input
                    if (!inventoryField.getText().matches("^\\d+$") || !minField.getText().matches("^\\d+$") ||
                            !maxField.getText().matches("^\\d+$")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for Outsourced Part");
                        alert.setContentText("Please enter a valid integer for fields Inventory, Min, and Max");
                        alert.showAndWait();
                    }
                    if (partOriginField.getText().matches("^\\d*")) {
                        // Creates error specifically for Outsourced Part's Company Name field if it only receives integers
                        partOriginField.clear();
                        partOriginField.requestFocus();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for Outsourced Part");
                        alert.setContentText("Please enter a combination of alphanumeric characters to create a valid company name. e.x 'Sector9 Skateboards'");
                        alert.showAndWait();
                    }
                }

                if (partType.equals("In-House")) {
                    Part newPart = new InHouse(
                            IDGenerator.generateID(),
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(inventoryField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            Integer.parseInt(partOriginField.getText())
                    );
                    return newPart;
                } else if (partType.equals("Outsourced")) {
                    Part newPart = new Outsourced(
                            IDGenerator.generateID(),
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(inventoryField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            partOriginField.getText());
                    return newPart;
                }
            }
            return null;
        });
    };
};

/**
 * This class creates a dialog window where the user can complete the fields to create a new Product object and save it to the inventory.
 */
class addProductDialog extends Dialog<Product> {
    private final TextField IDField;
    private final TextField nameField;
    private final TextField priceField;
    private final TextField inventoryField;
    private final TextField minField;
    private final TextField maxField;
    private final Product product = new Product(IDGenerator.generateID(), "", 0, 0, 0, 0);
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private TableView<Part> associatedPartsTable = new TableView<>();

    /**
     * This method creates the dialog window that the user completes to create a new Product part and add it to the inventory.
     */
    public addProductDialog() {

        // Set the dialog title and header text
        setTitle("Add Product");
        setHeaderText("Please include the necessary Associated Parts by using the Parts Table to the Right.\n*All text fields are required. Associating parts with a product is optional.");

        TableView<Part> partsTableCopy = new TableView<>();
        //Gets all the parts from the private allParts variable in Inventory Class
        partsTableCopy.setItems(Inventory.getAllParts());
        partsTableCopy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        partsTableCopy.setMaxHeight(150); // set a maximum height to limit the size of the table
        partsTableCopy.setPrefWidth(450); // set a preferred width to control the width of the table

        TableColumn<Part, String> partIDColumn = new TableColumn<>("Part ID");
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part, String> partNameColumn = new TableColumn<>("Part Name");
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> partInventoryColumn = new TableColumn<>("Inventory Level");
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> partPriceColumn = new TableColumn<>("Price per Unit");
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPriceColumn.setCellFactory(column -> {
            TableCell<Part, Double> cell = new TableCell<Part, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        partsTableCopy.getColumns().addAll(partIDColumn, partNameColumn, partInventoryColumn, partPriceColumn);

        associatedPartsTable.setItems(associatedParts);
        associatedPartsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        associatedPartsTable.setMaxHeight(150);
        associatedPartsTable.setPrefWidth(450);

        TableColumn<Part, String> associatedPartIDColumn = new TableColumn<>("Part ID");
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part, String> associatedPartNameColumn = new TableColumn<>("Part Name");
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> associatedPartInventoryColumn = new TableColumn<>("Inventory Level");
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> associatedPartPriceColumn = new TableColumn<>("Price per Unit");
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartPriceColumn.setCellFactory(column -> {
            TableCell<Part, Double> cell = new TableCell<Part, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        associatedPartsTable.getColumns().addAll(associatedPartIDColumn, associatedPartNameColumn, associatedPartInventoryColumn, associatedPartPriceColumn);
        //Label emptyAssocPartTableLabel = new Label("THERE ARE CURRENTLY NO ASSOCIATED PARTS");
        //associatedPartsTable.setPlaceholder(emptyAssocPartTableLabel);

        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        IDField = new TextField();
        Label IDLabel = new Label("Product ID:");
        IDField.setEditable(false);
        IDField.setDisable(true);
        IDField.setPromptText("ID Auto Generated");

        nameField = new TextField();
        Label nameLabel = new Label("Product Name:");

        priceField = new TextField();
        Label priceLabel = new Label("Price per Unit:");

        inventoryField = new TextField();
        Label inventoryLabel = new Label("Inventory Level:");

        minField = new TextField();
        Label minLabel = new Label("Min:");

        maxField = new TextField();
        Label maxLabel = new Label("Max:");

        // A Grid for the TextField specs for Products
        GridPane specsGrid = new GridPane();
        specsGrid.setHgap(10);
        specsGrid.setVgap(10);
        specsGrid.addRow(0, IDLabel, IDField, partsTableCopy);
        specsGrid.addRow(1, nameLabel, nameField);
        specsGrid.addRow(2, priceLabel, priceField);
        specsGrid.addRow(3, inventoryLabel, inventoryField);
        specsGrid.addRow(5, minLabel, minField);
        specsGrid.addRow(6, maxLabel, maxField);
        VBox.setMargin(specsGrid, new Insets(10, 10, 10, 10));

        Button addAssociatedPartButton = new Button("Add Associated Part");
        addAssociatedPartButton.setOnAction(event -> {
            Part selectedPart = partsTableCopy.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
                nothingSelectedDialog.setTitle("Nothing Selected");
                nothingSelectedDialog.setHeaderText("You must select a part!");
                nothingSelectedDialog.setContentText("In order to add an associated part, please select one from the upper table to continue.");
                nothingSelectedDialog.showAndWait();
            }
            if (selectedPart != null) {
                product.addAssociatedPart(selectedPart);
                associatedParts.setAll(product.getAllAssociatedParts());
                associatedPartsTable.setItems(associatedParts);
            }
        });

        Button removeAssociatedPartButton = new Button("Remove Associated Part");
        removeAssociatedPartButton.setOnAction(event -> {
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Removal");
                confirmDialog.setHeaderText("Are you sure you want to remove this associated part?");
                confirmDialog.setContentText("This action cannot be undone.");
                Optional<ButtonType> result = confirmDialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    product.deleteAssociatedPart(selectedPart);
                    associatedParts.setAll(product.getAllAssociatedParts());
                    associatedPartsTable.setItems(associatedParts);
                }
            }
            if (selectedPart == null) {
                Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
                nothingSelectedDialog.setTitle("Nothing Selected");
                nothingSelectedDialog.setHeaderText("You must select a part!");
                nothingSelectedDialog.setContentText("In order to remove an associated part, please select one from the lower table to continue.");
                nothingSelectedDialog.showAndWait();
            }
        });

        //There is likely a much less "spaghetti code" version of doing this, but it's the best I could do for now
        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by Part ID or Name");
        partsSearchBox.prefWidthProperty().set(210);
        partsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+")) {
                if (Integer.parseInt(newValue) == 0) {
                    partsTableCopy.setItems(Inventory.getAllParts());
                } else {
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    for (Part part : Inventory.getAllParts()) {
                        if (String.valueOf(part.getId()).contains(String.valueOf(newValue))) {
                            filteredParts.add(part);
                        }
                    }
                    partsTableCopy.setItems(filteredParts);
                    if (filteredParts.isEmpty()) {
                        Label noResultsLabel = new Label("NO SUCH PART EXISTS");
                        partsTableCopy.setPlaceholder(noResultsLabel);
                    }
                }
            } else if (newValue.isEmpty()) {
                partsTableCopy.setItems(Inventory.getAllParts());
            } else {
                ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredParts.add(part);
                    }
                }
                partsTableCopy.setItems(filteredParts);
                if (filteredParts.isEmpty()) {
                    Label noResultsLabel = new Label("NO SUCH PART EXISTS");
                    partsTableCopy.setPlaceholder(noResultsLabel);
                }
            }
        });

        //Need to have search bar be in a more appropriate spot
        HBox partsTitleAndSearch = new HBox();
        HBox.setHgrow(partsSearchBox, Priority.NEVER);
        HBox.setMargin(partsSearchBox, new Insets(0, 0, 0, 10));
        partsTitleAndSearch.getChildren().addAll(new Label("Table of Currently Available Parts:"), partsSearchBox);

        // A Grid for the parts list, related buttons, and associated parts
        GridPane partsListGrid = new GridPane();
        partsListGrid.setHgap(10);
        partsListGrid.setVgap(10);
        partsListGrid.addRow(0, partsTitleAndSearch);
        partsListGrid.addRow(1, partsTableCopy);
        partsListGrid.addRow(2, addAssociatedPartButton);
        partsListGrid.addRow(3, new Label("Table of Currently Associated Parts for this Product:"));
        partsListGrid.addRow(4, associatedPartsTable);
        partsListGrid.addRow(5, removeAssociatedPartButton);

        // Hbox to keep these two grids side-by-side
        HBox addProductContent = new HBox();
        HBox.setHgrow(partsListGrid, Priority.NEVER);
        HBox.setMargin(partsListGrid, new Insets(0, 0, 0, 50));
        addProductContent.getChildren().addAll(specsGrid, partsListGrid);

        // Enable/disable the save button depending on whether any fields are empty
        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        inventoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the dialog content to the grid
        getDialogPane().setContent(addProductContent);

        // Request focus on the column1 field by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to an item when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                        inventoryField.getText().isEmpty() ||
                        minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                    // Display an alert dialog informing the user that all fields are required
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("All fields are required");
                    alert.setContentText("Please fill in all fields before saving.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(minField.getText()) > Integer.parseInt(maxField.getText())) {
                    minField.clear();
                    minField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Minimum must be less than Maximum.");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(inventoryField.getText()) < Integer.parseInt(minField.getText()) || Integer.parseInt(inventoryField.getText()) > Integer.parseInt(maxField.getText())) {
                    inventoryField.clear();
                    inventoryField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Inventory amount must be between Maximum and Minimum");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (!priceField.getText().matches("^\\d+(\\.\\d{1,2})?$")) {
                    // Creates error if price field does receive an appropriate decimal number
                    priceField.clear();
                    priceField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter a valid decimal number to represent price. e.x. '1.27'");
                    alert.showAndWait();
                }
                //I needed make this into a named variable "newProduct" in order to call the getAllAssociatedParts() method for it so that I could set the AsspartsTable items to it and return it, so that the modify method could properly pull the data from that array
                Product newProduct = new Product(
                        IDGenerator.generateID(),
                        nameField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(inventoryField.getText()),
                        Integer.parseInt(minField.getText()),
                        Integer.parseInt(maxField.getText())
                        //Do I need to add the associated parts here one more time???
                );
                newProduct.getAllAssociatedParts().setAll(associatedPartsTable.getItems());
                return newProduct;
            }
            return null;
        });
    };
};

/**
 * This class creates a dialog window where the user can update the automatically populated fields to alter a selected Part object and save it to the inventory.
 */
class PartUpdateDialog extends Dialog<Part> {
    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField inventoryField = new TextField();
    private final TextField partOriginField = new TextField();
    private final TextField minField = new TextField();
    private final TextField maxField = new TextField();
    private final ToggleGroup typeGroup = new ToggleGroup();
    private final RadioButton inHouseButton = new RadioButton("In-House");
    private final RadioButton outsourcedButton = new RadioButton("Outsourced");
    private final Label partOriginLabel = new Label();
    private final ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
    private final ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

    /**
     * This class creates a dialog window where the user can update the automatically populated fields to alter a selected Part's data. Editable fields include name, price, stock, minimum, maximum, and the part's origin of either being an InHouse item or Outsourced.
     * @param selectedPart The Part object object that will be updated
     */
    public PartUpdateDialog(Part selectedPart) {
        setTitle("Modify Part");
        setHeaderText("Confirm data is correct and update as necessary.");

        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        inHouseButton.setToggleGroup(typeGroup);
        outsourcedButton.setToggleGroup(typeGroup);

        if (selectedPart instanceof InHouse) {
            inHouseButton.setSelected(true);
            partOriginLabel.setText("Machine ID:");
            partOriginField.setText(Integer.toString(((InHouse) selectedPart).getMachineId()));
        } else if (selectedPart instanceof Outsourced) {
            outsourcedButton.setSelected(true);
            partOriginLabel.setText("Company Name:");
            partOriginField.setText(((Outsourced) selectedPart).getCompanyName());
        }

        typeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == outsourcedButton) {
                partOriginLabel.setText("Company Name:");
            } else if (newValue == inHouseButton) {
                partOriginLabel.setText("Machine ID:");
            }
        });

        idField.setText(Integer.toString(selectedPart.getId()));
        idField.setEditable(false);
        idField.setDisable(true);

        nameField.setText(selectedPart.getName());

        priceField.setText(Double.toString(selectedPart.getPrice()));

        inventoryField.setText(Integer.toString(selectedPart.getStock()));

        minField.setText(Integer.toString(selectedPart.getMin()));

        maxField.setText(Integer.toString(selectedPart.getMax()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Part Origin"), inHouseButton, outsourcedButton);
        grid.addRow(1, new Label("Part ID:"), idField);
        grid.addRow(2, new Label("Part Name:"), nameField);
        grid.addRow(3, new Label("Price/Cost:"), priceField);
        grid.addRow(4, new Label("Inventory Level:"), inventoryField);
        grid.addRow(5, new Label("Minimum:"), minField);
        grid.addRow(6, new Label("Maximum:"), maxField);
        grid.addRow(7, partOriginLabel, partOriginField);

        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        idField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        inventoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        partOriginField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        getDialogPane().setContent(grid);

        Platform.runLater(() -> nameField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Part updatedPart;
                // Check if any of the fields are empty
                if (nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                        inventoryField.getText().isEmpty() || partOriginField.getText().isEmpty() ||
                        minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                    // Display an alert dialog informing the user that all fields are required
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("All fields are required");
                    alert.setContentText("Please fill in all fields before saving.");
                    alert.showAndWait();

                }
                if (Integer.parseInt(minField.getText()) > Integer.parseInt(maxField.getText())) {
                    minField.clear();
                    minField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Minimum must be less than Maximum.");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(inventoryField.getText()) < Integer.parseInt(minField.getText()) || Integer.parseInt(inventoryField.getText()) > Integer.parseInt(maxField.getText())) {
                    inventoryField.clear();
                    inventoryField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Inventory amount must be between Maximum and Minimum");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (!priceField.getText().matches("^\\d+(\\.\\d{1,2})?$")) {
                    // Creates error if price field does receive an appropriate decimal number
                    priceField.clear();
                    priceField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter a valid decimal number to represent price. e.x. '1.27'");
                    alert.showAndWait();
                }
                if (inHouseButton.isSelected()) {
                    // Creates error if fields that should only accept integers have anything else in their input
                    if (!inventoryField.getText().matches("^\\d+$") || !minField.getText().matches("^\\d+$") ||
                            !maxField.getText().matches("^\\d+$") || !partOriginField.getText().matches("^\\d+$")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for In-House Part");
                        alert.setContentText("Please enter a valid integer for fields Inventory, Min, Max, and Machine ID.");
                        alert.showAndWait();
                    }
                }
                if (outsourcedButton.isSelected()) {
                    // Creates error if fields that should only accept integers have anything else in their input
                    if (!inventoryField.getText().matches("^\\d+$") || !minField.getText().matches("^\\d+$") ||
                            !maxField.getText().matches("^\\d+$")) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for Outsourced Part");
                        alert.setContentText("Please enter a valid integer for fields Inventory, Min, and Max");
                        alert.showAndWait();
                    }
                    if (partOriginField.getText().matches("^\\d*")) {
                        // Creates error specifically for Outsourced Part's Company Name field if it only receives integers
                        partOriginField.clear();
                        partOriginField.requestFocus();
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Input Error");
                        alert.setHeaderText("Invalid Input for Outsourced Part");
                        alert.setContentText("Please enter a combination of alphanumeric characters to create a valid company name. e.x 'Sector9 Skateboards'");
                        alert.showAndWait();
                    }
                }
                if (inHouseButton.isSelected()) {
                    updatedPart = new InHouse(
                            Integer.parseInt(idField.getText()),
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(inventoryField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            Integer.parseInt(partOriginField.getText())
                    );
                } else {
                    updatedPart = new Outsourced(
                            Integer.parseInt(idField.getText()),
                            nameField.getText(),
                            Double.parseDouble(priceField.getText()),
                            Integer.parseInt(inventoryField.getText()),
                            Integer.parseInt(minField.getText()),
                            Integer.parseInt(maxField.getText()),
                            partOriginField.getText()
                    );
                }
                return updatedPart;
            }
            return null;
        });
    }
}

/**
 * This class creates a dialog window where the user can update the automatically populated fields to alter a selected Product object and save it to the inventory.
 */
class ProductUpdateDialog extends Dialog<Product> {
    private final TextField IDField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField inventoryField = new TextField();
    private final TextField minField = new TextField();
    private final TextField maxField = new TextField();
    //private final Product selectedProduct; //Didn't need this; was redundant
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private TableView<Part> associatedPartsTable = new TableView<>();

    /**
     * This class creates a dialog window where the user can update the automatically populated fields to alter a selected Product's data. Editable fields include name, price, stock, minimum, maximum, and the product's list of associated parts.
     * @param selectedProduct
     */
    public ProductUpdateDialog(Product selectedProduct) {

        //This seems like the right idea???
        associatedParts.setAll(selectedProduct.getAllAssociatedParts());

        // Set the dialog title and header text
        setTitle("Modify Product");
        setHeaderText("Please confirm data is correct and/or edit as necessary.");

        TableView<Part> partsTableCopy = new TableView<>();
        //Gets all the parts from the private allParts variable in Inventory Class
        partsTableCopy.setItems(Inventory.getAllParts());
        partsTableCopy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        partsTableCopy.setMaxHeight(150);
        partsTableCopy.setPrefWidth(450);

        TableColumn<Part, String> partIDColumn = new TableColumn<>("Part ID");
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part, String> partNameColumn = new TableColumn<>("Part Name");
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> partInventoryColumn = new TableColumn<>("Inventory Level");
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> partPriceColumn = new TableColumn<>("Price per Unit");
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partPriceColumn.setCellFactory(column -> {
            TableCell<Part, Double> cell = new TableCell<Part, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        partsTableCopy.getColumns().addAll(partIDColumn, partNameColumn, partInventoryColumn, partPriceColumn);

        associatedPartsTable.setItems(associatedParts);
        associatedPartsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        associatedPartsTable.setMaxHeight(150);
        associatedPartsTable.setPrefWidth(450);

        TableColumn<Part, String> associatedPartIDColumn = new TableColumn<>("Part ID");
        associatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Part, String> associatedPartNameColumn = new TableColumn<>("Part Name");
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> associatedPartInventoryColumn = new TableColumn<>("Inventory Level");
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        TableColumn<Part, Double> associatedPartPriceColumn = new TableColumn<>("Price per Unit");
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartPriceColumn.setCellFactory(column -> {
            TableCell<Part, Double> cell = new TableCell<Part, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            };
            return cell;
        });

        associatedPartsTable.getColumns().addAll(associatedPartIDColumn, associatedPartNameColumn, associatedPartInventoryColumn, associatedPartPriceColumn);

        //I was doing this previously to say "no such parts" when search table was empty from search results, which wasn't exactly logically correct
        //Label emptyAssocPartTableLabel = new Label("THERE ARE CURRENTLY NO ASSOCIATED PARTS");
        //associatedPartsTable.setPlaceholder(emptyAssocPartTableLabel);

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        IDField.setText(Integer.toString(selectedProduct.getId()));
        Label IDLabel = new Label("Product ID:");
        IDField.setEditable(false);
        IDField.setDisable(true);

        nameField.setText(selectedProduct.getName());
        Label nameLabel = new Label("Product Name:");

        priceField.setText(Double.toString(selectedProduct.getPrice()));
        Label priceLabel = new Label("Price per Unit:");

        inventoryField.setText(Integer.toString(selectedProduct.getStock()));
        Label inventoryLabel = new Label("Inventory Level:");

        minField.setText(Integer.toString(selectedProduct.getMin()));
        Label minLabel = new Label("Minimum:");

        maxField.setText(Integer.toString(selectedProduct.getMax()));
        Label maxLabel = new Label("Maximum:");

        // A Grid for the TextField specs for Products
        GridPane specsGrid = new GridPane();
        specsGrid.setHgap(10);
        specsGrid.setVgap(10);
        specsGrid.addRow(0, IDLabel, IDField, partsTableCopy);
        specsGrid.addRow(1, nameLabel, nameField);
        specsGrid.addRow(2, priceLabel, priceField);
        specsGrid.addRow(3, inventoryLabel, inventoryField);
        specsGrid.addRow(5, minLabel, minField);
        specsGrid.addRow(6, maxLabel, maxField);
        VBox.setMargin(specsGrid, new Insets(10, 10, 10, 10));

        //Need to move this button earlier in the code so that I can un-disable it after adding/removing associated parts
        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        Button addAssociatedPartButton = new Button("Add Associated Part");
        addAssociatedPartButton.setOnAction(event -> {
            Part selectedPart = partsTableCopy.getSelectionModel().getSelectedItem();
            //determing if selectedPart is not null is more clear
            if (selectedPart != null) {
                selectedProduct.addAssociatedPart(selectedPart);
                associatedParts.setAll(selectedProduct.getAllAssociatedParts());
                associatedPartsTable.setItems(associatedParts);
                saveButton.setDisable(false);
            }
            if (selectedPart == null) {
                Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
                nothingSelectedDialog.setTitle("Nothing Selected");
                nothingSelectedDialog.setHeaderText("You must select a part!");
                nothingSelectedDialog.setContentText("In order to add an associated part, please select one from the upper table to continue.");
                nothingSelectedDialog.showAndWait();
            }
        });

        Button removeAssociatedPartButton = new Button("Remove Associated Part");
        removeAssociatedPartButton.setOnAction(event -> {
            Part selectedPart = associatedPartsTable.getSelectionModel().getSelectedItem();
            if (selectedPart != null) {
                Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmDialog.setTitle("Confirm Removal");
                confirmDialog.setHeaderText("Are you sure you want to remove this associated part?");
                confirmDialog.setContentText("This action cannot be undone.");
                Optional<ButtonType> result = confirmDialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    selectedProduct.deleteAssociatedPart(selectedPart);
                    associatedParts.remove(selectedPart);
                    associatedPartsTable.setItems(associatedParts);
                    saveButton.setDisable(false);
                }
            }
            if (selectedPart == null) {
                Alert nothingSelectedDialog = new Alert(Alert.AlertType.INFORMATION);
                nothingSelectedDialog.setTitle("Nothing Selected");
                nothingSelectedDialog.setHeaderText("You must select a part!");
                nothingSelectedDialog.setContentText("In order to remove an associated part, please select one from the lower table to continue.");
                nothingSelectedDialog.showAndWait();
            }
        });

        TextField partsSearchBox = new TextField();
        partsSearchBox.setPromptText("Search by Part ID or Name");
        partsSearchBox.prefWidthProperty().set(210);
        partsSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d+")) {
                if (Integer.parseInt(newValue) == 0) {
                    partsTableCopy.setItems(Inventory.getAllParts());
                } else {
                    ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                    for (Part part : Inventory.getAllParts()) {
                        if (String.valueOf(part.getId()).contains(String.valueOf(newValue))) {
                            filteredParts.add(part);
                        }
                    }
                    partsTableCopy.setItems(filteredParts);
                    if (filteredParts.isEmpty()) {
                        Label noResultsLabel = new Label("NO SUCH PART EXISTS");
                        partsTableCopy.setPlaceholder(noResultsLabel);
                    }
                }
            } else if (newValue.isEmpty()) {
                partsTableCopy.setItems(Inventory.getAllParts());
            } else {
                ObservableList<Part> filteredParts = FXCollections.observableArrayList();
                for (Part part : Inventory.getAllParts()) {
                    if (part.getName().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredParts.add(part);
                    }
                }
                partsTableCopy.setItems(filteredParts);
                if (filteredParts.isEmpty()) {
                    Label noResultsLabel = new Label("NO SUCH PART EXISTS");
                    partsTableCopy.setPlaceholder(noResultsLabel);
                }
            }
        });

        HBox partsTitleAndSearch = new HBox();
        HBox.setHgrow(partsSearchBox, Priority.NEVER);
        HBox.setMargin(partsSearchBox, new Insets(0, 0, 0, 10));
        partsTitleAndSearch.getChildren().addAll(new Label("Table of Currently Available Parts:"), partsSearchBox);

        // A Grid for the parts list, related buttons, and associated parts
        GridPane partsListGrid = new GridPane();
        partsListGrid.setHgap(10);
        partsListGrid.setVgap(10);
        partsListGrid.addRow(0, partsTitleAndSearch);
        partsListGrid.addRow(1, partsTableCopy);
        partsListGrid.addRow(2, addAssociatedPartButton);
        partsListGrid.addRow(3, new Label("Table of Currently Associated Parts for this Product:"));
        partsListGrid.addRow(4, associatedPartsTable);
        partsListGrid.addRow(5, removeAssociatedPartButton);

        // Hbox to keep these two grids side-by-side
        HBox modifyProductContent = new HBox();
        HBox.setHgrow(partsListGrid, Priority.NEVER);
        HBox.setMargin(partsListGrid, new Insets(0, 0, 0, 50));
        modifyProductContent.getChildren().addAll(specsGrid, partsListGrid);

        // Enable/disable the save button depending on whether any fields are empty

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        inventoryField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        minField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        maxField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty());
        });

        // Set the dialog content to the grid
        getDialogPane().setContent(modifyProductContent);

        // Puts focus on nameField by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to a new product when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (nameField.getText().isEmpty() || priceField.getText().isEmpty() ||
                        inventoryField.getText().isEmpty() ||
                        minField.getText().isEmpty() || maxField.getText().isEmpty()) {
                    // Display an alert dialog informing the user that all fields are required
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("All fields are required");
                    alert.setContentText("Please fill in all fields before saving.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(minField.getText()) > Integer.parseInt(maxField.getText())) {
                    minField.clear();
                    minField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Minimum must be less than Maximum.");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (Integer.parseInt(inventoryField.getText()) < Integer.parseInt(minField.getText()) || Integer.parseInt(inventoryField.getText()) > Integer.parseInt(maxField.getText())) {
                    inventoryField.clear();
                    inventoryField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Inventory amount must be between Maximum and Minimum");
                    alert.setContentText("Please correct this error to continue.");
                    alert.showAndWait();
                }
                if (!priceField.getText().matches("^\\d+(\\.\\d{1,2})?$")) {
                    // Creates error if price field does receive an appropriate decimal number
                    priceField.clear();
                    priceField.requestFocus();
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Input Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter a valid decimal number to represent price. e.x. '1.27'");
                    alert.showAndWait();
                }
                // Update the existing associated parts list
                //associatedParts.setAll(associatedPartsTable.getItems()); //this is unnecessary

                //This made more sense to stick with selectedProduct rather than "creating" a new Product()
                selectedProduct.setName(nameField.getText());
                selectedProduct.setPrice(Double.parseDouble(priceField.getText()));
                selectedProduct.setStock(Integer.parseInt(inventoryField.getText()));
                selectedProduct.setMin(Integer.parseInt(minField.getText()));
                selectedProduct.setMax(Integer.parseInt(maxField.getText()));

                selectedProduct.getAllAssociatedParts().setAll(associatedPartsTable.getItems()); //This was previuoosly set to just .setAll(associatedParts) and it wasn't quite saving the data correctly to be displayed/persist to the next dialog window opening

                return selectedProduct;
            }
            return null;
        });

    };
};
