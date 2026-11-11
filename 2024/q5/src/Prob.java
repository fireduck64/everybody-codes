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
    ArrayList< ArrayList< Integer> > board = readBoard(scan);

    int col = 0;
    for(int round=0; round<10; round++)
    {
      round(board, col);
      col = (col + 1) % 4;
    }

    return getShout(board);
  }

  public  ArrayList< ArrayList< Integer> > readBoard(Scanner scan)
  {
    ArrayList< ArrayList< Integer> > board = new ArrayList<>();
    for(int i=0; i<4; i++)
    {
      board.add(new ArrayList<Integer>());
    }
    int col = 0;
    for(int x : In.ints(scan))
    {
      board.get(col).add(x);
      col = (col + 1) % 4;
    }
    return board;

  }

  public String getShout(List<ArrayList<Integer>> board)
  {
    String s="";
    for(int i=0; i<board.size(); i++)
    {
      s = s + board.get(i).get(0);
    }
    return s;

  }

  public void round(List<ArrayList<Integer>> board, int col)
  {
      int clap = board.get(col).get(0);
      board.get(col).remove(0);
      int dance_col = (col+1)%4;
      ArrayList<Integer> dc = board.get(dance_col);
      int clap_rem = clap;
      // complete loops

      while(clap_rem > (dc.size() * 2))
      {
        clap_rem -= dc.size() * 2;
      }

      if (clap_rem <= dc.size())
      {
        dc.add(clap_rem-1, clap);
      }
      else
      {
        clap_rem -= dc.size();
        int idx = dc.size() - (clap_rem )+1;
        dc.add(idx, clap);
      }

  }

  

  public String Part2(Scanner scan)
  {
    ArrayList< ArrayList< Integer> > board = readBoard(scan);

    int col = 0;
    TreeMap<String, Integer> counts = new TreeMap<>();

    long rounds = 0;
    while(true)
    {
      round(board, col);
      rounds++;

      String shout = getShout(board);
      //System.out.println("r: " + rounds + " " + shout);
      //if (rounds > 10) return "";

      if (!counts.containsKey(shout))
      {
        counts.put(shout, 0);
      }
      counts.put( shout, counts.get(shout) + 1);
      if (counts.get(shout) == 2024)
      {
        long n = Long.parseLong(shout);
        System.out.println(n);
        System.out.println(rounds);

        long a = n * rounds;
        return "" + a;

      }
      col = (col + 1) % 4;
    }


  }
  public String Part3(Scanner scan)
  {
    ArrayList< ArrayList< Integer> > board = readBoard(scan);

    int col = 0;
    long high=0;
    TreeSet<String> states = new TreeSet<>();

    long rounds = 0;
    while(true)
    {
      round(board, col);

      String shout = getShout(board);

      String state = board.toString();
      if (states.contains(state)) return "" + high;
      states.add(state);

      high = Math.max( high, Long.parseLong( getShout(board)));

      col = (col + 1) % 4;
    }



  }


  
}
