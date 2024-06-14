package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DirectedGraphController {

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Pane dibujo;

    @FXML
    private Text textInfo; // Añadido para mostrar información al pasar el ratón sobre la arista

    private DirectedSinglyLinkedListGraph graph;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private final double centerX = 200;
    private final double centerY = 180;
    private final double circleRadius = 200;

    @FXML
    public void initialize() {
        graph = new DirectedSinglyLinkedListGraph();
        vertices = new ArrayList<>();
    }

    @FXML
    public void toStringOnAction(ActionEvent actionEvent) {
        infoTextArea.setText(graph.toString());
    }

    @FXML
    public void DFSOnAction(ActionEvent actionEvent) {
        try {
            infoTextArea.setText(graph.dfs());
        } catch (Exception e) {
            infoTextArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        dibujo.getChildren().clear();
        vertices.clear();
        graph.clear();

        String[] monumentos = {
                "Gran Pirámide de Giza", "Taj Mahal", "Coliseo Romano", "Acrópolis de Atenas", "Muralla China",
                "Petra", "Machu Picchu", "Angkor Wat", "Sagrada Familia", "Torre Eiffel",
                "Estatua de la Libertad", "Cristo Redentor", "Castillo de Praga"
        };
        int totalVertices = monumentos.length;
        double angleStep = 2 * Math.PI / totalVertices;

        // Agregar los vértices al grafo y dibujarlos en círculo
        for (int i = 0; i < totalVertices; i++) {
            try {
                graph.addVertex(monumentos[i]);
                Vertex vertex = new Vertex(monumentos[i]);

                double angle = i * angleStep;
                double x = centerX + circleRadius * Math.cos(angle);
                double y = centerY + circleRadius * Math.sin(angle);

                vertex.setX(x);
                vertex.setY(y);

                vertices.add(vertex);
                drawVertex(vertex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Random random = new Random();
        int maxEdgesPerVertex = 2;  // Número máximo de aristas por nodo

        // Agregar aristas entre los vértices de forma aleatoria
        for (int i = 0; i < totalVertices; i++) {
            // Añadir al menos tres bucles
            for (int j = 0; j < maxEdgesPerVertex; j++) {
                int randomIndex = random.nextInt(totalVertices);

                // Evitar agregar múltiples aristas a un mismo vértice
                if (i != randomIndex || j < 3) {
                    try {
                        int weight = 1000 + random.nextInt(1001);
                        if (i == randomIndex) {
                            // Si el índice aleatorio es igual al índice actual, es un bucle
                            graph.addEdgeWeight(vertices.get(i).getData(), vertices.get(i).getData(), weight);
                            drawEdge(vertices.get(i), vertices.get(i), weight);
                        } else {
                            // De lo contrario, es una arista normal
                            graph.addEdgeWeight(vertices.get(i).getData(), vertices.get(randomIndex).getData(), weight);
                            drawEdge(vertices.get(i), vertices.get(randomIndex), weight);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    //Dibujar el grafo con sus respectivas lineas y flechas
    private void drawArrowHead(Line line, Pane pane) {
        double arrowLength = 10;
        double arrowWidth = 7;

        double ex = line.getEndX();
        double ey = line.getEndY();
        double sx = line.getStartX();
        double sy = line.getStartY();

        double angle = Math.atan2(ey - sy, ex - sx);

        double x1 = ex - arrowLength * Math.cos(angle - Math.PI / 6);
        double y1 = ey - arrowLength * Math.sin(angle - Math.PI / 6);
        double x2 = ex - arrowLength * Math.cos(angle + Math.PI / 6);
        double y2 = ey - arrowLength * Math.sin(angle + Math.PI / 6);

        Line arrow1 = new Line(ex, ey, x1, y1);
        Line arrow2 = new Line(ex, ey, x2, y2);

        arrow1.setStroke(line.getStroke());
        arrow2.setStroke(line.getStroke());
        arrow1.setStrokeWidth(line.getStrokeWidth());
        arrow2.setStrokeWidth(line.getStrokeWidth());

        pane.getChildren().addAll(arrow1, arrow2);
    }

    private void drawEdge(Vertex v1, Vertex v2, int weight) {
        if (v1 == v2) {
            // Si v1 y v2 son el mismo vértice (bucle), dibujamos una línea curvada
            double centerX = v1.getX();
            double centerY = v1.getY();
            double radius = vertexRadius * 1.5;

            // Calcular los puntos de control para la curva
            double controlX1 = centerX + radius * Math.cos(Math.PI / 4);
            double controlY1 = centerY - radius * Math.sin(Math.PI / 4);
            double controlX2 = centerX + radius * Math.cos(3 * Math.PI / 4);
            double controlY2 = centerY - radius * Math.sin(3 * Math.PI / 4);

            // Dibujar la curva que representa el bucle
            Line curve1 = new Line(centerX, centerY, controlX1, controlY1);
            Line curve2 = new Line(controlX1, controlY1, controlX2, controlY2);
            Line curve3 = new Line(controlX2, controlY2, centerX, centerY);

            curve1.setStroke(Color.BLACK);
            curve2.setStroke(Color.BLACK);
            curve3.setStroke(Color.BLACK);
            curve1.setStrokeWidth(1);
            curve2.setStrokeWidth(1);
            curve3.setStrokeWidth(1);

            Group curveGroup = new Group(curve1, curve2, curve3);
            dibujo.getChildren().add(curveGroup);

            Text weightText = new Text(centerX - 10, centerY + 20, "Loop: " + v1.getData() + ", Weight: " + weight);
            weightText.setBoundsType(TextBoundsType.VISUAL);
            weightText.setFill(Color.RED);

            curveGroup.setOnMouseEntered(e -> {
                curve1.setStroke(Color.CRIMSON); // Cambiar color de la línea al pasar el ratón
                curve2.setStroke(Color.CRIMSON);
                curve3.setStroke(Color.CRIMSON);
                textInfo.setText("Loop between vertex: " + v1.getData() + ", Weight: " + weight); // Mostrar información en textInfo
            });

            curveGroup.setOnMouseExited(e -> {
                curve1.setStroke(Color.BLACK); // Restaurar color original de la línea al salir el ratón
                curve2.setStroke(Color.BLACK);
                curve3.setStroke(Color.BLACK);
                textInfo.setText(""); // Limpiar textInfo
            });

            Group loopGroup = new Group(curveGroup);
            dibujo.getChildren().add(loopGroup);
        } else {
            // Si no es un bucle, dibujamos la línea normalmente
            Line line = new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(1); // Puedes ajustar el grosor de la línea aquí

            Text weightText = new Text((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, String.valueOf(weight));
            weightText.setBoundsType(TextBoundsType.VISUAL);
            weightText.setFill(Color.RED);

            // Cambiar color de la arista al pasar el ratón sobre ella
            line.setOnMouseEntered(e -> {
                line.setStroke(Color.CRIMSON); // Color de la arista al pasar el ratón
                textInfo.setText("Edge between vertices: " + v1.getData() + " - " + v2.getData() + ", Weight: " + weight); // Mostrar información en textInfo
            });

            // Restaurar color original de la arista al salir el ratón
            line.setOnMouseExited(e -> {
                line.setStroke(Color.BLACK); // Color original de la arista
                textInfo.setText(""); // Limpiar textInfo
            });

            Group edgeGroup = new Group(line, weightText);
            dibujo.getChildren().add(edgeGroup);

            drawArrowHead(line, dibujo); // Añadir la flecha
        }
    }




    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text text = new Text(vertex.getX() - vertexRadius / 2, vertex.getY() + vertexRadius / 2, vertex.getData());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.BLACK);
        text.setStyle("-fx-font: 14px Arial; -fx-font-weight: bold;");

        Group vertexGroup = new Group(circle, text);
        dibujo.getChildren().add(vertexGroup);
    }

    @FXML
    public void containsEdgeOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contains Edge Check");
        dialog.setHeaderText("Enter two vertex monumentos separated by a comma:");
        dialog.setContentText("Vertices:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String input = result.get().trim();
            String[] vertices = input.split(",");
            if (vertices.length != 2) {
                infoTextArea.setText("Please enter two vertex monumentos separated by a comma.");
                return;
            }
            try {
                boolean containsEdge = graph.containsEdge(vertices[0].trim(), vertices[1].trim());
                infoTextArea.setText("Contains edge between " + vertices[0] + " and " + vertices[1] + ": " + containsEdge);
            } catch (Exception e) {
                infoTextArea.setText("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void BFSOnAAction(ActionEvent actionEvent) {
        try {
            infoTextArea.setText(graph.bfs());
        } catch (Exception e) {
            infoTextArea.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void containsVextexOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contains Vertex Check");
        dialog.setHeaderText("Enter vertex name:");
        dialog.setContentText("Vertex:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String vertexName = result.get().trim();
            try {
                boolean containsVertex = graph.containsVertex(vertexName);
                infoTextArea.setText("Contains vertex " + vertexName + ": " + containsVertex);
            } catch (Exception e) {
                infoTextArea.setText("Error: " + e.getMessage());
            }
        }
    }


    class Vertex {
        private String data;
        private double x, y;

        public Vertex(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}