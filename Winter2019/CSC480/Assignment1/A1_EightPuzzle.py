#python3

import sys
import collections
import math
from queue import PriorityQueue

class puzzleState:
	def __init__(self, matrix, action, depth, pathcost, expanded):
		self.matrix = matrix
		self.action = action
		self.depth = depth
		self.pathcost = pathcost
		self.expanded = expanded
		self.cost = 1

		self.parent = None
		self.children = []

	def cloneMatrix(self):
		output = []
		for i in range(3):
			row = []
			for j in range(3):
				row.append(self.matrix[i][j])
			output.append(row)

		return output


	def moveUp(self):
		#rows
		for i in range(3):
			for j in range(3):
				if self.matrix[i][j] == 0:
					if i < 2:
						temp = self.matrix[i + 1][j]
						self.matrix[i + 1][j] = 0
						self.matrix[i][j] = temp
						self.pathcost = self.pathcost + temp
						return 1
					else:
						return 0

	def moveDown(self):
		#rows
		for i in range(3):
			for j in range(3):
				if self.matrix[i][j] == 0:
					if i > 0:
						temp = self.matrix[i - 1][j]
						self.matrix[i - 1][j] = 0
						self.matrix[i][j] = temp
						self.pathcost = self.pathcost + temp
						return 1
					else:
						return 0

	def moveLeft(self):
		#rows
		for i in range(3):
			for j in range(3):
				if self.matrix[i][j] == 0:
					if j < 2:
						temp = self.matrix[i][j + 1]
						self.matrix[i][j + 1] = 0
						self.matrix[i][j] = temp
						self.pathcost = self.pathcost + temp
						return 1
					else:
						return 0

	def moveRight(self):
		#rows
		for i in range(3):
			for j in range(3):
				if self.matrix[i][j] == 0:
					if j > 0:
						temp = self.matrix[i][j - 1]
						self.matrix[i][j - 1] = 0
						self.matrix[i][j] = temp
						self.pathcost = self.pathcost + temp
						return 1
					else:
						return 0

	def copyContents(self, newState):
		self.matrix = newState.matrix
		self.action = newState.action
		self.depth = newState.depth
		self.pathcost = newState.pathcost
		self.expanded = newState.expanded
		self.cost = newState.cost

		self.parent = newState.parent
		self.children = newState.children

	def compare(self, secondMatrix):
		for i in range(3):
			for j in range(3):
				if self.matrix[i][j] != secondMatrix[i][j]:
					return 0
		return 1

	def printMatrix(matrix):
		for i in range(3):
			for j in range(3):
				print(str(matrix[i][j]) + " ", end='')
			print()
		print("---")

	def printInfo(self):
		print("Length (depth)" + str(self.depth) + " Cost = " + str(self.pathcost) + " Time (iterations): " + str(self.iteration) + " Space (Max Queue Size): " + str(self.maxQueueSize))


	def printSolution(self):
		if self.parent is not None:
			self.parent.printSolution()

		if self.action is not None:
			print(self.action + ", ", end='')
		print("Length (depth)" + str(self.depth) + " Cost = " + str(self.pathcost) + " Time (iterations): " + str(self.iteration) + " Space (Max Queue Size): " + str(self.maxQueueSize))
		puzzleState.printMatrix(self.matrix)

	def printSequence(self):
		s = ""
		for i in range(3):
			for j in range(3):
				s = s + str(self.matrix[i][j])
		return s

	#Comparitors
	def __eq__(self, other):
		return self.pathcost == other.pathcost
	def __lt__(self, other):
		return self.pathcost < other.pathcost
	def __gt__(self, other):
		return self.pathcost > other.pathcost



