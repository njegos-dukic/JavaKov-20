package simulacija;

import java.util.ArrayList;

public class Dijkstra
{
//    private static final int NO_PARENT = -1;
//
//    public static ArrayList<Integer> najkracaPutanja(int startVertex, int destVertex)
//    {
//        ArrayList<Integer> najkraciPut = new ArrayList<>();
//        int[][] adjacencyMatrix = Graph.getGraph();
//
//        if (dijkstra(adjacencyMatrix, startVertex, destVertex, najkraciPut) == -1)
//            return null;
//
//        return najkraciPut;
//    }
//
//    private static int dijkstra(int[][] adjacencyMatrix, int startVertex, int destVertex, ArrayList<Integer> putanja)
//    {
//        startVertex -= 1;
//        destVertex -= 1;
//        int nVertices = adjacencyMatrix[0].length;
//        int[] shortestDistances = new int[nVertices];
//        boolean[] added = new boolean[nVertices];
//
//        for (int vertexIndex1 = 0; vertexIndex1 < nVertices; vertexIndex1++)
//        {
//            shortestDistances[vertexIndex1] = Integer.MAX_VALUE;
//            added[vertexIndex1] = false;
//        }
//
//        shortestDistances[startVertex] = 0;
//
//        int[] parents = new int[nVertices];
//
//        parents[startVertex] = NO_PARENT;
//
//        for (int i = 1; i < nVertices; i++)
//        {
//            int nearestVertex = -1;
//            int shortestDistance = Integer.MAX_VALUE;
//            for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
//                if (!added[vertexIndex] && shortestDistances[vertexIndex] < shortestDistance)
//                {
//                    nearestVertex = vertexIndex;
//                    shortestDistance = shortestDistances[vertexIndex];
//                }
//
//            if (nearestVertex != -1)
//            {
//                added[nearestVertex] = true;
//
//                for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++)
//                {
//                    int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];
//                    if (edgeDistance > 0 && ((shortestDistance + edgeDistance) < shortestDistances[vertexIndex]))
//                    {
//                        parents[vertexIndex] = nearestVertex;
//                        shortestDistances[vertexIndex] = shortestDistance + edgeDistance;
//                    }
//                }
//            }
//        }
//
//        // System.out.print("\n" + (startVertex + 1) + " -> " + (destVertex + 1) + ": ");
//
//        if (shortestDistances[destVertex] == Integer.MAX_VALUE)
//        {
//            System.out.print("Nedostizno.");
//            return -1;
//        }
//
//        createPutanja(destVertex, parents, putanja);
//
//        if(!putanja.contains(destVertex))
//            putanja.add(destVertex);
//
//        return 0;
//    }
//
//    private static void createPutanja(int currentVertex, int[] parents, ArrayList<Integer> putanja)
//    {
//        if (currentVertex == NO_PARENT)
//            return;
//
//        createPutanja(parents[currentVertex], parents, putanja);
//        putanja.add(currentVertex + 1);
//        // System.out.print((currentVertex + 1) + " ");
//    }
}