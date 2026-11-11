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
    ArrayList<Integer> input = In.ints(scan);

    LinkedList<Integer> wheel = new LinkedList<>();

    wheel.add(1);
    for(int i=0; i<input.size(); i++)
    {
      if (i % 2 == 0) wheel.addLast(input.get(i));
      else wheel.addFirst(input.get(i));
    }

    int idx =0;
    for(int i=0; i<wheel.size(); i++)
    {
      if (wheel.get(i) == 1) idx=i;
    }
    idx += 2025;
    idx = idx % wheel.size();

    return "" + wheel.get(idx);
  }

  public String Part2(Scanner scan)
  {
    List<String> input = In.lines(scan);

    LinkedList<Integer> wheel = new LinkedList<>();

    wheel.add(1);
    for(int i=0; i<input.size(); i++)
    {
      int low = Tok.ent(input.get(i), "-").get(0);
      int high = Tok.ent(input.get(i), "-").get(1);
      if (high < low) E.er();

      for(int j=low; j<=high; j++)
      {
        if (i % 2 == 0) wheel.addLast(j);
        else wheel.addFirst(j);
      }
    }
    //System.out.println(wheel);
    System.out.println("Wheel done, size: " + wheel.size());

    int idx =0;
    for(int v : wheel)
    {
      if (v == 1) break;
      idx++;
    }
    idx += 20252025;
    idx = idx % wheel.size();

    return "" + wheel.get(idx);

  }
  public String Part3(Scanner scan)
  {
    List<String> input = In.lines(scan);

    LinkedList<Span> wheel = new LinkedList<>();

    wheel.add(new Span(1,1,false));
    for(int i=0; i<input.size(); i++)
    {
      int low = Tok.ent(input.get(i), "-").get(0);
      int high = Tok.ent(input.get(i), "-").get(1);
      if (high < low) E.er();

      if (i % 2 ==0) wheel.addLast(new Span(low, high, false));
      else wheel.addFirst(new Span(low, high, true));

    }
    //System.out.println(wheel);
    System.out.println("Wheel done, size: " + wheel.size());
    long wheel_len = 0;
    for(Span s : wheel) wheel_len += s.getLen();

    long idx =0;
    for(Span v : wheel)
    {
      if (v.low == 1) break;
      idx+=v.getLen();
    }
    idx += 202520252025L;
    idx = idx % wheel_len;

    long seek = idx;
    for(Span v : wheel)
    {
      if (seek >= v.getLen())
      {
        seek -= v.getLen();
      }
      else
      {
        return "" + v.seek((int)seek);

      }

    }
    E.er();
    return "";


  }

  public class Span
  {
    int low;
    int high;
    boolean back;
    public Span(int low, int high, boolean back)
    {
      this.low = low;
      this.high = high;
      this.back = back;
    }
    public int getLen()
    {
      return high - low + 1;
    }
    public int seek(int s)
    {
      int start = low;
      int dir=1;
      if (back)
      {
        start = high; dir=-1;
      }
      return start + (dir * s);

    }

  }
}
