
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*检测函数或运算符是否存在*/
class Fun_name {
	HashSet<String> para_single = new HashSet<String>();
	HashSet<String> para_double = new HashSet<String>();
	HashSet<String> para_multi = new HashSet<String>();
	HashSet<String> opt = new HashSet<String>();
	HashSet<String> bracket = new HashSet<String>();
	//使用HashSet来保存，方便查找运算符或函数名是否出现过
	Fun_name() {
 		opt.add("+");
 		opt.add("-");
 		opt.add("*");
 		opt.add("/");
 		opt.add("mod");
 		opt.add("^");
 		opt.add("%");
 		opt.add(".");
 		opt.add("!");
 		
 		bracket.add("(");
 		bracket.add(")");
 		bracket.add("[");
 		bracket.add("]");
 		
		para_single.add("sin(");
		para_single.add("cos(");
		para_single.add("tan(");
		para_single.add("arcsin(");
		para_single.add("arccos(");
		para_single.add("arctan(");
		para_single.add("sinh(");
		para_single.add("cosh(");
		para_single.add("tanh(");
		para_single.add("log10(");
		para_single.add("ln(");
		para_single.add("exp(");
		para_single.add("fact(");
		para_single.add("sqrt(");
		para_single.add("cuberoot(");
		para_single.add("arctan(");
		para_single.add("arctan(");
	
		para_double.add("log(");
		para_double.add("pow(");
		para_double.add("mod(");
		para_double.add("yroot(");
	
		para_multi.add("avg(");
		para_multi.add("sum(");
		para_multi.add("var(");
		para_multi.add("varp(");
		para_multi.add("stdev(");
		para_multi.add("stdevp(");
	}

 	//判断比较函数名时是否加入(
	//除括号外的运算符是否存在
	public boolean opt_exist(String s) {
		String str = new String(s);
		if (opt.contains(str))
			return true;
		else
			return false;
	}
	
	//括号是否存在
	public boolean bracket_exist(String s) {
		String str = new String(s);
		if (bracket.contains(str))
			return true;
		else
			return false;
	}
	//该单参函数是否存在
	//布尔值用来判断函数名是否加了左括号
	public boolean single_exist(String s, boolean only_name) {
		String str = new String(s);
 		if (only_name)
			str += "(";
 		if (para_single.contains(str))
			return true;
		else
			return false;
	}
	//双参函数是否存在
	public boolean double_exist(String s, boolean only_name) {
		String str = new String(s);
		if (only_name)
			str += "(";
		if (para_double.contains(str))
			return true;
		else 
			return false;
	}
	//多参函数是否存在
	public boolean multi_exist(String s, boolean only_name) {
		String str = new String(s);
		if (only_name)
			str += "(";
		if (para_multi.contains(str))
			return true;
		else
			return false;
	}
	//包括括号在内的运算符存在
	public boolean allopt_exist(String s) {
		String str = new String(s);
		if (opt.contains(str) || bracket.contains(str))
			return true;
		else
			return false;
	}
	//函数和运算符是否存在
	public boolean opt_fun_exist(String s, boolean only_name) {
		if ( allopt_exist(s) || multi_exist(s, only_name) || double_exist(s, only_name) || single_exist(s, only_name))
			return true;
		else 
			return false;
	}
	//函数是否存在
	public boolean fun_exist(String s, boolean only_name) {
		if (multi_exist(s, only_name) || double_exist(s, only_name) || single_exist(s, only_name))
			return true;
		else 
			return false;
	}
}

