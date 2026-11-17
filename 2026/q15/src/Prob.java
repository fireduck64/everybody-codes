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

  Map2D<Character> map;
  long start_line;

  int total_sulfur;

  void countSulfur()
  {
    total_sulfur = 0;
    for(Point p : map.getAllPoints())
    {
      if (map.get(p)=='@') total_sulfur++;
    }
  }
  Point findStart()
  {
    
    for(Point p : map.getAllPoints())
    {
      if (map.get(p)=='S') return p;
    }
    E.er();
    return null;

  }

  public String Part1(Scanner scan)
    throws Exception
  {
    map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    countSulfur();

    Point start = findStart();

    P1State sol = (P1State) Search.searchPara(new P1State(start, Nav.E, new TreeSet<Point>(), 0));

    return "" + sol.cost;
  }

  public class P1State extends State
  {
    Point loc;
    Point dir;
    TreeSet<Point> found_s;
    int cost;

    public P1State(Point loc, Point dir, TreeSet<Point> found_s, int cost)
    {
      this.loc = loc;
      this.dir = dir;
      this.found_s = found_s;
      this.cost = cost;
    }

    public boolean isTerm()
    {
      if (found_s.size() == total_sulfur)
      if (map.get(loc) == 'E')
      {
        return true;
      }

      return false;

    }
    public String toString()
    {
      return "" + loc + " " + dir + " " + found_s;
    }

    public List<State> next()
    {
      ArrayList<Point> dirs = new ArrayList<>();
      dirs.add(dir);
      dirs.add( Nav.turnLeft(dir));
      dirs.add( Nav.turnRight(dir));
      List<State> lst = new LinkedList<>();

      for(Point next_dir : dirs)
      {
        Point np = loc.add(next_dir);
        char z = map.get(np);
        if (z != '#')
        {
          TreeSet<Point> found_s_n = found_s;
          if (z == '@')
          {
            found_s_n = new TreeSet<>();
            found_s_n.addAll(found_s);
            found_s_n.add(np);
          }
          lst.add(new P1State(np, next_dir, found_s_n, cost+1));
        }

      }

      return lst;

    }
    public double getCost()
    {
      return cost;
    }
  

  }

  long target;
  public String Part2(Scanner scan)
    throws Exception
  {
    target = 2026L;
    mod_mod_mod=0;

    map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    countSulfur();

    Point start = findStart();
    start_line = start.y;

    P2State sol = (P2State) Search.searchPara(new P2State(start, Nav.E, 0, 0, map.copy()));

    sol.path.print();



    return "" + sol.cost;

  }
  public class P2State extends State
  {
    Point loc;
    Point dir;
    int cost;
    int sulfur;
    Map2D<Character> path;

    public P2State(Point loc, Point dir, int cost, int sulfur, Map2D<Character> path)
    {
      this.loc = loc;
      this.dir = dir;
      this.cost = cost;
      this.sulfur = sulfur;
      this.path = path;
      if ((path.get(loc) == '.') || (path.get(loc) == '@'))
      {
        this.path = path.copy();
        this.path.set(loc, '1');
      }
      else if ((path.get(loc) >= '1') && (path.get(loc) < '9'))
      {
        this.path = path.copy();

        int v = Integer.parseInt("" + path.get(loc));
        v++;
        String str = "" + v;
        this.path.set(loc, str.charAt(0));

      }

    }

    public boolean isTerm()
    {
      if (sulfur >= target)
      if (map.get(loc) == 'E')
      {
        if (mod_mod_mod > 0)
        {
          if ((p3_target - sulfur) % mod_mod_mod == 0)
            return true;
        }
        else
        {
          return true;
        }
      }

      return false;

    }
    public String toString()
    {
      return "" + loc + " " + dir + " " + sulfur;
    }

    public List<State> next()
    {
      ArrayList<Point> dirs = new ArrayList<>();
      dirs.add(dir);
      dirs.add( Nav.turnLeft(dir));
      dirs.add( Nav.turnRight(dir));
      List<State> lst = new LinkedList<>();

      for(Point next_dir : dirs)
      {
        Point np = loc.add(next_dir);
        char z = map.get(np);
        if (z != '#')
        {
          int ns = sulfur;
          if (z == '@')
          {
            ns++;
          }
          lst.add(new P2State(np, next_dir, cost+1, ns, path));
        }
      }

      return lst;

    }
    public double getCost()
    {
      return cost;
    }
  

  }

  long mod_mod_mod=0;
  long p3_target=202620262026L;

  public String Part3(Scanner scan)
    throws Exception
  {
    map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    countSulfur();

    Point start = findStart();
    start_line = start.y;

    target = 670;

    /*Box best_box = getBestBox();
    best_box.sulfur=58;
    while( (p3_target - target) % best_box.sulfur != 0) target++;
    */
    P2State sol = null;

    TreeSet<Point> nines;
    long sulfur_loop;

    while( true )
    {
      System.out.println("Running with target: " + target);
      sol = (P2State) Search.searchPara(new P2State(start, Nav.E, 0, 0, map.copy()));
      sol.path.print();

      nines = new TreeSet<Point>();
      sulfur_loop=0;
      for(Point p : sol.path.getAllPoints())
      {
        if (sol.path.get(p) == '9')
        {
          nines.add(p);
          if (map.get(p) == '@') sulfur_loop++;
        }
      }
      System.out.println("Nines: c:" + nines.size() + " s:" + sulfur_loop);
      // Establish target parameters
      long rem = (p3_target - sol.sulfur) % sulfur_loop;
      System.out.println("Remainder: " + rem);
      System.out.println("Target: " + target + " sulfur: " + sol.sulfur);

      mod_mod_mod= sulfur_loop;

      if ((p3_target - sol.sulfur) % sulfur_loop != 0)
      {
        target++;
        continue;
      }
      else
      {
        break;
      }

    }



    //if (sulfur_loop != best_box.sulfur) E.er();
    //if (nines.size() != best_box.cost) E.er();

    if (sulfur_loop == 0L) E.er(nines.toString());
    target = p3_target;

    long sulfur = sol.sulfur;
    long loops = (p3_target - sulfur) / sulfur_loop;
    if ((sulfur + loops * sulfur_loop) < target)
    {
      long v = sulfur + loops * sulfur_loop;
      System.out.println("Reaching: " + v);
      System.out.println("Target: " + target);
      long rem = target - sulfur + loops * sulfur_loop;
      System.out.println("Rem: " + rem);
      //E.er("Shouldn't need extra loop");
      System.out.println("Extra loop");
      sulfur_loop++;
    }

    sulfur += loops * sulfur_loop;
    long cost = sol.cost + loops * nines.size();

    /*target = 202620262026L;

    long cost = map.high_x - 1;
    long loops = target / best_box.sulfur;
    //if (202620262026L % best_box.sulfur != 0) loops++;

    cost-=best_box.x_dist + 1;

    cost+=loops * best_box.cost;
    long sulfur = loops * best_box.sulfur;*/


    //return "" + cost;
    return "" + cost;

  }

  public Box getBestBox()
  {
    ArrayList<Box> boxes = getBoxes();

    Box best_box = null;
    for(Box b : boxes)
    {
      //System.out.println(b);
      if ((best_box == null) || (b.getCostPerSulfur() < best_box.getCostPerSulfur()))
      {
        best_box = b;
      }
    }

    return best_box;

  }

  public ArrayList<Box> getBoxes()
  {
    ArrayList<Box> lst = new ArrayList<>();

    for(long x=0; x<=map.high_x; x++)
    {
      for(long y=0; y<=map.high_y; y++)
      {
        if (y != start_line)
        for(long x_end = x+1; x_end <= map.high_x; x_end++)
        {
          TreeSet<Point> points = new TreeSet<>();
          points.addAll( getLine(new Point(x, start_line), new Point(x_end,start_line), Nav.E));
          points.addAll( getLine(new Point(x, y), new Point(x_end,y), Nav.E));

          if (y < start_line)
          {
            points.addAll( getLine(new Point(x, start_line), new Point(x, y), Nav.N));
            points.addAll( getLine(new Point(x_end, start_line), new Point(x_end, y), Nav.N));
          }
          else
          {
            points.addAll( getLine(new Point(x, start_line), new Point(x, y), Nav.S));
            points.addAll( getLine(new Point(x_end, start_line), new Point(x_end, y), Nav.S));
          }

          int sc = 0;
          boolean wall=false;
          for(Point p : points)
          {
            if (map.get(p) == '@') sc++;
            if (map.get(p) == '#') wall=true;
          }
          if ((sc > 0) && (!wall))
          {
            Box b = new Box();
            b.sulfur=sc;
            b.cost = points.size();
            b.x_dist = (int) (x_end - x);
            lst.add(b);

          }
        }
      }
    }

    return lst;
  }

  public Set<Point> getLine(Point a, Point b, Point dir)
  {
    Point cur = a;
    TreeSet<Point> set=new TreeSet<>();
    set.add(a);
    set.add(b);

    while(!cur.equals(b))
    {
      set.add(cur);
      if (set.size() > 8000) E.er();
      cur = cur.add(dir);

    }

    return set;
  }

  public class Box
  {
    long cost;
    long sulfur;
    long x_dist;

    double getCostPerSulfur()
    {
      double d_cost = cost;
      double d_sulfur = sulfur;

      return d_cost / d_sulfur;
      
    }
    public String toString()
    {
      return String.format("Box cost:%d sulfur:%d x:%d", cost, sulfur, x_dist);
    }

  }
}
