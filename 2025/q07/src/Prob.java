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

  TreeMap<String, TreeSet<String> > token_map;

  public String Part1(Scanner scan)
  {
    List<String> names = Tok.en(scan.nextLine(), ",");

    scan.nextLine();

    token_map = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      String key = Tok.en(line, ">").get(0).trim();
      String vals = Tok.en(line, ">").get(1).trim();

      TreeSet<String> ts = new TreeSet<>();
      ts.addAll(Tok.en(vals, ","));

      token_map.put(key, ts);

    }

   
    int idx=1;
    int p2_sum=0;
    for(String n : names)
    {

      if (isDoable(n, 0))
      {
        System.out.println(n);
        p2_sum+=idx;

      }
      idx++;

    }

    return "" + p2_sum;
  }
  
  public boolean isDoable(String name, int idx)
  {
    if (idx + 1 == name.length()) return true;

    String curr = "" + name.charAt(idx);
    String next = "" + name.charAt(idx+1);
    if (token_map.containsKey(curr))
    if (token_map.get(curr).contains(next))
    {
      return isDoable(name, idx+1);
    }
    return false;

  }

  public long getCounts(String name)
  {
    String end = "" + name.charAt(name.length()-1);

    long cnt = 0;
    if (name.length() >= 7) cnt++;
    if (name.length() < 11)
    {
     
      if (token_map.containsKey(end))
      {
        for(String n : token_map.get(end))
        {
          cnt += getCounts(name + n);
        }

      }
    }
    return cnt;


  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {
    TreeSet<String> names = new TreeSet<>();
    names.addAll(Tok.en(scan.nextLine(), ","));

    scan.nextLine();

    token_map = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      String key = Tok.en(line, ">").get(0).trim();
      String vals = Tok.en(line, ">").get(1).trim();

      TreeSet<String> ts = new TreeSet<>();
      ts.addAll(Tok.en(vals, ","));

      token_map.put(key, ts);

    }
    long p3=0;
    for(String n : names)
    {
      boolean prefixed=false;
      for(String m : names)
      {
        if (!m.equals(n))
        if (n.startsWith(m))
        {
          prefixed=true;

        }

      }

      if (!prefixed)
      if (isDoable(n, 0))
      {
        p3+=getCounts(n);

      }
    }

    return "" + p3;
  }
}
