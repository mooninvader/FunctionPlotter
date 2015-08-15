package com.logicorp.functionPlotter.graph;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.util.function.Function;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author jewelsea
 *
 */
public class Plot extends Pane {

    public Plot(
            Function<Double, Double> f,
            double xMin, double xMax, double xInc,
            Axes axes
    ) {
        Path path = new Path();
        path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
        path.setStrokeWidth(2);

        path.setClip(
                new Rectangle(
                        0, 0,
                        axes.getPrefWidth(),
                        axes.getPrefHeight()
                )
        );

        double x = xMin;        
        boolean firstDotPlotted = false;

  
        while (x < xMax) {
           try {
                if (firstDotPlotted == true) {
                    path.getElements().add(
                            new LineTo(
                                    mapX(x, axes), mapY(f.apply(x), axes)
                            )
                    );
                } else {
                    path.getElements().add(
                            new MoveTo(
                                    mapX(x, axes), mapY(f.apply(x), axes)
                            )
                    );
                            firstDotPlotted=true;
                }

        } catch (Exception e) {
               System.out.println(e.getMessage());
               // firstDotPlotted=false;
            } finally {
                x += xInc;
            }
        }

        setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
        setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
        setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

        getChildren().setAll(axes, path);
    }

    private double mapX(double x, Axes axes) {
        double tx = axes.getPrefWidth() / 2;
        double sx = axes.getPrefWidth()
                / (axes.getXAxis().getUpperBound()
                - axes.getXAxis().getLowerBound());

        return x * sx + tx;
    }

    private double mapY(double y, Axes axes) {
        double ty = axes.getPrefHeight() / 2;
        double sy = axes.getPrefHeight()
                / (axes.getYAxis().getUpperBound()
                - axes.getYAxis().getLowerBound());
        return -y * sy + ty;
    }
}
