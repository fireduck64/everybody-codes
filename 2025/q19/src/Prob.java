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
  int target_x;
  TreeMap<Integer, List<Point> > imap;

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>('.');
    Point start=new Point(0,0);

    map.set(0,0,'S');

    int x_loc=0;
    while(scan.hasNextLine())
    {
      List<Integer> seg = Tok.ent(scan.nextLine(), ",");
      int before = seg.get(0);
      int h = seg.get(1);
      int w = seg.get(2);



      x_loc = before;
      System.out.println(seg);
      for(int y=0; y<h; y++)
      {
        map.set(x_loc, y, '#');
      }
      for(int y=0; y<8+before*2+h+w; y++)
      {
        map.set(x_loc, y+h+w, '#');
      }
    }
    target_x = x_loc+1;

    //map.print();

    SS sol = (SS) Search.search(new SS(start, 0));


    return "" + sol.flaps;
  }

  public String Part2(Scanner scan)
    throws Exception
  {

    return Part3(scan);

    /*map = new Map2D<Character>('.');
    Point start=new Point(0,0);

    map.set(0,0,'S');

    int x_loc=0;
    while(scan.hasNextLine())
    {
      List<Integer> seg = Tok.ent(scan.nextLine(), ",");
      int before = seg.get(0);
      int h = seg.get(1);
      int w = seg.get(2);

      x_loc = before;
      for(int y=0; y<1000; y++)
      {
        if (map.get(x_loc, y) == '.') map.set(x_loc, y, '#');
      }

      System.out.println(seg);
      for(int y=0; y<w; y++)
      {
        map.set(x_loc, h+y, ' ');
      }
    }
    target_x = x_loc+1;

    //map.print();

    SS sol = (SS) Search.search(new SS(start, 0));


    return "" + sol.flaps;*/

  }
  public String Part3(Scanner scan)
    throws Exception
  {
    map = new Map2D<Character>('.');
    Point start=new Point(0,0);

    imap = new TreeMap<>();

    int x_loc=0;
    while(scan.hasNextLine())
    {
      List<Integer> seg = Tok.ent(scan.nextLine(), ",");
      int x = seg.get(0);
      int h = seg.get(1);
      int w = seg.get(2);

      if (!imap.containsKey(x))
      {
        imap.put(x, new LinkedList<Point>());
      }
      imap.get(x).add(new Point(h, h+w-1));

      x_loc = x;


    }
    target_x = x_loc;

    costmap = new Map2D<Long>(1000000000000L);

    //map.print();

    //SS2 sol = (SS2) Search.searchPara(new SS2(start, 0));
    x_loc=0;
    costmap.set(0,0,0L);

    while(x_loc < target_x)
    {
      x_loc = dpRound(x_loc);
    }

    long low = 1000000000L;

    for(int y : getAll(imap.get(target_x)))
    {
      //System.out.println(" " + y + " " + costmap.get(target_x, y));
      low = Math.min(low, costmap.get(target_x, y));
    }

    //return "" + sol.flaps;
    return "" + low;

  }

  Map2D<Long> costmap;

  public int dpRound(int x1)
  {
    int x2 = imap.higherKey(x1);
    //System.out.println("dp round " + x1 + " " + x2);

    TreeSet<Integer> start_set = null;
    if (x1 == 0)
    {
      start_set =new TreeSet<>();
      start_set.add(0);
    }
    else
    {
      start_set= getAll(imap.get(x1));
    }
    TreeSet<Integer> end_set= getAll(imap.get(x2));
    //System.out.println("  Sizes " + start_set.size() + " " + end_set.size());

    int dist_x = x2-x1;
    for(int s : start_set)
    {
      long cost = costmap.get(x1,s);
      if (cost < 0) E.er();
      if (cost < 1000000000L)
      for(int e : end_set)
      {
        //System.out.println(" x1 " + x1 + " x2 " + x2 + " s" + s + " e" + e);
        long flaps=getFlaps(s,e,dist_x);
        if (flaps < 0)
        {
          //System.out.println("Not doable");

        }

        if (flaps >= 0)
        {
          flaps += cost;
          long v = costmap.get(x2, e);
          if (v >= flaps)
          {
            //System.out.println("Store " + x1 + " " + x2 + " " + s + " " + e + " " + flaps);
            costmap.set(x2,e, flaps);
          }
        }
      }
    }
    return x2;

  }
  public int getFlaps(int s, int e, int dist)
  {
    int flaps =0;
    int x=0;
    int diff_y = e-s;
    if (diff_y > 0)
    {
      flaps += diff_y;
      x += diff_y;
    }
    else if (diff_y < 0)
    {
      x+=Math.abs(diff_y);
    }
    if (x > dist) return -1;
    dist -= x;

    if (dist == 0) return flaps;

    if (dist % 2 == 1) return -1;
    flaps+=dist/2;

    if (flaps < 0) E.er();
    //System.out.println("Flap s" + s + " e" + e + " d" + dist + " f" + flaps);
    return flaps;

  }

  public TreeSet<Integer> getAll(List<Point> inter)
  {
    TreeSet<Integer> set = new TreeSet<>();

    for(Point p : inter)
    {
      for(int i=(int)p.x; i<=p.y; i++) set.add(i);
    }

    return set;

  }

  public class SS extends State
  {
    Point loc;
    int flaps;

    public SS(Point loc, int flaps)
    {
      this.loc = loc;
      this.flaps = flaps;

    }
    public String toString()
    {
      return loc.toString();
    }
    public boolean isTerm()
    {
      return (loc.x == target_x);
    }
    public double getCost()
    {
      return flaps;
    }
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      // Flap
      {
        Point n = loc.add(new Point(1, 1));
        if (map.get(n) != '#')
        if (n.y >= 0)
        {
          lst.add(new SS(n, flaps+1));
        }

      }
      // No flap
      {
         Point n = loc.add(new Point(1, -1));
        if (map.get(n) != '#')
        if (n.y >= 0)
        {
          lst.add(new SS(n, flaps));
        }

      }

      return lst;

    }
  }

  public class SS2 extends State
  {
    Point loc;
    int flaps;

    public SS2(Point loc, int flaps)
    {
      this.loc = loc;
      this.flaps = flaps;

    }
    public String toString()
    {
      return loc.toString();
    }
    public boolean isTerm()
    {
      return (loc.x == target_x);
    }
    public double getCost()
    {
      return flaps;
    }
    @Override
    public double getLean()
    {
      return loc.x;
    }
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      // Flap
      {
        Point n = loc.add(new Point(1, 1));
        if (isAllowed(n))
        {
          lst.add(new SS2(n, flaps+1));
        }

      }
      // No flap
      {
        Point n = loc.add(new Point(1, -1));
        if (isAllowed(n))
        {
          lst.add(new SS2(n, flaps));
        }

      }

      return lst;

    }
  }
  public boolean isAllowed(Point p)
  {
    if (p.y < 0) return false;

    List<Point> inter = imap.get((int)p.x);

    if (inter == null) return true;

    for(Point n : inter)
    {
      if (n.x <= p.y)
      if (p.y <= n.y)
        return true;

    }
    return false;
  }
}
