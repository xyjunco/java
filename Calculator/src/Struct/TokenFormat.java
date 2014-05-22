package Struct;

/**
 * 定义中缀转换成后缀的包裹格式
 * <br/>定义当flag为false时表示当前对象存储操作数 ，num有效
 * <br/>当flag为true时，表示当前对象存储运算符， op有效
 * @author Junco
 */
public class TokenFormat {

    public boolean flag;
    public double num;
    public String op;
}

