import Mustache from 'mustache';
import fs from 'fs';
import path from 'path';

import Website from './website';
import TemplateIndex from './templates';
import TemplateBook from './templates/book';

export default class Orientalism {
    static all(root, output) {
        let files = [];
        let books = [];
        let categories = new Map();
        fs.readdirSync(root).forEach(o => {
            const book = o;
            books.push(book);
            let objects = [];
            categories.set(book, objects);
            fs.readdirSync(root + '/' + book).forEach(o => {
                if(path.extname(o) === '.json') {
                    const category = o.substr(0, o.length - 5);
                    objects.push(category);
                    const sources = JSON.parse(fs.readFileSync(root + '/' + book + '/' + o));
                    const stat = fs.statSync(root + '/' + book + '/' + o);
                    files.push({book, category, sources, date: stat.mtime});
                }
            });
        });
        if(files.length > 0) {
            const first = files[0];
            const book = first.book;
            const category = first.category;
            const sources = first.sources;

            if(sources.length > 0) {
                const o = sources[sources.length - 1];

                const section = o.content.split(/\s+/)[0];
                const source = Object.assign(o, {book, category, section});
                const destination = `${output}/index`;
                Orientalism.index(source, destination + '.html', books);
            }
        }
        files.sort((x, y) => x.date >= y.date)
             .forEach(o => {
                const book = o.book;
                const category = o.category;
                const sources = o.sources;
                if(sources.length > 0) {
                    const o = sources[sources.length - 1];
                    const section = o.content.split(/\s+/)[0];
                    const source = Object.assign(o, {book, category, section});
                    const destination = `${output}/${book}/index`;
                    Orientalism.book(source, destination + '.html', categories.get(book));
                }
                sources.forEach(o => {
                    const section = o.content.split(/\s+/)[0];
                    const source = Object.assign(o, {book, category, section});
                    const destination = `${output}/${book}/${category}/${section}`;
                    Orientalism.gen(source, destination + '.html');

                    fs.mkdirSync(path.dirname(path.resolve(destination + '.json')), {recursive: true});
                    fs.writeFileSync(destination + '.json', JSON.stringify(source));
                });
             });
    }

    static gen(source, output) {
        const html = Orientalism.html(source, Orientalism);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output, html);

        return html;
    }

    static index(source, output, params) {
        const html = Orientalism.html(source, TemplateIndex, params);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output, html);

        return html;
    }

    static book(source, output, params) {
        const html = Orientalism.html(source, TemplateBook, params);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output, html);

        return html;
    }

    static html(o, func, params) {
        const input = Object.assign(JSON.parse(fs.readFileSync("./.conf/global.json")), o);
        const twitter = JSON.parse(fs.readFileSync("./.conf/twitter.json"));
        const opengraph = JSON.parse(fs.readFileSync("./.conf/opengraph.json"));
        const scripts = JSON.parse(fs.readFileSync("./.conf/scripts.json"));
        const styles = JSON.parse(fs.readFileSync("./.conf/styles.json"));

        if(input.publicPath) {
            Website.publicPath = input.publicPath;
        }
        
        return Website.gen(Website.meta(input, twitter, opengraph), styles, scripts, func, params);
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
                                    <i class="fab fa-heart fa-lg fa-fw"></i>
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
