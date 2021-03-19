package simulacija;

import utils.OrderedPair;
import java.util.ArrayList;
import static GUI.MainScreen.SistemZaNadzorController.getVelicinaGrada;

public class Graph
{
//    static int n = getVelicinaGrada() * getVelicinaGrada();
//    private static int graph[][] = new int[n][n];
//    public static ArrayList<ArrayList<Integer>> adjList = new ArrayList<>(n);
//    public static boolean created = false;
//
//    public static int[][] getGraph()
//    {
//        if (!created)
//            createGraph();
//
//        return graph;
//    }
//
//    private static void createGraph()
//    {
//        for (int i = 0; i < n; i++)
//        {
//            int lokacija;
//            for (int j = 0; j < n; j++)
//            {
//                adjList.add(i, new ArrayList<>());
//                if (Math.abs(i - j) <= 1 || (i == j + getVelicinaGrada() || i == j - getVelicinaGrada()))
//                {
//                    graph[i][j] = 1;
//
//                    if (i % getVelicinaGrada() == getVelicinaGrada() - 1 && i == j - 1)
//                        graph[i][j] = 0;
//
//                    else if (i % getVelicinaGrada() == 0 && i == j + 1)
//                        graph[i][j] = 0;
//                }
//            }
//
//            for (OrderedPair p : Simulacija.kreirajSimulaciju().getLokacijeObjekata())
//            {
//                lokacija = (p.getY() - 1) * getVelicinaGrada() + (p.getX() - 1);
//                graph[i][lokacija] = 0;
//            }
//        }
//
//        for (int i = 0; i < n; i++)
//        {
//            for (int j = 0; j < n; j++)
//                if (graph[i][j] == 1)
//                    adjList.get(i).add(j);
//        }
//        created = true;
//    }
//
//    public static void ispisGraf()
//    {
//        getGraph();
//
//        int n = getVelicinaGrada() * getVelicinaGrada();
//        for (int i = 0; i < n; i++)
//        {
//            System.out.print(i + 1 + ": ");
//            for (int j = 0; j < n; j++)
//                if (graph[i][j] == 1)
//                    System.out.print(j + 1 + " ");
//                //System.out.print(graph[i][j] + " ");
//
//            System.out.print("\n");
//        }
//    }
//
//    public static ArrayList<ArrayList<Integer>> getAdjList()
//    {
//        getGraph();
//        return adjList;
//    }
//
//    public static void ispisAdjList()
//    {
//        for (int i = 0; i < n; i++)
//        {
//            System.out.print(i + 1 + ": ");
//            for (int j = 0; j < adjList.get(i).size(); j++)
//                System.out.print(adjList.get(i).get(j) + " ");
//
//            System.out.print("\n");
//        }
//    }
}