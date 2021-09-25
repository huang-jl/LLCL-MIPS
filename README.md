# 第五届龙芯杯参赛作品

该项目包含清华大学琉璃晨露队在第五届“龙芯杯”全国大学生系统能力培养大赛的参赛作品

琉璃晨露队伍成员分别是黄嘉良、杨倚天、余泰来、刘松铭

## 简介

最终我们实现了一个顺序双发射八级流水线的处理器LLCL-MIPS，主频在龙芯提供的开发板上（fpga芯片是xilinx的xc7a200tfbg676-2）可以达到120MHz，支持MIPS32 R1的绝大多数指令，能够运行比赛时最新的稳定版Linux内核v5.13，在Linux下能够驱动VGA、PS/2、串口、Flash等外设。

LLCL-MIPS没有采用经典的硬件描述语言如Verilog（或者SystemVerilog），而是基于SpinalHDL进行开发，由于项目初期组内对SpinalHDL并不熟悉，因开发周期短的因素有很大程度遗留的冗余代码，后期也无力重构了。基于我使用SpinalHDL的经验，在下面会对使用SpinalHDL开发流水线结构的处理器进行一些建议。

CPU的流水段划分如下：

- 取指/指令队列：第1-3级
- 译码/发射：第4级
- 执行：第5级
- 访存：第6-7级
- 写回：第8级

我们CPU的主要特点如下：

- 两级分支预测：包括PHT和BHT，以及BTB
- 可配置多路组L1缓存：分别有指令Cache和数据Cache，最终均使用的是2路组相连、单路4KiB的缓存
- 指令队列，支持最多两条指令入队和出队，能够保证分支指令和延迟槽一起发出

## 代码文件说明

CPU的代码都在`src/main/scala`下，代码文件的一些名字采用缩写的命名，这里列出可能不清楚的文件的含义

| 文件名    | 说明                                                         |
| --------- | ------------------------------------------------------------ |
| DCU/ICU   | DCache/ICache Unit，CPU和Cache交互时进行转换的一个辅助单元   |
| DU        | Decoder Unit，译码单元                                       |
| EIU       | Exception Interrupt Unit，历史遗留的冗余模块，到最后只有数据包和常量定义 |
| FetchInst | 指令队列的实现                                               |
| HLU       | HiLo Unit                                                    |
| JU        | Jump Unit执行阶段判断分支的模块                              |
| MU        | Mem Unit，历史遗留的冗余模块，到最后只定义了访存是符号扩展还是零扩展的一个变量 |

我们的CPU控制是在MultiIssueCPU.scala里了，不得不承认代码有点混乱。可能给大家的帮助更多是对一些小模块的实现，整体的逻辑要理清楚还是比较复杂的。

*实现的比较清楚的例如是CP0模块，对CP0寄存器进行了一定程度的抽象*

---
最后的性能CPU(main)和运行OS(cpu-os)的CPU在两个不同的分支上

我们还提供了一个7级流水的单发射的CPU(simple)作为参考，它的实现更加清晰，也包含了分支预测、缓存等基本模块


## 建议

### 对于SpinalHDL

- SpinalHDL的最基本特点是给硬件表达增加了更强的语法和数据结构的支持，所以可以尝试大胆的使用函数调用、`Array | Map`这样的数据结构
- SpinalHDL有很多package并没有在官方文档上提到，需要多在IDE中点来点去，自行探索（
- SpinalHDL对于二维的信号支持很差，可以说无法实现像SystemVerilog下的二维logic数组，解决方案是使用`Vec`，但他们在硬件上应该是等价的，只是把二维数组拆成多个一维的信号了
- **强烈建议**使用`spinal.lib.Stream`以及`spinal.lib.Flow`在你的流水线结构中，SpinalHDL实现了关于这两个类非常好用的方法以及一些相关的类，能够帮助你快速构建流水线

### 其他

- 在写Cache的时候，可以使用Xilinx Parameterized Macro(XPM)声明BRAM或者LUTRAM，在Spinal下可以使用BlackBox套上

- 有一些不在初赛要求的，但在运行Linux必要的指令，它的限制比较多，例如修改TLB、刷新Cache的指令，我们的做法是
  - 限制一次最多发射一条这样的指令
  - 在它发射后暂停整个流水线直到它的执行结束

## 致谢

除了队员的努力之外，我们还要特别感谢往届的学长，包括高一川、陈晟祺、陈嘉杰、王邈

项目参考了往届参赛的队伍作品，包括[nontrivial-MIPS](https://github.com/trivialmips)以及[NaiveMIPS](https://github.com/z4yx/NaiveMIPS-HDL)

