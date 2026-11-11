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
    Map2D<Character> input=new Map2D<>(' ');
    MapLoad.loadMap(input, scan);

    Map2D<Character> m = input;

    Map2D<Character> all_rounds=new Map2D<>(' ');
    int count =0;

    for(int i=0; i<10; i++)
    {
      m = round(m);

      for(Point p : m.getAllPoints())
      {
        if (m.get(p) == '#') 
        {
          count++;
          all_rounds.set(p, '#');
        }

      }
    }

    //all_rounds.print();



    return "" + count;
  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> input=new Map2D<>(' ');
    MapLoad.loadMap(input, scan);

    Map2D<Character> m = input;

    int count =0;

    for(int i=0; i<2025; i++)
    {
      m = round(m);

      for(Point p : m.getAllPoints())
      {
        if (m.get(p) == '#') 
        {
          count++;
        }

      }
    }

    return "" + count;
  }
  public String Part3(Scanner scan)
  {
    Map2D<Character> input=new Map2D<>(' ');
    MapLoad.loadMap(input, scan);

    Map2D<Character> m = new Map2D<Character>(' ');
    for(int i=0; i<34; i++)
    for(int j=0; j<34; j++)
    {
      m.set(i,j,'.');
    }

    HashMap<String, Long> state_map = new HashMap<>(8192, 0.5f);
    
    int count =0;
    state_map.put( m.getHashState(), 0L);

    for(long i=1; i<1000000000L; i++)
    {
      m = round(m);

      String hash = m.getHashState();
      if (state_map.containsKey(hash))
      {
        //m.print();
        long old = state_map.get(hash);
        long n = i;
        System.out.println("Repeat " + old + " " + n);
        //break;
        long span_count = count;
        long len = n-old;
        while(i + len < 1000000000L)
        {
          i+=len;
          count+=span_count;
        }
      }

      if (i==1)
      {
        state_map.put(hash, i);
      }

      long corner = 34/2-input.high_x/2-1;

      Map2D<Character> sub = m.copyRange(corner, corner, corner + input.high_x, corner + input.high_y);
      /*if (i==125)
      {
      m.print();
      sub.print();
      input.print();
      }*/
      if (sub.getHashState().equals(input.getHashState()))
      {
        System.out.println("Match at: " + i);
        for(Point p : m.getAllPoints())
        {
          if (m.get(p) == '#') 
          {
            count++;
          }
        }
      }
    }

    return "" + count;

  }

  public Map2D<Character> round(Map2D<Character> input)
  {
    Map2D<Character> output=new Map2D<Character>(' ');

    for(Point p : input.getAllPoints())
    {
      int n = 0;
      for(Point q : input.getAdjD(p))
      {
        if (input.get(q) == '#') n++;
      }
      if (input.get(p) == '#')
      {
        if (n % 2 == 1) output.set(p, '#');
        else output.set(p, '.');
      }
      else
      {
        if (n % 2 == 0) output.set(p, '#');
        else output.set(p, '.');
      }
    }
    return output;

  }
}
