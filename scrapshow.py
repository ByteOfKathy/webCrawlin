import requests
res = requests.get('https://automatetheboringstuff.com/files/rj.txt')
try:
    res.raise_for_status()
except Exception as e:
    print(e)

print(res.text[:250])