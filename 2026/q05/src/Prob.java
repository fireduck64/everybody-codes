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
    Map2D<Character> input =new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    long center_x = input.high_x / 2;
    long center_y = input.high_y / 2;

    input.set(center_x, center_y, '#');


    Map2D<Character> m = input;

    while(true)
    {
      Map2D<Character> n = expand(m);
      if (n == null) break;

      m = n;
    }

    setWater(m);
    //m.print();

    return "" + m.getCounts().get('#');
  }

  public void setWater(Map2D<Character> in)
  {
    for(Point p : in.getAllPoints())
    {
      if (in.get(p) != '#') in.set(p, '.');

    }

  }


  public Map2D<Character> expand(Map2D<Character> in)
  {
    Map2D<Character> out = in.copy();
    int changed=0;

    for(Point m : in.getAllPoints())
    {
      if (in.get(m) == '#')
      for(Point p : in.getAdj(m, true))
      {
        if ((in.get(p) == '0') || (in.get(p) == '1'))
        {
          int count = 0;
          for(Point q : in.getAdj(p, true))
          {
            if ((in.get(q)=='#') || (in.get(q) =='1'))
            {
              count++;
            }
          }
          if (count >= 5)
          {
            out.set(p, '#');
            changed++;
          }

        }

      }
    }
    if (changed > 0)
      return out;
    return null;

  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> input =new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    long center_x = input.high_x / 2;
    long center_y = input.high_y / 2;

    input.set(center_x, center_y, '#');

    Map2D<Character> m = input;

    while(true)
    {
      Map2D<Character> n = expand(m);
      if (n == null) break;

      m = n;
    }


    setWater(m);
    setSand(m);
    setTree(m);
    //m.print();

    return "" + m.getCounts().get('=');
  }

  public void setTree(Map2D<Character> in)
  {
    for(Point p : in.getAllPoints())
    if (in.get(p) == '#')
    {
      int good=0;
      for(Point a : in.getAdj(p, true))
      {
        if (checkSubTree(in, a)) good++;
      }

      if (good == 8)
      {
        in.set(p, '@');

      }

    }

    for(int i=0; i<2; i++)
    {
      TreeSet<Point> add = new TreeSet<>();
      for(Point a : in.getAllPoints())
      {
        if (in.get(a) == '#')
        {
          int count = 0;
          int sand = 0;
          for(Point b : in.getAdj(a, true))
          {
            if (in.get(b) == '@') count++;
            if (in.get(b) == '=') sand++;
          }
          if ((count >= 4) && (sand == 0))
          {
            //in.set(a, '@');
            add.add(a);
            //found=true;
          }

        }

      }
      for(Point a : add) in.set(a, '@');

    }

  }

  public boolean checkSubTree(Map2D<Character> in, Point a)
  {
    TreeSet<Point> lst = new TreeSet<>();
    lst.add(a);
    lst.addAll(in.getAdj(a, false));

    for(Point b : lst)
    {
      if (in.get(b) != '#')
      if (in.get(b) != '@')
        return false;

    }

    return true;


  }

  public void setSand(Map2D<Character> in)
  {
    for(Point p : in.getAllPoints())
    {
      if (in.get(p) == '#')
      for(Point a : in.getAdj(p,true))
      {
        if (in.get(a) == '.') in.set(p,'=');
      }
    }

    boolean found=true;
    while(found)
    {
      found=false;
      for(Point a : in.getAllPoints())
      {
        if (in.get(a) == '#')
        {
          int count = 0;
          for(Point b : in.getAdj(a, true))
          {
            if (in.get(b) == '=') count++;
          }
          if (count >= 5)
          {
            in.set(a, '=');
            found=true;
          }

        }

      }

    }

  }

  public String Part3(Scanner scan)
  {
    Map2D<Character> input =new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    long center_x = input.high_x / 2;
    long center_y = input.high_y / 2;

    input.set(center_x, center_y, '#');

    Map2D<Character> m = input;

    while(true)
    {
      Map2D<Character> n = expand(m);
      if (n == null) break;

      m = n;
    }


    setWater(m);
    setSand(m);
    setTree(m);
    m.print();

    return "" + m.getCounts().get('@');

  }
}
