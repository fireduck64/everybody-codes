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

  TreeMap<String, List<String> > worm_map;

  public void loadWormMap(Scanner scan)
  {
    worm_map = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      String name = line.split(":")[0];

      List<String> into = Tok.en(line.split(":")[1], ",");
      worm_map.put(name, into);
    }

  }

  public TreeMap<String, Long> doStep(TreeMap<String, Long> in)
  {
    TreeMap<String, Long> out = new TreeMap<>();

    for(String name : in.keySet())
    {
      long in_count = in.get(name);
      for(String o : worm_map.get(name))
      {
        if (!out.containsKey(o))
        {
          out.put(o, 0L);
        }
        out.put(o, out.get(o) + in_count);

      }
    }
    return out;

  }

  public String Part1(Scanner scan)
  {
    loadWormMap(scan);
    System.out.println(worm_map);

    TreeMap<String, Long> worms = new TreeMap<>();
    worms.put("A", 1L);

    for(int i=0; i<4; i++)
    {
      worms = doStep(worms);
    }
    long cnt = 0;
    for(long v : worms.values()) cnt+=v;

    return "" + cnt;
  }

  public String Part2(Scanner scan)
  {
    loadWormMap(scan);
    System.out.println(worm_map);

    TreeMap<String, Long> worms = new TreeMap<>();
    worms.put("Z", 1L);

    for(int i=0; i<10; i++)
    {
      worms = doStep(worms);
    }
    long cnt = 0;
    for(long v : worms.values()) cnt+=v;

    return "" + cnt;
  }
  public String Part3(Scanner scan)
  {
    loadWormMap(scan);
    System.out.println(worm_map);

    long min = 0L;
    long max = 0L;

    for(String start : worm_map.keySet())
    {

      TreeMap<String, Long> worms = new TreeMap<>();
      worms.put(start, 1L);

      for(int i=0; i<20; i++)
      {
        worms = doStep(worms);
      }
      long cnt = 0;
      for(long v : worms.values()) cnt+=v;

      if (min == 0) min = cnt;

      min = Math.min(cnt, min);
      max = Math.max(cnt, max);

    }
    long ans = max - min;

    return "" + ans;

  }
}
