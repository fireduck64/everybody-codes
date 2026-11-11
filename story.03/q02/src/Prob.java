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
    Map2D<Character> input = new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    Point start = null;
    for(Point p : input.getAllPoints())
    {
      if (input.get(p) == '@') start=p;
    }

    State cur = new State(start, Nav.N);
    int steps = 0;
    while(true)
    {
      cur = doStep1(input, cur);
      steps++;
      if (cur == null) break;
      
    }

    input.print();

    return "" + steps;
  }

  public class State
  {
    Point cur;
    Point dir;
    int action_idx;

    public State(Point cur, Point dir)
    {
      this.cur = cur;
      this.dir = dir;

    }
    public State(Point cur, int action_idx)
    {
      this.cur = cur;
      this.action_idx=action_idx;

    }
  }

  public State doStep1(Map2D<Character> m, State s)
  {
    Point dir = s.dir;
    for(int i=0; i<4; i++)
    {
      Point check = s.cur.add(dir);
      if (m.get(check) == '.')
      {
        m.set(check, '+');
        return new State(check, Nav.turnRight(dir));
      }
      if (m.get(check) == '#')
      {
        return null;
      }

      dir = Nav.turnRight(dir);
    }

    E.er("no dir");
    return null;


  }

  public State doStep2(Map2D<Character> m, State s)
  {
    Point dir = s.dir;
    for(int i=0; i<4; i++)
    {
      Point check = s.cur.add(dir);
      if (m.get(check) == '.')
      {
        m.set(check, '+');
        return new State(check, Nav.turnRight(dir));
      }

      dir = Nav.turnRight(dir);
    }

    m.print();

    E.er("no dir");
    return null;
  }

  public State doStep3(Map2D<Character> m, State s, List<Point> dirs)
  {
    int action_idx = s.action_idx;
    for(int i=0; i<dirs.size(); i++)
    {
      Point dir = dirs.get( action_idx % dirs.size());
      Point check = s.cur.add(dir);
      if (m.get(check) == '.')
      {
        m.set(check, '+');
        return new State(check, action_idx+1);
      }
      action_idx++;
    }

    m.set(s.cur, 'X');
    m.print();


    E.er("no dir");
    return null;
  }

  public boolean infill(Map2D<Character> m, Point cur)
  {
    TreeSet<Point> check = new TreeSet<>();

    for(Point p : m.getAllPoints())
    //if (m.get(p) == '+')
    if (m.get(p) != '.')
    for(Point q : m.getAdj(p,false))
    //for(Point q : m.getAdj(cur,false))
    {
      if (m.get(q)=='.') check.add(q);
    }

    boolean found=false;

    for(Point a : check)
    {
      int cnt = 0;
      for(Point b : m.getAdj(a,false))
      {
        if (m.get(b) != '.') cnt++;
      }
      if (cnt == 4)
      {
        m.set(a, '+');
        found=true;
      }
    }

    TreeSet<Point> flood_checked=new TreeSet<>();

    for(Point a : check)
    {
      if (flood_checked.contains(a)) continue;
      //System.out.println("Checking " + a);
      TreeSet<Point> inside=new TreeSet<>();
      TreeSet<Point> queue=new TreeSet<>();
      queue.add(a);

      boolean edge=false;

      while(queue.size() > 0)
      {
        Point b = queue.pollFirst();
        if (m.get(b) == '.')
        if (!inside.contains(b))
        {
          if (b.x < m.low_x) {edge=true; break;}
          if (b.y < m.low_y) {edge=true; break;}
          if (b.x > m.high_x) {edge=true; break;}
          if (b.y > m.high_y) {edge=true; break;}
          inside.add(b);
          flood_checked.add(b);
          queue.addAll( m.getAdj(b, false));
        }
      }
      if ((!edge) && (inside.size() > 0))
      {
        found=true;
        for(Point b : inside) m.set(b, '+');
      }

    }
    return found;


  }


  public String Part2(Scanner scan)
  {
    Map2D<Character> input = new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    Point start = null;
    LinkedList<Point> bones=new LinkedList<>();
    for(Point p : input.getAllPoints())
    {
      if (input.get(p) == '@') start=p;
      if (input.get(p) =='#') bones.add(p);
    }

    ArrayList<Point> dirs = new ArrayList<>();
    dirs.add(Nav.N);
    dirs.add(Nav.E);
    dirs.add(Nav.S);
    dirs.add(Nav.W);

    State cur = new State(start, 0);
    int steps = 0;
    while(true)
    {
      cur = doStep3(input, cur, dirs);
      steps++;
      //input.print();

      while(true)
      {
        if (!infill(input, cur.cur)) break;
      }

      int missing=0;

      for(Point a : bones)
      {
        for(Point b : input.getAdj(a,false))
        {
          if (input.get(b) == '.') missing++;

        }

      }
      if (missing == 0) break;
      
    }

    input.print();

    return "" + steps;

  }
  public String Part3(Scanner scan)
  {
    Map2D<Character> input = new Map2D<>('.');
    MapLoad.loadMap(input, scan);

    Point start = null;
    LinkedList<Point> bones=new LinkedList<>();
    for(Point p : input.getAllPoints())
    {
      if (input.get(p) == '@') start=p;
      if (input.get(p) =='#') bones.add(p);
    }

    State cur = new State(start, 0);
    int steps = 0;
    ArrayList<Point> dirs = new ArrayList<>();
    dirs.add(Nav.N);
    dirs.add(Nav.N);
    dirs.add(Nav.N);
    dirs.add(Nav.E);
    dirs.add(Nav.E);
    dirs.add(Nav.E);
    dirs.add(Nav.S);
    dirs.add(Nav.S);
    dirs.add(Nav.S);
    dirs.add(Nav.W);
    dirs.add(Nav.W);
    dirs.add(Nav.W);
    while(true)
    {
      cur = doStep3(input, cur, dirs);
      steps++;
      //input.print();

      while(true)
      {
        if (!infill(input,cur.cur)) break;
      }

      int missing=0;

      for(Point a : bones)
      {
        for(Point b : input.getAdj(a,false))
        {
          if (input.get(b) == '.') missing++;

        }

      }
      if (missing == 0) break;
      
    }

    input.print();

    return "" + steps;
  }
}
