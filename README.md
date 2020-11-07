__[NOVEMBERIZING] ORIENTALISM__
===============================

동양사상에 대한 정보를 제공합니다.

[GITHUB PAGE] [REACT] 

```
npm install -g
orientalism [source] [destination]
docker run -it --rm -v ${PWD}/docs:/source novemberizing/orientalism-java ./gradlew run --args='"/source" "/source"'
```

개발 서버 확인 방법
```

```

https://ctext.org/analects/xue-er/

- [x] INDEX PAGE 를 어떻게 구현해야할까?
- [ ] BUTTON 의 활용방안

개발 서버 실행

```
docker run -it --rm -v ${PWD}/docs:/usr/local/apache2/htdocs -p 80:80 --name orientalism httpd
```