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

  public Map<String, String> readInput(Scanner scan)
  {
    TreeMap<String, String> m = new TreeMap<>();
    while(scan.hasNextLine())
    {
      String line = scan.nextLine();
      String key = Tok.en(line, "=").get(0);
      String value = Tok.en(line, "=").get(1);

      m.put(key, value);
    }
    return m;
  }

  public Map2D<Character> paint(Map<String,String> input)
  {
    Map2D<Character> m = new Map2D<>(' ');
    int height = Integer.parseInt(input.get("height"));
    int width = Integer.parseInt(input.get("width"));
    String vert_offset = input.get("vertical-offsets");
    String horz_offset = input.get("horizontal-offsets");

    //m.set(0,0,'#');
    //m.set(width*2+2, height*2+2, '#'); 
    for(int x=0; x<=width*2+2; x++)
    for(int y=0; y<=height*2+2; y++)
    {
      m.set(x,y,'#');
    }
    for(int x=1; x<=width*2+1; x++)
    for(int y=1; y<=height*2+1; y++)
    {
      m.set(x,y,' ');
    }


    // horz -- somehow

    int idx=0;
    //for(int y=height; y>=0; y--)
    for(int y=0; y<=height; y++)
    {
      char z = horz_offset.charAt(idx % horz_offset.length());
      int start_x = 0;
      if (z == '1') start_x=1;
      for(int x=start_x; x<width; x+=2)
      {
        m.set(x*2+2, y*2+1, '-');
      }

      idx++;

    }

    idx=0;
    //for(int y=height; y>=0; y--)
    for(int x=0; x<=width; x++)
    {
      char z = vert_offset.charAt(idx % vert_offset.length());
      int start_y = 0;
      if (z == '1') start_y=1;
      for(int y=start_y; y<height; y+=2)
      {
        m.set(x*2+1, y*2+2, '|');
      }
      idx++;

    }

    return m;

  }

  public String Part1(Scanner scan)
  {
    Map<String,String> input = readInput(scan);
    System.out.println(input);

    Map2D<Character> m = paint(input);

    long p1=0;
    for(long x = 0; x<=m.high_x; x++)
    for(long y=0; y<=m.high_y; y++)
    {
      int cnt = 0;
      for(Point p : m.getAdj(new Point(x,y), false))
      {
        if ((m.get(p) != ' ') && (m.get(p) != '#')) cnt++;

      }
      if (cnt ==4) p1++;

    }

    //m.print();

    return "" + p1;
  }

  public void paint2Color(Map2D<Character> m)
  {
    char color='a';
    paintFill(m, color, new Point(1,1));

    for(Point p : m.getAllPoints())
    {
      if (m.get(p) == '-')
      {
        char up = m.get(p.x, p.y-1);
        char down = m.get(p.x, p.y+1);
        if ((up == 'a') && (down ==' ')) paintFill(m, 'b', new Point(p.x,p.y+1));
        if ((up == 'b') && (down ==' ')) paintFill(m, 'a', new Point(p.x,p.y+1));

        if ((up == ' ') && (down =='a')) paintFill(m, 'b', new Point(p.x,p.y-1));
        if ((up == ' ') && (down =='b')) paintFill(m, 'a', new Point(p.x,p.y-1));

      }

    }

  }

  public void paintFill(Map2D<Character> m, char color, Point cur)
  {
    TreeSet<Point> queue =new TreeSet<>();
    queue.add(cur);

    while(queue.size() > 0)
    {
      Point p = queue.pollFirst();
      if (m.get(p) == ' ')
      {
        m.set(p, color);
        for(Point q : m.getAdj(p, false)) queue.add(q);
      }

    }


  }

  public String Part2(Scanner scan)
  {
    Map<String,String> input = readInput(scan);
    System.out.println(input);

    Map2D<Character> m = paint(input);
    paint2Color(m);

    long a=0;
    long b=0;
    for(Point p : m.getAllPoints())
    {
      if ((m.get(p) == 'a') || (m.get(p) == 'b'))
      {
        int cnt =0;
        for(Point q : m.getAdj(p,false))
        {
          if ((m.get(q) == '-') || (m.get(q) == '|')) cnt++;
        }
        if (cnt == 4)
        {
          if (m.get(p) == 'a') a++;
          if (m.get(p) == 'b') b++;
        }
      }
    }
    return "" + Math.max(a,b);
  }

  public TreeMap<Character, Long> getCounts(Map2D<Character> m)
  {

    long a=0;
    long b=0;
    for(Point p : m.getAllPoints())
    {
      if ((m.get(p) == 'a') || (m.get(p) == 'b'))
      {
        int cnt =0;
        for(Point q : m.getAdj(p,false))
        {
          if ((m.get(q) == '-') || (m.get(q) == '|')) cnt++;
        }
        if (cnt == 4)
        {
          if (m.get(p) == 'a') a++;
          if (m.get(p) == 'b') b++;
        }
      }
    }
    TreeMap<Character, Long> counts = new TreeMap<>();
    counts.put('a',a);
    counts.put('b',b);
    return counts;

 
  }
  public String Part3(Scanner scan)
  {
    Map<String,String> input = readInput(scan);
    System.out.println(input);

    long full_width = Integer.parseInt(input.get("width"));
    long full_height = Integer.parseInt(input.get("height"));

    long sub_width = input.get("vertical-offsets").length() * 2;
    long sub_height = input.get("horizontal-offsets").length() * 2;

    TreeMap<Character, Long> counts = new TreeMap<>();
    counts.put('a',0L);
    counts.put('b',0L);


    // Full block
    {
      Map<String,String> sub_input=new TreeMap<>();
      sub_input.putAll(input);
      sub_input.put("width", "" + sub_width);
      sub_input.put("height", "" + sub_height);
      System.out.println(sub_input);
      Map2D<Character> m = paint(sub_input);

      paint2Color(m);

      Map<Character, Long> c = getCounts(m);

      // how many?
      long multi = (full_width / sub_width) * (full_height / sub_height);

      counts.put( 'a', counts.get('a') + multi*c.get('a'));
      counts.put( 'b', counts.get('b') + multi*c.get('b'));
    }
    
    // Right strip
    {
      Map<String,String> sub_input=new TreeMap<>();
      sub_input.putAll(input);
      long strip_width = full_width % sub_width;
      sub_input.put("width", "" + strip_width);
      sub_input.put("height", "" + sub_height);
      System.out.println(sub_input);
      Map2D<Character> m = paint(sub_input);

      paint2Color(m);

      Map<Character, Long> c = getCounts(m);

      // how many?
      long multi = (full_height / sub_height);

      counts.put( 'a', counts.get('a') + multi*c.get('a'));
      counts.put( 'b', counts.get('b') + multi*c.get('b'));
    }
    
    // Bottom strip
    {
      Map<String,String> sub_input=new TreeMap<>();
      sub_input.putAll(input);
      long strip_height = full_height % sub_height;
      sub_input.put("width", "" + sub_width);
      sub_input.put("height", "" + strip_height);
      System.out.println(sub_input);
      Map2D<Character> m = paint(sub_input);

      paint2Color(m);

      Map<Character, Long> c = getCounts(m);

      // how many?
      long multi = (full_width / sub_width);

      counts.put( 'a', counts.get('a') + multi*c.get('a'));
      counts.put( 'b', counts.get('b') + multi*c.get('b'));
    }
    // Corner
    {
      Map<String,String> sub_input=new TreeMap<>();
      sub_input.putAll(input);
      long strip_height = full_height % sub_height;
      long strip_width = full_width % sub_width;
      sub_input.put("width", "" + strip_width);
      sub_input.put("height", "" + strip_height);
      System.out.println(sub_input);
      Map2D<Character> m = paint(sub_input);

      paint2Color(m);

      Map<Character, Long> c = getCounts(m);

      // how many?
      long multi = 1L;

      counts.put( 'a', counts.get('a') + multi*c.get('a'));
      counts.put( 'b', counts.get('b') + multi*c.get('b'));
    }

    long a = counts.get('a');
    long b = counts.get('b');

    return "" + Math.max(a,b);

  }
}
