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
    ArrayList<DNA> lst = new ArrayList<>();

    while(scan.hasNextLine())
    {
      lst.add(new DNA(scan.nextLine()));
    }

    int a = getSim(lst.get(0), lst.get(2));
    int b = getSim(lst.get(1), lst.get(2));


    int p = a* b;

    return "" + p;
  }

  public String Part2(Scanner scan)
  {
    ArrayList<DNA> lst = new ArrayList<>();

    while(scan.hasNextLine())
    {
      lst.add(new DNA(scan.nextLine()));
    }
    long p2=0;


    for(int i=0; i<lst.size(); i++)
    for(int j=0; j<lst.size(); j++)
    for(int k=0; k<lst.size(); k++)
    {
      if (i<j)
      if (i!=j)
      if (i!=k)
      if (j!=k)
      if (isPossible(lst.get(i), lst.get(j), lst.get(k)))
      {
        //System.out.println("" + lst.get(k).id + " is child of " + lst.get(i).id + " and " + lst.get(j).id);

        p2 += getSim(lst.get(i), lst.get(k)) * getSim(lst.get(j), lst.get(k));
      }

    }

 
    return "" + p2;
  }
  public String Part3(Scanner scan)
  {
    ArrayList<DNA> lst = new ArrayList<>();

    while(scan.hasNextLine())
    {
      lst.add(new DNA(scan.nextLine()));
    }

    TreeMap<Integer, TreeSet<Integer> > map = new TreeMap<>();
    for(int i=0; i<lst.size(); i++)
    {
      map.put(i, new TreeSet<Integer>());
    }

    for(int i=0; i<lst.size(); i++)
    for(int j=0; j<lst.size(); j++)
    for(int k=0; k<lst.size(); k++)
    {
      if (i<j)
      if (i!=j)
      if (i!=k)
      if (j!=k)
      if (isPossible(lst.get(i), lst.get(j), lst.get(k)))
      {
        map.get(i).add(k);
        map.get(j).add(k);
        map.get(k).add(i);
        map.get(k).add(j);
        map.get(i).add(j);
        map.get(j).add(i);
      }
    }

    Set<Integer> big=new TreeSet<>();
    for(int i=0; i<lst.size(); i++)
    {
      Set<Integer> fam = getFamily(map, i);
      if (fam.size() > big.size()) big=fam;
    }
    long p2 = 0;
    for(int id : big)
    {
      p2 += Integer.parseInt(lst.get(id).id);


    }
 
    return "" + p2;
  }

  public Set<Integer> getFamily(Map<Integer, TreeSet<Integer> > map, int start)
  {
    TreeSet<Integer> queue = new TreeSet<>();
    queue.add(start);
    TreeSet<Integer> fam = new TreeSet<>();

    while(queue.size() >0)
    {
      int v = queue.pollFirst();

      if (!fam.contains(v))
      {
        fam.add(v);
        queue.addAll(map.get(v));
      }
    }

    return fam;


  }

  public class DNA
  {
    String id;
    String seq;

    public DNA(String line)
    {
      id = Tok.en(line, ":").get(0);
      seq = Tok.en(line, ":").get(1);

    }

  }
  public int getSim(DNA a, DNA b)
  {
    int sim=0;
    for(int idx=0; idx<a.seq.length(); idx++)
    {
      if (a.seq.charAt(idx) == b.seq.charAt(idx)) sim++;

    }
    return sim;

  }

  public boolean isPossible(DNA a, DNA b, DNA child)
  {
    for(int idx=0; idx<a.seq.length(); idx++)
    {
      char az = a.seq.charAt(idx);
      char bz = b.seq.charAt(idx);
      char cz = child.seq.charAt(idx);
      if (cz != az)
      if (cz != bz)
      {
        return false;
      }
    }

    return true;
  

  }
}
