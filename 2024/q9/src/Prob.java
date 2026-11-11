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

  List<Integer> stamps = ImmutableList.of(1, 3, 5, 10);

  public String Part1(Scanner scan)
  {
    stamps = ImmutableList.of(1, 3, 5, 10);
    long count = 0;
    while(scan.hasNextInt())
    {
      int v = scan.nextInt();

      SS fin = (SS) Search.search(new SS(v, 0));

      count += fin.cost;
    }

    return "" + count;
  }

  public String Part2(Scanner scan)
  {
    stamps = ImmutableList.of(1, 3, 5, 10, 15, 16, 20, 24, 25, 30);
    long count = 0;
    while(scan.hasNextInt())
    {
      int v = scan.nextInt();

      SS fin = (SS) Search.search(new SS(v, 0));

      count += fin.cost;
    }

    return "" + count;
 
  }

  public String Part3(Scanner scan)
  {
    stamps = ImmutableList.of(1, 3, 5, 10, 15, 16, 20, 24, 25, 30, 37, 38, 49, 50, 74, 75, 100, 101);
    long count = 0;

    Search.search(new FF(0, 0));
    System.out.println("Part 3");
    System.out.println("78275 " + getCost(78275));
    System.out.println("78213 " + getCost(78213));

    while(scan.hasNextInt())
    {
      int min_cost = 1000000000;
      int v = scan.nextInt();

      for(int a=v/2-100; a<v/2+100; a++)
      {
        int b = v - a;

        if (Math.abs(b-a) <= 100)
        {
          min_cost = Math.min(min_cost, getCost(a) + getCost(b));    
        }
      }
      count += min_cost;
    }

    return "" + count;

  }

  TreeMap<Integer, Integer> cost_map = new TreeMap<>();

  public int getCost(int rem)
  {
    if (cost_map.containsKey(rem))
    {
      return cost_map.get(rem);
    }
    E.er();
    return 0;

  }


  public class FF extends Flood
  {
    int val;
    int cost;
    public FF(int val, int cost)
    {
      this.val = val;
      this.cost = cost;
    }

    public boolean isTerm()
    {
      return val > 500000;
    }
    public double getCost()
    {
      return cost;
    }
    public String toString()
    {
      return "" + val;
    }
    public List<State> next()
    {
      cost_map.put(val, cost);
      List<State> lst = new LinkedList<>();

      for(int v : stamps)
      {
        lst.add(new FF(val + v, cost+1));
      }

      return lst;
    }

  }




  public class SS extends State
  {
    int cost;
    int rem;
    public SS(int rem, int cost)
    {
      this.rem = rem;
      this.cost =cost;
    }

    public String toString()
    {
      return "" + rem;
    }

    public double getCost()
    {
      return cost;
    }
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();
      for(int v : stamps)
      {
        if (v <= rem)
        {
          lst.add(new SS(rem - v, cost+1));
        }
      }

      return lst;


    }

    public boolean isTerm()
    {
      return (rem == 0);
    }

  }
}
