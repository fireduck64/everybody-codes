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

  String shoot(String in, char bolt)
  {
    int idx=0;
    while(true)
    {
      if (idx == in.length()) return "";
      if (in.charAt(idx) != bolt) break;
      idx++;
    }

    String out = in.substring(idx + 1);
    //System.out.println("" + in + " " + bolt + " " + out);
    return out;

  }
 
  String shoot2(String in, char bolt)
  {
    if (in.length() % 2 == 1)
    {
      return in.substring(1);
    }
    
    if (in.charAt(0) != bolt)
    {
      return in.substring(1);
    }

    // so we are even and matching and need to remove these two
    // X0000X0000

    if (in.length() == 2) return "";

    int mid = in.length()/2;

    String out = in.substring(1, mid) + in.substring(mid+1);

    return out;

  }
  
  void shoot2Lst(BigList<Character> in, char bolt)
  {
    if (in.size() % 2 == 1)
    {
      in.remove(0);
      return;
    }
    
    if (in.get(0) != bolt)
    {
      in.remove(0);
      return;
    }

    // so we are even and matching and need to remove these two
    // X0000X0000

    if (in.size() == 2)
    {
      in.remove(0);
      in.remove(0);
      return;
    }

    int mid = in.size()/2;

    in.remove(mid);
    in.remove(0);

    return;
  }
  
 
 
  public String Part1(Scanner scan)
  {
    String input = scan.nextLine();

    String order="RGB";

    String line = input;
    int shot=0;
    while(line.length() > 0)
    {
      char z = order.charAt(shot % order.length());
      line = shoot(line, z);
      shot++;
    }


    return "" + shot;
  }

  public String Part2(Scanner scan)
  {
    String input = scan.nextLine();

    String order="RGB";

    String line = "";
    int times = 100;
    for(int i=0; i<times; i++)
    {
      line = line + input;
    }

    int shot=0;
    while(line.length() > 0)
    {
      char z = order.charAt(shot % order.length());
      line = shoot2(line, z);
      shot++;
    }
    return "" + shot;
  }
  public String Part3(Scanner scan)
  {
    String input = scan.nextLine();

    String order="RGB";

    int times = 100000;
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<times; i++)
    {
      sb.append(input);
    }
    
    BigList<Character> line = new BigList<>();
    line.addAll( Str.stolist(sb.toString()));
    System.out.println("Meow");

    int shot=0;
    while(line.size() > 0)
    {
      char z = order.charAt(shot % order.length());
      shoot2Lst(line, z);
      shot++;
      if (line.size() % 1000 == 0)
      {
        //System.out.println(line.size());
      }
    }
    return "" + shot;

  }
}