/*
* 预处理类
* 去掉空格，大写转小写，".7"->"0.7"，"+3"->"3"，"-3"->"0-3" 缺少乘号加上
*/
class Pretreatment {
	String target = new String("");//最终返回的字符串
	public String preChange (String s) throws Exception {
		//转小写并去空格
		s = s.toLowerCase().replace(" ", "");
		//先转成以空格分离的，然后再比较，运算符遇到'.'和'-'时执行添加，遇到'+'替换为空格，最后全部将空格替换为空
		String symbol = "\\.|\\+|\\-|\\*|\\/|\\%|\\^|\\,|pow\\(|yroot\\(|exp\\(|fact\\(|cuberoot\\(|sqrt\\(|arcsin\\(|arccos\\(|arctan\\(|sinh\\(|cosh\\(|tanh\\(|sin\\(|cos\\(|tan\\(|mod\\(|log10\\(|log\\(|ln\\(|avg\\(\\[|sum\\(\\[|varp\\(\\[|var\\(\\[|stdevp\\(\\[|stdev\\(\\[|\\(|\\[|\\]|\\)|e|pi";
		
		Pattern p = Pattern.compile(symbol);
		Matcher m = p.matcher(s);
		
		//按照分割符分割表达式
		String[] str = p.split(s);
		
		//存储分割后的表达式
		int count = 0;
		boolean flag;
		//将分割式子存入target中
		while(count < str.length) {
			if((flag = m.find()) || count < str.length ) {
				target += str[count];
				target += " ";
				if (flag) {
					target += m.group();
					target += " "; 
				}
			}
			count++;
		}
		//如果末尾还有多余的括号，还需继续输出
		while( m.find() )
		{
		    target += " ";
			target += m.group();
		}

		//开始格式化，如改++3的+3，改.7的0.7，加上8+-1为8+0-1
		String[] cal = target.split(" "); 
		
		/*for ( int i = 0; i < cal.length; i++) {
			System.out.println("cal[" + i + "] " + cal[i]);
		}
		*/
		//判断是否是数字
		Fun_name check = new Fun_name();
		Pattern pattern = Pattern.compile("[0-9]++");
		int begin;
		for ( begin = 0; begin < cal.length;  begin++) {
		    if (!cal[begin].isEmpty())
		        break;
		}
		if (cal[begin].equals("+")) {
		     cal[begin] = "";
		 }
		  //找到补右括号的位置
         if (cal[begin].equals("-")) {
              cal[0] = "(0" + cal[0];
              for ( int j = 1; j < cal.length; j++) {
                  if (pattern.matcher(cal[j]).matches() || cal[j].contains("e") || cal[j].contains("pi")){
                      cal[j] += ")";
                      break;
                  }
              }
          }
		for ( int i = 0; i < cal.length; i++) {
			//开始处理正负号和省略的小数点
			if (cal[i].isEmpty() || cal[i].contains(" ")){
				//处理正号的+
				if (cal[i+1].contains("+")){
					//防字符串下标溢出检测
					if (i-1 >= 0) {
						if (check.opt_fun_exist(cal[i-1], false) && !cal[i-1].contains(")")) {
 							cal[i+1] = "";
						}
					}
				}
				//处理省略0的小数点
				if (cal[i+1].contains(".")) {
					cal[i+1] = "0.";
				}
				//处理负号的-
				if (cal[i+1].contains("-")) {
					//防字符串下标溢出检测
					if (i+1 < cal.length-1 && i-1 > 0) {
						if (!cal[i-1].contains(")") && !cal[i-1].contains("]")) {
							cal[i] = "(0";
						}
						else {
							continue;
						}
					}
					//防字符串下标越界检测
					else if (i-1 <= 0) {
						cal[i] = "(0";
					}
					else {
						continue;
					}
					//找到补右括号的位置
					for ( int j = i+1; j < cal.length; j++) {
						if (pattern.matcher(cal[j]).matches() || cal[j].contains("e") || cal[j].contains("pi")){
							cal[j] += ")";
							break;
						}
					}
				}
			}
		}
		
		//补充乘号
		for ( int i = 0; i < cal.length; i++) {
			//省略乘号的两部分，前面一定是1.数字 2.e 3.pi 4.右括号
			if (pattern.matcher(cal[i]).matches() || cal[i].equals("e") || cal[i].equals("pi")|| cal[i].contains(")")) {
				for ( int j = i+1; j < cal.length; j++) {
					if (cal[j].contains(" ") || cal[j].isEmpty()) {
						continue;
					}
					else if (check.fun_exist(cal[j], false) || cal[j].contains("(") || cal[j].equals("e") || cal[j].equals("pi") || pattern.matcher(cal[j]).matches()) {
						cal[i] += "*";
						break;
					}
					else {
						break;
					}
				}
			}
		}
		
		//频繁添加的时候，用StringBuffer提高效率
		StringBuffer temp = new StringBuffer();
		//把整理后字符串返回
		for ( int i = 0; i < cal.length; i++)
			temp.append(cal[i]);
		target = temp.toString();
	
		//改小写并且去空格
		target = target.replace(" ", "");
		return target;
	}
}

