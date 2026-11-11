import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableMap;

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
      //System.out.println(Part2(new Scanner(new FileInputStream("input.p2"))));
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


  Map2D<Character> map;
  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);
    map.print();

    TreeSet<Character> launchers = new TreeSet<>();
    launchers.addAll( map.getCounts().keySet());
    launchers.remove('T');
    launchers.remove('.');
    launchers.remove('=');

    System.out.println("Launchers: " + launchers);

    TreeMap<Point, Integer> targets = new TreeMap<>();

    for(Point p : map.getAllPoints('T'))
    {
      targets.put(p, 1);

    }
    System.out.println("Targets: " + targets);


    long score =getSolution(launchers, targets);


    return "" + score;
  }

  // Returns -1 on no solution or score on solution
  public long getSolution(TreeSet<Character> rem_launchers, TreeMap<Point, Integer> rem_targets)
  {
    // If we are out of targets, good solution
    if (rem_targets.size() == 0) return 0;

    // If we have targets but no launchers, bad solution
    if (rem_launchers.size() == 0) return -1;

    for(Character launcher : rem_launchers)
    {
      Point start = map.getAllPoints(launcher).get(0);

      for(int p=0; p<1000; p++)
      {
        Point hit = checkShot(start, p, rem_targets.keySet());
        if (hit != null)
        if (!rem_targets.containsKey(hit.add(new Point(0, -1))))
        {
          //System.out.println("Hit : " + launcher + " " + p);
          long score = (launcher - 'A' + 1) * p;
          TreeSet<Character> rem_launchers2 = new TreeSet<>();
          rem_launchers2.addAll(rem_launchers);
          //rem_launchers2.remove(launcher);

          TreeMap<Point,Integer> rem_targets2 = new TreeMap<>();
          rem_targets2.putAll( rem_targets );
          rem_targets2.put(hit, rem_targets2.get(hit) - 1);
          if (rem_targets2.get(hit) == 0)
            rem_targets2.remove(hit);
          long sub_score = getSolution(rem_launchers2, rem_targets2);

          if (sub_score >= 0) return score+sub_score;
        }
      }
    }

    return -1L;


  }

  public Point checkShot(Point start, int power, Set<Point> rem_targets)
  {
    //System.out.println("Shot " + start + " " + power);
    Point cur = start;

    // Assent
    for(int i=0; i<power; i++)
    {
      cur = cur.add(new Point(1,-1));
      int check = isClear(cur, rem_targets);
      if (check == -1) return null;
      if (check == 1) return cur;
    }

    // Horizontal
    for(int i=0; i<power; i++)
    {
      cur = cur.add(new Point(1,0));
      int check = isClear(cur, rem_targets);
      if (check == -1) return null;
      if (check == 1) return cur;
    }
    // Down
    while(true)
    {
      cur = cur.add(new Point(1,1));
      int check = isClear(cur, rem_targets);
      if (check == -1) return null;
      if (check == 1) return cur;
    }

  }

  // -1 - out of bounds, hit non-target
  // 0 - clear
  // 1 - hit target
  public int isClear(Point p, Set<Point> rem_targets)
  {
    //System.out.println(p);
    if (p.y > map.high_y) return -1;

    char z = map.get(p);
    //System.out.println("point : " + p + " " + z);
    if (z == '.') return 0;
    if ((z == 'T') || (z =='H'))
    {
      if (rem_targets.contains(p)) return 1;
      return 0;
    }
    return -1;

  }



  public String Part2(Scanner scan)
  {
    map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);
    map.print();

    TreeSet<Character> launchers = new TreeSet<>();
    launchers.addAll( map.getCounts().keySet());
    launchers.remove('T');
    launchers.remove('H');
    launchers.remove('.');
    launchers.remove('=');

    System.out.println("Launchers: " + launchers);

    TreeMap<Point, Integer> targets = new TreeMap<>();

    for(Point p : map.getAllPoints('T'))
    {
      targets.put(p, 1);
    }
    for(Point p : map.getAllPoints('H'))
    {
      targets.put(p, 2);
    }

    //System.out.println("Targets: " + targets);


    long score =getSolution(launchers, targets);


    return "" + score;

  }


  Map2D<Map<Integer, Integer> > intercept = new Map2D<>(ImmutableMap.of());
  Map2D<Character> meat_map = new Map2D<>('.');

  public String Part3(Scanner scan)
  {
    map = new Map2D<Character>('.');
    map.set(0,0,'A');
    map.set(0,1,'B');
    map.set(0,2,'C');

    map.print();
    TreeSet<Character> launchers = new TreeSet<>();
    launchers.addAll( map.getCounts().keySet());
    launchers.remove('T');
    launchers.remove('.');
    launchers.remove('=');

    System.out.println("Launchers: " + launchers);


    LinkedList<Point> meat = new LinkedList<>();
    int high_x = 0;
    System.out.println("Painting meat");
    while(scan.hasNextInt())
    {
      int x = scan.nextInt();
      int y = scan.nextInt();
      Point p = new Point(x,y);
      meat.add(p);

      high_x = Math.max(high_x, x);


      paintMeat(p);

    }

    System.out.println("Painting launchers");
    for(char z : launchers)
    {
      Point start = map.getAllPoints(z).get(0);
      for(int p=1; p<5000; p++)
      {
        paintLauncher(z, start, p, high_x);
      }
    }

    //intercept.print();

    long score = 0L;

    for(Point m : meat)
    {
      long s = plotIntercept(m);
      System.out.println("" + m + " " + s);
      score += s;
    }


    return "" + score;
  }

  public long plotIntercept(Point meat)
  {
    int t = 0;
    Point cur = meat;
    while(true)
    {
      cur = cur.add(new Point(-1,-1));
      t++;
      //System.out.println("Meat: " + cur + " t" + t);

      if (intercept.get(cur).size() > 0)
      {
        Map<Integer, Integer> m = intercept.get(cur);
        int best = 0;

        for(int it : m.keySet())
        {
          if (it <= t)
          {
            if ((best == 0) || (best > m.get(it)))
            {
              best = m.get(it);
            }

          }

        }
        if (best > 0) 
        {
          System.out.println("Intercept at : " + cur + " t" + t + " " + m);
          return best;
        }

      }

      if (cur.y <= 0) E.er();
    }

  }

  public void paintMeat(Point start)
  {
    Point cur = start;
    while(true)
    {
      meat_map.set(cur, '#');
      cur = cur.add(new Point(-1,-1));
      if (cur.y < 0) return;
    }

  }

  public void paintL(int score, Point p, int t)
  {
    //System.out.println("Paint: " + score + " " + p + " " + t);
    if (meat_map.get(p) == '#')
    {
      if (!(intercept.get(p) instanceof TreeMap))
      {
        intercept.set(p, new TreeMap<Integer, Integer>());
      }

      TreeMap<Integer, Integer> m = (TreeMap<Integer, Integer> )intercept.get(p);
      if (!m.containsKey(t))
      {
        m.put(t,score);
      }
      else
      {
        m.put(t, Math.min(score, m.get(t)));
      }
      //System.out.println("  " + p + " " + m);

    }

  }

  public void paintLauncher(char launcher, Point start, int power, int high_x)
  {
    Point cur = start;
    int t = 0;
    int score = (launcher - 'A' + 1) * power;
    //System.out.println("Score: " + score + " " + launcher + " " + power);

    // Assent
    for(int i=0; i<power; i++)
    {
      cur = cur.add(new Point(1,1));
      t++;
      if (cur.x > high_x) return;
      paintL(score, cur, t);
    }

    // Horizontal
    for(int i=0; i<power; i++)
    {
      cur = cur.add(new Point(1,0));
      t++;
      if (cur.x > high_x) return;
      paintL(score, cur, t);
    }
    // Down
    while(true)
    {
      cur = cur.add(new Point(1,-1));
      t++;
      if (cur.x > high_x) return;
      paintL(score, cur, t);
    }
   

  }
}
