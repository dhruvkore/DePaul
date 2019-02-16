# Dhruv Kore
# CSC 575 Loop
# HW#4


import sys
import csv
import math
import re
import nltk
import operator
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.corpus import stopwords

termIndexfile = 'medline_term_index.csv'
invertedIndexFile = 'medline_inverted_index.csv'
docIndexFile = 'medline_doc_index.csv'

N = 1033

H_invertedIndex = {}
DL_doclength = {}

tid_to_term = {}

fileIn = open(termIndexfile, 'r', encoding='utf-8')
reader = csv.reader(fileIn, delimiter='\t')
for line in reader:
    term = line[0]
    termID = line[1]
    df = int(line[2])
    idf = math.log10(N/df)
    H_invertedIndex[term] = (idf, dict())
    tid_to_term[termID] = term
    #print("Still Working " + line[1])
fileIn.close()


fileIn = open(invertedIndexFile, 'r')
reader = csv.reader(fileIn, delimiter='\t')
for line in reader:
    termID = line[0]
    idx = 1
    while idx < (len(line) - 1):
        docID = line[idx]
        tf = int(line[idx+1])
        L = (H_invertedIndex[tid_to_term[termID]])[1]
        L[docID] = tf

        tfidf = tf * (H_invertedIndex[tid_to_term[termID]])[0]
        tfidfSquare = math.pow(tfidf, 2.0)
        if docID in DL_doclength:
            DL_doclength[docID] += tfidfSquare
        else:
            DL_doclength[docID] = tfidfSquare
        idx += 2

fileIn.close()


for docID in DL_doclength.keys():
    val = DL_doclength[docID]
    DL_doclength[docID] = math.sqrt(val)
    #print("DocID" + str(docID))


# print ('Total # terms: %d' % len(H_invertedIndex))
# for term in ['pentobarbit', 'defici', 'treatment']:
#     print (' - Entry for \'%s\': df=%s, idf=%s' % (term, len(H_invertedIndex[term][1]), H_invertedIndex[term][0]))

# print ('\nTotal # documents: %d' % len(DL_doclength))
# for docID in ['59', '1033']:
#     print (' - Vector len for Doc %s = %s' % (docID, DL_doclength[docID]))



queryFile = 'medline.query'

Queries_List = []

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
        Queries_List.append((queryID, dict(fdist)))

fileIn.close()

# print ('Total # queries: %d' % len(Queries_List))
# for qid in [1, 21]:
#     print (' - Query %s: %s' % (Queries_List[qid][0], Queries_List[qid][1]))

### Relavent file parse

relaventDocsFile = 'medline.rel'

relaventDocs = {}

fileIn = open(relaventDocsFile, 'r')
reader = csv.reader(fileIn, delimiter=' ')
for line in reader:
    relaventDocID = line[0]
    relaventDocs[relaventDocID] = set()

    index = 1
    while index < (len(line) - 1):
        relaventDocs[relaventDocID].add(line[index])
        index += 1


averagePrecisions = {}


### Step 3 Compute Cosine

rankedListOutput = open("rankedlist.txt", "w")

for qID, queryDist in Queries_List:
    R = {}

    squaresOfWeights = 0
    for term in queryDist:
        #print("term is : " + term)

        if term in H_invertedIndex:
            I = H_invertedIndex[term][0] #idf of the term
            K = queryDist[term]
            W = K * I # tf * idf of T in Q

            squaresOfWeights += math.pow(W, 2.0) # W^2

            L = H_invertedIndex[term][1] # posting list of T from H
            for D, C in L.items(): # pairs O
                if D not in R:
                    R[D] = 0.0
                R[D] += W * float(C) * I # tf * idf for T in Q x tf * idf for T in D

    QVectorLength = math.sqrt(squaresOfWeights)
    for D in R:
        S = R[D]
        Y = DL_doclength[D]
        R[D] = R[D] / (QVectorLength * Y)

    sortedR = sorted(R.items(), key=operator.itemgetter(1), reverse=True)


    outputLine = str(qID)
    for doc in sortedR:
        outputLine += " " + str(doc[0])
    outputLine += "\n"

    rankedListOutput.write(outputLine)

    ### Calculating Average Precision

    currentPrecisionCount = 0
    precisionDenominator = 0
    relavent = 0
    for docID, score in sortedR:
        precisionDenominator += 1
        if docID in relaventDocs[qID]:
            relavent += 1
            currentPrecisionCount += ( relavent / precisionDenominator )

    print("qID: " + str(qID) + " Average Precision: " + str(currentPrecisionCount / relavent))
    averagePrecisions[qID] = currentPrecisionCount / relavent

### Calculating Max Average Precision

map = 0
for q, avgprecision in averagePrecisions.items():
    map += avgprecision

map /= len(averagePrecisions)

print("Mean Average Precision: " + str(map))


