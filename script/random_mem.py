import random
import math

STRB_WIDTH = 2

def gen_args():
    addr_width = input('Please input the address width: ')
    data_width = input('Please input the data width: ')
    strb_width = input('Please input the strb width: ')
    addr_width = int(addr_width)
    data_width = int(data_width)
    strb_width = int(strb_width)
    return addr_width, data_width, strb_width

def gen_random_data(addr_width, data_width, strb_width):
    res = []
    high_bound = (2 ** data_width) - 1
    for i in range(2**(addr_width-1-math.ceil(math.log2(strb_width)))):
        res.append(random.randint(0, high_bound // 2))
        res.append(random.randint(high_bound//2, high_bound))
    return res

def dump(data, path='data.txt'):
    with open(path, 'w') as f:
        for item in data:
            f.write('{:0<8x}\n'.format(item))

if __name__ == '__main__':
    addr_width, data_width, strb_width = gen_args()
    data = gen_random_data(addr_width, data_width, strb_width)
    dump(data)