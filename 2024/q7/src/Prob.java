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
    TreeMap<String, List<String> > map = parse(scan);

    TreeMap<String, Long> score = sim(map, 10);

    TreeMap<Double, String> order = new TreeMap<>();
    for(String s : score.keySet())
    {
      order.put( rnd.nextDouble() + -score.get(s), s);
    }
    String res="";
    for(String s : order.values())
    {
      res += s;
    }

    return res;
  }


  public String Part2(Scanner scan)
  {
    TreeMap<String, List<String> > map = parse(scan);
    track.print();
    TreeMap<String, Long> score = new TreeMap<>();
    for(String s : map.keySet())
    {
      System.out.println("Knight : " + s);
      long sc = sim2( map.get(s), 10);
      score.put(s, sc);

    }
    System.out.println(score);
    TreeMap<Double, String> order = new TreeMap<>();
    for(String s : score.keySet())
    {
      order.put( rnd.nextDouble() + -score.get(s), s);
    }
    String res="";
    for(String s : order.values())
    {
      res += s;
    }

    return res;

  }
  List<Character> track_list;
  public String Part3(Scanner scan)
  {
    TreeMap<String, List<String> > map = parse(scan);
    track.print();
    track_list = getTrack();

    long a_score = sim3(track_list,map.get("A"), 2024);
    System.out.println("Score to beat: " + a_score);

    return "" + rec(ImmutableList.of(), 5, 3, 3, a_score);



  }

  public long rec(List<String> steps, int plus_left, int minus_left, int eq_left, long target_score)
  {
    if (steps.size() == 11)
    {
      //return 1L;
      long sss = sim3(track_list, steps, 2024);
      //if (sss > target_score) System.out.print("WIN: " );
      //System.out.println(sss);
      if (sss > target_score) return 1L;
      return 0L;
    }
    //System.out.println(steps);

    long v = 0;
    if (plus_left > 0)
    {
      ArrayList<String> lst = new ArrayList<String>();
      lst.addAll(steps);
      lst.add("+");
      v+=rec(lst, plus_left-1, minus_left, eq_left, target_score);
    }
    if (minus_left > 0)
    {
      ArrayList<String> lst = new ArrayList<String>();
      lst.addAll(steps);
      lst.add("-");
      v+=rec(lst, plus_left, minus_left-1, eq_left, target_score);
    }
    if (eq_left > 0)
    {
      ArrayList<String> lst = new ArrayList<String>();
      lst.addAll(steps);
      lst.add("=");
      v+=rec(lst, plus_left, minus_left, eq_left-1, target_score);
    }

    //System.out.println(v);



    return v;
  }

  public List<Character> getTrack()
  {   

    Point cur = new Point(0, 0);
    TreeSet<Point> visit = new TreeSet<>();
    ArrayList<Character> list = new ArrayList<>();

    while(true)
    {

      Point next = null;
      if (cur.equals(new Point(0,0)))
      {
        next = new Point(1,0);
      }
      else
      for(Point p : track.getAdj(cur, false))
      {
        if (track.get(p) != ' ')
        if (!visit.contains(p))
        {
          next=p;
        }
      }
      visit.add(next);

      cur = next;

      // Operate on point


      char track_char = track.get(cur);
      list.add(track_char);

      if (track_char == 'S')
      {
        break;
      }

    }
    return list;

  }
  public long sim3(List<Character> track_list, List<String> steps, int loops)
  {
    long power = 10;
    long score = 0;
    int r =0;

    for(int loop = 0; loop < loops; loop++)
    {
      for(char track_char : track_list)
      {
        int idx = (r % steps.size());
        String step = steps.get(idx);

        if (track_char == '+') power++;
        else if (track_char == '-') power--;
        else
        {
          if (step.equals("+")) power++;
          if (step.equals("-")) power--;
        }

        score += power;

        r++;
        if (track_char == 'S')
        {
          break;
        }

      }

    }

    return score;

  }


  public long sim2(List<String> steps, int loops)
  {
    long power = 10;
    long score = 0;
    int r =0;

    for(int loop = 0; loop < loops; loop++)
    {
      Point cur = new Point(0, 0);
      TreeSet<Point> visit = new TreeSet<>();

      while(true)
      {

        Point next = null;
        if (cur.equals(new Point(0,0)))
        {
          next = new Point(1,0);
        }
        else
        for(Point p : track.getAdj(cur, false))
        {
          if (track.get(p) != ' ')
          if (!visit.contains(p))
          {
            next=p;
          }
        }
        visit.add(next);

        cur = next;

        // Operate on point
        //System.out.println(cur);
        char track_char = track.get(cur);
        int idx = (r % steps.size());
        String step = steps.get(idx);

        if (track_char == '+') power++;
        else if (track_char == '-') power--;
        else
        {
          if (step.equals("+")) power++;
          if (step.equals("-")) power--;
        }

        score += power;

        r++;
        if (track_char == 'S')
        {
          break;
        }

      }

    }

    return score;

  }

  public TreeMap<String, Long> sim(TreeMap<String, List<String>> input, int rounds)
  {
    TreeMap<String, Long> power = new TreeMap<>();
    TreeMap<String, Long> score = new TreeMap<>();
    for(String s : input.keySet())
    {
      power.put(s, 10L);
      score.put(s, 0L);
    }
    for(int r =0; r<rounds; r++)
    {
      for(String s : input.keySet())
      {
        int idx = (r % input.get(s).size());
        String sym = input.get(s).get(idx);
        if (sym.equals("+"))
        {
          power.put(s, power.get(s) + 1);
        }
        if (sym.equals("-"))
        {
          power.put(s, power.get(s) - 1);
        }

        score.put(s, score.get(s) + power.get(s));
      }

    }
    return score;


  }
  Map2D<Character> track;

  public TreeMap<String, List<String> > parse(Scanner scan)
  {
    TreeMap<String, List<String> > map =new TreeMap<>();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      if (line.trim().length()==0) break;
      String name = line.split(":")[0];
      String lst_str = line.split(":")[1];

      map.put(name, Tok.en(lst_str, ","));

    }
    track = new Map2D<Character>(' ');
    MapLoad.loadMap(track, scan);

    return map;

  }
}
