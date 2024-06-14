package controller;

import domain.SinglyLinkedListGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class LinkedGraphController {

    @FXML
    private Text textInfo;

    @FXML
    private Pane dibujo;

    @FXML
    private TextArea infoTextArea;

    private SinglyLinkedListGraph graph;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private final double centerX = 300; // Coordenada X del centro del círculo
    private final double centerY = 300; // Coordenada Y del centro del círculo
    private final double circleRadius = 200; // Radio del círculo

    @FXML
    public void initialize() {
        graph = new SinglyLinkedListGraph();
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

        String[] names = {"Einstein", "Newton", "Curie", "Galileo", "Tesla", "Darwin", "Hawking", "Turing", "Edison", "Lovelace"};
        int totalVertices = names.length;
        double angleStep = 2 * Math.PI / totalVertices;

        // Calcular posiciones de los vértices en el círculo
        for (int i = 0; i < totalVertices; i++) {
            try {
                graph.addVertex(names[i]);
                Vertex vertex = new Vertex(names[i]);

                // Calcular posición en el círculo
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

        // Dibujar aristas entre vértices de manera aleatoria
        Random random = new Random();
        for (int i = 0; i < totalVertices; i++) {
            for (int j = i + 1; j < totalVertices; j++) {
                if (random.nextBoolean()) {
                    try {
                        int weight = 1000 + random.nextInt(2000); // Peso entre 1 y 10
                        graph.addEdgeWeight(vertices.get(i).getData(), vertices.get(j).getData(), weight);
                        drawEdge(vertices.get(i), vertices.get(j), weight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    public void containsEdgeOnAction(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Contains Edge Check");
        dialog.setHeaderText("Enter two vertex names separated by a comma:");
        dialog.setContentText("Vertices:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String input = result.get().trim();
            String[] vertices = input.split(",");
            if (vertices.length != 2) {
                infoTextArea.setText("Please enter two vertex names separated by a comma.");
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

    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text text = new Text(vertex.getX() - vertexRadius / 2, vertex.getY() + vertexRadius / 2, vertex.getData());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.BLACK);

        // Ajustar el tamaño de la fuente del texto
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14)); // Ejemplo: Fuente Arial, negrita, tamaño 14

        Group vertexGroup = new Group(circle, text);
        dibujo.getChildren().add(vertexGroup);
    }

    private void drawEdge(Vertex v1, Vertex v2, int weight) {
        Line line = new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);

        Text weightText = new Text((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, String.valueOf(weight));
        weightText.setBoundsType(TextBoundsType.VISUAL);
        weightText.setFill(Color.RED);

        // Cambiar color de la arista y mostrar peso al pasar el ratón sobre ella
        line.setOnMouseEntered(e -> {
            line.setStroke(Color.BLUE); // Cambiar color de la arista al pasar el ratón
            weightText.setFill(Color.BLUE); // Cambiar color del texto del peso
            textInfo.setText("Peso de la arista: " + weight); // Mostrar peso en textInfo
        });

        // Restaurar color original de la arista al salir el ratón
        line.setOnMouseExited(e -> {
            line.setStroke(Color.GRAY); // Restaurar color original de la arista
            weightText.setFill(Color.RED); // Restaurar color original del texto del peso
            textInfo.setText(""); // Limpiar textInfo
        });

        Group edgeGroup = new Group(line, weightText);
        dibujo.getChildren().add(edgeGroup);
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