import requests, os, bs4, sys

url = 'https://xkcd.com'

if len(sys.argv) == 2:
    url = 'https://' + sys.argv[1]

if not os.path.isdir('./comics'):
    os.makedirs('comics', exist_ok=True)

res = requests.get(url)
res.raise_for_status()

while not url.endswith('#'):
    mysoup = bs4.BeautifulSoup(res.text, 'html.parser')
    comic = mysoup.select('#comic img')

    if comic == []:
        exit("comics downloaded")
    else:
        res = requests.get('https:' + comic[0].get('src'))
        res.raise_for_status()
        image = open(os.path.join('comics', os.path.basename(comic)))