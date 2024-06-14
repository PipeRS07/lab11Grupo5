package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MSTKrusKalAndPrimController {

    @FXML
    private Text textInfo;
    @FXML
    private Pane dibujo1;
    @FXML
    private RadioButton AdjMatrix;
    @FXML
    private RadioButton AdjList;
    @FXML
    private Pane dibujo;
    @FXML
    private Text textInfo1;
    @FXML
    private RadioButton LinkedList;
    @FXML
    private RadioButton Kruskal;
    @FXML
    private RadioButton Prim;

    private Graph graph;
    private List<Edge> mstEdges;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private double centerX;
    private double centerY;
    private double circleRadius;

    @FXML
    public void initialize() {
        vertices = new ArrayList<>();
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        dibujo.getChildren().clear();
        dibujo1.getChildren().clear();
        vertices.clear();

        // Configurar las coordenadas del centro y el radio del círculo basado en el tamaño del Pane
        centerX = dibujo.getWidth() / 2;
        centerY = dibujo.getHeight() / 2;
        circleRadius = Math.min(centerX, centerY) - vertexRadius - 10; // Dejar un pequeño margen

        Random random = new Random();
        int numVertices = 10; // Número de vértices
        graph = generateRandomGraph(numVertices, random);

        double angleStep = 2 * Math.PI / numVertices;

        // Calcular posiciones de los vértices en el círculo
        for (int i = 0; i < numVertices; i++) {
            Vertex vertex = vertices.get(i);

            // Calcular posición en el círculo
            double angle = i * angleStep;
            double x = centerX + circleRadius * Math.cos(angle);
            double y = centerY + circleRadius * Math.sin(angle);

            vertex.setX(x);
            vertex.setY(y);

            drawVertex(vertex, dibujo);
        }

        // Dibujar aristas del grafo original
        for (Edge edge : graph.getEdges()) {
            drawEdge(vertices.get(edge.getSource()), vertices.get(edge.getDestination()), edge.getWeight(), dibujo);
        }

        // Ejecutar el algoritmo seleccionado y obtener el MST
        if (Kruskal.isSelected()) {
            mstEdges = MSTAlgorithms.kruskalMST(graph);
        } else if (Prim.isSelected()) {
            mstEdges = MSTAlgorithms.primMST(graph);
        }

        // Dibujar el MST
        for (Edge edge : mstEdges) {
            drawEdge(vertices.get(edge.getSource()), vertices.get(edge.getDestination()), edge.getWeight(), dibujo1);
        }
    }

    private Graph generateRandomGraph(int numVertices, Random random) {
        Set<Integer> vertexIds = new HashSet<>();
        while (vertexIds.size() < numVertices) {
            vertexIds.add(random.nextInt(100));
        }

        vertices = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        for (int id : vertexIds) {
            vertices.add(new Vertex(id));
        }

        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (random.nextBoolean()) {
                    int weight = 200 + random.nextInt(801);
                    edges.add(new Edge(i, j, weight));
                    edges.add(new Edge(j, i, weight)); // Asegurar que sea bidireccional
                }
            }
        }

        return new Graph(numVertices, edges);
    }

    private void drawVertex(Vertex vertex, Pane pane) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text text = new Text(vertex.getX() - vertexRadius / 2, vertex.getY() + vertexRadius / 2, String.valueOf(vertex.getId()));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        pane.getChildren().addAll(circle, text);
    }

    private void drawEdge(Vertex v1, Vertex v2, int weight, Pane pane) {
        Line line = new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);

        Text weightText = new Text((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, String.valueOf(weight));
        weightText.setBoundsType(TextBoundsType.VISUAL);
        weightText.setFill(Color.RED);

        pane.getChildren().addAll(line, weightText);
    }

    @FXML
    public void KruskalOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void AdjencyListOnActon(ActionEvent actionEvent) {
    }

    @FXML
    public void PrimOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void LinkedListOnAction(ActionEvent actionEvent) {
    }

    @FXML
    public void AdjencyMatrixOnAction(ActionEvent actionEvent) {
    }

    private static class Graph {
        private final int numVertices;
        private final List<Edge> edges;

        public Graph(int numVertices, List<Edge> edges) {
            this.numVertices = numVertices;
            this.edges = edges;
        }

        public int getNumVertices() {
            return numVertices;
        }

        public List<Edge> getEdges() {
            return edges;
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

    private static class Edge implements Comparable<Edge> {
        private final int source;
        private final int destination;
        private final int weight;

        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        public int getSource() {
            return source;
        }

        public int getDestination() {
            return destination;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    private static class MSTAlgorithms {
        public static List<Edge> kruskalMST(Graph graph) {
            int numVertices = graph.getNumVertices();
            List<Edge> edges = new ArrayList<>(graph.getEdges());
            Collections.sort(edges);

            int[] parent = new int[numVertices];
            for (int i = 0; i < numVertices; i++) {
                parent[i] = i;
            }

            List<Edge> mst = new ArrayList<>();
            for (Edge edge : edges) {
                int root1 = find(parent, edge.getSource());
                int root2 = find(parent, edge.getDestination());

                if (root1 != root2) {
                    mst.add(edge);
                    union(parent, root1, root2);
                }
            }
            return mst;
        }

        private static int find(int[] parent, int vertex) {
            if (parent[vertex] != vertex) {
                parent[vertex] = find(parent, parent[vertex]);
            }
            return parent[vertex];
        }

        private static void union(int[] parent, int root1, int root2) {
            parent[root1] = root2;
        }

        public static List<Edge> primMST(Graph graph) {
            int numVertices = graph.getNumVertices();
            boolean[] inMST = new boolean[numVertices];
            int[] key = new int[numVertices];
            int[] parent = new int[numVertices];

            for (int i = 0; i < numVertices; i++) {
                key[i] = Integer.MAX_VALUE;
                parent[i] = -1;
            }

            key[0] = 0;
            for (int count = 0; count < numVertices - 1; count++) {
                int u = minKey(key, inMST, numVertices);
                inMST[u] = true;

                for (Edge edge : graph.getEdges()) {
                    if (edge.getSource() == u) {
                        int v = edge.getDestination();
                        if (!inMST[v] && edge.getWeight() < key[v]) {
                            key[v] = edge.getWeight();
                            parent[v] = u;
                        }
                    }
                }
            }

            List<Edge> mst = new ArrayList<>();
            for (int i = 1; i < numVertices; i++) {
                mst.add(new Edge(parent[i], i, key[i]));
            }
            return mst;
        }

        private static int minKey(int[] key, boolean[] inMST, int numVertices) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;

            for (int v = 0; v < numVertices; v++) {
                if (!inMST[v] && key[v] < min) {
                    min = key[v];
                    minIndex = v;
                }
            }
            return minIndex;
        }
    }
}


