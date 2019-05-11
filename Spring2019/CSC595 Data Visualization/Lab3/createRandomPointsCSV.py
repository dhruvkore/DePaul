import random

file = open("walkPoints.csv", "w")

file.write("x,y\n")

x = 0
y = 0

file.write(str(x) + "," + str(y) + "\n")

for i in range(0, 500):
	x += random.randint(1, 10)
	y += random.randint(1, 10)
	file.write(str(x) + "," + str(y) + "\n")

file.close()
