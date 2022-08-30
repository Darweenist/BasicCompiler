package ast;

import java.util.*;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the actions taken when a procedure is called
 * 
 * @version 10/18/19
 * @author Dawson Chen
 */
public class ProcedureCall extends Expression 
{
    private String name;
    private List<Expression> exps;
//    private List<String> formalParams;
    /**
     * Instantiates a new Procedure call referenced by its name and parameters 
     * represented by a list of expressions
     * @param n the name of the procedure
     */
    public ProcedureCall(String n, List<Expression> exps) 
    {
        name = n;
        this.exps = exps;
//        formalParams = new ArrayList<String>();
    }
    @Override
    public int eval(Environment env) 
    {
    	// Set up child env
    	ProcedureDeclaration procDec = env.getProcedure(name);
    	Environment childEnv = new Environment(env);
    	List<String> formalParams = procDec.getParams();
        for(int i = 0; i < exps.size(); i++)
        {
        	int val = exps.get(i).eval(env);
            childEnv.declareVariable(formalParams.get(i), val);
        }
        env.getProcedure(name).getStatement().exec(childEnv);
        return 0;
    }
    /**
     * Compiles the procedure call into MIPS code and emits it to a given file
     * @param e the Emitter to use to write the MIPS code to a file
     */
    @Override
    public void compile(Emitter e)
    {
    	//    	e.emitPush("$ra"); //return address
    	for(Expression exp : exps)
    	{
    		exp.compile(e);
    		e.emitPush("$v0");
    	}
    	e.emit("jal proc" + name);
    	//e.emit("li $v0 0");
    	for(Expression exp : exps)
    	{
    		e.emitPop("$a1");
    	}
    	//e.emitPop("$ra");
    }
}