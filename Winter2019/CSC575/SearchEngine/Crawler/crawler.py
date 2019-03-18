from urllib import request
from bs4 import BeautifulSoup
from urllib.parse import urlparse

from queue import *
from Crawler.crawler_HTMLParser import crawlerHTMLParser

from Crawler.invertedIndex import invertedIndex

import re


class crawler:
	def __init__(self, site, limit):
		self.site = site
		self.limit = limit
		self.invertedIndex = invertedIndex()

	def crawl(self):
		numOfSites = 1
		queue = Queue()
		queue.put(self.site)
		visited = set()

		while not queue.empty() and numOfSites <= self.limit:
			website = queue.get()
			if website in visited:
				continue

			try:
				response = request.urlopen(website)
				page = response.read().decode('utf8')

				htmlstring = BeautifulSoup(page, 'html.parser')
				raw = htmlstring.get_text()

				data = htmlstring.find_all(text=True)

				result = filter(self.visible, data)
				cleaned = [str(item) for item in result]
				resultstring = ''.join(cleaned)
				# print(resultstring)
				print("Cleaned WebPage")

				self.invertedIndex.addToInvertedIndex(website, resultstring)

				myparser = crawlerHTMLParser()
				myparser.feed(str(htmlstring))

				visited.add(website)

				# searchDomain = urlparse(self.site).hostname
				# print("Search Domain: " + searchDomain)

				for link in myparser.links:
					# if link.startswith(self.site) and self.isValidHtmlPage(link):
					# searchDomain = urlparse(link).hostname
					# print("Search Domain: " + str(searchDomain))
					if not link.startswith("/") and "depaul.edu" in str(urlparse(link).hostname) and self.isValidHtmlPage(link): # DePaul domain constrain required in prompt
						queue.put(link)
						# print(str(numOfSites) + ": " + link)
					elif not link.startswith("http") and self.isValidHtmlPage(link): # if linking to internal website without explicit domain name
						queue.put(self.site + link)
						# print(str(numOfSites) + ": " + self.site[:-1] + link)
					# print(link)
				print(str(numOfSites) + ": " + website)

				numOfSites += 1
			except: # if domain redirects or is invalid, rejects domain name
				print("Exception on:" + website)
				visited.add(website)

		print("Number of Sites: " + str(numOfSites))
		print("Queue Amount: " + str(queue.qsize()))


		# Write Inverted Index to file
		self.invertedIndex.writeTermIndexFile()
		self.invertedIndex.writeDocIndexFile()
		self.invertedIndex.writeInvertedIndexFile()

		print("Done writing to file.")

	def isValidHtmlPage(self, link):
		if (link.endswith(".aspx") or
				link.endswith(".html") or
				link.endswith(".htm") or
				link.endswith(".shtml") or
				link.endswith(".asp") or
				link.endswith("/") or
				link.endswith(".php")
		):
			return True
		else:
			return False


	def visible(self, element):
		if element.parent.name in ['style', 'script', '[document]', 'head', 'title']:
			return False
		elif re.match('<!--.*-->', str(element.encode('utf-8'))):
			return False
		return True

def main():
	c = crawler('https://www.depaul.edu', 5000)
	c.crawl()

if __name__ == "__main__":
	main()
