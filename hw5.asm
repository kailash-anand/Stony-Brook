.data
	space: .asciiz " "

.text

init_student:
	li $t0 0  # Intializing t0 to 0
	li $t1 0  # Intializing t1 to 0

	sll $t0 $a0 10  # Shifting the first argument (id) by 10 bits to the left into t0
	or $t1 $t0 $a1  # Storing the id and name in a temp variable t1

	sw $t1 0($a3)  # Storing id and name in the struct
	sw $a2 4($a3)  # Store the name in the struct

	jr $ra
	
print_student:
	li $t0 0  # Initializing t0 to 0
	li $t1 0  # Initializing t1 to 0
	li $t2 0  # Intializing t2 to 0

	lw $t0 0($a0)  # Load id and credits in t0
	lw $t1 4($a0)  # Load name in t1

	srl $t2 $t0 10  # Shift by 10 to remove credits and store id in t2

	move $a0 $t2  # Print the id
	li $v0 1
	syscall

	la $a0 space  # Print space
	li $v0 4
	syscall

	sll $t2 $t0 22  # Left shift then immediately right shift by 22 to remove id
	srl $t2 $t2 22

	move $a0 $t2  # Print the credits
	li $v0 1
	syscall

	la $a0 space  # Print space
	li $v0 4
	syscall

	move $a0 $t1  # Print the name
	li $v0 4
	syscall

	jr $ra
	
init_student_array:
	jr $ra
	
insert:
	jr $ra
	
search:
	jr $ra

delete:
	jr $ra
