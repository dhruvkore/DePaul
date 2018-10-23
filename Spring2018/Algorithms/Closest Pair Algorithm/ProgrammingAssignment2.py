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

#Returns X coordinate of point. Used as sorting parameter.
def byX(p):
	return p[0]

#Returns Y coordinate of point. Used as sorting parameter.
def byY(p):
	return p[1]

#Merge Sort with polar angle as comparison
#Merge Sort is O(nlg(n))
def mergesort(A, f):
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

	mergesort(lArr, f)
	mergesort(rArr, f)
	merge(A, lArr, rArr, f)

def merge(A, lArr, rArr, f):
	endl = len(lArr)
	endr = len(rArr)
	startl = 0
	startr = 0
	k = 0

	while (startl < endl) and (startr < endr):
		if (f(lArr[startl]) < f(rArr[startr])):
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

#Finds by brute forece for under 3 points
def bruteForce(sx):
	m = distance(sx[0], sx[1])

	p1 = sx[0]
	p2 = sx[1]

	if len(sx) == 2:
		return p1, p2, m
	for i in range(len(sx)):
		for j in range(i + 1, len(sx)):
			if j != 0 and i != 0:
				dist = distance(sx[i], sx[j])
				if dist < m:
					p1 = sx[i]
					p2 = sx[j]
					m = dist

	return p1, p2, m

#Finds Boundary Points Closest Points
def boundaryPoints(sx, sy, dist, i1, i2):
	mid = sx[int(len(sx) / 2)][0]

	#allpoints within lambda of mid
	sYL = []
	for x in sy:
		if (mid - dist <= x[0]) and (x[0] <= mid + dist):
			sYL.append(x)

	smallestDist = dist

	for i in range(len(sYL) - 1):
		for j in range(i + 1, min(i + 7, len(sYL))):
			p1 = sYL[i]
			p2 = sYL[j]
			d = distance(p1, p2)
			if d < smallestDist:
				i1 = p1
				i2 = p2
				smallestDist = d
	return i1, i2, smallestDist


#Finds Closest Pair Auxilary Method
def closest(sx, sy):
	#Brute force on recurrsion when less than or equal to three points O(1)
	if len(sx) <= 3:
		return bruteForce(sx)

	#Get Midpoint
	m = int(len(sx) / 2)
	lX = sx[:m]
	rX = sx[m:]
	mp = sx[m][0]

	#split left and right side O(n)
	lY = []
	rY = []
	for y in sy:
		if y[0] <= mp:
			lY.append(y)
		else:
			rY.append(y)

	#T(n) = 2T(n/2) + O(n)
	p1, p2, m1 = closest(lX, lY)
	p3, p4, m2 = closest(rX, rY)

	if m1 < m2:
		dist = m1
		o1 = p1
		o2 = p2
	else:
		dist = m2
		o1 = p3
		o2 = p4

	#Check Boundary Points O(n)
	p5, p6, m3 = boundaryPoints(sx, sy, dist, o1, o2)

	if dist <= m3:
		return o1, o2, dist
	else:
		return p5, p6, m3


#Finds the Closest Points from Input File
def findClosestPoints(file):
	coords = []
	print ("---Current File: " + file)

	#Input
	with open(file) as f:
		for line in f:
			coordsStr = line.split(" ")
			coords.append((int(coordsStr[0]), int(coordsStr[1]))) #list.append = O(n)

	sx = coords[:]
	sy = coords[:]

	#If less than three points, then just get distances O(1)
	if len(sx) <= 3:
		return bruteForce(sx)

	mergesort(sx, byX) #sorted by X O(nlg(n))
	mergesort(sy, byY) #sorted by Y O(nlg(n))

	p1, p2, m = closest(sx, sy) #O(nlg(n))
	return p1, p2, m


def main():
	print('\n\n-----Closest-Pair Algorithm-----\n\n')
	inputFiles = []

	for arg in sys.argv[1:]:
		inputFiles.append(arg)

	if(len(inputFiles) < 1):
		print('Please pass in file paths as command line arguments. For Example: ')
		print('python ProgrammingAssignment2.py ./tests/10points.txt ./tests/100points.txt ./tests/1000points.txt')

	for file in inputFiles:
		p1, p2, dist = findClosestPoints(file)
		print('---Answer to ' + file + ' : ---')
		print("Distance: " + str(dist) + " Points: (" + str(p1[0]) + ", " + str(p1[1]) + ") AND (" + str(p2[0]) + ", " + str(p2[1]) + ")")
		print('---Answer Done---\n\n')

	print('------------Done------------')


if __name__ == "__main__":
    main()