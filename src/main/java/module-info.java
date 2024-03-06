module sk_gui.skateboard_company_gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens sk_gui.skateboard_company_gui to javafx.fxml;
    exports sk_gui.skateboard_company_gui;
}