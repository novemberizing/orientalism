import Mustache from 'mustache';
import fs from 'fs';
import path from 'path';

import Website from './website';

export default class Orientalism {
    static gen(source, output) {
        const html = Orientalism.html(JSON.parse(fs.readFileSync(source)));

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
                                    {{book}} / {{category}} / {{section}}
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

const source = './example.json';
const destination = './docs/論語/學而/學而時習之.html';

console.log("TODO: REMOVE THIS");
console.log(Orientalism.gen(source, destination));

