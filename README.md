__[NOVEMBERIZING] ORIENTALISM__
===============================

동양사상에 대한 정보를 제공합니다.

[GITHUB PAGE] [REACT] 

```
npm install -g
orientalism [source] [destination]
docker run -it --rm -v ${PWD}/docs:/source novemberizing/orientalism-java ./gradlew run --args='"/source" "/source"'
```

## EXAMPLE

```
docker run -it --rm -v C:\Users\novemberizing\Desktop\Workspace\novemberizing\orientalism\docs:/source novemberizing/orientalism-java ./gradlew run --args='"/source" "/source"'
```


개발 서버 확인 방법
```

```

https://ctext.org/analects/xue-er/

- [x] INDEX PAGE 를 어떻게 구현해야할까?
- [x] BUTTON 의 활용방안

TWITTER: SEND TWITTER
GOOGLE: SEARCH USING GOOGLE
HEART: ??????

- [ ] HEART
- [ ] RANDOM PLAY
- [ ] PLAY BUTTON
- [ ] FAVICON
- [ ] CATEGORY BUTTON IN CONTENT VIEW
- [ ] BOOK BUTTON IN CATEGORY VIEW
- [x] GOOGLE ANALYTICS

개발 서버 실행

쿼리문자열을 다시 쿼리 문자열로 바꾸어야 하는구나.

```
docker run -it --rm -v ${PWD}/docs:/usr/local/apache2/htdocs -p 80:80 --name orientalism httpd
```