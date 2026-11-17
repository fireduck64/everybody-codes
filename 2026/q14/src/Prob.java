import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;


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
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p2"))
      {
        System.out.println("Part 2 - " + f.getName() + ":");
        System.out.println(Part2(new Scanner(new FileInputStream(f))));
      }
    }

    for(File f : new File(System.getProperty("user.dir")).listFiles())
    {
      if (f.getName().endsWith(".p3"))
      {
        System.out.println("Part 3 - " + f.getName() + ":");
        System.out.println(Part3(new Scanner(new FileInputStream(f))));
      }

    }

  }

  Random rnd=new Random();

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>('.');
    MapLoad.loadMap(map, scan);

    long out_count=0;
    long in_count=0;
    for(Point p : map.getAllPoints())
    {
      for(Point a : map.getAdj(p, false))
      {
        char zp = map.get(p);
        char za = map.get(a);
        if (zp != za)
        {
          if (map.get(a)=='.') out_count++;
          else in_count++;

        }

      }
    }
    if (in_count % 2 != 0) E.er();
    long p = out_count + in_count/2;

    return "" + p;
  }

  TreeSet<Character> all_sections = new TreeSet<>();
  Map2D<Character> map;
  TreeMap<String, Integer> seam_counts;
  public String Part2(Scanner scan)
  {
    map = new Map2D<Character>('.');

    MapLoad.loadMap(map, scan);

    all_sections.clear();
    all_sections.add('.');
    for(Point p : map.getAllPoints())
    {
      all_sections.add(map.get(p));
    }
    char start = all_sections.first();

    long min_x =-1; long min_y=-1;
    long max_x = map.high_x + 1;
    long max_y = map.high_y + 1;

    for(long x = min_x; x<=max_x; x++)
    for(long y = min_y; y<=max_y; y++)
    {
      if (map.get(x,y) == '.') map.set(x,y,'.');
    }
    seam_counts = new TreeMap<>();
    for(Point p : map.getAllPoints())
    {
      for(Point a : map.getAdj(p, false))
      {
        char zp = map.get(p);
        char za = map.get(a);
        if (zp != za)
        {
          TreeSet<Character> s = new TreeSet<>();
          s.add(zp);
          s.add(za);
          String key = "" + s.pollFirst();
          key += s.pollFirst();
          if (!seam_counts.containsKey(key)) seam_counts.put(key, 0);
          seam_counts.put(key, seam_counts.get(key) + 1);
        }
      }
    }

    TreeSet<Character> attached =new TreeSet<>();
    attached.add('.');
    TreeMap<Double, String> seam_pri = new TreeMap<>();
    for(String key : seam_counts.keySet())
    {
      seam_pri.put( seam_counts.get(key) + rnd.nextDouble(), key);
    }
    System.out.println(seam_counts);

    int cost=0;
    while(attached.size() < all_sections.size())
    {
      for(String key : seam_pri.values())
      {
        int c = seam_counts.get(key) / 2;
        int has=0;
        char other='a';
        for(char z : Str.stolist(key))
        {
          if (attached.contains(z)) has++;
          else
          {
            other = z;
          }
        }
        if (has == 1)
        {
          //System.out.println("Added " + other + " - " + c + " " + key);
          cost += c;
          attached.add(other);
          break;
        }
      }
    }

    return "" + cost;
  }

  /*public class P2State extends State
  {
    TreeSet<Character> added;
    int cost;

    public P2State(TreeSet<Character> added, int cost)
    {
      this.added = added;
      this.cost =cost;

    }

    public boolean isTerm()
    {
      return (added.containsAll(all_sections));

    }

    public String toString()
    {
      return added.toString();
    }
    public double getCost()
    {
      return cost;
    }

    @Override
    public double getEstimate()
    {
      return all_sections.size() - added.size();
    }
    public List<State> next()
    {
      List<State> lst = new LinkedList<>();

      for(Point p : map.getAllPoints())
      {
        char zp = map.get(p);
        if (added.contains(zp))
        {
          for(Point a : map.getAdj(p, false))
          {
            char za = map.get(a);
            if (!added.contains(za))
            {
              int len = getSeamLength(p,a);
              TreeSet<Character> added2 = new TreeSet<>();
              added2.addAll(added);
              added2.add(za);


              lst.add(new P2State(added2, cost + len));

            }
          }
        }

      }

      return lst;
    }

  }*/

  public String Part3(Scanner scan)
  {
    map = new Map2D<Character>('.');

    MapLoad.loadMap(map, scan);

    all_sections.clear();
    all_sections.add('.');
    for(Point p : map.getAllPoints())
    {
      all_sections.add(map.get(p));
    }
    long min_x =-1; 
    long min_y=-1;
    long max_x = map.high_x + 1;
    long max_y = map.high_y + 1;

    for(long x = min_x; x<=max_x; x++)
    for(long y = min_y; y<=max_y; y++)
    {
      if (map.get(x,y) == '.') map.set(x,y,'.');
    }

    int min_cost = 1000000;
    System.out.println(all_sections);

    ArrayList<Character> all_lst = new ArrayList<>();
    all_lst.addAll(all_sections);

    for(int i=0; i<all_lst.size(); i++)
    for(int j=i+1; j<all_lst.size(); j++)
    {
      char magic1 = all_lst.get(i);
      char magic2 = all_lst.get(j);
      if (magic1 != '.')
      if (magic2 != '.')
      if (magic1 != magic2)
      {
        TreeSet<Character> magic = new TreeSet<>();
        magic.add(magic1);
        magic.add(magic2);
        System.out.println(magic);
        seam_counts = new TreeMap<>();
        for(Point p : map.getAllPoints())
        {
          for(Point a : map.getAdj(p, false))
          {
            char zp = map.get(p);
            char za = map.get(a);
            if (zp != za)
            {
              TreeSet<Character> s = new TreeSet<>();
              s.add(zp);
              s.add(za);
              String key = "" + s.pollFirst();
              key += s.pollFirst();
              if (!seam_counts.containsKey(key)) seam_counts.put(key, 0);
              seam_counts.put(key, seam_counts.get(key) + 1);
            }
          }
        }
        for(String key : seam_counts.keySet())
        {
          if ((key.contains("" + magic1)) || (key.contains("" + magic2)))
          {
            seam_counts.put(key, 0);
          }
        }

      
        TreeMap<Double, String> seam_pri = new TreeMap<>();
        for(String key : seam_counts.keySet())
        {
          seam_pri.put( seam_counts.get(key) + rnd.nextDouble(), key);
        }

        TreeSet<Character> attached =new TreeSet<>();
        attached.add('.');

        int cost=0;
        while(attached.size() < all_sections.size())
        {
          for(String key : seam_pri.values())
          {
            int c = seam_counts.get(key) / 2;
            int has=0;
            char other='a';
            for(char z : Str.stolist(key))
            {
              if (attached.contains(z)) has++;
              else
              {
                other = z;
              }
            }
            if (has == 1)
            {
              //System.out.println("Added " + other + " - " + c + " " + key);
              cost += c;
              attached.add(other);
              break;
            }
          }
        }
        min_cost = Math.min(min_cost, cost);
      }

    }

    return "" + min_cost;
  }


}
