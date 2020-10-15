package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.*;

///Iterative deepening using A*/////////////
public class Main {
    public static Queue<int[][]> myqueue;
    public static int[][] goal_State;
    public static int count =0;
    public static int count1 =0;
    public static int initial_start_column =0;
    public static int initial_start_row =0;
    public static int start_column =0;
    public static int start_row =0;
    public static  int[][] arr;
    public static int goal_count=0;
    public static ArrayList<int[][]> duplicate;
    public static ArrayList<int[][]> visited;
    public static Set<int[][]> hs;
    public static String moves_sol="";
    public static int nodes = 0;
    public static Queue<int[][]> stk;
    public static int curr_node_cost= 0;
    public static int Total_cost= 0;
    public static int curr_goal_cost= 0;

    public static Map<int[][],Integer> total_cost;
    public static Map<int[][],String> map_moves;

    public static void main(String[] args) throws IOException {
        // did this project in two way
        //1-) create a tree with nodes of 2D-arrays (this is the commented part)
        //2-) Treated 2D arrays as nodes
        arr = new int[4][4];
        //taking input from user
        System.out.println("Input the range from 0-15 ");
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        String line = read.readLine();
        String breakPoint[] = line.split(" ");

        //  int[] arr1 = {1, 0, 2, 4, 5, 7, 3, 8, 9, 6,11, 12, 13, 10, 14, 15}; //hardcoded to test
        int[] goal_State1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0}; //goal state
        goal_State = new int[4][4];
        for (int i = 0; i < goal_State.length; i++) {
            for (int j = 0; j < goal_State.length; j++) {
                goal_State[i][j] = goal_State1[count];
                count++;
            }
        }

        System.out.println("");
        System.out.println("");
        System.out.println("GOAL STATE");
//converting goal state into 2D array
        for (int i = 0; i < goal_State.length; i++) {
            for (int j = 0; j < goal_State[i].length; j++) {
                System.out.print(goal_State[i][j] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arr[i][j] = Integer.parseInt(breakPoint[count1]);

                count1++;
            }
        }
        System.out.println();
        System.out.println();
        System.out.println("Initial State");
        printarray2D(arr);



        //printing time and memory used by the program
        final long startTime = System.nanoTime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        System.out.println("Which Heuristic you want to use to solve the puzzle" +
                " 1: Hamming" +

                " 2: Manhattan");
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        if(choice==1) {
            Hamming_misplaced_tiles(arr); //Heruistic 1
        }
        else{
            Manhattan_distance(arr); //Heruistic 2
        }
        final long endTime = System.nanoTime();

        long rtime = System.nanoTime();

        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed=afterUsedMem-beforeUsedMem;

        System.out.println("Nodes Expanded: "+ nodes);
        System.out.println("Moves: "+ moves_sol);
        System.out.println("Time: "+ (double)(endTime-startTime)/1000000000 + "  Seconds");
        System.out.println("Memory: "+ (double) actualMemUsed/1000+ "  KB");

    }


    public static void printarray2D(int[][]arr)
    {

        for(int i =0 ;i <arr.length;i++)
        {
            for(int j=0;j<arr[i].length;j++)
            {
                System.out.print(arr[i][j]+ " ");
            }
            System.out.println();
        }
    }



    public static int calculate_cost(int[][]startnode,int[][] curr)
    {
        int count = 0;
        //g(n)
        for(int i=0;i<startnode.length;i++)
        {
            for(int j=0; j < startnode[i].length;j++)
            {

                if (curr[i][j]!=0&&startnode[i][j] != curr[i][j]) {
                    count++;
                } else {
                    continue;

                }
            }

        }

        return count;

    }

    public static int calculate_goal_cost(int[][]goalstate,int[][] curr)
    {
//h(n)
        int count = 0;

        for(int i=0;i<goalstate.length;i++)
        {
            for(int j=0; j < goalstate[i].length;j++)
            {

                if (curr[i][j]!=0 && goalstate[i][j] != curr[i][j]) {
                    count++;
                } else {

                    continue;
                }
            }
        }
        return count;


    }

