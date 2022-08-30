package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the While statement and loop in our grammar
 * 
 * @author Dawson Chen
 * @version 10/1/19
 */
public class While extends Statement
{
    private Condition condition;
    private Statement stmt;
    /**
     * Instantiates a WriteLn statement with a given expression
     * @param cond is the condition to test before executing s
     * @param s is the statement to be executed if cond is true
     */
    public While(Condition cond, Statement s)
    {
        condition = cond;
        stmt = s;
    }
    /**
     * While the condition is true, executes the statemnt that follows
     */
    @Override
    public void exec(Environment env)
    {
        while(condition.eval(env) == 1)
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
    	e.emit("while" + nextLabelID + ":");
    	condition.compile(e, "endwhile" + nextLabelID);
    	stmt.compile(e);    	
    	e.emit("j while" + nextLabelID);
    	e.emit("endwhile" + nextLabelID + ":");
    }
} 