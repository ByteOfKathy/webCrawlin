import requests, bs4

res = requests.get('https://nostarch.com/automatestuff2/')
res.raise_for_status()
soupy = bs4.BeautifulSoup(res.text, 'html.parser')

myfile = open('example.html', 'w')
myfile.write(res.text)
myfile.close()