/*检测括号匹配的类*/
class MatchBracketsError {
	private String str;
	private StringBuffer str_p;
	public void setString(String s) {
		//复制一份
		str = s.trim();
	}
	public void setStringBuffer(StringBuffer ptr) {
        str_p = ptr;
    }
	public Boolean check(String s, StringBuffer ptr) {
		// TODO Auto-generated method stub
		setString(s);
		setStringBuffer(ptr);
		//存括号的栈
		Stack<Character> st = new Stack<Character>();
		//避免多次调用str.charAt()
		ErrorLocation loca = new ErrorLocation();
		char a;
		for ( int i = 0; i < str.length(); i++) {
			a = str.charAt(i);
			if (a == '(')
				st.push(')');
			if (a == '[')
				st.push(']');
			if (a == ')' || a == ']') {
				if (st.isEmpty() == true) {
					loca.printError(str, str_p, i);
					str_p.append("括号匹配错误\n");
					return false;
				}
				//若弹栈元素与栈顶元素不符，避免出现[)这种错误
				if (a != st.pop()) {
				    loca.printError(str, str_p, i);
				    str_p.append("括号匹配错误\n");
					return false;
					}
			}
		}	
		if (st.isEmpty() == true) 
			return true;
		else return false;
	
	}
	
}


/*检测函数名错误的类*/
class NameError implements Callable<Boolean> {
	private String str;
	private StringBuffer str_p;
	public void setString(String s) {
		//复制一份
		this.str = s.trim();
	}
	public void setStringBuffer (StringBuffer ptr) {
	    str_p = ptr;
	}
	public Boolean call() throws Exception {
		// TODO Auto-generated method stub
		//检测表达式中是否存在函数名
		
	    boolean letterflag = false;
		for ( int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i))) {
				letterflag = true;
				break;
			}
		}
		if (!letterflag)
			return true;

		//开始检测函数名正确性
		Fun_name check = new Fun_name();
		ErrorLocation loca = new ErrorLocation();
		for ( int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			//有无函数名
			//防溢出检测
			if (Character.isLetter(ch) && ch != 'e') {
				//排除PI的影响
				//防越界处理
				if (i+2<= str.length() && ch == 'p') {
					if (str.substring(i, i+2).equals("pi")) {
						i = i+1;
						continue;
					}
				}
				//单独处理mod作为运算符而非函数名的情况
				else if (i+3 <= str.length() && ch == 'm') {
					if (str.substring(i, i+3).equals("mod")) {
						i = i+2;
						continue;
					}
				}
				//到 '(' 时为一个完整函数名
				int j = str.indexOf('(', i);
				//函数是否有括号
				if (j != -1) {
					if (check.fun_exist(str.substring(i, j), true)) {
						i = j;
					}
					else{
						//输出错误位置
						loca.printError(str, str_p, j);
						str_p.append("函数名错误\n");
						return false;
					}
				}
				
				//没有这个长度的函数名
				else {
					//输出错误位置
					loca.printError(str, str_p, i);
					str_p.append("函数名错误\n");
					return false;
				}
			}else if (ch == 'e') {
				//防越界处理
				if (i+4 < str.length()) {
					if (check.fun_exist(str.substring(i, i+4), false)){
						i = i+4;
					}
				}
				//防越界处理
				if (i+1 < str.length()) {
					if (Character.isLetter(str.charAt(i+1))){
						//输出错误位置
						loca.printError(str, str_p, i);
					    str_p.append("函数名错误\n");
						return false;
					}
				}
			}
		}
		//表达式检测完，没有错误
		return true;
	}
}

