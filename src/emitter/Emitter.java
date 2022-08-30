package emitter;

import java.io.*;

import ast.ProcedureDeclaration;

public class Emitter
{
    private PrintWriter out;
    private int counter;
    private ProcedureDeclaration currProcDec;
    private int excessStackHeight;
 
    /**
     * Creates an emitter for writing to a new file with given name
     * @param outputFileName the file to be written to
     */
    public Emitter(String outputFileName)
    {
        try
        {
            counter = 1;
            excessStackHeight = 0;
            currProcDec = null;
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setProcedureContext(ProcedureDeclaration proc)
    {
        excessStackHeight = 0;
        currProcDec = proc;
    }

    public void clearProcedureContext()
    {
        currProcDec = null;
    }

    public boolean isLocalVariable(String varName)
    {
        if (currProcDec != null && (currProcDec.getParams().contains(varName) || varName.equals(currProcDec.getName())))
            return true;
        else
            return false;
    }

    /**
     * Gets the offset from the top of the stack according to the index of the parameter
     * @Precondition: localVarName is the name of a local
     * @param localVarName
     * @return the offset from the head of the stack
     */
    public int getOffset(String localVarName)
    {
        //         int index = currProcDec.getParams().indexOf(localVarName);
        //         index = currProcDec.getParams().size() - 1 - index;
        //         return index *  4 + excessStackHeight;
		if (localVarName.equals(currProcDec.getName()))
		{
			//System.out.println(localVarName);
			// adding 1 for the ra
			//int retval = (currProcDec.getParams().size()+2)*4 + excessStackHeight;
			
			int retval = excessStackHeight;
			System.out.println("Found retval at " + retval);
			return retval;
		}
		
		int index = currProcDec.getParams().indexOf(localVarName);
		index = currProcDec.getParams().size()  - index;
		//return index * 4 + excessStackHeight;

        int off = (index*4)+excessStackHeight;
        System.out.println("Found " + localVarName + " at " + off);
        return off;
    }

    /**
     * Prints one line of code to file (with non-labels indented)
     * @param code to print to file
     */
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

    /**
     * Pushes the a given register onto a stack storing all currently used operands
     * @param reg the register to push
     */
    public void emitPush(String reg)
    { 
        excessStackHeight += 4;
        emit("subu $sp $sp 4");
        emit("sw " + reg + " ($sp)");
        System.out.println("Pushed " + reg);
    }

    /**
     * Pops the a given register off a stack storing all currently used operands
     * @param reg the register to pop into
     */
    public void emitPop(String reg)
    {
        excessStackHeight -= 4;
        emit("lw " + reg + " ($sp)");
        emit("addu $sp $sp 4");
        System.out.println("Popped " + reg);
    }

    /**
     * Closes the file.  should be called after all calls to emit.
     */
    public void close()
    {
        out.close();
    }

    /**
     * Return 1 the first time it's called, 2 the next time, then 3, etc. 
     * @return
     */
    public int nextLabelID() 
    {
        counter++;
        return counter - 1;
    }
}