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

  public void test()
    throws Exception
  {
    Map2D<Character> test = new Map2D<Character>(' ');
    MapLoad.loadMap(test, new Scanner(new FileInputStream("test.p1")));

    test.print();

    test = rotate(test, 'L', new Point(1,1));
    test.print();

  }

  public Map2D<Character> decode(Map2D<Character> map, String plan)
  {
    int plan_idx = 0;
    for(long y=map.low_y; y<=map.high_y; y++)
    for(long x=map.low_x; x<=map.high_x; x++)
    {
      if (x > map.low_x)
      if (x < map.high_x)
      if (y > map.low_y)
      if (y < map.high_y)
      {
        char dir = plan.charAt(plan_idx);
        map = rotate(map, dir, new Point(x,y));
        //map.set(new Point(x,y), dir);

        plan_idx++;
        if (plan_idx >= plan.length()) plan_idx -= plan.length();
      }
    }
    return map;

  }

  public Map2D<Point> decodeL(Map2D<Point> map, String plan)
  {
    int plan_idx = 0;
    for(long y=map.low_y; y<=map.high_y; y++)
    for(long x=map.low_x; x<=map.high_x; x++)
    {
      if (x > map.low_x)
      if (x < map.high_x)
      if (y > map.low_y)
      if (y < map.high_y)
      {
        char dir = plan.charAt(plan_idx);
        map = rotateL(map, dir, new Point(x,y));
        //map.set(new Point(x,y), dir);

        plan_idx++;
        if (plan_idx >= plan.length()) plan_idx -= plan.length();
      }
    }
    return map;

  }


  public String Part1(Scanner scan)
    throws Exception
  {
    String plan = scan.nextLine();
    scan.nextLine();

    Map2D<Character> map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    map = decode(map, plan);


    map.print();
    return "";
  }

  public String Part2(Scanner scan)
  {
    String plan = scan.nextLine();
    scan.nextLine();

    Map2D<Character> map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    System.out.println("Plan: " + plan);

    for(int i=0; i<100; i++)
    {
      map = decode(map, plan);
      System.out.print('.');
    }
    System.out.println();


    map.print();
    return "";
  }
  public String Part3(Scanner scan)
  {
    String plan = scan.nextLine();
    scan.nextLine();

    Map2D<Character> map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);

    long steps = 0;
    long target = 1024L;
    //long target = 1048576000L;

    Map2D<Point> pmap = new Map2D<Point>(new Point(0,0));
    for(Point p : map.getAllPoints())
    {
      pmap.set(p, p);
    }
    pmap = resolveMap(plan, pmap, target);

    Map2D<Character> fin = new Map2D<Character>(' ');
    for(Point p : map.getAllPoints())
    {
      fin.set(p, map.get(pmap.get(p)));

    }
    fin.print();

    //E.er();

    /*
    HashMap<String, Long> memo = new HashMap<>(1024,0.5f);
    memo.put( map.getHashState(), steps);
    while(steps < target)
    {
      map = decode(map, plan);
      steps++;
      System.out.print('.');

      String state = map.getHashState();
      if (memo.containsKey(state))
      {
        long diff = steps - memo.get(state);
        long mult = (target - steps) / diff;
        System.out.println();

        System.out.println("Step " + steps + " " + diff);

        steps += diff * mult;

      }
      else
      {
        memo.put(state, steps);
      }

    }
    System.out.println();

    map.print();*/

    return "";
  }

  Map2D<Point> resolveMap(String plan, Map2D<Point> map, long target)
  {
    Map2D<Point> in = map.copy();

    long steps=0;
    for(int i=0; i<16; i++)
    {
      map = decodeL(map, plan);
      System.out.print(".");
      steps++;
    }
    
    while(steps < target)
    {
      // The idea hear is we apply the same mapping twice, which is the 
      // same as doubling the steps
      Map2D<Point> m2 = new Map2D<Point>(new Point(0,0));
      for(Point p : map.getAllPoints())
      {
        Point a = map.get(p);
        Point c = map.get(a);

        // I had a real hard time reasoning about which of these was correct
        // I ended up testing with the p2 input and then trying each one of these
        //m2.set(c,a);
        m2.set(c,p);
        //m2.set(a,c);
        //m2.set(a,p);
        //m2.set(p,a);
        //m2.set(p,c);

      }
      steps=steps*2;
      map = m2;
    }
    if (steps != target) E.er();

    return map;




  }
  
  List<Point> circle = ImmutableList.of(
      new Point(-1, -1),
      new Point(0, -1),
      new Point(1, -1),
      new Point(1, 0),
      new Point(1, 1),
      new Point(0, 1),
      new Point(-1, 1),
      new Point(-1, 0));


  public Map2D<Point> rotateL(Map2D<Point> in, char dir, Point center)
  {
    Map2D<Point> in_copy = new Map2D<Point>(new Point(0,0));
    for(Point p : circle)
    {
      Point z = center.add(p);
      in_copy.set(z, in.get(z));

    }

    Map2D<Point> out = in;

    int delta = 0;
    if (dir == 'R') delta=1;
    if (dir == 'L') delta=-1;

    for(int n =0; n<circle.size(); n++)
    {
      Point src = circle.get(n);
      int d_idx = n + delta;
      if (d_idx >= circle.size()) d_idx -= circle.size();
      if (d_idx < 0) d_idx += circle.size();

      Point dst = circle.get(d_idx);

      out.set( center.add(dst), in_copy.get(center.add(src)));



    }

    return out;


  }
  public Map2D<Character> rotate(Map2D<Character> in, char dir, Point center)
  {
    Map2D<Character> in_copy = new Map2D<Character>(' ');
    for(Point p : circle)
    {
      Point z = center.add(p);
      in_copy.set(z, in.get(z));

    }

    Map2D<Character> out = in;

    int delta = 0;
    if (dir == 'R') delta=1;
    if (dir == 'L') delta=-1;

    for(int n =0; n<circle.size(); n++)
    {
      Point src = circle.get(n);
      int d_idx = n + delta;
      if (d_idx >= circle.size()) d_idx -= circle.size();
      if (d_idx < 0) d_idx += circle.size();

      Point dst = circle.get(d_idx);

      out.set( center.add(dst), in_copy.get(center.add(src)));



    }

    return out;





  }
}