/*传参错误类*/
/*返回-1是缺参或参数格式不正确，0正确，1多参, -2为中括号位置错误, -3统计函数缺少中括号*/
//没有考虑统计函数的[]问题
class ParaError implements Callable<Integer> {
	private String str;
	private StringBuffer str_p;
	public void setString(String s) {
		str = s.trim();
	}
	public void setStringBuffer(StringBuffer ptr) {
	    str_p = ptr;
	}
	private int JudgePara() {
		//是否缺少参数的标志
		//先检测几种和函数名无关的传参错
		ErrorLocation loca = new ErrorLocation();
		int index_error;
		boolean flag_p = false;
		if ((index_error = str.indexOf(",)")) != -1)  {
		    flag_p = true;
		    loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf(",,")) != -1) {
		    flag_p = true;
			loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf("(,")) != -1){
		    flag_p = true;
			loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf("()")) != -1){
		    flag_p = true;
		    loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf("[]")) != -1){
		    flag_p = true;
			loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf("[,")) != -1){
		    flag_p = true;
			loca.printError(str, str_p, index_error);
		}
		if ((index_error = str.indexOf(",]")) != -1){
		    flag_p = true;
			loca.printError(str, str_p, index_error);
		}
		if (flag_p) {
			str_p.append("函数参数过少\n");
			return -1;
		}
		
		//检测传参
		String end_string = new String();
		//用于分割的正则表达式
		String symbol = "\\.|\\+|\\-|\\*|\\/|\\%|\\^|\\,|pow\\(|"
				+ "yroot\\(|exp\\(|fact\\(|cuberoot\\(|sqrt\\(|arcsin\\(|"
				+ "arccos\\(|arctan\\(|sinh\\(|cosh\\(|tanh\\(|sin\\(|"
				+ "cos\\(|tan\\(|mod\\(|log10\\(|log\\(|ln\\(|avg\\(\\[|"
				+ "sum\\(\\[|varp\\(\\[|var\\(\\[|stdevp\\(\\[|stdev\\(\\[|"
				+ "avg\\(|sum\\(|varp\\(|var\\(|stdevp\\(|stdev\\(|\\(|\\[|\\]\\)|\\)|\\]|mod";

		Pattern p = Pattern.compile(symbol);
		Matcher m = p.matcher(str);

		//按照分割符分割表达式
		String[] target = p.split(str);
		
		//存储分割后的表达式
		int count = 0;
		boolean flag_n;//记录matcher中有无剩余
		while(count < target.length) {
			if((flag_n = m.find()) || count < target.length ) {
				end_string += target[count];
				end_string += " ";
				if (flag_n) {
					end_string += m.group();
					end_string += " ";
				}
			}
			count++;
		}
		//如果末尾还有多余的括号，还需继续输出
		while( m.find() )
		{
			end_string += " ";
			end_string += m.group();
		}
		
		
		String[] cal = end_string.split(" "); 
		//便于后期判定是单参双参还是多参，用hashset存
		Fun_name check = new Fun_name();
		
	/*	for ( int i = 0; i < cal.length; i++) {
			System.out.println("cal[" + i + "]" + cal[i]);
		}
	*/
		//函数(和普通(的栈
		//遇到"("和类似"sin("的函数结构时入栈
		Stack<String> stack = new Stack<String>();
		//便于找到出栈括号的下标
		HashMap<String, Integer> index = new HashMap<String, Integer>();
		for (int i = 0; i < cal.length; i++) {
			index.put(cal[i], i);
		}
		
