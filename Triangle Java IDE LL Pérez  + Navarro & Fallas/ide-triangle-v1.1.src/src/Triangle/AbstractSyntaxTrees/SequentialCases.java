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
public class SequentialCases extends Command{
    CaseCommand commandC,commandCNext;

    public SequentialCases(CaseCommand commandCase,CaseCommand commandCaseNext,SourcePosition thePosition) {
        super(thePosition);
        commandC = commandCase;
        commandCNext = commandCaseNext;
    }

    @Override
    public Object visit(Visitor v, Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
