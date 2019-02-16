# Dhruv Kore
# CSC 480 Loop
# Assignment 2 - Dots and Boxes
from random import randint

class GameState:
    def __init__(self, sizeX, sizeY):
        self.actualXArraySize = (sizeX * 2) + 1
        self.actualYArraySize = (sizeY * 2) + 1

        self.matrix = []

    def __init__(self, matrix, actualXArraySize, actualYArraySize):
        self.matrix = matrix
        self.actualXArraySize = actualXArraySize
        self.actualYArraySize = actualYArraySize

    def initializeMatrix(self):
        for i in range (self.actualYArraySize):
            row = []
            for j in range (self.actualXArraySize):
                if i % 2 == 1 and j % 2 == 1: # if x and y are odd, will be rand number
                    row.append(randint(1,5))  # random points of 1 to 5
                elif i % 2 == 0 and j % 2 == 0:
                    row.append('*') # corners of boxes
                else:
                    row.append(' ') # places for player move
            self.matrix.append(row)

    def cloneMatrix(self):
        output = []
        for i in range(self.actualYArraySize):
            row = []
            for j in range(self.actualXArraySize):
                row.append(self.matrix[i][j])
            output.append(row)

        return output

    def cloneGameState(self):
        return GameState(self.cloneMatrix(), self.actualXArraySize, self.actualYArraySize)

    def makeMove(self, x, y):
        try:
            if self.matrix[y][x] != ' ':
                return -1

            toReturnSum = 0

            if y % 2 == 0 and x % 2 == 1:
                self.matrix[y][x] = '-'
                if y > 0:
                    if self.matrix[y + 2][x] != ' ' \
                            and self.matrix[y + 1][x + 1] != ' ' \
                            and self.matrix[y + 1][x - 1] != ' ':
                        toReturnSum += self.matrix[y + 1][x]
                if y < self.actualYArraySize - 1:
                    if self.matrix[y - 2][x] != ' ' \
                            and self.matrix[y - 1][x + 1] != ' ' \
                            and self.matrix[y - 1][x - 1] != ' ':
                        toReturnSum += self.matrix[y - 1][x]

            else:
                self.matrix[y][x] = '|'
                if x < self.actualXArraySize - 1:
                    if self.matrix[y][x + 2] != ' ' \
                            and self.matrix[y + 1][x + 1] != ' ' \
                            and self.matrix[y - 1][x + 1] != ' ':
                        toReturnSum += self.matrix[y][x + 1]
                if x > 0:
                    if self.matrix[y][x - 2] != ' ' \
                            and self.matrix[y + 1][x - 1] != ' ' \
                            and self.matrix[y - 1][x - 1] != ' ':
                        toReturnSum += self.matrix[y][x - 1]
            return toReturnSum
        except:
            return -1



