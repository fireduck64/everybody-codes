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
    long p1=0;

    for(String line : In.lines(scan))
    {
      TreeSet<String> blades=new TreeSet<>();
      TreeSet<String> rules=new TreeSet<>();

      for(String tok : Tok.en(line, " "))
      {
        if (tok.charAt(1) == '>')
        {
          tok = "" + tok.charAt(2) + "<" + tok.charAt(0);
        }
        rules.add(tok);
        blades.add("" + tok.charAt(0));
        blades.add("" + tok.charAt(2));

      }

      ArrayList<String> sorted = new ArrayList<>();
      sorted.addAll(blades);
      for(int k=0; k<sorted.size(); k++)
      for(int i=0; i<sorted.size(); i++)
      for(int j=i+1; j<sorted.size(); j++)
      {
        String a = sorted.get(i);
        String b = sorted.get(j);
        String r = "" + a +"<" +b;
        if (rules.contains(r))
        {
          sorted.set(i, b);
          sorted.set(j, a);

        }

      }
      String n = "";
      for(String s : sorted) n+=s;
      p1 += Long.parseLong(n);
      //System.out.println(sorted);

    }

    return "" + p1;
  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {
    return Part1(scan);
  }
}
