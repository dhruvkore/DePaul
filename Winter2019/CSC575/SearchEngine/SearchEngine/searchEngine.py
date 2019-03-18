import csv
import sys
import csv
import math
import re
import nltk
import operator
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
            idf = math.log10(self.N / df) #idf
            self.H_invertedIndex[term] = (idf, dict())
            self.tid_to_term[termID] = term
            # print("Still Working " + line[1])
        fileIn.close()


        # Read in Inverted Index File
        fileIn = open(invertedIndexFile, 'r')
        reader = csv.reader(fileIn, delimiter='\t')
        for line in reader:
            termID = line[0]
            idx = 1
            while idx < (len(line) - 1):
                docID = line[idx]
                tf = int(line[idx+1])
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





def main():
    sEngine = searchEngine('../term_index.csv', '../inverted_index.csv', '../doc_index.csv', '../queries.csv')



if __name__ == "__main__":
	main()






















