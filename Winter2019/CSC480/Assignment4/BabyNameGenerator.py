from string import ascii_uppercase

import numpy as np


class BabyNameGenerator:
    def __init__(self, gender, minNameLength, maxNameLength, orderOfModel, numOfNames):
        self.className = "Baby Generator"
        self.maleMarkovModel = {}
        self.femaleMarkovModel = {}
        self.boyNamesFile = "namesBoys.txt"
        self.girlNamesFile = "namesGirls.txt"
        self.createMaleMarkovModel()
        self.createFemaleMarkovModel()

        self.gender = gender
        self.minNameLength = minNameLength
        self.maxNameLength = maxNameLength
        self.orderOfModel = orderOfModel
        self.numOfNames = numOfNames


    def generateName(self):
        print('Generate Name here')

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
                firstLetter = "__" + line[0]
                if firstLetter not in markovModel.keys():
                    markovModel[firstLetter] = letterProbabilty(1)
                else:
                    markovModel[firstLetter].probability += 1

                previousLetter = firstLetter

                for letter in line[1:-1]:
                    if letter not in markovModel.keys():
                        markovModel[letter] = letterProbabilty(1)
                    else:
                        markovModel[letter].probability += 1

                    if letter not in markovModel[previousLetter].nextLetterAndProbability.keys():
                        markovModel[previousLetter].nextLetterAndProbability[letter] = 1
                    else:
                        markovModel[previousLetter].nextLetterAndProbability[letter] += 1

                    previousLetter = letter

                if '__' not in markovModel[previousLetter].nextLetterAndProbability.keys():
                    markovModel[previousLetter].nextLetterAndProbability['__'] = 1
                else:
                    markovModel[previousLetter].nextLetterAndProbability['__'] += 1

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
                # First Letter
                previousLetter = self.getFirstLetter(markovModel)
                outputName = outputName + previousLetter[2]

                nextLetter = ''
                while nextLetter != "__":
                    outputName = outputName + nextLetter
                    nextLetter = self.getNextLetter(markovModel, previousLetter)
                    previousLetter = nextLetter

                if len(outputName) >= minNameLength and len(outputName) <= maxNameLength:
                    validName = True


            print("i: " + outputName)

    def getFirstLetter(self, markovModel):
        totalOccurances = 0
        letters = []
        probs = []

        for letter in ascii_uppercase:
            currentLetter = "__" + letter
            if currentLetter in markovModel.keys():
                totalOccurances += markovModel[currentLetter].probability

        for letter in ascii_uppercase:
            currentLetter = "__" + letter
            if currentLetter in markovModel.keys():
                letters.append("__" + letter)
                probs.append(markovModel["__" + letter].probability / totalOccurances)

        chosenLetter = np.random.choice(letters, p=probs)
        #print("Chosen First Letter: " + chosenLetter)

        return chosenLetter

    def getNextLetter(self, markovModel, prevLetter):
        totalOccurances = 0
        letters = []
        probs = []

        currentLetters = markovModel[prevLetter].nextLetterAndProbability

        for key, val in currentLetters.items():
            totalOccurances += val

        for key, val in currentLetters.items():
            letters.append(key)
            probs.append(val / totalOccurances)

        chosenLetter = np.random.choice(letters, p=probs)
        #print("Chosen Next Letter: " + chosenLetter)

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
            if minNameLength == "exit":
                exit()

        while maxNameLength is not int and maxNameLength < 1:
            maxNameLength = int(input("Enter maximum name length. Greater than " + str(minNameLength) + ": "))
            if maxNameLength == "exit":
                exit()

        while numOfNames is not int and numOfNames < 1:
            numOfNames = int(input("Enter number of names: "))
            if numOfNames == "exit":
                exit()

        babynamegenerator = BabyNameGenerator(gender, minNameLength, maxNameLength, orderOfModel, numOfNames)

        babynamegenerator.generateName()
