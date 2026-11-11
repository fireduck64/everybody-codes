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
    String words_line = scan.nextLine();

    List<String> words = Tok.en(Tok.en(words_line, ":").get(1), ",");
    TreeSet<String> words_set = new TreeSet<>();
    words_set.addAll(words);
    int count =0;

    while(scan.hasNext())
    {
      String s = scan.next();
      for(String w : words)
      {
        int start = 0;
        while(true)
        {
          int x = s.indexOf(w,start);
          if (x >= 0) 
          {
            count++;
            start=x+1;
          }
          else
          {
            break;
          }
        }

      }

    }
    

    return "" + count;
  }

  public String Part2(Scanner scan)
  {
    String words_line = scan.nextLine();

    List<String> words = Tok.en(Tok.en(words_line, ":").get(1), ",");
    TreeSet<String> words_set = new TreeSet<>();
    words_set.addAll(words);
    
    for(String w : words)
    {
      String n = "";
      for(char z : Str.stolist(w))
      {
        n = "" + z + n;
      }
      words_set.add(n);
    }
    
    int count =0;

    while(scan.hasNext())
    {
      String s = scan.next();
      TreeSet<Integer> runes=new TreeSet<>();
      for(String w : words_set)
      {
        int start = 0;
        while(true)
        {
          int x = s.indexOf(w,start);
          if (x >= 0) 
          {
            for(int j=0; j<w.length(); j++)
            {
              runes.add(x+j);
            }
            start=x+1;
          }
          else
          {
            break;
          }
        }

      }
      count+=runes.size();

    }
    

    return "" + count;

  }
  public String Part3(Scanner scan)
  {
    String words_line = scan.nextLine();

    List<String> words = Tok.en(Tok.en(words_line, ":").get(1), ",");
    TreeSet<String> words_set = new TreeSet<>();
    words_set.addAll(words);
    for(String w : words)
    {
      String n = "";
      for(char z : Str.stolist(w))
      {
        n = "" + z + n;
      }
      words_set.add(n);
    }
    scan.nextLine();

    Map2D<Character> m = new Map2D<>('.');
    MapLoad.loadMap(m, scan);
    //m.print();
    Map2D<Character> runes = new Map2D<>('.');

    for(Point start : m.getAllPoints())
    {
      for(String w : words_set)
      {
        String n1 = read(start, m, new Point(0, 1), w.length());
        if (n1.equals(w))
        {
          write(start, m, runes, new Point(0,1), w.length());
        }

        String n2 = read(start, m, new Point(1, 0), w.length());
        if (n2.equals(w))
        {
          write(start, m, runes, new Point(1,0), w.length());
        }

      }
      

    }
    //runes.print();

 
    return "" + runes.getCounts().get('R');
  }
  private void write(Point s, Map2D<Character> m, Map2D<Character> runes, Point dir, int len)
  {
    if (len == 0) return;
    char z = m.get(s);
    if (z=='.')
    {
      if (s.y > m.high_y)
      {
        write(new Point(s.x, 0), m, runes, dir, len);
        return;
      }
      if (s.x > m.high_x)
      {
        write(new Point(0, s.y), m, runes, dir, len);
        return;
      }
      System.out.println(s);
      E.er();
    }

    runes.set(s, 'R');

    write(s.add(dir), m, runes, dir, len-1);

  }

  private String read(Point s, Map2D<Character> m, Point dir, int len)
  {
    if (len == 0) return "";
    char z = m.get(s);
    if (z=='.')
    {
      if (s.y > m.high_y)
      {
        return "";
        //return read(new Point(s.x, 0), m, dir, len);
      }
      if (s.x > m.high_x)
      {
        return read(new Point(0, s.y), m, dir, len);
      }
      System.out.println(s);
      E.er();
    }

    return "" + z + read(s.add(dir), m, dir, len-1);

  }

}
