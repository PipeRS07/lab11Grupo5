package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MSTKrusKalAndPrimController {

    @FXML
    private Text textInfo;

    @FXML
    private RadioButton AdjMatrix;

    @FXML
    private RadioButton AdjList;

    @FXML
    private RadioButton LinkedList;

    private ToggleGroup mstAlgorithmToggleGroup;

    private List<Vertex> vertices;
    private List<Edge> edges;

    private final int vertexRadius = 20;
    private final double centerX = 300; // Coordenada X del centro del círculo
    private final double centerY = 300; // Coordenada Y del centro del círculo
    private final double circleRadius = 200; // Radio del círculo
    @FXML
    private RadioButton KruskalRadioButton;
    @FXML
    private Pane dibujo1;
    @FXML
    private Text textInfo1;
    @FXML
    private RadioButton PrimRadioButton;
    @FXML
    private Pane dibujo11;

    public MSTKrusKalAndPrimController() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        mstAlgorithmToggleGroup = new ToggleGroup();
        KruskalRadioButton.setToggleGroup(mstAlgorithmToggleGroup);
        PrimRadioButton.setToggleGroup(mstAlgorithmToggleGroup);
    }

    @FXML
    public void randomizeOnAction(ActionEvent actionEvent) {
        dibujo1.getChildren().clear();
        vertices.clear();
        edges.clear();

        int totalVertices = 10; // Cambiar según tus necesidades
        generateRandomGraph(totalVertices);
    }

    private void generateRandomGraph(int totalVertices) {
        // Generar vértices aleatorios
        Random random = new Random();
        for (int i = 0; i < totalVertices; i++) {
            Vertex vertex = new Vertex(i);
            double angle = (2 * Math.PI * i) / totalVertices;
            double x = centerX + circleRadius * Math.cos(angle);
            double y = centerY + circleRadius * Math.sin(angle);
            vertex.setX(x);
            vertex.setY(y);
            vertices.add(vertex);
            drawVertex(vertex);
        }

        // Generar aristas aleatorias con pesos
        for (int i = 0; i < totalVertices; i++) {
            for (int j = i + 1; j < totalVertices; j++) {
                if (random.nextBoolean()) {
                    int weight = 200 + random.nextInt(801); // Peso entre 200 y 1000
                    Edge edge = new Edge(vertices.get(i), vertices.get(j), weight);
                    edges.add(edge);
                    drawEdge(edge);
                }
            }
        }
    }

    @Deprecated
    public void solveMST(ActionEvent actionEvent) {
        // Verificar qué algoritmo está seleccionado
        RadioButton selectedAlgorithm = (RadioButton) mstAlgorithmToggleGroup.getSelectedToggle();
        if (selectedAlgorithm != null) {
            if (selectedAlgorithm == KruskalRadioButton) {
                // Lógica para resolver MST usando Kruskal
                solveMSTKruskal();
            } else if (selectedAlgorithm == PrimRadioButton) {
                // Lógica para resolver MST usando Prim
                solveMSTPrim();
            }
        } else {
            textInfo.setText("Select an algorithm first.");
        }
    }

    private void solveMSTKruskal() {
        // Implementar algoritmo de Kruskal aquí
        // Puedes usar la lista de aristas 'edges'
        textInfo.setText("Kruskal algorithm executed.");
    }

    private void solveMSTPrim() {
        // Implementar algoritmo de Prim aquí
        // Puedes usar la lista de vértices 'vertices' y la lista de aristas 'edges'
        textInfo.setText("Prim algorithm executed.");
    }

    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);
        dibujo1.getChildren().add(circle);
    }

    private void drawEdge(Edge edge) {
        Line line = new Line(edge.getStart().getX(), edge.getStart().getY(),
                edge.getEnd().getX(), edge.getEnd().getY());
        line.setStroke(Color.GRAY);
        line.setStrokeWidth(2);
        dibujo1.getChildren().add(line);
    }

    @javafx.fxml.FXML
    public void radioB3OnAction(ActionEvent actionEvent) {
        this.AdjMatrix.setSelected(false);
        this.AdjList.setSelected(false);
        this.LinkedList.setSelected(true);
    }

    @javafx.fxml.FXML
    public void radioB1OnAction(ActionEvent actionEvent) {
        this.AdjMatrix.setSelected(true);
        this.AdjList.setSelected(false);
        this.LinkedList.setSelected(false);
    }

    @javafx.fxml.FXML
    public void radioB2OnAction(ActionEvent actionEvent) {
        this.AdjMatrix.setSelected(false);
        this.AdjList.setSelected(true);
        this.LinkedList.setSelected(false);
    }

    @FXML
    public void kruskalOnAction(ActionEvent actionEvent) {
        KruskalRadioButton.setSelected(true);
        PrimRadioButton.setSelected(false);
    }

    @FXML
    public void primOnAction(ActionEvent actionEvent) {
        KruskalRadioButton.setSelected(false);
        PrimRadioButton.setSelected(true);
    }


    class Vertex {
        private int id;
        private double x, y;

        public Vertex(int id) {
            this.id = id;
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

    class Edge {
        private Vertex start;
        private Vertex end;
        private int weight;

        public Edge(Vertex start, Vertex end, int weight) {
            this.start = start;
            this.end = end;
            this.weight = weight;
        }

        public Vertex getStart() {
            return start;
        }

        public Vertex getEnd() {
            return end;
        }

        public int getWeight() {
            return weight;
        }
    }
}