    //    missplaced heuristic implementation
//Heuristic 1 completed
//    1 2 3 4 5 6 8 0 9 11 7 12 13 10 14 15
//    hamming
    public static int[][] Hamming_misplaced_tiles(int[][] node) {
        int count =0;
        stk = new LinkedList<>();
        myqueue = new LinkedList<>();
        duplicate = new ArrayList<>();
        total_cost = new HashMap<>();
        map_moves = new HashMap<>();
        stk.add(node); //add the root node to queue
        int[][] start_node = node;
        while(!stk.isEmpty())
        {
            int[][] next_node = stk.poll();//get the top
            nodes++;
            duplicate.add(next_node); // keep the copy of  node
            if(count==0) {
                curr_node_cost = calculate_cost(start_node, next_node);  //g(n) this will calcualte the cost of curr_node from start_node
                Total_cost = curr_node_cost + calculate_goal_cost(goal_State, next_node); //calculate total cost h(n)

                System.out.println(curr_node_cost);
                System.out.println();
                System.out.println(Total_cost);
            }
//            total_cost.put(next_node,Total_cost); //store the total cost of the node

            for(int i =1 ; i <=4;i++)
            {
                count++;
                nodes++;
                Tree check = new Tree();
                int[][] result1 = check.Node_swap(next_node, i);
                String move = map_moves.get(next_node)+moves_sol;
                map_moves.put(result1,move);
                if (check != null) {
                    if (!check(duplicate, result1)) { //check for duplicates
                        System.out.println();
                        System.out.println();
                        System.out.println();
                        printarray2D(result1);


                        if(match(goal_State,result1,9))
                        {
                            System.out.println("FOUND SOLUTION WITH HAMMING");
                            move = move.replace("null","");
                            moves_sol=move;
                            //f(n) = g(n) + h(n)

                            return null;
                        }
                        curr_node_cost = calculate_cost(start_node,result1);  // g (n) is the cost of the path from the initial state to n

                        Total_cost = curr_node_cost+ calculate_goal_cost(goal_State,result1); //calculate total cost f(n) = g(n) + h(n)
                        total_cost.put(result1,Total_cost);
                        myqueue.add(result1);

                    }
                    else
                    {
                        curr_node_cost=0;
                        Total_cost=0;
                    }
                }
            }
            stk.add(check_lowest_2(myqueue)); //add the lowest path to second queue and neglect all others

//1 3 4 8 5 2 0 6 9 10 7 11 13 14 15 12
        }
        return null;
        //using stack cause its lifo help us in traversing

    }

    public static int[][] Manhattan_distance(int[][] node) {
        int count =0;
        stk = new LinkedList<>();
        myqueue = new LinkedList<>();
        duplicate = new ArrayList<>();
        total_cost = new HashMap<>();
        map_moves = new HashMap<>();
        stk.add(node); //add the root node to queue
        int[][] start_node = node;
        while(!stk.isEmpty())
        {
            int[][] next_node = stk.poll();//get the top
            nodes++;
            duplicate.add(next_node); // keep the copy of  node
            if(count==0) {
                curr_node_cost = Manhattan_distance_start(start_node, next_node);  //g(n) this will calcualte the cost of curr_node from start_node
                Total_cost = curr_node_cost + Manhattan_distance(goal_State, next_node); //calculate total cost f(n) =  g(n)+h(n)
                //if total cost is zero then startstate is goal state
            }
//            total_cost.put(next_node,Total_cost); //store the total cost of the node
//1 3 4 8 5 2 0 6 9 10 7 11 13 14 15 12
            for(int i =1 ; i <=4;i++)
            {
                count++;
                nodes++;
                Tree check = new Tree();
                int[][] result1 = check.Node_swap(next_node, i);
                String move = map_moves.get(next_node)+moves_sol;
                map_moves.put(result1,move);
                if (check != null) {
                    if (!check(duplicate, result1)) { //check for duplicates
                        System.out.println();
                        System.out.println();
                        System.out.println();
                        printarray2D(result1);


                        if(match(goal_State,result1,9))
                        {
                            System.out.println("FOUND SOLUTION WITH Manhattan");
                            move = move.replace("null","");
                            moves_sol=move;
                            return null;
                        }
                        curr_node_cost = Manhattan_distance_start(start_node,result1);  //g(n) this will calcualte the cost of curr_node from start_node
                        Total_cost = curr_node_cost+Manhattan_distance(goal_State,result1); //calculate total cost h(n)
                        total_cost.put(result1,Total_cost);
                        myqueue.add(result1);
                        ;

                    }
                    else
                    {
                        curr_node_cost=0;
                        Total_cost=0;
                    }
                }
            }
            stk.add(check_lowest_1(myqueue)); //add the lowest path to second queue and neglect all others


        }
        return null;
        //using stack cause its lifo help us in traversing

    }
    public static int[][] check_lowest_1(Queue<int[][]>my_queue) //java the reference "GOD"
    {
        int[][] lowest = null;
        int[][] cost1_node = null;
        int[][] cost2_node = null;

        for (int i = 0; i <= my_queue.size(); i++) {

            if(my_queue.size()==1)
            {
                lowest =my_queue.poll();
            }
            cost1_node = my_queue.poll();
            cost2_node = my_queue.poll();
            if (cost1_node != null && cost2_node != null) //means nothings to poll in queue{
            {
                int cost1 = total_cost.get(cost1_node);

                int cost2 = total_cost.get(cost2_node);
                if (cost1 > cost2) {
                    //;
                    lowest = cost2_node;
                    my_queue.add(lowest); //keep it in queue so that you can compare with others
                }

                if (cost1 < cost2) {
                    //;
                    lowest = cost1_node;
                    my_queue.add(lowest); //keep it in queue so that you can compare with others
                }


                if (cost1 == cost2) {
                    lowest = cost2_node;
                    my_queue.add(cost2_node);
                }

            }
        }
        my_queue.clear();
        return lowest;

    }

