.data
	space: .asciiz " "

.text

init_student:
	li $t7 0  # Intializing t0 to 0
	li $t8 0  # Intializing t1 to 0

	sll $t7 $a0 10  # Shifting the first argument (id) by 10 bits to the left into t0
	or $t8 $t7 $a1  # Storing the id and name in a temp variable t1

	sw $t8 0($a3)  # Storing id and name in the struct
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
	li $t0 0  # Intializing loop variable i in t0 
	li $t1 0  # Initialize temp for first argument (number of students)
	li $t2 0  # Initialize temp for second argument (id)
	li $t3 0  # Initialize temp for third argument (credits)
	li $t4 0  # Initialize temp for fourth argument (names)
	li $t5 0  # Initialize temp for fifth argument (records)
	li $t6 0  # Initialize temp for name traversal 

	move $t1 $a0  # Move all arguments to temporaries 
	move $t2 $a1  # Need this as arguments will be modified to call another function (init_student)
	move $t3 $a2  
	move $t4 $a3 

	move $fp $sp  # Initializing frame pointer to start position
	addi $sp $sp -4  # Allocating space for one element in the stack pointer (sp)

	lw $t5 0($fp)  # Store fifth argument in temp variable (t5) 
	sw $ra 0($sp)  # Store return address (ra) of caller in the stack

	lw $a0 0($t2)  # Move id to first argument (a0)
	lw $a1 0($t3)  # Move credits to second argument (a1)
	move $a2 $t4  # Move names to third argument (a2)
	move $a3 $t5  # Move records to fourth argument (a3)

	while:
		beq $t0 $t1 done  # Condition to terminate loop
		jal init_student  # Function call to initialize student at i th index

		addi $t2 $t2 4 # Increment the index in id, credits and record
		addi $t3 $t3 4
		addi $t5 $t5 8
		
		loop:
			lbu $t6 0($t4)  # Load character in temp
			beqz $t6 exit
			addi $t4 $t4 1  
			j loop
			
		exit:
			addi $t4 $t4 1
		
		lw $a0 0($t2)  # Load from the temp into the arguments
		lw $a1 0($t3)  # Loading id and credits as values and name and record as pointers
		move $a2 $t4
		move $a3 $t5

		addi $t0 $t0 1  # Increment loop variable i by 1
		j while 

	done:

	lw $ra 0($sp)  # Storing callers return address in stack back in ra
	addi $sp $sp 4  # Deallocating stack	

	jr $ra
	
insert:
	li $t0 0  # Initializing t0 to 0 (Stores id)
	li $t1 0  # Initializing t1 to 0 (Stores credits)
	li $t2 0  # Intializing t2 to 0 (Stores name)
	move $t7 $a1  # Storing the table in a temp variable t7
	
	li $t8 0 # Temp value for retrieving from the table

	li $t3 0  # Intializing t3 to store index
	li $t6 0  # Initializing t6 to store loop variable (count) (counts no. of iterations) and i (increment)

	li $t4 0  # Empty symbol 
	li $t5 -1  # Tombstone symbol

	lw $t1 0($a0)  # Load id and credits in t0
	lw $t2 4($a0)  # Load name in t1

	srl $t0 $t1 10  # Shift by 10 to remove credits and store id in t2

	sll $t1 $t1 22  # Left shift then immediately right shift by 22 to remove id
	srl $t1 $t1 22

	div $t0 $a2  # Generating index step 1
	mfhi $t3  # Generating index step 2
	
	for2:
		beq $t6 $t3 reset  # If t6 (i) reaches the index
		
		addi $t7 $t7 4  # Incrementing table pointer
		addi $t6 $t6 1  # Incrementing t6 (i) by 1
		
		j for2
		
	reset:
		li $t6 0  # Reinitializing t6 (count) for use
		j for
		
	cycle:
		li $t3 0
		move $t7 $a1
		
	for:  # Loop through the table starting at index till cycle is completed
		beq $t6 $a2 stop  # If the value is not inserted and t6 (count) reaches a2 (size of table)
		beq $t3 $a2 cycle  
		
		lw $t8 0($t7)
		
		beq $t8 $t4 put
		beq $t8 $t5 put
		
		addi $t7 $t7 4  # Increment the table pointer 
		addi $t6 $t6 1  # Increment t6 (count) by 1
		addi $t3 $t3 1  # Increment index
		
		j for

	put:
		sw $a0 0($t7)
		move $v0 $t3
		j end

	stop:
		li $v0 -1
		j end
		
	end:

	jr $ra
	
search:
	jr $ra

delete:
	jr $ra
