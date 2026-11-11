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
  Point end;
  Point start;

  TreeSet<Long> x_loc=new TreeSet<>();
  TreeSet<Long> y_loc=new TreeSet<>();
  LinkedList<Segment> segments;

   
  TreeMap<Long, TreeSet<Long> > x_int=new TreeMap<>();
  TreeMap<Long, TreeSet<Long> > y_int=new TreeMap<>();

  public void addInterest(Point p)
  {
    if (!x_int.containsKey(p.x)) x_int.put(p.x, new TreeSet<Long>());
    if (!y_int.containsKey(p.y)) y_int.put(p.y, new TreeSet<Long>());

    x_int.get(p.x).add(p.y);
    y_int.get(p.y).add(p.x);


  }


  public String Part1(Scanner scan)
    throws Exception
  {
    segments = new LinkedList<>();
    x_loc.clear(); x_int.clear();
    y_loc.clear(); y_int.clear();
    List<String> cmd = Tok.en(scan.nextLine(), ",");
    map = new Map2D<>(' ');
    Point cur = new Point(0,0);
    start = cur;
    Point dir = Nav.getDir('N');
    map.set(cur, 'S');
    x_loc.add(cur.x); y_loc.add(cur.y);
    for(String c : cmd)
    {
      char d = c.charAt(0);
      int cnt = Integer.parseInt(c.substring(1));
      if (d == 'L') dir = Nav.turnLeft(dir);
      if (d == 'R') dir = Nav.turnRight(dir);

      segments.add(new Segment(cur, cur.add( dir.mult(cnt))));
      cur = cur.add(dir.mult(cnt));
      x_loc.add(cur.x); y_loc.add(cur.y);

      /*for(int i=0; i<cnt; i++)
      {
        cur = cur.add(dir);
        x_loc.add(cur.x); y_loc.add(cur.y);

        map.set(cur, '#');
      }*/
    }
    map.set(cur, 'E');
    end=cur;

    for(long x : x_loc)
    for(long y : y_loc)
    {
      Point p = new Point(x,y);
      for(Point q : map.getAdj(p, true))
      {
        if (isAllowed(q))
        {
          addInterest(q);
        }

      }
      if (isAllowed(p))
      {
        addInterest(p);
      }


    }
    System.out.println("Map drawn " + x_loc.size() +" " + y_loc.size());
    //map.print();

    long cost = Math.round(Search.search(new SS(new Point(0,0), 0)).getCost());
    cost -= 2L; // WTF BBQ
    return "" + cost;
    //return "" + Search.search(new SS(new Point(0,0), 0)).getCost();
  }

  public String Part2(Scanner scan)
    throws Exception
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
    throws Exception
  {
    return Part2(scan);
    //return "";
  }

  public boolean isAllowed(Point p)
  {
    if (p.equals(start)) return true;
    if (p.equals(end)) return true;

    for(Segment s : segments)
    {
      if (s.isIn(p)) return false;
    }

    return true;
  }
  public boolean isAllowedSeg(Segment seq)
  {
    for(Segment s : segments)
    {
      if (s.overlap(seq)) return false;
      if (seq.overlap(s)) return false;
    }
    return true;


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

    public double getCost()
    {
      return cost;
    }
    @Override
    public double getEstimate()
    {
      return loc.getDistM(end);
    }

    public boolean isTerm()
    {
      return (loc.equals(end));
    }
    public String toString()
    {
      return loc.toString();
    }

    public List<State> next()
    {
      List<State> lst = new LinkedList<State>();
      if (!isAllowed(loc)) return lst;

      // North
      {
        if (x_int.get(loc.x) != null)
        if (x_int.get(loc.x).higher(loc.y) != null)
        {
          Point p = new Point(loc.x, x_int.get(loc.x).higher(loc.y));
          if (isAllowedSeg(new Segment(loc, p)))
          {
            lst.add(new SS(p, cost + (int)p.getDistM(loc)));
          }
        }

      }
      // South
      {
        if (x_int.get(loc.x) != null)
        if (x_int.get(loc.x).lower(loc.y) != null)
        {
          Point p = new Point(loc.x, x_int.get(loc.x).lower(loc.y));
          if (isAllowedSeg(new Segment(loc, p)))
          {
            lst.add(new SS(p, cost + (int)p.getDistM(loc)));
          }
        }

      }
      // West
      {
        if (y_int.get(loc.y) != null)
        if (y_int.get(loc.y).lower(loc.x) != null)
        {
          Point p = new Point(y_int.get(loc.y).lower(loc.x), loc.y);
          if (isAllowedSeg(new Segment(loc, p)))
          {
            lst.add(new SS(p, cost + (int)p.getDistM(loc)));
          }
        }


      }
      // East
      {
        if (y_int.get(loc.y) != null)
        if (y_int.get(loc.y).higher(loc.x) != null)
        {
          Point p = new Point(y_int.get(loc.y).higher(loc.x), loc.y);
          if (isAllowedSeg(new Segment(loc, p)))
          {
            lst.add(new SS(p, cost + (int)p.getDistM(loc)));
          }
        }


      }
      for(Point p : map.getAdj(loc, false))
      {
        if (map.get(p) == 'E')
        {
          lst.add(new SS(p, cost+1));
        }
      }

      /*for(Point p : map.getAdj(loc, false))
      {
        if (map.get(p) != '#')
        {
          lst.add(new SS(p, cost+1));
        }
      }*/
      /*List<Point> far = new LinkedList<Point>();

      if (y_loc.higher(loc.y) != null)
      { long y = y_loc.higher(loc.y)-1; far.add(new Point(loc.x, y)); }
      if (y_loc.lower(loc.y) != null)
      { long y = y_loc.lower(loc.y)+1; far.add(new Point(loc.x, y)); }
      if (x_loc.lower(loc.x) != null)
      { long x = x_loc.lower(loc.x)+1; far.add(new Point(x, loc.y)); }
      if (x_loc.higher(loc.x) != null)
      { long x = x_loc.higher(loc.x)-1; far.add(new Point(x, loc.y)); }
      for(Point p : far)
      {
        if (map.get(p) != '#')
        if (p.getDistM(loc) > 5)
        {
          lst.add(new SS(p, cost + (int)p.getDistM(loc)));
        }

      }*/
      return lst;
    }

  }

  public class Segment
  {
    Point low;
    Point high;
    public Segment(Point a, Point b)
    {
      this.low = new Point(Math.min(a.x, b.x), Math.min(a.y, b.y));
      this.high = new Point(Math.max(a.x, b.x), Math.max(a.y, b.y));

    }

    public boolean isIn(Point p)
    {
      if (low.x <= p.x)
      if (low.y <= p.y)
      if (p.x <= high.x)
      if (p.y <= high.y)
      {
        return true;
      }

      return false;

    }
    public boolean overlap(Segment o)
    {
      // O is inside by x
      if (low.x <= o.low.x)
      if (o.high.x <= high.x)
      if (o.low.y <= low.y)
      if (high.y <= o.high.y)
      {
        return true;
      }
      if (low.y <= o.low.y)
      if (o.high.y <= high.y)
      if (o.low.x <= low.x)
      if (high.x <= o.high.x)
      {
        return true;
      }
      return false;

    }

  }
}
