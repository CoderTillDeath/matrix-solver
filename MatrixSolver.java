import javax.swing.*;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;


public class MatrixSolver
{
	public static void main(String[] args)
	{
		new GUI();
	}
}

class GUI extends JFrame
{
	int vars;
	int numEqs;
	ArrayList<Equation> eqs;
	
	public GUI()
	{
		fileInput();
		resize();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setContentPane(new MyPanel());
		setVisible(true);
	}
	
	public void resize()
	{
		setSize(vars*200,100*numEqs + 150);
	}
	
	public void fileInput()
	{
		Scanner scan = null;
		
		try
		{
			scan = new Scanner(new File("input.txt"));
		}
		catch(FileNotFoundException e){e.printStackTrace();}
		
		String in = scan.nextLine();
		String[] split = in.split(" ");
		
		vars = Integer.parseInt(split[0]);
		numEqs = Integer.parseInt(split[1]);
		
		eqs = new ArrayList<Equation>();
		
		for(int e = 0; e < numEqs; e++)
		{
			Equation eq = new Equation(vars+1);
			String line = scan.nextLine();
			String[] lineSplit = line.split(" ");
			
			for(int ne = 0; ne < vars+1; ne++)
			{
				eq.add(Integer.parseInt(lineSplit[ne]));
			}
			eqs.add(eq);
		}
	}
	
	public void input()
	{
		String in = JOptionPane.showInputDialog("Please input how many variables");
		
		vars = Integer.parseInt(in);
		
		in = JOptionPane.showInputDialog("Please input how many equations");
		
		numEqs = Integer.parseInt(in);
		
		eqs = new ArrayList<Equation>();
		
		for(int x = 0; x < vars; x++)
		{
			eqs.add(addEquation());
		}
	}
	
	public Equation addEquation()
	{	
		Equation eq = new Equation(vars+1);
		for(int y = 0; y < numEqs; y++)
		{
			String variable = JOptionPane.showInputDialog(eq.queryString());
			eq.add(Integer.parseInt(variable));
		}
		String ans = JOptionPane.showInputDialog(eq.answerString());
		eq.add(Integer.parseInt(ans));
		
		return eq;
	}
	
	class MyPanel extends JPanel
	{
		PrintWriter writer;
		ArrayList<JLabel> labels;
		ArrayList<JButton> scale;
		ArrayList<JButton> replace;
		JButton interchange;
		JButton addEquation;
		JButton switchForm;
		JButton done;
		
		public MyPanel()
		{
			try
			{
				writer = new PrintWriter(new FileWriter("output.txt"));
			}
			catch(Exception e){e.printStackTrace();}
			
			labels = new ArrayList<JLabel>();
			scale = new ArrayList<JButton>();
			replace = new ArrayList<JButton>();
			
			for(int x = 0; x < eqs.size(); x++)
			{
				JLabel lab = new JLabel(eqs.get(x).toString());
				lab.setFont(new Font("Times New Roman", Font.PLAIN, 40));
				labels.add(lab);
				add(lab);
				
				JButton sc = new JButton("Scale");
				sc.addActionListener(new ScaleListener(x));
				sc.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				add(sc);
				
				JButton rep = new JButton("Replace");
				rep.addActionListener(new ReplaceListener(x));
				rep.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				add(rep);
				
				JLabel br = new JLabel("<html><b/><html/>");
				add(br);
			}
			
			interchange = new JButton("Interchange");
			interchange.addActionListener(new InterchangeListener());
			interchange.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			add(interchange);
			
			addEquation = new JButton("Add Equation");
			addEquation.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			add(addEquation);
			
			switchForm = new JButton("Switch Form");
			switchForm.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			add(switchForm);
			
			done = new JButton("Done");
			done.addActionListener(new DoneListener());
			done.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			add(done);
			
			repaint();
			writeToFile();
		}
		
		public void update()
		{
			for(int x = 0; x < labels.size(); x++)
			{
				labels.get(x).setText(eqs.get(x).toString());
			}
			writeToFile();
		}
		
		public void writeToFile()
		{
			for(int x = 0; x < eqs.size(); x++)
			{
				writer.println(eqs.get(x).toMatrix());
			}
			writer.println();
		}
				
		class ScaleListener implements ActionListener
		{
			int eq;
			
			public ScaleListener(int s)
			{
				super();
				eq = s;
			}
			
			public void actionPerformed(ActionEvent e)
			{
				String in = JOptionPane.showInputDialog("Scale by what number?");
				if(in.contains("/"))
				{
					String[] split = in.split("/");
					int a = Integer.parseInt(split[0]);
					int b = Integer.parseInt(split[1]);
					eqs.get(eq).fraction(a,b);
				}
				else
				{
					eqs.get(eq).scale(Integer.parseInt(in));
				}
				update();
			}
		}
		
		class ReplaceListener implements ActionListener
		{
			int eq;
			
			public ReplaceListener(int s)
			{
				super();
				eq = s;
			}
			
			public void actionPerformed(ActionEvent e)
			{
				String in = JOptionPane.showInputDialog("Combine with which row?");
				Equation replacer = eqs.get(Integer.parseInt(in)-1);
				in = JOptionPane.showInputDialog("Would you like to scale? (1 for no)");
				
				if(in.contains("/"))
				{
					String[] split = in.split("/");
					int a = Integer.parseInt(split[0]);
					int b = Integer.parseInt(split[1]);
					replacer.fraction(a,b);
				
					eqs.get(eq).add(replacer);
					
					replacer.fraction(b,a);
				}
				else
				{
					replacer.scale(Integer.parseInt(in));
				
					eqs.get(eq).add(replacer);
					
					replacer.scaleDown(Integer.parseInt(in));
				}
				
				update();
			}
		}
		
