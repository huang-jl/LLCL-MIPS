尝试性的可以将自己写的 C 和汇编编译成内存初始化文件的框架。

### 测试组织方式

tests 下的每个子目录对应一个测试。每个测试下可以写任意多 .S 或 .c，最后会被编译到一起。暂时按照官方的功能测试的规定，程序运行到 PC = 0xbfc00100 时结束（可在 .org 0x100 处死循环）。

### 编译

用 make 编译，会为每个测试在其子目录下的 obj 子目录生成二进制文件、inst_ram.coe 等内存初始化文件和可用来检查编译正确性的 test.s。

举例：现在 tests/or 是一个测试，目录下有 or.S。用 make 编译，会得到 tests/or/or.O（or.O 名字来自 or.S 而非目录名），并在 tests/or/obj 下生成 code.elf、inst_ram.coe、test.s 等等。

### 运行

尚待自动化。

在服务器上 vivado 中分别打开 func_test 下 cpu132_gettrace 和 myCPU 的 .xpr。展开左上角 sources 方格中 CPU 模块，找到 inst_ram 或 axi_ram 的 IP 并双击，在弹出窗口的 Other Options 页修改 coe file 到编译出来的 inst_ram.coe 文件（*）。注意确认后下方 console 是否报错，如果有错会修改失败。之后 Run behavioral simulation 即可。其中 cpu132_gettrace 负责生成正确的 golden_trace，myCPU 会与其比对，所以应该先在 cpu132 上跑。

（*）：暂时没有考虑怎么把 data ram 也加入。粗看官方的功能测试直接没管 data ram，把 inst_ram.coe 复制成 axi_ram.coe。
