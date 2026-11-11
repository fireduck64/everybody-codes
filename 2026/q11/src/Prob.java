import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;


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

  int portals;
  TreeMap<Integer, TreeSet<Integer> > links=new TreeMap<>();
  public int[][] floyd()
  {
    return floyd(new TreeMap<Integer, TreeSet<Integer>>());
  }

  public int[][] floyd(TreeMap<Integer, TreeSet<Integer> > add_links)
  {

    int[][] dist=new int[portals+1][portals+1];

    for(int i=1; i<=portals; i++)
    for(int j=1; j<=portals; j++)
    {
      dist[i][j]=1000000;
    }

    for(int a : links.keySet())
    {
      dist[a][a]=0;
      for(int b : links.get(a))
      {
        dist[a][b] = 1;
      }
    }

    for(int a : add_links.keySet())
    {
      dist[a][a]=0;
      for(int b : add_links.get(a))
      {
        dist[a][b] = 1;
        dist[b][a] = 1;
      }
    }


    for(int k=1; k<=portals; k++)
    for(int i=1; i<=portals; i++)
    for(int j=1; j<=portals; j++)
    {
      if (dist[i][j] > dist[i][k] + dist[k][j])
      {
        dist[i][j] = dist[i][k] + dist[k][j];
      }
    }
    return dist;

  }

  public String Part1(Scanner scan)
  {
    links.clear();

    portals = Tok.ent(scan.nextLine(), " ").get(0);

    for(int i=1; i<=portals; i++)
    {
      links.put(i, new TreeSet<Integer>());
    }
    for(int i=1; i<=portals; i++)
    {
      int p=i-1;
      int n=i+1;
      if (p<1) p+=portals;
      if (n>portals) n-=portals;
      links.get(i).add(p);
      links.get(i).add(n);
    }


    int before = getStrain(floyd());
    scan.nextLine();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      List<Integer> lst = Tok.ent(line, " ");

      int a = lst.get(0);
      int b = lst.get(1);
      links.get(a).add(b);
      links.get(b).add(a);
      

    }
    int after = getStrain(floyd());
    int res = before - after;


    return "" + res;
  }

  public int getStrain(int[][] dist)
  {
    int sum = 0;
    for(int i=1; i<=portals; i++)
    {
      for(int j=1; j<=portals; j++)
      {
        sum += dist[i][j];
      }

    }

    return sum;


  }

  public String Part2(Scanner scan)
  {
    links.clear();
    portals = Tok.ent(scan.nextLine(), " ").get(0);

    for(int i=1; i<=portals; i++)
    {
      links.put(i, new TreeSet<Integer>());
    }
    for(int i=1; i<=portals; i++)
    {
      for(int k = -3; k<=3; k++)
      {
        if (k != 0)
        {
          int n = i+k;
          while(n < 1) n+=portals;
          while(n > portals) n-=portals;
          links.get(i).add(n);
        }

      }
    }

    int before = getStrain(floyd());

    scan.nextLine();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      List<Integer> lst = Tok.ent(line, " ");

      int a = lst.get(0);
      int b = lst.get(1);
      links.get(a).add(b);
      links.get(b).add(a);
    }
    //int res = before - after;
    int low = before;
    for(int i=1; i<=portals; i++)
    for(int j=i+1; j<=portals; j++)
    {
      TreeMap<Integer, TreeSet<Integer> > add_links = new TreeMap<>();
      add_links.put(i, new TreeSet<>());
      add_links.put(j, new TreeSet<>());
      add_links.get(i).add(j);
      add_links.get(j).add(i);

      int meow = getStrain(floyd(add_links));

      //System.out.println("Path " + i + " " + j + " " + meow + " " + before);

      low = Math.min(meow, low);

    }
    int res = before - low;

    return "" + res;

  }
  public String Part3(Scanner scan)
  {
    links.clear();
    portals = Tok.ent(scan.nextLine(), " ").get(0);

    if (portals % 8 != 0) E.er();

    for(int i=1; i<=portals; i++)
    {
      links.put(i, new TreeSet<Integer>());
    }
    for(int i=1; i<=portals; i++)
    {
      for(int k = -3; k<=3; k++)
      {
        if (k != 0)
        {
          int n = i+k;
          while(n < 1) n+=portals;
          while(n > portals) n-=portals;
          links.get(i).add(n);
        }

      }
    }

    int before = getStrain(floyd());

    scan.nextLine();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      List<Integer> lst = Tok.ent(line, " ");

      int a = lst.get(0);
      int b = lst.get(1);
      links.get(a).add(b);
      links.get(b).add(a);
    }

    System.out.println("Before: " + before);
    //int res = before - after;
    int low = before;
    int dx = portals/8;
    for(int i=1; i<portals/8; i++)
    {
      // i starting position
      TreeSet<Integer> pos = new TreeSet<>();
      for(int j=0; j<8; j++)
      {
        pos.add(i + j*dx);
      }
      TreeMap<Integer, TreeSet<Integer> > add_links = new TreeMap<>();
      for(int k=1; k<portals; k++)
      {
        add_links.put(k, new TreeSet<Integer>());
      }

      if (pos.size() != 8) E.er();

      ArrayList<Integer> pos_lst = new ArrayList<>();
      pos_lst.addAll(pos);

      int meow = rec(pos, add_links);


      low = Math.min(meow, low);

    }
    int res = before - low;

    return "" + res;
  }

  int rec(TreeSet<Integer> pos, TreeMap<Integer, TreeSet<Integer> > add_links)
  {
    if (pos.size() == 0)
    {
      return getStrain(floyd(add_links));
    }
    
    int a = pos.pollFirst();
    int low = 10000000;
    for(int b : pos)
    {
      add_links.get(a).add(b);
      add_links.get(b).add(a);

      TreeSet<Integer> npos = new TreeSet<>();
      npos.addAll(pos);
      npos.remove(b);
      low = Math.min(low, rec(npos, add_links));

      add_links.get(a).remove(b);
      add_links.get(b).remove(a);
    }

    return low;
  }
}
