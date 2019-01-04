package sample;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.WINDOW_MODAL);
        window.setTitle(title);
        window.setWidth(800);
        window.setHeight(600);

        Label label = new Label(message);
        label.setPadding(new Insets(10, 10, 10, 10));
        label.setWrapText(true);
        Hyperlink closeLink = new Hyperlink("close");
        closeLink.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeLink);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
}