# Dhruv Kore
# CSC 575 Loop
# Web Crawler and Search Engine

from html.parser import HTMLParser

class crawlerHTMLParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.links = []

    def handle_starttag(self, tag, attrs):
        if tag == 'a':
            for name, val in attrs:
                if name == 'href':
                    self.links.append(val)
                    break

