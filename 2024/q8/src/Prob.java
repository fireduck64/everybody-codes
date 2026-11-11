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
    long input = scan.nextLong();

    int h = 1;
    Point b = null;
    while(true)
    {
      h++;
      b = getBlocks(h);
      if (b.y >= input)
      {
        break;
      }

    }

    long diff = b.y - input;
    long width = b.x;


    long ans = width * diff;

    return "" + ans;
  }

  public String Part2(Scanner scan)
  {
    int input = scan.nextInt();

    return "" + getBlocks2(3L,5L, 1L, 1L, 50L-1L) + " " + getBlocks2(input, 1111L, 1L, 1L, 20240000L-1L);
  }
  public String Part3(Scanner scan)
  {
    
    int mult = scan.nextInt();
    int mod = scan.nextInt();
    int blocks = scan.nextInt();

    return "" + fillBlocks2(mult, mod, blocks);

  }

  public Point getBlocks(int height)
  {
    long blocks = 1;
    long width = 1;

    for(int h=1; h<height; h++)
    {
      width = width + 2;
      blocks += width;

    }
    return new Point(width, blocks);


  }

  TreeMap<String, Long> memo = new TreeMap<>();

  public long getBlocks2(long mult, long mod, long width, long thick, long rem)
  {
    String key = "" + mult + " " + mod + " " + width;

    long thick2 = (thick * mult) % mod;
    long width2 = width + 2;
    long used = thick2 * width2;
    if (used >= rem)
    {
      long diff = used - rem;
      return width2 * diff;

    }

    return getBlocks2(mult, mod, width2, thick2, rem-used);

  }
  public long getBlocks3(long mult, long mod, long width, long thick, long rem)
  {
    String key = "" + mult + " " + mod + " " + width;

    long thick2 = (thick * mult) % mod + mod;
    long width2 = width + 2;

    long used = thick2 * width2;

    long gap = Math.max(0, width) * Math.max(0, thick2-1);
    long hole = Math.max(0, width-2);
    System.out.println("used full: " + used + " hole:" + hole + " gap:" + gap);

    used = used - gap - hole;
    System.out.println("w:" + width2 + " t:" + thick2 + " u:" + used + " " + rem);
    if (used >= rem)
    {
      long diff = used - rem;
      return diff;

    }

    return getBlocks3(mult, mod, width2, thick2, rem-used);

  }

  public long fillBlocks2(int mult, int mod, int max_blocks)
  {
    TreeMap<Integer, Integer> heights = new TreeMap<>();

    int layer = 1;
    int width = 1;
    int thick = 1;
    heights.put(0, 1);
    int rm = countRemovable2(heights, mult, mod, width);
    ;

    while(count(heights) - rm < max_blocks)
    {
      thick = (thick * mult) % mod + mod;
      width = width+2;
      layer++;
      //System.out.println(String.format("Layer %d thick %d width %d", layer, thick, width));

      for(int x=-width/2; x<=width/2; x++)
      {
        if (heights.containsKey(x))
        {
          heights.put(x, heights.get(x) + thick);
        }
        else
        {
          heights.put(x, thick);
        }

      }
      rm = countRemovable2(heights, mult, mod, width);
    }

    return count(heights) - rm - max_blocks;
  }

 

  public long fillBlocks(int mult, int mod, int max_blocks)
  {
    Map2D<Character> map = new Map2D<Character>(' ');

    int layer = 1;
    int width = 1;
    int thick = 1;
    map.set(0,0, '#');
    int rm = 0;

    while(map.getCounts().get('#') - rm < max_blocks)
    {
      long layer_start = map.getCounts().get('#');
      thick = (thick * mult) % mod + mod;
      width = width+2;
      layer++;
      System.out.println(String.format("Layer %d thick %d width %d - %d", layer, thick, width, layer_start));

      for(int x=-width/2; x<=width/2; x++)
      {
        map.set(x,-1,'=');
        int y=0;
        while(map.get(x,y)!=' ') y++;
        for(int t=0; t<thick; t++)
        {
          map.set(x,y+t, '#');

        }


      }
      //empty(map);
      rm = countRemovable(map, mult, mod, width);
      //if (layer >= 5) break;
    }

    return map.getCounts().get('#') - rm - max_blocks;
  }
  public int count(Map<Integer,Integer> map)
  {
    int cnt = 0;
    for(int v : map.values()) cnt+=v;

    return cnt;

  }

  public int countRemovable2(Map<Integer,Integer> map, int mult, int mod, int width)
  {
    int removable = 0;
    for(int x : map.keySet())
    {
      int h = map.get(x); 

      long pow = mult;
      pow *= width;
      pow *= h;
     
      int rem = (int) ((pow) % mod);

      int left =0;
      if (map.containsKey(x-1)) left = map.get(x-1);
      int right = 0;
      if (map.containsKey(x+1)) right = map.get(x+1);

      rem = Math.min(rem, h-1);
      if (left == 0) rem =0;
      else rem = Math.min(rem, left-1);
      if (right == 0) rem = 0;
      else rem = Math.min(rem, right-1);

      rem = Math.max(0, rem);

      removable+=rem;

    }
    return removable;
   
  }
  public int countRemovable(Map2D<Character> map, int mult, int mod, int width)
  {
    int removable = 0;
    for(long x = map.low_x; x<=map.high_x; x++)
    {
      int h = 0;
      while( map.get(x,h) == '#') h++;
      int rem = (mult * width * h) % mod;

      for(int y=0; y<rem; y++)
      {
        int voids = 0;
        for(Point q : map.getAdj(new Point(x,y), true))
        {
          if (map.get(q) == ' ') voids++;
        }
        if (voids == 0)
        {
          removable++;

        }

      }


    }
    return removable;

  }
  public void empty(Map2D<Character> map)
  {
    for(Point p : map.getAllPoints())
    {
      if (map.get(p) == '#')
      {
        int voids = 0;
        for(Point q : map.getAdj(p, true))
        {
          if (map.get(q) == ' ') voids++;
        }
        if (voids == 0)
        {
          map.set(p, 'X');
        }

      }
      

    }


  }


}
