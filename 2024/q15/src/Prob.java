import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Prob
{

  public static void main(String args[]) throws Exception
  {
    new Prob();
  }
  public Prob()
    throws Exception
  {

    if (new File("sample.p1").exists())
    {
      System.out.println("Sample Part 1:");
      System.out.println(Part1(new Scanner(new FileInputStream("sample.p1"))));
    }
    if (new File("input.p1").exists())
    {
      System.out.println("Part 1:");
      System.out.println(Part1(new Scanner(new FileInputStream("input.p1"))));
    }

    if (new File("sample.p2").exists())
    {
      System.out.println("Sample Part 2:");
      System.out.println(Part2(new Scanner(new FileInputStream("sample.p2"))));
    }
    if (new File("input.p2").exists())
    {
      System.out.println("Part 2:");
      System.out.println(Part2(new Scanner(new FileInputStream("input.p2"))));
    }

    if (new File("sample.p3").exists())
    {
      System.out.println("Sample Part 3:");
      System.out.println(Part3(new Scanner(new FileInputStream("sample.p3"))));
    }
    if (new File("input.p3").exists())
    {
      System.out.println("Part 3:");
      System.out.println(Part3(new Scanner(new FileInputStream("input.p3"))));
    }

  }

  Random rnd=new Random();

  Map2D<Character> map;

  public class SS extends State
  {
    int cost;
    Set<Character> collected;
    Point loc;
    Point dest;
    Set<Character> desired;

    public SS(int cost, Set<Character> collected, Point loc, Point dest, Set<Character> desired)
    {
      this.cost = cost;
      this.collected = collected;
      this.loc = loc;
      this.dest = dest;
      this.desired = desired;
    }

    public boolean isTerm()
    {
      if (loc.equals(dest))
      if (collected.equals(desired))
      {
        return true;
      }
      return false;
    }

    public String toString()
    {
      return "" + loc + "/" + collected;
    }
    public double getCost()
    {
      return cost;
    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      for(Point n : map.getAdj(loc, false))
      {
        char z = map.get(n);
        if (z != '#')
        if (z != '~')
        {
          Set<Character> nc = collected;
          if (z != '.')
          {
            nc = new TreeSet<>();
            nc.addAll(collected);
            nc.add(z);
          }
          lst.add(new SS(cost+1, nc, n, dest, desired)); 

        }

      }


      return lst;

    }

  }
  public class SS2 extends State
  {
    int cost;
    Set<Character> collected;
    Point loc;
    Point dest;
    Set<Character> desired;
    List<Character> order;

    public SS2(int cost, Set<Character> collected, Point loc, Point dest, Set<Character> desired, List<Character> order)
    {
      this.cost = cost;
      this.collected = collected;
      this.loc = loc;
      this.dest = dest;
      this.desired = desired;
      this.order = order;
    }

    public boolean isTerm()
    {
      if (loc.equals(dest))
      if (collected.equals(desired))
      {
        return true;
      }
      return false;
    }

    public String toString()
    {
      return "S2 " + loc + "/" + collected;
    }
    public double getCost()
    {
      return cost;
    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<>();
      char next_target = '.';
      for(char nt : desired)
      {
        if (!collected.contains(nt))
        {
          next_target = nt;
          break;
        }
      }

      for(Point n : warp_map.get(loc).keySet())
      {
        char z = map.get(n);
        //if ((z == next_target) || (next_target > 'E'))
        //if (z == next_target)
        {
          Set<Character> nc = collected;
          if (z != '.')
          {
            if (!collected.contains(z))
            {
              nc = new TreeSet<>();
              nc.addAll(collected);
              nc.add(z);

              List<Character> o2 = new LinkedList<>();
              o2.addAll(order);
              o2.add(z);
              lst.add(new SS2(cost+warp_map.get(loc).get(n), nc, n, dest, desired, o2)); 
            }
            else
            {
              //lst.add(new SS2(cost+warp_map.get(loc).get(n), nc, n, dest, desired)); 
              // no add - not a new value
            }
          }
          else
          {
            if (collected.size() == desired.size())
            {
            // but we can go to dot
            lst.add(new SS2(cost+warp_map.get(loc).get(n), nc, n, dest, desired, order)); 
            }
          }
        }
      }
      return lst;

    }

  }


  public String Part1(Scanner scan)
  {
    map = new Map2D<>('#');
    MapLoad.loadMap(map, scan);

    map.print();

    Point start =null;
    for(int x = 0; x<=map.high_x; x++)
    {
      if (map.get(x,0) == '.') start = new Point(x,0);
    }
    System.out.println("Start: " + start);

    SS fin = (SS) Search.search(new SS(0, ImmutableSet.of(), start, start, ImmutableSet.of('H')));
    
    return "" + fin.cost;
  }

  public String Part2(Scanner scan)
    throws Exception
  {
    //return Part3(scan);

    map = new Map2D<>('#');
    MapLoad.loadMap(map, scan);

    map.print();

    Point start =null;
    for(int x = 0; x<=map.high_x; x++)
    {
      if (map.get(x,0) == '.') start = new Point(x,0);
    }
    System.out.println("Start: " + start);

    TreeSet<Character> targets = new TreeSet<>();
    for(char z : map.getCounts().keySet())
    {
      if (z >= 'A')
      if (z <= 'Z')
        targets.add(z);

    }

    SS fin = (SS) Search.searchPara(new SS(0, ImmutableSet.of(), start, start, targets));
    
    return "" + fin.cost;
  }

  // Point A -> Point B -> Cost
  TreeMap<Point, TreeMap<Point, Integer> > warp_map=new TreeMap<>();
  public String Part3(Scanner scan)
    throws Exception
  {
    return Part2(scan);
    /*
    map = new Map2D<>('#');
    MapLoad.loadMap(map, scan);
    warp_map.clear();
    Point start =null;
    for(int x = 0; x<=map.high_x; x++)
    {
      if (map.get(x,0) == '.') start = new Point(x,0);
    }
    System.out.println("Start: " + start);
    populateWarpMap(start);
    TreeSet<Character> targets = new TreeSet<>();

    for(Point p : map.getAllPoints())
    {
      char z = map.get(p);
      if (z >= 'A')
      if (z <= 'Z')
      {
        targets.add(z);
        populateWarpMap(p);
      }

    }
    System.out.println("Warp map populated");

    SS2 fin = (SS2) Search.searchPara(new SS2(0, ImmutableSet.of(), start, start, targets, ImmutableList.of()));
    
    System.out.println(fin);
    System.out.println(fin.order);
    return "" + fin.cost;*/
  }

  public void populateWarpMap(Point start)
  {
    Search.search(new FF(start, 0, start));

  }
  public void addWarp(Point a, Point b, int cost)
  {
    if (!warp_map.containsKey(a)) warp_map.put(a, new TreeMap<Point, Integer>());
    if (!warp_map.containsKey(b)) warp_map.put(b, new TreeMap<Point, Integer>());

    warp_map.get(a).put(b,cost);
    warp_map.get(b).put(a,cost);

  }
  public class FF extends Flood
  {
    Point src;
    Point loc;
    int cost;
    public FF(Point loc, int cost, Point src)
    {
      this.loc = loc;
      this.cost = cost;
      this.src = src;

    }
    public String toString()
    {
      return loc.toString();
    }

    @Override
    public double getCost()
    {
      return cost;
    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      char z = map.get(loc);
      if (z >= 'A')
      if (z <= 'Z')
      {
        addWarp(src,loc,cost);
      }
      for(Point n : map.getAdj(loc,false))
      {
        char q = map.get(n);
        if (q != '#')
        if (q != '~')
        {
          lst.add(new FF(n, cost+1, src));
        }
      }
      return lst;
    }

  }
}
