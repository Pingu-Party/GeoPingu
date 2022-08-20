package de.pinguparty.geopingu.worker.domain.formula;

import de.pinguparty.geopingu.worker.domain.formula.functions.Function;
import de.pinguparty.geopingu.worker.domain.formula.literals.Literal;
import de.pinguparty.geopingu.worker.domain.formula.utils.Utils;
import de.pinguparty.geopingu.worker.domain.formula.variables.Variable;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.*;

public class Formula {

    private static final String FUNCTIONS_PACKAGE = "de.pinguparty.geopingu.worker.domain.formula.functions";
    private static final Map<String, Function> FUNCTIONS_MAP = new HashMap<>();

    private static final String OPENING_BRACKET = "(";
    private static final String CLOSING_BRACKET = ")";
    private static final String DECIMAL_SEPARATOR = ".";
    private static final String ARGUMENT_SEPARATOR = ",";
    private static final String STRING_VALUE = "\"";
    private static final String IDENTIFIERS_PATTERN = "[A-z][A-z0-9]*";
    private static final String STRING_VALUE_PATTERN = "\"[a-zA-Z]+\"";
    private static final String NUMBER_PATTERN = "[0-9]+.?[0-9]*";

    static {
        Reflections reflections = new Reflections(FUNCTIONS_PACKAGE);
        reflections.getSubTypesOf(Function.class).stream()
                .filter(f -> !f.isInterface())
                .filter(f -> !Modifier.isAbstract(f.getModifiers()))
                .map(Utils::instantiateClass)
                .filter(Objects::nonNull)
                .forEach(f -> FUNCTIONS_MAP.put(f.getName(), f));
    }

    private String formula;
    private Stack<FormulaElement> reversePolishNotation = new Stack<>();
    private Set<Variable> variables = new HashSet<>();

    public Formula(String formula) throws IllegalFormulaSyntaxException {
        setFormula(formula);
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) throws IllegalFormulaSyntaxException {
        //Sanity check
        if ((formula == null) || formula.isEmpty())
            throw new IllegalFormulaSyntaxException("The formula must not be null or empty.");

        //Check whether the formula syntax is valid
        checkFormulaSyntax(formula);

        //Set formula
        this.formula = formula;

        //Parse to formula to extract variables
        parse();
    }


    public List<Variable> getVariables() {
        //TODO
        return List.of();
    }

    public Variable getVariable(String name) {
        //TODO
        return null;
    }

    public void setVariable(String name, Literal value) {
        //TODO
    }

    public boolean allVariablesSet() {
        //TODO
        return false;
    }

    public String evaluate() {
        if (!allVariablesSet())
            throw new IllegalStateException("The formula cannot be evaluated, because some variable values are still missing.");
        //TODO
        return "";
    }

    private void checkFormulaSyntax(String formula) throws IllegalFormulaSyntaxException {
        //TODO
    }

    private void parse() throws IllegalFormulaSyntaxException {

        //TODO
        System.out.println("parsi parse");
        if ((1 + 2) == 3) {
            return;
        }

        /*
        Preparations
         */
        reversePolishNotation.clear();

        //Pre-process the formula
        String preprocessedFormula = preprocessFormula(formula);
        //Tokenize the formula
        List<String> tokens = tokenizeFormula(preprocessedFormula);

        /*
        Parsing the formula by iterating over the previously extracted tokens
         */

        Stack<Function> functionStack = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            //Get the current token
            String token = tokens.get(i);

            //Check for argument separator
            if (token.equals(ARGUMENT_SEPARATOR)) {
                while (!functionStack.isEmpty()) {
                    reversePolishNotation.push(functionStack.pop());
                }
                continue;
            }
            //Check for function
            if (isFunction(token)) {
                functionStack.push(FUNCTIONS_MAP.get(token));
                continue;
            }

            //Check for variable
            if (token.matches(IDENTIFIERS_PATTERN)) {
                variables.add(new Variable(token));
                continue;
            }

            //Check for value ("Test" or 3.2232)
            if (token.matches(STRING_VALUE_PATTERN) || token.matches(NUMBER_PATTERN)) {
                token = token.replaceAll("\"", "");
                reversePolishNotation.push(new Literal(token));
                //Now create a new literal and give it a String or Double depending on the value
                continue;
            }

            //Check for operator
            if (FUNCTIONS_MAP.containsKey(token)) {
                while (!functionStack.isEmpty() && !functionStack.peek().getName().equals(OPENING_BRACKET) &&
                        (functionStack.peek().getPrecedence() > FUNCTIONS_MAP.get(token).getPrecedence() || (
                                functionStack.peek().getPrecedence() == FUNCTIONS_MAP.get(token).getPrecedence() && FUNCTIONS_MAP.get(token).isRightAssociative()
                        ))
                ) {
                    reversePolishNotation.push(functionStack.pop());
                }
                functionStack.push(FUNCTIONS_MAP.get(token));
                continue;
            }

            //Check for left brackets
            if (token.equals(OPENING_BRACKET)) {
                functionStack.push(FUNCTIONS_MAP.get(token));
                continue;
            }

            //check for right brackets
            if (token.equals(CLOSING_BRACKET)) {
                while (!functionStack.isEmpty() && !functionStack.peek().getName().equals(OPENING_BRACKET)) {
                    reversePolishNotation.push(functionStack.pop());
                }
                if (!functionStack.peek().getName().equals(OPENING_BRACKET)) {
                    throw new IllegalFormulaSyntaxException("No opening brackets found!");
                }
                functionStack.pop();
                if (!functionStack.isEmpty() && isFunction(functionStack.peek().getName())) {
                    reversePolishNotation.push(functionStack.pop());
                }
                continue;
            }

            //Token wasn't a function, operator, variable or value
            throw new IllegalFormulaSyntaxException("No match found!");
        }

