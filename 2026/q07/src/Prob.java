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

  TreeMap<Integer, Cloud> clouds=new TreeMap<>();


  HashMap<String, TreeMap<Integer, Long> > cache=new HashMap<>();

  TreeMap<Integer, Long> getAddition(int id, long pulse)
  {

    String key = "" + id + "/" + pulse;
    if (cache.containsKey(key)) cache.get(key);

    TreeMap<Integer, Long> ans = new TreeMap<>();

    if (pulse == 0L) E.er();
    ans.put(id, pulse);

    if (pulse > 1L)
    {
      Cloud c = clouds.get(id);
      for(int sub : c.conns)
      {
        addMerge(ans, getAddition(sub, pulse-1L));
      }
      //cache.put(key, ans);

    }
    return ans;

  }

  long EXP_MAX=1000000L;

  long resolveP3(int start_id, long start_pulse)
  {
    //System.out.println("------------------------------------");
    //System.out.println("Working target : " + start_id);

    // Maps id to pulses and counts
    TreeMap<Integer, TreeMap<Long, Long> > next=new TreeMap<>();

    addPulse(next, start_id, start_pulse, 1L);

    while(next.size() > 0)
    {
      TreeMap<Integer, TreeMap<Long, Long> > last = next;
      next = new TreeMap<>();
      //System.out.println("Current stage: " + last);

      for(int id : last.keySet())
      if (!clouds.get(id).done_exploded)
      {
        for(Map.Entry<Long,Long> me : last.get(id).entrySet())
        {
          long pulse=me.getKey();
          long count=me.getValue();

          clouds.get(id).energy += pulse * count;

          for(int sub : clouds.get(id).conns)
          {
            addPulse(next, sub, pulse-1, count);
          }

        }
        for(Cloud c : clouds.values())
        {
          if ((!c.done_exploded) && (c.energy >= EXP_MAX))
          {
            //System.out.println("  exploded: " + c.id);
            
            c.done_exploded=true;
            for(int sub : c.conns)
            {
              addPulse(next, sub, 10L, 1L);
            }
          }

        }

      }
    }

    long p = 0L;
    for(Cloud c : clouds.values())
    {
      if (!c.done_exploded) p+= c.energy;
    }
    return p;

  }

  public void addPulse(TreeMap<Integer, TreeMap<Long, Long> > map, int id, long pulse, long count)
  {
    if (pulse == 0L) return;
    if (count == 0L) return;
    if (clouds.get(id).done_exploded) return;
    
    if (!map.containsKey(id)) map.put(id, new TreeMap<Long, Long>());

    long p =0L;
    if (map.get(id).containsKey(pulse)) p = map.get(id).get(pulse);
    map.get(id).put(pulse, p+count);

  }


  public class Cloud
  {
    int id;
    long energy;
    TreeSet<Integer> conns=new TreeSet<>();
    boolean done_exploded=false;
    public Cloud(String line)
    {
      List<Integer> lst = Tok.ent(line, ",= ");
      id = lst.get(0);
      energy = lst.get(1);

      for(int i=2; i<lst.size(); i++)
      {
        conns.add(lst.get(i));

      }

    }

  }

  public String Part1(Scanner scan)
  {
    clouds.clear(); cache.clear();

    for(String line : In.lines(scan))
    {
      Cloud c = new Cloud(line);
      clouds.put(c.id, c);
    }

    int target =1;
    for(Cloud c : clouds.values())
    {   

      if (c.conns.size() > clouds.get(target).conns.size()) target = c.id;

    }

    TreeMap<Integer, Long> add = getAddition(target, 5L);

    long p = 0;
    for(Cloud c : clouds.values())
    {
      p += c.energy;
    }
    for(long v : add.values()) p+=v;

    System.out.println(add);


    return "" + p;
  }

  public String Part2(Scanner scan)
  {
    clouds.clear(); cache.clear();

    for(String line : In.lines(scan))
    {
      Cloud c = new Cloud(line);
      clouds.put(c.id, c);
    }

    int target =1;
    for(Cloud c : clouds.values())
    {   

      if (c.conns.size() > clouds.get(target).conns.size()) target = c.id;

    }

    EXP_MAX=1000000000L;
    long p = resolveP3(target, 10L);

    /*TreeMap<Integer, Long> add = getAddition(target, 10L);

    long p = 0;
    for(Cloud c : clouds.values())
    {
      p += c.energy;
    }
    for(long v : add.values()) p+=v;

    System.out.println(add);*/

    return "" + p;
  }
  public String Part3(Scanner scan)
  {
    List<String> lines = In.lines(scan);

    clouds.clear(); 
    for(String line : lines)
    {
      Cloud c = new Cloud(line);
      clouds.put(c.id, c);
    }

    TreeSet<Integer> id_set = new TreeSet<>();
    id_set.addAll(clouds.keySet());
    long best = 0;
    if (id_set.size() < 10) EXP_MAX=100L;
    else EXP_MAX=1000000L;

    for(int target : id_set)
    {
      clouds.clear(); 
      for(String line : lines)
      {
        Cloud c = new Cloud(line);
        clouds.put(c.id, c);
      }

      long p = resolveP3(target, 10L);
      //System.out.println("Target: " + target + " - " + p);

      best = Math.max(p ,best);

    }


    return "" + best;

  }

  public void addMerge(Map<Integer, Long> target, Map<Integer, Long> add)
  {
    for(int n : add.keySet())
    {
      long p =0;
      if (target.containsKey(n)) p = target.get(n);
      target.put(n, p+add.get(n));
    }
  }

}
