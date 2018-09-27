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

    public Command commandCaseElse;
            
    public CaseElseCommand(Command commandElse,SourcePosition thePosition) {
        super(thePosition);
        commandCaseElse = commandElse;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitCaseElseCommand(this,o);
    }
    
}
