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
    String line = scan.nextLine();

    long opts = 0;
    long mentors = 0;
    for(char z : Str.stolist(line))
    {
      if (z == 'A')
      {
        mentors++;
      }
      if (z == 'a')
      {
        opts += mentors;

      }
     

    }

    return "" + opts;
  }

  public String Part2(Scanner scan)
  {
    String line = scan.nextLine();

    long opts = 0;
    TreeMap<Character, Long> mentors=new TreeMap<>();
    for(char z : Str.stolist(line))
    {
      if (z >= 'A')
      if (z <= 'Z')
      {
        char ez = Character.toLowerCase(z);

        if (!mentors.containsKey(ez)) mentors.put(ez, 0L);
        mentors.put(ez, mentors.get(ez) + 1L);
      }
      if (z >= 'a')
      if (z <= 'z')
      {
        long m = 0;
        if (mentors.containsKey(z)) m = mentors.get(z);
        opts += m;
      }
    }
    System.out.println(mentors);

    return "" + opts;

  }
  public String Part3(Scanner scan)
  {
    String in = scan.nextLine();
    String line = "";
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<1000; i++)
    {
      sb.append(in);
    }
    line = sb.toString();
    long opts =0;
    for(int i=0; i<line.length(); i++)
    {
      char z = line.charAt(i);
      if (z <= 'z')
      if (z >= 'a')
      {
        int s = Math.max(0, i-1000);
        int e = Math.min(line.length(), i+1001);
        char m = Character.toUpperCase(z);
        long m_count=0;
        for(int j = s; j<e; j++)
        {
          if (line.charAt(j) == m)
          {
            m_count++;
          }

        }
        opts += m_count;

      }

    }

    return "" + opts;
  }
}
