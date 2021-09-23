from pyeda.inter import *

if __name__ == '__main__':
    X = ttvars('x', 6)
    f = truthtable(X, '10010000111111110---------------11-111--00-0--------------------')
    print(espresso_tts(f))
    f = truthtable(X, '-00-0000--------1-----------------------00-0--------------------')
    print(espresso_tts(f))