		class InterchangeListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				String in = JOptionPane.showInputDialog("Switch the ___th row");
				int a = Integer.parseInt(in)-1;
				in = JOptionPane.showInputDialog("with the ___th row");
				int b = Integer.parseInt(in)-1;
				
				Equation temp = eqs.get(a);
				eqs.set(a,eqs.get(b));
				eqs.set(b,temp);
				
				update();
			}
		}
		
		class DoneListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				writer.close();
			}
		}
	}
}

class Equation
{
	Fraction[] values;
	int index;
	
	public Equation(int size)
	{
		values = new Fraction[size];
		index = 0;
	}
	
	public void scale(int d)
	{
		for(int x = 0; x < values.length; x++)
		{
			values[x].multiply(d);
		}
	}
	
	public void scaleDown()
	{
		for(int x = 0; x < values.length; x++)
		{
			values[x].simplify();
		}
	}
	
	public void scaleDown(int a)
	{
		for(int x = 0; x < values.length; x++)
		{
			values[x].multiply(1,a);
		}
	}
	
	public void fraction(int a, int b)
	{
		for(int x = 0; x < values.length; x++)
		{
			values[x].multiply(a,b);
		}
	}
	
	public void add(int x)
	{
		this.add(x, 1);
	}
	
	public void add(int x, int y)
	{
		values[index] = new Fraction(x, y);
		index++;
	}
	
	public int size()
	{
		return values.length;
	}
	
	public double getDouble(int x)
	{
		return values[x].getDouble();
	}
	
	public void add(Equation e)
	{
		for(int x = 0; x < values.length; x++)
		{
			values[x].add(e.get(x));
		}
	}
	
	public Fraction get(int x)
	{
		return values[x];
	}
	
	public String queryString()
	{
		String all = "";
		
		int x;
		
		for(x = 1; x <= index; x++)
		{
			all += values[x-1] + "x" + x + "  +  ";
		}
		
		all += "_x" + x;
		
		return all;
	}
	
	public String answerString()
	{
		String all = "";
		
		int x;
		
		for(x = 1; x < index; x++)
		{
			all += values[x-1] + "x" + x + "  +  ";
		}
		
		all += values[x-1] + "x" + x;
		all += " = _";
		
		return all;
	}
	
	public String toMatrix()
	{
		String all = "";
		
		for(int x = 0; x < values.length; x++)
		{
			all += values[x].getDouble() + " ";
		}
		
		return all;
	}
	
	public String toDoubleString()
	{
		String all = "<html>";
		
		int x;
		
		for(x = 1; x < index-1; x++)
		{
			all += String.format("%.2f",values[x-1].getDouble()) + "x<font size=\"4\">" + x + "</font>  +  ";
		}
		
		all += String.format("%.2f", values[x-1].getDouble()) + "x<font size=\"4\">" + x;
		all += "</font> = " + String.format("%.2f",values[x].getDouble());
		all += "</html>";
		
		return all;
	}
	
	public String toString()
	{
		String all = "<html>";
		
		int x;
		
		for(x = 1; x < index-1; x++)
		{
			all += values[x-1].getHTMLFraction() + "x<font size=\"4\">" + x + "</font>  +  ";
		}
		
		all += values[x-1].getHTMLFraction() + "x<font size=\"4\">" + x;
		all += "</font> = " + values[x].getHTMLFraction();
		all += "</html>";
		
		return all;
	}
}

class Fraction
{
	int num;
	int den;
	
	public Fraction()
	{
		num = 0;
		den = 1;
	}
	
	public Fraction(int x)
	{
		num = x;
		den = 1;
	}
	
	public Fraction(int x, int y)
	{
		num = x;
		den = y;
		simplify();
	}
	
	public Fraction(int x, int y, boolean useless)
	{
		num = x;
		den = y;
	}
	
	public Fraction scale(int x)
	{
		return new Fraction(num*x,den*x, true);
	}
	
	public void multiply(int a)
	{
		num *= a;
		simplify();
	}
	
	public void divide(int a)
	{
		den *= a;
		simplify();
	}
	
	public void multiply(int a, int b)
	{
		num *= a;
		den *= b;
		simplify();
	}
	
	public void multiply(Fraction a)
	{
		num *= a.getNum();
		den *= a.getDen();
	}
	
	public void add(Fraction a)
	{
		if(den != a.getDen())
		{
			a.multiply(den, den);
			this.multiply(a.getDen(), a.getDen());
		}
		
		num += a.getNum();
		
		this.simplify();
		a.simplify();
	}
	
	public void simplify()
	{
		if(num == den)
		{
			num = 1;
			den = 1;
		}
		
		if(num == 0)
		{
			den = 1;
		}
		
		if(num < 0 && den < 0)
		{
			num *= -1;
			den *= -1;
		}
		
		for(int x = 2; x <= Math.abs(num); x++)
		{
			if(num%x == 0 && den%x == 0)
			{
				num /= x;
				den /= x;
				x = 1;
			}
		}
	}
	
	public int getNum()
	{
		return num;
	}
	
	public int getDen()
	{
		return den;
	}
	
	public int getRound()
	{
		double d = (double) num / den;
		
		return (int) d;
	}
	
	public double getDouble()
	{
		return (double)num/den;
	}
	
	public String getFraction()
	{
		return num + "/" + den;
	}
	
	public String getHTMLFraction()
	{
		StringBuilder sb = new StringBuilder();
		if(getDen() == 1)
		{
			sb.append(getNum());
		}
		else
		{
			sb.append("<sup><font size=\"7\">");
			sb.append(this.getNum());
			sb.append("</font></sup>");
			sb.append("/");
			sb.append("<sub><font size=\"7\">");
			sb.append(this.getDen());
			sb.append("</font></sub>");
		}
		
		return sb.toString();
	}
}
