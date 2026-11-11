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
    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p1"))
      {
        System.out.println("Part 1 - " + f.getName() + ":");
        System.out.println(Part1(new Scanner(new FileInputStream(f))));
      }
      if (f.getName().endsWith(".p2"))
      {
        System.out.println("Part 2 - " + f.getName() + ":");
        System.out.println(Part2(new Scanner(new FileInputStream(f))));
      }
      if (f.getName().endsWith(".p3"))
      {
        System.out.println("Part 3 - " + f.getName() + ":");
        System.out.println(Part3(new Scanner(new FileInputStream(f))));
      }

    }


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

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    TreeSet<String> pairs = new TreeSet<>();

    for(Point p : map.getAllPoints())
    {
      if (map.get(p)=='T')
      for(Point q : map.getAdjT(p))
      {
        if (map.get(q) == 'T')
        {
          TreeSet<String> pair = new TreeSet<String>();
          pair.add(p.toString());
          pair.add(q.toString());
          pairs.add(pair.toString());
        }

      }

    }
    return "" + pairs.size();
  }

  public String Part2(Scanner scan)
  {

    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);
    Point start = null;
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == 'S') start=p;
    }

    SS fin = (SS) Search.search(new SS(start, 0));


    return "" + fin.cost;
  }

  ArrayList<Map2D<Character>> maps;

  TreeMap<Point, Tri> tri_map;
  Point mid;

  public String Part3(Scanner scan)
  {
    map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);
    Point start=null;

    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == 'S') start=p;
    }
    tri_map = new TreeMap<>();
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) != '.')
      {
        tri_map.put(p, new Tri(p));
      }
    }
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) !='.')
      {
        Tri t = tri_map.get(p);
        //System.out.println("Working neighbors for " + p);
        if ((p.x + p.y) % 2 == 0)
        {  //Inverted
          { // A
            Point n = new Point(p.x+1, p.y);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("A", tri_map.get(n));
              tri_map.get(n).addNeighbor("A", t);
            }
          }
          { // B
            Point n = new Point(p.x-1, p.y);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("B", tri_map.get(n));
              tri_map.get(n).addNeighbor("B", t);
            }
          }
          { // C
            Point n = new Point(p.x, p.y-1);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("C", tri_map.get(n));
              tri_map.get(n).addNeighbor("C", t);
            }

          }
        }
        else
        {  //Upright
          { // A
            Point n = new Point(p.x-1, p.y);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("A", tri_map.get(n));
              tri_map.get(n).addNeighbor("A", t);
            }
          }
          { // B
            Point n = new Point(p.x+1, p.y);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("B", tri_map.get(n));
              tri_map.get(n).addNeighbor("B", t);
            }
          }
          { // C
            Point n = new Point(p.x, p.y+1);
            if (tri_map.containsKey(n))
            {
              t.addNeighbor("C", tri_map.get(n));
              tri_map.get(n).addNeighbor("C", t);
            }
          }
        } 
      }
    }

    //mid = new Point(map.high_x/2, (map.high_y)/2);
    //mid = new Point(78,31);
    mid = findMid();
    System.out.println("Selected mid: " + mid);
    //if ((mid.x + mid.y % 2) != 1) E.er("Mid always upright " + mid);

    maps = new ArrayList<>();
    maps.add(map);
    maps.add(rotate(maps.get(0)));
    maps.add(rotate(maps.get(1)));
    maps.add(rotate(maps.get(2)));

    for(int i=0; i<3; i++)
    {
      //maps.get(i).print();
    }

    for(Point p : maps.get(0).getAllPoints())
    {
      if (maps.get(0).get(p) != maps.get(3).get(p)) E.er();
    }
    System.out.println("Map rotations confirmed");


    SS3 fin = (SS3) Search.search(new SS3(start, 0, 0));

    return "" + fin.cost;
  }

  public class SFindMid extends State
  {
    LinkedList<String> path;
    Point target;
    Point loc;
    int cost;

    public SFindMid(Point target, LinkedList<String> path, Point loc, int cost)
    {
      this.target = target;
      this.path = path;
      this.loc = loc;
      this.cost = cost;

    }

    public boolean isTerm()
    {
      return (loc.equals(target));

    }
    public double getCost()
    {
      return cost;
    }
    public String toString()
    {
      return loc.toString();
    }
    @Override
    public double getEstimate()
    {
      return loc.getDistM(target);
    }

    public List<State> next()
    {
      LinkedList<State> lst = new LinkedList<>();

      Tri t = tri_map.get(loc);

      for(String step : t.neighbors.keySet())
      {
        Point next_loc = t.neighbors.get(step).map_loc;
        LinkedList<String> next_path = new LinkedList<>();
        next_path.addAll(path);
        next_path.add(step);
        lst.add(new SFindMid(target, next_path, next_loc, cost+1));
      }
      return lst;

    }

  }

  public class SS extends State
  {
    Point loc;
    int cost;

    public SS(Point loc, int cost)
    {
      this.loc = loc;
      this.cost = cost;

    }
    public double getCost(){return cost; }

    public boolean isTerm()
    {
      return (map.get(loc) == 'E');

    }
    public String toString()
    {
      return loc.toString();
    }
    public List<State> next()
    {
      LinkedList<State> lst=  new LinkedList<>();

      for(Point p : map.getAdjT(loc))
      {
        char n = map.get(p);
        if ((n == 'T') || (n=='E'))
        {
          lst.add(new SS(p, cost+1));
        }
      }
      return lst;

    }
  }
  public class SS3 extends State
  {
    Point loc;
    int cost;
    int f;

    public SS3(Point loc, int cost, int f)
    {
      this.loc = loc;
      this.cost = cost;
      this.f = f;

    }
    public double getCost(){return cost; }

    public boolean isTerm()
    {
      return (maps.get(f).get(loc) == 'E');

    }
    public String toString()
    {
      return "" + f + "." + loc.toString();
    }
    public List<State> next()
    {
      LinkedList<State> lst=  new LinkedList<>();

      int fn = (f + 1) % 3;

      for(Point p : map.getAdjT(loc))
      {
        char n = maps.get(fn).get(p);
        if ((n == 'T') || (n=='E'))
        {
          lst.add(new SS3(p, cost+1, fn));
        }
      }
      return lst;

    }


  }

  public Point findMid()
  {
    ArrayList<Map2D<Integer> > distances = new ArrayList<>();

    List<Point> corners = new LinkedList<Point>();
    corners.add(new Point(0,0));
    corners.add(new Point(map.high_x, 0));
    Point high_y = new Point(0,0);
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) != '.')
      {
        if (p.y > high_y.y)
        {
          high_y=p;
        }

      }

    }
    corners.add(high_y);

    for(Point corner : corners)
    {
      Map2D<Integer> dist=new Map2D<Integer>(0);
      for(Point p : map.getAllPoints())
      {
        if (map.get(p) != '.')
        {
          SFindMid find = (SFindMid)Search.search(new SFindMid(corner, new LinkedList<String>(), p, 0));

          dist.set(p, find.cost);
        }
      }
      distances.add(dist);
    }
    
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) != '.')
      {
        int a = distances.get(0).get(p);
        int b = distances.get(1).get(p);
        int c = distances.get(2).get(p);

        if (a == b)
        if (b == c)
        {
          return p;
        }

      }


    }


    return null;

  }

  public Map2D<Character> rotate(Map2D<Character> in)
  {
    Map2D<Character> out = new Map2D<Character>('.');

    //in.print();

    for(Point p : in.getAllPoints())
    {
      if (in.get(p) != '.')
      {
        if (p.equals(mid))
        {
          out.set(p, in.get(p));
        }
        else
        {
          //System.out.println("Searching from " + p + " " + map.get(p) + " to " + mid);
          
          SFindMid sf = (SFindMid)Search.search(new SFindMid(mid, new LinkedList<String>(), p, 0));

          LinkedList<String> path = sf.path;
          //System.out.println("Path: " + path);

          Point loc = mid;
          while(path.size() > 0)
          {
            String step = path.pollLast();
            Tri t = tri_map.get(loc);
            String backstep = null;
            if (step.equals("A")) backstep="B";
            if (step.equals("B")) backstep="C";
            if (step.equals("C")) backstep="A";

            //System.out.println("Backstep " + backstep);

            loc = t.neighbors.get(backstep).map_loc;
            //System.out.println(loc);
          }

          //System.out.println("Set " + loc + " " + in.get(p));
          out.set(loc, in.get(p));
        }


      }

    }

    //out.print();
    return out;
  }


  public class Tri
  {
    Point map_loc;
    TreeMap<String, Tri> neighbors;

    public Tri(Point map_loc)
    { 
      this.map_loc = map_loc;
      neighbors = new TreeMap<>();
    }
    public String toString()
    {
      return "" + map_loc.toString() + " " + neighbors.size() + " " + neighbors.keySet();
    }

    void addNeighbor(String step, Tri n)
    {
      if (neighbors.containsKey(step))
      {
        
        if (n != neighbors.get(step))
        {
          System.out.println("On " + map_loc + " " + step + " has " + neighbors.get(step) + " new " + n);
          E.er();
        }
      }
      neighbors.put(step, n);

    }

  }

}
