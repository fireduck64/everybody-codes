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

  TreeMap<String, TreeSet<String> > map=new TreeMap<>();


  void getAll(String start, TreeSet<String> current)
  {
    if (current.contains(start)) return;

    current.add(start);

    if (map.containsKey(start))
    {
      for(String sub : map.get(start))
      {
        if (!current.contains(sub))
        {
          getAll(sub, current);
        }
      }
    }

  }

  TreeSet<String> getAll(String start)
  {
    TreeSet<String> set = new TreeSet<>();
    getAll(start, set);

    return set;

  }

  public String Part1(Scanner scan)
  {
    map.clear();
    for(String line : In.lines(scan))
    {
      String a = Tok.en(line, ":").get(0);

      Collection<String> lst = Tok.en(Tok.en(line, ":").get(1), ",");

      TreeSet<String> set = new TreeSet<>();
      set.addAll(lst);
      map.put(a, set);

    }
    //System.out.println(map);
  

    return "" + (getAll("NULL").size() - 1);
  }

  public String Part2(Scanner scan)
  {
    map.clear();
    TreeSet<String> all_set = new TreeSet<>();

    for(String line : In.lines(scan))
    {
      String a = Tok.en(line, ":").get(0);

      Collection<String> lst = Tok.en(Tok.en(line, ":").get(1), ",");

      TreeSet<String> set = new TreeSet<>();
      set.addAll(lst);
      map.put(a, set);
      all_set.add(a);
      all_set.addAll(set);

    }

    int p2=0;
    for(String n : all_set)
    {
      TreeSet<String> in = getAll(n);
      int c=0;
      for(String s : in)
      {
        if (s.startsWith("NULL")) c++;
      }
      if (c>=1) p2++;

    }
    p2--;
    return "" + p2;
  }
  public String Part3(Scanner scan)
  {
    map.clear();
    TreeSet<String> all_set = new TreeSet<>();

    for(String line : In.lines(scan))
    {
      String a = Tok.en(line, ":").get(0);

      Collection<String> lst = Tok.en(Tok.en(line, ":").get(1), ",");

      TreeSet<String> set = new TreeSet<>();
      set.addAll(lst);
      map.put(a, set);
      all_set.add(a);
      all_set.addAll(set);

    }

    int p2=0;
    for(String n : all_set)
    {
      TreeSet<String> in = getAll(n);
      int c=0;
      for(String s : in)
      {
        if (s.startsWith("NULL")) c++;

      }
      if (c>=2) p2++;

    }
    return "" + p2;

  }
}
