/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.course.java.fxpaint;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Paint tools.
 */
public class PaintTools {

    /**
     * Tool for painting a line.
     */
    public static class LineTool implements PaintTool {

        private Line line;

        @Override
        public void onMousePressed(MouseEvent event, PaintCanvas canvas) {
            double x = event.getX();
            double y = event.getY();
            line = new Line(x, y, x, y);
            canvas.addShape(line);
        }

        @Override
        public void onMouseDragged(MouseEvent event, PaintCanvas canvas) {

            if (line == null) {
                return;
            }

            line.setEndX(event.getX());
            line.setEndY(event.getY());
        }

        @Override
        public void onMouseReleased(MouseEvent event, PaintCanvas canvas) {
            onMouseDragged(event, canvas);
            onEnd();
        }

        @Override
        public void onEnd() {
            line = null;
        }
    }

    /**
     * Tool for painting a rectangle.
     */
    public static class RectangleTool implements PaintTool {

        private double x;
        private double y;
        private Rectangle rectangle;

        @Override
        public void onMousePressed(MouseEvent event, PaintCanvas canvas) {
            x = event.getX();
            y = event.getY();
            rectangle = new Rectangle(x, y, 0, 0);
            canvas.addShape(rectangle);
        }

        @Override
        public void onMouseDragged(MouseEvent event, PaintCanvas canvas) {

            if (rectangle == null) {
                return;
            }

            double eventX = event.getX();
            rectangle.setX(Math.min(x, eventX));
            rectangle.setWidth(Math.abs(eventX - x));
            double eventY = event.getY();
            rectangle.setY(Math.min(y, eventY));
            rectangle.setHeight(Math.abs(eventY - y));
        }

        @Override
        public void onMouseReleased(MouseEvent event, PaintCanvas canvas) {
            onMouseDragged(event, canvas);
            onEnd();
        }

        @Override
        public void onEnd() {
            rectangle = null;
        }
    }

    /**
     * Tool for painting an ellipse.
     */
    public static class EllipseTool implements PaintTool {

        private double x;
        private double y;
        private Ellipse ellipse;

        @Override
        public void onMousePressed(MouseEvent event, PaintCanvas canvas) {
            x = event.getX();
            y = event.getY();
            ellipse = new Ellipse(x, y, 0, 0);
            canvas.addShape(ellipse);
        }

        @Override
        public void onMouseDragged(MouseEvent event, PaintCanvas canvas) {

            if (ellipse == null) {
                return;
            }

            double eventX = event.getX();
            ellipse.setCenterX(MathUtils.mean(x, eventX));
            ellipse.setRadiusX(Math.abs(eventX - x) / 2);
            double eventY = event.getY();
            ellipse.setCenterY(MathUtils.mean(y, eventY));
            ellipse.setRadiusY(Math.abs(eventY - y) / 2);
        }

        @Override
        public void onMouseReleased(MouseEvent event, PaintCanvas canvas) {
            onMouseDragged(event, canvas);
            onEnd();
        }

        @Override
        public void onEnd() {
            ellipse = null;
        }
    }

    /**
     * Tool for painting text.
     */
    public static class TextTool implements PaintTool {

        private TextField textField;

        public void setTextField(TextField textField) {
            this.textField = textField;
        }

        @Override
        public void onMousePressed(MouseEvent event, PaintCanvas canvas) {}

        @Override
        public void onMouseDragged(MouseEvent event, PaintCanvas canvas) {}

        @Override
        public void onMouseReleased(MouseEvent event, final PaintCanvas canvas) {

            onEnd();

            textField.relocate(event.getX(), event.getY());
            textField.setVisible(true);
            textField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Text text = new Text(textField.getLayoutX(),
                            textField.getLayoutY() + textField.getHeight(),
                            textField.getCharacters().toString());
                    text.setFont(textField.getFont());
                    canvas.addShape(text);
                    textField.clear();
                    textField.setVisible(false);
                }
            });

            textField.requestFocus();
        }

        @Override
        public void onEnd() {
            if (textField.isVisible()) {
                textField.getOnAction().handle(null);
            }
        }
    }
}