    public static int[][] check_lowest_2(Queue<int[][]>my_queue) //java the reference "GOD"
    {
        int[][] lowest = null;
        int[][] cost1_node = null;
        int[][] cost2_node = null;

        for (int i = 0; i <= my_queue.size(); i++) {

            if(my_queue.size()==1)
            {
                lowest =my_queue.poll();
            }
            cost1_node = my_queue.poll();
            cost2_node = my_queue.poll();
            if (cost1_node != null && cost2_node != null) //means nothings to poll in queue{
            {
                int cost1 = total_cost.get(cost1_node);

                int cost2 = total_cost.get(cost2_node);
                if (cost1 > cost2) {
                    //;
                    lowest = cost2_node;
                    my_queue.add(lowest); //keep it in queue so that you can compare with others
                }

                if (cost1 < cost2) {
                    //;
                    lowest = cost1_node;
                    my_queue.add(lowest); //keep it in queue so that you can compare with others
                }


                if (cost1 == cost2) {
                    lowest = cost1_node;
                    my_queue.add(lowest);
                }

            }
        }
        my_queue.clear();
        return lowest;

    }

    public static int[] coordinates(int val,int[][]start_State) {
        int arr[] = new int[2];
        for (int i = 0; i < start_State.length; i++) {
            for (int j = 0; j < start_State[i].length; j++) {
                if(start_State[i][j]==val)
                {
                    arr[0]=i;
                    arr[1]=j;
                    return arr;
                }
            }
        }
        return arr;
    }
    public static int Manhattan_distance_start(int[][]start_State,int[][]curr)
    {
        double a,b,c,d;
        int val;
        int[] arr = new int[2];
        int distance = 0;
        for(int i=0;i<start_State.length;i++)
        {
            for (int j=0;j<start_State[i].length;j++)
            {
                val = curr[i][j]; //got the coordinates
                if(val!=0) //we do not have to count blank space
                {
                    arr = coordinates(val,start_State);
                    //use the same formula however, subtract the coordinates of goal and current state to get the correct distance
                    distance += Math.abs(i-arr[0]) + Math.abs(j-arr[1]);


                }
            }



        }

        return distance;
    }


    public static int Manhattan_distance(int[][]goal_State,int[][]curr)
    {
        double a,b,c,d;
        int val;
        int[] arr = new int[2];
        int distance = 0;
        for(int i=0;i<goal_State.length;i++)
        {
            for (int j=0;j<goal_State[i].length;j++)
            {
                val = curr[i][j]; //got the coordinates
                if(val!=0) //we do not have to count blank space
                {
                    arr = coordinates(val,goal_State);
                    //use the same formula however, subtract the coordinates of goal and current state to get the correct distance
                    distance += Math.abs(i-arr[0]) + Math.abs(j-arr[1]);


                }
            }


        }

        return distance;
    }
    public static boolean match(int[][]goal_State,int[][] child_node,int limit)
    {
        if(limit<0)
        {
            return false;
        }

        if (Arrays.deepEquals(goal_State, child_node)) {
            return true;

        }
        return false;
    }

    public static boolean check(ArrayList<int[][]> arr1, int[][] arr2)
    {
        for(int i =0;i<arr1.size();i++){
            if(Arrays.deepEquals(arr1.get(i),arr2))
            {
                return true;
            }
        }
        return false;
    }


    public static int[][] create_copy(int[][] arr,int[][] copy_array)
    {
        for (int i = 0; i < arr.length; ++i) {
            for (int j = 0; j < arr[i].length; ++j) {
                copy_array[i][j] = arr[i][j];
            }
        }
        return copy_array;
    }
    public static class Tree {


