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
public class ArrayTypeDenoterDDot extends TypeDenoter {
    public IntegerLiteral IL1;
    public IntegerLiteral IL2;
    public TypeDenoter T;

  public ArrayTypeDenoterDDot(IntegerLiteral ilAST, IntegerLiteral ilAST2, TypeDenoter tAST,
                    SourcePosition thePosition) {
    super (thePosition);
    IL1 = ilAST;
    IL2 = ilAST2;
    T = tAST;
  }
    
    @Override
    public boolean equals(Object obj) { //Compara elementos
      if (obj != null && obj instanceof ErrorTypeDenoter)
          return true;
      else if (obj != null && obj instanceof ArrayTypeDenoterDDot)
      return this.IL1.spelling.compareTo(((ArrayTypeDenoterDDot) obj).IL1.spelling) == 0 &&
             this.IL2.spelling.compareTo(((ArrayTypeDenoterDDot) obj).IL2.spelling) == 0 &&
             this.T.equals(((ArrayTypeDenoterDDot) obj).T);
      else
          return false;
    }

    @Override
  public Object visit(Visitor v, Object o) {
    return v.visitArrayTypeDenoterDDot(this, o);
  }
    
    
}
