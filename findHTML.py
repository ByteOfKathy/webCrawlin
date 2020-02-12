import bs4

myfile = open('example.html', 'r')
mysoup = bs4.BeautifulSoup(myfile.read(), 'html.parser')
elems = mysoup.select('#reviews')
print(elems[0].getText())