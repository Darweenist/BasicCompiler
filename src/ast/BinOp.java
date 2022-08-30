package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents a binary operation expression, where an operation is performed on two expressions.
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class BinOp extends Expression 
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Instantiates a new BinOp expression with a given op and 2 given exp
     * 
     * @param op the given operator
     * @param e1 the first given expression
     * @param e2 the 2nd given expression
     */
    public BinOp(String op, Expression e1, Expression e2) 
    {
        this.op = op;
        exp1 = e1;
        exp2 = e2;
    }

    @Override
    public void compile(Emitter e)
    {
    	//li $v0 3
    	exp1.compile(e);
    	e.emitPush("$v0");
    	exp2.compile(e);
    	e.emitPop("$t0");
    	if(op.equals("+"))
    		e.emit("addu $v0 $t0 $v0");
    	else if(op.equals("-"))
    		e.emit("subu $v0 $t0 $v0");
    	else if(op.equals("*"))
    	{
    		e.emit("mult $t0 $v0");
    		e.emit("mflo $v0");
    	}
    	else if(op.equals("/"))
    	{
    		e.emit("div $t0 $v0");
    		e.emit("mflo $v0");
    	}
    }
    
    /**
     * Evaluates the binary operation by performing an arithmetic operation
     * @return the result of the operation
     */
    @Override
    public int eval(Environment env) 
    {
        if(op.equals("+"))
            return exp1.eval(env) + exp2.eval(env);
        else if(op.equals("-"))
            return exp1.eval(env) + exp2.eval(env);
        else if(op.equals("*"))
            return exp1.eval(env) * exp2.eval(env);
        else if(op.equals("/"))
            return exp1.eval(env) / exp2.eval(env);
        else if(op.equals("mod"))
            return exp1.eval(env) % exp2.eval(env);
        return 0;
    }
}