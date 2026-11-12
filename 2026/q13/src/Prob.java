import java.io.FileInputStream;
import java.util.*;
import java.io.File;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.magicwerk.brownies.collections.BigList;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.IntNum;
import com.microsoft.z3.RatNum;
import com.microsoft.z3.Symbol;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Model;
import com.microsoft.z3.*;
import com.microsoft.z3.Status;
import java.text.DecimalFormat;

import duckutil.TaskMaster;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.Callable;

public class Prob
{
  DecimalFormat df = new DecimalFormat("0.00000");

  public static void main(String args[]) throws Exception
  {
    new Prob();
  }
  ThreadPoolExecutor exec;
  public Prob()
    throws Exception
  {
    exec = TaskMaster.getBasicExecutor(64, "p3");

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

  public class Hand
  {

    int pos;
    int cur;
    long moves_left;
    int dir;

    public Hand(int pos, String dir, int cur, long moves)
    {
      this.pos = pos;
      this.cur = cur;
      this.moves_left =moves;

      if (dir.equals("CW")) this.dir=1;
      if (dir.equals("CCW")) this.dir=-1;

      if (this.dir == 0) E.er();

    }

    public double getAngle()
    {
      double a_pos = 360.0 / pos;
      return a_pos * cur;
    }
    public double doMove()
    {
      if (moves_left>0)
      {
        cur += dir;
        while (cur < 0) cur+=pos;
        while (cur >= pos) cur-=pos;
        moves_left--;

        //if (pos == 0)
        //if (dir > 0)
        //{
        //  return 360.0;
        //}

      }
      return getAngle();

    }
    boolean isDone()
    {
      return (moves_left == 0);
    }

  }

  public class ClockState
  {
    long bangs;
    long move_cnt;
    public ClockState(long bangs, long move_cnt)
    {
      this.bangs = bangs;
      this.move_cnt = move_cnt;

    }

  }


  public class Clock
  {
    HashMap<String, ClockState> c_memo=new HashMap<>();
    ArrayList<Hand> hands = new ArrayList<>();
    long bangs;
    long move_cnt;

    public Clock(Scanner scan)
    {
      String line = scan.nextLine();
      if (!line.startsWith("hands")) E.er();
      List<Integer> pos = Tok.ent( Tok.en(line, " ").get(1), ":");
      line = scan.nextLine();
      if (!line.startsWith("direction")) E.er(line);
      List<String> dirs = Tok.en( Tok.en(line, " ").get(1), ":");

      line = scan.nextLine();
      if (!line.startsWith("current")) E.er();
      List<Integer> current = Tok.ent( Tok.en(line, " ").get(1), ":");

      line = scan.nextLine();
      if (!line.startsWith("move")) E.er();
      List<Long> move = Tok.enl( Tok.en(line, " ").get(1), ":");

      int cnt = pos.size();

      for(int i=0; i<cnt; i++)
      {
        hands.add(new Hand( pos.get(i), dirs.get(i), current.get(i), move.get(i)));
      }
    }
    public String getStateKey()
    {
      StringBuilder sb=new StringBuilder();

      for(Hand h : hands)
      {
        sb.append("h");
        sb.append(h.isDone());
        sb.append("-");
        sb.append(h.cur);
        sb.append("e");
      }
      return sb.toString();
    }
    public ClockState getState()
    {
      return new ClockState(bangs, move_cnt);
    }

    public boolean isDone()
    {
      for(Hand h : hands)
      {
        if (!h.isDone()) return false;
      }
      return true;

    }
    public void move()
    {
      String key = getStateKey();
      ClockState state = getState();
      if (c_memo.containsKey(key))
      {
        ClockState prev_state = c_memo.get(key);
        long step = state.move_cnt - prev_state.move_cnt;
        long max_jump = 1000000000L;

        for(Hand h : hands) 
        {
          if (!h.isDone())
          {
            max_jump = Math.min(max_jump, h.moves_left-5);
          }
        }
        if (max_jump >= step)
        {

          long mult = max_jump / step;
          long add_bangs = state.bangs - prev_state.bangs;
          //System.out.println("Step: " + step + " " + max_jump + " " + mult);
          for(Hand h : hands) 
          {
            if (!h.isDone())
            {
              h.moves_left -= mult*step;
            }
          }
          bangs += mult * add_bangs;
          move_cnt += mult * step;

          return;
        }

      }
      c_memo.put(key, state);
      moveSingle();

    }
    public void moveSingle()
    {
      move_cnt++;
      ArrayList<Double> starts = new ArrayList<>();
      ArrayList<Double> ends = new ArrayList<>();

      for(int i=0; i<hands.size(); i++)
      {
        Hand h = hands.get(i);

        double st = h.getAngle();
        double en = h.doMove();

        if ((st == 0.0) && (en > 180.0))
        {
          st=360.0;
        }
        if ((en == 0.0) && (st > 180.0))
        {
          en=360.0;
        }

        starts.add( st );
        ends.add( en );
      }

      for(int i=0; i<hands.size(); i++)
      {
        for(int j=i+1; j<hands.size(); j++)
        {
          if (hit(starts.get(i), ends.get(i), starts.get(j), ends.get(j))) 
          {
            //System.out.println(String.format("  Move %d Hit: %d %d", move_cnt, i+1, j+1));
            //System.out.println(String.format("    %f %f %f %f", starts.get(i), ends.get(i), starts.get(j), ends.get(j)));
            bangs++;
          }

        }
      }
    }

  }

  // Ignore a hit at the start
  double close = 1e-6;

  HashMap<String, Boolean> memo = new HashMap<>(8192,0.5f);
  public boolean hit(double start_a, double end_a, double start_b, double end_b)
  {
    if (Math.abs(start_a-start_b) < close)
    if (Math.abs(end_a-end_b) < close)
    {
      return false;
    }

    if (Math.abs(end_a - end_b) < close) return true;
    if (Math.abs(360.0 - Math.abs(end_a - end_b)) < close) return true;

    //String key = String.format("%f %f %f %f", start_a, end_a, start_b, end_b);
    String key = String.format("%s %s %s %s", df.format(start_a), df.format(end_a), df.format(start_b), df.format(end_b));


    if (memo.containsKey(key)) return memo.get(key);
    //System.out.println(key);

    // start_a + diff_a * t = start_b + diff_b * t
    // t > 0.0
    // t <= 1.0

		double diff_a = end_a - start_a;
		double diff_b = end_b - start_b;

		try (Context ctx = new Context()) {
				Solver s = ctx.mkSolver();

				// 1. Create symbolic Real variable 't' directly as a RealExpr
				RealExpr t = ctx.mkRealConst("t");

				// 2. Convert Java doubles to Z3 RatNum literals via String
				RatNum zStartA = ctx.mkReal(String.valueOf(start_a));
				RatNum zDiffA  = ctx.mkReal(String.valueOf(diff_a));
				RatNum zStartB = ctx.mkReal(String.valueOf(start_b));
				RatNum zDiffB  = ctx.mkReal(String.valueOf(diff_b));

				// 3. Add bounds constraints to the solver: 0 < t < 1
				s.add(ctx.mkGt(t, ctx.mkReal(0)));
				s.add(ctx.mkLe(t, ctx.mkReal(1))); // Use mkLe if you want t <= 1

				// 4. Build sides: start_a + diff_a * t == start_b + diff_b * t
				ArithExpr<RealSort> lhs = ctx.mkAdd(zStartA, ctx.mkMul(zDiffA, t));
				ArithExpr<RealSort> rhs = ctx.mkAdd(zStartB, ctx.mkMul(zDiffB, t));
				s.add(ctx.mkEq(lhs, rhs));

				// 5. Check satisfiability and extract the solution
				if (s.check() == Status.SATISFIABLE) {
						Model model = s.getModel();
						Expr<RealSort> tVal = model.evaluate(t, false);
						//System.out.println("Solution t = " + tVal);


						// Optional: convert exact Z3 rational result back to a Java double
						if (tVal instanceof RatNum) {
								RatNum rat = (RatNum) tVal;
								double result = rat.getBigIntNumerator().doubleValue() 
															/ rat.getBigIntDenominator().doubleValue();
								//System.out.println("As double: " + result);
						}
            memo.put(key, true);
						return true;
				} else {
						//System.out.println("No solution in (0, 1). Status: " + s.check());
				}
		}

    memo.put(key,false);

    return false;

  }

  public String Part1(Scanner scan)
  {
    Clock c = new Clock(scan);

    while(!c.isDone())
    {
      c.move();
    }

    return "" + c.bangs; 
  }

  public String Part2(Scanner scan)
  {
    long p = 0;
    List<Callable<Long> > action_list = new ArrayList<>();

    while(scan.hasNextLine())
    {
      Clock c = new Clock(scan);

      action_list.add(new Action(c));
      if (scan.hasNextLine())
      {
        scan.nextLine();
      }
    }
    List<Long> lst = new TaskMaster<Long>(action_list, exec).getResults();

    for(long x : lst) p+=x;
	
    return "" + p;
  }

  public class Action implements Callable<Long>
  {
    Clock c;
    public Action(Clock c)
    {
      this.c = c;
    }

    public Long call()
    {
      while(!c.isDone())
      {
        c.move();
      }
      return c.bangs;
    }


  }
  public String Part3(Scanner scan)
  {
    return Part2(scan);
  }
}
