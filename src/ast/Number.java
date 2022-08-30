package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents a number in the given grammar
 * 
 * @author Dawson Chen
 * @version 10/1/2019
 */
public class Number extends Expression 
{
    private int value;

    /**
     * Instantiates the value of the number with a specified integer
     * 
     * @param n the given value for value
     */
    public Number(int n) 
    {
        value = n;
    }
    /**
     * Returns the integer provided
     * @return the given integer
     */
    @Override
    public int eval(Environment env) 
    {
        return value;
    }
    @Override
    public void compile(Emitter e)
    {
    	//li $v0 3
    	e.emit("li $v0 " + value);
    }
}