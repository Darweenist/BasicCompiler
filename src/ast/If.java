package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the If statement in our grammar
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class If extends Statement
{
    private Condition condition;
    private Statement stmt;
    /**
     * Instantiates a WriteLn statement with a given expression
     * @param cond is the condition to test before executing s
     * @param s is the statement to be executed if cond is true
     */
    public If(Condition cond, Statement s)
    {
        condition = cond;
        stmt = s;
    }
    /**
     * Checks whether the condition is true, then executes if it is.
     */
    @Override
    public void exec(Environment env)
    {
        if(condition.eval(env) == 1)
        {
            stmt.exec(env);
        }
    }
    /**
     * Write assembly MIPS code to a file using a given Emitter e
     * @param e the Emitter used to write the code to file
     */
    public void compile(Emitter e)
    {
    	int nextLabelID = e.nextLabelID();
    	condition.compile(e, "endif" + nextLabelID);
    	stmt.compile(e);    	
    	e.emit("endif" + nextLabelID + ":");
    }
} 