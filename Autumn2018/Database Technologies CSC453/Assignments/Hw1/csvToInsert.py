#Alterations need to be made after this for plausible insert statements

i = 1

with open("Loan.csv") as f1, open("loaninserts", "w") as f3:
	for line in f1:
		output = str(i)
		f3.write("INSERT INTO Loans VALUES(")
		f3.write(output)
		split = line.split(',')
		for a in split:
			f3.write(", '" + a + "'")

		f3.write(");\n")
		i += 1;