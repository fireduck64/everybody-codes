import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;
import duckutil.TaskMaster;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Callable;

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
    ArrayList<Dice> dlst = new ArrayList<>();
    for(String line : In.lines(scan))
    {
      Dice d = new Dice(line);

      System.out.println(d);
      dlst.add(d);
    }

    int rolls = 0;
    long sum = 0;
    while(sum <= 10000)
    {
      for(Dice d : dlst)
      {
        sum += d.roll();
      }
      rolls++;

    }

    return "" + rolls;
  }

  public String Part2(Scanner scan)
  {
    ArrayList<Dice> dlst = new ArrayList<>();
    while(true)
    {
      String line = scan.nextLine();
      if (line.trim().length() == 0) break;
      Dice d = new Dice(line);

      System.out.println(d);
      dlst.add(d);
    }

    String track = scan.nextLine();

    TreeMap<Integer, Integer> fin = new TreeMap<>();
    for(Dice d : dlst)
    {
      int r = race(d, track);

      if (fin.containsKey(r)) E.er();
      fin.put(r, d.id);
    }




    return "" + fin.values().toString().replaceAll(" ","");
  }

  public int race(Dice d, String track)
  {
    int rolls=0;
    int track_idx = 0;
    d.init();
    while(track_idx < track.length())
    {
      rolls++;
      int n = d.roll();
      String str_n = "" + n;
      if (str_n.charAt(0) == track.charAt(track_idx)) 
      {
        track_idx++;
      }


    }
    return rolls;
  }

  HashSet<String> cache=new HashSet<>(8192,0.5f);
  
  public void recwiggle(Point loc, Dice d, Map2D<Character> map, Map2D<Character> marked)
  {
    int n = d.roll();
    String str_n = "" + n;
    if (map.get(loc) != str_n.charAt(0)) return;

    String key = loc.x + "/" + loc.y + "/" + d.id + "/" + d.roll_cnt;
    if (cache.contains(key)) return;
    cache.add(key);

    if (d.roll_cnt > 5000) 
    {
      System.out.print('|');
      return;
    }
 
   
    synchronized(marked)
    {
      if (marked.get(loc) != '#')
      {
        marked.set(loc, '#');
        System.out.print('.');
      }
    }

    ArrayList<Point> dirs = new ArrayList<>();
    dirs.add(new Point(0,0));
    dirs.add(Nav.N);
    dirs.add(Nav.S);
    dirs.add(Nav.E);
    dirs.add(Nav.W);

    for(Point dir : dirs)
    {
      Point nx = loc.add(dir);
      recwiggle(nx, d.copy(), map, marked);
    }

  }
  
  public String Part3(Scanner scan)
  {

    ThreadPoolExecutor exec = TaskMaster.getBasicExecutor(64, "p3");

    ArrayList<Dice> dlst = new ArrayList<>();
    while(true)
    {
      String line = scan.nextLine();
      if (line.trim().length() == 0) break;
      Dice d = new Dice(line);

      System.out.println(d);
      dlst.add(d);
    }
    Map2D<Character> map = new Map2D<Character>('.');

    MapLoad.loadMap(map, scan);

    //map.print();

    Map2D<Character> marked = new Map2D<>('.');

    List<Callable<String> > action_list = new ArrayList<>();

    for(Point p : map.getAllPoints())
    for(Dice d : dlst)
    {
      action_list.add(new Action(p, d.copy(), map, marked));
    }

    new TaskMaster<String>(action_list, exec).getResults();
    System.out.println();

    return "" + marked.getCounts().get('#');
  }
  public class Action implements Callable<String>
  {
    Point p;
    Dice d;
    Map2D<Character> map;
    Map2D<Character> marked;

    public Action(Point p, Dice d, Map2D<Character> map,  Map2D<Character> marked)
    {
      this.p = p;
      this.d = d;
      this.map = map;
      this.marked = marked;

    }
    public String call()
    {
      d.init();
      recwiggle(p, d, map, marked);
      return "";


    }

  }

  public class Dice
  {
    int id;
    ArrayList<Integer> faces;
    long seed;

    long pulse;
    long roll_cnt;
    long spin;
    long last_face =0;

    public Dice(String line)
    {
      //1: faces=[1,2,3,4,5,6] seed=7

      id = Tok.ent(line, ":").get(0);

      faces = new ArrayList<>();
      String face_str = Tok.en(line, " ").get(1);
      String seed_str = Tok.en(line, " ").get(2);

      faces.addAll ( Tok.ent(face_str,"[],"));

      seed = Tok.ent(seed_str, "=").get(0);

      init();

    }
    public Dice()
    {
    }

    public String toString()
    {
      return String.format("Dice{id:%d faces:%s seed:%d",id, faces.toString(), seed);
    }

    public void init()
    {
      pulse = seed;
      roll_cnt = 0;
      spin = 0;
      last_face =0;

    }

    public Dice copy()
    {
      Dice o = new Dice();

      o.id = id;
      o.faces = faces;
      o.seed = seed;

      o.pulse = pulse;
      o.roll_cnt = roll_cnt;
      o.spin = spin;
      o.last_face = last_face;

      return o;
    }

    public int roll()
    {
      roll_cnt++;
      spin = roll_cnt * pulse;
      //spin = spin % faces.size();

      last_face = (last_face + spin) % faces.size();
      while(last_face < 0) last_face += faces.size();

      int res = faces.get( (int) last_face );

      //pulse += roll_cnt * pulse;
      pulse += spin;

      pulse = pulse % seed;
      pulse = pulse + 1 + roll_cnt + seed;

      //System.out.println("" + roll_cnt + " " + spin + " " + pulse);

      return res;
      

    }

  }
}
