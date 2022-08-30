package ast;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents an Expression in the tree
 * @author Dawson Chen
 * @version 10/1/2019
 */
public abstract class Expression
{
    /**
     * Evaluates according to the expression and the env
     * @param env the env provided to help execute the expression
     * @return the result of the evaluation
     */
    public abstract int eval(Environment env);
    /**
     * Emits a sequence of MIPS instructions corresponding to a certain Expression to a file
     * @param e an Emitter that will handle identifying writing the instructions
     */
    public void compile(Emitter e)
    {
    	throw new RuntimeException("Implement me!!!!!");
    } 
}