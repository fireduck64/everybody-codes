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
    long p1 = 0;

    String line = scan.nextLine();
    for(long x : Tok.enl(line, ","))
    {
      long side = 1;
      boolean done=false;

      while(!done)
      {
        long sq = side *side;
        if (x <= sq)
        {
          p1 += (sq - x);
          done=true;
        }
        side++;
      }

    }

    return "" + p1;
  }

  public String Part2(Scanner scan)
  {
    long p2 = 0;

    String line = scan.nextLine();
    for(long x : Tok.enl(line, ","))
    {
      long side = 3;
      boolean done=false;

      while(!done)
      {
        long sq = side *side - (side-2) * (side-2);
        if (x <= sq)
        {
          p2 += (sq - x);
          done=true;
        }
        side++;
      }

    }

    return "" + p2;
 
  }
  public String Part3(Scanner scan)
  {
    long p3 = 0;

    String line = scan.nextLine();
    for(long x : Tok.enl(line, ","))
    {
      long side = 3;
      boolean done=false;

      while(!done)
      {
        long sq = side *side - 1L;
        if (x <= sq)
        {
          p3 += (sq - x);
          done=true;
        }
        side+=2;
      }

    }

    return "" + p3;
  }
}
