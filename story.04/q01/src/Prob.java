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
    long p1=0;
    while(scan.hasNextLine())
    {
      List<Integer> input = Tok.ent(scan.nextLine(),",");
      p1 += p1Dance(0, 0, new TreeSet<Long>(), input);
    }

    return "" + p1;
  }

  public long p1Dance(long cur, int step, TreeSet<Long> visited, List<Integer> steps)
  {
    visited.add(cur);
    if (step >= steps.size()) return cur;
    int dist = steps.get(step);

    if ((cur - dist >= 0) && (!visited.contains(cur - dist)))
    {
      return p1Dance(cur - dist, step+1, visited, steps);
    }
    else
    {
      return p1Dance(cur + dist, step+1, visited, steps);
    }


  }

  public long p2Dance(long cur, int step, TreeSet<Long> visited, List<Integer> steps)
  {
    while (visited.contains(cur)) cur++;

    visited.add(cur);
    if (step >= steps.size()) return cur;
    int dist = steps.get(step);

    if ((cur - dist >= 0) && (!visited.contains(cur - dist)))
    {
      return p2Dance(cur - dist, step+1, visited, steps);
    }
    else
    {
      return p2Dance(cur + dist, step+1, visited, steps);
    }

  }

  public class JumpState
  {
    List<Segment> up;
    List<Segment> down;

    public JumpState()
    {
      up = new ArrayList<>();
      down = new ArrayList<>();
    }

    public boolean canAdd(boolean is_up, Segment s)
    {
      if (is_up)
      {
        //System.out.println("check up " + s);
      }
      else
      {
        //System.out.println("check down " + s);
      }
      List<Segment> sl = down;
      if (is_up) sl = up;

      for(Segment seq : sl)
      {
        if (!seq.isOk(s)) return false;

      }
      return true;

    }
    public void add(boolean is_up, Segment s)
    {
      if (is_up)
      {
        //System.out.println("Up " + s);
      }
      else
      {
        //System.out.println("Down " + s);

      }
      
      if (is_up) up.add(s);
      else down.add(s);
    }

  }

  public class Segment
  {
    long a;
    long b;

    public Segment(long a, long b)
    {
      this.a = Math.min(a,b);
      this.b = Math.max(a,b);
    }

    public boolean isOk(Segment o)
    {
      if (b < o.a) return true;
      if (a > o.b) return true;

      if ((o.a < a) && (b < o.b)) return true;
      if ((a < o.a) && (o.b < b)) return true;

      return false;

    }
    public String toString()
    {
      return "[" + a + "," + b +"]";

    }

  }

  public long p3Dance(long cur, int step, TreeSet<Long> visited, JumpState js, List<Integer> steps)
  {
    boolean up = (step % 2 == 1);

    visited.add(cur);
    if (step >= steps.size()) return cur;
    int dist = steps.get(step);

    if (
      (cur - dist >= 0) 
      && (!visited.contains(cur - dist))
      && (js.canAdd(up, new Segment(cur, cur-dist)))
      )
    {
      long n = cur - dist;
      js.add(up, new Segment(cur, cur-dist));
      return p3Dance(n, step+1, visited, js, steps);
    }
    else
    {
      long n = cur + dist;
      while(true)
      {
        Segment seg = new Segment(cur, n);
        if (!visited.contains(n))
        if (js.canAdd(up, seg))
        {
          js.add(up, new Segment(cur, n));
          return p3Dance(n, step+1, visited, js, steps);
        }

        if (n > (visited.last()+5))
        {
          steps.remove(step);
          return p3Dance(cur, step, visited, js, steps);
        }
        n++;
      }

    }


  }



  public String Part2(Scanner scan)
  {
    long p2=0;
    while(scan.hasNextLine())
    {
      List<Integer> input = Tok.ent(scan.nextLine(),",");
      p2 += p2Dance(0, 0, new TreeSet<Long>(), input);
    }

    return "" + p2;
 
  }
  public String Part3(Scanner scan)
  {
    long p3=0;
    while(scan.hasNextLine())
    {
      List<Integer> input = Tok.ent(scan.nextLine(),",");
      System.out.println(input);
      p3 += p3Dance(0, 0, new TreeSet<Long>(), new JumpState(), input);
    }

    return "" + p3;
 
  }
}
