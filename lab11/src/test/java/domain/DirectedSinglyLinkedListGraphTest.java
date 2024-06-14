package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;
import util.Utility;

import static org.junit.jupiter.api.Assertions.*;

class DirectedSinglyLinkedListGraphTest {

    @Test
    void test() {
        DirectedSinglyLinkedListGraph graph = new DirectedSinglyLinkedListGraph();
        try {
            graph.addVertex(1);
            graph.addVertex(2);
            graph.addVertex(3);
            graph.addVertex(4);
            graph.addVertex(5);

            graph.addEdgeWeight(1, 1, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(2, 2, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(3, 3, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(4, 4, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(5, 5, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(1, 2, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(1, 3, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(1, 4, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(1, 5, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(2, 4, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(2, 1, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(3, 4, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(4, 2, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(4, 3, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(4, 5, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(5, 1, Utility.getRandom(500, 1000));
            graph.addEdgeWeight(5, 4, Utility.getRandom(500, 1000));

            System.out.println(graph);
            System.out.println("***************************************\n");
            System.out.println("Recorrido dfs()"+graph.dfs());
            System.out.println("Recorrido bfs()"+graph.bfs());
            System.out.println("\n*************************************\n");


            graph.removeVertexEdge(2);
            graph.removeVertexEdge(4);
            graph.removeVertexEdge(5);

            System.out.println("\n*************************************\n");
            System.out.println(graph);

        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        }


    }



}