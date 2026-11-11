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

  public class Node
  {
    TreeMap<String,String> input_map;
    final int id;
    final String plug;
    final String left;
    final String right;
    final String data;

    Node c_left;
    Node c_right;
    
    public Node(String input)
    {
      input_map = new TreeMap<>();
      List<String> elements = Tok.en(input,",");
      for(String ele : elements)
      {
        ele=ele.trim();
        String key = Tok.en(ele,"=").get(0);
        String value = Tok.en(ele,"=").get(1);

        input_map.put(key, value);

      }
      id = Integer.parseInt(input_map.get("id"));
      plug = input_map.get("plug");
      left = input_map.get("leftSocket");
      right = input_map.get("rightSocket");
      data = input_map.get("data");
    }

  }

  // 0 - no connection
  // 1 - weak connection
  // 2 - strong connection
  public int checkConn(String a, String b)
  {
    if (a.equals(b)) return 2;
    TreeSet<String> words = new TreeSet<>();

    words.addAll(Tok.en(a, " "));
    words.addAll(Tok.en(b, " "));
    if (words.size() == 3) return 1;
    if (words.size() == 4) return 0;

    E.er("nonsense " + a + " / " + b);

    return 0;



  }


  TreeMap<Integer, Node> nodes;

  public void readNodes(Scanner scan)
  {
    nodes = new TreeMap<>();

    while(scan.hasNextLine())
    {
      String line = scan.nextLine();

      Node n = new Node(line);
      nodes.put(n.id, n);
    }

  }
  public Node buildTree1(int min_con)
  {
    Node root = null;

    for(Node n : nodes.values())
    {
      if (root == null)
      {
        root = n;
      }
      else
      {
        if (!hangNode1(n, root, min_con))
        {
          E.er("Unable to hang node " + n.id);
        }
      }
    }
    return root;
  }
  public boolean hangNode1(Node add, Node cur, int min_con)
  {
    if (cur.c_left == null)
    {
      if (checkConn(cur.left, add.plug) >= min_con)
      {
        cur.c_left = add;
        return true;
      }
    }
    else
    {
      if (hangNode1(add, cur.c_left, min_con)) return true;
    }
    if (cur.c_right == null)
    {
      if (checkConn(cur.right, add.plug) >= min_con)
      {
        cur.c_right = add;
        return true;
      }
    }
    else
    {
      if (hangNode1(add, cur.c_right, min_con)) return true;
    }
    return false;
  }
  
  public Node buildTree3()
  {
    Node root = null;

    for(Node n : nodes.values())
    {
      if (root == null)
      {
        root = n;
      }
      else
      {
        Node add = hangNode3(n, root);
        if (add != null)
        {
          E.er("Unable to hang node " + add.id);
        }
      }
    }
    return root;
  }

  // returns node to hang
  public Node hangNode3(Node add, Node cur)
  {
    if (cur.c_left == null)
    {
      if (checkConn(cur.left, add.plug) >= 1)
      {
        System.out.println("Hanging node " + add.id + " on " + cur.id + " left");
        cur.c_left = add;
        return null;
      }
    }
    else
    {
      Node broke = null;
      if (checkConn(cur.left, cur.c_left.plug) == 1)
      if (checkConn(cur.left, add.plug) == 2)
      { // Break
        broke = cur.c_left;
        System.out.println("Replacing " + broke.id + " on " + cur.id + " left with " + add.id);
        cur.c_left = add;
        add=broke;
      }

      if (broke == null)
      {
        add = hangNode3(add, cur.c_left);
        if (add == null) return add;
      }
    }
    if (cur.c_right == null)
    {
      if (checkConn(cur.right, add.plug) >= 1)
      {
        System.out.println("Hanging node " + add.id + " on " + cur.id + " left");
        cur.c_right = add;
        return null;
      }
    }
    else
    {
      Node broke = null;
      if (checkConn(cur.right, cur.c_right.plug) == 1)
      if (checkConn(cur.right, add.plug) == 2)
      { // Break
        broke = cur.c_right;
        System.out.println("Replacing " + broke.id + " on " + cur.id + " right with " + add.id);
        cur.c_right = add;
        add=broke;
      }
      if (broke == null)
      {
        add = hangNode3(add, cur.c_right);
        if (add == null) return add;
      }
    }
    return add;
  }


  public List<Integer> listTree(Node n)
  {
    LinkedList<Integer> lst = new LinkedList<>();
    if (n.c_left != null) lst.addAll(listTree(n.c_left));
    lst.add(n.id);
    if (n.c_right != null) lst.addAll(listTree(n.c_right));

    return lst;
  }

  public String Part1(Scanner scan)
  {
    readNodes(scan);

    Node root = buildTree1(2);
    List<Integer> lst = listTree(root);

    long sum =0;
    long idx = 1;
    System.out.println("List: " + lst);
    for(int id : lst)
    {
      sum += id * idx;
      idx++;
    }
    return "" + sum;
  }

  public String Part2(Scanner scan)
  {
    readNodes(scan);

    Node root = buildTree1(1);
    List<Integer> lst = listTree(root);

    long sum =0;
    long idx = 1;
    System.out.println("List: " + lst);
    for(int id : lst)
    {
      sum += id * idx;
      idx++;
    }
    return "" + sum;

  }
  public String Part3(Scanner scan)
  {
    readNodes(scan);

    Node root = buildTree3();
    List<Integer> lst = listTree(root);

    long sum =0;
    long idx = 1;
    System.out.println("List: " + lst);
    for(int id : lst)
    {
      sum += id * idx;
      idx++;
    }
    return "" + sum;
  }
}
