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
public class SequentialCases extends Case{
    public Case commandC,commandCNext;

    public SequentialCases(Case commandCase,Case commandCaseNext,SourcePosition thePosition) {
        super(thePosition);
        commandC = commandCase;
        commandCNext = commandCaseNext;
    }

    public Object visit(Visitor v, Object o) {
        return v.visitSequentialCases(this, o);
    }
    
}
