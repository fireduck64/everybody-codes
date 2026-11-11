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

  TreeSet<Point> explode(Point start, Map2D<Character> map)
  {
    TreeSet<Point> lst = new TreeSet<>();

    TreeSet<Point> queue = new TreeSet<>();
    queue.add(start);

    while(queue.size() > 0)
    {
      Point p = queue.pollFirst();
      char z = map.get(p);
      lst.add(p);
      for(Point n : map.getAdj(p, false))
      {
        if (!queue.contains(n))
        if (!lst.contains(n))
        if (map.get(n) != ' ')
        if (map.get(n) != '.')
        if (z >= map.get(n))
        {
          queue.add(n);
        }

      }

    }

    return lst;

  }

  public String Part1(Scanner scan)
  {
    Map2D<Character> map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);

    return "" + explode(new Point(0,0), map).size();
  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);

    TreeSet<Point> bar = new TreeSet<>();

    bar.addAll(explode(new Point(0,0), map));
    bar.addAll(explode(new Point(map.high_x,map.high_y), map));

    return "" + bar.size();
  }
  public String Part3(Scanner scan)
  {

    Map2D<Character> map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);

    Map2D<Character> remaining = map.copy();
    TreeSet<Point> bar = new TreeSet<>();

    for(int i=0; i<3; i++)
    {
      TreeSet<Point> best = new TreeSet<>();
      
      for(Point p : map.getAllPoints())
      { 
        //System.out.println("" + i + " " + p);
        if (remaining.get(p) != '.')
        {
        TreeSet<Point> ex = explode(p, remaining);
        if (ex.size() > best.size())
        {
          best=ex;
        }
        }

      }
      bar.addAll(best);
      for(Point p : best)
      {
        remaining.set(p, '.');
      }

    }

    return "" + bar.size();
  }
}
