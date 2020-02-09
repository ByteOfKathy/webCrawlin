import requests, bs4

res = requests.get('https://nostarch.com')
res.raise_for_status()
soupy = bs4.BeautifulSoup(res.text, 'html.parser')
