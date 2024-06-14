package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class DijkstraController {

    @FXML
    private TableColumn<VertexDistance, Integer> Vertex;
    @FXML
    private RadioButton AdjMatrix;
    @FXML
    private TableColumn<VertexDistance, Integer> Distance;
    @FXML
    private RadioButton AdjList;
    @FXML
    private Pane dibujo;
    @FXML
    private TableView<VertexDistance> tableView;
    @FXML
    private TableColumn<VertexDistance, Integer> Position;
    @FXML
    private RadioButton LinkedList;

    private Grafo grafo;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private double centerX;
    private double centerY;
    private double circleRadius;

    @FXML
    public void initialize() {
        vertices = new ArrayList<>();
        // Configurar cell value factory para las columnas de la tabla
        Position.setCellValueFactory(cellData -> cellData.getValue().positionProperty().asObject());
        Vertex.setCellValueFactory(cellData -> cellData.getValue().vertexProperty().asObject());
        Distance.setCellValueFactory(cellData -> cellData.getValue().distanceProperty().asObject());
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        dibujo.getChildren().clear();
        vertices.clear();

        // Configurar las coordenadas del centro y el radio del círculo basado en el tamaño del Pane
        centerX = dibujo.getWidth() / 2;
        centerY = dibujo.getHeight() / 2;
        circleRadius = Math.min(centerX, centerY) - vertexRadius - 10; // Dejar un pequeño margen

        Random random = new Random();
        int numVertices = random.nextInt(10) + 1;
        grafo = new Grafo(numVertices);

        double angleStep = 2 * Math.PI / numVertices;

        // Calcular posiciones de los vértices en el círculo
        for (int i = 0; i < numVertices; i++) {
            Vertex vertex = new Vertex(i);

            // Calcular posición en el círculo
            double angle = i * angleStep;
            double x = centerX + circleRadius * Math.cos(angle);
            double y = centerY + circleRadius * Math.sin(angle);

            vertex.setX(x);
            vertex.setY(y);

            vertices.add(vertex);
            drawVertex(vertex);
        }

        // Dibujar aristas entre vértices de manera aleatoria
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (random.nextBoolean()) {
                    int peso = 200 + random.nextInt(801);
                    grafo.agregarArista(i, j, peso);
                    grafo.agregarArista(j, i, peso); // Asegurar que sea bidireccional
                    drawEdge(vertices.get(i), vertices.get(j), peso);
                }
            }
        }

        // Aplicar algoritmo de Dijkstra
        int[] distancias = Dijkstra.dijkstra(grafo.getMatrizAdyacencia(), 0);

        // Mostrar resultados en la tabla
        ObservableList<VertexDistance> data = FXCollections.observableArrayList();
        for (int i = 0; i < distancias.length; i++) {
            data.add(new VertexDistance(i, i, distancias[i]));
        }
        tableView.setItems(data);

        // Actualizar la vista de la tabla para que se muestren los datos
        tableView.refresh();
    }

    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text text = new Text(vertex.getX() - vertexRadius / 2, vertex.getY() + vertexRadius / 2, String.valueOf(vertex.getId()));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

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

        line.setOnMouseEntered(e -> {
            line.setStroke(Color.BLUE);
            weightText.setFill(Color.BLUE);
        });

        line.setOnMouseExited(e -> {
            line.setStroke(Color.GRAY);
            weightText.setFill(Color.RED);
        });

        Group edgeGroup = new Group(line, weightText);
        dibujo.getChildren().add(edgeGroup);
    }

    private static class Grafo {
        private final int numVertices;
        private final int[][] matrizAdyacencia;

        public Grafo(int numVertices) {
            this.numVertices = numVertices;
            matrizAdyacencia = new int[numVertices][numVertices];
        }

        public void agregarArista(int origen, int destino, int peso) {
            matrizAdyacencia[origen][destino] = peso;
        }

        public int[][] getMatrizAdyacencia() {
            return matrizAdyacencia;
        }
    }

    private static class Dijkstra {
        public static int[] dijkstra(int[][] grafo, int src) {
            int n = grafo.length;
            int[] dist = new int[n];
            boolean[] visitados = new boolean[n];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[src] = 0;

            for (int i = 0; i < n - 1; i++) {
                int u = minDist(dist, visitados);
                visitados[u] = true;

                for (int v = 0; v < n; v++) {
                    if (!visitados[v] && grafo[u][v] != 0 &&
                            dist[u] != Integer.MAX_VALUE &&
                            dist[u] + grafo[u][v] < dist[v]) {
                        dist[v] = dist[u] + grafo[u][v];
                    }
                }
            }
            return dist;
        }

        private static int minDist(int[] dist, boolean[] visitados) {
            int min = Integer.MAX_VALUE, minIndex = -1;

            for (int v = 0; v < dist.length; v++) {
                if (!visitados[v] && dist[v] <= min) {
                    min = dist[v];
                    minIndex = v;
                }
            }
            return minIndex;
        }
    }

    private static class Vertex {
        private final int id;
        private double x, y;

        public Vertex(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
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

    public static class VertexDistance {
        private final SimpleIntegerProperty position;
        private final SimpleIntegerProperty vertex;
        private final SimpleIntegerProperty distance;

        public VertexDistance(int position, int vertex, int distance) {
            this.position = new SimpleIntegerProperty(position);
            this.vertex = new SimpleIntegerProperty(vertex);
            this.distance = new SimpleIntegerProperty(distance);
        }

        public int getPosition() {
            return position.get();
        }

        public SimpleIntegerProperty positionProperty() {
            return position;
        }

        public int getVertex() {
            return vertex.get();
        }

        public SimpleIntegerProperty vertexProperty() {
            return vertex;
        }

        public int getDistance() {
            return distance.get();
        }

        public SimpleIntegerProperty distanceProperty() {
            return distance;
        }
    }
}