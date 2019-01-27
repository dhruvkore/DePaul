#python3

import sys
import collections

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
						return 1
					else:
						return 0

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
		print("Length (depth)" + str(self.depth) + " Cost = " + str(self.pathcost) + " Time (iterations): " + str(self.iteration))


	def printSolution(self):
		if self.parent is not None:
			self.parent.printSolution()

		if self.action is not None:
			print(self.action + ", ", end='')
		print("Length (depth)" + str(self.depth) + " Cost = " + str(self.pathcost) + " Time (iterations): " + str(self.iteration))
		puzzleState.printMatrix(self.matrix)

	def printSequence(self):
		s = ""
		for i in range(3):
			for j in range(3):
				s = s + str(self.matrix[i][j])
		return s



class eightPuzzleAlgorithms:
	#Breadth-First-Search
	#Explores entire current depth before moving to next depth
	def bfs(initState, goal):
		queue = collections.deque([initState]) #queue
		iteration = 0

		visited = set()

		while queue:
			iteration = iteration + 1
			currentState = queue.popleft() #remove from leftside of the queue
			currentState.iteration = iteration

			if currentState.compare(goal): #compare to goal
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost + 1, False)
			if up.moveUp() and up.printSequence() not in visited:
				queue.append(up)
				up.parent = currentState
				currentState.children.append(up)
				visited.add(up.printSequence()) #add to visited states

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost + 1, False)
			if left.moveLeft() and left.printSequence() not in visited:
				queue.append(left)
				left.parent = currentState
				currentState.children.append(left)
				visited.add(left.printSequence())

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost + 1, False)
			if down.moveDown() and down.printSequence() not in visited:
				queue.append(down)
				down.parent = currentState
				currentState.children.append(down)
				visited.add(down.printSequence())

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost + 1, False)
			if right.moveRight() and right.printSequence() not in visited:
				queue.append(right)
				right.parent = currentState
				currentState.children.append(right)
				visited.add(right.printSequence())

			currentState.expanded = True

	#Depth-First-Search
	#Explores entire depth before going to the next sibling nodes
	def dfs(initState, goal, maxDepth = 0):
		queue = collections.deque([initState])
		iteration = 0

		visited = set()

		while queue:
			iteration = iteration + 1
			currentState = queue.pop()
			currentState.iteration = iteration

			if currentState.compare(goal):
				return currentState

			up = puzzleState(currentState.cloneMatrix(), "UP", currentState.depth + 1, currentState.pathcost + 1, False)
			if up.moveUp() and up.printSequence() not in visited:
				queue.append(up)
				visited.add(up.printSequence()) #add to visited states

			left = puzzleState(currentState.cloneMatrix(), "LEFT", currentState.depth + 1, currentState.pathcost + 1, False)
			if left.moveLeft() and left.printSequence() not in visited:
				queue.append(left)
				visited.add(left.printSequence())

			down = puzzleState(currentState.cloneMatrix(), "DOWN", currentState.depth + 1, currentState.pathcost + 1, False)
			if down.moveDown() and down.printSequence() not in visited:
				queue.append(down)
				visited.add(down.printSequence())

			right = puzzleState(currentState.cloneMatrix(), "RIGHT", currentState.depth + 1, currentState.pathcost + 1, False)
			if right.moveRight() and right.printSequence() not in visited:
				queue.append(right)
				visited.add(right.printSequence())

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






if __name__ == "__main__":
	main(sys.argv[1:])