package ast;

import java.util.*;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents an Block in the grammar
 * 
 * @author Dawson Chen
 * @version 10/1/2019
 */
public class Block extends Statement 
{
    private List<Statement> stmts;

    /**
     * Creates a new instance of Block with a specified list of statements
     * 
     * @param st the initial list of statements in the block
     */
    public Block(List<Statement> st) 
    {
        stmts = st;
    }

    @Override
    public void compile(Emitter e)
    {
    	for(Statement s : stmts)
    	{
    		s.compile(e);
    	}
    }
    
    /**
     * Executes the list of statements one by one
     */
    @Override
    public void exec(Environment env) 
    {
        for(Statement s : stmts)
        {
            s.exec(env);
        }
    }
}