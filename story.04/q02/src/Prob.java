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
    TreeMap<String, Point> locs = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      if (line.startsWith("MOVES="))
      {
        String moves = Tok.en(line, "=").get(1);

        Map2D<Boolean> m = p1Visit(locs, moves);


        return "" + m.getCounts().get(true);

      }
      else
      {
        String name = Tok.en(line, "=").get(0);
        String s = Tok.en(line, "=").get(1);
        s = s.replace("[","").replace("]","");
        List<Integer> lst = Tok.ent(s, ",");
        Point p = new Point(lst.get(0), lst.get(1));

        locs.put(name, p);

      }

    }

    return "";
  }

  Map2D<Boolean> p1Visit(TreeMap<String, Point> locs, String moves)
  {
    Point cur = locs.get("START");
    Map2D<Boolean> m = new Map2D<>(false);
    for(char z : Str.stolist(moves))
    {
      m.set(cur, true);
      String name = "" + z;
      Point dst = locs.get(name);

      cur = getMid(cur, dst);
      m.set(cur, true);

    }
    return m;

  }

  Map2D<Character> p2Visit(TreeMap<String, Point> locs, String moves)
  {
    Point cur = locs.get("START");
    Map2D<Character> m = new Map2D<>(' ');
    for(char z : Str.stolist(moves))
    {
      for(Point a : m.getAdj(cur, false)) if (m.get(a)==' ') m.set(a, 'F');
      m.set(cur, 'X');
      String name = "" + z;
      Point dst = locs.get(name);

      cur = getMid(cur, dst);
      //m.set(cur, true);
      for(Point a : m.getAdj(cur, false)) if (m.get(a)==' ') m.set(a, 'F');
      m.set(cur, 'X');

    }
    return m;

  }

  HashMap<String, Score> memo=new HashMap<>();

  Score p3Rec(TreeSet<Point> moves, Map2D<Character> m, Point cur)
  {

    for(Point a : m.getAdj(cur, false)) if (m.get(a)==' ') m.set(a, 'F');
    m.set(cur, 'X');

    String state = "" + cur + "" + m.getHashState() + " " + moves;
    if (memo.containsKey(state)) return memo.get(state);
    //System.out.println(state);
    //System.out.println(memo.size());

    Score best = new Score(m);
    for(Point move : moves)
    {
      Point mid = getMid(cur, move);
      char z = m.get(mid);
      if (z != 'X')
      {
        Score s2 = p3Rec(moves, m.copy(), mid);
        if (s2.total > best.total) best = s2;
      }
    }

    memo.put(state, best);
    return best;

  }

  public class Score
  {
    public long total;
    public long flies;
    public Score(long total, long flies)
    {
      this.total = total;
      this.flies = flies;

    }
    public Score(Map2D<Character> m)
    {
      Map<Character, Long> counts = m.getCounts();
      total =  counts.get('X') + counts.get('F');
      flies = counts.get('F');
    }

  }
  

  Score p3Visit(TreeMap<String, Point> locs)
  {
    Point start = locs.get("START");
    locs.remove("START");
    TreeSet<Point> orig = new TreeSet<>();
    orig.addAll(locs.values());

    TreeSet<Point> points = new TreeSet<>();
    points.add(start);

    while(true)
    {
      TreeSet<Point> add = new TreeSet<>();
      for(Point a : points)
      for(Point b : orig)
      {
        Point mid = getMid(a,b);
        if (!points.contains(mid)) add.add(mid);
      }
      if (add.size() == 0) break;

      points.addAll(add);
    }

    Map2D<Character> m = new Map2D<>(' ');

    for(Point cur : points)
    {
      for(Point a : m.getAdj(cur, false)) if (m.get(a)==' ') m.set(a, 'F');
      m.set(cur, 'X');
    }

    return new Score(m);

    //return p3Rec(orig, new Map2D<Character>(' '), cur);


  }



  public Point getMid(Point a, Point b)
  {
    long x = (a.x + b.x) / 2;
    long y = (a.y + b.y) / 2;

    return new Point(x,y);

  }

  public String Part2(Scanner scan)
  {
    TreeMap<String, Point> locs = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      if (line.startsWith("MOVES="))
      {
        String moves = Tok.en(line, "=").get(1);

        Map2D<Character> m = p2Visit(locs, moves);


        return "" + m.getCounts().get('F');

      }
      else
      {
        String name = Tok.en(line, "=").get(0);
        String s = Tok.en(line, "=").get(1);
        s = s.replace("[","").replace("]","");
        List<Integer> lst = Tok.ent(s, ",");
        Point p = new Point(lst.get(0), lst.get(1));

        locs.put(name, p);

      }

    }
    return "";
  }
  public String Part3(Scanner scan)
  {
    TreeMap<String, Point> locs = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
        String name = Tok.en(line, "=").get(0);
        String s = Tok.en(line, "=").get(1);
        s = s.replace("[","").replace("]","");
        List<Integer> lst = Tok.ent(s, ",");
        Point p = new Point(lst.get(0), lst.get(1));

        locs.put(name, p);
    }

    return "" + p3Visit(locs).flies;
  }
}
