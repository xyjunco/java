import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Struct.TokenFormat;

/**
 * <br/>定义运算符优先级，存储于HashMap中
 * @author Junco
 */
class Operator {
	public HashMap<String, Integer> level;
	public Operator() {
		level = new HashMap<>();
		level.put("+", 1);
		level.put("-", 1);
		level.put("*", 2);
		level.put("/", 2);
		level.put("%", 3);
		level.put("^", 3);
		level.put("pow", 3);
		level.put("mod", 3);
		level.put("sin", 3);
		level.put("cos", 3);
		level.put("tan", 3);
		level.put("sinh", 3);
		level.put("cosh", 3);
		level.put("tanh", 3);
		level.put("arcsin", 3);
		level.put("arcos", 3);
		level.put("arctan", 3);
		level.put("log", 3);
		level.put("log10", 3);
		level.put("ln", 3);
		level.put("exp", 3);
		level.put("sqrt", 3);
		level.put("cuberoot", 3);
		level.put("yroot", 3);	
		level.put("sum", 4);
		level.put("avg", 4);
		level.put("var", 4);
		level.put("varp", 4);
		level.put("stdev", 4);
		level.put("stdevp", 4);
		level.put("!", 5);
		level.put("fact", 5);
		level.put("[", 0);
		level.put("]", 6);
		level.put("(",0);
		level.put(")",6);
	}
}

/**
 * Decription 包含四个方法，将中缀表达式转为后缀表达式
 * @author Junco
 */
