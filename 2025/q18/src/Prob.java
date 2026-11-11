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

  TreeMap<Integer, Plant> plants;
  ArrayList<List<Integer> > grid;
  List<Integer> s_grid;

  public String Part1(Scanner scan)
    throws Exception
  {
    s_grid=null;
    plants = new TreeMap<>();
    while(scan.hasNextLine())
    {
      Plant p = new Plant(scan);
      plants.put(p.id, p);
    }
    int max =0;
    for(int pid : plants.keySet())
    {
      max = Math.max(max, getEnergy(pid));
      //System.out.println("" + pid + " " + getEnergy(pid));
    }
    
    return "" + max;
  }

  public String Part2(Scanner scan)
  {
    plants = new TreeMap<>();
    int last_plant = 0;
    grid = null;
    while(scan.hasNextLine())
    {
      try
      {
        Plant p = new Plant(scan);
        plants.put(p.id, p);
        System.out.println(p);
        last_plant = p.id;
      }
      catch(Exception e)
      {
        e.printStackTrace();
        System.out.println("Grid set: " + grid.size());
      }
    }

    long p2=0;
    for(int grid_item=0; grid_item < grid.size(); grid_item++)
    {
      s_grid = grid.get(grid_item);
      System.out.println(getEnergy(last_plant));
      p2+=getEnergy(last_plant);

    }
 
    return "" + p2;
  }

  TreeMap<Integer, String> score;

  public String Part3(Scanner scan)
  {
    plants = new TreeMap<>();
    int last_plant = 0;
    score=new TreeMap<>();
    grid = null;
    while(scan.hasNextLine())
    {
      try
      {
        Plant p = new Plant(scan);
        plants.put(p.id, p);
        System.out.println(p);
        last_plant = p.id;
        score.put(p.id, "");
      }
      catch(Exception e)
      {
        System.out.println("Grid set: " + grid.size());
      }
    }

    for(Plant p : plants.values())
    {
      for(int oid : p.branches.keySet())
      {
        int b = p.branches.get(oid);
        if (b < 0) score.put(oid, score.get(oid) + "-");
        if (b > 0) score.put(oid, score.get(oid) + "+");
      }

    }
    System.out.println(score);
    long p2=0;
    long activated=0;
    long max = 0;
    for(int grid_item=0; grid_item < grid.size(); grid_item++)
    {
      s_grid = grid.get(grid_item);
      long en = getEnergy(last_plant);
      if (en > 0)
      {
        p2+=en;
        activated++;
        max = Math.max(max, en);
      }
      //System.out.println(getEnergy(last_plant));
    }
    int sz = s_grid.size();
    s_grid= new ArrayList<Integer>();
    for(int i=1; i<=sz; i++)
    {
      String s = score.get(i);
      if (s.equals("--"))
      {
        s_grid.add(0);
      }
      else if (s.equals("++"))
      {
        s_grid.add(1);
      }
      else
      {
        System.out.println("Unk: " + s);
        s_grid.add(1);

      }

    }
    long high_e = getEnergy(last_plant);
    System.out.println("Best seen: "  + max + " high_e: " + high_e);
    long missing = high_e * activated - p2;
 
    return "" + missing;
  }

  public int getEnergy(int pid)
  {
    Plant p = plants.get(pid);
    if (p.free)
    {
      if (s_grid == null)
      {
        return 1;
      }
      else
      {
        int val = s_grid.get(pid-1);
        return val;

      }
    }

    int sum = 0;
    for(int oid : p.branches.keySet())
    {
      int bthick = p.branches.get(oid);
      int penergy = getEnergy(oid);
      sum += bthick * penergy;
    }
    if (sum < p.thickness) return 0;
    return sum;

  }


  public class Plant
  {
    int id;
    int thickness;
    boolean free;
    TreeMap<Integer, Integer> branches;

    public Plant(Scanner scan)
      throws Exception
    {
      String l1 = scan.nextLine();
      if (l1.trim().length() == 0) l1 = scan.nextLine();
      l1 = l1.replace(":","");
      List<Integer> n = Tok.ent(l1, " ");
      if (n.size() > 2)
      {
        grid = new ArrayList<>();
        grid.add(n);
        while(scan.hasNextLine())
        {
          grid.add(Tok.ent(scan.nextLine(), " "));
        }
        throw new Exception("input");
      }
      id = n.get(0);
      thickness=n.get(1);
      branches = new TreeMap<>();
      while(scan.hasNextLine())
      {
        String line = scan.nextLine();
        if (line.trim().length()==0) break;

        n = Tok.ent(line, " ");
        if (n.size() == 1)
        {
          free=true;
        }
        else
        {
          int pid = n.get(0);
          int thick = n.get(1);
          branches.put(pid, thick);
        }
      }
    }
    public String toString()
    {
      return "P{" + id + " " + thickness + " " + free + " " + branches + "}";

    }

  }
}
