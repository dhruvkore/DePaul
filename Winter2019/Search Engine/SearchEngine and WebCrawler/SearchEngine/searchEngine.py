# Dhruv Kore
# CSC 575 Loop
# Web Crawler and Search Engine

import csv
import sys
import csv
import math
import re
import operator
import nltk

from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.corpus import stopwords

class searchEngine:
    def __init__(self, termIndexfile, invertedIndexFile, docIndexFile, queryFile):

        self.N = sum(1 for line in open(docIndexFile)) # Gets total number of documents

        self.H_invertedIndex = {}
        self.DL_doclength = {}

        self.tid_to_term = {}

        self.DocIndex = {}

        self.Queries_List = []

        self.readInFiles(termIndexfile, invertedIndexFile, docIndexFile, queryFile)

    def readInFiles(self, termIndexfile, invertedIndexFile, docIndexFile, queryFile):
        # For calculations uses Uses LTC.LTN

        # Read in Term Index File
        fileIn = open(termIndexfile, 'r', encoding='utf-8')
        reader = csv.reader(fileIn, delimiter='\t')
        for line in reader:
            print(line)
            term = line[0]
            termID = line[1]
            df = int(line[2])
            idf = math.log10(self.N / df)                   # IDF = log(N / df) since T in LTC
            self.H_invertedIndex[term] = (idf, dict())
            self.tid_to_term[termID] = term
            # print("Still Working " + line[1])
        fileIn.close()

        # Uses LTC
        # Read in Inverted Index File
        fileIn = open(invertedIndexFile, 'r')
        reader = csv.reader(fileIn, delimiter='\t')
        for line in reader:
            termID = line[0]
            idx = 1
            while idx < (len(line) - 1):
                docID = line[idx]
                tf = 1 + math.log10(int(line[idx+1]))        # TF = 1 + log(tf) since L in LTC
                L = (self.H_invertedIndex[self.tid_to_term[termID]])[1]
                L[docID] = tf

                tfidf = tf * (self.H_invertedIndex[self.tid_to_term[termID]])[0]
                tfidfSquare = math.pow(tfidf, 2.0)
                if docID in self.DL_doclength:
                    self.DL_doclength[docID] += tfidfSquare
                else:
                    self.DL_doclength[docID] = tfidfSquare
                idx += 2
        fileIn.close()

        for docID in self.DL_doclength.keys():
            val = self.DL_doclength[docID]
            self.DL_doclength[docID] = math.sqrt(val)


        # Read in Doc Index File
        fileIn = open(docIndexFile, 'r')
        reader = csv.reader(fileIn, delimiter='\t')
        for line in reader:
            self.DocIndex[line[0]] = line[1]
        fileIn.close()


        # Read In Queries
        fileIn = open(queryFile, 'r', encoding='utf-8')
        porter = nltk.PorterStemmer()

        for line in fileIn:
            matchObject = re.match(r'^(\d+)\s+(.*)', line)
            if not matchObject:
                print("ERROR with line: %s" % line)
            else:
                queryID = matchObject.group(1)
                text = matchObject.group(2)

                tokens = word_tokenize(text.lower())
                terms = [porter.stem(w) for w in tokens if w not in stopwords.words('english') and len(w) > 1]
                fdist = nltk.FreqDist(terms)
                self.Queries_List.append((queryID, dict(fdist)))
        fileIn.close()

    def computeWeighting(self):
        rankedListOutput = open("../rankedlist.txt", "w")

        for qID, queryDist in self.Queries_List:
            R = {}

            # squaresOfWeights = 0
            for term in queryDist:
                # print("term is : " + term)

                if term in self.H_invertedIndex:
                    I = self.H_invertedIndex[term][0]   # idf of the term
                    K = 1 + math.log10(queryDist[term]) # TF = 1 + log(tf) since L in LTN for Query
                    W = K * I  # tf * idf of T in Q

                    # squaresOfWeights += math.pow(W, 2.0)  # W^2 # Not needed in LTC.LTN

                    L = self.H_invertedIndex[term][1]  # posting list of T from H
                    for D, C in L.items():  # pairs O
                        if D not in R:
                            R[D] = 0.0
                        R[D] += W * float(C) * I  # tf * idf for T in Q x tf * idf for T in D

            # QVectorLength = math.sqrt(squaresOfWeights) # Not needed in Implementation
            for D in R:
                S = R[D]
                Y = self.DL_doclength[D]
                # R[D] = R[D] / (QVectorLength * Y)
                R[D] = R[D] / (Y) # Normalization on Y since LTC.LTN where N = none = 1

            sortedR = sorted(R.items(), key=operator.itemgetter(1), reverse=True)

            outputLine = str(qID)
            rankedListOutput.write(str(qID) + " Query: " + str(queryDist) + "\n")
            rankedListOutput.write("---\n")
            resultNum = 1
            for doc in sortedR:
                if resultNum > 10:
                    break
                rankedListOutput.write(str(resultNum) + " - DocID: " + str(doc[0]) + " Similarity: " + str(doc[1]) + " Link: " + self.DocIndex[doc[0]] + "\n")
                resultNum += 1
            rankedListOutput.write("\n")

        rankedListOutput.close()




def main():
    sEngine = searchEngine('../term_index.csv', '../inverted_index.csv', '../doc_index.csv', '../queries.csv')
    sEngine.computeWeighting()



if __name__ == "__main__":
	main()






















