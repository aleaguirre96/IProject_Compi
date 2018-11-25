/*
 * @(#)Visitor.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.AbstractSyntaxTrees;

public interface Visitor {
    
  //Case
  public abstract Object visitCase(CaseCommand ast, Object o);//Visitor para Case
  public abstract Object visitSequentialCases(SequentialCases ast,Object o);//Visitor sequential cases
  public abstract Object visitCaseElseCommand(CaseElseCommand ast,Object o);
  public abstract Object visitCases(CasesCommand ast,Object o);

  // Commands
  public abstract Object visitAssignCommand(AssignCommand ast, Object o);
  public abstract Object visitCallCommand(CallCommand ast, Object o);
  // eliminado metodo del EmptyCommand
  public abstract Object visitIfCommand(IfCommand ast, Object o);
  public abstract Object visitLetCommand(LetCommand ast, Object o);
  public abstract Object visitNILCommand(NILCommand ast, Object o); //Se agrega el NILcommand al visitor
  public abstract Object visitSelectCommand(SelectCommand ast, Object o); // Se agrega el SelectCommand
  public abstract Object visitSequentialCommand(SequentialCommand ast, Object o);
  public abstract Object visitWhileCommand(WhileCommand ast, Object o);
   //Repeats (parte de commands agregados por el equipo)
  public abstract Object visitRepeatWhile(RepeatWhile ast, Object o);/////// Se agrega el RepeatWhile al visitor
  public abstract Object visitRepeatUntil(RepeatUntil ast, Object o); /////// Se agrega el RepeatUntil al visitor
  public abstract Object visitRepeatDoWhile(RepeatDoWhile ast, Object o); /////// Se agrega el RepeatDoWhile al visitor
  public abstract Object visitRepeatDoUntil(RepeatDoUntil ast, Object o); /////// Se agrega el RepeatDoUntil al visitor
  public abstract Object visitRepeatFor(RepeatFor ast, Object o); /////// Se agrega el RepeatDoUntil al visitor
  // Expressions
  public abstract Object visitArrayExpression(ArrayExpression ast, Object o);
  public abstract Object visitBinaryExpression(BinaryExpression ast, Object o);
  public abstract Object visitCallExpression(CallExpression ast, Object o);
  public abstract Object visitCharacterExpression(CharacterExpression ast, Object o);
  public abstract Object visitEmptyExpression(EmptyExpression ast, Object o);
  public abstract Object visitIfExpression(IfExpression ast, Object o);
  public abstract Object visitIntegerExpression(IntegerExpression ast, Object o);
  public abstract Object visitLetExpression(LetExpression ast, Object o);
  public abstract Object visitRecordExpression(RecordExpression ast, Object o);
  public abstract Object visitUnaryExpression(UnaryExpression ast, Object o);
  public abstract Object visitVnameExpression(VnameExpression ast, Object o);
  public abstract Object visitSequentialExpression(SequentialExpression ast, Object o);//Se agrega el SequentielExpresion para la regla parseCaseLiterals()

  // Declarations
  public abstract Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o);
  public abstract Object visitConstDeclaration(ConstDeclaration ast, Object o);
  public abstract Object visitFuncDeclaration(FuncDeclaration ast, Object o);
  public abstract Object visitLocalDeclaration(LocalDeclaration ast, Object o); //Se agregar la declaracion de Local
  public abstract Object visitProcDeclaration(ProcDeclaration ast, Object o);
  public abstract Object visitRecursiveDeclaration(RecursiveDeclaration ast, Object o); //Se agrega la declaracion de recursividad
  public abstract Object visitSequentialDeclaration(SequentialDeclaration ast, Object o);
  public abstract Object visitTypeDeclaration(TypeDeclaration ast, Object o);
  public abstract Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o);
  public abstract Object visitVarDeclaration(VarDeclaration ast, Object o);
  public abstract Object visitVarDeclarationInitialized(VarDeclarationInitialized ast, Object o);//Metodo visitor para el caso "var" Identifier ":=" Expression

  // Array Aggregates
  public abstract Object visitMultipleArrayAggregate(MultipleArrayAggregate ast, Object o);
  public abstract Object visitSingleArrayAggregate(SingleArrayAggregate ast, Object o);

  // Record Aggregates
  public abstract Object visitMultipleRecordAggregate(MultipleRecordAggregate ast, Object o);
  public abstract Object visitSingleRecordAggregate(SingleRecordAggregate ast, Object o);

  // Formal Parameters
  public abstract Object visitConstFormalParameter(ConstFormalParameter ast, Object o);
  public abstract Object visitFuncFormalParameter(FuncFormalParameter ast, Object o);
  public abstract Object visitProcFormalParameter(ProcFormalParameter ast, Object o);
  public abstract Object visitVarFormalParameter(VarFormalParameter ast, Object o);

  public abstract Object visitEmptyFormalParameterSequence(EmptyFormalParameterSequence ast, Object o);
  public abstract Object visitMultipleFormalParameterSequence(MultipleFormalParameterSequence ast, Object o);
  public abstract Object visitSingleFormalParameterSequence(SingleFormalParameterSequence ast, Object o);

  // Actual Parameters
  public abstract Object visitConstActualParameter(ConstActualParameter ast, Object o);
  public abstract Object visitFuncActualParameter(FuncActualParameter ast, Object o);
  public abstract Object visitProcActualParameter(ProcActualParameter ast, Object o);
  public abstract Object visitVarActualParameter(VarActualParameter ast, Object o);

  public abstract Object visitEmptyActualParameterSequence(EmptyActualParameterSequence ast, Object o);
  public abstract Object visitMultipleActualParameterSequence(MultipleActualParameterSequence ast, Object o);
  public abstract Object visitSingleActualParameterSequence(SingleActualParameterSequence ast, Object o);

  // Type Denoters
  public abstract Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o);
  public abstract Object visitArrayTypeDenoter(ArrayTypeDenoter ast, Object o);
  //Metodo visitor para el caso "array" Integer-Literal [".." Integer-Literal] "of" Type-denoter
  public abstract Object visitArrayTypeDenoterDDot(ArrayTypeDenoterDDot ast, Object o);
  public abstract Object visitBoolTypeDenoter(BoolTypeDenoter ast, Object o);
  public abstract Object visitCharTypeDenoter(CharTypeDenoter ast, Object o);
  public abstract Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o);
  public abstract Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o);
  public abstract Object visitIntTypeDenoter(IntTypeDenoter ast, Object o);
  public abstract Object visitRecordTypeDenoter(RecordTypeDenoter ast, Object o);

  public abstract Object visitMultipleFieldTypeDenoter(MultipleFieldTypeDenoter ast, Object o);
  public abstract Object visitSingleFieldTypeDenoter(SingleFieldTypeDenoter ast, Object o);

  // Literals, Identifiers and Operators
  public abstract Object visitCharacterLiteral(CharacterLiteral ast, Object o);
  public abstract Object visitIdentifier(Identifier ast, Object o);
  public abstract Object visitIntegerLiteral(IntegerLiteral ast, Object o);
  public abstract Object visitOperator(Operator ast, Object o);

  // Value-or-variable names
  public abstract Object visitDotVname(DotVname ast, Object o);
  public abstract Object visitSimpleVname(SimpleVname ast, Object o);
  public abstract Object visitSubscriptVname(SubscriptVname ast, Object o);

  // Programs
  public abstract Object visitProgram(Program ast, Object o);
  
  
 


}
