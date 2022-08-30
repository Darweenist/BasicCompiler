package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents a statement in the tree
 * @author Dawson Chen
 * @version 10/1/2019
 */
public abstract class Statement
{
    /**
     * Executes the statement according to the subclass's functions
     * @param env is the environment of variables used to execute the statement
     */
    public abstract void exec(Environment env);
    /**
     * Emits a sequence of MIPS instructions corresponding to each statement to a file
     * @param e an Emitter that will handle identifying writing the instructions
     */
    public void compile(Emitter e)
    {
    	throw new RuntimeException("Implement me!!!!!");
    } 
}