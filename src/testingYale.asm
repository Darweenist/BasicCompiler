	.text
	.globl main
main:
	li $v0 2
	sw $v0 varf
	li $v0 3
	subu $sp $sp 4
	sw $v0 ($sp)
	jal procfoo
	lw $a1 ($sp)
	addu $sp $sp 4
	sw $v0 varignore
	la $t0 varf
	lw $v0 ($t0)
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 nextline
	li $v0 4
	syscall
	li $v0 10
	syscall # halt
procfoo:
	subu $sp $sp 4
	sw $ra ($sp)
	li $t1 0
	subu $sp $sp 4
	sw $t1 ($sp)
	lw $v0 12($sp)
	subu $sp $sp 4
	sw $v0 ($sp)
	la $t0 varf
	lw $v0 ($t0)
	lw $t0 ($sp)
	addu $sp $sp 4
	addu $v0 $t0 $v0
	subu $sp $sp 4
	sw $v0 ($sp)
	jal procbar
	lw $a1 ($sp)
	addu $sp $sp 4
	sw $v0 varignore
	lw $v0 ($sp)
	addu $sp $sp 4
	lw $ra ($sp)
	addu $sp $sp 4
	jr $ra
procbar:
	subu $sp $sp 4
	sw $ra ($sp)
	li $t1 0
	subu $sp $sp 4
	sw $t1 ($sp)
	lw $v0 12($sp)
	move $a0 $v0
	li $v0 1
	syscall
	la $a0 nextline
	li $v0 4
	syscall
	lw $v0 ($sp)
	addu $sp $sp 4
	lw $ra ($sp)
	addu $sp $sp 4
	jr $ra
	.data
	nextline: .asciiz "\n"
	varf: .word 0
	varignore: .word 0
