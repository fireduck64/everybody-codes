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

    List<Integer> lst = Tok.ent(scan.nextLine(), ",");
    Collections.sort(lst);
    Collections.reverse(lst);
    //System.out.println(lst);
    int sum =0;
    int size=1000000;

    for(int v : lst)
    {
      if (v < size)
      {
        sum+=v;
        size=v;

      }

    }

    return "" + sum;
  }

  public String Part2(Scanner scan)
  {
    List<Integer> lst = Tok.ent(scan.nextLine(), ",");
    Collections.sort(lst);
    //System.out.println(lst);

    int sum =0;
    int size = 0;
    int cnt = 0;
    for(int v : lst)
    {
      if (v > size)
      {
        sum+=v;
        size=v;
        cnt++;
      }
      if (cnt == 20) break;
    }


    return "" + sum;
  }
  public String Part3(Scanner scan)
  {

    List<Integer> lst = Tok.ent(scan.nextLine(), ",");
    Collections.sort(lst);
    Collections.reverse(lst);
    //System.out.println(lst);

    List<Integer> remain = new ArrayList<>();

    int stacks=0;
    while(true)
    {

      int sum =0;
      int size=1000000;
      for(int v : lst)
      {
        if (v < size)
        {
          sum+=v;
          size=v;
        }
        else
        {
          remain.add(v);
        }

      }
      stacks++;

      lst.clear();
      lst.addAll(remain);
      remain.clear();
      if (lst.size() ==0) break;

    }

    return "" + stacks;
  }
}