public class translate {
	public static void main( String[] args ) throws Exception
	{
		System.out.print(">> ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		
		//若输入exit，quit或q，则退出程序
		if( input.equals("exit") || input.equals("quit") || input.equals("q") )
		{
			System.out.println("Quit Calculator!");
			System.exit(0);
		}

		//对输入的表达式进行预处理
		PreDeal pre = new PreDeal();
		input = pre.deal(input);
		if( input == null )
		{
			main(null);
		}
	
		//对表达式进行操作数、运算符的分离
		LinkedList<String> list = desprate(input);
/*
		//测试表达式分离输出
		Iterator<String> it = list.iterator();
		while( it.hasNext() )
		{
			System.out.print( it.next()+" " );
		}
		System.out.println();
*/		
		//将表达式中的操作数由Stirng形式转换为Double型
		LinkedList<TokenFormat> newlist = strToDouble(list);
		
		//将分离后的表达式由中缀形式转换为后缀形式
		LinkedList<TokenFormat> result = midToRPN(newlist);
/*	
		//测试表达式string to double输出
		Iterator<TokenFormat> p = result.iterator();
		while( p.hasNext() )
		{
			TokenFormat ob = p.next();
			if( ob.flag )
				System.out.print(ob.op + " ");
			else
				System.out.print(ob.num + " ");
		}
*/
		//将转换为后缀形式的表达式进行计算
		Main cal = new Main();
		System.out.println(">> " + cal.calculate_express(result));
		
		main(null);
}
	
	/**
	 * <br/>对输入的表达式进行操作数和运算符的分离，分离后操作数和运算符仍为String型，但分别存于LinkedList的每一个结点中。
	 * @param input  用户输入的通过括号匹配、错误检测的表达式
	 * @return LinkedList，其中每一结点为一个token（操作数或运算符，函数）
	 */
	public static LinkedList<String> desprate( String input )
	{
		//正则表达式，需要分割的符号
		String symbol = "\\+|\\-|\\*|\\/|\\%|\\(|\\)|\\[|\\]|\\^|\\!|,|"
				        + "pow|sqrt|sinh|cosh|tanh|arcsin|arcos|arctan|"
				        + "sin|cos|tan|mod|log10|log|ln|exp|avg|sum|varp|"
				        + "var|fact|cuberoot|yroot|stdevp|stdev";
		Pattern p = Pattern.compile(symbol);
		Matcher m = p.matcher(input);
		
		//按照分割符分割表达式
		String[] str = p.split(input);
		
		//存储分割后的表达式
		LinkedList<String> list = new LinkedList<>();

		int count = 0;
		while( count < str.length )
		{
			if( m.find() )
			{
				list.add( str[count] );
				list.add( m.group() );
			}
			count++;
		}
		//如果末尾还有多余的括号，还需继续输出
		while( m.find() )
		{
			list.add( m.group() );
		}
		//若input最后一个token是操作数，还需将其加入list中
		//通过比较list中最后一个String的Index值与input最后一个值是否相同来实现
		//如果不同，则还需将str最后一个元素加入list中
		if( input.lastIndexOf(list.get(list.size()-1)) != input.length()-1 )
		{
			list.add( str[count-1] );
		}
		
		//当两个运算符或函数相邻时，会多split出来一个空串，将多出的空串在list中去除
		for( int i=0; i<list.size(); i++ )
		{
			if( list.get(i).equals("") )
				list.remove(i);
		}
		return list;
	}
	
	/**
	 * <br/>将LinkedList中的操作数由String转化为double型
	 * @param list	中缀表达式token形式
	 * @return	新的LinkedList类的对象newlist
	 */
	public static LinkedList<TokenFormat> strToDouble( LinkedList<String> list )
	{
		LinkedList<TokenFormat> newlist = new LinkedList<>();
		Iterator<String> it = list.iterator();
		while( it.hasNext() )
		{
			String temp = it.next();
			char first = temp.charAt(0);
			//如果当前的String以数字开头，则将其转化为double形式
			if( first>='0' && first<='9' )
			{
				TokenFormat ob = new TokenFormat();
				//定义当flag为false时表示当前对象存储操作数
				ob.flag = false;
				ob.num = Double.parseDouble(temp);				
				newlist.add(ob);
			}
			//如果当前的String为自然对数e，则将其替换为e的double形式
			else if( first=='e' && temp.length()==1 )
			{
				TokenFormat ob = new TokenFormat();
				ob.flag = false;
				ob.num = Math.E;
				newlist.add(ob);
			}
			//如果当前的String为圆周率PI，则将其替换为PI的double形式
			else if( first=='p' && temp.length()==2 )
			{
				TokenFormat ob = new TokenFormat();
				ob.flag = false;
				ob.num = Math.PI;
				newlist.add(ob);
			}
			else
			{
				TokenFormat ob = new TokenFormat();
				//定义当flag为true时，表示当前对象存储运算符
				ob.flag = true;
				ob.op = temp;
				newlist.add(ob);
			}
		}
		return newlist;
	}
	
	/**
	 * <br/>将中缀表达式token形式转化为后缀形式(逆波兰表达式 RPN)
	 * @param list	中缀表达式
	 * @return	存储后缀表达式的token形式
	 */
	public static LinkedList<TokenFormat> midToRPN( LinkedList<TokenFormat> list )
	{
		LinkedList<TokenFormat> newlist = new LinkedList<>();
		Stack<TokenFormat> stack = new Stack<>();
		Operator op = new Operator();
		Iterator<TokenFormat> it = list.iterator();
		while( it.hasNext() )
		{
/*
			Iterator<TokenFormat> p = newlist.iterator();
			while( p.hasNext() )
			{
				TokenFormat ob = p.next();
				if( ob.flag )
					System.out.print(ob.op + " ");
				else
					System.out.print(ob.num + " ");
			}
			System.out.println();
*/
			TokenFormat ob = it.next();
			//当当前对象中存储的是运算数时，直接写入新表				
			if( ob.flag == false )
			{
				newlist.add(ob);
			}
			else
			{
				//如果当前操作符是统计函数中的逗号分隔符，则从栈中弹出一个运算符
				//如果当前操作符是其它函数中的逗号分隔符，则直接略过
				if( ob.op.equals(",") )
				{
					if( !stack.peek().op.equals("(") &&
						!stack.peek().op.equals("[") )
					{
						newlist.add(stack.pop());
					}
					continue;
				}
				//如果当前操作符是统计函数中的左中括号，则将其入栈，并将其写入新表中，作为与其它操作数的分隔
				else if( ob.op.equals("[") )
				{
					stack.push(ob);
					newlist.add(ob);
				}
				//如果当前运算符是右括号，则将栈中元素依次出栈，直到遇到左括号为止
				else if( ob.op.equals(")") || 
						 ob.op.equals("]") )
				{
					while( !stack.peek().op.equals("(") && 
						   !stack.peek().op.equals("[") )
					{
						//出栈的运算符直接加入新表中
						newlist.add(stack.pop());
					}
					//最后将左括号也出栈丢弃，后缀表达式中不需要括号
					stack.pop();
				}
				//如果当前运算符是左括号，或栈中元素为空，或当前运算符优先级大于等于栈顶运算符优先级，或为统计函数，则将当前运算符入栈
				else if( ob.op.equals("(") || 
						 stack.size() == 0 || 
						 op.level.get(ob.op) > op.level.get(stack.peek().op) || 
						 op.level.get(ob.op) == 4 )
				{
					stack.push(ob);
				}
				else if( op.level.get(ob.op) == op.level.get(stack.peek().op) )
				{
					newlist.add(stack.pop());
					stack.push(ob);
				}
				//如果当前运算符优先级小于栈顶元素优先级，则将栈中元素依次出栈，直到栈空或遇到左括号为止
				else if( op.level.get(ob.op) < op.level.get(stack.peek().op) )
				{
					while( !stack.peek().op.equals("(") &&
						   !stack.peek().op.equals("[") )
					{
						newlist.add( stack.pop() );
						if( stack.size() == 0 )
						{
							break;
						}
					}
					//在弹栈操作完成后，还要将当前运算符入栈
					stack.push(ob);
				}
			}
		}	
		//待所有操作数都已加入新表中时，若栈中仍有元素，还需将栈中元素弹出并加入新表中
		while( stack.size() != 0 )
		{
			newlist.add( stack.pop() );
		}
		return newlist;
	}
}
