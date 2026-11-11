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
    long p = 0;
    for(String line : In.lines(scan))
    {
      String std = Tok.en(Tok.en(line, " ").get(0), "-").get(1);
      String num = Tok.en(line, " ").get(1);

      long v = 0;

      for(int i=0; i<4; i++)
      {
        long n = Integer.parseInt( "" + num.charAt(i));
        long base = Integer.parseInt("" + std.charAt(i));
        int place = 4 - 1 - i;

        long b = 1;
        for(int pow=0; pow<place; pow++) b = b*base;

        v += n * b;

      }
      p += v;

    }

    return "" + p;
  }

  public String Part2(Scanner scan)
  {
    long p = 0;
    for(String line : In.lines(scan))
    {


      String std = Tok.en(Tok.en(line, " ").get(0), "-").get(1);
      String num = Tok.en(line, " ").get(1);

      std = new StringBuilder(std).reverse().toString();
      num = new StringBuilder(num).reverse().toString();

      while(std.length() < num.length()) std = std + std;

      long v = 0;

      for(int i=0; i<num.length(); i++)
      {
        long n = Integer.parseInt( "" + num.charAt(i));
        long base = Integer.parseInt("" + std.charAt(i));
        int place = i;

        long b = 1;
        for(int pow=0; pow<place; pow++) b = b*base;

        v += n * b;

      }
      p += v;

    }

    return "" + p;

  }
  public String Part3(Scanner scan)
  {
    long p = 0;
    for(String line : In.lines(scan))
    {


      String std = Tok.en(Tok.en(line, " ").get(0), "-").get(1);
      String num = Tok.en(line, " ").get(1);

      long v = getIsoVal(std, num);

      int high = Integer.parseInt(num);
      boolean found=false;

      for(int t=0; t<high; t++)
      {
        if (getIsoVal(std, "" + t) == v) 
        {
          found=true;
          break;
        }

      }
      if (!found) p+=v;

    }

    return "" + p;

  }

  public long getIsoVal(String std, String num)
  {

      std = new StringBuilder(std).reverse().toString();
      num = new StringBuilder(num).reverse().toString();

      while(std.length() < num.length()) std = std + std;

      long v = 0;

      for(int i=0; i<num.length(); i++)
      {
        long n = Integer.parseInt( "" + num.charAt(i));
        long base = Integer.parseInt("" + std.charAt(i));
        int place = i;

        long b = 1;
        for(int pow=0; pow<place; pow++) b = b*base;

        v += n * b;

      }

      return v;
 
  }
}
