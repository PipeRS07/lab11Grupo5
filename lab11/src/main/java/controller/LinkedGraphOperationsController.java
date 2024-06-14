package controller;

import domain.GraphException;
import domain.SinglyLinkedListGraph;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
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
import java.util.Random;

public class LinkedGraphOperationsController {

    @FXML
    private Text textInfo;

    @FXML
    private Pane dibujo;

    @FXML
    private TextArea infoTextArea;

    private SinglyLinkedListGraph graph;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private final double centerX = 200; // Coordenada X del centro del círculo
    private final double centerY = 200; // Coordenada Y del centro del círculo
    private final double circleRadius = 200; // Radio del círculo

    @FXML
    public void initialize() {
        graph = new SinglyLinkedListGraph();
        vertices = new ArrayList<>();
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        infoTextArea.clear(); // Limpiar el contenido anterior
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

        Random random = new Random();
        int maxEdgesPerVertex = 1;  // Número máximo de aristas por nodo

        // Agregar aristas entre los vértices de forma aleatoria
        for (int i = 0; i < totalVertices; i++) {
            for (int j = 0; j < maxEdgesPerVertex; j++) {
                int randomIndex = random.nextInt(totalVertices);
                if (i != randomIndex) {
                    try {
                        int weight = 1000 + random.nextInt(1001);
                        graph.addEdgeWeight(vertices.get(i).getData(), vertices.get(randomIndex).getData(), weight);
                        drawEdge(vertices.get(i), vertices.get(randomIndex), weight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Obtener el resultado del método toString del grafo y mostrarlo en infoTextArea
        updateGraphInfo();
    }

    @FXML
    public void addVertexOnAction(ActionEvent actionEvent) {
        String[] names = {"AlejandroMagno", "Aristóteles", "Cleopatra", "Confucio", "Dalí", "DaVinci", "Dickens", "Dostoievski", "Freud", "GenghisKhan", "Gutenberg", "Homero", "Hypatia",
                "Lincoln", "Lovelace", "Mandela", "Marx", "Mozart", "Picasso", "Platón",
                "Shakespeare", "Sócrates", "Tesla", "Teresa", "Turing", "VanGogh"};
        if (vertices.size() < names.length) {
            String newName = names[vertices.size()];
            try {
                graph.addVertex(newName);
                Vertex vertex = new Vertex(newName);

                // Calcular posición aleatoria dentro del círculo
                double angle = 10 * Math.PI * Math.random();
                double x = centerX + circleRadius * Math.cos(angle);
                double y = centerY + circleRadius * Math.sin(angle);

                vertex.setX(x + 2);
                vertex.setY(y);

                vertices.add(vertex);
                drawVertex(vertex);

                // Actualizar infoTextArea
                updateGraphInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addEdgesWeigtsOnAction(ActionEvent actionEvent) {
        if (vertices.size() < 2) return;

        Random random = new Random();
        int i = random.nextInt(vertices.size());
        int j = random.nextInt(vertices.size());
        while (i == j) {
            j = random.nextInt(vertices.size());
        }

        try {
            int weight = 1000 + random.nextInt(1001); // Peso entre 1000 y 2000
            graph.addEdgeWeight(vertices.get(i).getData(), vertices.get(j).getData(), weight);
            drawEdge(vertices.get(i), vertices.get(j), weight);

            // Actualizar infoTextArea
            updateGraphInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeVertexOnAction(ActionEvent actionEvent) {
        if (vertices.isEmpty()) {
            return;
        }

        // Seleccionar un vértice para eliminar (por ejemplo, el último vértice añadido)
        Vertex vertexToRemove = vertices.get(vertices.size() - 1);
        try {
            graph.removeVertex(vertexToRemove.getData());
            vertices.remove(vertexToRemove);
            dibujo.getChildren().clear(); // Limpiar el dibujo
            redrawGraph(); // Redibujar el grafo sin el vértice eliminado

            // Actualizar infoTextArea
            updateGraphInfo();
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void removeEdgesWeigtsOnAction(ActionEvent actionEvent) {
        if (vertices.size() < 2) {
            return;
        }

        Random random = new Random();
        int i = random.nextInt(vertices.size());
        int j = random.nextInt(vertices.size());
        while (i == j) {
            j = random.nextInt(vertices.size());
        }

        Vertex v1 = vertices.get(i);
        Vertex v2 = vertices.get(j);

        try {
            graph.removeEdge(v1.getData(), v2.getData());
            dibujo.getChildren().clear(); // Limpiar el dibujo
            redrawGraph(); // Redibujar el grafo sin la arista eliminada

            // Actualizar infoTextArea
            updateGraphInfo();
        } catch (GraphException | ListException e) {
            e.printStackTrace();
        }
    }

    private void redrawGraph() {
        for (Vertex vertex : vertices) {
            drawVertex(vertex);
        }

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                try {
                    if (graph.containsEdge(vertices.get(i).getData(), vertices.get(j).getData())) {
                        // Suponiendo que el peso de la arista es 0 si no se almacena explícitamente
                        drawEdge(vertices.get(i), vertices.get(j), 0);
                    }
                } catch (GraphException | ListException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void clearOnAAction(ActionEvent actionEvent) {
        dibujo.getChildren().clear();
        vertices.clear();
        graph.clear();

        // Actualizar infoTextArea
        updateGraphInfo();
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
            textInfo.setText("Edge between vertices: " + v1.getData() + " - " + v2.getData() + ", Weight: " + weight); // Mostrar información en textInfo
        });

        // Restaurar color original de la arista al salir el ratón
        line.setOnMouseExited(e -> {
            line.setStroke(Color.GRAY); // Restaurar color original de la arista
            weightText.setFill(Color.RED); // Restaurar color original del texto del peso
            textInfo.setText(""); // Limpiar textInfo
        });

        Group edgeGroup = new Group(line);
        dibujo.getChildren().add(edgeGroup);
    }

    private void updateGraphInfo() {
        String graphInfo = graph.toString();
        infoTextArea.setText(graphInfo);
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