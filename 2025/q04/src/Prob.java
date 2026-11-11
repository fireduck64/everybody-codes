import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import java.text.DecimalFormat;
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

    if (new File("sample2.p1").exists())
    {
      System.out.println("Sample2 Part 1:");
      System.out.println(Part1(new Scanner(new FileInputStream("sample2.p1"))));
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

    if (new File("sample2.p2").exists())
    {
      System.out.println("Sample2 Part 2:");
      System.out.println(Part2(new Scanner(new FileInputStream("sample2.p2"))));
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

    if (new File("sample2.p3").exists())
    {
      System.out.println("Sample2 Part 3:");
      System.out.println(Part3(new Scanner(new FileInputStream("sample2.p3"))));
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
    List<Integer> input = In.ints(scan);

    double teeth=0;
    double rotations=0;
    for(int v : input)
    {
      if (teeth == 0)
      {
        rotations=2025;
        teeth = v * rotations;
      }
      else
      {
        rotations = teeth / v;
        teeth = rotations * v;
      }
      

    }

    DecimalFormat df = new DecimalFormat("0.00");
    return "" + df.format(rotations);
  }

  public String Part2(Scanner scan)
  {
    List<Integer> input = In.ints(scan);
    Collections.reverse(input);

    double teeth=0;
    double rotations=0;
    for(int v : input)
    {
      if (teeth == 0)
      {
        rotations=10000000000000.0;
        teeth = v * rotations;
      }
      else
      {
        rotations = teeth / v;
        teeth = rotations * v;
      }
    }
    DecimalFormat df = new DecimalFormat("0.00");
    return "" + df.format(Math.ceil(rotations));

  }
  public String Part3(Scanner scan)
  {
    
    List<String> lines = In.lines(scan);

    double teeth=0;
    double rotations=0;
    for(String line : lines)
    {
      int v_in, v_out;
      if (line.contains("|"))
      {
        v_in = Tok.ent(line, "|").get(0);
        v_out = Tok.ent(line, "|").get(1);
      }
      else
      {
        v_in = Tok.ent(line,"|").get(0);
        v_out = Tok.ent(line,"|").get(0);
      }
      if (teeth == 0)
      {
        rotations=100.0;
        teeth = v_out * rotations;
      }
      else
      {
        rotations = teeth / v_in;
        teeth = rotations * v_out;
      }
    }
    DecimalFormat df = new DecimalFormat("0.00");
    return "" + df.format(rotations);
  }
}
