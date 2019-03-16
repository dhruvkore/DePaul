from urllib import request
from bs4 import BeautifulSoup

import re


class crawler:
	def __init__(self, site):
		self.site = site

	def crawl(self):
		response = request.urlopen(self.site)
		page = response.read().decode('utf8')

		htmlstring = BeautifulSoup(page, 'html.parser')
		raw = htmlstring.get_text()

		data = htmlstring.find_all(text=True)

		result = filter(self.visible, data)
		cleaned = [str(item) for item in result]
		resultstring = ''.join(cleaned)
		print(resultstring)



	def visible(self, element):
		if element.parent.name in ['style', 'script', '[document]', 'head', 'title']:
			return False
		elif re.match('<!--.*-->', str(element.encode('utf-8'))):
			return False
		return True

def main():
	c = crawler('https://www.depaul.edu')
	c.crawl()

if __name__ == "__main__":
	main()
