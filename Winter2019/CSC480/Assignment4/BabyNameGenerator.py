from string import ascii_uppercase

import numpy as np


class BabyNameGenerator:
    def __init__(self, gender, minNameLength, maxNameLength, orderOfModel, numOfNames):
        self.className = "Baby Generator"
        self.maleMarkovModel = {}
        self.femaleMarkovModel = {}
        self.boyNamesFile = "namesBoys.txt"
        self.girlNamesFile = "namesGirls.txt"

        self.gender = gender
        self.minNameLength = minNameLength
        self.maxNameLength = maxNameLength
        self.orderOfModel = orderOfModel
        self.numOfNames = numOfNames

        self.bufferIndexSize = -1 * (orderOfModel - 1)

        self.createMaleMarkovModel()
        self.createFemaleMarkovModel()

    def CreateMarkovModels(self):
        self.createMaleMarkovModel()
        self.createFeMaleMarkovModel()

    def createMaleMarkovModel(self):
        self.createMarkovModel(self.maleMarkovModel, self.boyNamesFile)

    def createFemaleMarkovModel(self):
        self.createMarkovModel(self.femaleMarkovModel, self.girlNamesFile)

    def createMarkovModel(self, markovModel, namesFile):
        with open(namesFile) as fileIn:
            for line in fileIn:
                previousStr = ""

                for i in range(orderOfModel):
                    previousStr += "_"
                currentline = line[:-1] + "__"

                for letter in currentline:
                    if previousStr not in markovModel.keys():
                        markovModel[previousStr] = letterProbabilty(1)
                    else:
                        markovModel[previousStr].probability += 1

                    if letter not in markovModel[previousStr].nextLetterAndProbability.keys():
                        markovModel[previousStr].nextLetterAndProbability[letter] = 1
                    else:
                        markovModel[previousStr].nextLetterAndProbability[letter] += 1

                    previousStr = previousStr[self.bufferIndexSize:] + letter
                    #print(previousStr)


    def generateName(self):
        if self.gender == "m":
            babynamegenerator.generateMaleName()
        else:
            babynamegenerator.generateFemaleName()

    def generateMaleName(self):
        return self.runNameGenerator(self.maleMarkovModel)

    def generateFemaleName(self):
        return self.runNameGenerator(self.femaleMarkovModel)

    def runNameGenerator(self, markovModel):
        for i in range(self.numOfNames):
            outputName = ''
            validName = False
            while not validName:
                outputName = ''
                nextLetter = ''
                previousStr = ''
                for i in range(orderOfModel):
                    previousStr += "_"

                while nextLetter != "_":
                    outputName = outputName + nextLetter
                    nextLetter = self.getNextLetter(markovModel, previousStr)
                    previousStr = previousStr[self.bufferIndexSize:] + nextLetter

                if len(outputName) >= minNameLength and len(outputName) <= maxNameLength:
                    validName = True


            print(str(i + 1) + ": " + outputName)

    def getNextLetter(self, markovModel, prevStr):
        totalOccurances = 0
        letters = []
        probs = []

        currentLetters = markovModel[prevStr].nextLetterAndProbability

        for key, val in currentLetters.items():
            totalOccurances += val

        for key, val in currentLetters.items():
            letters.append(key)
            probs.append(val / totalOccurances)

        chosenLetter = np.random.choice(letters, p=probs)
        # print("Chosen Next Letter: " + chosenLetter)

        return chosenLetter


class letterProbabilty:
    def __init__(self, probability):
        self.probability = probability
        self.nextLetterAndProbability = {}


if __name__ == '__main__':
    print("Baby Name Generator")

    while True:
        gender = ''
        minNameLength = 0
        maxNameLength = 0
        orderOfModel = 0
        numOfNames = 0

        while gender != "m" and gender != "f":
            gender = input("Enter 'm' for boy name or 'f' for girl name: ")
            if gender == "exit":
                exit()

        while minNameLength is not int and minNameLength < 1:
            minNameLength = int(input("Enter minimum name length: "))
            if minNameLength == -1:
                exit()

        while maxNameLength is not int and maxNameLength < 1:
            maxNameLength = int(input("Enter maximum name length. Greater than " + str(minNameLength) + ": "))
            if maxNameLength == -1:
                exit()

        while orderOfModel is not int and orderOfModel < 1:
            orderOfModel = int(input("Enter Order of Markov Model: "))
            if orderOfModel == -1:
                exit()

        while numOfNames is not int and numOfNames < 1:
            numOfNames = int(input("Enter number of names: "))
            if numOfNames == -1:
                exit()

        babynamegenerator = BabyNameGenerator(gender, minNameLength, maxNameLength, orderOfModel, numOfNames)

        babynamegenerator.generateName()
