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
    Map2D<Character> m_in = new Map2D<Character>('.');
    MapLoad.loadMap(m_in, scan);

    Map2D<Integer> m_d = new Map2D<Integer>(0);
    for(Point p : m_in.getAllPoints())
    {
      if (m_in.get(p) == '#') m_d.set(p, 1);
      else m_d.set(p, 0);

    }

    while(digDeeper(m_d, false) > 0)
    {}

    int count =0;
    for(Point p : m_d.getAllPoints())
    {
      count += m_d.get(p);

    }

    m_d.print();


    

    return "" + count;
  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);
  }
  public String Part3(Scanner scan)
  {
    Map2D<Character> m_in = new Map2D<Character>('.');
    MapLoad.loadMap(m_in, scan);

    Map2D<Integer> m_d = new Map2D<Integer>(0);
    for(Point p : m_in.getAllPoints())
    {
      if (m_in.get(p) == '#') m_d.set(p, 1);
      else m_d.set(p, 0);

    }

    while(digDeeper(m_d, true) > 0)
    {}

    int count =0;
    for(Point p : m_d.getAllPoints())
    {
      count += m_d.get(p);

    }

    m_d.print();


    

    return "" + count;
 
  }

  public int digDeeper(Map2D<Integer> m_d, boolean diag)
  {
    int deeper = 0;

    for(Point p : m_d.getAllPoints())
    {
      int d = m_d.get(p);
      if (d == 0) continue;

      int n_min = d;
      for(Point q : m_d.getAdj(p, diag))
      {
        n_min = Math.min( m_d.get(q), n_min);
      }

      if (n_min == d)
      {
        m_d.set(p,d +1);
        deeper++;

      }


    }


    return deeper;

  }
}