        Tree root;
        int arr1[][];
        Tree left;
        Tree right;
        Tree Up;
        Tree Down;
        int weight;

        Tree() {

        }

        public Tree update_root()
        {
            return root;
        }
        Tree(int value[][]) {
            this.arr1 = value;

        }
        public void set_root(Tree root)
        {
            this.root = root;

        }

        public boolean is_safe(int x, int y)
        {
            if(x <4 && y < 4 && x>=0 && y>=0)
            {
                return true;
            }
            else
                return false;
        }
        public void find_blank(int[][]arr2)
        {
            for (int i = 0; i < arr2.length; ++i) {
                for (int j = 0; j < arr2[i].length; ++j) {
                    if(arr2[i][j]==0)
                    {
                        start_row=i;
                        start_column = j;
                    }
                }
            }

        }


        public int[][] Node_swap( int[][]arr2,int which_move) {
            Tree node1;
            int[][]  copy_arr=new int[4][4];


            if(which_move==0)
            {
                find_blank(arr2);
                int[][] new_copy = create_copy(arr2,copy_arr);
                if (arr2[start_row][start_column] == 0) //means we are on correct position
                {//make moves now
                    if (is_safe(start_column, start_row)) {
                        if (new_copy[start_row][start_column] != 0) //[column-1] move to left if its not 0(empty) /
                        {
                            return new_copy;
                        }
                    }
                }

            }

            if(which_move==1) { //make the left move
                find_blank(arr2);
                int[][] new_copy = create_copy(arr2,copy_arr);
                if (arr2[start_row][start_column] == 0) //means we are on correct position
                {//make moves now
                    if (is_safe(start_column - 1, start_row)) {
                        if (new_copy[start_row][start_column - 1] != 0) //[column-1] move to left if its not 0(empty) /
                        {
                            int temp = new_copy[start_row][start_column - 1];
                            new_copy[start_row][start_column - 1] = new_copy[start_row][start_column];
                            new_copy[start_row][start_column] = temp;

                            moves_sol=" L ";
                            return new_copy;
                        }
                    }
                }
            }

            if(which_move==2) { //make the down move
                find_blank(arr2);
                int[][] new_copy = create_copy(arr2,copy_arr);
                if (arr2[start_row][start_column] == 0) //means we are on correct position
                {//make moves now
                    if (is_safe(start_column, start_row + 1)) {
                        if (new_copy[start_row + 1][start_column] != 0) //[column-1] move to down if its not 0(empty) /
                        {
                            int temp = new_copy[start_row + 1][start_column];
                            new_copy[start_row + 1][start_column] = new_copy[start_row][start_column];
                            new_copy[start_row][start_column] = temp;

                            moves_sol=" D ";
                            return new_copy;
                        }
                    }
                }
            }

            if(which_move==3) { //make the up move
                find_blank(arr2);
                int[][] new_copy = create_copy(arr2,copy_arr);
                if (arr2[start_row][start_column] == 0) //means we are on correct position
                {//make moves now
                    if (is_safe(start_column, start_row - 1)) {
                        if (new_copy[start_row - 1][start_column] != 0) //[column-1] move to up if its not 0(empty) /
                        {

                            int temp = new_copy[start_row - 1][start_column];
                            new_copy[start_row - 1][start_column] = new_copy[start_row][start_column];
                            new_copy[start_row][start_column] = temp;
                            moves_sol=" U ";
                            return new_copy;

                        }
                    }
                }
            }


            if(which_move==4) { //make the right move
                find_blank(arr2);
                int[][] new_copy = create_copy(arr2,copy_arr);
                if (new_copy[start_row][start_column] == 0) //means we are on correct position
                {//make moves now
                    if (is_safe(start_column+1, start_row)) {

                        if (new_copy[start_row][start_column + 1] != 0) //[column-1] move to right if its not 0(empty) /
                        {
                            int temp = new_copy[start_row][start_column + 1];
                            new_copy[start_row][start_column + 1] = new_copy[start_row][start_column];
                            new_copy[start_row][start_column] = temp;
                            //  node1 = add_recursive(node, new_copy, which_move);
                            //matched(goal_State,node1.arr1);
                            moves_sol=" R " ;
                            return new_copy;

                        }
                    }
                }
            }
            return copy_arr;
        }


        public Tree add_recursive(Tree curr_node, int[][] data,int i) {


            return curr_node;
        }

        public Tree add(Tree node,int[][] data,int i)
        {

            return root;
        }
    }


}
