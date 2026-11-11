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
  ArrayList<String> moves;

  public void scanInput(Scanner scan)
  {
    map = new Map2D<Character>('#');
    moves = new ArrayList<>();
    ArrayList<String> map_lines = new ArrayList<>();

    while(true)
    {
      String line = scan.nextLine();
      if (line.trim().length() == 0) break;
      map_lines.add(line);
    }

    MapLoad.loadMap(map, In.newScan(map_lines));

    while(scan.hasNextLine())
    {
      moves.add( scan.nextLine());
    }
  }

  public int drop(int start_slot, String inst)
  {
    LinkedList<Character> action_queue = new LinkedList<>();
    action_queue.addAll( Str.stolist(inst));
    int idx=0;
    Point cur = new Point(start_slot, -1);

    while(true)
    {
      //System.out.println(cur);
      Point down = cur.add(Nav.S);
      if (map.get(down) == '#') return (int) cur.x;
      if (map.get(down) == '*')
      {
        if (action_queue.size() == 0) E.er();

        char move = action_queue.pollFirst();
        Point next = null;
        if (move == 'R')
        {
          next = cur.add(new Point(1,1));
          if (map.get(next) == '#') next = cur.add(new Point(-1,1));
        }
        else if (move=='L')
        {
          next = cur.add(new Point(-1,1));
          if (map.get(next) == '#') next = cur.add(new Point(1,1));

        }
        if (next == null) E.er();
        if (map.get(next) != '.') E.er();

        cur = next;
      }
      if (map.get(down) == '.')
      {
        cur = down;
      }

    }

  }

  public String Part1(Scanner scan)
  {
    scanInput(scan);

    long score = 0;

    for(int i=0; i<moves.size(); i++)
    {
      int land = drop(i*2, moves.get(i));
      int toss = i+1;
      land = land/2+1;
      int s = Math.max(0, land*2  - toss);
      //System.out.println("Toss: " + toss + " land " + land + " s:" + s);
      score+=s;
    }

    return "" + score;
  }

  public String Part2(Scanner scan)
  {
    scanInput(scan);

    long score = 0;

    for(int i=0; i<moves.size(); i++)
    {
      int high = 0;

      for(int j=0; j<=map.high_x/2; j++)
      {
        int land = drop(j*2, moves.get(i));
        int toss = j+1;
        land = land/2+1;
        int s = Math.max(0, land*2  - toss);
        //System.out.println("Toss: " + toss + " land " + land + " s:" + s);
        high = Math.max( high, s);
      }
      score+=high;
    }

    return "" + score;
  }


  HashMap<String, Long> memo=new HashMap<>(8192, 0.5f);

  public long recHigh(int move_idx, TreeSet<Integer> used)
  {
    if (move_idx == moves.size()) return 0L;

    String key = "H" + move_idx + "/" + used.toString();
    if (memo.containsKey(key)) return memo.get(key);

    String m = moves.get(move_idx);
    long high = 0;
    for(int j=0; j<=map.high_x/2; j++)
    if (!used.contains(j))
    {
      int land = drop(j*2, m);
      int toss = j+1;
      land = land/2+1;
      long s = Math.max(0, land*2  - toss);

      TreeSet<Integer> u2 = new TreeSet<>();
      u2.addAll(used);
      u2.add(j);
      s+= recHigh(move_idx + 1, u2);

      high = Math.max(s, high);
    
    }
    memo.put(key, high);

    return high;

  }

  public long recLow(int move_idx, TreeSet<Integer> used)
  {
    if (move_idx == moves.size()) return 0L;


    String key = "L" + move_idx + "/" + used.toString();
    if (memo.containsKey(key)) return memo.get(key);

    String m = moves.get(move_idx);
    long high = 10000000L;
    for(int j=0; j<=map.high_x/2; j++)
    if (!used.contains(j))
    {
      int land = drop(j*2, m);
      int toss = j+1;
      land = land/2+1;
      long s = Math.max(0, land*2  - toss);

      TreeSet<Integer> u2 = new TreeSet<>();
      u2.addAll(used);
      u2.add(j);
      s+= recLow(move_idx + 1, u2);

      high = Math.min(s, high);
    
    }
    memo.put(key, high);

    return high;

  }


  public String Part3(Scanner scan)
  {
    scanInput(scan);
    memo.clear();
    long low = recLow(0, new TreeSet<Integer>());
    memo.clear();
    long high = recHigh(0, new TreeSet<Integer>());

    return "" + low + " " + high;
  }
}