class eightPuzzleAlgorithms:
	#Breadth-First-Search
	#Explores entire current depth before moving to next depth
	def bfs(initState, goal):
		queue = collections.deque([initState]) #queue
		iteration = 0

		visited = set()
		maxQueueSize = 1

		while queue:
			iteration = iteration + 1
			currentState = queue.popleft() #remove from leftside of the queue
			currentState.iteration = iteration
			if len(queue) > maxQueueSize:
				maxQueueSize = len(queue)
				currentState.maxQueueSize = maxQueueSize
			else:
				currentState.maxQueueSize = maxQueueSize

			if currentState.compare(goal): #compare to goal
				print("Final Maxqueue size: " + str(currentState.maxQueueSize))
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost, False)
			if up.moveUp() and up.printSequence() not in visited:
				queue.append(up)
				up.parent = currentState
				currentState.children.append(up)
				visited.add(up.printSequence()) #add to visited states

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost, False)
			if left.moveLeft() and left.printSequence() not in visited:
				queue.append(left)
				left.parent = currentState
				currentState.children.append(left)
				visited.add(left.printSequence())

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost, False)
			if down.moveDown() and down.printSequence() not in visited:
				queue.append(down)
				down.parent = currentState
				currentState.children.append(down)
				visited.add(down.printSequence())

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost, False)
			if right.moveRight() and right.printSequence() not in visited:
				queue.append(right)
				right.parent = currentState
				currentState.children.append(right)
				visited.add(right.printSequence())

			currentState.expanded = True


	#Depth-First-Search
	#Explores entire depth before going to the next sibling nodes
	def dfs(initState, goal):
		queue = collections.deque([initState])
		iteration = 0

		visited = set()
		maxQueueSize = 1

		while queue:
			iteration = iteration + 1
			currentState = queue.pop()
			currentState.iteration = iteration
			if  len(queue) > maxQueueSize:
				maxQueueSize = len(queue)
				currentState.maxQueueSize = maxQueueSize
			else:
				currentState.maxQueueSize = maxQueueSize

			if currentState.compare(goal):
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost, False)
			if up.moveUp() and up.printSequence() not in visited:
				queue.append(up)
				visited.add(up.printSequence()) #add to visited states

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost, False)
			if left.moveLeft() and left.printSequence() not in visited:
				queue.append(left)
				visited.add(left.printSequence())

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost, False)
			if down.moveDown() and down.printSequence() not in visited:
				queue.append(down)
				visited.add(down.printSequence())

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost, False)
			if right.moveRight() and right.printSequence() not in visited:
				queue.append(right)
				visited.add(right.printSequence())

			currentState.expanded = True


	def ids(initState, goal):
		iteration = 0

		maxDepth = 1
		maxQueueSize = 1

		while maxDepth < 999999:
			visited = set()

			queue = collections.deque([initState])
			while queue:
				iteration = iteration + 1
				currentState = queue.pop()
				currentState.iteration = iteration
				if queue.qsize() > maxQueueSize:
					maxQueueSize = queue.qsize()
					currentState.maxQueueSize = maxQueueSize
				else:
					currentState.maxQueueSize = maxQueueSize

				if currentState.compare(goal):
					return currentState

				if currentState.depth == maxDepth: #If maxDepth reached, restart and increment maxDepth
					break

				up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost, False)
				if up.moveUp() and up.printSequence() not in visited:
					queue.append(up)
					visited.add(up.printSequence()) #add to visited states

				left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost, False)
				if left.moveLeft() and left.printSequence() not in visited:
					queue.append(left)
					visited.add(left.printSequence())

				down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost, False)
				if down.moveDown() and down.printSequence() not in visited:
					queue.append(down)
					visited.add(down.printSequence())

				right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost, False)
				if right.moveRight() and right.printSequence() not in visited:
					queue.append(right)
					visited.add(right.printSequence())

				currentState.expanded = True

			maxDepth = maxDepth + 1


	def ucs(initState, goal):
		iteration = 0

		visited = {}

		queue = PriorityQueue()
		queue.put((initState.pathcost, iteration, initState))
		maxQueueSize = 1

		while queue:
			iteration = iteration + 1
			currentStateTuple = queue.get()
			currentState = currentStateTuple[2]
			currentState.iteration = iteration
			if queue.qsize() > maxQueueSize:
				maxQueueSize = queue.qsize()
				currentState.maxQueueSize = maxQueueSize
			else:
				currentState.maxQueueSize = maxQueueSize

			if currentState.compare(goal):
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost, False)
			if up.moveUp():
				if up.printSequence() not in visited:
					visited[up.printSequence()] = (up.pathcost, iteration, up) #add to visited states
					queue.put(visited[up.printSequence()])
					
				elif not visited[up.printSequence()][2].expanded:
					currentDuplicate = visited[up.printSequence()][2]
					if currentDuplicate.pathcost > up.pathcost:
						visited[up.printSequence()][2].copyContents(up)

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost, False)
			if left.moveLeft():
				if left.printSequence() not in visited:
					visited[left.printSequence()] = (left.pathcost, iteration, left) #add to visited states
					queue.put(visited[left.printSequence()])
					
				elif not visited[left.printSequence()][2].expanded:
					currentDuplicate = visited[left.printSequence()][2]
					if currentDuplicate.pathcost > left.pathcost:
						visited[left.printSequence()][2].copyContents(left)

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost, False)
			if down.moveDown():
				if down.printSequence() not in visited:
					visited[down.printSequence()] = (down.pathcost, iteration, down) #add to visited states
					queue.put(visited[down.printSequence()])
					
				elif not visited[down.printSequence()][2].expanded:
					currentDuplicate = visited[down.printSequence()][2]
					if currentDuplicate.pathcost > down.pathcost:
						visited[down.printSequence()][2].copyContents(down)

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost, False)
			if right.moveRight():
				if right.printSequence() not in visited:
					visited[right.printSequence()] = (right.pathcost, iteration, right) #add to visited states
					queue.put(visited[right.printSequence()])
					
				elif not visited[right.printSequence()][2].expanded:
					currentDuplicate = visited[right.printSequence()][2]
					if currentDuplicate.pathcost > right.pathcost:
						visited[right.printSequence()][2].copyContents(right)

			currentState.expanded = True

	def gbf(initState, goal):
		return eightPuzzleAlgorithms.heuristicAlgo(initState, goal, eightPuzzleAlgorithms.gbfStrategy)

	def gbfStrategy(state, goal):
		notInPosition = 0
		for i in range(3):
			for j in range(3):
				if state.matrix[i][j] != goal[i][j]:
					notInPosition = notInPosition + 1

		return notInPosition

	def aStarOne(initState, goal):
		return eightPuzzleAlgorithms.heuristicAlgo(initState, goal, eightPuzzleAlgorithms.aStarOneStrategy)

	def aStarOneStrategy(state, goal):
		notInPosition = 0
		for i in range(3):
			for j in range(3):
				if state.matrix[i][j] != goal[i][j]:
					notInPosition = notInPosition + 1

		return notInPosition + state.pathcost


	def aStarTwo(initState, goal):
		return eightPuzzleAlgorithms.heuristicAlgo(initState, goal, eightPuzzleAlgorithms.aStarTwoStrategy)

	def aStarTwoStrategy(state, goal):
		numToPosition = {
			1:(0,0), 2:(0,1), 3:(0,2),
			8:(1,0), 0:(1,1), 4:(1,2),
			7:(2,0), 6:(2,1), 5:(2,2)
		}

		sumOfDists = 0
		for i in range(3):
			for j in range(3):
				sumOfDists = sumOfDists + eightPuzzleAlgorithms.manhattandistance((i,j), numToPosition[state.matrix[i][j]])

		return sumOfDists + state.pathcost

	def manhattandistance(one, two):
		return (abs(one[0] - one[1]) + abs(two[0] - two[1]))

	def euclideandistance(one, two):
		return math.sqrt((one[0]-two[0])**2 + (one[1]-two[1])**2)

	def aStarThree(initState, goal):
		return eightPuzzleAlgorithms.heuristicAlgo(initState, goal, eightPuzzleAlgorithms.aStarThreeStrategy)

	# heuristic uses Euclidean
	def aStarThreeStrategy(state, goal):
		numToPosition = {
			1:(0,0), 2:(0,1), 3:(0,2),
			8:(1,0), 0:(1,1), 4:(1,2),
			7:(2,0), 6:(2,1), 5:(2,2)
		}
		sumOfDists = 0
		for i in range(3):
			for j in range(3):
				sumOfDists = sumOfDists + eightPuzzleAlgorithms.euclideandistance((i,j), numToPosition[state.matrix[i][j]])

		return sumOfDists + state.pathcost



	def heuristicAlgo(initState, goal, f):
		iteration = 0

		visited = {}

		queue = PriorityQueue()
		queue.put((initState.pathcost, iteration, initState))

		maxQueueSize = 1

		while queue:
			iteration = iteration + 1
			currentStateTuple = queue.get()
			currentState = currentStateTuple[2]
			currentState.iteration = iteration
			if queue.qsize() > maxQueueSize:
				maxQueueSize = queue.qsize()
				currentState.maxQueueSize = maxQueueSize
			else:
				currentState.maxQueueSize = maxQueueSize

			if currentState.compare(goal):
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost, False)
			if up.moveUp():
				up.parent = currentState
				currentState.children.append(up)

				if up.printSequence() not in visited:
					visited[up.printSequence()] = (f(up, goal), iteration, up) #add to visited states
					queue.put(visited[up.printSequence()])
					
				elif not visited[up.printSequence()][2].expanded:
					currentDuplicate = visited[up.printSequence()][2]
					if f(currentDuplicate, goal) > f(up, goal):
						visited[up.printSequence()][2].copyContents(up)

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost, False)
			if left.moveLeft():
				left.parent = currentState
				currentState.children.append(left)

				if left.printSequence() not in visited:
					visited[left.printSequence()] = (f(left, goal), iteration, left) #add to visited states
					queue.put(visited[left.printSequence()])
					
				elif not visited[left.printSequence()][2].expanded:
					currentDuplicate = visited[left.printSequence()][2]
					if f(currentDuplicate, goal) > f(left, goal):
						visited[left.printSequence()][2].copyContents(left)

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost, False)
			if down.moveDown():
				down.parent = currentState
				currentState.children.append(down)

				if down.printSequence() not in visited:
					visited[down.printSequence()] = (f(down, goal), iteration, down) #add to visited states
					queue.put(visited[down.printSequence()])
					
				elif not visited[down.printSequence()][2].expanded:
					currentDuplicate = visited[down.printSequence()][2]
					if f(currentDuplicate, goal) > f(down, goal):
						visited[down.printSequence()][2].copyContents(down)

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost, False)
			if right.moveRight():
				right.parent = currentState
				currentState.children.append(right)

				if right.printSequence() not in visited:
					visited[right.printSequence()] = (f(right, goal), iteration, right) #add to visited states
					queue.put(visited[right.printSequence()])
					
				elif not visited[right.printSequence()][2].expanded:
					currentDuplicate = visited[right.printSequence()][2]
					if f(currentDuplicate, goal) > f(right, goal):
						visited[right.printSequence()][2].copyContents(right)

			currentState.expanded = True


