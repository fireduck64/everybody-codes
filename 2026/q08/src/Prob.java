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

  public String Part1(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('.');
    Map2D<Integer> shadow_map = new Map2D<Integer>(0);
    MapLoad.loadMap(map, scan);

    Point sun_dir = new Point(1,1);

    updateShadowMap(map, shadow_map, sun_dir);


    return "" + countOpen(map, shadow_map);
  }

  public long countOpen(Map2D<Character> map, Map2D<Integer> shadow_map)
  {
    long cnt = 0;
    for(Point p : map.getAllPoints())
    if (map.get(p) != '.')
    {
      int val = Integer.parseInt("" + map.get(p));

      if (val > shadow_map.get(p)) cnt++;
    }


    return cnt;

  }

  public void updateShadowMap(Map2D<Character> map, Map2D<Integer> shadow_map, Point sun_dir)
  {
    for(Point p : map.getAllPoints())
    if (map.get(p) != '.')
    {
      char z = map.get(p);
      if ((z >= '0') && (z <= '9'))
      {
        int val = Integer.parseInt("" + map.get(p));

        Point cur = p.add(sun_dir);
        val-=1;

        while(val > 0)
        {
          shadow_map.set(cur, Math.max(shadow_map.get(cur), val));
          val-=1;
          cur = cur.add(sun_dir);

        }
      }

    }

  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    List<Point> suns = new LinkedList<Point>();
    suns.add(new Point(1,1));
    suns.add(new Point(-1,1));
    suns.add(new Point(1,-1));
    suns.add(new Point(-1,-1));
    suns.add(new Point(2,0));
    suns.add(new Point(-2,0));
    

    long p = 0;
    for(Point sun_dir : suns)
    {
      Map2D<Integer> shadow_map = new Map2D<Integer>(0);

      updateShadowMap(map, shadow_map, sun_dir);

      p += countOpen(map, shadow_map);

    }

    return "" + p;

  }
  public String Part3(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    List<Point> suns = new ArrayList<Point>();
    // in order this time
    suns.add(new Point(1,1));
    suns.add(new Point(-1,1));
    suns.add(new Point(-2,0));
    suns.add(new Point(-1,-1));
    suns.add(new Point(1,-1));
    suns.add(new Point(2,0));

    long energy=0;
    int round=0;
    warriors = 0;
    while(warriors < 100000L)
    {
      Point sun_dir = suns.get( round % 6); 
      Map2D<Integer> shadow_map = new Map2D<Integer>(0);
      updateShadowMap(map, shadow_map, sun_dir);
      energy += grow(map, shadow_map);      
      round++;

      //map.print();
    }

    System.out.println("Rounds: " + round);

    return "" + energy;
  }

  long warriors;

  public long grow(Map2D<Character> map, Map2D<Integer> shadow_map)
  {
    long cnt = 0;
    for(Point p : map.getAllPoints())
    if (map.get(p) != '.')
    {
      char z = map.get(p);
      if ((z >= '0') && (z <= '9'))
      {
        int val = Integer.parseInt("" + map.get(p));
        if (val > shadow_map.get(p))
        {
          cnt++;
          int nv = val + 1;
          if (nv == 10)
          {
            warriors++;
            map.set(p, 'A');
          }
          else
          {
            String n = "" + nv;
            map.set(p, n.charAt(0));
          }

        }
      }
      if (z == 'A') map.set(p, 'B');
      if (z == 'B') map.set(p, 'C');
      if (z == 'C') map.set(p, '1');
      if (z == 'D') map.set(p, '1');
    }

    return cnt;

  }


}
