package edu.siren.game.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.siren.core.geom.Rectangle;
import edu.siren.game.entity.Entity;

public class Follower implements AI {
    private int width, height;
    public Entity other, self;
    public Stack<Integer> path = new Stack<Integer>();
    
    public Follower(Entity other) {
        this.other = other;
    }
    
    public void attach(Entity entity) {
        this.self = entity;
    }
    
    public void think() {
        path = new Stack<Integer>();
        ArrayList<Integer> queue = new ArrayList<Integer>();
        ArrayList<Integer> queueAlt = new ArrayList<Integer>();
        Set<Integer> seen = new HashSet<Integer>();
        Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
        Map<Integer, Integer> cost = new HashMap<Integer, Integer>();
        
        int sx = (int) self.getRect().x;
        int sy = (int) self.getRect().y;
        int ex = (int) other.getRect().x;
        int ey = (int) other.getRect().y;
        
        width = (int) self.getWorld().bounds.width;
        height = (int) self.getWorld().bounds.height;
        
        cost.put(width * sy + sx, 0);
        queue.add(width * sy + sx);
        
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
                Integer m = k;
                while (k != null) {
                    if (prev.get(k) == null)
                        break;
                    m = k;
                    k = prev.get(k);
                }
                
                int xx = m % width;
                int yy = (m / width) % height;
                self.moveTo(xx, yy);
                break;
            }
            
            boolean doContinue = false;
            for (Rectangle solid : self.getWorld().solids) {
                if (solid.contains(x, y)) {
                    if (queue.isEmpty() && !queueAlt.isEmpty()) {
                        queue.add(queueAlt.remove(queueAlt.size() - 1));
                    }
                    doContinue = true;
                    break;
                }
            }
            
            if (doContinue)
                continue;
            
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
    }
        
    private void check_cost_ignore(int x, int y, int lx, int rx, int uy,
            int dy, int nc, int n, int direction,
            Map<Integer, Integer> cost, Map<Integer, Integer> prev,
            ArrayList<Integer> queue) 
     {
        // top-left or NW
        if (direction != 5 && lx > 0 && uy >= 0) {
            int d = width * uy + lx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        }
        
        // top-right or NE
        if (direction != 3 && rx < width && uy >= 0) {
            int d = width * uy + rx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        }
        
        // bottom-right or SW
        if (direction != 7 && rx < width && dy < height) {
            int d = width * dy + rx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        }
        
        // bottom-left or SE
        if (direction != 1 && lx >= 0 && dy < height) {
            int d = width * dy + lx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        }
        
        // left or W
        if (direction != 6 && lx >= 0) {
            int l = width * y + lx;
            Integer c = cost.get(l);
            if (c == null || nc <= c) { 
                cost.put(l, nc);
                prev.put(l, n);
                queue.add(l);
            }
        }
        
        // right or E
        if (direction != 2 && rx < width) {
            int r = width * y + rx;
            Integer c = cost.get(r);
            if (c == null || nc <= c) { 
                cost.put(r, nc);
                prev.put(r, n);
                queue.add(r);
            }
        }
        
        // down or S
        if (direction != 0 && dy < height) {
            int u = width * dy + x;
            Integer c = cost.get(u);
            if (c == null || nc <= c) { 
                cost.put(u, nc);
                prev.put(u, n);
                queue.add(u);
            }
        }
        
        // up or North
        if (direction != 4 && uy >= 0) {
            int d = width * uy + x;
            Integer c = cost.get(d);
            if (c == null || nc <= c) { 
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
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        // top-right or NE
        } else if (direction == 3 && rx < width && uy >= 0) {
            int d = width * uy + rx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        // bottom-right or SE
        } else if (direction == 1 && rx < width && dy < height) {
            int d = width * dy + rx;
            Integer c = cost.get(d);
            if (c == null || nc < c) { 
                cost.put(d, nc);
                prev.put(d, n);
                queue.add(d);
            }
        // bottom-right or SW
        } else if (direction == 7 && lx >= 0 && dy < height) {
            int sw = width * dy + lx;
            Integer c = cost.get(sw);
            if (c == null || nc < c) { 
                cost.put(sw, nc);
                prev.put(sw, n);
                queue.add(sw);
            }
        // left or E
        } else if (direction == 6 && lx >= 0) {
            int l = width * y + lx;
            Integer c = cost.get(l);
            if (c == null || nc <= c) { 
                cost.put(l, nc);
                prev.put(l, n);
                queue.add(l);
            }
        // right or W
        } else if (direction == 2 && rx < width) {
            int r = width * y + rx;
            Integer c = cost.get(r);
            if (c == null || nc <= c) { 
                cost.put(r, nc);
                prev.put(r, n);
                queue.add(r);
            }
        // down or S
        } else if (direction == 0 && dy < height) {
            int u = width * dy + x;
            Integer c = cost.get(u);
            if (c == null || nc <= c) { 
                cost.put(u, nc);
                prev.put(u, n);
                queue.add(u);
            }
        // up or N
            } else if (direction == 4 && uy >= 0) {
                int d = width * uy + x;
                Integer c = cost.get(d);
                if (c == null || nc <= c) { 
                    cost.put(d, nc);
                    prev.put(d, n);
                    queue.add(d);
                }
            }
            
            return queue.size() != origSize;
        }

}
