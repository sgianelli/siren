package edu.siren.tests.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class AIPathFollowingTest {
    
    public static class World {
        public int width, height, resolution, dec = 65;
        public int grid[];
        
        public World(int width, int height) {
            this.width = width;
            this.height = height;
            grid = new int[width * height];
            
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    grid[width * i + j] = 32;
                }
            }
        }
        
        public void addCharacter(char s, int x, int y) {
            grid[width * (height - 1 - y) + x] = s;
        }
        
        public void addObject(int x, int y, int w, int h) {
            y = (height - 1) - y;
            for (int i = y; i > (y - h); i--) {
                for (int j = x; j < (w + x); j++) {
                    grid[width * i + j] = dec;
                }
            }
            dec++;
        }
        
        public void bfs(char start, char end, int sx, int sy) {
            Queue<Integer> queue = new LinkedList<Integer>();
            Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
            Map<Integer, Integer> cost = new HashMap<Integer, Integer>();
            
            for (int i = 0; i < (width * height); i++) {
                cost.put(i, Integer.MAX_VALUE);
            }
            
            cost.put(width * sy + sx, 0);
            queue.add(width * sy + sx);
            
            System.out.println("Width: " + width + ", Height: " + height);
            
            while (!queue.isEmpty()) {
                int n = queue.poll();
                
                int x = n % width;
                int y = (n / width) % height;
                
                if (grid[n] != ' ' && grid[n] != end && grid[n] != start)
                    continue;
                
                if (grid[n] == end) {
                    System.out.println("found");
                    Integer k = prev.get(n);
                    while (k != null) {
                        grid[k] = '*';
                        k = prev.get(k);
                    }
                    return;
                }
                
                // Get around
                int lx = x - 1;
                int rx = x + 1;
                int uy = y - 1;
                int dy = y + 1;
                int nc = cost.get(n) + 1;
                
                if (lx > 0 && uy >= 0) {
                    System.out.println("up-left");
                    int d = width * uy + lx;
                    int c = cost.get(d);
                    if (nc < c) { 
                        cost.put(d, nc);
                        prev.put(d, n);
                        queue.add(d);
                    }
                }
                
                if (rx < width && uy >= 0) {
                    System.out.println("up-right");
                    int d = width * uy + rx;
                    int c = cost.get(d);
                    if (nc < c) { 
                        cost.put(d, nc);
                        prev.put(d, n);
                        queue.add(d);
                    }
                }
                
                if (rx < width && dy < height) {
                    System.out.println("bottom-right");
                    int d = width * dy + rx;
                    int c = cost.get(d);
                    if (nc < c) { 
                        cost.put(d, nc);
                        prev.put(d, n);
                        queue.add(d);
                    }
                }
                
                if (lx >= 0 && dy < height) {
                    System.out.println("bottom-left");
                    int d = width * dy + lx;
                    int c = cost.get(d);
                    if (nc < c) { 
                        cost.put(d, nc);
                        prev.put(d, n);
                        queue.add(d);
                    }
                }
                
                // Check left
                if (lx >= 0) {
                    System.out.println("left");
                    int l = width * y + lx;
                    int c = cost.get(l);
                    if (nc <= c) { 
                        cost.put(l, nc);
                        prev.put(l, n);
                        queue.add(l);
                    }
                }
                
                // Check right
                if (rx < width) {
                    System.out.println("right");
                    int r = width * y + rx;
                    int c = cost.get(r);
                    if (nc <= c) { 
                        cost.put(r, nc);
                        prev.put(r, n);
                        queue.add(r);
                    }
                }
                
                // Check up
                if (dy < height) {
                    System.out.println("down");
                    int u = width * dy + x;
                    int c = cost.get(u);
                    if (nc <= c) { 
                        cost.put(u, nc);
                        prev.put(u, n);
                        queue.add(u);
                    }
                }
                
                // Check down
                if (uy >= 0) {
                    System.out.println("up");
                    int d = width * uy + x;
                    int c = cost.get(d);
                    if (nc <= c) { 
                        cost.put(d, nc);
                        prev.put(d, n);
                        queue.add(d);
                    }
                }
            }
        }
        
        public String toString() {
            String g = "";
            for (int i = 0; i < height; i++) {
                g += "[ ";
                for (int j = 0; j < width; j++) {
                    g += (char)(grid[width * i + j])  + " "; 
                }
                g += "]\n";
            }
            return g;
        }
    }

    public static void main(String[] args) {
        World world = new World(10, 8);
        
        // Add a bunch of objects to the world
        world.addObject(3, 0, 2, 3); // A
        world.addObject(0, 3, 3, 2); // B
        world.addObject(3, 4, 2, 3); // C
        world.addObject(6, 1, 2, 2); // D
        
        world.addCharacter('S', 0, 0);
        world.addCharacter('E', 7, 6);
        
        world.bfs('S', 'E', 0, 7);
        
        System.out.println(world);
    }
}
