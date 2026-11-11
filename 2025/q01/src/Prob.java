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
    String names_str = scan.nextLine();
    List<String> names = Tok.en(names_str, ",");

    scan.nextLine();
    String moves_str = scan.nextLine();
    moves_str = moves_str.replace("R","");
    moves_str = moves_str.replace("L","-");

    List<Integer> moves = Tok.ent(moves_str, ",");

    int idx = 0;
    for(int move : moves)
    {
      idx += move;
      if (idx >= names.size()) idx = names.size()-1;
      if (idx < 0) idx=0;
    }

    

    return names.get(idx);
  }

  public String Part2(Scanner scan)
  {
    String names_str = scan.nextLine();
    List<String> names = Tok.en(names_str, ",");

    scan.nextLine();
    String moves_str = scan.nextLine();
    moves_str = moves_str.replace("R","");
    moves_str = moves_str.replace("L","-");

    List<Integer> moves = Tok.ent(moves_str, ",");

    int idx = 0;
    for(int move : moves)
    {
      idx += move;
      while (idx >= names.size()) idx -= names.size();
      while (idx < 0) idx+=names.size();

    }

    return names.get(idx);
  }
  public String Part3(Scanner scan)
  {
    String names_str = scan.nextLine();
    List<String> names = Tok.en(names_str, ",");

    scan.nextLine();
    String moves_str = scan.nextLine();
    moves_str = moves_str.replace("R","");
    moves_str = moves_str.replace("L","-");

    List<Integer> moves = Tok.ent(moves_str, ",");

    for(int move : moves)
    {
      int idx = 0;
      idx += move;
      while (idx >= names.size()) idx -= names.size();
      while (idx < 0) idx+=names.size();

      String tmp_a = names.get(0);
      String tmp_b = names.get(idx);
      names.set(0, tmp_b);
      names.set(idx, tmp_a);
      //System.out.println("Swapping " + tmp_a + " and " + tmp_b);
    }

    return names.get(0);

  }
}
