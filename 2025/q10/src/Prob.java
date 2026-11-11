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
  Map2D<Character> map;
  Map2D<Character> base_map;

  TreeSet<Point> getSheep(int jumps, Point start)
  {
    TreeSet<Point> sheeps = new TreeSet<>();
    char z = map.get(start);
    if (z == ' ') return sheeps;
    if (z == 'S')
    {
      sheeps.add(start);
    }
    if (jumps > 0)
    {
      for(int i=-2; i<=2; i++)
      for(int j=-2; j<=2; j++)
      {
        if (Math.abs(i) + Math.abs(j) == 3)
        {
          sheeps.addAll(getSheep(jumps-1, new Point(start.x + i, start.y + j)));
        }
      }
    }

    return sheeps;

  }


  public String Part1(Scanner scan)
  {
    map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);

    TreeSet<Point> sheeps = new TreeSet<>();

    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == 'D')
      {
        sheeps.addAll(getSheep(4, p));

      }

    }

    return "" + sheeps.size();
  }

  public int getBase(Map2D<Character> state, int rounds, Point dragon)
  {
    if (rounds == 0) return 0;
    if (dragon == null) E.er();

    int best = 0;
    // Dragon
    {
      Point p = dragon;
      for(int i=-2; i<=2; i++)
      for(int j=-2; j<=2; j++)
      {
        if (Math.abs(i) + Math.abs(j) == 3)
        {
          Point new_d = new Point(p.x+i, p.y+j);
          if (base_map.get(new_d) != ' ')
          {
          
            Map2D<Character> new_map = state.copy();
            new_map.set(p, base_map.get(p));
            int sheeps = 0;
            if (new_map.get(new_d) == 'S')
            if (base_map.get(new_d) != '#')
            {
              sheeps++;
            }
            int score = sheeps + getBaseSheep(new_map, rounds, new_d);
            best = Math.max(best, score);
          }
        }
      }

    }

    return best;
  }
  public int getBaseSheep(Map2D<Character> state, int rounds, Point dragon)
  {
    Map2D<Character> new_map = base_map.copy();

    int sheeps=0;

    for(Point p : state.getAllPoints())
    {
      if (state.get(p) == 'S')
      {
        Point np = new Point(p.x, p.y+1);
        if (base_map.get(np) != ' ')
        {
          new_map.set(np, 'S');
          if (np.equals(dragon))
          {
            if (base_map.get(np) != '#')
            {
              sheeps++;
              new_map.set(np, '.');
            }
          }

        }
      }
    }

    return sheeps + getBase(new_map, rounds-1, dragon);

  }

  public int chew(Map2D<Character> sheep_map, Map2D<Character> dragon_map)
  {
    int eaten=0;
    for(Point p : dragon_map.getAllPoints())
    {
      if (dragon_map.get(p) == 'D')
      if (sheep_map.get(p) == 'S')
      if (base_map.get(p) != '#')
      {
        eaten++;
        sheep_map.set(p, '.');
      }
    }
    return eaten;
 
  }

  public int simulate(Map2D<Character> sheep_map, Map2D<Character> dragon_map, int rounds)
  {
    int eaten=0;

    //sheep_map.print();

    sheep_map = sheep_map.copy();
    dragon_map = dragon_map.copy();
    //eaten+=chew(sheep_map, dragon_map);

    if (rounds == 0) return eaten;

    Map2D<Character> new_dragons=new Map2D<>(' ');
    // move dragons
    for(Point p : dragon_map.getAllPoints())
    {
      if (dragon_map.get(p) == 'D')
      for(int i=-2; i<=2; i++)
      for(int j=-2; j<=2; j++)
      {
        if (Math.abs(i) + Math.abs(j) == 3)
        {
          Point new_d = new Point(p.x+i, p.y+j);
          if (base_map.get(new_d) != ' ')
          {
            new_dragons.set(new_d, 'D');
          }
        }
      }
    }

    dragon_map = new_dragons;
    eaten+=chew(sheep_map, dragon_map);

    Map2D<Character> new_sheep = new Map2D<Character>(' ');

    //move sheep
    for(Point p : sheep_map.getAllPoints())
    {
      if (sheep_map.get(p) == 'S')
      {
        Point np = new Point(p.x, p.y+1);
        if (base_map.get(np) != ' ')
        {
          new_sheep.set(np, 'S');
        }
      }
    }
    sheep_map = new_sheep;
    
    eaten+=chew(sheep_map, dragon_map);
    return eaten+simulate(sheep_map, dragon_map, rounds-1);

  }

  public String Part2(Scanner scan)
  {
    int rounds = Integer.parseInt(scan.nextLine());
    map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);

    base_map = map.copy();
    Point dragon = null;

    Map2D<Character> sheep_map = new Map2D<>(' ');
    Map2D<Character> dragon_map = new Map2D<>(' ');


    for(Point p : map.getAllPoints())
    {
      if (base_map.get(p) == 'S') base_map.set(p, '.');
      if (base_map.get(p) == 'D') base_map.set(p, '.');
      if (map.get(p) == 'D')
      {
        dragon_map.set(p, 'D');
      }
      if (map.get(p) == 'S')
      {
        sheep_map.set(p, 'S');
      }

    }

    //map.print();
    //base_map.print();
    //sheep_map.print();
    //dragon_map.print();


    return "" + simulate(sheep_map, dragon_map, rounds);
  }
  public String Part3(Scanner scan)
  {
    memo.clear();

    map = new Map2D<>(' ');
    MapLoad.loadMap(map, scan);
    base_map = map.copy();
    Point dragon = null;
    TreeSet<Point> sheep = new TreeSet<>();
    for(Point p : map.getAllPoints())
    {
      if (base_map.get(p) == 'S') base_map.set(p, '.');
      if (base_map.get(p) == 'D') base_map.set(p, '.');

      if (map.get(p) == 'D') dragon=p;
      if (map.get(p) == 'S') sheep.add(p);
    }

    return "" + findOptionsSheep(dragon, sheep);
  }

  public long findOptionsSheep(Point dragon, TreeSet<Point> sheep)
  {
    if (sheep.size() == 0)
    {
      //System.out.println("SSS " + seq);
      return 1L;
    }

    long count = 0;

    int found_moves=0;
    for(Point s : sheep)
    {
      Point np = new Point(s.x, s.y+1);
      if ((!np.equals(dragon)) || (base_map.get(np) == '#'))
      {
        found_moves++;
        if (base_map.get(np) != ' ')
        {
          TreeSet<Point> nset = new TreeSet<>();
          nset.addAll(sheep);
          nset.remove(s);
          nset.add(np);
          count += findOptionsDragon(dragon, nset);
        }
        if (base_map.get(np) == ' ')
        {
          // a sheep has escaped, no options
        }
      }
    }

    // skip sheep
    if (found_moves == 0)
    {
      count+=findOptionsDragon(dragon, sheep);
    }

    return count;
  }

  HashMap<String, Long> memo=new HashMap<>();

  public long findOptionsDragon(Point dragon, TreeSet<Point> sheep)
  {
    String key = "D: " + dragon + " S:" + sheep;

    if (memo.containsKey(key)) return memo.get(key);
    if (sheep.size() == 0)
    {
      E.er();
      return 1L;
    }

    long count = 0;
    for(int i=-2; i<=2; i++)
    for(int j=-2; j<=2; j++)
    {
      if (Math.abs(i) + Math.abs(j) == 3)
      {
        Point new_d = new Point(dragon.x+i, dragon.y+j);
        if (base_map.get(new_d) != ' ')
        {
          //System.out.println("Dragon " + dragon + " to " + new_d);
          TreeSet<Point> ns = new TreeSet<>();
          ns.addAll(sheep);
          if (base_map.get(new_d) == '.') ns.remove(new_d);
          count += findOptionsSheep(new_d, ns);
        }
      }
    }
    //System.out.println(key + " - " + count);
    memo.put(key, count);
    return count;
  }
}
