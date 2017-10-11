package com.marek.mazegenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public final class MazeGenerator {
    private final int size;
    private final int[][] maze;
    private final boolean[][] visited;
    
    public MazeGenerator(){
        size = 71;
        maze = new int[size][size];
        visited = new boolean[size][size];        
        initializeMaze();
//        generateRecursive();
//        generatePrims();
        generateHuntAndKill();
        markWayToStartPoint();
        findTheExit();
    }
    
    private void initializeMaze(){
        for (int i = 2; i < size - 2; i++){
            for (int j = 2; j < size - 2; j++){
                if (i % 2 == 0 && j % 2 == 0){
                    maze[i][j] = 1;
                }
            }
        }
        
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (i == 0 || i == 1 || i == size - 1 || i == size - 2||
                        j == 0 || j == 1 || j == size - 1 || j == size - 2){
                    visited[i][j] = true;
                }
            }
        }
    }
    
    private void generateRecursive(){
        int currentCellX = 2, currentCellY = 2;
        visited[currentCellX][currentCellY] = true;
        Stack<Integer> stack = new Stack<>();
        
        while (true){
            int[] wall = makeNewPassage(currentCellX, currentCellY);
            if (wall[0] != -1 && wall[1] != -1){
                stack.push(currentCellY);
                stack.push(currentCellX);
                currentCellX = wall[0];
                currentCellY = wall[1];
                visited[currentCellX][currentCellY] = true;
            }
            else if (!stack.isEmpty()){
                currentCellX = stack.pop();
                currentCellY = stack.pop();
            }
            else {
                break;
            }
        }
    }
    
    private int[] makeNewPassage(int x, int y){
        List<String> unvisitedNeighbours = new ArrayList<>();
        if (!visited[x - 2][y]){
            unvisitedNeighbours.add("left");
        }
        if (!visited[x + 2][y]){
            unvisitedNeighbours.add("right");
        }
        if (!visited[x][y - 2]){
            unvisitedNeighbours.add("up");
        }
        if (!visited[x][y + 2]){
            unvisitedNeighbours.add("down");
        }
        
        String wallToRemove;
        if (!unvisitedNeighbours.isEmpty()){
            int randomIndex = new Random().nextInt(unvisitedNeighbours.size());
            wallToRemove = unvisitedNeighbours.get(randomIndex);
            
            switch (wallToRemove) {
                case "right":
                    maze[x + 1][y] = 1;
                    return new int[] {x + 2, y};
                case "left":
                    maze[x - 1][y] = 1;
                    return new int[] {x - 2, y};
                case "up":
                    maze[x][y - 1] = 1;
                    return new int[] {x, y - 2};
                case "down":
                    maze[x][y + 1] = 1;
                    return new int[] {x, y + 2};
                default:
                    break;
            }
        }
        return new int[] {-1, -1};
    }
    
    public int[][] getMaze(){
        return maze;
    }
    
    public int getSize(){
        return size;
    }
    
    private void markWayToStartPoint(){
        int startX = 2, startY = 2;
        int finishX = size - 3, finishY = size - 3;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(startX);
        queue.add(startY);
        
        while (!queue.isEmpty()){
            int x = queue.poll();
            int y = queue.poll();
            
            if (x == finishX && y == finishY){
                break;
            }
            
            if (maze[x - 1][y] == 1){
                maze[x - 1][y] = 'd';
                maze[x - 2][y] = 'd';
                queue.add(x - 2);
                queue.add(y);
            }
            if (maze[x + 1][y] == 1){
                maze[x + 1][y] = 'u';
                maze[x + 2][y] = 'u';
                queue.add(x + 2);
                queue.add(y);
            }
            if (maze[x][y - 1] == 1){
                maze[x][y - 1] = 'r';
                maze[x][y - 2] = 'r';
                queue.add(x);
                queue.add(y - 2);
            }
            if (maze[x][y + 1] == 1){
                maze[x][y + 1] = 'l';
                maze[x][y + 2] = 'l';
                queue.add(x);
                queue.add(y + 2);
            }
        }
    }
    
    private void findTheExit(){
        int finishX = size - 3, finishY = size - 3;
        
        while ((finishX != 2) || (finishY != 2)){
            int direction = maze[finishX][finishY];
            maze[finishX][finishY] = 2;

            switch (direction){
                case 100:   finishX++;
                            break;
                case 108:   finishY--;
                            break;
                case 114:   finishY++;
                            break;
                case 117:   finishX--;
                            break;
            }
        }
        
        // clear the unused paths
        maze[2][2] = 2;
        for (int i = 1; i < size - 1; i++){
            for (int j = 1; j < size - 1; j++){
                if (maze[i][j] == 100 || maze[i][j] == 108 ||
                        maze[i][j] == 114 || maze[i][j] == 117){
                    maze[i][j] = 1;
                }
            }
        }
    }
    
    private void generatePrims(){
        int newX = 2, newY = 2;
        visited[newX][newY] = true;
        List<Integer> frontiers = new ArrayList<>();
        
        do {
            if (maze[newX - 2][newY] == 1){
                frontiers.add(newX - 2);
                frontiers.add(newY);
                maze[newX - 2][newY] = 2;
            }
            if (maze[newX + 2][newY] == 1){
                frontiers.add(newX + 2);
                frontiers.add(newY);
                maze[newX + 2][newY] = 2;
            }
            if (maze[newX][newY - 2] == 1){
                frontiers.add(newX);
                frontiers.add(newY - 2);
                maze[newX][newY - 2] = 2;
            }
            if (maze[newX][newY + 2] == 1){
                frontiers.add(newX);
                frontiers.add(newY + 2);
                maze[newX][newY + 2] = 2;
            }

            int index = new Random().nextInt(frontiers.size() / 2);
            index *= 2;
            newX = frontiers.remove(index);
            newY = frontiers.remove(index);
            visited[newX][newY] = true;
            
            List<Integer> availableMazeCells = new ArrayList<>();
            if (visited[newX - 2][newY] && maze[newX - 2][newY] != 0){
                availableMazeCells.add(newX - 2);
                availableMazeCells.add(newY);
            }
            if (visited[newX + 2][newY] && maze[newX + 2][newY] != 0){
                availableMazeCells.add(newX + 2);
                availableMazeCells.add(newY);
            }
            if (visited[newX][newY - 2] && maze[newX][newY - 2] != 0){
                availableMazeCells.add(newX);
                availableMazeCells.add(newY - 2);
            }
            if (visited[newX][newY + 2] && maze[newX][newY + 2] != 0){
                availableMazeCells.add(newX);
                availableMazeCells.add(newY + 2);
            }
            
            int index2 = new Random().nextInt(availableMazeCells.size() / 2);
            index2 *= 2;
            int chosenX = availableMazeCells.remove(index2);
            int chosenY = availableMazeCells.remove(index2);
            maze[(newX + chosenX)/2][(newY + chosenY)/2] = 1;
            
            
        } while (!frontiers.isEmpty());
        
        for (int i = 1; i < size - 1; i++){
            for (int j = 1; j < size - 1; j++){
                if (maze[i][j] == 2){
                    maze[i][j] = 1;
                }
            }
        }
    }
    
    private void generateHuntAndKill(){
        int x = 2, y = 2;
        visited[x][y] = true;
        
        boolean notAllVisited;
        do {
            notAllVisited = false;
            List<Integer> availableCells = new ArrayList<>();
            if (!visited[x - 2][y]){
                availableCells.add(x - 2);
                availableCells.add(y);
            }
            if (!visited[x + 2][y]){
                availableCells.add(x + 2);
                availableCells.add(y);
            }
            if (!visited[x][y - 2]){
                availableCells.add(x);
                availableCells.add(y - 2);
            }
            if (!visited[x][y + 2]){
                availableCells.add(x);
                availableCells.add(y + 2);
            }
            
            if (!availableCells.isEmpty()){
                int index = new Random().nextInt(availableCells.size() / 2);
                index *= 2;
                int tempX = availableCells.remove(index);
                int tempY = availableCells.remove(index);
                maze[(x + tempX)/2][(y + tempY)/2] = 1;
                x = tempX;
                y = tempY;
                visited[x][y] = true;
            }
            else {
                outer:
                for (int i = 2; i <= size - 3; i += 2){
                    for (int j = 2; j <= size - 3; j += 2){
                        if (!visited[i][j]){
                            List<Integer> visitedNeighbours = new ArrayList<>();
                            if (visited[i - 2][j] && maze[i - 2][j] == 1){
                                visitedNeighbours.add(i - 2);
                                visitedNeighbours.add(j);
                            }
                            if (visited[i + 2][j] && maze[i + 2][j] == 1){
                                visitedNeighbours.add(i + 2);
                                visitedNeighbours.add(j);
                            }
                            if (visited[i][j - 2] && maze[i][j - 2] == 1){
                                visitedNeighbours.add(i);
                                visitedNeighbours.add(j - 2);
                            }
                            if (visited[i][j + 2] && maze[i][j + 2] == 1){
                                visitedNeighbours.add(i);
                                visitedNeighbours.add(j + 2);
                            }
                            
                            if (!visitedNeighbours.isEmpty()){
                                int index = new Random().nextInt(visitedNeighbours.size() / 2);
                                index *= 2;
                                int tempX = visitedNeighbours.remove(index);
                                int tempY = visitedNeighbours.remove(index);
                                maze[(i + tempX)/2][(j + tempY)/2] = 1;
                                x = i;
                                y = j;
                                visited[x][y] = true;
                                break outer;
                            }
                        }
                    }
                } 
            }
            check:
            for (int i = 2; i <= size - 3; i += 2){
                for (int j = 2; j <= size - 3; j += 2){
                    if (!visited[i][j]){
                        notAllVisited = true;
                        break check;
                    }
                }
            }
        } while (notAllVisited);
    }
}