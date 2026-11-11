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

  int countFloodTime(Map2D<Character> map)
  {
    TreeSet<Point> palms = new TreeSet<>();
    palms.addAll(map.getAllPoints('P'));
    Map2D<Character> flood_map = map.copy();

    int time = 0;
    for(Point p : findStart(flood_map))
    {
      flood_map.set(p, '~');
    }
    while(true)
    {
      int flood_count = 0;
      for(Point p : palms)
      {
        if (flood_map.get(p) == '~') flood_count++;

      }
      if (flood_count == palms.size()) return time;

      time++;
      flood_map = flood(flood_map);
    }

  }

  Map2D<Character> flood(Map2D<Character> in)
  {
    Map2D<Character> out = in.copy();

    for(Point p : in.getAllPoints('~'))
    {
      for(Point q : in.getAdj(p, false))
      {
        if ((in.get(q) == '.') || (in.get(q) == 'P'))
        {
          out.set(q, '~');

        }


      }

    }
    return out;


  }
  List<Point> findStart(Map2D<Character> map)
  {
    LinkedList<Point> starts = new LinkedList<>();
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == '.')
      {
        if (p.x == map.low_x) starts.add(p);
        if (p.x == map.high_x) starts.add(p);
        if (p.y == map.low_y) starts.add(p);
        if (p.y == map.high_y) starts.add(p);

      }

    }
    return starts;

  }

  public String Part1(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('#');

    MapLoad.loadMap(map, scan);

    return "" + countFloodTime(map);
  }

  public String Part2(Scanner scan)
  {
    
    return Part1(scan);
  }
  Map2D<Long> time_map;
  Map2D<Character> p3_map;
  public String Part3(Scanner scan)
  {
    time_map = new Map2D<Long>(0L);
    Map2D<Character> map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);
    p3_map = map.copy();

    //map.print();
    //p3_map.print();

    for(Point p : map.getAllPoints('P'))
    {
      System.out.print('p');
      Search.search(new FF(p, 0));
    }
    System.out.println();
    LinkedList<Long> vals = new LinkedList<>();
    for(Point p : time_map.getAllPoints())
    {
      vals.add(time_map.get(p));
    }
    //System.out.println(vals);
    Collections.sort(vals);
    return "" + vals.get(0);
  }

  public class FF extends Flood
  {
    Point loc;
    int cost;

    public FF(Point loc, int cost)
    {
      this.loc = loc;
      this.cost = cost;

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
      //System.out.println("FF "  + toString());
      LinkedList<State> lst = new LinkedList<>();
      if (p3_map.get(loc) == '.')
      {
        time_map.set(loc, time_map.get(loc) + cost);
      }
      for(Point p : p3_map.getAdj(loc, false))
      {
        if ((p3_map.get(p) =='.') || (p3_map.get(p) == 'P'))
        {
          lst.add(new FF(p, cost+1));
        }
      }

      return lst;



    }
  }
}
