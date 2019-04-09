# Dhruv Kore
# CSC 575 Loop
# Web Crawler and Search Engine

import nltk

from nltk.tokenize import word_tokenize, wordpunct_tokenize
from nltk.corpus import stopwords


class invertedIndex:
    def __init__(self):
        self.termIndex = {}
        self.termID = 1
        self.docIndex = {}
        self.docID = 1
        self.invertedIndex = {}

    def addToInvertedIndex(self, url, text):
        self.docIndex[self.docID] = url

        low = text.lower()
        tokens = nltk.tokenize.word_tokenize(low)  # handles word tokenization and case folding
        tokens = [w for w in tokens if w not in stopwords.words('english')]  # handles stopwords
        tokens = [w for w in tokens if w.isalpha()]  # revmoves non-alpha

        p = nltk.PorterStemmer()
        tokens = [p.stem(t) for t in tokens]  # applies porter stemmer

        # Count Each Token
        for t in tokens:
            currentTermID = self.termID
            # Term Index
            if t in self.termIndex:
                self.termIndex[t] = (self.termIndex[t][0], self.termIndex[t][1] + 1)
                currentTermID = self.termIndex[t][0]
            else:
                self.termIndex[t] = (self.termID, 1)
                self.termID = self.termID + 1

            # Inverted Index
            # term is in index
            if currentTermID in self.invertedIndex:
                # increment or initialize DocID to Frequency for term
                if self.docID in self.invertedIndex[currentTermID]:
                    self.invertedIndex[currentTermID][self.docID] = self.invertedIndex[currentTermID][self.docID] + 1
                else:
                    self.invertedIndex[currentTermID][self.docID] = 1
            # term not in index
            else:
                self.invertedIndex[currentTermID] = {self.docID: 1}

        self.docID = self.docID + 1

    def writeTermIndexFile(self):
        # Term Index Alphabetically to File
        with open('term_index.csv', 'w', newline='', encoding='utf-8') as termIndexFile:
            for key in sorted(self.termIndex):
                docFrequency = len(self.invertedIndex[self.termIndex[key][0]].keys())  # Number of docs that this term is in
                termIndexFile.write(str(key) + "\t" + str(self.termIndex[key][0]) + "\t" + str(docFrequency) + "\n")

    def writeDocIndexFile(self):
        # Doc Index To File
        with open('doc_index.csv', 'w', newline='', encoding='utf-8') as docIndexFile:
            for key in sorted(self.docIndex):
                docIndexFile.write(str(key) + "\t" + str(self.docIndex[key]))
                docIndexFile.write("\n")


    def writeInvertedIndexFile(self):
        # Inverted Index To File
        with open('inverted_index.csv', 'w', newline='', encoding='utf-8') as invertedIndexFile:
            for key in sorted(self.invertedIndex):
                invertedIndexFile.write(str(key))
                for docKey in self.invertedIndex[key]:
                    invertedIndexFile.write("\t" + str(docKey) + "\t" + str(self.invertedIndex[key][docKey]))

                invertedIndexFile.write("\n")



