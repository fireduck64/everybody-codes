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

  Map2D<Character> maze;
  Point fin;

  public String Part1(Scanner scan)
    throws Exception
  {
 
    maze = new Map2D<Character>('#');
    MapLoad.loadMap(maze, scan);

    maze.print();
    Point start = maze.getAllPoints('S').get(0);
    fin = maze.getAllPoints('E').get(0);
    SS ss = (SS) Search.search(new SS(0, start, 0, new TreeSet<Point>(), new LinkedList<Point>()));

    return "" + ss.cost;
  }

  public String Part2(Scanner scan)
    throws Exception
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {
    maze = new Map2D<Character>('#');
    MapLoad.loadMap(maze, scan);

    fin = maze.getAllPoints('E').get(0);

    LinkedList<State> starts = new LinkedList<>();
    for(Point p :  maze.getAllPoints('S'))
    {
      starts.add (new SS(0, p, 0, new TreeSet<Point>(), new LinkedList<Point>()));

    }

    SS ss = (SS) Search.searchM(starts);

    return "" + ss.cost;

  }

  public class SS extends State
  {
    int cost;
    Point loc;
    int height;
    TreeSet<Point> used;
    LinkedList<Point> trail;

    public SS(int cost, Point loc, int height, TreeSet<Point> used, LinkedList<Point> trail)
    {
      this.cost = cost;
      this.loc = loc;
      this.height = height;
      this.used = used;
      this.trail = trail;

      while (this.trail.size() > 6)
      {
        this.trail.pollFirst();

      }

    }

    public String toString()
    {
      //return "" + loc + " " + height + " " + used;
      return "" + loc + " " + height;
    }

    public boolean isTerm()
    {
      if (height == 0)
      if (maze.get(loc) == 'E')
        return true;
      return false;
    }

    public double getCost()
    {
      return cost;
    }   

    @Override
    public double getEstimate()
    {
      //return 0;

      return Math.min(height, 10-height) + loc.getDistM(fin);
    }

    public List<State> next()
    {
      LinkedList<State> lst = new LinkedList<>();

  
      if (height == 0)
      {
        lst.add(new SS(cost+1, loc, (height + 1), used, trail));
        lst.add(new SS(cost+1, loc, 9, used, trail));

      }
      else if (height == 9)
      {
        lst.add(new SS(cost+1, loc, 0, used, trail));
        lst.add(new SS(cost+1, loc, 8, used, trail));
      }
      else
      {
        lst.add(new SS(cost+1, loc, height+1, used, trail));
        lst.add(new SS(cost+1, loc, height-1, used, trail));
      }

      for(Point n : maze.getAdj(loc, false))
      {
        char z = maze.get(n);
        if (z != '#')
        {
          int target_h = -9;
          if (z == 'E') target_h =0;
          if (z == 'S') target_h =0;
          if (z >= '0') 
          if (z <= '9')
            target_h = Integer.parseInt("" + z);

          if (height == target_h)
          {
            TreeSet<Point> nu = new TreeSet<>();
            nu.addAll(used);
            nu.add(loc);

            LinkedList<Point> nt = new LinkedList<>();
            nt.addAll(trail);
            nt.add(loc);
            lst.add(new SS(cost+1, n, height, nu, nt));
          }

        }

      }

      return lst;


    }

  }
}
