package ast;

import java.util.*;

import ast.environment.Environment;
import emitter.Emitter;

/**
 * Represents the declaration of a procedure at the beginning of a program.
 * 
 * @author Dawson Chen
 * @version 10/18/19
 */
public class ProcedureDeclaration extends Statement 
{
    private Statement body;
    private String name;
    private List<String> params;
    /**
     * Creates an instance of ProcedureDeclaration with a given name and body to be
     * executed.
     * 
     * @param body is the block of code to be executed when this procedure is called
     * @param name is the name of the Procedure to be stored in the global
     *             environment
     */
    public ProcedureDeclaration(Statement body, String name, List<String> p) 
    {
        this.body = body;
        this.name = name;
        params = p;
    }

    @Override
    public void exec(Environment env) 
    {
        env.setProcedure(name, this);
    }
    /**
     * Gets the name of this Procedure
     * @return name of this Procedure object
     */
    public String getName()
    {
        return name;
    }
    /**
     * Gets the statement body of this Procedure
     * @return the statement to be executed in the body
     */
    public Statement getStatement()
    {
        return body;
    }
    /**
     * Gets the params of this Procedure
     * @return the params to be used in the body
     */
    public List<String> getParams()
    {
        return params;
    }
    /**
     * Emits the procDec into a file using
     */
    public void compile(Emitter e)
    {
        e.setProcedureContext(this);
        e.emit("proc" + name + ":");
        e.emitPush("$ra");
        e.emit("li $t1 0");
        e.emitPush("$t1"); //return value
        
        body.compile(e);
        
        e.emitPop("$v0"); //pop the return value
        e.emitPop("$ra");
        e.emit("jr $ra");
        e.clearProcedureContext();
    }
}