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
    int p1=0;
    while(scan.hasNextLine())
    {
      Dragon d = new Dragon(scan.nextLine());

      if (d.g > d.r) 
      if (d.g > d.b)
      {

        p1 += d.id;

      }

    }

    return "" + p1;
  }

  public String Part2(Scanner scan)
  {

    Dragon best = null;
    while(scan.hasNextLine())
    {
      Dragon d = new Dragon(scan.nextLine());

      if (best == null) best=d;
      if (d.s > best.s)
      {
        best = d;

      }
      if (d.s == best.s)
      {
        if (d.t < best.t) best=d;

      }

    }
    return "" + best.id;
  }
  public String Part3(Scanner scan)
  {

    TreeMap<String, List<Dragon> > groups=new TreeMap<>();

    while(scan.hasNextLine())
    {
      Dragon d = new Dragon(scan.nextLine());

      String grp = d.getGroup();

      if (grp != null)
      {
        if (!groups.containsKey(grp)) groups.put(grp, new LinkedList<Dragon>());
        groups.get(grp).add(d);

      }

    }

    int cnt = 0;
    List<Dragon> biggest = null;
    for(List<Dragon> lst : groups.values())
    {
      if (lst.size() > cnt)
      {
        biggest = lst;
        cnt = lst.size();
      }

    }
    long p3=0;
    for(Dragon d : biggest)
    {
      p3 += d.id;
    }
    return "" + p3;
  }


  public class Dragon
  {
    int id;
    int r;
    int g;
    int b;
    int s;
    int t;
    public Dragon(String input)
    {
      id=Integer.parseInt(Tok.en(input,":").get(0));

      List<String> scales = Tok.en(Tok.en(input,":").get(1), " ");

      r = getBinVal( Tok.en(Tok.en(input,":").get(1), " ").get(0));
      g = getBinVal( Tok.en(Tok.en(input,":").get(1), " ").get(1));
      b = getBinVal( Tok.en(Tok.en(input,":").get(1), " ").get(2));

      if (scales.size() == 4)
      {
        s = getBinVal( scales.get(3));
      }
      t = r+g+b;
    }

    String getSheen()
    {
      if (s <= 30) return "M";
      if (s >= 33) return "S";
      return "-";
    }

    String getColor()
    {
      if ((r > g) && (r > b)) return "R";
      if ((g > r) && (g > b)) return "G";
      if ((b > r) && (b > g)) return "B";

      return "-";

    }
    String getGroup()
    {
      String grp = "" + getSheen() + getColor();
      if (grp.contains("-")) return null;
      return grp;

    }

  }


  public int getBinVal(String in)
  {
    in = in.replace('r','0');
    in = in.replace('R','1');

    in = in.replace('g','0');
    in = in.replace('G','1');

    in = in.replace('b','0');
    in = in.replace('B','1');

    in = in.replace('s','0');
    in = in.replace('S','1');
    return Integer.parseInt(in, 2);
  }
}
