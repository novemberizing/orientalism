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
            content: o.content
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

    static gen(meta, styles, scripts, generator) {
        const twitter = Twitter.from(meta);
        const opengraph = Opengraph.from(meta);

        const html = `<!DOCTYPE html>
<html lang="${meta.lang}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="${meta.description}">
        <meta name="author" content="${meta.author}">
        <meta name="generator" content="${meta.generator}">
        ${twitter.html()}
        ${opengraph.html()}
        ${Website.styles(styles)}
    </head>
    ${generator.body(meta)}
</html>
`;
        return html;
    }
}

// Website.__publicPath = 'http://localhost/orientalism';
