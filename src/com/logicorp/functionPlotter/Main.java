package com.logicorp.functionPlotter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.logicorp.functionPlotter.graph.*;
import com.logicorp.functionPlotter.parser.Parser;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;


/**
 *
 * @author hakim 
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {

        
        Parser parser = new Parser();
        String Expression = "(1+x)";

        BorderPane bp = new BorderPane();
        Pane topPane = new Pane();
        TextField tf = new TextField();

        topPane.setPrefHeight(56.0d);
        topPane.setStyle("-fx-background-color: rgb(68, 72, 83);");

        tf.setPromptText("enter the function here");
        tf.setPrefWidth(174.0d);
        tf.setLayoutX(35.0d);
        tf.setLayoutY(16.0d);

        Button btn = new Button();
        btn.setLayoutX(216.0d);
        btn.setLayoutY(16.0d);
        btn.setText("plot");

        topPane.getChildren().addAll(tf, btn);

        Plot plot;

       
        Axes axes = new Axes(
                800, 600,
                -16, 16, 1,
                -12, 12, 1
        );

        plot = new Plot(
                x -> {
                    parser.setValue("x", x);
                    return parser.eval(Expression);
                },
                -16, 16, 0.01,
                axes
        );
        
        

        StackPane layout = new StackPane(
                plot
        );

        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: rgb(35, 39, 50);");

        btn.setOnAction((ActionEvent e) -> {
            layout.getChildren().clear();
            layout.getChildren().add(
                    new Plot(
                    x -> {
                        parser.setValue("x", x);
                        return parser.eval(tf.getText());             
                    },
                    -16, 16, 0.01,
                    axes
            ));
            stage.setTitle(tf.getText());
        });

        
        bp.setCenter(layout);
        bp.setTop(topPane);

        stage.setTitle(Expression);

        stage.setScene(new Scene(bp, Color.rgb(35, 39, 50)));

        stage.show();
    }
    
    public static void main(){
        
    }

}
