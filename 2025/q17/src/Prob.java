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
  Map2D<Character> map = null;
  Point volc = null;

  HashMap<String, Map2D<Integer> > overlay_cache=new HashMap<>(8192, 0.5f);

  Map2D<Integer> getOverlay(long r, Point start)
  {
    String key = "" + r + " " + start;
    if (overlay_cache.containsKey(key)) return overlay_cache.get(key);

    Map2D<Integer> over = new Map2D<>(0);
    for(long x = map.low_x -1; x<=map.high_x+1; x++)
    for(long y = map.low_y -1; y<=map.high_y+1; y++)
    {
      Point p = new Point(x,y);
      long dist = p.getDist2(start);
      if (dist <= r*r) over.set(p, 1);
    }
    overlay_cache.put(key, over);
    return over;

  }

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    long p1=0;
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == '@')
      {
        for(Point q : getOverlay(10, p).getAllPoints())
        {
          if ((map.get(q) >= '0') && (map.get(q) <= '9'))
          {
            int v = Integer.parseInt("" + map.get(q));
            p1+=v;

          }
        }
      }

    }

    return "" + p1;
  }

  public String Part2(Scanner scan)
  {
    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    int most_dmg =0;
    int most_r = 0;

    int r=1;
    Map2D<Integer> overlay = new Map2D<Integer>(0);
    boolean done=false;
    while(!done)
    {
      int dmg = 0;


      for(Point p : map.getAllPoints())
      {
        if (map.get(p) == '@')
        {
          Map2D<Integer> ov = getOverlay(r, p);
          for(Point q : ov.getAllPoints())
          {
            if (ov.get(q) == 1)
            if (overlay.get(q) == 0)
            if ((map.get(q) >= '0') && (map.get(q) <= '9'))
            {
              int v = Integer.parseInt("" + map.get(q));
              dmg+=v;
            }
            if (map.get(q) == ' ') done=true;
            overlay.set(q,1);
          }
        }
      }
      //System.out.println("Radius: " + r + " " + dmg);
      if (!done)
      if (dmg > most_dmg)
      {
        most_dmg = dmg;
        most_r =r;
      }
      r++;
    }
    long p2 = most_dmg * most_r;

    return "" + p2;

  }

  Map2D<Integer> bigmap = new Map2D<Integer>(0);

  public String Part3(Scanner scan)
    throws Exception
  {

    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    bigmap = new Map2D<Integer>(0);

    for(long x = map.low_x -1; x<=map.high_x+1; x++)
    for(long y = map.low_y -1; y<=map.high_y+1; y++)
    {
      bigmap.set(x,y,1);
    }
    for(Point p : map.getAllPoints())
    {
      bigmap.set(p, 2);
    }
    Point start = null;
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == '@') volc = p;
      if (map.get(p) == 'S') start = p;
    }

    int r = 0;
    while(true)
    {
      System.out.println("Checking radius: " + r);
      search_cost_limit = (r+1)*30-1;
      search_overlay = getOverlay(r, volc);
      SS fin = (SS) Search.searchPara(new SS(0, start, new Map2D<Character>(' '), new TreeSet<String>()));
      if (fin != null)
      {
        System.out.println(fin);
        fin.diag();

        long score = fin.cost * (fin.cost / 30);
        return "" + score;
      }
      r++;

    }

    //return "";
  }

  int search_cost_limit;
  Map2D<Integer> search_overlay;


  public class SS extends State
  {
    int cost;
    Point loc;
    Map2D<Character> wall;
    TreeSet<String> dirs;

    public SS(int cost, Point loc, Map2D<Character> wall, TreeSet<String> dirs)
    {
      this.cost = cost;
      this.loc=loc;
      this.wall=wall;
      this.dirs = new TreeSet<>();
      this.dirs.addAll(dirs);

      if (loc.x == volc.x)
      {
        if (loc.y < volc.y) this.dirs.add("N");
        if (loc.y > volc.y) this.dirs.add("S");
      }

      if (loc.y == volc.y)
      {
        if (loc.x < volc.x) this.dirs.add("E");
        if (loc.x > volc.x) this.dirs.add("W");
      }
     

    }


    public double getCost()
    {
      return cost;
    }
    public boolean isTerm()
    {
      if (cost <= search_cost_limit)
      if (map.get(loc) == 'S')
      if (dirs.size() == 4)
      {
        Map2D<Integer> marked = new Map2D<Integer>(0);
        if (Search.search(new F(0, wall, volc)) == null)
        {
          return true;
        }

      }

      return false;

    }

    public String toString()
    {
      //return loc.toString() + " " + wall.getCounts().get('#');
      //if ((loc.x == volc.x) || (loc.y == volc.y))
      return loc.toString() + " " + dirs;

      //return loc.toString() + " " + wall.getHashState();

      // + " " + wall.getHashState();
    }
    public void diag()
    {
      System.out.println("Cost: " + cost);
      System.out.println("Loc: " + loc);
      System.out.println("Dirs: " + dirs);
      wall.print();

    }

    public List<State> next()
    {
      //diag();
      //System.out.println(toString());
      LinkedList<State> lst = new LinkedList<>();
      
      if (cost > search_cost_limit)
      {
        return lst;
      }

      /*int r = cost / 30;
      if (r >= 1)
      {
        Map2D<Integer> ov = getOverlay(r, volc);
        for(Point p : ov.getAllPoints())
        {
          if (ov.get(p) == 1)
          if (wall.get(p) == '#')
          {
            return lst;
          }
        }
      }*/

      for(Point p : map.getAdj(loc, false))
      {
        if (map.get(p) != ' ')
        {
          int c2=cost;
          if (map.get(p) >= '0') 
          if (map.get(p) <= '9')
          {
            c2 += Integer.parseInt("" + map.get(p));
          }
          if (search_overlay.get(p) == 0)
          {
            Map2D<Character> w2 = wall.copy();
            w2.set(p, '#');
            lst.add(new SS(c2, p, w2, dirs));
          }
          

        }

      }




      return lst;


    }


  }

  public class F extends State
  {
    Map2D<Character> wall;
    Point loc;
    int cost;

    public F(int cost, Map2D<Character> wall, Point loc)
    {
      this.cost = cost;
      this.wall=wall;
      this.loc = loc;

    }
    public double getCost()
    {
      return cost;
    }

    public String toString(){return "" + loc;}
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      for(Point p : wall.getAdj(loc, false))
      {
        if (bigmap.get(p) > 0)
        if (wall.get(p) == ' ')
        {
          lst.add(new F(cost+1, wall, p));
        }

      }

      return lst;

    }

    public boolean isTerm()
    {
      return (bigmap.get(loc) == 1);
    }

  }
}
