def f5(a, b, c):
    pass

arg = [1, 2, 3]
kwarg = {'c':3}
f5(*arg, **kwarg) # ok
f5(1,2, **kwarg) # ok
f5<warning descr="Unexpected argument(s)">(1, 2, 3, <warning descr="Unexpected argument">**kwarg</warning>)</warning> # fail
f5<warning descr="Unexpected argument(s)">(1, 2, 3, <warning descr="Unexpected argument">*arg</warning>)</warning> # fail
