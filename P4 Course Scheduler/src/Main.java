import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Main {
    public static void main(String [] args){
        try {
            // simply reading in the file
            FileInputStream file = new FileInputStream(args[0]);
            Scanner inp = new Scanner(file);
            // takes in the amount of classes
            int totalCourses = Integer.parseInt(inp.next());
            // makes our graph with the correct size
            Graph graph = new Graph<>(totalCourses);
            // populates our graph with nodes located in the file
            for (int i =0; i< totalCourses; i++) {
                graph.setValue(i, inp.next());
                inp.nextLine();
            }
            inp.close();
            // closing and reopening the file to
            // access the information once again
            FileInputStream fileAgain = new FileInputStream(args[0]);
            Scanner in = new Scanner(fileAgain);
            in.nextLine();
            for (int i =0; i<totalCourses; i++) {
                // takes in the course name as a string for the
                // isValid function to make sure it consists of
                // 4 letters and then 3 numbers
                String courseName = (String) graph.getValue(i);
                if (courseName.equals(in.next()) && isValid(courseName)) {
                    // prereqNum is the number of edges that connect to the node
                    int prereqNum = Integer.parseInt(in.next());
                    for (int j = 0; j < prereqNum; j++) {
                        String prereq = in.next();
                        int prerecLocation = graph.lookup(prereq);
                        // our first time through we made a bunch of heads and
                        // here we just match heads to prereqs
                        if (prerecLocation != -1) {
                            graph.insertEdge(prerecLocation, i);
                        } else
                            System.out.println("Your input is not valid.");
                    }
                }
            }
            // orders the graph
            int[] ordered = orderMySchedule(graph);
            // prints out the ordered graph
            for (int i=0;i<ordered.length;i++){
                System.out.println(graph.getValue(ordered[i]));
            }
        }
        catch (FileNotFoundException e){
            System.out.println("Input a valid file my good sir!");
        }
    }

    // this orders the graph
    public static int[] orderMySchedule(Graph graph){
        int size = graph.getSize();
        // ordering is the final ordering and the activeset is the current set being used to work in
        int[] ordering = new int[size];
        int[] activeSet = new int[size];
        int activeIndex=0;
        // this checks that the current node has no more edges and if so
        // it adds it to the activeset to start us off
        for (int i=0; i<size;i++){
                if (noMoreEdges(graph,i)) {
                    // increments activeSet by activeIndex
                    activeSet[activeIndex] = i;
                    ordering[activeIndex]=i;
                    activeIndex++;
                }
            }
        if (activeIndex==0){
            // in case it doesn't have a way to begin
            System.out.println("There is no place to start!");
            ordering=new int[0];
        }
        // starts at our active indexed nodes
        for (int i=0;i<activeIndex;i++){
            int curPos = activeSet[i];
            // searches for nodes that the curPos points to so it can sever the edge
            for(int j=0; j<size;j++){
                if(graph.isEdge(curPos,j)){
                    graph.removeEdge(curPos,j);
                    // if there are no more existing edges, the
                    // node gets added to the active set to be used to find more nodes
                    if (noMoreEdges(graph,j)) {
                        activeSet[activeIndex] = j;
                        ordering[activeIndex]=j;
                        activeIndex++;
                        }
                    }
                }
            }
        // in a case where the graph is not correct, this
        // prints a list of the unreachable courses
        if (activeIndex!=size) {
            // searches through the array of values already verified as reachable
            // and makes sure those aren't the ones being returned
            int[] cannotReach = new int[size-activeIndex];
            int notInDex=0;
            for (int i=0;i<size;i++){
                boolean notWithin=true;
                for (int j=0; j<activeIndex;j++){
                    if(activeSet[j]==i){
                        notWithin=false;
                    }
                }
                if(notWithin){
                   cannotReach[notInDex]=i;
                   notInDex++;
                }
            }
            System.out.println("These courses cannot be taken!");
            return cannotReach;
        }
        return ordering;
        }

    // this checks every connection at the node to see if it has any edges
    // leading to it
    public static boolean noMoreEdges(Graph graph,int index){
        Boolean noMore=true;
        for (int i=0;i< graph.getSize();i++){
              if(graph.isEdge(i,index)){
                  noMore=false;
              }
        }
        return noMore;
    }

    // checks if the name of all the courses/nodes are in valid course name format
    public static boolean isValid(String courseName){
       char[] letters = courseName.toCharArray();
       // it has to be 4 letters, could be upper or lower case
       for (int i =0; i<4;i++){
           if ((letters[i]<91 && letters[i]>64) || (letters[i]<123 && letters[i]>96)){
                  //its valid B)
           }
           else
               return false;
       }
       // the last 3 digits have to be numbers
       for (int i=4;i<7;i++){
           if (letters[i]<58 && letters[i]>47){
               // still valid B)
           }
           else
               return false;
       }
       return true;
    }
}
