package domain;

import domain.list.ListException;
import domain.queue.QueueException;
import domain.stack.StackException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SinglyLinkedListGraphTest {

    @Test
    void testSinglyLinkedListGraph(){
        SinglyLinkedListGraph graph = new SinglyLinkedListGraph();
        try {
            graph.addVertex('a');
            graph.addVertex('b');
            graph.addVertex('c');
            graph.addVertex('d');
            graph.addVertex('e');
            graph.addVertex('f');
            graph.addVertex('g');
            graph.addVertex('h');
            graph.addVertex('i');
            graph.addVertex('j');

            graph.addEdgeWeight('a', 'b', "Maria");
            graph.addEdgeWeight('a', 'c', "Lucia");
            graph.addEdgeWeight('b', 'f', "Daniel");
            graph.addEdgeWeight('c', 'g', "Juan");
            graph.addEdgeWeight('d', 'h', "Jose");
            graph.addEdgeWeight('f', 'e', "Vero");
            graph.addEdgeWeight('f', 'j', "Nigel");
            graph.addEdgeWeight('g', 'j', "Felipe");
            graph.addEdgeWeight('h', 'j', "Juan");
            graph.addEdgeWeight('i', 'h', "Julia");
            System.out.println(graph.containsEdge('i', 'h'));
            System.out.println(graph.containsEdge('h', 'i'));


            System.out.println(graph);
            System.out.println("***************************************\n");
            System.out.println("Recorrido dfs()" + graph.dfs());
            System.out.println("Recorrido bfs()" + graph.bfs());
            System.out.println("\n*************************************\n");

            graph.removeEdge('b', 'f');
            graph.removeEdge('e', 'f');
            graph.removeEdge('j', 'f');
            graph.removeVertex('f');

            graph.removeEdge('h', 'j');
            graph.removeEdge('h', 'i');
            graph.removeEdge('h', 'd');
            graph.removeVertex('h');

            graph.removeEdge('j', 'g');
            graph.removeVertex('g');

            System.out.println("***************************************\n");
            System.out.println("Recorrido dfs()" + graph.dfs());
            System.out.println("Recorrido bfs()" + graph.bfs());
            System.out.println("\n*************************************\n");
            System.out.println(graph);




        } catch (GraphException e) {
            throw new RuntimeException(e);
        } catch (ListException e) {
            throw new RuntimeException(e);
        } catch (QueueException e) {
            throw new RuntimeException(e);
        } catch (StackException e) {
            throw new RuntimeException(e);
        }

    }
}