def main(argv):
	goal = [[1,2,3], [8,0,4], [7,6,5]]
	start = [[1,2,3], [8,0,4], [7,6,5]]

	while (True):
		difficulty = input("Enter easy, medium, or hard: ")

		if difficulty == "easy":
			start = [[1,3,4], 
					 [8,6,2], 
					 [7,0,5]]
		elif difficulty == "medium":
			start = [[2,8,1], [0,4,3], [7,6,5]]
		elif difficulty == "hard":
			start = [[5,6,7], [4,0,8], [3,2,1]]
		else:
			print("Ending Program.")
			exit()

		startPuzzleState = puzzleState(start, None, 0, 0, True)

		algorithm = input("Enter algorithm: ")

		if algorithm == "bfs":
			solution = eightPuzzleAlgorithms.bfs(startPuzzleState, goal)
			solution.printSolution()
		elif algorithm == "dfs":
			solution = eightPuzzleAlgorithms.dfs(startPuzzleState, goal)
			solution.printInfo()
			#solution.printSolution()
		elif algorithm == "ids":
			solution = eightPuzzleAlgorithms.ids(startPuzzleState, goal)
			solution.printInfo()
		elif algorithm == "ucs":
			solution = eightPuzzleAlgorithms.ucs(startPuzzleState, goal)
			solution.printInfo()
		elif algorithm == "gbf":
			solution = eightPuzzleAlgorithms.gbf(startPuzzleState, goal)
			solution.printInfo()
		elif algorithm == "a*1":
			solution = eightPuzzleAlgorithms.aStarOne(startPuzzleState, goal)
			solution.printInfo()
		elif algorithm == "a*2":
			solution = eightPuzzleAlgorithms.aStarTwo(startPuzzleState, goal)
			solution.printSolution()
		elif algorithm == "a*3":
			solution = eightPuzzleAlgorithms.aStarThree(startPuzzleState, goal)
			solution.printSolution()





if __name__ == "__main__":
	main(sys.argv[1:])