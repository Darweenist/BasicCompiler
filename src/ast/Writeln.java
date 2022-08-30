package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the WriteLn statement in our grammar
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Instantiates a WriteLn statement with a given expression
     * @param exp the expression to be written
     */
    public Writeln(Expression exp)
    {
        this.exp = exp;
    }
    /**
     * Prints out the result of the given expression
     */
    @Override
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }
    @Override
    public void compile(Emitter e)
    {
    	/*
    	 * move $a0 $v0
	     * li $v0 1
		 * syscall
    	 */
    	exp.compile(e);
    	e.emit("move $a0 $v0");
    	e.emit("li $v0 1");
    	e.emit("syscall");
    	e.emit("la $a0 nextline");
    	e.emit("li $v0 4");
    	e.emit("syscall");
    }
} 