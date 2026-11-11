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
  Map2D<Character> map;

  public String Part1(Scanner scan)
  {
    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);
    map.print();

    Map2D<Character> fill = new Map2D<Character>(' ');
    fill1(map, fill);

    fill.print();

    StringBuilder sb = new StringBuilder();
    for(long y=fill.low_y; y<=fill.high_y; y++)
    for(long x=fill.low_x; x<=fill.high_x; x++)
    {
      Point p = new Point(x,y);
      if (fill.get(p) != ' ')
        sb.append(fill.get(p));
    }

    //System.out.println(getValue(sb.toString()));

    return sb.toString();
  }

  public String Part2(Scanner scan)
  {
    map = new Map2D<Character>(' ');
    MapLoad.loadMap(map, scan);
    long total_val = 0;

    for(Point p : map.getAllPoints())
    {
      if (p.x % 9 == 0)
      if (p.y % 9 == 0)
      {
        if (map.get(p) != '*') E.er();

        Map2D<Character> sub = map.copyRange(p.x, p.y, p.x+7, p.y+7);
        //String word = solveWord(sub, p);
        //sub.print();
        Map2D<Character> fill = new Map2D<Character>(' ');
        fill1(sub, fill);
        String word = extractWord(fill);
        //System.out.println("" + p + " " + word);

        total_val += getValue(word);
      }

    }
    //Map2D<Character> fill = new Map2D<Character>(' ');
    //fill1(map, fill);

    return "" + total_val;
  }
  public String extractWord(Map2D<Character> fill)
  {

    StringBuilder sb = new StringBuilder();
    for(long y=fill.low_y; y<=fill.high_y; y++)
    for(long x=fill.low_x; x<=fill.high_x; x++)
    {
      Point p = new Point(x,y);
      if (fill.get(p) != ' ')
        sb.append(fill.get(p));
    }
    return sb.toString();

  }
  
  Map2D<Set<Character> > questions;
  public String Part3(Scanner scan)
  {

    map = new Map2D<Character>(' ');
    //questions = new Map2D<>(ImmutableSet.of());


    MapLoad.loadMap(map, scan);
    /*for(Point p : map.getAllPoints())
    {
      if (map.get(p) == '?')
      {
        TreeSet<Character> all = new TreeSet<>();
        for(int i=0; i<26; i++)
        {
          all.add((char)('A' + i));
        }
        questions.set(p, all);
      }
    }*/

    for(int i=0; i<100; i++)
    {
      p3Reduce();
    }

    //while(p3Reduce() > 0);

    long v = p3Reduce();
    map.print(); 
    v = p3Reduce();
    return "" + v;
  }

  public long p3Reduce()
  {
    long updates=0;
    for(Point p : map.getAllPoints())
    {
      if (p.x % 6 == 0)
      if (p.y % 6 == 0)
      if (p.x +6 <= map.high_x)
      if (p.y +6 <= map.high_y)
      {
        if (map.get(p) != '*') E.er();

        Map2D<Character> sub = map.copyRange(p.x, p.y, p.x+7, p.y+7);
        //String word = solveWord(sub, p);
        //sub.print();

        updates += p3ReduceBlock2(sub, p);
        //Map2D<Character> fill = new Map2D<Character>(' ');
        //fill1(sub, fill);
        //String word = extractWord(fill);
        //System.out.println("" + p + " " + word);

        //total_val += getValue(word);
      }

    }
    return updates;
  }
  public long p3ReduceBlock2(Map2D<Character> src, Point corner)
  {
    Map2D<Set<Character> > qq = new Map2D<>(ImmutableSet.of());
    Map2D<Character> setmap = new Map2D<>('.');

    TreeSet<Character> all = new TreeSet<>();
    for(int i=0; i<26; i++)
    {
      all.add((char)('A' + i));
    }


    for(Point p : src.getAllPoints())
    {
      if ((src.get(p) == '?') || (src.get(p) == '.'))
      {
        qq.set(p, all);
      }
    }

    int changes = 0;

    while(true)
    {
      int sub_c = 0;

      // Set based on size wingers
      for(Point p : src.getAllPoints())
      {
        if (src.get(p) == '.')
        if (setmap.get(p) == '.')
        {
          TreeSet<Character> row = getSym(src, new Point(0, p.y), new Point(1, 0));
          TreeSet<Character> col = getSym(src, new Point(p.x, 0), new Point(0, 1));

          TreeSet<Character> opt = all;
          if (!row.contains('?')) opt = getIntersect(opt, row);
          if (!col.contains('?')) opt = getIntersect(opt, col);

          TreeSet<Character> inter = getIntersect(row, col);
          inter.remove('?');
          if (inter.size() == 1)
          {
            opt = inter;
          }
          opt = getIntersect(opt, qq.get(p));
          if (opt.size() < qq.get(p).size())
          {
            sub_c++;
            System.out.println(row);
            System.out.println(col);
            System.out.println("S " + qq.get(p) + " to " + opt);
            qq.set(p, opt);
          }
        }
      }
      // Set based on set friends
      for(Point p : setmap.getAllPoints())
      {
        if (setmap.get(p) != '.')
        {
          char z = setmap.get(p);
          for(Point q : src.getAllPoints())
          {
            if (src.get(q) == '.')
            if (setmap.get(q) == '.')
            {
              if (qq.get(q).contains(z))
              {
                sub_c++;
                TreeSet<Character> n = new TreeSet<>();
                n.addAll(qq.get(q));
                n.remove(z);
                System.out.println("F " + qq.get(q) + " to " + n);
                qq.set(q, n);
              }
            }
          }
        }
      }
      // Update Qs
      for(Point p : src.getAllPoints())
      {
        if (map.get(p) == '?')
        {
          TreeSet<Character> row = getSym(setmap, new Point(0, p.y), new Point(1, 0));
          TreeSet<Character> col = getSym(setmap, new Point(p.x, 0), new Point(0, 1));

          if (row.size() == 4)
          {
            TreeSet<Character> out = getSym(src, new Point(0, p.y), new Point(1, 0));
            row.removeAll(out);

            if (row.size() == 1)
            {
              sub_c++;
              char z = row.first();
              map.set(p, z);
              src.set(p, z);
            }
          }
          if (col.size() == 4)
          {
            TreeSet<Character> out = getSym(src, new Point(p.x, 0), new Point(0, 1));
            col.removeAll(out);

            if (col.size() == 1)
            {
              sub_c++;
              char z = col.first();
              map.set(p, z);
              src.set(p, z);
            }
          }
        }
      }
      // Update when no options
      for(Point p : qq.getAllPoints())
      {
        if (qq.get(p).size() == 1)
        {
          char z = qq.get(p).iterator().next();
          if (src.get(p) == '.')
          if (setmap.get(p) == '.')
          {
            setmap.set(p, z);
            sub_c++;
          }
          if (src.get(p) == '?')
          {
            E.er();
            src.set(p,z);
            sub_c++;
          }
        }
      }

      if (sub_c == 0) break;
      changes += sub_c;
      System.out.println("Changes: " + sub_c);
    }
    setmap.print();
    if (setmap.getAllPoints().size() == 16)
    {
      long score = getValue(extractWord(setmap));
      return score;

    }
    return 0;

  }
  public int p3ReduceBlock(Map2D<Character> src, Point corner)
  {
    Map2D<Character> vert = map.copyRange(corner.x+2, corner.y, corner.x+2+3, corner.y+7);
    Map2D<Character> horz = map.copyRange(corner.x, corner.y+2, corner.x+7, corner.y+2+3);

    //vert.print();
    //horz.print();
    Map<Character, Long> c_vert = vert.getCounts();
    Map<Character, Long> c_horz = horz.getCounts();

    TreeSet<Character> all = new TreeSet<Character>();
    all.addAll( src.getCounts().keySet());
    all.remove('.');
    all.remove('?');
    c_vert.remove('.');
    c_horz.remove('.');
    for(char z : all)
    {
      if (c_vert.containsKey(z) && c_horz.containsKey(z))
      {
        c_vert.remove(z);
        c_horz.remove(z);
      }
    }
    //System.out.println(c_vert);
    //System.out.println(c_horz);
    int changes = 0;

    if (c_vert.size() == 1)
    if (c_vert.containsKey('?'))
    if (c_vert.get('?') == c_horz.size())
    if (!c_horz.containsKey('?'))
    {
      // all vert ? should be set with our unknown set

      for(Point p : vert.getAllPoints())
      {
        if (vert.get(p) == '?')
        {
          TreeSet<Character> n = getIntersect( c_horz.keySet(), questions.get(p) );
          if (n.size() != questions.get(p).size())
          {
            System.out.println("Update: " + questions.get(p) + " to " + n);
            changes++;
          }

          questions.set(p, n);

          if (n.size() == 1)
          {
            E.er();
            char z = n.first();
            map.set(p, z);
            changes++;

          }
        }
      }
    }

    if (c_horz.size() == 1)
    if (c_horz.containsKey('?'))
    if (c_horz.get('?') == c_vert.size())
    if (!c_vert.containsKey('?'))
    {
      // all horz ? should be set with our unknown set

      for(Point p : horz.getAllPoints())
      {
        if (horz.get(p) == '?')
        {
          TreeSet<Character> n = getIntersect( c_vert.keySet(), questions.get(p) );
          if (n.size() != questions.get(p).size())
          {
            System.out.println("Update: " + questions.get(p) + " to " + n);
            changes++;
          }

          questions.set(p, n);

          if (n.size() == 1)
          {
            E.er();
            char z = n.first();
            map.set(p, z);
            changes++;

          }
        }
      }
    }


    return changes;

  }

  public String solveWord(Map2D<Character> src, Point corner)
  {
    Map2D<Character> fill = new Map2D<Character>(' ');

    ArrayList<Point> points = new ArrayList<>();
    for(long x = 0; x<4; x++)
    for(long y = 0; y<4; y++)
    {
      Point p = new Point(corner.x + 2 + x, corner.y + 2 + y);
      points.add(p);
    }
    win = null;

    long sols = solveWordRec(src, fill, points, 0, new TreeSet<Character>());
    if (sols != 1L)
    { 
      E.er("solutions: " + sols);
    }
    return extractWord(win);

  }
  Map2D<Character> win;
  public long solveWordRec(Map2D<Character> src, Map2D<Character> fill, ArrayList<Point> points, int idx, Set<Character> used)
  {
    if (idx == points.size()) 
    {
      win = fill.copy();
      return 1L;
    }

    Point p = points.get(idx);
    TreeSet<Character> row = getSym(src, new Point(0, p.y), new Point(1, 0));
    TreeSet<Character> col = getSym(src, new Point(p.x, 0), new Point(0, 1));

    row.remove('.');
    col.remove('.');
    TreeSet<Character> inter = getIntersect(row, col);
    if (inter.size() ==0) return 0L;
    long sol = 0;
    for(char z : inter)
    {
      if (!used.contains(z))
      {
        fill.set(p, z);
        used.add(z);

        sol += solveWordRec(src, fill, points, idx+1, used);

        used.remove(z);
        fill.set(p, '.');
      }
    }
    return sol;
  }

  public void fill1(Map2D<Character> src, Map2D<Character> fill)
  {
    for(Point p : src.getAllPoints())
    {
      if (src.get(p) == '.')
      {
        TreeSet<Character> row = getSym(src, new Point(0, p.y), new Point(1, 0));
        TreeSet<Character> col = getSym(src, new Point(p.x, 0), new Point(0, 1));

        row.remove('.');
        col.remove('.');
        row.remove(',');
        col.remove(',');
        col.remove(' ');
        row.remove(' ');
        TreeSet<Character> inter = getIntersect(row, col);

        //if (inter.size() < 3) System.out.println("Options: " + inter.size());
        if (inter.size() != 1)
        {
          E.er( inter.toString());
        }
        if (inter.size() == 1)
        {

          char z = inter.first();
          fill.set(p, z);
        }
      }

    }
  }

  public TreeSet<Character> getIntersect(Set<Character> a, Set<Character> b)
  {
    TreeSet<Character> inter = new TreeSet<>();
    for(char z : a)
    {
      if (b.contains(z)) inter.add(z);
    }

    return inter;

  }

  public TreeSet<Character> getSym(Map2D<Character> m, Point start, Point diff)
  {
    TreeSet<Character> set = new TreeSet<>();
    
    Point p = start;
    while(true)
    {
      if (p.x > m.high_x) break;
      if (p.y > m.high_y) break;
      set.add(m.get(p));
      p = p.add(diff);
    }
    set.remove('.');
    set.remove(',');
    set.remove(' ');
    return set;
  }

  public long getValue(String in)
  {
    long v = 0;
    for(int i=0; i<in.length(); i++)
    {
      long z = in.charAt(i) - 'A' + 1;
      long p = i+1;

      v+= (z*p);


    }
    return v;

  }
}
