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
public class CaseElseCommand extends Case{

    Command commandCaseElse;
            
    public CaseElseCommand(Command commandElse,SourcePosition thePosition) {
        super(thePosition);
        commandCaseElse = commandElse;
    }

    public Object visit(Visitor v, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
