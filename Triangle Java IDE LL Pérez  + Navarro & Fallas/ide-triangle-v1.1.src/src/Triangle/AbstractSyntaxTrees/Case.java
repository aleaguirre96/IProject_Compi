/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Triangle.AbstractSyntaxTrees;

import Triangle.SyntacticAnalyzer.SourcePosition;

/**
 *
 * @author jose
 */
public abstract class Case extends Command {
    public Command ComandCase;
    
    public Case(Command c1AST,SourcePosition thePosition) {
        super(thePosition);
        ComandCase = c1AST;
    }
   
}
