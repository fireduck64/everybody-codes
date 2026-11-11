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
    LinkedList<Map<String, Long> > lst = new LinkedList<>();
    for(String line : In.lines(scan))
    {
      Map<String, Long> m = readMap(line);
      //System.out.println(m);
      lst.add(m);

    }

    TreeMap<Long, Long> scores=new TreeMap<>();
    for(Map<String, Long> m : lst)
    {
      long id = m.get("id");
      long time = m.get("time");

      if (!scores.containsKey(id)) scores.put(id, 0L);
      scores.put(id, scores.get(id) + time);
    }
    TreeMap<Long, Long> order = new TreeMap<>();
    for(long id : scores.keySet())
    {
      order.put(scores.get(id), id);

    }
    long p = 0;
    int ord = 1;
    for(long score : order.keySet())
    {
      p += ord * score;
      ord++;

    }

    return "" + p;
  }

  public String Part2(Scanner scan)
  {
    LinkedList<Map<String, Long> > lst = new LinkedList<>();
    for(String line : In.lines(scan))
    {
      Map<String, Long> m = readMap(line);
      lst.add(m);

    }

    TreeMap<Long, Crab> crabs = new TreeMap<>();
    for(Map<String, Long> m : lst)
    {
      long id = m.get("id");
      long time = m.get("time");
      long quest = m.get("quest");
      long part = m.get("part");

      String qp = quest + "/" + part;
      if (crabs.get(id) == null) crabs.put(id, new Crab());

      crabs.get(id).time_sum += time;
      crabs.get(id).part_set.add(qp);
    }
    TreeMap<Long, Crab> order = new TreeMap<>();
    for(long id : crabs.keySet())
    {
      Crab crab = crabs.get(id);
      long score = -100000000L * crab.part_set.size() + crab.time_sum;
      order.put(score, crab);

    }
    long p = 0;
    int ord = 1;
    for(long score : order.keySet())
    {
      p += ord * order.get(score).time_sum;
      ord++;

    }



    return "" + p;
  }

  public class Crab
  {
    long time_sum;
    TreeSet<String> part_set;
    long id;
    public Crab()
    {
      time_sum = 0;
      part_set = new TreeSet<>();
      part_time = new TreeMap<>();
    }

    TreeMap<String, Long> part_time;
    long p3_score;

    public String toString()
    {
      
      return "";
    }

    public long getP3Score(TreeMap<String, ArrayList<Long> > qp_times)
    {
      long score = 0;
      for(String qp : part_time.keySet())
      {
        long tm = part_time.get(qp);
        ArrayList<Long> time_lst = new ArrayList<>();
        time_lst.addAll(qp_times.get(qp));
        Collections.sort(time_lst);

        long part = Integer.parseInt(Tok.en(qp, "/").get(1));

        for(int idx = 0; idx<time_lst.size(); idx++)
        {
          if (tm == time_lst.get(idx))
          {
            long add = part * 50 - idx;
            if (add > 0)
            { 
              score+=add;
              //System.out.println("id " + qp + " " + add);
            }
            break;
          }
        }
      }
      p3_score = score;
      return score;

    }

  }

  public String Part3(Scanner scan)
  {
    ArrayList<Map<String, Long> > lst = new ArrayList<>();
    for(String line : In.lines(scan))
    {
      Map<String, Long> m = readMap(line);
      lst.add(m);

    }

    //for(int k=0; k<lst.size(); k++)
    for(int i=0; i<lst.size(); i++)
    for(int j=i+1; j<lst.size(); j++)
    {
      Map<String, Long> a = lst.get(i);
      Map<String, Long> b = lst.get(j);
      if (a.get("part") > b.get("part"))
      {
        lst.set(i,b);
        lst.set(j,a);
      }
    }
    System.out.println("Sort by part done");
    TreeMap<String, ArrayList<Long> > qp_times=new TreeMap<>();
    TreeMap<Long, Crab> crabs = new TreeMap<>();
    for(Map<String, Long> m : lst)
    {
      long id = m.get("id");
      long time = m.get("time");
      long quest = m.get("quest");
      long part = m.get("part");

      String qp = quest + "/" + part;
      if (crabs.get(id) == null) crabs.put(id, new Crab());
      Crab crab = crabs.get(id);
      crab.id = id;
      if (part > 1)
      {
        long pp = part - 1;
        String qp_prev = quest + "/" + pp;
        time += crab.part_time.get(qp_prev);
      }
      crab.part_time.put(qp, time);
      if (!qp_times.containsKey(qp)) qp_times.put(qp, new ArrayList<>());
      qp_times.get(qp).add(time);
    }

    ArrayList<Crab> order = new ArrayList<>();

    for(Crab c : crabs.values())
    {
      c.getP3Score(qp_times);

      order.add(c);
    }

    for(int k=0; k<order.size(); k++)
    for(int i=0; i<order.size(); i++)
    for(int j=i+1; j<order.size(); j++)
    {
      Crab a = order.get(i);
      Crab b = order.get(j);
      if (b.p3_score > a.p3_score)
      {
        order.set(i,b);
        order.set(j,a);
      }
    }

    long last_ord=0;
    long last_score=0;
    long p=0;
    for(int i=0; i<order.size(); i++)
    {
      Crab a = order.get(i);
      if (a.p3_score != last_score)
      {
        last_ord=i+1;
        last_score = a.p3_score;
      }
      p += last_ord * a.p3_score;
      //System.out.println(" " + last_ord + "*" + a.p3_score);

    }

    
    


    return "" + p;
  }

  public TreeMap<String, Long> readMap(String line)
  {
    TreeMap<String, Long> m = new TreeMap<>();
    for(String item : Tok.en(line, " "))
    {
      String k = Tok.en(item, "=").get(0);

      long v = Long.parseLong( Tok.en(item,"=").get(1));
      m.put(k,v);
    }

    return m;

  }
}
