import sys
import math
from math import atan2

#Run-time estimates for python functions taken from https://wiki.python.org/moin/TimeComplexity


#With respect to the origin (0,0) to p1 to p0
#This function is O(1)
def polarAngle(p0, p1):
	y = p0[1]-p1[1]
	x = p0[0]-p1[0]
	return atan2(y, x)

#O(1)
#Left or Right from p1, p2, p3
def crossProductofThree(p1,p2,p3):
	return (p2[0]-p1[0])*(p3[1]-p1[1]) - (p2[1]-p1[1])*(p3[0]-p1[0])

#distance between p1 and p2
#This function is O(1)
def distance(p1, p2):
	return math.sqrt(abs((p1[0] - p2[0])**2 + (p1[1] - p2[1])**2))

#Merge Sort with polar angle as comparison
#Merge Sort is O(nlg(n))
def mergesort(A, yminCoord):
	lArr = []
	rArr = []

	n = len(A)

	if n <= 1:
		return

	for i in range(0, n):
		if i < (n / 2):
			lArr.append(A[i])
		else:
			rArr.append	(A[i])

	mergesort(lArr, yminCoord)
	mergesort(rArr, yminCoord)
	merge(A, lArr, rArr, yminCoord)

def merge(A, lArr, rArr, yminCoord):
	endl = len(lArr)
	endr = len(rArr)
	startl = 0
	startr = 0
	k = 0

	while (startl < endl) and (startr < endr):
		if (polarAngle(lArr[startl], yminCoord) < polarAngle(rArr[startr], yminCoord)):
			A[k] = lArr[startl]
			startl += 1
		else:
			A[k] = rArr[startr]
			startr += 1
		k += 1

	while startl < endl:
		A[k] = lArr[startl]
		startl += 1
		k += 1

	while startr < endr:
		A[k] = rArr[startr]
		startr += 1
		k += 1

#finds ConvexHull of input file
#findConvexHull is O(nlg(n) due to sorting bottleneck
def findConvexHull(file):
	coords = []
	print ("---Current File: " + file)

	#Input
	with open(file) as f:
		for line in f:
			coordsStr = line.split(" ")
			coords.append((int(coordsStr[0]), int(coordsStr[1]))) #list.append = O(n)

	sortedByPolarAngle = [(0,0)] * (len(coords) - 1)

	#O(n)
	#Find y-min and leftmost if tie
	#Also creates a second array of all other values
	i = 0
	ymin = coords[0]
	for coord in coords[1:]:
		if coord[1] < ymin[1]:
			sortedByPolarAngle[i] = ymin
			ymin = coord
		elif coord[1] == ymin[1] and coord[0] < ymin[0]:
			sortedByPolarAngle[i] = ymin
			ymin = coord
		else:
			sortedByPolarAngle[i] = coord
		i+=1

	#O(n*lg(n)) for merge sort
	mergesort(sortedByPolarAngle, ymin)

	#---sortedByPolarAngle is now actually sorted---

	#O(1)
	prevAngle = polarAngle(sortedByPolarAngle[0], ymin)
	prevCoord = sortedByPolarAngle[0] 
	withoutColinear = []
	withoutColinear.append(prevCoord)

	#O(n)
	#If colinear, keep one farthest distance point and remove points with same polar angle
	for coord in sortedByPolarAngle[1:]:
		if(polarAngle(prevCoord, ymin) == polarAngle(coord, ymin)):
			if(distance(ymin, prevCoord) > distance(ymin, coord)):
				continue
			else:
				withoutColinear.pop()
				withoutColinear.append(coord)
				prevCoord = coord
		else:
			prevAngle = polarAngle(coord, ymin)
			withoutColinear.append(coord)
			prevCoord = coord

	#O(1)
	# if 3 points or less then return the points
	stack = []
	if(len(withoutColinear) < 4):
		stack.append(ymin)
		for coord in withoutColinear:
			stack.append(coord)
		return stack
	

	#Gram Scan here O(n)
	stack.append(ymin)
	stack.append(withoutColinear[0])

	for s in withoutColinear[1:]:
		while crossProductofThree(stack[-2], stack[-1], s) <= 0:
			stack.pop()
			if len(stack)<2: break
		stack.append(s)
	return stack


def main():
	print('\n\n-----Graham\'s Algorithm-----\n\n')
	inputFiles = []

	for arg in sys.argv[1:]:
		inputFiles.append(arg)

	if(len(inputFiles) < 1):
		print('Please pass in file paths as command line arguments. For Example: ')
		print('python3 ProgrammingAssignment1.py ./TestFiles-Graham/case1.txt ./TestFiles-Graham/case2.txt ./TestFiles-Graham/case3.txt ./TestFiles-Graham/case4.txt ./TestFiles-Graham/case5.txt ./TestFiles-Graham/case6.txt')

	for file in inputFiles:
		output = findConvexHull(file)
		print('---Answer to ' + file + ' : ---')
		for x in output:
			print(str(x))
		print('---Answer Done---\n\n')

	print('------------Done------------')


if __name__ == "__main__":
    main()