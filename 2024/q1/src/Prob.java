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

    if (new File("input.p1").exists())
    {
      System.out.println("Part 1:");
      Part1(new Scanner(new FileInputStream("input.p1")));
    }
    if (new File("input.p2").exists())
    {
      System.out.println("Part 2:");
      Part2(new Scanner(new FileInputStream("input.p2")));
    }
    if (new File("input.p3").exists())
    {
      System.out.println("Part 3:");
      Part3(new Scanner(new FileInputStream("input.p3")));
    }



  }

  Random rnd=new Random();

  public void Part1(Scanner scan)
  {
		int val =0;
		String s = scan.next();
    for(char c : Str.stolist(s))
    {
      if (c == 'B') val +=1;
      if (c == 'C') val +=3;
      if (c == 'D') val +=5;

    }
    System.out.println(val);

  }

  public void Part2(Scanner scan)
  {

    String s = scan.next();

    int val = 0;
    for(int i=0; i<s.length(); i+=2)
    {
      String sub = s.substring(i,i+2);
      int count=0;
      for(char c : Str.stolist(sub))
      {
        if (c == 'x') continue;
        count++;
        if (c == 'A') val+=0;
        if (c == 'B') val+=1;
        if (c == 'C') val+=3;
        if (c == 'D') val+=5;

      }
      if (count == 2) val+=2;
      if (count == 3) val+=6;
    }
		System.out.println(val);
	

  }
  public void Part3(Scanner scan)
  {

    String s = scan.next();

    int val = 0;
    for(int i=0; i<s.length(); i+=3)
    {
      String sub = s.substring(i,i+3);
      int count=0;
      for(char c : Str.stolist(sub))
      {
        if (c == 'x') continue;
        count++;
        if (c == 'A') val+=0;
        if (c == 'B') val+=1;
        if (c == 'C') val+=3;
        if (c == 'D') val+=5;

      }
      if (count == 2) val+=2;
      if (count == 3) val+=6;
    }
		System.out.println(val);
	
  }
}