/*		for ( int i = 0; i < cal.length; i++) {
			System.out.println("cal[" + i + "] " + cal[i]);
		}
*/		
		for ( int i = 0; i < cal.length; i++) {
			//形如"fun(" "sum(["和 "(" 的入栈
			if (check.fun_exist(cal[i], false) || check.fun_exist(cal[i].replace("[", ""), false) || cal[i].compareTo("(") == 0) {
					stack.push(cal[i]);
			}
			if (cal[i].compareTo("]") == 0 || cal[i].compareTo("[") == 0) {
				loca.printError(str, str_p, i);
				str_p.append("中括号位置错误\n");
				return -2;
			}
			//遇到")" , 说明该出栈了
			if (cal[i].contains(")")) {
				String last = stack.pop();
				//单参函数只要两括号间出现一个","就算错
				if (check.single_exist(last, false)){
					//这里存任意数都可以，只是为了把整个里层括号表达式转化为一个数字，防止造成里层的逗号影响外层
					cal[i] = "0";
					for ( int j = index.get(last); j < i; j++) {
						if (cal[j].contains(",")){
							loca.printError(str, str_p, i);
							str_p.append("函数参数过多\n");
							return 1;
						}
						cal[j] = "";
					}
				}
				
				//双参函数只要出现一个以上","就算错
				if (check.double_exist(last, false)) {
					int flag = 0;
					//这里存任意数都可以，只是为了把整个里层括号表达式转化为一个数字，防止造成里层的逗号影响外层
					cal[i] = "0";
					for ( int j = index.get(last); j < i; j++) {
						if (cal[j].contains(",")) {
							flag++;
						}
						cal[j] = "";
					}
					if (flag > 1) {
						loca.printError(str, str_p, i);
						str_p.append("函数参数过多\n");
						return 1;
					}
					else if (flag < 1) {
						loca.printError(str, str_p, i);
						str_p.append("函数参数过少\n");
						return -1;
					}
				}
				
				//多参函数直接出栈
				if (check.multi_exist(last.replace("[", ""), false)) {
					//这里存任意数都可以，只是为了把整个最里层表达式转化为一个数字，防止造成里层的逗号影响外层
					cal[i] = "0";
					for ( int j = index.get(last); j < i; j++) {
						cal[j] = "";
					}
				}
				//缺少[的统计函数
				if (check.multi_exist(last, false)) {
					loca.printError(str, str_p, i);
					str_p.append("统计函数缺少中括号\n");
					return -3;
				}
			}
		}
		return 0;
	}
	public Integer call() throws Exception {
		//根据结果返回
		return JudgePara();
	}
}

/*多余小数点*/
class PointError implements Callable<Boolean> {
	private String str;
	private StringBuffer str_p;
	public void setString(String s) {
		str = s.trim();
	}
	public void setStringBuffer (StringBuffer ptr) {
	    str_p = ptr;
	}
	public Boolean call() throws Exception {
		//解决3.2.3之类错误
	    
		int point = 0;
		ErrorLocation loca = new ErrorLocation();
		for ( int i = 0; i < str.length(); i++) {
			point = 0;
		    char ch = str.charAt(i);
			if (ch == '.') {
				for ( int j = i;  j < str.length(); j++) {
				    ch = str.charAt(j);
					if (ch == '.' && j+1 == str.length()) {
                            loca.printError(str, str_p, i);
                            str_p.append("多余小数点\n");
                            return false;
					}
				
					if (Character.isDigit(ch) || ch == 'e'){
						continue;
					}
					else if (ch == '.') {
						point++;
					}
					else {
						i = j;
						if (point > 1) {
							loca.printError(str, str_p, i);
							str_p.append("多余小数点\n");
							return false;
						}
						break;
					}
				}
			}
			
		}
		return true;
	}
}

/*多余运算符或缺少运算数*/
class OperatorError implements Callable<Boolean> {
	private String str;
	private StringBuffer str_p;
	public void setString(String s) {
		str = s.trim();
	}
	public void setStringBuffer(StringBuffer ptr) {
	    str_p = ptr;
	}
	public OperatorError() {
		super();
	}
	public OperatorError(String s) {
		super();
		setString(s);
	}
	
