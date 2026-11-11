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

  public String Part1(Scanner scan)
  {

    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    TreeSet<Point> points_to_include = new TreeSet<>();
    points_to_include.addAll( map.getAllPoints('*'));
    long dist = sumPrim(points_to_include);
    long p1 = dist + points_to_include.size();


    return "" + p1;
  }

  long sumPrim(TreeSet<Point> points_to_include)
  {
    long path_sum = 0;
    TreeSet<Point> included = new TreeSet<>();
    TreeSet<Point> not_yet = new TreeSet<>();
    not_yet.addAll(points_to_include);

    // Seed with first
    included.add(not_yet.pollFirst());

    while(not_yet.size() > 0)
    {
      long lowest = -1L;
      Point low = null;

      for(Point a : included)
      for(Point b : not_yet)
      {
        long dist = a.getDistM(b);
        if ((low == null) || (dist < lowest))
        {
          low = b;
          lowest = dist;

        }
      }
      if (low == null) E.er();
      path_sum += lowest;
      not_yet.remove(low);
      included.add(low);

    }

    return path_sum;


  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {

    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    TreeSet<Point> all_stars=new TreeSet<>();
    all_stars.addAll( map.getAllPoints('*'));

    LinkedList<Long> sizes = new LinkedList<>();

    while(all_stars.size() > 0)
    {
      Point start = all_stars.first();
      TreeSet<Point> cons = getConnected(map, start, all_stars);
      long score = cons.size() + sumPrim(cons);
      sizes.add(score);

      all_stars.removeAll(cons);

    }
    Collections.sort(sizes);
    long p =1;
    for(int i=0; i<3; i++)
    {
      p = p * sizes.pollLast();

    }
 
    return "" + p;
  }

  public TreeSet<Point> getConnected(Map2D<Character> map, Point start, Collection<Point> possible)
  {
    TreeSet<Point> conn = new TreeSet<>();

    conn.add(start);
    int changes = 1;
    while(changes>0)
    {
      changes=0;
      TreeSet<Point> to_add = new TreeSet<>();
      for(Point a : conn)
      for(Point b : possible)
      {
        long dist = a.getDistM(b);
        if (dist < 6L)
        if (!conn.contains(b))
        {
          changes++;
          to_add.add(b);
        }

      }
      conn.addAll(to_add);
    }


    return conn;

  }
}
