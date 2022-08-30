package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents an assignment statement in our grammar
 * @author Dawson Chen
 * @version 10/1/19
 */
public class Assignment extends Statement
{
    private String var;
    private Expression expr;
    /**
     * Instantiates an Assignment with given variables and assigned expressions
     * @param v the given variable
     * @param e the given expression to be assigned
     */
    public Assignment(String v, Expression e)
    {
        var = v;
        expr = e;
    }
    /**
     * Executes the assignment statement by assigning the given
     * expression to the given variable in the given environment
     */
    @Override
    public void exec(Environment env) 
    {
        env.setVariable(var, expr.eval(env));
    }
    /**
     * Emits a sequence of MIPS instructions corresponding to this class to a file
     * @param e an Emitter that will handle identifying writing the instructions
     */
    public void compile(Emitter e)
    {
//    	la $a0, globalVariable #get address
//    	   li $a1, 11 #new value
//    	   sw $a1 0($a0) #save new value
    	expr.compile(e);
    	if(!e.isLocalVariable(var))
    		e.emit("sw $v0 var" + var);
    	else
    	{
    		e.emit("sw $v0 " + e.getOffset(var) + "($sp)");
    	}
    } 
}