	public Boolean call() throws Exception {
		//当运算符相遇，即为错,example: "(/" "*%", "%)"
		//但"*(" 或 ")^" 不算错 "*["   "]/"同理
		//多了一个mod
		
		//检测传参
		String end_string = new String();
		String symbol = "\\.|\\+|\\-|\\*|\\/|\\%|\\^|\\,|pow\\(|yroot\\(|exp\\(|fact\\(|cuberoot\\(|sqrt\\(|arcsin\\(|arccos\\(|arctan\\(|sinh\\(|cosh\\(|tanh\\(|sin\\(|cos\\(|tan\\(|mod\\(|log10\\(|log\\(|ln\\(|avg\\(|sum\\(|varp\\(|var\\(|stdevp\\(|stdev\\(|\\(|\\[|\\]|\\)";

		Pattern p = Pattern.compile(symbol);
		Matcher m = p.matcher(str);

		//按照分割符分割表达式
		String[] target = p.split(str);
				
		//存储分割后的表达式
		int count = 0;
		boolean flag;
		while(count < target.length) {
			if((flag = m.find()) || count < target.length ) {
				end_string += target[count];
				end_string += " ";
				if (flag) {
					end_string += m.group();
					end_string += " "; 
				}
			}
			count++;
		}
		
		//如果末尾还有多余的括号，还需继续输出
		while( m.find() )
		{
			end_string += " ";
			end_string += m.group();
		}
				
		String[] cal = end_string.split(" "); 
		Fun_name check = new Fun_name();
		//为* / ^ % mod 建立查找表,它们不能相邻
		HashSet<String> side = new HashSet<String>();
		side.add("*");
		side.add("/");
		side.add("^");
		side.add("mod");
		side.add("%");
		
		ErrorLocation loca = new ErrorLocation();
		Pattern pattern = Pattern.compile("[0-9]+");
		//先判断首尾是否有多余运算符
		if (check.opt_exist(cal[cal.length-1]) && !cal[cal.length-1].equals("!")){
			loca.printError(str, str_p, cal.length-1);
			str_p.append("运算符错误或缺少运算符\n");
			return false;
		}
		if (check.opt_exist(cal[0]) && !cal[0].equals("+") && !cal[0].equals(".") && !cal[0].equals("-")) {
			loca.printError(str, str_p, 0);
			str_p.append("运算符错误或缺少运算符\n");
			return false;
		}
		for ( int i = 0; i < cal.length; i++) {
			int opt_count = 0;
			if (check.allopt_exist(cal[i])) {
				if (check.opt_exist(cal[i])) /*side.contains(cal[i]) )*/ {
					//防溢出检测
					if (i+2 < cal.length) {
						//运算符相遇且中间没有数字
						if (((cal[i+2].contains(")") && !cal[i+2].equals("!")) || side.contains(cal[i+2])) && !pattern.matcher(cal[i+1]).matches()) {
							loca.printError(str, str_p, i);
							str_p.append("运算符错误或缺少运算符\n");
							return false;
						}
					}
				}
				//左括号直接与运算符相遇，而且除去运算符是正负号情况
				if (cal[i].contains("(")) {
					if (i+2 < cal.length) {
						if (!pattern.matcher(cal[i+1]).matches() && (side.contains(cal[i+2]) || cal[i+2].contains("+") || cal[i+2].contains("-"))) {
							loca.printError(str, str_p, i);
							str_p.append("运算符错误或缺少运算符\n");
							return false;
						}
					}
				}
				//保守处理，检测有无出现运算符连续出现三次以上，主要用于检测加减号
				for ( int j = i; j < cal.length; j++) {
					if (cal[j].contains(" ") || cal[j].isEmpty()) {
						continue;
					}
					else if (check.opt_exist(cal[j])) {
						opt_count++;
					}
					else {
						i = j;
						break;
					}
				}
				if (opt_count >= 3) {
					loca.printError(str, str_p, i);
					str_p.append("运算符错误或缺少运算符\n");
					return false;
				}
			}
		}
		return true;
	}
}

//打印错误位置函数
class ErrorLocation {
	String str;
	StringBuffer str_p;
	void setString(String s) {
		str = s.trim();
	}
	void setStringBuffer (StringBuffer ptr) {
	    str_p = ptr;
	}
	public boolean printError (String s, StringBuffer ptr, int index) {
		if (index < 0 || index > s.length()-1) {
			return false;
		}
		
		setString(s);
		setStringBuffer(ptr);
		//控制到错误点的前后距离
		int begin = 0, end = 0;
		if (index > 10 && s.length()-index > 10) {
			begin = 10;
			end = 10;
		}
		else if (index < 10 && s.length()-index > 10) {
			begin = index;
			end = 10;
		}
		else if (index > 10 && s.length()-index < 10) {
			begin = 10;
			end = s.length()-index-1;
		}
		else {
			begin = index;
			end = s.length()-index;
		}
			
		str_p.append("error around:\n\t" + str.substring(index-begin, index+end) + "\n");
		str_p.append("\t");
		for ( int i = 0; i < begin; i++) {
			str_p.append(" ");
		}
		str_p.append('^'+ "\n");
		return true;
	}
}

