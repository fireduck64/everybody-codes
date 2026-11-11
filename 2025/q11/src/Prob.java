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
    ArrayList<Long> birds = new ArrayList<>();
    while(scan.hasNextLong()) birds.add(scan.nextLong());

    System.out.println(birds);
    boolean p1_done=false;
    for(int i=0; i<10; i++)
    {
      int round = i+1;
      //System.out.println("Round " + round);
      if (!p1_done)
      {
        p1_done = !phase1(birds);
        //System.out.println("P1" + birds);
      }
      if (p1_done)
      {
        phase2(birds);
        //System.out.println("P2" + birds);
      }

    }

    System.out.println(birds);

    return "" + getSum(birds);
  }

  public int getSum(ArrayList<Long> birds)
  {
    int sum =0;
    for(int i=0; i<birds.size(); i++)
    {
      sum += (i+1) * birds.get(i);

    }
    return sum;

  }

  public String Part2(Scanner scan)
  {
    ArrayList<Long> birds = new ArrayList<>();
    while(scan.hasNextLong()) birds.add(scan.nextLong());

    System.out.println(birds);
    
    boolean p1_done=false;
    boolean p2_done=false;
    int round=0;
    while(!p2_done)
    {
      if (!p1_done)
      {
        p1_done = !phase1(birds);
        if (p1_done)
        {
          System.out.println("ph1 - " + round);
        }
      }
      if (p1_done)
      {
        p2_done = !phase2(birds);
      }
      if (!p2_done) round++;

    }


    return "" + round;
  }
  public String Part3(Scanner scan)
  {
    
    ArrayList<Long> birds = new ArrayList<>();
    while(scan.hasNextLong()) birds.add(scan.nextLong());

    long p1 = 0;
    boolean p1_done=false;
    while(!p1_done)
    {
      p1_done = !phase1(birds);
      if (!p1_done) p1++;
    }
    System.out.println("phase1: " + p1);

    long sum = 0;
    for(long v : birds) sum+=v;
    long target = sum / birds.size();
    if (sum % birds.size() != 0) E.er();

    long flow =0;
    System.out.println(birds);
    for(int i=0; i<birds.size()-1; i++)
    {
      long cur = birds.get(i);
      long next = birds.get(i+1);
      if (cur > target) E.er();
      long take = target - cur;
      //System.out.println("Cur: " + cur + " next: " + next + " take: " + take);

      birds.set(i, target);
      birds.set(i+1, next-take);
      flow = Math.max(flow, take);
    }

    long p = flow+p1;
    return "P3: " + p;
  }

  private void applyAndReset(ArrayList<Long> birds, ArrayList<Long> adds)
  {
      for(int i=0; i<birds.size(); i++)
      {
        birds.set(i, birds.get(i) + adds.get(i));
      }
      adds.clear(); while(adds.size() < birds.size()) adds.add(0L);

  }

  public boolean phase1(ArrayList<Long> birds)
  {
    int moves=0;

    //phase 1
    //while(moves > 0)
    {
      moves=0;
      for(int i=0; i<birds.size()-1; i++)
      {
        int j = i+1;
        if (birds.get(j) < birds.get(i))
        {
          birds.set(i, birds.get(i)-1);
          birds.set(j, birds.get(j)+1);
          moves++;
        }
      } 
    }
    return (moves > 0);
  }

  public long phase1c(ArrayList<Long> birds)
  {
    long moves=0;
    
    while(true)
    {
      long cnt = 0;
      for(int i=0; i<birds.size()-1; i++)
      {
        int j = i+1;
        if (birds.get(j) < birds.get(i))
        {
          long acts = (birds.get(i) - birds.get(j)) / 2;
          cnt = Math.max(cnt, acts);
          birds.set(i, birds.get(i)-acts);
          birds.set(j, birds.get(j)+acts);
        }
      } 
      if (cnt == 0L) break;
      moves+=cnt;
      //System.out.println(" ph1 cnt: " + cnt);
    }
    return moves;
  }
  public long phase2c(ArrayList<Long> birds)
  {
    long moves=0;

    {
      for(int i=birds.size()-2; i>=0; i--)
      {
        int j = i+1;
        if (birds.get(j) > birds.get(i))
        {
          long acts = (birds.get(j) - birds.get(i)) / 2;
          moves = Math.max(moves, acts);

          birds.set(i, birds.get(i)+acts);
          birds.set(j, birds.get(j)-acts);
        }
      } 
    }
    return moves;
  }


  public boolean phase2(ArrayList<Long> birds)
  {

    //phase 2
    int moves=0;
    {
      moves=0;
      for(int i=0; i<birds.size()-1; i++)
      {
        int j = i+1;
        if (birds.get(j) > birds.get(i))
        {

          birds.set(i, birds.get(i)+1);
          birds.set(j, birds.get(j)-1);
          moves++;
        }
      }
    }

    return (moves>0);


  }
}
