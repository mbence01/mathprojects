# exceptions
class VariableNameError(Exception):
    def __init__(self, var_name):
        super().__init__(f'Invalid variable name given. Name must be a one length character from ABC. Given name: {var_name} ({type(var_name)})')

class NegationParameterError(Exception):
    def __init__(self, neg):
        super().__init__(f'Negation parameter can only be True or False. Given value: {neg} ({type(neg)})')

class ClauseVariableListError(Exception):
    def __init__(self, vars, item = None, i = None):
        if item == None:
            super().__init__(f'Variable list parameter must be an instance of list. Given parameter: {vars} ({type(vars)})')
        else:
            super().__init__(f'Variable list parameter must contain only instances of Variable. Wrong input: list[{i}] = {item} ({type(item)})')

class InvalidParameterGivenError(Exception):
    def __init__(self, message):
        super().__init__('Invalid parameter given. ' + message)

# base classes
class Variable:
    def __init__(self, name, neg = False):
        if type(name) != str or len(name) != 1:
            raise VariableNameError(name)

        if type(neg) != bool:
            raise NegationParameterError(neg)

        self._name = name
        self._neg = neg

    @property
    def name(self):
        return self._name

    def __str__(self):
        return f"{'¬' if self._neg else ''}{self._name}"

    def __eq__(self, other):
        if other == None:
            return False
        return self._name == other._name and self._neg == other._neg

    def negof(self):
        return Variable(self.name, not self._neg)

    def isnegof(self, other):
        return self._name == other._name and self._neg != other._neg

class Clause:
    def __init__(self, vars):
        if not isinstance(vars, list):
            raise ClauseVariableListError(vars)

        for i, item in enumerate(vars):
            if not isinstance(item, Variable):
                raise ClauseVariableListError(vars, item, i)

        self._vars = []

        for new_item in vars:
            contain = False

            for self_item in self._vars:
                if self_item == new_item:
                    contain = True
                    break

            if not contain:
                self._vars.append(new_item)

    @property
    def vars(self):
        return self._vars

    def contains(self, var):
        for _var in self._vars:
            if _var == var:
                return True
        return False

    def remove(self, var):
        if not isinstance(var, Variable):
            raise InvalidParameterGivenError(f'Expected: Variable, Given: {type(var)}')

        self._vars.remove(var)
        return self

    def __str__(self):
        return '{ ' + ( ', '.join([ str(i) for i in self._vars ]) ) + ' }'


def simplify(clauseset, literal):
    if not isinstance(clauseset, list):
        raise InvalidParameterGivenError(f'Expected: list, Given: {type(clauseset)}')
    
    if not isinstance(literal, Variable):
        raise InvalidParameterGivenError(f'Expected: Variable, Given: {type(literal)}')

    for i, item in enumerate(clauseset):
        if not isinstance(item, Clause):
            raise InvalidParameterGivenError(f'ClauseList[{i}], expected: Clause, given: {type(item)}')

    tmp_set = clauseset.copy()
    ret_set = []

    for clause in tmp_set:
        #print(f'> Current clause: {clause}, contains {literal}: {clause.contains(literal)}, contains {literal.negof()}: {clause.contains(literal.negof())}')

        if clause.contains(literal):
            continue
        elif clause.contains(literal.negof()):
            new_clause = clause.remove(literal.negof())
            ret_set.append(new_clause)
        else:
            ret_set.append(clause)

    return ret_set

def solve(clauseset):
    print('\n\nΣ = { ' + (', '.join([str(i) for i in clauseset])) + ' }')

    # If sigma is empty
    if len(clauseset) == 0:
        print('> Σ set of clause is empty -> Σ IS SATISFIABLE -> RETURN TRUE')
        return True

    # If sigma contains an empty clause
    for clause in clauseset:
        if len(clause.vars) == 0:
            print('> Σ set of clause contains an empty clause -> Σ IS NOT SATISFIABLE -> RETURN FALSE')
            return False

    # If sigma has unit clause
    curr_var = None
    curr_char = 'z'

    for clause in clauseset:
        if len(clause.vars) == 1 and clause.vars[0].name < curr_char:
            curr_var = clause.vars[0]
            curr_char = clause.vars[0].name

    if curr_var != None and curr_char != 'z':
        print('Σ has unit clause')
        print(f'Selecting {curr_var} as a literal. Set {curr_var} to True')

        return solve( simplify(clauseset, curr_var) )

    # If sigma has pure literal
    checked_variables = []

    for clause in clauseset:
        for var in clause.vars:
            hasNegOf = False

            for check_clause in clauseset:
                if check_clause.contains(var.negof()):
                    hasNegOf = True
                    break

            if not hasNegOf:
                checked_variables.append(var)

    if len(checked_variables) > 0:
        curr_var = None
        cur_char = 'z'

        for var in checked_variables:
            if var.name < curr_char:
                curr_var = var
                curr_char = var.name

        print('Σ has pure literal')
        print(f'Selecting {curr_var} as literal. Set {curr_var} to True')

        return solve( simplify(clauseset, curr_var) )
    
    # Else
    curr_var = None
    curr_char = 'z'

    for clause in clauseset:
        for var in clause.vars:
            if var.name < curr_char:
                curr_var = var
                curr_char = var.name

    print(f'No unit or pure literal found in Σ. Set {curr_var} to True and {curr_var.negof()} to True')

    return solve( simplify(clauseset, curr_var) ) or solve( simplify(clauseset, curr_var.negof()) )


if __name__ == '__main__':
    v01 = Variable('p', True)
    v02 = Variable('q')
    v03 = Variable('p', True)
    v04 = Variable('s')
    v05 = Variable('p')
    v06 = Variable('r', True)
    v07 = Variable('s', True)
    v08 = Variable('p')
    v09 = Variable('s')
    v10 = Variable('q', True)
    v11 = Variable('s', True)
    v12 = Variable('q', True)
    v13 = Variable('r', True)

    variables = [ v01, v02, v03, v04, v05, v06, v07, v08, v09, v10, v11, v12, v13 ]

    for var in variables:
        print('One variable of the list:', var)

    print('----------------------------')

    c1 = Clause([ v01, v02 ])
    c2 = Clause([ v03, v04 ])
    c3 = Clause([ v05, v06, v07 ])
    c4 = Clause([ v08, v09 ])
    c5 = Clause([ v10, v11 ])
    c6 = Clause([ v12, v13 ])

    clauses = [ c1, c2, c3, c4, c5, c6 ]

    for clause in clauses:
        print('One clause of the list:', clause)

    print('----------------------------')

    solve(clauses)