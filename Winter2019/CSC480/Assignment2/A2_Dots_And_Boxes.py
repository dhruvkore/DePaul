# Dhruv Kore
# CSC 480 Loop
# Assignment 2 - Dots and Boxes
import collections
from random import randint

class Node:
    def __init__(self, gamestate):
        self.currentState = gamestate
        self.currentStateScore = 0
        self.children = {}

    def createChild(self, x, y, isPlayer):
        self.children[(x, y)] = Node(self.currentState.cloneGameState())
        multiplier = 1
        if isPlayer:
            multiplier *= -1
        # print("Score Before: " + str(self.currentStateScore))
        self.children[(x, y)].currentStateScore = \
            (self.children[(x, y)].currentState.makeMove(x, y) \
              * multiplier) \
              + self.currentStateScore
        # print("Score After: " + str(self.currentStateScore) + " Child Store: " + str(self.children[(x, y)].currentStateScore))
        # print("Set (" + str(x) + ", " + str(y) + ") Score set to: " + str(self.children[(x, y)].currentStateScore))

    def addChildren(self, x, y, childNode):
        self.children[(x, y)] = childNode

    def printBoard(self):
        self.currentState.printMatrix()

class GameState:
    def __init__(self, matrix, actualXArraySize, actualYArraySize):
        self.matrix = matrix
        self.actualXArraySize = actualXArraySize
        self.actualYArraySize = actualYArraySize

    def initializeMatrix(self):
        for i in range(0, self.actualYArraySize):
            row = []
            for j in range (0, self.actualXArraySize):
                if i % 2 == 1 and j % 2 == 1: # if x and y are odd, will be rand number
                    row.append(randint(1, 5))  # random points of 1 to 5
                elif i % 2 == 0 and j % 2 == 0:
                    row.append('*') # corners of boxes
                else:
                    row.append(' ') # places for player move
            self.matrix.append(row)

    def cloneMatrix(self):
        output = []
        for i in range(0, self.actualYArraySize):
            row = []
            for j in range(0, self.actualXArraySize):
                row.append(self.matrix[i][j])
            output.append(row)
        return output

    def printMatrix(self):
        #Top Row Index
        if self.actualXArraySize > 9:
            print(" ", end='')
        print("   ", end='')
        for x in range(0, self.actualXArraySize):
            print(str(x), end='')
        print()

        #Top Row Seperation Dashes
        if self.actualXArraySize > 9:
            print(" ", end='')
        print("   ", end='')
        for x in range(0, self.actualXArraySize + 1):
            print("-", end='')
        print()
        for i in range(self.actualYArraySize):
            if self.actualXArraySize > 9 and i < 10: # additional space for single digit numbers
                print(" ", end='')
            print(str(i) + "| ", end='')
            for j in range(self.actualXArraySize):
                print(str(self.matrix[i][j]), end='')
            print()
        print("---")

    def cloneGameState(self):
        return GameState(self.cloneMatrix(), self.actualXArraySize, self.actualYArraySize)

    def makeMove(self, x, y):
        toReturnSum = 0
        # print("x: " + str(x) + " y: " + str(y))

        if y % 2 == 0 and x % 2 == 1:
            self.matrix[y][x] = '-'
            if y < self.actualYArraySize - 1:
                if self.matrix[y + 2][x] == '-' \
                        and self.matrix[y + 1][x + 1] == '|' \
                        and self.matrix[y + 1][x - 1] == '|':
                    # print("HERE1")
                    toReturnSum += self.matrix[y + 1][x]
            if y > 0:
                if self.matrix[y - 2][x] == '-' \
                        and self.matrix[y - 1][x + 1] == '|' \
                        and self.matrix[y - 1][x - 1] == '|':
                    # print("HERE2")
                    toReturnSum += self.matrix[y - 1][x]

        else:
            self.matrix[y][x] = '|'
            if x < self.actualXArraySize - 1:
                if self.matrix[y][x + 2] == '|' \
                        and self.matrix[y + 1][x + 1] == '-' \
                        and self.matrix[y - 1][x + 1] == '-':
                    # print("HERE3")
                    toReturnSum += self.matrix[y][x + 1]
            if x > 0:
                if self.matrix[y][x - 2] == '|' \
                        and self.matrix[y + 1][x - 1] == '-' \
                        and self.matrix[y - 1][x - 1] == '-':
                    # print("HERE4")
                    toReturnSum += self.matrix[y][x - 1]
        # print("Returning : " + str(toReturnSum))
        return toReturnSum

