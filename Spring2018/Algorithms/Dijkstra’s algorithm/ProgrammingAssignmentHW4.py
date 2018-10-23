import sys
from collections import defaultdict
from heapq import *

#
# Documentation on "heapq" below. It is a module that implements minheap.
# https://docs.python.org/3.0/library/heapq.html
# Functions heappop and heappush are used.
#


#Finds the Shortest Path from Input File
#Runtime O(mlgn)
def dijkstras(file):
	edges = []
	print ("---Current File: " + file + "---\n")

	graph = defaultdict(list)

	#Input
	with open(file) as f:
		next(f)
		for line in f:
			coordsStr = line.split(" ")
			graph[coordsStr[0]].append((coordsStr[1], int(coordsStr[2]))) #list.append = O(n)

	#hard-coded start and end as per assignment file
	start = "A"
	end = "B"

	distCurrentPi = [(0, start, ())]
	visited = set()

	while distCurrentPi:
		(dist, current, pi) = heappop(distCurrentPi) #Extract-Min O(lg(n))
		if current not in visited:
			visited.add(current)
			pi = (current, pi)
			if current == end:
				print(dist)
				print(printUnpackTupleRecc(pi))
				return pi

			for nextV, distNextV in graph.get(current, ()):
				if nextV not in visited:
					total = dist + distNextV
					heappush(distCurrentPi, (total, nextV, pi)) #Add neighbors to current
	return pi

# Recurrsively prints out tuple lisp
# Lisp style list was created using tuples
# O(n) since only run at end
def printUnpackTupleRecc(x):
	if(x != ()):
		return str(printUnpackTupleRecc(x[1])) + " " + str(x[0])
	else:
		return ""

def main():
	print('\n\n-----Dijkstra\'s Algorithm-----\n\n')
	inputFiles = []

	for arg in sys.argv[1:]:
		inputFiles.append(arg)

	if(len(inputFiles) < 1):
		print('Please pass in file paths as command line arguments. For Example: ')
		print('python ProgrammingAssignmentHW4.py ./tests/Case1.txt ./tests/Case2.txt ./tests/Case3.txt')

	for file in inputFiles:
		print('---Answer to ' + file + ' : ---')
		dijkstras(file)
		print('\n---Answer Done---\n\n')

	print('------------Done------------')


if __name__ == "__main__":
    main()