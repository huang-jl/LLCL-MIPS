- 分支预测

  - `IF2`：分支？目标？
    - `BTB` 查到说明要跳转，知道分支也知道目标
    - `BTB` 没查到则没法跳，因此不跳，不知道分支也不知道目标
  - `ID`：目标？
    - 
  - `EX`：分支？

  

  - `BTB` 只保存分支目标确定的跳转指令

  

  - 知道目标，知道要分支
    - `BTB` 中没有自己，则写入 `BTB`、`LRU`
    - `BTB` 中有自己，则写入 `LRU`
  - 知道目标，知道不要分支
    - `BTB` 中没有自己，则不管
    - `BTB` 中有自己，则写入 `BTB`、`LRU`
  - 知道目标，不知道要不要分支
    - 没法写入 `BTB`，只能不管
  - 不知道目标
    - 没法写入 `BTB`，只能不管

  

  - 由于要尽快跳转，因此读取 `BTB` 是在 `IF` 阶段
  - 由于要知道目标，知道是否分支才能写入 `BTB`，因此写入 `BTB` 是在 `EX` 阶段
    - （也可能 `ID` 阶段就已经知道了，那么要提前写入吗？）（先不了，允许多个阶段写入可能发生写入的冲突，还会造成需要多个阶段一起进行广播，太复杂，先不考虑）

  

  - 虽然是否写入在 `EX` 阶段才知道，但是如果要写入，则写入的数据是什么在 `ID` 阶段就能够知道

  

  - `IF` 阶段读取的值不一定准确，也没法准确
  - `EX` 阶段写入的值可以是准确的，通过前向广播写入的数据达到目的，归纳可以证明写入的数据都是准确的

  

  - 要读写的 `BTB` 与 `LRU` 由 `index` 决定，如果广播的 `index` 和自身相同，则收下广播的数据

  - `IF` 读取 `BTB` 时是否要采取写优先策略？
    - 好像可以采取
    
    
    
  - `BHT` 和 `PHT`。一条跳转指令，其 `PHT` 可能只有某一些项是有用的，其它的都不会用到，因此可以考虑多条跳转指令共用一个 `PHT`

    - 用哈希值寻址 `BHT`，用地址低位寻址 `PHT`
    - 写 `BHT`：写一项
    - 读 `BHT`：读一项
    - 写 `PHT`：写一项
    - 读 `PHT`：读一项

    

    - 一条指令是**目标确定的跳转指令**
      - 要修改 `LRU`
      - 如果 `BTB` 命中则不修改 `BTB`，未命中要修改 `BTB`
      - 根据跳转情况修改 `BHT`、`PHT`

    

    - `BHT` 和 `PHT` 跳转，`BTB` 命中



- 为了避免前传问题，能尽量早写入的就尽量早写入
- 由于中断异常是在 `MEM` 阶段判断的，因此写入最早不能超过 `MEM` 阶段




- `CP0`

  - 对 `CP0` 写入不需要 `MEM` 的结果，因此 `CP0` 的写入放在 `MEM` 阶段
  - 
  
  
  
  - 写：`MEM` 阶段
  - 

- 数据流

  - `CP0 -> TLB`

  - `TLB -> CP0`

    

  - `CP0 -> reg`

  - `reg -> CP0`

    

  - `reg -> stage.id`

  - 由 `CP0 -> reg` 则 `CP0 -> stage.exe`
  
    
  
  - `reg -> stage.exe`
  
  - `reg -> stage.mem.1`
