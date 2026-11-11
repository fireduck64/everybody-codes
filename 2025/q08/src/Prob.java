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
    int nails = Integer.parseInt(scan.nextLine());

    List<Integer> seq = Tok.ent(scan.nextLine(),",");
    int last=-1;
    int cross=0;
    for(int n : seq)
    {
    
      if (last > 0)
      {
        int diff = Math.abs(n - last);
        if (diff == nails / 2) cross++;

      }
      last = n;

    }

    return "" + cross;
  }

  public String Part2(Scanner scan)
  {
    int nails = Integer.parseInt(scan.nextLine());

    List<Integer> seq = Tok.ent(scan.nextLine(),",");
    List<Seq> segments = new LinkedList<>();

    int cross=0;
    int last =-1;
    for(int n : seq)
    {
  
      if (last > 0)
      {
        Seq ns = new Seq(n, last);
        for(Seq s : segments)
        {
          if (doCross(s, ns)) cross++; 

        }
        segments.add(ns);
      }
      last = n;


    }
 
    return "" + cross;
  }
  public String Part3(Scanner scan)
  {
   int nails = Integer.parseInt(scan.nextLine());

    List<Integer> seq = Tok.ent(scan.nextLine(),",");
    List<Seq> segments = new LinkedList<>();

    int last =-1;
    for(int n : seq)
    {
  
      if (last > 0)
      {
        Seq ns = new Seq(n, last);
        segments.add(ns);
      }
      last = n;
    }
    int best =0;

    for(int i=1; i<=nails; i++)
    for(int j=i+1; j<=nails; j++)
    {
      int cnt = 0;
      Seq ns = new Seq(i,j);
      for(Seq ss : segments)
      {
        if (doCross(ss, ns)) cnt++;
      }
      best = Math.max(best, cnt);

    }
 

    return "" + best;
  }

  public class Seq
  {
    int a;
    int b;

    public Seq(int x, int y)
    {
      a = Math.min(x,y);
      b = Math.max(x,y);

    }



  }
    public static boolean doCross(Seq i, Seq j)
    {
      TreeSet<Integer> vals = new TreeSet<>();
      TreeMap<Integer, Integer> colors=new TreeMap<>();
      vals.add(i.a);
      vals.add(i.b);
      vals.add(j.a);
      vals.add(j.b);
      if (vals.size() == 2) return true;
      if (vals.size() < 4) return false;

      colors.put(i.a, 0);
      colors.put(i.b, 0);
      colors.put(j.a, 1);
      colors.put(j.b, 1);


      StringBuilder sb= new StringBuilder();
      for(int v : colors.values())
      {
        sb.append("" + v);
      }
      String sss = sb.toString();
      if (sss.equals("0101")) return true;
      if (sss.equals("1010")) return true;

      return false;
    }
}
