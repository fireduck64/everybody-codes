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
  TreeMap<String, Node> map;


  public String Part1(Scanner scan)
  {
    map = loadTree(scan);

    List<List<String> > paths = getFruitPaths("RR", ImmutableList.of());
    System.out.println(paths);
    TreeMap<Integer, Integer> counts=  new TreeMap<>();

    for(List<String> l : paths)
    {
      int sz = l.size();
      if (!counts.containsKey(sz))
      {
        counts.put(sz, 0);

      }
      counts.put(sz, counts.get(sz) + 1);

    }
    int low=1000;
    int low_len = 1000;
    for(int n : counts.keySet())
    {
      int cnt = counts.get(n);
      if (cnt < low)
      {
        low = Math.min(cnt, low);
        low_len = n;
      }

    }
    for(List<String> l : paths)
    {
      int sz = l.size();
      if (sz == low_len)
      {
        String p2 = "";
        for(String  s : l)
        {
          p2 += s.charAt(0);
        }
        p2 += "@";
        return p2;
        //return l.toString().replace(";","").replace(" ","");

      }

    }
    System.out.println(counts);

    

    return "";
  }

  public String Part2(Scanner scan)
  {
    return Part1(scan);

  }
  public String Part3(Scanner scan)
  {

    return Part1(scan);
  }

  public TreeMap<String, Node> loadTree(Scanner scan)
  {
    TreeMap<String, Node> map = new TreeMap<>();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      String name = line.split(":")[0];
      List<String> subs = Tok.en(line.split(":")[1], ",");

      Node n = new Node(name);
      n.children.addAll(subs);

      map.put(name, n);

    }
    return map;

  }

  

  public class Node
  {
    String name;

    public TreeSet<String> children = new TreeSet<>();

    public Node(String n)
    {
      this.name = n;

    }
  }

  public List<List<String>> getFruitPaths(String name, List<String> path_so_far)
  {
    for(String p : path_so_far)
    {
      if (p.equals(name)) return ImmutableList.of();

    }
    if (name.equals("@"))
    {
      return ImmutableList.of(path_so_far);
    }

    List<List<String> > paths = new LinkedList<>();

    
    Node n = map.get(name);
    if (n == null)
    {
      return ImmutableList.of();
      //E.er("name: " + name);

    }
    List<String> new_path = new LinkedList<>();
    new_path.addAll(path_so_far);
    new_path.add(name);

    for(String child : n.children)
    {
      paths.addAll( getFruitPaths(child, new_path) );

    }

    return paths;

  }

  

  
}
