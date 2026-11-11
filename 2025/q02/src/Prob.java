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
    String line = scan.nextLine();
    line=line.replace("[",",");
    line=line.replace("]",",");
    List<Long> input = Tok.enl(line, ",");
    CN a = new CN(input);
    CN r = new CN(0,0);

    for(int c=0; c<3; c++)
    {
      r = CN.mult(r,r);
      r = CN.div(r, new CN(10,10));
      r = CN.add(r, a);

    }

    return "" + r;
  }

  public String Part2(Scanner scan)
  {
    String line = scan.nextLine();
    line=line.replace("[",",");
    line=line.replace("]",",");
    List<Long> input = Tok.enl(line, ",");

    CN a = new CN(input);
    Map2D<Character> map = new Map2D<Character>('.');

    CN opp = CN.add(a, new CN(1000,1000));
    for(int x = 0; x<=100; x++)
    for(int y = 0; y<=100; y++)
    { 
      
      //int x=16; int y=0;

      CN p = CN.add(a, new CN(x*10, y*10));
      CN working = new CN(0,0);
      boolean fail=false;

      for(int c =0; c<100; c++)
      {
        working = CN.mult(working, working);
        working = CN.div(working, new CN(100000, 100000));
        working = CN.add(working, p);

        if (Math.abs(working.val.get(0)) >= 1000000) fail=true;
        if (Math.abs(working.val.get(1)) >= 1000000) fail=true;
        if (fail) break;
        //System.out.println("P: " + p + " R: " + working + " " + fail + " C: " + c);
      }
      //System.out.println("P: " + p + " R: " + working + " " + fail);
      if (!fail)
      {
        map.set(x,y,'x');

      }
      

    }


 
    return "" + map.getCounts();
  }
  public String Part3(Scanner scan)
  {
    String line = scan.nextLine();
    line=line.replace("[",",");
    line=line.replace("]",",");
    List<Long> input = Tok.enl(line, ",");

    CN a = new CN(input);
    Map2D<Character> map = new Map2D<Character>('.');

    CN opp = CN.add(a, new CN(1000,1000));
    for(int x = 0; x<=1000; x++)
    for(int y = 0; y<=1000; y++)
    { 
      
      //int x=16; int y=0;

      CN p = CN.add(a, new CN(x*1, y*1));
      CN working = new CN(0,0);
      boolean fail=false;

      for(int c =0; c<100; c++)
      {
        working = CN.mult(working, working);
        working = CN.div(working, new CN(100000, 100000));
        working = CN.add(working, p);

        if (Math.abs(working.val.get(0)) >= 1000000) fail=true;
        if (Math.abs(working.val.get(1)) >= 1000000) fail=true;
        if (fail) break;
        //System.out.println("P: " + p + " R: " + working + " " + fail + " C: " + c);
      }
      //System.out.println("P: " + p + " R: " + working + " " + fail);
      if (!fail)
      {
        map.set(x,y,'x');

      }
      

    }


 
    return "" + map.getCounts();
 
  }
}
