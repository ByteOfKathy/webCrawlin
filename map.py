import webbrowser, sys, pyperclip

if len(sys.argv) > 1:
    address = ''.join(sys.argv[1:]).replace(' ', '+')
else:
    address = pyperclip.paste().replace(' ', '+')
webbrowser.open('https://www.google.com/maps/place/' + address)