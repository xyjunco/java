/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dazzle
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import Struct.TokenFormat;

public class Main {
  
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
   }
    
String calculate_express(LinkedList<TokenFormat> Arr) //计算
{
    Stack<String> stack = new Stack<String>();
    String element = null, result = null;
    
    Iterator<TokenFormat> p = Arr.iterator();
    while(p.hasNext())
     {   
    	 TokenFormat ob = p.next();
         if(ob.flag){    						  //若为运算符号
        	 element = ob.op;
             result = doOperate(element,stack);   //把运算符传进函数运算
             stack.push(result); 				  //把结果转成String 再压入栈
         }
         else{ 
             element = Double.toString(ob.num);   //否则换成数字压入栈中
             stack.push(element);              } 
}
        return result;
}

private String doOperate(String element,Stack<String> stack)
{
    if(element.equals("+"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        result = result1 + result2;
        return Double.toString(result);
    }
    else if(element.equals("-"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        result = result2 - result1;
        return Double.toString(result);
    }
    else if(element.equals("*"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        result = result1 * result2;
        return Double.toString(result);
    }
    else if(element.equals("/"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        if(result1 == 0)
            return null;
        result = result2 / result1;
        return Double.toString(result);
    }
    else if(element.equals("%"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        result = result2 % result1;
        return Double.toString(result);
    }
    else if(element.equals("^"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result2 = Double.parseDouble(stack.pop());
        double result;
        result = Math.pow(result2 , result1);
        return Double.toString(result);
    }
    else if(element.equals("!"))
    {
        double result1 = Double.parseDouble(stack.pop());
        double result = 1;
        for(int i = 1;i <= result1;i++)
        {
            result *= i;
        }
        return Double.toString(result);
    }
    
    else if(element.equals("sin"))
    {
        double result = Double.parseDouble(stack.pop());
        result = result*Math.PI/180;
        return Double.toString(Math.sin(result));
    }
       
        else if(element.equals("cos"))
        {
            double result = Double.parseDouble(stack.pop());
            result = result*Math.PI/180;
            return Double.toString(Math.cos(result));
        }
        
        else if(element.equals("tan"))
        {
            double result = Double.parseDouble(stack.pop());
            if( result==90 || result==270 )
            {
                return "Indeterminate";
            }
            result = result*Math.PI/180;
            System.out.println(result);
            return Double.toString(Math.tan(result));
        }   
        else if(element.equals("arcsin"))
        {
            double result = Double.parseDouble(stack.pop());
            if( result<-1 || result>1 )
            {
            	return "Indeterminate";
            }
            return Double.toString(Math.asin(result));
        }
        else if(element.equals("arccos"))
        {
            double result = Double.parseDouble(stack.pop());
            if( result<-1 || result>1 )
            {
            	return "Indeterminate";
            }
            return Double.toString(Math.acos(result));
        }
        else if(element.equals("arctan"))
        {
            double result = Double.parseDouble(stack.pop());
            return Double.toString(Math.atan(result));
        }
        else if(element.equals("sinh"))
        {
            double result = Double.parseDouble(stack.pop());
            return Double.toString(Math.sinh(result));
        }
        else if(element.equals("cosh"))
        {
            double result = Double.parseDouble(stack.pop());
            return Double.toString(Math.cosh(result));
        }
        else if(element.equals("tanh"))
        {
            double result = Double.parseDouble(stack.pop());
            return Double.toString(Math.tanh(result));
        }
        else if(element.equals("log"))
        {
            double result1 = Double.parseDouble(stack.pop());
            double result2 = Double.parseDouble(stack.pop());
            double result =0;
            if(Math.log(result2) <= 0)
            {
                System.out.println("error expression");
                return null;
            }
            result = Math.log(result1)/Math.log(result2);
            return (Double.toString(result));
        }
        else if(element.equals("log10"))
        {
             double result = Double.parseDouble(stack.pop());
            if(result <= 0)
            {
                System.out.println("error expression!!");
                return null;
            }
            return Double.toString(Math.log10(result));
        }
        else if(element.equals("ln"))
        {
            double result = Double.parseDouble(stack.pop());
            if(result <= 0)
            {
                System.out.println("error expression!!");
                return null;
            }
            return Double.toString(Math.log(result));
        }
        else if(element.equals("pow"))
        {
            double result1 = Double.parseDouble(stack.pop());
            double result2 = Double.parseDouble(stack.pop());
            double result =0;
            result = Math.pow(result2, result1);
            return (Double.toString(result));
        }
        else if(element.equals("exp"))
        {
           double result = Double.parseDouble(stack.pop());
            return Double.toString(Math.exp(result));
        }
        else if(element.equals("fact"))
        {
            long result = (long)Double.parseDouble(stack.pop());
            long all=1;
            for(int i=1;i<=result;i++)
                all *= i;
            return Long.toString(all);
        }
        else if(element.equals("mod"))
        {
             double result1 = Double.parseDouble(stack.pop());
            double result2 = Double.parseDouble(stack.pop());
            double result =0;
            result = result2 % result1;
            return (Double.toString(result));
        }
        else if(element.equals("sqrt"))
        {
              double result = Double.parseDouble(stack.pop());
              if(result < 0)
              {
                    System.out.println("error expression");
                    return null;
               }
            return Double.toString(Math.sqrt(result));
        }
        else if(element.equals("cuberoot"))
        {
            double result = Double.parseDouble(stack.pop());
              if(result < 0)
              {
                    System.out.println("error expression");
                    return null;
               }
            return Double.toString(Math.cbrt(result));
        }
        else if(element.equals("yroot"))
        {
            double result1 = Double.parseDouble(stack.pop());
            double result2 = Double.parseDouble(stack.pop());
            double result = 0;
            
            if(result1 == 0)
                return Double.toString(Math.pow(result2,0));
            
            result = Math.pow(result2,1/result1);   //若y=0
            return Double.toString(result);
        }
        else  if(element.equals("sum"))
        {
  /*          String op_or_data = stack.pop();
            double sum=0,avg=0;
            int count=0;
            while(op_or_data != "[")
            {
                sum += Double.parseDouble(op_or_data);
                count++;
                op_or_data = stack.pop();
            }
            avg = sum/count;
            return (Double.toString(avg));
            */
            String sum = null;
            sum = total_cal(stack,1);
            return sum;
        }
    else  if(element.equals("avg"))
        {
         /*       String op_or_data = stack.pop();
            double sum=0;
            int count=0;
            while(op_or_data != "[")
            {
                sum += Double.parseDouble(op_or_data);
                count++;
                op_or_data = stack.pop();
            }
            return (Double.toString(sum));
                 */
            String avg = null;
            avg = total_cal(stack,2);
            return avg;
        }
    
    else  if(element.equals("var"))
        {
            String var = null;
            var = total_cal(stack,3);
            return var;
        }
    else  if(element.equals("varp"))
        {
            String varp = null;
            varp = total_cal(stack,4);
            return varp;
        }
    else  if(element.equals("stdev"))
        {
            String stdev = null;
            stdev = total_cal(stack,5);
            return stdev;
        }
    else  if(element.equals("stdevp"))
        {
            String stdevp = null;
            stdevp = total_cal(stack,6);
            return stdevp;
        }
            return null;
        ///统计函数
    
}

String total_cal(Stack<String> stack,int i)
{
            String op_or_data = stack.pop();
            double sum=0,avg=0,var=0,varp=0,stdev=0,stdevp=0;
            int count=0;
            double[] aRR = new double[stack.size()];
            
            if(op_or_data == "[")
                return null;
            
            while(op_or_data != "[")
            {   
                sum += Double.parseDouble(op_or_data);
                aRR[count] = Double.parseDouble(op_or_data);
                count++;
                op_or_data = stack.pop();
                if( op_or_data == null )
                	break;
            }
            
            if(i == 1)
            {
                return Double.toString(sum);
            }
            
             avg = sum/count;
             if(i == 2)
            {
                return Double.toString(avg);
            }
             
             var = cal_var(avg,count,aRR);
             if(i == 3)
            {
                return Double.toString(var);
            }
             
    //         varp = Math.sqrt(var);
             varp = cal_varp(avg, count, aRR);
            
             if(i == 4)
            {
                return Double.toString(varp);
            }
             
   //          stdev = cal_stdev(avg,count,aRR);
             stdev = Math.sqrt(var);
             if(i == 5)
            {
                return Double.toString(stdev);
            }
             
             stdevp = Math.sqrt(varp);
             if(i == 6)
            {
                return Double.toString(stdevp);
            }
        return null;        
}

double cal_var(double avg,int count,double aRR[])
{
        double var = 0,var1 = 0;
        
        //计算每一个数的偏�?/
        for(int i =0;i<count;i++)
        {
            var1 += Math.pow((aRR[i]-avg),2);
        }
        
        var = var1/(count-1);   //除n求方
        
        return var;
    
}
double cal_varp(double avg,int count,double aRR[])
{
        double stdev = 0,stdev1 = 0;
        
        //计算每一个数的偏�?/
        for(int i =0;i<count;i++)
        {
            stdev1 += Math.pow((aRR[i]-avg),2);
        }
        
        stdev = stdev1/count;   //除n求方
        
        return stdev;
    
}
}
           