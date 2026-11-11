import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import duckutil.LRUCache;

public class Prob
{

  public static void main(String args[]) throws Exception
  {
    new Prob();
  }
  public Prob()
    throws Exception
  {

    /*if (new File("sample.p1").exists())
    {
      System.out.println("Sample Part 1:");
      System.out.println(Part1(new Scanner(new FileInputStream("sample.p1"))));
    }
   if (new File("sample.p1.2").exists())
    {
      System.out.println("Sample Part 1.2:");
      System.out.println(Part1(new Scanner(new FileInputStream("sample.p1.2"))));
    }

    if (new File("input.p1").exists())
    {
      System.out.println("Part 1:");
      System.out.println(Part1(new Scanner(new FileInputStream("input.p1"))));
    }*/

    /*
    if (new File("sample.p2").exists())
    {
      System.out.println("Sample Part 2:");
      System.out.println(Part2(new Scanner(new FileInputStream("sample.p2"))));
    }
    if (new File("sample.p2.2").exists())
    {
      System.out.println("Sample Part 2.2:");
      System.out.println(Part2(new Scanner(new FileInputStream("sample.p2.2"))));
    }
    if (new File("sample.p2.3").exists())
    {
      System.out.println("Sample Part 2.3:");
      System.out.println(Part2(new Scanner(new FileInputStream("sample.p2.3"))));
    }

    if (new File("input.p2").exists())
    {
      System.out.println("Part 2:");
      System.out.println(Part2(new Scanner(new FileInputStream("input.p2"))));
    }*/

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
  TreeMap<Character, Point> loc_map;

  public String Part1(Scanner scan)
  {
    memo.clear();
    map = new Map2D<Character>('#');

    MapLoad.loadMap(map, scan);

    map.print();

    Point start = map.getAllPoints('S').get(0);

    int best = -1;
    for(Point n : map.getAdj(start,false))
    {
      best = Math.max(best, rec(n, start, 1000, 100));
    }

    return "" + best;
  }

  HashMap<String, Integer> memo = new HashMap<>(1024, 0.5f);

  public int rec(Point loc, Point last, final int alt, int time_rem)
  {
    if (time_rem == 0) return alt;

    String key = "" + loc.toString() + "/" + last.toString() + "/" + alt + "/" + time_rem;
    if (memo.containsKey(key))
    {
      return memo.get(key);
    }
    //System.out.println(key);

    char z = map.get(loc);
    int alt2 = alt;
    if (z == '#') return -1;
    else if (z == '+')
    {
      alt2+=1;
    }
    else if (z == '-')
    {
      alt2-=2;
    }
    else if (z == '.')
    {
      alt2-=1;
    }
    else if (z == 'S')
    {
      return -1;
    }
    else
    {
      E.er();
    }
    if (time_rem == 0) return alt2;

    int best = -1;

    for(Point n : map.getAdj(loc, false))
    {
      if (!n.equals(last))
      {
        best = Math.max(best, rec(n, loc, alt2, time_rem-1));
      }

    }
    memo.put(key, best);

    return best;

  }

  public String Part2(Scanner scan)
    throws Exception
  {
    memo.clear();
    loc_map = new TreeMap<>();
    

    map = new Map2D<Character>('#');

    MapLoad.loadMap(map, scan);

    map.print();

    loc_map.put('A', map.getAllPoints('A').get(0));
    loc_map.put('B', map.getAllPoints('B').get(0));
    loc_map.put('C', map.getAllPoints('C').get(0));
    loc_map.put('S', map.getAllPoints('S').get(0));


    Point start = map.getAllPoints('S').get(0);
  
    LinkedList<State> starts = new LinkedList<>();
    for(Point n : map.getAdj(start,false))
    {
      starts.add(new SS(ImmutableList.of('A', 'B', 'C', 'S'), 10000, n, start, 0));
    }
    SS fin = (SS) Search.searchPara(starts);

    return "" + fin.time;
  }
  public String Part3(Scanner scan)
    throws Exception
  {
    highest.clear();
    best_y = -1;
    map = new Map2D<Character>('#');

    MapLoad.loadMap(map, scan);

    buildCrossSection();

    
    Point start = map.getAllPoints('S').get(0);
    LinkedList<State> starts = new LinkedList<>();
    for(Point n : map.getAdj(start,false))
    {
      starts.add(new FF(384400, n, start));
      //starts.add(new FF(1000, n, start));
    }
    Search.searchPara(starts);
 
    return "" + best_y;
  }

  public class SS extends State
  {
    List<Character> checks;
    int alt;
    Point loc;
    Point last;
    int time;

    public SS(List<Character> checks, int alt, Point loc, Point last, int time)
    {
      this.checks = ImmutableList.copyOf(checks);
      this.alt = alt;
      this.loc = loc;
      this.last = last;
      this.time = time;
    }

    public double getCost(){return time; }

    public String toString()
    {
      return "" + checks + "/" +alt + "/"+ loc + "/" + last;
    }

    public List<State> next()
    {
      LinkedList<State> lst = new LinkedList<>();
      char z = map.get(loc);

      LinkedList<Character> n2 = new LinkedList<Character>();
      n2.addAll(checks);
      int alt2 = alt;

      if (z == n2.get(0))
      {
        alt2 = alt2 - 1;
        if (z == 'S')
        {
          if (alt2 >= 10000)
          {
            n2.pollFirst();
          }
        }
        else
        {
          n2.pollFirst();
        }
      }
      else if (z == '.')
      {
        alt2 = alt2 - 1;
      }
      else if (z == '#')
      {
        return lst;
      }
      else if (z == '+')
      {
        alt2 += 1;
      }
      else if (z == '-')
      {
        alt2 -= 2;
      }

      for(Point n : map.getAdj(loc, false))
      {
        if (!n.equals(last))
        {
          lst.add(new SS(n2, alt2, n, loc, time+1));
        }
      }


      return lst;

    }
    public boolean isTerm()
    {
      return (checks.size() == 0);
    }
    @Override
    public double getEstimate()
    {
      double e = 0.0;
      Point x = loc;

      for(char z : checks)
      {
        Point zp = loc_map.get(z);

        e += loc.getDistM(zp);
        x=zp;
      }

      return Math.max(e, 10000.0 - alt);

    }
  }

  Map<String, Integer> highest=new LRUCache<>(2000000);
  long best_y= -1;

  // Maps to alt drop
  Map<Point, Map<Point, Integer>> cross_section_map=new TreeMap<>();

  public void buildCrossSection()
  {
    System.out.println("Building cross section map");
    cross_section_map.clear();
    for(long x=map.low_x; x<=map.high_x; x++)
    {
      highest.clear();
      Search.search(new FFCross(10000, new Point(x,0), new Point(-5,-5), new Point(x,0)));

      highest.clear();
    }

    System.out.println("Cross section map complete");
    //System.out.println(cross_section_map);
    

  }

  public class FFCross extends Flood
  {
    int alt;
    Point loc;
    Point last;
    Point start;
    public FFCross(int alt, Point loc, Point last, Point start)
    {
      this.alt = alt;
      this.loc = loc;
      this.last = last;
      this.start = start;

    }

    @Override
    public List<State> next()
    {
      LinkedList<State> lst = new LinkedList<>();
      if (loc.y > map.high_y)
      {
        if (!cross_section_map.containsKey(start))
        {
          cross_section_map.put(start, new TreeMap<Point, Integer>());
        }
        int my_drop = 10000 - alt - 1;
        if ((!cross_section_map.get(start).containsKey(loc)) || (cross_section_map.get(start).get(loc) > my_drop))
        {
          cross_section_map.get(start).put(loc, my_drop);
        }

        return lst;  
      }
      String key = loc.toString() +"/" + last.toString();

      if (loc.y < 0) return lst;
      if (alt <= 0) return lst;

      long effective_y = loc.y % (map.high_y+1);
      char z = map.get(loc.x, effective_y);
      if (z == '#') return lst;

      int alt2 = alt;
      if (z == '.')
      {
        alt2 = alt2 - 1;
      }
      else if (z == '+')
      {
        alt2 += 1;
      }
      else if (z == '-')
      {
        alt2 -= 2;
      }

      synchronized(highest)
      {
        if (highest.containsKey(key))
        {
          if (alt2 <= highest.get(key)) return lst;
        }
        highest.put(key, alt2);
      }

      for(Point p : map.getAdj(loc, false))
      {
        if (!p.equals(last))
        {
          lst.add(new FFCross(alt2, p, loc, start));  
        }
      }

      return lst;



    }
    
    public String toString()
    {
      return "" + loc + "/" + last + "/" + alt;
    }

    @Override
    public double getCost()
    {
      return - alt;
    }


  }

  public class FF extends Flood
  {
    int alt;
    Point loc;
    Point last;
    public FF(int alt, Point loc, Point last)
    {
      this.alt = alt;
      this.loc = loc;
      this.last = last;

    }
    
    public String toString()
    {
      return "" + loc + "/" + last + "/" + alt;
    }

    @Override
    public double getCost()
    {
      return - alt;
    }

    public List<State> next()
    {
      LinkedList<State> lst = new LinkedList<>();
      String key = loc.toString() +"/" + last.toString();

      if (loc.y < 0) return lst;
      if (alt <= 0) return lst;

      long effective_y = loc.y % (map.high_y+1);
      char z = map.get(loc.x, effective_y);
      if (z == '#') return lst;

      int alt2 = alt;
      if (z == '.')
      {
        alt2 = alt2 - 1;
      }
      else if (z == '+')
      {
        alt2 += 1;
      }
      else if (z == '-')
      {
        alt2 -= 2;
      }

      synchronized(highest)
      {
        if (highest.containsKey(key))
        {
          if (alt2 <= highest.get(key)) return lst;
        }
        highest.put(key, alt2);
      }

      if (alt2 >= 0) best_y = Math.max(best_y, loc.y);

      // If we have just stepped over the boarder
      if (alt2 > 20)
      if (effective_y == 0)
      if (loc.y > last.y)
      {
        for(Map.Entry<Point, Integer> me : cross_section_map.get(new Point(loc.x, 0)).entrySet())
        {
          int cost = me.getValue();
          Point n = me.getKey();
          Point actual = new Point(n.x, loc.y + map.high_y + 1);
          Point l2 = new Point(n.x, actual.y-1);
          //System.out.println("Jumping from " + loc + " to " + actual + " - " + cost);
          lst.add(new FF(alt2 - cost, actual, l2));
        }

        return lst;
      }

      for(Point p : map.getAdj(loc, false))
      {
        if (!p.equals(last))
        {
          lst.add(new FF(alt2, p, loc));  
        }
      }

      return lst;

    }

  }
}