//检测类，提供接口
class CheckError {
	String str;
	public void setString(String s) {
		str = s.trim();
	}
	public boolean check(String s) throws InterruptedException, ExecutionException {
			MatchBracketsError err1 = new MatchBracketsError();
			ParaError err2 = new ParaError();
			NameError err3 = new NameError();
			PointError err4 = new PointError();
			OperatorError err5 = new OperatorError();
			
			FutureTask<Integer> task2 = new FutureTask<Integer>(err2);
			FutureTask<Boolean> task3 = new FutureTask<Boolean>(err3);
			FutureTask<Boolean> task4 = new FutureTask<Boolean>(err4);
			FutureTask<Boolean> task5 = new FutureTask<Boolean>(err5);
			
			StringBuffer [] print_str = new StringBuffer[5];
			
			for ( int i= 0; i <print_str.length; i++) {
			    print_str[i] = new StringBuffer();
			}
			
			setString(s);
			err1.setString(str);
			err1.setStringBuffer(print_str[0]);
			err2.setString(str);
			err2.setStringBuffer(print_str[1]);
			err3.setString(str);
			err3.setStringBuffer(print_str[2]);
			err4.setString(str);
			err4.setStringBuffer(print_str[3]);
			err5.setString(str);
			err5.setStringBuffer(print_str[4]);
			
			Thread th2 = new Thread(task2, "函数参数检测");
			Thread th3 = new Thread(task3, "函数名检测");
			Thread th4 = new Thread(task4, "小数点多余检测");
			Thread th5 = new Thread(task5, "运算符多余检测");

			if (!err1.check(str, print_str[0])) {
			    System.out.println(print_str[0]);
			    return false;
			}
			th2.start();
			th3.start();
			th4.start();
			th5.start();
	
			th2.join();
			th3.join();
			th4.join();
			th5.join();
		
			for ( int i = 0; i < print_str.length; i++) {
			    if (print_str[i] != null)
			        System.out.print(print_str[i]);
			}
			
			if (task2.get()!=0 || !task3.get() || !task4.get() || !task5.get())
				return false;
			return true;
			
		}
}

class PreDeal {
	String str;
	void setString(String s) {
		str = s.trim();
	}
	private String pre_deal(String s) {
	    StringBuilder temp = new StringBuilder(s);
	    for ( int i = 0; i < temp.length(); i++) 
	    {
	        if( temp.charAt(i) == '-' )
	        {
	        	if( i+1 <= temp.length() )
	        	{
	        		if( temp.charAt(i+1) == '-' )
	        		{
	        			temp.replace(i, i+2, "+");
	        		}
	        	}
	        }
	        if( temp.charAt(i) == '+' )
	        {
	        	if( i+1 <= temp.length() )
	        	{
	        		if( temp.charAt(i+1) == '+' )
	        		{
	        			temp.replace(i, i+2, "+");
	        		}
	        	}
	        }
	        if( temp.charAt(i) == '+' )
	        {
	        	if( i+1 <= temp.length() )
	        	{
	        		if( temp.charAt(i+1) == '-' )
	        		{
	        			temp.replace(i, i+2, "-");
	        		}
	        	}
	        }
	        if( temp.charAt(i) == '-' )
	        {
	        	if( i+1 <= temp.length() )
	        	{
	        		if( temp.charAt(i+1) == '+' )
	        		{
	        			temp.replace(i, i+2, "-");
	        		}
	        	}
	        }
	    }
	    return temp.toString().toLowerCase().replace(" ", "");
	}
	
	public String deal(String s) throws Exception {
		setString(s);
		
		if (s.replace(" ", "").replace("\t", "").isEmpty())
		    return null;
		
		CheckError err = new CheckError();
		Pretreatment pre = new Pretreatment();
		
		if (err.check(pre_deal(s) ) ) {
			return pre.preChange(pre_deal(s));
		}
		else return null;
	}
}
