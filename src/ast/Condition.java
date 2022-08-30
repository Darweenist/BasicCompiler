package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the condition in an if statement in our grammar. Has 2 expressions and a relative
 * operator to compare them and determine whether the condition is true or false.
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class Condition extends Expression
{
    private Expression exp1;
    private Expression exp2;
    private String relOp;
    /**
     * Instantiates a WriteLn statement with a given expression
     * @param e1 is the first expression in the if statement
     * @param e2 is the second expression in the if statement
     * @param relOp is the relative operator that compares the two expressions
     */
    public Condition(Expression e1, Expression e2, String relOp)
    {
        exp1 = e1;
        exp2 = e2;
        this.relOp = relOp;
    }
    /**
     * Method: eval
     * @return 1 if exp1 and exp2 are related by the relOp(the condition is true);
     *      otherwise return 0
     */
    @Override
    public int eval(Environment env)
    {
        if(relOp.equals("="))
        {
            if(exp1.eval(env) == exp2.eval(env))
                return 1;
            return 0;
        }
        else if(relOp.equals("<>"))
        {
            if(exp1.eval(env) != exp2.eval(env))
                return 1;
            return 0;
        }
        else if(relOp.equals("<"))
        {
            if(exp1.eval(env) < exp2.eval(env))
                return 1;
            return 0;
        }
        else if(relOp.equals(">"))
        {
            if(exp1.eval(env) > exp2.eval(env))
                return 1;
            return 0;
        }
        else if(relOp.equals("<="))
        {
            if(exp1.eval(env) <= exp2.eval(env))
                return 1;
            return 0;
        }
        else if(relOp.equals(">="))
        {
            if(exp1.eval(env) >= exp2.eval(env))
                return 1;
            return 0;
        }
        return 0;
    }
	/**
 	* Emits a sequence of MIPS instructions corresponding to a certain Expression to a file
 	* @param e an Emitter that will handle identifying writing the instructions
 	*/
	public void compile(Emitter e, String retAd) 
	{
		exp1.compile(e);
		e.emit("move $t0 $v0");
		exp2.compile(e);
		if(relOp.equals("<>"))
        {
			e.emit("beq $t0 $v0 " + retAd);
        }
        else if(relOp.equals("="))
        {
        	e.emit("bne $t0 $v0 " + retAd);
        }
        else if(relOp.equals(">="))
        {
        	e.emit("blt $t0 $v0 " + retAd);
        }
        else if(relOp.equals("<="))
        {
        	e.emit("bgt $t0 $v0 " + retAd);
        }
        else if(relOp.equals(">"))
        {
        	e.emit("ble $t0 $v0 " + retAd);
        }
        else if(relOp.equals("<"))
        {
        	e.emit("bge $t0 $v0 " + retAd);
        }
	}
} 