#include <asm.h>
#include <regdef.h>
#include <cpu_cde.h>

    .set	noreorder
	.globl	_start
	.globl	start
	.globl	__main
_start:
start:
    li  t0, 1
    li  t1, 2
    and t2, t0, t1
    b   finish

.org 0x100
finish:
    b   finish
