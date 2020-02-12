import requests, sys, webbrowser, bs4, pyperclip, os
from pathlib import Path

firefox_path = os.path.join(str(Path.home()) + '/usr/bin/firefox')
webbrowser.register('firefox', None, webbrowser.BackgroundBrowser(firefox_path), 1)
firefox = webbrowser.get('firefox')

if len(sys.argv) > 1:
    query = '+'.join(sys.argv[1:])
else:
    query = pyperclip.paste()
# res = requests.get('https://duckduckgo.com/?q=' + query.strip().replace(' ','+'))
res = requests.get('https://google.com/?q=' + query.strip().replace(' ','+'))
res.raise_for_status()

mysoup =  bs4.BeautifulSoup('res.text', 'html.parser')
# links = mysoup.select('.result__body links_main links_deep')
links = mysoup.select('.package-snippet')

openLinks = min(10, len(links))
for i in range(openLinks):
    openlink = 'https://pypi.org' + links[i].get('href')
    firefox.open_new_tab(openlink, new=0, autoraise=True)