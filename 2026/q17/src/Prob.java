import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;


public class Prob
{

  public static void main(String args[]) throws Exception
  {
    new Prob();
  }
  public Prob()
    throws Exception
  {
    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p1"))
      {
        System.out.println("Part 1 - " + f.getName() + ":");
        System.out.println(Part1(new Scanner(new FileInputStream(f))));
      }
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p2"))
      {
        System.out.println("Part 2 - " + f.getName() + ":");
        System.out.println(Part2(new Scanner(new FileInputStream(f))));
      }
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p3"))
      {
        System.out.println("Part 3 - " + f.getName() + ":");
        System.out.println(Part3(new Scanner(new FileInputStream(f))));
      }

    }

  }

  Random rnd=new Random();

  Map2D<Character> map = new Map2D<>('.');

  public String Part1(Scanner scan)
  {
    map = new Map2D<>('.');

    MapLoad.loadMap(map, scan);

    P1State sol = (P1State) Search.search(new P1State(new Point(0,0), 0));

    return "" + sol.cost;
  }

  public class P1State extends State
  { 
    Point loc;
    int cost;

    public P1State(Point loc, int cost)
    {
      this.loc = loc;
      this.cost = cost;
    }
    public double getCost(){return cost; }

    public String toString()
    {
      return loc.toString();
    }

    public boolean isTerm()
    {
      if (loc.x == map.high_x)
      if (loc.y == map.high_y)
      {
        return true;
      }

      return false;

    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<>();
      if (map.get(loc) != '#') return lst;

      for(Point a : map.getAdj(loc, false))
      {
        lst.add(new P1State(a, cost+1));
      }
      return lst;
    }

  }

  Map2D<Character> flood =new Map2D<Character>('.');
  public class P2State extends State
  { 
    Point loc;
    int cost;

    public P2State(Point loc, int cost)
    {
      this.loc = loc;
      this.cost = cost;
    }
    public double getCost(){return cost; }

    public String toString()
    {
      return loc.toString();
    }

    public boolean isTerm()
    {
      return false;

    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<>();
      if (map.get(loc) != '#') return lst;

      flood.set(loc, 'X');

      for(Point a : map.getAdj(loc, false))
      {
        lst.add(new P2State(a, cost+1));
      }
      return lst;
    }

  }


  public String Part2(Scanner scan)
  {
    map = new Map2D<>('.');
    flood = new Map2D<>('.');

    MapLoad.loadMap(map, scan);

    Search.search(new P2State(new Point(0,0), 0));

      int cnt = 0;
    for(Point p : flood.getAllPoints())
    {
      if (flood.get(p) == 'X')
      for(Point a : flood.getAdj(p, false))
      {
        if (flood.get(a) == '.') cnt++;
      }

    }


    return "" + cnt;

  }
  public String Part3(Scanner scan)
  {
    map = new Map2D<>('.');
    flood = new Map2D<>('.');

    MapLoad.loadMap(map, scan);

    Search.search(new P2State(new Point(0,0), 0));

    System.out.println("All reachable positions:");
    flood.print();

    for(long x = map.low_x-1; x<=map.high_x+1; x++)
    for(long y = map.low_y-1; y<=map.high_y+1; y++)
    {
      if ((x < 0) || (y < 0) || (x > map.high_x) || (y > map.high_y))
      {
        flood.set(x,y,'S');
        if (x == -1) flood.set(x,y,'A');
        if (y == -1) flood.set(x,y,'B');
        if (x == map.high_x+1) flood.set(x,y,'B');
        if (y == map.high_y+1) flood.set(x,y,'A');
      }
    }
    flood.set(-1,-1,'O');
    flood.set(map.high_x+1,map.high_y+1,'O');

    while(true)
    {
      int changes =0;
      for(long x = map.low_x-1; x<=map.high_x+1; x++)
      for(long y = map.low_y-1; y<=map.high_y+1; y++)
      {
        char z;
        z = flood.get(x,y);
        
        if ((z == 'A') || (z == 'B'))
        {
          for(Point p : map.getAdj(new Point(x,y), true))
          {
            if (flood.get(p) == '.')
            {
              flood.set(p, z);
              changes++;
            }
          }
        }
      }
      if (changes==0) break;
    }

    int cnt_a = 0;
    int cnt_b = 0;
    for(Point p : flood.getAllPoints())
    {
      if (flood.get(p) == 'X')
      for(Point a : flood.getAdj(p, false))
      {
        if (flood.get(a) == 'A') cnt_a++;
        if (flood.get(a) == 'B') cnt_b++;
      }
    }

 

    flood.print();

    int p = Math.min(cnt_a, cnt_b);


    return "" + p;

  }
}
