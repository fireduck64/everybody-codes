import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import duckutil.Pair;

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

  ArrayList<ArrayList<String> > wheels;
  List<Integer> clicks;

  public void readInput(Scanner scan)
  {
    clicks=Tok.ent(scan.nextLine(), ",");
    scan.nextLine();
    wheels=new ArrayList<>();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      int idx =0;
      int pos= 0;
      while(pos < line.length())
      {
        String cat = line.substring(pos, pos+3);
        if (cat.trim().length() == 3)
        {
          while (idx >= wheels.size())
          {
            wheels.add(new ArrayList<String>());
          }
          wheels.get(idx).add(cat);
        }

        idx++;
        pos+=4;

      }

    }
    System.out.println("clicks: " + clicks);
    System.out.println("wheels: " + wheels);

  }

  ArrayList<Integer> left(ArrayList<Integer> start, int dir)
  {
    ArrayList<Integer> out = new ArrayList<>();

    for(int i=0; i<clicks.size(); i++)
    {
      int b = start.get(i) + dir;
      if (b < 0) b = wheels.get(i).size()-1;
      if (b >= wheels.get(i).size()) b = 0;
      out.add(b);
    }

    return out;
  }

  ArrayList<Integer> step(ArrayList<Integer> start, long spins)
  {
    ArrayList<Integer> out = new ArrayList<>();
    for(int i=0; i<clicks.size(); i++)
    {
      int a = 0;
      if (start != null) a = start.get(i);
      int b = (int)((a + spins * clicks.get(i)) % wheels.get(i).size());

      out.add(b);
    }
    return out;

  }

  long getScore(ArrayList<Integer> w)
  {
    long score = 0;
    TreeMap<Character, Integer> counts = new TreeMap<>();
    String face = getFace(w);
    for(int i=0; i<face.length(); i+=2)
    {
      char z = face.charAt(i);
      if (!counts.containsKey(z)) counts.put(z, 0);
      counts.put(z, counts.get(z) + 1);
    }
    for(int v : counts.values())
    {
      if (v >= 3)
      {
        score += v-2;
      }

    }
    //System.out.println("Score " + face + " " + counts + " " + score);
    return score;

  }
  String getFace(ArrayList<Integer> w)
  {
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<wheels.size(); i++)
    {
      if (i > 0) sb.append(" ");
      sb.append(wheels.get(i).get(w.get(i)));

    }

    return sb.toString();

  }

  public String Part1(Scanner scan)
  {
    readInput(scan);

    ArrayList<Integer> w = step(null, 100);
    System.out.println(w);

    return getFace(w);
  }

  public String Part2(Scanner scan)
  {
    readInput(scan);
    ArrayList<Integer> w = null;
    long total_score=0;
    // Maps W to (step, total_score)
    TreeMap<String, Pair<Long, Long> > score_map=new TreeMap<>();

    long s = 0;
    long target = 202420242024L;

    while(s < target)
    {
      w = step(w, 1);
      long score = getScore(w);
      total_score += score;

      String key = w.toString();
      if (score_map.containsKey(key))
      {
        long step_diff = s - score_map.get(key).getA();
        long score_diff = total_score - score_map.get(key).getB();

        System.out.println("Stepdiff: " + step_diff);

        long jumps = (target - s) / step_diff;

        total_score += jumps * score_diff;
        s += jumps * step_diff;

        score_map.clear();

      }
      score_map.put(key, new Pair<Long,Long>(s, total_score));

      s++;
      System.out.println("Pull: " + s + " - " + total_score);

    }

    return "" + total_score;
  }
  public String Part3(Scanner scan)
  {
    memo.clear();
    readInput(scan);
    ArrayList<Integer> start = new ArrayList<>();
    for(int i=0; i<wheels.size(); i++) start.add(0);


    Pair<Long, Long> ans = rec(start, 256);

    return "" + ans.getB() + " " + ans.getA();
  }


  TreeMap<String, Pair<Long, Long> > memo = new TreeMap();
  public Pair<Long, Long> rec(ArrayList<Integer> w, int rem_pulls)
  {
    
    String key = w.toString() +"/" + rem_pulls;

    if (memo.containsKey(key)) return memo.get(key);
    //System.out.println(key);

    if (rem_pulls == 0) return new Pair<>(0L, 0L);

    Pair<Long, Long> ans = null;
    for(int dir=-1; dir<=1; dir++)
    {
      ArrayList<Integer> w2 = step(left(w, dir), 1);
      long s2 = getScore(w2);

      Pair<Long, Long> sub = rec(w2, rem_pulls-1);
      sub = new Pair<>(sub.getA() + s2, sub.getB() + s2);

      if (ans == null) ans = sub;
      else ans = updateMinMax(ans, sub);

    }
    memo.put(key, ans);

    return ans;

  }


  public Pair<Long, Long> updateMinMax(Pair<Long, Long> a, Pair<Long, Long> b)
  {
    long min = Math.min(a.getA(), b.getA());
    long max = Math.max(a.getB(), b.getB());

    return new Pair<>(min, max);

  }
}
