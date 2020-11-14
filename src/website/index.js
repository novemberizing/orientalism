/**
 * 
 * @author  novemberizing <novemberizing@gmail.com>
 * @version 0.0.1
 */

import Opengraph from "./meta/opengraph";
import Twitter from "./meta/twitter";

export default class Website {
    static __publicPath = "http://localhost/orientalism";

    static get publicPath() {
        return Website.__publicPath;
    }

    static set publicPath(o) {
        Website.__publicPath = o;
    }

    static meta(o, twitter, opengraph) {
        return {
            sitename: o.sitename,
            url: `${Website.publicPath}/${o.book}/${o.category}/${o.section}.html`,
            lang: o.lang,
            description: o.description,
            author: o.author,
            generator: 'novemberizing static website generator version 0.0.1',
            locale: o.locale,
            title: `${o.book}/${o.category}/${o.section}`,
            twitter: {
                type: Twitter.SummaryLargeImageCard,
                username: twitter.username,
                url: `${Website.publicPath}/${o.book}/${o.category}/${o.section}.png`,
                alt: `${o.book}/${o.category}/${o.section}`
            },
            opengraph: {
                image: {
                    url: `${Website.publicPath}/${o.book}/${o.category}/${o.section}.png`
                }
            },
            book: o.book,
            category: o.category,
            section: o.section,
            prefix: o.prefix,
            content: o.content,
            sections: o.sections
        };
    }

    static styles(objects) {
        return objects.map(o => {
            const attributes = {
                rel: "stylesheet",
                href: o.href,
                integrity: o.integrity,
                crossorigin: o.crossorigin
            };

            const tags = Object.keys(attributes)
                               .filter(o => (attributes[o]))
                               .map(o => `${o}="${attributes[o]}"`)
                               .join(' ');

            return `        <link ${tags}></link>`;
        }).join('\n').trim();
    }

    static gen(meta, styles, scripts, body) {
        const twitter = Twitter.from(meta);
        const opengraph = Opengraph.from(meta);

        let i = 0;
        for(; i < meta.sections.length; i++) {
            if(meta.sections[i].section === meta.section) {
                break;
            }
        }

        i = (i + 1 === meta.sections.length ? 0 : i +1);

        const next = {
            book: meta.book,
            category: meta.category,
            section: meta.sections[i].section
        }

        const html = `<!DOCTYPE html>
<html lang="${meta.lang}">
    <head>
        <meta charset="utf-8">
        <title>${meta.book}/${meta.category} - ${meta.section}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link rel="apple-touch-icon" sizes="57x57" href="/apple-icon-57x57.png">
        <link rel="apple-touch-icon" sizes="60x60" href="/apple-icon-60x60.png">
        <link rel="apple-touch-icon" sizes="72x72" href="/apple-icon-72x72.png">
        <link rel="apple-touch-icon" sizes="76x76" href="/apple-icon-76x76.png">
        <link rel="apple-touch-icon" sizes="114x114" href="/apple-icon-114x114.png">
        <link rel="apple-touch-icon" sizes="120x120" href="/apple-icon-120x120.png">
        <link rel="apple-touch-icon" sizes="144x144" href="/apple-icon-144x144.png">
        <link rel="apple-touch-icon" sizes="152x152" href="/apple-icon-152x152.png">
        <link rel="apple-touch-icon" sizes="180x180" href="/apple-icon-180x180.png">
        <link rel="icon" type="image/png" sizes="192x192"  href="/android-icon-192x192.png">
        <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="96x96" href="/favicon-96x96.png">
        <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
        <link rel="manifest" href="/manifest.json">
        <meta name="msapplication-TileColor" content="#ffffff">
        <meta name="msapplication-TileImage" content="/ms-icon-144x144.png">
        <meta name="theme-color" content="#ffffff">
        <meta name="description" content="${meta.description}">
        <meta name="author" content="${meta.author}">
        <meta name="generator" content="${meta.generator}">
        ${twitter.html()}
        ${opengraph.html()}
        ${Website.styles(styles)}
        <script async src="https://www.googletagmanager.com/gtag/js?id=G-4W2KXXRVN7"></script>
        <script>
            window.dataLayer = window.dataLayer || [];
            function gtag(){dataLayer.push(arguments);}
            gtag('js', new Date());
            gtag('config', 'G-4W2KXXRVN7');
        </script>
        <script>
            var playid = null;
            function orientalismPlay() {
                if(document.getElementById('orientalism-play-btn')) {
                    document.getElementById('orientalism-play-btn').removeEventListener('click', orientalismPlay);
                    document.getElementById('orientalism-play-btn').className = 'text-success';
                }
                playid = window.setTimeout(function(){
                    location.href="/orientalism/${next.book}/${next.category}/${next.section}.html?play=on";
                }, 15000);
                if(document.getElementById('orientalism-play-btn')) {
                    document.getElementById('orientalism-play-btn').addEventListener('click', orientalismStop);
                }
            }
            function orientalismStop() {
                document.getElementById('orientalism-play-btn').removeEventListener('click', orientalismStop);
                if(playid) {
                    window.clearTimeout(playid);
                    playid = null;
                    document.getElementById('orientalism-play-btn').className = 'text-secondary';
                    document.getElementById('orientalism-play-btn').addEventListener('click', orientalismPlay);
                }
            }
            const params = new URLSearchParams(window.location.search);
            const status = {
                play: params.get('play') || 'off'
            };
            console.log(status);
            if(status.play === 'on') {
                orientalismPlay();
            }
        </script>
    </head>
    ${body}
</html>
`;
        return html;
    }
}

