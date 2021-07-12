
obj/coremark/main.elf:     file format elf32-tradlittlemips
obj/coremark/main.elf


Disassembly of section .text:

9fc00000 <_ftext>:
_ftext():
9fc00000:	3c1bbfb0 	lui	k1,0xbfb0
9fc00004:	af608ffc 	sw	zero,-28676(k1)
9fc00008:	af608ffc 	sw	zero,-28676(k1)
9fc0000c:	af60fff8 	sw	zero,-8(k1)
9fc00010:	af608ffc 	sw	zero,-28676(k1)
9fc00014:	af608ffc 	sw	zero,-28676(k1)
9fc00018:	8f608ffc 	lw	zero,-28676(k1)
9fc0001c:	8f7bfff8 	lw	k1,-8(k1)
9fc00020:	3c1bbfb0 	lui	k1,0xbfb0
9fc00024:	af608ffc 	sw	zero,-28676(k1)
9fc00028:	af608ffc 	sw	zero,-28676(k1)
9fc0002c:	af60fffc 	sw	zero,-4(k1)
9fc00030:	af608ffc 	sw	zero,-28676(k1)
9fc00034:	af608ffc 	sw	zero,-28676(k1)
9fc00038:	8f608ffc 	lw	zero,-28676(k1)
9fc0003c:	8f7bfffc 	lw	k1,-4(k1)
9fc00040:	3c0a0040 	lui	t2,0x40
9fc00044:	408a6000 	mtc0	t2,c0_sr
9fc00048:	00000000 	nop
9fc0004c:	40806800 	mtc0	zero,c0_cause
9fc00050:	3c1d9fc2 	lui	sp,0x9fc2
9fc00054:	27bdac5c 	addiu	sp,sp,-21412
9fc00058:	3c1c9fc1 	lui	gp,0x9fc1
9fc0005c:	279c2c90 	addiu	gp,gp,11408
9fc00060:	3c199fc0 	lui	t9,0x9fc0
9fc00064:	2739038c 	addiu	t9,t9,908
9fc00068:	03200008 	jr	t9
9fc0006c:	00000000 	nop
	...

9fc00100 <test_finish>:
test_finish():
9fc00100:	25080001 	addiu	t0,t0,1
9fc00104:	240900ff 	li	t1,255
9fc00108:	3c0abfaf 	lui	t2,0xbfaf
9fc0010c:	354afff0 	ori	t2,t2,0xfff0
9fc00110:	a1490000 	sb	t1,0(t2)
9fc00114:	3c08bfaf 	lui	t0,0xbfaf
9fc00118:	3508f020 	ori	t0,t0,0xf020
9fc0011c:	8d080000 	lw	t0,0(t0)
9fc00120:	00000000 	nop
9fc00124:	31080080 	andi	t0,t0,0x80
9fc00128:	11000008 	beqz	t0,9fc0014c <test_finish+0x4c>
9fc0012c:	00000000 	nop
9fc00130:	00000000 	nop
9fc00134:	3c08bfaf 	lui	t0,0xbfaf
9fc00138:	35088000 	ori	t0,t0,0x8000
9fc0013c:	8d090000 	lw	t1,0(t0)
9fc00140:	10000005 	b	9fc00158 <test_finish+0x58>
9fc00144:	00000000 	nop
9fc00148:	00000000 	nop
9fc0014c:	3c08bfaf 	lui	t0,0xbfaf
9fc00150:	35088004 	ori	t0,t0,0x8004
9fc00154:	8d090000 	lw	t1,0(t0)
9fc00158:	3c08bfaf 	lui	t0,0xbfaf
9fc0015c:	3508f010 	ori	t0,t0,0xf010
9fc00160:	1000ffec 	b	9fc00114 <test_finish+0x14>
9fc00164:	ad090000 	sw	t1,0(t0)
9fc00168:	00000000 	nop
9fc0016c:	3c088000 	lui	t0,0x8000
9fc00170:	25290001 	addiu	t1,t1,1
9fc00174:	01005025 	move	t2,t0
9fc00178:	01ae5821 	addu	t3,t5,t6
9fc0017c:	8d0c0000 	lw	t4,0(t0)
	...
9fc00380:	1000ffff 	b	9fc00380 <test_finish+0x280>
9fc00384:	25080001 	addiu	t0,t0,1
9fc00388:	00000000 	nop

9fc0038c <run_test>:
run_test():
9fc0038c:	0411000c 	bal	9fc003c0 <shell3>
9fc00390:	00000000 	nop
9fc00394:	00000000 	nop

9fc00398 <go_finish>:
go_finish():
9fc00398:	3c099fc0 	lui	t1,0x9fc0
9fc0039c:	25290100 	addiu	t1,t1,256
9fc003a0:	3c0a2000 	lui	t2,0x2000
9fc003a4:	012ac825 	or	t9,t1,t2
9fc003a8:	03200008 	jr	t9
9fc003ac:	00000000 	nop
	...

9fc003c0 <shell3>:
shell3():
9fc003c0:	27bdffa8 	addiu	sp,sp,-88
9fc003c4:	afb10034 	sw	s1,52(sp)
9fc003c8:	3c11bfaf 	lui	s1,0xbfaf
9fc003cc:	3623e000 	ori	v1,s1,0xe000
9fc003d0:	afbf0054 	sw	ra,84(sp)
9fc003d4:	afbe0050 	sw	s8,80(sp)
9fc003d8:	afb7004c 	sw	s7,76(sp)
9fc003dc:	afb60048 	sw	s6,72(sp)
9fc003e0:	afb50044 	sw	s5,68(sp)
9fc003e4:	afb40040 	sw	s4,64(sp)
9fc003e8:	afb3003c 	sw	s3,60(sp)
9fc003ec:	afb20038 	sw	s2,56(sp)
9fc003f0:	afb00030 	sw	s0,48(sp)
9fc003f4:	ac600000 	sw	zero,0(v1)
9fc003f8:	40804800 	mtc0	zero,$9
9fc003fc:	3c049fc1 	lui	a0,0x9fc1
9fc00400:	0ff0284a 	jal	9fc0a128 <puts>
9fc00404:	2484a3b0 	addiu	a0,a0,-23632
9fc00408:	0ff0289c 	jal	9fc0a270 <get_count>
9fc0040c:	3630fff4 	ori	s0,s1,0xfff4
9fc00410:	0ff028a0 	jal	9fc0a280 <get_count_my>
9fc00414:	afa20028 	sw	v0,40(sp)
9fc00418:	afa20024 	sw	v0,36(sp)
9fc0041c:	8e020000 	lw	v0,0(s0)
9fc00420:	00000000 	nop
9fc00424:	144000a6 	bnez	v0,9fc006c0 <shell3+0x300>
9fc00428:	24120001 	li	s2,1
9fc0042c:	24110007 	li	s1,7
9fc00430:	241307d0 	li	s3,2000
9fc00434:	00002021 	move	a0,zero
9fc00438:	00002821 	move	a1,zero
9fc0043c:	24060066 	li	a2,102
9fc00440:	24070001 	li	a3,1
9fc00444:	afb10010 	sw	s1,16(sp)
9fc00448:	afb20014 	sw	s2,20(sp)
9fc0044c:	0ff00224 	jal	9fc00890 <core_mark>
9fc00450:	afb30018 	sw	s3,24(sp)
9fc00454:	00002021 	move	a0,zero
9fc00458:	00002821 	move	a1,zero
9fc0045c:	24060066 	li	a2,102
9fc00460:	24070001 	li	a3,1
9fc00464:	00408021 	move	s0,v0
9fc00468:	afb10010 	sw	s1,16(sp)
9fc0046c:	afb20014 	sw	s2,20(sp)
9fc00470:	0ff00224 	jal	9fc00890 <core_mark>
9fc00474:	afb30018 	sw	s3,24(sp)
9fc00478:	00002021 	move	a0,zero
9fc0047c:	00002821 	move	a1,zero
9fc00480:	24060066 	li	a2,102
9fc00484:	24070001 	li	a3,1
9fc00488:	afb10010 	sw	s1,16(sp)
9fc0048c:	afa2002c 	sw	v0,44(sp)
9fc00490:	afb20014 	sw	s2,20(sp)
9fc00494:	0ff00224 	jal	9fc00890 <core_mark>
9fc00498:	afb30018 	sw	s3,24(sp)
9fc0049c:	00002021 	move	a0,zero
9fc004a0:	00002821 	move	a1,zero
9fc004a4:	24060066 	li	a2,102
9fc004a8:	24070001 	li	a3,1
9fc004ac:	afb10010 	sw	s1,16(sp)
9fc004b0:	afa20020 	sw	v0,32(sp)
9fc004b4:	afb20014 	sw	s2,20(sp)
9fc004b8:	0ff00224 	jal	9fc00890 <core_mark>
9fc004bc:	afb30018 	sw	s3,24(sp)
9fc004c0:	00002021 	move	a0,zero
9fc004c4:	00002821 	move	a1,zero
9fc004c8:	24060066 	li	a2,102
9fc004cc:	24070001 	li	a3,1
9fc004d0:	0040f021 	move	s8,v0
9fc004d4:	afb10010 	sw	s1,16(sp)
9fc004d8:	afb20014 	sw	s2,20(sp)
9fc004dc:	0ff00224 	jal	9fc00890 <core_mark>
9fc004e0:	afb30018 	sw	s3,24(sp)
9fc004e4:	00002021 	move	a0,zero
9fc004e8:	00002821 	move	a1,zero
9fc004ec:	24060066 	li	a2,102
9fc004f0:	24070001 	li	a3,1
9fc004f4:	0040b821 	move	s7,v0
9fc004f8:	afb10010 	sw	s1,16(sp)
9fc004fc:	afb20014 	sw	s2,20(sp)
9fc00500:	0ff00224 	jal	9fc00890 <core_mark>
9fc00504:	afb30018 	sw	s3,24(sp)
9fc00508:	00002021 	move	a0,zero
9fc0050c:	00002821 	move	a1,zero
9fc00510:	24060066 	li	a2,102
9fc00514:	24070001 	li	a3,1
9fc00518:	0040b021 	move	s6,v0
9fc0051c:	afb10010 	sw	s1,16(sp)
9fc00520:	afb20014 	sw	s2,20(sp)
9fc00524:	0ff00224 	jal	9fc00890 <core_mark>
9fc00528:	afb30018 	sw	s3,24(sp)
9fc0052c:	00002021 	move	a0,zero
9fc00530:	00002821 	move	a1,zero
9fc00534:	24060066 	li	a2,102
9fc00538:	24070001 	li	a3,1
9fc0053c:	0040a821 	move	s5,v0
9fc00540:	afb10010 	sw	s1,16(sp)
9fc00544:	afb20014 	sw	s2,20(sp)
9fc00548:	0ff00224 	jal	9fc00890 <core_mark>
9fc0054c:	afb30018 	sw	s3,24(sp)
9fc00550:	00002021 	move	a0,zero
9fc00554:	00002821 	move	a1,zero
9fc00558:	24060066 	li	a2,102
9fc0055c:	24070001 	li	a3,1
9fc00560:	0040a021 	move	s4,v0
9fc00564:	afb10010 	sw	s1,16(sp)
9fc00568:	afb20014 	sw	s2,20(sp)
9fc0056c:	0ff00224 	jal	9fc00890 <core_mark>
9fc00570:	afb30018 	sw	s3,24(sp)
9fc00574:	8fb8002c 	lw	t8,44(sp)
9fc00578:	8faf0020 	lw	t7,32(sp)
9fc0057c:	02187021 	addu	t6,s0,t8
9fc00580:	01cf6821 	addu	t5,t6,t7
9fc00584:	01be6021 	addu	t4,t5,s8
9fc00588:	01973821 	addu	a3,t4,s7
9fc0058c:	00f63021 	addu	a2,a3,s6
9fc00590:	00d52821 	addu	a1,a2,s5
9fc00594:	00b45821 	addu	t3,a1,s4
9fc00598:	00002021 	move	a0,zero
9fc0059c:	00002821 	move	a1,zero
9fc005a0:	24060066 	li	a2,102
9fc005a4:	24070001 	li	a3,1
9fc005a8:	0162a021 	addu	s4,t3,v0
9fc005ac:	afb10010 	sw	s1,16(sp)
9fc005b0:	afb20014 	sw	s2,20(sp)
9fc005b4:	0ff00224 	jal	9fc00890 <core_mark>
9fc005b8:	afb30018 	sw	s3,24(sp)
9fc005bc:	00548821 	addu	s1,v0,s4
9fc005c0:	0ff028a0 	jal	9fc0a280 <get_count_my>
9fc005c4:	00000000 	nop
9fc005c8:	0ff0289c 	jal	9fc0a270 <get_count>
9fc005cc:	00409821 	move	s3,v0
9fc005d0:	8fa40028 	lw	a0,40(sp)
9fc005d4:	8fb90024 	lw	t9,36(sp)
9fc005d8:	00449023 	subu	s2,v0,a0
9fc005dc:	12200028 	beqz	s1,9fc00680 <shell3+0x2c0>
9fc005e0:	02798023 	subu	s0,s3,t9
9fc005e4:	3c169fc1 	lui	s6,0x9fc1
9fc005e8:	0ff0284a 	jal	9fc0a128 <puts>
9fc005ec:	26c4a3d8 	addiu	a0,s6,-23592
9fc005f0:	3c07bfaf 	lui	a3,0xbfaf
9fc005f4:	34ebf000 	ori	t3,a3,0xf000
9fc005f8:	34f5f008 	ori	s5,a3,0xf008
9fc005fc:	34f4f004 	ori	s4,a3,0xf004
9fc00600:	24060001 	li	a2,1
9fc00604:	24050002 	li	a1,2
9fc00608:	aea60000 	sw	a2,0(s5)
9fc0060c:	ae850000 	sw	a1,0(s4)
9fc00610:	ad600000 	sw	zero,0(t3)
9fc00614:	3c1ebfaf 	lui	s8,0xbfaf
9fc00618:	37cc8004 	ori	t4,s8,0x8004
9fc0061c:	37cdf010 	ori	t5,s8,0xf010
9fc00620:	37d78000 	ori	s7,s8,0x8000
9fc00624:	3c0e9fc1 	lui	t6,0x9fc1
9fc00628:	adb00000 	sw	s0,0(t5)
9fc0062c:	02402821 	move	a1,s2
9fc00630:	aef00000 	sw	s0,0(s7)
9fc00634:	25c4a3ec 	addiu	a0,t6,-23572
9fc00638:	ad920000 	sw	s2,0(t4)
9fc0063c:	0ff02784 	jal	9fc09e10 <printf>
9fc00640:	3c129fc1 	lui	s2,0x9fc1
9fc00644:	2644a418 	addiu	a0,s2,-23528
9fc00648:	02002821 	move	a1,s0
9fc0064c:	8fbf0054 	lw	ra,84(sp)
9fc00650:	8fbe0050 	lw	s8,80(sp)
9fc00654:	8fb7004c 	lw	s7,76(sp)
9fc00658:	8fb60048 	lw	s6,72(sp)
9fc0065c:	8fb50044 	lw	s5,68(sp)
9fc00660:	8fb40040 	lw	s4,64(sp)
9fc00664:	8fb3003c 	lw	s3,60(sp)
9fc00668:	8fb20038 	lw	s2,56(sp)
9fc0066c:	8fb10034 	lw	s1,52(sp)
9fc00670:	8fb00030 	lw	s0,48(sp)
9fc00674:	0bf02784 	j	9fc09e10 <printf>
9fc00678:	27bd0058 	addiu	sp,sp,88
9fc0067c:	00000000 	nop
9fc00680:	3c1f9fc1 	lui	ra,0x9fc1
9fc00684:	27e4a3c8 	addiu	a0,ra,-23608
9fc00688:	0ff0284a 	jal	9fc0a128 <puts>
9fc0068c:	3c11bfaf 	lui	s1,0xbfaf
9fc00690:	24080001 	li	t0,1
9fc00694:	362af000 	ori	t2,s1,0xf000
9fc00698:	3629f008 	ori	t1,s1,0xf008
9fc0069c:	3623f004 	ori	v1,s1,0xf004
9fc006a0:	3402ffff 	li	v0,0xffff
9fc006a4:	ac680000 	sw	t0,0(v1)
9fc006a8:	ad420000 	sw	v0,0(t2)
9fc006ac:	0bf00185 	j	9fc00614 <shell3+0x254>
9fc006b0:	ad280000 	sw	t0,0(t1)
	...
9fc006c0:	240a0007 	li	t2,7
9fc006c4:	24090001 	li	t1,1
9fc006c8:	240807d0 	li	t0,2000
9fc006cc:	00002021 	move	a0,zero
9fc006d0:	00002821 	move	a1,zero
9fc006d4:	24060066 	li	a2,102
9fc006d8:	24070001 	li	a3,1
9fc006dc:	afaa0010 	sw	t2,16(sp)
9fc006e0:	afa90014 	sw	t1,20(sp)
9fc006e4:	0ff00224 	jal	9fc00890 <core_mark>
9fc006e8:	afa80018 	sw	t0,24(sp)
9fc006ec:	0bf00170 	j	9fc005c0 <shell3+0x200>
9fc006f0:	00408821 	move	s1,v0
	...

9fc00700 <iterate>:
iterate():
9fc00700:	27bdffd8 	addiu	sp,sp,-40
9fc00704:	afb30020 	sw	s3,32(sp)
9fc00708:	8c93001c 	lw	s3,28(a0)
9fc0070c:	afb00014 	sw	s0,20(sp)
9fc00710:	afbf0024 	sw	ra,36(sp)
9fc00714:	afb2001c 	sw	s2,28(sp)
9fc00718:	afb10018 	sw	s1,24(sp)
9fc0071c:	00808021 	move	s0,a0
9fc00720:	a4800038 	sh	zero,56(a0)
9fc00724:	a480003a 	sh	zero,58(a0)
9fc00728:	a480003c 	sh	zero,60(a0)
9fc0072c:	1260004d 	beqz	s3,9fc00864 <iterate+0x164>
9fc00730:	a480003e 	sh	zero,62(a0)
9fc00734:	24050001 	li	a1,1
9fc00738:	2662ffff 	addiu	v0,s3,-1
9fc0073c:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00740:	30520001 	andi	s2,v0,0x1
9fc00744:	96050038 	lhu	a1,56(s0)
9fc00748:	0ff01c04 	jal	9fc07010 <crcu16>
9fc0074c:	00402021 	move	a0,v0
9fc00750:	a6020038 	sh	v0,56(s0)
9fc00754:	2405ffff 	li	a1,-1
9fc00758:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc0075c:	02002021 	move	a0,s0
9fc00760:	96050038 	lhu	a1,56(s0)
9fc00764:	24110001 	li	s1,1
9fc00768:	0ff01c04 	jal	9fc07010 <crcu16>
9fc0076c:	00402021 	move	a0,v0
9fc00770:	0233182b 	sltu	v1,s1,s3
9fc00774:	a602003a 	sh	v0,58(s0)
9fc00778:	1060003a 	beqz	v1,9fc00864 <iterate+0x164>
9fc0077c:	a6020038 	sh	v0,56(s0)
9fc00780:	12400013 	beqz	s2,9fc007d0 <iterate+0xd0>
9fc00784:	02002021 	move	a0,s0
9fc00788:	24050001 	li	a1,1
9fc0078c:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00790:	02002021 	move	a0,s0
9fc00794:	96050038 	lhu	a1,56(s0)
9fc00798:	0ff01c04 	jal	9fc07010 <crcu16>
9fc0079c:	00402021 	move	a0,v0
9fc007a0:	a6020038 	sh	v0,56(s0)
9fc007a4:	2405ffff 	li	a1,-1
9fc007a8:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc007ac:	02002021 	move	a0,s0
9fc007b0:	96050038 	lhu	a1,56(s0)
9fc007b4:	00402021 	move	a0,v0
9fc007b8:	0ff01c04 	jal	9fc07010 <crcu16>
9fc007bc:	26310001 	addiu	s1,s1,1
9fc007c0:	0233202b 	sltu	a0,s1,s3
9fc007c4:	10800027 	beqz	a0,9fc00864 <iterate+0x164>
9fc007c8:	a6020038 	sh	v0,56(s0)
9fc007cc:	02002021 	move	a0,s0
9fc007d0:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc007d4:	24050001 	li	a1,1
9fc007d8:	96050038 	lhu	a1,56(s0)
9fc007dc:	0ff01c04 	jal	9fc07010 <crcu16>
9fc007e0:	00402021 	move	a0,v0
9fc007e4:	a6020038 	sh	v0,56(s0)
9fc007e8:	02002021 	move	a0,s0
9fc007ec:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc007f0:	2405ffff 	li	a1,-1
9fc007f4:	96050038 	lhu	a1,56(s0)
9fc007f8:	0ff01c04 	jal	9fc07010 <crcu16>
9fc007fc:	00402021 	move	a0,v0
9fc00800:	26320001 	addiu	s2,s1,1
9fc00804:	02002021 	move	a0,s0
9fc00808:	24050001 	li	a1,1
9fc0080c:	16200002 	bnez	s1,9fc00818 <iterate+0x118>
9fc00810:	a6020038 	sh	v0,56(s0)
9fc00814:	a602003a 	sh	v0,58(s0)
9fc00818:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc0081c:	00000000 	nop
9fc00820:	96050038 	lhu	a1,56(s0)
9fc00824:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00828:	00402021 	move	a0,v0
9fc0082c:	a6020038 	sh	v0,56(s0)
9fc00830:	02002021 	move	a0,s0
9fc00834:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00838:	2405ffff 	li	a1,-1
9fc0083c:	96050038 	lhu	a1,56(s0)
9fc00840:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00844:	00402021 	move	a0,v0
9fc00848:	16400002 	bnez	s2,9fc00854 <iterate+0x154>
9fc0084c:	a6020038 	sh	v0,56(s0)
9fc00850:	a602003a 	sh	v0,58(s0)
9fc00854:	26510001 	addiu	s1,s2,1
9fc00858:	0233282b 	sltu	a1,s1,s3
9fc0085c:	14a0ffdb 	bnez	a1,9fc007cc <iterate+0xcc>
9fc00860:	00000000 	nop
9fc00864:	8fbf0024 	lw	ra,36(sp)
9fc00868:	00001021 	move	v0,zero
9fc0086c:	8fb30020 	lw	s3,32(sp)
9fc00870:	8fb2001c 	lw	s2,28(sp)
9fc00874:	8fb10018 	lw	s1,24(sp)
9fc00878:	8fb00014 	lw	s0,20(sp)
9fc0087c:	03e00008 	jr	ra
9fc00880:	27bd0028 	addiu	sp,sp,40
	...

9fc00890 <core_mark>:
core_mark():
9fc00890:	27bdf798 	addiu	sp,sp,-2152
9fc00894:	afb40850 	sw	s4,2128(sp)
9fc00898:	afb3084c 	sw	s3,2124(sp)
9fc0089c:	00c0a021 	move	s4,a2
9fc008a0:	00a09821 	move	s3,a1
9fc008a4:	afb20848 	sw	s2,2120(sp)
9fc008a8:	afb00840 	sw	s0,2112(sp)
9fc008ac:	00809021 	move	s2,a0
9fc008b0:	8fb00878 	lw	s0,2168(sp)
9fc008b4:	8fa2087c 	lw	v0,2172(sp)
9fc008b8:	8fa30880 	lw	v1,2176(sp)
9fc008bc:	3c049fc1 	lui	a0,0x9fc1
9fc008c0:	afb10844 	sw	s1,2116(sp)
9fc008c4:	2484a444 	addiu	a0,a0,-23484
9fc008c8:	00e08821 	move	s1,a3
9fc008cc:	02402821 	move	a1,s2
9fc008d0:	02603021 	move	a2,s3
9fc008d4:	02803821 	move	a3,s4
9fc008d8:	afbf0864 	sw	ra,2148(sp)
9fc008dc:	afbe0860 	sw	s8,2144(sp)
9fc008e0:	afb7085c 	sw	s7,2140(sp)
9fc008e4:	afb60858 	sw	s6,2136(sp)
9fc008e8:	afb50854 	sw	s5,2132(sp)
9fc008ec:	afa20018 	sw	v0,24(sp)
9fc008f0:	afa3001c 	sw	v1,28(sp)
9fc008f4:	afb10010 	sw	s1,16(sp)
9fc008f8:	0ff02784 	jal	9fc09e10 <printf>
9fc008fc:	afb00014 	sw	s0,20(sp)
9fc00900:	a7b20020 	sh	s2,32(sp)
9fc00904:	a7b30022 	sh	s3,34(sp)
9fc00908:	a7b40024 	sh	s4,36(sp)
9fc0090c:	16000002 	bnez	s0,9fc00918 <core_mark+0x88>
9fc00910:	afb1003c 	sw	s1,60(sp)
9fc00914:	24100007 	li	s0,7
9fc00918:	8fa30020 	lw	v1,32(sp)
9fc0091c:	00000000 	nop
9fc00920:	14600006 	bnez	v1,9fc0093c <core_mark+0xac>
9fc00924:	24070001 	li	a3,1
9fc00928:	87a50024 	lh	a1,36(sp)
9fc0092c:	00000000 	nop
9fc00930:	10a002db 	beqz	a1,9fc014a0 <core_mark+0xc10>
9fc00934:	24060066 	li	a2,102
9fc00938:	24070001 	li	a3,1
9fc0093c:	106702de 	beq	v1,a3,9fc014b8 <core_mark+0xc28>
9fc00940:	00000000 	nop
9fc00944:	87a60020 	lh	a2,32(sp)
9fc00948:	32040001 	andi	a0,s0,0x1
9fc0094c:	27a50064 	addiu	a1,sp,100
9fc00950:	248c0001 	addiu	t4,a0,1
9fc00954:	320b0002 	andi	t3,s0,0x2
9fc00958:	afa50028 	sw	a1,40(sp)
9fc0095c:	a7a60020 	sh	a2,32(sp)
9fc00960:	a7a00060 	sh	zero,96(sp)
9fc00964:	afb00040 	sw	s0,64(sp)
9fc00968:	11600265 	beqz	t3,9fc01300 <core_mark+0xa70>
9fc0096c:	3182ffff 	andi	v0,t4,0xffff
9fc00970:	00401821 	move	v1,v0
9fc00974:	320d0004 	andi	t5,s0,0x4
9fc00978:	11a0025d 	beqz	t5,9fc012f0 <core_mark+0xa60>
9fc0097c:	24640001 	addiu	a0,v1,1
9fc00980:	00801021 	move	v0,a0
9fc00984:	304fffff 	andi	t7,v0,0xffff
9fc00988:	241507d0 	li	s5,2000
9fc0098c:	15e00002 	bnez	t7,9fc00998 <core_mark+0x108>
9fc00990:	02af001b 	divu	zero,s5,t7
9fc00994:	0007000d 	break	0x7
9fc00998:	320e0001 	andi	t6,s0,0x1
9fc0099c:	00003812 	mflo	a3
9fc009a0:	15c00243 	bnez	t6,9fc012b0 <core_mark+0xa20>
9fc009a4:	afa70038 	sw	a3,56(sp)
9fc009a8:	00001821 	move	v1,zero
9fc009ac:	32160002 	andi	s6,s0,0x2
9fc009b0:	12c00007 	beqz	s6,9fc009d0 <core_mark+0x140>
9fc009b4:	321e0004 	andi	s8,s0,0x4
9fc009b8:	00670018 	mult	v1,a3
9fc009bc:	24790001 	addiu	t9,v1,1
9fc009c0:	3323ffff 	andi	v1,t9,0xffff
9fc009c4:	0000c012 	mflo	t8
9fc009c8:	00b8b821 	addu	s7,a1,t8
9fc009cc:	afb70030 	sw	s7,48(sp)
9fc009d0:	13c00005 	beqz	s8,9fc009e8 <core_mark+0x158>
9fc009d4:	32140001 	andi	s4,s0,0x1
9fc009d8:	00670018 	mult	v1,a3
9fc009dc:	00008812 	mflo	s1
9fc009e0:	00b1f821 	addu	ra,a1,s1
9fc009e4:	afbf0034 	sw	ra,52(sp)
9fc009e8:	12800007 	beqz	s4,9fc00a08 <core_mark+0x178>
9fc009ec:	32130002 	andi	s3,s0,0x2
9fc009f0:	8fa5002c 	lw	a1,44(sp)
9fc009f4:	0ff022a4 	jal	9fc08a90 <core_list_init>
9fc009f8:	00e02021 	move	a0,a3
9fc009fc:	8fb00040 	lw	s0,64(sp)
9fc00a00:	afa20044 	sw	v0,68(sp)
9fc00a04:	32130002 	andi	s3,s0,0x2
9fc00a08:	1660021d 	bnez	s3,9fc01280 <core_mark+0x9f0>
9fc00a0c:	00000000 	nop
9fc00a10:	32020004 	andi	v0,s0,0x4
9fc00a14:	1440006a 	bnez	v0,9fc00bc0 <core_mark+0x330>
9fc00a18:	00000000 	nop
9fc00a1c:	8fa4003c 	lw	a0,60(sp)
9fc00a20:	00000000 	nop
9fc00a24:	14800070 	bnez	a0,9fc00be8 <core_mark+0x358>
9fc00a28:	3c159fc1 	lui	s5,0x9fc1
9fc00a2c:	24020001 	li	v0,1
9fc00a30:	27b20020 	addiu	s2,sp,32
9fc00a34:	000238c0 	sll	a3,v0,0x3
9fc00a38:	00023040 	sll	a2,v0,0x1
9fc00a3c:	00c72821 	addu	a1,a2,a3
9fc00a40:	0ff0165c 	jal	9fc05970 <start_time>
9fc00a44:	afa5003c 	sw	a1,60(sp)
9fc00a48:	8fb3003c 	lw	s3,60(sp)
9fc00a4c:	a7a00058 	sh	zero,88(sp)
9fc00a50:	a7a0005a 	sh	zero,90(sp)
9fc00a54:	a7a0005c 	sh	zero,92(sp)
9fc00a58:	1260004d 	beqz	s3,9fc00b90 <core_mark+0x300>
9fc00a5c:	a7a0005e 	sh	zero,94(sp)
9fc00a60:	2669ffff 	addiu	t1,s3,-1
9fc00a64:	24050001 	li	a1,1
9fc00a68:	02402021 	move	a0,s2
9fc00a6c:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00a70:	31310001 	andi	s1,t1,0x1
9fc00a74:	97a50058 	lhu	a1,88(sp)
9fc00a78:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00a7c:	00402021 	move	a0,v0
9fc00a80:	2405ffff 	li	a1,-1
9fc00a84:	02402021 	move	a0,s2
9fc00a88:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00a8c:	a7a20058 	sh	v0,88(sp)
9fc00a90:	97a50058 	lhu	a1,88(sp)
9fc00a94:	24100001 	li	s0,1
9fc00a98:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00a9c:	00402021 	move	a0,v0
9fc00aa0:	0213402b 	sltu	t0,s0,s3
9fc00aa4:	a7a2005a 	sh	v0,90(sp)
9fc00aa8:	11000039 	beqz	t0,9fc00b90 <core_mark+0x300>
9fc00aac:	a7a20058 	sh	v0,88(sp)
9fc00ab0:	12200011 	beqz	s1,9fc00af8 <core_mark+0x268>
9fc00ab4:	24050001 	li	a1,1
9fc00ab8:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00abc:	02402021 	move	a0,s2
9fc00ac0:	97a50058 	lhu	a1,88(sp)
9fc00ac4:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00ac8:	00402021 	move	a0,v0
9fc00acc:	2405ffff 	li	a1,-1
9fc00ad0:	02402021 	move	a0,s2
9fc00ad4:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00ad8:	a7a20058 	sh	v0,88(sp)
9fc00adc:	97a50058 	lhu	a1,88(sp)
9fc00ae0:	26100001 	addiu	s0,s0,1
9fc00ae4:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00ae8:	00402021 	move	a0,v0
9fc00aec:	0213882b 	sltu	s1,s0,s3
9fc00af0:	12200027 	beqz	s1,9fc00b90 <core_mark+0x300>
9fc00af4:	a7a20058 	sh	v0,88(sp)
9fc00af8:	02402021 	move	a0,s2
9fc00afc:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00b00:	24050001 	li	a1,1
9fc00b04:	97a50058 	lhu	a1,88(sp)
9fc00b08:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00b0c:	00402021 	move	a0,v0
9fc00b10:	02402021 	move	a0,s2
9fc00b14:	2405ffff 	li	a1,-1
9fc00b18:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00b1c:	a7a20058 	sh	v0,88(sp)
9fc00b20:	97a50058 	lhu	a1,88(sp)
9fc00b24:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00b28:	00402021 	move	a0,v0
9fc00b2c:	26110001 	addiu	s1,s0,1
9fc00b30:	02402021 	move	a0,s2
9fc00b34:	24050001 	li	a1,1
9fc00b38:	16000002 	bnez	s0,9fc00b44 <core_mark+0x2b4>
9fc00b3c:	a7a20058 	sh	v0,88(sp)
9fc00b40:	a7a2005a 	sh	v0,90(sp)
9fc00b44:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00b48:	00000000 	nop
9fc00b4c:	97a50058 	lhu	a1,88(sp)
9fc00b50:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00b54:	00402021 	move	a0,v0
9fc00b58:	02402021 	move	a0,s2
9fc00b5c:	2405ffff 	li	a1,-1
9fc00b60:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00b64:	a7a20058 	sh	v0,88(sp)
9fc00b68:	97a50058 	lhu	a1,88(sp)
9fc00b6c:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00b70:	00402021 	move	a0,v0
9fc00b74:	16200002 	bnez	s1,9fc00b80 <core_mark+0x2f0>
9fc00b78:	a7a20058 	sh	v0,88(sp)
9fc00b7c:	a7a2005a 	sh	v0,90(sp)
9fc00b80:	26300001 	addiu	s0,s1,1
9fc00b84:	0213302b 	sltu	a2,s0,s3
9fc00b88:	14c0ffdc 	bnez	a2,9fc00afc <core_mark+0x26c>
9fc00b8c:	02402021 	move	a0,s2
9fc00b90:	0ff01658 	jal	9fc05960 <stop_time>
9fc00b94:	00000000 	nop
9fc00b98:	0ff01630 	jal	9fc058c0 <get_time>
9fc00b9c:	00000000 	nop
9fc00ba0:	0ff01648 	jal	9fc05920 <time_in_secs>
9fc00ba4:	00402021 	move	a0,v0
9fc00ba8:	144001c5 	bnez	v0,9fc012c0 <core_mark+0xa30>
9fc00bac:	240e000a 	li	t6,10
9fc00bb0:	8fa2003c 	lw	v0,60(sp)
9fc00bb4:	0bf0028e 	j	9fc00a38 <core_mark+0x1a8>
9fc00bb8:	000238c0 	sll	a3,v0,0x3
9fc00bbc:	00000000 	nop
9fc00bc0:	8fa40038 	lw	a0,56(sp)
9fc00bc4:	87a50020 	lh	a1,32(sp)
9fc00bc8:	8fa60034 	lw	a2,52(sp)
9fc00bcc:	0ff01660 	jal	9fc05980 <core_init_state>
9fc00bd0:	00000000 	nop
9fc00bd4:	8fa4003c 	lw	a0,60(sp)
9fc00bd8:	00000000 	nop
9fc00bdc:	1080ff94 	beqz	a0,9fc00a30 <core_mark+0x1a0>
9fc00be0:	24020001 	li	v0,1
9fc00be4:	3c159fc1 	lui	s5,0x9fc1
9fc00be8:	240f0001 	li	t7,1
9fc00bec:	26a4a470 	addiu	a0,s5,-23440
9fc00bf0:	0ff0284a 	jal	9fc0a128 <puts>
9fc00bf4:	af8f8010 	sw	t7,-32752(gp)
9fc00bf8:	0ff028a7 	jal	9fc0a29c <get_ns>
9fc00bfc:	00000000 	nop
9fc00c00:	8fb3003c 	lw	s3,60(sp)
9fc00c04:	0040a021 	move	s4,v0
9fc00c08:	a7a00058 	sh	zero,88(sp)
9fc00c0c:	a7a0005a 	sh	zero,90(sp)
9fc00c10:	a7a0005c 	sh	zero,92(sp)
9fc00c14:	1260004e 	beqz	s3,9fc00d50 <core_mark+0x4c0>
9fc00c18:	a7a0005e 	sh	zero,94(sp)
9fc00c1c:	27b10020 	addiu	s1,sp,32
9fc00c20:	24050001 	li	a1,1
9fc00c24:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00c28:	02202021 	move	a0,s1
9fc00c2c:	97a50058 	lhu	a1,88(sp)
9fc00c30:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00c34:	00402021 	move	a0,v0
9fc00c38:	2405ffff 	li	a1,-1
9fc00c3c:	02202021 	move	a0,s1
9fc00c40:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00c44:	a7a20058 	sh	v0,88(sp)
9fc00c48:	97a50058 	lhu	a1,88(sp)
9fc00c4c:	24100001 	li	s0,1
9fc00c50:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00c54:	00402021 	move	a0,v0
9fc00c58:	2677ffff 	addiu	s7,s3,-1
9fc00c5c:	0213b02b 	sltu	s6,s0,s3
9fc00c60:	32f20001 	andi	s2,s7,0x1
9fc00c64:	a7a2005a 	sh	v0,90(sp)
9fc00c68:	12c00039 	beqz	s6,9fc00d50 <core_mark+0x4c0>
9fc00c6c:	a7a20058 	sh	v0,88(sp)
9fc00c70:	12400011 	beqz	s2,9fc00cb8 <core_mark+0x428>
9fc00c74:	24050001 	li	a1,1
9fc00c78:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00c7c:	02202021 	move	a0,s1
9fc00c80:	97a50058 	lhu	a1,88(sp)
9fc00c84:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00c88:	00402021 	move	a0,v0
9fc00c8c:	2405ffff 	li	a1,-1
9fc00c90:	02202021 	move	a0,s1
9fc00c94:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00c98:	a7a20058 	sh	v0,88(sp)
9fc00c9c:	97a50058 	lhu	a1,88(sp)
9fc00ca0:	26100001 	addiu	s0,s0,1
9fc00ca4:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00ca8:	00402021 	move	a0,v0
9fc00cac:	0213902b 	sltu	s2,s0,s3
9fc00cb0:	12400027 	beqz	s2,9fc00d50 <core_mark+0x4c0>
9fc00cb4:	a7a20058 	sh	v0,88(sp)
9fc00cb8:	02202021 	move	a0,s1
9fc00cbc:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00cc0:	24050001 	li	a1,1
9fc00cc4:	97a50058 	lhu	a1,88(sp)
9fc00cc8:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00ccc:	00402021 	move	a0,v0
9fc00cd0:	02202021 	move	a0,s1
9fc00cd4:	2405ffff 	li	a1,-1
9fc00cd8:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00cdc:	a7a20058 	sh	v0,88(sp)
9fc00ce0:	97a50058 	lhu	a1,88(sp)
9fc00ce4:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00ce8:	00402021 	move	a0,v0
9fc00cec:	26120001 	addiu	s2,s0,1
9fc00cf0:	02202021 	move	a0,s1
9fc00cf4:	24050001 	li	a1,1
9fc00cf8:	16000002 	bnez	s0,9fc00d04 <core_mark+0x474>
9fc00cfc:	a7a20058 	sh	v0,88(sp)
9fc00d00:	a7a2005a 	sh	v0,90(sp)
9fc00d04:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00d08:	00000000 	nop
9fc00d0c:	97a50058 	lhu	a1,88(sp)
9fc00d10:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00d14:	00402021 	move	a0,v0
9fc00d18:	02202021 	move	a0,s1
9fc00d1c:	2405ffff 	li	a1,-1
9fc00d20:	0ff024e0 	jal	9fc09380 <core_bench_list>
9fc00d24:	a7a20058 	sh	v0,88(sp)
9fc00d28:	97a50058 	lhu	a1,88(sp)
9fc00d2c:	0ff01c04 	jal	9fc07010 <crcu16>
9fc00d30:	00402021 	move	a0,v0
9fc00d34:	16400002 	bnez	s2,9fc00d40 <core_mark+0x4b0>
9fc00d38:	a7a20058 	sh	v0,88(sp)
9fc00d3c:	a7a2005a 	sh	v0,90(sp)
9fc00d40:	26500001 	addiu	s0,s2,1
9fc00d44:	0213f82b 	sltu	ra,s0,s3
9fc00d48:	17e0ffdc 	bnez	ra,9fc00cbc <core_mark+0x42c>
9fc00d4c:	02202021 	move	a0,s1
9fc00d50:	0ff028a7 	jal	9fc0a29c <get_ns>
9fc00d54:	00000000 	nop
9fc00d58:	3c199fc1 	lui	t9,0x9fc1
9fc00d5c:	2724a47c 	addiu	a0,t9,-23428
9fc00d60:	0ff0284a 	jal	9fc0a128 <puts>
9fc00d64:	0054b823 	subu	s7,v0,s4
9fc00d68:	87a40020 	lh	a0,32(sp)
9fc00d6c:	0ff01d20 	jal	9fc07480 <crc16>
9fc00d70:	00002821 	move	a1,zero
9fc00d74:	87a40022 	lh	a0,34(sp)
9fc00d78:	0ff01d20 	jal	9fc07480 <crc16>
9fc00d7c:	00402821 	move	a1,v0
9fc00d80:	87a40024 	lh	a0,36(sp)
9fc00d84:	0ff01d20 	jal	9fc07480 <crc16>
9fc00d88:	00402821 	move	a1,v0
9fc00d8c:	87a40038 	lh	a0,56(sp)
9fc00d90:	0ff01d20 	jal	9fc07480 <crc16>
9fc00d94:	00402821 	move	a1,v0
9fc00d98:	24187b05 	li	t8,31493
9fc00d9c:	10580238 	beq	v0,t8,9fc01680 <core_mark+0xdf0>
9fc00da0:	0040b021 	move	s6,v0
9fc00da4:	2c5e7b06 	sltiu	s8,v0,31494
9fc00da8:	17c00159 	bnez	s8,9fc01310 <core_mark+0xa80>
9fc00dac:	241f18f2 	li	ra,6386
9fc00db0:	34148a02 	li	s4,0x8a02
9fc00db4:	1054022b 	beq	v0,s4,9fc01664 <core_mark+0xdd4>
9fc00db8:	3413e9f5 	li	s3,0xe9f5
9fc00dbc:	105301c7 	beq	v0,s3,9fc014dc <core_mark+0xc4c>
9fc00dc0:	3c029fc1 	lui	v0,0x9fc1
9fc00dc4:	3410ffff 	li	s0,0xffff
9fc00dc8:	0ff01c00 	jal	9fc07000 <check_data_types>
9fc00dcc:	3c139fc1 	lui	s3,0x9fc1
9fc00dd0:	02021821 	addu	v1,s0,v0
9fc00dd4:	8fa50038 	lw	a1,56(sp)
9fc00dd8:	0003cc00 	sll	t9,v1,0x10
9fc00ddc:	2664a604 	addiu	a0,s3,-23036
9fc00de0:	0ff02784 	jal	9fc09e10 <printf>
9fc00de4:	00199c03 	sra	s3,t9,0x10
9fc00de8:	3c189fc1 	lui	t8,0x9fc1
9fc00dec:	2704a61c 	addiu	a0,t8,-23012
9fc00df0:	0ff02784 	jal	9fc09e10 <printf>
9fc00df4:	02e02821 	move	a1,s7
9fc00df8:	8f8f8010 	lw	t7,-32752(gp)
9fc00dfc:	8fa8003c 	lw	t0,60(sp)
9fc00e00:	3c0d3b9a 	lui	t5,0x3b9a
9fc00e04:	010f0018 	mult	t0,t7
9fc00e08:	35acca00 	ori	t4,t5,0xca00
9fc00e0c:	3c0e9fc1 	lui	t6,0x9fc1
9fc00e10:	25c4a62c 	addiu	a0,t6,-22996
9fc00e14:	3c119fc1 	lui	s1,0x9fc1
9fc00e18:	3c1e9fc1 	lui	s8,0x9fc1
9fc00e1c:	00002812 	mflo	a1
	...
9fc00e28:	00ac0018 	mult	a1,t4
9fc00e2c:	00005812 	mflo	t3
	...
9fc00e38:	16e00002 	bnez	s7,9fc00e44 <core_mark+0x5b4>
9fc00e3c:	0177001b 	divu	zero,t3,s7
9fc00e40:	0007000d 	break	0x7
9fc00e44:	00002812 	mflo	a1
9fc00e48:	0ff02784 	jal	9fc09e10 <printf>
9fc00e4c:	3c159fc1 	lui	s5,0x9fc1
9fc00e50:	3c0a9fc1 	lui	t2,0x9fc1
9fc00e54:	0ff0284a 	jal	9fc0a128 <puts>
9fc00e58:	2544a644 	addiu	a0,t2,-22972
9fc00e5c:	0ff0284a 	jal	9fc0a128 <puts>
9fc00e60:	2624a6a0 	addiu	a0,s1,-22880
9fc00e64:	8fa5003c 	lw	a1,60(sp)
9fc00e68:	02e03021 	move	a2,s7
9fc00e6c:	27c4a6cc 	addiu	a0,s8,-22836
9fc00e70:	0ff02784 	jal	9fc09e10 <printf>
9fc00e74:	3c179fc1 	lui	s7,0x9fc1
9fc00e78:	00002821 	move	a1,zero
9fc00e7c:	0ff02784 	jal	9fc09e10 <printf>
9fc00e80:	26e4a6f8 	addiu	a0,s7,-22792
9fc00e84:	0ff01648 	jal	9fc05920 <time_in_secs>
9fc00e88:	00002021 	move	a0,zero
9fc00e8c:	00402821 	move	a1,v0
9fc00e90:	0ff02784 	jal	9fc09e10 <printf>
9fc00e94:	26a4a710 	addiu	a0,s5,-22768
9fc00e98:	0ff01648 	jal	9fc05920 <time_in_secs>
9fc00e9c:	00002021 	move	a0,zero
9fc00ea0:	144001db 	bnez	v0,9fc01610 <core_mark+0xd80>
9fc00ea4:	00000000 	nop
9fc00ea8:	8faa003c 	lw	t2,60(sp)
9fc00eac:	8f8b8010 	lw	t3,-32752(gp)
9fc00eb0:	3c119fc1 	lui	s1,0x9fc1
9fc00eb4:	016a0018 	mult	t3,t2
9fc00eb8:	2624a740 	addiu	a0,s1,-22720
9fc00ebc:	3c1e9fc1 	lui	s8,0x9fc1
9fc00ec0:	3c179fc1 	lui	s7,0x9fc1
9fc00ec4:	00002812 	mflo	a1
9fc00ec8:	0ff02784 	jal	9fc09e10 <printf>
9fc00ecc:	3c159fc1 	lui	s5,0x9fc1
9fc00ed0:	27c4a758 	addiu	a0,s8,-22696
9fc00ed4:	0ff02784 	jal	9fc09e10 <printf>
9fc00ed8:	26e5a770 	addiu	a1,s7,-22672
9fc00edc:	3c099fc1 	lui	t1,0x9fc1
9fc00ee0:	2525a6f4 	addiu	a1,t1,-22796
9fc00ee4:	0ff02784 	jal	9fc09e10 <printf>
9fc00ee8:	26a4a77c 	addiu	a0,s5,-22660
9fc00eec:	3c079fc1 	lui	a3,0x9fc1
9fc00ef0:	3c069fc1 	lui	a2,0x9fc1
9fc00ef4:	24e4a794 	addiu	a0,a3,-22636
9fc00ef8:	0ff02784 	jal	9fc09e10 <printf>
9fc00efc:	24c5a7ac 	addiu	a1,a2,-22612
9fc00f00:	3c049fc1 	lui	a0,0x9fc1
9fc00f04:	02c02821 	move	a1,s6
9fc00f08:	0ff02784 	jal	9fc09e10 <printf>
9fc00f0c:	2484a7fc 	addiu	a0,a0,-22532
9fc00f10:	8fa30040 	lw	v1,64(sp)
9fc00f14:	00000000 	nop
9fc00f18:	30760001 	andi	s6,v1,0x1
9fc00f1c:	16c00174 	bnez	s6,9fc014f0 <core_mark+0xc60>
9fc00f20:	00000000 	nop
9fc00f24:	8f868010 	lw	a2,-32752(gp)
9fc00f28:	30740002 	andi	s4,v1,0x2
9fc00f2c:	1280003f 	beqz	s4,9fc0102c <core_mark+0x79c>
9fc00f30:	30720004 	andi	s2,v1,0x4
9fc00f34:	10c0003d 	beqz	a2,9fc0102c <core_mark+0x79c>
9fc00f38:	00000000 	nop
9fc00f3c:	3c109fc1 	lui	s0,0x9fc1
9fc00f40:	2612a834 	addiu	s2,s0,-22476
9fc00f44:	27b10020 	addiu	s1,sp,32
9fc00f48:	00008021 	move	s0,zero
9fc00f4c:	00103080 	sll	a2,s0,0x2
9fc00f50:	0010f180 	sll	s8,s0,0x6
9fc00f54:	00de2021 	addu	a0,a2,s8
9fc00f58:	0224f821 	addu	ra,s1,a0
9fc00f5c:	97e6003c 	lhu	a2,60(ra)
9fc00f60:	02002821 	move	a1,s0
9fc00f64:	02402021 	move	a0,s2
9fc00f68:	0ff02784 	jal	9fc09e10 <printf>
9fc00f6c:	26170001 	addiu	s7,s0,1
9fc00f70:	32f0ffff 	andi	s0,s7,0xffff
9fc00f74:	8f868010 	lw	a2,-32752(gp)
9fc00f78:	00104880 	sll	t1,s0,0x2
9fc00f7c:	0010a980 	sll	s5,s0,0x6
9fc00f80:	01353821 	addu	a3,t1,s5
9fc00f84:	0206b02b 	sltu	s6,s0,a2
9fc00f88:	02402021 	move	a0,s2
9fc00f8c:	02271821 	addu	v1,s1,a3
9fc00f90:	12c00023 	beqz	s6,9fc01020 <core_mark+0x790>
9fc00f94:	02002821 	move	a1,s0
9fc00f98:	9466003c 	lhu	a2,60(v1)
9fc00f9c:	0ff02784 	jal	9fc09e10 <printf>
9fc00fa0:	26140001 	addiu	s4,s0,1
9fc00fa4:	3290ffff 	andi	s0,s4,0xffff
9fc00fa8:	8f868010 	lw	a2,-32752(gp)
9fc00fac:	0010c880 	sll	t9,s0,0x2
9fc00fb0:	00101180 	sll	v0,s0,0x6
9fc00fb4:	0322c021 	addu	t8,t9,v0
9fc00fb8:	0206402b 	sltu	t0,s0,a2
9fc00fbc:	02402021 	move	a0,s2
9fc00fc0:	02381821 	addu	v1,s1,t8
9fc00fc4:	11000016 	beqz	t0,9fc01020 <core_mark+0x790>
9fc00fc8:	02002821 	move	a1,s0
9fc00fcc:	9466003c 	lhu	a2,60(v1)
9fc00fd0:	0ff02784 	jal	9fc09e10 <printf>
9fc00fd4:	26150001 	addiu	s5,s0,1
9fc00fd8:	32b0ffff 	andi	s0,s5,0xffff
9fc00fdc:	8f868010 	lw	a2,-32752(gp)
9fc00fe0:	00101980 	sll	v1,s0,0x6
9fc00fe4:	00104880 	sll	t1,s0,0x2
9fc00fe8:	01233821 	addu	a3,t1,v1
9fc00fec:	0206b02b 	sltu	s6,s0,a2
9fc00ff0:	02402021 	move	a0,s2
9fc00ff4:	02271821 	addu	v1,s1,a3
9fc00ff8:	12c00009 	beqz	s6,9fc01020 <core_mark+0x790>
9fc00ffc:	02002821 	move	a1,s0
9fc01000:	9466003c 	lhu	a2,60(v1)
9fc01004:	0ff02784 	jal	9fc09e10 <printf>
9fc01008:	261e0001 	addiu	s8,s0,1
9fc0100c:	8f868010 	lw	a2,-32752(gp)
9fc01010:	33d0ffff 	andi	s0,s8,0xffff
9fc01014:	0206b82b 	sltu	s7,s0,a2
9fc01018:	16e0ffcc 	bnez	s7,9fc00f4c <core_mark+0x6bc>
9fc0101c:	00000000 	nop
9fc01020:	8fa30040 	lw	v1,64(sp)
9fc01024:	00000000 	nop
9fc01028:	30720004 	andi	s2,v1,0x4
9fc0102c:	1240003f 	beqz	s2,9fc0112c <core_mark+0x89c>
9fc01030:	00000000 	nop
9fc01034:	10c0007a 	beqz	a2,9fc01220 <core_mark+0x990>
9fc01038:	00000000 	nop
9fc0103c:	3c119fc1 	lui	s1,0x9fc1
9fc01040:	2632a850 	addiu	s2,s1,-22448
9fc01044:	00008021 	move	s0,zero
9fc01048:	27b10020 	addiu	s1,sp,32
9fc0104c:	00104080 	sll	t0,s0,0x2
9fc01050:	0010c180 	sll	t8,s0,0x6
9fc01054:	01187821 	addu	t7,t0,t8
9fc01058:	022f7021 	addu	t6,s1,t7
9fc0105c:	95c6003e 	lhu	a2,62(t6)
9fc01060:	02002821 	move	a1,s0
9fc01064:	0ff02784 	jal	9fc09e10 <printf>
9fc01068:	02402021 	move	a0,s2
9fc0106c:	26050001 	addiu	a1,s0,1
9fc01070:	30b0ffff 	andi	s0,a1,0xffff
9fc01074:	8f868010 	lw	a2,-32752(gp)
9fc01078:	00106080 	sll	t4,s0,0x2
9fc0107c:	00106980 	sll	t5,s0,0x6
9fc01080:	018d5821 	addu	t3,t4,t5
9fc01084:	0206502b 	sltu	t2,s0,a2
9fc01088:	02402021 	move	a0,s2
9fc0108c:	022b1821 	addu	v1,s1,t3
9fc01090:	11400026 	beqz	t2,9fc0112c <core_mark+0x89c>
9fc01094:	02002821 	move	a1,s0
9fc01098:	9466003e 	lhu	a2,62(v1)
9fc0109c:	0ff02784 	jal	9fc09e10 <printf>
9fc010a0:	00000000 	nop
9fc010a4:	260a0001 	addiu	t2,s0,1
9fc010a8:	3150ffff 	andi	s0,t2,0xffff
9fc010ac:	8f868010 	lw	a2,-32752(gp)
9fc010b0:	0010b880 	sll	s7,s0,0x2
9fc010b4:	0010f180 	sll	s8,s0,0x6
9fc010b8:	02fea821 	addu	s5,s7,s8
9fc010bc:	0206482b 	sltu	t1,s0,a2
9fc010c0:	02402021 	move	a0,s2
9fc010c4:	02351821 	addu	v1,s1,s5
9fc010c8:	11200018 	beqz	t1,9fc0112c <core_mark+0x89c>
9fc010cc:	02002821 	move	a1,s0
9fc010d0:	9466003e 	lhu	a2,62(v1)
9fc010d4:	0ff02784 	jal	9fc09e10 <printf>
9fc010d8:	00000000 	nop
9fc010dc:	260e0001 	addiu	t6,s0,1
9fc010e0:	31d0ffff 	andi	s0,t6,0xffff
9fc010e4:	8f868010 	lw	a2,-32752(gp)
9fc010e8:	00102980 	sll	a1,s0,0x6
9fc010ec:	00106880 	sll	t5,s0,0x2
9fc010f0:	01a56021 	addu	t4,t5,a1
9fc010f4:	0206582b 	sltu	t3,s0,a2
9fc010f8:	02402021 	move	a0,s2
9fc010fc:	022c1821 	addu	v1,s1,t4
9fc01100:	1160000a 	beqz	t3,9fc0112c <core_mark+0x89c>
9fc01104:	02002821 	move	a1,s0
9fc01108:	9466003e 	lhu	a2,62(v1)
9fc0110c:	0ff02784 	jal	9fc09e10 <printf>
9fc01110:	00000000 	nop
9fc01114:	260f0001 	addiu	t7,s0,1
9fc01118:	8f868010 	lw	a2,-32752(gp)
9fc0111c:	31f0ffff 	andi	s0,t7,0xffff
9fc01120:	0206202b 	sltu	a0,s0,a2
9fc01124:	1480ffc9 	bnez	a0,9fc0104c <core_mark+0x7bc>
9fc01128:	00000000 	nop
9fc0112c:	10c0003c 	beqz	a2,9fc01220 <core_mark+0x990>
9fc01130:	00000000 	nop
9fc01134:	3c199fc1 	lui	t9,0x9fc1
9fc01138:	2732a86c 	addiu	s2,t9,-22420
9fc0113c:	00008021 	move	s0,zero
9fc01140:	27b10020 	addiu	s1,sp,32
9fc01144:	0010f880 	sll	ra,s0,0x2
9fc01148:	00102180 	sll	a0,s0,0x6
9fc0114c:	03e4b821 	addu	s7,ra,a0
9fc01150:	0237a821 	addu	s5,s1,s7
9fc01154:	96a60038 	lhu	a2,56(s5)
9fc01158:	02002821 	move	a1,s0
9fc0115c:	0ff02784 	jal	9fc09e10 <printf>
9fc01160:	02402021 	move	a0,s2
9fc01164:	26090001 	addiu	t1,s0,1
9fc01168:	3130ffff 	andi	s0,t1,0xffff
9fc0116c:	8f948010 	lw	s4,-32752(gp)
9fc01170:	00101980 	sll	v1,s0,0x6
9fc01174:	00103880 	sll	a3,s0,0x2
9fc01178:	00e3b021 	addu	s6,a3,v1
9fc0117c:	0214102b 	sltu	v0,s0,s4
9fc01180:	02402021 	move	a0,s2
9fc01184:	02361821 	addu	v1,s1,s6
9fc01188:	10400025 	beqz	v0,9fc01220 <core_mark+0x990>
9fc0118c:	02002821 	move	a1,s0
9fc01190:	94660038 	lhu	a2,56(v1)
9fc01194:	0ff02784 	jal	9fc09e10 <printf>
9fc01198:	00000000 	nop
9fc0119c:	260e0001 	addiu	t6,s0,1
9fc011a0:	31d0ffff 	andi	s0,t6,0xffff
9fc011a4:	8f8b8010 	lw	t3,-32752(gp)
9fc011a8:	00102980 	sll	a1,s0,0x6
9fc011ac:	00106880 	sll	t5,s0,0x2
9fc011b0:	01a56021 	addu	t4,t5,a1
9fc011b4:	020b502b 	sltu	t2,s0,t3
9fc011b8:	02402021 	move	a0,s2
9fc011bc:	022c1821 	addu	v1,s1,t4
9fc011c0:	11400017 	beqz	t2,9fc01220 <core_mark+0x990>
9fc011c4:	02002821 	move	a1,s0
9fc011c8:	94660038 	lhu	a2,56(v1)
9fc011cc:	0ff02784 	jal	9fc09e10 <printf>
9fc011d0:	26140001 	addiu	s4,s0,1
9fc011d4:	3290ffff 	andi	s0,s4,0xffff
9fc011d8:	8f888010 	lw	t0,-32752(gp)
9fc011dc:	0010c880 	sll	t9,s0,0x2
9fc011e0:	00101180 	sll	v0,s0,0x6
9fc011e4:	0322c021 	addu	t8,t9,v0
9fc011e8:	0208782b 	sltu	t7,s0,t0
9fc011ec:	02402021 	move	a0,s2
9fc011f0:	02381821 	addu	v1,s1,t8
9fc011f4:	11e0000a 	beqz	t7,9fc01220 <core_mark+0x990>
9fc011f8:	02002821 	move	a1,s0
9fc011fc:	94660038 	lhu	a2,56(v1)
9fc01200:	0ff02784 	jal	9fc09e10 <printf>
9fc01204:	00000000 	nop
9fc01208:	26030001 	addiu	v1,s0,1
9fc0120c:	8f878010 	lw	a3,-32752(gp)
9fc01210:	3070ffff 	andi	s0,v1,0xffff
9fc01214:	0207b02b 	sltu	s6,s0,a3
9fc01218:	16c0ffca 	bnez	s6,9fc01144 <core_mark+0x8b4>
9fc0121c:	00000000 	nop
9fc01220:	126000f7 	beqz	s3,9fc01600 <core_mark+0xd70>
9fc01224:	3c109fc1 	lui	s0,0x9fc1
9fc01228:	1a600109 	blez	s3,9fc01650 <core_mark+0xdc0>
9fc0122c:	3c069fc1 	lui	a2,0x9fc1
9fc01230:	0ff0284a 	jal	9fc0a128 <puts>
9fc01234:	24c4a8d4 	addiu	a0,a2,-22316
9fc01238:	0ff01654 	jal	9fc05950 <portable_fini>
9fc0123c:	27a40062 	addiu	a0,sp,98
9fc01240:	8fbf0864 	lw	ra,2148(sp)
9fc01244:	02601021 	move	v0,s3
9fc01248:	8fbe0860 	lw	s8,2144(sp)
9fc0124c:	8fb7085c 	lw	s7,2140(sp)
9fc01250:	8fb60858 	lw	s6,2136(sp)
9fc01254:	8fb50854 	lw	s5,2132(sp)
9fc01258:	8fb40850 	lw	s4,2128(sp)
9fc0125c:	8fb3084c 	lw	s3,2124(sp)
9fc01260:	8fb20848 	lw	s2,2120(sp)
9fc01264:	8fb10844 	lw	s1,2116(sp)
9fc01268:	8fb00840 	lw	s0,2112(sp)
9fc0126c:	03e00008 	jr	ra
9fc01270:	27bd0868 	addiu	sp,sp,2152
	...
9fc01280:	87a30022 	lh	v1,34(sp)
9fc01284:	87b20020 	lh	s2,32(sp)
9fc01288:	00038400 	sll	s0,v1,0x10
9fc0128c:	8fa40038 	lw	a0,56(sp)
9fc01290:	8fa50030 	lw	a1,48(sp)
9fc01294:	02123025 	or	a2,s0,s2
9fc01298:	0ff005ac 	jal	9fc016b0 <core_init_matrix>
9fc0129c:	27a70048 	addiu	a3,sp,72
9fc012a0:	8fb00040 	lw	s0,64(sp)
9fc012a4:	0bf00285 	j	9fc00a14 <core_mark+0x184>
9fc012a8:	32020004 	andi	v0,s0,0x4
9fc012ac:	00000000 	nop
9fc012b0:	afa5002c 	sw	a1,44(sp)
9fc012b4:	0bf0026b 	j	9fc009ac <core_mark+0x11c>
9fc012b8:	24030001 	li	v1,1
9fc012bc:	00000000 	nop
9fc012c0:	14400002 	bnez	v0,9fc012cc <core_mark+0xa3c>
9fc012c4:	01c2001b 	divu	zero,t6,v0
9fc012c8:	0007000d 	break	0x7
9fc012cc:	8fab003c 	lw	t3,60(sp)
9fc012d0:	00006812 	mflo	t5
9fc012d4:	25ac0001 	addiu	t4,t5,1
9fc012d8:	00000000 	nop
9fc012dc:	016c0018 	mult	t3,t4
9fc012e0:	00005012 	mflo	t2
9fc012e4:	0bf002f9 	j	9fc00be4 <core_mark+0x354>
9fc012e8:	afaa003c 	sw	t2,60(sp)
9fc012ec:	00000000 	nop
9fc012f0:	0bf00261 	j	9fc00984 <core_mark+0xf4>
9fc012f4:	00601021 	move	v0,v1
	...
9fc01300:	0bf0025d 	j	9fc00974 <core_mark+0xe4>
9fc01304:	00801821 	move	v1,a0
	...
9fc01310:	105f00e0 	beq	v0,ra,9fc01694 <core_mark+0xe04>
9fc01314:	24114eaf 	li	s1,20143
9fc01318:	1451feab 	bne	v0,s1,9fc00dc8 <core_mark+0x538>
9fc0131c:	3410ffff 	li	s0,0xffff
9fc01320:	3c039fc1 	lui	v1,0x9fc1
9fc01324:	0ff0284a 	jal	9fc0a128 <puts>
9fc01328:	2464a4e8 	addiu	a0,v1,-23320
9fc0132c:	24050002 	li	a1,2
9fc01330:	8f868010 	lw	a2,-32752(gp)
9fc01334:	00000000 	nop
9fc01338:	10c000db 	beqz	a2,9fc016a8 <core_mark+0xe18>
9fc0133c:	3c0e9fc1 	lui	t6,0x9fc1
9fc01340:	3c099fc1 	lui	t1,0x9fc1
9fc01344:	00052840 	sll	a1,a1,0x1
9fc01348:	25cda978 	addiu	t5,t6,-22152
9fc0134c:	3c0b9fc1 	lui	t3,0x9fc1
9fc01350:	2528a96c 	addiu	t0,t1,-22164
9fc01354:	00ad6021 	addu	t4,a1,t5
9fc01358:	256aa960 	addiu	t2,t3,-22176
9fc0135c:	00a83821 	addu	a3,a1,t0
9fc01360:	afac083c 	sw	t4,2108(sp)
9fc01364:	00aaf021 	addu	s8,a1,t2
9fc01368:	afa70838 	sw	a3,2104(sp)
9fc0136c:	00008821 	move	s1,zero
9fc01370:	0000a021 	move	s4,zero
9fc01374:	27b50020 	addiu	s5,sp,32
9fc01378:	00119880 	sll	s3,s1,0x2
9fc0137c:	00119180 	sll	s2,s1,0x6
9fc01380:	0272c021 	addu	t8,s3,s2
9fc01384:	02b88021 	addu	s0,s5,t8
9fc01388:	8e080020 	lw	t0,32(s0)
9fc0138c:	00000000 	nop
9fc01390:	310f0001 	andi	t7,t0,0x1
9fc01394:	11e0000f 	beqz	t7,9fc013d4 <core_mark+0xb44>
9fc01398:	a6000040 	sh	zero,64(s0)
9fc0139c:	961f003a 	lhu	ra,58(s0)
9fc013a0:	97d90000 	lhu	t9,0(s8)
9fc013a4:	3c039fc1 	lui	v1,0x9fc1
9fc013a8:	02202821 	move	a1,s1
9fc013ac:	2464a570 	addiu	a0,v1,-23184
9fc013b0:	03e03021 	move	a2,ra
9fc013b4:	13f90007 	beq	ra,t9,9fc013d4 <core_mark+0xb44>
9fc013b8:	03203821 	move	a3,t9
9fc013bc:	0ff02784 	jal	9fc09e10 <printf>
9fc013c0:	00000000 	nop
9fc013c4:	96040040 	lhu	a0,64(s0)
9fc013c8:	8e080020 	lw	t0,32(s0)
9fc013cc:	24820001 	addiu	v0,a0,1
9fc013d0:	a6020040 	sh	v0,64(s0)
9fc013d4:	02723821 	addu	a3,s3,s2
9fc013d8:	31060002 	andi	a2,t0,0x2
9fc013dc:	10c00010 	beqz	a2,9fc01420 <core_mark+0xb90>
9fc013e0:	02a78021 	addu	s0,s5,a3
9fc013e4:	8fab0838 	lw	t3,2104(sp)
9fc013e8:	960a003c 	lhu	t2,60(s0)
9fc013ec:	95690000 	lhu	t1,0(t3)
9fc013f0:	3c0c9fc1 	lui	t4,0x9fc1
9fc013f4:	02202821 	move	a1,s1
9fc013f8:	2584a5a0 	addiu	a0,t4,-23136
9fc013fc:	01403021 	move	a2,t2
9fc01400:	11490007 	beq	t2,t1,9fc01420 <core_mark+0xb90>
9fc01404:	01203821 	move	a3,t1
9fc01408:	0ff02784 	jal	9fc09e10 <printf>
9fc0140c:	00000000 	nop
9fc01410:	960d0040 	lhu	t5,64(s0)
9fc01414:	8e080020 	lw	t0,32(s0)
9fc01418:	25a50001 	addiu	a1,t5,1
9fc0141c:	a6050040 	sh	a1,64(s0)
9fc01420:	02727821 	addu	t7,s3,s2
9fc01424:	310e0004 	andi	t6,t0,0x4
9fc01428:	11c00010 	beqz	t6,9fc0146c <core_mark+0xbdc>
9fc0142c:	02af8021 	addu	s0,s5,t7
9fc01430:	8fb9083c 	lw	t9,2108(sp)
9fc01434:	9618003e 	lhu	t8,62(s0)
9fc01438:	97280000 	lhu	t0,0(t9)
9fc0143c:	3c1f9fc1 	lui	ra,0x9fc1
9fc01440:	02202821 	move	a1,s1
9fc01444:	27e4a5d4 	addiu	a0,ra,-23084
9fc01448:	03003021 	move	a2,t8
9fc0144c:	13080007 	beq	t8,t0,9fc0146c <core_mark+0xbdc>
9fc01450:	01003821 	move	a3,t0
9fc01454:	0ff02784 	jal	9fc09e10 <printf>
9fc01458:	00000000 	nop
9fc0145c:	96020040 	lhu	v0,64(s0)
9fc01460:	00000000 	nop
9fc01464:	24430001 	addiu	v1,v0,1
9fc01468:	a6030040 	sh	v1,64(s0)
9fc0146c:	02724821 	addu	t1,s3,s2
9fc01470:	02a93821 	addu	a3,s5,t1
9fc01474:	94e40040 	lhu	a0,64(a3)
9fc01478:	26260001 	addiu	a2,s1,1
9fc0147c:	8f928010 	lw	s2,-32752(gp)
9fc01480:	30d1ffff 	andi	s1,a2,0xffff
9fc01484:	02848021 	addu	s0,s4,a0
9fc01488:	0010a400 	sll	s4,s0,0x10
9fc0148c:	0232982b 	sltu	s3,s1,s2
9fc01490:	1660ffb9 	bnez	s3,9fc01378 <core_mark+0xae8>
9fc01494:	0014a403 	sra	s4,s4,0x10
9fc01498:	0bf00372 	j	9fc00dc8 <core_mark+0x538>
9fc0149c:	3290ffff 	andi	s0,s4,0xffff
9fc014a0:	a7a00020 	sh	zero,32(sp)
9fc014a4:	a7a00022 	sh	zero,34(sp)
9fc014a8:	8fa30020 	lw	v1,32(sp)
9fc014ac:	24070001 	li	a3,1
9fc014b0:	1467fd24 	bne	v1,a3,9fc00944 <core_mark+0xb4>
9fc014b4:	a7a60024 	sh	a2,36(sp)
9fc014b8:	87a80024 	lh	t0,36(sp)
9fc014bc:	00000000 	nop
9fc014c0:	1500fd20 	bnez	t0,9fc00944 <core_mark+0xb4>
9fc014c4:	24093415 	li	t1,13333
9fc014c8:	240a0066 	li	t2,102
9fc014cc:	a7a90022 	sh	t1,34(sp)
9fc014d0:	a7aa0024 	sh	t2,36(sp)
9fc014d4:	0bf00251 	j	9fc00944 <core_mark+0xb4>
9fc014d8:	a7a90020 	sh	t1,32(sp)
9fc014dc:	0ff0284a 	jal	9fc0a128 <puts>
9fc014e0:	2444a518 	addiu	a0,v0,-23272
9fc014e4:	0bf004cc 	j	9fc01330 <core_mark+0xaa0>
9fc014e8:	24050003 	li	a1,3
9fc014ec:	00000000 	nop
9fc014f0:	8f868010 	lw	a2,-32752(gp)
9fc014f4:	00000000 	nop
9fc014f8:	10c0fe8b 	beqz	a2,9fc00f28 <core_mark+0x698>
9fc014fc:	3c0c9fc1 	lui	t4,0x9fc1
9fc01500:	2592a818 	addiu	s2,t4,-22504
9fc01504:	00008021 	move	s0,zero
9fc01508:	27b10020 	addiu	s1,sp,32
9fc0150c:	00101980 	sll	v1,s0,0x6
9fc01510:	00101080 	sll	v0,s0,0x2
9fc01514:	0043c821 	addu	t9,v0,v1
9fc01518:	0239c021 	addu	t8,s1,t9
9fc0151c:	9706003a 	lhu	a2,58(t8)
9fc01520:	02002821 	move	a1,s0
9fc01524:	0ff02784 	jal	9fc09e10 <printf>
9fc01528:	02402021 	move	a0,s2
9fc0152c:	26080001 	addiu	t0,s0,1
9fc01530:	3110ffff 	andi	s0,t0,0xffff
9fc01534:	8f868010 	lw	a2,-32752(gp)
9fc01538:	00107080 	sll	t6,s0,0x2
9fc0153c:	00107980 	sll	t7,s0,0x6
9fc01540:	01cf2821 	addu	a1,t6,t7
9fc01544:	0206682b 	sltu	t5,s0,a2
9fc01548:	02251821 	addu	v1,s1,a1
9fc0154c:	02402021 	move	a0,s2
9fc01550:	11a00026 	beqz	t5,9fc015ec <core_mark+0xd5c>
9fc01554:	02002821 	move	a1,s0
9fc01558:	9466003a 	lhu	a2,58(v1)
9fc0155c:	0ff02784 	jal	9fc09e10 <printf>
9fc01560:	00000000 	nop
9fc01564:	26050001 	addiu	a1,s0,1
9fc01568:	30b0ffff 	andi	s0,a1,0xffff
9fc0156c:	8f868010 	lw	a2,-32752(gp)
9fc01570:	00106080 	sll	t4,s0,0x2
9fc01574:	00106980 	sll	t5,s0,0x6
9fc01578:	018d5821 	addu	t3,t4,t5
9fc0157c:	0206502b 	sltu	t2,s0,a2
9fc01580:	02402021 	move	a0,s2
9fc01584:	022b1821 	addu	v1,s1,t3
9fc01588:	11400018 	beqz	t2,9fc015ec <core_mark+0xd5c>
9fc0158c:	02002821 	move	a1,s0
9fc01590:	9466003a 	lhu	a2,58(v1)
9fc01594:	0ff02784 	jal	9fc09e10 <printf>
9fc01598:	00000000 	nop
9fc0159c:	26190001 	addiu	t9,s0,1
9fc015a0:	3330ffff 	andi	s0,t9,0xffff
9fc015a4:	8f868010 	lw	a2,-32752(gp)
9fc015a8:	00104080 	sll	t0,s0,0x2
9fc015ac:	0010c180 	sll	t8,s0,0x6
9fc015b0:	01187821 	addu	t7,t0,t8
9fc015b4:	0206702b 	sltu	t6,s0,a2
9fc015b8:	02402021 	move	a0,s2
9fc015bc:	022f1821 	addu	v1,s1,t7
9fc015c0:	11c0000a 	beqz	t6,9fc015ec <core_mark+0xd5c>
9fc015c4:	02002821 	move	a1,s0
9fc015c8:	9466003a 	lhu	a2,58(v1)
9fc015cc:	0ff02784 	jal	9fc09e10 <printf>
9fc015d0:	00000000 	nop
9fc015d4:	26020001 	addiu	v0,s0,1
9fc015d8:	8f868010 	lw	a2,-32752(gp)
9fc015dc:	3050ffff 	andi	s0,v0,0xffff
9fc015e0:	0206202b 	sltu	a0,s0,a2
9fc015e4:	1480ffc9 	bnez	a0,9fc0150c <core_mark+0xc7c>
9fc015e8:	00000000 	nop
9fc015ec:	8fa30040 	lw	v1,64(sp)
9fc015f0:	0bf003cb 	j	9fc00f2c <core_mark+0x69c>
9fc015f4:	30740002 	andi	s4,v1,0x2
	...
9fc01600:	0ff0284a 	jal	9fc0a128 <puts>
9fc01604:	2604a888 	addiu	a0,s0,-22392
9fc01608:	0bf0048e 	j	9fc01238 <core_mark+0x9a8>
9fc0160c:	00000000 	nop
9fc01610:	8fbf003c 	lw	ra,60(sp)
9fc01614:	8f928010 	lw	s2,-32752(gp)
9fc01618:	00002021 	move	a0,zero
9fc0161c:	025f0018 	mult	s2,ra
9fc01620:	0000a012 	mflo	s4
9fc01624:	0ff01648 	jal	9fc05920 <time_in_secs>
9fc01628:	3c109fc1 	lui	s0,0x9fc1
9fc0162c:	2604a728 	addiu	a0,s0,-22744
9fc01630:	14400002 	bnez	v0,9fc0163c <core_mark+0xdac>
9fc01634:	0282001b 	divu	zero,s4,v0
9fc01638:	0007000d 	break	0x7
9fc0163c:	00001012 	mflo	v0
9fc01640:	0ff02784 	jal	9fc09e10 <printf>
9fc01644:	00402821 	move	a1,v0
9fc01648:	0bf003aa 	j	9fc00ea8 <core_mark+0x618>
9fc0164c:	00000000 	nop
9fc01650:	3c1e9fc1 	lui	s8,0x9fc1
9fc01654:	0ff0284a 	jal	9fc0a128 <puts>
9fc01658:	27c4a8e4 	addiu	a0,s8,-22300
9fc0165c:	0bf0048e 	j	9fc01238 <core_mark+0x9a8>
9fc01660:	00000000 	nop
9fc01664:	3c109fc1 	lui	s0,0x9fc1
9fc01668:	0ff0284a 	jal	9fc0a128 <puts>
9fc0166c:	2604a490 	addiu	a0,s0,-23408
9fc01670:	0bf004cc 	j	9fc01330 <core_mark+0xaa0>
9fc01674:	00002821 	move	a1,zero
	...
9fc01680:	3c129fc1 	lui	s2,0x9fc1
9fc01684:	0ff0284a 	jal	9fc0a128 <puts>
9fc01688:	2644a4bc 	addiu	a0,s2,-23364
9fc0168c:	0bf004cc 	j	9fc01330 <core_mark+0xaa0>
9fc01690:	24050001 	li	a1,1
9fc01694:	3c049fc1 	lui	a0,0x9fc1
9fc01698:	0ff0284a 	jal	9fc0a128 <puts>
9fc0169c:	2484a544 	addiu	a0,a0,-23228
9fc016a0:	0bf004cc 	j	9fc01330 <core_mark+0xaa0>
9fc016a4:	24050004 	li	a1,4
9fc016a8:	0bf00372 	j	9fc00dc8 <core_mark+0x538>
9fc016ac:	00008021 	move	s0,zero

9fc016b0 <core_init_matrix>:
core_init_matrix():
9fc016b0:	27bdffe8 	addiu	sp,sp,-24
9fc016b4:	afb50014 	sw	s5,20(sp)
9fc016b8:	afb40010 	sw	s4,16(sp)
9fc016bc:	afb3000c 	sw	s3,12(sp)
9fc016c0:	afb20008 	sw	s2,8(sp)
9fc016c4:	afb10004 	sw	s1,4(sp)
9fc016c8:	afb00000 	sw	s0,0(sp)
9fc016cc:	00c06821 	move	t5,a2
9fc016d0:	14c00002 	bnez	a2,9fc016dc <core_init_matrix+0x2c>
9fc016d4:	00e0a821 	move	s5,a3
9fc016d8:	240d0001 	li	t5,1
9fc016dc:	108000e3 	beqz	a0,9fc01a6c <core_init_matrix+0x3bc>
9fc016e0:	00004821 	move	t1,zero
9fc016e4:	25260001 	addiu	a2,t1,1
9fc016e8:	00c60018 	mult	a2,a2
9fc016ec:	24cf0007 	addiu	t7,a2,7
9fc016f0:	24ca0003 	addiu	t2,a2,3
9fc016f4:	24cb0004 	addiu	t3,a2,4
9fc016f8:	24cc0005 	addiu	t4,a2,5
9fc016fc:	00003812 	mflo	a3
9fc01700:	000718c0 	sll	v1,a3,0x3
9fc01704:	0064102b 	sltu	v0,v1,a0
9fc01708:	10400039 	beqz	v0,9fc017f0 <core_init_matrix+0x140>
9fc0170c:	24ce0006 	addiu	t6,a2,6
9fc01710:	24c80001 	addiu	t0,a2,1
9fc01714:	01080018 	mult	t0,t0
9fc01718:	00009012 	mflo	s2
9fc0171c:	001288c0 	sll	s1,s2,0x3
9fc01720:	0224802b 	sltu	s0,s1,a0
9fc01724:	12000032 	beqz	s0,9fc017f0 <core_init_matrix+0x140>
9fc01728:	00c04821 	move	t1,a2
9fc0172c:	25190001 	addiu	t9,t0,1
9fc01730:	03390018 	mult	t9,t9
9fc01734:	00009812 	mflo	s3
9fc01738:	001390c0 	sll	s2,s3,0x3
9fc0173c:	0244882b 	sltu	s1,s2,a0
9fc01740:	1220002b 	beqz	s1,9fc017f0 <core_init_matrix+0x140>
9fc01744:	01004821 	move	t1,t0
9fc01748:	014a0018 	mult	t2,t2
9fc0174c:	00003812 	mflo	a3
9fc01750:	0007c0c0 	sll	t8,a3,0x3
9fc01754:	0304a02b 	sltu	s4,t8,a0
9fc01758:	12800025 	beqz	s4,9fc017f0 <core_init_matrix+0x140>
9fc0175c:	24c90002 	addiu	t1,a2,2
9fc01760:	016b0018 	mult	t3,t3
9fc01764:	01404821 	move	t1,t2
9fc01768:	00001812 	mflo	v1
9fc0176c:	000380c0 	sll	s0,v1,0x3
9fc01770:	0204502b 	sltu	t2,s0,a0
9fc01774:	1140001e 	beqz	t2,9fc017f0 <core_init_matrix+0x140>
9fc01778:	018c0018 	mult	t4,t4
9fc0177c:	01604821 	move	t1,t3
9fc01780:	00003012 	mflo	a2
9fc01784:	000610c0 	sll	v0,a2,0x3
9fc01788:	0044582b 	sltu	t3,v0,a0
9fc0178c:	11600018 	beqz	t3,9fc017f0 <core_init_matrix+0x140>
9fc01790:	01ce0018 	mult	t6,t6
9fc01794:	01804821 	move	t1,t4
9fc01798:	00008812 	mflo	s1
9fc0179c:	001140c0 	sll	t0,s1,0x3
9fc017a0:	0104602b 	sltu	t4,t0,a0
9fc017a4:	11800012 	beqz	t4,9fc017f0 <core_init_matrix+0x140>
9fc017a8:	01ef0018 	mult	t7,t7
9fc017ac:	01c04821 	move	t1,t6
9fc017b0:	00009812 	mflo	s3
9fc017b4:	001390c0 	sll	s2,s3,0x3
9fc017b8:	0244702b 	sltu	t6,s2,a0
9fc017bc:	11c0000c 	beqz	t6,9fc017f0 <core_init_matrix+0x140>
9fc017c0:	25e60001 	addiu	a2,t7,1
9fc017c4:	01e04821 	move	t1,t7
9fc017c8:	00c60018 	mult	a2,a2
9fc017cc:	24cf0007 	addiu	t7,a2,7
9fc017d0:	24ca0003 	addiu	t2,a2,3
9fc017d4:	24cb0004 	addiu	t3,a2,4
9fc017d8:	24cc0005 	addiu	t4,a2,5
9fc017dc:	00003812 	mflo	a3
9fc017e0:	000718c0 	sll	v1,a3,0x3
9fc017e4:	0064102b 	sltu	v0,v1,a0
9fc017e8:	1440ffc9 	bnez	v0,9fc01710 <core_init_matrix+0x60>
9fc017ec:	24ce0006 	addiu	t6,a2,6
9fc017f0:	01290018 	mult	t1,t1
9fc017f4:	24a8ffff 	addiu	t0,a1,-1
9fc017f8:	2404fffc 	li	a0,-4
9fc017fc:	01042824 	and	a1,t0,a0
9fc01800:	24b10004 	addiu	s1,a1,4
9fc01804:	00003012 	mflo	a2
9fc01808:	0006a040 	sll	s4,a2,0x1
9fc0180c:	1120009f 	beqz	t1,9fc01a8c <core_init_matrix+0x3dc>
9fc01810:	02349021 	addu	s2,s1,s4
9fc01814:	0120c021 	move	t8,t1
9fc01818:	3c108000 	lui	s0,0x8000
9fc0181c:	3619ffff 	ori	t9,s0,0xffff
9fc01820:	00189840 	sll	s3,t8,0x1
9fc01824:	24080001 	li	t0,1
9fc01828:	00003821 	move	a3,zero
9fc0182c:	00002821 	move	a1,zero
9fc01830:	3c10ffff 	lui	s0,0xffff
9fc01834:	01a80018 	mult	t5,t0
9fc01838:	270affff 	addiu	t2,t8,-1
9fc0183c:	314f0001 	andi	t7,t2,0x1
9fc01840:	02457021 	addu	t6,s2,a1
9fc01844:	00004812 	mflo	t1
9fc01848:	01393024 	and	a2,t1,t9
9fc0184c:	04c00080 	bltz	a2,9fc01a50 <core_init_matrix+0x3a0>
9fc01850:	02255021 	addu	t2,s1,a1
9fc01854:	310bffff 	andi	t3,t0,0xffff
9fc01858:	01666021 	addu	t4,t3,a2
9fc0185c:	3183ffff 	andi	v1,t4,0xffff
9fc01860:	006b1021 	addu	v0,v1,t3
9fc01864:	24090001 	li	t1,1
9fc01868:	304d00ff 	andi	t5,v0,0xff
9fc0186c:	0138202b 	sltu	a0,t1,t8
9fc01870:	a5c30000 	sh	v1,0(t6)
9fc01874:	25080001 	addiu	t0,t0,1
9fc01878:	a54d0000 	sh	t5,0(t2)
9fc0187c:	25cc0002 	addiu	t4,t6,2
9fc01880:	00c06821 	move	t5,a2
9fc01884:	1080005b 	beqz	a0,9fc019f4 <core_init_matrix+0x344>
9fc01888:	254b0002 	addiu	t3,t2,2
9fc0188c:	11e00032 	beqz	t7,9fc01958 <core_init_matrix+0x2a8>
9fc01890:	01a80018 	mult	t5,t0
9fc01894:	00c80018 	mult	a2,t0
9fc01898:	00007812 	mflo	t7
9fc0189c:	01f93024 	and	a2,t7,t9
9fc018a0:	04c0006f 	bltz	a2,9fc01a60 <core_init_matrix+0x3b0>
9fc018a4:	24cdffff 	addiu	t5,a2,-1
9fc018a8:	3104ffff 	andi	a0,t0,0xffff
9fc018ac:	00c06821 	move	t5,a2
9fc018b0:	00863021 	addu	a2,a0,a2
9fc018b4:	30c3ffff 	andi	v1,a2,0xffff
9fc018b8:	00647821 	addu	t7,v1,a0
9fc018bc:	25290001 	addiu	t1,t1,1
9fc018c0:	31e200ff 	andi	v0,t7,0xff
9fc018c4:	0138302b 	sltu	a2,t1,t8
9fc018c8:	a5c30002 	sh	v1,2(t6)
9fc018cc:	25080001 	addiu	t0,t0,1
9fc018d0:	a5420002 	sh	v0,2(t2)
9fc018d4:	258c0002 	addiu	t4,t4,2
9fc018d8:	14c0001e 	bnez	a2,9fc01954 <core_init_matrix+0x2a4>
9fc018dc:	256b0002 	addiu	t3,t3,2
9fc018e0:	0bf0067e 	j	9fc019f8 <core_init_matrix+0x348>
9fc018e4:	24e70001 	addiu	a3,a3,1
	...
9fc018f0:	004a0018 	mult	v0,t2
9fc018f4:	01021821 	addu	v1,t0,v0
9fc018f8:	306dffff 	andi	t5,v1,0xffff
9fc018fc:	01a81021 	addu	v0,t5,t0
9fc01900:	304600ff 	andi	a2,v0,0xff
9fc01904:	a58d0000 	sh	t5,0(t4)
9fc01908:	a5660000 	sh	a2,0(t3)
9fc0190c:	00002012 	mflo	a0
9fc01910:	00993024 	and	a2,a0,t9
9fc01914:	04c00027 	bltz	a2,9fc019b4 <core_init_matrix+0x304>
9fc01918:	24cbffff 	addiu	t3,a2,-1
9fc0191c:	00c06821 	move	t5,a2
9fc01920:	3146ffff 	andi	a2,t2,0xffff
9fc01924:	00cd4021 	addu	t0,a2,t5
9fc01928:	3103ffff 	andi	v1,t0,0xffff
9fc0192c:	00661021 	addu	v0,v1,a2
9fc01930:	25290001 	addiu	t1,t1,1
9fc01934:	304c00ff 	andi	t4,v0,0xff
9fc01938:	0138202b 	sltu	a0,t1,t8
9fc0193c:	a5c30000 	sh	v1,0(t6)
9fc01940:	25480001 	addiu	t0,t2,1
9fc01944:	a5ec0000 	sh	t4,0(t7)
9fc01948:	25eb0002 	addiu	t3,t7,2
9fc0194c:	10800029 	beqz	a0,9fc019f4 <core_init_matrix+0x344>
9fc01950:	25cc0002 	addiu	t4,t6,2
9fc01954:	01a80018 	mult	t5,t0
9fc01958:	250a0001 	addiu	t2,t0,1
9fc0195c:	258e0002 	addiu	t6,t4,2
9fc01960:	3108ffff 	andi	t0,t0,0xffff
9fc01964:	256f0002 	addiu	t7,t3,2
9fc01968:	00006812 	mflo	t5
9fc0196c:	01b91024 	and	v0,t5,t9
9fc01970:	0441ffdf 	bgez	v0,9fc018f0 <core_init_matrix+0x240>
9fc01974:	25290001 	addiu	t1,t1,1
9fc01978:	2443ffff 	addiu	v1,v0,-1
9fc0197c:	00701025 	or	v0,v1,s0
9fc01980:	24420001 	addiu	v0,v0,1
9fc01984:	004a0018 	mult	v0,t2
9fc01988:	01021821 	addu	v1,t0,v0
9fc0198c:	306dffff 	andi	t5,v1,0xffff
9fc01990:	01a81021 	addu	v0,t5,t0
9fc01994:	304600ff 	andi	a2,v0,0xff
9fc01998:	a58d0000 	sh	t5,0(t4)
9fc0199c:	a5660000 	sh	a2,0(t3)
9fc019a0:	00002012 	mflo	a0
9fc019a4:	00993024 	and	a2,a0,t9
9fc019a8:	04c1ffdd 	bgez	a2,9fc01920 <core_init_matrix+0x270>
9fc019ac:	00c06821 	move	t5,a2
9fc019b0:	24cbffff 	addiu	t3,a2,-1
9fc019b4:	01704025 	or	t0,t3,s0
9fc019b8:	25060001 	addiu	a2,t0,1
9fc019bc:	00c06821 	move	t5,a2
9fc019c0:	3146ffff 	andi	a2,t2,0xffff
9fc019c4:	00cd4021 	addu	t0,a2,t5
9fc019c8:	3103ffff 	andi	v1,t0,0xffff
9fc019cc:	00661021 	addu	v0,v1,a2
9fc019d0:	25290001 	addiu	t1,t1,1
9fc019d4:	304c00ff 	andi	t4,v0,0xff
9fc019d8:	0138202b 	sltu	a0,t1,t8
9fc019dc:	a5c30000 	sh	v1,0(t6)
9fc019e0:	25480001 	addiu	t0,t2,1
9fc019e4:	a5ec0000 	sh	t4,0(t7)
9fc019e8:	25eb0002 	addiu	t3,t7,2
9fc019ec:	1480ffd9 	bnez	a0,9fc01954 <core_init_matrix+0x2a4>
9fc019f0:	25cc0002 	addiu	t4,t6,2
9fc019f4:	24e70001 	addiu	a3,a3,1
9fc019f8:	00f8582b 	sltu	t3,a3,t8
9fc019fc:	1560ff8d 	bnez	t3,9fc01834 <core_init_matrix+0x184>
9fc01a00:	00b32821 	addu	a1,a1,s3
9fc01a04:	2699ffff 	addiu	t9,s4,-1
9fc01a08:	02597821 	addu	t7,s2,t9
9fc01a0c:	2413fffc 	li	s3,-4
9fc01a10:	01f37024 	and	t6,t7,s3
9fc01a14:	25cc0004 	addiu	t4,t6,4
9fc01a18:	aeb10004 	sw	s1,4(s5)
9fc01a1c:	aeb20008 	sw	s2,8(s5)
9fc01a20:	aeac000c 	sw	t4,12(s5)
9fc01a24:	aeb80000 	sw	t8,0(s5)
9fc01a28:	03001021 	move	v0,t8
9fc01a2c:	8fb50014 	lw	s5,20(sp)
9fc01a30:	8fb40010 	lw	s4,16(sp)
9fc01a34:	8fb3000c 	lw	s3,12(sp)
9fc01a38:	8fb20008 	lw	s2,8(sp)
9fc01a3c:	8fb10004 	lw	s1,4(sp)
9fc01a40:	8fb00000 	lw	s0,0(sp)
9fc01a44:	03e00008 	jr	ra
9fc01a48:	27bd0018 	addiu	sp,sp,24
9fc01a4c:	00000000 	nop
9fc01a50:	24c4ffff 	addiu	a0,a2,-1
9fc01a54:	00904825 	or	t1,a0,s0
9fc01a58:	0bf00615 	j	9fc01854 <core_init_matrix+0x1a4>
9fc01a5c:	25260001 	addiu	a2,t1,1
9fc01a60:	01b02025 	or	a0,t5,s0
9fc01a64:	0bf0062a 	j	9fc018a8 <core_init_matrix+0x1f8>
9fc01a68:	24860001 	addiu	a2,a0,1
9fc01a6c:	24b8ffff 	addiu	t8,a1,-1
9fc01a70:	2407fffc 	li	a3,-4
9fc01a74:	0307a024 	and	s4,t8,a3
9fc01a78:	26920006 	addiu	s2,s4,6
9fc01a7c:	26910004 	addiu	s1,s4,4
9fc01a80:	2418ffff 	li	t8,-1
9fc01a84:	0bf00606 	j	9fc01818 <core_init_matrix+0x168>
9fc01a88:	24140002 	li	s4,2
9fc01a8c:	0bf00681 	j	9fc01a04 <core_init_matrix+0x354>
9fc01a90:	0000c021 	move	t8,zero
	...

9fc01aa0 <matrix_sum>:
matrix_sum():
9fc01aa0:	00063400 	sll	a2,a2,0x10
9fc01aa4:	00807021 	move	t6,a0
9fc01aa8:	108000c7 	beqz	a0,9fc01dc8 <matrix_sum+0x328>
9fc01aac:	00063403 	sra	a2,a2,0x10
9fc01ab0:	0004c080 	sll	t8,a0,0x2
9fc01ab4:	00004021 	move	t0,zero
9fc01ab8:	00001821 	move	v1,zero
9fc01abc:	00004821 	move	t1,zero
9fc01ac0:	00007821 	move	t7,zero
9fc01ac4:	8cac0000 	lw	t4,0(a1)
9fc01ac8:	3122ffff 	andi	v0,t1,0xffff
9fc01acc:	010c5021 	addu	t2,t0,t4
9fc01ad0:	006c402a 	slt	t0,v1,t4
9fc01ad4:	00485821 	addu	t3,v0,t0
9fc01ad8:	2447000a 	addiu	a3,v0,10
9fc01adc:	25cdffff 	addiu	t5,t6,-1
9fc01ae0:	0007cc00 	sll	t9,a3,0x10
9fc01ae4:	000b4c00 	sll	t1,t3,0x10
9fc01ae8:	00ca402a 	slt	t0,a2,t2
9fc01aec:	31a40003 	andi	a0,t5,0x3
9fc01af0:	00193c03 	sra	a3,t9,0x10
9fc01af4:	150000a6 	bnez	t0,9fc01d90 <matrix_sum+0x2f0>
9fc01af8:	00091403 	sra	v0,t1,0x10
9fc01afc:	00404821 	move	t1,v0
9fc01b00:	15000002 	bnez	t0,9fc01b0c <matrix_sum+0x6c>
9fc01b04:	00004021 	move	t0,zero
9fc01b08:	01404021 	move	t0,t2
9fc01b0c:	240b0001 	li	t3,1
9fc01b10:	016ec82b 	sltu	t9,t3,t6
9fc01b14:	24aa0004 	addiu	t2,a1,4
9fc01b18:	13200087 	beqz	t9,9fc01d38 <matrix_sum+0x298>
9fc01b1c:	01801821 	move	v1,t4
9fc01b20:	1080003f 	beqz	a0,9fc01c20 <matrix_sum+0x180>
9fc01b24:	00000000 	nop
9fc01b28:	108b0028 	beq	a0,t3,9fc01bcc <matrix_sum+0x12c>
9fc01b2c:	24070002 	li	a3,2
9fc01b30:	10870013 	beq	a0,a3,9fc01b80 <matrix_sum+0xe0>
9fc01b34:	3139ffff 	andi	t9,t1,0xffff
9fc01b38:	8d440000 	lw	a0,0(t2)
9fc01b3c:	00000000 	nop
9fc01b40:	01046821 	addu	t5,t0,a0
9fc01b44:	0184402a 	slt	t0,t4,a0
9fc01b48:	03281821 	addu	v1,t9,t0
9fc01b4c:	2722000a 	addiu	v0,t9,10
9fc01b50:	00024c00 	sll	t1,v0,0x10
9fc01b54:	00036400 	sll	t4,v1,0x10
9fc01b58:	00cd402a 	slt	t0,a2,t5
9fc01b5c:	00093c03 	sra	a3,t1,0x10
9fc01b60:	1500008f 	bnez	t0,9fc01da0 <matrix_sum+0x300>
9fc01b64:	000c1403 	sra	v0,t4,0x10
9fc01b68:	1500008f 	bnez	t0,9fc01da8 <matrix_sum+0x308>
9fc01b6c:	00404821 	move	t1,v0
9fc01b70:	01a04021 	move	t0,t5
9fc01b74:	256b0001 	addiu	t3,t3,1
9fc01b78:	254a0004 	addiu	t2,t2,4
9fc01b7c:	00801821 	move	v1,a0
9fc01b80:	8d440000 	lw	a0,0(t2)
9fc01b84:	3122ffff 	andi	v0,t1,0xffff
9fc01b88:	0064c82a 	slt	t9,v1,a0
9fc01b8c:	00591821 	addu	v1,v0,t9
9fc01b90:	2449000a 	addiu	t1,v0,10
9fc01b94:	01046021 	addu	t4,t0,a0
9fc01b98:	00093c00 	sll	a3,t1,0x10
9fc01b9c:	00036c00 	sll	t5,v1,0x10
9fc01ba0:	00cc402a 	slt	t0,a2,t4
9fc01ba4:	00073c03 	sra	a3,a3,0x10
9fc01ba8:	15000085 	bnez	t0,9fc01dc0 <matrix_sum+0x320>
9fc01bac:	000d1403 	sra	v0,t5,0x10
9fc01bb0:	00404821 	move	t1,v0
9fc01bb4:	15000002 	bnez	t0,9fc01bc0 <matrix_sum+0x120>
9fc01bb8:	00004021 	move	t0,zero
9fc01bbc:	01804021 	move	t0,t4
9fc01bc0:	256b0001 	addiu	t3,t3,1
9fc01bc4:	254a0004 	addiu	t2,t2,4
9fc01bc8:	00801821 	move	v1,a0
9fc01bcc:	8d440000 	lw	a0,0(t2)
9fc01bd0:	3122ffff 	andi	v0,t1,0xffff
9fc01bd4:	01046021 	addu	t4,t0,a0
9fc01bd8:	0064402a 	slt	t0,v1,a0
9fc01bdc:	00481821 	addu	v1,v0,t0
9fc01be0:	2449000a 	addiu	t1,v0,10
9fc01be4:	00093c00 	sll	a3,t1,0x10
9fc01be8:	00036c00 	sll	t5,v1,0x10
9fc01bec:	00cc402a 	slt	t0,a2,t4
9fc01bf0:	00073c03 	sra	a3,a3,0x10
9fc01bf4:	1500006e 	bnez	t0,9fc01db0 <matrix_sum+0x310>
9fc01bf8:	000d1403 	sra	v0,t5,0x10
9fc01bfc:	00404821 	move	t1,v0
9fc01c00:	15000002 	bnez	t0,9fc01c0c <matrix_sum+0x16c>
9fc01c04:	00004021 	move	t0,zero
9fc01c08:	01804021 	move	t0,t4
9fc01c0c:	256b0001 	addiu	t3,t3,1
9fc01c10:	016e602b 	sltu	t4,t3,t6
9fc01c14:	254a0004 	addiu	t2,t2,4
9fc01c18:	11800047 	beqz	t4,9fc01d38 <matrix_sum+0x298>
9fc01c1c:	00801821 	move	v1,a0
9fc01c20:	8d440000 	lw	a0,0(t2)
9fc01c24:	312dffff 	andi	t5,t1,0xffff
9fc01c28:	01044821 	addu	t1,t0,a0
9fc01c2c:	0064402a 	slt	t0,v1,a0
9fc01c30:	01a81821 	addu	v1,t5,t0
9fc01c34:	25ac000a 	addiu	t4,t5,10
9fc01c38:	000c3c00 	sll	a3,t4,0x10
9fc01c3c:	00031400 	sll	v0,v1,0x10
9fc01c40:	00c9402a 	slt	t0,a2,t1
9fc01c44:	00073c03 	sra	a3,a3,0x10
9fc01c48:	11000002 	beqz	t0,9fc01c54 <matrix_sum+0x1b4>
9fc01c4c:	00021403 	sra	v0,v0,0x10
9fc01c50:	00e01021 	move	v0,a3
9fc01c54:	11000002 	beqz	t0,9fc01c60 <matrix_sum+0x1c0>
9fc01c58:	01203821 	move	a3,t1
9fc01c5c:	00003821 	move	a3,zero
9fc01c60:	8d480004 	lw	t0,4(t2)
9fc01c64:	304cffff 	andi	t4,v0,0xffff
9fc01c68:	0088682a 	slt	t5,a0,t0
9fc01c6c:	2582000a 	addiu	v0,t4,10
9fc01c70:	00e84821 	addu	t1,a3,t0
9fc01c74:	018d1821 	addu	v1,t4,t5
9fc01c78:	00023c00 	sll	a3,v0,0x10
9fc01c7c:	0003cc00 	sll	t9,v1,0x10
9fc01c80:	00c9202a 	slt	a0,a2,t1
9fc01c84:	256b0001 	addiu	t3,t3,1
9fc01c88:	00073c03 	sra	a3,a3,0x10
9fc01c8c:	00191403 	sra	v0,t9,0x10
9fc01c90:	10800002 	beqz	a0,9fc01c9c <matrix_sum+0x1fc>
9fc01c94:	254c0004 	addiu	t4,t2,4
9fc01c98:	00e01021 	move	v0,a3
9fc01c9c:	14800002 	bnez	a0,9fc01ca8 <matrix_sum+0x208>
9fc01ca0:	00003821 	move	a3,zero
9fc01ca4:	01203821 	move	a3,t1
9fc01ca8:	8d840004 	lw	a0,4(t4)
9fc01cac:	3042ffff 	andi	v0,v0,0xffff
9fc01cb0:	0104182a 	slt	v1,t0,a0
9fc01cb4:	00e44821 	addu	t1,a3,a0
9fc01cb8:	0043c821 	addu	t9,v0,v1
9fc01cbc:	2447000a 	addiu	a3,v0,10
9fc01cc0:	00076c00 	sll	t5,a3,0x10
9fc01cc4:	00195400 	sll	t2,t9,0x10
9fc01cc8:	00c9402a 	slt	t0,a2,t1
9fc01ccc:	000d3c03 	sra	a3,t5,0x10
9fc01cd0:	11000002 	beqz	t0,9fc01cdc <matrix_sum+0x23c>
9fc01cd4:	000a1403 	sra	v0,t2,0x10
9fc01cd8:	00e01021 	move	v0,a3
9fc01cdc:	1500001c 	bnez	t0,9fc01d50 <matrix_sum+0x2b0>
9fc01ce0:	00004021 	move	t0,zero
9fc01ce4:	8d870008 	lw	a3,8(t4)
9fc01ce8:	01204021 	move	t0,t1
9fc01cec:	3042ffff 	andi	v0,v0,0xffff
9fc01cf0:	01075021 	addu	t2,t0,a3
9fc01cf4:	0087402a 	slt	t0,a0,a3
9fc01cf8:	0048c821 	addu	t9,v0,t0
9fc01cfc:	244d000a 	addiu	t5,v0,10
9fc01d00:	000d2400 	sll	a0,t5,0x10
9fc01d04:	00194c00 	sll	t1,t9,0x10
9fc01d08:	00ca402a 	slt	t0,a2,t2
9fc01d0c:	00042403 	sra	a0,a0,0x10
9fc01d10:	1500001b 	bnez	t0,9fc01d80 <matrix_sum+0x2e0>
9fc01d14:	00091403 	sra	v0,t1,0x10
9fc01d18:	1500001b 	bnez	t0,9fc01d88 <matrix_sum+0x2e8>
9fc01d1c:	00404821 	move	t1,v0
9fc01d20:	01404021 	move	t0,t2
9fc01d24:	256b0003 	addiu	t3,t3,3
9fc01d28:	016ec82b 	sltu	t9,t3,t6
9fc01d2c:	258a000c 	addiu	t2,t4,12
9fc01d30:	1720ffbb 	bnez	t9,9fc01c20 <matrix_sum+0x180>
9fc01d34:	00e01821 	move	v1,a3
9fc01d38:	25ef0001 	addiu	t7,t7,1
9fc01d3c:	01ee202b 	sltu	a0,t7,t6
9fc01d40:	1480ff60 	bnez	a0,9fc01ac4 <matrix_sum+0x24>
9fc01d44:	00b82821 	addu	a1,a1,t8
9fc01d48:	03e00008 	jr	ra
9fc01d4c:	01201021 	move	v0,t1
9fc01d50:	8d870008 	lw	a3,8(t4)
9fc01d54:	3042ffff 	andi	v0,v0,0xffff
9fc01d58:	01075021 	addu	t2,t0,a3
9fc01d5c:	0087402a 	slt	t0,a0,a3
9fc01d60:	0048c821 	addu	t9,v0,t0
9fc01d64:	244d000a 	addiu	t5,v0,10
9fc01d68:	000d2400 	sll	a0,t5,0x10
9fc01d6c:	00194c00 	sll	t1,t9,0x10
9fc01d70:	00ca402a 	slt	t0,a2,t2
9fc01d74:	00042403 	sra	a0,a0,0x10
9fc01d78:	1100ffe7 	beqz	t0,9fc01d18 <matrix_sum+0x278>
9fc01d7c:	00091403 	sra	v0,t1,0x10
9fc01d80:	1100ffe7 	beqz	t0,9fc01d20 <matrix_sum+0x280>
9fc01d84:	00804821 	move	t1,a0
9fc01d88:	0bf00749 	j	9fc01d24 <matrix_sum+0x284>
9fc01d8c:	00004021 	move	t0,zero
9fc01d90:	0bf006c0 	j	9fc01b00 <matrix_sum+0x60>
9fc01d94:	00e04821 	move	t1,a3
	...
9fc01da0:	1100ff73 	beqz	t0,9fc01b70 <matrix_sum+0xd0>
9fc01da4:	00e04821 	move	t1,a3
9fc01da8:	0bf006dd 	j	9fc01b74 <matrix_sum+0xd4>
9fc01dac:	00004021 	move	t0,zero
9fc01db0:	0bf00700 	j	9fc01c00 <matrix_sum+0x160>
9fc01db4:	00e04821 	move	t1,a3
	...
9fc01dc0:	0bf006ed 	j	9fc01bb4 <matrix_sum+0x114>
9fc01dc4:	00e04821 	move	t1,a3
9fc01dc8:	00004821 	move	t1,zero
9fc01dcc:	03e00008 	jr	ra
9fc01dd0:	01201021 	move	v0,t1
	...

9fc01de0 <matrix_mul_const>:
matrix_mul_const():
9fc01de0:	27bdfff0 	addiu	sp,sp,-16
9fc01de4:	00077400 	sll	t6,a3,0x10
9fc01de8:	afb2000c 	sw	s2,12(sp)
9fc01dec:	afb10008 	sw	s1,8(sp)
9fc01df0:	afb00004 	sw	s0,4(sp)
9fc01df4:	0080c821 	move	t9,a0
9fc01df8:	10800083 	beqz	a0,9fc02008 <matrix_mul_const+0x228>
9fc01dfc:	000e7403 	sra	t6,t6,0x10
9fc01e00:	00a0c021 	move	t8,a1
9fc01e04:	00c07821 	move	t7,a2
9fc01e08:	00049080 	sll	s2,a0,0x2
9fc01e0c:	00048840 	sll	s1,a0,0x1
9fc01e10:	00008021 	move	s0,zero
9fc01e14:	85e70000 	lh	a3,0(t7)
9fc01e18:	240d0001 	li	t5,1
9fc01e1c:	01c70018 	mult	t6,a3
9fc01e20:	272cffff 	addiu	t4,t9,-1
9fc01e24:	01b9482b 	sltu	t1,t5,t9
9fc01e28:	31830007 	andi	v1,t4,0x7
9fc01e2c:	25eb0002 	addiu	t3,t7,2
9fc01e30:	270c0004 	addiu	t4,t8,4
9fc01e34:	00004012 	mflo	t0
9fc01e38:	1120006e 	beqz	t1,9fc01ff4 <matrix_mul_const+0x214>
9fc01e3c:	af080000 	sw	t0,0(t8)
9fc01e40:	10600040 	beqz	v1,9fc01f44 <matrix_mul_const+0x164>
9fc01e44:	00000000 	nop
9fc01e48:	106d0035 	beq	v1,t5,9fc01f20 <matrix_mul_const+0x140>
9fc01e4c:	24060002 	li	a2,2
9fc01e50:	1066002c 	beq	v1,a2,9fc01f04 <matrix_mul_const+0x124>
9fc01e54:	24050003 	li	a1,3
9fc01e58:	10650023 	beq	v1,a1,9fc01ee8 <matrix_mul_const+0x108>
9fc01e5c:	24040004 	li	a0,4
9fc01e60:	1064001a 	beq	v1,a0,9fc01ecc <matrix_mul_const+0xec>
9fc01e64:	24020005 	li	v0,5
9fc01e68:	10620011 	beq	v1,v0,9fc01eb0 <matrix_mul_const+0xd0>
9fc01e6c:	240a0006 	li	t2,6
9fc01e70:	106a0008 	beq	v1,t2,9fc01e94 <matrix_mul_const+0xb4>
9fc01e74:	00000000 	nop
9fc01e78:	85690000 	lh	t1,0(t3)
9fc01e7c:	240d0002 	li	t5,2
9fc01e80:	01c90018 	mult	t6,t1
9fc01e84:	25eb0004 	addiu	t3,t7,4
9fc01e88:	00001812 	mflo	v1
9fc01e8c:	ad830000 	sw	v1,0(t4)
9fc01e90:	270c0008 	addiu	t4,t8,8
9fc01e94:	85670000 	lh	a3,0(t3)
9fc01e98:	25ad0001 	addiu	t5,t5,1
9fc01e9c:	01c70018 	mult	t6,a3
9fc01ea0:	256b0002 	addiu	t3,t3,2
9fc01ea4:	00004012 	mflo	t0
9fc01ea8:	ad880000 	sw	t0,0(t4)
9fc01eac:	258c0004 	addiu	t4,t4,4
9fc01eb0:	85650000 	lh	a1,0(t3)
9fc01eb4:	25ad0001 	addiu	t5,t5,1
9fc01eb8:	01c50018 	mult	t6,a1
9fc01ebc:	256b0002 	addiu	t3,t3,2
9fc01ec0:	00003012 	mflo	a2
9fc01ec4:	ad860000 	sw	a2,0(t4)
9fc01ec8:	258c0004 	addiu	t4,t4,4
9fc01ecc:	85620000 	lh	v0,0(t3)
9fc01ed0:	25ad0001 	addiu	t5,t5,1
9fc01ed4:	01c20018 	mult	t6,v0
9fc01ed8:	256b0002 	addiu	t3,t3,2
9fc01edc:	00002012 	mflo	a0
9fc01ee0:	ad840000 	sw	a0,0(t4)
9fc01ee4:	258c0004 	addiu	t4,t4,4
9fc01ee8:	85630000 	lh	v1,0(t3)
9fc01eec:	25ad0001 	addiu	t5,t5,1
9fc01ef0:	01c30018 	mult	t6,v1
9fc01ef4:	256b0002 	addiu	t3,t3,2
9fc01ef8:	00005012 	mflo	t2
9fc01efc:	ad8a0000 	sw	t2,0(t4)
9fc01f00:	258c0004 	addiu	t4,t4,4
9fc01f04:	85680000 	lh	t0,0(t3)
9fc01f08:	25ad0001 	addiu	t5,t5,1
9fc01f0c:	01c80018 	mult	t6,t0
9fc01f10:	256b0002 	addiu	t3,t3,2
9fc01f14:	00004812 	mflo	t1
9fc01f18:	ad890000 	sw	t1,0(t4)
9fc01f1c:	258c0004 	addiu	t4,t4,4
9fc01f20:	85650000 	lh	a1,0(t3)
9fc01f24:	25ad0001 	addiu	t5,t5,1
9fc01f28:	01c50018 	mult	t6,a1
9fc01f2c:	01b9382b 	sltu	a3,t5,t9
9fc01f30:	256b0002 	addiu	t3,t3,2
9fc01f34:	00003012 	mflo	a2
9fc01f38:	ad860000 	sw	a2,0(t4)
9fc01f3c:	10e0002d 	beqz	a3,9fc01ff4 <matrix_mul_const+0x214>
9fc01f40:	258c0004 	addiu	t4,t4,4
9fc01f44:	856a0000 	lh	t2,0(t3)
9fc01f48:	85630002 	lh	v1,2(t3)
9fc01f4c:	01ca0018 	mult	t6,t2
9fc01f50:	85640004 	lh	a0,4(t3)
9fc01f54:	85650006 	lh	a1,6(t3)
9fc01f58:	85660008 	lh	a2,8(t3)
9fc01f5c:	8567000a 	lh	a3,10(t3)
9fc01f60:	8568000c 	lh	t0,12(t3)
9fc01f64:	8569000e 	lh	t1,14(t3)
9fc01f68:	25ad0008 	addiu	t5,t5,8
9fc01f6c:	01b9502b 	sltu	t2,t5,t9
9fc01f70:	256b0010 	addiu	t3,t3,16
9fc01f74:	00001012 	mflo	v0
9fc01f78:	ad820000 	sw	v0,0(t4)
9fc01f7c:	00000000 	nop
9fc01f80:	01c30018 	mult	t6,v1
9fc01f84:	00001812 	mflo	v1
9fc01f88:	ad830004 	sw	v1,4(t4)
9fc01f8c:	00000000 	nop
9fc01f90:	01c40018 	mult	t6,a0
9fc01f94:	00002012 	mflo	a0
9fc01f98:	ad840008 	sw	a0,8(t4)
9fc01f9c:	00000000 	nop
9fc01fa0:	01c50018 	mult	t6,a1
9fc01fa4:	00002812 	mflo	a1
9fc01fa8:	ad85000c 	sw	a1,12(t4)
9fc01fac:	00000000 	nop
9fc01fb0:	01c60018 	mult	t6,a2
9fc01fb4:	00003012 	mflo	a2
9fc01fb8:	ad860010 	sw	a2,16(t4)
9fc01fbc:	00000000 	nop
9fc01fc0:	01c70018 	mult	t6,a3
9fc01fc4:	00003812 	mflo	a3
9fc01fc8:	ad870014 	sw	a3,20(t4)
9fc01fcc:	00000000 	nop
9fc01fd0:	01c80018 	mult	t6,t0
9fc01fd4:	00004012 	mflo	t0
9fc01fd8:	ad880018 	sw	t0,24(t4)
9fc01fdc:	00000000 	nop
9fc01fe0:	01c90018 	mult	t6,t1
9fc01fe4:	00004812 	mflo	t1
9fc01fe8:	ad89001c 	sw	t1,28(t4)
9fc01fec:	1540ffd5 	bnez	t2,9fc01f44 <matrix_mul_const+0x164>
9fc01ff0:	258c0020 	addiu	t4,t4,32
9fc01ff4:	26100001 	addiu	s0,s0,1
9fc01ff8:	0219582b 	sltu	t3,s0,t9
9fc01ffc:	0312c021 	addu	t8,t8,s2
9fc02000:	1560ff84 	bnez	t3,9fc01e14 <matrix_mul_const+0x34>
9fc02004:	01f17821 	addu	t7,t7,s1
9fc02008:	8fb2000c 	lw	s2,12(sp)
9fc0200c:	8fb10008 	lw	s1,8(sp)
9fc02010:	8fb00004 	lw	s0,4(sp)
9fc02014:	03e00008 	jr	ra
9fc02018:	27bd0010 	addiu	sp,sp,16
9fc0201c:	00000000 	nop

9fc02020 <matrix_add_const>:
matrix_add_const():
9fc02020:	00061400 	sll	v0,a2,0x10
9fc02024:	00807821 	move	t7,a0
9fc02028:	10800061 	beqz	a0,9fc021b0 <matrix_add_const+0x190>
9fc0202c:	00021403 	sra	v0,v0,0x10
9fc02030:	304dffff 	andi	t5,v0,0xffff
9fc02034:	00a07021 	move	t6,a1
9fc02038:	0004c840 	sll	t9,a0,0x1
9fc0203c:	0000c021 	move	t8,zero
9fc02040:	95c80000 	lhu	t0,0(t6)
9fc02044:	240c0001 	li	t4,1
9fc02048:	01a81021 	addu	v0,t5,t0
9fc0204c:	25e9ffff 	addiu	t1,t7,-1
9fc02050:	018f502b 	sltu	t2,t4,t7
9fc02054:	a5c20000 	sh	v0,0(t6)
9fc02058:	31230007 	andi	v1,t1,0x7
9fc0205c:	11400050 	beqz	t2,9fc021a0 <matrix_add_const+0x180>
9fc02060:	25cb0002 	addiu	t3,t6,2
9fc02064:	10600032 	beqz	v1,9fc02130 <matrix_add_const+0x110>
9fc02068:	00000000 	nop
9fc0206c:	106c0029 	beq	v1,t4,9fc02114 <matrix_add_const+0xf4>
9fc02070:	24070002 	li	a3,2
9fc02074:	10670022 	beq	v1,a3,9fc02100 <matrix_add_const+0xe0>
9fc02078:	24060003 	li	a2,3
9fc0207c:	1066001b 	beq	v1,a2,9fc020ec <matrix_add_const+0xcc>
9fc02080:	24050004 	li	a1,4
9fc02084:	10650014 	beq	v1,a1,9fc020d8 <matrix_add_const+0xb8>
9fc02088:	24040005 	li	a0,5
9fc0208c:	1064000d 	beq	v1,a0,9fc020c4 <matrix_add_const+0xa4>
9fc02090:	240a0006 	li	t2,6
9fc02094:	106a0006 	beq	v1,t2,9fc020b0 <matrix_add_const+0x90>
9fc02098:	00000000 	nop
9fc0209c:	95690000 	lhu	t1,0(t3)
9fc020a0:	240c0002 	li	t4,2
9fc020a4:	01a91821 	addu	v1,t5,t1
9fc020a8:	a5630000 	sh	v1,0(t3)
9fc020ac:	25cb0004 	addiu	t3,t6,4
9fc020b0:	95680000 	lhu	t0,0(t3)
9fc020b4:	258c0001 	addiu	t4,t4,1
9fc020b8:	01a81021 	addu	v0,t5,t0
9fc020bc:	a5620000 	sh	v0,0(t3)
9fc020c0:	256b0002 	addiu	t3,t3,2
9fc020c4:	95660000 	lhu	a2,0(t3)
9fc020c8:	258c0001 	addiu	t4,t4,1
9fc020cc:	01a63821 	addu	a3,t5,a2
9fc020d0:	a5670000 	sh	a3,0(t3)
9fc020d4:	256b0002 	addiu	t3,t3,2
9fc020d8:	95640000 	lhu	a0,0(t3)
9fc020dc:	258c0001 	addiu	t4,t4,1
9fc020e0:	01a42821 	addu	a1,t5,a0
9fc020e4:	a5650000 	sh	a1,0(t3)
9fc020e8:	256b0002 	addiu	t3,t3,2
9fc020ec:	95630000 	lhu	v1,0(t3)
9fc020f0:	258c0001 	addiu	t4,t4,1
9fc020f4:	01a35021 	addu	t2,t5,v1
9fc020f8:	a56a0000 	sh	t2,0(t3)
9fc020fc:	256b0002 	addiu	t3,t3,2
9fc02100:	95620000 	lhu	v0,0(t3)
9fc02104:	258c0001 	addiu	t4,t4,1
9fc02108:	01a24821 	addu	t1,t5,v0
9fc0210c:	a5690000 	sh	t1,0(t3)
9fc02110:	256b0002 	addiu	t3,t3,2
9fc02114:	95660000 	lhu	a2,0(t3)
9fc02118:	258c0001 	addiu	t4,t4,1
9fc0211c:	01a63821 	addu	a3,t5,a2
9fc02120:	018f402b 	sltu	t0,t4,t7
9fc02124:	a5670000 	sh	a3,0(t3)
9fc02128:	1100001d 	beqz	t0,9fc021a0 <matrix_add_const+0x180>
9fc0212c:	256b0002 	addiu	t3,t3,2
9fc02130:	95630000 	lhu	v1,0(t3)
9fc02134:	95640002 	lhu	a0,2(t3)
9fc02138:	95650004 	lhu	a1,4(t3)
9fc0213c:	95660006 	lhu	a2,6(t3)
9fc02140:	95670008 	lhu	a3,8(t3)
9fc02144:	9568000a 	lhu	t0,10(t3)
9fc02148:	9569000c 	lhu	t1,12(t3)
9fc0214c:	956a000e 	lhu	t2,14(t3)
9fc02150:	258c0008 	addiu	t4,t4,8
9fc02154:	01a31021 	addu	v0,t5,v1
9fc02158:	01a41821 	addu	v1,t5,a0
9fc0215c:	01a52021 	addu	a0,t5,a1
9fc02160:	01a62821 	addu	a1,t5,a2
9fc02164:	01a73021 	addu	a2,t5,a3
9fc02168:	01a83821 	addu	a3,t5,t0
9fc0216c:	01a94021 	addu	t0,t5,t1
9fc02170:	01aa4821 	addu	t1,t5,t2
9fc02174:	018f502b 	sltu	t2,t4,t7
9fc02178:	a5620000 	sh	v0,0(t3)
9fc0217c:	a5630002 	sh	v1,2(t3)
9fc02180:	a5640004 	sh	a0,4(t3)
9fc02184:	a5650006 	sh	a1,6(t3)
9fc02188:	a5660008 	sh	a2,8(t3)
9fc0218c:	a567000a 	sh	a3,10(t3)
9fc02190:	a568000c 	sh	t0,12(t3)
9fc02194:	a569000e 	sh	t1,14(t3)
9fc02198:	1540ffe5 	bnez	t2,9fc02130 <matrix_add_const+0x110>
9fc0219c:	256b0010 	addiu	t3,t3,16
9fc021a0:	27180001 	addiu	t8,t8,1
9fc021a4:	030f582b 	sltu	t3,t8,t7
9fc021a8:	1560ffa5 	bnez	t3,9fc02040 <matrix_add_const+0x20>
9fc021ac:	01d97021 	addu	t6,t6,t9
9fc021b0:	03e00008 	jr	ra
9fc021b4:	00000000 	nop
	...

9fc021c0 <matrix_mul_vect>:
matrix_mul_vect():
9fc021c0:	27bdffd8 	addiu	sp,sp,-40
9fc021c4:	afb70020 	sw	s7,32(sp)
9fc021c8:	afb40014 	sw	s4,20(sp)
9fc021cc:	afbe0024 	sw	s8,36(sp)
9fc021d0:	afb6001c 	sw	s6,28(sp)
9fc021d4:	afb50018 	sw	s5,24(sp)
9fc021d8:	afb30010 	sw	s3,16(sp)
9fc021dc:	afb2000c 	sw	s2,12(sp)
9fc021e0:	afb10008 	sw	s1,8(sp)
9fc021e4:	afb00004 	sw	s0,4(sp)
9fc021e8:	0080a021 	move	s4,a0
9fc021ec:	10800093 	beqz	a0,9fc0243c <matrix_mul_vect+0x27c>
9fc021f0:	00e0b821 	move	s7,a3
9fc021f4:	00a0b021 	move	s6,a1
9fc021f8:	00c09821 	move	s3,a2
9fc021fc:	0004f040 	sll	s8,a0,0x1
9fc02200:	0000a821 	move	s5,zero
9fc02204:	86e40000 	lh	a0,0(s7)
9fc02208:	86620000 	lh	v0,0(s3)
9fc0220c:	24110001 	li	s1,1
9fc02210:	00820018 	mult	a0,v0
9fc02214:	2683ffff 	addiu	v1,s4,-1
9fc02218:	0234282b 	sltu	a1,s1,s4
9fc0221c:	30630007 	andi	v1,v1,0x7
9fc02220:	26780002 	addiu	t8,s3,2
9fc02224:	00009012 	mflo	s2
9fc02228:	10a0007e 	beqz	a1,9fc02424 <matrix_mul_vect+0x264>
9fc0222c:	26f90002 	addiu	t9,s7,2
9fc02230:	10600047 	beqz	v1,9fc02350 <matrix_mul_vect+0x190>
9fc02234:	00000000 	nop
9fc02238:	1071003b 	beq	v1,s1,9fc02328 <matrix_mul_vect+0x168>
9fc0223c:	24100002 	li	s0,2
9fc02240:	10700031 	beq	v1,s0,9fc02308 <matrix_mul_vect+0x148>
9fc02244:	24070003 	li	a3,3
9fc02248:	10670027 	beq	v1,a3,9fc022e8 <matrix_mul_vect+0x128>
9fc0224c:	240f0004 	li	t7,4
9fc02250:	106f001d 	beq	v1,t7,9fc022c8 <matrix_mul_vect+0x108>
9fc02254:	24060005 	li	a2,5
9fc02258:	10660013 	beq	v1,a2,9fc022a8 <matrix_mul_vect+0xe8>
9fc0225c:	240e0006 	li	t6,6
9fc02260:	106e0009 	beq	v1,t6,9fc02288 <matrix_mul_vect+0xc8>
9fc02264:	00000000 	nop
9fc02268:	87190000 	lh	t9,0(t8)
9fc0226c:	86f80002 	lh	t8,2(s7)
9fc02270:	24110002 	li	s1,2
9fc02274:	03190018 	mult	t8,t9
9fc02278:	26780004 	addiu	t8,s3,4
9fc0227c:	26f90004 	addiu	t9,s7,4
9fc02280:	00002812 	mflo	a1
9fc02284:	02459021 	addu	s2,s2,a1
9fc02288:	87290000 	lh	t1,0(t9)
9fc0228c:	87040000 	lh	a0,0(t8)
9fc02290:	26310001 	addiu	s1,s1,1
9fc02294:	01240018 	mult	t1,a0
9fc02298:	27180002 	addiu	t8,t8,2
9fc0229c:	27390002 	addiu	t9,t9,2
9fc022a0:	00006812 	mflo	t5
9fc022a4:	024d9021 	addu	s2,s2,t5
9fc022a8:	87220000 	lh	v0,0(t9)
9fc022ac:	870b0000 	lh	t3,0(t8)
9fc022b0:	26310001 	addiu	s1,s1,1
9fc022b4:	004b0018 	mult	v0,t3
9fc022b8:	27180002 	addiu	t8,t8,2
9fc022bc:	27390002 	addiu	t9,t9,2
9fc022c0:	00006012 	mflo	t4
9fc022c4:	024c9021 	addu	s2,s2,t4
9fc022c8:	87280000 	lh	t0,0(t9)
9fc022cc:	87030000 	lh	v1,0(t8)
9fc022d0:	26310001 	addiu	s1,s1,1
9fc022d4:	01030018 	mult	t0,v1
9fc022d8:	27180002 	addiu	t8,t8,2
9fc022dc:	27390002 	addiu	t9,t9,2
9fc022e0:	00005012 	mflo	t2
9fc022e4:	024a9021 	addu	s2,s2,t2
9fc022e8:	87270000 	lh	a3,0(t9)
9fc022ec:	870f0000 	lh	t7,0(t8)
9fc022f0:	26310001 	addiu	s1,s1,1
9fc022f4:	00ef0018 	mult	a3,t7
9fc022f8:	27180002 	addiu	t8,t8,2
9fc022fc:	27390002 	addiu	t9,t9,2
9fc02300:	00008012 	mflo	s0
9fc02304:	02509021 	addu	s2,s2,s0
9fc02308:	872e0000 	lh	t6,0(t9)
9fc0230c:	87050000 	lh	a1,0(t8)
9fc02310:	26310001 	addiu	s1,s1,1
9fc02314:	01c50018 	mult	t6,a1
9fc02318:	27180002 	addiu	t8,t8,2
9fc0231c:	27390002 	addiu	t9,t9,2
9fc02320:	00003012 	mflo	a2
9fc02324:	02469021 	addu	s2,s2,a2
9fc02328:	87240000 	lh	a0,0(t9)
9fc0232c:	870c0000 	lh	t4,0(t8)
9fc02330:	26310001 	addiu	s1,s1,1
9fc02334:	008c0018 	mult	a0,t4
9fc02338:	0234682b 	sltu	t5,s1,s4
9fc0233c:	27180002 	addiu	t8,t8,2
9fc02340:	27390002 	addiu	t9,t9,2
9fc02344:	00004812 	mflo	t1
9fc02348:	11a00036 	beqz	t5,9fc02424 <matrix_mul_vect+0x264>
9fc0234c:	02499021 	addu	s2,s2,t1
9fc02350:	87280000 	lh	t0,0(t9)
9fc02354:	87030000 	lh	v1,0(t8)
9fc02358:	872b0002 	lh	t3,2(t9)
9fc0235c:	01030018 	mult	t0,v1
9fc02360:	870a0002 	lh	t2,2(t8)
9fc02364:	87240004 	lh	a0,4(t9)
9fc02368:	870c0004 	lh	t4,4(t8)
9fc0236c:	87250006 	lh	a1,6(t9)
9fc02370:	870d0006 	lh	t5,6(t8)
9fc02374:	87260008 	lh	a2,8(t9)
9fc02378:	870e0008 	lh	t6,8(t8)
9fc0237c:	8727000a 	lh	a3,10(t9)
9fc02380:	870f000a 	lh	t7,10(t8)
9fc02384:	8728000c 	lh	t0,12(t9)
9fc02388:	8710000c 	lh	s0,12(t8)
9fc0238c:	8723000e 	lh	v1,14(t9)
9fc02390:	00001012 	mflo	v0
9fc02394:	26310008 	addiu	s1,s1,8
9fc02398:	02421021 	addu	v0,s2,v0
9fc0239c:	27390010 	addiu	t9,t9,16
9fc023a0:	016a0018 	mult	t3,t2
9fc023a4:	870b000e 	lh	t3,14(t8)
9fc023a8:	0234502b 	sltu	t2,s1,s4
9fc023ac:	27180010 	addiu	t8,t8,16
9fc023b0:	00004812 	mflo	t1
	...
9fc023bc:	008c0018 	mult	a0,t4
9fc023c0:	00496021 	addu	t4,v0,t1
9fc023c4:	00002012 	mflo	a0
9fc023c8:	01841021 	addu	v0,t4,a0
9fc023cc:	00000000 	nop
9fc023d0:	00ad0018 	mult	a1,t5
9fc023d4:	00002812 	mflo	a1
	...
9fc023e0:	00ce0018 	mult	a2,t6
9fc023e4:	00457021 	addu	t6,v0,a1
9fc023e8:	00009012 	mflo	s2
9fc023ec:	01d26021 	addu	t4,t6,s2
9fc023f0:	00000000 	nop
9fc023f4:	00ef0018 	mult	a3,t7
9fc023f8:	00006812 	mflo	t5
9fc023fc:	018d4821 	addu	t1,t4,t5
9fc02400:	00000000 	nop
9fc02404:	01100018 	mult	t0,s0
9fc02408:	00004012 	mflo	t0
9fc0240c:	01283021 	addu	a2,t1,t0
9fc02410:	00000000 	nop
9fc02414:	006b0018 	mult	v1,t3
9fc02418:	00003812 	mflo	a3
9fc0241c:	1540ffcc 	bnez	t2,9fc02350 <matrix_mul_vect+0x190>
9fc02420:	00c79021 	addu	s2,a2,a3
9fc02424:	26b50001 	addiu	s5,s5,1
9fc02428:	02b4882b 	sltu	s1,s5,s4
9fc0242c:	aed20000 	sw	s2,0(s6)
9fc02430:	027e9821 	addu	s3,s3,s8
9fc02434:	1620ff73 	bnez	s1,9fc02204 <matrix_mul_vect+0x44>
9fc02438:	26d60004 	addiu	s6,s6,4
9fc0243c:	8fbe0024 	lw	s8,36(sp)
9fc02440:	8fb70020 	lw	s7,32(sp)
9fc02444:	8fb6001c 	lw	s6,28(sp)
9fc02448:	8fb50018 	lw	s5,24(sp)
9fc0244c:	8fb40014 	lw	s4,20(sp)
9fc02450:	8fb30010 	lw	s3,16(sp)
9fc02454:	8fb2000c 	lw	s2,12(sp)
9fc02458:	8fb10008 	lw	s1,8(sp)
9fc0245c:	8fb00004 	lw	s0,4(sp)
9fc02460:	03e00008 	jr	ra
9fc02464:	27bd0028 	addiu	sp,sp,40
	...

9fc02470 <matrix_mul_matrix>:
matrix_mul_matrix():
9fc02470:	27bdffc8 	addiu	sp,sp,-56
9fc02474:	afb50028 	sw	s5,40(sp)
9fc02478:	afbe0034 	sw	s8,52(sp)
9fc0247c:	afb70030 	sw	s7,48(sp)
9fc02480:	afb6002c 	sw	s6,44(sp)
9fc02484:	afb40024 	sw	s4,36(sp)
9fc02488:	afb30020 	sw	s3,32(sp)
9fc0248c:	afb2001c 	sw	s2,28(sp)
9fc02490:	afb10018 	sw	s1,24(sp)
9fc02494:	afb00014 	sw	s0,20(sp)
9fc02498:	0080a821 	move	s5,a0
9fc0249c:	108000a8 	beqz	a0,9fc02740 <matrix_mul_matrix+0x2d0>
9fc024a0:	afa70044 	sw	a3,68(sp)
9fc024a4:	00041080 	sll	v0,a0,0x2
9fc024a8:	00c0f021 	move	s8,a2
9fc024ac:	afa50004 	sw	a1,4(sp)
9fc024b0:	00049840 	sll	s3,a0,0x1
9fc024b4:	afa20000 	sw	v0,0(sp)
9fc024b8:	afa00008 	sw	zero,8(sp)
9fc024bc:	8fb40044 	lw	s4,68(sp)
9fc024c0:	8fb70004 	lw	s7,4(sp)
9fc024c4:	0000b021 	move	s6,zero
9fc024c8:	86840000 	lh	a0,0(s4)
9fc024cc:	87c60000 	lh	a2,0(s8)
9fc024d0:	24190001 	li	t9,1
9fc024d4:	00860018 	mult	a0,a2
9fc024d8:	26a3ffff 	addiu	v1,s5,-1
9fc024dc:	0335282b 	sltu	a1,t9,s5
9fc024e0:	30630007 	andi	v1,v1,0x7
9fc024e4:	27d80002 	addiu	t8,s8,2
9fc024e8:	00009012 	mflo	s2
9fc024ec:	10a00084 	beqz	a1,9fc02700 <matrix_mul_matrix+0x290>
9fc024f0:	02935021 	addu	t2,s4,s3
9fc024f4:	10600047 	beqz	v1,9fc02614 <matrix_mul_matrix+0x1a4>
9fc024f8:	00000000 	nop
9fc024fc:	1079003b 	beq	v1,t9,9fc025ec <matrix_mul_matrix+0x17c>
9fc02500:	240b0002 	li	t3,2
9fc02504:	106b0031 	beq	v1,t3,9fc025cc <matrix_mul_matrix+0x15c>
9fc02508:	24090003 	li	t1,3
9fc0250c:	10690027 	beq	v1,t1,9fc025ac <matrix_mul_matrix+0x13c>
9fc02510:	24110004 	li	s1,4
9fc02514:	1071001d 	beq	v1,s1,9fc0258c <matrix_mul_matrix+0x11c>
9fc02518:	24020005 	li	v0,5
9fc0251c:	10620013 	beq	v1,v0,9fc0256c <matrix_mul_matrix+0xfc>
9fc02520:	24050006 	li	a1,6
9fc02524:	10650009 	beq	v1,a1,9fc0254c <matrix_mul_matrix+0xdc>
9fc02528:	00000000 	nop
9fc0252c:	870c0000 	lh	t4,0(t8)
9fc02530:	854d0000 	lh	t5,0(t2)
9fc02534:	24190002 	li	t9,2
9fc02538:	01ac0018 	mult	t5,t4
9fc0253c:	01535021 	addu	t2,t2,s3
9fc02540:	27d80004 	addiu	t8,s8,4
9fc02544:	00001812 	mflo	v1
9fc02548:	02439021 	addu	s2,s2,v1
9fc0254c:	85460000 	lh	a2,0(t2)
9fc02550:	870e0000 	lh	t6,0(t8)
9fc02554:	27390001 	addiu	t9,t9,1
9fc02558:	00ce0018 	mult	a2,t6
9fc0255c:	27180002 	addiu	t8,t8,2
9fc02560:	01535021 	addu	t2,t2,s3
9fc02564:	00002012 	mflo	a0
9fc02568:	02449021 	addu	s2,s2,a0
9fc0256c:	854f0000 	lh	t7,0(t2)
9fc02570:	87080000 	lh	t0,0(t8)
9fc02574:	27390001 	addiu	t9,t9,1
9fc02578:	01e80018 	mult	t7,t0
9fc0257c:	27180002 	addiu	t8,t8,2
9fc02580:	01535021 	addu	t2,t2,s3
9fc02584:	00003812 	mflo	a3
9fc02588:	02479021 	addu	s2,s2,a3
9fc0258c:	854b0000 	lh	t3,0(t2)
9fc02590:	87090000 	lh	t1,0(t8)
9fc02594:	27390001 	addiu	t9,t9,1
9fc02598:	01690018 	mult	t3,t1
9fc0259c:	27180002 	addiu	t8,t8,2
9fc025a0:	01535021 	addu	t2,t2,s3
9fc025a4:	00008012 	mflo	s0
9fc025a8:	02509021 	addu	s2,s2,s0
9fc025ac:	85420000 	lh	v0,0(t2)
9fc025b0:	87050000 	lh	a1,0(t8)
9fc025b4:	27390001 	addiu	t9,t9,1
9fc025b8:	00450018 	mult	v0,a1
9fc025bc:	27180002 	addiu	t8,t8,2
9fc025c0:	01535021 	addu	t2,t2,s3
9fc025c4:	00008812 	mflo	s1
9fc025c8:	02519021 	addu	s2,s2,s1
9fc025cc:	854d0000 	lh	t5,0(t2)
9fc025d0:	870c0000 	lh	t4,0(t8)
9fc025d4:	27390001 	addiu	t9,t9,1
9fc025d8:	01ac0018 	mult	t5,t4
9fc025dc:	27180002 	addiu	t8,t8,2
9fc025e0:	01535021 	addu	t2,t2,s3
9fc025e4:	00001812 	mflo	v1
9fc025e8:	02439021 	addu	s2,s2,v1
9fc025ec:	854e0000 	lh	t6,0(t2)
9fc025f0:	87070000 	lh	a3,0(t8)
9fc025f4:	27390001 	addiu	t9,t9,1
9fc025f8:	01c70018 	mult	t6,a3
9fc025fc:	0335202b 	sltu	a0,t9,s5
9fc02600:	27180002 	addiu	t8,t8,2
9fc02604:	01535021 	addu	t2,t2,s3
9fc02608:	00003012 	mflo	a2
9fc0260c:	1080003c 	beqz	a0,9fc02700 <matrix_mul_matrix+0x290>
9fc02610:	02469021 	addu	s2,s2,a2
9fc02614:	854f0000 	lh	t7,0(t2)
9fc02618:	87080000 	lh	t0,0(t8)
9fc0261c:	01533821 	addu	a3,t2,s3
9fc02620:	01e80018 	mult	t7,t0
9fc02624:	870d0002 	lh	t5,2(t8)
9fc02628:	84ec0000 	lh	t4,0(a3)
9fc0262c:	00f37021 	addu	t6,a3,s3
9fc02630:	87110004 	lh	s1,4(t8)
9fc02634:	85c90000 	lh	t1,0(t6)
9fc02638:	01d33021 	addu	a2,t6,s3
9fc0263c:	87100006 	lh	s0,6(t8)
9fc02640:	84c80000 	lh	t0,0(a2)
9fc02644:	00d32021 	addu	a0,a2,s3
9fc02648:	870f0008 	lh	t7,8(t8)
9fc0264c:	84870000 	lh	a3,0(a0)
9fc02650:	00932821 	addu	a1,a0,s3
9fc02654:	00001012 	mflo	v0
9fc02658:	870e000a 	lh	t6,10(t8)
9fc0265c:	84a60000 	lh	a2,0(a1)
9fc02660:	00b31821 	addu	v1,a1,s3
9fc02664:	018d0018 	mult	t4,t5
9fc02668:	84640000 	lh	a0,0(v1)
9fc0266c:	870c000c 	lh	t4,12(t8)
9fc02670:	00735021 	addu	t2,v1,s3
9fc02674:	870d000e 	lh	t5,14(t8)
9fc02678:	85430000 	lh	v1,0(t2)
9fc0267c:	02421021 	addu	v0,s2,v0
9fc02680:	27390008 	addiu	t9,t9,8
9fc02684:	0335282b 	sltu	a1,t9,s5
9fc02688:	27180010 	addiu	t8,t8,16
9fc0268c:	01535021 	addu	t2,t2,s3
9fc02690:	00005812 	mflo	t3
9fc02694:	004b1021 	addu	v0,v0,t3
9fc02698:	00000000 	nop
9fc0269c:	01310018 	mult	t1,s1
9fc026a0:	00004812 	mflo	t1
9fc026a4:	00499021 	addu	s2,v0,t1
9fc026a8:	00000000 	nop
9fc026ac:	01100018 	mult	t0,s0
9fc026b0:	00004012 	mflo	t0
9fc026b4:	02488021 	addu	s0,s2,t0
9fc026b8:	00000000 	nop
9fc026bc:	00ef0018 	mult	a3,t7
9fc026c0:	00008812 	mflo	s1
	...
9fc026cc:	00ce0018 	mult	a2,t6
9fc026d0:	02117021 	addu	t6,s0,s1
9fc026d4:	00007812 	mflo	t7
9fc026d8:	01cf4821 	addu	t1,t6,t7
9fc026dc:	00000000 	nop
9fc026e0:	008c0018 	mult	a0,t4
9fc026e4:	00005812 	mflo	t3
9fc026e8:	012b3821 	addu	a3,t1,t3
9fc026ec:	00000000 	nop
9fc026f0:	006d0018 	mult	v1,t5
9fc026f4:	00004012 	mflo	t0
9fc026f8:	14a0ffc6 	bnez	a1,9fc02614 <matrix_mul_matrix+0x1a4>
9fc026fc:	00e89021 	addu	s2,a3,t0
9fc02700:	26d60001 	addiu	s6,s6,1
9fc02704:	02d5c02b 	sltu	t8,s6,s5
9fc02708:	aef20000 	sw	s2,0(s7)
9fc0270c:	26940002 	addiu	s4,s4,2
9fc02710:	1700ff6d 	bnez	t8,9fc024c8 <matrix_mul_matrix+0x58>
9fc02714:	26f70004 	addiu	s7,s7,4
9fc02718:	8fb00008 	lw	s0,8(sp)
9fc0271c:	8fb70004 	lw	s7,4(sp)
9fc02720:	8fb90000 	lw	t9,0(sp)
9fc02724:	26120001 	addiu	s2,s0,1
9fc02728:	02f9b021 	addu	s6,s7,t9
9fc0272c:	0255a02b 	sltu	s4,s2,s5
9fc02730:	afb20008 	sw	s2,8(sp)
9fc02734:	03d3f021 	addu	s8,s8,s3
9fc02738:	1680ff60 	bnez	s4,9fc024bc <matrix_mul_matrix+0x4c>
9fc0273c:	afb60004 	sw	s6,4(sp)
9fc02740:	8fbe0034 	lw	s8,52(sp)
9fc02744:	8fb70030 	lw	s7,48(sp)
9fc02748:	8fb6002c 	lw	s6,44(sp)
9fc0274c:	8fb50028 	lw	s5,40(sp)
9fc02750:	8fb40024 	lw	s4,36(sp)
9fc02754:	8fb30020 	lw	s3,32(sp)
9fc02758:	8fb2001c 	lw	s2,28(sp)
9fc0275c:	8fb10018 	lw	s1,24(sp)
9fc02760:	8fb00014 	lw	s0,20(sp)
9fc02764:	03e00008 	jr	ra
9fc02768:	27bd0038 	addiu	sp,sp,56
9fc0276c:	00000000 	nop

9fc02770 <matrix_mul_matrix_bitextract>:
matrix_mul_matrix_bitextract():
9fc02770:	27bdffd8 	addiu	sp,sp,-40
9fc02774:	afbe0024 	sw	s8,36(sp)
9fc02778:	afb00004 	sw	s0,4(sp)
9fc0277c:	afb70020 	sw	s7,32(sp)
9fc02780:	afb6001c 	sw	s6,28(sp)
9fc02784:	afb50018 	sw	s5,24(sp)
9fc02788:	afb40014 	sw	s4,20(sp)
9fc0278c:	afb30010 	sw	s3,16(sp)
9fc02790:	afb2000c 	sw	s2,12(sp)
9fc02794:	afb10008 	sw	s1,8(sp)
9fc02798:	00808021 	move	s0,a0
9fc0279c:	1080008e 	beqz	a0,9fc029d8 <matrix_mul_matrix_bitextract+0x268>
9fc027a0:	00e0f021 	move	s8,a3
9fc027a4:	00c0a021 	move	s4,a2
9fc027a8:	00a0a821 	move	s5,a1
9fc027ac:	0004c840 	sll	t9,a0,0x1
9fc027b0:	0004b880 	sll	s7,a0,0x2
9fc027b4:	0000b021 	move	s6,zero
9fc027b8:	03c08821 	move	s1,s8
9fc027bc:	02a09021 	move	s2,s5
9fc027c0:	00009821 	move	s3,zero
9fc027c4:	86290000 	lh	t1,0(s1)
9fc027c8:	868a0000 	lh	t2,0(s4)
9fc027cc:	240f0001 	li	t7,1
9fc027d0:	012a0018 	mult	t1,t2
9fc027d4:	2604ffff 	addiu	a0,s0,-1
9fc027d8:	01f0282b 	sltu	a1,t7,s0
9fc027dc:	30840003 	andi	a0,a0,0x3
9fc027e0:	268d0002 	addiu	t5,s4,2
9fc027e4:	00004012 	mflo	t0
9fc027e8:	00083083 	sra	a2,t0,0x2
9fc027ec:	00083943 	sra	a3,t0,0x5
9fc027f0:	30e2007f 	andi	v0,a3,0x7f
9fc027f4:	30c3000f 	andi	v1,a2,0xf
9fc027f8:	00430018 	mult	v0,v1
9fc027fc:	0000c012 	mflo	t8
9fc02800:	10a0006a 	beqz	a1,9fc029ac <matrix_mul_matrix_bitextract+0x23c>
9fc02804:	02397021 	addu	t6,s1,t9
9fc02808:	10800031 	beqz	a0,9fc028d0 <matrix_mul_matrix_bitextract+0x160>
9fc0280c:	00000000 	nop
9fc02810:	108f001f 	beq	a0,t7,9fc02890 <matrix_mul_matrix_bitextract+0x120>
9fc02814:	24030002 	li	v1,2
9fc02818:	1083000f 	beq	a0,v1,9fc02858 <matrix_mul_matrix_bitextract+0xe8>
9fc0281c:	00000000 	nop
9fc02820:	85a70000 	lh	a3,0(t5)
9fc02824:	85cc0000 	lh	t4,0(t6)
9fc02828:	240f0002 	li	t7,2
9fc0282c:	01870018 	mult	t4,a3
9fc02830:	01d97021 	addu	t6,t6,t9
9fc02834:	268d0004 	addiu	t5,s4,4
9fc02838:	00004812 	mflo	t1
9fc0283c:	00091083 	sra	v0,t1,0x2
9fc02840:	00092943 	sra	a1,t1,0x5
9fc02844:	30a8007f 	andi	t0,a1,0x7f
9fc02848:	304a000f 	andi	t2,v0,0xf
9fc0284c:	010a0018 	mult	t0,t2
9fc02850:	00002012 	mflo	a0
9fc02854:	0304c021 	addu	t8,t8,a0
9fc02858:	85c20000 	lh	v0,0(t6)
9fc0285c:	85a50000 	lh	a1,0(t5)
9fc02860:	25ef0001 	addiu	t7,t7,1
9fc02864:	00450018 	mult	v0,a1
9fc02868:	25ad0002 	addiu	t5,t5,2
9fc0286c:	01d97021 	addu	t6,t6,t9
9fc02870:	00004012 	mflo	t0
9fc02874:	00082083 	sra	a0,t0,0x2
9fc02878:	00085143 	sra	t2,t0,0x5
9fc0287c:	314b007f 	andi	t3,t2,0x7f
9fc02880:	3083000f 	andi	v1,a0,0xf
9fc02884:	01630018 	mult	t3,v1
9fc02888:	00003012 	mflo	a2
9fc0288c:	0306c021 	addu	t8,t8,a2
9fc02890:	85ca0000 	lh	t2,0(t6)
9fc02894:	85a80000 	lh	t0,0(t5)
9fc02898:	25ef0001 	addiu	t7,t7,1
9fc0289c:	01480018 	mult	t2,t0
9fc028a0:	01f0482b 	sltu	t1,t7,s0
9fc028a4:	25ad0002 	addiu	t5,t5,2
9fc028a8:	01d97021 	addu	t6,t6,t9
9fc028ac:	00002012 	mflo	a0
9fc028b0:	00045883 	sra	t3,a0,0x2
9fc028b4:	00041943 	sra	v1,a0,0x5
9fc028b8:	306c007f 	andi	t4,v1,0x7f
9fc028bc:	3166000f 	andi	a2,t3,0xf
9fc028c0:	01860018 	mult	t4,a2
9fc028c4:	00003812 	mflo	a3
9fc028c8:	11200038 	beqz	t1,9fc029ac <matrix_mul_matrix_bitextract+0x23c>
9fc028cc:	0307c021 	addu	t8,t8,a3
9fc028d0:	85c60000 	lh	a2,0(t6)
9fc028d4:	85ab0000 	lh	t3,0(t5)
9fc028d8:	01d96021 	addu	t4,t6,t9
9fc028dc:	00cb0018 	mult	a2,t3
9fc028e0:	85a90002 	lh	t1,2(t5)
9fc028e4:	85850000 	lh	a1,0(t4)
9fc028e8:	01997021 	addu	t6,t4,t9
9fc028ec:	85aa0004 	lh	t2,4(t5)
9fc028f0:	85c40000 	lh	a0,0(t6)
9fc028f4:	01d93821 	addu	a3,t6,t9
9fc028f8:	85ab0006 	lh	t3,6(t5)
9fc028fc:	84e60000 	lh	a2,0(a3)
9fc02900:	00f97021 	addu	t6,a3,t9
9fc02904:	25ef0004 	addiu	t7,t7,4
9fc02908:	01f0602b 	sltu	t4,t7,s0
9fc0290c:	25ad0008 	addiu	t5,t5,8
9fc02910:	00001012 	mflo	v0
9fc02914:	00024083 	sra	t0,v0,0x2
9fc02918:	00021143 	sra	v0,v0,0x5
9fc0291c:	00a90018 	mult	a1,t1
9fc02920:	3042007f 	andi	v0,v0,0x7f
9fc02924:	3108000f 	andi	t0,t0,0xf
9fc02928:	00001812 	mflo	v1
9fc0292c:	00034883 	sra	t1,v1,0x2
9fc02930:	00032943 	sra	a1,v1,0x5
9fc02934:	008a0018 	mult	a0,t2
9fc02938:	30a5007f 	andi	a1,a1,0x7f
9fc0293c:	3129000f 	andi	t1,t1,0xf
9fc02940:	00005012 	mflo	t2
9fc02944:	000a3883 	sra	a3,t2,0x2
9fc02948:	000a2143 	sra	a0,t2,0x5
9fc0294c:	00cb0018 	mult	a2,t3
9fc02950:	30ea000f 	andi	t2,a3,0xf
9fc02954:	3084007f 	andi	a0,a0,0x7f
9fc02958:	00001812 	mflo	v1
9fc0295c:	00033083 	sra	a2,v1,0x2
9fc02960:	00031943 	sra	v1,v1,0x5
9fc02964:	00480018 	mult	v0,t0
9fc02968:	3063007f 	andi	v1,v1,0x7f
9fc0296c:	30c6000f 	andi	a2,a2,0xf
9fc02970:	00005812 	mflo	t3
9fc02974:	030b1021 	addu	v0,t8,t3
9fc02978:	00000000 	nop
9fc0297c:	00a90018 	mult	a1,t1
9fc02980:	00004012 	mflo	t0
9fc02984:	0048c021 	addu	t8,v0,t0
9fc02988:	00000000 	nop
9fc0298c:	008a0018 	mult	a0,t2
9fc02990:	00003812 	mflo	a3
9fc02994:	03075821 	addu	t3,t8,a3
9fc02998:	00000000 	nop
9fc0299c:	00660018 	mult	v1,a2
9fc029a0:	00002812 	mflo	a1
9fc029a4:	1580ffca 	bnez	t4,9fc028d0 <matrix_mul_matrix_bitextract+0x160>
9fc029a8:	0165c021 	addu	t8,t3,a1
9fc029ac:	26730001 	addiu	s3,s3,1
9fc029b0:	0270682b 	sltu	t5,s3,s0
9fc029b4:	ae580000 	sw	t8,0(s2)
9fc029b8:	26310002 	addiu	s1,s1,2
9fc029bc:	15a0ff81 	bnez	t5,9fc027c4 <matrix_mul_matrix_bitextract+0x54>
9fc029c0:	26520004 	addiu	s2,s2,4
9fc029c4:	26d60001 	addiu	s6,s6,1
9fc029c8:	02d0782b 	sltu	t7,s6,s0
9fc029cc:	0299a021 	addu	s4,s4,t9
9fc029d0:	15e0ff79 	bnez	t7,9fc027b8 <matrix_mul_matrix_bitextract+0x48>
9fc029d4:	02b7a821 	addu	s5,s5,s7
9fc029d8:	8fbe0024 	lw	s8,36(sp)
9fc029dc:	8fb70020 	lw	s7,32(sp)
9fc029e0:	8fb6001c 	lw	s6,28(sp)
9fc029e4:	8fb50018 	lw	s5,24(sp)
9fc029e8:	8fb40014 	lw	s4,20(sp)
9fc029ec:	8fb30010 	lw	s3,16(sp)
9fc029f0:	8fb2000c 	lw	s2,12(sp)
9fc029f4:	8fb10008 	lw	s1,8(sp)
9fc029f8:	8fb00004 	lw	s0,4(sp)
9fc029fc:	03e00008 	jr	ra
9fc02a00:	27bd0028 	addiu	sp,sp,40
	...

9fc02a10 <matrix_test>:
matrix_test():
9fc02a10:	27bdffa0 	addiu	sp,sp,-96
9fc02a14:	87a20070 	lh	v0,112(sp)
9fc02a18:	afb40048 	sw	s4,72(sp)
9fc02a1c:	afbf005c 	sw	ra,92(sp)
9fc02a20:	afbe0058 	sw	s8,88(sp)
9fc02a24:	afb70054 	sw	s7,84(sp)
9fc02a28:	afb60050 	sw	s6,80(sp)
9fc02a2c:	afb5004c 	sw	s5,76(sp)
9fc02a30:	afb30044 	sw	s3,68(sp)
9fc02a34:	afb20040 	sw	s2,64(sp)
9fc02a38:	afb1003c 	sw	s1,60(sp)
9fc02a3c:	afb00038 	sw	s0,56(sp)
9fc02a40:	0080a021 	move	s4,a0
9fc02a44:	afa50028 	sw	a1,40(sp)
9fc02a48:	afa6002c 	sw	a2,44(sp)
9fc02a4c:	afa70030 	sw	a3,48(sp)
9fc02a50:	108005ad 	beqz	a0,9fc04108 <matrix_test+0x16f8>
9fc02a54:	afa20034 	sw	v0,52(sp)
9fc02a58:	304dffff 	andi	t5,v0,0xffff
9fc02a5c:	00049040 	sll	s2,a0,0x1
9fc02a60:	afa60014 	sw	a2,20(sp)
9fc02a64:	00c07021 	move	t6,a2
9fc02a68:	00007821 	move	t7,zero
9fc02a6c:	95c90000 	lhu	t1,0(t6)
9fc02a70:	240c0001 	li	t4,1
9fc02a74:	01a95021 	addu	t2,t5,t1
9fc02a78:	269fffff 	addiu	ra,s4,-1
9fc02a7c:	0194f02b 	sltu	s8,t4,s4
9fc02a80:	a5ca0000 	sh	t2,0(t6)
9fc02a84:	33e30007 	andi	v1,ra,0x7
9fc02a88:	13c00050 	beqz	s8,9fc02bcc <matrix_test+0x1bc>
9fc02a8c:	25cb0002 	addiu	t3,t6,2
9fc02a90:	10600032 	beqz	v1,9fc02b5c <matrix_test+0x14c>
9fc02a94:	00000000 	nop
9fc02a98:	106c0029 	beq	v1,t4,9fc02b40 <matrix_test+0x130>
9fc02a9c:	24100002 	li	s0,2
9fc02aa0:	10700022 	beq	v1,s0,9fc02b2c <matrix_test+0x11c>
9fc02aa4:	24170003 	li	s7,3
9fc02aa8:	1077001b 	beq	v1,s7,9fc02b18 <matrix_test+0x108>
9fc02aac:	24080004 	li	t0,4
9fc02ab0:	10680014 	beq	v1,t0,9fc02b04 <matrix_test+0xf4>
9fc02ab4:	24150005 	li	s5,5
9fc02ab8:	1075000d 	beq	v1,s5,9fc02af0 <matrix_test+0xe0>
9fc02abc:	24160006 	li	s6,6
9fc02ac0:	10760006 	beq	v1,s6,9fc02adc <matrix_test+0xcc>
9fc02ac4:	00000000 	nop
9fc02ac8:	95730000 	lhu	s3,0(t3)
9fc02acc:	240c0002 	li	t4,2
9fc02ad0:	01b31821 	addu	v1,t5,s3
9fc02ad4:	a5630000 	sh	v1,0(t3)
9fc02ad8:	25cb0004 	addiu	t3,t6,4
9fc02adc:	95710000 	lhu	s1,0(t3)
9fc02ae0:	258c0001 	addiu	t4,t4,1
9fc02ae4:	01b1c821 	addu	t9,t5,s1
9fc02ae8:	a5790000 	sh	t9,0(t3)
9fc02aec:	256b0002 	addiu	t3,t3,2
9fc02af0:	95620000 	lhu	v0,0(t3)
9fc02af4:	258c0001 	addiu	t4,t4,1
9fc02af8:	01a2c021 	addu	t8,t5,v0
9fc02afc:	a5780000 	sh	t8,0(t3)
9fc02b00:	256b0002 	addiu	t3,t3,2
9fc02b04:	95690000 	lhu	t1,0(t3)
9fc02b08:	258c0001 	addiu	t4,t4,1
9fc02b0c:	01a9f021 	addu	s8,t5,t1
9fc02b10:	a57e0000 	sh	s8,0(t3)
9fc02b14:	256b0002 	addiu	t3,t3,2
9fc02b18:	95670000 	lhu	a3,0(t3)
9fc02b1c:	258c0001 	addiu	t4,t4,1
9fc02b20:	01a72821 	addu	a1,t5,a3
9fc02b24:	a5650000 	sh	a1,0(t3)
9fc02b28:	256b0002 	addiu	t3,t3,2
9fc02b2c:	956a0000 	lhu	t2,0(t3)
9fc02b30:	258c0001 	addiu	t4,t4,1
9fc02b34:	01aa3021 	addu	a2,t5,t2
9fc02b38:	a5660000 	sh	a2,0(t3)
9fc02b3c:	256b0002 	addiu	t3,t3,2
9fc02b40:	95700000 	lhu	s0,0(t3)
9fc02b44:	258c0001 	addiu	t4,t4,1
9fc02b48:	01b0f821 	addu	ra,t5,s0
9fc02b4c:	0194202b 	sltu	a0,t4,s4
9fc02b50:	a57f0000 	sh	ra,0(t3)
9fc02b54:	1080001d 	beqz	a0,9fc02bcc <matrix_test+0x1bc>
9fc02b58:	256b0002 	addiu	t3,t3,2
9fc02b5c:	95790000 	lhu	t9,0(t3)
9fc02b60:	95780002 	lhu	t8,2(t3)
9fc02b64:	95770004 	lhu	s7,4(t3)
9fc02b68:	95760006 	lhu	s6,6(t3)
9fc02b6c:	95750008 	lhu	s5,8(t3)
9fc02b70:	9573000a 	lhu	s3,10(t3)
9fc02b74:	9571000c 	lhu	s1,12(t3)
9fc02b78:	9570000e 	lhu	s0,14(t3)
9fc02b7c:	258c0008 	addiu	t4,t4,8
9fc02b80:	01b91021 	addu	v0,t5,t9
9fc02b84:	01b81821 	addu	v1,t5,t8
9fc02b88:	01b72021 	addu	a0,t5,s7
9fc02b8c:	01b62821 	addu	a1,t5,s6
9fc02b90:	01b53021 	addu	a2,t5,s5
9fc02b94:	01b33821 	addu	a3,t5,s3
9fc02b98:	01b14021 	addu	t0,t5,s1
9fc02b9c:	01b04821 	addu	t1,t5,s0
9fc02ba0:	0194502b 	sltu	t2,t4,s4
9fc02ba4:	a5620000 	sh	v0,0(t3)
9fc02ba8:	a5630002 	sh	v1,2(t3)
9fc02bac:	a5640004 	sh	a0,4(t3)
9fc02bb0:	a5650006 	sh	a1,6(t3)
9fc02bb4:	a5660008 	sh	a2,8(t3)
9fc02bb8:	a567000a 	sh	a3,10(t3)
9fc02bbc:	a568000c 	sh	t0,12(t3)
9fc02bc0:	a569000e 	sh	t1,14(t3)
9fc02bc4:	1540ffe5 	bnez	t2,9fc02b5c <matrix_test+0x14c>
9fc02bc8:	256b0010 	addiu	t3,t3,16
9fc02bcc:	25ef0001 	addiu	t7,t7,1
9fc02bd0:	01f4582b 	sltu	t3,t7,s4
9fc02bd4:	1560ffa5 	bnez	t3,9fc02a6c <matrix_test+0x5c>
9fc02bd8:	01d27021 	addu	t6,t6,s2
9fc02bdc:	8fac0028 	lw	t4,40(sp)
9fc02be0:	00146880 	sll	t5,s4,0x2
9fc02be4:	8faf002c 	lw	t7,44(sp)
9fc02be8:	afad001c 	sw	t5,28(sp)
9fc02bec:	afac0020 	sw	t4,32(sp)
9fc02bf0:	01807021 	move	t6,t4
9fc02bf4:	00008021 	move	s0,zero
9fc02bf8:	85f10000 	lh	s1,0(t7)
9fc02bfc:	8fa50034 	lw	a1,52(sp)
9fc02c00:	240d0001 	li	t5,1
9fc02c04:	00b10018 	mult	a1,s1
9fc02c08:	2695ffff 	addiu	s5,s4,-1
9fc02c0c:	01b4382b 	sltu	a3,t5,s4
9fc02c10:	32a30007 	andi	v1,s5,0x7
9fc02c14:	25eb0002 	addiu	t3,t7,2
9fc02c18:	25cc0004 	addiu	t4,t6,4
9fc02c1c:	00004012 	mflo	t0
9fc02c20:	10e00077 	beqz	a3,9fc02e00 <matrix_test+0x3f0>
9fc02c24:	adc80000 	sw	t0,0(t6)
9fc02c28:	10600047 	beqz	v1,9fc02d48 <matrix_test+0x338>
9fc02c2c:	00000000 	nop
9fc02c30:	106d003b 	beq	v1,t5,9fc02d20 <matrix_test+0x310>
9fc02c34:	241e0002 	li	s8,2
9fc02c38:	107e0031 	beq	v1,s8,9fc02d00 <matrix_test+0x2f0>
9fc02c3c:	24090003 	li	t1,3
9fc02c40:	10690027 	beq	v1,t1,9fc02ce0 <matrix_test+0x2d0>
9fc02c44:	24050004 	li	a1,4
9fc02c48:	1065001d 	beq	v1,a1,9fc02cc0 <matrix_test+0x2b0>
9fc02c4c:	24070005 	li	a3,5
9fc02c50:	10670013 	beq	v1,a3,9fc02ca0 <matrix_test+0x290>
9fc02c54:	24060006 	li	a2,6
9fc02c58:	10660009 	beq	v1,a2,9fc02c80 <matrix_test+0x270>
9fc02c5c:	00000000 	nop
9fc02c60:	85640000 	lh	a0,0(t3)
9fc02c64:	8fab0034 	lw	t3,52(sp)
9fc02c68:	240d0002 	li	t5,2
9fc02c6c:	01640018 	mult	t3,a0
9fc02c70:	25eb0004 	addiu	t3,t7,4
9fc02c74:	00005012 	mflo	t2
9fc02c78:	ad8a0000 	sw	t2,0(t4)
9fc02c7c:	25cc0008 	addiu	t4,t6,8
9fc02c80:	85770000 	lh	s7,0(t3)
9fc02c84:	8fa30034 	lw	v1,52(sp)
9fc02c88:	25ad0001 	addiu	t5,t5,1
9fc02c8c:	00770018 	mult	v1,s7
9fc02c90:	256b0002 	addiu	t3,t3,2
9fc02c94:	0000f812 	mflo	ra
9fc02c98:	ad9f0000 	sw	ra,0(t4)
9fc02c9c:	258c0004 	addiu	t4,t4,4
9fc02ca0:	85750000 	lh	s5,0(t3)
9fc02ca4:	8fb60034 	lw	s6,52(sp)
9fc02ca8:	25ad0001 	addiu	t5,t5,1
9fc02cac:	02d50018 	mult	s6,s5
9fc02cb0:	256b0002 	addiu	t3,t3,2
9fc02cb4:	00004012 	mflo	t0
9fc02cb8:	ad880000 	sw	t0,0(t4)
9fc02cbc:	258c0004 	addiu	t4,t4,4
9fc02cc0:	85790000 	lh	t9,0(t3)
9fc02cc4:	8fb10034 	lw	s1,52(sp)
9fc02cc8:	25ad0001 	addiu	t5,t5,1
9fc02ccc:	02390018 	mult	s1,t9
9fc02cd0:	256b0002 	addiu	t3,t3,2
9fc02cd4:	00009812 	mflo	s3
9fc02cd8:	ad930000 	sw	s3,0(t4)
9fc02cdc:	258c0004 	addiu	t4,t4,4
9fc02ce0:	85620000 	lh	v0,0(t3)
9fc02ce4:	8fbe0034 	lw	s8,52(sp)
9fc02ce8:	25ad0001 	addiu	t5,t5,1
9fc02cec:	03c20018 	mult	s8,v0
9fc02cf0:	256b0002 	addiu	t3,t3,2
9fc02cf4:	0000c012 	mflo	t8
9fc02cf8:	ad980000 	sw	t8,0(t4)
9fc02cfc:	258c0004 	addiu	t4,t4,4
9fc02d00:	85650000 	lh	a1,0(t3)
9fc02d04:	8fa70034 	lw	a3,52(sp)
9fc02d08:	25ad0001 	addiu	t5,t5,1
9fc02d0c:	00e50018 	mult	a3,a1
9fc02d10:	256b0002 	addiu	t3,t3,2
9fc02d14:	00004812 	mflo	t1
9fc02d18:	ad890000 	sw	t1,0(t4)
9fc02d1c:	258c0004 	addiu	t4,t4,4
9fc02d20:	85640000 	lh	a0,0(t3)
9fc02d24:	8fbf0034 	lw	ra,52(sp)
9fc02d28:	25ad0001 	addiu	t5,t5,1
9fc02d2c:	03e40018 	mult	ra,a0
9fc02d30:	01b4302b 	sltu	a2,t5,s4
9fc02d34:	256b0002 	addiu	t3,t3,2
9fc02d38:	00005012 	mflo	t2
9fc02d3c:	ad8a0000 	sw	t2,0(t4)
9fc02d40:	10c0002f 	beqz	a2,9fc02e00 <matrix_test+0x3f0>
9fc02d44:	258c0004 	addiu	t4,t4,4
9fc02d48:	85690000 	lh	t1,0(t3)
9fc02d4c:	8fb30034 	lw	s3,52(sp)
9fc02d50:	8fb10034 	lw	s1,52(sp)
9fc02d54:	02690018 	mult	s3,t1
9fc02d58:	856a0002 	lh	t2,2(t3)
9fc02d5c:	857f0004 	lh	ra,4(t3)
9fc02d60:	85790006 	lh	t9,6(t3)
9fc02d64:	85780008 	lh	t8,8(t3)
9fc02d68:	8577000a 	lh	s7,10(t3)
9fc02d6c:	8576000c 	lh	s6,12(t3)
9fc02d70:	8575000e 	lh	s5,14(t3)
9fc02d74:	25ad0008 	addiu	t5,t5,8
9fc02d78:	01b4982b 	sltu	s3,t5,s4
9fc02d7c:	256b0010 	addiu	t3,t3,16
9fc02d80:	00001012 	mflo	v0
9fc02d84:	ad820000 	sw	v0,0(t4)
9fc02d88:	00000000 	nop
9fc02d8c:	022a0018 	mult	s1,t2
9fc02d90:	0000f012 	mflo	s8
9fc02d94:	ad9e0004 	sw	s8,4(t4)
9fc02d98:	00000000 	nop
9fc02d9c:	023f0018 	mult	s1,ra
9fc02da0:	00002012 	mflo	a0
9fc02da4:	ad840008 	sw	a0,8(t4)
9fc02da8:	00000000 	nop
9fc02dac:	02390018 	mult	s1,t9
9fc02db0:	00001812 	mflo	v1
9fc02db4:	ad83000c 	sw	v1,12(t4)
9fc02db8:	00000000 	nop
9fc02dbc:	02380018 	mult	s1,t8
9fc02dc0:	00003012 	mflo	a2
9fc02dc4:	ad860010 	sw	a2,16(t4)
9fc02dc8:	00000000 	nop
9fc02dcc:	02370018 	mult	s1,s7
9fc02dd0:	00002812 	mflo	a1
9fc02dd4:	ad850014 	sw	a1,20(t4)
9fc02dd8:	00000000 	nop
9fc02ddc:	02360018 	mult	s1,s6
9fc02de0:	00004012 	mflo	t0
9fc02de4:	ad880018 	sw	t0,24(t4)
9fc02de8:	00000000 	nop
9fc02dec:	02350018 	mult	s1,s5
9fc02df0:	00003812 	mflo	a3
9fc02df4:	ad87001c 	sw	a3,28(t4)
9fc02df8:	1660ffd3 	bnez	s3,9fc02d48 <matrix_test+0x338>
9fc02dfc:	258c0020 	addiu	t4,t4,32
9fc02e00:	26100001 	addiu	s0,s0,1
9fc02e04:	8fac001c 	lw	t4,28(sp)
9fc02e08:	0214582b 	sltu	t3,s0,s4
9fc02e0c:	01cc7021 	addu	t6,t6,t4
9fc02e10:	1560ff79 	bnez	t3,9fc02bf8 <matrix_test+0x1e8>
9fc02e14:	01f27821 	addu	t7,t7,s2
9fc02e18:	8faf0034 	lw	t7,52(sp)
9fc02e1c:	240ef000 	li	t6,-4096
9fc02e20:	8fab0028 	lw	t3,40(sp)
9fc02e24:	01eef025 	or	s8,t7,t6
9fc02e28:	00003021 	move	a2,zero
9fc02e2c:	00001821 	move	v1,zero
9fc02e30:	00002021 	move	a0,zero
9fc02e34:	00006021 	move	t4,zero
9fc02e38:	2687ffff 	addiu	a3,s4,-1
9fc02e3c:	30ea0003 	andi	t2,a3,0x3
9fc02e40:	01604021 	move	t0,t3
9fc02e44:	1140003f 	beqz	t2,9fc02f44 <matrix_test+0x534>
9fc02e48:	00004821 	move	t1,zero
9fc02e4c:	8d670000 	lw	a3,0(t3)
9fc02e50:	3093ffff 	andi	s3,a0,0xffff
9fc02e54:	0067c82a 	slt	t9,v1,a3
9fc02e58:	00c73021 	addu	a2,a2,a3
9fc02e5c:	0279b821 	addu	s7,s3,t9
9fc02e60:	2676000a 	addiu	s6,s3,10
9fc02e64:	0016ac00 	sll	s5,s6,0x10
9fc02e68:	0017fc00 	sll	ra,s7,0x10
9fc02e6c:	03c6282a 	slt	a1,s8,a2
9fc02e70:	00151403 	sra	v0,s5,0x10
9fc02e74:	10a00002 	beqz	a1,9fc02e80 <matrix_test+0x470>
9fc02e78:	001f2403 	sra	a0,ra,0x10
9fc02e7c:	00402021 	move	a0,v0
9fc02e80:	10a00002 	beqz	a1,9fc02e8c <matrix_test+0x47c>
9fc02e84:	24090001 	li	t1,1
9fc02e88:	00003021 	move	a2,zero
9fc02e8c:	0134882b 	sltu	s1,t1,s4
9fc02e90:	1220007b 	beqz	s1,9fc03080 <matrix_test+0x670>
9fc02e94:	25680004 	addiu	t0,t3,4
9fc02e98:	1149002a 	beq	t2,t1,9fc02f44 <matrix_test+0x534>
9fc02e9c:	00e01821 	move	v1,a3
9fc02ea0:	24180002 	li	t8,2
9fc02ea4:	11580014 	beq	t2,t8,9fc02ef8 <matrix_test+0x4e8>
9fc02ea8:	308fffff 	andi	t7,a0,0xffff
9fc02eac:	8d050000 	lw	a1,0(t0)
9fc02eb0:	00000000 	nop
9fc02eb4:	00c55021 	addu	t2,a2,a1
9fc02eb8:	00e5302a 	slt	a2,a3,a1
9fc02ebc:	01e68021 	addu	s0,t7,a2
9fc02ec0:	25e7000a 	addiu	a3,t7,10
9fc02ec4:	00071400 	sll	v0,a3,0x10
9fc02ec8:	00107400 	sll	t6,s0,0x10
9fc02ecc:	03ca302a 	slt	a2,s8,t2
9fc02ed0:	00021403 	sra	v0,v0,0x10
9fc02ed4:	10c00002 	beqz	a2,9fc02ee0 <matrix_test+0x4d0>
9fc02ed8:	000e2403 	sra	a0,t6,0x10
9fc02edc:	00402021 	move	a0,v0
9fc02ee0:	14c00002 	bnez	a2,9fc02eec <matrix_test+0x4dc>
9fc02ee4:	00003021 	move	a2,zero
9fc02ee8:	01403021 	move	a2,t2
9fc02eec:	25290001 	addiu	t1,t1,1
9fc02ef0:	25080004 	addiu	t0,t0,4
9fc02ef4:	00a01821 	move	v1,a1
9fc02ef8:	8d050000 	lw	a1,0(t0)
9fc02efc:	3097ffff 	andi	s7,a0,0xffff
9fc02f00:	0065182a 	slt	v1,v1,a1
9fc02f04:	00c53821 	addu	a3,a2,a1
9fc02f08:	02e36821 	addu	t5,s7,v1
9fc02f0c:	26ff000a 	addiu	ra,s7,10
9fc02f10:	001f2400 	sll	a0,ra,0x10
9fc02f14:	000d5400 	sll	t2,t5,0x10
9fc02f18:	03c7302a 	slt	a2,s8,a3
9fc02f1c:	00041403 	sra	v0,a0,0x10
9fc02f20:	10c00002 	beqz	a2,9fc02f2c <matrix_test+0x51c>
9fc02f24:	000a2403 	sra	a0,t2,0x10
9fc02f28:	00402021 	move	a0,v0
9fc02f2c:	14c00002 	bnez	a2,9fc02f38 <matrix_test+0x528>
9fc02f30:	00003021 	move	a2,zero
9fc02f34:	00e03021 	move	a2,a3
9fc02f38:	25290001 	addiu	t1,t1,1
9fc02f3c:	25080004 	addiu	t0,t0,4
9fc02f40:	00a01821 	move	v1,a1
9fc02f44:	8d070000 	lw	a3,0(t0)
9fc02f48:	3082ffff 	andi	v0,a0,0xffff
9fc02f4c:	0067502a 	slt	t2,v1,a3
9fc02f50:	004a1821 	addu	v1,v0,t2
9fc02f54:	245f000a 	addiu	ra,v0,10
9fc02f58:	00c73021 	addu	a2,a2,a3
9fc02f5c:	001fcc00 	sll	t9,ra,0x10
9fc02f60:	00032400 	sll	a0,v1,0x10
9fc02f64:	03c6282a 	slt	a1,s8,a2
9fc02f68:	00191403 	sra	v0,t9,0x10
9fc02f6c:	10a00002 	beqz	a1,9fc02f78 <matrix_test+0x568>
9fc02f70:	00042403 	sra	a0,a0,0x10
9fc02f74:	00402021 	move	a0,v0
9fc02f78:	10a00002 	beqz	a1,9fc02f84 <matrix_test+0x574>
9fc02f7c:	00000000 	nop
9fc02f80:	00003021 	move	a2,zero
9fc02f84:	25290001 	addiu	t1,t1,1
9fc02f88:	0134982b 	sltu	s3,t1,s4
9fc02f8c:	1260003c 	beqz	s3,9fc03080 <matrix_test+0x670>
9fc02f90:	250a0004 	addiu	t2,t0,4
9fc02f94:	8d050004 	lw	a1,4(t0)
9fc02f98:	3098ffff 	andi	t8,a0,0xffff
9fc02f9c:	00c54021 	addu	t0,a2,a1
9fc02fa0:	00e5302a 	slt	a2,a3,a1
9fc02fa4:	03066821 	addu	t5,t8,a2
9fc02fa8:	2717000a 	addiu	s7,t8,10
9fc02fac:	0017b400 	sll	s6,s7,0x10
9fc02fb0:	000d8400 	sll	s0,t5,0x10
9fc02fb4:	03c8302a 	slt	a2,s8,t0
9fc02fb8:	00162403 	sra	a0,s6,0x10
9fc02fbc:	10c00002 	beqz	a2,9fc02fc8 <matrix_test+0x5b8>
9fc02fc0:	00101403 	sra	v0,s0,0x10
9fc02fc4:	00801021 	move	v0,a0
9fc02fc8:	14c00002 	bnez	a2,9fc02fd4 <matrix_test+0x5c4>
9fc02fcc:	00003821 	move	a3,zero
9fc02fd0:	01003821 	move	a3,t0
9fc02fd4:	8d460004 	lw	a2,4(t2)
9fc02fd8:	3059ffff 	andi	t9,v0,0xffff
9fc02fdc:	00a6882a 	slt	s1,a1,a2
9fc02fe0:	00e63821 	addu	a3,a3,a2
9fc02fe4:	0331a821 	addu	s5,t9,s1
9fc02fe8:	2733000a 	addiu	s3,t9,10
9fc02fec:	0013b400 	sll	s6,s3,0x10
9fc02ff0:	00154400 	sll	t0,s5,0x10
9fc02ff4:	03c7282a 	slt	a1,s8,a3
9fc02ff8:	00162403 	sra	a0,s6,0x10
9fc02ffc:	10a00002 	beqz	a1,9fc03008 <matrix_test+0x5f8>
9fc03000:	00081403 	sra	v0,t0,0x10
9fc03004:	00801021 	move	v0,a0
9fc03008:	10a00002 	beqz	a1,9fc03014 <matrix_test+0x604>
9fc0300c:	00000000 	nop
9fc03010:	00003821 	move	a3,zero
9fc03014:	8d450008 	lw	a1,8(t2)
9fc03018:	304fffff 	andi	t7,v0,0xffff
9fc0301c:	00c5682a 	slt	t5,a2,a1
9fc03020:	25e2000a 	addiu	v0,t7,10
9fc03024:	00e53821 	addu	a3,a3,a1
9fc03028:	01ed7021 	addu	t6,t7,t5
9fc0302c:	00028400 	sll	s0,v0,0x10
9fc03030:	000ec400 	sll	t8,t6,0x10
9fc03034:	03c7302a 	slt	a2,s8,a3
9fc03038:	00101403 	sra	v0,s0,0x10
9fc0303c:	10c00002 	beqz	a2,9fc03048 <matrix_test+0x638>
9fc03040:	00182403 	sra	a0,t8,0x10
9fc03044:	00402021 	move	a0,v0
9fc03048:	14c00009 	bnez	a2,9fc03070 <matrix_test+0x660>
9fc0304c:	00000000 	nop
9fc03050:	00e03021 	move	a2,a3
9fc03054:	25290003 	addiu	t1,t1,3
9fc03058:	2548000c 	addiu	t0,t2,12
9fc0305c:	0bf00bd1 	j	9fc02f44 <matrix_test+0x534>
9fc03060:	00a01821 	move	v1,a1
	...
9fc03070:	0bf00c15 	j	9fc03054 <matrix_test+0x644>
9fc03074:	00003021 	move	a2,zero
	...
9fc03080:	258c0001 	addiu	t4,t4,1
9fc03084:	8fa8001c 	lw	t0,28(sp)
9fc03088:	0194482b 	sltu	t1,t4,s4
9fc0308c:	11200003 	beqz	t1,9fc0309c <matrix_test+0x68c>
9fc03090:	01685821 	addu	t3,t3,t0
9fc03094:	0bf00b8e 	j	9fc02e38 <matrix_test+0x428>
9fc03098:	00e01821 	move	v1,a3
9fc0309c:	0ff01d20 	jal	9fc07480 <crc16>
9fc030a0:	00002821 	move	a1,zero
9fc030a4:	8fb60028 	lw	s6,40(sp)
9fc030a8:	8fb5002c 	lw	s5,44(sp)
9fc030ac:	0040f821 	move	ra,v0
9fc030b0:	0000b821 	move	s7,zero
9fc030b4:	8faf0030 	lw	t7,48(sp)
9fc030b8:	86a50000 	lh	a1,0(s5)
9fc030bc:	85ee0000 	lh	t6,0(t7)
9fc030c0:	24130001 	li	s3,1
9fc030c4:	01c50018 	mult	t6,a1
9fc030c8:	8fac0030 	lw	t4,48(sp)
9fc030cc:	2691ffff 	addiu	s1,s4,-1
9fc030d0:	0274582b 	sltu	t3,s3,s4
9fc030d4:	32230007 	andi	v1,s1,0x7
9fc030d8:	25980002 	addiu	t8,t4,2
9fc030dc:	0000c812 	mflo	t9
9fc030e0:	1160007f 	beqz	t3,9fc032e0 <matrix_test+0x8d0>
9fc030e4:	26b10002 	addiu	s1,s5,2
9fc030e8:	10600048 	beqz	v1,9fc0320c <matrix_test+0x7fc>
9fc030ec:	00000000 	nop
9fc030f0:	1073003c 	beq	v1,s3,9fc031e4 <matrix_test+0x7d4>
9fc030f4:	240b0002 	li	t3,2
9fc030f8:	106b0032 	beq	v1,t3,9fc031c4 <matrix_test+0x7b4>
9fc030fc:	24090003 	li	t1,3
9fc03100:	10690028 	beq	v1,t1,9fc031a4 <matrix_test+0x794>
9fc03104:	24050004 	li	a1,4
9fc03108:	1065001e 	beq	v1,a1,9fc03184 <matrix_test+0x774>
9fc0310c:	240c0005 	li	t4,5
9fc03110:	106c0014 	beq	v1,t4,9fc03164 <matrix_test+0x754>
9fc03114:	24070006 	li	a3,6
9fc03118:	1067000a 	beq	v1,a3,9fc03144 <matrix_test+0x734>
9fc0311c:	00000000 	nop
9fc03120:	8faa0030 	lw	t2,48(sp)
9fc03124:	86380000 	lh	t8,0(s1)
9fc03128:	85480002 	lh	t0,2(t2)
9fc0312c:	24130002 	li	s3,2
9fc03130:	01180018 	mult	t0,t8
9fc03134:	26b10004 	addiu	s1,s5,4
9fc03138:	25580004 	addiu	t8,t2,4
9fc0313c:	00003012 	mflo	a2
9fc03140:	0326c821 	addu	t9,t9,a2
9fc03144:	870d0000 	lh	t5,0(t8)
9fc03148:	86240000 	lh	a0,0(s1)
9fc0314c:	26730001 	addiu	s3,s3,1
9fc03150:	01a40018 	mult	t5,a0
9fc03154:	26310002 	addiu	s1,s1,2
9fc03158:	27180002 	addiu	t8,t8,2
9fc0315c:	00007812 	mflo	t7
9fc03160:	032fc821 	addu	t9,t9,t7
9fc03164:	87020000 	lh	v0,0(t8)
9fc03168:	862e0000 	lh	t6,0(s1)
9fc0316c:	26730001 	addiu	s3,s3,1
9fc03170:	004e0018 	mult	v0,t6
9fc03174:	26310002 	addiu	s1,s1,2
9fc03178:	27180002 	addiu	t8,t8,2
9fc0317c:	00001812 	mflo	v1
9fc03180:	0323c821 	addu	t9,t9,v1
9fc03184:	870b0000 	lh	t3,0(t8)
9fc03188:	86290000 	lh	t1,0(s1)
9fc0318c:	26730001 	addiu	s3,s3,1
9fc03190:	01690018 	mult	t3,t1
9fc03194:	26310002 	addiu	s1,s1,2
9fc03198:	27180002 	addiu	t8,t8,2
9fc0319c:	00008012 	mflo	s0
9fc031a0:	0330c821 	addu	t9,t9,s0
9fc031a4:	870c0000 	lh	t4,0(t8)
9fc031a8:	86270000 	lh	a3,0(s1)
9fc031ac:	26730001 	addiu	s3,s3,1
9fc031b0:	01870018 	mult	t4,a3
9fc031b4:	26310002 	addiu	s1,s1,2
9fc031b8:	27180002 	addiu	t8,t8,2
9fc031bc:	00002812 	mflo	a1
9fc031c0:	0325c821 	addu	t9,t9,a1
9fc031c4:	870a0000 	lh	t2,0(t8)
9fc031c8:	86280000 	lh	t0,0(s1)
9fc031cc:	26730001 	addiu	s3,s3,1
9fc031d0:	01480018 	mult	t2,t0
9fc031d4:	26310002 	addiu	s1,s1,2
9fc031d8:	27180002 	addiu	t8,t8,2
9fc031dc:	00003012 	mflo	a2
9fc031e0:	0326c821 	addu	t9,t9,a2
9fc031e4:	87040000 	lh	a0,0(t8)
9fc031e8:	86230000 	lh	v1,0(s1)
9fc031ec:	26730001 	addiu	s3,s3,1
9fc031f0:	00830018 	mult	a0,v1
9fc031f4:	0274782b 	sltu	t7,s3,s4
9fc031f8:	26310002 	addiu	s1,s1,2
9fc031fc:	27180002 	addiu	t8,t8,2
9fc03200:	00006812 	mflo	t5
9fc03204:	11e00036 	beqz	t7,9fc032e0 <matrix_test+0x8d0>
9fc03208:	032dc821 	addu	t9,t9,t5
9fc0320c:	87080000 	lh	t0,0(t8)
9fc03210:	86230000 	lh	v1,0(s1)
9fc03214:	87100002 	lh	s0,2(t8)
9fc03218:	01030018 	mult	t0,v1
9fc0321c:	862a0002 	lh	t2,2(s1)
9fc03220:	87040004 	lh	a0,4(t8)
9fc03224:	862b0004 	lh	t3,4(s1)
9fc03228:	87050006 	lh	a1,6(t8)
9fc0322c:	862c0006 	lh	t4,6(s1)
9fc03230:	87060008 	lh	a2,8(t8)
9fc03234:	862d0008 	lh	t5,8(s1)
9fc03238:	8707000a 	lh	a3,10(t8)
9fc0323c:	862e000a 	lh	t6,10(s1)
9fc03240:	8708000c 	lh	t0,12(t8)
9fc03244:	862f000c 	lh	t7,12(s1)
9fc03248:	8703000e 	lh	v1,14(t8)
9fc0324c:	00001012 	mflo	v0
9fc03250:	26730008 	addiu	s3,s3,8
9fc03254:	03221021 	addu	v0,t9,v0
9fc03258:	27180010 	addiu	t8,t8,16
9fc0325c:	020a0018 	mult	s0,t2
9fc03260:	8630000e 	lh	s0,14(s1)
9fc03264:	0274502b 	sltu	t2,s3,s4
9fc03268:	26310010 	addiu	s1,s1,16
9fc0326c:	00004812 	mflo	t1
9fc03270:	0049c821 	addu	t9,v0,t1
9fc03274:	00000000 	nop
9fc03278:	008b0018 	mult	a0,t3
9fc0327c:	00002012 	mflo	a0
	...
9fc03288:	00ac0018 	mult	a1,t4
9fc0328c:	03246021 	addu	t4,t9,a0
9fc03290:	00002812 	mflo	a1
9fc03294:	01851021 	addu	v0,t4,a1
9fc03298:	00000000 	nop
9fc0329c:	00cd0018 	mult	a2,t5
9fc032a0:	00005812 	mflo	t3
9fc032a4:	004bc821 	addu	t9,v0,t3
9fc032a8:	00000000 	nop
9fc032ac:	00ee0018 	mult	a3,t6
9fc032b0:	00004812 	mflo	t1
9fc032b4:	03293021 	addu	a2,t9,t1
9fc032b8:	00000000 	nop
9fc032bc:	010f0018 	mult	t0,t7
9fc032c0:	00002012 	mflo	a0
	...
9fc032cc:	00700018 	mult	v1,s0
9fc032d0:	00c48021 	addu	s0,a2,a0
9fc032d4:	00006812 	mflo	t5
9fc032d8:	1540ffcc 	bnez	t2,9fc0320c <matrix_test+0x7fc>
9fc032dc:	020dc821 	addu	t9,s0,t5
9fc032e0:	26f70001 	addiu	s7,s7,1
9fc032e4:	02f4c02b 	sltu	t8,s7,s4
9fc032e8:	aed90000 	sw	t9,0(s6)
9fc032ec:	02b2a821 	addu	s5,s5,s2
9fc032f0:	1700ff70 	bnez	t8,9fc030b4 <matrix_test+0x6a4>
9fc032f4:	26d60004 	addiu	s6,s6,4
9fc032f8:	8fab0028 	lw	t3,40(sp)
9fc032fc:	00003021 	move	a2,zero
9fc03300:	00001821 	move	v1,zero
9fc03304:	00002021 	move	a0,zero
9fc03308:	00006021 	move	t4,zero
9fc0330c:	2687ffff 	addiu	a3,s4,-1
9fc03310:	30ea0003 	andi	t2,a3,0x3
9fc03314:	01604021 	move	t0,t3
9fc03318:	1140003f 	beqz	t2,9fc03418 <matrix_test+0xa08>
9fc0331c:	00004821 	move	t1,zero
9fc03320:	8d670000 	lw	a3,0(t3)
9fc03324:	3099ffff 	andi	t9,a0,0xffff
9fc03328:	0067c02a 	slt	t8,v1,a3
9fc0332c:	00c73021 	addu	a2,a2,a3
9fc03330:	0338a821 	addu	s5,t9,t8
9fc03334:	2733000a 	addiu	s3,t9,10
9fc03338:	0013b400 	sll	s6,s3,0x10
9fc0333c:	0015bc00 	sll	s7,s5,0x10
9fc03340:	03c6282a 	slt	a1,s8,a2
9fc03344:	00161403 	sra	v0,s6,0x10
9fc03348:	10a00002 	beqz	a1,9fc03354 <matrix_test+0x944>
9fc0334c:	00172403 	sra	a0,s7,0x10
9fc03350:	00402021 	move	a0,v0
9fc03354:	10a00002 	beqz	a1,9fc03360 <matrix_test+0x950>
9fc03358:	24090001 	li	t1,1
9fc0335c:	00003021 	move	a2,zero
9fc03360:	0134782b 	sltu	t7,t1,s4
9fc03364:	11e0007a 	beqz	t7,9fc03550 <matrix_test+0xb40>
9fc03368:	25680004 	addiu	t0,t3,4
9fc0336c:	1149002a 	beq	t2,t1,9fc03418 <matrix_test+0xa08>
9fc03370:	00e01821 	move	v1,a3
9fc03374:	240d0002 	li	t5,2
9fc03378:	114d0014 	beq	t2,t5,9fc033cc <matrix_test+0x9bc>
9fc0337c:	308effff 	andi	t6,a0,0xffff
9fc03380:	8d050000 	lw	a1,0(t0)
9fc03384:	00000000 	nop
9fc03388:	00c55021 	addu	t2,a2,a1
9fc0338c:	00e5302a 	slt	a2,a3,a1
9fc03390:	01c61821 	addu	v1,t6,a2
9fc03394:	25c2000a 	addiu	v0,t6,10
9fc03398:	00022400 	sll	a0,v0,0x10
9fc0339c:	00033c00 	sll	a3,v1,0x10
9fc033a0:	03ca302a 	slt	a2,s8,t2
9fc033a4:	00041403 	sra	v0,a0,0x10
9fc033a8:	10c00002 	beqz	a2,9fc033b4 <matrix_test+0x9a4>
9fc033ac:	00072403 	sra	a0,a3,0x10
9fc033b0:	00402021 	move	a0,v0
9fc033b4:	14c00002 	bnez	a2,9fc033c0 <matrix_test+0x9b0>
9fc033b8:	00003021 	move	a2,zero
9fc033bc:	01403021 	move	a2,t2
9fc033c0:	25290001 	addiu	t1,t1,1
9fc033c4:	25080004 	addiu	t0,t0,4
9fc033c8:	00a01821 	move	v1,a1
9fc033cc:	8d050000 	lw	a1,0(t0)
9fc033d0:	3095ffff 	andi	s5,a0,0xffff
9fc033d4:	0065b02a 	slt	s6,v1,a1
9fc033d8:	00c53821 	addu	a3,a2,a1
9fc033dc:	02b68021 	addu	s0,s5,s6
9fc033e0:	26b7000a 	addiu	s7,s5,10
9fc033e4:	00178c00 	sll	s1,s7,0x10
9fc033e8:	00105400 	sll	t2,s0,0x10
9fc033ec:	03c7302a 	slt	a2,s8,a3
9fc033f0:	00111403 	sra	v0,s1,0x10
9fc033f4:	10c00002 	beqz	a2,9fc03400 <matrix_test+0x9f0>
9fc033f8:	000a2403 	sra	a0,t2,0x10
9fc033fc:	00402021 	move	a0,v0
9fc03400:	14c00002 	bnez	a2,9fc0340c <matrix_test+0x9fc>
9fc03404:	00003021 	move	a2,zero
9fc03408:	00e03021 	move	a2,a3
9fc0340c:	25290001 	addiu	t1,t1,1
9fc03410:	25080004 	addiu	t0,t0,4
9fc03414:	00a01821 	move	v1,a1
9fc03418:	8d070000 	lw	a3,0(t0)
9fc0341c:	3085ffff 	andi	a1,a0,0xffff
9fc03420:	0067102a 	slt	v0,v1,a3
9fc03424:	00a26821 	addu	t5,a1,v0
9fc03428:	24a4000a 	addiu	a0,a1,10
9fc0342c:	00c73021 	addu	a2,a2,a3
9fc03430:	0004cc00 	sll	t9,a0,0x10
9fc03434:	000d7400 	sll	t6,t5,0x10
9fc03438:	03c6282a 	slt	a1,s8,a2
9fc0343c:	00191403 	sra	v0,t9,0x10
9fc03440:	10a00002 	beqz	a1,9fc0344c <matrix_test+0xa3c>
9fc03444:	000e2403 	sra	a0,t6,0x10
9fc03448:	00402021 	move	a0,v0
9fc0344c:	10a00002 	beqz	a1,9fc03458 <matrix_test+0xa48>
9fc03450:	00000000 	nop
9fc03454:	00003021 	move	a2,zero
9fc03458:	25290001 	addiu	t1,t1,1
9fc0345c:	0134802b 	sltu	s0,t1,s4
9fc03460:	1200003b 	beqz	s0,9fc03550 <matrix_test+0xb40>
9fc03464:	250a0004 	addiu	t2,t0,4
9fc03468:	8d050004 	lw	a1,4(t0)
9fc0346c:	3091ffff 	andi	s1,a0,0xffff
9fc03470:	00e5782a 	slt	t7,a3,a1
9fc03474:	00c54021 	addu	t0,a2,a1
9fc03478:	022fb021 	addu	s6,s1,t7
9fc0347c:	2633000a 	addiu	s3,s1,10
9fc03480:	0013bc00 	sll	s7,s3,0x10
9fc03484:	0016ac00 	sll	s5,s6,0x10
9fc03488:	03c8302a 	slt	a2,s8,t0
9fc0348c:	00172403 	sra	a0,s7,0x10
9fc03490:	10c00002 	beqz	a2,9fc0349c <matrix_test+0xa8c>
9fc03494:	00151403 	sra	v0,s5,0x10
9fc03498:	00801021 	move	v0,a0
9fc0349c:	14c00002 	bnez	a2,9fc034a8 <matrix_test+0xa98>
9fc034a0:	00003821 	move	a3,zero
9fc034a4:	01003821 	move	a3,t0
9fc034a8:	8d460004 	lw	a2,4(t2)
9fc034ac:	304fffff 	andi	t7,v0,0xffff
9fc034b0:	00a6682a 	slt	t5,a1,a2
9fc034b4:	00e63821 	addu	a3,a3,a2
9fc034b8:	01ed9821 	addu	s3,t7,t5
9fc034bc:	25f8000a 	addiu	t8,t7,10
9fc034c0:	0018cc00 	sll	t9,t8,0x10
9fc034c4:	00134400 	sll	t0,s3,0x10
9fc034c8:	03c7282a 	slt	a1,s8,a3
9fc034cc:	00192403 	sra	a0,t9,0x10
9fc034d0:	10a00002 	beqz	a1,9fc034dc <matrix_test+0xacc>
9fc034d4:	00081403 	sra	v0,t0,0x10
9fc034d8:	00801021 	move	v0,a0
9fc034dc:	10a00002 	beqz	a1,9fc034e8 <matrix_test+0xad8>
9fc034e0:	00000000 	nop
9fc034e4:	00003821 	move	a3,zero
9fc034e8:	8d450008 	lw	a1,8(t2)
9fc034ec:	3050ffff 	andi	s0,v0,0xffff
9fc034f0:	00c5882a 	slt	s1,a2,a1
9fc034f4:	00e53821 	addu	a3,a3,a1
9fc034f8:	02111821 	addu	v1,s0,s1
9fc034fc:	260e000a 	addiu	t6,s0,10
9fc03500:	000e1400 	sll	v0,t6,0x10
9fc03504:	00032400 	sll	a0,v1,0x10
9fc03508:	03c7302a 	slt	a2,s8,a3
9fc0350c:	00021403 	sra	v0,v0,0x10
9fc03510:	10c00002 	beqz	a2,9fc0351c <matrix_test+0xb0c>
9fc03514:	00042403 	sra	a0,a0,0x10
9fc03518:	00402021 	move	a0,v0
9fc0351c:	14c00008 	bnez	a2,9fc03540 <matrix_test+0xb30>
9fc03520:	00000000 	nop
9fc03524:	00e03021 	move	a2,a3
9fc03528:	25290003 	addiu	t1,t1,3
9fc0352c:	2548000c 	addiu	t0,t2,12
9fc03530:	0bf00d06 	j	9fc03418 <matrix_test+0xa08>
9fc03534:	00a01821 	move	v1,a1
	...
9fc03540:	0bf00d4a 	j	9fc03528 <matrix_test+0xb18>
9fc03544:	00003021 	move	a2,zero
	...
9fc03550:	258c0001 	addiu	t4,t4,1
9fc03554:	8faa001c 	lw	t2,28(sp)
9fc03558:	0194482b 	sltu	t1,t4,s4
9fc0355c:	11200003 	beqz	t1,9fc0356c <matrix_test+0xb5c>
9fc03560:	016a5821 	addu	t3,t3,t2
9fc03564:	0bf00cc3 	j	9fc0330c <matrix_test+0x8fc>
9fc03568:	00e01821 	move	v1,a3
9fc0356c:	03e02821 	move	a1,ra
9fc03570:	0ff01d20 	jal	9fc07480 <crc16>
9fc03574:	00000000 	nop
9fc03578:	8fa60028 	lw	a2,40(sp)
9fc0357c:	8fb7002c 	lw	s7,44(sp)
9fc03580:	afa20018 	sw	v0,24(sp)
9fc03584:	afa60010 	sw	a2,16(sp)
9fc03588:	afa00024 	sw	zero,36(sp)
9fc0358c:	8fb60010 	lw	s6,16(sp)
9fc03590:	8fb30030 	lw	s3,48(sp)
9fc03594:	0000a821 	move	s5,zero
9fc03598:	86ff0000 	lh	ra,0(s7)
9fc0359c:	86680000 	lh	t0,0(s3)
9fc035a0:	24190001 	li	t9,1
9fc035a4:	011f0018 	mult	t0,ra
9fc035a8:	268bffff 	addiu	t3,s4,-1
9fc035ac:	0334602b 	sltu	t4,t9,s4
9fc035b0:	31630007 	andi	v1,t3,0x7
9fc035b4:	26f80002 	addiu	t8,s7,2
9fc035b8:	0000f812 	mflo	ra
9fc035bc:	11800084 	beqz	t4,9fc037d0 <matrix_test+0xdc0>
9fc035c0:	02725021 	addu	t2,s3,s2
9fc035c4:	10600047 	beqz	v1,9fc036e4 <matrix_test+0xcd4>
9fc035c8:	00000000 	nop
9fc035cc:	1079003b 	beq	v1,t9,9fc036bc <matrix_test+0xcac>
9fc035d0:	240b0002 	li	t3,2
9fc035d4:	106b0031 	beq	v1,t3,9fc0369c <matrix_test+0xc8c>
9fc035d8:	24090003 	li	t1,3
9fc035dc:	10690027 	beq	v1,t1,9fc0367c <matrix_test+0xc6c>
9fc035e0:	24050004 	li	a1,4
9fc035e4:	1065001d 	beq	v1,a1,9fc0365c <matrix_test+0xc4c>
9fc035e8:	240c0005 	li	t4,5
9fc035ec:	106c0013 	beq	v1,t4,9fc0363c <matrix_test+0xc2c>
9fc035f0:	24070006 	li	a3,6
9fc035f4:	10670009 	beq	v1,a3,9fc0361c <matrix_test+0xc0c>
9fc035f8:	00000000 	nop
9fc035fc:	87020000 	lh	v0,0(t8)
9fc03600:	85440000 	lh	a0,0(t2)
9fc03604:	24190002 	li	t9,2
9fc03608:	00820018 	mult	a0,v0
9fc0360c:	01525021 	addu	t2,t2,s2
9fc03610:	26f80004 	addiu	t8,s7,4
9fc03614:	00003012 	mflo	a2
9fc03618:	03e6f821 	addu	ra,ra,a2
9fc0361c:	854e0000 	lh	t6,0(t2)
9fc03620:	87100000 	lh	s0,0(t8)
9fc03624:	27390001 	addiu	t9,t9,1
9fc03628:	01d00018 	mult	t6,s0
9fc0362c:	27180002 	addiu	t8,t8,2
9fc03630:	01525021 	addu	t2,t2,s2
9fc03634:	00004012 	mflo	t0
9fc03638:	03e8f821 	addu	ra,ra,t0
9fc0363c:	854f0000 	lh	t7,0(t2)
9fc03640:	870d0000 	lh	t5,0(t8)
9fc03644:	27390001 	addiu	t9,t9,1
9fc03648:	01ed0018 	mult	t7,t5
9fc0364c:	27180002 	addiu	t8,t8,2
9fc03650:	01525021 	addu	t2,t2,s2
9fc03654:	00008812 	mflo	s1
9fc03658:	03f1f821 	addu	ra,ra,s1
9fc0365c:	854b0000 	lh	t3,0(t2)
9fc03660:	87090000 	lh	t1,0(t8)
9fc03664:	27390001 	addiu	t9,t9,1
9fc03668:	01690018 	mult	t3,t1
9fc0366c:	27180002 	addiu	t8,t8,2
9fc03670:	01525021 	addu	t2,t2,s2
9fc03674:	00001812 	mflo	v1
9fc03678:	03e3f821 	addu	ra,ra,v1
9fc0367c:	854c0000 	lh	t4,0(t2)
9fc03680:	87070000 	lh	a3,0(t8)
9fc03684:	27390001 	addiu	t9,t9,1
9fc03688:	01870018 	mult	t4,a3
9fc0368c:	27180002 	addiu	t8,t8,2
9fc03690:	01525021 	addu	t2,t2,s2
9fc03694:	00002812 	mflo	a1
9fc03698:	03e5f821 	addu	ra,ra,a1
9fc0369c:	85440000 	lh	a0,0(t2)
9fc036a0:	87020000 	lh	v0,0(t8)
9fc036a4:	27390001 	addiu	t9,t9,1
9fc036a8:	00820018 	mult	a0,v0
9fc036ac:	27180002 	addiu	t8,t8,2
9fc036b0:	01525021 	addu	t2,t2,s2
9fc036b4:	00003012 	mflo	a2
9fc036b8:	03e6f821 	addu	ra,ra,a2
9fc036bc:	85500000 	lh	s0,0(t2)
9fc036c0:	87110000 	lh	s1,0(t8)
9fc036c4:	27390001 	addiu	t9,t9,1
9fc036c8:	02110018 	mult	s0,s1
9fc036cc:	0334402b 	sltu	t0,t9,s4
9fc036d0:	27180002 	addiu	t8,t8,2
9fc036d4:	01525021 	addu	t2,t2,s2
9fc036d8:	00007012 	mflo	t6
9fc036dc:	1100003c 	beqz	t0,9fc037d0 <matrix_test+0xdc0>
9fc036e0:	03eef821 	addu	ra,ra,t6
9fc036e4:	854f0000 	lh	t7,0(t2)
9fc036e8:	87080000 	lh	t0,0(t8)
9fc036ec:	01523821 	addu	a3,t2,s2
9fc036f0:	01e80018 	mult	t7,t0
9fc036f4:	870d0002 	lh	t5,2(t8)
9fc036f8:	84ec0000 	lh	t4,0(a3)
9fc036fc:	00f27021 	addu	t6,a3,s2
9fc03700:	87110004 	lh	s1,4(t8)
9fc03704:	85c90000 	lh	t1,0(t6)
9fc03708:	01d23021 	addu	a2,t6,s2
9fc0370c:	87100006 	lh	s0,6(t8)
9fc03710:	84c80000 	lh	t0,0(a2)
9fc03714:	00d22021 	addu	a0,a2,s2
9fc03718:	870f0008 	lh	t7,8(t8)
9fc0371c:	84870000 	lh	a3,0(a0)
9fc03720:	00922821 	addu	a1,a0,s2
9fc03724:	00001012 	mflo	v0
9fc03728:	870e000a 	lh	t6,10(t8)
9fc0372c:	03e21021 	addu	v0,ra,v0
9fc03730:	84a60000 	lh	a2,0(a1)
9fc03734:	018d0018 	mult	t4,t5
9fc03738:	00b21821 	addu	v1,a1,s2
9fc0373c:	870c000c 	lh	t4,12(t8)
9fc03740:	84640000 	lh	a0,0(v1)
9fc03744:	00725021 	addu	t2,v1,s2
9fc03748:	85430000 	lh	v1,0(t2)
9fc0374c:	870d000e 	lh	t5,14(t8)
9fc03750:	27390008 	addiu	t9,t9,8
9fc03754:	0334282b 	sltu	a1,t9,s4
9fc03758:	27180010 	addiu	t8,t8,16
9fc0375c:	01525021 	addu	t2,t2,s2
9fc03760:	00005812 	mflo	t3
	...
9fc0376c:	01310018 	mult	t1,s1
9fc03770:	004b8821 	addu	s1,v0,t3
9fc03774:	00004812 	mflo	t1
9fc03778:	02295821 	addu	t3,s1,t1
9fc0377c:	00000000 	nop
9fc03780:	01100018 	mult	t0,s0
9fc03784:	0000f812 	mflo	ra
9fc03788:	017f4821 	addu	t1,t3,ra
9fc0378c:	00000000 	nop
9fc03790:	00ef0018 	mult	a3,t7
9fc03794:	00003812 	mflo	a3
9fc03798:	01271021 	addu	v0,t1,a3
9fc0379c:	00000000 	nop
9fc037a0:	00ce0018 	mult	a2,t6
9fc037a4:	00008012 	mflo	s0
9fc037a8:	00507821 	addu	t7,v0,s0
9fc037ac:	00000000 	nop
9fc037b0:	008c0018 	mult	a0,t4
9fc037b4:	00007012 	mflo	t6
9fc037b8:	01ee8821 	addu	s1,t7,t6
9fc037bc:	00000000 	nop
9fc037c0:	006d0018 	mult	v1,t5
9fc037c4:	00001812 	mflo	v1
9fc037c8:	14a0ffc6 	bnez	a1,9fc036e4 <matrix_test+0xcd4>
9fc037cc:	0223f821 	addu	ra,s1,v1
9fc037d0:	26b50001 	addiu	s5,s5,1
9fc037d4:	02b4c02b 	sltu	t8,s5,s4
9fc037d8:	aedf0000 	sw	ra,0(s6)
9fc037dc:	26730002 	addiu	s3,s3,2
9fc037e0:	1700ff6d 	bnez	t8,9fc03598 <matrix_test+0xb88>
9fc037e4:	26d60004 	addiu	s6,s6,4
9fc037e8:	8fb00024 	lw	s0,36(sp)
9fc037ec:	8fb30010 	lw	s3,16(sp)
9fc037f0:	8fb9001c 	lw	t9,28(sp)
9fc037f4:	261f0001 	addiu	ra,s0,1
9fc037f8:	0279b021 	addu	s6,s3,t9
9fc037fc:	03f4a82b 	sltu	s5,ra,s4
9fc03800:	afbf0024 	sw	ra,36(sp)
9fc03804:	02f2b821 	addu	s7,s7,s2
9fc03808:	16a0ff60 	bnez	s5,9fc0358c <matrix_test+0xb7c>
9fc0380c:	afb60010 	sw	s6,16(sp)
9fc03810:	8fab0028 	lw	t3,40(sp)
9fc03814:	00003021 	move	a2,zero
9fc03818:	00001821 	move	v1,zero
9fc0381c:	00002021 	move	a0,zero
9fc03820:	00006021 	move	t4,zero
9fc03824:	2687ffff 	addiu	a3,s4,-1
9fc03828:	30ea0003 	andi	t2,a3,0x3
9fc0382c:	01604021 	move	t0,t3
9fc03830:	1140003f 	beqz	t2,9fc03930 <matrix_test+0xf20>
9fc03834:	00004821 	move	t1,zero
9fc03838:	8d670000 	lw	a3,0(t3)
9fc0383c:	3090ffff 	andi	s0,a0,0xffff
9fc03840:	0067982a 	slt	s3,v1,a3
9fc03844:	00c73021 	addu	a2,a2,a3
9fc03848:	0213a821 	addu	s5,s0,s3
9fc0384c:	2616000a 	addiu	s6,s0,10
9fc03850:	00167400 	sll	t6,s6,0x10
9fc03854:	0015bc00 	sll	s7,s5,0x10
9fc03858:	03c6282a 	slt	a1,s8,a2
9fc0385c:	000e1403 	sra	v0,t6,0x10
9fc03860:	10a00002 	beqz	a1,9fc0386c <matrix_test+0xe5c>
9fc03864:	00172403 	sra	a0,s7,0x10
9fc03868:	00402021 	move	a0,v0
9fc0386c:	10a00002 	beqz	a1,9fc03878 <matrix_test+0xe68>
9fc03870:	24090001 	li	t1,1
9fc03874:	00003021 	move	a2,zero
9fc03878:	0134c82b 	sltu	t9,t1,s4
9fc0387c:	13200078 	beqz	t9,9fc03a60 <matrix_test+0x1050>
9fc03880:	25680004 	addiu	t0,t3,4
9fc03884:	1149002a 	beq	t2,t1,9fc03930 <matrix_test+0xf20>
9fc03888:	00e01821 	move	v1,a3
9fc0388c:	24110002 	li	s1,2
9fc03890:	11510014 	beq	t2,s1,9fc038e4 <matrix_test+0xed4>
9fc03894:	309fffff 	andi	ra,a0,0xffff
9fc03898:	8d050000 	lw	a1,0(t0)
9fc0389c:	00000000 	nop
9fc038a0:	00c55021 	addu	t2,a2,a1
9fc038a4:	00e5302a 	slt	a2,a3,a1
9fc038a8:	03e67821 	addu	t7,ra,a2
9fc038ac:	27e7000a 	addiu	a3,ra,10
9fc038b0:	00076c00 	sll	t5,a3,0x10
9fc038b4:	000fc400 	sll	t8,t7,0x10
9fc038b8:	03ca302a 	slt	a2,s8,t2
9fc038bc:	000d1403 	sra	v0,t5,0x10
9fc038c0:	10c00002 	beqz	a2,9fc038cc <matrix_test+0xebc>
9fc038c4:	00182403 	sra	a0,t8,0x10
9fc038c8:	00402021 	move	a0,v0
9fc038cc:	14c00002 	bnez	a2,9fc038d8 <matrix_test+0xec8>
9fc038d0:	00003021 	move	a2,zero
9fc038d4:	01403021 	move	a2,t2
9fc038d8:	25290001 	addiu	t1,t1,1
9fc038dc:	25080004 	addiu	t0,t0,4
9fc038e0:	00a01821 	move	v1,a1
9fc038e4:	8d050000 	lw	a1,0(t0)
9fc038e8:	3097ffff 	andi	s7,a0,0xffff
9fc038ec:	0065a82a 	slt	s5,v1,a1
9fc038f0:	26e2000a 	addiu	v0,s7,10
9fc038f4:	00c53821 	addu	a3,a2,a1
9fc038f8:	02f51821 	addu	v1,s7,s5
9fc038fc:	00022400 	sll	a0,v0,0x10
9fc03900:	00035400 	sll	t2,v1,0x10
9fc03904:	03c7302a 	slt	a2,s8,a3
9fc03908:	00041403 	sra	v0,a0,0x10
9fc0390c:	10c00002 	beqz	a2,9fc03918 <matrix_test+0xf08>
9fc03910:	000a2403 	sra	a0,t2,0x10
9fc03914:	00402021 	move	a0,v0
9fc03918:	14c00002 	bnez	a2,9fc03924 <matrix_test+0xf14>
9fc0391c:	00003021 	move	a2,zero
9fc03920:	00e03021 	move	a2,a3
9fc03924:	25290001 	addiu	t1,t1,1
9fc03928:	25080004 	addiu	t0,t0,4
9fc0392c:	00a01821 	move	v1,a1
9fc03930:	8d070000 	lw	a3,0(t0)
9fc03934:	3085ffff 	andi	a1,a0,0xffff
9fc03938:	0067a82a 	slt	s5,v1,a3
9fc0393c:	00b52021 	addu	a0,a1,s5
9fc03940:	24b8000a 	addiu	t8,a1,10
9fc03944:	00c73021 	addu	a2,a2,a3
9fc03948:	00187c00 	sll	t7,t8,0x10
9fc0394c:	00045400 	sll	t2,a0,0x10
9fc03950:	03c6282a 	slt	a1,s8,a2
9fc03954:	000f1403 	sra	v0,t7,0x10
9fc03958:	10a00002 	beqz	a1,9fc03964 <matrix_test+0xf54>
9fc0395c:	000a2403 	sra	a0,t2,0x10
9fc03960:	00402021 	move	a0,v0
9fc03964:	10a00002 	beqz	a1,9fc03970 <matrix_test+0xf60>
9fc03968:	00000000 	nop
9fc0396c:	00003021 	move	a2,zero
9fc03970:	25290001 	addiu	t1,t1,1
9fc03974:	0134b02b 	sltu	s6,t1,s4
9fc03978:	12c00039 	beqz	s6,9fc03a60 <matrix_test+0x1050>
9fc0397c:	250a0004 	addiu	t2,t0,4
9fc03980:	8d050004 	lw	a1,4(t0)
9fc03984:	308effff 	andi	t6,a0,0xffff
9fc03988:	00e5182a 	slt	v1,a3,a1
9fc0398c:	25c2000a 	addiu	v0,t6,10
9fc03990:	00c54021 	addu	t0,a2,a1
9fc03994:	01c38821 	addu	s1,t6,v1
9fc03998:	00026c00 	sll	t5,v0,0x10
9fc0399c:	0011bc00 	sll	s7,s1,0x10
9fc039a0:	03c8302a 	slt	a2,s8,t0
9fc039a4:	000d2403 	sra	a0,t5,0x10
9fc039a8:	10c00002 	beqz	a2,9fc039b4 <matrix_test+0xfa4>
9fc039ac:	00171403 	sra	v0,s7,0x10
9fc039b0:	00801021 	move	v0,a0
9fc039b4:	14c00002 	bnez	a2,9fc039c0 <matrix_test+0xfb0>
9fc039b8:	00003821 	move	a3,zero
9fc039bc:	01003821 	move	a3,t0
9fc039c0:	8d460004 	lw	a2,4(t2)
9fc039c4:	3053ffff 	andi	s3,v0,0xffff
9fc039c8:	00a6c82a 	slt	t9,a1,a2
9fc039cc:	00e63821 	addu	a3,a3,a2
9fc039d0:	02797021 	addu	t6,s3,t9
9fc039d4:	2670000a 	addiu	s0,s3,10
9fc039d8:	0010b400 	sll	s6,s0,0x10
9fc039dc:	000e4400 	sll	t0,t6,0x10
9fc039e0:	03c7282a 	slt	a1,s8,a3
9fc039e4:	00162403 	sra	a0,s6,0x10
9fc039e8:	10a00002 	beqz	a1,9fc039f4 <matrix_test+0xfe4>
9fc039ec:	00081403 	sra	v0,t0,0x10
9fc039f0:	00801021 	move	v0,a0
9fc039f4:	10a00002 	beqz	a1,9fc03a00 <matrix_test+0xff0>
9fc039f8:	00000000 	nop
9fc039fc:	00003821 	move	a3,zero
9fc03a00:	8d450008 	lw	a1,8(t2)
9fc03a04:	305fffff 	andi	ra,v0,0xffff
9fc03a08:	00c5182a 	slt	v1,a2,a1
9fc03a0c:	00e53821 	addu	a3,a3,a1
9fc03a10:	03e3c021 	addu	t8,ra,v1
9fc03a14:	27ed000a 	addiu	t5,ra,10
9fc03a18:	000d7c00 	sll	t7,t5,0x10
9fc03a1c:	00188c00 	sll	s1,t8,0x10
9fc03a20:	03c7302a 	slt	a2,s8,a3
9fc03a24:	000f1403 	sra	v0,t7,0x10
9fc03a28:	10c00002 	beqz	a2,9fc03a34 <matrix_test+0x1024>
9fc03a2c:	00112403 	sra	a0,s1,0x10
9fc03a30:	00402021 	move	a0,v0
9fc03a34:	14c00006 	bnez	a2,9fc03a50 <matrix_test+0x1040>
9fc03a38:	00000000 	nop
9fc03a3c:	00e03021 	move	a2,a3
9fc03a40:	25290003 	addiu	t1,t1,3
9fc03a44:	2548000c 	addiu	t0,t2,12
9fc03a48:	0bf00e4c 	j	9fc03930 <matrix_test+0xf20>
9fc03a4c:	00a01821 	move	v1,a1
9fc03a50:	0bf00e90 	j	9fc03a40 <matrix_test+0x1030>
9fc03a54:	00003021 	move	a2,zero
	...
9fc03a60:	258c0001 	addiu	t4,t4,1
9fc03a64:	8fa8001c 	lw	t0,28(sp)
9fc03a68:	0194482b 	sltu	t1,t4,s4
9fc03a6c:	11200003 	beqz	t1,9fc03a7c <matrix_test+0x106c>
9fc03a70:	01685821 	addu	t3,t3,t0
9fc03a74:	0bf00e09 	j	9fc03824 <matrix_test+0xe14>
9fc03a78:	00e01821 	move	v1,a3
9fc03a7c:	8fa50018 	lw	a1,24(sp)
9fc03a80:	0ff01d20 	jal	9fc07480 <crc16>
9fc03a84:	0000b021 	move	s6,zero
9fc03a88:	8fb8002c 	lw	t8,44(sp)
9fc03a8c:	8fb50028 	lw	s5,40(sp)
9fc03a90:	0040b821 	move	s7,v0
9fc03a94:	8fb10030 	lw	s1,48(sp)
9fc03a98:	02a09821 	move	s3,s5
9fc03a9c:	0000c821 	move	t9,zero
9fc03aa0:	862e0000 	lh	t6,0(s1)
9fc03aa4:	87030000 	lh	v1,0(t8)
9fc03aa8:	240f0001 	li	t7,1
9fc03aac:	01c30018 	mult	t6,v1
9fc03ab0:	268dffff 	addiu	t5,s4,-1
9fc03ab4:	01f4582b 	sltu	t3,t7,s4
9fc03ab8:	31a40003 	andi	a0,t5,0x3
9fc03abc:	02327021 	addu	t6,s1,s2
9fc03ac0:	00001012 	mflo	v0
9fc03ac4:	00028143 	sra	s0,v0,0x5
9fc03ac8:	0002f883 	sra	ra,v0,0x2
9fc03acc:	320c007f 	andi	t4,s0,0x7f
9fc03ad0:	33e6000f 	andi	a2,ra,0xf
9fc03ad4:	01860018 	mult	t4,a2
9fc03ad8:	00008012 	mflo	s0
9fc03adc:	1160006a 	beqz	t3,9fc03c88 <matrix_test+0x1278>
9fc03ae0:	270d0002 	addiu	t5,t8,2
9fc03ae4:	10800031 	beqz	a0,9fc03bac <matrix_test+0x119c>
9fc03ae8:	00000000 	nop
9fc03aec:	108f001f 	beq	a0,t7,9fc03b6c <matrix_test+0x115c>
9fc03af0:	240b0002 	li	t3,2
9fc03af4:	108b000f 	beq	a0,t3,9fc03b34 <matrix_test+0x1124>
9fc03af8:	00000000 	nop
9fc03afc:	85af0000 	lh	t7,0(t5)
9fc03b00:	85cd0000 	lh	t5,0(t6)
9fc03b04:	01d27021 	addu	t6,t6,s2
9fc03b08:	01af0018 	mult	t5,t7
9fc03b0c:	240f0002 	li	t7,2
9fc03b10:	270d0004 	addiu	t5,t8,4
9fc03b14:	00004012 	mflo	t0
9fc03b18:	00085083 	sra	t2,t0,0x2
9fc03b1c:	00083143 	sra	a2,t0,0x5
9fc03b20:	30c7007f 	andi	a3,a2,0x7f
9fc03b24:	3145000f 	andi	a1,t2,0xf
9fc03b28:	00e50018 	mult	a3,a1
9fc03b2c:	00004812 	mflo	t1
9fc03b30:	02098021 	addu	s0,s0,t1
9fc03b34:	85c90000 	lh	t1,0(t6)
9fc03b38:	85a50000 	lh	a1,0(t5)
9fc03b3c:	25ef0001 	addiu	t7,t7,1
9fc03b40:	01250018 	mult	t1,a1
9fc03b44:	25ad0002 	addiu	t5,t5,2
9fc03b48:	01d27021 	addu	t6,t6,s2
9fc03b4c:	00005812 	mflo	t3
9fc03b50:	000b2083 	sra	a0,t3,0x2
9fc03b54:	000b1143 	sra	v0,t3,0x5
9fc03b58:	305f007f 	andi	ra,v0,0x7f
9fc03b5c:	3083000f 	andi	v1,a0,0xf
9fc03b60:	03e30018 	mult	ra,v1
9fc03b64:	00006012 	mflo	t4
9fc03b68:	020c8021 	addu	s0,s0,t4
9fc03b6c:	85c40000 	lh	a0,0(t6)
9fc03b70:	85a20000 	lh	v0,0(t5)
9fc03b74:	25ef0001 	addiu	t7,t7,1
9fc03b78:	00820018 	mult	a0,v0
9fc03b7c:	01f4382b 	sltu	a3,t7,s4
9fc03b80:	25ad0002 	addiu	t5,t5,2
9fc03b84:	01d27021 	addu	t6,t6,s2
9fc03b88:	00001812 	mflo	v1
9fc03b8c:	00036083 	sra	t4,v1,0x2
9fc03b90:	0003f943 	sra	ra,v1,0x5
9fc03b94:	33e6007f 	andi	a2,ra,0x7f
9fc03b98:	3188000f 	andi	t0,t4,0xf
9fc03b9c:	00c80018 	mult	a2,t0
9fc03ba0:	00005012 	mflo	t2
9fc03ba4:	10e00038 	beqz	a3,9fc03c88 <matrix_test+0x1278>
9fc03ba8:	020a8021 	addu	s0,s0,t2
9fc03bac:	85c60000 	lh	a2,0(t6)
9fc03bb0:	85a30000 	lh	v1,0(t5)
9fc03bb4:	01d26021 	addu	t4,t6,s2
9fc03bb8:	00c30018 	mult	a2,v1
9fc03bbc:	85a90002 	lh	t1,2(t5)
9fc03bc0:	85850000 	lh	a1,0(t4)
9fc03bc4:	01927021 	addu	t6,t4,s2
9fc03bc8:	85aa0004 	lh	t2,4(t5)
9fc03bcc:	85c40000 	lh	a0,0(t6)
9fc03bd0:	01d23821 	addu	a3,t6,s2
9fc03bd4:	85a30006 	lh	v1,6(t5)
9fc03bd8:	84e60000 	lh	a2,0(a3)
9fc03bdc:	00f27021 	addu	t6,a3,s2
9fc03be0:	25ef0004 	addiu	t7,t7,4
9fc03be4:	01f4602b 	sltu	t4,t7,s4
9fc03be8:	25ad0008 	addiu	t5,t5,8
9fc03bec:	0000f812 	mflo	ra
9fc03bf0:	001f4083 	sra	t0,ra,0x2
9fc03bf4:	001f1143 	sra	v0,ra,0x5
9fc03bf8:	00a90018 	mult	a1,t1
9fc03bfc:	305f007f 	andi	ra,v0,0x7f
9fc03c00:	3108000f 	andi	t0,t0,0xf
9fc03c04:	00005812 	mflo	t3
9fc03c08:	000b4883 	sra	t1,t3,0x2
9fc03c0c:	000b2943 	sra	a1,t3,0x5
9fc03c10:	008a0018 	mult	a0,t2
9fc03c14:	30a5007f 	andi	a1,a1,0x7f
9fc03c18:	3129000f 	andi	t1,t1,0xf
9fc03c1c:	00003812 	mflo	a3
9fc03c20:	00075083 	sra	t2,a3,0x2
9fc03c24:	00072143 	sra	a0,a3,0x5
9fc03c28:	00c30018 	mult	a2,v1
9fc03c2c:	3084007f 	andi	a0,a0,0x7f
9fc03c30:	3147000f 	andi	a3,t2,0xf
9fc03c34:	00005812 	mflo	t3
9fc03c38:	000b1943 	sra	v1,t3,0x5
9fc03c3c:	000b3083 	sra	a2,t3,0x2
9fc03c40:	03e80018 	mult	ra,t0
9fc03c44:	30cb000f 	andi	t3,a2,0xf
9fc03c48:	3068007f 	andi	t0,v1,0x7f
9fc03c4c:	00001012 	mflo	v0
9fc03c50:	0202f821 	addu	ra,s0,v0
9fc03c54:	00000000 	nop
9fc03c58:	00a90018 	mult	a1,t1
9fc03c5c:	00005012 	mflo	t2
9fc03c60:	03ea4821 	addu	t1,ra,t2
9fc03c64:	00000000 	nop
9fc03c68:	00870018 	mult	a0,a3
9fc03c6c:	00002012 	mflo	a0
9fc03c70:	01245021 	addu	t2,t1,a0
9fc03c74:	00000000 	nop
9fc03c78:	010b0018 	mult	t0,t3
9fc03c7c:	00002812 	mflo	a1
9fc03c80:	1580ffca 	bnez	t4,9fc03bac <matrix_test+0x119c>
9fc03c84:	01458021 	addu	s0,t2,a1
9fc03c88:	27390001 	addiu	t9,t9,1
9fc03c8c:	0334782b 	sltu	t7,t9,s4
9fc03c90:	ae700000 	sw	s0,0(s3)
9fc03c94:	26310002 	addiu	s1,s1,2
9fc03c98:	15e0ff81 	bnez	t7,9fc03aa0 <matrix_test+0x1090>
9fc03c9c:	26730004 	addiu	s3,s3,4
9fc03ca0:	26d60001 	addiu	s6,s6,1
9fc03ca4:	8fb9001c 	lw	t9,28(sp)
9fc03ca8:	02d4982b 	sltu	s3,s6,s4
9fc03cac:	0312c021 	addu	t8,t8,s2
9fc03cb0:	1660ff78 	bnez	s3,9fc03a94 <matrix_test+0x1084>
9fc03cb4:	02b9a821 	addu	s5,s5,t9
9fc03cb8:	00003021 	move	a2,zero
9fc03cbc:	00001821 	move	v1,zero
9fc03cc0:	00002021 	move	a0,zero
9fc03cc4:	00005821 	move	t3,zero
9fc03cc8:	2687ffff 	addiu	a3,s4,-1
9fc03ccc:	30ea0003 	andi	t2,a3,0x3
9fc03cd0:	8fa80020 	lw	t0,32(sp)
9fc03cd4:	11400040 	beqz	t2,9fc03dd8 <matrix_test+0x13c8>
9fc03cd8:	00004821 	move	t1,zero
9fc03cdc:	8d070000 	lw	a3,0(t0)
9fc03ce0:	3096ffff 	andi	s6,a0,0xffff
9fc03ce4:	0067802a 	slt	s0,v1,a3
9fc03ce8:	00c73021 	addu	a2,a2,a3
9fc03cec:	02d07021 	addu	t6,s6,s0
9fc03cf0:	26cc000a 	addiu	t4,s6,10
9fc03cf4:	000c7c00 	sll	t7,t4,0x10
9fc03cf8:	000e6c00 	sll	t5,t6,0x10
9fc03cfc:	03c6282a 	slt	a1,s8,a2
9fc03d00:	000f1403 	sra	v0,t7,0x10
9fc03d04:	10a00002 	beqz	a1,9fc03d10 <matrix_test+0x1300>
9fc03d08:	000d2403 	sra	a0,t5,0x10
9fc03d0c:	00402021 	move	a0,v0
9fc03d10:	10a00002 	beqz	a1,9fc03d1c <matrix_test+0x130c>
9fc03d14:	24090001 	li	t1,1
9fc03d18:	00003021 	move	a2,zero
9fc03d1c:	8fa30020 	lw	v1,32(sp)
9fc03d20:	0134282b 	sltu	a1,t1,s4
9fc03d24:	10a0007b 	beqz	a1,9fc03f14 <matrix_test+0x1504>
9fc03d28:	24680004 	addiu	t0,v1,4
9fc03d2c:	1149002a 	beq	t2,t1,9fc03dd8 <matrix_test+0x13c8>
9fc03d30:	00e01821 	move	v1,a3
9fc03d34:	24020002 	li	v0,2
9fc03d38:	11420014 	beq	t2,v0,9fc03d8c <matrix_test+0x137c>
9fc03d3c:	3091ffff 	andi	s1,a0,0xffff
9fc03d40:	8d050000 	lw	a1,0(t0)
9fc03d44:	00000000 	nop
9fc03d48:	00e5c02a 	slt	t8,a3,a1
9fc03d4c:	02382021 	addu	a0,s1,t8
9fc03d50:	00c55021 	addu	t2,a2,a1
9fc03d54:	2639000a 	addiu	t9,s1,10
9fc03d58:	00199c00 	sll	s3,t9,0x10
9fc03d5c:	00043c00 	sll	a3,a0,0x10
9fc03d60:	03ca302a 	slt	a2,s8,t2
9fc03d64:	00131403 	sra	v0,s3,0x10
9fc03d68:	10c00002 	beqz	a2,9fc03d74 <matrix_test+0x1364>
9fc03d6c:	00072403 	sra	a0,a3,0x10
9fc03d70:	00402021 	move	a0,v0
9fc03d74:	14c00002 	bnez	a2,9fc03d80 <matrix_test+0x1370>
9fc03d78:	00003021 	move	a2,zero
9fc03d7c:	01403021 	move	a2,t2
9fc03d80:	25290001 	addiu	t1,t1,1
9fc03d84:	25080004 	addiu	t0,t0,4
9fc03d88:	00a01821 	move	v1,a1
9fc03d8c:	8d050000 	lw	a1,0(t0)
9fc03d90:	308fffff 	andi	t7,a0,0xffff
9fc03d94:	00c53821 	addu	a3,a2,a1
9fc03d98:	0065302a 	slt	a2,v1,a1
9fc03d9c:	01e6a821 	addu	s5,t7,a2
9fc03da0:	25ee000a 	addiu	t6,t7,10
9fc03da4:	000e6c00 	sll	t5,t6,0x10
9fc03da8:	00155400 	sll	t2,s5,0x10
9fc03dac:	03c7302a 	slt	a2,s8,a3
9fc03db0:	000d1403 	sra	v0,t5,0x10
9fc03db4:	10c00002 	beqz	a2,9fc03dc0 <matrix_test+0x13b0>
9fc03db8:	000a2403 	sra	a0,t2,0x10
9fc03dbc:	00402021 	move	a0,v0
9fc03dc0:	14c00002 	bnez	a2,9fc03dcc <matrix_test+0x13bc>
9fc03dc4:	00003021 	move	a2,zero
9fc03dc8:	00e03021 	move	a2,a3
9fc03dcc:	25290001 	addiu	t1,t1,1
9fc03dd0:	25080004 	addiu	t0,t0,4
9fc03dd4:	00a01821 	move	v1,a1
9fc03dd8:	8d070000 	lw	a3,0(t0)
9fc03ddc:	3085ffff 	andi	a1,a0,0xffff
9fc03de0:	0067702a 	slt	t6,v1,a3
9fc03de4:	00ae5021 	addu	t2,a1,t6
9fc03de8:	24bf000a 	addiu	ra,a1,10
9fc03dec:	00c73021 	addu	a2,a2,a3
9fc03df0:	001f1400 	sll	v0,ra,0x10
9fc03df4:	000a2400 	sll	a0,t2,0x10
9fc03df8:	03c6282a 	slt	a1,s8,a2
9fc03dfc:	00021403 	sra	v0,v0,0x10
9fc03e00:	10a00002 	beqz	a1,9fc03e0c <matrix_test+0x13fc>
9fc03e04:	00042403 	sra	a0,a0,0x10
9fc03e08:	00402021 	move	a0,v0
9fc03e0c:	10a00002 	beqz	a1,9fc03e18 <matrix_test+0x1408>
9fc03e10:	00000000 	nop
9fc03e14:	00003021 	move	a2,zero
9fc03e18:	25290001 	addiu	t1,t1,1
9fc03e1c:	0134602b 	sltu	t4,t1,s4
9fc03e20:	1180003b 	beqz	t4,9fc03f10 <matrix_test+0x1500>
9fc03e24:	250a0004 	addiu	t2,t0,4
9fc03e28:	8d050004 	lw	a1,4(t0)
9fc03e2c:	3090ffff 	andi	s0,a0,0xffff
9fc03e30:	00e5682a 	slt	t5,a3,a1
9fc03e34:	00c54021 	addu	t0,a2,a1
9fc03e38:	020dc021 	addu	t8,s0,t5
9fc03e3c:	2616000a 	addiu	s6,s0,10
9fc03e40:	0016ac00 	sll	s5,s6,0x10
9fc03e44:	00188c00 	sll	s1,t8,0x10
9fc03e48:	03c8302a 	slt	a2,s8,t0
9fc03e4c:	00152403 	sra	a0,s5,0x10
9fc03e50:	10c00002 	beqz	a2,9fc03e5c <matrix_test+0x144c>
9fc03e54:	00111403 	sra	v0,s1,0x10
9fc03e58:	00801021 	move	v0,a0
9fc03e5c:	14c00002 	bnez	a2,9fc03e68 <matrix_test+0x1458>
9fc03e60:	00003821 	move	a3,zero
9fc03e64:	01003821 	move	a3,t0
9fc03e68:	8d460004 	lw	a2,4(t2)
9fc03e6c:	305fffff 	andi	ra,v0,0xffff
9fc03e70:	00a6182a 	slt	v1,a1,a2
9fc03e74:	00e63821 	addu	a3,a3,a2
9fc03e78:	03e36021 	addu	t4,ra,v1
9fc03e7c:	27f0000a 	addiu	s0,ra,10
9fc03e80:	0010b400 	sll	s6,s0,0x10
9fc03e84:	000c4400 	sll	t0,t4,0x10
9fc03e88:	03c7282a 	slt	a1,s8,a3
9fc03e8c:	00162403 	sra	a0,s6,0x10
9fc03e90:	10a00002 	beqz	a1,9fc03e9c <matrix_test+0x148c>
9fc03e94:	00081403 	sra	v0,t0,0x10
9fc03e98:	00801021 	move	v0,a0
9fc03e9c:	10a00002 	beqz	a1,9fc03ea8 <matrix_test+0x1498>
9fc03ea0:	00000000 	nop
9fc03ea4:	00003821 	move	a3,zero
9fc03ea8:	8d450008 	lw	a1,8(t2)
9fc03eac:	3051ffff 	andi	s1,v0,0xffff
9fc03eb0:	00c5c02a 	slt	t8,a2,a1
9fc03eb4:	00e53821 	addu	a3,a3,a1
9fc03eb8:	02389821 	addu	s3,s1,t8
9fc03ebc:	2639000a 	addiu	t9,s1,10
9fc03ec0:	00191400 	sll	v0,t9,0x10
9fc03ec4:	00132400 	sll	a0,s3,0x10
9fc03ec8:	03c7302a 	slt	a2,s8,a3
9fc03ecc:	00021403 	sra	v0,v0,0x10
9fc03ed0:	10c00002 	beqz	a2,9fc03edc <matrix_test+0x14cc>
9fc03ed4:	00042403 	sra	a0,a0,0x10
9fc03ed8:	00402021 	move	a0,v0
9fc03edc:	14c00008 	bnez	a2,9fc03f00 <matrix_test+0x14f0>
9fc03ee0:	00000000 	nop
9fc03ee4:	00e03021 	move	a2,a3
9fc03ee8:	25290003 	addiu	t1,t1,3
9fc03eec:	2548000c 	addiu	t0,t2,12
9fc03ef0:	0bf00f76 	j	9fc03dd8 <matrix_test+0x13c8>
9fc03ef4:	00a01821 	move	v1,a1
	...
9fc03f00:	0bf00fba 	j	9fc03ee8 <matrix_test+0x14d8>
9fc03f04:	00003021 	move	a2,zero
	...
9fc03f10:	8fa30020 	lw	v1,32(sp)
9fc03f14:	8faf001c 	lw	t7,28(sp)
9fc03f18:	256b0001 	addiu	t3,t3,1
9fc03f1c:	006f4821 	addu	t1,v1,t7
9fc03f20:	0174402b 	sltu	t0,t3,s4
9fc03f24:	11000003 	beqz	t0,9fc03f34 <matrix_test+0x1524>
9fc03f28:	afa90020 	sw	t1,32(sp)
9fc03f2c:	0bf00f32 	j	9fc03cc8 <matrix_test+0x12b8>
9fc03f30:	00e01821 	move	v1,a3
9fc03f34:	0ff01d20 	jal	9fc07480 <crc16>
9fc03f38:	02e02821 	move	a1,s7
9fc03f3c:	8fb70034 	lw	s7,52(sp)
9fc03f40:	00407821 	move	t7,v0
9fc03f44:	0017f023 	negu	s8,s7
9fc03f48:	33cdffff 	andi	t5,s8,0xffff
9fc03f4c:	00007021 	move	t6,zero
9fc03f50:	8fb30014 	lw	s3,20(sp)
9fc03f54:	240c0001 	li	t4,1
9fc03f58:	96780000 	lhu	t8,0(s3)
9fc03f5c:	2699ffff 	addiu	t9,s4,-1
9fc03f60:	01b88821 	addu	s1,t5,t8
9fc03f64:	0194b82b 	sltu	s7,t4,s4
9fc03f68:	a6710000 	sh	s1,0(s3)
9fc03f6c:	33230007 	andi	v1,t9,0x7
9fc03f70:	12e00051 	beqz	s7,9fc040b8 <matrix_test+0x16a8>
9fc03f74:	266b0002 	addiu	t3,s3,2
9fc03f78:	10600033 	beqz	v1,9fc04048 <matrix_test+0x1638>
9fc03f7c:	00000000 	nop
9fc03f80:	106c002a 	beq	v1,t4,9fc0402c <matrix_test+0x161c>
9fc03f84:	24060002 	li	a2,2
9fc03f88:	10660023 	beq	v1,a2,9fc04018 <matrix_test+0x1608>
9fc03f8c:	24150003 	li	s5,3
9fc03f90:	1075001c 	beq	v1,s5,9fc04004 <matrix_test+0x15f4>
9fc03f94:	24160004 	li	s6,4
9fc03f98:	10760015 	beq	v1,s6,9fc03ff0 <matrix_test+0x15e0>
9fc03f9c:	24100005 	li	s0,5
9fc03fa0:	1070000e 	beq	v1,s0,9fc03fdc <matrix_test+0x15cc>
9fc03fa4:	240a0006 	li	t2,6
9fc03fa8:	106a0007 	beq	v1,t2,9fc03fc8 <matrix_test+0x15b8>
9fc03fac:	00000000 	nop
9fc03fb0:	95690000 	lhu	t1,0(t3)
9fc03fb4:	8fbf0014 	lw	ra,20(sp)
9fc03fb8:	01a94021 	addu	t0,t5,t1
9fc03fbc:	a5680000 	sh	t0,0(t3)
9fc03fc0:	240c0002 	li	t4,2
9fc03fc4:	27eb0004 	addiu	t3,ra,4
9fc03fc8:	95650000 	lhu	a1,0(t3)
9fc03fcc:	258c0001 	addiu	t4,t4,1
9fc03fd0:	01a53821 	addu	a3,t5,a1
9fc03fd4:	a5670000 	sh	a3,0(t3)
9fc03fd8:	256b0002 	addiu	t3,t3,2
9fc03fdc:	95630000 	lhu	v1,0(t3)
9fc03fe0:	258c0001 	addiu	t4,t4,1
9fc03fe4:	01a32021 	addu	a0,t5,v1
9fc03fe8:	a5640000 	sh	a0,0(t3)
9fc03fec:	256b0002 	addiu	t3,t3,2
9fc03ff0:	957e0000 	lhu	s8,0(t3)
9fc03ff4:	258c0001 	addiu	t4,t4,1
9fc03ff8:	01be1021 	addu	v0,t5,s8
9fc03ffc:	a5620000 	sh	v0,0(t3)
9fc04000:	256b0002 	addiu	t3,t3,2
9fc04004:	95730000 	lhu	s3,0(t3)
9fc04008:	258c0001 	addiu	t4,t4,1
9fc0400c:	01b3b821 	addu	s7,t5,s3
9fc04010:	a5770000 	sh	s7,0(t3)
9fc04014:	256b0002 	addiu	t3,t3,2
9fc04018:	95710000 	lhu	s1,0(t3)
9fc0401c:	258c0001 	addiu	t4,t4,1
9fc04020:	01b1c821 	addu	t9,t5,s1
9fc04024:	a5790000 	sh	t9,0(t3)
9fc04028:	256b0002 	addiu	t3,t3,2
9fc0402c:	95750000 	lhu	s5,0(t3)
9fc04030:	258c0001 	addiu	t4,t4,1
9fc04034:	01b53021 	addu	a2,t5,s5
9fc04038:	0194c02b 	sltu	t8,t4,s4
9fc0403c:	a5660000 	sh	a2,0(t3)
9fc04040:	1300001d 	beqz	t8,9fc040b8 <matrix_test+0x16a8>
9fc04044:	256b0002 	addiu	t3,t3,2
9fc04048:	95620000 	lhu	v0,0(t3)
9fc0404c:	95630002 	lhu	v1,2(t3)
9fc04050:	95640004 	lhu	a0,4(t3)
9fc04054:	95650006 	lhu	a1,6(t3)
9fc04058:	95670008 	lhu	a3,8(t3)
9fc0405c:	9569000a 	lhu	t1,10(t3)
9fc04060:	9568000c 	lhu	t0,12(t3)
9fc04064:	957f000e 	lhu	ra,14(t3)
9fc04068:	258c0008 	addiu	t4,t4,8
9fc0406c:	01a25021 	addu	t2,t5,v0
9fc04070:	01a38021 	addu	s0,t5,v1
9fc04074:	01a4b021 	addu	s6,t5,a0
9fc04078:	01a5a821 	addu	s5,t5,a1
9fc0407c:	01a73021 	addu	a2,t5,a3
9fc04080:	01a9c021 	addu	t8,t5,t1
9fc04084:	01a88821 	addu	s1,t5,t0
9fc04088:	01bfc821 	addu	t9,t5,ra
9fc0408c:	0194982b 	sltu	s3,t4,s4
9fc04090:	a56a0000 	sh	t2,0(t3)
9fc04094:	a5700002 	sh	s0,2(t3)
9fc04098:	a5760004 	sh	s6,4(t3)
9fc0409c:	a5750006 	sh	s5,6(t3)
9fc040a0:	a5660008 	sh	a2,8(t3)
9fc040a4:	a578000a 	sh	t8,10(t3)
9fc040a8:	a571000c 	sh	s1,12(t3)
9fc040ac:	a579000e 	sh	t9,14(t3)
9fc040b0:	1660ffe5 	bnez	s3,9fc04048 <matrix_test+0x1638>
9fc040b4:	256b0010 	addiu	t3,t3,16
9fc040b8:	8fbe0014 	lw	s8,20(sp)
9fc040bc:	25ce0001 	addiu	t6,t6,1
9fc040c0:	03d26021 	addu	t4,s8,s2
9fc040c4:	01d4582b 	sltu	t3,t6,s4
9fc040c8:	1560ffa1 	bnez	t3,9fc03f50 <matrix_test+0x1540>
9fc040cc:	afac0014 	sw	t4,20(sp)
9fc040d0:	8fbf005c 	lw	ra,92(sp)
9fc040d4:	000f9400 	sll	s2,t7,0x10
9fc040d8:	00121403 	sra	v0,s2,0x10
9fc040dc:	8fbe0058 	lw	s8,88(sp)
9fc040e0:	8fb70054 	lw	s7,84(sp)
9fc040e4:	8fb60050 	lw	s6,80(sp)
9fc040e8:	8fb5004c 	lw	s5,76(sp)
9fc040ec:	8fb40048 	lw	s4,72(sp)
9fc040f0:	8fb30044 	lw	s3,68(sp)
9fc040f4:	8fb20040 	lw	s2,64(sp)
9fc040f8:	8fb1003c 	lw	s1,60(sp)
9fc040fc:	8fb00038 	lw	s0,56(sp)
9fc04100:	03e00008 	jr	ra
9fc04104:	27bd0060 	addiu	sp,sp,96
9fc04108:	0ff01d20 	jal	9fc07480 <crc16>
9fc0410c:	00002821 	move	a1,zero
9fc04110:	00402821 	move	a1,v0
9fc04114:	0ff01d20 	jal	9fc07480 <crc16>
9fc04118:	00002021 	move	a0,zero
9fc0411c:	00402821 	move	a1,v0
9fc04120:	0ff01d20 	jal	9fc07480 <crc16>
9fc04124:	00002021 	move	a0,zero
9fc04128:	00402821 	move	a1,v0
9fc0412c:	8fbf005c 	lw	ra,92(sp)
9fc04130:	8fbe0058 	lw	s8,88(sp)
9fc04134:	8fb70054 	lw	s7,84(sp)
9fc04138:	8fb60050 	lw	s6,80(sp)
9fc0413c:	8fb5004c 	lw	s5,76(sp)
9fc04140:	8fb40048 	lw	s4,72(sp)
9fc04144:	8fb30044 	lw	s3,68(sp)
9fc04148:	8fb20040 	lw	s2,64(sp)
9fc0414c:	8fb1003c 	lw	s1,60(sp)
9fc04150:	8fb00038 	lw	s0,56(sp)
9fc04154:	00002021 	move	a0,zero
9fc04158:	0bf01d20 	j	9fc07480 <crc16>
9fc0415c:	27bd0060 	addiu	sp,sp,96

9fc04160 <core_bench_matrix>:
core_bench_matrix():
9fc04160:	27bdff98 	addiu	sp,sp,-104
9fc04164:	8c820008 	lw	v0,8(a0)
9fc04168:	8c83000c 	lw	v1,12(a0)
9fc0416c:	afb40050 	sw	s4,80(sp)
9fc04170:	00052c00 	sll	a1,a1,0x10
9fc04174:	8c940000 	lw	s4,0(a0)
9fc04178:	8c840004 	lw	a0,4(a0)
9fc0417c:	00052c03 	sra	a1,a1,0x10
9fc04180:	30c6ffff 	andi	a2,a2,0xffff
9fc04184:	afbf0064 	sw	ra,100(sp)
9fc04188:	afbe0060 	sw	s8,96(sp)
9fc0418c:	afb7005c 	sw	s7,92(sp)
9fc04190:	afb60058 	sw	s6,88(sp)
9fc04194:	afb50054 	sw	s5,84(sp)
9fc04198:	afb3004c 	sw	s3,76(sp)
9fc0419c:	afb20048 	sw	s2,72(sp)
9fc041a0:	afb10044 	sw	s1,68(sp)
9fc041a4:	afb00040 	sw	s0,64(sp)
9fc041a8:	afa50034 	sw	a1,52(sp)
9fc041ac:	afa60038 	sw	a2,56(sp)
9fc041b0:	afa20028 	sw	v0,40(sp)
9fc041b4:	afa30030 	sw	v1,48(sp)
9fc041b8:	128005a8 	beqz	s4,9fc0585c <core_bench_matrix+0x16fc>
9fc041bc:	afa4002c 	sw	a0,44(sp)
9fc041c0:	30adffff 	andi	t5,a1,0xffff
9fc041c4:	00149040 	sll	s2,s4,0x1
9fc041c8:	afa40010 	sw	a0,16(sp)
9fc041cc:	00807021 	move	t6,a0
9fc041d0:	00007821 	move	t7,zero
9fc041d4:	95c90000 	lhu	t1,0(t6)
9fc041d8:	240c0001 	li	t4,1
9fc041dc:	01a95021 	addu	t2,t5,t1
9fc041e0:	2685ffff 	addiu	a1,s4,-1
9fc041e4:	0194302b 	sltu	a2,t4,s4
9fc041e8:	a5ca0000 	sh	t2,0(t6)
9fc041ec:	30a30007 	andi	v1,a1,0x7
9fc041f0:	10c00050 	beqz	a2,9fc04334 <core_bench_matrix+0x1d4>
9fc041f4:	25cb0002 	addiu	t3,t6,2
9fc041f8:	10600032 	beqz	v1,9fc042c4 <core_bench_matrix+0x164>
9fc041fc:	00000000 	nop
9fc04200:	106c0029 	beq	v1,t4,9fc042a8 <core_bench_matrix+0x148>
9fc04204:	24100002 	li	s0,2
9fc04208:	10700022 	beq	v1,s0,9fc04294 <core_bench_matrix+0x134>
9fc0420c:	24170003 	li	s7,3
9fc04210:	1077001b 	beq	v1,s7,9fc04280 <core_bench_matrix+0x120>
9fc04214:	24080004 	li	t0,4
9fc04218:	10680014 	beq	v1,t0,9fc0426c <core_bench_matrix+0x10c>
9fc0421c:	24130005 	li	s3,5
9fc04220:	1073000d 	beq	v1,s3,9fc04258 <core_bench_matrix+0xf8>
9fc04224:	24190006 	li	t9,6
9fc04228:	10790006 	beq	v1,t9,9fc04244 <core_bench_matrix+0xe4>
9fc0422c:	00000000 	nop
9fc04230:	95750000 	lhu	s5,0(t3)
9fc04234:	240c0002 	li	t4,2
9fc04238:	01b51821 	addu	v1,t5,s5
9fc0423c:	a5630000 	sh	v1,0(t3)
9fc04240:	25cb0004 	addiu	t3,t6,4
9fc04244:	95710000 	lhu	s1,0(t3)
9fc04248:	258c0001 	addiu	t4,t4,1
9fc0424c:	01b1b021 	addu	s6,t5,s1
9fc04250:	a5760000 	sh	s6,0(t3)
9fc04254:	256b0002 	addiu	t3,t3,2
9fc04258:	95620000 	lhu	v0,0(t3)
9fc0425c:	258c0001 	addiu	t4,t4,1
9fc04260:	01a2c021 	addu	t8,t5,v0
9fc04264:	a5780000 	sh	t8,0(t3)
9fc04268:	256b0002 	addiu	t3,t3,2
9fc0426c:	95690000 	lhu	t1,0(t3)
9fc04270:	258c0001 	addiu	t4,t4,1
9fc04274:	01a9f021 	addu	s8,t5,t1
9fc04278:	a57e0000 	sh	s8,0(t3)
9fc0427c:	256b0002 	addiu	t3,t3,2
9fc04280:	95670000 	lhu	a3,0(t3)
9fc04284:	258c0001 	addiu	t4,t4,1
9fc04288:	01a72821 	addu	a1,t5,a3
9fc0428c:	a5650000 	sh	a1,0(t3)
9fc04290:	256b0002 	addiu	t3,t3,2
9fc04294:	956a0000 	lhu	t2,0(t3)
9fc04298:	258c0001 	addiu	t4,t4,1
9fc0429c:	01aa3021 	addu	a2,t5,t2
9fc042a0:	a5660000 	sh	a2,0(t3)
9fc042a4:	256b0002 	addiu	t3,t3,2
9fc042a8:	95700000 	lhu	s0,0(t3)
9fc042ac:	258c0001 	addiu	t4,t4,1
9fc042b0:	01b0f821 	addu	ra,t5,s0
9fc042b4:	0194202b 	sltu	a0,t4,s4
9fc042b8:	a57f0000 	sh	ra,0(t3)
9fc042bc:	1080001d 	beqz	a0,9fc04334 <core_bench_matrix+0x1d4>
9fc042c0:	256b0002 	addiu	t3,t3,2
9fc042c4:	95620000 	lhu	v0,0(t3)
9fc042c8:	95630002 	lhu	v1,2(t3)
9fc042cc:	957f0004 	lhu	ra,4(t3)
9fc042d0:	957e0006 	lhu	s8,6(t3)
9fc042d4:	95790008 	lhu	t9,8(t3)
9fc042d8:	9578000a 	lhu	t8,10(t3)
9fc042dc:	9577000c 	lhu	s7,12(t3)
9fc042e0:	9576000e 	lhu	s6,14(t3)
9fc042e4:	258c0008 	addiu	t4,t4,8
9fc042e8:	01a2a821 	addu	s5,t5,v0
9fc042ec:	01a39821 	addu	s3,t5,v1
9fc042f0:	01bf2021 	addu	a0,t5,ra
9fc042f4:	01be8821 	addu	s1,t5,s8
9fc042f8:	01b98021 	addu	s0,t5,t9
9fc042fc:	01b83821 	addu	a3,t5,t8
9fc04300:	01b74021 	addu	t0,t5,s7
9fc04304:	01b64821 	addu	t1,t5,s6
9fc04308:	0194502b 	sltu	t2,t4,s4
9fc0430c:	a5750000 	sh	s5,0(t3)
9fc04310:	a5730002 	sh	s3,2(t3)
9fc04314:	a5640004 	sh	a0,4(t3)
9fc04318:	a5710006 	sh	s1,6(t3)
9fc0431c:	a5700008 	sh	s0,8(t3)
9fc04320:	a567000a 	sh	a3,10(t3)
9fc04324:	a568000c 	sh	t0,12(t3)
9fc04328:	a569000e 	sh	t1,14(t3)
9fc0432c:	1540ffe5 	bnez	t2,9fc042c4 <core_bench_matrix+0x164>
9fc04330:	256b0010 	addiu	t3,t3,16
9fc04334:	25ef0001 	addiu	t7,t7,1
9fc04338:	01f4582b 	sltu	t3,t7,s4
9fc0433c:	1560ffa5 	bnez	t3,9fc041d4 <core_bench_matrix+0x74>
9fc04340:	01d27021 	addu	t6,t6,s2
9fc04344:	8fac0030 	lw	t4,48(sp)
9fc04348:	00146880 	sll	t5,s4,0x2
9fc0434c:	8faf002c 	lw	t7,44(sp)
9fc04350:	afad001c 	sw	t5,28(sp)
9fc04354:	afac0020 	sw	t4,32(sp)
9fc04358:	01807021 	move	t6,t4
9fc0435c:	00008021 	move	s0,zero
9fc04360:	85f10000 	lh	s1,0(t7)
9fc04364:	8fa40034 	lw	a0,52(sp)
9fc04368:	240d0001 	li	t5,1
9fc0436c:	00910018 	mult	a0,s1
9fc04370:	2695ffff 	addiu	s5,s4,-1
9fc04374:	01b4382b 	sltu	a3,t5,s4
9fc04378:	32a30007 	andi	v1,s5,0x7
9fc0437c:	25eb0002 	addiu	t3,t7,2
9fc04380:	25cc0004 	addiu	t4,t6,4
9fc04384:	00004012 	mflo	t0
9fc04388:	10e00077 	beqz	a3,9fc04568 <core_bench_matrix+0x408>
9fc0438c:	adc80000 	sw	t0,0(t6)
9fc04390:	10600047 	beqz	v1,9fc044b0 <core_bench_matrix+0x350>
9fc04394:	00000000 	nop
9fc04398:	106d003b 	beq	v1,t5,9fc04488 <core_bench_matrix+0x328>
9fc0439c:	241e0002 	li	s8,2
9fc043a0:	107e0031 	beq	v1,s8,9fc04468 <core_bench_matrix+0x308>
9fc043a4:	24090003 	li	t1,3
9fc043a8:	10690027 	beq	v1,t1,9fc04448 <core_bench_matrix+0x2e8>
9fc043ac:	24050004 	li	a1,4
9fc043b0:	1065001d 	beq	v1,a1,9fc04428 <core_bench_matrix+0x2c8>
9fc043b4:	24070005 	li	a3,5
9fc043b8:	10670013 	beq	v1,a3,9fc04408 <core_bench_matrix+0x2a8>
9fc043bc:	24060006 	li	a2,6
9fc043c0:	10660009 	beq	v1,a2,9fc043e8 <core_bench_matrix+0x288>
9fc043c4:	00000000 	nop
9fc043c8:	85640000 	lh	a0,0(t3)
9fc043cc:	8fab0034 	lw	t3,52(sp)
9fc043d0:	240d0002 	li	t5,2
9fc043d4:	01640018 	mult	t3,a0
9fc043d8:	25eb0004 	addiu	t3,t7,4
9fc043dc:	00005012 	mflo	t2
9fc043e0:	ad8a0000 	sw	t2,0(t4)
9fc043e4:	25cc0008 	addiu	t4,t6,8
9fc043e8:	85770000 	lh	s7,0(t3)
9fc043ec:	8fa30034 	lw	v1,52(sp)
9fc043f0:	25ad0001 	addiu	t5,t5,1
9fc043f4:	00770018 	mult	v1,s7
9fc043f8:	256b0002 	addiu	t3,t3,2
9fc043fc:	0000f812 	mflo	ra
9fc04400:	ad9f0000 	sw	ra,0(t4)
9fc04404:	258c0004 	addiu	t4,t4,4
9fc04408:	85730000 	lh	s3,0(t3)
9fc0440c:	8fb90034 	lw	t9,52(sp)
9fc04410:	25ad0001 	addiu	t5,t5,1
9fc04414:	03330018 	mult	t9,s3
9fc04418:	256b0002 	addiu	t3,t3,2
9fc0441c:	00004012 	mflo	t0
9fc04420:	ad880000 	sw	t0,0(t4)
9fc04424:	258c0004 	addiu	t4,t4,4
9fc04428:	85760000 	lh	s6,0(t3)
9fc0442c:	8fb10034 	lw	s1,52(sp)
9fc04430:	25ad0001 	addiu	t5,t5,1
9fc04434:	02360018 	mult	s1,s6
9fc04438:	256b0002 	addiu	t3,t3,2
9fc0443c:	0000a812 	mflo	s5
9fc04440:	ad950000 	sw	s5,0(t4)
9fc04444:	258c0004 	addiu	t4,t4,4
9fc04448:	85620000 	lh	v0,0(t3)
9fc0444c:	8fbe0034 	lw	s8,52(sp)
9fc04450:	25ad0001 	addiu	t5,t5,1
9fc04454:	03c20018 	mult	s8,v0
9fc04458:	256b0002 	addiu	t3,t3,2
9fc0445c:	0000c012 	mflo	t8
9fc04460:	ad980000 	sw	t8,0(t4)
9fc04464:	258c0004 	addiu	t4,t4,4
9fc04468:	85650000 	lh	a1,0(t3)
9fc0446c:	8fa70034 	lw	a3,52(sp)
9fc04470:	25ad0001 	addiu	t5,t5,1
9fc04474:	00e50018 	mult	a3,a1
9fc04478:	256b0002 	addiu	t3,t3,2
9fc0447c:	00004812 	mflo	t1
9fc04480:	ad890000 	sw	t1,0(t4)
9fc04484:	258c0004 	addiu	t4,t4,4
9fc04488:	85640000 	lh	a0,0(t3)
9fc0448c:	8fbf0034 	lw	ra,52(sp)
9fc04490:	25ad0001 	addiu	t5,t5,1
9fc04494:	03e40018 	mult	ra,a0
9fc04498:	01b4302b 	sltu	a2,t5,s4
9fc0449c:	256b0002 	addiu	t3,t3,2
9fc044a0:	00005012 	mflo	t2
9fc044a4:	ad8a0000 	sw	t2,0(t4)
9fc044a8:	10c0002f 	beqz	a2,9fc04568 <core_bench_matrix+0x408>
9fc044ac:	258c0004 	addiu	t4,t4,4
9fc044b0:	85730000 	lh	s3,0(t3)
9fc044b4:	8fa50034 	lw	a1,52(sp)
9fc044b8:	8fb10034 	lw	s1,52(sp)
9fc044bc:	00b30018 	mult	a1,s3
9fc044c0:	85690002 	lh	t1,2(t3)
9fc044c4:	856a0004 	lh	t2,4(t3)
9fc044c8:	85660006 	lh	a2,6(t3)
9fc044cc:	857e0008 	lh	s8,8(t3)
9fc044d0:	8578000a 	lh	t8,10(t3)
9fc044d4:	8576000c 	lh	s6,12(t3)
9fc044d8:	8575000e 	lh	s5,14(t3)
9fc044dc:	25ad0008 	addiu	t5,t5,8
9fc044e0:	01b4982b 	sltu	s3,t5,s4
9fc044e4:	256b0010 	addiu	t3,t3,16
9fc044e8:	00001012 	mflo	v0
9fc044ec:	ad820000 	sw	v0,0(t4)
9fc044f0:	00000000 	nop
9fc044f4:	02290018 	mult	s1,t1
9fc044f8:	00001812 	mflo	v1
9fc044fc:	ad830004 	sw	v1,4(t4)
9fc04500:	00000000 	nop
9fc04504:	022a0018 	mult	s1,t2
9fc04508:	0000f812 	mflo	ra
9fc0450c:	ad9f0008 	sw	ra,8(t4)
9fc04510:	00000000 	nop
9fc04514:	02260018 	mult	s1,a2
9fc04518:	0000c812 	mflo	t9
9fc0451c:	ad99000c 	sw	t9,12(t4)
9fc04520:	00000000 	nop
9fc04524:	023e0018 	mult	s1,s8
9fc04528:	0000b812 	mflo	s7
9fc0452c:	ad970010 	sw	s7,16(t4)
9fc04530:	00000000 	nop
9fc04534:	02380018 	mult	s1,t8
9fc04538:	00002012 	mflo	a0
9fc0453c:	ad840014 	sw	a0,20(t4)
9fc04540:	00000000 	nop
9fc04544:	02360018 	mult	s1,s6
9fc04548:	00004012 	mflo	t0
9fc0454c:	ad880018 	sw	t0,24(t4)
9fc04550:	00000000 	nop
9fc04554:	02350018 	mult	s1,s5
9fc04558:	00003812 	mflo	a3
9fc0455c:	ad87001c 	sw	a3,28(t4)
9fc04560:	1660ffd3 	bnez	s3,9fc044b0 <core_bench_matrix+0x350>
9fc04564:	258c0020 	addiu	t4,t4,32
9fc04568:	26100001 	addiu	s0,s0,1
9fc0456c:	8fac001c 	lw	t4,28(sp)
9fc04570:	0214582b 	sltu	t3,s0,s4
9fc04574:	01cc7021 	addu	t6,t6,t4
9fc04578:	1560ff79 	bnez	t3,9fc04360 <core_bench_matrix+0x200>
9fc0457c:	01f27821 	addu	t7,t7,s2
9fc04580:	8faf0034 	lw	t7,52(sp)
9fc04584:	240ef000 	li	t6,-4096
9fc04588:	8fab0030 	lw	t3,48(sp)
9fc0458c:	01eef025 	or	s8,t7,t6
9fc04590:	00003021 	move	a2,zero
9fc04594:	00001821 	move	v1,zero
9fc04598:	00002021 	move	a0,zero
9fc0459c:	00006021 	move	t4,zero
9fc045a0:	2687ffff 	addiu	a3,s4,-1
9fc045a4:	30ea0003 	andi	t2,a3,0x3
9fc045a8:	01604021 	move	t0,t3
9fc045ac:	1140003f 	beqz	t2,9fc046ac <core_bench_matrix+0x54c>
9fc045b0:	00004821 	move	t1,zero
9fc045b4:	8d670000 	lw	a3,0(t3)
9fc045b8:	3095ffff 	andi	s5,a0,0xffff
9fc045bc:	0067b02a 	slt	s6,v1,a3
9fc045c0:	00c73021 	addu	a2,a2,a3
9fc045c4:	02b6b821 	addu	s7,s5,s6
9fc045c8:	26b9000a 	addiu	t9,s5,10
9fc045cc:	00199c00 	sll	s3,t9,0x10
9fc045d0:	0017fc00 	sll	ra,s7,0x10
9fc045d4:	03c6282a 	slt	a1,s8,a2
9fc045d8:	00131403 	sra	v0,s3,0x10
9fc045dc:	10a00002 	beqz	a1,9fc045e8 <core_bench_matrix+0x488>
9fc045e0:	001f2403 	sra	a0,ra,0x10
9fc045e4:	00402021 	move	a0,v0
9fc045e8:	10a00002 	beqz	a1,9fc045f4 <core_bench_matrix+0x494>
9fc045ec:	24090001 	li	t1,1
9fc045f0:	00003021 	move	a2,zero
9fc045f4:	0134882b 	sltu	s1,t1,s4
9fc045f8:	12200079 	beqz	s1,9fc047e0 <core_bench_matrix+0x680>
9fc045fc:	25680004 	addiu	t0,t3,4
9fc04600:	1149002a 	beq	t2,t1,9fc046ac <core_bench_matrix+0x54c>
9fc04604:	00e01821 	move	v1,a3
9fc04608:	24180002 	li	t8,2
9fc0460c:	11580014 	beq	t2,t8,9fc04660 <core_bench_matrix+0x500>
9fc04610:	308fffff 	andi	t7,a0,0xffff
9fc04614:	8d050000 	lw	a1,0(t0)
9fc04618:	00000000 	nop
9fc0461c:	00c55021 	addu	t2,a2,a1
9fc04620:	00e5302a 	slt	a2,a3,a1
9fc04624:	01e68021 	addu	s0,t7,a2
9fc04628:	25e7000a 	addiu	a3,t7,10
9fc0462c:	00071400 	sll	v0,a3,0x10
9fc04630:	00107400 	sll	t6,s0,0x10
9fc04634:	03ca302a 	slt	a2,s8,t2
9fc04638:	00021403 	sra	v0,v0,0x10
9fc0463c:	10c00002 	beqz	a2,9fc04648 <core_bench_matrix+0x4e8>
9fc04640:	000e2403 	sra	a0,t6,0x10
9fc04644:	00402021 	move	a0,v0
9fc04648:	14c00002 	bnez	a2,9fc04654 <core_bench_matrix+0x4f4>
9fc0464c:	00003021 	move	a2,zero
9fc04650:	01403021 	move	a2,t2
9fc04654:	25290001 	addiu	t1,t1,1
9fc04658:	25080004 	addiu	t0,t0,4
9fc0465c:	00a01821 	move	v1,a1
9fc04660:	8d050000 	lw	a1,0(t0)
9fc04664:	3097ffff 	andi	s7,a0,0xffff
9fc04668:	0065182a 	slt	v1,v1,a1
9fc0466c:	00c53821 	addu	a3,a2,a1
9fc04670:	02e36821 	addu	t5,s7,v1
9fc04674:	26ff000a 	addiu	ra,s7,10
9fc04678:	001f2400 	sll	a0,ra,0x10
9fc0467c:	000d5400 	sll	t2,t5,0x10
9fc04680:	03c7302a 	slt	a2,s8,a3
9fc04684:	00041403 	sra	v0,a0,0x10
9fc04688:	10c00002 	beqz	a2,9fc04694 <core_bench_matrix+0x534>
9fc0468c:	000a2403 	sra	a0,t2,0x10
9fc04690:	00402021 	move	a0,v0
9fc04694:	14c00002 	bnez	a2,9fc046a0 <core_bench_matrix+0x540>
9fc04698:	00003021 	move	a2,zero
9fc0469c:	00e03021 	move	a2,a3
9fc046a0:	25290001 	addiu	t1,t1,1
9fc046a4:	25080004 	addiu	t0,t0,4
9fc046a8:	00a01821 	move	v1,a1
9fc046ac:	8d070000 	lw	a3,0(t0)
9fc046b0:	3093ffff 	andi	s3,a0,0xffff
9fc046b4:	0067282a 	slt	a1,v1,a3
9fc046b8:	02651821 	addu	v1,s3,a1
9fc046bc:	2662000a 	addiu	v0,s3,10
9fc046c0:	00c73021 	addu	a2,a2,a3
9fc046c4:	00025400 	sll	t2,v0,0x10
9fc046c8:	0003fc00 	sll	ra,v1,0x10
9fc046cc:	03c6282a 	slt	a1,s8,a2
9fc046d0:	000a1403 	sra	v0,t2,0x10
9fc046d4:	10a00002 	beqz	a1,9fc046e0 <core_bench_matrix+0x580>
9fc046d8:	001f2403 	sra	a0,ra,0x10
9fc046dc:	00402021 	move	a0,v0
9fc046e0:	10a00002 	beqz	a1,9fc046ec <core_bench_matrix+0x58c>
9fc046e4:	00000000 	nop
9fc046e8:	00003021 	move	a2,zero
9fc046ec:	25290001 	addiu	t1,t1,1
9fc046f0:	0134a82b 	sltu	s5,t1,s4
9fc046f4:	12a0003a 	beqz	s5,9fc047e0 <core_bench_matrix+0x680>
9fc046f8:	250a0004 	addiu	t2,t0,4
9fc046fc:	8d050004 	lw	a1,4(t0)
9fc04700:	3098ffff 	andi	t8,a0,0xffff
9fc04704:	00e5c82a 	slt	t9,a3,a1
9fc04708:	00c54021 	addu	t0,a2,a1
9fc0470c:	03196821 	addu	t5,t8,t9
9fc04710:	2717000a 	addiu	s7,t8,10
9fc04714:	0017b400 	sll	s6,s7,0x10
9fc04718:	000d8400 	sll	s0,t5,0x10
9fc0471c:	03c8302a 	slt	a2,s8,t0
9fc04720:	00162403 	sra	a0,s6,0x10
9fc04724:	10c00002 	beqz	a2,9fc04730 <core_bench_matrix+0x5d0>
9fc04728:	00101403 	sra	v0,s0,0x10
9fc0472c:	00801021 	move	v0,a0
9fc04730:	14c00002 	bnez	a2,9fc0473c <core_bench_matrix+0x5dc>
9fc04734:	00003821 	move	a3,zero
9fc04738:	01003821 	move	a3,t0
9fc0473c:	8d460004 	lw	a2,4(t2)
9fc04740:	3056ffff 	andi	s6,v0,0xffff
9fc04744:	00a6882a 	slt	s1,a1,a2
9fc04748:	00e63821 	addu	a3,a3,a2
9fc0474c:	02d19821 	addu	s3,s6,s1
9fc04750:	26d5000a 	addiu	s5,s6,10
9fc04754:	0015cc00 	sll	t9,s5,0x10
9fc04758:	00134400 	sll	t0,s3,0x10
9fc0475c:	03c7282a 	slt	a1,s8,a3
9fc04760:	00192403 	sra	a0,t9,0x10
9fc04764:	10a00002 	beqz	a1,9fc04770 <core_bench_matrix+0x610>
9fc04768:	00081403 	sra	v0,t0,0x10
9fc0476c:	00801021 	move	v0,a0
9fc04770:	10a00002 	beqz	a1,9fc0477c <core_bench_matrix+0x61c>
9fc04774:	00000000 	nop
9fc04778:	00003821 	move	a3,zero
9fc0477c:	8d450008 	lw	a1,8(t2)
9fc04780:	304fffff 	andi	t7,v0,0xffff
9fc04784:	00c5682a 	slt	t5,a2,a1
9fc04788:	25e2000a 	addiu	v0,t7,10
9fc0478c:	00e53821 	addu	a3,a3,a1
9fc04790:	01ed7021 	addu	t6,t7,t5
9fc04794:	00028400 	sll	s0,v0,0x10
9fc04798:	000ec400 	sll	t8,t6,0x10
9fc0479c:	03c7302a 	slt	a2,s8,a3
9fc047a0:	00101403 	sra	v0,s0,0x10
9fc047a4:	10c00002 	beqz	a2,9fc047b0 <core_bench_matrix+0x650>
9fc047a8:	00182403 	sra	a0,t8,0x10
9fc047ac:	00402021 	move	a0,v0
9fc047b0:	14c00007 	bnez	a2,9fc047d0 <core_bench_matrix+0x670>
9fc047b4:	00000000 	nop
9fc047b8:	00e03021 	move	a2,a3
9fc047bc:	25290003 	addiu	t1,t1,3
9fc047c0:	2548000c 	addiu	t0,t2,12
9fc047c4:	0bf011ab 	j	9fc046ac <core_bench_matrix+0x54c>
9fc047c8:	00a01821 	move	v1,a1
9fc047cc:	00000000 	nop
9fc047d0:	0bf011ef 	j	9fc047bc <core_bench_matrix+0x65c>
9fc047d4:	00003021 	move	a2,zero
	...
9fc047e0:	258c0001 	addiu	t4,t4,1
9fc047e4:	8fa8001c 	lw	t0,28(sp)
9fc047e8:	0194482b 	sltu	t1,t4,s4
9fc047ec:	11200003 	beqz	t1,9fc047fc <core_bench_matrix+0x69c>
9fc047f0:	01685821 	addu	t3,t3,t0
9fc047f4:	0bf01168 	j	9fc045a0 <core_bench_matrix+0x440>
9fc047f8:	00e01821 	move	v1,a3
9fc047fc:	0ff01d20 	jal	9fc07480 <crc16>
9fc04800:	00002821 	move	a1,zero
9fc04804:	8fb60030 	lw	s6,48(sp)
9fc04808:	8fb5002c 	lw	s5,44(sp)
9fc0480c:	0040f821 	move	ra,v0
9fc04810:	0000b821 	move	s7,zero
9fc04814:	8fae0028 	lw	t6,40(sp)
9fc04818:	86b10000 	lh	s1,0(s5)
9fc0481c:	85c40000 	lh	a0,0(t6)
9fc04820:	24130001 	li	s3,1
9fc04824:	00910018 	mult	a0,s1
9fc04828:	8fab0028 	lw	t3,40(sp)
9fc0482c:	268cffff 	addiu	t4,s4,-1
9fc04830:	0274302b 	sltu	a2,s3,s4
9fc04834:	31830007 	andi	v1,t4,0x7
9fc04838:	26b10002 	addiu	s1,s5,2
9fc0483c:	0000c812 	mflo	t9
9fc04840:	10c0007d 	beqz	a2,9fc04a38 <core_bench_matrix+0x8d8>
9fc04844:	25780002 	addiu	t8,t3,2
9fc04848:	10600048 	beqz	v1,9fc0496c <core_bench_matrix+0x80c>
9fc0484c:	00000000 	nop
9fc04850:	1073003c 	beq	v1,s3,9fc04944 <core_bench_matrix+0x7e4>
9fc04854:	240b0002 	li	t3,2
9fc04858:	106b0032 	beq	v1,t3,9fc04924 <core_bench_matrix+0x7c4>
9fc0485c:	24090003 	li	t1,3
9fc04860:	10690028 	beq	v1,t1,9fc04904 <core_bench_matrix+0x7a4>
9fc04864:	24050004 	li	a1,4
9fc04868:	1065001e 	beq	v1,a1,9fc048e4 <core_bench_matrix+0x784>
9fc0486c:	240c0005 	li	t4,5
9fc04870:	106c0014 	beq	v1,t4,9fc048c4 <core_bench_matrix+0x764>
9fc04874:	24070006 	li	a3,6
9fc04878:	1067000a 	beq	v1,a3,9fc048a4 <core_bench_matrix+0x744>
9fc0487c:	00000000 	nop
9fc04880:	8faa0028 	lw	t2,40(sp)
9fc04884:	86280000 	lh	t0,0(s1)
9fc04888:	85580002 	lh	t8,2(t2)
9fc0488c:	24130002 	li	s3,2
9fc04890:	03080018 	mult	t8,t0
9fc04894:	26b10004 	addiu	s1,s5,4
9fc04898:	25580004 	addiu	t8,t2,4
9fc0489c:	00003012 	mflo	a2
9fc048a0:	0326c821 	addu	t9,t9,a2
9fc048a4:	870d0000 	lh	t5,0(t8)
9fc048a8:	86240000 	lh	a0,0(s1)
9fc048ac:	26730001 	addiu	s3,s3,1
9fc048b0:	01a40018 	mult	t5,a0
9fc048b4:	26310002 	addiu	s1,s1,2
9fc048b8:	27180002 	addiu	t8,t8,2
9fc048bc:	00007812 	mflo	t7
9fc048c0:	032fc821 	addu	t9,t9,t7
9fc048c4:	87020000 	lh	v0,0(t8)
9fc048c8:	862e0000 	lh	t6,0(s1)
9fc048cc:	26730001 	addiu	s3,s3,1
9fc048d0:	004e0018 	mult	v0,t6
9fc048d4:	26310002 	addiu	s1,s1,2
9fc048d8:	27180002 	addiu	t8,t8,2
9fc048dc:	00001812 	mflo	v1
9fc048e0:	0323c821 	addu	t9,t9,v1
9fc048e4:	870b0000 	lh	t3,0(t8)
9fc048e8:	86290000 	lh	t1,0(s1)
9fc048ec:	26730001 	addiu	s3,s3,1
9fc048f0:	01690018 	mult	t3,t1
9fc048f4:	26310002 	addiu	s1,s1,2
9fc048f8:	27180002 	addiu	t8,t8,2
9fc048fc:	00008012 	mflo	s0
9fc04900:	0330c821 	addu	t9,t9,s0
9fc04904:	870c0000 	lh	t4,0(t8)
9fc04908:	86270000 	lh	a3,0(s1)
9fc0490c:	26730001 	addiu	s3,s3,1
9fc04910:	01870018 	mult	t4,a3
9fc04914:	26310002 	addiu	s1,s1,2
9fc04918:	27180002 	addiu	t8,t8,2
9fc0491c:	00002812 	mflo	a1
9fc04920:	0325c821 	addu	t9,t9,a1
9fc04924:	870a0000 	lh	t2,0(t8)
9fc04928:	86280000 	lh	t0,0(s1)
9fc0492c:	26730001 	addiu	s3,s3,1
9fc04930:	01480018 	mult	t2,t0
9fc04934:	26310002 	addiu	s1,s1,2
9fc04938:	27180002 	addiu	t8,t8,2
9fc0493c:	00003012 	mflo	a2
9fc04940:	0326c821 	addu	t9,t9,a2
9fc04944:	87040000 	lh	a0,0(t8)
9fc04948:	86230000 	lh	v1,0(s1)
9fc0494c:	26730001 	addiu	s3,s3,1
9fc04950:	00830018 	mult	a0,v1
9fc04954:	0274782b 	sltu	t7,s3,s4
9fc04958:	26310002 	addiu	s1,s1,2
9fc0495c:	27180002 	addiu	t8,t8,2
9fc04960:	00006812 	mflo	t5
9fc04964:	11e00034 	beqz	t7,9fc04a38 <core_bench_matrix+0x8d8>
9fc04968:	032dc821 	addu	t9,t9,t5
9fc0496c:	87080000 	lh	t0,0(t8)
9fc04970:	86230000 	lh	v1,0(s1)
9fc04974:	87100002 	lh	s0,2(t8)
9fc04978:	01030018 	mult	t0,v1
9fc0497c:	862a0002 	lh	t2,2(s1)
9fc04980:	87040004 	lh	a0,4(t8)
9fc04984:	862b0004 	lh	t3,4(s1)
9fc04988:	87050006 	lh	a1,6(t8)
9fc0498c:	862c0006 	lh	t4,6(s1)
9fc04990:	87060008 	lh	a2,8(t8)
9fc04994:	862d0008 	lh	t5,8(s1)
9fc04998:	8707000a 	lh	a3,10(t8)
9fc0499c:	862e000a 	lh	t6,10(s1)
9fc049a0:	862f000c 	lh	t7,12(s1)
9fc049a4:	8708000c 	lh	t0,12(t8)
9fc049a8:	8703000e 	lh	v1,14(t8)
9fc049ac:	00001012 	mflo	v0
9fc049b0:	26730008 	addiu	s3,s3,8
9fc049b4:	03221021 	addu	v0,t9,v0
9fc049b8:	27180010 	addiu	t8,t8,16
9fc049bc:	020a0018 	mult	s0,t2
9fc049c0:	8630000e 	lh	s0,14(s1)
9fc049c4:	0274502b 	sltu	t2,s3,s4
9fc049c8:	26310010 	addiu	s1,s1,16
9fc049cc:	00004812 	mflo	t1
9fc049d0:	0049c821 	addu	t9,v0,t1
9fc049d4:	00000000 	nop
9fc049d8:	008b0018 	mult	a0,t3
9fc049dc:	00002012 	mflo	a0
9fc049e0:	03245821 	addu	t3,t9,a0
9fc049e4:	00000000 	nop
9fc049e8:	00ac0018 	mult	a1,t4
9fc049ec:	00006012 	mflo	t4
9fc049f0:	016c4821 	addu	t1,t3,t4
9fc049f4:	00000000 	nop
9fc049f8:	00cd0018 	mult	a2,t5
9fc049fc:	00003012 	mflo	a2
9fc04a00:	01261021 	addu	v0,t1,a2
9fc04a04:	00000000 	nop
9fc04a08:	00ee0018 	mult	a3,t6
9fc04a0c:	00002812 	mflo	a1
9fc04a10:	00456821 	addu	t5,v0,a1
9fc04a14:	00000000 	nop
9fc04a18:	010f0018 	mult	t0,t7
9fc04a1c:	0000c812 	mflo	t9
9fc04a20:	01b97821 	addu	t7,t5,t9
9fc04a24:	00000000 	nop
9fc04a28:	00700018 	mult	v1,s0
9fc04a2c:	00008012 	mflo	s0
9fc04a30:	1540ffce 	bnez	t2,9fc0496c <core_bench_matrix+0x80c>
9fc04a34:	01f0c821 	addu	t9,t7,s0
9fc04a38:	26f70001 	addiu	s7,s7,1
9fc04a3c:	02f4c02b 	sltu	t8,s7,s4
9fc04a40:	aed90000 	sw	t9,0(s6)
9fc04a44:	02b2a821 	addu	s5,s5,s2
9fc04a48:	1700ff72 	bnez	t8,9fc04814 <core_bench_matrix+0x6b4>
9fc04a4c:	26d60004 	addiu	s6,s6,4
9fc04a50:	8fab0030 	lw	t3,48(sp)
9fc04a54:	00003021 	move	a2,zero
9fc04a58:	00001821 	move	v1,zero
9fc04a5c:	00002021 	move	a0,zero
9fc04a60:	00006021 	move	t4,zero
9fc04a64:	2687ffff 	addiu	a3,s4,-1
9fc04a68:	30ea0003 	andi	t2,a3,0x3
9fc04a6c:	01604021 	move	t0,t3
9fc04a70:	1140003f 	beqz	t2,9fc04b70 <core_bench_matrix+0xa10>
9fc04a74:	00004821 	move	t1,zero
9fc04a78:	8d670000 	lw	a3,0(t3)
9fc04a7c:	3095ffff 	andi	s5,a0,0xffff
9fc04a80:	0067b02a 	slt	s6,v1,a3
9fc04a84:	00c73021 	addu	a2,a2,a3
9fc04a88:	02b6c021 	addu	t8,s5,s6
9fc04a8c:	26b9000a 	addiu	t9,s5,10
9fc04a90:	00199c00 	sll	s3,t9,0x10
9fc04a94:	0018bc00 	sll	s7,t8,0x10
9fc04a98:	03c6282a 	slt	a1,s8,a2
9fc04a9c:	00131403 	sra	v0,s3,0x10
9fc04aa0:	10a00002 	beqz	a1,9fc04aac <core_bench_matrix+0x94c>
9fc04aa4:	00172403 	sra	a0,s7,0x10
9fc04aa8:	00402021 	move	a0,v0
9fc04aac:	10a00002 	beqz	a1,9fc04ab8 <core_bench_matrix+0x958>
9fc04ab0:	24090001 	li	t1,1
9fc04ab4:	00003021 	move	a2,zero
9fc04ab8:	0134782b 	sltu	t7,t1,s4
9fc04abc:	11e00078 	beqz	t7,9fc04ca0 <core_bench_matrix+0xb40>
9fc04ac0:	25680004 	addiu	t0,t3,4
9fc04ac4:	1149002a 	beq	t2,t1,9fc04b70 <core_bench_matrix+0xa10>
9fc04ac8:	00e01821 	move	v1,a3
9fc04acc:	240d0002 	li	t5,2
9fc04ad0:	114d0014 	beq	t2,t5,9fc04b24 <core_bench_matrix+0x9c4>
9fc04ad4:	308effff 	andi	t6,a0,0xffff
9fc04ad8:	8d050000 	lw	a1,0(t0)
9fc04adc:	00000000 	nop
9fc04ae0:	00c55021 	addu	t2,a2,a1
9fc04ae4:	00e5302a 	slt	a2,a3,a1
9fc04ae8:	01c61821 	addu	v1,t6,a2
9fc04aec:	25c2000a 	addiu	v0,t6,10
9fc04af0:	00022400 	sll	a0,v0,0x10
9fc04af4:	00033c00 	sll	a3,v1,0x10
9fc04af8:	03ca302a 	slt	a2,s8,t2
9fc04afc:	00041403 	sra	v0,a0,0x10
9fc04b00:	10c00002 	beqz	a2,9fc04b0c <core_bench_matrix+0x9ac>
9fc04b04:	00072403 	sra	a0,a3,0x10
9fc04b08:	00402021 	move	a0,v0
9fc04b0c:	14c00002 	bnez	a2,9fc04b18 <core_bench_matrix+0x9b8>
9fc04b10:	00003021 	move	a2,zero
9fc04b14:	01403021 	move	a2,t2
9fc04b18:	25290001 	addiu	t1,t1,1
9fc04b1c:	25080004 	addiu	t0,t0,4
9fc04b20:	00a01821 	move	v1,a1
9fc04b24:	8d050000 	lw	a1,0(t0)
9fc04b28:	3098ffff 	andi	t8,a0,0xffff
9fc04b2c:	0065982a 	slt	s3,v1,a1
9fc04b30:	00c53821 	addu	a3,a2,a1
9fc04b34:	03138021 	addu	s0,t8,s3
9fc04b38:	2717000a 	addiu	s7,t8,10
9fc04b3c:	00178c00 	sll	s1,s7,0x10
9fc04b40:	00105400 	sll	t2,s0,0x10
9fc04b44:	03c7302a 	slt	a2,s8,a3
9fc04b48:	00111403 	sra	v0,s1,0x10
9fc04b4c:	10c00002 	beqz	a2,9fc04b58 <core_bench_matrix+0x9f8>
9fc04b50:	000a2403 	sra	a0,t2,0x10
9fc04b54:	00402021 	move	a0,v0
9fc04b58:	14c00002 	bnez	a2,9fc04b64 <core_bench_matrix+0xa04>
9fc04b5c:	00003021 	move	a2,zero
9fc04b60:	00e03021 	move	a2,a3
9fc04b64:	25290001 	addiu	t1,t1,1
9fc04b68:	25080004 	addiu	t0,t0,4
9fc04b6c:	00a01821 	move	v1,a1
9fc04b70:	8d070000 	lw	a3,0(t0)
9fc04b74:	3085ffff 	andi	a1,a0,0xffff
9fc04b78:	0067102a 	slt	v0,v1,a3
9fc04b7c:	00a26821 	addu	t5,a1,v0
9fc04b80:	24a4000a 	addiu	a0,a1,10
9fc04b84:	00c73021 	addu	a2,a2,a3
9fc04b88:	0004cc00 	sll	t9,a0,0x10
9fc04b8c:	000d7400 	sll	t6,t5,0x10
9fc04b90:	03c6282a 	slt	a1,s8,a2
9fc04b94:	00191403 	sra	v0,t9,0x10
9fc04b98:	10a00002 	beqz	a1,9fc04ba4 <core_bench_matrix+0xa44>
9fc04b9c:	000e2403 	sra	a0,t6,0x10
9fc04ba0:	00402021 	move	a0,v0
9fc04ba4:	10a00002 	beqz	a1,9fc04bb0 <core_bench_matrix+0xa50>
9fc04ba8:	00000000 	nop
9fc04bac:	00003021 	move	a2,zero
9fc04bb0:	25290001 	addiu	t1,t1,1
9fc04bb4:	0134802b 	sltu	s0,t1,s4
9fc04bb8:	12000039 	beqz	s0,9fc04ca0 <core_bench_matrix+0xb40>
9fc04bbc:	250a0004 	addiu	t2,t0,4
9fc04bc0:	8d050004 	lw	a1,4(t0)
9fc04bc4:	3091ffff 	andi	s1,a0,0xffff
9fc04bc8:	00e5782a 	slt	t7,a3,a1
9fc04bcc:	00c54021 	addu	t0,a2,a1
9fc04bd0:	022fb821 	addu	s7,s1,t7
9fc04bd4:	2635000a 	addiu	s5,s1,10
9fc04bd8:	00159c00 	sll	s3,s5,0x10
9fc04bdc:	0017b400 	sll	s6,s7,0x10
9fc04be0:	03c8302a 	slt	a2,s8,t0
9fc04be4:	00132403 	sra	a0,s3,0x10
9fc04be8:	10c00002 	beqz	a2,9fc04bf4 <core_bench_matrix+0xa94>
9fc04bec:	00161403 	sra	v0,s6,0x10
9fc04bf0:	00801021 	move	v0,a0
9fc04bf4:	14c00002 	bnez	a2,9fc04c00 <core_bench_matrix+0xaa0>
9fc04bf8:	00003821 	move	a3,zero
9fc04bfc:	01003821 	move	a3,t0
9fc04c00:	8d460004 	lw	a2,4(t2)
9fc04c04:	304fffff 	andi	t7,v0,0xffff
9fc04c08:	00a6682a 	slt	t5,a1,a2
9fc04c0c:	00e63821 	addu	a3,a3,a2
9fc04c10:	01edc821 	addu	t9,t7,t5
9fc04c14:	25f6000a 	addiu	s6,t7,10
9fc04c18:	0016ac00 	sll	s5,s6,0x10
9fc04c1c:	00194400 	sll	t0,t9,0x10
9fc04c20:	03c7282a 	slt	a1,s8,a3
9fc04c24:	00152403 	sra	a0,s5,0x10
9fc04c28:	10a00002 	beqz	a1,9fc04c34 <core_bench_matrix+0xad4>
9fc04c2c:	00081403 	sra	v0,t0,0x10
9fc04c30:	00801021 	move	v0,a0
9fc04c34:	10a00002 	beqz	a1,9fc04c40 <core_bench_matrix+0xae0>
9fc04c38:	00000000 	nop
9fc04c3c:	00003821 	move	a3,zero
9fc04c40:	8d450008 	lw	a1,8(t2)
9fc04c44:	3050ffff 	andi	s0,v0,0xffff
9fc04c48:	00c5882a 	slt	s1,a2,a1
9fc04c4c:	00e53821 	addu	a3,a3,a1
9fc04c50:	02111821 	addu	v1,s0,s1
9fc04c54:	260e000a 	addiu	t6,s0,10
9fc04c58:	000e1400 	sll	v0,t6,0x10
9fc04c5c:	00032400 	sll	a0,v1,0x10
9fc04c60:	03c7302a 	slt	a2,s8,a3
9fc04c64:	00021403 	sra	v0,v0,0x10
9fc04c68:	10c00002 	beqz	a2,9fc04c74 <core_bench_matrix+0xb14>
9fc04c6c:	00042403 	sra	a0,a0,0x10
9fc04c70:	00402021 	move	a0,v0
9fc04c74:	14c00006 	bnez	a2,9fc04c90 <core_bench_matrix+0xb30>
9fc04c78:	00000000 	nop
9fc04c7c:	00e03021 	move	a2,a3
9fc04c80:	25290003 	addiu	t1,t1,3
9fc04c84:	2548000c 	addiu	t0,t2,12
9fc04c88:	0bf012dc 	j	9fc04b70 <core_bench_matrix+0xa10>
9fc04c8c:	00a01821 	move	v1,a1
9fc04c90:	0bf01320 	j	9fc04c80 <core_bench_matrix+0xb20>
9fc04c94:	00003021 	move	a2,zero
	...
9fc04ca0:	258c0001 	addiu	t4,t4,1
9fc04ca4:	8faa001c 	lw	t2,28(sp)
9fc04ca8:	0194482b 	sltu	t1,t4,s4
9fc04cac:	11200003 	beqz	t1,9fc04cbc <core_bench_matrix+0xb5c>
9fc04cb0:	016a5821 	addu	t3,t3,t2
9fc04cb4:	0bf01299 	j	9fc04a64 <core_bench_matrix+0x904>
9fc04cb8:	00e01821 	move	v1,a3
9fc04cbc:	03e02821 	move	a1,ra
9fc04cc0:	0ff01d20 	jal	9fc07480 <crc16>
9fc04cc4:	00000000 	nop
9fc04cc8:	8fa60030 	lw	a2,48(sp)
9fc04ccc:	8fb6002c 	lw	s6,44(sp)
9fc04cd0:	afa20018 	sw	v0,24(sp)
9fc04cd4:	afa60014 	sw	a2,20(sp)
9fc04cd8:	afa00024 	sw	zero,36(sp)
9fc04cdc:	8fb70014 	lw	s7,20(sp)
9fc04ce0:	8fb30028 	lw	s3,40(sp)
9fc04ce4:	0000a821 	move	s5,zero
9fc04ce8:	86df0000 	lh	ra,0(s6)
9fc04cec:	86680000 	lh	t0,0(s3)
9fc04cf0:	24190001 	li	t9,1
9fc04cf4:	011f0018 	mult	t0,ra
9fc04cf8:	268bffff 	addiu	t3,s4,-1
9fc04cfc:	0334602b 	sltu	t4,t9,s4
9fc04d00:	31630007 	andi	v1,t3,0x7
9fc04d04:	26d80002 	addiu	t8,s6,2
9fc04d08:	0000f812 	mflo	ra
9fc04d0c:	11800084 	beqz	t4,9fc04f20 <core_bench_matrix+0xdc0>
9fc04d10:	02725021 	addu	t2,s3,s2
9fc04d14:	10600047 	beqz	v1,9fc04e34 <core_bench_matrix+0xcd4>
9fc04d18:	00000000 	nop
9fc04d1c:	1079003b 	beq	v1,t9,9fc04e0c <core_bench_matrix+0xcac>
9fc04d20:	240b0002 	li	t3,2
9fc04d24:	106b0031 	beq	v1,t3,9fc04dec <core_bench_matrix+0xc8c>
9fc04d28:	24090003 	li	t1,3
9fc04d2c:	10690027 	beq	v1,t1,9fc04dcc <core_bench_matrix+0xc6c>
9fc04d30:	24050004 	li	a1,4
9fc04d34:	1065001d 	beq	v1,a1,9fc04dac <core_bench_matrix+0xc4c>
9fc04d38:	240c0005 	li	t4,5
9fc04d3c:	106c0013 	beq	v1,t4,9fc04d8c <core_bench_matrix+0xc2c>
9fc04d40:	24070006 	li	a3,6
9fc04d44:	10670009 	beq	v1,a3,9fc04d6c <core_bench_matrix+0xc0c>
9fc04d48:	00000000 	nop
9fc04d4c:	87020000 	lh	v0,0(t8)
9fc04d50:	85440000 	lh	a0,0(t2)
9fc04d54:	24190002 	li	t9,2
9fc04d58:	00820018 	mult	a0,v0
9fc04d5c:	01525021 	addu	t2,t2,s2
9fc04d60:	26d80004 	addiu	t8,s6,4
9fc04d64:	00003012 	mflo	a2
9fc04d68:	03e6f821 	addu	ra,ra,a2
9fc04d6c:	854e0000 	lh	t6,0(t2)
9fc04d70:	87100000 	lh	s0,0(t8)
9fc04d74:	27390001 	addiu	t9,t9,1
9fc04d78:	01d00018 	mult	t6,s0
9fc04d7c:	27180002 	addiu	t8,t8,2
9fc04d80:	01525021 	addu	t2,t2,s2
9fc04d84:	00004012 	mflo	t0
9fc04d88:	03e8f821 	addu	ra,ra,t0
9fc04d8c:	854f0000 	lh	t7,0(t2)
9fc04d90:	870d0000 	lh	t5,0(t8)
9fc04d94:	27390001 	addiu	t9,t9,1
9fc04d98:	01ed0018 	mult	t7,t5
9fc04d9c:	27180002 	addiu	t8,t8,2
9fc04da0:	01525021 	addu	t2,t2,s2
9fc04da4:	00008812 	mflo	s1
9fc04da8:	03f1f821 	addu	ra,ra,s1
9fc04dac:	854b0000 	lh	t3,0(t2)
9fc04db0:	87090000 	lh	t1,0(t8)
9fc04db4:	27390001 	addiu	t9,t9,1
9fc04db8:	01690018 	mult	t3,t1
9fc04dbc:	27180002 	addiu	t8,t8,2
9fc04dc0:	01525021 	addu	t2,t2,s2
9fc04dc4:	00001812 	mflo	v1
9fc04dc8:	03e3f821 	addu	ra,ra,v1
9fc04dcc:	854c0000 	lh	t4,0(t2)
9fc04dd0:	87070000 	lh	a3,0(t8)
9fc04dd4:	27390001 	addiu	t9,t9,1
9fc04dd8:	01870018 	mult	t4,a3
9fc04ddc:	27180002 	addiu	t8,t8,2
9fc04de0:	01525021 	addu	t2,t2,s2
9fc04de4:	00002812 	mflo	a1
9fc04de8:	03e5f821 	addu	ra,ra,a1
9fc04dec:	85440000 	lh	a0,0(t2)
9fc04df0:	87020000 	lh	v0,0(t8)
9fc04df4:	27390001 	addiu	t9,t9,1
9fc04df8:	00820018 	mult	a0,v0
9fc04dfc:	27180002 	addiu	t8,t8,2
9fc04e00:	01525021 	addu	t2,t2,s2
9fc04e04:	00003012 	mflo	a2
9fc04e08:	03e6f821 	addu	ra,ra,a2
9fc04e0c:	85500000 	lh	s0,0(t2)
9fc04e10:	87110000 	lh	s1,0(t8)
9fc04e14:	27390001 	addiu	t9,t9,1
9fc04e18:	02110018 	mult	s0,s1
9fc04e1c:	0334402b 	sltu	t0,t9,s4
9fc04e20:	27180002 	addiu	t8,t8,2
9fc04e24:	01525021 	addu	t2,t2,s2
9fc04e28:	00007012 	mflo	t6
9fc04e2c:	1100003c 	beqz	t0,9fc04f20 <core_bench_matrix+0xdc0>
9fc04e30:	03eef821 	addu	ra,ra,t6
9fc04e34:	854f0000 	lh	t7,0(t2)
9fc04e38:	87080000 	lh	t0,0(t8)
9fc04e3c:	01523821 	addu	a3,t2,s2
9fc04e40:	01e80018 	mult	t7,t0
9fc04e44:	870d0002 	lh	t5,2(t8)
9fc04e48:	84ec0000 	lh	t4,0(a3)
9fc04e4c:	00f27021 	addu	t6,a3,s2
9fc04e50:	87110004 	lh	s1,4(t8)
9fc04e54:	85c90000 	lh	t1,0(t6)
9fc04e58:	01d23021 	addu	a2,t6,s2
9fc04e5c:	87100006 	lh	s0,6(t8)
9fc04e60:	84c80000 	lh	t0,0(a2)
9fc04e64:	00d22021 	addu	a0,a2,s2
9fc04e68:	870f0008 	lh	t7,8(t8)
9fc04e6c:	84870000 	lh	a3,0(a0)
9fc04e70:	00922821 	addu	a1,a0,s2
9fc04e74:	00001012 	mflo	v0
9fc04e78:	870e000a 	lh	t6,10(t8)
9fc04e7c:	03e21021 	addu	v0,ra,v0
9fc04e80:	84a60000 	lh	a2,0(a1)
9fc04e84:	018d0018 	mult	t4,t5
9fc04e88:	00b21821 	addu	v1,a1,s2
9fc04e8c:	870c000c 	lh	t4,12(t8)
9fc04e90:	84640000 	lh	a0,0(v1)
9fc04e94:	00725021 	addu	t2,v1,s2
9fc04e98:	85430000 	lh	v1,0(t2)
9fc04e9c:	870d000e 	lh	t5,14(t8)
9fc04ea0:	27390008 	addiu	t9,t9,8
9fc04ea4:	0334282b 	sltu	a1,t9,s4
9fc04ea8:	27180010 	addiu	t8,t8,16
9fc04eac:	01525021 	addu	t2,t2,s2
9fc04eb0:	00005812 	mflo	t3
	...
9fc04ebc:	01310018 	mult	t1,s1
9fc04ec0:	004b8821 	addu	s1,v0,t3
9fc04ec4:	00004812 	mflo	t1
9fc04ec8:	02295821 	addu	t3,s1,t1
9fc04ecc:	00000000 	nop
9fc04ed0:	01100018 	mult	t0,s0
9fc04ed4:	0000f812 	mflo	ra
9fc04ed8:	017f4821 	addu	t1,t3,ra
9fc04edc:	00000000 	nop
9fc04ee0:	00ef0018 	mult	a3,t7
9fc04ee4:	00003812 	mflo	a3
9fc04ee8:	01271021 	addu	v0,t1,a3
9fc04eec:	00000000 	nop
9fc04ef0:	00ce0018 	mult	a2,t6
9fc04ef4:	00008012 	mflo	s0
9fc04ef8:	00507821 	addu	t7,v0,s0
9fc04efc:	00000000 	nop
9fc04f00:	008c0018 	mult	a0,t4
9fc04f04:	00007012 	mflo	t6
9fc04f08:	01ee8821 	addu	s1,t7,t6
9fc04f0c:	00000000 	nop
9fc04f10:	006d0018 	mult	v1,t5
9fc04f14:	00001812 	mflo	v1
9fc04f18:	14a0ffc6 	bnez	a1,9fc04e34 <core_bench_matrix+0xcd4>
9fc04f1c:	0223f821 	addu	ra,s1,v1
9fc04f20:	26b50001 	addiu	s5,s5,1
9fc04f24:	02b4c02b 	sltu	t8,s5,s4
9fc04f28:	aeff0000 	sw	ra,0(s7)
9fc04f2c:	26730002 	addiu	s3,s3,2
9fc04f30:	1700ff6d 	bnez	t8,9fc04ce8 <core_bench_matrix+0xb88>
9fc04f34:	26f70004 	addiu	s7,s7,4
9fc04f38:	8fb00024 	lw	s0,36(sp)
9fc04f3c:	8fb50014 	lw	s5,20(sp)
9fc04f40:	8fb9001c 	lw	t9,28(sp)
9fc04f44:	261f0001 	addiu	ra,s0,1
9fc04f48:	02b99821 	addu	s3,s5,t9
9fc04f4c:	03f4b82b 	sltu	s7,ra,s4
9fc04f50:	afbf0024 	sw	ra,36(sp)
9fc04f54:	02d2b021 	addu	s6,s6,s2
9fc04f58:	16e0ff60 	bnez	s7,9fc04cdc <core_bench_matrix+0xb7c>
9fc04f5c:	afb30014 	sw	s3,20(sp)
9fc04f60:	8fab0030 	lw	t3,48(sp)
9fc04f64:	00003021 	move	a2,zero
9fc04f68:	00001821 	move	v1,zero
9fc04f6c:	00002021 	move	a0,zero
9fc04f70:	00006021 	move	t4,zero
9fc04f74:	2687ffff 	addiu	a3,s4,-1
9fc04f78:	30ea0003 	andi	t2,a3,0x3
9fc04f7c:	01604021 	move	t0,t3
9fc04f80:	1140003f 	beqz	t2,9fc05080 <core_bench_matrix+0xf20>
9fc04f84:	00004821 	move	t1,zero
9fc04f88:	8d670000 	lw	a3,0(t3)
9fc04f8c:	3090ffff 	andi	s0,a0,0xffff
9fc04f90:	0067c82a 	slt	t9,v1,a3
9fc04f94:	00c73021 	addu	a2,a2,a3
9fc04f98:	0219c021 	addu	t8,s0,t9
9fc04f9c:	2613000a 	addiu	s3,s0,10
9fc04fa0:	00137400 	sll	t6,s3,0x10
9fc04fa4:	0018bc00 	sll	s7,t8,0x10
9fc04fa8:	03c6282a 	slt	a1,s8,a2
9fc04fac:	000e1403 	sra	v0,t6,0x10
9fc04fb0:	10a00002 	beqz	a1,9fc04fbc <core_bench_matrix+0xe5c>
9fc04fb4:	00172403 	sra	a0,s7,0x10
9fc04fb8:	00402021 	move	a0,v0
9fc04fbc:	10a00002 	beqz	a1,9fc04fc8 <core_bench_matrix+0xe68>
9fc04fc0:	24090001 	li	t1,1
9fc04fc4:	00003021 	move	a2,zero
9fc04fc8:	0134882b 	sltu	s1,t1,s4
9fc04fcc:	12200078 	beqz	s1,9fc051b0 <core_bench_matrix+0x1050>
9fc04fd0:	25680004 	addiu	t0,t3,4
9fc04fd4:	1149002a 	beq	t2,t1,9fc05080 <core_bench_matrix+0xf20>
9fc04fd8:	00e01821 	move	v1,a3
9fc04fdc:	24150002 	li	s5,2
9fc04fe0:	11550014 	beq	t2,s5,9fc05034 <core_bench_matrix+0xed4>
9fc04fe4:	309fffff 	andi	ra,a0,0xffff
9fc04fe8:	8d050000 	lw	a1,0(t0)
9fc04fec:	00000000 	nop
9fc04ff0:	00c55021 	addu	t2,a2,a1
9fc04ff4:	00e5302a 	slt	a2,a3,a1
9fc04ff8:	03e67821 	addu	t7,ra,a2
9fc04ffc:	27e7000a 	addiu	a3,ra,10
9fc05000:	00076c00 	sll	t5,a3,0x10
9fc05004:	000fb400 	sll	s6,t7,0x10
9fc05008:	03ca302a 	slt	a2,s8,t2
9fc0500c:	000d1403 	sra	v0,t5,0x10
9fc05010:	10c00002 	beqz	a2,9fc0501c <core_bench_matrix+0xebc>
9fc05014:	00162403 	sra	a0,s6,0x10
9fc05018:	00402021 	move	a0,v0
9fc0501c:	14c00002 	bnez	a2,9fc05028 <core_bench_matrix+0xec8>
9fc05020:	00003021 	move	a2,zero
9fc05024:	01403021 	move	a2,t2
9fc05028:	25290001 	addiu	t1,t1,1
9fc0502c:	25080004 	addiu	t0,t0,4
9fc05030:	00a01821 	move	v1,a1
9fc05034:	8d050000 	lw	a1,0(t0)
9fc05038:	3097ffff 	andi	s7,a0,0xffff
9fc0503c:	0065c02a 	slt	t8,v1,a1
9fc05040:	26e2000a 	addiu	v0,s7,10
9fc05044:	00c53821 	addu	a3,a2,a1
9fc05048:	02f81821 	addu	v1,s7,t8
9fc0504c:	00022400 	sll	a0,v0,0x10
9fc05050:	00035400 	sll	t2,v1,0x10
9fc05054:	03c7302a 	slt	a2,s8,a3
9fc05058:	00041403 	sra	v0,a0,0x10
9fc0505c:	10c00002 	beqz	a2,9fc05068 <core_bench_matrix+0xf08>
9fc05060:	000a2403 	sra	a0,t2,0x10
9fc05064:	00402021 	move	a0,v0
9fc05068:	14c00002 	bnez	a2,9fc05074 <core_bench_matrix+0xf14>
9fc0506c:	00003021 	move	a2,zero
9fc05070:	00e03021 	move	a2,a3
9fc05074:	25290001 	addiu	t1,t1,1
9fc05078:	25080004 	addiu	t0,t0,4
9fc0507c:	00a01821 	move	v1,a1
9fc05080:	8d070000 	lw	a3,0(t0)
9fc05084:	3085ffff 	andi	a1,a0,0xffff
9fc05088:	0067b82a 	slt	s7,v1,a3
9fc0508c:	00b72021 	addu	a0,a1,s7
9fc05090:	24b8000a 	addiu	t8,a1,10
9fc05094:	00c73021 	addu	a2,a2,a3
9fc05098:	00187c00 	sll	t7,t8,0x10
9fc0509c:	00045400 	sll	t2,a0,0x10
9fc050a0:	03c6282a 	slt	a1,s8,a2
9fc050a4:	000f1403 	sra	v0,t7,0x10
9fc050a8:	10a00002 	beqz	a1,9fc050b4 <core_bench_matrix+0xf54>
9fc050ac:	000a2403 	sra	a0,t2,0x10
9fc050b0:	00402021 	move	a0,v0
9fc050b4:	10a00002 	beqz	a1,9fc050c0 <core_bench_matrix+0xf60>
9fc050b8:	00000000 	nop
9fc050bc:	00003021 	move	a2,zero
9fc050c0:	25290001 	addiu	t1,t1,1
9fc050c4:	0134982b 	sltu	s3,t1,s4
9fc050c8:	12600039 	beqz	s3,9fc051b0 <core_bench_matrix+0x1050>
9fc050cc:	250a0004 	addiu	t2,t0,4
9fc050d0:	8d050004 	lw	a1,4(t0)
9fc050d4:	308effff 	andi	t6,a0,0xffff
9fc050d8:	00e5182a 	slt	v1,a3,a1
9fc050dc:	25c2000a 	addiu	v0,t6,10
9fc050e0:	00c54021 	addu	t0,a2,a1
9fc050e4:	01c38821 	addu	s1,t6,v1
9fc050e8:	00026c00 	sll	t5,v0,0x10
9fc050ec:	0011b400 	sll	s6,s1,0x10
9fc050f0:	03c8302a 	slt	a2,s8,t0
9fc050f4:	000d2403 	sra	a0,t5,0x10
9fc050f8:	10c00002 	beqz	a2,9fc05104 <core_bench_matrix+0xfa4>
9fc050fc:	00161403 	sra	v0,s6,0x10
9fc05100:	00801021 	move	v0,a0
9fc05104:	14c00002 	bnez	a2,9fc05110 <core_bench_matrix+0xfb0>
9fc05108:	00003821 	move	a3,zero
9fc0510c:	01003821 	move	a3,t0
9fc05110:	8d460004 	lw	a2,4(t2)
9fc05114:	3059ffff 	andi	t9,v0,0xffff
9fc05118:	00a6882a 	slt	s1,a1,a2
9fc0511c:	00e63821 	addu	a3,a3,a2
9fc05120:	03317021 	addu	t6,t9,s1
9fc05124:	2730000a 	addiu	s0,t9,10
9fc05128:	00109c00 	sll	s3,s0,0x10
9fc0512c:	000e4400 	sll	t0,t6,0x10
9fc05130:	03c7282a 	slt	a1,s8,a3
9fc05134:	00132403 	sra	a0,s3,0x10
9fc05138:	10a00002 	beqz	a1,9fc05144 <core_bench_matrix+0xfe4>
9fc0513c:	00081403 	sra	v0,t0,0x10
9fc05140:	00801021 	move	v0,a0
9fc05144:	10a00002 	beqz	a1,9fc05150 <core_bench_matrix+0xff0>
9fc05148:	00000000 	nop
9fc0514c:	00003821 	move	a3,zero
9fc05150:	8d450008 	lw	a1,8(t2)
9fc05154:	305fffff 	andi	ra,v0,0xffff
9fc05158:	00c5182a 	slt	v1,a2,a1
9fc0515c:	00e53821 	addu	a3,a3,a1
9fc05160:	03e3b021 	addu	s6,ra,v1
9fc05164:	27ed000a 	addiu	t5,ra,10
9fc05168:	000d7c00 	sll	t7,t5,0x10
9fc0516c:	0016ac00 	sll	s5,s6,0x10
9fc05170:	03c7302a 	slt	a2,s8,a3
9fc05174:	000f1403 	sra	v0,t7,0x10
9fc05178:	10c00002 	beqz	a2,9fc05184 <core_bench_matrix+0x1024>
9fc0517c:	00152403 	sra	a0,s5,0x10
9fc05180:	00402021 	move	a0,v0
9fc05184:	14c00006 	bnez	a2,9fc051a0 <core_bench_matrix+0x1040>
9fc05188:	00000000 	nop
9fc0518c:	00e03021 	move	a2,a3
9fc05190:	25290003 	addiu	t1,t1,3
9fc05194:	2548000c 	addiu	t0,t2,12
9fc05198:	0bf01420 	j	9fc05080 <core_bench_matrix+0xf20>
9fc0519c:	00a01821 	move	v1,a1
9fc051a0:	0bf01464 	j	9fc05190 <core_bench_matrix+0x1030>
9fc051a4:	00003021 	move	a2,zero
	...
9fc051b0:	258c0001 	addiu	t4,t4,1
9fc051b4:	8fa8001c 	lw	t0,28(sp)
9fc051b8:	0194482b 	sltu	t1,t4,s4
9fc051bc:	11200003 	beqz	t1,9fc051cc <core_bench_matrix+0x106c>
9fc051c0:	01685821 	addu	t3,t3,t0
9fc051c4:	0bf013dd 	j	9fc04f74 <core_bench_matrix+0xe14>
9fc051c8:	00e01821 	move	v1,a3
9fc051cc:	8fa50018 	lw	a1,24(sp)
9fc051d0:	0ff01d20 	jal	9fc07480 <crc16>
9fc051d4:	0000b021 	move	s6,zero
9fc051d8:	8fb3002c 	lw	s3,44(sp)
9fc051dc:	8fb50030 	lw	s5,48(sp)
9fc051e0:	0040b821 	move	s7,v0
9fc051e4:	8fb10028 	lw	s1,40(sp)
9fc051e8:	02a0c021 	move	t8,s5
9fc051ec:	0000c821 	move	t9,zero
9fc051f0:	862e0000 	lh	t6,0(s1)
9fc051f4:	86630000 	lh	v1,0(s3)
9fc051f8:	240f0001 	li	t7,1
9fc051fc:	01c30018 	mult	t6,v1
9fc05200:	268dffff 	addiu	t5,s4,-1
9fc05204:	01f4582b 	sltu	t3,t7,s4
9fc05208:	31a40003 	andi	a0,t5,0x3
9fc0520c:	02327021 	addu	t6,s1,s2
9fc05210:	00001012 	mflo	v0
9fc05214:	00028143 	sra	s0,v0,0x5
9fc05218:	0002f883 	sra	ra,v0,0x2
9fc0521c:	320c007f 	andi	t4,s0,0x7f
9fc05220:	33e6000f 	andi	a2,ra,0xf
9fc05224:	01860018 	mult	t4,a2
9fc05228:	00008012 	mflo	s0
9fc0522c:	1160006a 	beqz	t3,9fc053d8 <core_bench_matrix+0x1278>
9fc05230:	266d0002 	addiu	t5,s3,2
9fc05234:	10800031 	beqz	a0,9fc052fc <core_bench_matrix+0x119c>
9fc05238:	00000000 	nop
9fc0523c:	108f001f 	beq	a0,t7,9fc052bc <core_bench_matrix+0x115c>
9fc05240:	240b0002 	li	t3,2
9fc05244:	108b000f 	beq	a0,t3,9fc05284 <core_bench_matrix+0x1124>
9fc05248:	00000000 	nop
9fc0524c:	85af0000 	lh	t7,0(t5)
9fc05250:	85cd0000 	lh	t5,0(t6)
9fc05254:	01d27021 	addu	t6,t6,s2
9fc05258:	01af0018 	mult	t5,t7
9fc0525c:	240f0002 	li	t7,2
9fc05260:	266d0004 	addiu	t5,s3,4
9fc05264:	00004012 	mflo	t0
9fc05268:	00085083 	sra	t2,t0,0x2
9fc0526c:	00083143 	sra	a2,t0,0x5
9fc05270:	30c7007f 	andi	a3,a2,0x7f
9fc05274:	3145000f 	andi	a1,t2,0xf
9fc05278:	00e50018 	mult	a3,a1
9fc0527c:	00004812 	mflo	t1
9fc05280:	02098021 	addu	s0,s0,t1
9fc05284:	85c90000 	lh	t1,0(t6)
9fc05288:	85a50000 	lh	a1,0(t5)
9fc0528c:	25ef0001 	addiu	t7,t7,1
9fc05290:	01250018 	mult	t1,a1
9fc05294:	25ad0002 	addiu	t5,t5,2
9fc05298:	01d27021 	addu	t6,t6,s2
9fc0529c:	00005812 	mflo	t3
9fc052a0:	000b2083 	sra	a0,t3,0x2
9fc052a4:	000b1143 	sra	v0,t3,0x5
9fc052a8:	305f007f 	andi	ra,v0,0x7f
9fc052ac:	3083000f 	andi	v1,a0,0xf
9fc052b0:	03e30018 	mult	ra,v1
9fc052b4:	00006012 	mflo	t4
9fc052b8:	020c8021 	addu	s0,s0,t4
9fc052bc:	85c40000 	lh	a0,0(t6)
9fc052c0:	85a20000 	lh	v0,0(t5)
9fc052c4:	25ef0001 	addiu	t7,t7,1
9fc052c8:	00820018 	mult	a0,v0
9fc052cc:	01f4382b 	sltu	a3,t7,s4
9fc052d0:	25ad0002 	addiu	t5,t5,2
9fc052d4:	01d27021 	addu	t6,t6,s2
9fc052d8:	00001812 	mflo	v1
9fc052dc:	00036083 	sra	t4,v1,0x2
9fc052e0:	0003f943 	sra	ra,v1,0x5
9fc052e4:	33e6007f 	andi	a2,ra,0x7f
9fc052e8:	3188000f 	andi	t0,t4,0xf
9fc052ec:	00c80018 	mult	a2,t0
9fc052f0:	00005012 	mflo	t2
9fc052f4:	10e00038 	beqz	a3,9fc053d8 <core_bench_matrix+0x1278>
9fc052f8:	020a8021 	addu	s0,s0,t2
9fc052fc:	85c60000 	lh	a2,0(t6)
9fc05300:	85a30000 	lh	v1,0(t5)
9fc05304:	01d26021 	addu	t4,t6,s2
9fc05308:	00c30018 	mult	a2,v1
9fc0530c:	85a90002 	lh	t1,2(t5)
9fc05310:	85850000 	lh	a1,0(t4)
9fc05314:	01927021 	addu	t6,t4,s2
9fc05318:	85aa0004 	lh	t2,4(t5)
9fc0531c:	85c40000 	lh	a0,0(t6)
9fc05320:	01d23821 	addu	a3,t6,s2
9fc05324:	85a30006 	lh	v1,6(t5)
9fc05328:	84e60000 	lh	a2,0(a3)
9fc0532c:	00f27021 	addu	t6,a3,s2
9fc05330:	25ef0004 	addiu	t7,t7,4
9fc05334:	01f4602b 	sltu	t4,t7,s4
9fc05338:	25ad0008 	addiu	t5,t5,8
9fc0533c:	0000f812 	mflo	ra
9fc05340:	001f4083 	sra	t0,ra,0x2
9fc05344:	001f1143 	sra	v0,ra,0x5
9fc05348:	00a90018 	mult	a1,t1
9fc0534c:	305f007f 	andi	ra,v0,0x7f
9fc05350:	3108000f 	andi	t0,t0,0xf
9fc05354:	00005812 	mflo	t3
9fc05358:	000b4883 	sra	t1,t3,0x2
9fc0535c:	000b2943 	sra	a1,t3,0x5
9fc05360:	008a0018 	mult	a0,t2
9fc05364:	30a5007f 	andi	a1,a1,0x7f
9fc05368:	3129000f 	andi	t1,t1,0xf
9fc0536c:	00003812 	mflo	a3
9fc05370:	00075083 	sra	t2,a3,0x2
9fc05374:	00072143 	sra	a0,a3,0x5
9fc05378:	00c30018 	mult	a2,v1
9fc0537c:	3084007f 	andi	a0,a0,0x7f
9fc05380:	3147000f 	andi	a3,t2,0xf
9fc05384:	00005812 	mflo	t3
9fc05388:	000b1943 	sra	v1,t3,0x5
9fc0538c:	000b3083 	sra	a2,t3,0x2
9fc05390:	03e80018 	mult	ra,t0
9fc05394:	30cb000f 	andi	t3,a2,0xf
9fc05398:	3068007f 	andi	t0,v1,0x7f
9fc0539c:	00001012 	mflo	v0
9fc053a0:	0202f821 	addu	ra,s0,v0
9fc053a4:	00000000 	nop
9fc053a8:	00a90018 	mult	a1,t1
9fc053ac:	00005012 	mflo	t2
9fc053b0:	03ea4821 	addu	t1,ra,t2
9fc053b4:	00000000 	nop
9fc053b8:	00870018 	mult	a0,a3
9fc053bc:	00002012 	mflo	a0
9fc053c0:	01245021 	addu	t2,t1,a0
9fc053c4:	00000000 	nop
9fc053c8:	010b0018 	mult	t0,t3
9fc053cc:	00002812 	mflo	a1
9fc053d0:	1580ffca 	bnez	t4,9fc052fc <core_bench_matrix+0x119c>
9fc053d4:	01458021 	addu	s0,t2,a1
9fc053d8:	27390001 	addiu	t9,t9,1
9fc053dc:	0334782b 	sltu	t7,t9,s4
9fc053e0:	af100000 	sw	s0,0(t8)
9fc053e4:	26310002 	addiu	s1,s1,2
9fc053e8:	15e0ff81 	bnez	t7,9fc051f0 <core_bench_matrix+0x1090>
9fc053ec:	27180004 	addiu	t8,t8,4
9fc053f0:	26d60001 	addiu	s6,s6,1
9fc053f4:	8fb1001c 	lw	s1,28(sp)
9fc053f8:	02d4c82b 	sltu	t9,s6,s4
9fc053fc:	02729821 	addu	s3,s3,s2
9fc05400:	1720ff78 	bnez	t9,9fc051e4 <core_bench_matrix+0x1084>
9fc05404:	02b1a821 	addu	s5,s5,s1
9fc05408:	00003021 	move	a2,zero
9fc0540c:	00001821 	move	v1,zero
9fc05410:	00002021 	move	a0,zero
9fc05414:	00005821 	move	t3,zero
9fc05418:	2687ffff 	addiu	a3,s4,-1
9fc0541c:	30ea0003 	andi	t2,a3,0x3
9fc05420:	8fa80020 	lw	t0,32(sp)
9fc05424:	11400040 	beqz	t2,9fc05528 <core_bench_matrix+0x13c8>
9fc05428:	00004821 	move	t1,zero
9fc0542c:	8d070000 	lw	a3,0(t0)
9fc05430:	3093ffff 	andi	s3,a0,0xffff
9fc05434:	0067802a 	slt	s0,v1,a3
9fc05438:	00c73021 	addu	a2,a2,a3
9fc0543c:	02707021 	addu	t6,s3,s0
9fc05440:	266c000a 	addiu	t4,s3,10
9fc05444:	000c7c00 	sll	t7,t4,0x10
9fc05448:	000e6c00 	sll	t5,t6,0x10
9fc0544c:	03c6282a 	slt	a1,s8,a2
9fc05450:	000f1403 	sra	v0,t7,0x10
9fc05454:	10a00002 	beqz	a1,9fc05460 <core_bench_matrix+0x1300>
9fc05458:	000d2403 	sra	a0,t5,0x10
9fc0545c:	00402021 	move	a0,v0
9fc05460:	10a00002 	beqz	a1,9fc0546c <core_bench_matrix+0x130c>
9fc05464:	24090001 	li	t1,1
9fc05468:	00003021 	move	a2,zero
9fc0546c:	8fa30020 	lw	v1,32(sp)
9fc05470:	0134282b 	sltu	a1,t1,s4
9fc05474:	10a0007b 	beqz	a1,9fc05664 <core_bench_matrix+0x1504>
9fc05478:	24680004 	addiu	t0,v1,4
9fc0547c:	1149002a 	beq	t2,t1,9fc05528 <core_bench_matrix+0x13c8>
9fc05480:	00e01821 	move	v1,a3
9fc05484:	24020002 	li	v0,2
9fc05488:	11420014 	beq	t2,v0,9fc054dc <core_bench_matrix+0x137c>
9fc0548c:	3095ffff 	andi	s5,a0,0xffff
9fc05490:	8d050000 	lw	a1,0(t0)
9fc05494:	00000000 	nop
9fc05498:	00e5b02a 	slt	s6,a3,a1
9fc0549c:	02b62021 	addu	a0,s5,s6
9fc054a0:	00c55021 	addu	t2,a2,a1
9fc054a4:	26b1000a 	addiu	s1,s5,10
9fc054a8:	0011cc00 	sll	t9,s1,0x10
9fc054ac:	00043c00 	sll	a3,a0,0x10
9fc054b0:	03ca302a 	slt	a2,s8,t2
9fc054b4:	00191403 	sra	v0,t9,0x10
9fc054b8:	10c00002 	beqz	a2,9fc054c4 <core_bench_matrix+0x1364>
9fc054bc:	00072403 	sra	a0,a3,0x10
9fc054c0:	00402021 	move	a0,v0
9fc054c4:	14c00002 	bnez	a2,9fc054d0 <core_bench_matrix+0x1370>
9fc054c8:	00003021 	move	a2,zero
9fc054cc:	01403021 	move	a2,t2
9fc054d0:	25290001 	addiu	t1,t1,1
9fc054d4:	25080004 	addiu	t0,t0,4
9fc054d8:	00a01821 	move	v1,a1
9fc054dc:	8d050000 	lw	a1,0(t0)
9fc054e0:	308fffff 	andi	t7,a0,0xffff
9fc054e4:	00c53821 	addu	a3,a2,a1
9fc054e8:	0065302a 	slt	a2,v1,a1
9fc054ec:	01e6c021 	addu	t8,t7,a2
9fc054f0:	25ee000a 	addiu	t6,t7,10
9fc054f4:	000e6c00 	sll	t5,t6,0x10
9fc054f8:	00185400 	sll	t2,t8,0x10
9fc054fc:	03c7302a 	slt	a2,s8,a3
9fc05500:	000d1403 	sra	v0,t5,0x10
9fc05504:	10c00002 	beqz	a2,9fc05510 <core_bench_matrix+0x13b0>
9fc05508:	000a2403 	sra	a0,t2,0x10
9fc0550c:	00402021 	move	a0,v0
9fc05510:	14c00002 	bnez	a2,9fc0551c <core_bench_matrix+0x13bc>
9fc05514:	00003021 	move	a2,zero
9fc05518:	00e03021 	move	a2,a3
9fc0551c:	25290001 	addiu	t1,t1,1
9fc05520:	25080004 	addiu	t0,t0,4
9fc05524:	00a01821 	move	v1,a1
9fc05528:	8d070000 	lw	a3,0(t0)
9fc0552c:	3085ffff 	andi	a1,a0,0xffff
9fc05530:	0067702a 	slt	t6,v1,a3
9fc05534:	00ae5021 	addu	t2,a1,t6
9fc05538:	24bf000a 	addiu	ra,a1,10
9fc0553c:	00c73021 	addu	a2,a2,a3
9fc05540:	001f1400 	sll	v0,ra,0x10
9fc05544:	000a2400 	sll	a0,t2,0x10
9fc05548:	03c6282a 	slt	a1,s8,a2
9fc0554c:	00021403 	sra	v0,v0,0x10
9fc05550:	10a00002 	beqz	a1,9fc0555c <core_bench_matrix+0x13fc>
9fc05554:	00042403 	sra	a0,a0,0x10
9fc05558:	00402021 	move	a0,v0
9fc0555c:	10a00002 	beqz	a1,9fc05568 <core_bench_matrix+0x1408>
9fc05560:	00000000 	nop
9fc05564:	00003021 	move	a2,zero
9fc05568:	25290001 	addiu	t1,t1,1
9fc0556c:	0134602b 	sltu	t4,t1,s4
9fc05570:	1180003b 	beqz	t4,9fc05660 <core_bench_matrix+0x1500>
9fc05574:	250a0004 	addiu	t2,t0,4
9fc05578:	8d050004 	lw	a1,4(t0)
9fc0557c:	3090ffff 	andi	s0,a0,0xffff
9fc05580:	00e5682a 	slt	t5,a3,a1
9fc05584:	00c54021 	addu	t0,a2,a1
9fc05588:	020db021 	addu	s6,s0,t5
9fc0558c:	2613000a 	addiu	s3,s0,10
9fc05590:	0013c400 	sll	t8,s3,0x10
9fc05594:	0016ac00 	sll	s5,s6,0x10
9fc05598:	03c8302a 	slt	a2,s8,t0
9fc0559c:	00182403 	sra	a0,t8,0x10
9fc055a0:	10c00002 	beqz	a2,9fc055ac <core_bench_matrix+0x144c>
9fc055a4:	00151403 	sra	v0,s5,0x10
9fc055a8:	00801021 	move	v0,a0
9fc055ac:	14c00002 	bnez	a2,9fc055b8 <core_bench_matrix+0x1458>
9fc055b0:	00003821 	move	a3,zero
9fc055b4:	01003821 	move	a3,t0
9fc055b8:	8d460004 	lw	a2,4(t2)
9fc055bc:	305fffff 	andi	ra,v0,0xffff
9fc055c0:	00a6182a 	slt	v1,a1,a2
9fc055c4:	00e63821 	addu	a3,a3,a2
9fc055c8:	03e36021 	addu	t4,ra,v1
9fc055cc:	27f0000a 	addiu	s0,ra,10
9fc055d0:	00109c00 	sll	s3,s0,0x10
9fc055d4:	000c4400 	sll	t0,t4,0x10
9fc055d8:	03c7282a 	slt	a1,s8,a3
9fc055dc:	00132403 	sra	a0,s3,0x10
9fc055e0:	10a00002 	beqz	a1,9fc055ec <core_bench_matrix+0x148c>
9fc055e4:	00081403 	sra	v0,t0,0x10
9fc055e8:	00801021 	move	v0,a0
9fc055ec:	10a00002 	beqz	a1,9fc055f8 <core_bench_matrix+0x1498>
9fc055f0:	00000000 	nop
9fc055f4:	00003821 	move	a3,zero
9fc055f8:	8d450008 	lw	a1,8(t2)
9fc055fc:	3055ffff 	andi	s5,v0,0xffff
9fc05600:	00c5b02a 	slt	s6,a2,a1
9fc05604:	00e53821 	addu	a3,a3,a1
9fc05608:	02b6c821 	addu	t9,s5,s6
9fc0560c:	26b1000a 	addiu	s1,s5,10
9fc05610:	00111400 	sll	v0,s1,0x10
9fc05614:	00192400 	sll	a0,t9,0x10
9fc05618:	03c7302a 	slt	a2,s8,a3
9fc0561c:	00021403 	sra	v0,v0,0x10
9fc05620:	10c00002 	beqz	a2,9fc0562c <core_bench_matrix+0x14cc>
9fc05624:	00042403 	sra	a0,a0,0x10
9fc05628:	00402021 	move	a0,v0
9fc0562c:	14c00008 	bnez	a2,9fc05650 <core_bench_matrix+0x14f0>
9fc05630:	00000000 	nop
9fc05634:	00e03021 	move	a2,a3
9fc05638:	25290003 	addiu	t1,t1,3
9fc0563c:	2548000c 	addiu	t0,t2,12
9fc05640:	0bf0154a 	j	9fc05528 <core_bench_matrix+0x13c8>
9fc05644:	00a01821 	move	v1,a1
	...
9fc05650:	0bf0158e 	j	9fc05638 <core_bench_matrix+0x14d8>
9fc05654:	00003021 	move	a2,zero
	...
9fc05660:	8fa30020 	lw	v1,32(sp)
9fc05664:	8faf001c 	lw	t7,28(sp)
9fc05668:	256b0001 	addiu	t3,t3,1
9fc0566c:	006f4821 	addu	t1,v1,t7
9fc05670:	0174402b 	sltu	t0,t3,s4
9fc05674:	11000003 	beqz	t0,9fc05684 <core_bench_matrix+0x1524>
9fc05678:	afa90020 	sw	t1,32(sp)
9fc0567c:	0bf01506 	j	9fc05418 <core_bench_matrix+0x12b8>
9fc05680:	00e01821 	move	v1,a3
9fc05684:	0ff01d20 	jal	9fc07480 <crc16>
9fc05688:	02e02821 	move	a1,s7
9fc0568c:	8fb70034 	lw	s7,52(sp)
9fc05690:	00407821 	move	t7,v0
9fc05694:	0017f023 	negu	s8,s7
9fc05698:	33cdffff 	andi	t5,s8,0xffff
9fc0569c:	00007021 	move	t6,zero
9fc056a0:	8fb90010 	lw	t9,16(sp)
9fc056a4:	240c0001 	li	t4,1
9fc056a8:	97360000 	lhu	s6,0(t9)
9fc056ac:	2691ffff 	addiu	s1,s4,-1
9fc056b0:	01b6a821 	addu	s5,t5,s6
9fc056b4:	0194b82b 	sltu	s7,t4,s4
9fc056b8:	a7350000 	sh	s5,0(t9)
9fc056bc:	32230007 	andi	v1,s1,0x7
9fc056c0:	12e00051 	beqz	s7,9fc05808 <core_bench_matrix+0x16a8>
9fc056c4:	272b0002 	addiu	t3,t9,2
9fc056c8:	10600033 	beqz	v1,9fc05798 <core_bench_matrix+0x1638>
9fc056cc:	00000000 	nop
9fc056d0:	106c002a 	beq	v1,t4,9fc0577c <core_bench_matrix+0x161c>
9fc056d4:	24060002 	li	a2,2
9fc056d8:	10660023 	beq	v1,a2,9fc05768 <core_bench_matrix+0x1608>
9fc056dc:	24180003 	li	t8,3
9fc056e0:	1078001c 	beq	v1,t8,9fc05754 <core_bench_matrix+0x15f4>
9fc056e4:	24130004 	li	s3,4
9fc056e8:	10730015 	beq	v1,s3,9fc05740 <core_bench_matrix+0x15e0>
9fc056ec:	24100005 	li	s0,5
9fc056f0:	1070000e 	beq	v1,s0,9fc0572c <core_bench_matrix+0x15cc>
9fc056f4:	240a0006 	li	t2,6
9fc056f8:	106a0007 	beq	v1,t2,9fc05718 <core_bench_matrix+0x15b8>
9fc056fc:	00000000 	nop
9fc05700:	95690000 	lhu	t1,0(t3)
9fc05704:	8fbf0010 	lw	ra,16(sp)
9fc05708:	01a94021 	addu	t0,t5,t1
9fc0570c:	a5680000 	sh	t0,0(t3)
9fc05710:	240c0002 	li	t4,2
9fc05714:	27eb0004 	addiu	t3,ra,4
9fc05718:	95650000 	lhu	a1,0(t3)
9fc0571c:	258c0001 	addiu	t4,t4,1
9fc05720:	01a53821 	addu	a3,t5,a1
9fc05724:	a5670000 	sh	a3,0(t3)
9fc05728:	256b0002 	addiu	t3,t3,2
9fc0572c:	95630000 	lhu	v1,0(t3)
9fc05730:	258c0001 	addiu	t4,t4,1
9fc05734:	01a32021 	addu	a0,t5,v1
9fc05738:	a5640000 	sh	a0,0(t3)
9fc0573c:	256b0002 	addiu	t3,t3,2
9fc05740:	957e0000 	lhu	s8,0(t3)
9fc05744:	258c0001 	addiu	t4,t4,1
9fc05748:	01be1021 	addu	v0,t5,s8
9fc0574c:	a5620000 	sh	v0,0(t3)
9fc05750:	256b0002 	addiu	t3,t3,2
9fc05754:	95790000 	lhu	t9,0(t3)
9fc05758:	258c0001 	addiu	t4,t4,1
9fc0575c:	01b9b821 	addu	s7,t5,t9
9fc05760:	a5770000 	sh	s7,0(t3)
9fc05764:	256b0002 	addiu	t3,t3,2
9fc05768:	95750000 	lhu	s5,0(t3)
9fc0576c:	258c0001 	addiu	t4,t4,1
9fc05770:	01b58821 	addu	s1,t5,s5
9fc05774:	a5710000 	sh	s1,0(t3)
9fc05778:	256b0002 	addiu	t3,t3,2
9fc0577c:	95780000 	lhu	t8,0(t3)
9fc05780:	258c0001 	addiu	t4,t4,1
9fc05784:	01b83021 	addu	a2,t5,t8
9fc05788:	0194b02b 	sltu	s6,t4,s4
9fc0578c:	a5660000 	sh	a2,0(t3)
9fc05790:	12c0001d 	beqz	s6,9fc05808 <core_bench_matrix+0x16a8>
9fc05794:	256b0002 	addiu	t3,t3,2
9fc05798:	95620000 	lhu	v0,0(t3)
9fc0579c:	95630002 	lhu	v1,2(t3)
9fc057a0:	95640004 	lhu	a0,4(t3)
9fc057a4:	95650006 	lhu	a1,6(t3)
9fc057a8:	95670008 	lhu	a3,8(t3)
9fc057ac:	9569000a 	lhu	t1,10(t3)
9fc057b0:	9568000c 	lhu	t0,12(t3)
9fc057b4:	957f000e 	lhu	ra,14(t3)
9fc057b8:	258c0008 	addiu	t4,t4,8
9fc057bc:	01a25021 	addu	t2,t5,v0
9fc057c0:	01a38021 	addu	s0,t5,v1
9fc057c4:	01a49821 	addu	s3,t5,a0
9fc057c8:	01a5c021 	addu	t8,t5,a1
9fc057cc:	01a73021 	addu	a2,t5,a3
9fc057d0:	01a9b021 	addu	s6,t5,t1
9fc057d4:	01a8a821 	addu	s5,t5,t0
9fc057d8:	01bf8821 	addu	s1,t5,ra
9fc057dc:	0194c82b 	sltu	t9,t4,s4
9fc057e0:	a56a0000 	sh	t2,0(t3)
9fc057e4:	a5700002 	sh	s0,2(t3)
9fc057e8:	a5730004 	sh	s3,4(t3)
9fc057ec:	a5780006 	sh	t8,6(t3)
9fc057f0:	a5660008 	sh	a2,8(t3)
9fc057f4:	a576000a 	sh	s6,10(t3)
9fc057f8:	a575000c 	sh	s5,12(t3)
9fc057fc:	a571000e 	sh	s1,14(t3)
9fc05800:	1720ffe5 	bnez	t9,9fc05798 <core_bench_matrix+0x1638>
9fc05804:	256b0010 	addiu	t3,t3,16
9fc05808:	8fbe0010 	lw	s8,16(sp)
9fc0580c:	25ce0001 	addiu	t6,t6,1
9fc05810:	03d26021 	addu	t4,s8,s2
9fc05814:	01d4582b 	sltu	t3,t6,s4
9fc05818:	1560ffa1 	bnez	t3,9fc056a0 <core_bench_matrix+0x1540>
9fc0581c:	afac0010 	sw	t4,16(sp)
9fc05820:	000f9400 	sll	s2,t7,0x10
9fc05824:	8fa50038 	lw	a1,56(sp)
9fc05828:	00122403 	sra	a0,s2,0x10
9fc0582c:	8fbf0064 	lw	ra,100(sp)
9fc05830:	8fbe0060 	lw	s8,96(sp)
9fc05834:	8fb7005c 	lw	s7,92(sp)
9fc05838:	8fb60058 	lw	s6,88(sp)
9fc0583c:	8fb50054 	lw	s5,84(sp)
9fc05840:	8fb40050 	lw	s4,80(sp)
9fc05844:	8fb3004c 	lw	s3,76(sp)
9fc05848:	8fb20048 	lw	s2,72(sp)
9fc0584c:	8fb10044 	lw	s1,68(sp)
9fc05850:	8fb00040 	lw	s0,64(sp)
9fc05854:	0bf01d20 	j	9fc07480 <crc16>
9fc05858:	27bd0068 	addiu	sp,sp,104
9fc0585c:	00002021 	move	a0,zero
9fc05860:	0ff01d20 	jal	9fc07480 <crc16>
9fc05864:	00002821 	move	a1,zero
9fc05868:	00402821 	move	a1,v0
9fc0586c:	0ff01d20 	jal	9fc07480 <crc16>
9fc05870:	00002021 	move	a0,zero
9fc05874:	00402821 	move	a1,v0
9fc05878:	0ff01d20 	jal	9fc07480 <crc16>
9fc0587c:	00002021 	move	a0,zero
9fc05880:	00402821 	move	a1,v0
9fc05884:	0ff01d20 	jal	9fc07480 <crc16>
9fc05888:	00002021 	move	a0,zero
9fc0588c:	0bf01608 	j	9fc05820 <core_bench_matrix+0x16c0>
9fc05890:	00407821 	move	t7,v0
	...

9fc058a0 <portable_malloc>:
portable_malloc():
9fc058a0:	03e00008 	jr	ra
9fc058a4:	00001021 	move	v0,zero
	...

9fc058b0 <portable_free>:
portable_free():
9fc058b0:	03e00008 	jr	ra
9fc058b4:	00000000 	nop
	...

9fc058c0 <get_time>:
get_time():
9fc058c0:	3c0c9fc1 	lui	t4,0x9fc1
9fc058c4:	3c0b9fc1 	lui	t3,0x9fc1
9fc058c8:	2582acc0 	addiu	v0,t4,-21312
9fc058cc:	2565acb0 	addiu	a1,t3,-21328
9fc058d0:	8c580004 	lw	t8,4(v0)
9fc058d4:	8cb90004 	lw	t9,4(a1)
9fc058d8:	3c0f000f 	lui	t7,0xf
9fc058dc:	03197023 	subu	t6,t8,t9
9fc058e0:	35ed4240 	ori	t5,t7,0x4240
9fc058e4:	15a00002 	bnez	t5,9fc058f0 <get_time+0x30>
9fc058e8:	01cd001b 	divu	zero,t6,t5
9fc058ec:	0007000d 	break	0x7
9fc058f0:	8d89acc0 	lw	t1,-21312(t4)
9fc058f4:	8d6aacb0 	lw	t2,-21328(t3)
9fc058f8:	00000000 	nop
9fc058fc:	012a1823 	subu	v1,t1,t2
9fc05900:	00034080 	sll	t0,v1,0x2
9fc05904:	000339c0 	sll	a3,v1,0x7
9fc05908:	00e83023 	subu	a2,a3,t0
9fc0590c:	00c32021 	addu	a0,a2,v1
9fc05910:	000410c0 	sll	v0,a0,0x3
9fc05914:	00002812 	mflo	a1
9fc05918:	03e00008 	jr	ra
9fc0591c:	00a21021 	addu	v0,a1,v0

9fc05920 <time_in_secs>:
time_in_secs():
9fc05920:	240203e8 	li	v0,1000
9fc05924:	14400002 	bnez	v0,9fc05930 <time_in_secs+0x10>
9fc05928:	0082001b 	divu	zero,a0,v0
9fc0592c:	0007000d 	break	0x7
9fc05930:	00001012 	mflo	v0
9fc05934:	03e00008 	jr	ra
9fc05938:	00000000 	nop
9fc0593c:	00000000 	nop

9fc05940 <portable_init>:
portable_init():
9fc05940:	24020001 	li	v0,1
9fc05944:	03e00008 	jr	ra
9fc05948:	a0820000 	sb	v0,0(a0)
9fc0594c:	00000000 	nop

9fc05950 <portable_fini>:
portable_fini():
9fc05950:	03e00008 	jr	ra
9fc05954:	a0800000 	sb	zero,0(a0)
	...

9fc05960 <stop_time>:
stop_time():
9fc05960:	3c059fc1 	lui	a1,0x9fc1
9fc05964:	24a5acc0 	addiu	a1,a1,-21312
9fc05968:	0bf028b7 	j	9fc0a2dc <clock_gettime>
9fc0596c:	00002021 	move	a0,zero

9fc05970 <start_time>:
start_time():
9fc05970:	3c059fc1 	lui	a1,0x9fc1
9fc05974:	24a5acb0 	addiu	a1,a1,-21328
9fc05978:	0bf028b7 	j	9fc0a2dc <clock_gettime>
9fc0597c:	00002021 	move	a0,zero

9fc05980 <core_init_state>:
core_init_state():
9fc05980:	27bdffe0 	addiu	sp,sp,-32
9fc05984:	00c0c821 	move	t9,a2
9fc05988:	3c069fc1 	lui	a2,0x9fc1
9fc0598c:	afb40010 	sw	s4,16(sp)
9fc05990:	00006021 	move	t4,zero
9fc05994:	24d4aa40 	addiu	s4,a2,-21952
9fc05998:	00003021 	move	a2,zero
9fc0599c:	afb20008 	sw	s2,8(sp)
9fc059a0:	00ccc021 	addu	t8,a2,t4
9fc059a4:	00809021 	move	s2,a0
9fc059a8:	afb00000 	sw	s0,0(sp)
9fc059ac:	270f0001 	addiu	t7,t8,1
9fc059b0:	2650ffff 	addiu	s0,s2,-1
9fc059b4:	00052c00 	sll	a1,a1,0x10
9fc059b8:	3c079fc1 	lui	a3,0x9fc1
9fc059bc:	3c049fc1 	lui	a0,0x9fc1
9fc059c0:	3c039fc1 	lui	v1,0x9fc1
9fc059c4:	3c029fc1 	lui	v0,0x9fc1
9fc059c8:	01f0502b 	sltu	t2,t7,s0
9fc059cc:	afb7001c 	sw	s7,28(sp)
9fc059d0:	afb60018 	sw	s6,24(sp)
9fc059d4:	afb50014 	sw	s5,20(sp)
9fc059d8:	afb3000c 	sw	s3,12(sp)
9fc059dc:	afb10004 	sw	s1,4(sp)
9fc059e0:	00052c03 	sra	a1,a1,0x10
9fc059e4:	24f1a990 	addiu	s1,a3,-22128
9fc059e8:	2495aa30 	addiu	s5,a0,-21968
9fc059ec:	2476aa20 	addiu	s6,v1,-21984
9fc059f0:	2457aa10 	addiu	s7,v0,-22000
9fc059f4:	00005821 	move	t3,zero
9fc059f8:	1140001c 	beqz	t2,9fc05a6c <core_init_state+0xec>
9fc059fc:	2413002c 	li	s3,44
9fc05a00:	1580007b 	bnez	t4,9fc05bf0 <core_init_state+0x270>
9fc05a04:	2d880004 	sltiu	t0,t4,4
9fc05a08:	00c07821 	move	t7,a2
9fc05a0c:	24aa0001 	addiu	t2,a1,1
9fc05a10:	000a2c00 	sll	a1,t2,0x10
9fc05a14:	00052c03 	sra	a1,a1,0x10
9fc05a18:	30a80007 	andi	t0,a1,0x7
9fc05a1c:	00085880 	sll	t3,t0,0x2
9fc05a20:	022b3821 	addu	a3,s1,t3
9fc05a24:	8cf80000 	lw	t8,0(a3)
9fc05a28:	00000000 	nop
9fc05a2c:	03000008 	jr	t8
9fc05a30:	00000000 	nop
	...
9fc05a40:	00054042 	srl	t0,a1,0x1
9fc05a44:	310b000c 	andi	t3,t0,0xc
9fc05a48:	01743821 	addu	a3,t3,s4
9fc05a4c:	8ceb0000 	lw	t3,0(a3)
9fc05a50:	240c0008 	li	t4,8
9fc05a54:	01e03021 	move	a2,t7
9fc05a58:	00ccc021 	addu	t8,a2,t4
9fc05a5c:	270f0001 	addiu	t7,t8,1
9fc05a60:	01f0502b 	sltu	t2,t7,s0
9fc05a64:	1540ffe6 	bnez	t2,9fc05a00 <core_init_state+0x80>
9fc05a68:	00000000 	nop
9fc05a6c:	00d2782b 	sltu	t7,a2,s2
9fc05a70:	11e0003a 	beqz	t7,9fc05b5c <core_init_state+0x1dc>
9fc05a74:	00069827 	nor	s3,zero,a2
9fc05a78:	24c50001 	addiu	a1,a2,1
9fc05a7c:	02728821 	addu	s1,s3,s2
9fc05a80:	03263021 	addu	a2,t9,a2
9fc05a84:	00b2802b 	sltu	s0,a1,s2
9fc05a88:	32270007 	andi	a3,s1,0x7
9fc05a8c:	a0c00000 	sb	zero,0(a2)
9fc05a90:	12000032 	beqz	s0,9fc05b5c <core_init_state+0x1dc>
9fc05a94:	24c40001 	addiu	a0,a2,1
9fc05a98:	10e00024 	beqz	a3,9fc05b2c <core_init_state+0x1ac>
9fc05a9c:	24150001 	li	s5,1
9fc05aa0:	10f5001d 	beq	a3,s5,9fc05b18 <core_init_state+0x198>
9fc05aa4:	24160002 	li	s6,2
9fc05aa8:	10f60018 	beq	a3,s6,9fc05b0c <core_init_state+0x18c>
9fc05aac:	24170003 	li	s7,3
9fc05ab0:	10f70013 	beq	a3,s7,9fc05b00 <core_init_state+0x180>
9fc05ab4:	24190004 	li	t9,4
9fc05ab8:	10f9000e 	beq	a3,t9,9fc05af4 <core_init_state+0x174>
9fc05abc:	24020005 	li	v0,5
9fc05ac0:	10e20009 	beq	a3,v0,9fc05ae8 <core_init_state+0x168>
9fc05ac4:	240e0006 	li	t6,6
9fc05ac8:	10ee0004 	beq	a3,t6,9fc05adc <core_init_state+0x15c>
9fc05acc:	00000000 	nop
9fc05ad0:	a0c00001 	sb	zero,1(a2)
9fc05ad4:	24a50001 	addiu	a1,a1,1
9fc05ad8:	24840001 	addiu	a0,a0,1
9fc05adc:	a0800000 	sb	zero,0(a0)
9fc05ae0:	24a50001 	addiu	a1,a1,1
9fc05ae4:	24840001 	addiu	a0,a0,1
9fc05ae8:	a0800000 	sb	zero,0(a0)
9fc05aec:	24a50001 	addiu	a1,a1,1
9fc05af0:	24840001 	addiu	a0,a0,1
9fc05af4:	a0800000 	sb	zero,0(a0)
9fc05af8:	24a50001 	addiu	a1,a1,1
9fc05afc:	24840001 	addiu	a0,a0,1
9fc05b00:	a0800000 	sb	zero,0(a0)
9fc05b04:	24a50001 	addiu	a1,a1,1
9fc05b08:	24840001 	addiu	a0,a0,1
9fc05b0c:	a0800000 	sb	zero,0(a0)
9fc05b10:	24a50001 	addiu	a1,a1,1
9fc05b14:	24840001 	addiu	a0,a0,1
9fc05b18:	24a50001 	addiu	a1,a1,1
9fc05b1c:	00b2302b 	sltu	a2,a1,s2
9fc05b20:	a0800000 	sb	zero,0(a0)
9fc05b24:	10c0000d 	beqz	a2,9fc05b5c <core_init_state+0x1dc>
9fc05b28:	24840001 	addiu	a0,a0,1
9fc05b2c:	24a50008 	addiu	a1,a1,8
9fc05b30:	00b2a02b 	sltu	s4,a1,s2
9fc05b34:	a0800000 	sb	zero,0(a0)
9fc05b38:	a0800001 	sb	zero,1(a0)
9fc05b3c:	a0800002 	sb	zero,2(a0)
9fc05b40:	a0800003 	sb	zero,3(a0)
9fc05b44:	a0800004 	sb	zero,4(a0)
9fc05b48:	a0800005 	sb	zero,5(a0)
9fc05b4c:	a0800006 	sb	zero,6(a0)
9fc05b50:	a0800007 	sb	zero,7(a0)
9fc05b54:	1680fff5 	bnez	s4,9fc05b2c <core_init_state+0x1ac>
9fc05b58:	24840008 	addiu	a0,a0,8
9fc05b5c:	8fb7001c 	lw	s7,28(sp)
9fc05b60:	8fb60018 	lw	s6,24(sp)
9fc05b64:	8fb50014 	lw	s5,20(sp)
9fc05b68:	8fb40010 	lw	s4,16(sp)
9fc05b6c:	8fb3000c 	lw	s3,12(sp)
9fc05b70:	8fb20008 	lw	s2,8(sp)
9fc05b74:	8fb10004 	lw	s1,4(sp)
9fc05b78:	8fb00000 	lw	s0,0(sp)
9fc05b7c:	03e00008 	jr	ra
9fc05b80:	27bd0020 	addiu	sp,sp,32
	...
9fc05b90:	0005c042 	srl	t8,a1,0x1
9fc05b94:	330c000c 	andi	t4,t8,0xc
9fc05b98:	01952021 	addu	a0,t4,s5
9fc05b9c:	8c8b0000 	lw	t3,0(a0)
9fc05ba0:	240c0008 	li	t4,8
9fc05ba4:	0bf01696 	j	9fc05a58 <core_init_state+0xd8>
9fc05ba8:	01e03021 	move	a2,t7
9fc05bac:	00000000 	nop
9fc05bb0:	00051842 	srl	v1,a1,0x1
9fc05bb4:	306d000c 	andi	t5,v1,0xc
9fc05bb8:	01b64821 	addu	t1,t5,s6
9fc05bbc:	8d2b0000 	lw	t3,0(t1)
9fc05bc0:	240c0008 	li	t4,8
9fc05bc4:	0bf01696 	j	9fc05a58 <core_init_state+0xd8>
9fc05bc8:	01e03021 	move	a2,t7
9fc05bcc:	00000000 	nop
9fc05bd0:	00057042 	srl	t6,a1,0x1
9fc05bd4:	31c6000c 	andi	a2,t6,0xc
9fc05bd8:	00d71021 	addu	v0,a2,s7
9fc05bdc:	8c4b0000 	lw	t3,0(v0)
9fc05be0:	240c0004 	li	t4,4
9fc05be4:	0bf01696 	j	9fc05a58 <core_init_state+0xd8>
9fc05be8:	01e03021 	move	a2,t7
9fc05bec:	00000000 	nop
9fc05bf0:	1100005b 	beqz	t0,9fc05d60 <core_init_state+0x3e0>
9fc05bf4:	03265021 	addu	t2,t9,a2
9fc05bf8:	916d0000 	lbu	t5,0(t3)
9fc05bfc:	24070001 	li	a3,1
9fc05c00:	2588ffff 	addiu	t0,t4,-1
9fc05c04:	00ec482b 	sltu	t1,a3,t4
9fc05c08:	31030007 	andi	v1,t0,0x7
9fc05c0c:	a14d0000 	sb	t5,0(t2)
9fc05c10:	1120004e 	beqz	t1,9fc05d4c <core_init_state+0x3cc>
9fc05c14:	25480001 	addiu	t0,t2,1
9fc05c18:	10600032 	beqz	v1,9fc05ce4 <core_init_state+0x364>
9fc05c1c:	01672021 	addu	a0,t3,a3
9fc05c20:	10670029 	beq	v1,a3,9fc05cc8 <core_init_state+0x348>
9fc05c24:	24090002 	li	t1,2
9fc05c28:	10690021 	beq	v1,t1,9fc05cb0 <core_init_state+0x330>
9fc05c2c:	240d0003 	li	t5,3
9fc05c30:	106d001a 	beq	v1,t5,9fc05c9c <core_init_state+0x31c>
9fc05c34:	24040004 	li	a0,4
9fc05c38:	10640013 	beq	v1,a0,9fc05c88 <core_init_state+0x308>
9fc05c3c:	24020005 	li	v0,5
9fc05c40:	1062000c 	beq	v1,v0,9fc05c74 <core_init_state+0x2f4>
9fc05c44:	240e0006 	li	t6,6
9fc05c48:	106e0006 	beq	v1,t6,9fc05c64 <core_init_state+0x2e4>
9fc05c4c:	01673021 	addu	a2,t3,a3
9fc05c50:	91630001 	lbu	v1,1(t3)
9fc05c54:	25480002 	addiu	t0,t2,2
9fc05c58:	a1430001 	sb	v1,1(t2)
9fc05c5c:	24070002 	li	a3,2
9fc05c60:	01673021 	addu	a2,t3,a3
9fc05c64:	90ca0000 	lbu	t2,0(a2)
9fc05c68:	24e70001 	addiu	a3,a3,1
9fc05c6c:	a10a0000 	sb	t2,0(t0)
9fc05c70:	25080001 	addiu	t0,t0,1
9fc05c74:	01676821 	addu	t5,t3,a3
9fc05c78:	91a90000 	lbu	t1,0(t5)
9fc05c7c:	24e70001 	addiu	a3,a3,1
9fc05c80:	a1090000 	sb	t1,0(t0)
9fc05c84:	25080001 	addiu	t0,t0,1
9fc05c88:	01671021 	addu	v0,t3,a3
9fc05c8c:	90440000 	lbu	a0,0(v0)
9fc05c90:	24e70001 	addiu	a3,a3,1
9fc05c94:	a1040000 	sb	a0,0(t0)
9fc05c98:	25080001 	addiu	t0,t0,1
9fc05c9c:	01671821 	addu	v1,t3,a3
9fc05ca0:	906e0000 	lbu	t6,0(v1)
9fc05ca4:	24e70001 	addiu	a3,a3,1
9fc05ca8:	a10e0000 	sb	t6,0(t0)
9fc05cac:	25080001 	addiu	t0,t0,1
9fc05cb0:	01673021 	addu	a2,t3,a3
9fc05cb4:	90ca0000 	lbu	t2,0(a2)
9fc05cb8:	24e70001 	addiu	a3,a3,1
9fc05cbc:	a10a0000 	sb	t2,0(t0)
9fc05cc0:	25080001 	addiu	t0,t0,1
9fc05cc4:	01672021 	addu	a0,t3,a3
9fc05cc8:	908d0000 	lbu	t5,0(a0)
9fc05ccc:	24e70001 	addiu	a3,a3,1
9fc05cd0:	00ec482b 	sltu	t1,a3,t4
9fc05cd4:	a10d0000 	sb	t5,0(t0)
9fc05cd8:	1120001c 	beqz	t1,9fc05d4c <core_init_state+0x3cc>
9fc05cdc:	25080001 	addiu	t0,t0,1
9fc05ce0:	01672021 	addu	a0,t3,a3
9fc05ce4:	90830000 	lbu	v1,0(a0)
9fc05ce8:	24e70008 	addiu	a3,a3,8
9fc05cec:	a1030000 	sb	v1,0(t0)
9fc05cf0:	90890001 	lbu	t1,1(a0)
9fc05cf4:	00000000 	nop
9fc05cf8:	a1090001 	sb	t1,1(t0)
9fc05cfc:	90860002 	lbu	a2,2(a0)
9fc05d00:	00000000 	nop
9fc05d04:	a1060002 	sb	a2,2(t0)
9fc05d08:	908a0003 	lbu	t2,3(a0)
9fc05d0c:	00000000 	nop
9fc05d10:	a10a0003 	sb	t2,3(t0)
9fc05d14:	908d0004 	lbu	t5,4(a0)
9fc05d18:	00000000 	nop
9fc05d1c:	a10d0004 	sb	t5,4(t0)
9fc05d20:	90890005 	lbu	t1,5(a0)
9fc05d24:	00000000 	nop
9fc05d28:	a1090005 	sb	t1,5(t0)
9fc05d2c:	90860006 	lbu	a2,6(a0)
9fc05d30:	00000000 	nop
9fc05d34:	a1060006 	sb	a2,6(t0)
9fc05d38:	90820007 	lbu	v0,7(a0)
9fc05d3c:	00ec202b 	sltu	a0,a3,t4
9fc05d40:	a1020007 	sb	v0,7(t0)
9fc05d44:	1480ffe6 	bnez	a0,9fc05ce0 <core_init_state+0x360>
9fc05d48:	25080008 	addiu	t0,t0,8
9fc05d4c:	03386021 	addu	t4,t9,t8
9fc05d50:	0bf01683 	j	9fc05a0c <core_init_state+0x8c>
9fc05d54:	a1930000 	sb	s3,0(t4)
	...
9fc05d60:	014b6825 	or	t5,t2,t3
9fc05d64:	31a90003 	andi	t1,t5,0x3
9fc05d68:	1520ffa3 	bnez	t1,9fc05bf8 <core_init_state+0x278>
9fc05d6c:	25620004 	addiu	v0,t3,4
9fc05d70:	004a702b 	sltu	t6,v0,t2
9fc05d74:	15c00005 	bnez	t6,9fc05d8c <core_init_state+0x40c>
9fc05d78:	000c7082 	srl	t6,t4,0x2
9fc05d7c:	25440004 	addiu	a0,t2,4
9fc05d80:	008b182b 	sltu	v1,a0,t3
9fc05d84:	1060ff9c 	beqz	v1,9fc05bf8 <core_init_state+0x278>
9fc05d88:	00000000 	nop
9fc05d8c:	000e6880 	sll	t5,t6,0x2
9fc05d90:	11a00058 	beqz	t5,9fc05ef4 <core_init_state+0x574>
9fc05d94:	24090001 	li	t1,1
9fc05d98:	8d680000 	lw	t0,0(t3)
9fc05d9c:	25c7ffff 	addiu	a3,t6,-1
9fc05da0:	012e202b 	sltu	a0,t1,t6
9fc05da4:	ad480000 	sw	t0,0(t2)
9fc05da8:	30e30007 	andi	v1,a3,0x7
9fc05dac:	25480004 	addiu	t0,t2,4
9fc05db0:	1080004e 	beqz	a0,9fc05eec <core_init_state+0x56c>
9fc05db4:	25670004 	addiu	a3,t3,4
9fc05db8:	10600032 	beqz	v1,9fc05e84 <core_init_state+0x504>
9fc05dbc:	00000000 	nop
9fc05dc0:	10690029 	beq	v1,t1,9fc05e68 <core_init_state+0x4e8>
9fc05dc4:	24040002 	li	a0,2
9fc05dc8:	10640022 	beq	v1,a0,9fc05e54 <core_init_state+0x4d4>
9fc05dcc:	24020003 	li	v0,3
9fc05dd0:	1062001b 	beq	v1,v0,9fc05e40 <core_init_state+0x4c0>
9fc05dd4:	24040004 	li	a0,4
9fc05dd8:	10640014 	beq	v1,a0,9fc05e2c <core_init_state+0x4ac>
9fc05ddc:	24020005 	li	v0,5
9fc05de0:	1062000d 	beq	v1,v0,9fc05e18 <core_init_state+0x498>
9fc05de4:	24040006 	li	a0,6
9fc05de8:	10640006 	beq	v1,a0,9fc05e04 <core_init_state+0x484>
9fc05dec:	00000000 	nop
9fc05df0:	8ce90000 	lw	t1,0(a3)
9fc05df4:	25480008 	addiu	t0,t2,8
9fc05df8:	ad490004 	sw	t1,4(t2)
9fc05dfc:	25670008 	addiu	a3,t3,8
9fc05e00:	24090002 	li	t1,2
9fc05e04:	8cea0000 	lw	t2,0(a3)
9fc05e08:	25290001 	addiu	t1,t1,1
9fc05e0c:	ad0a0000 	sw	t2,0(t0)
9fc05e10:	24e70004 	addiu	a3,a3,4
9fc05e14:	25080004 	addiu	t0,t0,4
9fc05e18:	8ce30000 	lw	v1,0(a3)
9fc05e1c:	25290001 	addiu	t1,t1,1
9fc05e20:	ad030000 	sw	v1,0(t0)
9fc05e24:	24e70004 	addiu	a3,a3,4
9fc05e28:	25080004 	addiu	t0,t0,4
9fc05e2c:	8ce20000 	lw	v0,0(a3)
9fc05e30:	25290001 	addiu	t1,t1,1
9fc05e34:	ad020000 	sw	v0,0(t0)
9fc05e38:	24e70004 	addiu	a3,a3,4
9fc05e3c:	25080004 	addiu	t0,t0,4
9fc05e40:	8ce40000 	lw	a0,0(a3)
9fc05e44:	25290001 	addiu	t1,t1,1
9fc05e48:	ad040000 	sw	a0,0(t0)
9fc05e4c:	24e70004 	addiu	a3,a3,4
9fc05e50:	25080004 	addiu	t0,t0,4
9fc05e54:	8cea0000 	lw	t2,0(a3)
9fc05e58:	25290001 	addiu	t1,t1,1
9fc05e5c:	ad0a0000 	sw	t2,0(t0)
9fc05e60:	24e70004 	addiu	a3,a3,4
9fc05e64:	25080004 	addiu	t0,t0,4
9fc05e68:	8ce20000 	lw	v0,0(a3)
9fc05e6c:	25290001 	addiu	t1,t1,1
9fc05e70:	012e182b 	sltu	v1,t1,t6
9fc05e74:	ad020000 	sw	v0,0(t0)
9fc05e78:	24e70004 	addiu	a3,a3,4
9fc05e7c:	1060001b 	beqz	v1,9fc05eec <core_init_state+0x56c>
9fc05e80:	25080004 	addiu	t0,t0,4
9fc05e84:	8cea0000 	lw	t2,0(a3)
9fc05e88:	25290008 	addiu	t1,t1,8
9fc05e8c:	ad0a0000 	sw	t2,0(t0)
9fc05e90:	8ce20004 	lw	v0,4(a3)
9fc05e94:	012e502b 	sltu	t2,t1,t6
9fc05e98:	ad020004 	sw	v0,4(t0)
9fc05e9c:	8ce40008 	lw	a0,8(a3)
9fc05ea0:	00000000 	nop
9fc05ea4:	ad040008 	sw	a0,8(t0)
9fc05ea8:	8ce3000c 	lw	v1,12(a3)
9fc05eac:	00000000 	nop
9fc05eb0:	ad03000c 	sw	v1,12(t0)
9fc05eb4:	8ce20010 	lw	v0,16(a3)
9fc05eb8:	00000000 	nop
9fc05ebc:	ad020010 	sw	v0,16(t0)
9fc05ec0:	8ce40014 	lw	a0,20(a3)
9fc05ec4:	00000000 	nop
9fc05ec8:	ad040014 	sw	a0,20(t0)
9fc05ecc:	8ce30018 	lw	v1,24(a3)
9fc05ed0:	00000000 	nop
9fc05ed4:	ad030018 	sw	v1,24(t0)
9fc05ed8:	8ce2001c 	lw	v0,28(a3)
9fc05edc:	24e70020 	addiu	a3,a3,32
9fc05ee0:	ad02001c 	sw	v0,28(t0)
9fc05ee4:	1540ffe7 	bnez	t2,9fc05e84 <core_init_state+0x504>
9fc05ee8:	25080020 	addiu	t0,t0,32
9fc05eec:	118dff97 	beq	t4,t5,9fc05d4c <core_init_state+0x3cc>
9fc05ef0:	00000000 	nop
9fc05ef4:	016d5821 	addu	t3,t3,t5
9fc05ef8:	01a61821 	addu	v1,t5,a2
9fc05efc:	000d4027 	nor	t0,zero,t5
9fc05f00:	91670000 	lbu	a3,0(t3)
9fc05f04:	25a90001 	addiu	t1,t5,1
9fc05f08:	03235021 	addu	t2,t9,v1
9fc05f0c:	010c7021 	addu	t6,t0,t4
9fc05f10:	012c302b 	sltu	a2,t1,t4
9fc05f14:	a1470000 	sb	a3,0(t2)
9fc05f18:	31c40007 	andi	a0,t6,0x7
9fc05f1c:	25680001 	addiu	t0,t3,1
9fc05f20:	10c0ff8a 	beqz	a2,9fc05d4c <core_init_state+0x3cc>
9fc05f24:	25470001 	addiu	a3,t2,1
9fc05f28:	10800032 	beqz	a0,9fc05ff4 <core_init_state+0x674>
9fc05f2c:	24020001 	li	v0,1
9fc05f30:	10820029 	beq	a0,v0,9fc05fd8 <core_init_state+0x658>
9fc05f34:	240e0002 	li	t6,2
9fc05f38:	108e0022 	beq	a0,t6,9fc05fc4 <core_init_state+0x644>
9fc05f3c:	24030003 	li	v1,3
9fc05f40:	1083001b 	beq	a0,v1,9fc05fb0 <core_init_state+0x630>
9fc05f44:	24060004 	li	a2,4
9fc05f48:	10860014 	beq	a0,a2,9fc05f9c <core_init_state+0x61c>
9fc05f4c:	24020005 	li	v0,5
9fc05f50:	1082000d 	beq	a0,v0,9fc05f88 <core_init_state+0x608>
9fc05f54:	240e0006 	li	t6,6
9fc05f58:	108e0006 	beq	a0,t6,9fc05f74 <core_init_state+0x5f4>
9fc05f5c:	00000000 	nop
9fc05f60:	91670001 	lbu	a3,1(t3)
9fc05f64:	25a90002 	addiu	t1,t5,2
9fc05f68:	a1470001 	sb	a3,1(t2)
9fc05f6c:	25680002 	addiu	t0,t3,2
9fc05f70:	25470002 	addiu	a3,t2,2
9fc05f74:	910b0000 	lbu	t3,0(t0)
9fc05f78:	25290001 	addiu	t1,t1,1
9fc05f7c:	a0eb0000 	sb	t3,0(a3)
9fc05f80:	25080001 	addiu	t0,t0,1
9fc05f84:	24e70001 	addiu	a3,a3,1
9fc05f88:	910a0000 	lbu	t2,0(t0)
9fc05f8c:	25290001 	addiu	t1,t1,1
9fc05f90:	a0ea0000 	sb	t2,0(a3)
9fc05f94:	25080001 	addiu	t0,t0,1
9fc05f98:	24e70001 	addiu	a3,a3,1
9fc05f9c:	910d0000 	lbu	t5,0(t0)
9fc05fa0:	25290001 	addiu	t1,t1,1
9fc05fa4:	a0ed0000 	sb	t5,0(a3)
9fc05fa8:	25080001 	addiu	t0,t0,1
9fc05fac:	24e70001 	addiu	a3,a3,1
9fc05fb0:	91040000 	lbu	a0,0(t0)
9fc05fb4:	25290001 	addiu	t1,t1,1
9fc05fb8:	a0e40000 	sb	a0,0(a3)
9fc05fbc:	25080001 	addiu	t0,t0,1
9fc05fc0:	24e70001 	addiu	a3,a3,1
9fc05fc4:	91030000 	lbu	v1,0(t0)
9fc05fc8:	25290001 	addiu	t1,t1,1
9fc05fcc:	a0e30000 	sb	v1,0(a3)
9fc05fd0:	25080001 	addiu	t0,t0,1
9fc05fd4:	24e70001 	addiu	a3,a3,1
9fc05fd8:	91020000 	lbu	v0,0(t0)
9fc05fdc:	25290001 	addiu	t1,t1,1
9fc05fe0:	012c302b 	sltu	a2,t1,t4
9fc05fe4:	a0e20000 	sb	v0,0(a3)
9fc05fe8:	25080001 	addiu	t0,t0,1
9fc05fec:	10c0ff57 	beqz	a2,9fc05d4c <core_init_state+0x3cc>
9fc05ff0:	24e70001 	addiu	a3,a3,1
9fc05ff4:	910b0000 	lbu	t3,0(t0)
9fc05ff8:	25290008 	addiu	t1,t1,8
9fc05ffc:	a0eb0000 	sb	t3,0(a3)
9fc06000:	91030001 	lbu	v1,1(t0)
9fc06004:	012c582b 	sltu	t3,t1,t4
9fc06008:	a0e30001 	sb	v1,1(a3)
9fc0600c:	910e0002 	lbu	t6,2(t0)
9fc06010:	00000000 	nop
9fc06014:	a0ee0002 	sb	t6,2(a3)
9fc06018:	91060003 	lbu	a2,3(t0)
9fc0601c:	00000000 	nop
9fc06020:	a0e60003 	sb	a2,3(a3)
9fc06024:	910a0004 	lbu	t2,4(t0)
9fc06028:	00000000 	nop
9fc0602c:	a0ea0004 	sb	t2,4(a3)
9fc06030:	91020005 	lbu	v0,5(t0)
9fc06034:	00000000 	nop
9fc06038:	a0e20005 	sb	v0,5(a3)
9fc0603c:	91040006 	lbu	a0,6(t0)
9fc06040:	00000000 	nop
9fc06044:	a0e40006 	sb	a0,6(a3)
9fc06048:	910d0007 	lbu	t5,7(t0)
9fc0604c:	25080008 	addiu	t0,t0,8
9fc06050:	a0ed0007 	sb	t5,7(a3)
9fc06054:	1560ffe7 	bnez	t3,9fc05ff4 <core_init_state+0x674>
9fc06058:	24e70008 	addiu	a3,a3,8
9fc0605c:	03386021 	addu	t4,t9,t8
9fc06060:	0bf01683 	j	9fc05a0c <core_init_state+0x8c>
9fc06064:	a1930000 	sb	s3,0(t4)
	...

9fc06070 <core_state_transition>:
core_state_transition():
9fc06070:	27bdffe8 	addiu	sp,sp,-24
9fc06074:	8c880000 	lw	t0,0(a0)
9fc06078:	afb10008 	sw	s1,8(sp)
9fc0607c:	afb40014 	sw	s4,20(sp)
9fc06080:	afb30010 	sw	s3,16(sp)
9fc06084:	afb2000c 	sw	s2,12(sp)
9fc06088:	afb00004 	sw	s0,4(sp)
9fc0608c:	91060000 	lbu	a2,0(t0)
9fc06090:	00000000 	nop
9fc06094:	10c000bd 	beqz	a2,9fc0638c <core_state_transition+0x31c>
9fc06098:	00808821 	move	s1,a0
9fc0609c:	2402002c 	li	v0,44
9fc060a0:	10c200bc 	beq	a2,v0,9fc06394 <core_state_transition+0x324>
9fc060a4:	3c039fc1 	lui	v1,0x9fc1
9fc060a8:	2464a9b0 	addiu	a0,v1,-22096
9fc060ac:	00003821 	move	a3,zero
9fc060b0:	24b80004 	addiu	t8,a1,4
9fc060b4:	240f0001 	li	t7,1
9fc060b8:	24ab0018 	addiu	t3,a1,24
9fc060bc:	2410002b 	li	s0,43
9fc060c0:	24aa000c 	addiu	t2,a1,12
9fc060c4:	2412002d 	li	s2,45
9fc060c8:	24130045 	li	s3,69
9fc060cc:	24ac0014 	addiu	t4,a1,20
9fc060d0:	24140065 	li	s4,101
9fc060d4:	2419002e 	li	t9,46
9fc060d8:	24ae0010 	addiu	t6,a1,16
9fc060dc:	24a90008 	addiu	t1,a1,8
9fc060e0:	240d002c 	li	t5,44
9fc060e4:	00071880 	sll	v1,a3,0x2
9fc060e8:	00831021 	addu	v0,a0,v1
9fc060ec:	8c430000 	lw	v1,0(v0)
9fc060f0:	00000000 	nop
9fc060f4:	00600008 	jr	v1
9fc060f8:	00000000 	nop
9fc060fc:	00000000 	nop
9fc06100:	24c3ffd0 	addiu	v1,a2,-48
9fc06104:	306200ff 	andi	v0,v1,0xff
9fc06108:	2c47000a 	sltiu	a3,v0,10
9fc0610c:	10e00084 	beqz	a3,9fc06320 <core_state_transition+0x2b0>
9fc06110:	24070004 	li	a3,4
9fc06114:	8ca60000 	lw	a2,0(a1)
9fc06118:	00000000 	nop
9fc0611c:	24c30001 	addiu	v1,a2,1
9fc06120:	aca30000 	sw	v1,0(a1)
9fc06124:	25080001 	addiu	t0,t0,1
9fc06128:	91060000 	lbu	a2,0(t0)
9fc0612c:	00000000 	nop
9fc06130:	10c00006 	beqz	a2,9fc0614c <core_state_transition+0xdc>
9fc06134:	00000000 	nop
9fc06138:	10ef0004 	beq	a3,t7,9fc0614c <core_state_transition+0xdc>
9fc0613c:	00000000 	nop
9fc06140:	14cdffe9 	bne	a2,t5,9fc060e8 <core_state_transition+0x78>
9fc06144:	00071880 	sll	v1,a3,0x2
9fc06148:	25080001 	addiu	t0,t0,1
9fc0614c:	ae280000 	sw	t0,0(s1)
9fc06150:	00e01021 	move	v0,a3
9fc06154:	8fb40014 	lw	s4,20(sp)
9fc06158:	8fb30010 	lw	s3,16(sp)
9fc0615c:	8fb2000c 	lw	s2,12(sp)
9fc06160:	8fb10008 	lw	s1,8(sp)
9fc06164:	8fb00004 	lw	s0,4(sp)
9fc06168:	03e00008 	jr	ra
9fc0616c:	27bd0018 	addiu	sp,sp,24
9fc06170:	24c3ffd0 	addiu	v1,a2,-48
9fc06174:	306200ff 	andi	v0,v1,0xff
9fc06178:	2c46000a 	sltiu	a2,v0,10
9fc0617c:	14c0ffe9 	bnez	a2,9fc06124 <core_state_transition+0xb4>
9fc06180:	00000000 	nop
9fc06184:	8f020000 	lw	v0,0(t8)
9fc06188:	24070001 	li	a3,1
9fc0618c:	24460001 	addiu	a2,v0,1
9fc06190:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06194:	af060000 	sw	a2,0(t8)
	...
9fc061a0:	24c2ffd0 	addiu	v0,a2,-48
9fc061a4:	304600ff 	andi	a2,v0,0xff
9fc061a8:	2cc7000a 	sltiu	a3,a2,10
9fc061ac:	10e0004c 	beqz	a3,9fc062e0 <core_state_transition+0x270>
9fc061b0:	00000000 	nop
9fc061b4:	8d660000 	lw	a2,0(t3)
9fc061b8:	24070007 	li	a3,7
9fc061bc:	24c30001 	addiu	v1,a2,1
9fc061c0:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc061c4:	ad630000 	sw	v1,0(t3)
	...
9fc061d0:	10d30033 	beq	a2,s3,9fc062a0 <core_state_transition+0x230>
9fc061d4:	00000000 	nop
9fc061d8:	10d40031 	beq	a2,s4,9fc062a0 <core_state_transition+0x230>
9fc061dc:	24c3ffd0 	addiu	v1,a2,-48
9fc061e0:	306200ff 	andi	v0,v1,0xff
9fc061e4:	2c46000a 	sltiu	a2,v0,10
9fc061e8:	14c0ffce 	bnez	a2,9fc06124 <core_state_transition+0xb4>
9fc061ec:	00000000 	nop
9fc061f0:	8d820000 	lw	v0,0(t4)
9fc061f4:	24070001 	li	a3,1
9fc061f8:	24460001 	addiu	a2,v0,1
9fc061fc:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06200:	ad860000 	sw	a2,0(t4)
	...
9fc06210:	10d90057 	beq	a2,t9,9fc06370 <core_state_transition+0x300>
9fc06214:	24c2ffd0 	addiu	v0,a2,-48
9fc06218:	304600ff 	andi	a2,v0,0xff
9fc0621c:	2cc3000a 	sltiu	v1,a2,10
9fc06220:	1460ffc0 	bnez	v1,9fc06124 <core_state_transition+0xb4>
9fc06224:	00000000 	nop
9fc06228:	8dc60000 	lw	a2,0(t6)
9fc0622c:	24070001 	li	a3,1
9fc06230:	24c30001 	addiu	v1,a2,1
9fc06234:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06238:	adc30000 	sw	v1,0(t6)
9fc0623c:	00000000 	nop
9fc06240:	24c3ffd0 	addiu	v1,a2,-48
9fc06244:	306200ff 	andi	v0,v1,0xff
9fc06248:	2c47000a 	sltiu	a3,v0,10
9fc0624c:	10e0002c 	beqz	a3,9fc06300 <core_state_transition+0x290>
9fc06250:	24070004 	li	a3,4
9fc06254:	8d220000 	lw	v0,0(t1)
9fc06258:	00000000 	nop
9fc0625c:	24460001 	addiu	a2,v0,1
9fc06260:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06264:	ad260000 	sw	a2,0(t1)
	...
9fc06270:	10d00013 	beq	a2,s0,9fc062c0 <core_state_transition+0x250>
9fc06274:	00000000 	nop
9fc06278:	10d20011 	beq	a2,s2,9fc062c0 <core_state_transition+0x250>
9fc0627c:	00000000 	nop
9fc06280:	8d430000 	lw	v1,0(t2)
9fc06284:	24070001 	li	a3,1
9fc06288:	24620001 	addiu	v0,v1,1
9fc0628c:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06290:	ad420000 	sw	v0,0(t2)
	...
9fc062a0:	8d830000 	lw	v1,0(t4)
9fc062a4:	24070003 	li	a3,3
9fc062a8:	24620001 	addiu	v0,v1,1
9fc062ac:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc062b0:	ad820000 	sw	v0,0(t4)
	...
9fc062c0:	8d460000 	lw	a2,0(t2)
9fc062c4:	24070006 	li	a3,6
9fc062c8:	24c30001 	addiu	v1,a2,1
9fc062cc:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc062d0:	ad430000 	sw	v1,0(t2)
	...
9fc062e0:	8d630000 	lw	v1,0(t3)
9fc062e4:	24070001 	li	a3,1
9fc062e8:	24620001 	addiu	v0,v1,1
9fc062ec:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc062f0:	ad620000 	sw	v0,0(t3)
	...
9fc06300:	10d90013 	beq	a2,t9,9fc06350 <core_state_transition+0x2e0>
9fc06304:	00000000 	nop
9fc06308:	8d230000 	lw	v1,0(t1)
9fc0630c:	24070001 	li	a3,1
9fc06310:	24620001 	addiu	v0,v1,1
9fc06314:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06318:	ad220000 	sw	v0,0(t1)
9fc0631c:	00000000 	nop
9fc06320:	10d0ff7c 	beq	a2,s0,9fc06114 <core_state_transition+0xa4>
9fc06324:	24070002 	li	a3,2
9fc06328:	10d2ff7a 	beq	a2,s2,9fc06114 <core_state_transition+0xa4>
9fc0632c:	00000000 	nop
9fc06330:	10d90014 	beq	a2,t9,9fc06384 <core_state_transition+0x314>
9fc06334:	00000000 	nop
9fc06338:	8f020000 	lw	v0,0(t8)
9fc0633c:	24070001 	li	a3,1
9fc06340:	24460001 	addiu	a2,v0,1
9fc06344:	0bf01845 	j	9fc06114 <core_state_transition+0xa4>
9fc06348:	af060000 	sw	a2,0(t8)
9fc0634c:	00000000 	nop
9fc06350:	8d260000 	lw	a2,0(t1)
9fc06354:	24070005 	li	a3,5
9fc06358:	24c30001 	addiu	v1,a2,1
9fc0635c:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06360:	ad230000 	sw	v1,0(t1)
	...
9fc06370:	8dc20000 	lw	v0,0(t6)
9fc06374:	24070005 	li	a3,5
9fc06378:	24460001 	addiu	a2,v0,1
9fc0637c:	0bf01849 	j	9fc06124 <core_state_transition+0xb4>
9fc06380:	adc60000 	sw	a2,0(t6)
9fc06384:	0bf01845 	j	9fc06114 <core_state_transition+0xa4>
9fc06388:	24070005 	li	a3,5
9fc0638c:	0bf01853 	j	9fc0614c <core_state_transition+0xdc>
9fc06390:	00003821 	move	a3,zero
9fc06394:	00003821 	move	a3,zero
9fc06398:	0bf01853 	j	9fc0614c <core_state_transition+0xdc>
9fc0639c:	25080001 	addiu	t0,t0,1

9fc063a0 <core_bench_state>:
core_bench_state():
9fc063a0:	27bdff98 	addiu	sp,sp,-104
9fc063a4:	afb40060 	sw	s4,96(sp)
9fc063a8:	afb3005c 	sw	s3,92(sp)
9fc063ac:	afb20058 	sw	s2,88(sp)
9fc063b0:	afb00050 	sw	s0,80(sp)
9fc063b4:	afbf0064 	sw	ra,100(sp)
9fc063b8:	afb10054 	sw	s1,84(sp)
9fc063bc:	90ab0000 	lbu	t3,0(a1)
9fc063c0:	00068400 	sll	s0,a2,0x10
9fc063c4:	00079400 	sll	s2,a3,0x10
9fc063c8:	00809821 	move	s3,a0
9fc063cc:	00108403 	sra	s0,s0,0x10
9fc063d0:	afa00030 	sw	zero,48(sp)
9fc063d4:	afa00010 	sw	zero,16(sp)
9fc063d8:	afa00034 	sw	zero,52(sp)
9fc063dc:	afa00014 	sw	zero,20(sp)
9fc063e0:	afa00038 	sw	zero,56(sp)
9fc063e4:	afa00018 	sw	zero,24(sp)
9fc063e8:	afa0003c 	sw	zero,60(sp)
9fc063ec:	afa0001c 	sw	zero,28(sp)
9fc063f0:	afa00040 	sw	zero,64(sp)
9fc063f4:	afa00020 	sw	zero,32(sp)
9fc063f8:	afa00044 	sw	zero,68(sp)
9fc063fc:	afa00024 	sw	zero,36(sp)
9fc06400:	afa00048 	sw	zero,72(sp)
9fc06404:	afa00028 	sw	zero,40(sp)
9fc06408:	afa0004c 	sw	zero,76(sp)
9fc0640c:	afa0002c 	sw	zero,44(sp)
9fc06410:	87ad0078 	lh	t5,120(sp)
9fc06414:	97b4007c 	lhu	s4,124(sp)
9fc06418:	11600054 	beqz	t3,9fc0656c <core_bench_state+0x1cc>
9fc0641c:	00129403 	sra	s2,s2,0x10
9fc06420:	3c029fc1 	lui	v0,0x9fc1
9fc06424:	01604821 	move	t1,t3
9fc06428:	240e002c 	li	t6,44
9fc0642c:	2459a9d0 	addiu	t9,v0,-22064
9fc06430:	00a06021 	move	t4,a1
9fc06434:	27b10010 	addiu	s1,sp,16
9fc06438:	240f0001 	li	t7,1
9fc0643c:	241f002b 	li	ra,43
9fc06440:	2404002d 	li	a0,45
9fc06444:	24060045 	li	a2,69
9fc06448:	112e0028 	beq	t1,t6,9fc064ec <core_bench_state+0x14c>
9fc0644c:	24070065 	li	a3,101
9fc06450:	00005021 	move	t2,zero
9fc06454:	2418002e 	li	t8,46
9fc06458:	000a4080 	sll	t0,t2,0x2
9fc0645c:	03281021 	addu	v0,t9,t0
9fc06460:	8c430000 	lw	v1,0(v0)
9fc06464:	00000000 	nop
9fc06468:	00600008 	jr	v1
9fc0646c:	00000000 	nop
9fc06470:	1126008f 	beq	t1,a2,9fc066b0 <core_bench_state+0x310>
9fc06474:	00000000 	nop
9fc06478:	1127008d 	beq	t1,a3,9fc066b0 <core_bench_state+0x310>
9fc0647c:	2522ffd0 	addiu	v0,t1,-48
9fc06480:	304900ff 	andi	t1,v0,0xff
9fc06484:	2d23000a 	sltiu	v1,t1,10
9fc06488:	14600005 	bnez	v1,9fc064a0 <core_bench_state+0x100>
9fc0648c:	00000000 	nop
9fc06490:	8fa30044 	lw	v1,68(sp)
9fc06494:	240a0001 	li	t2,1
9fc06498:	24680001 	addiu	t0,v1,1
9fc0649c:	afa80044 	sw	t0,68(sp)
9fc064a0:	258c0001 	addiu	t4,t4,1
9fc064a4:	91880000 	lbu	t0,0(t4)
9fc064a8:	00000000 	nop
9fc064ac:	11000029 	beqz	t0,9fc06554 <core_bench_state+0x1b4>
9fc064b0:	01004821 	move	t1,t0
9fc064b4:	114f00b2 	beq	t2,t7,9fc06780 <core_bench_state+0x3e0>
9fc064b8:	00000000 	nop
9fc064bc:	152effe7 	bne	t1,t6,9fc0645c <core_bench_state+0xbc>
9fc064c0:	000a4080 	sll	t0,t2,0x2
9fc064c4:	02281821 	addu	v1,s1,t0
9fc064c8:	8c6a0000 	lw	t2,0(v1)
9fc064cc:	258c0001 	addiu	t4,t4,1
9fc064d0:	91880000 	lbu	t0,0(t4)
9fc064d4:	25490001 	addiu	t1,t2,1
9fc064d8:	11000024 	beqz	t0,9fc0656c <core_bench_state+0x1cc>
9fc064dc:	ac690000 	sw	t1,0(v1)
9fc064e0:	01004821 	move	t1,t0
9fc064e4:	152effdb 	bne	t1,t6,9fc06454 <core_bench_state+0xb4>
9fc064e8:	00005021 	move	t2,zero
9fc064ec:	00005021 	move	t2,zero
9fc064f0:	000a4080 	sll	t0,t2,0x2
9fc064f4:	02281821 	addu	v1,s1,t0
9fc064f8:	8c6a0000 	lw	t2,0(v1)
9fc064fc:	258c0001 	addiu	t4,t4,1
9fc06500:	91880000 	lbu	t0,0(t4)
9fc06504:	25490001 	addiu	t1,t2,1
9fc06508:	1500fff5 	bnez	t0,9fc064e0 <core_bench_state+0x140>
9fc0650c:	ac690000 	sw	t1,0(v1)
9fc06510:	0bf0195c 	j	9fc06570 <core_bench_state+0x1d0>
9fc06514:	00b32021 	addu	a0,a1,s3
	...
9fc06520:	2522ffd0 	addiu	v0,t1,-48
9fc06524:	304300ff 	andi	v1,v0,0xff
9fc06528:	2c68000a 	sltiu	t0,v1,10
9fc0652c:	1500ffdc 	bnez	t0,9fc064a0 <core_bench_state+0x100>
9fc06530:	00000000 	nop
9fc06534:	8fa80034 	lw	t0,52(sp)
9fc06538:	258c0001 	addiu	t4,t4,1
9fc0653c:	25090001 	addiu	t1,t0,1
9fc06540:	afa90034 	sw	t1,52(sp)
9fc06544:	91880000 	lbu	t0,0(t4)
9fc06548:	240a0001 	li	t2,1
9fc0654c:	1500ffd9 	bnez	t0,9fc064b4 <core_bench_state+0x114>
9fc06550:	01004821 	move	t1,t0
9fc06554:	000a6080 	sll	t4,t2,0x2
9fc06558:	022cc821 	addu	t9,s1,t4
9fc0655c:	8f3f0000 	lw	ra,0(t9)
9fc06560:	00000000 	nop
9fc06564:	27e40001 	addiu	a0,ra,1
9fc06568:	af240000 	sw	a0,0(t9)
9fc0656c:	00b32021 	addu	a0,a1,s3
9fc06570:	00a4382b 	sltu	a3,a1,a0
9fc06574:	10e00091 	beqz	a3,9fc067bc <core_bench_state+0x41c>
9fc06578:	00ad1821 	addu	v1,a1,t5
9fc0657c:	00a04021 	move	t0,a1
9fc06580:	240a002c 	li	t2,44
9fc06584:	000d4823 	negu	t1,t5
9fc06588:	116a0002 	beq	t3,t2,9fc06594 <core_bench_state+0x1f4>
9fc0658c:	01703026 	xor	a2,t3,s0
9fc06590:	a1060000 	sb	a2,0(t0)
9fc06594:	006d1821 	addu	v1,v1,t5
9fc06598:	006d6023 	subu	t4,v1,t5
9fc0659c:	0184582b 	sltu	t3,t4,a0
9fc065a0:	11600084 	beqz	t3,9fc067b4 <core_bench_state+0x414>
9fc065a4:	010d4021 	addu	t0,t0,t5
9fc065a8:	00697021 	addu	t6,v1,t1
9fc065ac:	91cb0000 	lbu	t3,0(t6)
9fc065b0:	0bf01962 	j	9fc06588 <core_bench_state+0x1e8>
9fc065b4:	00000000 	nop
	...
9fc065c0:	11380067 	beq	t1,t8,9fc06760 <core_bench_state+0x3c0>
9fc065c4:	2528ffd0 	addiu	t0,t1,-48
9fc065c8:	310200ff 	andi	v0,t0,0xff
9fc065cc:	2c49000a 	sltiu	t1,v0,10
9fc065d0:	1520ffb3 	bnez	t1,9fc064a0 <core_bench_state+0x100>
9fc065d4:	00000000 	nop
9fc065d8:	8fa90040 	lw	t1,64(sp)
9fc065dc:	240a0001 	li	t2,1
9fc065e0:	25230001 	addiu	v1,t1,1
9fc065e4:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc065e8:	afa30040 	sw	v1,64(sp)
9fc065ec:	00000000 	nop
9fc065f0:	113f0037 	beq	t1,ra,9fc066d0 <core_bench_state+0x330>
9fc065f4:	00000000 	nop
9fc065f8:	11240035 	beq	t1,a0,9fc066d0 <core_bench_state+0x330>
9fc065fc:	00000000 	nop
9fc06600:	8fa3003c 	lw	v1,60(sp)
9fc06604:	240a0001 	li	t2,1
9fc06608:	24680001 	addiu	t0,v1,1
9fc0660c:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06610:	afa8003c 	sw	t0,60(sp)
	...
9fc06620:	2522ffd0 	addiu	v0,t1,-48
9fc06624:	304900ff 	andi	t1,v0,0xff
9fc06628:	2d2a000a 	sltiu	t2,t1,10
9fc0662c:	11400038 	beqz	t2,9fc06710 <core_bench_state+0x370>
9fc06630:	00000000 	nop
9fc06634:	8fa30048 	lw	v1,72(sp)
9fc06638:	240a0007 	li	t2,7
9fc0663c:	24680001 	addiu	t0,v1,1
9fc06640:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06644:	afa80048 	sw	t0,72(sp)
	...
9fc06650:	2522ffd0 	addiu	v0,t1,-48
9fc06654:	304300ff 	andi	v1,v0,0xff
9fc06658:	2c6a000a 	sltiu	t2,v1,10
9fc0665c:	11400034 	beqz	t2,9fc06730 <core_bench_state+0x390>
9fc06660:	240a0004 	li	t2,4
9fc06664:	8fa20030 	lw	v0,48(sp)
9fc06668:	00000000 	nop
9fc0666c:	24430001 	addiu	v1,v0,1
9fc06670:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06674:	afa30030 	sw	v1,48(sp)
	...
9fc06680:	2523ffd0 	addiu	v1,t1,-48
9fc06684:	306800ff 	andi	t0,v1,0xff
9fc06688:	2d0a000a 	sltiu	t2,t0,10
9fc0668c:	11400018 	beqz	t2,9fc066f0 <core_bench_state+0x350>
9fc06690:	240a0004 	li	t2,4
9fc06694:	8fa20038 	lw	v0,56(sp)
9fc06698:	00000000 	nop
9fc0669c:	24490001 	addiu	t1,v0,1
9fc066a0:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc066a4:	afa90038 	sw	t1,56(sp)
	...
9fc066b0:	8fa80044 	lw	t0,68(sp)
9fc066b4:	240a0003 	li	t2,3
9fc066b8:	25020001 	addiu	v0,t0,1
9fc066bc:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc066c0:	afa20044 	sw	v0,68(sp)
	...
9fc066d0:	8fa2003c 	lw	v0,60(sp)
9fc066d4:	240a0006 	li	t2,6
9fc066d8:	24490001 	addiu	t1,v0,1
9fc066dc:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc066e0:	afa9003c 	sw	t1,60(sp)
	...
9fc066f0:	1138002b 	beq	t1,t8,9fc067a0 <core_bench_state+0x400>
9fc066f4:	00000000 	nop
9fc066f8:	8fa20038 	lw	v0,56(sp)
9fc066fc:	240a0001 	li	t2,1
9fc06700:	24490001 	addiu	t1,v0,1
9fc06704:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06708:	afa90038 	sw	t1,56(sp)
9fc0670c:	00000000 	nop
9fc06710:	8fa20048 	lw	v0,72(sp)
9fc06714:	240a0001 	li	t2,1
9fc06718:	24490001 	addiu	t1,v0,1
9fc0671c:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06720:	afa90048 	sw	t1,72(sp)
	...
9fc06730:	113fffcc 	beq	t1,ra,9fc06664 <core_bench_state+0x2c4>
9fc06734:	240a0002 	li	t2,2
9fc06738:	1124ffca 	beq	t1,a0,9fc06664 <core_bench_state+0x2c4>
9fc0673c:	00000000 	nop
9fc06740:	11380136 	beq	t1,t8,9fc06c1c <core_bench_state+0x87c>
9fc06744:	00000000 	nop
9fc06748:	8fa80034 	lw	t0,52(sp)
9fc0674c:	240a0001 	li	t2,1
9fc06750:	25090001 	addiu	t1,t0,1
9fc06754:	0bf01999 	j	9fc06664 <core_bench_state+0x2c4>
9fc06758:	afa90034 	sw	t1,52(sp)
9fc0675c:	00000000 	nop
9fc06760:	8fa30040 	lw	v1,64(sp)
9fc06764:	240a0005 	li	t2,5
9fc06768:	24680001 	addiu	t0,v1,1
9fc0676c:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc06770:	afa80040 	sw	t0,64(sp)
	...
9fc06780:	8fa90014 	lw	t1,20(sp)
9fc06784:	00000000 	nop
9fc06788:	252a0001 	addiu	t2,t1,1
9fc0678c:	afaa0014 	sw	t2,20(sp)
9fc06790:	0bf01939 	j	9fc064e4 <core_bench_state+0x144>
9fc06794:	01004821 	move	t1,t0
	...
9fc067a0:	8fa30038 	lw	v1,56(sp)
9fc067a4:	240a0005 	li	t2,5
9fc067a8:	24680001 	addiu	t0,v1,1
9fc067ac:	0bf01928 	j	9fc064a0 <core_bench_state+0x100>
9fc067b0:	afa80038 	sw	t0,56(sp)
9fc067b4:	90ab0000 	lbu	t3,0(a1)
9fc067b8:	00000000 	nop
9fc067bc:	11600053 	beqz	t3,9fc0690c <core_bench_state+0x56c>
9fc067c0:	3c119fc1 	lui	s1,0x9fc1
9fc067c4:	240a002c 	li	t2,44
9fc067c8:	262fa9f0 	addiu	t7,s1,-22032
9fc067cc:	00a04821 	move	t1,a1
9fc067d0:	27b10010 	addiu	s1,sp,16
9fc067d4:	240c0001 	li	t4,1
9fc067d8:	2418002b 	li	t8,43
9fc067dc:	2419002d 	li	t9,45
9fc067e0:	241f0045 	li	ra,69
9fc067e4:	116a002a 	beq	t3,t2,9fc06890 <core_bench_state+0x4f0>
9fc067e8:	24100065 	li	s0,101
9fc067ec:	00004021 	move	t0,zero
9fc067f0:	240e002e 	li	t6,46
9fc067f4:	00081080 	sll	v0,t0,0x2
9fc067f8:	01e21821 	addu	v1,t7,v0
9fc067fc:	8c730000 	lw	s3,0(v1)
9fc06800:	00000000 	nop
9fc06804:	02600008 	jr	s3
9fc06808:	00000000 	nop
9fc0680c:	00000000 	nop
9fc06810:	117f00bf 	beq	t3,ra,9fc06b10 <core_bench_state+0x770>
9fc06814:	00000000 	nop
9fc06818:	117000bd 	beq	t3,s0,9fc06b10 <core_bench_state+0x770>
9fc0681c:	2566ffd0 	addiu	a2,t3,-48
9fc06820:	30cb00ff 	andi	t3,a2,0xff
9fc06824:	2d62000a 	sltiu	v0,t3,10
9fc06828:	14400005 	bnez	v0,9fc06840 <core_bench_state+0x4a0>
9fc0682c:	00000000 	nop
9fc06830:	8fa30044 	lw	v1,68(sp)
9fc06834:	24080001 	li	t0,1
9fc06838:	24730001 	addiu	s3,v1,1
9fc0683c:	afb30044 	sw	s3,68(sp)
9fc06840:	25290001 	addiu	t1,t1,1
9fc06844:	91260000 	lbu	a2,0(t1)
9fc06848:	00000000 	nop
9fc0684c:	10c00029 	beqz	a2,9fc068f4 <core_bench_state+0x554>
9fc06850:	00c05821 	move	t3,a2
9fc06854:	110c00e2 	beq	t0,t4,9fc06be0 <core_bench_state+0x840>
9fc06858:	00000000 	nop
9fc0685c:	156affe6 	bne	t3,t2,9fc067f8 <core_bench_state+0x458>
9fc06860:	00081080 	sll	v0,t0,0x2
9fc06864:	00083080 	sll	a2,t0,0x2
9fc06868:	02261821 	addu	v1,s1,a2
9fc0686c:	8c620000 	lw	v0,0(v1)
9fc06870:	25290001 	addiu	t1,t1,1
9fc06874:	91260000 	lbu	a2,0(t1)
9fc06878:	24530001 	addiu	s3,v0,1
9fc0687c:	10c00023 	beqz	a2,9fc0690c <core_bench_state+0x56c>
9fc06880:	ac730000 	sw	s3,0(v1)
9fc06884:	00c05821 	move	t3,a2
9fc06888:	156affd9 	bne	t3,t2,9fc067f0 <core_bench_state+0x450>
9fc0688c:	00004021 	move	t0,zero
9fc06890:	00004021 	move	t0,zero
9fc06894:	00083080 	sll	a2,t0,0x2
9fc06898:	02261821 	addu	v1,s1,a2
9fc0689c:	8c620000 	lw	v0,0(v1)
9fc068a0:	25290001 	addiu	t1,t1,1
9fc068a4:	91260000 	lbu	a2,0(t1)
9fc068a8:	24530001 	addiu	s3,v0,1
9fc068ac:	14c0fff5 	bnez	a2,9fc06884 <core_bench_state+0x4e4>
9fc068b0:	ac730000 	sw	s3,0(v1)
9fc068b4:	0bf01a43 	j	9fc0690c <core_bench_state+0x56c>
9fc068b8:	00000000 	nop
9fc068bc:	00000000 	nop
9fc068c0:	2566ffd0 	addiu	a2,t3,-48
9fc068c4:	30cb00ff 	andi	t3,a2,0xff
9fc068c8:	2d62000a 	sltiu	v0,t3,10
9fc068cc:	1440ffdc 	bnez	v0,9fc06840 <core_bench_state+0x4a0>
9fc068d0:	00000000 	nop
9fc068d4:	8fa30034 	lw	v1,52(sp)
9fc068d8:	25290001 	addiu	t1,t1,1
9fc068dc:	24730001 	addiu	s3,v1,1
9fc068e0:	afb30034 	sw	s3,52(sp)
9fc068e4:	91260000 	lbu	a2,0(t1)
9fc068e8:	24080001 	li	t0,1
9fc068ec:	14c0ffd9 	bnez	a2,9fc06854 <core_bench_state+0x4b4>
9fc068f0:	00c05821 	move	t3,a2
9fc068f4:	00081080 	sll	v0,t0,0x2
9fc068f8:	02225821 	addu	t3,s1,v0
9fc068fc:	8d710000 	lw	s1,0(t3)
9fc06900:	00000000 	nop
9fc06904:	26300001 	addiu	s0,s1,1
9fc06908:	ad700000 	sw	s0,0(t3)
9fc0690c:	10e0000d 	beqz	a3,9fc06944 <core_bench_state+0x5a4>
9fc06910:	00a01821 	move	v1,a1
9fc06914:	2406002c 	li	a2,44
9fc06918:	00ad2821 	addu	a1,a1,t5
9fc0691c:	90620000 	lbu	v0,0(v1)
9fc06920:	00000000 	nop
9fc06924:	10460002 	beq	v0,a2,9fc06930 <core_bench_state+0x590>
9fc06928:	00523826 	xor	a3,v0,s2
9fc0692c:	a0670000 	sb	a3,0(v1)
9fc06930:	00ad2821 	addu	a1,a1,t5
9fc06934:	00adc023 	subu	t8,a1,t5
9fc06938:	0304782b 	sltu	t7,t8,a0
9fc0693c:	15e0fff7 	bnez	t7,9fc0691c <core_bench_state+0x57c>
9fc06940:	006d1821 	addu	v1,v1,t5
9fc06944:	8fa40010 	lw	a0,16(sp)
9fc06948:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc0694c:	02802821 	move	a1,s4
9fc06950:	8fa40030 	lw	a0,48(sp)
9fc06954:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc06958:	00402821 	move	a1,v0
9fc0695c:	8fa40014 	lw	a0,20(sp)
9fc06960:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc06964:	00402821 	move	a1,v0
9fc06968:	8fa40034 	lw	a0,52(sp)
9fc0696c:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc06970:	00402821 	move	a1,v0
9fc06974:	8fa40018 	lw	a0,24(sp)
9fc06978:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc0697c:	00402821 	move	a1,v0
9fc06980:	8fa40038 	lw	a0,56(sp)
9fc06984:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc06988:	00402821 	move	a1,v0
9fc0698c:	8fa4001c 	lw	a0,28(sp)
9fc06990:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc06994:	00402821 	move	a1,v0
9fc06998:	8fa4003c 	lw	a0,60(sp)
9fc0699c:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069a0:	00402821 	move	a1,v0
9fc069a4:	8fa40020 	lw	a0,32(sp)
9fc069a8:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069ac:	00402821 	move	a1,v0
9fc069b0:	8fa40040 	lw	a0,64(sp)
9fc069b4:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069b8:	00402821 	move	a1,v0
9fc069bc:	8fa40024 	lw	a0,36(sp)
9fc069c0:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069c4:	00402821 	move	a1,v0
9fc069c8:	8fa40044 	lw	a0,68(sp)
9fc069cc:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069d0:	00402821 	move	a1,v0
9fc069d4:	8fa40028 	lw	a0,40(sp)
9fc069d8:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069dc:	00402821 	move	a1,v0
9fc069e0:	8fa40048 	lw	a0,72(sp)
9fc069e4:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069e8:	00402821 	move	a1,v0
9fc069ec:	8fa4002c 	lw	a0,44(sp)
9fc069f0:	0ff01e3c 	jal	9fc078f0 <crcu32>
9fc069f4:	00402821 	move	a1,v0
9fc069f8:	8fa4004c 	lw	a0,76(sp)
9fc069fc:	8fbf0064 	lw	ra,100(sp)
9fc06a00:	8fb40060 	lw	s4,96(sp)
9fc06a04:	8fb3005c 	lw	s3,92(sp)
9fc06a08:	8fb20058 	lw	s2,88(sp)
9fc06a0c:	8fb10054 	lw	s1,84(sp)
9fc06a10:	8fb00050 	lw	s0,80(sp)
9fc06a14:	00402821 	move	a1,v0
9fc06a18:	0bf01e3c 	j	9fc078f0 <crcu32>
9fc06a1c:	27bd0068 	addiu	sp,sp,104
9fc06a20:	2562ffd0 	addiu	v0,t3,-48
9fc06a24:	304300ff 	andi	v1,v0,0xff
9fc06a28:	2c68000a 	sltiu	t0,v1,10
9fc06a2c:	11000048 	beqz	t0,9fc06b50 <core_bench_state+0x7b0>
9fc06a30:	00000000 	nop
9fc06a34:	8fa60048 	lw	a2,72(sp)
9fc06a38:	24080007 	li	t0,7
9fc06a3c:	24cb0001 	addiu	t3,a2,1
9fc06a40:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06a44:	afab0048 	sw	t3,72(sp)
	...
9fc06a50:	2573ffd0 	addiu	s3,t3,-48
9fc06a54:	326600ff 	andi	a2,s3,0xff
9fc06a58:	2cc8000a 	sltiu	t0,a2,10
9fc06a5c:	11000044 	beqz	t0,9fc06b70 <core_bench_state+0x7d0>
9fc06a60:	24080004 	li	t0,4
9fc06a64:	8fa60030 	lw	a2,48(sp)
9fc06a68:	00000000 	nop
9fc06a6c:	24c20001 	addiu	v0,a2,1
9fc06a70:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06a74:	afa20030 	sw	v0,48(sp)
	...
9fc06a80:	1178002b 	beq	t3,t8,9fc06b30 <core_bench_state+0x790>
9fc06a84:	00000000 	nop
9fc06a88:	11790029 	beq	t3,t9,9fc06b30 <core_bench_state+0x790>
9fc06a8c:	00000000 	nop
9fc06a90:	8fb3003c 	lw	s3,60(sp)
9fc06a94:	24080001 	li	t0,1
9fc06a98:	26660001 	addiu	a2,s3,1
9fc06a9c:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06aa0:	afa6003c 	sw	a2,60(sp)
	...
9fc06ab0:	116e0043 	beq	t3,t6,9fc06bc0 <core_bench_state+0x820>
9fc06ab4:	2562ffd0 	addiu	v0,t3,-48
9fc06ab8:	304300ff 	andi	v1,v0,0xff
9fc06abc:	2c73000a 	sltiu	s3,v1,10
9fc06ac0:	1660ff5f 	bnez	s3,9fc06840 <core_bench_state+0x4a0>
9fc06ac4:	00000000 	nop
9fc06ac8:	8fa60040 	lw	a2,64(sp)
9fc06acc:	24080001 	li	t0,1
9fc06ad0:	24cb0001 	addiu	t3,a2,1
9fc06ad4:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06ad8:	afab0040 	sw	t3,64(sp)
9fc06adc:	00000000 	nop
9fc06ae0:	2563ffd0 	addiu	v1,t3,-48
9fc06ae4:	307300ff 	andi	s3,v1,0xff
9fc06ae8:	2e68000a 	sltiu	t0,s3,10
9fc06aec:	1100002c 	beqz	t0,9fc06ba0 <core_bench_state+0x800>
9fc06af0:	24080004 	li	t0,4
9fc06af4:	8fa20038 	lw	v0,56(sp)
9fc06af8:	00000000 	nop
9fc06afc:	244b0001 	addiu	t3,v0,1
9fc06b00:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06b04:	afab0038 	sw	t3,56(sp)
	...
9fc06b10:	8fa30044 	lw	v1,68(sp)
9fc06b14:	24080003 	li	t0,3
9fc06b18:	24730001 	addiu	s3,v1,1
9fc06b1c:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06b20:	afb30044 	sw	s3,68(sp)
	...
9fc06b30:	8fab003c 	lw	t3,60(sp)
9fc06b34:	24080006 	li	t0,6
9fc06b38:	25620001 	addiu	v0,t3,1
9fc06b3c:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06b40:	afa2003c 	sw	v0,60(sp)
	...
9fc06b50:	8fa30048 	lw	v1,72(sp)
9fc06b54:	24080001 	li	t0,1
9fc06b58:	24730001 	addiu	s3,v1,1
9fc06b5c:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06b60:	afb30048 	sw	s3,72(sp)
	...
9fc06b70:	1178ffbc 	beq	t3,t8,9fc06a64 <core_bench_state+0x6c4>
9fc06b74:	24080002 	li	t0,2
9fc06b78:	1179ffba 	beq	t3,t9,9fc06a64 <core_bench_state+0x6c4>
9fc06b7c:	00000000 	nop
9fc06b80:	116e0024 	beq	t3,t6,9fc06c14 <core_bench_state+0x874>
9fc06b84:	00000000 	nop
9fc06b88:	8fa30034 	lw	v1,52(sp)
9fc06b8c:	24080001 	li	t0,1
9fc06b90:	246b0001 	addiu	t3,v1,1
9fc06b94:	0bf01a99 	j	9fc06a64 <core_bench_state+0x6c4>
9fc06b98:	afab0034 	sw	t3,52(sp)
9fc06b9c:	00000000 	nop
9fc06ba0:	116e0017 	beq	t3,t6,9fc06c00 <core_bench_state+0x860>
9fc06ba4:	00000000 	nop
9fc06ba8:	8fab0038 	lw	t3,56(sp)
9fc06bac:	24080001 	li	t0,1
9fc06bb0:	25630001 	addiu	v1,t3,1
9fc06bb4:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06bb8:	afa30038 	sw	v1,56(sp)
9fc06bbc:	00000000 	nop
9fc06bc0:	8fa60040 	lw	a2,64(sp)
9fc06bc4:	24080005 	li	t0,5
9fc06bc8:	24c20001 	addiu	v0,a2,1
9fc06bcc:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06bd0:	afa20040 	sw	v0,64(sp)
	...
9fc06be0:	8fa80014 	lw	t0,20(sp)
9fc06be4:	00c05821 	move	t3,a2
9fc06be8:	250e0001 	addiu	t6,t0,1
9fc06bec:	0bf01a22 	j	9fc06888 <core_bench_state+0x4e8>
9fc06bf0:	afae0014 	sw	t6,20(sp)
	...
9fc06c00:	8fb30038 	lw	s3,56(sp)
9fc06c04:	24080005 	li	t0,5
9fc06c08:	26660001 	addiu	a2,s3,1
9fc06c0c:	0bf01a10 	j	9fc06840 <core_bench_state+0x4a0>
9fc06c10:	afa60038 	sw	a2,56(sp)
9fc06c14:	0bf01a99 	j	9fc06a64 <core_bench_state+0x6c4>
9fc06c18:	24080005 	li	t0,5
9fc06c1c:	0bf01999 	j	9fc06664 <core_bench_state+0x2c4>
9fc06c20:	240a0005 	li	t2,5
	...

9fc06c30 <parseval>:
parseval():
9fc06c30:	80870000 	lb	a3,0(a0)
9fc06c34:	2402002d 	li	v0,45
9fc06c38:	10e2003d 	beq	a3,v0,9fc06d30 <parseval+0x100>
9fc06c3c:	00802821 	move	a1,a0
9fc06c40:	24030030 	li	v1,48
9fc06c44:	10e3003f 	beq	a3,v1,9fc06d44 <parseval+0x114>
9fc06c48:	240a0001 	li	t2,1
9fc06c4c:	24ebffd0 	addiu	t3,a3,-48
9fc06c50:	316900ff 	andi	t1,t3,0xff
9fc06c54:	2d28000a 	sltiu	t0,t1,10
9fc06c58:	1100002b 	beqz	t0,9fc06d08 <parseval+0xd8>
9fc06c5c:	00004021 	move	t0,zero
9fc06c60:	000868c0 	sll	t5,t0,0x3
9fc06c64:	00086040 	sll	t4,t0,0x1
9fc06c68:	018d4021 	addu	t0,t4,t5
9fc06c6c:	00e85821 	addu	t3,a3,t0
9fc06c70:	80a70001 	lb	a3,1(a1)
9fc06c74:	2568ffd0 	addiu	t0,t3,-48
9fc06c78:	24e4ffd0 	addiu	a0,a3,-48
9fc06c7c:	000848c0 	sll	t1,t0,0x3
9fc06c80:	00083040 	sll	a2,t0,0x1
9fc06c84:	308300ff 	andi	v1,a0,0xff
9fc06c88:	00c91021 	addu	v0,a2,t1
9fc06c8c:	2c79000a 	sltiu	t9,v1,10
9fc06c90:	1320001d 	beqz	t9,9fc06d08 <parseval+0xd8>
9fc06c94:	00e21021 	addu	v0,a3,v0
9fc06c98:	80a70002 	lb	a3,2(a1)
9fc06c9c:	2448ffd0 	addiu	t0,v0,-48
9fc06ca0:	24e4ffd0 	addiu	a0,a3,-48
9fc06ca4:	000810c0 	sll	v0,t0,0x3
9fc06ca8:	00083040 	sll	a2,t0,0x1
9fc06cac:	309900ff 	andi	t9,a0,0xff
9fc06cb0:	00c21821 	addu	v1,a2,v0
9fc06cb4:	2f38000a 	sltiu	t8,t9,10
9fc06cb8:	13000013 	beqz	t8,9fc06d08 <parseval+0xd8>
9fc06cbc:	00e31821 	addu	v1,a3,v1
9fc06cc0:	80a70003 	lb	a3,3(a1)
9fc06cc4:	24a50004 	addiu	a1,a1,4
9fc06cc8:	24eeffd0 	addiu	t6,a3,-48
9fc06ccc:	31c800ff 	andi	t0,t6,0xff
9fc06cd0:	2d09000a 	sltiu	t1,t0,10
9fc06cd4:	2468ffd0 	addiu	t0,v1,-48
9fc06cd8:	000868c0 	sll	t5,t0,0x3
9fc06cdc:	00086040 	sll	t4,t0,0x1
9fc06ce0:	018d5821 	addu	t3,t4,t5
9fc06ce4:	11200008 	beqz	t1,9fc06d08 <parseval+0xd8>
9fc06ce8:	00eb1821 	addu	v1,a3,t3
9fc06cec:	80a70000 	lb	a3,0(a1)
9fc06cf0:	00000000 	nop
9fc06cf4:	24f9ffd0 	addiu	t9,a3,-48
9fc06cf8:	333800ff 	andi	t8,t9,0xff
9fc06cfc:	2f0f000a 	sltiu	t7,t8,10
9fc06d00:	15e0ffd7 	bnez	t7,9fc06c60 <parseval+0x30>
9fc06d04:	2468ffd0 	addiu	t0,v1,-48
9fc06d08:	240e004b 	li	t6,75
9fc06d0c:	10ee0028 	beq	a3,t6,9fc06db0 <parseval+0x180>
9fc06d10:	240f004d 	li	t7,77
9fc06d14:	14ef0003 	bne	a3,t7,9fc06d24 <parseval+0xf4>
9fc06d18:	010a0018 	mult	t0,t2
9fc06d1c:	00084500 	sll	t0,t0,0x14
9fc06d20:	010a0018 	mult	t0,t2
9fc06d24:	00001012 	mflo	v0
9fc06d28:	03e00008 	jr	ra
9fc06d2c:	00000000 	nop
9fc06d30:	24850001 	addiu	a1,a0,1
9fc06d34:	80a70000 	lb	a3,0(a1)
9fc06d38:	24030030 	li	v1,48
9fc06d3c:	14e3ffc3 	bne	a3,v1,9fc06c4c <parseval+0x1c>
9fc06d40:	240affff 	li	t2,-1
9fc06d44:	80a60001 	lb	a2,1(a1)
9fc06d48:	24040078 	li	a0,120
9fc06d4c:	14c4ffc0 	bne	a2,a0,9fc06c50 <parseval+0x20>
9fc06d50:	24ebffd0 	addiu	t3,a3,-48
9fc06d54:	24a40002 	addiu	a0,a1,2
9fc06d58:	00004021 	move	t0,zero
9fc06d5c:	80870000 	lb	a3,0(a0)
9fc06d60:	00084900 	sll	t1,t0,0x4
9fc06d64:	30e500ff 	andi	a1,a3,0xff
9fc06d68:	24b8ffd0 	addiu	t8,a1,-48
9fc06d6c:	24afff9f 	addiu	t7,a1,-97
9fc06d70:	330e00ff 	andi	t6,t8,0xff
9fc06d74:	24e5ffd0 	addiu	a1,a3,-48
9fc06d78:	31ed00ff 	andi	t5,t7,0xff
9fc06d7c:	2dcc000a 	sltiu	t4,t6,10
9fc06d80:	28a6000a 	slti	a2,a1,10
9fc06d84:	24840001 	addiu	a0,a0,1
9fc06d88:	15800003 	bnez	t4,9fc06d98 <parseval+0x168>
9fc06d8c:	2da30006 	sltiu	v1,t5,6
9fc06d90:	1060ffde 	beqz	v1,9fc06d0c <parseval+0xdc>
9fc06d94:	240e004b 	li	t6,75
9fc06d98:	14c00002 	bnez	a2,9fc06da4 <parseval+0x174>
9fc06d9c:	00000000 	nop
9fc06da0:	24e5ffa9 	addiu	a1,a3,-87
9fc06da4:	0bf01b57 	j	9fc06d5c <parseval+0x12c>
9fc06da8:	00a94021 	addu	t0,a1,t1
9fc06dac:	00000000 	nop
9fc06db0:	00084280 	sll	t0,t0,0xa
9fc06db4:	010a0018 	mult	t0,t2
9fc06db8:	00001012 	mflo	v0
9fc06dbc:	03e00008 	jr	ra
9fc06dc0:	00000000 	nop
	...

9fc06dd0 <crcu8>:
crcu8():
9fc06dd0:	30a5ffff 	andi	a1,a1,0xffff
9fc06dd4:	308400ff 	andi	a0,a0,0xff
9fc06dd8:	00853026 	xor	a2,a0,a1
9fc06ddc:	38a74002 	xori	a3,a1,0x4002
9fc06de0:	00071042 	srl	v0,a3,0x1
9fc06de4:	30c30001 	andi	v1,a2,0x1
9fc06de8:	00042042 	srl	a0,a0,0x1
9fc06dec:	34468000 	ori	a2,v0,0x8000
9fc06df0:	14600043 	bnez	v1,9fc06f00 <crcu8+0x130>
9fc06df4:	00051042 	srl	v0,a1,0x1
9fc06df8:	00402821 	move	a1,v0
9fc06dfc:	00855026 	xor	t2,a0,a1
9fc06e00:	38ab4002 	xori	t3,a1,0x4002
9fc06e04:	000b4842 	srl	t1,t3,0x1
9fc06e08:	31480001 	andi	t0,t2,0x1
9fc06e0c:	00042042 	srl	a0,a0,0x1
9fc06e10:	35268000 	ori	a2,t1,0x8000
9fc06e14:	15000043 	bnez	t0,9fc06f24 <crcu8+0x154>
9fc06e18:	00051042 	srl	v0,a1,0x1
9fc06e1c:	00402821 	move	a1,v0
9fc06e20:	00857026 	xor	t6,a0,a1
9fc06e24:	38af4002 	xori	t7,a1,0x4002
9fc06e28:	000f6842 	srl	t5,t7,0x1
9fc06e2c:	31cc0001 	andi	t4,t6,0x1
9fc06e30:	00042042 	srl	a0,a0,0x1
9fc06e34:	35a68000 	ori	a2,t5,0x8000
9fc06e38:	15800043 	bnez	t4,9fc06f48 <crcu8+0x178>
9fc06e3c:	00051042 	srl	v0,a1,0x1
9fc06e40:	00402821 	move	a1,v0
9fc06e44:	00851826 	xor	v1,a0,a1
9fc06e48:	38a24002 	xori	v0,a1,0x4002
9fc06e4c:	0002c842 	srl	t9,v0,0x1
9fc06e50:	30780001 	andi	t8,v1,0x1
9fc06e54:	00042042 	srl	a0,a0,0x1
9fc06e58:	37268000 	ori	a2,t9,0x8000
9fc06e5c:	17000043 	bnez	t8,9fc06f6c <crcu8+0x19c>
9fc06e60:	00051042 	srl	v0,a1,0x1
9fc06e64:	00402821 	move	a1,v0
9fc06e68:	00853026 	xor	a2,a0,a1
9fc06e6c:	38a94002 	xori	t1,a1,0x4002
9fc06e70:	00094042 	srl	t0,t1,0x1
9fc06e74:	30c70001 	andi	a3,a2,0x1
9fc06e78:	00042042 	srl	a0,a0,0x1
9fc06e7c:	35068000 	ori	a2,t0,0x8000
9fc06e80:	14e00043 	bnez	a3,9fc06f90 <crcu8+0x1c0>
9fc06e84:	00051042 	srl	v0,a1,0x1
9fc06e88:	00402821 	move	a1,v0
9fc06e8c:	00856026 	xor	t4,a0,a1
9fc06e90:	38ad4002 	xori	t5,a1,0x4002
9fc06e94:	000d5842 	srl	t3,t5,0x1
9fc06e98:	318a0001 	andi	t2,t4,0x1
9fc06e9c:	00042042 	srl	a0,a0,0x1
9fc06ea0:	35668000 	ori	a2,t3,0x8000
9fc06ea4:	15400043 	bnez	t2,9fc06fb4 <crcu8+0x1e4>
9fc06ea8:	00051042 	srl	v0,a1,0x1
9fc06eac:	00402821 	move	a1,v0
9fc06eb0:	0085c026 	xor	t8,a0,a1
9fc06eb4:	38b94002 	xori	t9,a1,0x4002
9fc06eb8:	00197842 	srl	t7,t9,0x1
9fc06ebc:	330e0001 	andi	t6,t8,0x1
9fc06ec0:	35e68000 	ori	a2,t7,0x8000
9fc06ec4:	15c00043 	bnez	t6,9fc06fd4 <crcu8+0x204>
9fc06ec8:	00051042 	srl	v0,a1,0x1
9fc06ecc:	00402821 	move	a1,v0
9fc06ed0:	38a74002 	xori	a3,a1,0x4002
9fc06ed4:	00071042 	srl	v0,a3,0x1
9fc06ed8:	00042042 	srl	a0,a0,0x1
9fc06edc:	30a30001 	andi	v1,a1,0x1
9fc06ee0:	34468000 	ori	a2,v0,0x8000
9fc06ee4:	14640043 	bne	v1,a0,9fc06ff4 <crcu8+0x224>
9fc06ee8:	00051042 	srl	v0,a1,0x1
9fc06eec:	03e00008 	jr	ra
9fc06ef0:	3042ffff 	andi	v0,v0,0xffff
	...
9fc06f00:	00c02821 	move	a1,a2
9fc06f04:	00855026 	xor	t2,a0,a1
9fc06f08:	38ab4002 	xori	t3,a1,0x4002
9fc06f0c:	000b4842 	srl	t1,t3,0x1
9fc06f10:	31480001 	andi	t0,t2,0x1
9fc06f14:	00042042 	srl	a0,a0,0x1
9fc06f18:	35268000 	ori	a2,t1,0x8000
9fc06f1c:	1100ffbf 	beqz	t0,9fc06e1c <crcu8+0x4c>
9fc06f20:	00051042 	srl	v0,a1,0x1
9fc06f24:	00c02821 	move	a1,a2
9fc06f28:	00857026 	xor	t6,a0,a1
9fc06f2c:	38af4002 	xori	t7,a1,0x4002
9fc06f30:	000f6842 	srl	t5,t7,0x1
9fc06f34:	31cc0001 	andi	t4,t6,0x1
9fc06f38:	00042042 	srl	a0,a0,0x1
9fc06f3c:	35a68000 	ori	a2,t5,0x8000
9fc06f40:	1180ffbf 	beqz	t4,9fc06e40 <crcu8+0x70>
9fc06f44:	00051042 	srl	v0,a1,0x1
9fc06f48:	00c02821 	move	a1,a2
9fc06f4c:	00851826 	xor	v1,a0,a1
9fc06f50:	38a24002 	xori	v0,a1,0x4002
9fc06f54:	0002c842 	srl	t9,v0,0x1
9fc06f58:	30780001 	andi	t8,v1,0x1
9fc06f5c:	00042042 	srl	a0,a0,0x1
9fc06f60:	37268000 	ori	a2,t9,0x8000
9fc06f64:	1300ffbf 	beqz	t8,9fc06e64 <crcu8+0x94>
9fc06f68:	00051042 	srl	v0,a1,0x1
9fc06f6c:	00c02821 	move	a1,a2
9fc06f70:	00853026 	xor	a2,a0,a1
9fc06f74:	38a94002 	xori	t1,a1,0x4002
9fc06f78:	00094042 	srl	t0,t1,0x1
9fc06f7c:	30c70001 	andi	a3,a2,0x1
9fc06f80:	00042042 	srl	a0,a0,0x1
9fc06f84:	35068000 	ori	a2,t0,0x8000
9fc06f88:	10e0ffbf 	beqz	a3,9fc06e88 <crcu8+0xb8>
9fc06f8c:	00051042 	srl	v0,a1,0x1
9fc06f90:	00c02821 	move	a1,a2
9fc06f94:	00856026 	xor	t4,a0,a1
9fc06f98:	38ad4002 	xori	t5,a1,0x4002
9fc06f9c:	000d5842 	srl	t3,t5,0x1
9fc06fa0:	318a0001 	andi	t2,t4,0x1
9fc06fa4:	00042042 	srl	a0,a0,0x1
9fc06fa8:	35668000 	ori	a2,t3,0x8000
9fc06fac:	1140ffbf 	beqz	t2,9fc06eac <crcu8+0xdc>
9fc06fb0:	00051042 	srl	v0,a1,0x1
9fc06fb4:	00c02821 	move	a1,a2
9fc06fb8:	0085c026 	xor	t8,a0,a1
9fc06fbc:	38b94002 	xori	t9,a1,0x4002
9fc06fc0:	00197842 	srl	t7,t9,0x1
9fc06fc4:	330e0001 	andi	t6,t8,0x1
9fc06fc8:	35e68000 	ori	a2,t7,0x8000
9fc06fcc:	11c0ffbf 	beqz	t6,9fc06ecc <crcu8+0xfc>
9fc06fd0:	00051042 	srl	v0,a1,0x1
9fc06fd4:	00c02821 	move	a1,a2
9fc06fd8:	38a74002 	xori	a3,a1,0x4002
9fc06fdc:	00071042 	srl	v0,a3,0x1
9fc06fe0:	00042042 	srl	a0,a0,0x1
9fc06fe4:	30a30001 	andi	v1,a1,0x1
9fc06fe8:	34468000 	ori	a2,v0,0x8000
9fc06fec:	1064ffbf 	beq	v1,a0,9fc06eec <crcu8+0x11c>
9fc06ff0:	00051042 	srl	v0,a1,0x1
9fc06ff4:	00c01021 	move	v0,a2
9fc06ff8:	03e00008 	jr	ra
9fc06ffc:	3042ffff 	andi	v0,v0,0xffff

9fc07000 <check_data_types>:
check_data_types():
9fc07000:	03e00008 	jr	ra
9fc07004:	00001021 	move	v0,zero
	...

9fc07010 <crcu16>:
crcu16():
9fc07010:	3087ffff 	andi	a3,a0,0xffff
9fc07014:	30a5ffff 	andi	a1,a1,0xffff
9fc07018:	30e400ff 	andi	a0,a3,0xff
9fc0701c:	00853026 	xor	a2,a0,a1
9fc07020:	38a84002 	xori	t0,a1,0x4002
9fc07024:	00081042 	srl	v0,t0,0x1
9fc07028:	30c30001 	andi	v1,a2,0x1
9fc0702c:	00042042 	srl	a0,a0,0x1
9fc07030:	34468000 	ori	a2,v0,0x8000
9fc07034:	1460008a 	bnez	v1,9fc07260 <crcu16+0x250>
9fc07038:	00051042 	srl	v0,a1,0x1
9fc0703c:	00402821 	move	a1,v0
9fc07040:	00855826 	xor	t3,a0,a1
9fc07044:	38ac4002 	xori	t4,a1,0x4002
9fc07048:	000c5042 	srl	t2,t4,0x1
9fc0704c:	31690001 	andi	t1,t3,0x1
9fc07050:	00042042 	srl	a0,a0,0x1
9fc07054:	35468000 	ori	a2,t2,0x8000
9fc07058:	1520008a 	bnez	t1,9fc07284 <crcu16+0x274>
9fc0705c:	00051042 	srl	v0,a1,0x1
9fc07060:	00402821 	move	a1,v0
9fc07064:	00857826 	xor	t7,a0,a1
9fc07068:	38b84002 	xori	t8,a1,0x4002
9fc0706c:	00187042 	srl	t6,t8,0x1
9fc07070:	31ed0001 	andi	t5,t7,0x1
9fc07074:	00042042 	srl	a0,a0,0x1
9fc07078:	35c68000 	ori	a2,t6,0x8000
9fc0707c:	15a0008a 	bnez	t5,9fc072a8 <crcu16+0x298>
9fc07080:	00051042 	srl	v0,a1,0x1
9fc07084:	00402821 	move	a1,v0
9fc07088:	00851826 	xor	v1,a0,a1
9fc0708c:	38a64002 	xori	a2,a1,0x4002
9fc07090:	00061042 	srl	v0,a2,0x1
9fc07094:	30790001 	andi	t9,v1,0x1
9fc07098:	34468000 	ori	a2,v0,0x8000
9fc0709c:	00042042 	srl	a0,a0,0x1
9fc070a0:	1720008a 	bnez	t9,9fc072cc <crcu16+0x2bc>
9fc070a4:	00051042 	srl	v0,a1,0x1
9fc070a8:	00402821 	move	a1,v0
9fc070ac:	00855026 	xor	t2,a0,a1
9fc070b0:	38ab4002 	xori	t3,a1,0x4002
9fc070b4:	000b4842 	srl	t1,t3,0x1
9fc070b8:	31480001 	andi	t0,t2,0x1
9fc070bc:	00042042 	srl	a0,a0,0x1
9fc070c0:	35268000 	ori	a2,t1,0x8000
9fc070c4:	1500008a 	bnez	t0,9fc072f0 <crcu16+0x2e0>
9fc070c8:	00051042 	srl	v0,a1,0x1
9fc070cc:	00402821 	move	a1,v0
9fc070d0:	00857026 	xor	t6,a0,a1
9fc070d4:	38af4002 	xori	t7,a1,0x4002
9fc070d8:	000f6842 	srl	t5,t7,0x1
9fc070dc:	31cc0001 	andi	t4,t6,0x1
9fc070e0:	00042042 	srl	a0,a0,0x1
9fc070e4:	35a68000 	ori	a2,t5,0x8000
9fc070e8:	1580008a 	bnez	t4,9fc07314 <crcu16+0x304>
9fc070ec:	00051042 	srl	v0,a1,0x1
9fc070f0:	00402821 	move	a1,v0
9fc070f4:	38a24002 	xori	v0,a1,0x4002
9fc070f8:	00851826 	xor	v1,a0,a1
9fc070fc:	0002c842 	srl	t9,v0,0x1
9fc07100:	30780001 	andi	t8,v1,0x1
9fc07104:	37268000 	ori	a2,t9,0x8000
9fc07108:	1700008a 	bnez	t8,9fc07334 <crcu16+0x324>
9fc0710c:	00051042 	srl	v0,a1,0x1
9fc07110:	00402821 	move	a1,v0
9fc07114:	38a64002 	xori	a2,a1,0x4002
9fc07118:	00064842 	srl	t1,a2,0x1
9fc0711c:	00042042 	srl	a0,a0,0x1
9fc07120:	30a80001 	andi	t0,a1,0x1
9fc07124:	35268000 	ori	a2,t1,0x8000
9fc07128:	1504008a 	bne	t0,a0,9fc07354 <crcu16+0x344>
9fc0712c:	00051042 	srl	v0,a1,0x1
9fc07130:	00402821 	move	a1,v0
9fc07134:	00075a02 	srl	t3,a3,0x8
9fc07138:	01656026 	xor	t4,t3,a1
9fc0713c:	38ad4002 	xori	t5,a1,0x4002
9fc07140:	000d5042 	srl	t2,t5,0x1
9fc07144:	31870001 	andi	a3,t4,0x1
9fc07148:	000b2042 	srl	a0,t3,0x1
9fc0714c:	35468000 	ori	a2,t2,0x8000
9fc07150:	14e0008a 	bnez	a3,9fc0737c <crcu16+0x36c>
9fc07154:	00051042 	srl	v0,a1,0x1
9fc07158:	00402821 	move	a1,v0
9fc0715c:	0085c026 	xor	t8,a0,a1
9fc07160:	38b94002 	xori	t9,a1,0x4002
9fc07164:	00197842 	srl	t7,t9,0x1
9fc07168:	330e0001 	andi	t6,t8,0x1
9fc0716c:	00042042 	srl	a0,a0,0x1
9fc07170:	35e68000 	ori	a2,t7,0x8000
9fc07174:	15c0008a 	bnez	t6,9fc073a0 <crcu16+0x390>
9fc07178:	00051042 	srl	v0,a1,0x1
9fc0717c:	00402821 	move	a1,v0
9fc07180:	00854026 	xor	t0,a0,a1
9fc07184:	38a94002 	xori	t1,a1,0x4002
9fc07188:	00091042 	srl	v0,t1,0x1
9fc0718c:	31030001 	andi	v1,t0,0x1
9fc07190:	34468000 	ori	a2,v0,0x8000
9fc07194:	00042042 	srl	a0,a0,0x1
9fc07198:	1460008a 	bnez	v1,9fc073c4 <crcu16+0x3b4>
9fc0719c:	00051042 	srl	v0,a1,0x1
9fc071a0:	00402821 	move	a1,v0
9fc071a4:	00853026 	xor	a2,a0,a1
9fc071a8:	38ab4002 	xori	t3,a1,0x4002
9fc071ac:	000b5042 	srl	t2,t3,0x1
9fc071b0:	30c70001 	andi	a3,a2,0x1
9fc071b4:	00042042 	srl	a0,a0,0x1
9fc071b8:	35468000 	ori	a2,t2,0x8000
9fc071bc:	14e0008a 	bnez	a3,9fc073e8 <crcu16+0x3d8>
9fc071c0:	00051042 	srl	v0,a1,0x1
9fc071c4:	00402821 	move	a1,v0
9fc071c8:	00857026 	xor	t6,a0,a1
9fc071cc:	38af4002 	xori	t7,a1,0x4002
9fc071d0:	000f6842 	srl	t5,t7,0x1
9fc071d4:	31cc0001 	andi	t4,t6,0x1
9fc071d8:	00042042 	srl	a0,a0,0x1
9fc071dc:	35a68000 	ori	a2,t5,0x8000
9fc071e0:	1580008a 	bnez	t4,9fc0740c <crcu16+0x3fc>
9fc071e4:	00051042 	srl	v0,a1,0x1
9fc071e8:	00402821 	move	a1,v0
9fc071ec:	00851826 	xor	v1,a0,a1
9fc071f0:	38a24002 	xori	v0,a1,0x4002
9fc071f4:	0002c842 	srl	t9,v0,0x1
9fc071f8:	30780001 	andi	t8,v1,0x1
9fc071fc:	00042042 	srl	a0,a0,0x1
9fc07200:	37268000 	ori	a2,t9,0x8000
9fc07204:	1700008a 	bnez	t8,9fc07430 <crcu16+0x420>
9fc07208:	00051042 	srl	v0,a1,0x1
9fc0720c:	00402821 	move	a1,v0
9fc07210:	00853826 	xor	a3,a0,a1
9fc07214:	38aa4002 	xori	t2,a1,0x4002
9fc07218:	000a4842 	srl	t1,t2,0x1
9fc0721c:	30e80001 	andi	t0,a3,0x1
9fc07220:	35268000 	ori	a2,t1,0x8000
9fc07224:	1500008a 	bnez	t0,9fc07450 <crcu16+0x440>
9fc07228:	00051042 	srl	v0,a1,0x1
9fc0722c:	00402821 	move	a1,v0
9fc07230:	38a64002 	xori	a2,a1,0x4002
9fc07234:	00066042 	srl	t4,a2,0x1
9fc07238:	00042042 	srl	a0,a0,0x1
9fc0723c:	30ab0001 	andi	t3,a1,0x1
9fc07240:	35868000 	ori	a2,t4,0x8000
9fc07244:	1564008a 	bne	t3,a0,9fc07470 <crcu16+0x460>
9fc07248:	00051042 	srl	v0,a1,0x1
9fc0724c:	03e00008 	jr	ra
9fc07250:	3042ffff 	andi	v0,v0,0xffff
	...
9fc07260:	00c02821 	move	a1,a2
9fc07264:	00855826 	xor	t3,a0,a1
9fc07268:	38ac4002 	xori	t4,a1,0x4002
9fc0726c:	000c5042 	srl	t2,t4,0x1
9fc07270:	31690001 	andi	t1,t3,0x1
9fc07274:	00042042 	srl	a0,a0,0x1
9fc07278:	35468000 	ori	a2,t2,0x8000
9fc0727c:	1120ff78 	beqz	t1,9fc07060 <crcu16+0x50>
9fc07280:	00051042 	srl	v0,a1,0x1
9fc07284:	00c02821 	move	a1,a2
9fc07288:	00857826 	xor	t7,a0,a1
9fc0728c:	38b84002 	xori	t8,a1,0x4002
9fc07290:	00187042 	srl	t6,t8,0x1
9fc07294:	31ed0001 	andi	t5,t7,0x1
9fc07298:	00042042 	srl	a0,a0,0x1
9fc0729c:	35c68000 	ori	a2,t6,0x8000
9fc072a0:	11a0ff78 	beqz	t5,9fc07084 <crcu16+0x74>
9fc072a4:	00051042 	srl	v0,a1,0x1
9fc072a8:	00c02821 	move	a1,a2
9fc072ac:	00851826 	xor	v1,a0,a1
9fc072b0:	38a64002 	xori	a2,a1,0x4002
9fc072b4:	00061042 	srl	v0,a2,0x1
9fc072b8:	30790001 	andi	t9,v1,0x1
9fc072bc:	34468000 	ori	a2,v0,0x8000
9fc072c0:	00042042 	srl	a0,a0,0x1
9fc072c4:	1320ff78 	beqz	t9,9fc070a8 <crcu16+0x98>
9fc072c8:	00051042 	srl	v0,a1,0x1
9fc072cc:	00c02821 	move	a1,a2
9fc072d0:	00855026 	xor	t2,a0,a1
9fc072d4:	38ab4002 	xori	t3,a1,0x4002
9fc072d8:	000b4842 	srl	t1,t3,0x1
9fc072dc:	31480001 	andi	t0,t2,0x1
9fc072e0:	00042042 	srl	a0,a0,0x1
9fc072e4:	35268000 	ori	a2,t1,0x8000
9fc072e8:	1100ff78 	beqz	t0,9fc070cc <crcu16+0xbc>
9fc072ec:	00051042 	srl	v0,a1,0x1
9fc072f0:	00c02821 	move	a1,a2
9fc072f4:	00857026 	xor	t6,a0,a1
9fc072f8:	38af4002 	xori	t7,a1,0x4002
9fc072fc:	000f6842 	srl	t5,t7,0x1
9fc07300:	31cc0001 	andi	t4,t6,0x1
9fc07304:	00042042 	srl	a0,a0,0x1
9fc07308:	35a68000 	ori	a2,t5,0x8000
9fc0730c:	1180ff78 	beqz	t4,9fc070f0 <crcu16+0xe0>
9fc07310:	00051042 	srl	v0,a1,0x1
9fc07314:	00c02821 	move	a1,a2
9fc07318:	38a24002 	xori	v0,a1,0x4002
9fc0731c:	00851826 	xor	v1,a0,a1
9fc07320:	0002c842 	srl	t9,v0,0x1
9fc07324:	30780001 	andi	t8,v1,0x1
9fc07328:	37268000 	ori	a2,t9,0x8000
9fc0732c:	1300ff78 	beqz	t8,9fc07110 <crcu16+0x100>
9fc07330:	00051042 	srl	v0,a1,0x1
9fc07334:	00c02821 	move	a1,a2
9fc07338:	38a64002 	xori	a2,a1,0x4002
9fc0733c:	00064842 	srl	t1,a2,0x1
9fc07340:	00042042 	srl	a0,a0,0x1
9fc07344:	30a80001 	andi	t0,a1,0x1
9fc07348:	35268000 	ori	a2,t1,0x8000
9fc0734c:	1104ff78 	beq	t0,a0,9fc07130 <crcu16+0x120>
9fc07350:	00051042 	srl	v0,a1,0x1
9fc07354:	00c02821 	move	a1,a2
9fc07358:	00075a02 	srl	t3,a3,0x8
9fc0735c:	01656026 	xor	t4,t3,a1
9fc07360:	38ad4002 	xori	t5,a1,0x4002
9fc07364:	000d5042 	srl	t2,t5,0x1
9fc07368:	31870001 	andi	a3,t4,0x1
9fc0736c:	000b2042 	srl	a0,t3,0x1
9fc07370:	35468000 	ori	a2,t2,0x8000
9fc07374:	10e0ff78 	beqz	a3,9fc07158 <crcu16+0x148>
9fc07378:	00051042 	srl	v0,a1,0x1
9fc0737c:	00c02821 	move	a1,a2
9fc07380:	0085c026 	xor	t8,a0,a1
9fc07384:	38b94002 	xori	t9,a1,0x4002
9fc07388:	00197842 	srl	t7,t9,0x1
9fc0738c:	330e0001 	andi	t6,t8,0x1
9fc07390:	00042042 	srl	a0,a0,0x1
9fc07394:	35e68000 	ori	a2,t7,0x8000
9fc07398:	11c0ff78 	beqz	t6,9fc0717c <crcu16+0x16c>
9fc0739c:	00051042 	srl	v0,a1,0x1
9fc073a0:	00c02821 	move	a1,a2
9fc073a4:	00854026 	xor	t0,a0,a1
9fc073a8:	38a94002 	xori	t1,a1,0x4002
9fc073ac:	00091042 	srl	v0,t1,0x1
9fc073b0:	31030001 	andi	v1,t0,0x1
9fc073b4:	34468000 	ori	a2,v0,0x8000
9fc073b8:	00042042 	srl	a0,a0,0x1
9fc073bc:	1060ff78 	beqz	v1,9fc071a0 <crcu16+0x190>
9fc073c0:	00051042 	srl	v0,a1,0x1
9fc073c4:	00c02821 	move	a1,a2
9fc073c8:	00853026 	xor	a2,a0,a1
9fc073cc:	38ab4002 	xori	t3,a1,0x4002
9fc073d0:	000b5042 	srl	t2,t3,0x1
9fc073d4:	30c70001 	andi	a3,a2,0x1
9fc073d8:	00042042 	srl	a0,a0,0x1
9fc073dc:	35468000 	ori	a2,t2,0x8000
9fc073e0:	10e0ff78 	beqz	a3,9fc071c4 <crcu16+0x1b4>
9fc073e4:	00051042 	srl	v0,a1,0x1
9fc073e8:	00c02821 	move	a1,a2
9fc073ec:	00857026 	xor	t6,a0,a1
9fc073f0:	38af4002 	xori	t7,a1,0x4002
9fc073f4:	000f6842 	srl	t5,t7,0x1
9fc073f8:	31cc0001 	andi	t4,t6,0x1
9fc073fc:	00042042 	srl	a0,a0,0x1
9fc07400:	35a68000 	ori	a2,t5,0x8000
9fc07404:	1180ff78 	beqz	t4,9fc071e8 <crcu16+0x1d8>
9fc07408:	00051042 	srl	v0,a1,0x1
9fc0740c:	00c02821 	move	a1,a2
9fc07410:	00851826 	xor	v1,a0,a1
9fc07414:	38a24002 	xori	v0,a1,0x4002
9fc07418:	0002c842 	srl	t9,v0,0x1
9fc0741c:	30780001 	andi	t8,v1,0x1
9fc07420:	00042042 	srl	a0,a0,0x1
9fc07424:	37268000 	ori	a2,t9,0x8000
9fc07428:	1300ff78 	beqz	t8,9fc0720c <crcu16+0x1fc>
9fc0742c:	00051042 	srl	v0,a1,0x1
9fc07430:	00c02821 	move	a1,a2
9fc07434:	00853826 	xor	a3,a0,a1
9fc07438:	38aa4002 	xori	t2,a1,0x4002
9fc0743c:	000a4842 	srl	t1,t2,0x1
9fc07440:	30e80001 	andi	t0,a3,0x1
9fc07444:	35268000 	ori	a2,t1,0x8000
9fc07448:	1100ff78 	beqz	t0,9fc0722c <crcu16+0x21c>
9fc0744c:	00051042 	srl	v0,a1,0x1
9fc07450:	00c02821 	move	a1,a2
9fc07454:	38a64002 	xori	a2,a1,0x4002
9fc07458:	00066042 	srl	t4,a2,0x1
9fc0745c:	00042042 	srl	a0,a0,0x1
9fc07460:	30ab0001 	andi	t3,a1,0x1
9fc07464:	35868000 	ori	a2,t4,0x8000
9fc07468:	1164ff78 	beq	t3,a0,9fc0724c <crcu16+0x23c>
9fc0746c:	00051042 	srl	v0,a1,0x1
9fc07470:	00c01021 	move	v0,a2
9fc07474:	03e00008 	jr	ra
9fc07478:	3042ffff 	andi	v0,v0,0xffff
9fc0747c:	00000000 	nop

9fc07480 <crc16>:
crc16():
9fc07480:	3087ffff 	andi	a3,a0,0xffff
9fc07484:	30a5ffff 	andi	a1,a1,0xffff
9fc07488:	30e400ff 	andi	a0,a3,0xff
9fc0748c:	00a43026 	xor	a2,a1,a0
9fc07490:	38a84002 	xori	t0,a1,0x4002
9fc07494:	00081042 	srl	v0,t0,0x1
9fc07498:	30c30001 	andi	v1,a2,0x1
9fc0749c:	00042042 	srl	a0,a0,0x1
9fc074a0:	34468000 	ori	a2,v0,0x8000
9fc074a4:	1460008a 	bnez	v1,9fc076d0 <crc16+0x250>
9fc074a8:	00051042 	srl	v0,a1,0x1
9fc074ac:	00402821 	move	a1,v0
9fc074b0:	00855826 	xor	t3,a0,a1
9fc074b4:	38ac4002 	xori	t4,a1,0x4002
9fc074b8:	000c5042 	srl	t2,t4,0x1
9fc074bc:	31690001 	andi	t1,t3,0x1
9fc074c0:	00042042 	srl	a0,a0,0x1
9fc074c4:	35468000 	ori	a2,t2,0x8000
9fc074c8:	1520008a 	bnez	t1,9fc076f4 <crc16+0x274>
9fc074cc:	00051042 	srl	v0,a1,0x1
9fc074d0:	00402821 	move	a1,v0
9fc074d4:	00857826 	xor	t7,a0,a1
9fc074d8:	38b84002 	xori	t8,a1,0x4002
9fc074dc:	00187042 	srl	t6,t8,0x1
9fc074e0:	31ed0001 	andi	t5,t7,0x1
9fc074e4:	00042042 	srl	a0,a0,0x1
9fc074e8:	35c68000 	ori	a2,t6,0x8000
9fc074ec:	15a0008a 	bnez	t5,9fc07718 <crc16+0x298>
9fc074f0:	00051042 	srl	v0,a1,0x1
9fc074f4:	00402821 	move	a1,v0
9fc074f8:	00851826 	xor	v1,a0,a1
9fc074fc:	38a64002 	xori	a2,a1,0x4002
9fc07500:	00061042 	srl	v0,a2,0x1
9fc07504:	30790001 	andi	t9,v1,0x1
9fc07508:	34468000 	ori	a2,v0,0x8000
9fc0750c:	00042042 	srl	a0,a0,0x1
9fc07510:	1720008a 	bnez	t9,9fc0773c <crc16+0x2bc>
9fc07514:	00051042 	srl	v0,a1,0x1
9fc07518:	00402821 	move	a1,v0
9fc0751c:	00855026 	xor	t2,a0,a1
9fc07520:	38ab4002 	xori	t3,a1,0x4002
9fc07524:	000b4842 	srl	t1,t3,0x1
9fc07528:	31480001 	andi	t0,t2,0x1
9fc0752c:	00042042 	srl	a0,a0,0x1
9fc07530:	35268000 	ori	a2,t1,0x8000
9fc07534:	1500008a 	bnez	t0,9fc07760 <crc16+0x2e0>
9fc07538:	00051042 	srl	v0,a1,0x1
9fc0753c:	00402821 	move	a1,v0
9fc07540:	00857026 	xor	t6,a0,a1
9fc07544:	38af4002 	xori	t7,a1,0x4002
9fc07548:	000f6842 	srl	t5,t7,0x1
9fc0754c:	31cc0001 	andi	t4,t6,0x1
9fc07550:	00042042 	srl	a0,a0,0x1
9fc07554:	35a68000 	ori	a2,t5,0x8000
9fc07558:	1580008a 	bnez	t4,9fc07784 <crc16+0x304>
9fc0755c:	00051042 	srl	v0,a1,0x1
9fc07560:	00402821 	move	a1,v0
9fc07564:	38a24002 	xori	v0,a1,0x4002
9fc07568:	00851826 	xor	v1,a0,a1
9fc0756c:	0002c842 	srl	t9,v0,0x1
9fc07570:	30780001 	andi	t8,v1,0x1
9fc07574:	37268000 	ori	a2,t9,0x8000
9fc07578:	1700008a 	bnez	t8,9fc077a4 <crc16+0x324>
9fc0757c:	00051042 	srl	v0,a1,0x1
9fc07580:	00402821 	move	a1,v0
9fc07584:	38a64002 	xori	a2,a1,0x4002
9fc07588:	00064842 	srl	t1,a2,0x1
9fc0758c:	00042042 	srl	a0,a0,0x1
9fc07590:	30a80001 	andi	t0,a1,0x1
9fc07594:	35268000 	ori	a2,t1,0x8000
9fc07598:	1504008a 	bne	t0,a0,9fc077c4 <crc16+0x344>
9fc0759c:	00051042 	srl	v0,a1,0x1
9fc075a0:	00402821 	move	a1,v0
9fc075a4:	00075a02 	srl	t3,a3,0x8
9fc075a8:	01656026 	xor	t4,t3,a1
9fc075ac:	38ad4002 	xori	t5,a1,0x4002
9fc075b0:	000d5042 	srl	t2,t5,0x1
9fc075b4:	31870001 	andi	a3,t4,0x1
9fc075b8:	000b2042 	srl	a0,t3,0x1
9fc075bc:	35468000 	ori	a2,t2,0x8000
9fc075c0:	14e0008a 	bnez	a3,9fc077ec <crc16+0x36c>
9fc075c4:	00051042 	srl	v0,a1,0x1
9fc075c8:	00402821 	move	a1,v0
9fc075cc:	0085c026 	xor	t8,a0,a1
9fc075d0:	38b94002 	xori	t9,a1,0x4002
9fc075d4:	00197842 	srl	t7,t9,0x1
9fc075d8:	330e0001 	andi	t6,t8,0x1
9fc075dc:	00042042 	srl	a0,a0,0x1
9fc075e0:	35e68000 	ori	a2,t7,0x8000
9fc075e4:	15c0008a 	bnez	t6,9fc07810 <crc16+0x390>
9fc075e8:	00051042 	srl	v0,a1,0x1
9fc075ec:	00402821 	move	a1,v0
9fc075f0:	00854026 	xor	t0,a0,a1
9fc075f4:	38a94002 	xori	t1,a1,0x4002
9fc075f8:	00091042 	srl	v0,t1,0x1
9fc075fc:	31030001 	andi	v1,t0,0x1
9fc07600:	34468000 	ori	a2,v0,0x8000
9fc07604:	00042042 	srl	a0,a0,0x1
9fc07608:	1460008a 	bnez	v1,9fc07834 <crc16+0x3b4>
9fc0760c:	00051042 	srl	v0,a1,0x1
9fc07610:	00402821 	move	a1,v0
9fc07614:	00853026 	xor	a2,a0,a1
9fc07618:	38ab4002 	xori	t3,a1,0x4002
9fc0761c:	000b5042 	srl	t2,t3,0x1
9fc07620:	30c70001 	andi	a3,a2,0x1
9fc07624:	00042042 	srl	a0,a0,0x1
9fc07628:	35468000 	ori	a2,t2,0x8000
9fc0762c:	14e0008a 	bnez	a3,9fc07858 <crc16+0x3d8>
9fc07630:	00051042 	srl	v0,a1,0x1
9fc07634:	00402821 	move	a1,v0
9fc07638:	00857026 	xor	t6,a0,a1
9fc0763c:	38af4002 	xori	t7,a1,0x4002
9fc07640:	000f6842 	srl	t5,t7,0x1
9fc07644:	31cc0001 	andi	t4,t6,0x1
9fc07648:	00042042 	srl	a0,a0,0x1
9fc0764c:	35a68000 	ori	a2,t5,0x8000
9fc07650:	1580008a 	bnez	t4,9fc0787c <crc16+0x3fc>
9fc07654:	00051042 	srl	v0,a1,0x1
9fc07658:	00402821 	move	a1,v0
9fc0765c:	00851826 	xor	v1,a0,a1
9fc07660:	38a24002 	xori	v0,a1,0x4002
9fc07664:	0002c842 	srl	t9,v0,0x1
9fc07668:	30780001 	andi	t8,v1,0x1
9fc0766c:	00042042 	srl	a0,a0,0x1
9fc07670:	37268000 	ori	a2,t9,0x8000
9fc07674:	1700008a 	bnez	t8,9fc078a0 <crc16+0x420>
9fc07678:	00051042 	srl	v0,a1,0x1
9fc0767c:	00402821 	move	a1,v0
9fc07680:	00853826 	xor	a3,a0,a1
9fc07684:	38aa4002 	xori	t2,a1,0x4002
9fc07688:	000a4842 	srl	t1,t2,0x1
9fc0768c:	30e80001 	andi	t0,a3,0x1
9fc07690:	35268000 	ori	a2,t1,0x8000
9fc07694:	1500008a 	bnez	t0,9fc078c0 <crc16+0x440>
9fc07698:	00051042 	srl	v0,a1,0x1
9fc0769c:	00402821 	move	a1,v0
9fc076a0:	38a64002 	xori	a2,a1,0x4002
9fc076a4:	00066042 	srl	t4,a2,0x1
9fc076a8:	00042042 	srl	a0,a0,0x1
9fc076ac:	30ab0001 	andi	t3,a1,0x1
9fc076b0:	35868000 	ori	a2,t4,0x8000
9fc076b4:	1564008a 	bne	t3,a0,9fc078e0 <crc16+0x460>
9fc076b8:	00051042 	srl	v0,a1,0x1
9fc076bc:	03e00008 	jr	ra
9fc076c0:	3042ffff 	andi	v0,v0,0xffff
	...
9fc076d0:	00c02821 	move	a1,a2
9fc076d4:	00855826 	xor	t3,a0,a1
9fc076d8:	38ac4002 	xori	t4,a1,0x4002
9fc076dc:	000c5042 	srl	t2,t4,0x1
9fc076e0:	31690001 	andi	t1,t3,0x1
9fc076e4:	00042042 	srl	a0,a0,0x1
9fc076e8:	35468000 	ori	a2,t2,0x8000
9fc076ec:	1120ff78 	beqz	t1,9fc074d0 <crc16+0x50>
9fc076f0:	00051042 	srl	v0,a1,0x1
9fc076f4:	00c02821 	move	a1,a2
9fc076f8:	00857826 	xor	t7,a0,a1
9fc076fc:	38b84002 	xori	t8,a1,0x4002
9fc07700:	00187042 	srl	t6,t8,0x1
9fc07704:	31ed0001 	andi	t5,t7,0x1
9fc07708:	00042042 	srl	a0,a0,0x1
9fc0770c:	35c68000 	ori	a2,t6,0x8000
9fc07710:	11a0ff78 	beqz	t5,9fc074f4 <crc16+0x74>
9fc07714:	00051042 	srl	v0,a1,0x1
9fc07718:	00c02821 	move	a1,a2
9fc0771c:	00851826 	xor	v1,a0,a1
9fc07720:	38a64002 	xori	a2,a1,0x4002
9fc07724:	00061042 	srl	v0,a2,0x1
9fc07728:	30790001 	andi	t9,v1,0x1
9fc0772c:	34468000 	ori	a2,v0,0x8000
9fc07730:	00042042 	srl	a0,a0,0x1
9fc07734:	1320ff78 	beqz	t9,9fc07518 <crc16+0x98>
9fc07738:	00051042 	srl	v0,a1,0x1
9fc0773c:	00c02821 	move	a1,a2
9fc07740:	00855026 	xor	t2,a0,a1
9fc07744:	38ab4002 	xori	t3,a1,0x4002
9fc07748:	000b4842 	srl	t1,t3,0x1
9fc0774c:	31480001 	andi	t0,t2,0x1
9fc07750:	00042042 	srl	a0,a0,0x1
9fc07754:	35268000 	ori	a2,t1,0x8000
9fc07758:	1100ff78 	beqz	t0,9fc0753c <crc16+0xbc>
9fc0775c:	00051042 	srl	v0,a1,0x1
9fc07760:	00c02821 	move	a1,a2
9fc07764:	00857026 	xor	t6,a0,a1
9fc07768:	38af4002 	xori	t7,a1,0x4002
9fc0776c:	000f6842 	srl	t5,t7,0x1
9fc07770:	31cc0001 	andi	t4,t6,0x1
9fc07774:	00042042 	srl	a0,a0,0x1
9fc07778:	35a68000 	ori	a2,t5,0x8000
9fc0777c:	1180ff78 	beqz	t4,9fc07560 <crc16+0xe0>
9fc07780:	00051042 	srl	v0,a1,0x1
9fc07784:	00c02821 	move	a1,a2
9fc07788:	38a24002 	xori	v0,a1,0x4002
9fc0778c:	00851826 	xor	v1,a0,a1
9fc07790:	0002c842 	srl	t9,v0,0x1
9fc07794:	30780001 	andi	t8,v1,0x1
9fc07798:	37268000 	ori	a2,t9,0x8000
9fc0779c:	1300ff78 	beqz	t8,9fc07580 <crc16+0x100>
9fc077a0:	00051042 	srl	v0,a1,0x1
9fc077a4:	00c02821 	move	a1,a2
9fc077a8:	38a64002 	xori	a2,a1,0x4002
9fc077ac:	00064842 	srl	t1,a2,0x1
9fc077b0:	00042042 	srl	a0,a0,0x1
9fc077b4:	30a80001 	andi	t0,a1,0x1
9fc077b8:	35268000 	ori	a2,t1,0x8000
9fc077bc:	1104ff78 	beq	t0,a0,9fc075a0 <crc16+0x120>
9fc077c0:	00051042 	srl	v0,a1,0x1
9fc077c4:	00c02821 	move	a1,a2
9fc077c8:	00075a02 	srl	t3,a3,0x8
9fc077cc:	01656026 	xor	t4,t3,a1
9fc077d0:	38ad4002 	xori	t5,a1,0x4002
9fc077d4:	000d5042 	srl	t2,t5,0x1
9fc077d8:	31870001 	andi	a3,t4,0x1
9fc077dc:	000b2042 	srl	a0,t3,0x1
9fc077e0:	35468000 	ori	a2,t2,0x8000
9fc077e4:	10e0ff78 	beqz	a3,9fc075c8 <crc16+0x148>
9fc077e8:	00051042 	srl	v0,a1,0x1
9fc077ec:	00c02821 	move	a1,a2
9fc077f0:	0085c026 	xor	t8,a0,a1
9fc077f4:	38b94002 	xori	t9,a1,0x4002
9fc077f8:	00197842 	srl	t7,t9,0x1
9fc077fc:	330e0001 	andi	t6,t8,0x1
9fc07800:	00042042 	srl	a0,a0,0x1
9fc07804:	35e68000 	ori	a2,t7,0x8000
9fc07808:	11c0ff78 	beqz	t6,9fc075ec <crc16+0x16c>
9fc0780c:	00051042 	srl	v0,a1,0x1
9fc07810:	00c02821 	move	a1,a2
9fc07814:	00854026 	xor	t0,a0,a1
9fc07818:	38a94002 	xori	t1,a1,0x4002
9fc0781c:	00091042 	srl	v0,t1,0x1
9fc07820:	31030001 	andi	v1,t0,0x1
9fc07824:	34468000 	ori	a2,v0,0x8000
9fc07828:	00042042 	srl	a0,a0,0x1
9fc0782c:	1060ff78 	beqz	v1,9fc07610 <crc16+0x190>
9fc07830:	00051042 	srl	v0,a1,0x1
9fc07834:	00c02821 	move	a1,a2
9fc07838:	00853026 	xor	a2,a0,a1
9fc0783c:	38ab4002 	xori	t3,a1,0x4002
9fc07840:	000b5042 	srl	t2,t3,0x1
9fc07844:	30c70001 	andi	a3,a2,0x1
9fc07848:	00042042 	srl	a0,a0,0x1
9fc0784c:	35468000 	ori	a2,t2,0x8000
9fc07850:	10e0ff78 	beqz	a3,9fc07634 <crc16+0x1b4>
9fc07854:	00051042 	srl	v0,a1,0x1
9fc07858:	00c02821 	move	a1,a2
9fc0785c:	00857026 	xor	t6,a0,a1
9fc07860:	38af4002 	xori	t7,a1,0x4002
9fc07864:	000f6842 	srl	t5,t7,0x1
9fc07868:	31cc0001 	andi	t4,t6,0x1
9fc0786c:	00042042 	srl	a0,a0,0x1
9fc07870:	35a68000 	ori	a2,t5,0x8000
9fc07874:	1180ff78 	beqz	t4,9fc07658 <crc16+0x1d8>
9fc07878:	00051042 	srl	v0,a1,0x1
9fc0787c:	00c02821 	move	a1,a2
9fc07880:	00851826 	xor	v1,a0,a1
9fc07884:	38a24002 	xori	v0,a1,0x4002
9fc07888:	0002c842 	srl	t9,v0,0x1
9fc0788c:	30780001 	andi	t8,v1,0x1
9fc07890:	00042042 	srl	a0,a0,0x1
9fc07894:	37268000 	ori	a2,t9,0x8000
9fc07898:	1300ff78 	beqz	t8,9fc0767c <crc16+0x1fc>
9fc0789c:	00051042 	srl	v0,a1,0x1
9fc078a0:	00c02821 	move	a1,a2
9fc078a4:	00853826 	xor	a3,a0,a1
9fc078a8:	38aa4002 	xori	t2,a1,0x4002
9fc078ac:	000a4842 	srl	t1,t2,0x1
9fc078b0:	30e80001 	andi	t0,a3,0x1
9fc078b4:	35268000 	ori	a2,t1,0x8000
9fc078b8:	1100ff78 	beqz	t0,9fc0769c <crc16+0x21c>
9fc078bc:	00051042 	srl	v0,a1,0x1
9fc078c0:	00c02821 	move	a1,a2
9fc078c4:	38a64002 	xori	a2,a1,0x4002
9fc078c8:	00066042 	srl	t4,a2,0x1
9fc078cc:	00042042 	srl	a0,a0,0x1
9fc078d0:	30ab0001 	andi	t3,a1,0x1
9fc078d4:	35868000 	ori	a2,t4,0x8000
9fc078d8:	1164ff78 	beq	t3,a0,9fc076bc <crc16+0x23c>
9fc078dc:	00051042 	srl	v0,a1,0x1
9fc078e0:	00c01021 	move	v0,a2
9fc078e4:	03e00008 	jr	ra
9fc078e8:	3042ffff 	andi	v0,v0,0xffff
9fc078ec:	00000000 	nop

9fc078f0 <crcu32>:
crcu32():
9fc078f0:	3087ffff 	andi	a3,a0,0xffff
9fc078f4:	30a5ffff 	andi	a1,a1,0xffff
9fc078f8:	00804021 	move	t0,a0
9fc078fc:	30e400ff 	andi	a0,a3,0xff
9fc07900:	00a43026 	xor	a2,a1,a0
9fc07904:	38a94002 	xori	t1,a1,0x4002
9fc07908:	00091042 	srl	v0,t1,0x1
9fc0790c:	30c30001 	andi	v1,a2,0x1
9fc07910:	00042042 	srl	a0,a0,0x1
9fc07914:	34468000 	ori	a2,v0,0x8000
9fc07918:	14600119 	bnez	v1,9fc07d80 <crcu32+0x490>
9fc0791c:	00051042 	srl	v0,a1,0x1
9fc07920:	00402821 	move	a1,v0
9fc07924:	00856026 	xor	t4,a0,a1
9fc07928:	38ad4002 	xori	t5,a1,0x4002
9fc0792c:	000d5842 	srl	t3,t5,0x1
9fc07930:	318a0001 	andi	t2,t4,0x1
9fc07934:	00042042 	srl	a0,a0,0x1
9fc07938:	35668000 	ori	a2,t3,0x8000
9fc0793c:	15400119 	bnez	t2,9fc07da4 <crcu32+0x4b4>
9fc07940:	00051042 	srl	v0,a1,0x1
9fc07944:	00402821 	move	a1,v0
9fc07948:	0085c026 	xor	t8,a0,a1
9fc0794c:	38b94002 	xori	t9,a1,0x4002
9fc07950:	00197842 	srl	t7,t9,0x1
9fc07954:	330e0001 	andi	t6,t8,0x1
9fc07958:	00042042 	srl	a0,a0,0x1
9fc0795c:	35e68000 	ori	a2,t7,0x8000
9fc07960:	15c00119 	bnez	t6,9fc07dc8 <crcu32+0x4d8>
9fc07964:	00051042 	srl	v0,a1,0x1
9fc07968:	00402821 	move	a1,v0
9fc0796c:	00853026 	xor	a2,a0,a1
9fc07970:	38a94002 	xori	t1,a1,0x4002
9fc07974:	00091042 	srl	v0,t1,0x1
9fc07978:	30c30001 	andi	v1,a2,0x1
9fc0797c:	00042042 	srl	a0,a0,0x1
9fc07980:	34468000 	ori	a2,v0,0x8000
9fc07984:	14600119 	bnez	v1,9fc07dec <crcu32+0x4fc>
9fc07988:	00051042 	srl	v0,a1,0x1
9fc0798c:	00402821 	move	a1,v0
9fc07990:	00856026 	xor	t4,a0,a1
9fc07994:	38ad4002 	xori	t5,a1,0x4002
9fc07998:	000d5842 	srl	t3,t5,0x1
9fc0799c:	318a0001 	andi	t2,t4,0x1
9fc079a0:	00042042 	srl	a0,a0,0x1
9fc079a4:	35668000 	ori	a2,t3,0x8000
9fc079a8:	15400119 	bnez	t2,9fc07e10 <crcu32+0x520>
9fc079ac:	00051042 	srl	v0,a1,0x1
9fc079b0:	00402821 	move	a1,v0
9fc079b4:	0085c026 	xor	t8,a0,a1
9fc079b8:	38b94002 	xori	t9,a1,0x4002
9fc079bc:	00197842 	srl	t7,t9,0x1
9fc079c0:	330e0001 	andi	t6,t8,0x1
9fc079c4:	00042042 	srl	a0,a0,0x1
9fc079c8:	35e68000 	ori	a2,t7,0x8000
9fc079cc:	15c00119 	bnez	t6,9fc07e34 <crcu32+0x544>
9fc079d0:	00051042 	srl	v0,a1,0x1
9fc079d4:	00402821 	move	a1,v0
9fc079d8:	00853026 	xor	a2,a0,a1
9fc079dc:	38a94002 	xori	t1,a1,0x4002
9fc079e0:	00091042 	srl	v0,t1,0x1
9fc079e4:	30c30001 	andi	v1,a2,0x1
9fc079e8:	34468000 	ori	a2,v0,0x8000
9fc079ec:	14600119 	bnez	v1,9fc07e54 <crcu32+0x564>
9fc079f0:	00051042 	srl	v0,a1,0x1
9fc079f4:	00402821 	move	a1,v0
9fc079f8:	38ac4002 	xori	t4,a1,0x4002
9fc079fc:	000c5842 	srl	t3,t4,0x1
9fc07a00:	00042042 	srl	a0,a0,0x1
9fc07a04:	30aa0001 	andi	t2,a1,0x1
9fc07a08:	35668000 	ori	a2,t3,0x8000
9fc07a0c:	15440119 	bne	t2,a0,9fc07e74 <crcu32+0x584>
9fc07a10:	00051042 	srl	v0,a1,0x1
9fc07a14:	00402821 	move	a1,v0
9fc07a18:	00077202 	srl	t6,a3,0x8
9fc07a1c:	01c57826 	xor	t7,t6,a1
9fc07a20:	38b84002 	xori	t8,a1,0x4002
9fc07a24:	00186842 	srl	t5,t8,0x1
9fc07a28:	31e70001 	andi	a3,t7,0x1
9fc07a2c:	000e2042 	srl	a0,t6,0x1
9fc07a30:	35a68000 	ori	a2,t5,0x8000
9fc07a34:	14e00119 	bnez	a3,9fc07e9c <crcu32+0x5ac>
9fc07a38:	00051042 	srl	v0,a1,0x1
9fc07a3c:	00402821 	move	a1,v0
9fc07a40:	00851826 	xor	v1,a0,a1
9fc07a44:	38a64002 	xori	a2,a1,0x4002
9fc07a48:	00061042 	srl	v0,a2,0x1
9fc07a4c:	30790001 	andi	t9,v1,0x1
9fc07a50:	34468000 	ori	a2,v0,0x8000
9fc07a54:	00042042 	srl	a0,a0,0x1
9fc07a58:	17200119 	bnez	t9,9fc07ec0 <crcu32+0x5d0>
9fc07a5c:	00051042 	srl	v0,a1,0x1
9fc07a60:	00402821 	move	a1,v0
9fc07a64:	00855826 	xor	t3,a0,a1
9fc07a68:	38ac4002 	xori	t4,a1,0x4002
9fc07a6c:	000c5042 	srl	t2,t4,0x1
9fc07a70:	31690001 	andi	t1,t3,0x1
9fc07a74:	00042042 	srl	a0,a0,0x1
9fc07a78:	35468000 	ori	a2,t2,0x8000
9fc07a7c:	15200119 	bnez	t1,9fc07ee4 <crcu32+0x5f4>
9fc07a80:	00051042 	srl	v0,a1,0x1
9fc07a84:	00402821 	move	a1,v0
9fc07a88:	00857026 	xor	t6,a0,a1
9fc07a8c:	38af4002 	xori	t7,a1,0x4002
9fc07a90:	000f6842 	srl	t5,t7,0x1
9fc07a94:	31c70001 	andi	a3,t6,0x1
9fc07a98:	00042042 	srl	a0,a0,0x1
9fc07a9c:	35a68000 	ori	a2,t5,0x8000
9fc07aa0:	14e00119 	bnez	a3,9fc07f08 <crcu32+0x618>
9fc07aa4:	00051042 	srl	v0,a1,0x1
9fc07aa8:	00402821 	move	a1,v0
9fc07aac:	00851826 	xor	v1,a0,a1
9fc07ab0:	38a24002 	xori	v0,a1,0x4002
9fc07ab4:	0002c842 	srl	t9,v0,0x1
9fc07ab8:	30780001 	andi	t8,v1,0x1
9fc07abc:	00042042 	srl	a0,a0,0x1
9fc07ac0:	37268000 	ori	a2,t9,0x8000
9fc07ac4:	17000119 	bnez	t8,9fc07f2c <crcu32+0x63c>
9fc07ac8:	00051042 	srl	v0,a1,0x1
9fc07acc:	00402821 	move	a1,v0
9fc07ad0:	00853026 	xor	a2,a0,a1
9fc07ad4:	38ab4002 	xori	t3,a1,0x4002
9fc07ad8:	000b5042 	srl	t2,t3,0x1
9fc07adc:	30c90001 	andi	t1,a2,0x1
9fc07ae0:	00042042 	srl	a0,a0,0x1
9fc07ae4:	35468000 	ori	a2,t2,0x8000
9fc07ae8:	15200119 	bnez	t1,9fc07f50 <crcu32+0x660>
9fc07aec:	00051042 	srl	v0,a1,0x1
9fc07af0:	00402821 	move	a1,v0
9fc07af4:	00856826 	xor	t5,a0,a1
9fc07af8:	38ae4002 	xori	t6,a1,0x4002
9fc07afc:	000e3842 	srl	a3,t6,0x1
9fc07b00:	31ac0001 	andi	t4,t5,0x1
9fc07b04:	34e68000 	ori	a2,a3,0x8000
9fc07b08:	15800119 	bnez	t4,9fc07f70 <crcu32+0x680>
9fc07b0c:	00051042 	srl	v0,a1,0x1
9fc07b10:	00402821 	move	a1,v0
9fc07b14:	38b94002 	xori	t9,a1,0x4002
9fc07b18:	0019c042 	srl	t8,t9,0x1
9fc07b1c:	00042042 	srl	a0,a0,0x1
9fc07b20:	30af0001 	andi	t7,a1,0x1
9fc07b24:	37068000 	ori	a2,t8,0x8000
9fc07b28:	15e40119 	bne	t7,a0,9fc07f90 <crcu32+0x6a0>
9fc07b2c:	00051042 	srl	v0,a1,0x1
9fc07b30:	00083c02 	srl	a3,t0,0x10
9fc07b34:	00402821 	move	a1,v0
9fc07b38:	30e900ff 	andi	t1,a3,0xff
9fc07b3c:	01251826 	xor	v1,t1,a1
9fc07b40:	38aa4002 	xori	t2,a1,0x4002
9fc07b44:	000a1042 	srl	v0,t2,0x1
9fc07b48:	30680001 	andi	t0,v1,0x1
9fc07b4c:	34468000 	ori	a2,v0,0x8000
9fc07b50:	00092042 	srl	a0,t1,0x1
9fc07b54:	15000119 	bnez	t0,9fc07fbc <crcu32+0x6cc>
9fc07b58:	00051042 	srl	v0,a1,0x1
9fc07b5c:	00402821 	move	a1,v0
9fc07b60:	00853026 	xor	a2,a0,a1
9fc07b64:	38ad4002 	xori	t5,a1,0x4002
9fc07b68:	000d6042 	srl	t4,t5,0x1
9fc07b6c:	30cb0001 	andi	t3,a2,0x1
9fc07b70:	00042042 	srl	a0,a0,0x1
9fc07b74:	35868000 	ori	a2,t4,0x8000
9fc07b78:	15600119 	bnez	t3,9fc07fe0 <crcu32+0x6f0>
9fc07b7c:	00051042 	srl	v0,a1,0x1
9fc07b80:	00402821 	move	a1,v0
9fc07b84:	0085c026 	xor	t8,a0,a1
9fc07b88:	38b94002 	xori	t9,a1,0x4002
9fc07b8c:	00197842 	srl	t7,t9,0x1
9fc07b90:	330e0001 	andi	t6,t8,0x1
9fc07b94:	00042042 	srl	a0,a0,0x1
9fc07b98:	35e68000 	ori	a2,t7,0x8000
9fc07b9c:	15c00119 	bnez	t6,9fc08004 <crcu32+0x714>
9fc07ba0:	00051042 	srl	v0,a1,0x1
9fc07ba4:	00402821 	move	a1,v0
9fc07ba8:	00854826 	xor	t1,a0,a1
9fc07bac:	38a34002 	xori	v1,a1,0x4002
9fc07bb0:	00031042 	srl	v0,v1,0x1
9fc07bb4:	31280001 	andi	t0,t1,0x1
9fc07bb8:	34468000 	ori	a2,v0,0x8000
9fc07bbc:	00042042 	srl	a0,a0,0x1
9fc07bc0:	15000119 	bnez	t0,9fc08028 <crcu32+0x738>
9fc07bc4:	00051042 	srl	v0,a1,0x1
9fc07bc8:	00402821 	move	a1,v0
9fc07bcc:	00856026 	xor	t4,a0,a1
9fc07bd0:	38a64002 	xori	a2,a1,0x4002
9fc07bd4:	00065842 	srl	t3,a2,0x1
9fc07bd8:	318a0001 	andi	t2,t4,0x1
9fc07bdc:	00042042 	srl	a0,a0,0x1
9fc07be0:	35668000 	ori	a2,t3,0x8000
9fc07be4:	15400119 	bnez	t2,9fc0804c <crcu32+0x75c>
9fc07be8:	00051042 	srl	v0,a1,0x1
9fc07bec:	00402821 	move	a1,v0
9fc07bf0:	00857826 	xor	t7,a0,a1
9fc07bf4:	38b84002 	xori	t8,a1,0x4002
9fc07bf8:	00187042 	srl	t6,t8,0x1
9fc07bfc:	31ed0001 	andi	t5,t7,0x1
9fc07c00:	00042042 	srl	a0,a0,0x1
9fc07c04:	35c68000 	ori	a2,t6,0x8000
9fc07c08:	15a00119 	bnez	t5,9fc08070 <crcu32+0x780>
9fc07c0c:	00051042 	srl	v0,a1,0x1
9fc07c10:	00402821 	move	a1,v0
9fc07c14:	38a24002 	xori	v0,a1,0x4002
9fc07c18:	00854826 	xor	t1,a0,a1
9fc07c1c:	00024042 	srl	t0,v0,0x1
9fc07c20:	31390001 	andi	t9,t1,0x1
9fc07c24:	35068000 	ori	a2,t0,0x8000
9fc07c28:	17200119 	bnez	t9,9fc08090 <crcu32+0x7a0>
9fc07c2c:	00051042 	srl	v0,a1,0x1
9fc07c30:	00402821 	move	a1,v0
9fc07c34:	38ab4002 	xori	t3,a1,0x4002
9fc07c38:	000b5042 	srl	t2,t3,0x1
9fc07c3c:	00042042 	srl	a0,a0,0x1
9fc07c40:	30a30001 	andi	v1,a1,0x1
9fc07c44:	35468000 	ori	a2,t2,0x8000
9fc07c48:	14640119 	bne	v1,a0,9fc080b0 <crcu32+0x7c0>
9fc07c4c:	00051042 	srl	v0,a1,0x1
9fc07c50:	00073202 	srl	a2,a3,0x8
9fc07c54:	00402821 	move	a1,v0
9fc07c58:	00c56826 	xor	t5,a2,a1
9fc07c5c:	38ae4002 	xori	t6,a1,0x4002
9fc07c60:	000e6042 	srl	t4,t6,0x1
9fc07c64:	31a70001 	andi	a3,t5,0x1
9fc07c68:	00062042 	srl	a0,a2,0x1
9fc07c6c:	00051042 	srl	v0,a1,0x1
9fc07c70:	14e00119 	bnez	a3,9fc080d8 <crcu32+0x7e8>
9fc07c74:	35868000 	ori	a2,t4,0x8000
9fc07c78:	00402821 	move	a1,v0
9fc07c7c:	0085c826 	xor	t9,a0,a1
9fc07c80:	38a84002 	xori	t0,a1,0x4002
9fc07c84:	0008c042 	srl	t8,t0,0x1
9fc07c88:	332f0001 	andi	t7,t9,0x1
9fc07c8c:	00042042 	srl	a0,a0,0x1
9fc07c90:	37068000 	ori	a2,t8,0x8000
9fc07c94:	15e00119 	bnez	t7,9fc080fc <crcu32+0x80c>
9fc07c98:	00051042 	srl	v0,a1,0x1
9fc07c9c:	00402821 	move	a1,v0
9fc07ca0:	00851826 	xor	v1,a0,a1
9fc07ca4:	38aa4002 	xori	t2,a1,0x4002
9fc07ca8:	000a1042 	srl	v0,t2,0x1
9fc07cac:	30690001 	andi	t1,v1,0x1
9fc07cb0:	34468000 	ori	a2,v0,0x8000
9fc07cb4:	00042042 	srl	a0,a0,0x1
9fc07cb8:	15200119 	bnez	t1,9fc08120 <crcu32+0x830>
9fc07cbc:	00051042 	srl	v0,a1,0x1
9fc07cc0:	00402821 	move	a1,v0
9fc07cc4:	00856026 	xor	t4,a0,a1
9fc07cc8:	38a64002 	xori	a2,a1,0x4002
9fc07ccc:	00063842 	srl	a3,a2,0x1
9fc07cd0:	318b0001 	andi	t3,t4,0x1
9fc07cd4:	00042042 	srl	a0,a0,0x1
9fc07cd8:	34e68000 	ori	a2,a3,0x8000
9fc07cdc:	15600119 	bnez	t3,9fc08144 <crcu32+0x854>
9fc07ce0:	00051042 	srl	v0,a1,0x1
9fc07ce4:	00402821 	move	a1,v0
9fc07ce8:	00857826 	xor	t7,a0,a1
9fc07cec:	38b84002 	xori	t8,a1,0x4002
9fc07cf0:	00187042 	srl	t6,t8,0x1
9fc07cf4:	31ed0001 	andi	t5,t7,0x1
9fc07cf8:	00042042 	srl	a0,a0,0x1
9fc07cfc:	35c68000 	ori	a2,t6,0x8000
9fc07d00:	15a00119 	bnez	t5,9fc08168 <crcu32+0x878>
9fc07d04:	00051042 	srl	v0,a1,0x1
9fc07d08:	00402821 	move	a1,v0
9fc07d0c:	00854826 	xor	t1,a0,a1
9fc07d10:	38a24002 	xori	v0,a1,0x4002
9fc07d14:	00024042 	srl	t0,v0,0x1
9fc07d18:	31390001 	andi	t9,t1,0x1
9fc07d1c:	00042042 	srl	a0,a0,0x1
9fc07d20:	35068000 	ori	a2,t0,0x8000
9fc07d24:	17200119 	bnez	t9,9fc0818c <crcu32+0x89c>
9fc07d28:	00051042 	srl	v0,a1,0x1
9fc07d2c:	00402821 	move	a1,v0
9fc07d30:	00855826 	xor	t3,a0,a1
9fc07d34:	38a74002 	xori	a3,a1,0x4002
9fc07d38:	00075042 	srl	t2,a3,0x1
9fc07d3c:	31630001 	andi	v1,t3,0x1
9fc07d40:	35468000 	ori	a2,t2,0x8000
9fc07d44:	14600119 	bnez	v1,9fc081ac <crcu32+0x8bc>
9fc07d48:	00051042 	srl	v0,a1,0x1
9fc07d4c:	00402821 	move	a1,v0
9fc07d50:	38a64002 	xori	a2,a1,0x4002
9fc07d54:	00066842 	srl	t5,a2,0x1
9fc07d58:	00042042 	srl	a0,a0,0x1
9fc07d5c:	30ac0001 	andi	t4,a1,0x1
9fc07d60:	35a68000 	ori	a2,t5,0x8000
9fc07d64:	15840119 	bne	t4,a0,9fc081cc <crcu32+0x8dc>
9fc07d68:	00051042 	srl	v0,a1,0x1
9fc07d6c:	03e00008 	jr	ra
9fc07d70:	3042ffff 	andi	v0,v0,0xffff
	...
9fc07d80:	00c02821 	move	a1,a2
9fc07d84:	00856026 	xor	t4,a0,a1
9fc07d88:	38ad4002 	xori	t5,a1,0x4002
9fc07d8c:	000d5842 	srl	t3,t5,0x1
9fc07d90:	318a0001 	andi	t2,t4,0x1
9fc07d94:	00042042 	srl	a0,a0,0x1
9fc07d98:	35668000 	ori	a2,t3,0x8000
9fc07d9c:	1140fee9 	beqz	t2,9fc07944 <crcu32+0x54>
9fc07da0:	00051042 	srl	v0,a1,0x1
9fc07da4:	00c02821 	move	a1,a2
9fc07da8:	0085c026 	xor	t8,a0,a1
9fc07dac:	38b94002 	xori	t9,a1,0x4002
9fc07db0:	00197842 	srl	t7,t9,0x1
9fc07db4:	330e0001 	andi	t6,t8,0x1
9fc07db8:	00042042 	srl	a0,a0,0x1
9fc07dbc:	35e68000 	ori	a2,t7,0x8000
9fc07dc0:	11c0fee9 	beqz	t6,9fc07968 <crcu32+0x78>
9fc07dc4:	00051042 	srl	v0,a1,0x1
9fc07dc8:	00c02821 	move	a1,a2
9fc07dcc:	00853026 	xor	a2,a0,a1
9fc07dd0:	38a94002 	xori	t1,a1,0x4002
9fc07dd4:	00091042 	srl	v0,t1,0x1
9fc07dd8:	30c30001 	andi	v1,a2,0x1
9fc07ddc:	00042042 	srl	a0,a0,0x1
9fc07de0:	34468000 	ori	a2,v0,0x8000
9fc07de4:	1060fee9 	beqz	v1,9fc0798c <crcu32+0x9c>
9fc07de8:	00051042 	srl	v0,a1,0x1
9fc07dec:	00c02821 	move	a1,a2
9fc07df0:	00856026 	xor	t4,a0,a1
9fc07df4:	38ad4002 	xori	t5,a1,0x4002
9fc07df8:	000d5842 	srl	t3,t5,0x1
9fc07dfc:	318a0001 	andi	t2,t4,0x1
9fc07e00:	00042042 	srl	a0,a0,0x1
9fc07e04:	35668000 	ori	a2,t3,0x8000
9fc07e08:	1140fee9 	beqz	t2,9fc079b0 <crcu32+0xc0>
9fc07e0c:	00051042 	srl	v0,a1,0x1
9fc07e10:	00c02821 	move	a1,a2
9fc07e14:	0085c026 	xor	t8,a0,a1
9fc07e18:	38b94002 	xori	t9,a1,0x4002
9fc07e1c:	00197842 	srl	t7,t9,0x1
9fc07e20:	330e0001 	andi	t6,t8,0x1
9fc07e24:	00042042 	srl	a0,a0,0x1
9fc07e28:	35e68000 	ori	a2,t7,0x8000
9fc07e2c:	11c0fee9 	beqz	t6,9fc079d4 <crcu32+0xe4>
9fc07e30:	00051042 	srl	v0,a1,0x1
9fc07e34:	00c02821 	move	a1,a2
9fc07e38:	00853026 	xor	a2,a0,a1
9fc07e3c:	38a94002 	xori	t1,a1,0x4002
9fc07e40:	00091042 	srl	v0,t1,0x1
9fc07e44:	30c30001 	andi	v1,a2,0x1
9fc07e48:	34468000 	ori	a2,v0,0x8000
9fc07e4c:	1060fee9 	beqz	v1,9fc079f4 <crcu32+0x104>
9fc07e50:	00051042 	srl	v0,a1,0x1
9fc07e54:	00c02821 	move	a1,a2
9fc07e58:	38ac4002 	xori	t4,a1,0x4002
9fc07e5c:	000c5842 	srl	t3,t4,0x1
9fc07e60:	00042042 	srl	a0,a0,0x1
9fc07e64:	30aa0001 	andi	t2,a1,0x1
9fc07e68:	35668000 	ori	a2,t3,0x8000
9fc07e6c:	1144fee9 	beq	t2,a0,9fc07a14 <crcu32+0x124>
9fc07e70:	00051042 	srl	v0,a1,0x1
9fc07e74:	00c02821 	move	a1,a2
9fc07e78:	00077202 	srl	t6,a3,0x8
9fc07e7c:	01c57826 	xor	t7,t6,a1
9fc07e80:	38b84002 	xori	t8,a1,0x4002
9fc07e84:	00186842 	srl	t5,t8,0x1
9fc07e88:	31e70001 	andi	a3,t7,0x1
9fc07e8c:	000e2042 	srl	a0,t6,0x1
9fc07e90:	35a68000 	ori	a2,t5,0x8000
9fc07e94:	10e0fee9 	beqz	a3,9fc07a3c <crcu32+0x14c>
9fc07e98:	00051042 	srl	v0,a1,0x1
9fc07e9c:	00c02821 	move	a1,a2
9fc07ea0:	00851826 	xor	v1,a0,a1
9fc07ea4:	38a64002 	xori	a2,a1,0x4002
9fc07ea8:	00061042 	srl	v0,a2,0x1
9fc07eac:	30790001 	andi	t9,v1,0x1
9fc07eb0:	34468000 	ori	a2,v0,0x8000
9fc07eb4:	00042042 	srl	a0,a0,0x1
9fc07eb8:	1320fee9 	beqz	t9,9fc07a60 <crcu32+0x170>
9fc07ebc:	00051042 	srl	v0,a1,0x1
9fc07ec0:	00c02821 	move	a1,a2
9fc07ec4:	00855826 	xor	t3,a0,a1
9fc07ec8:	38ac4002 	xori	t4,a1,0x4002
9fc07ecc:	000c5042 	srl	t2,t4,0x1
9fc07ed0:	31690001 	andi	t1,t3,0x1
9fc07ed4:	00042042 	srl	a0,a0,0x1
9fc07ed8:	35468000 	ori	a2,t2,0x8000
9fc07edc:	1120fee9 	beqz	t1,9fc07a84 <crcu32+0x194>
9fc07ee0:	00051042 	srl	v0,a1,0x1
9fc07ee4:	00c02821 	move	a1,a2
9fc07ee8:	00857026 	xor	t6,a0,a1
9fc07eec:	38af4002 	xori	t7,a1,0x4002
9fc07ef0:	000f6842 	srl	t5,t7,0x1
9fc07ef4:	31c70001 	andi	a3,t6,0x1
9fc07ef8:	00042042 	srl	a0,a0,0x1
9fc07efc:	35a68000 	ori	a2,t5,0x8000
9fc07f00:	10e0fee9 	beqz	a3,9fc07aa8 <crcu32+0x1b8>
9fc07f04:	00051042 	srl	v0,a1,0x1
9fc07f08:	00c02821 	move	a1,a2
9fc07f0c:	00851826 	xor	v1,a0,a1
9fc07f10:	38a24002 	xori	v0,a1,0x4002
9fc07f14:	0002c842 	srl	t9,v0,0x1
9fc07f18:	30780001 	andi	t8,v1,0x1
9fc07f1c:	00042042 	srl	a0,a0,0x1
9fc07f20:	37268000 	ori	a2,t9,0x8000
9fc07f24:	1300fee9 	beqz	t8,9fc07acc <crcu32+0x1dc>
9fc07f28:	00051042 	srl	v0,a1,0x1
9fc07f2c:	00c02821 	move	a1,a2
9fc07f30:	00853026 	xor	a2,a0,a1
9fc07f34:	38ab4002 	xori	t3,a1,0x4002
9fc07f38:	000b5042 	srl	t2,t3,0x1
9fc07f3c:	30c90001 	andi	t1,a2,0x1
9fc07f40:	00042042 	srl	a0,a0,0x1
9fc07f44:	35468000 	ori	a2,t2,0x8000
9fc07f48:	1120fee9 	beqz	t1,9fc07af0 <crcu32+0x200>
9fc07f4c:	00051042 	srl	v0,a1,0x1
9fc07f50:	00c02821 	move	a1,a2
9fc07f54:	00856826 	xor	t5,a0,a1
9fc07f58:	38ae4002 	xori	t6,a1,0x4002
9fc07f5c:	000e3842 	srl	a3,t6,0x1
9fc07f60:	31ac0001 	andi	t4,t5,0x1
9fc07f64:	34e68000 	ori	a2,a3,0x8000
9fc07f68:	1180fee9 	beqz	t4,9fc07b10 <crcu32+0x220>
9fc07f6c:	00051042 	srl	v0,a1,0x1
9fc07f70:	00c02821 	move	a1,a2
9fc07f74:	38b94002 	xori	t9,a1,0x4002
9fc07f78:	0019c042 	srl	t8,t9,0x1
9fc07f7c:	00042042 	srl	a0,a0,0x1
9fc07f80:	30af0001 	andi	t7,a1,0x1
9fc07f84:	37068000 	ori	a2,t8,0x8000
9fc07f88:	11e4fee9 	beq	t7,a0,9fc07b30 <crcu32+0x240>
9fc07f8c:	00051042 	srl	v0,a1,0x1
9fc07f90:	00083c02 	srl	a3,t0,0x10
9fc07f94:	00c02821 	move	a1,a2
9fc07f98:	30e900ff 	andi	t1,a3,0xff
9fc07f9c:	01251826 	xor	v1,t1,a1
9fc07fa0:	38aa4002 	xori	t2,a1,0x4002
9fc07fa4:	000a1042 	srl	v0,t2,0x1
9fc07fa8:	30680001 	andi	t0,v1,0x1
9fc07fac:	34468000 	ori	a2,v0,0x8000
9fc07fb0:	00092042 	srl	a0,t1,0x1
9fc07fb4:	1100fee9 	beqz	t0,9fc07b5c <crcu32+0x26c>
9fc07fb8:	00051042 	srl	v0,a1,0x1
9fc07fbc:	00c02821 	move	a1,a2
9fc07fc0:	00853026 	xor	a2,a0,a1
9fc07fc4:	38ad4002 	xori	t5,a1,0x4002
9fc07fc8:	000d6042 	srl	t4,t5,0x1
9fc07fcc:	30cb0001 	andi	t3,a2,0x1
9fc07fd0:	00042042 	srl	a0,a0,0x1
9fc07fd4:	35868000 	ori	a2,t4,0x8000
9fc07fd8:	1160fee9 	beqz	t3,9fc07b80 <crcu32+0x290>
9fc07fdc:	00051042 	srl	v0,a1,0x1
9fc07fe0:	00c02821 	move	a1,a2
9fc07fe4:	0085c026 	xor	t8,a0,a1
9fc07fe8:	38b94002 	xori	t9,a1,0x4002
9fc07fec:	00197842 	srl	t7,t9,0x1
9fc07ff0:	330e0001 	andi	t6,t8,0x1
9fc07ff4:	00042042 	srl	a0,a0,0x1
9fc07ff8:	35e68000 	ori	a2,t7,0x8000
9fc07ffc:	11c0fee9 	beqz	t6,9fc07ba4 <crcu32+0x2b4>
9fc08000:	00051042 	srl	v0,a1,0x1
9fc08004:	00c02821 	move	a1,a2
9fc08008:	00854826 	xor	t1,a0,a1
9fc0800c:	38a34002 	xori	v1,a1,0x4002
9fc08010:	00031042 	srl	v0,v1,0x1
9fc08014:	31280001 	andi	t0,t1,0x1
9fc08018:	34468000 	ori	a2,v0,0x8000
9fc0801c:	00042042 	srl	a0,a0,0x1
9fc08020:	1100fee9 	beqz	t0,9fc07bc8 <crcu32+0x2d8>
9fc08024:	00051042 	srl	v0,a1,0x1
9fc08028:	00c02821 	move	a1,a2
9fc0802c:	00856026 	xor	t4,a0,a1
9fc08030:	38a64002 	xori	a2,a1,0x4002
9fc08034:	00065842 	srl	t3,a2,0x1
9fc08038:	318a0001 	andi	t2,t4,0x1
9fc0803c:	00042042 	srl	a0,a0,0x1
9fc08040:	35668000 	ori	a2,t3,0x8000
9fc08044:	1140fee9 	beqz	t2,9fc07bec <crcu32+0x2fc>
9fc08048:	00051042 	srl	v0,a1,0x1
9fc0804c:	00c02821 	move	a1,a2
9fc08050:	00857826 	xor	t7,a0,a1
9fc08054:	38b84002 	xori	t8,a1,0x4002
9fc08058:	00187042 	srl	t6,t8,0x1
9fc0805c:	31ed0001 	andi	t5,t7,0x1
9fc08060:	00042042 	srl	a0,a0,0x1
9fc08064:	35c68000 	ori	a2,t6,0x8000
9fc08068:	11a0fee9 	beqz	t5,9fc07c10 <crcu32+0x320>
9fc0806c:	00051042 	srl	v0,a1,0x1
9fc08070:	00c02821 	move	a1,a2
9fc08074:	38a24002 	xori	v0,a1,0x4002
9fc08078:	00854826 	xor	t1,a0,a1
9fc0807c:	00024042 	srl	t0,v0,0x1
9fc08080:	31390001 	andi	t9,t1,0x1
9fc08084:	35068000 	ori	a2,t0,0x8000
9fc08088:	1320fee9 	beqz	t9,9fc07c30 <crcu32+0x340>
9fc0808c:	00051042 	srl	v0,a1,0x1
9fc08090:	00c02821 	move	a1,a2
9fc08094:	38ab4002 	xori	t3,a1,0x4002
9fc08098:	000b5042 	srl	t2,t3,0x1
9fc0809c:	00042042 	srl	a0,a0,0x1
9fc080a0:	30a30001 	andi	v1,a1,0x1
9fc080a4:	35468000 	ori	a2,t2,0x8000
9fc080a8:	1064fee9 	beq	v1,a0,9fc07c50 <crcu32+0x360>
9fc080ac:	00051042 	srl	v0,a1,0x1
9fc080b0:	00c02821 	move	a1,a2
9fc080b4:	00073202 	srl	a2,a3,0x8
9fc080b8:	00c56826 	xor	t5,a2,a1
9fc080bc:	38ae4002 	xori	t6,a1,0x4002
9fc080c0:	000e6042 	srl	t4,t6,0x1
9fc080c4:	31a70001 	andi	a3,t5,0x1
9fc080c8:	00062042 	srl	a0,a2,0x1
9fc080cc:	00051042 	srl	v0,a1,0x1
9fc080d0:	10e0fee9 	beqz	a3,9fc07c78 <crcu32+0x388>
9fc080d4:	35868000 	ori	a2,t4,0x8000
9fc080d8:	00c02821 	move	a1,a2
9fc080dc:	0085c826 	xor	t9,a0,a1
9fc080e0:	38a84002 	xori	t0,a1,0x4002
9fc080e4:	0008c042 	srl	t8,t0,0x1
9fc080e8:	332f0001 	andi	t7,t9,0x1
9fc080ec:	00042042 	srl	a0,a0,0x1
9fc080f0:	37068000 	ori	a2,t8,0x8000
9fc080f4:	11e0fee9 	beqz	t7,9fc07c9c <crcu32+0x3ac>
9fc080f8:	00051042 	srl	v0,a1,0x1
9fc080fc:	00c02821 	move	a1,a2
9fc08100:	00851826 	xor	v1,a0,a1
9fc08104:	38aa4002 	xori	t2,a1,0x4002
9fc08108:	000a1042 	srl	v0,t2,0x1
9fc0810c:	30690001 	andi	t1,v1,0x1
9fc08110:	34468000 	ori	a2,v0,0x8000
9fc08114:	00042042 	srl	a0,a0,0x1
9fc08118:	1120fee9 	beqz	t1,9fc07cc0 <crcu32+0x3d0>
9fc0811c:	00051042 	srl	v0,a1,0x1
9fc08120:	00c02821 	move	a1,a2
9fc08124:	00856026 	xor	t4,a0,a1
9fc08128:	38a64002 	xori	a2,a1,0x4002
9fc0812c:	00063842 	srl	a3,a2,0x1
9fc08130:	318b0001 	andi	t3,t4,0x1
9fc08134:	00042042 	srl	a0,a0,0x1
9fc08138:	34e68000 	ori	a2,a3,0x8000
9fc0813c:	1160fee9 	beqz	t3,9fc07ce4 <crcu32+0x3f4>
9fc08140:	00051042 	srl	v0,a1,0x1
9fc08144:	00c02821 	move	a1,a2
9fc08148:	00857826 	xor	t7,a0,a1
9fc0814c:	38b84002 	xori	t8,a1,0x4002
9fc08150:	00187042 	srl	t6,t8,0x1
9fc08154:	31ed0001 	andi	t5,t7,0x1
9fc08158:	00042042 	srl	a0,a0,0x1
9fc0815c:	35c68000 	ori	a2,t6,0x8000
9fc08160:	11a0fee9 	beqz	t5,9fc07d08 <crcu32+0x418>
9fc08164:	00051042 	srl	v0,a1,0x1
9fc08168:	00c02821 	move	a1,a2
9fc0816c:	00854826 	xor	t1,a0,a1
9fc08170:	38a24002 	xori	v0,a1,0x4002
9fc08174:	00024042 	srl	t0,v0,0x1
9fc08178:	31390001 	andi	t9,t1,0x1
9fc0817c:	00042042 	srl	a0,a0,0x1
9fc08180:	35068000 	ori	a2,t0,0x8000
9fc08184:	1320fee9 	beqz	t9,9fc07d2c <crcu32+0x43c>
9fc08188:	00051042 	srl	v0,a1,0x1
9fc0818c:	00c02821 	move	a1,a2
9fc08190:	00855826 	xor	t3,a0,a1
9fc08194:	38a74002 	xori	a3,a1,0x4002
9fc08198:	00075042 	srl	t2,a3,0x1
9fc0819c:	31630001 	andi	v1,t3,0x1
9fc081a0:	35468000 	ori	a2,t2,0x8000
9fc081a4:	1060fee9 	beqz	v1,9fc07d4c <crcu32+0x45c>
9fc081a8:	00051042 	srl	v0,a1,0x1
9fc081ac:	00c02821 	move	a1,a2
9fc081b0:	38a64002 	xori	a2,a1,0x4002
9fc081b4:	00066842 	srl	t5,a2,0x1
9fc081b8:	00042042 	srl	a0,a0,0x1
9fc081bc:	30ac0001 	andi	t4,a1,0x1
9fc081c0:	35a68000 	ori	a2,t5,0x8000
9fc081c4:	1184fee9 	beq	t4,a0,9fc07d6c <crcu32+0x47c>
9fc081c8:	00051042 	srl	v0,a1,0x1
9fc081cc:	00c01021 	move	v0,a2
9fc081d0:	03e00008 	jr	ra
9fc081d4:	3042ffff 	andi	v0,v0,0xffff
	...

9fc081e0 <get_seed_args>:
get_seed_args():
9fc081e0:	0085282a 	slt	a1,a0,a1
9fc081e4:	10a00042 	beqz	a1,9fc082f0 <get_seed_args+0x110>
9fc081e8:	00001021 	move	v0,zero
9fc081ec:	00043880 	sll	a3,a0,0x2
9fc081f0:	00c71821 	addu	v1,a2,a3
9fc081f4:	8c660000 	lw	a2,0(v1)
9fc081f8:	2402002d 	li	v0,45
9fc081fc:	80c70000 	lb	a3,0(a2)
9fc08200:	00000000 	nop
9fc08204:	10e2003e 	beq	a3,v0,9fc08300 <get_seed_args+0x120>
9fc08208:	24040030 	li	a0,48
9fc0820c:	10e40041 	beq	a3,a0,9fc08314 <get_seed_args+0x134>
9fc08210:	240a0001 	li	t2,1
9fc08214:	24edffd0 	addiu	t5,a3,-48
9fc08218:	31ac00ff 	andi	t4,t5,0xff
9fc0821c:	2d8b000a 	sltiu	t3,t4,10
9fc08220:	1160002b 	beqz	t3,9fc082d0 <get_seed_args+0xf0>
9fc08224:	00004021 	move	t0,zero
9fc08228:	000878c0 	sll	t7,t0,0x3
9fc0822c:	00087040 	sll	t6,t0,0x1
9fc08230:	01cf4021 	addu	t0,t6,t7
9fc08234:	00e86821 	addu	t5,a3,t0
9fc08238:	80c70001 	lb	a3,1(a2)
9fc0823c:	25a8ffd0 	addiu	t0,t5,-48
9fc08240:	24e9ffd0 	addiu	t1,a3,-48
9fc08244:	000860c0 	sll	t4,t0,0x3
9fc08248:	00085840 	sll	t3,t0,0x1
9fc0824c:	312400ff 	andi	a0,t1,0xff
9fc08250:	016c1021 	addu	v0,t3,t4
9fc08254:	2c83000a 	sltiu	v1,a0,10
9fc08258:	1060001d 	beqz	v1,9fc082d0 <get_seed_args+0xf0>
9fc0825c:	00e21021 	addu	v0,a3,v0
9fc08260:	80c70002 	lb	a3,2(a2)
9fc08264:	2448ffd0 	addiu	t0,v0,-48
9fc08268:	24e4ffd0 	addiu	a0,a3,-48
9fc0826c:	000810c0 	sll	v0,t0,0x3
9fc08270:	00084840 	sll	t1,t0,0x1
9fc08274:	308500ff 	andi	a1,a0,0xff
9fc08278:	01221821 	addu	v1,t1,v0
9fc0827c:	2cb9000a 	sltiu	t9,a1,10
9fc08280:	13200013 	beqz	t9,9fc082d0 <get_seed_args+0xf0>
9fc08284:	00e31821 	addu	v1,a3,v1
9fc08288:	80c70003 	lb	a3,3(a2)
9fc0828c:	24c60004 	addiu	a2,a2,4
9fc08290:	24efffd0 	addiu	t7,a3,-48
9fc08294:	31e800ff 	andi	t0,t7,0xff
9fc08298:	2d0b000a 	sltiu	t3,t0,10
9fc0829c:	2468ffd0 	addiu	t0,v1,-48
9fc082a0:	000870c0 	sll	t6,t0,0x3
9fc082a4:	00086840 	sll	t5,t0,0x1
9fc082a8:	01ae6021 	addu	t4,t5,t6
9fc082ac:	11600008 	beqz	t3,9fc082d0 <get_seed_args+0xf0>
9fc082b0:	00ec1821 	addu	v1,a3,t4
9fc082b4:	80c70000 	lb	a3,0(a2)
9fc082b8:	00000000 	nop
9fc082bc:	24e5ffd0 	addiu	a1,a3,-48
9fc082c0:	30b900ff 	andi	t9,a1,0xff
9fc082c4:	2f38000a 	sltiu	t8,t9,10
9fc082c8:	1700ffd7 	bnez	t8,9fc08228 <get_seed_args+0x48>
9fc082cc:	2468ffd0 	addiu	t0,v1,-48
9fc082d0:	2418004b 	li	t8,75
9fc082d4:	10f8002a 	beq	a3,t8,9fc08380 <get_seed_args+0x1a0>
9fc082d8:	2406004d 	li	a2,77
9fc082dc:	14e60003 	bne	a3,a2,9fc082ec <get_seed_args+0x10c>
9fc082e0:	010a0018 	mult	t0,t2
9fc082e4:	00084500 	sll	t0,t0,0x14
9fc082e8:	010a0018 	mult	t0,t2
9fc082ec:	00001012 	mflo	v0
9fc082f0:	03e00008 	jr	ra
9fc082f4:	00000000 	nop
	...
9fc08300:	24c60001 	addiu	a2,a2,1
9fc08304:	80c70000 	lb	a3,0(a2)
9fc08308:	00000000 	nop
9fc0830c:	14e4ffc1 	bne	a3,a0,9fc08214 <get_seed_args+0x34>
9fc08310:	240affff 	li	t2,-1
9fc08314:	80c90001 	lb	t1,1(a2)
9fc08318:	24080078 	li	t0,120
9fc0831c:	1528ffbe 	bne	t1,t0,9fc08218 <get_seed_args+0x38>
9fc08320:	24edffd0 	addiu	t5,a3,-48
9fc08324:	24c40002 	addiu	a0,a2,2
9fc08328:	00004021 	move	t0,zero
9fc0832c:	80870000 	lb	a3,0(a0)
9fc08330:	00084900 	sll	t1,t0,0x4
9fc08334:	30e500ff 	andi	a1,a3,0xff
9fc08338:	24b9ffd0 	addiu	t9,a1,-48
9fc0833c:	24a6ff9f 	addiu	a2,a1,-97
9fc08340:	333800ff 	andi	t8,t9,0xff
9fc08344:	30cf00ff 	andi	t7,a2,0xff
9fc08348:	24e5ffd0 	addiu	a1,a3,-48
9fc0834c:	2f0e000a 	sltiu	t6,t8,10
9fc08350:	28a6000a 	slti	a2,a1,10
9fc08354:	24840001 	addiu	a0,a0,1
9fc08358:	15c00003 	bnez	t6,9fc08368 <get_seed_args+0x188>
9fc0835c:	2de30006 	sltiu	v1,t7,6
9fc08360:	1060ffdc 	beqz	v1,9fc082d4 <get_seed_args+0xf4>
9fc08364:	2418004b 	li	t8,75
9fc08368:	14c00002 	bnez	a2,9fc08374 <get_seed_args+0x194>
9fc0836c:	00000000 	nop
9fc08370:	24e5ffa9 	addiu	a1,a3,-87
9fc08374:	0bf020cb 	j	9fc0832c <get_seed_args+0x14c>
9fc08378:	00a94021 	addu	t0,a1,t1
9fc0837c:	00000000 	nop
9fc08380:	00084280 	sll	t0,t0,0xa
9fc08384:	010a0018 	mult	t0,t2
9fc08388:	00001012 	mflo	v0
9fc0838c:	03e00008 	jr	ra
9fc08390:	00000000 	nop
	...

9fc083a0 <cmp_idx>:
cmp_idx():
9fc083a0:	10c00007 	beqz	a2,9fc083c0 <cmp_idx+0x20>
9fc083a4:	00803821 	move	a3,a0
9fc083a8:	84ef0002 	lh	t7,2(a3)
9fc083ac:	84a70002 	lh	a3,2(a1)
9fc083b0:	03e00008 	jr	ra
9fc083b4:	01e71023 	subu	v0,t7,a3
	...
9fc083c0:	848d0000 	lh	t5,0(a0)
9fc083c4:	2409ff00 	li	t1,-256
9fc083c8:	31aeffff 	andi	t6,t5,0xffff
9fc083cc:	000e5a02 	srl	t3,t6,0x8
9fc083d0:	01a96024 	and	t4,t5,t1
9fc083d4:	016c5025 	or	t2,t3,t4
9fc083d8:	a48a0000 	sh	t2,0(a0)
9fc083dc:	84a80000 	lh	t0,0(a1)
9fc083e0:	84ef0002 	lh	t7,2(a3)
9fc083e4:	3106ffff 	andi	a2,t0,0xffff
9fc083e8:	01091824 	and	v1,t0,t1
9fc083ec:	00062202 	srl	a0,a2,0x8
9fc083f0:	84a70002 	lh	a3,2(a1)
9fc083f4:	00831025 	or	v0,a0,v1
9fc083f8:	a4a20000 	sh	v0,0(a1)
9fc083fc:	03e00008 	jr	ra
9fc08400:	01e71023 	subu	v0,t7,a3
	...

9fc08410 <copy_info>:
copy_info():
9fc08410:	94a20002 	lhu	v0,2(a1)
9fc08414:	94a30000 	lhu	v1,0(a1)
9fc08418:	a4820002 	sh	v0,2(a0)
9fc0841c:	03e00008 	jr	ra
9fc08420:	a4830000 	sh	v1,0(a0)
	...

9fc08430 <core_list_insert_new>:
core_list_insert_new():
9fc08430:	8cc80000 	lw	t0,0(a2)
9fc08434:	8fa30010 	lw	v1,16(sp)
9fc08438:	25090008 	addiu	t1,t0,8
9fc0843c:	0123102b 	sltu	v0,t1,v1
9fc08440:	00805021 	move	t2,a0
9fc08444:	14400006 	bnez	v0,9fc08460 <core_list_insert_new+0x30>
9fc08448:	00a01821 	move	v1,a1
9fc0844c:	00004021 	move	t0,zero
9fc08450:	03e00008 	jr	ra
9fc08454:	01001021 	move	v0,t0
	...
9fc08460:	8ce40000 	lw	a0,0(a3)
9fc08464:	8fa50014 	lw	a1,20(sp)
9fc08468:	248c0004 	addiu	t4,a0,4
9fc0846c:	0185582b 	sltu	t3,t4,a1
9fc08470:	1160fff6 	beqz	t3,9fc0844c <core_list_insert_new+0x1c>
9fc08474:	00000000 	nop
9fc08478:	ad040004 	sw	a0,4(t0)
9fc0847c:	8cee0000 	lw	t6,0(a3)
9fc08480:	acc90000 	sw	t1,0(a2)
9fc08484:	25cd0004 	addiu	t5,t6,4
9fc08488:	aced0000 	sw	t5,0(a3)
9fc0848c:	8d490000 	lw	t1,0(t2)
9fc08490:	8d060004 	lw	a2,4(t0)
9fc08494:	94640002 	lhu	a0,2(v1)
9fc08498:	94670000 	lhu	a3,0(v1)
9fc0849c:	ad090000 	sw	t1,0(t0)
9fc084a0:	a4c40002 	sh	a0,2(a2)
9fc084a4:	a4c70000 	sh	a3,0(a2)
9fc084a8:	0bf02114 	j	9fc08450 <core_list_insert_new+0x20>
9fc084ac:	ad480000 	sw	t0,0(t2)

9fc084b0 <core_list_remove>:
core_list_remove():
9fc084b0:	8c820000 	lw	v0,0(a0)
9fc084b4:	8c860004 	lw	a2,4(a0)
9fc084b8:	8c430004 	lw	v1,4(v0)
9fc084bc:	8c450000 	lw	a1,0(v0)
9fc084c0:	ac830004 	sw	v1,4(a0)
9fc084c4:	ac850000 	sw	a1,0(a0)
9fc084c8:	ac460004 	sw	a2,4(v0)
9fc084cc:	03e00008 	jr	ra
9fc084d0:	ac400000 	sw	zero,0(v0)
	...

9fc084e0 <core_list_undo_remove>:
core_list_undo_remove():
9fc084e0:	00801021 	move	v0,a0
9fc084e4:	8c860004 	lw	a2,4(a0)
9fc084e8:	8ca30004 	lw	v1,4(a1)
9fc084ec:	8ca40000 	lw	a0,0(a1)
9fc084f0:	ac430004 	sw	v1,4(v0)
9fc084f4:	ac440000 	sw	a0,0(v0)
9fc084f8:	aca60004 	sw	a2,4(a1)
9fc084fc:	03e00008 	jr	ra
9fc08500:	aca20000 	sw	v0,0(a1)
	...

9fc08510 <core_list_find>:
core_list_find():
9fc08510:	84a60002 	lh	a2,2(a1)
9fc08514:	00000000 	nop
9fc08518:	04c00011 	bltz	a2,9fc08560 <core_list_find+0x50>
9fc0851c:	00000000 	nop
9fc08520:	1080000b 	beqz	a0,9fc08550 <core_list_find+0x40>
9fc08524:	00000000 	nop
9fc08528:	8c870004 	lw	a3,4(a0)
9fc0852c:	00000000 	nop
9fc08530:	84e50002 	lh	a1,2(a3)
9fc08534:	00000000 	nop
9fc08538:	10a60005 	beq	a1,a2,9fc08550 <core_list_find+0x40>
9fc0853c:	00000000 	nop
9fc08540:	8c840000 	lw	a0,0(a0)
9fc08544:	00000000 	nop
9fc08548:	1480fff7 	bnez	a0,9fc08528 <core_list_find+0x18>
9fc0854c:	00000000 	nop
9fc08550:	03e00008 	jr	ra
9fc08554:	00801021 	move	v0,a0
	...
9fc08560:	1080fffb 	beqz	a0,9fc08550 <core_list_find+0x40>
9fc08564:	00000000 	nop
9fc08568:	8c830004 	lw	v1,4(a0)
9fc0856c:	84a50000 	lh	a1,0(a1)
9fc08570:	90620000 	lbu	v0,0(v1)
9fc08574:	00000000 	nop
9fc08578:	1445000b 	bne	v0,a1,9fc085a8 <core_list_find+0x98>
9fc0857c:	00000000 	nop
9fc08580:	0bf02154 	j	9fc08550 <core_list_find+0x40>
9fc08584:	00000000 	nop
	...
9fc08590:	8c880004 	lw	t0,4(a0)
9fc08594:	00000000 	nop
9fc08598:	91060000 	lbu	a2,0(t0)
9fc0859c:	00000000 	nop
9fc085a0:	10c5ffeb 	beq	a2,a1,9fc08550 <core_list_find+0x40>
9fc085a4:	00000000 	nop
9fc085a8:	8c840000 	lw	a0,0(a0)
9fc085ac:	00000000 	nop
9fc085b0:	1480fff7 	bnez	a0,9fc08590 <core_list_find+0x80>
9fc085b4:	00801021 	move	v0,a0
9fc085b8:	03e00008 	jr	ra
9fc085bc:	00000000 	nop

9fc085c0 <core_list_reverse>:
core_list_reverse():
9fc085c0:	10800027 	beqz	a0,9fc08660 <core_list_reverse+0xa0>
9fc085c4:	00801021 	move	v0,a0
9fc085c8:	8c430000 	lw	v1,0(v0)
9fc085cc:	00002021 	move	a0,zero
9fc085d0:	10600023 	beqz	v1,9fc08660 <core_list_reverse+0xa0>
9fc085d4:	ac440000 	sw	a0,0(v0)
9fc085d8:	8c640000 	lw	a0,0(v1)
9fc085dc:	ac620000 	sw	v0,0(v1)
9fc085e0:	1080001f 	beqz	a0,9fc08660 <core_list_reverse+0xa0>
9fc085e4:	00601021 	move	v0,v1
9fc085e8:	8c850000 	lw	a1,0(a0)
9fc085ec:	00801021 	move	v0,a0
9fc085f0:	10a0001b 	beqz	a1,9fc08660 <core_list_reverse+0xa0>
9fc085f4:	ac830000 	sw	v1,0(a0)
9fc085f8:	8ca30000 	lw	v1,0(a1)
9fc085fc:	00a01021 	move	v0,a1
9fc08600:	10600017 	beqz	v1,9fc08660 <core_list_reverse+0xa0>
9fc08604:	aca40000 	sw	a0,0(a1)
9fc08608:	8c640000 	lw	a0,0(v1)
9fc0860c:	00601021 	move	v0,v1
9fc08610:	10800013 	beqz	a0,9fc08660 <core_list_reverse+0xa0>
9fc08614:	ac650000 	sw	a1,0(v1)
9fc08618:	8c850000 	lw	a1,0(a0)
9fc0861c:	00801021 	move	v0,a0
9fc08620:	10a0000f 	beqz	a1,9fc08660 <core_list_reverse+0xa0>
9fc08624:	ac830000 	sw	v1,0(a0)
9fc08628:	8ca30000 	lw	v1,0(a1)
9fc0862c:	00a01021 	move	v0,a1
9fc08630:	1060000b 	beqz	v1,9fc08660 <core_list_reverse+0xa0>
9fc08634:	aca40000 	sw	a0,0(a1)
9fc08638:	00601021 	move	v0,v1
9fc0863c:	8c630000 	lw	v1,0(v1)
9fc08640:	00402021 	move	a0,v0
9fc08644:	10600006 	beqz	v1,9fc08660 <core_list_reverse+0xa0>
9fc08648:	ac450000 	sw	a1,0(v0)
9fc0864c:	00601021 	move	v0,v1
9fc08650:	8c430000 	lw	v1,0(v0)
9fc08654:	00000000 	nop
9fc08658:	1460ffdf 	bnez	v1,9fc085d8 <core_list_reverse+0x18>
9fc0865c:	ac440000 	sw	a0,0(v0)
9fc08660:	03e00008 	jr	ra
9fc08664:	00000000 	nop
	...

9fc08670 <core_list_mergesort>:
core_list_mergesort():
9fc08670:	27bdffc8 	addiu	sp,sp,-56
9fc08674:	afb60028 	sw	s6,40(sp)
9fc08678:	0080b021 	move	s6,a0
9fc0867c:	afbe0030 	sw	s8,48(sp)
9fc08680:	afb50024 	sw	s5,36(sp)
9fc08684:	afbf0034 	sw	ra,52(sp)
9fc08688:	afb7002c 	sw	s7,44(sp)
9fc0868c:	afb40020 	sw	s4,32(sp)
9fc08690:	afb3001c 	sw	s3,28(sp)
9fc08694:	afb20018 	sw	s2,24(sp)
9fc08698:	afb10014 	sw	s1,20(sp)
9fc0869c:	afb00010 	sw	s0,16(sp)
9fc086a0:	00a0f021 	move	s8,a1
9fc086a4:	afa60040 	sw	a2,64(sp)
9fc086a8:	12c00090 	beqz	s6,9fc088ec <core_list_mergesort+0x27c>
9fc086ac:	24150001 	li	s5,1
9fc086b0:	02c09821 	move	s3,s6
9fc086b4:	0000a021 	move	s4,zero
9fc086b8:	0000b021 	move	s6,zero
9fc086bc:	0000b821 	move	s7,zero
9fc086c0:	8e700000 	lw	s0,0(s3)
9fc086c4:	26a2ffff 	addiu	v0,s5,-1
9fc086c8:	26f70001 	addiu	s7,s7,1
9fc086cc:	30430007 	andi	v1,v0,0x7
9fc086d0:	12000053 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc086d4:	24110001 	li	s1,1
9fc086d8:	0235202a 	slt	a0,s1,s5
9fc086dc:	10800050 	beqz	a0,9fc08820 <core_list_mergesort+0x1b0>
9fc086e0:	00000000 	nop
9fc086e4:	1060002c 	beqz	v1,9fc08798 <core_list_mergesort+0x128>
9fc086e8:	00000000 	nop
9fc086ec:	10710023 	beq	v1,s1,9fc0877c <core_list_mergesort+0x10c>
9fc086f0:	24050002 	li	a1,2
9fc086f4:	1065001d 	beq	v1,a1,9fc0876c <core_list_mergesort+0xfc>
9fc086f8:	24060003 	li	a2,3
9fc086fc:	10660017 	beq	v1,a2,9fc0875c <core_list_mergesort+0xec>
9fc08700:	24070004 	li	a3,4
9fc08704:	10670011 	beq	v1,a3,9fc0874c <core_list_mergesort+0xdc>
9fc08708:	24080005 	li	t0,5
9fc0870c:	1068000b 	beq	v1,t0,9fc0873c <core_list_mergesort+0xcc>
9fc08710:	24090006 	li	t1,6
9fc08714:	10690005 	beq	v1,t1,9fc0872c <core_list_mergesort+0xbc>
9fc08718:	00000000 	nop
9fc0871c:	8e100000 	lw	s0,0(s0)
9fc08720:	00000000 	nop
9fc08724:	1200003e 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08728:	24110002 	li	s1,2
9fc0872c:	8e100000 	lw	s0,0(s0)
9fc08730:	00000000 	nop
9fc08734:	1200003a 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08738:	26310001 	addiu	s1,s1,1
9fc0873c:	8e100000 	lw	s0,0(s0)
9fc08740:	00000000 	nop
9fc08744:	12000036 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08748:	26310001 	addiu	s1,s1,1
9fc0874c:	8e100000 	lw	s0,0(s0)
9fc08750:	00000000 	nop
9fc08754:	12000032 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08758:	26310001 	addiu	s1,s1,1
9fc0875c:	8e100000 	lw	s0,0(s0)
9fc08760:	00000000 	nop
9fc08764:	1200002e 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08768:	26310001 	addiu	s1,s1,1
9fc0876c:	8e100000 	lw	s0,0(s0)
9fc08770:	00000000 	nop
9fc08774:	1200002a 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08778:	26310001 	addiu	s1,s1,1
9fc0877c:	8e100000 	lw	s0,0(s0)
9fc08780:	00000000 	nop
9fc08784:	12000026 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08788:	26310001 	addiu	s1,s1,1
9fc0878c:	0235502a 	slt	t2,s1,s5
9fc08790:	11400023 	beqz	t2,9fc08820 <core_list_mergesort+0x1b0>
9fc08794:	00000000 	nop
9fc08798:	8e100000 	lw	s0,0(s0)
9fc0879c:	26310001 	addiu	s1,s1,1
9fc087a0:	1200001f 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087a4:	02201021 	move	v0,s1
9fc087a8:	8e100000 	lw	s0,0(s0)
9fc087ac:	00000000 	nop
9fc087b0:	1200001b 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087b4:	26310001 	addiu	s1,s1,1
9fc087b8:	8e100000 	lw	s0,0(s0)
9fc087bc:	00000000 	nop
9fc087c0:	12000017 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087c4:	24510002 	addiu	s1,v0,2
9fc087c8:	8e100000 	lw	s0,0(s0)
9fc087cc:	00000000 	nop
9fc087d0:	12000013 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087d4:	24510003 	addiu	s1,v0,3
9fc087d8:	8e100000 	lw	s0,0(s0)
9fc087dc:	00000000 	nop
9fc087e0:	1200000f 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087e4:	24510004 	addiu	s1,v0,4
9fc087e8:	8e100000 	lw	s0,0(s0)
9fc087ec:	00000000 	nop
9fc087f0:	1200000b 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc087f4:	24510005 	addiu	s1,v0,5
9fc087f8:	8e100000 	lw	s0,0(s0)
9fc087fc:	00000000 	nop
9fc08800:	12000007 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08804:	24510006 	addiu	s1,v0,6
9fc08808:	8e100000 	lw	s0,0(s0)
9fc0880c:	24510007 	addiu	s1,v0,7
9fc08810:	12000003 	beqz	s0,9fc08820 <core_list_mergesort+0x1b0>
9fc08814:	0235102a 	slt	v0,s1,s5
9fc08818:	1440ffdf 	bnez	v0,9fc08798 <core_list_mergesort+0x128>
9fc0881c:	00000000 	nop
9fc08820:	12200017 	beqz	s1,9fc08880 <core_list_mergesort+0x210>
9fc08824:	02a09021 	move	s2,s5
9fc08828:	12400021 	beqz	s2,9fc088b0 <core_list_mergesort+0x240>
9fc0882c:	00000000 	nop
9fc08830:	1200001f 	beqz	s0,9fc088b0 <core_list_mergesort+0x240>
9fc08834:	00000000 	nop
9fc08838:	8e640004 	lw	a0,4(s3)
9fc0883c:	8e050004 	lw	a1,4(s0)
9fc08840:	8fa60040 	lw	a2,64(sp)
9fc08844:	03c0f809 	jalr	s8
9fc08848:	00000000 	nop
9fc0884c:	18400018 	blez	v0,9fc088b0 <core_list_mergesort+0x240>
9fc08850:	00000000 	nop
9fc08854:	8e040000 	lw	a0,0(s0)
9fc08858:	2652ffff 	addiu	s2,s2,-1
9fc0885c:	02601821 	move	v1,s3
9fc08860:	02001021 	move	v0,s0
9fc08864:	1280000e 	beqz	s4,9fc088a0 <core_list_mergesort+0x230>
9fc08868:	00000000 	nop
9fc0886c:	ae820000 	sw	v0,0(s4)
9fc08870:	0040a021 	move	s4,v0
9fc08874:	00609821 	move	s3,v1
9fc08878:	1620ffeb 	bnez	s1,9fc08828 <core_list_mergesort+0x1b8>
9fc0887c:	00808021 	move	s0,a0
9fc08880:	12400013 	beqz	s2,9fc088d0 <core_list_mergesort+0x260>
9fc08884:	00000000 	nop
9fc08888:	12000013 	beqz	s0,9fc088d8 <core_list_mergesort+0x268>
9fc0888c:	2652ffff 	addiu	s2,s2,-1
9fc08890:	02601821 	move	v1,s3
9fc08894:	8e040000 	lw	a0,0(s0)
9fc08898:	1680fff4 	bnez	s4,9fc0886c <core_list_mergesort+0x1fc>
9fc0889c:	02001021 	move	v0,s0
9fc088a0:	0bf0221c 	j	9fc08870 <core_list_mergesort+0x200>
9fc088a4:	0040b021 	move	s6,v0
	...
9fc088b0:	2631ffff 	addiu	s1,s1,-1
9fc088b4:	02002021 	move	a0,s0
9fc088b8:	8e630000 	lw	v1,0(s3)
9fc088bc:	0bf02219 	j	9fc08864 <core_list_mergesort+0x1f4>
9fc088c0:	02601021 	move	v0,s3
	...
9fc088d0:	1600ff7b 	bnez	s0,9fc086c0 <core_list_mergesort+0x50>
9fc088d4:	02009821 	move	s3,s0
9fc088d8:	24030001 	li	v1,1
9fc088dc:	12e30004 	beq	s7,v1,9fc088f0 <core_list_mergesort+0x280>
9fc088e0:	ae800000 	sw	zero,0(s4)
9fc088e4:	16c0ff72 	bnez	s6,9fc086b0 <core_list_mergesort+0x40>
9fc088e8:	0015a840 	sll	s5,s5,0x1
9fc088ec:	aec00000 	sw	zero,0(s6)
9fc088f0:	8fbf0034 	lw	ra,52(sp)
9fc088f4:	02c01021 	move	v0,s6
9fc088f8:	8fbe0030 	lw	s8,48(sp)
9fc088fc:	8fb7002c 	lw	s7,44(sp)
9fc08900:	8fb60028 	lw	s6,40(sp)
9fc08904:	8fb50024 	lw	s5,36(sp)
9fc08908:	8fb40020 	lw	s4,32(sp)
9fc0890c:	8fb3001c 	lw	s3,28(sp)
9fc08910:	8fb20018 	lw	s2,24(sp)
9fc08914:	8fb10014 	lw	s1,20(sp)
9fc08918:	8fb00010 	lw	s0,16(sp)
9fc0891c:	03e00008 	jr	ra
9fc08920:	27bd0038 	addiu	sp,sp,56
	...

9fc08930 <calc_func>:
calc_func():
9fc08930:	27bdffd0 	addiu	sp,sp,-48
9fc08934:	afb10020 	sw	s1,32(sp)
9fc08938:	84910000 	lh	s1,0(a0)
9fc0893c:	afb30028 	sw	s3,40(sp)
9fc08940:	32220080 	andi	v0,s1,0x80
9fc08944:	afb20024 	sw	s2,36(sp)
9fc08948:	afbf002c 	sw	ra,44(sp)
9fc0894c:	afb0001c 	sw	s0,28(sp)
9fc08950:	00809821 	move	s3,a0
9fc08954:	14400036 	bnez	v0,9fc08a30 <calc_func+0x100>
9fc08958:	00a09021 	move	s2,a1
9fc0895c:	001120c3 	sra	a0,s1,0x3
9fc08960:	3086000f 	andi	a2,a0,0xf
9fc08964:	00061900 	sll	v1,a2,0x4
9fc08968:	32240007 	andi	a0,s1,0x7
9fc0896c:	14800028 	bnez	a0,9fc08a10 <calc_func+0xe0>
9fc08970:	00662825 	or	a1,v1,a2
9fc08974:	28a80022 	slti	t0,a1,34
9fc08978:	11000002 	beqz	t0,9fc08984 <calc_func+0x54>
9fc0897c:	00a01821 	move	v1,a1
9fc08980:	24030022 	li	v1,34
9fc08984:	8e450014 	lw	a1,20(s2)
9fc08988:	8e440018 	lw	a0,24(s2)
9fc0898c:	86460000 	lh	a2,0(s2)
9fc08990:	86470002 	lh	a3,2(s2)
9fc08994:	96490038 	lhu	t1,56(s2)
9fc08998:	afa30010 	sw	v1,16(sp)
9fc0899c:	0ff018e8 	jal	9fc063a0 <core_bench_state>
9fc089a0:	afa90014 	sw	t1,20(sp)
9fc089a4:	9645003e 	lhu	a1,62(s2)
9fc089a8:	00028400 	sll	s0,v0,0x10
9fc089ac:	14a00002 	bnez	a1,9fc089b8 <calc_func+0x88>
9fc089b0:	00108403 	sra	s0,s0,0x10
9fc089b4:	a642003e 	sh	v0,62(s2)
9fc089b8:	3210ffff 	andi	s0,s0,0xffff
9fc089bc:	96450038 	lhu	a1,56(s2)
9fc089c0:	0ff01c04 	jal	9fc07010 <crcu16>
9fc089c4:	02002021 	move	a0,s0
9fc089c8:	240fff00 	li	t7,-256
9fc089cc:	022f7024 	and	t6,s1,t7
9fc089d0:	3210007f 	andi	s0,s0,0x7f
9fc089d4:	35cd0080 	ori	t5,t6,0x80
9fc089d8:	8fbf002c 	lw	ra,44(sp)
9fc089dc:	020d6025 	or	t4,s0,t5
9fc089e0:	a6420038 	sh	v0,56(s2)
9fc089e4:	8fb10020 	lw	s1,32(sp)
9fc089e8:	02001021 	move	v0,s0
9fc089ec:	a66c0000 	sh	t4,0(s3)
9fc089f0:	8fb20024 	lw	s2,36(sp)
9fc089f4:	8fb30028 	lw	s3,40(sp)
9fc089f8:	8fb0001c 	lw	s0,28(sp)
9fc089fc:	03e00008 	jr	ra
9fc08a00:	27bd0030 	addiu	sp,sp,48
	...
9fc08a10:	24070001 	li	a3,1
9fc08a14:	10870012 	beq	a0,a3,9fc08a60 <calc_func+0x130>
9fc08a18:	02208021 	move	s0,s1
9fc08a1c:	0bf0226f 	j	9fc089bc <calc_func+0x8c>
9fc08a20:	3210ffff 	andi	s0,s0,0xffff
	...
9fc08a30:	8fbf002c 	lw	ra,44(sp)
9fc08a34:	3230007f 	andi	s0,s1,0x7f
9fc08a38:	02001021 	move	v0,s0
9fc08a3c:	8fb30028 	lw	s3,40(sp)
9fc08a40:	8fb20024 	lw	s2,36(sp)
9fc08a44:	8fb10020 	lw	s1,32(sp)
9fc08a48:	8fb0001c 	lw	s0,28(sp)
9fc08a4c:	03e00008 	jr	ra
9fc08a50:	27bd0030 	addiu	sp,sp,48
	...
9fc08a60:	96460038 	lhu	a2,56(s2)
9fc08a64:	0ff01058 	jal	9fc04160 <core_bench_matrix>
9fc08a68:	26440028 	addiu	a0,s2,40
9fc08a6c:	964a003c 	lhu	t2,60(s2)
9fc08a70:	00025c00 	sll	t3,v0,0x10
9fc08a74:	1540ffd0 	bnez	t2,9fc089b8 <calc_func+0x88>
9fc08a78:	000b8403 	sra	s0,t3,0x10
9fc08a7c:	0bf0226e 	j	9fc089b8 <calc_func+0x88>
9fc08a80:	a642003c 	sh	v0,60(s2)
	...

9fc08a90 <core_list_init>:
core_list_init():
9fc08a90:	24090014 	li	t1,20
9fc08a94:	15200002 	bnez	t1,9fc08aa0 <core_list_init+0x10>
9fc08a98:	0089001b 	divu	zero,a0,t1
9fc08a9c:	0007000d 	break	0x7
9fc08aa0:	27bdffc8 	addiu	sp,sp,-56
9fc08aa4:	afb10014 	sw	s1,20(sp)
9fc08aa8:	24a90010 	addiu	t1,a1,16
9fc08aac:	00063400 	sll	a2,a2,0x10
9fc08ab0:	24028080 	li	v0,-32640
9fc08ab4:	afb00010 	sw	s0,16(sp)
9fc08ab8:	afbf0034 	sw	ra,52(sp)
9fc08abc:	afbe0030 	sw	s8,48(sp)
9fc08ac0:	afb7002c 	sw	s7,44(sp)
9fc08ac4:	afb60028 	sw	s6,40(sp)
9fc08ac8:	afb50024 	sw	s5,36(sp)
9fc08acc:	afb40020 	sw	s4,32(sp)
9fc08ad0:	afb3001c 	sw	s3,28(sp)
9fc08ad4:	afb20018 	sw	s2,24(sp)
9fc08ad8:	00063403 	sra	a2,a2,0x10
9fc08adc:	aca00000 	sw	zero,0(a1)
9fc08ae0:	24ab0008 	addiu	t3,a1,8
9fc08ae4:	00004012 	mflo	t0
9fc08ae8:	2511fffe 	addiu	s1,t0,-2
9fc08aec:	001138c0 	sll	a3,s1,0x3
9fc08af0:	00a76821 	addu	t5,a1,a3
9fc08af4:	00111880 	sll	v1,s1,0x2
9fc08af8:	012d202b 	sltu	a0,t1,t5
9fc08afc:	a5a20000 	sh	v0,0(t5)
9fc08b00:	01a38021 	addu	s0,t5,v1
9fc08b04:	acad0004 	sw	t5,4(a1)
9fc08b08:	a5a00002 	sh	zero,2(t5)
9fc08b0c:	10800188 	beqz	a0,9fc09130 <core_list_init+0x6a0>
9fc08b10:	25aa0004 	addiu	t2,t5,4
9fc08b14:	25a70008 	addiu	a3,t5,8
9fc08b18:	00f0602b 	sltu	t4,a3,s0
9fc08b1c:	11800184 	beqz	t4,9fc09130 <core_list_init+0x6a0>
9fc08b20:	240f7fff 	li	t7,32767
9fc08b24:	240effff 	li	t6,-1
9fc08b28:	ad600000 	sw	zero,0(t3)
9fc08b2c:	01604021 	move	t0,t3
9fc08b30:	a54f0002 	sh	t7,2(t2)
9fc08b34:	a5ae0004 	sh	t6,4(t5)
9fc08b38:	ad6a0004 	sw	t2,4(t3)
9fc08b3c:	acab0000 	sw	t3,0(a1)
9fc08b40:	122000c4 	beqz	s1,9fc08e54 <core_list_init+0x3c4>
9fc08b44:	262affff 	addiu	t2,s1,-1
9fc08b48:	31440003 	andi	a0,t2,0x3
9fc08b4c:	30cfffff 	andi	t7,a2,0xffff
9fc08b50:	00006021 	move	t4,zero
9fc08b54:	10800077 	beqz	a0,9fc08d34 <core_list_init+0x2a4>
9fc08b58:	24127fff 	li	s2,32767
9fc08b5c:	252b0008 	addiu	t3,t1,8
9fc08b60:	016da02b 	sltu	s4,t3,t5
9fc08b64:	1680007a 	bnez	s4,9fc08d50 <core_list_init+0x2c0>
9fc08b68:	24ea0004 	addiu	t2,a3,4
9fc08b6c:	01205821 	move	t3,t1
9fc08b70:	00e05021 	move	t2,a3
9fc08b74:	240c0001 	li	t4,1
9fc08b78:	01604821 	move	t1,t3
9fc08b7c:	108c006d 	beq	a0,t4,9fc08d34 <core_list_init+0x2a4>
9fc08b80:	01403821 	move	a3,t2
9fc08b84:	241e0002 	li	s8,2
9fc08b88:	109e000a 	beq	a0,s8,9fc08bb4 <core_list_init+0x124>
9fc08b8c:	00000000 	nop
9fc08b90:	25670008 	addiu	a3,t3,8
9fc08b94:	00edf82b 	sltu	ra,a3,t5
9fc08b98:	17e0007d 	bnez	ra,9fc08d90 <core_list_init+0x300>
9fc08b9c:	254e0004 	addiu	t6,t2,4
9fc08ba0:	01603821 	move	a3,t3
9fc08ba4:	01407021 	move	t6,t2
9fc08ba8:	00e04821 	move	t1,a3
9fc08bac:	258c0001 	addiu	t4,t4,1
9fc08bb0:	01c03821 	move	a3,t6
9fc08bb4:	252a0008 	addiu	t2,t1,8
9fc08bb8:	014d582b 	sltu	t3,t2,t5
9fc08bbc:	15600088 	bnez	t3,9fc08de0 <core_list_init+0x350>
9fc08bc0:	24eb0004 	addiu	t3,a3,4
9fc08bc4:	01205021 	move	t2,t1
9fc08bc8:	00e05821 	move	t3,a3
9fc08bcc:	258c0001 	addiu	t4,t4,1
9fc08bd0:	01404821 	move	t1,t2
9fc08bd4:	0bf0234d 	j	9fc08d34 <core_list_init+0x2a4>
9fc08bd8:	01603821 	move	a3,t3
9fc08bdc:	24ea0004 	addiu	t2,a3,4
9fc08be0:	0150a02b 	sltu	s4,t2,s0
9fc08be4:	12800057 	beqz	s4,9fc08d44 <core_list_init+0x2b4>
9fc08be8:	319effff 	andi	s8,t4,0xffff
9fc08bec:	03cf2026 	xor	a0,s8,t7
9fc08bf0:	309f000f 	andi	ra,a0,0xf
9fc08bf4:	001fc0c0 	sll	t8,ra,0x3
9fc08bf8:	33d90007 	andi	t9,s8,0x7
9fc08bfc:	0319b825 	or	s7,t8,t9
9fc08c00:	0017b200 	sll	s6,s7,0x8
9fc08c04:	02d7a825 	or	s5,s6,s7
9fc08c08:	ad280000 	sw	t0,0(t1)
9fc08c0c:	a4f50000 	sh	s5,0(a3)
9fc08c10:	01204021 	move	t0,t1
9fc08c14:	a4f20002 	sh	s2,2(a3)
9fc08c18:	aca90000 	sw	t1,0(a1)
9fc08c1c:	ad270004 	sw	a3,4(t1)
9fc08c20:	258e0001 	addiu	t6,t4,1
9fc08c24:	01d1182b 	sltu	v1,t6,s1
9fc08c28:	1060008a 	beqz	v1,9fc08e54 <core_list_init+0x3c4>
9fc08c2c:	256c0008 	addiu	t4,t3,8
9fc08c30:	018d102b 	sltu	v0,t4,t5
9fc08c34:	10400084 	beqz	v0,9fc08e48 <core_list_init+0x3b8>
9fc08c38:	00000000 	nop
9fc08c3c:	25470004 	addiu	a3,t2,4
9fc08c40:	00f0482b 	sltu	t1,a3,s0
9fc08c44:	11200080 	beqz	t1,9fc08e48 <core_list_init+0x3b8>
9fc08c48:	31d9ffff 	andi	t9,t6,0xffff
9fc08c4c:	032ff826 	xor	ra,t9,t7
9fc08c50:	33fe000f 	andi	s8,ra,0xf
9fc08c54:	001eb8c0 	sll	s7,s8,0x3
9fc08c58:	33380007 	andi	t8,t9,0x7
9fc08c5c:	02f8b025 	or	s6,s7,t8
9fc08c60:	0016aa00 	sll	s5,s6,0x8
9fc08c64:	02b6a025 	or	s4,s5,s6
9fc08c68:	ad680000 	sw	t0,0(t3)
9fc08c6c:	a5540000 	sh	s4,0(t2)
9fc08c70:	01604021 	move	t0,t3
9fc08c74:	a5520002 	sh	s2,2(t2)
9fc08c78:	acab0000 	sw	t3,0(a1)
9fc08c7c:	ad6a0004 	sw	t2,4(t3)
9fc08c80:	258a0008 	addiu	t2,t4,8
9fc08c84:	014d582b 	sltu	t3,t2,t5
9fc08c88:	1160006c 	beqz	t3,9fc08e3c <core_list_init+0x3ac>
9fc08c8c:	25c30001 	addiu	v1,t6,1
9fc08c90:	24e90004 	addiu	t1,a3,4
9fc08c94:	0130202b 	sltu	a0,t1,s0
9fc08c98:	10800068 	beqz	a0,9fc08e3c <core_list_init+0x3ac>
9fc08c9c:	3076ffff 	andi	s6,v1,0xffff
9fc08ca0:	02cfc026 	xor	t8,s6,t7
9fc08ca4:	3317000f 	andi	s7,t8,0xf
9fc08ca8:	0017a0c0 	sll	s4,s7,0x3
9fc08cac:	32d50007 	andi	s5,s6,0x7
9fc08cb0:	02951825 	or	v1,s4,s5
9fc08cb4:	00039a00 	sll	s3,v1,0x8
9fc08cb8:	02631025 	or	v0,s3,v1
9fc08cbc:	ad880000 	sw	t0,0(t4)
9fc08cc0:	a4e20000 	sh	v0,0(a3)
9fc08cc4:	01804021 	move	t0,t4
9fc08cc8:	a4f20002 	sh	s2,2(a3)
9fc08ccc:	acac0000 	sw	t4,0(a1)
9fc08cd0:	ad870004 	sw	a3,4(t4)
9fc08cd4:	25470008 	addiu	a3,t2,8
9fc08cd8:	00ed602b 	sltu	t4,a3,t5
9fc08cdc:	11800054 	beqz	t4,9fc08e30 <core_list_init+0x3a0>
9fc08ce0:	25c30002 	addiu	v1,t6,2
9fc08ce4:	252b0004 	addiu	t3,t1,4
9fc08ce8:	0170c82b 	sltu	t9,t3,s0
9fc08cec:	13200050 	beqz	t9,9fc08e30 <core_list_init+0x3a0>
9fc08cf0:	3074ffff 	andi	s4,v1,0xffff
9fc08cf4:	028fa826 	xor	s5,s4,t7
9fc08cf8:	32a3000f 	andi	v1,s5,0xf
9fc08cfc:	000398c0 	sll	s3,v1,0x3
9fc08d00:	32820007 	andi	v0,s4,0x7
9fc08d04:	02622025 	or	a0,s3,v0
9fc08d08:	0004fa00 	sll	ra,a0,0x8
9fc08d0c:	03e4f025 	or	s8,ra,a0
9fc08d10:	ad480000 	sw	t0,0(t2)
9fc08d14:	a53e0000 	sh	s8,0(t1)
9fc08d18:	01404021 	move	t0,t2
9fc08d1c:	a5320002 	sh	s2,2(t1)
9fc08d20:	acaa0000 	sw	t2,0(a1)
9fc08d24:	ad490004 	sw	t1,4(t2)
9fc08d28:	00e04821 	move	t1,a3
9fc08d2c:	25cc0003 	addiu	t4,t6,3
9fc08d30:	01603821 	move	a3,t3
9fc08d34:	252b0008 	addiu	t3,t1,8
9fc08d38:	016d982b 	sltu	s3,t3,t5
9fc08d3c:	1660ffa7 	bnez	s3,9fc08bdc <core_list_init+0x14c>
9fc08d40:	00000000 	nop
9fc08d44:	01205821 	move	t3,t1
9fc08d48:	0bf02308 	j	9fc08c20 <core_list_init+0x190>
9fc08d4c:	00e05021 	move	t2,a3
9fc08d50:	0150a82b 	sltu	s5,t2,s0
9fc08d54:	12a0ff85 	beqz	s5,9fc08b6c <core_list_init+0xdc>
9fc08d58:	31f9000f 	andi	t9,t7,0xf
9fc08d5c:	0019c0c0 	sll	t8,t9,0x3
9fc08d60:	0018ba00 	sll	s7,t8,0x8
9fc08d64:	02f8b025 	or	s6,s7,t8
9fc08d68:	ad280000 	sw	t0,0(t1)
9fc08d6c:	a4f60000 	sh	s6,0(a3)
9fc08d70:	01204021 	move	t0,t1
9fc08d74:	a4f20002 	sh	s2,2(a3)
9fc08d78:	aca90000 	sw	t1,0(a1)
9fc08d7c:	0bf022dd 	j	9fc08b74 <core_list_init+0xe4>
9fc08d80:	ad270004 	sw	a3,4(t1)
	...
9fc08d90:	01d0202b 	sltu	a0,t6,s0
9fc08d94:	1080ff82 	beqz	a0,9fc08ba0 <core_list_init+0x110>
9fc08d98:	3195ffff 	andi	s5,t4,0xffff
9fc08d9c:	02afb826 	xor	s7,s5,t7
9fc08da0:	32f6000f 	andi	s6,s7,0xf
9fc08da4:	001698c0 	sll	s3,s6,0x3
9fc08da8:	32b40007 	andi	s4,s5,0x7
9fc08dac:	02741825 	or	v1,s3,s4
9fc08db0:	00034a00 	sll	t1,v1,0x8
9fc08db4:	01231025 	or	v0,t1,v1
9fc08db8:	ad680000 	sw	t0,0(t3)
9fc08dbc:	a5420000 	sh	v0,0(t2)
9fc08dc0:	01604021 	move	t0,t3
9fc08dc4:	a5520002 	sh	s2,2(t2)
9fc08dc8:	acab0000 	sw	t3,0(a1)
9fc08dcc:	0bf022ea 	j	9fc08ba8 <core_list_init+0x118>
9fc08dd0:	ad6a0004 	sw	t2,4(t3)
	...
9fc08de0:	0170702b 	sltu	t6,t3,s0
9fc08de4:	11c0ff77 	beqz	t6,9fc08bc4 <core_list_init+0x134>
9fc08de8:	3182ffff 	andi	v0,t4,0xffff
9fc08dec:	004f9826 	xor	s3,v0,t7
9fc08df0:	3263000f 	andi	v1,s3,0xf
9fc08df4:	0003f8c0 	sll	ra,v1,0x3
9fc08df8:	30440007 	andi	a0,v0,0x7
9fc08dfc:	03e4f025 	or	s8,ra,a0
9fc08e00:	001eca00 	sll	t9,s8,0x8
9fc08e04:	033ec025 	or	t8,t9,s8
9fc08e08:	ad280000 	sw	t0,0(t1)
9fc08e0c:	a4f80000 	sh	t8,0(a3)
9fc08e10:	01204021 	move	t0,t1
9fc08e14:	a4f20002 	sh	s2,2(a3)
9fc08e18:	aca90000 	sw	t1,0(a1)
9fc08e1c:	0bf022f3 	j	9fc08bcc <core_list_init+0x13c>
9fc08e20:	ad270004 	sw	a3,4(t1)
	...
9fc08e30:	01403821 	move	a3,t2
9fc08e34:	0bf0234a 	j	9fc08d28 <core_list_init+0x298>
9fc08e38:	01205821 	move	t3,t1
9fc08e3c:	01805021 	move	t2,t4
9fc08e40:	0bf02335 	j	9fc08cd4 <core_list_init+0x244>
9fc08e44:	00e04821 	move	t1,a3
9fc08e48:	01606021 	move	t4,t3
9fc08e4c:	0bf02320 	j	9fc08c80 <core_list_init+0x1f0>
9fc08e50:	01403821 	move	a3,t2
9fc08e54:	240b0005 	li	t3,5
9fc08e58:	15600002 	bnez	t3,9fc08e64 <core_list_init+0x3d4>
9fc08e5c:	022b001b 	divu	zero,s1,t3
9fc08e60:	0007000d 	break	0x7
9fc08e64:	24090002 	li	t1,2
9fc08e68:	00002012 	mflo	a0
9fc08e6c:	0bf023a3 	j	9fc08e8c <core_list_init+0x3fc>
9fc08e70:	24070001 	li	a3,1
9fc08e74:	8d0d0004 	lw	t5,4(t0)
9fc08e78:	00000000 	nop
9fc08e7c:	a5a70002 	sh	a3,2(t5)
9fc08e80:	25290001 	addiu	t1,t1,1
9fc08e84:	24e70001 	addiu	a3,a3,1
9fc08e88:	00604021 	move	t0,v1
9fc08e8c:	312e0007 	andi	t6,t1,0x7
9fc08e90:	000e9200 	sll	s2,t6,0x8
9fc08e94:	00c76026 	xor	t4,a2,a3
9fc08e98:	8d030000 	lw	v1,0(t0)
9fc08e9c:	024c8825 	or	s1,s2,t4
9fc08ea0:	322a3fff 	andi	t2,s1,0x3fff
9fc08ea4:	10600006 	beqz	v1,9fc08ec0 <core_list_init+0x430>
9fc08ea8:	00e4102b 	sltu	v0,a3,a0
9fc08eac:	1440fff1 	bnez	v0,9fc08e74 <core_list_init+0x3e4>
9fc08eb0:	00000000 	nop
9fc08eb4:	8d100004 	lw	s0,4(t0)
9fc08eb8:	0bf023a0 	j	9fc08e80 <core_list_init+0x3f0>
9fc08ebc:	a60a0002 	sh	t2,2(s0)
9fc08ec0:	24150001 	li	s5,1
9fc08ec4:	10a0008b 	beqz	a1,9fc090f4 <core_list_init+0x664>
9fc08ec8:	241e0001 	li	s8,1
9fc08ecc:	00a08821 	move	s1,a1
9fc08ed0:	0000b821 	move	s7,zero
9fc08ed4:	0000a021 	move	s4,zero
9fc08ed8:	0000b021 	move	s6,zero
9fc08edc:	8e300000 	lw	s0,0(s1)
9fc08ee0:	26a5ffff 	addiu	a1,s5,-1
9fc08ee4:	26f70001 	addiu	s7,s7,1
9fc08ee8:	30a30007 	andi	v1,a1,0x7
9fc08eec:	12000053 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08ef0:	24120001 	li	s2,1
9fc08ef4:	0255382a 	slt	a3,s2,s5
9fc08ef8:	10e00050 	beqz	a3,9fc0903c <core_list_init+0x5ac>
9fc08efc:	00000000 	nop
9fc08f00:	1060002c 	beqz	v1,9fc08fb4 <core_list_init+0x524>
9fc08f04:	00000000 	nop
9fc08f08:	10720023 	beq	v1,s2,9fc08f98 <core_list_init+0x508>
9fc08f0c:	24080002 	li	t0,2
9fc08f10:	1068001d 	beq	v1,t0,9fc08f88 <core_list_init+0x4f8>
9fc08f14:	24060003 	li	a2,3
9fc08f18:	10660017 	beq	v1,a2,9fc08f78 <core_list_init+0x4e8>
9fc08f1c:	24090004 	li	t1,4
9fc08f20:	10690011 	beq	v1,t1,9fc08f68 <core_list_init+0x4d8>
9fc08f24:	240f0005 	li	t7,5
9fc08f28:	106f000b 	beq	v1,t7,9fc08f58 <core_list_init+0x4c8>
9fc08f2c:	240a0006 	li	t2,6
9fc08f30:	106a0005 	beq	v1,t2,9fc08f48 <core_list_init+0x4b8>
9fc08f34:	00000000 	nop
9fc08f38:	8e100000 	lw	s0,0(s0)
9fc08f3c:	00000000 	nop
9fc08f40:	1200003e 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f44:	24120002 	li	s2,2
9fc08f48:	8e100000 	lw	s0,0(s0)
9fc08f4c:	00000000 	nop
9fc08f50:	1200003a 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f54:	26520001 	addiu	s2,s2,1
9fc08f58:	8e100000 	lw	s0,0(s0)
9fc08f5c:	00000000 	nop
9fc08f60:	12000036 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f64:	26520001 	addiu	s2,s2,1
9fc08f68:	8e100000 	lw	s0,0(s0)
9fc08f6c:	00000000 	nop
9fc08f70:	12000032 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f74:	26520001 	addiu	s2,s2,1
9fc08f78:	8e100000 	lw	s0,0(s0)
9fc08f7c:	00000000 	nop
9fc08f80:	1200002e 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f84:	26520001 	addiu	s2,s2,1
9fc08f88:	8e100000 	lw	s0,0(s0)
9fc08f8c:	00000000 	nop
9fc08f90:	1200002a 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08f94:	26520001 	addiu	s2,s2,1
9fc08f98:	8e100000 	lw	s0,0(s0)
9fc08f9c:	00000000 	nop
9fc08fa0:	12000026 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08fa4:	26520001 	addiu	s2,s2,1
9fc08fa8:	0255982a 	slt	s3,s2,s5
9fc08fac:	12600023 	beqz	s3,9fc0903c <core_list_init+0x5ac>
9fc08fb0:	00000000 	nop
9fc08fb4:	8e100000 	lw	s0,0(s0)
9fc08fb8:	26520001 	addiu	s2,s2,1
9fc08fbc:	1200001f 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08fc0:	02401021 	move	v0,s2
9fc08fc4:	8e100000 	lw	s0,0(s0)
9fc08fc8:	00000000 	nop
9fc08fcc:	1200001b 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08fd0:	26520001 	addiu	s2,s2,1
9fc08fd4:	8e100000 	lw	s0,0(s0)
9fc08fd8:	00000000 	nop
9fc08fdc:	12000017 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08fe0:	24520002 	addiu	s2,v0,2
9fc08fe4:	8e100000 	lw	s0,0(s0)
9fc08fe8:	00000000 	nop
9fc08fec:	12000013 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc08ff0:	24520003 	addiu	s2,v0,3
9fc08ff4:	8e100000 	lw	s0,0(s0)
9fc08ff8:	00000000 	nop
9fc08ffc:	1200000f 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc09000:	24520004 	addiu	s2,v0,4
9fc09004:	8e100000 	lw	s0,0(s0)
9fc09008:	00000000 	nop
9fc0900c:	1200000b 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc09010:	24520005 	addiu	s2,v0,5
9fc09014:	8e100000 	lw	s0,0(s0)
9fc09018:	00000000 	nop
9fc0901c:	12000007 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc09020:	24520006 	addiu	s2,v0,6
9fc09024:	8e100000 	lw	s0,0(s0)
9fc09028:	24520007 	addiu	s2,v0,7
9fc0902c:	12000003 	beqz	s0,9fc0903c <core_list_init+0x5ac>
9fc09030:	0255102a 	slt	v0,s2,s5
9fc09034:	1440ffdf 	bnez	v0,9fc08fb4 <core_list_init+0x524>
9fc09038:	00000000 	nop
9fc0903c:	12400016 	beqz	s2,9fc09098 <core_list_init+0x608>
9fc09040:	02a09821 	move	s3,s5
9fc09044:	1260001e 	beqz	s3,9fc090c0 <core_list_init+0x630>
9fc09048:	00000000 	nop
9fc0904c:	1200001c 	beqz	s0,9fc090c0 <core_list_init+0x630>
9fc09050:	00000000 	nop
9fc09054:	8e240004 	lw	a0,4(s1)
9fc09058:	8e050004 	lw	a1,4(s0)
9fc0905c:	0ff020e8 	jal	9fc083a0 <cmp_idx>
9fc09060:	00003021 	move	a2,zero
9fc09064:	18400016 	blez	v0,9fc090c0 <core_list_init+0x630>
9fc09068:	00000000 	nop
9fc0906c:	8e020000 	lw	v0,0(s0)
9fc09070:	02201821 	move	v1,s1
9fc09074:	2673ffff 	addiu	s3,s3,-1
9fc09078:	02008821 	move	s1,s0
9fc0907c:	1280000e 	beqz	s4,9fc090b8 <core_list_init+0x628>
9fc09080:	00000000 	nop
9fc09084:	ae910000 	sw	s1,0(s4)
9fc09088:	0220a021 	move	s4,s1
9fc0908c:	00408021 	move	s0,v0
9fc09090:	1640ffec 	bnez	s2,9fc09044 <core_list_init+0x5b4>
9fc09094:	00608821 	move	s1,v1
9fc09098:	1260000d 	beqz	s3,9fc090d0 <core_list_init+0x640>
9fc0909c:	00000000 	nop
9fc090a0:	1200000f 	beqz	s0,9fc090e0 <core_list_init+0x650>
9fc090a4:	02201821 	move	v1,s1
9fc090a8:	2673ffff 	addiu	s3,s3,-1
9fc090ac:	8e020000 	lw	v0,0(s0)
9fc090b0:	1680fff4 	bnez	s4,9fc09084 <core_list_init+0x5f4>
9fc090b4:	02008821 	move	s1,s0
9fc090b8:	0bf02422 	j	9fc09088 <core_list_init+0x5f8>
9fc090bc:	0220b021 	move	s6,s1
9fc090c0:	2652ffff 	addiu	s2,s2,-1
9fc090c4:	8e230000 	lw	v1,0(s1)
9fc090c8:	0bf0241f 	j	9fc0907c <core_list_init+0x5ec>
9fc090cc:	02001021 	move	v0,s0
9fc090d0:	1600ff82 	bnez	s0,9fc08edc <core_list_init+0x44c>
9fc090d4:	02008821 	move	s1,s0
	...
9fc090e0:	12fe0006 	beq	s7,s8,9fc090fc <core_list_init+0x66c>
9fc090e4:	ae800000 	sw	zero,0(s4)
9fc090e8:	02c02821 	move	a1,s6
9fc090ec:	14a0ff77 	bnez	a1,9fc08ecc <core_list_init+0x43c>
9fc090f0:	0015a840 	sll	s5,s5,0x1
9fc090f4:	aca00000 	sw	zero,0(a1)
9fc090f8:	0000b021 	move	s6,zero
9fc090fc:	8fbf0034 	lw	ra,52(sp)
9fc09100:	02c01021 	move	v0,s6
9fc09104:	8fbe0030 	lw	s8,48(sp)
9fc09108:	8fb7002c 	lw	s7,44(sp)
9fc0910c:	8fb60028 	lw	s6,40(sp)
9fc09110:	8fb50024 	lw	s5,36(sp)
9fc09114:	8fb40020 	lw	s4,32(sp)
9fc09118:	8fb3001c 	lw	s3,28(sp)
9fc0911c:	8fb20018 	lw	s2,24(sp)
9fc09120:	8fb10014 	lw	s1,20(sp)
9fc09124:	8fb00010 	lw	s0,16(sp)
9fc09128:	03e00008 	jr	ra
9fc0912c:	27bd0038 	addiu	sp,sp,56
9fc09130:	8ca80000 	lw	t0,0(a1)
9fc09134:	01604821 	move	t1,t3
9fc09138:	0bf022d0 	j	9fc08b40 <core_list_init+0xb0>
9fc0913c:	01403821 	move	a3,t2

9fc09140 <cmp_complex>:
cmp_complex():
9fc09140:	27bdffc8 	addiu	sp,sp,-56
9fc09144:	afb10020 	sw	s1,32(sp)
9fc09148:	84910000 	lh	s1,0(a0)
9fc0914c:	afb50030 	sw	s5,48(sp)
9fc09150:	32220080 	andi	v0,s1,0x80
9fc09154:	afb4002c 	sw	s4,44(sp)
9fc09158:	afb20024 	sw	s2,36(sp)
9fc0915c:	afbf0034 	sw	ra,52(sp)
9fc09160:	afb30028 	sw	s3,40(sp)
9fc09164:	afb0001c 	sw	s0,28(sp)
9fc09168:	0080a021 	move	s4,a0
9fc0916c:	00a0a821 	move	s5,a1
9fc09170:	1440003b 	bnez	v0,9fc09260 <cmp_complex+0x120>
9fc09174:	00c09021 	move	s2,a2
9fc09178:	001120c3 	sra	a0,s1,0x3
9fc0917c:	3086000f 	andi	a2,a0,0xf
9fc09180:	00061900 	sll	v1,a2,0x4
9fc09184:	32240007 	andi	a0,s1,0x7
9fc09188:	1480002d 	bnez	a0,9fc09240 <cmp_complex+0x100>
9fc0918c:	00662825 	or	a1,v1,a2
9fc09190:	28a80022 	slti	t0,a1,34
9fc09194:	11000002 	beqz	t0,9fc091a0 <cmp_complex+0x60>
9fc09198:	00a01821 	move	v1,a1
9fc0919c:	24030022 	li	v1,34
9fc091a0:	8e450014 	lw	a1,20(s2)
9fc091a4:	8e440018 	lw	a0,24(s2)
9fc091a8:	86460000 	lh	a2,0(s2)
9fc091ac:	86470002 	lh	a3,2(s2)
9fc091b0:	96490038 	lhu	t1,56(s2)
9fc091b4:	afa30010 	sw	v1,16(sp)
9fc091b8:	0ff018e8 	jal	9fc063a0 <core_bench_state>
9fc091bc:	afa90014 	sw	t1,20(sp)
9fc091c0:	9645003e 	lhu	a1,62(s2)
9fc091c4:	00028400 	sll	s0,v0,0x10
9fc091c8:	14a00002 	bnez	a1,9fc091d4 <cmp_complex+0x94>
9fc091cc:	00108403 	sra	s0,s0,0x10
9fc091d0:	a642003e 	sh	v0,62(s2)
9fc091d4:	3210ffff 	andi	s0,s0,0xffff
9fc091d8:	96450038 	lhu	a1,56(s2)
9fc091dc:	0ff01c04 	jal	9fc07010 <crcu16>
9fc091e0:	02002021 	move	a0,s0
9fc091e4:	240fff00 	li	t7,-256
9fc091e8:	022f7024 	and	t6,s1,t7
9fc091ec:	3213007f 	andi	s3,s0,0x7f
9fc091f0:	35cd0080 	ori	t5,t6,0x80
9fc091f4:	026d6025 	or	t4,s3,t5
9fc091f8:	a6420038 	sh	v0,56(s2)
9fc091fc:	a68c0000 	sh	t4,0(s4)
9fc09200:	86b10000 	lh	s1,0(s5)
9fc09204:	00000000 	nop
9fc09208:	32340080 	andi	s4,s1,0x80
9fc0920c:	1280001a 	beqz	s4,9fc09278 <cmp_complex+0x138>
9fc09210:	0011f8c3 	sra	ra,s1,0x3
9fc09214:	3230007f 	andi	s0,s1,0x7f
9fc09218:	8fbf0034 	lw	ra,52(sp)
9fc0921c:	02701023 	subu	v0,s3,s0
9fc09220:	8fb50030 	lw	s5,48(sp)
9fc09224:	8fb4002c 	lw	s4,44(sp)
9fc09228:	8fb30028 	lw	s3,40(sp)
9fc0922c:	8fb20024 	lw	s2,36(sp)
9fc09230:	8fb10020 	lw	s1,32(sp)
9fc09234:	8fb0001c 	lw	s0,28(sp)
9fc09238:	03e00008 	jr	ra
9fc0923c:	27bd0038 	addiu	sp,sp,56
9fc09240:	24070001 	li	a3,1
9fc09244:	10870042 	beq	a0,a3,9fc09350 <cmp_complex+0x210>
9fc09248:	02208021 	move	s0,s1
9fc0924c:	0bf02476 	j	9fc091d8 <cmp_complex+0x98>
9fc09250:	3210ffff 	andi	s0,s0,0xffff
	...
9fc09260:	3233007f 	andi	s3,s1,0x7f
9fc09264:	86b10000 	lh	s1,0(s5)
9fc09268:	00000000 	nop
9fc0926c:	32340080 	andi	s4,s1,0x80
9fc09270:	1680ffe8 	bnez	s4,9fc09214 <cmp_complex+0xd4>
9fc09274:	0011f8c3 	sra	ra,s1,0x3
9fc09278:	33f8000f 	andi	t8,ra,0xf
9fc0927c:	0018c900 	sll	t9,t8,0x4
9fc09280:	32240007 	andi	a0,s1,0x7
9fc09284:	14800016 	bnez	a0,9fc092e0 <cmp_complex+0x1a0>
9fc09288:	03382825 	or	a1,t9,t8
9fc0928c:	28a60022 	slti	a2,a1,34
9fc09290:	14c00002 	bnez	a2,9fc0929c <cmp_complex+0x15c>
9fc09294:	24030022 	li	v1,34
9fc09298:	00a01821 	move	v1,a1
9fc0929c:	8e440018 	lw	a0,24(s2)
9fc092a0:	86470002 	lh	a3,2(s2)
9fc092a4:	8e450014 	lw	a1,20(s2)
9fc092a8:	86460000 	lh	a2,0(s2)
9fc092ac:	96480038 	lhu	t0,56(s2)
9fc092b0:	afa30010 	sw	v1,16(sp)
9fc092b4:	0ff018e8 	jal	9fc063a0 <core_bench_state>
9fc092b8:	afa80014 	sw	t0,20(sp)
9fc092bc:	9644003e 	lhu	a0,62(s2)
9fc092c0:	00023c00 	sll	a3,v0,0x10
9fc092c4:	14800009 	bnez	a0,9fc092ec <cmp_complex+0x1ac>
9fc092c8:	00078403 	sra	s0,a3,0x10
9fc092cc:	0bf024bb 	j	9fc092ec <cmp_complex+0x1ac>
9fc092d0:	a642003e 	sh	v0,62(s2)
	...
9fc092e0:	24020001 	li	v0,1
9fc092e4:	1082000e 	beq	a0,v0,9fc09320 <cmp_complex+0x1e0>
9fc092e8:	02208021 	move	s0,s1
9fc092ec:	3210ffff 	andi	s0,s0,0xffff
9fc092f0:	96450038 	lhu	a1,56(s2)
9fc092f4:	0ff01c04 	jal	9fc07010 <crcu16>
9fc092f8:	02002021 	move	a0,s0
9fc092fc:	240bff00 	li	t3,-256
9fc09300:	022b5024 	and	t2,s1,t3
9fc09304:	3210007f 	andi	s0,s0,0x7f
9fc09308:	35430080 	ori	v1,t2,0x80
9fc0930c:	02038825 	or	s1,s0,v1
9fc09310:	a6420038 	sh	v0,56(s2)
9fc09314:	0bf02486 	j	9fc09218 <cmp_complex+0xd8>
9fc09318:	a6b10000 	sh	s1,0(s5)
9fc0931c:	00000000 	nop
9fc09320:	96460038 	lhu	a2,56(s2)
9fc09324:	0ff01058 	jal	9fc04160 <core_bench_matrix>
9fc09328:	26440028 	addiu	a0,s2,40
9fc0932c:	9645003c 	lhu	a1,60(s2)
9fc09330:	00024c00 	sll	t1,v0,0x10
9fc09334:	14a0ffed 	bnez	a1,9fc092ec <cmp_complex+0x1ac>
9fc09338:	00098403 	sra	s0,t1,0x10
9fc0933c:	0bf024bb 	j	9fc092ec <cmp_complex+0x1ac>
9fc09340:	a642003c 	sh	v0,60(s2)
	...
9fc09350:	96460038 	lhu	a2,56(s2)
9fc09354:	0ff01058 	jal	9fc04160 <core_bench_matrix>
9fc09358:	26440028 	addiu	a0,s2,40
9fc0935c:	964a003c 	lhu	t2,60(s2)
9fc09360:	00025c00 	sll	t3,v0,0x10
9fc09364:	1540ff9b 	bnez	t2,9fc091d4 <cmp_complex+0x94>
9fc09368:	000b8403 	sra	s0,t3,0x10
9fc0936c:	0bf02475 	j	9fc091d4 <cmp_complex+0x94>
9fc09370:	a642003c 	sh	v0,60(s2)
	...

9fc09380 <core_bench_list>:
core_bench_list():
9fc09380:	848a0004 	lh	t2,4(a0)
9fc09384:	27bdffc0 	addiu	sp,sp,-64
9fc09388:	00052c00 	sll	a1,a1,0x10
9fc0938c:	afb5002c 	sw	s5,44(sp)
9fc09390:	afbf003c 	sw	ra,60(sp)
9fc09394:	afbe0038 	sw	s8,56(sp)
9fc09398:	afb70034 	sw	s7,52(sp)
9fc0939c:	afb60030 	sw	s6,48(sp)
9fc093a0:	afb40028 	sw	s4,40(sp)
9fc093a4:	afb30024 	sw	s3,36(sp)
9fc093a8:	afb20020 	sw	s2,32(sp)
9fc093ac:	afb1001c 	sw	s1,28(sp)
9fc093b0:	afb00018 	sw	s0,24(sp)
9fc093b4:	afa40040 	sw	a0,64(sp)
9fc093b8:	8c950024 	lw	s5,36(a0)
9fc093bc:	1940028f 	blez	t2,9fc09dfc <core_bench_list+0xa7c>
9fc093c0:	00052c03 	sra	a1,a1,0x10
9fc093c4:	afa50010 	sw	a1,16(sp)
9fc093c8:	00004821 	move	t1,zero
9fc093cc:	00004021 	move	t0,zero
9fc093d0:	00005821 	move	t3,zero
9fc093d4:	00003821 	move	a3,zero
9fc093d8:	8fa30010 	lw	v1,16(sp)
9fc093dc:	30e200ff 	andi	v0,a3,0xff
9fc093e0:	04600160 	bltz	v1,9fc09964 <core_bench_list+0x5e4>
9fc093e4:	afa20014 	sw	v0,20(sp)
9fc093e8:	12a00174 	beqz	s5,9fc099bc <core_bench_list+0x63c>
9fc093ec:	00000000 	nop
9fc093f0:	8eaf0004 	lw	t7,4(s5)
9fc093f4:	8fad0010 	lw	t5,16(sp)
9fc093f8:	85ee0002 	lh	t6,2(t7)
9fc093fc:	00000000 	nop
9fc09400:	11cd000d 	beq	t6,t5,9fc09438 <core_bench_list+0xb8>
9fc09404:	02a03021 	move	a2,s5
9fc09408:	0bf0250a 	j	9fc09428 <core_bench_list+0xa8>
9fc0940c:	02a02021 	move	a0,s5
9fc09410:	8c920004 	lw	s2,4(a0)
9fc09414:	8fb00010 	lw	s0,16(sp)
9fc09418:	86510002 	lh	s1,2(s2)
9fc0941c:	00000000 	nop
9fc09420:	12300005 	beq	s1,s0,9fc09438 <core_bench_list+0xb8>
9fc09424:	00803021 	move	a2,a0
9fc09428:	8c840000 	lw	a0,0(a0)
9fc0942c:	00000000 	nop
9fc09430:	1480fff7 	bnez	a0,9fc09410 <core_bench_list+0x90>
9fc09434:	00003021 	move	a2,zero
9fc09438:	8ea20000 	lw	v0,0(s5)
9fc0943c:	00001821 	move	v1,zero
9fc09440:	10400023 	beqz	v0,9fc094d0 <core_bench_list+0x150>
9fc09444:	aea30000 	sw	v1,0(s5)
9fc09448:	8c430000 	lw	v1,0(v0)
9fc0944c:	ac550000 	sw	s5,0(v0)
9fc09450:	1060001f 	beqz	v1,9fc094d0 <core_bench_list+0x150>
9fc09454:	0040a821 	move	s5,v0
9fc09458:	8c640000 	lw	a0,0(v1)
9fc0945c:	0060a821 	move	s5,v1
9fc09460:	1080001b 	beqz	a0,9fc094d0 <core_bench_list+0x150>
9fc09464:	ac620000 	sw	v0,0(v1)
9fc09468:	8c820000 	lw	v0,0(a0)
9fc0946c:	0080a821 	move	s5,a0
9fc09470:	10400017 	beqz	v0,9fc094d0 <core_bench_list+0x150>
9fc09474:	ac830000 	sw	v1,0(a0)
9fc09478:	8c430000 	lw	v1,0(v0)
9fc0947c:	0040a821 	move	s5,v0
9fc09480:	10600013 	beqz	v1,9fc094d0 <core_bench_list+0x150>
9fc09484:	ac440000 	sw	a0,0(v0)
9fc09488:	8c640000 	lw	a0,0(v1)
9fc0948c:	0060a821 	move	s5,v1
9fc09490:	1080000f 	beqz	a0,9fc094d0 <core_bench_list+0x150>
9fc09494:	ac620000 	sw	v0,0(v1)
9fc09498:	8c820000 	lw	v0,0(a0)
9fc0949c:	0080a821 	move	s5,a0
9fc094a0:	1040000b 	beqz	v0,9fc094d0 <core_bench_list+0x150>
9fc094a4:	ac830000 	sw	v1,0(a0)
9fc094a8:	0040a821 	move	s5,v0
9fc094ac:	8c420000 	lw	v0,0(v0)
9fc094b0:	02a01821 	move	v1,s5
9fc094b4:	10400006 	beqz	v0,9fc094d0 <core_bench_list+0x150>
9fc094b8:	aea40000 	sw	a0,0(s5)
9fc094bc:	0040a821 	move	s5,v0
9fc094c0:	8ea20000 	lw	v0,0(s5)
9fc094c4:	00000000 	nop
9fc094c8:	1440ffdf 	bnez	v0,9fc09448 <core_bench_list+0xc8>
9fc094cc:	aea30000 	sw	v1,0(s5)
9fc094d0:	10c0013c 	beqz	a2,9fc099c4 <core_bench_list+0x644>
9fc094d4:	02a01021 	move	v0,s5
9fc094d8:	8ccd0004 	lw	t5,4(a2)
9fc094dc:	250c0001 	addiu	t4,t0,1
9fc094e0:	85a40000 	lh	a0,0(t5)
9fc094e4:	00000000 	nop
9fc094e8:	30820001 	andi	v0,a0,0x1
9fc094ec:	10400005 	beqz	v0,9fc09504 <core_bench_list+0x184>
9fc094f0:	3188ffff 	andi	t0,t4,0xffff
9fc094f4:	00047a43 	sra	t7,a0,0x9
9fc094f8:	31ee0001 	andi	t6,t7,0x1
9fc094fc:	012e2021 	addu	a0,t1,t6
9fc09500:	3089ffff 	andi	t1,a0,0xffff
9fc09504:	8cc40000 	lw	a0,0(a2)
9fc09508:	00000000 	nop
9fc0950c:	10800008 	beqz	a0,9fc09530 <core_bench_list+0x1b0>
9fc09510:	00000000 	nop
9fc09514:	8c900000 	lw	s0,0(a0)
9fc09518:	00000000 	nop
9fc0951c:	acd00000 	sw	s0,0(a2)
9fc09520:	8ea60000 	lw	a2,0(s5)
9fc09524:	00000000 	nop
9fc09528:	ac860000 	sw	a2,0(a0)
9fc0952c:	aea40000 	sw	a0,0(s5)
9fc09530:	8fa30010 	lw	v1,16(sp)
9fc09534:	00000000 	nop
9fc09538:	04600004 	bltz	v1,9fc0954c <core_bench_list+0x1cc>
9fc0953c:	24730001 	addiu	s3,v1,1
9fc09540:	00139400 	sll	s2,s3,0x10
9fc09544:	00128c03 	sra	s1,s2,0x10
9fc09548:	afb10010 	sw	s1,16(sp)
9fc0954c:	24f60001 	addiu	s6,a3,1
9fc09550:	00163c00 	sll	a3,s6,0x10
9fc09554:	00073c03 	sra	a3,a3,0x10
9fc09558:	00eaa02a 	slt	s4,a3,t2
9fc0955c:	1680ff9e 	bnez	s4,9fc093d8 <core_bench_list+0x58>
9fc09560:	00000000 	nop
9fc09564:	012b5023 	subu	t2,t1,t3
9fc09568:	00084880 	sll	t1,t0,0x2
9fc0956c:	01494021 	addu	t0,t2,t1
9fc09570:	311effff 	andi	s8,t0,0xffff
9fc09574:	18a00090 	blez	a1,9fc097b8 <core_bench_list+0x438>
9fc09578:	00000000 	nop
9fc0957c:	12a0008d 	beqz	s5,9fc097b4 <core_bench_list+0x434>
9fc09580:	24160001 	li	s6,1
9fc09584:	0000b821 	move	s7,zero
9fc09588:	0000a021 	move	s4,zero
9fc0958c:	00002021 	move	a0,zero
9fc09590:	8eb10000 	lw	s1,0(s5)
9fc09594:	26cbffff 	addiu	t3,s6,-1
9fc09598:	26f70001 	addiu	s7,s7,1
9fc0959c:	31630007 	andi	v1,t3,0x7
9fc095a0:	12200053 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc095a4:	24130001 	li	s3,1
9fc095a8:	0276502a 	slt	t2,s3,s6
9fc095ac:	11400051 	beqz	t2,9fc096f4 <core_bench_list+0x374>
9fc095b0:	02a08021 	move	s0,s5
9fc095b4:	1060002c 	beqz	v1,9fc09668 <core_bench_list+0x2e8>
9fc095b8:	00000000 	nop
9fc095bc:	10730023 	beq	v1,s3,9fc0964c <core_bench_list+0x2cc>
9fc095c0:	24060002 	li	a2,2
9fc095c4:	1066001d 	beq	v1,a2,9fc0963c <core_bench_list+0x2bc>
9fc095c8:	24090003 	li	t1,3
9fc095cc:	10690017 	beq	v1,t1,9fc0962c <core_bench_list+0x2ac>
9fc095d0:	240b0004 	li	t3,4
9fc095d4:	106b0011 	beq	v1,t3,9fc0961c <core_bench_list+0x29c>
9fc095d8:	24180005 	li	t8,5
9fc095dc:	1078000b 	beq	v1,t8,9fc0960c <core_bench_list+0x28c>
9fc095e0:	24190006 	li	t9,6
9fc095e4:	10790005 	beq	v1,t9,9fc095fc <core_bench_list+0x27c>
9fc095e8:	00000000 	nop
9fc095ec:	8e310000 	lw	s1,0(s1)
9fc095f0:	00000000 	nop
9fc095f4:	1220003f 	beqz	s1,9fc096f4 <core_bench_list+0x374>
9fc095f8:	24130002 	li	s3,2
9fc095fc:	8e310000 	lw	s1,0(s1)
9fc09600:	00000000 	nop
9fc09604:	1220003a 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09608:	26730001 	addiu	s3,s3,1
9fc0960c:	8e310000 	lw	s1,0(s1)
9fc09610:	00000000 	nop
9fc09614:	12200036 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09618:	26730001 	addiu	s3,s3,1
9fc0961c:	8e310000 	lw	s1,0(s1)
9fc09620:	00000000 	nop
9fc09624:	12200032 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09628:	26730001 	addiu	s3,s3,1
9fc0962c:	8e310000 	lw	s1,0(s1)
9fc09630:	00000000 	nop
9fc09634:	1220002e 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09638:	26730001 	addiu	s3,s3,1
9fc0963c:	8e310000 	lw	s1,0(s1)
9fc09640:	00000000 	nop
9fc09644:	1220002a 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09648:	26730001 	addiu	s3,s3,1
9fc0964c:	8e310000 	lw	s1,0(s1)
9fc09650:	00000000 	nop
9fc09654:	12200026 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09658:	26730001 	addiu	s3,s3,1
9fc0965c:	0276f82a 	slt	ra,s3,s6
9fc09660:	13e00024 	beqz	ra,9fc096f4 <core_bench_list+0x374>
9fc09664:	02a08021 	move	s0,s5
9fc09668:	8e310000 	lw	s1,0(s1)
9fc0966c:	26730001 	addiu	s3,s3,1
9fc09670:	1220001f 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09674:	02601021 	move	v0,s3
9fc09678:	8e310000 	lw	s1,0(s1)
9fc0967c:	00000000 	nop
9fc09680:	1220001b 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09684:	26730001 	addiu	s3,s3,1
9fc09688:	8e310000 	lw	s1,0(s1)
9fc0968c:	00000000 	nop
9fc09690:	12200017 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc09694:	24530002 	addiu	s3,v0,2
9fc09698:	8e310000 	lw	s1,0(s1)
9fc0969c:	00000000 	nop
9fc096a0:	12200013 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc096a4:	24530003 	addiu	s3,v0,3
9fc096a8:	8e310000 	lw	s1,0(s1)
9fc096ac:	00000000 	nop
9fc096b0:	1220000f 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc096b4:	24530004 	addiu	s3,v0,4
9fc096b8:	8e310000 	lw	s1,0(s1)
9fc096bc:	00000000 	nop
9fc096c0:	1220000b 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc096c4:	24530005 	addiu	s3,v0,5
9fc096c8:	8e310000 	lw	s1,0(s1)
9fc096cc:	00000000 	nop
9fc096d0:	12200007 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc096d4:	24530006 	addiu	s3,v0,6
9fc096d8:	8e310000 	lw	s1,0(s1)
9fc096dc:	24530007 	addiu	s3,v0,7
9fc096e0:	12200003 	beqz	s1,9fc096f0 <core_bench_list+0x370>
9fc096e4:	0276102a 	slt	v0,s3,s6
9fc096e8:	1440ffdf 	bnez	v0,9fc09668 <core_bench_list+0x2e8>
9fc096ec:	00000000 	nop
9fc096f0:	02a08021 	move	s0,s5
9fc096f4:	02c09021 	move	s2,s6
9fc096f8:	12600017 	beqz	s3,9fc09758 <core_bench_list+0x3d8>
9fc096fc:	0080a821 	move	s5,a0
9fc09700:	1240001f 	beqz	s2,9fc09780 <core_bench_list+0x400>
9fc09704:	00000000 	nop
9fc09708:	1220001d 	beqz	s1,9fc09780 <core_bench_list+0x400>
9fc0970c:	00000000 	nop
9fc09710:	8e040004 	lw	a0,4(s0)
9fc09714:	8e250004 	lw	a1,4(s1)
9fc09718:	8fa60040 	lw	a2,64(sp)
9fc0971c:	0ff02450 	jal	9fc09140 <cmp_complex>
9fc09720:	00000000 	nop
9fc09724:	18400016 	blez	v0,9fc09780 <core_bench_list+0x400>
9fc09728:	00000000 	nop
9fc0972c:	8e220000 	lw	v0,0(s1)
9fc09730:	02001821 	move	v1,s0
9fc09734:	2652ffff 	addiu	s2,s2,-1
9fc09738:	02208021 	move	s0,s1
9fc0973c:	1280000e 	beqz	s4,9fc09778 <core_bench_list+0x3f8>
9fc09740:	00000000 	nop
9fc09744:	ae900000 	sw	s0,0(s4)
9fc09748:	0200a021 	move	s4,s0
9fc0974c:	00408821 	move	s1,v0
9fc09750:	1660ffeb 	bnez	s3,9fc09700 <core_bench_list+0x380>
9fc09754:	00608021 	move	s0,v1
9fc09758:	1240000d 	beqz	s2,9fc09790 <core_bench_list+0x410>
9fc0975c:	00000000 	nop
9fc09760:	1220000f 	beqz	s1,9fc097a0 <core_bench_list+0x420>
9fc09764:	02001821 	move	v1,s0
9fc09768:	2652ffff 	addiu	s2,s2,-1
9fc0976c:	8e220000 	lw	v0,0(s1)
9fc09770:	1680fff4 	bnez	s4,9fc09744 <core_bench_list+0x3c4>
9fc09774:	02208021 	move	s0,s1
9fc09778:	0bf025d2 	j	9fc09748 <core_bench_list+0x3c8>
9fc0977c:	0200a821 	move	s5,s0
9fc09780:	2673ffff 	addiu	s3,s3,-1
9fc09784:	8e030000 	lw	v1,0(s0)
9fc09788:	0bf025cf 	j	9fc0973c <core_bench_list+0x3bc>
9fc0978c:	02201021 	move	v0,s1
9fc09790:	12200003 	beqz	s1,9fc097a0 <core_bench_list+0x420>
9fc09794:	02a02021 	move	a0,s5
9fc09798:	0bf02564 	j	9fc09590 <core_bench_list+0x210>
9fc0979c:	0220a821 	move	s5,s1
9fc097a0:	24050001 	li	a1,1
9fc097a4:	12e50004 	beq	s7,a1,9fc097b8 <core_bench_list+0x438>
9fc097a8:	ae800000 	sw	zero,0(s4)
9fc097ac:	16a0ff75 	bnez	s5,9fc09584 <core_bench_list+0x204>
9fc097b0:	0016b040 	sll	s6,s6,0x1
9fc097b4:	aea00000 	sw	zero,0(s5)
9fc097b8:	8eb80000 	lw	t8,0(s5)
9fc097bc:	8fb70010 	lw	s7,16(sp)
9fc097c0:	8f110000 	lw	s1,0(t8)
9fc097c4:	8f030004 	lw	v1,4(t8)
9fc097c8:	8e390004 	lw	t9,4(s1)
9fc097cc:	8e3f0000 	lw	ra,0(s1)
9fc097d0:	af190004 	sw	t9,4(t8)
9fc097d4:	af1f0000 	sw	ra,0(t8)
9fc097d8:	00603021 	move	a2,v1
9fc097dc:	ae200000 	sw	zero,0(s1)
9fc097e0:	06e00172 	bltz	s7,9fc09dac <core_bench_list+0xa2c>
9fc097e4:	ae230004 	sw	v1,4(s1)
9fc097e8:	8ea50004 	lw	a1,4(s5)
9fc097ec:	8fa40010 	lw	a0,16(sp)
9fc097f0:	84ad0002 	lh	t5,2(a1)
9fc097f4:	00000000 	nop
9fc097f8:	11a4000f 	beq	t5,a0,9fc09838 <core_bench_list+0x4b8>
9fc097fc:	02a08021 	move	s0,s5
9fc09800:	0bf02608 	j	9fc09820 <core_bench_list+0x4a0>
9fc09804:	02a02021 	move	a0,s5
9fc09808:	8c900004 	lw	s0,4(a0)
9fc0980c:	8fae0010 	lw	t6,16(sp)
9fc09810:	860f0002 	lh	t7,2(s0)
9fc09814:	00000000 	nop
9fc09818:	11ee0007 	beq	t7,t6,9fc09838 <core_bench_list+0x4b8>
9fc0981c:	00808021 	move	s0,a0
9fc09820:	8c840000 	lw	a0,0(a0)
9fc09824:	00000000 	nop
9fc09828:	1480fff7 	bnez	a0,9fc09808 <core_bench_list+0x488>
9fc0982c:	00000000 	nop
9fc09830:	8eb00000 	lw	s0,0(s5)
9fc09834:	00000000 	nop
9fc09838:	1200006d 	beqz	s0,9fc099f0 <core_bench_list+0x670>
9fc0983c:	00000000 	nop
9fc09840:	84a40000 	lh	a0,0(a1)
9fc09844:	0ff01d20 	jal	9fc07480 <crc16>
9fc09848:	03c02821 	move	a1,s8
9fc0984c:	8e100000 	lw	s0,0(s0)
9fc09850:	0040f021 	move	s8,v0
9fc09854:	12000065 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc09858:	00402821 	move	a1,v0
9fc0985c:	8ebe0004 	lw	s8,4(s5)
9fc09860:	00000000 	nop
9fc09864:	87c40000 	lh	a0,0(s8)
9fc09868:	0ff01d20 	jal	9fc07480 <crc16>
9fc0986c:	00000000 	nop
9fc09870:	8e100000 	lw	s0,0(s0)
9fc09874:	0040f021 	move	s8,v0
9fc09878:	1200005c 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc0987c:	00402821 	move	a1,v0
9fc09880:	8eb20004 	lw	s2,4(s5)
9fc09884:	00000000 	nop
9fc09888:	86440000 	lh	a0,0(s2)
9fc0988c:	0ff01d20 	jal	9fc07480 <crc16>
9fc09890:	00000000 	nop
9fc09894:	8e100000 	lw	s0,0(s0)
9fc09898:	0040f021 	move	s8,v0
9fc0989c:	12000053 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc098a0:	00402821 	move	a1,v0
9fc098a4:	8eb30004 	lw	s3,4(s5)
9fc098a8:	00000000 	nop
9fc098ac:	86640000 	lh	a0,0(s3)
9fc098b0:	0ff01d20 	jal	9fc07480 <crc16>
9fc098b4:	00000000 	nop
9fc098b8:	8e100000 	lw	s0,0(s0)
9fc098bc:	0040f021 	move	s8,v0
9fc098c0:	1200004a 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc098c4:	00402821 	move	a1,v0
9fc098c8:	8eb40004 	lw	s4,4(s5)
9fc098cc:	00000000 	nop
9fc098d0:	86840000 	lh	a0,0(s4)
9fc098d4:	0ff01d20 	jal	9fc07480 <crc16>
9fc098d8:	00000000 	nop
9fc098dc:	8e100000 	lw	s0,0(s0)
9fc098e0:	0040f021 	move	s8,v0
9fc098e4:	12000041 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc098e8:	00402821 	move	a1,v0
9fc098ec:	8ebe0004 	lw	s8,4(s5)
9fc098f0:	00000000 	nop
9fc098f4:	87c40000 	lh	a0,0(s8)
9fc098f8:	0ff01d20 	jal	9fc07480 <crc16>
9fc098fc:	00000000 	nop
9fc09900:	8e100000 	lw	s0,0(s0)
9fc09904:	0040f021 	move	s8,v0
9fc09908:	12000038 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc0990c:	00402821 	move	a1,v0
9fc09910:	8ea70004 	lw	a3,4(s5)
9fc09914:	00000000 	nop
9fc09918:	84e40000 	lh	a0,0(a3)
9fc0991c:	0ff01d20 	jal	9fc07480 <crc16>
9fc09920:	00000000 	nop
9fc09924:	8e100000 	lw	s0,0(s0)
9fc09928:	0040f021 	move	s8,v0
9fc0992c:	1200002f 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc09930:	00402821 	move	a1,v0
9fc09934:	8ea80004 	lw	t0,4(s5)
9fc09938:	00000000 	nop
9fc0993c:	85040000 	lh	a0,0(t0)
9fc09940:	0ff01d20 	jal	9fc07480 <crc16>
9fc09944:	00000000 	nop
9fc09948:	8e100000 	lw	s0,0(s0)
9fc0994c:	00000000 	nop
9fc09950:	12000026 	beqz	s0,9fc099ec <core_bench_list+0x66c>
9fc09954:	0040f021 	move	s8,v0
9fc09958:	8ea50004 	lw	a1,4(s5)
9fc0995c:	0bf02610 	j	9fc09840 <core_bench_list+0x4c0>
9fc09960:	00000000 	nop
9fc09964:	12a00016 	beqz	s5,9fc099c0 <core_bench_list+0x640>
9fc09968:	00001021 	move	v0,zero
9fc0996c:	8eac0004 	lw	t4,4(s5)
9fc09970:	8fa40014 	lw	a0,20(sp)
9fc09974:	91860000 	lbu	a2,0(t4)
9fc09978:	00000000 	nop
9fc0997c:	10c4feae 	beq	a2,a0,9fc09438 <core_bench_list+0xb8>
9fc09980:	02a03021 	move	a2,s5
9fc09984:	0bf02669 	j	9fc099a4 <core_bench_list+0x624>
9fc09988:	02a02021 	move	a0,s5
9fc0998c:	8c960004 	lw	s6,4(a0)
9fc09990:	8fb30014 	lw	s3,20(sp)
9fc09994:	92d40000 	lbu	s4,0(s6)
9fc09998:	00000000 	nop
9fc0999c:	1293fea6 	beq	s4,s3,9fc09438 <core_bench_list+0xb8>
9fc099a0:	00803021 	move	a2,a0
9fc099a4:	8c840000 	lw	a0,0(a0)
9fc099a8:	00000000 	nop
9fc099ac:	1480fff7 	bnez	a0,9fc0998c <core_bench_list+0x60c>
9fc099b0:	00003021 	move	a2,zero
9fc099b4:	0bf0250e 	j	9fc09438 <core_bench_list+0xb8>
9fc099b8:	00000000 	nop
9fc099bc:	00001021 	move	v0,zero
9fc099c0:	0000a821 	move	s5,zero
9fc099c4:	8c430000 	lw	v1,0(v0)
9fc099c8:	257f0001 	addiu	ra,t3,1
9fc099cc:	8c7e0004 	lw	s8,4(v1)
9fc099d0:	33ebffff 	andi	t3,ra,0xffff
9fc099d4:	83d90001 	lb	t9,1(s8)
9fc099d8:	00000000 	nop
9fc099dc:	33380001 	andi	t8,t9,0x1
9fc099e0:	0138b821 	addu	s7,t1,t8
9fc099e4:	0bf0254c 	j	9fc09530 <core_bench_list+0x1b0>
9fc099e8:	32e9ffff 	andi	t1,s7,0xffff
9fc099ec:	8e260004 	lw	a2,4(s1)
9fc099f0:	8ea70000 	lw	a3,0(s5)
9fc099f4:	24160001 	li	s6,1
9fc099f8:	8cea0004 	lw	t2,4(a3)
9fc099fc:	8ce80000 	lw	t0,0(a3)
9fc09a00:	ae2a0004 	sw	t2,4(s1)
9fc09a04:	ae280000 	sw	t0,0(s1)
9fc09a08:	ace60004 	sw	a2,4(a3)
9fc09a0c:	12a0008d 	beqz	s5,9fc09c44 <core_bench_list+0x8c4>
9fc09a10:	acf10000 	sw	s1,0(a3)
9fc09a14:	0000b821 	move	s7,zero
9fc09a18:	0000a021 	move	s4,zero
9fc09a1c:	00002021 	move	a0,zero
9fc09a20:	8eb10000 	lw	s1,0(s5)
9fc09a24:	26c6ffff 	addiu	a2,s6,-1
9fc09a28:	26f70001 	addiu	s7,s7,1
9fc09a2c:	30c30007 	andi	v1,a2,0x7
9fc09a30:	12200053 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09a34:	24130001 	li	s3,1
9fc09a38:	0276282a 	slt	a1,s3,s6
9fc09a3c:	10a00051 	beqz	a1,9fc09b84 <core_bench_list+0x804>
9fc09a40:	02a08021 	move	s0,s5
9fc09a44:	1060002c 	beqz	v1,9fc09af8 <core_bench_list+0x778>
9fc09a48:	00000000 	nop
9fc09a4c:	10730023 	beq	v1,s3,9fc09adc <core_bench_list+0x75c>
9fc09a50:	24020002 	li	v0,2
9fc09a54:	1062001d 	beq	v1,v0,9fc09acc <core_bench_list+0x74c>
9fc09a58:	240c0003 	li	t4,3
9fc09a5c:	106c0017 	beq	v1,t4,9fc09abc <core_bench_list+0x73c>
9fc09a60:	240d0004 	li	t5,4
9fc09a64:	106d0011 	beq	v1,t5,9fc09aac <core_bench_list+0x72c>
9fc09a68:	240e0005 	li	t6,5
9fc09a6c:	106e000b 	beq	v1,t6,9fc09a9c <core_bench_list+0x71c>
9fc09a70:	240f0006 	li	t7,6
9fc09a74:	106f0005 	beq	v1,t7,9fc09a8c <core_bench_list+0x70c>
9fc09a78:	00000000 	nop
9fc09a7c:	8e310000 	lw	s1,0(s1)
9fc09a80:	00000000 	nop
9fc09a84:	1220003f 	beqz	s1,9fc09b84 <core_bench_list+0x804>
9fc09a88:	24130002 	li	s3,2
9fc09a8c:	8e310000 	lw	s1,0(s1)
9fc09a90:	00000000 	nop
9fc09a94:	1220003a 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09a98:	26730001 	addiu	s3,s3,1
9fc09a9c:	8e310000 	lw	s1,0(s1)
9fc09aa0:	00000000 	nop
9fc09aa4:	12200036 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09aa8:	26730001 	addiu	s3,s3,1
9fc09aac:	8e310000 	lw	s1,0(s1)
9fc09ab0:	00000000 	nop
9fc09ab4:	12200032 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09ab8:	26730001 	addiu	s3,s3,1
9fc09abc:	8e310000 	lw	s1,0(s1)
9fc09ac0:	00000000 	nop
9fc09ac4:	1220002e 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09ac8:	26730001 	addiu	s3,s3,1
9fc09acc:	8e310000 	lw	s1,0(s1)
9fc09ad0:	00000000 	nop
9fc09ad4:	1220002a 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09ad8:	26730001 	addiu	s3,s3,1
9fc09adc:	8e310000 	lw	s1,0(s1)
9fc09ae0:	00000000 	nop
9fc09ae4:	12200026 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09ae8:	26730001 	addiu	s3,s3,1
9fc09aec:	0276802a 	slt	s0,s3,s6
9fc09af0:	12000024 	beqz	s0,9fc09b84 <core_bench_list+0x804>
9fc09af4:	02a08021 	move	s0,s5
9fc09af8:	8e310000 	lw	s1,0(s1)
9fc09afc:	26730001 	addiu	s3,s3,1
9fc09b00:	1220001f 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b04:	02601021 	move	v0,s3
9fc09b08:	8e310000 	lw	s1,0(s1)
9fc09b0c:	00000000 	nop
9fc09b10:	1220001b 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b14:	26730001 	addiu	s3,s3,1
9fc09b18:	8e310000 	lw	s1,0(s1)
9fc09b1c:	00000000 	nop
9fc09b20:	12200017 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b24:	24530002 	addiu	s3,v0,2
9fc09b28:	8e310000 	lw	s1,0(s1)
9fc09b2c:	00000000 	nop
9fc09b30:	12200013 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b34:	24530003 	addiu	s3,v0,3
9fc09b38:	8e310000 	lw	s1,0(s1)
9fc09b3c:	00000000 	nop
9fc09b40:	1220000f 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b44:	24530004 	addiu	s3,v0,4
9fc09b48:	8e310000 	lw	s1,0(s1)
9fc09b4c:	00000000 	nop
9fc09b50:	1220000b 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b54:	24530005 	addiu	s3,v0,5
9fc09b58:	8e310000 	lw	s1,0(s1)
9fc09b5c:	00000000 	nop
9fc09b60:	12200007 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b64:	24530006 	addiu	s3,v0,6
9fc09b68:	8e310000 	lw	s1,0(s1)
9fc09b6c:	24530007 	addiu	s3,v0,7
9fc09b70:	12200003 	beqz	s1,9fc09b80 <core_bench_list+0x800>
9fc09b74:	0276102a 	slt	v0,s3,s6
9fc09b78:	1440ffdf 	bnez	v0,9fc09af8 <core_bench_list+0x778>
9fc09b7c:	00000000 	nop
9fc09b80:	02a08021 	move	s0,s5
9fc09b84:	02c09021 	move	s2,s6
9fc09b88:	12600016 	beqz	s3,9fc09be4 <core_bench_list+0x864>
9fc09b8c:	0080a821 	move	s5,a0
9fc09b90:	1240001f 	beqz	s2,9fc09c10 <core_bench_list+0x890>
9fc09b94:	00000000 	nop
9fc09b98:	1220001d 	beqz	s1,9fc09c10 <core_bench_list+0x890>
9fc09b9c:	00000000 	nop
9fc09ba0:	8e040004 	lw	a0,4(s0)
9fc09ba4:	8e250004 	lw	a1,4(s1)
9fc09ba8:	0ff020e8 	jal	9fc083a0 <cmp_idx>
9fc09bac:	00003021 	move	a2,zero
9fc09bb0:	18400017 	blez	v0,9fc09c10 <core_bench_list+0x890>
9fc09bb4:	00000000 	nop
9fc09bb8:	8e220000 	lw	v0,0(s1)
9fc09bbc:	02001821 	move	v1,s0
9fc09bc0:	2652ffff 	addiu	s2,s2,-1
9fc09bc4:	02208021 	move	s0,s1
9fc09bc8:	1280000e 	beqz	s4,9fc09c04 <core_bench_list+0x884>
9fc09bcc:	00000000 	nop
9fc09bd0:	ae900000 	sw	s0,0(s4)
9fc09bd4:	0200a021 	move	s4,s0
9fc09bd8:	00408821 	move	s1,v0
9fc09bdc:	1660ffec 	bnez	s3,9fc09b90 <core_bench_list+0x810>
9fc09be0:	00608021 	move	s0,v1
9fc09be4:	1240000e 	beqz	s2,9fc09c20 <core_bench_list+0x8a0>
9fc09be8:	00000000 	nop
9fc09bec:	12200010 	beqz	s1,9fc09c30 <core_bench_list+0x8b0>
9fc09bf0:	02001821 	move	v1,s0
9fc09bf4:	2652ffff 	addiu	s2,s2,-1
9fc09bf8:	8e220000 	lw	v0,0(s1)
9fc09bfc:	1680fff4 	bnez	s4,9fc09bd0 <core_bench_list+0x850>
9fc09c00:	02208021 	move	s0,s1
9fc09c04:	0bf026f5 	j	9fc09bd4 <core_bench_list+0x854>
9fc09c08:	0200a821 	move	s5,s0
9fc09c0c:	00000000 	nop
9fc09c10:	2673ffff 	addiu	s3,s3,-1
9fc09c14:	8e030000 	lw	v1,0(s0)
9fc09c18:	0bf026f2 	j	9fc09bc8 <core_bench_list+0x848>
9fc09c1c:	02201021 	move	v0,s1
9fc09c20:	12200003 	beqz	s1,9fc09c30 <core_bench_list+0x8b0>
9fc09c24:	02a02021 	move	a0,s5
9fc09c28:	0bf02688 	j	9fc09a20 <core_bench_list+0x6a0>
9fc09c2c:	0220a821 	move	s5,s1
9fc09c30:	24110001 	li	s1,1
9fc09c34:	12f10004 	beq	s7,s1,9fc09c48 <core_bench_list+0x8c8>
9fc09c38:	ae800000 	sw	zero,0(s4)
9fc09c3c:	16a0ff75 	bnez	s5,9fc09a14 <core_bench_list+0x694>
9fc09c40:	0016b040 	sll	s6,s6,0x1
9fc09c44:	aea00000 	sw	zero,0(s5)
9fc09c48:	8eb00000 	lw	s0,0(s5)
9fc09c4c:	00000000 	nop
9fc09c50:	12000049 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09c54:	00000000 	nop
9fc09c58:	8eb60004 	lw	s6,4(s5)
9fc09c5c:	00000000 	nop
9fc09c60:	86c40000 	lh	a0,0(s6)
9fc09c64:	0ff01d20 	jal	9fc07480 <crc16>
9fc09c68:	03c02821 	move	a1,s8
9fc09c6c:	8e100000 	lw	s0,0(s0)
9fc09c70:	0040f021 	move	s8,v0
9fc09c74:	12000040 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09c78:	00402821 	move	a1,v0
9fc09c7c:	8ea90004 	lw	t1,4(s5)
9fc09c80:	00000000 	nop
9fc09c84:	85240000 	lh	a0,0(t1)
9fc09c88:	0ff01d20 	jal	9fc07480 <crc16>
9fc09c8c:	00000000 	nop
9fc09c90:	8e100000 	lw	s0,0(s0)
9fc09c94:	0040f021 	move	s8,v0
9fc09c98:	12000037 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09c9c:	00402821 	move	a1,v0
9fc09ca0:	8eab0004 	lw	t3,4(s5)
9fc09ca4:	00000000 	nop
9fc09ca8:	85640000 	lh	a0,0(t3)
9fc09cac:	0ff01d20 	jal	9fc07480 <crc16>
9fc09cb0:	00000000 	nop
9fc09cb4:	8e100000 	lw	s0,0(s0)
9fc09cb8:	0040f021 	move	s8,v0
9fc09cbc:	1200002e 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09cc0:	00402821 	move	a1,v0
9fc09cc4:	8eb70004 	lw	s7,4(s5)
9fc09cc8:	00000000 	nop
9fc09ccc:	86e40000 	lh	a0,0(s7)
9fc09cd0:	0ff01d20 	jal	9fc07480 <crc16>
9fc09cd4:	00000000 	nop
9fc09cd8:	8e100000 	lw	s0,0(s0)
9fc09cdc:	0040f021 	move	s8,v0
9fc09ce0:	12000025 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09ce4:	00402821 	move	a1,v0
9fc09ce8:	8eb80004 	lw	t8,4(s5)
9fc09cec:	00000000 	nop
9fc09cf0:	87040000 	lh	a0,0(t8)
9fc09cf4:	0ff01d20 	jal	9fc07480 <crc16>
9fc09cf8:	00000000 	nop
9fc09cfc:	8e100000 	lw	s0,0(s0)
9fc09d00:	0040f021 	move	s8,v0
9fc09d04:	1200001c 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09d08:	00402821 	move	a1,v0
9fc09d0c:	8eb90004 	lw	t9,4(s5)
9fc09d10:	00000000 	nop
9fc09d14:	87240000 	lh	a0,0(t9)
9fc09d18:	0ff01d20 	jal	9fc07480 <crc16>
9fc09d1c:	00000000 	nop
9fc09d20:	8e100000 	lw	s0,0(s0)
9fc09d24:	0040f021 	move	s8,v0
9fc09d28:	12000013 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09d2c:	00402821 	move	a1,v0
9fc09d30:	8ebf0004 	lw	ra,4(s5)
9fc09d34:	00000000 	nop
9fc09d38:	87e40000 	lh	a0,0(ra)
9fc09d3c:	0ff01d20 	jal	9fc07480 <crc16>
9fc09d40:	00000000 	nop
9fc09d44:	8e100000 	lw	s0,0(s0)
9fc09d48:	0040f021 	move	s8,v0
9fc09d4c:	1200000a 	beqz	s0,9fc09d78 <core_bench_list+0x9f8>
9fc09d50:	00402821 	move	a1,v0
9fc09d54:	8ea30004 	lw	v1,4(s5)
9fc09d58:	00000000 	nop
9fc09d5c:	84640000 	lh	a0,0(v1)
9fc09d60:	0ff01d20 	jal	9fc07480 <crc16>
9fc09d64:	00000000 	nop
9fc09d68:	8e100000 	lw	s0,0(s0)
9fc09d6c:	00000000 	nop
9fc09d70:	1600ffb9 	bnez	s0,9fc09c58 <core_bench_list+0x8d8>
9fc09d74:	0040f021 	move	s8,v0
9fc09d78:	8fbf003c 	lw	ra,60(sp)
9fc09d7c:	03c01021 	move	v0,s8
9fc09d80:	8fbe0038 	lw	s8,56(sp)
9fc09d84:	8fb70034 	lw	s7,52(sp)
9fc09d88:	8fb60030 	lw	s6,48(sp)
9fc09d8c:	8fb5002c 	lw	s5,44(sp)
9fc09d90:	8fb40028 	lw	s4,40(sp)
9fc09d94:	8fb30024 	lw	s3,36(sp)
9fc09d98:	8fb20020 	lw	s2,32(sp)
9fc09d9c:	8fb1001c 	lw	s1,28(sp)
9fc09da0:	8fb00018 	lw	s0,24(sp)
9fc09da4:	03e00008 	jr	ra
9fc09da8:	27bd0040 	addiu	sp,sp,64
9fc09dac:	8ea50004 	lw	a1,4(s5)
9fc09db0:	8fac0014 	lw	t4,20(sp)
9fc09db4:	90a20000 	lbu	v0,0(a1)
9fc09db8:	00000000 	nop
9fc09dbc:	104cfe9e 	beq	v0,t4,9fc09838 <core_bench_list+0x4b8>
9fc09dc0:	02a08021 	move	s0,s5
9fc09dc4:	0bf02779 	j	9fc09de4 <core_bench_list+0xa64>
9fc09dc8:	02a02021 	move	a0,s5
9fc09dcc:	8c940004 	lw	s4,4(a0)
9fc09dd0:	8fb20014 	lw	s2,20(sp)
9fc09dd4:	92930000 	lbu	s3,0(s4)
9fc09dd8:	00000000 	nop
9fc09ddc:	1272fe96 	beq	s3,s2,9fc09838 <core_bench_list+0x4b8>
9fc09de0:	00808021 	move	s0,a0
9fc09de4:	8c840000 	lw	a0,0(a0)
9fc09de8:	00000000 	nop
9fc09dec:	1480fff7 	bnez	a0,9fc09dcc <core_bench_list+0xa4c>
9fc09df0:	00000000 	nop
9fc09df4:	0bf0260c 	j	9fc09830 <core_bench_list+0x4b0>
9fc09df8:	00000000 	nop
9fc09dfc:	afa50010 	sw	a1,16(sp)
9fc09e00:	0bf0255d 	j	9fc09574 <core_bench_list+0x1f4>
9fc09e04:	0000f021 	move	s8,zero
	...

9fc09e10 <printf>:
printf():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:2
9fc09e10:	27bdffc8 	addiu	sp,sp,-56
9fc09e14:	afb30024 	sw	s3,36(sp)
9fc09e18:	afbf0034 	sw	ra,52(sp)
9fc09e1c:	afb60030 	sw	s6,48(sp)
9fc09e20:	afb5002c 	sw	s5,44(sp)
9fc09e24:	afb40028 	sw	s4,40(sp)
9fc09e28:	afb20020 	sw	s2,32(sp)
9fc09e2c:	afb1001c 	sw	s1,28(sp)
9fc09e30:	afb00018 	sw	s0,24(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:10
9fc09e34:	80900000 	lb	s0,0(a0)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:2
9fc09e38:	00809821 	move	s3,a0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:8
9fc09e3c:	27a4003c 	addiu	a0,sp,60
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:2
9fc09e40:	afa5003c 	sw	a1,60(sp)
9fc09e44:	afa60040 	sw	a2,64(sp)
9fc09e48:	afa70044 	sw	a3,68(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:10
9fc09e4c:	12000013 	beqz	s0,9fc09e9c <printf+0x8c>
9fc09e50:	afa40010 	sw	a0,16(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:17
9fc09e54:	3c029fc1 	lui	v0,0x9fc1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:9
9fc09e58:	00809021 	move	s2,a0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:17
9fc09e5c:	2456ab00 	addiu	s6,v0,-21760
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:9
9fc09e60:	00008821 	move	s1,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:13
9fc09e64:	24140025 	li	s4,37
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:79
9fc09e68:	2415000a 	li	s5,10
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:13
9fc09e6c:	12140016 	beq	s0,s4,9fc09ec8 <printf+0xb8>
9fc09e70:	02711021 	addu	v0,s3,s1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:79
9fc09e74:	1215002f 	beq	s0,s5,9fc09f34 <printf+0x124>
9fc09e78:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:80
9fc09e7c:	0ff0281d 	jal	9fc0a074 <putchar>
9fc09e80:	02002021 	move	a0,s0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:10
9fc09e84:	26310001 	addiu	s1,s1,1
9fc09e88:	02711021 	addu	v0,s3,s1
9fc09e8c:	80500000 	lb	s0,0(v0)
9fc09e90:	00000000 	nop
9fc09e94:	1600fff5 	bnez	s0,9fc09e6c <printf+0x5c>
9fc09e98:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:84
9fc09e9c:	8fbf0034 	lw	ra,52(sp)
9fc09ea0:	00001021 	move	v0,zero
9fc09ea4:	8fb60030 	lw	s6,48(sp)
9fc09ea8:	8fb5002c 	lw	s5,44(sp)
9fc09eac:	8fb40028 	lw	s4,40(sp)
9fc09eb0:	8fb30024 	lw	s3,36(sp)
9fc09eb4:	8fb20020 	lw	s2,32(sp)
9fc09eb8:	8fb1001c 	lw	s1,28(sp)
9fc09ebc:	8fb00018 	lw	s0,24(sp)
9fc09ec0:	03e00008 	jr	ra
9fc09ec4:	27bd0038 	addiu	sp,sp,56
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:13
9fc09ec8:	80440001 	lb	a0,1(v0)
9fc09ecc:	24050001 	li	a1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:17
9fc09ed0:	2482ffdb 	addiu	v0,a0,-37
9fc09ed4:	304200ff 	andi	v0,v0,0xff
9fc09ed8:	2c430054 	sltiu	v1,v0,84
9fc09edc:	14600005 	bnez	v1,9fc09ef4 <printf+0xe4>
9fc09ee0:	00021080 	sll	v0,v0,0x2
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:73
9fc09ee4:	0ff0281d 	jal	9fc0a074 <putchar>
9fc09ee8:	24040025 	li	a0,37
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:10
9fc09eec:	0bf027a2 	j	9fc09e88 <printf+0x78>
9fc09ef0:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:17
9fc09ef4:	02c21021 	addu	v0,s6,v0
9fc09ef8:	8c430000 	lw	v1,0(v0)
9fc09efc:	00000000 	nop
9fc09f00:	00600008 	jr	v1
9fc09f04:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:65
9fc09f08:	26310001 	addiu	s1,s1,1
9fc09f0c:	02711021 	addu	v0,s3,s1
9fc09f10:	80440001 	lb	a0,1(v0)
9fc09f14:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:67
9fc09f18:	2482ffcf 	addiu	v0,a0,-49
9fc09f1c:	304200ff 	andi	v0,v0,0xff
9fc09f20:	2c420009 	sltiu	v0,v0,9
9fc09f24:	1440003f 	bnez	v0,9fc0a024 <printf+0x214>
9fc09f28:	00002821 	move	a1,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:17
9fc09f2c:	0bf027b5 	j	9fc09ed4 <printf+0xc4>
9fc09f30:	2482ffdb 	addiu	v0,a0,-37
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:79
9fc09f34:	0ff0281d 	jal	9fc0a074 <putchar>
9fc09f38:	2404000d 	li	a0,13
9fc09f3c:	0bf0279f 	j	9fc09e7c <printf+0x6c>
9fc09f40:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:30
9fc09f44:	8e440000 	lw	a0,0(s2)
9fc09f48:	2406000a 	li	a2,10
9fc09f4c:	0ff02858 	jal	9fc0a160 <printbase>
9fc09f50:	00003821 	move	a3,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:31
9fc09f54:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:32
9fc09f58:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09f5c:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:20
9fc09f60:	8e440000 	lw	a0,0(s2)
9fc09f64:	0ff02828 	jal	9fc0a0a0 <putstring>
9fc09f68:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:21
9fc09f6c:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09f70:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:56
9fc09f74:	8e440000 	lw	a0,0(s2)
9fc09f78:	24060010 	li	a2,16
9fc09f7c:	0ff02858 	jal	9fc0a160 <printbase>
9fc09f80:	00003821 	move	a3,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:57
9fc09f84:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:58
9fc09f88:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09f8c:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:45
9fc09f90:	8e440000 	lw	a0,0(s2)
9fc09f94:	24060008 	li	a2,8
9fc09f98:	0ff02858 	jal	9fc0a160 <printbase>
9fc09f9c:	00003821 	move	a3,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:46
9fc09fa0:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:47
9fc09fa4:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09fa8:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:40
9fc09fac:	8e440000 	lw	a0,0(s2)
9fc09fb0:	2406000a 	li	a2,10
9fc09fb4:	0ff02858 	jal	9fc0a160 <printbase>
9fc09fb8:	00003821 	move	a3,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:41
9fc09fbc:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:42
9fc09fc0:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09fc4:	26310002 	addiu	s1,s1,2
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:35
9fc09fc8:	8e440000 	lw	a0,0(s2)
9fc09fcc:	2406000a 	li	a2,10
9fc09fd0:	0ff02858 	jal	9fc0a160 <printbase>
9fc09fd4:	24070001 	li	a3,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:36
9fc09fd8:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:37
9fc09fdc:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09fe0:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:25
9fc09fe4:	8e440000 	lw	a0,0(s2)
9fc09fe8:	0ff0281d 	jal	9fc0a074 <putchar>
9fc09fec:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:26
9fc09ff0:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc09ff4:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:50
9fc09ff8:	8e440000 	lw	a0,0(s2)
9fc09ffc:	24060002 	li	a2,2
9fc0a000:	0ff02858 	jal	9fc0a160 <printbase>
9fc0a004:	00003821 	move	a3,zero
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:51
9fc0a008:	26520004 	addiu	s2,s2,4
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:52
9fc0a00c:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc0a010:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:61
9fc0a014:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a018:	24040025 	li	a0,37
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:62
9fc0a01c:	0bf027a1 	j	9fc09e84 <printf+0x74>
9fc0a020:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:67
9fc0a024:	02713021 	addu	a2,s3,s1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:68
9fc0a028:	000510c0 	sll	v0,a1,0x3
9fc0a02c:	00051840 	sll	v1,a1,0x1
9fc0a030:	00621821 	addu	v1,v1,v0
9fc0a034:	00641821 	addu	v1,v1,a0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:67
9fc0a038:	80c40002 	lb	a0,2(a2)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:68
9fc0a03c:	2465ffd0 	addiu	a1,v1,-48
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:67
9fc0a040:	2482ffcf 	addiu	v0,a0,-49
9fc0a044:	304200ff 	andi	v0,v0,0xff
9fc0a048:	2c420009 	sltiu	v0,v0,9
9fc0a04c:	26310001 	addiu	s1,s1,1
9fc0a050:	1040ff9f 	beqz	v0,9fc09ed0 <printf+0xc0>
9fc0a054:	24c60001 	addiu	a2,a2,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printf.c:68
9fc0a058:	0bf0280b 	j	9fc0a02c <printf+0x21c>
9fc0a05c:	000510c0 	sll	v0,a1,0x3

9fc0a060 <tgt_putchar>:
tgt_putchar():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/putchar.c:9
9fc0a060:	3c19bfb0 	lui	t9,0xbfb0
9fc0a064:	03e00008 	jr	ra
9fc0a068:	a324fff0 	sb	a0,-16(t9)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/putchar.c:18
9fc0a06c:	03e00008 	jr	ra
9fc0a070:	00000000 	nop

9fc0a074 <putchar>:
putchar():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/putchar.c:2
9fc0a074:	27bdffe8 	addiu	sp,sp,-24
9fc0a078:	afbf0014 	sw	ra,20(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/putchar.c:3
9fc0a07c:	0ff02818 	jal	9fc0a060 <tgt_putchar>
9fc0a080:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/putchar.c:5
9fc0a084:	8fbf0014 	lw	ra,20(sp)
9fc0a088:	00001021 	move	v0,zero
9fc0a08c:	03e00008 	jr	ra
9fc0a090:	27bd0018 	addiu	sp,sp,24
	...

9fc0a0a0 <putstring>:
putstring():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:2
9fc0a0a0:	27bdffe0 	addiu	sp,sp,-32
9fc0a0a4:	afb10014 	sw	s1,20(sp)
9fc0a0a8:	afbf001c 	sw	ra,28(sp)
9fc0a0ac:	afb20018 	sw	s2,24(sp)
9fc0a0b0:	afb00010 	sw	s0,16(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:4
9fc0a0b4:	80900000 	lb	s0,0(a0)
9fc0a0b8:	00000000 	nop
9fc0a0bc:	12000013 	beqz	s0,9fc0a10c <putstring+0x6c>
9fc0a0c0:	00808821 	move	s1,a0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:6
9fc0a0c4:	0bf02839 	j	9fc0a0e4 <putstring+0x44>
9fc0a0c8:	2412000a 	li	s2,10
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:7
9fc0a0cc:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a0d0:	02002021 	move	a0,s0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:4
9fc0a0d4:	82300000 	lb	s0,0(s1)
9fc0a0d8:	00000000 	nop
9fc0a0dc:	1200000b 	beqz	s0,9fc0a10c <putstring+0x6c>
9fc0a0e0:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:6
9fc0a0e4:	1612fff9 	bne	s0,s2,9fc0a0cc <putstring+0x2c>
9fc0a0e8:	26310001 	addiu	s1,s1,1
9fc0a0ec:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a0f0:	2404000d 	li	a0,13
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:7
9fc0a0f4:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a0f8:	02002021 	move	a0,s0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:4
9fc0a0fc:	82300000 	lb	s0,0(s1)
9fc0a100:	00000000 	nop
9fc0a104:	1600fff7 	bnez	s0,9fc0a0e4 <putstring+0x44>
9fc0a108:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:11
9fc0a10c:	8fbf001c 	lw	ra,28(sp)
9fc0a110:	00001021 	move	v0,zero
9fc0a114:	8fb20018 	lw	s2,24(sp)
9fc0a118:	8fb10014 	lw	s1,20(sp)
9fc0a11c:	8fb00010 	lw	s0,16(sp)
9fc0a120:	03e00008 	jr	ra
9fc0a124:	27bd0020 	addiu	sp,sp,32

9fc0a128 <puts>:
puts():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:15
9fc0a128:	27bdffe8 	addiu	sp,sp,-24
9fc0a12c:	afbf0014 	sw	ra,20(sp)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:16
9fc0a130:	0ff02828 	jal	9fc0a0a0 <putstring>
9fc0a134:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:17
9fc0a138:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a13c:	2404000d 	li	a0,13
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:18
9fc0a140:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a144:	2404000a 	li	a0,10
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/puts.c:20
9fc0a148:	8fbf0014 	lw	ra,20(sp)
9fc0a14c:	00001021 	move	v0,zero
9fc0a150:	03e00008 	jr	ra
9fc0a154:	27bd0018 	addiu	sp,sp,24
	...

9fc0a160 <printbase>:
printbase():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:2
9fc0a160:	27bdff98 	addiu	sp,sp,-104
9fc0a164:	afb30060 	sw	s3,96(sp)
9fc0a168:	afb2005c 	sw	s2,92(sp)
9fc0a16c:	afbf0064 	sw	ra,100(sp)
9fc0a170:	afb10058 	sw	s1,88(sp)
9fc0a174:	afb00054 	sw	s0,84(sp)
9fc0a178:	00801821 	move	v1,a0
9fc0a17c:	00a09821 	move	s3,a1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:7
9fc0a180:	10e00003 	beqz	a3,9fc0a190 <printbase+0x30>
9fc0a184:	00c09021 	move	s2,a2
9fc0a188:	0480002f 	bltz	a0,9fc0a248 <printbase+0xe8>
9fc0a18c:	2404002d 	li	a0,45
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:12
9fc0a190:	00608021 	move	s0,v1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:14
9fc0a194:	1200000c 	beqz	s0,9fc0a1c8 <printbase+0x68>
9fc0a198:	00008821 	move	s1,zero
9fc0a19c:	27a50010 	addiu	a1,sp,16
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:16
9fc0a1a0:	16400002 	bnez	s2,9fc0a1ac <printbase+0x4c>
9fc0a1a4:	0212001b 	divu	zero,s0,s2
9fc0a1a8:	0007000d 	break	0x7
9fc0a1ac:	00b12021 	addu	a0,a1,s1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:14
9fc0a1b0:	26310001 	addiu	s1,s1,1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:16
9fc0a1b4:	00001010 	mfhi	v0
9fc0a1b8:	a0820000 	sb	v0,0(a0)
9fc0a1bc:	00001812 	mflo	v1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:14
9fc0a1c0:	1460fff7 	bnez	v1,9fc0a1a0 <printbase+0x40>
9fc0a1c4:	00608021 	move	s0,v1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:22
9fc0a1c8:	0233102a 	slt	v0,s1,s3
9fc0a1cc:	10400002 	beqz	v0,9fc0a1d8 <printbase+0x78>
9fc0a1d0:	02201821 	move	v1,s1
9fc0a1d4:	02601821 	move	v1,s3
9fc0a1d8:	1060000c 	beqz	v1,9fc0a20c <printbase+0xac>
9fc0a1dc:	2470ffff 	addiu	s0,v1,-1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:2
9fc0a1e0:	27a20010 	addiu	v0,sp,16
9fc0a1e4:	00509021 	addu	s2,v0,s0
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:24
9fc0a1e8:	26020001 	addiu	v0,s0,1
9fc0a1ec:	0222102a 	slt	v0,s1,v0
9fc0a1f0:	1040000e 	beqz	v0,9fc0a22c <printbase+0xcc>
9fc0a1f4:	24040030 	li	a0,48
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:25
9fc0a1f8:	02009821 	move	s3,s0
9fc0a1fc:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a200:	2610ffff 	addiu	s0,s0,-1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:22
9fc0a204:	1660fff8 	bnez	s3,9fc0a1e8 <printbase+0x88>
9fc0a208:	2652ffff 	addiu	s2,s2,-1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:28
9fc0a20c:	8fbf0064 	lw	ra,100(sp)
9fc0a210:	00001021 	move	v0,zero
9fc0a214:	8fb30060 	lw	s3,96(sp)
9fc0a218:	8fb2005c 	lw	s2,92(sp)
9fc0a21c:	8fb10058 	lw	s1,88(sp)
9fc0a220:	8fb00054 	lw	s0,84(sp)
9fc0a224:	03e00008 	jr	ra
9fc0a228:	27bd0068 	addiu	sp,sp,104
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:24
9fc0a22c:	82440000 	lb	a0,0(s2)
9fc0a230:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:25
9fc0a234:	2882000a 	slti	v0,a0,10
9fc0a238:	14400007 	bnez	v0,9fc0a258 <printbase+0xf8>
9fc0a23c:	02009821 	move	s3,s0
9fc0a240:	0bf0287f 	j	9fc0a1fc <printbase+0x9c>
9fc0a244:	24840057 	addiu	a0,a0,87
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:10
9fc0a248:	0ff0281d 	jal	9fc0a074 <putchar>
9fc0a24c:	00038023 	negu	s0,v1
9fc0a250:	0bf02865 	j	9fc0a194 <printbase+0x34>
9fc0a254:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/printbase.c:25
9fc0a258:	0bf0287e 	j	9fc0a1f8 <printbase+0x98>
9fc0a25c:	24840030 	addiu	a0,a0,48

9fc0a260 <_get_count>:
_get_count():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:14
9fc0a260:	3c19bfb0 	lui	t9,0xbfb0
9fc0a264:	8f22e000 	lw	v0,-8192(t9)
9fc0a268:	03e00008 	jr	ra
9fc0a26c:	00000000 	nop

9fc0a270 <get_count>:
get_count():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:19
9fc0a270:	3c19bfb0 	lui	t9,0xbfb0
9fc0a274:	8f22e000 	lw	v0,-8192(t9)
9fc0a278:	03e00008 	jr	ra
9fc0a27c:	00000000 	nop

9fc0a280 <get_count_my>:
get_count_my():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:24
9fc0a280:	40024800 	mfc0	v0,$9
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:29
9fc0a284:	03e00008 	jr	ra
9fc0a288:	00000000 	nop

9fc0a28c <get_clock>:
get_clock():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:48
9fc0a28c:	3c19bfb0 	lui	t9,0xbfb0
9fc0a290:	8f22e000 	lw	v0,-8192(t9)
9fc0a294:	03e00008 	jr	ra
9fc0a298:	00000000 	nop

9fc0a29c <get_ns>:
_get_count():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:6
9fc0a29c:	3c19bfb0 	lui	t9,0xbfb0
9fc0a2a0:	8f22e000 	lw	v0,-8192(t9)
9fc0a2a4:	00000000 	nop
9fc0a2a8:	000218c0 	sll	v1,v0,0x3
9fc0a2ac:	00021040 	sll	v0,v0,0x1
get_ns():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:56
9fc0a2b0:	03e00008 	jr	ra
9fc0a2b4:	00431021 	addu	v0,v0,v1

9fc0a2b8 <get_us>:
_get_count():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:6
9fc0a2b8:	3c19bfb0 	lui	t9,0xbfb0
9fc0a2bc:	8f23e000 	lw	v1,-8192(t9)
9fc0a2c0:	24020064 	li	v0,100
get_us():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:65
9fc0a2c4:	14400002 	bnez	v0,9fc0a2d0 <get_us+0x18>
9fc0a2c8:	0062001b 	divu	zero,v1,v0
9fc0a2cc:	0007000d 	break	0x7
9fc0a2d0:	00001012 	mflo	v0
9fc0a2d4:	03e00008 	jr	ra
9fc0a2d8:	00000000 	nop

9fc0a2dc <clock_gettime>:
clock_gettime():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:32
9fc0a2dc:	27bdffe8 	addiu	sp,sp,-24
9fc0a2e0:	afbf0014 	sw	ra,20(sp)
9fc0a2e4:	00a05021 	move	t2,a1
_get_count():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:6
9fc0a2e8:	3c19bfb0 	lui	t9,0xbfb0
9fc0a2ec:	8f26e000 	lw	a2,-8192(t9)
clock_gettime():
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:37
9fc0a2f0:	3c030001 	lui	v1,0x1
9fc0a2f4:	346386a0 	ori	v1,v1,0x86a0
9fc0a2f8:	14600002 	bnez	v1,9fc0a304 <clock_gettime+0x28>
9fc0a2fc:	00c3001b 	divu	zero,a2,v1
9fc0a300:	0007000d 	break	0x7
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:36
9fc0a304:	24080064 	li	t0,100
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:38
9fc0a308:	3c054876 	lui	a1,0x4876
9fc0a30c:	34a5e800 	ori	a1,a1,0xe800
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:35
9fc0a310:	000610c0 	sll	v0,a2,0x3
9fc0a314:	00063840 	sll	a3,a2,0x1
9fc0a318:	00e23821 	addu	a3,a3,v0
9fc0a31c:	240203e8 	li	v0,1000
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:39
9fc0a320:	3c049fc1 	lui	a0,0x9fc1
9fc0a324:	2484ac50 	addiu	a0,a0,-21424
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:37
9fc0a328:	00001812 	mflo	v1
9fc0a32c:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:36
9fc0a330:	15000002 	bnez	t0,9fc0a33c <clock_gettime+0x60>
9fc0a334:	00c8001b 	divu	zero,a2,t0
9fc0a338:	0007000d 	break	0x7
9fc0a33c:	00004012 	mflo	t0
9fc0a340:	00000000 	nop
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:38
9fc0a344:	14a00002 	bnez	a1,9fc0a350 <clock_gettime+0x74>
9fc0a348:	00c5001b 	divu	zero,a2,a1
9fc0a34c:	0007000d 	break	0x7
9fc0a350:	00003012 	mflo	a2
9fc0a354:	ad460000 	sw	a2,0(t2)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:37
9fc0a358:	14400002 	bnez	v0,9fc0a364 <clock_gettime+0x88>
9fc0a35c:	0062001b 	divu	zero,v1,v0
9fc0a360:	0007000d 	break	0x7
9fc0a364:	00004810 	mfhi	t1
9fc0a368:	ad49000c 	sw	t1,12(t2)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:35
9fc0a36c:	14400002 	bnez	v0,9fc0a378 <clock_gettime+0x9c>
9fc0a370:	00e2001b 	divu	zero,a3,v0
9fc0a374:	0007000d 	break	0x7
9fc0a378:	00002810 	mfhi	a1
9fc0a37c:	ad450004 	sw	a1,4(t2)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:36
9fc0a380:	14400002 	bnez	v0,9fc0a38c <clock_gettime+0xb0>
9fc0a384:	0102001b 	divu	zero,t0,v0
9fc0a388:	0007000d 	break	0x7
9fc0a38c:	00001810 	mfhi	v1
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:39
9fc0a390:	0ff02784 	jal	9fc09e10 <printf>
9fc0a394:	ad430008 	sw	v1,8(t2)
/home/scc2/nscscc/nscscc/nscscc2021_group_v0.01/perf_test_v0.01/soft/perf_func/lib/time.c:41
9fc0a398:	8fbf0014 	lw	ra,20(sp)
9fc0a39c:	00001021 	move	v0,zero
9fc0a3a0:	03e00008 	jr	ra
9fc0a3a4:	27bd0018 	addiu	sp,sp,24
	...

Disassembly of section .data:

9fc0a3b0 <list_known_crc-0x5b0>:
9fc0a3b0:	65726f63 	0x65726f63
9fc0a3b4:	6b72616d 	0x6b72616d
9fc0a3b8:	73657420 	0x73657420
9fc0a3bc:	65622074 	0x65622074
9fc0a3c0:	2e6e6967 	sltiu	t6,s3,26983
9fc0a3c4:	00000000 	nop
9fc0a3c8:	65726f63 	0x65726f63
9fc0a3cc:	6b72616d 	0x6b72616d
9fc0a3d0:	53415020 	0x53415020
9fc0a3d4:	00002153 	0x2153
9fc0a3d8:	65726f63 	0x65726f63
9fc0a3dc:	6b72616d 	0x6b72616d
9fc0a3e0:	52524520 	0x52524520
9fc0a3e4:	2121524f 	addi	at,t1,21071
9fc0a3e8:	00000021 	move	zero,zero
9fc0a3ec:	65726f63 	0x65726f63
9fc0a3f0:	6b72616d 	0x6b72616d
9fc0a3f4:	6f54203a 	0x6f54203a
9fc0a3f8:	206c6174 	addi	t4,v1,24948
9fc0a3fc:	6e756f43 	0x6e756f43
9fc0a400:	6f532874 	0x6f532874
9fc0a404:	6f632043 	0x6f632043
9fc0a408:	29746e75 	slti	s4,t3,28277
9fc0a40c:	30203d20 	andi	zero,at,0x3d20
9fc0a410:	0a782578 	j	99e095e0 <data_size+0x99e08cec>
9fc0a414:	00000000 	nop
9fc0a418:	65726f63 	0x65726f63
9fc0a41c:	6b72616d 	0x6b72616d
9fc0a420:	6f54203a 	0x6f54203a
9fc0a424:	206c6174 	addi	t4,v1,24948
9fc0a428:	6e756f43 	0x6e756f43
9fc0a42c:	50432874 	0x50432874
9fc0a430:	6f632055 	0x6f632055
9fc0a434:	29746e75 	slti	s4,t3,28277
9fc0a438:	30203d20 	andi	zero,at,0x3d20
9fc0a43c:	0a782578 	j	99e095e0 <data_size+0x99e08cec>
9fc0a440:	00000000 	nop
9fc0a444:	20677261 	addi	a3,v1,29281
9fc0a448:	6c25203a 	0x6c25203a
9fc0a44c:	25202c64 	addiu	zero,t1,11364
9fc0a450:	202c646c 	addi	t4,at,25708
9fc0a454:	2c646c25 	sltiu	a0,v1,27685
9fc0a458:	646c2520 	0x646c2520
9fc0a45c:	6c25202c 	0x6c25202c
9fc0a460:	25202c64 	addiu	zero,t1,11364
9fc0a464:	202c646c 	addi	t4,at,25708
9fc0a468:	20646c25 	addi	a0,v1,27685
9fc0a46c:	0000000a 	0xa
9fc0a470:	74736574 	jalx	91cd95d0 <data_size+0x91cd8cdc>
9fc0a474:	61747320 	0x61747320
9fc0a478:	00007472 	0x7472
9fc0a47c:	706d6f63 	0x706d6f63
9fc0a480:	74617475 	jalx	9185d1d4 <data_size+0x9185c8e0>
9fc0a484:	206e6f69 	addi	t6,v1,28521
9fc0a488:	656e6f64 	0x656e6f64
9fc0a48c:	00000020 	add	zero,zero,zero
9fc0a490:	70206b36 	0x70206b36
9fc0a494:	6f667265 	0x6f667265
9fc0a498:	6e616d72 	0x6e616d72
9fc0a49c:	72206563 	0x72206563
9fc0a4a0:	70206e75 	0x70206e75
9fc0a4a4:	6d617261 	0x6d617261
9fc0a4a8:	72657465 	0x72657465
9fc0a4ac:	6f662073 	0x6f662073
9fc0a4b0:	6f632072 	0x6f632072
9fc0a4b4:	616d6572 	0x616d6572
9fc0a4b8:	002e6b72 	0x2e6b72
9fc0a4bc:	76206b36 	jalx	9881acd8 <data_size+0x9881a3e4>
9fc0a4c0:	64696c61 	0x64696c61
9fc0a4c4:	6f697461 	0x6f697461
9fc0a4c8:	7572206e 	jalx	95c881b8 <data_size+0x95c878c4>
9fc0a4cc:	6170206e 	0x6170206e
9fc0a4d0:	656d6172 	0x656d6172
9fc0a4d4:	73726574 	0x73726574
9fc0a4d8:	726f6620 	0x726f6620
9fc0a4dc:	726f6320 	0x726f6320
9fc0a4e0:	72616d65 	0x72616d65
9fc0a4e4:	00002e6b 	0x2e6b
9fc0a4e8:	666f7250 	0x666f7250
9fc0a4ec:	20656c69 	addi	a1,v1,27753
9fc0a4f0:	656e6567 	0x656e6567
9fc0a4f4:	69746172 	0x69746172
9fc0a4f8:	72206e6f 	0x72206e6f
9fc0a4fc:	70206e75 	0x70206e75
9fc0a500:	6d617261 	0x6d617261
9fc0a504:	72657465 	0x72657465
9fc0a508:	6f662073 	0x6f662073
9fc0a50c:	6f632072 	0x6f632072
9fc0a510:	616d6572 	0x616d6572
9fc0a514:	002e6b72 	0x2e6b72
9fc0a518:	70204b32 	0x70204b32
9fc0a51c:	6f667265 	0x6f667265
9fc0a520:	6e616d72 	0x6e616d72
9fc0a524:	72206563 	0x72206563
9fc0a528:	70206e75 	0x70206e75
9fc0a52c:	6d617261 	0x6d617261
9fc0a530:	72657465 	0x72657465
9fc0a534:	6f662073 	0x6f662073
9fc0a538:	6f632072 	0x6f632072
9fc0a53c:	616d6572 	0x616d6572
9fc0a540:	002e6b72 	0x2e6b72
9fc0a544:	76204b32 	jalx	98812cc8 <data_size+0x988123d4>
9fc0a548:	64696c61 	0x64696c61
9fc0a54c:	6f697461 	0x6f697461
9fc0a550:	7572206e 	jalx	95c881b8 <data_size+0x95c878c4>
9fc0a554:	6170206e 	0x6170206e
9fc0a558:	656d6172 	0x656d6172
9fc0a55c:	73726574 	0x73726574
9fc0a560:	726f6620 	0x726f6620
9fc0a564:	726f6320 	0x726f6320
9fc0a568:	72616d65 	0x72616d65
9fc0a56c:	00002e6b 	0x2e6b
9fc0a570:	5d75255b 	0x5d75255b
9fc0a574:	4f525245 	c3	0x1525245
9fc0a578:	6c202152 	0x6c202152
9fc0a57c:	20747369 	addi	s4,v1,29545
9fc0a580:	20637263 	addi	v1,v1,29283
9fc0a584:	30257830 	andi	a1,at,0x7830
9fc0a588:	2d207834 	sltiu	zero,t1,30772
9fc0a58c:	6f687320 	0x6f687320
9fc0a590:	20646c75 	addi	a0,v1,27765
9fc0a594:	30206562 	andi	zero,at,0x6562
9fc0a598:	34302578 	ori	s0,at,0x2578
9fc0a59c:	00000a78 	0xa78
9fc0a5a0:	5d75255b 	0x5d75255b
9fc0a5a4:	4f525245 	c3	0x1525245
9fc0a5a8:	6d202152 	0x6d202152
9fc0a5ac:	69727461 	0x69727461
9fc0a5b0:	72632078 	0x72632078
9fc0a5b4:	78302063 	0x78302063
9fc0a5b8:	78343025 	0x78343025
9fc0a5bc:	73202d20 	0x73202d20
9fc0a5c0:	6c756f68 	0x6c756f68
9fc0a5c4:	65622064 	0x65622064
9fc0a5c8:	25783020 	addiu	t8,t3,12320
9fc0a5cc:	0a783430 	j	99e0d0c0 <data_size+0x99e0c7cc>
9fc0a5d0:	00000000 	nop
9fc0a5d4:	5d75255b 	0x5d75255b
9fc0a5d8:	4f525245 	c3	0x1525245
9fc0a5dc:	73202152 	0x73202152
9fc0a5e0:	65746174 	0x65746174
9fc0a5e4:	63726320 	0x63726320
9fc0a5e8:	25783020 	addiu	t8,t3,12320
9fc0a5ec:	20783430 	addi	t8,v1,13360
9fc0a5f0:	6873202d 	0x6873202d
9fc0a5f4:	646c756f 	0x646c756f
9fc0a5f8:	20656220 	addi	a1,v1,25120
9fc0a5fc:	30257830 	andi	a1,at,0x7830
9fc0a600:	000a7834 	0xa7834
9fc0a604:	65726f43 	0x65726f43
9fc0a608:	6b72614d 	0x6b72614d
9fc0a60c:	7a695320 	0x7a695320
9fc0a610:	20202065 	addi	zero,at,8293
9fc0a614:	25203a20 	addiu	zero,t1,14880
9fc0a618:	000a756c 	0xa756c
9fc0a61c:	61746f54 	0x61746f54
9fc0a620:	736e206c 	0x736e206c
9fc0a624:	25203a20 	addiu	zero,t1,14880
9fc0a628:	000a756c 	0xa756c
9fc0a62c:	72657449 	0x72657449
9fc0a630:	6f697461 	0x6f697461
9fc0a634:	532f736e 	0x532f736e
9fc0a638:	3a206365 	xori	zero,s1,0x6365
9fc0a63c:	756c2520 	jalx	95b09480 <data_size+0x95b08b8c>
9fc0a640:	0000000a 	0xa
9fc0a644:	45524f43 	0x45524f43
9fc0a648:	4b52414d 	c2	0x152414d
9fc0a64c:	5a484d2f 	0x5a484d2f
9fc0a650:	28203d20 	slti	zero,at,15648
9fc0a654:	30303031 	andi	s0,at,0x3031
9fc0a658:	2e303030 	sltiu	s0,s1,12336
9fc0a65c:	50432f30 	0x50432f30
9fc0a660:	4f435f55 	c3	0x1435f55
9fc0a664:	5f544e55 	0x5f544e55
9fc0a668:	5f524550 	0x5f524550
9fc0a66c:	2a295355 	slti	t1,s1,21333
9fc0a670:	4345534e 	c0	0x145534e
9fc0a674:	5245505f 	0x5245505f
9fc0a678:	4553555f 	0x4553555f
9fc0a67c:	65722a43 	0x65722a43
9fc0a680:	746c7573 	jalx	91b1d5cc <data_size+0x91b1ccd8>
9fc0a684:	5d305b73 	0x5d305b73
9fc0a688:	6574692e 	0x6574692e
9fc0a68c:	69746172 	0x69746172
9fc0a690:	2f736e6f 	sltiu	s3,k1,28271
9fc0a694:	61746f74 	0x61746f74
9fc0a698:	736e5f6c 	0x736e5f6c
9fc0a69c:	00000000 	nop
9fc0a6a0:	65207449 	0x65207449
9fc0a6a4:	6c617571 	0x6c617571
9fc0a6a8:	6f742073 	0x6f742073
9fc0a6ac:	30303120 	andi	s0,at,0x3120
9fc0a6b0:	30312a30 	andi	s1,at,0x2a30
9fc0a6b4:	692a3030 	0x692a3030
9fc0a6b8:	61726574 	0x61726574
9fc0a6bc:	6e6f6974 	0x6e6f6974
9fc0a6c0:	746f742f 	jalx	91bdd0bc <data_size+0x91bdc7c8>
9fc0a6c4:	6e5f6c61 	0x6e5f6c61
9fc0a6c8:	00000073 	0x73
9fc0a6cc:	74206e49 	jalx	9081b924 <data_size+0x9081b030>
9fc0a6d0:	20736968 	addi	s3,v1,26984
9fc0a6d4:	2c6e7572 	sltiu	t6,v1,30066
9fc0a6d8:	65746920 	0x65746920
9fc0a6dc:	65746172 	0x65746172
9fc0a6e0:	756c253d 	jalx	95b094f4 <data_size+0x95b08c00>
9fc0a6e4:	6f74202c 	0x6f74202c
9fc0a6e8:	5f6c6174 	0x5f6c6174
9fc0a6ec:	253d736e 	addiu	sp,t1,29550
9fc0a6f0:	0a0a756c 	j	9829d5b0 <data_size+0x9829ccbc>
9fc0a6f4:	00000000 	nop
9fc0a6f8:	61746f54 	0x61746f54
9fc0a6fc:	6974206c 	0x6974206c
9fc0a700:	20736b63 	addi	s3,v1,27491
9fc0a704:	20202020 	addi	zero,at,8224
9fc0a708:	25203a20 	addiu	zero,t1,14880
9fc0a70c:	000a756c 	0xa756c
9fc0a710:	61746f54 	0x61746f54
9fc0a714:	6974206c 	0x6974206c
9fc0a718:	2820656d 	slti	zero,at,25965
9fc0a71c:	73636573 	0x73636573
9fc0a720:	25203a29 	addiu	zero,t1,14889
9fc0a724:	00000a64 	0xa64
9fc0a728:	72657449 	0x72657449
9fc0a72c:	6f697461 	0x6f697461
9fc0a730:	532f736e 	0x532f736e
9fc0a734:	20206365 	addi	zero,at,25445
9fc0a738:	25203a20 	addiu	zero,t1,14880
9fc0a73c:	00000a64 	0xa64
9fc0a740:	72657449 	0x72657449
9fc0a744:	6f697461 	0x6f697461
9fc0a748:	2020736e 	addi	zero,at,29550
9fc0a74c:	20202020 	addi	zero,at,8224
9fc0a750:	25203a20 	addiu	zero,t1,14880
9fc0a754:	000a756c 	0xa756c
9fc0a758:	706d6f43 	0x706d6f43
9fc0a75c:	72656c69 	0x72656c69
9fc0a760:	72657620 	0x72657620
9fc0a764:	6e6f6973 	0x6e6f6973
9fc0a768:	25203a20 	addiu	zero,t1,14880
9fc0a76c:	00000a73 	0xa73
9fc0a770:	34434347 	ori	v1,v0,0x4347
9fc0a774:	302e332e 	andi	t6,at,0x332e
9fc0a778:	00000000 	nop
9fc0a77c:	706d6f43 	0x706d6f43
9fc0a780:	72656c69 	0x72656c69
9fc0a784:	616c6620 	0x616c6620
9fc0a788:	20207367 	addi	zero,at,29543
9fc0a78c:	25203a20 	addiu	zero,t1,14880
9fc0a790:	00000a73 	0xa73
9fc0a794:	6f6d654d 	0x6f6d654d
9fc0a798:	6c207972 	0x6c207972
9fc0a79c:	7461636f 	jalx	91858dbc <data_size+0x918584c8>
9fc0a7a0:	206e6f69 	addi	t6,v1,28521
9fc0a7a4:	25203a20 	addiu	zero,t1,14880
9fc0a7a8:	00000a73 	0xa73
9fc0a7ac:	61656c50 	0x61656c50
9fc0a7b0:	70206573 	0x70206573
9fc0a7b4:	64207475 	0x64207475
9fc0a7b8:	20617461 	addi	at,v1,29793
9fc0a7bc:	6f6d656d 	0x6f6d656d
9fc0a7c0:	6c207972 	0x6c207972
9fc0a7c4:	7461636f 	jalx	91858dbc <data_size+0x918584c8>
9fc0a7c8:	206e6f69 	addi	t6,v1,28521
9fc0a7cc:	65726568 	0x65726568
9fc0a7d0:	0909090a 	j	94242428 <data_size+0x94241b34>
9fc0a7d4:	672e6528 	0x672e6528
9fc0a7d8:	6f63202e 	0x6f63202e
9fc0a7dc:	69206564 	0x69206564
9fc0a7e0:	6c66206e 	0x6c66206e
9fc0a7e4:	2c687361 	sltiu	t0,v1,29537
9fc0a7e8:	74616420 	jalx	91859080 <data_size+0x9185878c>
9fc0a7ec:	6e6f2061 	0x6e6f2061
9fc0a7f0:	61656820 	0x61656820
9fc0a7f4:	74652070 	jalx	919481c0 <data_size+0x919478cc>
9fc0a7f8:	00002963 	0x2963
9fc0a7fc:	64656573 	0x64656573
9fc0a800:	20637263 	addi	v1,v1,29283
9fc0a804:	20202020 	addi	zero,at,8224
9fc0a808:	20202020 	addi	zero,at,8224
9fc0a80c:	30203a20 	andi	zero,at,0x3a20
9fc0a810:	34302578 	ori	s0,at,0x2578
9fc0a814:	00000a78 	0xa78
9fc0a818:	5d64255b 	0x5d64255b
9fc0a81c:	6c637263 	0x6c637263
9fc0a820:	20747369 	addi	s4,v1,29545
9fc0a824:	20202020 	addi	zero,at,8224
9fc0a828:	203a2020 	addi	k0,at,8224
9fc0a82c:	30257830 	andi	a1,at,0x7830
9fc0a830:	000a7834 	0xa7834
9fc0a834:	5d64255b 	0x5d64255b
9fc0a838:	6d637263 	0x6d637263
9fc0a83c:	69727461 	0x69727461
9fc0a840:	20202078 	addi	zero,at,8312
9fc0a844:	203a2020 	addi	k0,at,8224
9fc0a848:	30257830 	andi	a1,at,0x7830
9fc0a84c:	000a7834 	0xa7834
9fc0a850:	5d64255b 	0x5d64255b
9fc0a854:	73637263 	0x73637263
9fc0a858:	65746174 	0x65746174
9fc0a85c:	20202020 	addi	zero,at,8224
9fc0a860:	203a2020 	addi	k0,at,8224
9fc0a864:	30257830 	andi	a1,at,0x7830
9fc0a868:	000a7834 	0xa7834
9fc0a86c:	5d64255b 	0x5d64255b
9fc0a870:	66637263 	0x66637263
9fc0a874:	6c616e69 	0x6c616e69
9fc0a878:	20202020 	addi	zero,at,8224
9fc0a87c:	203a2020 	addi	k0,at,8224
9fc0a880:	30257830 	andi	a1,at,0x7830
9fc0a884:	000a7834 	0xa7834
9fc0a888:	72726f43 	0x72726f43
9fc0a88c:	20746365 	addi	s4,v1,25445
9fc0a890:	7265706f 	0x7265706f
9fc0a894:	6f697461 	0x6f697461
9fc0a898:	6176206e 	0x6176206e
9fc0a89c:	6164696c 	0x6164696c
9fc0a8a0:	2e646574 	sltiu	a0,s3,25972
9fc0a8a4:	65655320 	0x65655320
9fc0a8a8:	61657220 	0x61657220
9fc0a8ac:	2e656d64 	sltiu	a1,s3,28004
9fc0a8b0:	20747874 	addi	s4,v1,30836
9fc0a8b4:	20726f66 	addi	s2,v1,28518
9fc0a8b8:	206e7572 	addi	t6,v1,30066
9fc0a8bc:	20646e61 	addi	a0,v1,28257
9fc0a8c0:	6f706572 	0x6f706572
9fc0a8c4:	6e697472 	0x6e697472
9fc0a8c8:	75722067 	jalx	95c8819c <data_size+0x95c878a8>
9fc0a8cc:	2e73656c 	sltiu	s3,s3,25964
9fc0a8d0:	00000000 	nop
9fc0a8d4:	6f727245 	0x6f727245
9fc0a8d8:	64207372 	0x64207372
9fc0a8dc:	63657465 	0x63657465
9fc0a8e0:	00646574 	0x646574
9fc0a8e4:	6e6e6143 	0x6e6e6143
9fc0a8e8:	7620746f 	jalx	9881d1bc <data_size+0x9881c8c8>
9fc0a8ec:	64696c61 	0x64696c61
9fc0a8f0:	20657461 	addi	a1,v1,29793
9fc0a8f4:	7265706f 	0x7265706f
9fc0a8f8:	6f697461 	0x6f697461
9fc0a8fc:	6f66206e 	0x6f66206e
9fc0a900:	68742072 	0x68742072
9fc0a904:	20657365 	addi	a1,v1,29541
9fc0a908:	64656573 	0x64656573
9fc0a90c:	6c617620 	0x6c617620
9fc0a910:	2c736575 	sltiu	s3,v1,25973
9fc0a914:	656c7020 	0x656c7020
9fc0a918:	20657361 	addi	a1,v1,29537
9fc0a91c:	706d6f63 	0x706d6f63
9fc0a920:	20657261 	addi	a1,v1,29281
9fc0a924:	68746977 	0x68746977
9fc0a928:	73657220 	0x73657220
9fc0a92c:	73746c75 	0x73746c75
9fc0a930:	206e6f20 	addi	t6,v1,28448
9fc0a934:	6e6b2061 	0x6e6b2061
9fc0a938:	206e776f 	addi	t6,v1,30575
9fc0a93c:	74616c70 	jalx	9185b1c0 <data_size+0x9185a8cc>
9fc0a940:	6d726f66 	0x6d726f66
9fc0a944:	0000002e 	0x2e
9fc0a948:	74617453 	jalx	9185d14c <data_size+0x9185c858>
9fc0a94c:	00006369 	0x6369
9fc0a950:	70616548 	0x70616548
9fc0a954:	00000000 	nop
9fc0a958:	63617453 	0x63617453
9fc0a95c:	0000006b 	0x6b

9fc0a960 <list_known_crc>:
9fc0a960:	3340d4b0 	andi	zero,k0,0xd4b0
9fc0a964:	e7146a79 	swc1	$f20,27257(t8)
9fc0a968:	0000e3c1 	0xe3c1

9fc0a96c <matrix_known_crc>:
9fc0a96c:	1199be52 	beq	t4,t9,9fbfa2b8 <data_size+0x9fbf99c4>
9fc0a970:	1fd75608 	0x1fd75608
9fc0a974:	00000747 	0x747

9fc0a978 <state_known_crc>:
9fc0a978:	39bf5e47 	xori	ra,t5,0x5e47
9fc0a97c:	8e3ae5a4 	lw	k0,-6748(s1)
9fc0a980:	00008d84 	0x8d84
	...
9fc0a990:	9fc05bd0 	0x9fc05bd0
9fc0a994:	9fc05bd0 	0x9fc05bd0
9fc0a998:	9fc05bd0 	0x9fc05bd0
9fc0a99c:	9fc05bb0 	0x9fc05bb0
9fc0a9a0:	9fc05bb0 	0x9fc05bb0
9fc0a9a4:	9fc05b90 	0x9fc05b90
9fc0a9a8:	9fc05b90 	0x9fc05b90
9fc0a9ac:	9fc05a40 	0x9fc05a40
9fc0a9b0:	9fc06100 	0x9fc06100
9fc0a9b4:	9fc06124 	0x9fc06124
9fc0a9b8:	9fc06240 	0x9fc06240
9fc0a9bc:	9fc06270 	0x9fc06270
9fc0a9c0:	9fc06210 	0x9fc06210
9fc0a9c4:	9fc061d0 	0x9fc061d0
9fc0a9c8:	9fc061a0 	0x9fc061a0
9fc0a9cc:	9fc06170 	0x9fc06170
9fc0a9d0:	9fc06650 	0x9fc06650
9fc0a9d4:	9fc064a0 	0x9fc064a0
9fc0a9d8:	9fc06680 	0x9fc06680
9fc0a9dc:	9fc065f0 	0x9fc065f0
9fc0a9e0:	9fc065c0 	0x9fc065c0
9fc0a9e4:	9fc06470 	0x9fc06470
9fc0a9e8:	9fc06620 	0x9fc06620
9fc0a9ec:	9fc06520 	0x9fc06520
9fc0a9f0:	9fc06a50 	0x9fc06a50
9fc0a9f4:	9fc06840 	0x9fc06840
9fc0a9f8:	9fc06ae0 	0x9fc06ae0
9fc0a9fc:	9fc06a80 	0x9fc06a80
9fc0aa00:	9fc06ab0 	0x9fc06ab0
9fc0aa04:	9fc06810 	0x9fc06810
9fc0aa08:	9fc06a20 	0x9fc06a20
9fc0aa0c:	9fc068c0 	0x9fc068c0

9fc0aa10 <intpat>:
9fc0aa10:	9fc0aa50 	0x9fc0aa50
9fc0aa14:	9fc0aa58 	0x9fc0aa58
9fc0aa18:	9fc0aa60 	0x9fc0aa60
9fc0aa1c:	9fc0aa68 	0x9fc0aa68

9fc0aa20 <floatpat>:
9fc0aa20:	9fc0aa70 	0x9fc0aa70
9fc0aa24:	9fc0aa7c 	0x9fc0aa7c
9fc0aa28:	9fc0aa88 	0x9fc0aa88
9fc0aa2c:	9fc0aa94 	0x9fc0aa94

9fc0aa30 <scipat>:
9fc0aa30:	9fc0aaa0 	0x9fc0aaa0
9fc0aa34:	9fc0aaac 	0x9fc0aaac
9fc0aa38:	9fc0aab8 	0x9fc0aab8
9fc0aa3c:	9fc0aac4 	0x9fc0aac4

9fc0aa40 <errpat>:
9fc0aa40:	9fc0aad0 	0x9fc0aad0
9fc0aa44:	9fc0aadc 	0x9fc0aadc
9fc0aa48:	9fc0aae8 	0x9fc0aae8
9fc0aa4c:	9fc0aaf4 	0x9fc0aaf4
9fc0aa50:	32313035 	andi	s1,s1,0x3035
9fc0aa54:	00000000 	nop
9fc0aa58:	34333231 	ori	s3,at,0x3231
9fc0aa5c:	00000000 	nop
9fc0aa60:	3437382d 	ori	s7,at,0x382d
9fc0aa64:	00000000 	nop
9fc0aa68:	3232312b 	andi	s2,s1,0x312b
9fc0aa6c:	00000000 	nop
9fc0aa70:	352e3533 	ori	t6,t1,0x3533
9fc0aa74:	30303434 	andi	s0,at,0x3434
9fc0aa78:	00000000 	nop
9fc0aa7c:	3332312e 	andi	s2,t9,0x312e
9fc0aa80:	30303534 	andi	s0,at,0x3534
9fc0aa84:	00000000 	nop
9fc0aa88:	3031312d 	andi	s1,at,0x312d
9fc0aa8c:	3030372e 	andi	s0,at,0x372e
9fc0aa90:	00000000 	nop
9fc0aa94:	362e302b 	ori	t6,s1,0x302b
9fc0aa98:	30303434 	andi	s0,at,0x3434
9fc0aa9c:	00000000 	nop
9fc0aaa0:	30352e35 	andi	s5,at,0x2e35
9fc0aaa4:	332b6530 	andi	t3,t9,0x6530
9fc0aaa8:	00000000 	nop
9fc0aaac:	32312e2d 	andi	s1,s1,0x2e2d
9fc0aab0:	322d6533 	andi	t5,s1,0x6533
9fc0aab4:	00000000 	nop
9fc0aab8:	6537382d 	0x6537382d
9fc0aabc:	3233382b 	andi	s3,s1,0x382b
9fc0aac0:	00000000 	nop
9fc0aac4:	362e302b 	ori	t6,s1,0x302b
9fc0aac8:	32312d65 	andi	s1,s1,0x2d65
9fc0aacc:	00000000 	nop
9fc0aad0:	332e3054 	andi	t6,t9,0x3054
9fc0aad4:	46312d65 	c1	0x312d65
9fc0aad8:	00000000 	nop
9fc0aadc:	542e542d 	0x542e542d
9fc0aae0:	71542b2b 	0x71542b2b
9fc0aae4:	00000000 	nop
9fc0aae8:	2e335431 	sltiu	s3,s1,21553
9fc0aaec:	7a346534 	0x7a346534
9fc0aaf0:	00000000 	nop
9fc0aaf4:	302e3433 	andi	t6,at,0x3433
9fc0aaf8:	5e542d65 	0x5e542d65
9fc0aafc:	00000000 	nop
9fc0ab00:	9fc0a014 	0x9fc0a014
9fc0ab04:	9fc09ee4 	0x9fc09ee4
9fc0ab08:	9fc09ee4 	0x9fc09ee4
9fc0ab0c:	9fc09ee4 	0x9fc09ee4
9fc0ab10:	9fc09ee4 	0x9fc09ee4
9fc0ab14:	9fc09ee4 	0x9fc09ee4
9fc0ab18:	9fc09ee4 	0x9fc09ee4
9fc0ab1c:	9fc09ee4 	0x9fc09ee4
9fc0ab20:	9fc09ee4 	0x9fc09ee4
9fc0ab24:	9fc09ee4 	0x9fc09ee4
9fc0ab28:	9fc09ee4 	0x9fc09ee4
9fc0ab2c:	9fc09f08 	0x9fc09f08
9fc0ab30:	9fc09f18 	0x9fc09f18
9fc0ab34:	9fc09f18 	0x9fc09f18
9fc0ab38:	9fc09f18 	0x9fc09f18
9fc0ab3c:	9fc09f18 	0x9fc09f18
9fc0ab40:	9fc09f18 	0x9fc09f18
9fc0ab44:	9fc09f18 	0x9fc09f18
9fc0ab48:	9fc09f18 	0x9fc09f18
9fc0ab4c:	9fc09f18 	0x9fc09f18
9fc0ab50:	9fc09f18 	0x9fc09f18
9fc0ab54:	9fc09ee4 	0x9fc09ee4
9fc0ab58:	9fc09ee4 	0x9fc09ee4
9fc0ab5c:	9fc09ee4 	0x9fc09ee4
9fc0ab60:	9fc09ee4 	0x9fc09ee4
9fc0ab64:	9fc09ee4 	0x9fc09ee4
9fc0ab68:	9fc09ee4 	0x9fc09ee4
9fc0ab6c:	9fc09ee4 	0x9fc09ee4
9fc0ab70:	9fc09ee4 	0x9fc09ee4
9fc0ab74:	9fc09ee4 	0x9fc09ee4
9fc0ab78:	9fc09ee4 	0x9fc09ee4
9fc0ab7c:	9fc09ee4 	0x9fc09ee4
9fc0ab80:	9fc09ee4 	0x9fc09ee4
9fc0ab84:	9fc09ee4 	0x9fc09ee4
9fc0ab88:	9fc09ee4 	0x9fc09ee4
9fc0ab8c:	9fc09ee4 	0x9fc09ee4
9fc0ab90:	9fc09ee4 	0x9fc09ee4
9fc0ab94:	9fc09ee4 	0x9fc09ee4
9fc0ab98:	9fc09ee4 	0x9fc09ee4
9fc0ab9c:	9fc09ee4 	0x9fc09ee4
9fc0aba0:	9fc09ee4 	0x9fc09ee4
9fc0aba4:	9fc09ee4 	0x9fc09ee4
9fc0aba8:	9fc09ee4 	0x9fc09ee4
9fc0abac:	9fc09ee4 	0x9fc09ee4
9fc0abb0:	9fc09ee4 	0x9fc09ee4
9fc0abb4:	9fc09ee4 	0x9fc09ee4
9fc0abb8:	9fc09ee4 	0x9fc09ee4
9fc0abbc:	9fc09ee4 	0x9fc09ee4
9fc0abc0:	9fc09ee4 	0x9fc09ee4
9fc0abc4:	9fc09ee4 	0x9fc09ee4
9fc0abc8:	9fc09ee4 	0x9fc09ee4
9fc0abcc:	9fc09ee4 	0x9fc09ee4
9fc0abd0:	9fc09ee4 	0x9fc09ee4
9fc0abd4:	9fc09ee4 	0x9fc09ee4
9fc0abd8:	9fc09ee4 	0x9fc09ee4
9fc0abdc:	9fc09ee4 	0x9fc09ee4
9fc0abe0:	9fc09ee4 	0x9fc09ee4
9fc0abe4:	9fc09ee4 	0x9fc09ee4
9fc0abe8:	9fc09ee4 	0x9fc09ee4
9fc0abec:	9fc09ee4 	0x9fc09ee4
9fc0abf0:	9fc09ee4 	0x9fc09ee4
9fc0abf4:	9fc09ff8 	0x9fc09ff8
9fc0abf8:	9fc09fe4 	0x9fc09fe4
9fc0abfc:	9fc09fc8 	0x9fc09fc8
9fc0ac00:	9fc09ee4 	0x9fc09ee4
9fc0ac04:	9fc09ee4 	0x9fc09ee4
9fc0ac08:	9fc09ee4 	0x9fc09ee4
9fc0ac0c:	9fc09ee4 	0x9fc09ee4
9fc0ac10:	9fc09ee4 	0x9fc09ee4
9fc0ac14:	9fc09ee4 	0x9fc09ee4
9fc0ac18:	9fc09ee4 	0x9fc09ee4
9fc0ac1c:	9fc09fac 	0x9fc09fac
9fc0ac20:	9fc09ee4 	0x9fc09ee4
9fc0ac24:	9fc09ee4 	0x9fc09ee4
9fc0ac28:	9fc09f90 	0x9fc09f90
9fc0ac2c:	9fc09f74 	0x9fc09f74
9fc0ac30:	9fc09ee4 	0x9fc09ee4
9fc0ac34:	9fc09ee4 	0x9fc09ee4
9fc0ac38:	9fc09f60 	0x9fc09f60
9fc0ac3c:	9fc09ee4 	0x9fc09ee4
9fc0ac40:	9fc09f44 	0x9fc09f44
9fc0ac44:	9fc09ee4 	0x9fc09ee4
9fc0ac48:	9fc09ee4 	0x9fc09ee4
9fc0ac4c:	9fc09f74 	0x9fc09f74
9fc0ac50:	636f6c63 	0x636f6c63
9fc0ac54:	736e206b 	0x736e206b
9fc0ac58:	2c64253d 	sltiu	a0,v1,9533
9fc0ac5c:	3d636573 	0x3d636573
9fc0ac60:	000a6425 	0xa6425
9fc0ac64:	ba007f00 	swr	zero,32512(s0)
	...

9fc0ac7c <_fdata>:
_fdata():
9fc0ac7c:	00000000 	nop

9fc0ac80 <mem_name>:
9fc0ac80:	9fc0a948 	0x9fc0a948
9fc0ac84:	9fc0a950 	0x9fc0a950
9fc0ac88:	9fc0a958 	0x9fc0a958
9fc0ac8c:	00000000 	nop

9fc0ac90 <__CTOR_LIST__>:
	...

9fc0ac98 <__CTOR_END__>:
	...

9fc0aca0 <__DTOR_END__>:
__DTOR_END__():
9fc0aca0:	00000001 	0x1

Disassembly of section .bss:

9fc0acb0 <start_time_val>:
	...

9fc0acc0 <stop_time_val>:
	...

Disassembly of section .debug_aranges:

00000000 <.debug_aranges>:
   0:	0000001c 	0x1c
   4:	00000002 	srl	zero,zero,0x0
   8:	00040000 	sll	zero,a0,0x0
   c:	00000000 	nop
  10:	9fc09e10 	0x9fc09e10
  14:	00000250 	0x250
	...
  20:	0000001c 	0x1c
  24:	00cd0002 	0xcd0002
  28:	00040000 	sll	zero,a0,0x0
  2c:	00000000 	nop
  30:	9fc0a060 	0x9fc0a060
  34:	00000034 	0x34
	...
  40:	0000001c 	0x1c
  44:	01590002 	0x1590002
  48:	00040000 	sll	zero,a0,0x0
  4c:	00000000 	nop
  50:	9fc0a0a0 	0x9fc0a0a0
  54:	000000b8 	0xb8
	...
  60:	0000001c 	0x1c
  64:	02080002 	0x2080002
  68:	00040000 	sll	zero,a0,0x0
  6c:	00000000 	nop
  70:	9fc0a160 	0x9fc0a160
  74:	00000100 	sll	zero,zero,0x4
	...
  80:	0000001c 	0x1c
  84:	03030002 	0x3030002
  88:	00040000 	sll	zero,a0,0x0
  8c:	00000000 	nop
  90:	9fc0a260 	0x9fc0a260
  94:	00000148 	0x148
	...

Disassembly of section .debug_pubnames:

00000000 <.debug_pubnames>:
   0:	00000019 	multu	zero,zero
   4:	00000002 	srl	zero,zero,0x0
   8:	00cd0000 	0xcd0000
   c:	00350000 	0x350000
  10:	72700000 	0x72700000
  14:	66746e69 	0x66746e69
  18:	00000000 	nop
  1c:	00002a00 	sll	a1,zero,0x8
  20:	cd000200 	lwc3	$0,512(t0)
  24:	8c000000 	lw	zero,0(zero)
  28:	33000000 	andi	zero,t8,0x0
  2c:	74000000 	jalx	0 <data_size-0x8f4>
  30:	705f7467 	0x705f7467
  34:	68637475 	0x68637475
  38:	60007261 	0x60007261
  3c:	70000000 	0x70000000
  40:	68637475 	0x68637475
  44:	00007261 	0x7261
  48:	25000000 	addiu	zero,t0,0
  4c:	02000000 	0x2000000
  50:	00015900 	sll	t3,at,0x4
  54:	0000af00 	sll	s5,zero,0x1c
  58:	00003300 	sll	a2,zero,0xc
  5c:	74757000 	jalx	1d5c000 <data_size+0x1d5b70c>
  60:	69727473 	0x69727473
  64:	8300676e 	lb	zero,26478(t8)
  68:	70000000 	0x70000000
  6c:	00737475 	0x737475
  70:	00000000 	nop
  74:	0000001c 	0x1c
  78:	02080002 	0x2080002
  7c:	00fb0000 	0xfb0000
  80:	00330000 	0x330000
  84:	72700000 	0x72700000
  88:	62746e69 	0x62746e69
  8c:	00657361 	0x657361
  90:	00000000 	nop
  94:	00000072 	0x72
  98:	03030002 	0x3030002
  9c:	023b0000 	0x23b0000
  a0:	00a70000 	0xa70000
  a4:	675f0000 	0x675f0000
  a8:	635f7465 	0x635f7465
  ac:	746e756f 	jalx	1b9d5bc <data_size+0x1b9ccc8>
  b0:	0000c400 	sll	t8,zero,0x10
  b4:	74656700 	jalx	1959c00 <data_size+0x195930c>
  b8:	756f635f 	jalx	5bd8d7c <data_size+0x5bd8488>
  bc:	de00746e 	0xde00746e
  c0:	67000000 	0x67000000
  c4:	635f7465 	0x635f7465
  c8:	746e756f 	jalx	1b9d5bc <data_size+0x1b9ccc8>
  cc:	00796d5f 	0x796d5f
  d0:	0000010a 	0x10a
  d4:	5f746567 	0x5f746567
  d8:	636f6c63 	0x636f6c63
  dc:	0132006b 	0x132006b
  e0:	65670000 	0x65670000
  e4:	736e5f74 	0x736e5f74
  e8:	00017e00 	sll	t7,at,0x18
  ec:	74656700 	jalx	1959c00 <data_size+0x195930c>
  f0:	0073755f 	0x73755f
  f4:	000001c8 	0x1c8
  f8:	636f6c63 	0x636f6c63
  fc:	65675f6b 	0x65675f6b
 100:	6d697474 	0x6d697474
 104:	00000065 	0x65
	...

Disassembly of section .pdr:

00000000 <.pdr>:
   0:	9fc003c0 	0x9fc003c0
   4:	c0ff0000 	lwc0	$31,0(a3)
   8:	fffffffc 	0xfffffffc
	...
  14:	00000058 	0x58
  18:	0000001d 	0x1d
  1c:	0000001f 	0x1f
  20:	9fc00700 	0x9fc00700
  24:	800f0000 	lb	t7,0(zero)
  28:	fffffffc 	0xfffffffc
	...
  34:	00000028 	0x28
  38:	0000001d 	0x1d
  3c:	0000001f 	0x1f
  40:	9fc00890 	0x9fc00890
  44:	c0ff0000 	lwc0	$31,0(a3)
  48:	fffffffc 	0xfffffffc
	...
  54:	00000868 	0x868
  58:	0000001d 	0x1d
  5c:	0000001f 	0x1f
  60:	9fc016b0 	0x9fc016b0
  64:	003f0000 	0x3f0000
  68:	fffffffc 	0xfffffffc
	...
  74:	00000018 	mult	zero,zero
  78:	0000001d 	0x1d
  7c:	0000001f 	0x1f
  80:	9fc01aa0 	0x9fc01aa0
	...
  98:	0000001d 	0x1d
  9c:	0000001f 	0x1f
  a0:	9fc01de0 	0x9fc01de0
  a4:	00070000 	sll	zero,a3,0x0
  a8:	fffffffc 	0xfffffffc
	...
  b4:	00000010 	mfhi	zero
  b8:	0000001d 	0x1d
  bc:	0000001f 	0x1f
  c0:	9fc02020 	0x9fc02020
	...
  d8:	0000001d 	0x1d
  dc:	0000001f 	0x1f
  e0:	9fc021c0 	0x9fc021c0
  e4:	40ff0000 	0x40ff0000
  e8:	fffffffc 	0xfffffffc
	...
  f4:	00000028 	0x28
  f8:	0000001d 	0x1d
  fc:	0000001f 	0x1f
 100:	9fc02470 	0x9fc02470
 104:	40ff0000 	0x40ff0000
 108:	fffffffc 	0xfffffffc
	...
 114:	00000038 	0x38
 118:	0000001d 	0x1d
 11c:	0000001f 	0x1f
 120:	9fc02770 	0x9fc02770
 124:	40ff0000 	0x40ff0000
 128:	fffffffc 	0xfffffffc
	...
 134:	00000028 	0x28
 138:	0000001d 	0x1d
 13c:	0000001f 	0x1f
 140:	9fc02a10 	0x9fc02a10
 144:	c0ff0000 	lwc0	$31,0(a3)
 148:	fffffffc 	0xfffffffc
	...
 154:	00000060 	0x60
 158:	0000001d 	0x1d
 15c:	0000001f 	0x1f
 160:	9fc04160 	0x9fc04160
 164:	c0ff0000 	lwc0	$31,0(a3)
 168:	fffffffc 	0xfffffffc
	...
 174:	00000068 	0x68
 178:	0000001d 	0x1d
 17c:	0000001f 	0x1f
 180:	9fc058a0 	0x9fc058a0
	...
 198:	0000001d 	0x1d
 19c:	0000001f 	0x1f
 1a0:	9fc058b0 	0x9fc058b0
	...
 1b8:	0000001d 	0x1d
 1bc:	0000001f 	0x1f
 1c0:	9fc058c0 	0x9fc058c0
	...
 1d8:	0000001d 	0x1d
 1dc:	0000001f 	0x1f
 1e0:	9fc05920 	0x9fc05920
	...
 1f8:	0000001d 	0x1d
 1fc:	0000001f 	0x1f
 200:	9fc05940 	0x9fc05940
	...
 218:	0000001d 	0x1d
 21c:	0000001f 	0x1f
 220:	9fc05950 	0x9fc05950
	...
 238:	0000001d 	0x1d
 23c:	0000001f 	0x1f
 240:	9fc05960 	0x9fc05960
	...
 258:	0000001d 	0x1d
 25c:	0000001f 	0x1f
 260:	9fc05970 	0x9fc05970
	...
 278:	0000001d 	0x1d
 27c:	0000001f 	0x1f
 280:	9fc05980 	0x9fc05980
 284:	00ff0000 	0xff0000
 288:	fffffffc 	0xfffffffc
	...
 294:	00000020 	add	zero,zero,zero
 298:	0000001d 	0x1d
 29c:	0000001f 	0x1f
 2a0:	9fc06070 	0x9fc06070
 2a4:	001f0000 	sll	zero,ra,0x0
 2a8:	fffffffc 	0xfffffffc
	...
 2b4:	00000018 	mult	zero,zero
 2b8:	0000001d 	0x1d
 2bc:	0000001f 	0x1f
 2c0:	9fc063a0 	0x9fc063a0
 2c4:	801f0000 	lb	ra,0(zero)
 2c8:	fffffffc 	0xfffffffc
	...
 2d4:	00000068 	0x68
 2d8:	0000001d 	0x1d
 2dc:	0000001f 	0x1f
 2e0:	9fc06c30 	0x9fc06c30
	...
 2f8:	0000001d 	0x1d
 2fc:	0000001f 	0x1f
 300:	9fc06dd0 	0x9fc06dd0
	...
 318:	0000001d 	0x1d
 31c:	0000001f 	0x1f
 320:	9fc07000 	0x9fc07000
	...
 338:	0000001d 	0x1d
 33c:	0000001f 	0x1f
 340:	9fc07010 	0x9fc07010
	...
 358:	0000001d 	0x1d
 35c:	0000001f 	0x1f
 360:	9fc07480 	0x9fc07480
	...
 378:	0000001d 	0x1d
 37c:	0000001f 	0x1f
 380:	9fc078f0 	0x9fc078f0
	...
 398:	0000001d 	0x1d
 39c:	0000001f 	0x1f
 3a0:	9fc081e0 	0x9fc081e0
	...
 3b8:	0000001d 	0x1d
 3bc:	0000001f 	0x1f
 3c0:	9fc083a0 	0x9fc083a0
	...
 3d8:	0000001d 	0x1d
 3dc:	0000001f 	0x1f
 3e0:	9fc08410 	0x9fc08410
	...
 3f8:	0000001d 	0x1d
 3fc:	0000001f 	0x1f
 400:	9fc08430 	0x9fc08430
	...
 418:	0000001d 	0x1d
 41c:	0000001f 	0x1f
 420:	9fc084b0 	0x9fc084b0
	...
 438:	0000001d 	0x1d
 43c:	0000001f 	0x1f
 440:	9fc084e0 	0x9fc084e0
	...
 458:	0000001d 	0x1d
 45c:	0000001f 	0x1f
 460:	9fc08510 	0x9fc08510
	...
 478:	0000001d 	0x1d
 47c:	0000001f 	0x1f
 480:	9fc085c0 	0x9fc085c0
	...
 498:	0000001d 	0x1d
 49c:	0000001f 	0x1f
 4a0:	9fc08670 	0x9fc08670
 4a4:	c0ff0000 	lwc0	$31,0(a3)
 4a8:	fffffffc 	0xfffffffc
	...
 4b4:	00000038 	0x38
 4b8:	0000001d 	0x1d
 4bc:	0000001f 	0x1f
 4c0:	9fc08930 	0x9fc08930
 4c4:	800f0000 	lb	t7,0(zero)
 4c8:	fffffffc 	0xfffffffc
	...
 4d4:	00000030 	0x30
 4d8:	0000001d 	0x1d
 4dc:	0000001f 	0x1f
 4e0:	9fc08a90 	0x9fc08a90
 4e4:	c0ff0000 	lwc0	$31,0(a3)
 4e8:	fffffffc 	0xfffffffc
	...
 4f4:	00000038 	0x38
 4f8:	0000001d 	0x1d
 4fc:	0000001f 	0x1f
 500:	9fc09140 	0x9fc09140
 504:	803f0000 	lb	ra,0(at)
 508:	fffffffc 	0xfffffffc
	...
 514:	00000038 	0x38
 518:	0000001d 	0x1d
 51c:	0000001f 	0x1f
 520:	9fc09380 	0x9fc09380
 524:	c0ff0000 	lwc0	$31,0(a3)
 528:	fffffffc 	0xfffffffc
	...
 534:	00000040 	sll	zero,zero,0x1
 538:	0000001d 	0x1d
 53c:	0000001f 	0x1f
 540:	9fc09e10 	0x9fc09e10
 544:	807f0000 	lb	ra,0(v1)
 548:	fffffffc 	0xfffffffc
	...
 554:	00000038 	0x38
 558:	0000001d 	0x1d
 55c:	0000001f 	0x1f
 560:	9fc0a060 	0x9fc0a060
	...
 578:	0000001d 	0x1d
 57c:	0000001f 	0x1f
 580:	9fc0a074 	0x9fc0a074
 584:	80000000 	lb	zero,0(zero)
 588:	fffffffc 	0xfffffffc
	...
 594:	00000018 	mult	zero,zero
 598:	0000001d 	0x1d
 59c:	0000001f 	0x1f
 5a0:	9fc0a0a0 	0x9fc0a0a0
 5a4:	80070000 	lb	a3,0(zero)
 5a8:	fffffffc 	0xfffffffc
	...
 5b4:	00000020 	add	zero,zero,zero
 5b8:	0000001d 	0x1d
 5bc:	0000001f 	0x1f
 5c0:	9fc0a128 	0x9fc0a128
 5c4:	80000000 	lb	zero,0(zero)
 5c8:	fffffffc 	0xfffffffc
	...
 5d4:	00000018 	mult	zero,zero
 5d8:	0000001d 	0x1d
 5dc:	0000001f 	0x1f
 5e0:	9fc0a160 	0x9fc0a160
 5e4:	800f0000 	lb	t7,0(zero)
 5e8:	fffffffc 	0xfffffffc
	...
 5f4:	00000068 	0x68
 5f8:	0000001d 	0x1d
 5fc:	0000001f 	0x1f
 600:	9fc0a260 	0x9fc0a260
	...
 618:	0000001d 	0x1d
 61c:	0000001f 	0x1f
 620:	9fc0a270 	0x9fc0a270
	...
 638:	0000001d 	0x1d
 63c:	0000001f 	0x1f
 640:	9fc0a280 	0x9fc0a280
	...
 658:	0000001d 	0x1d
 65c:	0000001f 	0x1f
 660:	9fc0a28c 	0x9fc0a28c
	...
 678:	0000001d 	0x1d
 67c:	0000001f 	0x1f
 680:	9fc0a29c 	0x9fc0a29c
	...
 698:	0000001d 	0x1d
 69c:	0000001f 	0x1f
 6a0:	9fc0a2b8 	0x9fc0a2b8
	...
 6b8:	0000001d 	0x1d
 6bc:	0000001f 	0x1f
 6c0:	9fc0a2dc 	0x9fc0a2dc
 6c4:	80000000 	lb	zero,0(zero)
 6c8:	fffffffc 	0xfffffffc
	...
 6d4:	00000018 	mult	zero,zero
 6d8:	0000001d 	0x1d
 6dc:	0000001f 	0x1f

Disassembly of section .comment:

00000000 <.comment>:
   0:	43434700 	c0	0x1434700
   4:	4728203a 	c1	0x128203a
   8:	2029554e 	addi	t1,at,21838
   c:	2e332e34 	sltiu	s3,s1,11828
  10:	47000030 	c1	0x1000030
  14:	203a4343 	addi	k0,at,17219
  18:	554e4728 	0x554e4728
  1c:	2e342029 	sltiu	s4,s1,8233
  20:	00302e33 	0x302e33
  24:	43434700 	c0	0x1434700
  28:	4728203a 	c1	0x128203a
  2c:	2029554e 	addi	t1,at,21838
  30:	2e332e34 	sltiu	s3,s1,11828
  34:	47000030 	c1	0x1000030
  38:	203a4343 	addi	k0,at,17219
  3c:	554e4728 	0x554e4728
  40:	2e342029 	sltiu	s4,s1,8233
  44:	00302e33 	0x302e33
  48:	43434700 	c0	0x1434700
  4c:	4728203a 	c1	0x128203a
  50:	2029554e 	addi	t1,at,21838
  54:	2e332e34 	sltiu	s3,s1,11828
  58:	47000030 	c1	0x1000030
  5c:	203a4343 	addi	k0,at,17219
  60:	554e4728 	0x554e4728
  64:	2e342029 	sltiu	s4,s1,8233
  68:	00302e33 	0x302e33
  6c:	43434700 	c0	0x1434700
  70:	4728203a 	c1	0x128203a
  74:	2029554e 	addi	t1,at,21838
  78:	2e332e34 	sltiu	s3,s1,11828
  7c:	47000030 	c1	0x1000030
  80:	203a4343 	addi	k0,at,17219
  84:	554e4728 	0x554e4728
  88:	2e342029 	sltiu	s4,s1,8233
  8c:	00302e33 	0x302e33
  90:	43434700 	c0	0x1434700
  94:	4728203a 	c1	0x128203a
  98:	2029554e 	addi	t1,at,21838
  9c:	2e332e34 	sltiu	s3,s1,11828
  a0:	47000030 	c1	0x1000030
  a4:	203a4343 	addi	k0,at,17219
  a8:	554e4728 	0x554e4728
  ac:	2e342029 	sltiu	s4,s1,8233
  b0:	00302e33 	0x302e33
  b4:	43434700 	c0	0x1434700
  b8:	4728203a 	c1	0x128203a
  bc:	2029554e 	addi	t1,at,21838
  c0:	2e332e34 	sltiu	s3,s1,11828
  c4:	47000030 	c1	0x1000030
  c8:	203a4343 	addi	k0,at,17219
  cc:	554e4728 	0x554e4728
  d0:	2e342029 	sltiu	s4,s1,8233
  d4:	00302e33 	0x302e33

Disassembly of section .gnu.attributes:

00000000 <.gnu.attributes>:
   0:	00000f41 	0xf41
   4:	756e6700 	jalx	5b99c00 <data_size+0x5b9930c>
   8:	00070100 	sll	zero,a3,0x4
   c:	03040000 	0x3040000

Disassembly of section .debug_abbrev:

00000000 <.debug_abbrev>:
   0:	25011101 	addiu	at,t0,4353
   4:	030b130e 	0x30b130e
   8:	110e1b0e 	beq	t0,t6,6c44 <data_size+0x6350>
   c:	10011201 	beq	zero,at,4814 <data_size+0x3f20>
  10:	02000006 	srlv	zero,zero,s0
  14:	0b0b000f 	j	c2c003c <data_size+0xc2bf748>
  18:	24030000 	li	v1,0
  1c:	3e0b0b00 	0x3e0b0b00
  20:	000e030b 	0xe030b
  24:	012e0400 	0x12e0400
  28:	0e030c3f 	jal	80c30fc <data_size+0x80c2808>
  2c:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
  30:	13490c27 	beq	k0,t1,30d0 <data_size+0x27dc>
  34:	01120111 	0x1120111
  38:	40064081 	0x40064081
  3c:	00130106 	0x130106
  40:	00050500 	sll	zero,a1,0x14
  44:	0b3a0803 	j	ce8200c <data_size+0xce81718>
  48:	13490b3b 	beq	k0,t1,2d38 <data_size+0x2444>
  4c:	00000602 	srl	zero,zero,0x18
  50:	00001806 	srlv	v1,zero,zero
  54:	00340700 	0x340700
  58:	0b3a0803 	j	ce8200c <data_size+0xce81718>
  5c:	13490b3b 	beq	k0,t1,2d4c <data_size+0x2458>
  60:	00000602 	srl	zero,zero,0x18
  64:	03003408 	0x3003408
  68:	3b0b3a08 	xori	t3,t8,0x3a08
  6c:	0013490b 	0x13490b
  70:	00340900 	0x340900
  74:	0b3a0803 	j	ce8200c <data_size+0xce81718>
  78:	13490b3b 	beq	k0,t1,2d68 <data_size+0x2474>
  7c:	00000a02 	srl	at,zero,0x8
  80:	03000a0a 	0x3000a0a
  84:	3b0b3a0e 	xori	t3,t8,0x3a0e
  88:	0b00000b 	j	c00002c <data_size+0xbfff738>
  8c:	0b0b0024 	j	c2c0090 <data_size+0xc2bf79c>
  90:	08030b3e 	j	c2cf8 <data_size+0xc2404>
  94:	0f0c0000 	jal	c300000 <data_size+0xc2ff70c>
  98:	490b0b00 	0x490b0b00
  9c:	0d000013 	jal	400004c <data_size+0x3fff758>
  a0:	13490026 	beq	k0,t1,13c <data_size-0x7b8>
  a4:	01000000 	0x1000000
  a8:	0e250111 	jal	8940444 <data_size+0x893fb50>
  ac:	0e030b13 	jal	80c2c4c <data_size+0x80c2358>
  b0:	01110e1b 	0x1110e1b
  b4:	06100112 	bltzal	s0,500 <data_size-0x3f4>
  b8:	24020000 	li	v0,0
  bc:	3e0b0b00 	0x3e0b0b00
  c0:	000e030b 	0xe030b
  c4:	012e0300 	0x12e0300
  c8:	0e030c3f 	jal	80c30fc <data_size+0x80c2808>
  cc:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
  d0:	01120111 	0x1120111
  d4:	40064081 	0x40064081
  d8:	0013010a 	0x13010a
  dc:	00050400 	sll	zero,a1,0x10
  e0:	0b3a0803 	j	ce8200c <data_size+0xce81718>
  e4:	13490b3b 	beq	k0,t1,2dd4 <data_size+0x24e0>
  e8:	00000a02 	srl	at,zero,0x8
  ec:	0b002405 	j	c009014 <data_size+0xc008720>
  f0:	030b3e0b 	0x30b3e0b
  f4:	06000008 	bltz	s0,118 <data_size-0x7dc>
  f8:	0c3f012e 	jal	fc04b8 <data_size+0xfbfbc4>
  fc:	0b3a0e03 	j	ce8380c <data_size+0xce82f18>
 100:	0c270b3b 	jal	9c2cec <data_size+0x9c23f8>
 104:	01111349 	0x1111349
 108:	40810112 	0x40810112
 10c:	00064006 	srlv	t0,a2,zero
 110:	00050700 	sll	zero,a1,0x1c
 114:	0b3a0803 	j	ce8200c <data_size+0xce81718>
 118:	13490b3b 	beq	k0,t1,2e08 <data_size+0x2514>
 11c:	00000602 	srl	zero,zero,0x18
 120:	01110100 	0x1110100
 124:	0b130e25 	j	c4c3894 <data_size+0xc4c2fa0>
 128:	0e1b0e03 	jal	86c380c <data_size+0x86c2f18>
 12c:	01120111 	0x1120111
 130:	00000610 	0x610
 134:	0b002402 	j	c009008 <data_size+0xc008714>
 138:	030b3e0b 	0x30b3e0b
 13c:	0300000e 	0x300000e
 140:	0c3f012e 	jal	fc04b8 <data_size+0xfbfbc4>
 144:	0b3a0e03 	j	ce8380c <data_size+0xce82f18>
 148:	0c270b3b 	jal	9c2cec <data_size+0x9c23f8>
 14c:	01111349 	0x1111349
 150:	40810112 	0x40810112
 154:	01064006 	srlv	t0,a2,t0
 158:	04000013 	bltz	zero,1a8 <data_size-0x74c>
 15c:	08030005 	j	c0014 <data_size+0xbf720>
 160:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 164:	06021349 	0x6021349
 168:	34050000 	li	a1,0x0
 16c:	3a080300 	xori	t0,s0,0x300
 170:	490b3b0b 	0x490b3b0b
 174:	00060213 	0x60213
 178:	00240600 	0x240600
 17c:	0b3e0b0b 	j	cf82c2c <data_size+0xcf82338>
 180:	00000803 	sra	at,zero,0x0
 184:	0b000f07 	j	c003c1c <data_size+0xc003328>
 188:	0013490b 	0x13490b
 18c:	012e0800 	0x12e0800
 190:	0e030c3f 	jal	80c30fc <data_size+0x80c2808>
 194:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 198:	13490c27 	beq	k0,t1,3238 <data_size+0x2944>
 19c:	01120111 	0x1120111
 1a0:	40064081 	0x40064081
 1a4:	00000006 	srlv	zero,zero,zero
 1a8:	25011101 	addiu	at,t0,4353
 1ac:	030b130e 	0x30b130e
 1b0:	110e1b0e 	beq	t0,t6,6dec <data_size+0x64f8>
 1b4:	10011201 	beq	zero,at,49bc <data_size+0x40c8>
 1b8:	02000006 	srlv	zero,zero,s0
 1bc:	0b0b0024 	j	c2c0090 <data_size+0xc2bf79c>
 1c0:	0e030b3e 	jal	80c2cf8 <data_size+0x80c2404>
 1c4:	2e030000 	sltiu	v1,s0,0
 1c8:	030c3f01 	0x30c3f01
 1cc:	3b0b3a0e 	xori	t3,t8,0x3a0e
 1d0:	490c270b 	0x490c270b
 1d4:	12011113 	beq	s0,at,4624 <data_size+0x3d30>
 1d8:	06408101 	bltz	s2,fffe05e0 <_stack+0x603c5984>
 1dc:	13010640 	beq	t8,at,1ae0 <data_size+0x11ec>
 1e0:	05040000 	0x5040000
 1e4:	3a080300 	xori	t0,s0,0x300
 1e8:	490b3b0b 	0x490b3b0b
 1ec:	00060213 	0x60213
 1f0:	00050500 	sll	zero,a1,0x14
 1f4:	0b3a0e03 	j	ce8380c <data_size+0xce82f18>
 1f8:	13490b3b 	beq	k0,t1,2ee8 <data_size+0x25f4>
 1fc:	00000602 	srl	zero,zero,0x18
 200:	03003406 	0x3003406
 204:	3b0b3a08 	xori	t3,t8,0x3a08
 208:	0213490b 	0x213490b
 20c:	07000006 	bltz	t8,228 <data_size-0x6cc>
 210:	08030034 	j	c00d0 <data_size+0xbf7dc>
 214:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 218:	0a021349 	j	8084d24 <data_size+0x8084430>
 21c:	34080000 	li	t0,0x0
 220:	3a0e0300 	xori	t6,s0,0x300
 224:	490b3b0b 	0x490b3b0b
 228:	00060213 	0x60213
 22c:	00240900 	0x240900
 230:	0b3e0b0b 	j	cf82c2c <data_size+0xcf82338>
 234:	00000803 	sra	at,zero,0x0
 238:	4901010a 	bc2t	664 <data_size-0x290>
 23c:	00130113 	0x130113
 240:	00210b00 	0x210b00
 244:	0b2f1349 	j	cbc4d24 <data_size+0xcbc4430>
 248:	240c0000 	li	t4,0
 24c:	3e0b0b00 	0x3e0b0b00
 250:	0000000b 	0xb
 254:	25011101 	addiu	at,t0,4353
 258:	030b130e 	0x30b130e
 25c:	110e1b0e 	beq	t0,t6,6e98 <data_size+0x65a4>
 260:	10011201 	beq	zero,at,4a68 <data_size+0x4174>
 264:	02000006 	srlv	zero,zero,s0
 268:	0b0b0024 	j	c2c0090 <data_size+0xc2bf79c>
 26c:	0e030b3e 	jal	80c2cf8 <data_size+0x80c2404>
 270:	16030000 	bne	s0,v1,274 <data_size-0x680>
 274:	3a0e0300 	xori	t6,s0,0x300
 278:	490b3b0b 	0x490b3b0b
 27c:	04000013 	bltz	zero,2cc <data_size-0x628>
 280:	0b0b0024 	j	c2c0090 <data_size+0xc2bf79c>
 284:	08030b3e 	j	c2cf8 <data_size+0xc2404>
 288:	13050000 	beq	t8,a1,28c <data_size-0x668>
 28c:	0b0e0301 	j	c380c04 <data_size+0xc380310>
 290:	3b0b3a0b 	xori	t3,t8,0x3a0b
 294:	0013010b 	0x13010b
 298:	000d0600 	sll	zero,t5,0x18
 29c:	0b3a0e03 	j	ce8380c <data_size+0xce82f18>
 2a0:	13490b3b 	beq	k0,t1,2f90 <data_size+0x269c>
 2a4:	00000a38 	0xa38
 2a8:	3f012e07 	0x3f012e07
 2ac:	3a0e030c 	xori	t6,s0,0x30c
 2b0:	490b3b0b 	0x490b3b0b
 2b4:	010b2013 	0x10b2013
 2b8:	08000013 	j	4c <data_size-0x8a8>
 2bc:	0e030034 	jal	80c00d0 <data_size+0x80bf7dc>
 2c0:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 2c4:	00001349 	0x1349
 2c8:	31012e09 	andi	at,t0,0x2e09
 2cc:	12011113 	beq	s0,at,471c <data_size+0x3e28>
 2d0:	06408101 	bltz	s2,fffe06d8 <_stack+0x603c5a7c>
 2d4:	13010a40 	beq	t8,at,2bd8 <data_size+0x22e4>
 2d8:	340a0000 	li	t2,0x0
 2dc:	00133100 	sll	a2,s3,0x4
 2e0:	002e0b00 	0x2e0b00
 2e4:	0e030c3f 	jal	80c30fc <data_size+0x80c2808>
 2e8:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 2ec:	01111349 	0x1111349
 2f0:	40810112 	0x40810112
 2f4:	000a4006 	srlv	t0,t2,zero
 2f8:	012e0c00 	0x12e0c00
 2fc:	0e030c3f 	jal	80c30fc <data_size+0x80c2808>
 300:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 304:	01111349 	0x1111349
 308:	40810112 	0x40810112
 30c:	010a4006 	srlv	t0,t2,t0
 310:	0d000013 	jal	400004c <data_size+0x3fff758>
 314:	08030034 	j	c00d0 <data_size+0xbf7dc>
 318:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 31c:	06021349 	0x6021349
 320:	340e0000 	li	t6,0x0
 324:	3a080300 	xori	t0,s0,0x300
 328:	490b3b0b 	0x490b3b0b
 32c:	0f000013 	jal	c00004c <data_size+0xbfff758>
 330:	0c3f012e 	jal	fc04b8 <data_size+0xfbfbc4>
 334:	0b3a0e03 	j	ce8380c <data_size+0xce82f18>
 338:	0c270b3b 	jal	9c2cec <data_size+0x9c23f8>
 33c:	01111349 	0x1111349
 340:	40810112 	0x40810112
 344:	010a4006 	srlv	t0,t2,t0
 348:	10000013 	b	398 <data_size-0x55c>
 34c:	1331011d 	beq	t9,s1,7c4 <data_size-0x130>
 350:	01120111 	0x1120111
 354:	0b590b58 	j	d642d60 <data_size+0xd64246c>
 358:	0b110000 	j	c440000 <data_size+0xc43f70c>
 35c:	12011101 	beq	s0,at,4764 <data_size+0x3e70>
 360:	12000001 	beqz	s0,368 <data_size-0x58c>
 364:	08030034 	j	c00d0 <data_size+0xbf7dc>
 368:	0b3b0b3a 	j	cec2ce8 <data_size+0xcec23f4>
 36c:	0a021349 	j	8084d24 <data_size+0x8084430>
 370:	2e130000 	sltiu	s3,s0,0
 374:	030c3f01 	0x30c3f01
 378:	3b0b3a0e 	xori	t3,t8,0x3a0e
 37c:	490c270b 	0x490c270b
 380:	12011113 	beq	s0,at,47d0 <data_size+0x3edc>
 384:	06408101 	bltz	s2,fffe078c <_stack+0x603c5b30>
 388:	13010640 	beq	t8,at,1c8c <data_size+0x1398>
 38c:	05140000 	0x5140000
 390:	3a080300 	xori	t0,s0,0x300
 394:	490b3b0b 	0x490b3b0b
 398:	00060213 	0x60213
 39c:	000f1500 	sll	v0,t7,0x14
 3a0:	13490b0b 	beq	k0,t1,2fd0 <data_size+0x26dc>
 3a4:	Address 0x00000000000003a4 is out of bounds.


Disassembly of section .debug_info:

00000000 <.debug_info>:
   0:	000000c9 	0xc9
   4:	00000002 	srl	zero,zero,0x0
   8:	01040000 	0x1040000
   c:	00000018 	mult	zero,zero
  10:	00007a01 	0x7a01
  14:	00002700 	sll	a0,zero,0x1c
  18:	c09e1000 	lwc0	$30,4096(a0)
  1c:	c0a0609f 	lwc0	$0,24735(a1)
  20:	0000009f 	0x9f
  24:	03040200 	0x3040200
  28:	000b0704 	0xb0704
  2c:	04030000 	0x4030000
  30:	00000607 	0x607
  34:	83010400 	lb	at,1024(t8)
  38:	01000000 	0x1000000
  3c:	00ad0102 	0xad0102
  40:	9e100000 	0x9e100000
  44:	a0609fc0 	sb	zero,-24640(v1)
  48:	00109fc0 	sll	s3,s0,0x1f
  4c:	00000000 	nop
  50:	00ad0000 	0xad0000
  54:	66050000 	0x66050000
  58:	0100746d 	0x100746d
  5c:	0000b401 	0xb401
  60:	00001f00 	sll	v1,zero,0x1c
  64:	69070600 	0x69070600
  68:	ad030100 	sw	v1,256(t0)
  6c:	48000000 	mfc2	zero,$0
  70:	08000000 	j	0 <data_size-0x8f4>
  74:	04010063 	b	204 <data_size-0x6f0>
  78:	000000bf 	0xbf
  7c:	67726107 	0x67726107
  80:	c6050100 	lwc1	$f5,256(s0)
  84:	66000000 	0x66000000
  88:	09000000 	j	4000000 <data_size+0x3fff70c>
  8c:	01007061 	0x1007061
  90:	00002506 	0x2506
  94:	108d0200 	beq	a0,t5,898 <data_size-0x5c>
  98:	01007707 	0x1007707
  9c:	0000ad07 	0xad07
  a0:	00008400 	sll	s0,zero,0x10
  a4:	00000a00 	sll	at,zero,0x8
  a8:	45010000 	bc1t	ac <data_size-0x848>
  ac:	05040b00 	0x5040b00
  b0:	00746e69 	0x746e69
  b4:	00ba040c 	syscall	0x2e810
  b8:	bf0d0000 	0xbf0d0000
  bc:	03000000 	0x3000000
  c0:	00910601 	0x910601
  c4:	040c0000 	0x40c0000
  c8:	00000025 	move	zero,zero
  cc:	00008800 	sll	s1,zero,0x0
  d0:	a7000200 	sh	zero,512(t8)
  d4:	04000000 	bltz	zero,d8 <data_size-0x81c>
  d8:	00001801 	0x1801
  dc:	00960100 	0x960100
  e0:	00270000 	0x270000
  e4:	a0600000 	sb	zero,0(v1)
  e8:	a0949fc0 	sb	s4,-24640(a0)
  ec:	00ac9fc0 	0xac9fc0
  f0:	04020000 	0x4020000
  f4:	00000b07 	0xb07
  f8:	07040200 	0x7040200
  fc:	00000006 	srlv	zero,zero,zero
 100:	008a0103 	0x8a0103
 104:	08010000 	j	40000 <data_size+0x3f70c>
 108:	9fc0a060 	0x9fc0a060
 10c:	9fc0a074 	0x9fc0a074
 110:	00000044 	0x44
 114:	00596d01 	0x596d01
 118:	63040000 	0x63040000
 11c:	59080100 	0x59080100
 120:	01000000 	0x1000000
 124:	04050054 	0x4050054
 128:	746e6905 	jalx	1b9a414 <data_size+0x1b99b20>
 12c:	8e010600 	lw	at,1536(s0)
 130:	01000000 	0x1000000
 134:	00590102 	0x590102
 138:	a0740000 	sb	s4,0(v1)
 13c:	a0949fc0 	sb	s4,-24640(a0)
 140:	00549fc0 	0x549fc0
 144:	01100000 	0x1100000
 148:	63070000 	0x63070000
 14c:	59010100 	0x59010100
 150:	2f000000 	sltiu	zero,t8,0
 154:	00000001 	0x1
 158:	0000ab00 	sll	s5,zero,0xc
 15c:	21000200 	addi	zero,t0,512
 160:	04000001 	bltz	zero,168 <data_size-0x78c>
 164:	00001801 	0x1801
 168:	00a00100 	0xa00100
 16c:	00270000 	0x270000
 170:	a0a00000 	sb	zero,0(a1)
 174:	a1589fc0 	sb	t8,-24640(t2)
 178:	00ec9fc0 	0xec9fc0
 17c:	04020000 	0x4020000
 180:	00000b07 	0xb07
 184:	07040200 	0x7040200
 188:	00000006 	srlv	zero,zero,zero
 18c:	00ac0103 	0xac0103
 190:	02010000 	0x2010000
 194:	00006f01 	0x6f01
 198:	c0a0a000 	lwc0	$0,-24576(a1)
 19c:	c0a1289f 	lwc0	$1,10399(a1)
 1a0:	00007c9f 	0x7c9f
 1a4:	00014200 	sll	t0,at,0x8
 1a8:	00006f00 	sll	t5,zero,0x1c
 1ac:	00730400 	0x730400
 1b0:	00760101 	0x760101
 1b4:	01610000 	0x1610000
 1b8:	63050000 	0x63050000
 1bc:	7c030100 	0x7c030100
 1c0:	8a000000 	lwl	zero,0(s0)
 1c4:	00000001 	0x1
 1c8:	69050406 	0x69050406
 1cc:	0700746e 	bltz	t8,1d388 <data_size+0x1ca94>
 1d0:	00007c04 	0x7c04
 1d4:	06010200 	bgez	s0,9d8 <data_size+0xe4>
 1d8:	00000091 	0x91
 1dc:	00a70108 	0xa70108
 1e0:	0f010000 	jal	c040000 <data_size+0xc03f70c>
 1e4:	00006f01 	0x6f01
 1e8:	c0a12800 	lwc0	$1,10240(a1)
 1ec:	c0a1589f 	lwc0	$1,22687(a1)
 1f0:	0000989f 	0x989f
 1f4:	00019d00 	sll	s3,at,0x14
 1f8:	00730400 	0x730400
 1fc:	00760e01 	0x760e01
 200:	01bc0000 	0x1bc0000
 204:	00000000 	nop
 208:	000000f7 	0xf7
 20c:	01a80002 	0x1a80002
 210:	01040000 	0x1040000
 214:	00000018 	mult	zero,zero
 218:	0000bc01 	0xbc01
 21c:	00002700 	sll	a0,zero,0x1c
 220:	c0a16000 	lwc0	$1,24576(a1)
 224:	c0a2609f 	lwc0	$2,24735(a1)
 228:	00012f9f 	0x12f9f
 22c:	07040200 	0x7040200
 230:	0000000b 	0xb
 234:	06070402 	0x6070402
 238:	03000000 	0x3000000
 23c:	0000d601 	0xd601
 240:	01020100 	0x1020100
 244:	000000d2 	0xd2
 248:	9fc0a160 	0x9fc0a160
 24c:	9fc0a260 	0x9fc0a260
 250:	000000c0 	sll	zero,zero,0x3
 254:	000001cf 	0x1cf
 258:	000000d2 	0xd2
 25c:	01007604 	0x1007604
 260:	0000d901 	0xd901
 264:	0001ef00 	sll	sp,at,0x1c
 268:	00770400 	0x770400
 26c:	00d20101 	0xd20101
 270:	024f0000 	0x24f0000
 274:	db050000 	0xdb050000
 278:	01000000 	0x1000000
 27c:	0000d201 	0xd201
 280:	0002ba00 	sll	s7,v0,0x8
 284:	00d10500 	0xd10500
 288:	01010000 	0x1010000
 28c:	000000d2 	0xd2
 290:	0000030f 	0x30f
 294:	01006906 	0x1006906
 298:	0000d203 	sra	k0,zero,0x8
 29c:	00033800 	sll	a3,v1,0x0
 2a0:	006a0600 	0x6a0600
 2a4:	00d20301 	0xd20301
 2a8:	03610000 	0x3610000
 2ac:	63060000 	0x63060000
 2b0:	d2040100 	0xd2040100
 2b4:	8a000000 	lwl	zero,0(s0)
 2b8:	07000003 	bltz	t8,2c8 <data_size-0x62c>
 2bc:	00667562 	0x667562
 2c0:	00e00501 	0xe00501
 2c4:	91030000 	lbu	v1,0(t0)
 2c8:	b6087fa8 	0xb6087fa8
 2cc:	01000000 	0x1000000
 2d0:	00002c06 	0x2c06
 2d4:	0003a800 	sll	s5,v1,0x0
 2d8:	04090000 	0x4090000
 2dc:	746e6905 	jalx	1b9a414 <data_size+0x1b99b20>
 2e0:	05040200 	0x5040200
 2e4:	000000c8 	0xc8
 2e8:	0000f30a 	0xf30a
 2ec:	0000f000 	sll	s8,zero,0x0
 2f0:	00f00b00 	0xf00b00
 2f4:	003f0000 	0x3f0000
 2f8:	0207040c 	syscall	0x81c10
 2fc:	00910601 	0x910601
 300:	37000000 	ori	zero,t8,0x0
 304:	02000002 	0x2000002
 308:	00025400 	sll	t2,v0,0x10
 30c:	18010400 	0x18010400
 310:	01000000 	0x1000000
 314:	00000115 	0x115
 318:	00000027 	nor	zero,zero,zero
 31c:	9fc0a260 	0x9fc0a260
 320:	9fc0a3a8 	0x9fc0a3a8
 324:	00000186 	0x186
 328:	0b070402 	j	c1c1008 <data_size+0xc1c0714>
 32c:	02000000 	0x2000000
 330:	00060704 	0x60704
 334:	fd030000 	0xfd030000
 338:	02000000 	0x2000000
 33c:	00002c03 	sra	a1,zero,0x10
 340:	05040400 	0x5040400
 344:	00746e69 	0x746e69
 348:	0000f405 	0xf405
 34c:	1f021000 	0x1f021000
 350:	0000008a 	0x8a
 354:	00014206 	0x14206
 358:	33200200 	andi	zero,t9,0x200
 35c:	02000000 	0x2000000
 360:	49060010 	0x49060010
 364:	02000001 	0x2000001
 368:	00003321 	0x3321
 36c:	04100200 	bltzal	zero,b70 <data_size+0x27c>
 370:	00013106 	0x13106
 374:	33220200 	andi	v0,t9,0x200
 378:	02000000 	0x2000000
 37c:	06060810 	0x6060810
 380:	02000001 	0x2000001
 384:	00003323 	0x3323
 388:	0c100200 	jal	400800 <data_size+0x3fff0c>
 38c:	1c010700 	0x1c010700
 390:	01000001 	0x1000001
 394:	00002c04 	0x2c04
 398:	00a70000 	0xa70000
 39c:	39080000 	xori	t0,t0,0x0
 3a0:	01000001 	0x1000001
 3a4:	00002c05 	0x2c05
 3a8:	8a090000 	lwl	t1,0(s0)
 3ac:	60000000 	0x60000000
 3b0:	709fc0a2 	0x709fc0a2
 3b4:	f09fc0a2 	0xf09fc0a2
 3b8:	01000000 	0x1000000
 3bc:	0000c46d 	0xc46d
 3c0:	009b0a00 	0x9b0a00
 3c4:	0b000000 	j	c000000 <data_size+0xbfff70c>
 3c8:	00011d01 	0x11d01
 3cc:	2c110100 	sltiu	s1,zero,256
 3d0:	70000000 	0x70000000
 3d4:	809fc0a2 	lb	ra,-16222(a0)
 3d8:	009fc0a2 	0x9fc0a2
 3dc:	01000001 	0x1000001
 3e0:	e7010c6d 	swc1	$f1,3181(t8)
 3e4:	01000000 	0x1000000
 3e8:	00002c16 	0x2c16
 3ec:	c0a28000 	lwc0	$2,-32768(a1)
 3f0:	c0a28c9f 	lwc0	$2,-29537(a1)
 3f4:	0001109f 	0x1109f
 3f8:	0a6d0100 	j	9b40400 <data_size+0x9b3fb0c>
 3fc:	0d000001 	jal	4000004 <data_size+0x3fff710>
 400:	1701006e 	bne	t8,at,5bc <data_size-0x338>
 404:	0000002c 	0x2c
 408:	000003d1 	0x3d1
 40c:	27010c00 	addiu	at,t8,3072
 410:	01000001 	0x1000001
 414:	00002c2c 	0x2c2c
 418:	c0a28c00 	lwc0	$2,-29696(a1)
 41c:	c0a29c9f 	lwc0	$2,-25441(a1)
 420:	0001209f 	0x1209f
 424:	326d0100 	andi	t5,s3,0x100
 428:	0e000001 	jal	8000004 <data_size+0x7fff710>
 42c:	2d01006e 	sltiu	at,t0,110
 430:	0000002c 	0x2c
 434:	0e010f00 	jal	8043c00 <data_size+0x804330c>
 438:	01000001 	0x1000001
 43c:	002c0133 	0x2c0133
 440:	a29c0000 	sb	gp,0(s4)
 444:	a2b89fc0 	sb	t8,-24640(s5)
 448:	01309fc0 	0x1309fc0
 44c:	6d010000 	0x6d010000
 450:	0000017e 	0x17e
 454:	01006e0d 	break	0x100,0x1b8
 458:	00002c34 	0x2c34
 45c:	0003e400 	sll	gp,v1,0x10
 460:	008a1000 	0x8a1000
 464:	a29c0000 	sb	gp,0(s4)
 468:	a2b09fc0 	sb	s0,-24640(s5)
 46c:	35019fc0 	ori	at,t0,0x9fc0
 470:	c0a29c11 	lwc0	$2,-25583(a1)
 474:	c0a2b09f 	lwc0	$2,-20321(a1)
 478:	009b0a9f 	0x9b0a9f
 47c:	00000000 	nop
 480:	e0010f00 	swc0	$1,3840(zero)
 484:	01000000 	0x1000000
 488:	002c013c 	0x2c013c
 48c:	a2b80000 	sb	t8,0(s5)
 490:	a2dc9fc0 	sb	gp,-24640(s6)
 494:	01409fc0 	0x1409fc0
 498:	6d010000 	0x6d010000
 49c:	000001c8 	0x1c8
 4a0:	01006e12 	0x1006e12
 4a4:	00002c3d 	0x2c3d
 4a8:	10530100 	beq	v0,s3,8ac <data_size-0x48>
 4ac:	0000008a 	0x8a
 4b0:	9fc0a2b8 	0x9fc0a2b8
 4b4:	9fc0a2c4 	0x9fc0a2c4
 4b8:	b8113e01 	swr	s1,15873(zero)
 4bc:	c49fc0a2 	lwc1	$f31,-16222(a0)
 4c0:	0a9fc0a2 	j	a7f0288 <data_size+0xa7ef994>
 4c4:	0000009b 	0x9b
 4c8:	13000000 	beqz	t8,4cc <data_size-0x428>
 4cc:	00015101 	0x15101
 4d0:	01200100 	0x1200100
 4d4:	0000002c 	0x2c
 4d8:	9fc0a2dc 	0x9fc0a2dc
 4dc:	9fc0a3a8 	0x9fc0a3a8
 4e0:	00000150 	0x150
 4e4:	000003f7 	0x3f7
 4e8:	00000234 	0x234
 4ec:	6c657314 	0x6c657314
 4f0:	3e1f0100 	0x3e1f0100
 4f4:	16000000 	bnez	s0,4f8 <data_size-0x3fc>
 4f8:	14000004 	bnez	zero,50c <data_size-0x3e8>
 4fc:	00706d74 	0x706d74
 500:	02341f01 	0x2341f01
 504:	04290000 	0x4290000
 508:	6e0d0000 	0x6e0d0000
 50c:	2c210100 	sltiu	at,at,256
 510:	47000000 	c1	0x1000000
 514:	10000004 	b	528 <data_size-0x3cc>
 518:	0000008a 	0x8a
 51c:	9fc0a2e8 	0x9fc0a2e8
 520:	9fc0a2f0 	0x9fc0a2f0
 524:	e8112201 	swc2	$17,8705(zero)
 528:	f09fc0a2 	0xf09fc0a2
 52c:	0a9fc0a2 	j	a7f0288 <data_size+0xa7ef994>
 530:	0000009b 	0x9b
 534:	15000000 	bnez	t0,538 <data_size-0x3bc>
 538:	00004504 	0x4504
	...

Disassembly of section .debug_line:

00000000 <.debug_line>:
   0:	000000a8 	0xa8
   4:	001f0002 	srl	zero,ra,0x0
   8:	01010000 	0x1010000
   c:	000d0efb 	0xd0efb
  10:	01010101 	0x1010101
  14:	01000000 	0x1000000
  18:	00010000 	sll	zero,at,0x0
  1c:	6e697270 	0x6e697270
  20:	632e6674 	0x632e6674
  24:	00000000 	nop
  28:	02050000 	0x2050000
  2c:	9fc09e10 	0x9fc09e10
  30:	1a240213 	0x1a240213
  34:	504a7803 	0x504a7803
  38:	c24a7a03 	lwc0	$10,31235(s2)
  3c:	4a780389 	c2	0x780389
  40:	4a780352 	c2	0x780352
  44:	00c2034e 	0xc2034e
  48:	7fbe034a 	0x7fbe034a
  4c:	00c2034a 	0xc2034a
  50:	ba038382 	swr	v1,-31870(s0)
  54:	ca03827f 	lwc2	$3,-32129(s0)
  58:	03740800 	0x3740800
  5c:	2c027fb9 	sltiu	v0,zero,32697
  60:	38038601 	xori	v1,zero,0x8601
  64:	41033c08 	0x41033c08
  68:	30038982 	andi	v1,zero,0x8982
  6c:	03f43c08 	0x3f43c08
  70:	033c084e 	0x33c084e
  74:	4f03823e 	c3	0x103823e
  78:	034bf3f2 	0x34bf3f2
  7c:	03bb8274 	0x3bb8274
  80:	4bf38223 	c2	0x1f38223
  84:	f3827303 	0xf3827303
  88:	8279034b 	lb	t9,843(s3)
  8c:	79034bf3 	0x79034bf3
  90:	034bf382 	0x34bf382
  94:	03bb8274 	0x3bb8274
  98:	4bf38218 	c2	0x1f38218
  9c:	83820903 	lb	v0,2307(gp)
  a0:	4bf14b87 	c2	0x1f14b87
  a4:	02750849 	0x2750849
  a8:	01010008 	0x1010008
  ac:	0000003c 	0x3c
  b0:	00200002 	0x200002
  b4:	01010000 	0x1010000
  b8:	000d0efb 	0xd0efb
  bc:	01010101 	0x1010101
  c0:	01000000 	0x1000000
  c4:	00010000 	sll	zero,at,0x0
  c8:	63747570 	0x63747570
  cc:	2e726168 	sltiu	s2,s3,24936
  d0:	00000063 	0x63
  d4:	05000000 	bltz	t0,d8 <data_size-0x81c>
  d8:	c0a06002 	lwc0	$0,24578(a1)
  dc:	0313199f 	0x313199f
  e0:	7003ba09 	0x7003ba09
  e4:	02848382 	0x2848382
  e8:	01010010 	0x1010010
  ec:	0000003f 	0x3f
  f0:	001d0002 	srl	zero,sp,0x0
  f4:	01010000 	0x1010000
  f8:	000d0efb 	0xd0efb
  fc:	01010101 	0x1010101
 100:	01000000 	0x1000000
 104:	00010000 	sll	zero,at,0x0
 108:	73747570 	0x73747570
 10c:	0000632e 	0x632e
 110:	00000000 	nop
 114:	a0a00205 	sb	zero,517(a1)
 118:	08139fc0 	j	4e7f00 <data_size+0x4e760c>
 11c:	7f83f43e 	0x7f83f43e
 120:	f97ff3f4 	0xf97ff3f4
 124:	8383b008 	lb	v1,-20472(gp)
 128:	10028483 	beq	zero,v0,fffe1338 <_stack+0x603c66dc>
 12c:	53010100 	0x53010100
 130:	02000000 	0x2000000
 134:	00002200 	sll	a0,zero,0x8
 138:	fb010100 	0xfb010100
 13c:	01000d0e 	0x1000d0e
 140:	00010101 	0x10101
 144:	00010000 	sll	zero,at,0x0
 148:	70000100 	0x70000100
 14c:	746e6972 	jalx	1b9a5c8 <data_size+0x1b99cd4>
 150:	65736162 	0x65736162
 154:	0000632e 	0x632e
 158:	00000000 	nop
 15c:	a1600205 	sb	zero,517(t3)
 160:	08139fc0 	j	4e7f00 <data_size+0x4e760c>
 164:	4cf78774 	0x4cf78774
 168:	b84cf0bc 	swr	t4,-3908(v0)
 16c:	086c038a 	j	1b00e28 <data_size+0x1b00534>
 170:	82160374 	lb	s6,884(s0)
 174:	0888b7f3 	j	222dfcc <data_size+0x222d6d8>
 178:	710383e0 	0x710383e0
 17c:	0f033c08 	jal	c0cf020 <data_size+0xc0ce72c>
 180:	000802f2 	0x802f2
 184:	007c0101 	0x7c0101
 188:	00020000 	sll	zero,v0,0x0
 18c:	00000032 	0x32
 190:	0efb0101 	jal	bec0404 <data_size+0xbebfb10>
 194:	0101000d 	break	0x101
 198:	00000101 	0x101
 19c:	00000100 	sll	zero,zero,0x4
 1a0:	2f2e2e01 	sltiu	t6,t9,11777
 1a4:	6c636e69 	0x6c636e69
 1a8:	00656475 	0x656475
 1ac:	6d697400 	0x6d697400
 1b0:	00632e65 	0x632e65
 1b4:	74000000 	jalx	0 <data_size-0x8f4>
 1b8:	2e656d69 	sltiu	a1,s3,28009
 1bc:	00010068 	0x10068
 1c0:	05000000 	bltz	t0,1c4 <data_size-0x730>
 1c4:	c0a26002 	lwc0	$2,24578(a1)
 1c8:	0a03159f 	j	80c567c <data_size+0x80c4d88>
 1cc:	f514f501 	0xf514f501
 1d0:	0f034f14 	jal	c0d3c50 <data_size+0xc0d335c>
 1d4:	03f51682 	0x3f51682
 1d8:	32030153 	andi	v1,s0,0x153
 1dc:	03863c08 	0x3863c08
 1e0:	3b03014a 	xori	v1,t8,0x14a
 1e4:	085f03ba 	j	17c0ee8 <data_size+0x17c05f4>
 1e8:	66038274 	0x66038274
 1ec:	821f034a 	lb	ra,842(s0)
 1f0:	7f4c3b08 	0x7f4c3b08
 1f4:	088180f6 	j	20603d8 <data_size+0x205fae4>
 1f8:	083b083e 	j	ec20f8 <data_size+0xec1804>
 1fc:	f53d083a 	0xf53d083a
 200:	00100284 	0x100284
 204:	Address 0x0000000000000204 is out of bounds.


Disassembly of section .debug_frame:

00000000 <.debug_frame>:
   0:	0000000c 	syscall
   4:	ffffffff 	0xffffffff
   8:	7c010001 	0x7c010001
   c:	001d0c1f 	0x1d0c1f
  10:	00000020 	add	zero,zero,zero
  14:	00000000 	nop
  18:	9fc09e10 	0x9fc09e10
  1c:	00000250 	0x250
  20:	60380e44 	0x60380e44
  24:	07910890 	bgezal	gp,2268 <data_size+0x1974>
  28:	04940692 	0x4940692
  2c:	02960395 	0x2960395
  30:	0593019f 	0x593019f
  34:	0000000c 	syscall
  38:	ffffffff 	0xffffffff
  3c:	7c010001 	0x7c010001
  40:	001d0c1f 	0x1d0c1f
  44:	0000000c 	syscall
  48:	00000034 	0x34
  4c:	9fc0a060 	0x9fc0a060
  50:	00000014 	0x14
  54:	00000014 	0x14
  58:	00000034 	0x34
  5c:	9fc0a074 	0x9fc0a074
  60:	00000020 	add	zero,zero,zero
  64:	44180e44 	0x44180e44
  68:	0000019f 	0x19f
  6c:	0000000c 	syscall
  70:	ffffffff 	0xffffffff
  74:	7c010001 	0x7c010001
  78:	001d0c1f 	0x1d0c1f
  7c:	00000018 	mult	zero,zero
  80:	0000006c 	0x6c
  84:	9fc0a0a0 	0x9fc0a0a0
  88:	00000088 	0x88
  8c:	50200e44 	0x50200e44
  90:	02920490 	0x2920490
  94:	0391019f 	0x391019f
  98:	00000014 	0x14
  9c:	0000006c 	0x6c
  a0:	9fc0a128 	0x9fc0a128
  a4:	00000030 	0x30
  a8:	44180e44 	0x44180e44
  ac:	0000019f 	0x19f
  b0:	0000000c 	syscall
  b4:	ffffffff 	0xffffffff
  b8:	7c010001 	0x7c010001
  bc:	001d0c1f 	0x1d0c1f
  c0:	0000001c 	0x1c
  c4:	000000b0 	0xb0
  c8:	9fc0a160 	0x9fc0a160
  cc:	00000100 	sll	zero,zero,0x4
  d0:	54680e44 	0x54680e44
  d4:	04910590 	bgezal	a0,1718 <data_size+0xe24>
  d8:	0392019f 	0x392019f
  dc:	00000293 	0x293
  e0:	0000000c 	syscall
  e4:	ffffffff 	0xffffffff
  e8:	7c010001 	0x7c010001
  ec:	001d0c1f 	0x1d0c1f
  f0:	0000000c 	syscall
  f4:	000000e0 	0xe0
  f8:	9fc0a260 	0x9fc0a260
  fc:	00000010 	mfhi	zero
 100:	0000000c 	syscall
 104:	000000e0 	0xe0
 108:	9fc0a270 	0x9fc0a270
 10c:	00000010 	mfhi	zero
 110:	0000000c 	syscall
 114:	000000e0 	0xe0
 118:	9fc0a280 	0x9fc0a280
 11c:	0000000c 	syscall
 120:	0000000c 	syscall
 124:	000000e0 	0xe0
 128:	9fc0a28c 	0x9fc0a28c
 12c:	00000010 	mfhi	zero
 130:	0000000c 	syscall
 134:	000000e0 	0xe0
 138:	9fc0a29c 	0x9fc0a29c
 13c:	0000001c 	0x1c
 140:	0000000c 	syscall
 144:	000000e0 	0xe0
 148:	9fc0a2b8 	0x9fc0a2b8
 14c:	00000024 	and	zero,zero,zero
 150:	00000014 	0x14
 154:	000000e0 	0xe0
 158:	9fc0a2dc 	0x9fc0a2dc
 15c:	000000cc 	syscall	0x3
 160:	44180e44 	0x44180e44
 164:	0000019f 	0x19f

Disassembly of section .debug_loc:

00000000 <.debug_loc>:
   0:	00000000 	nop
   4:	00000004 	sllv	zero,zero,zero
   8:	046d0001 	0x46d0001
   c:	50000000 	0x50000000
  10:	02000002 	0x2000002
  14:	00388d00 	0x388d00
	...
  20:	30000000 	andi	zero,zero,0x0
  24:	01000000 	0x1000000
  28:	00305400 	0x305400
  2c:	00a40000 	0xa40000
  30:	00010000 	sll	zero,at,0x0
  34:	0000b863 	0xb863
  38:	00025000 	sll	t2,v0,0x0
  3c:	63000100 	0x63000100
	...
  48:	00000054 	0x54
  4c:	000000ac 	0xac
  50:	b8610001 	swr	at,1(v1)
  54:	50000000 	0x50000000
  58:	01000002 	0x1000002
  5c:	00006100 	sll	t4,zero,0x4
  60:	00000000 	nop
  64:	004c0000 	0x4c0000
  68:	00a80000 	0xa80000
  6c:	00010000 	sll	zero,at,0x0
  70:	0000b862 	0xb862
  74:	00025000 	sll	t2,v0,0x0
  78:	62000100 	0x62000100
	...
  84:	000000c0 	sll	zero,zero,0x3
  88:	000000dc 	0xdc
  8c:	e4550001 	swc1	$f21,1(v0)
  90:	24000000 	li	zero,0
  94:	01000001 	0x1000001
  98:	01345500 	0x1345500
  9c:	01440000 	0x1440000
  a0:	00010000 	sll	zero,at,0x0
  a4:	00015055 	0x15055
  a8:	00015c00 	sll	t3,at,0x10
  ac:	55000100 	0x55000100
  b0:	00000164 	0x164
  b4:	00000174 	0x174
  b8:	80550001 	lb	s5,1(v0)
  bc:	90000001 	lbu	zero,1(zero)
  c0:	01000001 	0x1000001
  c4:	019c5500 	0x19c5500
  c8:	01ac0000 	0x1ac0000
  cc:	00010000 	sll	zero,at,0x0
  d0:	0001b855 	0x1b855
  d4:	0001c800 	sll	t9,at,0x0
  d8:	55000100 	0x55000100
  dc:	000001d4 	0x1d4
  e0:	000001e0 	0x1e0
  e4:	e8550001 	swc2	$21,1(v0)
  e8:	f8000001 	0xf8000001
  ec:	01000001 	0x1000001
  f0:	02045500 	0x2045500
  f4:	020c0000 	0x20c0000
  f8:	00010000 	sll	zero,at,0x0
  fc:	00021455 	0x21455
 100:	00025000 	sll	t2,v0,0x0
 104:	55000100 	0x55000100
	...
 110:	00000014 	0x14
 114:	00000018 	mult	zero,zero
 118:	186d0001 	0x186d0001
 11c:	34000000 	li	zero,0x0
 120:	02000000 	0x2000000
 124:	00188d00 	sll	s1,t8,0x14
 128:	00000000 	nop
 12c:	14000000 	bnez	zero,130 <data_size-0x7c4>
 130:	24000000 	li	zero,0
 134:	01000000 	0x1000000
 138:	00005400 	sll	t2,zero,0x10
	...
 144:	00040000 	sll	zero,a0,0x0
 148:	00010000 	sll	zero,at,0x0
 14c:	0000046d 	0x46d
 150:	00008800 	sll	s1,zero,0x0
 154:	8d000200 	lw	zero,512(t0)
 158:	00000020 	add	zero,zero,zero
	...
 164:	00002400 	sll	a0,zero,0x10
 168:	54000100 	0x54000100
 16c:	00000024 	and	zero,zero,zero
 170:	0000007c 	0x7c
 174:	7c610001 	0x7c610001
 178:	88000000 	lwl	zero,0(zero)
 17c:	01000000 	0x1000000
 180:	00005400 	sll	t2,zero,0x10
 184:	00000000 	nop
 188:	001c0000 	sll	zero,gp,0x0
 18c:	00800000 	0x800000
 190:	00010000 	sll	zero,at,0x0
 194:	00000060 	0x60
 198:	00000000 	nop
 19c:	00008800 	sll	s1,zero,0x0
 1a0:	00008c00 	sll	s1,zero,0x10
 1a4:	6d000100 	0x6d000100
 1a8:	0000008c 	syscall	0x2
 1ac:	000000b8 	0xb8
 1b0:	188d0002 	0x188d0002
	...
 1bc:	00000088 	0x88
 1c0:	00000098 	0x98
 1c4:	00540001 	0x540001
	...
 1d0:	04000000 	bltz	zero,1d4 <data_size-0x720>
 1d4:	01000000 	0x1000000
 1d8:	00046d00 	sll	t5,a0,0x14
 1dc:	01000000 	0x1000000
 1e0:	00030000 	sll	zero,v1,0x0
 1e4:	0000e88d 	break	0x0,0x3a2
	...
 1f0:	28000000 	slti	zero,zero,0
 1f4:	01000000 	0x1000000
 1f8:	00285400 	0x285400
 1fc:	00600000 	0x600000
 200:	00010000 	sll	zero,at,0x0
 204:	00006853 	0x6853
 208:	00006c00 	sll	t5,zero,0x10
 20c:	53000100 	0x53000100
 210:	0000006c 	0x6c
 214:	00000098 	0x98
 218:	ac540001 	sw	s4,1(v0)
 21c:	d4000000 	0xd4000000
 220:	01000000 	0x1000000
 224:	00e85400 	0xe85400
 228:	00e80000 	0xe80000
 22c:	00010000 	sll	zero,at,0x0
 230:	0000e854 	0xe854
 234:	0000f000 	sll	s8,zero,0x0
 238:	53000100 	0x53000100
 23c:	000000f8 	0xf8
 240:	000000f8 	0xf8
 244:	00530001 	0x530001
	...
 250:	28000000 	slti	zero,zero,0
 254:	01000000 	0x1000000
 258:	00285500 	0x285500
 25c:	00980000 	0x980000
 260:	00010000 	sll	zero,at,0x0
 264:	00009863 	0x9863
 268:	0000a400 	sll	s4,zero,0x10
 26c:	55000100 	0x55000100
 270:	000000ac 	0xac
 274:	000000b8 	0xb8
 278:	b8630001 	swr	v1,1(v1)
 27c:	cc000000 	lwc3	$0,0(zero)
 280:	01000000 	0x1000000
 284:	00cc5500 	0xcc5500
 288:	00d40000 	0xd40000
 28c:	00010000 	sll	zero,at,0x0
 290:	0000d463 	0xd463
 294:	0000e800 	sll	sp,zero,0x0
 298:	55000100 	0x55000100
 29c:	000000e8 	0xe8
 2a0:	000000f8 	0xf8
 2a4:	f8630001 	0xf8630001
 2a8:	00000000 	nop
 2ac:	01000001 	0x1000001
 2b0:	00005500 	sll	t2,zero,0x14
	...
 2bc:	00280000 	0x280000
 2c0:	00010000 	sll	zero,at,0x0
 2c4:	00002856 	0x2856
 2c8:	00008800 	sll	s1,zero,0x0
 2cc:	62000100 	0x62000100
 2d0:	00000088 	0x88
 2d4:	000000a4 	0xa4
 2d8:	ac560001 	sw	s6,1(v0)
 2dc:	bc000000 	0xbc000000
 2e0:	01000000 	0x1000000
 2e4:	00bc6200 	0xbc6200
 2e8:	00e80000 	0xe80000
 2ec:	00010000 	sll	zero,at,0x0
 2f0:	0000e856 	0xe856
 2f4:	0000f800 	sll	ra,zero,0x0
 2f8:	62000100 	0x62000100
 2fc:	000000f8 	0xf8
 300:	00000100 	sll	zero,zero,0x4
 304:	00560001 	0x560001
	...
 310:	a4000000 	sh	zero,0(zero)
 314:	01000000 	0x1000000
 318:	00ac5700 	0xac5700
 31c:	00f00000 	0xf00000
 320:	00010000 	sll	zero,at,0x0
 324:	0000f857 	0xf857
 328:	00010000 	sll	zero,at,0x0
 32c:	57000100 	0x57000100
	...
 338:	0000003c 	0x3c
 33c:	000000c0 	sll	zero,zero,0x3
 340:	cc610001 	lwc3	$1,1(v1)
 344:	e8000000 	swc2	$0,0(zero)
 348:	01000000 	0x1000000
 34c:	00f86100 	0xf86100
 350:	01000000 	0x1000000
 354:	00010000 	sll	zero,at,0x0
 358:	00000061 	0x61
 35c:	00000000 	nop
 360:	00006c00 	sll	t5,zero,0x10
 364:	0000a400 	sll	s4,zero,0x10
 368:	53000100 	0x53000100
 36c:	000000ac 	0xac
 370:	000000e8 	0xe8
 374:	f8530001 	0xf8530001
 378:	00000000 	nop
 37c:	01000001 	0x1000001
 380:	00005300 	sll	t2,zero,0xc
 384:	00000000 	nop
 388:	00d40000 	0xd40000
 38c:	00e00000 	0xe00000
 390:	00010000 	sll	zero,at,0x0
 394:	0000f854 	0xf854
 398:	0000f800 	sll	ra,zero,0x0
 39c:	54000100 	0x54000100
	...
 3a8:	00000034 	0x34
 3ac:	00000080 	sll	zero,zero,0x2
 3b0:	ac600001 	sw	zero,1(v1)
 3b4:	c4000000 	lwc1	$f0,0(zero)
 3b8:	01000000 	0x1000000
 3bc:	00e86000 	0xe86000
 3c0:	00f80000 	0xf80000
 3c4:	00010000 	sll	zero,at,0x0
 3c8:	00000060 	0x60
 3cc:	00000000 	nop
 3d0:	00002400 	sll	a0,zero,0x10
 3d4:	00002400 	sll	a0,zero,0x10
 3d8:	52000100 	0x52000100
	...
 3e4:	00000048 	0x48
 3e8:	00000050 	0x50
 3ec:	00520001 	0x520001
 3f0:	00000000 	nop
 3f4:	7c000000 	0x7c000000
 3f8:	80000000 	lb	zero,0(zero)
 3fc:	01000000 	0x1000000
 400:	00806d00 	0x806d00
 404:	01480000 	0x1480000
 408:	00020000 	sll	zero,v0,0x0
 40c:	0000188d 	break	0x0,0x62
 410:	00000000 	nop
 414:	007c0000 	0x7c0000
 418:	00c40000 	0xc40000
 41c:	00010000 	sll	zero,at,0x0
 420:	00000054 	0x54
 424:	00000000 	nop
 428:	00007c00 	sll	t7,zero,0x10
 42c:	0000ac00 	sll	s5,zero,0x10
 430:	55000100 	0x55000100
 434:	000000ac 	0xac
 438:	00000138 	0x138
 43c:	005a0001 	0x5a0001
 440:	00000000 	nop
 444:	90000000 	lbu	zero,0(zero)
 448:	f4000000 	0xf4000000
 44c:	01000000 	0x1000000
 450:	00005600 	sll	t2,zero,0x18
 454:	00000000 	nop
	...

Disassembly of section .debug_str:

00000000 <.debug_str>:
   0:	69616761 	0x69616761
   4:	6f6c006e 	0x6f6c006e
   8:	7520676e 	jalx	4819db8 <data_size+0x48194c4>
   c:	6769736e 	0x6769736e
  10:	2064656e 	addi	a0,v1,25966
  14:	00746e69 	0x746e69
  18:	20554e47 	addi	s5,v0,20039
  1c:	2e342043 	sltiu	s4,s1,8259
  20:	20302e33 	addi	s0,at,11827
  24:	2f00672d 	sltiu	zero,t8,26413
  28:	656d6f68 	0x656d6f68
  2c:	6363732f 	0x6363732f
  30:	736e2f32 	0x736e2f32
  34:	63637363 	0x63637363
  38:	63736e2f 	0x63736e2f
  3c:	2f636373 	sltiu	v1,k1,25459
  40:	7363736e 	0x7363736e
  44:	30326363 	andi	s2,at,0x6363
  48:	675f3132 	0x675f3132
  4c:	70756f72 	0x70756f72
  50:	2e30765f 	sltiu	s0,s1,30303
  54:	702f3130 	0x702f3130
  58:	5f667265 	0x5f667265
  5c:	74736574 	jalx	1cd95d0 <data_size+0x1cd8cdc>
  60:	2e30765f 	sltiu	s0,s1,30303
  64:	732f3130 	0x732f3130
  68:	2f74666f 	sltiu	s4,k1,26223
  6c:	66726570 	0x66726570
  70:	6e75665f 	0x6e75665f
  74:	696c2f63 	0x696c2f63
  78:	72700062 	0x72700062
  7c:	66746e69 	0x66746e69
  80:	7000632e 	0x7000632e
  84:	746e6972 	jalx	1b9a5c8 <data_size+0x1b99cd4>
  88:	67740066 	0x67740066
  8c:	75705f74 	jalx	5c17dd0 <data_size+0x5c174dc>
  90:	61686374 	0x61686374
  94:	75700072 	jalx	5c001c8 <data_size+0x5bff8d4>
  98:	61686374 	0x61686374
  9c:	00632e72 	0x632e72
  a0:	73747570 	0x73747570
  a4:	7000632e 	0x7000632e
  a8:	00737475 	0x737475
  ac:	73747570 	0x73747570
  b0:	6e697274 	0x6e697274
  b4:	61760067 	0x61760067
  b8:	0065756c 	0x65756c
  bc:	6e697270 	0x6e697270
  c0:	73616274 	0x73616274
  c4:	00632e65 	0x632e65
  c8:	676e6f6c 	0x676e6f6c
  cc:	746e6920 	jalx	1b9a480 <data_size+0x1b99b8c>
  d0:	67697300 	0x67697300
  d4:	7270006e 	0x7270006e
  d8:	62746e69 	0x62746e69
  dc:	00657361 	0x657361
  e0:	5f746567 	0x5f746567
  e4:	67007375 	0x67007375
  e8:	635f7465 	0x635f7465
  ec:	746e756f 	jalx	1b9d5bc <data_size+0x1b9ccc8>
  f0:	00796d5f 	0x796d5f
  f4:	656d6974 	0x656d6974
  f8:	63657073 	0x63657073
  fc:	6c635f00 	0x6c635f00
 100:	5f6b636f 	0x5f6b636f
 104:	76740074 	jalx	9d001d0 <data_size+0x9cff8dc>
 108:	65736d5f 	0x65736d5f
 10c:	65670063 	0x65670063
 110:	736e5f74 	0x736e5f74
 114:	6d697400 	0x6d697400
 118:	00632e65 	0x632e65
 11c:	7465675f 	jalx	1959d7c <data_size+0x1959488>
 120:	756f635f 	jalx	5bd8d7c <data_size+0x5bd8488>
 124:	6700746e 	0x6700746e
 128:	635f7465 	0x635f7465
 12c:	6b636f6c 	0x6b636f6c
 130:	5f767400 	0x5f767400
 134:	63657375 	0x63657375
 138:	6f635f00 	0x6f635f00
 13c:	6176746e 	0x6176746e
 140:	7674006c 	jalx	9d001b0 <data_size+0x9cff8bc>
 144:	6365735f 	0x6365735f
 148:	5f767400 	0x5f767400
 14c:	6365736e 	0x6365736e
 150:	6f6c6300 	0x6f6c6300
 154:	675f6b63 	0x675f6b63
 158:	69747465 	0x69747465
 15c:	Address 0x000000000000015c is out of bounds.

