package controller;

import domain.DirectedSinglyLinkedListGraph;
import domain.GraphException;
import domain.list.ListException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class directedGraphOperationsController {

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Pane dibujo;

    private DirectedSinglyLinkedListGraph graph;
    private List<Vertex> vertices;
    private final int vertexRadius = 20;
    private final double centerX = 200;
    private final double centerY = 180;
    private final double circleRadius = 200;
    @FXML
    private Text textInfo;

    @FXML
    public void initialize() {
        graph = new DirectedSinglyLinkedListGraph();
        vertices = new ArrayList<>();
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
                directedGraphOperationsController.Vertex vertex = new directedGraphOperationsController.Vertex(monumentos[i]);

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



    private void drawLoop(Vertex vertex) {
        Circle loopCircle = new Circle(vertex.getX(), vertex.getY(), vertexRadius * 0.6, Color.LIGHTGRAY); // Radio más pequeño para el círculo del bucle
        loopCircle.setStroke(Color.BLACK);
        loopCircle.setStrokeWidth(2);

        Text loopText = new Text(vertex.getX() - 10, vertex.getY() + 20, "Loop at " + vertex.getData());
        loopText.setFill(Color.BLACK);

        loopCircle.setOnMouseEntered(e -> {
            loopCircle.setFill(Color.CRIMSON); // Cambiar color del círculo al pasar el ratón
            textInfo.setText("Loop at vertex: " + vertex.getData()); // Mostrar información en textInfo
        });

        loopCircle.setOnMouseExited(e -> {
            loopCircle.setFill(Color.LIGHTGRAY); // Restaurar color original del círculo al salir el ratón
            textInfo.setText(""); // Limpiar textInfo
        });

        Group loopGroup = new Group(loopCircle, loopText);
        dibujo.getChildren().add(loopGroup);
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
            QuadCurve curve = new QuadCurve(centerX, centerY, controlX1, controlY1, centerX, centerY);
            curve.setStroke(Color.BLACK);
            curve.setStrokeWidth(1);

            Text weightText = new Text(centerX - 10, centerY + 20, "Loop: " + v1.getData() + ", Weight: " + weight);
            weightText.setBoundsType(TextBoundsType.VISUAL);
            weightText.setFill(Color.RED);

            curve.setOnMouseEntered(e -> {
                curve.setStroke(Color.CRIMSON); // Cambiar color de la línea al pasar el ratón
                textInfo.setText("Loop between vertex: " + v1.getData() + ", Weight: " + weight); // Mostrar información en textInfo
            });

            curve.setOnMouseExited(e -> {
                curve.setStroke(Color.BLACK); // Restaurar color original de la línea al salir el ratón
                textInfo.setText(""); // Limpiar textInfo
            });

            Group loopGroup = new Group(curve);
            dibujo.getChildren().add(loopGroup);

        } else {
            // Si no es un bucle, dibujamos la línea normalmente como en el segundo método drawEdge

            Line line = new Line(v1.getX(), v1.getY(), v2.getX(), v2.getY());
            line.setStroke(Color.BLACK);
            line.setStrokeWidth(1);

            // Flecha
            double angle = Math.atan2(v2.getY() - v1.getY(), v2.getX() - v1.getX());
            double arrowLength = 10;
            double arrowAngle = Math.toRadians(30);

            Line arrow1 = new Line();
            arrow1.setStartX(line.getEndX());
            arrow1.setStartY(line.getEndY());
            arrow1.setEndX(line.getEndX() - arrowLength * Math.cos(angle - arrowAngle));
            arrow1.setEndY(line.getEndY() - arrowLength * Math.sin(angle - arrowAngle));

            Line arrow2 = new Line();
            arrow2.setStartX(line.getEndX());
            arrow2.setStartY(line.getEndY());
            arrow2.setEndX(line.getEndX() - arrowLength * Math.cos(angle + arrowAngle));
            arrow2.setEndY(line.getEndY() - arrowLength * Math.sin(angle + arrowAngle));

            Text weightText = new Text((v1.getX() + v2.getX()) / 2, (v1.getY() + v2.getY()) / 2, String.valueOf(weight));
            weightText.setBoundsType(TextBoundsType.VISUAL);
            weightText.setFill(Color.RED);

            Group edgeGroup = new Group(line, arrow1, arrow2);

            // Evento para cambiar el color al pasar el cursor sobre la arista
            edgeGroup.setOnMouseEntered(event -> {
                line.setStroke(Color.GREEN); // Cambia el color al pasar el cursor
                arrow1.setStroke(Color.GREEN);
                arrow2.setStroke(Color.GREEN);
                textInfo.setText("Edge between vertices: " + v1.getData() + " - " + v2.getData() + ", Weight: " + weight); // Mostrar información en textInfo
            });

            // Evento para volver al color original al salir el cursor de la arista
            edgeGroup.setOnMouseExited(event -> {
                line.setStroke(Color.BLACK); // Vuelve al color original
                arrow1.setStroke(Color.BLACK);
                arrow2.setStroke(Color.BLACK);
                textInfo.setText(""); // Limpiar el texto cuando se sale del borde
            });

            dibujo.getChildren().add(edgeGroup);
        }
    }


    private void drawVertex(Vertex vertex) {
        Circle circle = new Circle(vertex.getX(), vertex.getY(), vertexRadius, Color.LIGHTBLUE);
        circle.setStroke(Color.DARKBLUE);
        circle.setStrokeWidth(2);

        Text text = new Text(vertex.getX() - vertexRadius / 2, vertex.getY() + vertexRadius / 2, vertex.getData());
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setFill(Color.BLACK);

        Group vertexGroup = new Group(circle, text);

        // Evento para cambiar de color al pasar el cursor sobre el vértice
        vertexGroup.setOnMouseEntered(event -> {
            circle.setFill(Color.BLUEVIOLET); // Cambia el color al pasar el cursor
        });

        // Evento para volver al color original al salir el cursor del vértice
        vertexGroup.setOnMouseExited(event -> {
            circle.setFill(Color.LIGHTBLUE); // Vuelve al color original
        });

        dibujo.getChildren().add(vertexGroup);
    }

    // Redibujar el grafo completo
    private void redrawGraph() {
        dibujo.getChildren().clear(); // Limpiar el panel de dibujo

        for (Vertex vertex : vertices) {
            drawVertex(vertex);
        }

        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                try {
                    if (graph.containsEdge(vertices.get(i).getData(), vertices.get(j).getData())) {
                        // Suponiendo que el peso de la arista es 0 si no se almacena explícitamente
                        drawEdge(vertices.get(i), vertices.get(j), 0); // Cambiar el 0 por el peso real
                    }
                } catch (GraphException | ListException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Actualizar información del grafo en el área de texto
    private void updateGraphInfo() {
        String graphInfo = graph.toString();
        infoTextArea.setText(graphInfo);
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
    public void addVertexOnAction(ActionEvent actionEvent) {
        String[] names = {"Moai de Isla de Pascua", "Pirámides de Teotihuacán",
                "Alhambra de Granada", "Partenón de Atenas", "Torre de Londres", "Castillo de Neuschwanstein",
                "Kremlin de Moscú", "Puerta de Brandeburgo", "Templo de Borobudur", "Castillo de Edimburgo",
                "Casa de la Ópera de Sídney", "Monte Rushmore", "Monte Saint-Michel", "Puerta de Alcalá",
                "Basílica de San Pedro", "Palacio de Versalles", "Mezquita Sheikh Zayed",};
        if (vertices.size() < names.length) {
            String newName = names[vertices.size()];
            try {
                graph.addVertex(newName);

                directedGraphOperationsController.Vertex vertex = new directedGraphOperationsController.Vertex(newName);

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
    public void removeVertexOnAction(ActionEvent actionEvent) {
        if (vertices.isEmpty()) {
            return;
        }

        // Seleccionar un vértice para eliminar (por ejemplo, el último vértice añadido)
        directedGraphOperationsController.Vertex vertexToRemove = vertices.get(vertices.size() - 1);
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

        directedGraphOperationsController.Vertex v1 = vertices.get(i);
        directedGraphOperationsController.Vertex v2 = vertices.get(j);

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

    @FXML
    public void clearOnAAction(ActionEvent actionEvent) {
        dibujo.getChildren().clear();
        vertices.clear();
        graph.clear();

        // Actualizar infoTextArea
        updateGraphInfo();
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