        while (!functionStack.isEmpty()) {
            reversePolishNotation.push(functionStack.pop());
        }

        System.out.println(reversePolishNotation);
        //System.out.println(reversePolishNotation.stream().map(FormulaElement::toString).collect(Collectors.joining(", ")));
    }

    private String preprocessFormula(String formula) {
        return formula.trim().replaceAll("\\s", "");
    }

    private List<String> tokenizeFormula(String formula) throws IllegalFormulaSyntaxException {
        List<String> tokens = new LinkedList<>();
        StringBuilder currentToken = new StringBuilder();
        boolean isCurrentTokenNumeric = false;

        //Iterate over the formula character for character
        for (int i = 0; i < formula.length(); i++) {
            //Get current character
            char currentCharacter = formula.charAt(i);

            //Check whether token is quotation marks at the beginning of a String
            if (Character.toString(currentCharacter).equals(STRING_VALUE)) {
                if (currentToken.length() != 0 && currentToken.toString().matches("\"[a-zA-Z]+")) {
                    currentToken.append(currentCharacter);
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                } else if (currentToken.length() == 0) {
                    currentToken.append(currentCharacter);
                } else {
                    tokens.add(currentToken.toString());
                    currentToken.setLength(0);
                    currentToken.append(currentCharacter);
                }
                continue;
            }

            //Check whether token is decimal separator
            if (Character.toString(currentCharacter).equals(DECIMAL_SEPARATOR)) {
                if (!isCurrentTokenNumeric) {
                    throw new IllegalFormulaSyntaxException(String.format("The '%s' character is only allowed within numbers.", DECIMAL_SEPARATOR));
                }
                currentToken.append(currentCharacter);
                continue;
            }

            //If the character is alphanumeric or numeric, it may be part of a larger token
            if (Character.isDigit(currentCharacter)) {
                if (currentToken.length() == 0) isCurrentTokenNumeric = true;
                currentToken.append(currentCharacter);
                continue;
            } else if (Character.isAlphabetic(currentCharacter)) {
                if ((currentToken.length() != 0) && isCurrentTokenNumeric)
                    throw new IllegalFormulaSyntaxException("Numbers must not contain letters.");
                currentToken.append(currentCharacter);
                isCurrentTokenNumeric = false;
                continue;
            }

            //Character is special character, so it is a separate token and finishes the previous token (if existing)
            if (currentToken.length() != 0) tokens.add(currentToken.toString());
            tokens.add(Character.toString(currentCharacter));
            isCurrentTokenNumeric = false;
            currentToken.setLength(0);
        }

        //Check whether the current token still contains characters
        if (currentToken.length() != 0) tokens.add(currentToken.toString());

        return tokens;
    }

    private boolean isFunction(String token) {
        return token.matches(IDENTIFIERS_PATTERN) && FUNCTIONS_MAP.containsKey(token);
    }

    private boolean isSpecialCharacter(char c) {
        return !(Character.isAlphabetic(c) || Character.isDigit(c));
    }
}
