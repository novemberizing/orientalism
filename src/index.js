import Mustache from 'mustache';
import fs from 'fs';
import path from 'path';

import Website from './website';

export default class Orientalism {
    static all(root, output) {
        fs.readdirSync(root).forEach(o => {
            const book = o;
            fs.readdirSync(root + '/' + book).forEach(o => {
                if(path.extname(o) === '.json') {
                    const category = o.substr(0, o.length - 5);
                    const sources = JSON.parse(fs.readFileSync(root + '/' + book + '/' + o));
                    sources.forEach(o => {
                        const section = o.content.split(/\s+/)[0];
                        const source = Object.assign(o, {book, category, section});
                        const destination = `${output}/${book}/${category}/${section}`;
                        Orientalism.gen(source, destination + '.html');

                        fs.mkdirSync(path.dirname(path.resolve(destination + '.json')), {recursive: true});
                        fs.writeFileSync(destination + '.json', JSON.stringify(source));

                        console.log('run docker');
                    });
                }
            });
        });
    }

    static gen(source, output) {
        const html = Orientalism.html(source);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output, html);

        return html;
    }

    static html(o) {
        const input = Object.assign(JSON.parse(fs.readFileSync("./.conf/global.json")), o);
        const twitter = JSON.parse(fs.readFileSync("./.conf/twitter.json"));
        const opengraph = JSON.parse(fs.readFileSync("./.conf/opengraph.json"));
        const scripts = JSON.parse(fs.readFileSync("./.conf/scripts.json"));
        const styles = JSON.parse(fs.readFileSync("./.conf/styles.json"));

        if(input.publicPath) {
            Website.publicPath = input.publicPath;
        }
        
        return Website.gen(Website.meta(input, twitter, opengraph), styles, scripts, Orientalism);
    }

    static body(meta) {
        const data = {
            book: meta.book,
            category: meta.category,
            section: meta.section,
            prefix: meta.prefix,
            content: meta.content
        };

        return Mustache.render(`<body>
        <div id="root">
            <div class="all">
                <div class="row h-100 m-0 p-0 my-auto d-flex">
                    <div class="col-lg-8 col-md-12 px-0">
                        <div class="row">
                            <div class="col-2 pr-0">
                                <h6 class="text-chinese text-right">
                                    {{book}} / {{category}}
                                </h6>
                            </div>
                            <div class="col-10">
                            </div>
                        </div>
                        <hr class="row mt-0 mr-1" />
                        <div class="row">
                            <div class="col-2 pr-0">
                                <h4 class="text-chinese text-right">
                                    {{prefix}}
                                </h4>
                            </div>
                            <div class="col-10">
                                <h1 class="text-chinese">
                                    {{content}}
                                </h1>
                            </div>
                        </div>
                        <hr class="row mb-2 mr-1" />
                        <div class="row text-right">
                            <div class="col-12 text-right">
                                <a href="/orientalism" class="text-secondary">
                                    <i class="fab fa-twitter fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-4 mt-5 pr-5"></div>
                </div>
            </div>
        </div>
    </body>`, data);
    }
}
