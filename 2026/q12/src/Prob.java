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

  public class Box
  {
    Point corner;
    Point size;

    public Box(Point corner, Point size)
    {
      this.corner = corner;
      this.size = size;
    }

  }

  public List<Box> scanBox1(Map2D<Character> map)
  {
    Map2D<Character> marked=new Map2D<Character>('.');

    List<Box> lst = new LinkedList<>();

    for(Point p : map.getAllPoints())
    {
      if (map.get(p) =='#')
      if (marked.get(p) == '.')
      {
        Point corner=p;

        int size_x=0;
        int size_y=0;
        while(map.get(p.add(new Point(size_x, 0))) == '#') size_x++;
        while(map.get(p.add(new Point(0, size_y))) == '#') size_y++;

        lst.add(new Box(corner, new Point(size_x, size_y)));

        for(int i=0; i<size_x; i++)
        for(int j=0; j<size_y; j++)
        {
          marked.set( p.add(new Point(i,j)), 'X');
        }
      }
    }
    return lst;
  }
 
  public List<Map2D<Character> > scanBox2(Map2D<Character> map)
  {
    Map2D<Character> marked=new Map2D<Character>('.');

    List<Map2D<Character> > lst = new LinkedList<>();

    for(Point p : map.getAllPoints())
    {
      if (map.get(p) =='#')
      if (marked.get(p) == '.')
      {
        Map2D<Character> box = new Map2D<Character>('.');

        LinkedList<Point> queue = new LinkedList<>();
        queue.add(p);

        while(queue.size() > 0)
        {
          Point a = queue.pop();

          if (map.get(a) == '#')
          if (marked.get(a) == '.')
          {
            box.set(a, '#');
            marked.set(a, 'X');

            for(Point b : map.getAdj(a, false))
            {
              queue.add(b);
            }
          }
        }
        lst.add(box);

        Point corner=p;
      }
    }
    return lst;
  }


  HashMap<String, Integer> memo=new HashMap<>();

  public int getCover(Map2D<Character> box)
  {
    Map<Character, Long> counts = box.getCounts();

    if (!counts.containsKey('#')) return 0;
    if (counts.get('#')==1) return 1;

    box = box.reduce();

    String key = box.getHashState();
    synchronized(memo)
    {
      if (memo.containsKey(key)) return memo.get(key);
    }
    
    //box.print();

    int low = 1000000000;

    TreeMap<Double, Map2D<Character>> subs = new TreeMap<>();
    
    for(Point p : box.getAllPoints())
    {
      if ((box.get(p) == '#') && (box.get(p.add(Nav.W))=='.'))
      {
        Map2D<Character> b_sub = box.copy();
        int cnt = 0;
        while(box.get(p.add(new Point(cnt, 0)))=='#')
        {
          b_sub.set(p.add(new Point(cnt, 0)),'.');
          cnt++;
        }
        //if (isConnected(b_sub))
        {
          double  rem = 0;
          if (b_sub.getCounts().containsKey('#'))
          {
            long rl = b_sub.getCounts().get('#');
            rem = rl;
          }
          subs.put(rem + rnd.nextDouble(), b_sub);
          //low = Math.min(low, lmagic(b_sub) + 1);
        }

      }
      if ((box.get(p) == '#') && (box.get(p.add(Nav.N))=='.'))
      {
        Map2D<Character> b_sub = box.copy();
        int cnt = 0;
        while(box.get(p.add(new Point(0, cnt)))=='#')
        {
          b_sub.set(p.add(new Point(0, cnt)),'.');
          cnt++;
        }

        //if (isConnected(b_sub))
        {
          double rem = 0;
          if (b_sub.getCounts().containsKey('#'))
          {
            long rl = b_sub.getCounts().get('#');
            rem = rl;
          }
          subs.put(rem + rnd.nextDouble(), b_sub);

          //low = Math.min(low, lmagic(b_sub) + 1);
        }

      }
    }

 
    int branch=3;
    for(Map2D<Character> b_sub : subs.values())
    {
      low = Math.min( lmagic(b_sub) + 1, low);
      branch--;
      if (branch<=0) break;
    }
    synchronized(memo)
    {
      memo.put(key, low);
    }
    return low;

  }

  public boolean isConnected(Map2D<Character> box)
  {
    TreeSet<Point> queue = new TreeSet<Point>();
    if (!box.getCounts().containsKey('#')) return true;

    for(Point p : box.getAllPoints())
    {
      if (box.get(p)=='#')
      {
        queue.add(p);
        break;
      }
    }
    TreeSet<Point> pts = new TreeSet<>();

    while(queue.size() > 0)
    {
      Point a = queue.pollFirst();
      if (box.get(a) == '#')
      if (!pts.contains(a))
      {
        pts.add(a);

        for(Point b : box.getAdj(a, false))
        {
          queue.add(b);
        }
      }

    }
    if (pts.size() == box.getCounts().get('#')) return true;

    return false;

  }

  public String Part1(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    List<Box> box_list = scanBox1(map);

    long p = 0;

    for(Box b : box_list)
    {   
      p+= Math.min(b.size.x, b.size.y);
    }


    return "" + p;
  }

  public int pickAt(Map2D<Character> map)
  {
    for(Point p : map.getAllPoints())
    if (map.get(p) == '#')
    {
      int edges = 0;
      for(Point a : map.getAdj(p, false))
      {
        if (map.get(a) == '#') edges++;
      }
      if (edges == 0)
      {
        map.set(p,'.');
        return 1;
      }

      if (edges == 2)
      {
        

      }

    }

    return 0;

  }

  int lmagic(Map2D<Character> box)
  {
    box = box.reduce();
    Map<Character, Long> counts = box.getCounts();

    // Rotate around until the gap is top left
    if (!counts.containsKey('#')) return 0;
    for(int i=0; i<4; i++)
    {
      if (box.get(0,0) == '.') break;
      box = box.rotateR().reduce();
    }


    // Filled box case, easy
    int b_x = (int) (box.high_x+1);
    int b_y =(int) (box.high_y+1);
    if (counts.get('#') == b_x*b_y)
    {
      return Math.min(b_x,b_y);
    }

    // See how big of a void there might be
    int void_x = 0;
    int void_y = 0;
    while(box.get(void_x, 0) == '.') void_x++;
    while(box.get(0, void_y) == '.') void_y++;


    // ok, so we have an L shape, just do some mins for that
    if (counts.get('#') == (b_x*b_y) - (void_x * void_y))
    {

      int low = b_x;
      low = Math.min(low, b_y);
      low = Math.min(low, b_x - void_x + b_y - void_y);
      return low;
    }
    else
    {

      // Check for box void in a big box
      long low_x_v=1000000; 
      long low_y_v=1000000;
      long high_x_v=0;
      long high_y_v=0;

      for(long x=0; x<=box.high_x; x++)
      for(long y=0; y<=box.high_y; y++)
      {
        if (box.get(x,y)=='.')
        {
          low_x_v = Math.min( low_x_v, x);
          low_y_v = Math.min( low_y_v, y);
          high_x_v = Math.max( high_x_v, x);
          high_y_v = Math.max( high_y_v, y);

        }

      }

      boolean box_hole=true;
      for(long x = low_x_v; x<=high_x_v; x++)
      for(long y = low_y_v; y<=high_y_v; y++)
      {
        if (box.get(x,y) != '.') box_hole=false;

      }

      if (box_hole)
      {
        // ok, we have a big box.  Cool.
        int hole_x = (int) (high_x_v - low_x_v +1L);
        int hole_y = (int) (high_y_v - low_y_v+1L);

        // take off top and bottom
        int low = 1000000;
        {
          int lines = b_y - hole_y;
          int left_x = (int) low_x_v;
          int left_y = hole_y;
          int right_x = (int) (box.high_x - high_x_v);
          int right_y = hole_y;
          low = Math.min(low, lines + Math.min(left_x, left_y) + Math.min(right_x, right_y));

        }

        // remove sides
        {
          int lines = b_x - hole_x;

          int top_x = hole_x;
          int top_y = (int) low_y_v;
          int bot_x = hole_x;
          int bot_y = (int) (box.high_y - high_y_v);

          low = Math.min(low, lines + Math.min( top_x, top_y) + Math.min(bot_x, bot_y));

        }


        return low;
      }

      // We have some other damn thing, do it the hard way
      // FUN FACT - this occurs in the samples but not in my test data
      // HAHAHAHAHAHHAHAHAHAHAHAHAHAHAHAHHAHAHAHAHAHAHAHAHAHHAHAHAHAHAHAAHAHAHAHHAHAHAHAHAHAHAHAHHAHAHAHAHAHAHHAHAHAHAAHAHAHAAH

      //System.out.println("odd:");
      //box.print();

      return getCover(box);

    }

  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);


    long p = 0;
    List<Map2D<Character> > box_list = scanBox2(map);
    for(Map2D<Character> box : box_list)
    {
      //box.print();
      p+=lmagic(box);
      //p += getCover(box);
    }

    //while(map.getCounts().containsKey('#'))
    //{
    //  p += pickAt(map);
    //}

    return "" + p;

  }
  public String Part3(Scanner scan)
  {
    return Part2(scan);
  }
}
