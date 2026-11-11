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

  public String Part1(Scanner scan)
  {
    List<Long> nails = In.longs(scan);
    long min = nails.get(0);
    for(long x : nails)
    {
      min = Math.min(x, min);
    }
    int hits = 0;
    for(long x : nails)
    {
      hits += x - min;

    }

    return "" + hits;
  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {
    List<Long> nails = In.longs(scan);
    long min = nails.get(0);
    long max = nails.get(0);
    for(long x : nails)
    {
      min = Math.min(x, min);
      max = Math.max(x, max);
    }
    long min_hits = getHits(nails, 0L);

    for(long t = min; t<=max; t++)
    {
      min_hits = Math.min( getHits(nails, t), min_hits);
    }

    return "" + min_hits;
  }

  private long getHits(List<Long> nails, long target)
  {
    long hits = 0;

    for(long x : nails)
    {
      hits += Math.abs(target-x);

    }
    return hits;

  }
}
