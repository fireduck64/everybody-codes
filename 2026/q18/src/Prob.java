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

  ArrayList<Point> readPoints(Scanner scan)
  {
    ArrayList<Point> lst = new ArrayList<>();

    while(scan.hasNextLine())
    {
      List<Integer> vals = Tok.ent(scan.nextLine(), " =");
      if (vals.size() == 2)
      {
        Point a = new Point(vals.get(0), vals.get(1));
        lst.add(a);
      }
      if (vals.size() == 3)
      {
        Point a = new Point(vals.get(0), vals.get(1), vals.get(2));
        lst.add(a);
      }

    }
    return lst;

  }

  public boolean isBound(Point m)
  {
    boolean low=false;
    boolean high=false;

    for(long x=0; x<=map.high_x; x++)
    {
      if (map.get(x, m.y) =='#')
      {
        if (x < m.x) low=true;
        if (x > m.x) high=true;

        if (low)
        if (high)
          return true;
      }

    }

    if (low)
    if (high)
      return true;

    return false;

  }
  Map2D<Character> map = new Map2D<Character>('.');

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>('.');

    List<Point> input = readPoints(scan);
    System.out.println(input);

    for(Point p : input)
    {
      for(long y=1; y<=p.y; y++)
      {
        map.set(p.x, y, '#');
      }
    }
    for(long x = 0; x<=map.high_x; x++)
    for(long y = 1; y<=map.high_y; y++)
    {
      Point p = new Point(x,y);
      if (map.get(p) == '.')
      if (isBound(p))
      {
        map.set(p, '=');
      }

    }

    map.print();


    long sum = map.getCounts().get('#');
    sum += map.getCounts().get('=');

    return "" + sum;
  }

  public String Part2(Scanner scan)
  {
    map = new Map2D<Character>('.');

    List<Point> input = readPoints(scan);

    // Maps y to x
    TreeMap<Long, Long> low_x=new TreeMap<>();
    TreeMap<Long, Long> high_x=new TreeMap<>();


    for(Point p : input)
    {

      for(long w=0; w<p.z; w++)
      for(long y=1; y<=p.y; y++)
      {
        Point a = new Point(p.x+w,y);
        if (!low_x.containsKey(a.y)) low_x.put(a.y, a.x);
        if (!high_x.containsKey(a.y)) high_x.put(a.y, a.x);

        low_x.put(a.y, Math.min(a.x, low_x.get(a.y)));
        high_x.put(a.y, Math.max(a.x, high_x.get(a.y)));

        map.set(a, '#');
      }
      
    }
    for(long x = 0; x<=map.high_x; x++)
    for(long y = 1; y<=map.high_y; y++)
    {
      Point p = new Point(x,y);
      if (map.get(p) == '.')
      if (low_x.containsKey(p.y))
      if (high_x.containsKey(p.y))
      if (low_x.get(p.y) < p.x)
      if (high_x.get(p.y) > p.x)
      {
        map.set(p, '=');
      }

    }

    //map.print();


    long sum = map.getCounts().get('#');
    sum += map.getCounts().get('=');

    return "" + sum;
  }
  public String Part3(Scanner scan)
  {
    ArrayList<Point> input = readPoints(scan);
    TreeSet<Long> all_x=new TreeSet<>();
    TreeSet<Long> all_y=new TreeSet<>();
    TreeMap<Long, Point> walls = new TreeMap<>();
    all_y.add(0L);

    

    for(Point p : input)
    {
      walls.put(p.x, p);
      all_x.add(p.x);
      all_y.add(p.y);
      all_x.add(p.x + p.z);
    }

    TreeMap<Long, Long> low_x=new TreeMap<>();
    TreeMap<Long, Long> high_x=new TreeMap<>();

    for(long y : all_y)
    {
      long low = 1000000000L;
      long high = 0L;
      for(Point p : input)
      {
        if (p.y > y)
        {
          low = Math.min(p.x, low);
          high = Math.max(p.x + p.z, high);
        }
      }
      low_x.put(y,low);
      high_x.put(y,high);
    }
    
    System.out.println( all_x.size() );
    System.out.println( all_y.size() );

    ArrayList<Long> x_lst = new ArrayList<>();
    ArrayList<Long> y_lst = new ArrayList<>();
    x_lst.addAll(all_x);
    y_lst.addAll(all_y);

    long sum = 0;

    for(int x_idx = 0; x_idx < x_lst.size() -1 ; x_idx++)
    for(int y_idx = 0; y_idx < y_lst.size() -1 ; y_idx++)
    {
      long x = x_lst.get(x_idx);
      long y = y_lst.get(y_idx);
      long area = (x_lst.get(x_idx+1) - x) * (y_lst.get(y_idx+1) - y);

      if (low_x.get(y) <= x)
      if (high_x.get(y) > x)
      {
        sum += area;
      }
    }

    return "" + sum;
  }
}
