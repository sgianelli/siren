package edu.siren.tests.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
        
        public void bfs(int sx, int sy, int ex, int ey) {
            ArrayList<Integer> queue = new ArrayList<Integer>();
            ArrayList<Integer> queueAlt = new ArrayList<Integer>();
            Set<Integer> seen = new HashSet<Integer>();
            Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
            Map<Integer, Integer> cost = new HashMap<Integer, Integer>();
            
            for (int i = 0; i < (width * height); i++) {
                cost.put(i, Integer.MAX_VALUE);
            }
            
            cost.put(width * sy + sx, 0);
            queue.add(width * sy + sx);
            
            int touched = 0;
            
            while (!queue.isEmpty()) {
                int n = queue.remove(queue.size() - 1);
                
                int x = n % width;
                int y = (n / width) % height;
                
                if (seen.contains(n)) {
                    if (queue.isEmpty() && !queueAlt.isEmpty()) {
                        queue.add(queueAlt.remove(queueAlt.size() - 1));
                    }
                    continue;
                }
                
                seen.add(n);
                
                if (x == ex && y == ey) {
                    Integer k = prev.get(n);
                    while (k != null) {
                        grid[k] = '*';
                        k = prev.get(k);
                    }
                    break;
                }
                
                if (grid[n] != ' ') {
                    if (queue.isEmpty() && !queueAlt.isEmpty()) {
                        queue.add(queueAlt.remove(queueAlt.size() - 1));
                    }
                    continue;
                }
                
                touched++;
                
                grid[n] = '#';
                
                // Figure out the optimal angle between the two points
                float dyp = ey - y;
                float dxp = ex - x;
                
                double rads = Math.atan2(dyp, dxp) + Math.PI / 2.0;
                
                if (rads < 0) {
                    rads += 2*Math.PI;
                }
                
                double angle = rads * 180.0f / Math.PI;
                
                // North
                int direction = 0;
                
                // Figure out the optimal angle between where the point is
                // and the current object
                
                // North
                if (angle <= 0.0 || angle > 315 && angle <= 360) {
                    direction = 0;
                // North East
                } else if (angle > 0 && angle <= 45) {
                    direction = 3;
                // East
                } else if (angle > 45 && angle <= 90) {
                    direction = 2;
                // South East 
                } else if (angle > 90 && angle <= 135) {
                    direction = 1;
                // South
                } else if (angle > 130 && angle <= 180) {
                    direction = 4;
                // South West
                } else if (angle > 180 && angle <= 225) {
                    direction = 7;
                // West
                } else if (angle > 225 && angle <= 270) {
                    direction = 6;
                // North West
                } else if (angle > 270 && angle <= 315) {
                    direction = 5;
                }
                
                // Get around
                int lx = x - 1;
                int rx = x + 1;
                int uy = y - 1;
                int dy = y + 1;
                int nc = cost.get(n) + 1;
                
                check_cost(x, y, lx, rx, uy, dy, nc, n, direction, cost, prev, queue);
                check_cost_ignore(x, y, lx, rx, uy, dy, nc, n, direction, cost, prev, queueAlt);
                
                if (queue.isEmpty() && !queueAlt.isEmpty()) {
                    queue.add(queueAlt.remove(queueAlt.size() - 1));
                }
            }
            
            System.out.println("Touched: " + touched + " or " + ((touched / (width * height * 1.0f)) * 100.0f) + "% of the grid");
        }
        
        private void check_cost_ignore(int x, int y, int lx, int rx, int uy,
                int dy, int nc, int n, int direction,
                Map<Integer, Integer> cost, Map<Integer, Integer> prev,
                ArrayList<Integer> queue) 
         {
            // top-left or NW
            if (direction != 5 && lx > 0 && uy >= 0) {
                int d = width * uy + lx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            // top-right or NE
            if (direction != 3 && rx < width && uy >= 0) {
                int d = width * uy + rx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            // bottom-right or SW
            if (direction != 7 && rx < width && dy < height) {
                int d = width * dy + rx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            // bottom-left or SE
            if (direction != 1 && lx >= 0 && dy < height) {
                int d = width * dy + lx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            // left or W
            if (direction != 6 && lx >= 0) {
                int l = width * y + lx;
                int c = cost.get(l);
                if (nc <= c) { 
                    cost.put(l, nc);
                    prev.put(l, n);
                    queue.add(l);
                }
            }
            
            // right or E
            if (direction != 2 && rx < width) {
                int r = width * y + rx;
                int c = cost.get(r);
                if (nc <= c) { 
                    cost.put(r, nc);
                    prev.put(r, n);
                    queue.add(r);
                }
            }
            
            // down or S
            if (direction != 0 && dy < height) {
                int u = width * dy + x;
                int c = cost.get(u);
                if (nc <= c) { 
                    cost.put(u, nc);
                    prev.put(u, n);
                    queue.add(u);
                }
            }
            
            // up or North
            if (direction != 4 && uy >= 0) {
                int d = width * uy + x;
                int c = cost.get(d);
                if (nc <= c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
        }   

        private boolean check_cost(int x, int y, int lx, int rx, int uy, int dy, 
                int nc, int n, int direction, Map<Integer, Integer> cost,
                Map<Integer, Integer> prev, ArrayList<Integer> queue) 
        {
            int origSize = queue.size();
            
            // top-left or NW
            if (direction == 5 && lx > 0 && uy >= 0) {
                int d = width * uy + lx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            // top-right or NE
            } else if (direction == 3 && rx < width && uy >= 0) {
                int d = width * uy + rx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            // bottom-right or SE
            } else if (direction == 1 && rx < width && dy < height) {
                int d = width * dy + rx;
                int c = cost.get(d);
                if (nc < c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            // bottom-right or SW
            } else if (direction == 7 && lx >= 0 && dy < height) {
                int sw = width * dy + lx;
                int c = cost.get(sw);
                if (nc < c) { 
                    cost.put(sw, nc);
                    prev.put(sw, n);
                    queue.add(sw);
                }
            // left or E
            } else if (direction == 6 && lx >= 0) {
                int l = width * y + lx;
                int c = cost.get(l);
                if (nc <= c) { 
                    cost.put(l, nc);
                    prev.put(l, n);
                    queue.add(l);
                }
            // right or W
            } else if (direction == 2 && rx < width) {
                int r = width * y + rx;
                int c = cost.get(r);
                if (nc <= c) { 
                    cost.put(r, nc);
                    prev.put(r, n);
                    queue.add(r);
                }
            // down or S
            } else if (direction == 0 && dy < height) {
                int u = width * dy + x;
                int c = cost.get(u);
                if (nc <= c) { 
                    cost.put(u, nc);
                    prev.put(u, n);
                    queue.add(u);
                }
            // up or N
            } else if (direction == 4 && uy >= 0) {
                int d = width * uy + x;
                int c = cost.get(d);
                if (nc <= c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            return queue.size() != origSize;
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
        World world = new World(1000, 1000);
        
        // Add a bunch of objects to the world
        
        world.addObject(0, 3, 3, 2); // A
        world.addObject(3, 4, 2, 3); // B
        world.addObject(6, 1, 2, 2); // C
        world.addObject(50, 50, 30, 20); // D
        world.addObject(20, 30, 30, 18); // E
        world.addObject(69, 70, 30, 25); // F
        world.addObject(15, 30, 20, 10); // G
        
        world.addCharacter('H', 50, 80);
        
        world.bfs(0, 999, 50, 30);
    }
}
