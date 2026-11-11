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
    String line = scan.nextLine().replace(":",",");

    LinkedList<Integer> lst = new LinkedList<Integer>();
    lst.addAll(Tok.ent(line, ","));
    int id = lst.poll();
    LinkedList<Spine> spines = new LinkedList<>();

    for(int v : lst)
    {
      boolean added=false;
      for(Spine s : spines)
      {
        if (s.left == 0)
        if (v < s.v)
        {
          s.left = v;
          added=true;
          break;
        }
        if (s.right == 0)
        if (v > s.v)
        {
          s.right = v;
          added=true;
          break;

        }
      }
      if (!added)
      {
        spines.add(new Spine(v));
      }

    }
    StringBuilder sb = new StringBuilder();
    for(Spine s : spines)
    {
      sb.append("" + s.v);

    }

    return "" + sb.toString();
  }

  public String Part2(Scanner scan)
  {
    LinkedList<Long> quals=new LinkedList<>();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine().replace(":",",");

      LinkedList<Integer> lst = new LinkedList<Integer>();
      lst.addAll(Tok.ent(line, ","));
      int id = lst.poll();
      LinkedList<Spine> spines = new LinkedList<>();

      for(int v : lst)
      {
        boolean added=false;
        for(Spine s : spines)
        {
          if (s.left == 0)
          if (v < s.v)
          {
            s.left = v;
            added=true;
            break;
          }
          if (s.right == 0)
          if (v > s.v)
          {
            s.right = v;
            added=true;
            break;

          }
        }
        if (!added)
        {
          spines.add(new Spine(v));
        }

      }
      StringBuilder sb = new StringBuilder();
      for(Spine s : spines)
      {
        sb.append("" + s.v);
      }
      long qual = Long.parseLong(sb.toString());
      quals.add(qual);

    }
    Collections.sort(quals);
    long diff = quals.peekLast() - quals.peekFirst();
    return "" + diff;
  }
  public String Part3(Scanner scan)
  {
    LinkedList<Sword> swords = new LinkedList<Sword>();
    while(scan.hasNextLine())
    {
      swords.add(new Sword(scan.nextLine()));

    }
    Collections.sort(swords);

    int sum = 0;
    int idx=1;
    for(Sword s : swords)
    {
      sum += s.id * idx;
      //System.out.println(s);
      idx++;
    }

    return "" + sum;
  }

  public class Sword implements Comparable<Sword>
  {
    ArrayList<Spine> spines;
    long qual;
    int id;

    public String toString()
    {
      return "" + qual + " " + id + " " + spines;

    }
    public Sword(String line)
    {
      line = line.replace(":",",");

      LinkedList<Integer> lst = new LinkedList<Integer>();
      lst.addAll(Tok.ent(line, ","));
      id = lst.poll();
      spines = new ArrayList<>();

      for(int v : lst)
      {
        boolean added=false;
        for(Spine s : spines)
        {
          if (s.left == 0)
          if (v < s.v)
          {
            s.left = v;
            added=true;
            break;
          }
          if (s.right == 0)
          if (v > s.v)
          {
            s.right = v;
            added=true;
            break;

          }
        }
        if (!added)
        {
          spines.add(new Spine(v));
        }

      }
      StringBuilder sb = new StringBuilder();
      for(Spine s : spines)
      {
        sb.append("" + s.v);
      }
      qual = Long.parseLong(sb.toString());

    }
    public int compareTo(Sword o)
    {
      if (qual > o.qual) return -1;
      if (qual < o.qual) return 1;

      int sz = Math.min(o.spines.size(), spines.size());
      for(int i=0; i<sz; i++)
      {
        int s = spines.get(i).getScore();
        int os = o.spines.get(i).getScore();
        if (s > os) return -1;
        if (s < os) return 1;
      }

      if (id > o.id) return -1;
      if (id < o.id) return 1;

      return 0;

    }

  }

  public class Spine
  {
    int v;
    int left;
    int right;

    public Spine(int v){ this.v = v;}

    public String toString()
    {
      return ("{" + left + "," + v + "," + right +"}");

    }
    public int getScore()
    {
      StringBuilder sb = new StringBuilder();
      if (left > 0) sb.append("" + left);
      sb.append(v);
      if (right > 0) sb.append("" + right);

      return Integer.parseInt(sb.toString());

    }

  }
}
