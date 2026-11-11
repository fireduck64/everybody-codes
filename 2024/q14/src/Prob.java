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

  public void drawPath(Point start, List<String> steps, Map3D<Character> tree)
  {
    drawPath(start, steps, tree, false);
  }

  public void drawPath(Point start, List<String> steps, Map3D<Character> tree, boolean last_leaf)
  {
    Point cur = start;

    for(String step : steps)
    {
      Point dir = Nav.getDir(step.charAt(0));
      int len = Integer.parseInt(step.substring(1));

      for(int i=0; i<len; i++)
      {
        cur = cur.add(dir);
        tree.set(cur, '#');
        //System.out.println(cur);
      }
    }
    if (last_leaf)
    {
      tree.set(cur, 'L');
    }

  }

  public int getDistance(Point a, Point b)
  {
    SS fin = (SS) Search.search(new SS(0, a, b));

    return fin.cost;
  }

  public class SS extends State
  {
    int cost;
    Point loc;
    Point dest;
    public SS(int cost, Point loc, Point dest)
    {
      this.cost = cost;
      this.loc = loc;
      this.dest = dest;

    }
    public boolean isTerm()
    {
      return (loc.equals(dest));

    }

    public String toString()
    {
      return loc +"/"+dest;
    }
    public double getCost()
    {
      return cost;
    }
    @Override
    public double getEstimate()
    {
      return loc.getDistM(dest);
    }
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      for(Point p : tree.getAdj(loc, false))
      {
        if (tree.get(p) != ' ')
        {
          lst.add(new SS(cost+1, p, dest));
        }
      }

      return lst;
    }

  }

  Map3D<Character> tree;

  public String Part1(Scanner scan)
  {
    List<String> steps = Tok.en(scan.nextLine(), ",");
    System.out.println(steps);
    tree = new Map3D<Character>(' ');

    drawPath(new Point(0,0,0), steps, tree);

    return "" + tree.high_y;
  }

  public String Part2(Scanner scan)
  {
    tree = new Map3D<Character>(' ');
   
    while(scan.hasNextLine())
    {
      List<String> steps = Tok.en(scan.nextLine(), ",");

      drawPath(new Point(0,0,0), steps, tree);
    }

    return "" + tree.getCounts().get('#');
  }
  public String Part3(Scanner scan)
  {
    tree = new Map3D<Character>(' ');
   
    while(scan.hasNextLine())
    {
      List<String> steps = Tok.en(scan.nextLine(), ",");

      drawPath(new Point(0,0,0), steps, tree, true);
    }
    LinkedList<Point> leafs = new LinkedList<>();
    for(Point p : tree.getAllPoints())
    {
      if (tree.get(p) == 'L') leafs.add(p);

    }

    int best = 1000000000;

    for(int y=1; y<= tree.high_y; y++)
    {
      System.out.println("y: " + y);
      Point main = new Point(0, y, 0);

      if (tree.get(main) != ' ')
      {
        int cost = 0;

        for(Point l : leafs)
        {
          cost += getDistance(main, l);
        }

        best = Math.min(best, cost);
        System.out.println(cost);
      }
      else
      {
        break;
      }

    }

    return "" + best;
  }
}