class DotsAndBoxes:
    def __init__(self, boardSizeX, boardSizeY, ply):
        gamestate = GameState([], boardSizeX, boardSizeY)
        gamestate.initializeMatrix()
        self.currentStateNode = Node(gamestate)
        self.ply = ply
        self.Score = 0

    def playerMove(self):
        self.currentStateNode.printBoard()

        playerMoveX = int(input("PlayerMove X: "))
        playerMoveY = int(input("PlayerMove Y: "))
        if (playerMoveX, playerMoveY) not in self.currentStateNode.children:
            self.currentStateNode.createChild(playerMoveX, playerMoveY, False)
            self.currentStateNode = self.currentStateNode.children[(playerMoveX, playerMoveY)]
        else:
            self.currentStateNode = self.currentStateNode.children[(playerMoveX, playerMoveY)]

        print("Current Score is now: " + str(self.currentStateNode.currentStateScore))

        self.AIMove()


    def AIMove(self):
        self.currentStateNode.printBoard()

        move = Algorithm.miniMax(self.currentStateNode, self.ply)

        self.currentStateNode = self.currentStateNode.children[(move[0], move[1])]

        print("AI Played X:" + str(move[0]) + " Y:" + str(move[1]))

        print("Current Score is now: " + str(self.currentStateNode.currentStateScore))

        if len(self.currentStateNode.children) == 0:
            self.currentStateNode.printBoard()
            self.evaluateGame()
            return

        self.playerMove()

    def evaluateGame(self):
        print("Game has finished")
        if self.currentStateNode.currentStateScore > 0:
            print("Player Has Won!")
        elif self.currentStateNode.currentStateScore < 0:
            print("AI Has Won!")
        else:
            print("TIE!")

    def start(self):
        self.playerMove()

class Algorithm:
    def miniMax(currentStateNode, ply):

        for i in range(currentStateNode.currentState.actualYArraySize):
            for j in range(currentStateNode.currentState.actualXArraySize):
                if currentStateNode.currentState.matrix[i][j] == ' ' and (j, i) not in currentStateNode.children:
                    currentStateNode.createChild(j, i, True)
                    if ply < 2:
                        return (i, j)

        minScore = 9999
        x = 0
        y = 0
        for key, child in currentStateNode.children.items():
            currentMaxMin = Algorithm.Max(child, ply - 1, minScore)
            # print("currentMaxMins: " + str(currentMaxMin))
            if minScore > currentMaxMin:
                minScore = currentMaxMin
                x = key[0]
                y = key[1]

        # print("MiniMax returning: " + str(minScore) + " | X: " + str(x) + " Y: " + str(y))
        return (x, y)


    def Max(currentStateNode, ply, alphaScore):
        if ply == 0:
            return currentStateNode.currentStateScore

        for i in range(currentStateNode.currentState.actualYArraySize):
            for j in range(currentStateNode.currentState.actualXArraySize):
                if currentStateNode.currentState.matrix[i][j] == ' ' and (j, i) not in currentStateNode.children:
                    # print("testing: " + " | X: " + str(j) + " Y: " + str(i))
                    currentStateNode.createChild(j, i, False)
                    # print("^ points: " + str(currentStateNode.children[(j, i)].currentStateScore))

        maxScore = -9999
        x = 0
        y = 0
        for key, child in currentStateNode.children.items():
            currentMaxMin = Algorithm.Min(child, ply - 1, maxScore)
            if maxScore < currentMaxMin:
                maxScore = currentMaxMin
            if currentMaxMin > alphaScore:
                # print("Skipped Min Tree")
                return currentMaxMin

        # print("Max returning: " + str(maxScore) + " | X: " + str(x) + " Y: " + str(y))
        return maxScore


    def Min(currentStateNode, ply, betaScore):
        if ply == 0:
            return currentStateNode.currentStateScore

        for i in range(currentStateNode.currentState.actualYArraySize):
            for j in range(currentStateNode.currentState.actualXArraySize):
                if currentStateNode.currentState.matrix[i][j] == ' ' and (j, i) not in currentStateNode.children:
                    currentStateNode.createChild(j, i, True)

        minScore = 9999
        x = 0
        y = 0
        for key, child in currentStateNode.children.items():
            currentMaxMin = Algorithm.Max(child, ply - 1, minScore)
            if minScore > currentMaxMin:
                minScore = currentMaxMin
            if currentMaxMin < betaScore:
                # print("Skipped Max Tree")
                return currentMaxMin

        # print("Min returning: " + str(minScore) + " | X: " + str(x) + " Y: " + str(y))
        return minScore




def main():
    while True:
        ply = int(input("# of Ply's per  for this game: "))

        if ply == -1:
            exit()


        boardSizeX = int(input("Board size X: ")) * 2 + 1

        if boardSizeX == -1:
            exit()

        boardSizeY = int(input("Board size Y: ")) * 2 + 1

        if boardSizeY == -1:
            exit()

        dotsandboxes = DotsAndBoxes(boardSizeX, boardSizeY, ply)
        dotsandboxes.start()

if __name__ == "__main__":
    main()

