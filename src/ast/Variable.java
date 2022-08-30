package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents a variable expression in our grammar
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class Variable extends Expression 
{
    private String name;

    /**
     * Instantiates a Variable with a given identifying name
     * @param n is the variable name
     */
    public Variable(String n) 
    {
        name = n;
    }
    /**
     * Returns the value corresponding to the given variable name
     * @return the value of the variable in the given environment
     */
    @Override
    public int eval(Environment env) 
    {
//    	System.out.println("looking for " + name);
        return env.getVariable(name);
    }
    /**
     * Returns the name of the variable
     * @return the variable name
     */
    public String getName() {
    	return name;
    }
    /**
     * Emits a sequence of MIPS instructions corresponding to a certain Expression to a file
     * @param e an Emitter that will handle identifying writing the instructions
     */
    @Override
    public void compile(Emitter e)
    {
    	if(!e.isLocalVariable(name))
    	{
	//    	la $t0 varx
	//    	lw $v0 ($t0)
	    	e.emit("la $t0 var" + name);
	    	e.emit("lw $v0 ($t0)");    		
    	}
    	else
    	{
    		e.emit("lw $v0 " + e.getOffset(name) + "($sp)");
    	}
    } 
}