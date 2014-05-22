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
 * <br/>������������ȼ����洢��HashMap��
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
 * Decription �����ĸ�����������׺���ʽתΪ��׺���ʽ
 * @author Junco
 */
public class translate {
	public static void main( String[] args ) throws Exception
	{
		System.out.print(">> ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		
		//������exit��quit��q�����˳�����
		if( input.equals("exit") || input.equals("quit") || input.equals("q") )
		{
			System.out.println("Quit Calculator!");
			System.exit(0);
		}

		//������ı��ʽ����Ԥ����
		PreDeal pre = new PreDeal();
		input = pre.deal(input);
		if( input == null )
		{
			main(null);
		}
	
		//�Ա��ʽ���в�������������ķ���
		LinkedList<String> list = desprate(input);
/*
		//���Ա��ʽ�������
		Iterator<String> it = list.iterator();
		while( it.hasNext() )
		{
			System.out.print( it.next()+" " );
		}
		System.out.println();
*/		
		//�����ʽ�еĲ�������Stirng��ʽת��ΪDouble��
		LinkedList<TokenFormat> newlist = strToDouble(list);
		
		//�������ı��ʽ����׺��ʽת��Ϊ��׺��ʽ
		LinkedList<TokenFormat> result = midToRPN(newlist);
/*	
		//���Ա��ʽstring to double���
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
		//��ת��Ϊ��׺��ʽ�ı��ʽ���м���
		Main cal = new Main();
		System.out.println(">> " + cal.calculate_express(result));
		
		main(null);
}
	
	/**
	 * <br/>������ı��ʽ���в�������������ķ��룬�������������������ΪString�ͣ����ֱ����LinkedList��ÿһ������С�
	 * @param input  �û������ͨ������ƥ�䡢������ı��ʽ
	 * @return LinkedList������ÿһ���Ϊһ��token�����������������������
	 */
	public static LinkedList<String> desprate( String input )
	{
		//������ʽ����Ҫ�ָ�ķ���
		String symbol = "\\+|\\-|\\*|\\/|\\%|\\(|\\)|\\[|\\]|\\^|\\!|,|"
				        + "pow|sqrt|sinh|cosh|tanh|arcsin|arcos|arctan|"
				        + "sin|cos|tan|mod|log10|log|ln|exp|avg|sum|varp|"
				        + "var|fact|cuberoot|yroot|stdevp|stdev";
		Pattern p = Pattern.compile(symbol);
		Matcher m = p.matcher(input);
		
		//���շָ���ָ���ʽ
		String[] str = p.split(input);
		
		//�洢�ָ��ı��ʽ
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
		//���ĩβ���ж�������ţ�����������
		while( m.find() )
		{
			list.add( m.group() );
		}
		//��input���һ��token�ǲ����������轫�����list��
		//ͨ���Ƚ�list�����һ��String��Indexֵ��input���һ��ֵ�Ƿ���ͬ��ʵ��
		//�����ͬ�����轫str���һ��Ԫ�ؼ���list��
		if( input.lastIndexOf(list.get(list.size()-1)) != input.length()-1 )
		{
			list.add( str[count-1] );
		}
		
		//�������������������ʱ�����split����һ���մ���������Ŀմ���list��ȥ��
		for( int i=0; i<list.size(); i++ )
		{
			if( list.get(i).equals("") )
				list.remove(i);
		}
		return list;
	}
	
	/**
	 * <br/>��LinkedList�еĲ�������Stringת��Ϊdouble��
	 * @param list	��׺���ʽtoken��ʽ
	 * @return	�µ�LinkedList��Ķ���newlist
	 */
	public static LinkedList<TokenFormat> strToDouble( LinkedList<String> list )
	{
		LinkedList<TokenFormat> newlist = new LinkedList<>();
		Iterator<String> it = list.iterator();
		while( it.hasNext() )
		{
			String temp = it.next();
			char first = temp.charAt(0);
			//�����ǰ��String�����ֿ�ͷ������ת��Ϊdouble��ʽ
			if( first>='0' && first<='9' )
			{
				TokenFormat ob = new TokenFormat();
				//���嵱flagΪfalseʱ��ʾ��ǰ����洢������
				ob.flag = false;
				ob.num = Double.parseDouble(temp);				
				newlist.add(ob);
			}
			//�����ǰ��StringΪ��Ȼ����e�������滻Ϊe��double��ʽ
			else if( first=='e' && temp.length()==1 )
			{
				TokenFormat ob = new TokenFormat();
				ob.flag = false;
				ob.num = Math.E;
				newlist.add(ob);
			}
			//�����ǰ��StringΪԲ����PI�������滻ΪPI��double��ʽ
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
				//���嵱flagΪtrueʱ����ʾ��ǰ����洢�����
				ob.flag = true;
				ob.op = temp;
				newlist.add(ob);
			}
		}
		return newlist;
	}
	
	/**
	 * <br/>����׺���ʽtoken��ʽת��Ϊ��׺��ʽ(�沨�����ʽ RPN)
	 * @param list	��׺���ʽ
	 * @return	�洢��׺���ʽ��token��ʽ
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
			//����ǰ�����д洢����������ʱ��ֱ��д���±�				
			if( ob.flag == false )
			{
				newlist.add(ob);
			}
			else
			{
				//�����ǰ��������ͳ�ƺ����еĶ��ŷָ��������ջ�е���һ�������
				//�����ǰ�����������������еĶ��ŷָ�������ֱ���Թ�
				if( ob.op.equals(",") )
				{
					if( !stack.peek().op.equals("(") &&
						!stack.peek().op.equals("[") )
					{
						newlist.add(stack.pop());
					}
					continue;
				}
				//�����ǰ��������ͳ�ƺ����е��������ţ�������ջ��������д���±��У���Ϊ�������������ķָ�
				else if( ob.op.equals("[") )
				{
					stack.push(ob);
					newlist.add(ob);
				}
				//�����ǰ������������ţ���ջ��Ԫ�����γ�ջ��ֱ������������Ϊֹ
				else if( ob.op.equals(")") || 
						 ob.op.equals("]") )
				{
					while( !stack.peek().op.equals("(") && 
						   !stack.peek().op.equals("[") )
					{
						//��ջ�������ֱ�Ӽ����±���
						newlist.add(stack.pop());
					}
					//���������Ҳ��ջ��������׺���ʽ�в���Ҫ����
					stack.pop();
				}
				//�����ǰ������������ţ���ջ��Ԫ��Ϊ�գ���ǰ��������ȼ����ڵ���ջ����������ȼ�����Ϊͳ�ƺ������򽫵�ǰ�������ջ
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
				//�����ǰ��������ȼ�С��ջ��Ԫ�����ȼ�����ջ��Ԫ�����γ�ջ��ֱ��ջ�ջ�����������Ϊֹ
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
					//�ڵ�ջ������ɺ󣬻�Ҫ����ǰ�������ջ
					stack.push(ob);
				}
			}
		}	
		//�����в��������Ѽ����±���ʱ����ջ������Ԫ�أ����轫ջ��Ԫ�ص����������±���
		while( stack.size() != 0 )
		{
			newlist.add( stack.pop() );
		}
		return newlist;
	}
}
