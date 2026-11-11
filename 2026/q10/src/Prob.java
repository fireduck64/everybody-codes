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

  TreeMap<Character, Point> ghost_dir = new TreeMap<>();

  {
    ghost_dir.put('^', Nav.N);
    ghost_dir.put('>', Nav.E);
    ghost_dir.put('<', Nav.W);
    ghost_dir.put('v', Nav.S);
  }

  long max_coll=10;

  public class Ghost
  {
    Point loc;
    Point dir;
    long coll_count=0;

    public Ghost(Point loc, char z)
    {
      this.loc = loc;
      this.dir = ghost_dir.get(z);
      if (this.dir == null) E.er();
    }
    public Ghost(Point loc, Point dir, long coll)
    {
      this.loc = loc;
      this.dir = dir;
      this.coll_count = coll;

    }

    public String toString()
    {
      return "" + loc.toString() +"," + dir.toString();
    }
    public Ghost copy()
    {
      return new Ghost(loc, dir, coll_count);
    }

    public boolean stillExists()
    {
      return (coll_count < max_coll);
    }

    public int move(Map2D<Character> map)
    {
      int coll = 0;
      /*if (counts.get(loc) > 1)
      {
        //coll++;
        //dir = Nav.turnRight( Nav.turnRight( dir));
      }*/
      Point next = loc.add(dir);
      if (map.get(next) == '.')
      {
        loc = next;
      }
      else
      {
        dir = Nav.turnRight(dir);
        coll++;
        coll_count++;
      }

      return coll;

    }
    public void rev()
    {
        dir = Nav.turnRight( Nav.turnRight( dir));
        coll_count++;

    }
  }

  public TreeMap<Point, Integer> countGhosts(List<Ghost> ghosts)
  {
    TreeMap<Point, Integer> map = new TreeMap<>();

    for(Ghost g : ghosts)
    if (g.stillExists())
    {
      if (!map.containsKey(g.loc)) map.put(g.loc, 0);
      map.put( g.loc, map.get(g.loc) + 1);
    }
    return map;
  }

  public int doRound(Map2D<Character> map, List<Ghost> ghosts)
  {
    TreeMap<Point, Integer> counts = countGhosts(ghosts);

    int coll = 0;
    for(Ghost g : ghosts)
    {
      coll += g.move(map);
    }
    counts = countGhosts(ghosts);
    for(Ghost g : ghosts)
    {
      if (g.stillExists())
      if (counts.get(g.loc) > 1)
      {
        g.rev();
        coll++;
      }
    }
    return coll;
  }
  public Map2D<Character> render(Map2D<Character> map, List<Ghost> ghosts)
  {
    TreeMap<Point, Integer> counts = countGhosts(ghosts);

    Map2D<Character> rend = map.copy();
    for(Ghost g : ghosts)
    {
      char z = '@';
      for(Map.Entry<Character, Point> me : ghost_dir.entrySet())
      {
        if (me.getValue().equals(g.dir)) z = me.getKey();

      }
      rend.set(g.loc, z);
    }
    for(Map.Entry<Point, Integer> me : counts.entrySet())
    {
      if (me.getValue() > 1)
      {
        Point p = me.getKey();
        String s = "" + me.getValue();
        rend.set(p, s.charAt(0));

      }

    }
    return rend;

  }

  public ArrayList<Ghost> copyGhost(List<Ghost> in)
  {
    ArrayList<Ghost> out = new ArrayList<>();
    for(Ghost g : in)
    {
      out.add(g.copy());
    }
    return out;

  }

  public String Part1(Scanner scan)
  {
    if (ghost_dir.size() != 4) E.er();
    Map2D<Character> map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    ArrayList<Ghost> ghosts=new ArrayList<>();


    for(Point p : map.getAllPoints())
    {
      char z = map.get(p);
      if (z!='#')
      if (z!='.')
      {
        Ghost g = new Ghost(p, z);
        ghosts.add(g);
        map.set(p, '.');
      }
    }
    //render(map, ghosts).print();
    long coll = 0;
    for(int i=1; i<=10; i++)
    {
      long c = doRound(map, ghosts);

      //System.out.println("Round: " + i + " coll: " + c);
      //render(map, ghosts).print();

      coll += c;

    }

    return "" + coll;
  }

  public String Part2(Scanner scan)
  {
    Map2D<Character> map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    ArrayList<Ghost> ghosts=new ArrayList<>();
    max_coll=10000;

    for(Point p : map.getAllPoints())
    {
      char z = map.get(p);
      if (z!='#')
      if (z!='.')
      {
        Ghost g = new Ghost(p, z);
        ghosts.add(g);
        map.set(p, '.');
      }
    }
    //render(map, ghosts).print();
    long rounds = 0;
    while(ghosts.size() > 0)
    {
      long c = doRound(map, ghosts);

      ArrayList<Ghost> new_g = new ArrayList<>();
      for(Ghost g : ghosts)
      {
        if (g.stillExists()) new_g.add(g);
      }
      ghosts = new_g;

      //System.out.println("Round: " + i + " coll: " + c);
      rounds++;

      //System.out.println("Round: " + rounds + " left: " + ghosts.size());
      //render(map, ghosts).print();

    }

    return "" + rounds;

  }
  public String Part3(Scanner scan)
  {
    max_coll=1000000000000L;
    Map2D<Character> map = new Map2D<Character>('#');
    MapLoad.loadMap(map, scan);

    ArrayList<Ghost> ghosts=new ArrayList<>();

    for(Point p : map.getAllPoints())
    {
      char z = map.get(p);
      if (z!='#')
      if (z!='.')
      {
        Ghost g = new Ghost(p, z);
        ghosts.add(g);
        map.set(p, '.');
      }
    }

    HashMap<String, GhostState > prev_state=new HashMap(8192, 0.5f);

    long rounds = 0;
    while(ghosts.size() > 0)
    {
      long c = doRound(map, ghosts);

      ArrayList<Ghost> new_g = new ArrayList<>();
      for(Ghost g : ghosts)
      {
        if (g.stillExists()) new_g.add(g);
      }
      ghosts = new_g;
      rounds++;

      GhostState gs = new GhostState(rounds, ghosts);
      if (prev_state.containsKey(gs.getHash()))
      {
        GhostState prev = prev_state.get(gs.getHash());
        //System.out.println("Loop " + prev.round + " " + gs.round);
        long round_diff = gs.round - prev.round;
        long max_cycles = 10000000000000L;
        for(int i=0; i<gs.ghosts.size(); i++)
        {
          Ghost old = prev.ghosts.get(i);
          Ghost cur = gs.ghosts.get(i);
          long coll_per_cycle = cur.coll_count - old.coll_count;
          long dist = max_coll - cur.coll_count;
          if (coll_per_cycle > 0)
          {
            long mx = (dist / coll_per_cycle);
            if (mx > 2) mx--;

            max_cycles = Math.min(mx, max_cycles);
          }

        }
        //System.out.println("Skipping ahead " + round_diff + " * " + max_cycles);

        rounds += round_diff * max_cycles;
        for(int i=0; i<gs.ghosts.size(); i++)
        {
          Ghost old = prev.ghosts.get(i);
          Ghost cur = ghosts.get(i);
          long coll_per_cycle = cur.coll_count - old.coll_count;
          cur.coll_count += (coll_per_cycle * max_cycles);

        }


        prev_state.clear();

      }
      else
      {
        prev_state.put(gs.getHash(), gs);

      }

    }

    return "" + rounds;
  }

  public class GhostState
  {
    long round;
    List<Ghost> ghosts;

    public GhostState(long round, List<Ghost> in)
    {
      this.round = round;
      ghosts = copyGhost(in);

    }

    public String getHash()
    {
      return HUtil.getHash(ghosts.toString());
    }

  }

}
