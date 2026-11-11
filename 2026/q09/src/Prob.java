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

  long max_age_jump=1L;
  Random rnd=new Random();
  public class Plant
  {
    long id;
    long growth;
    long limit;


    public Plant(String line)
    {
      List<Long> lst = Tok.enl(line, ",= ");
      id = lst.get(0);
      growth = lst.get(1);
      limit = lst.get(2);
    }

    long age = 0;
    // Status
    long knobs = 0;
    // branch size to count
    TreeMap<Long, Long> seg=new TreeMap<>();

    public long getSize()
    {
      long count = 0;
      count += knobs;
      for(long len : seg.keySet())
      {
        count += seg.get(len) * len;
      }
      return count;
    }
    public long getLeafs()
    {
      if (age == 0) return 0;
      return knobs+1;
    }

    public void grow()
    {
      if (age == 0)
      {
        addSeg(seg, 1, 1);
        age++;
      }
      else
      {
        TreeMap<Long, Long> new_seg = new TreeMap<>();

        long age_jump = max_age_jump;
        //long age_jump=20L;
        // Jump the smalled amount to get to a growth time
        for(long len : seg.keySet())
        {
          age_jump = Math.min( growth - len, age_jump);
        }
        if (age_jump < 1L) E.er();
        //if (age_jump < 20L) age_jump=1L;

        //System.out.println("Jump size: " + age_jump);
        //age_jump=1L;

        for(long len : seg.keySet())
        {
          long count = seg.get(len);
          len+=age_jump;
          if (len > growth) E.er();
          if (len == growth)
          {
            knobs += count;
            addSeg(new_seg, (len - 1L) / 2L, count*2L);
            addSeg(new_seg, 1L, count);
          }
          else
          {
            addSeg(new_seg, len, count);
          }
        }
        seg = new_seg;
        age += age_jump;
      }
    }

    public String toString()
    {
      return String.format("Plant{id:%d, age:%d, size:%d, knobs:%d, segs:%s", id, age, getSize(), knobs, seg.toString());


    }


  }

  public void addSeg(Map<Long,Long> seg, long len, long count)
  {
    if (!seg.containsKey(len)) seg.put(len, 0L);
    seg.put( len, seg.get(len) + count);

  }

  public String Part1(Scanner scan)
  {
    long age = 0;
    max_age_jump=1L;
    for(String line : In.lines(scan))
    {
      Plant p = new Plant(line);
      while(p.getSize() < p.limit)
      {
        p.grow();
      }
      //System.out.println(p);
      age += p.age;
    }

    return "" + age;
  }

  public String Part2(Scanner scan)
  {
    max_age_jump=10000000000L;
    long age = 0;
    for(String line : In.lines(scan))
    {
      Plant p = new Plant(line);
      while(p.getLeafs() < p.limit)
      {
        p.grow();
      }
      //System.out.println(p);
      age += p.age;
    }

    return "" + age;
  }
  public String Part3(Scanner scan)
  {
    return Part2(scan);
  }
}
