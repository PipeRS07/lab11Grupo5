package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SinglyLinkedListGraphTest {

    @Test
    void test() {
        SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
        try {
            graph.addVertex('A');
            graph.addVertex('B');
            graph.addVertex('C');
            graph.addVertex('D');
            graph.addVertex('E');
            graph.addVertex('F');
            graph.addVertex('G');
            graph.addVertex('H');

            graph.addEdgeWeight('A', 'B', 20);
            graph.addEdgeWeight('A', 'E', 15);
            graph.addEdgeWeight('B', 'C', 10);
            graph.addEdgeWeight('E', 'F', 30);
            graph.addEdgeWeight('C', 'D', 40);
            graph.addEdgeWeight('F', 'G', 5);
            graph.addEdgeWeight('G', 'H', 7);

            //lanza una excepción
            //graph.addEdgeWeight('G', 'K', 7);

            System.out.println(graph.toString());

            //Busqueda en Profundidad
            System.out.println("\nRECORRIDO CON EL ALGORITMO DFS (DEPTH FIRST SEARCH): "
                    +"\n"+graph.dfs());

            //Busqueda en Amplitud
            System.out.println("\nRECORRIDO CON EL ALGORITMO BFS (BREADTH FIRST SEARCH): "
                    +"\n"+graph.bfs());

        } catch (GraphException | ListException | StackException | QueueException e) {
            throw new RuntimeException(e);
        }
    }
}