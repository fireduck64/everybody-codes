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
    List<Integer> lst = Tok.ent(scan.nextLine(), ",");

    return "" + p1Calc(90, lst);
  }

  public long p1Calc(long wall, List<Integer> lst)
  {
    long sum = 0;
    for(int v : lst)
    {
      long vv = v;
      sum += wall / vv;
    }
    return sum;

  }

  public String Part2(Scanner scan)
  {
    ArrayList<Integer> wall = Tok.ent(scan.nextLine(), ",");

    long p = 1;
    for(int n : getP2(wall, 1))
    {
      p = p * n;
    }
    return "" + p;
  }
  
  public List<Integer> getP2(ArrayList<Integer> wall, int next)
  {
    int items=0;
    for(int v : wall)
    {
      items+=v;
    }
    if (items == 0) return new LinkedList<Integer>();
    if (next > wall.size()) return null;

    boolean all_match=true;
    boolean some_match=false;
    ArrayList<Integer> wall2 = new ArrayList<>();
    wall2.addAll(wall);
    for(int i=next-1; i<wall.size(); i+=next)
    {
      if (wall.get(i) == 0)
      {
        all_match=false;
      }
      else
      {
        some_match=true;
        wall2.set(i, wall2.get(i) - 1);
      }
    }
    if (all_match)
    if (some_match)
    {
      List<Integer> r = getP2(wall2, next);
      if (r != null)
      { 
        LinkedList<Integer> lst = new LinkedList<>();
        lst.add(next);
        lst.addAll(r);
        return lst;
      }
    }

    return getP2(wall, next+1);

  }

  public String Part3(Scanner scan)
  {
    long limit=Long.parseLong(scan.nextLine());
    ArrayList<Integer> wlst = Tok.ent(scan.nextLine(), ",");
    List<Integer> lst = getP2(wlst, 1);

    long wall = 1;
    while( p1Calc(wall, lst) < limit )
    {
      wall = wall*2;
    }

    //System.out.println("Upper limit: " + wall);

    //System.out.println("S: " + p1Calc(94439495762954L, lst));



    return "" + bin(1L, wall, limit, lst);
  }
  public long bin(long low, long high, long limit, List<Integer> lst)
  {
    long mid = (high + low + 1) / 2;
    if (low == high) return low;

    long w = p1Calc(mid, lst);
    //System.out.println("" + low + " " + mid + " " + high);
    //System.out.println("   " + w);
    if (w > limit)
    {
      return bin(low, mid-1, limit, lst);
    }
    if (w < limit)
    {
      return bin(mid, high, limit, lst);
    }
    if (w == limit)
    {
      return mid;
    }
    return mid;

  }
}
