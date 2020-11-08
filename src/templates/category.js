import fs from 'fs';
import path from 'path';
import qs from 'querystring';
import urlencode from 'urlencode';
import Mustache from 'mustache';

import Website from '../website';

export default class TemplateCategory {
    static gen(o ,output) {
        const html = TemplateCategory.html(o);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output + '.html', html);
        fs.writeFileSync(output + '.json', JSON.stringify(o.categories));

        return html;
    }

    static html(o) {
        const input = Object.assign(JSON.parse(fs.readFileSync("./.conf/global.json")), o.source);
        const twitter = JSON.parse(fs.readFileSync("./.conf/twitter.json"));
        const opengraph = JSON.parse(fs.readFileSync("./.conf/opengraph.json"));
        const scripts = JSON.parse(fs.readFileSync("./.conf/scripts.json"));
        const styles = JSON.parse(fs.readFileSync("./.conf/styles.json"));

        if(input.publicPath) {
            Website.publicPath = input.publicPath;
        }

        const body = TemplateCategory.body(Object.assign(o, input, twitter, opengraph, scripts, styles));
        
        return Website.gen(Website.meta(input, twitter, opengraph), styles, scripts, body);
    }

    static tag(book, category, section, o) {
        const badge = category === o ? "btn btn-outline-success" : "btn btn-outline-secondary";
        return `<a href="/orientalism/${book}/${o}/" class="${badge} p0"><p class="font-weight-bold m0 p0 d-inline">${o}</p></a>`;
    }

    static body(meta) {
        const data = {
            book: meta.source.book,
            category: meta.source.category,
            section: meta.source.section,
            prefix: meta.source.prefix,
            content: meta.source.content,
            url: qs.stringify({url: meta.publicPath + '/' + urlencode(meta.source.book) + '/' + urlencode(meta.source.category) + '/' + urlencode(meta.source.section) + '.html'}),
            hashtags: qs.stringify({hashtags: meta.source.section}),
            search: qs.stringify({q: meta.source.content}),
            sections: meta.categories.map(o => TemplateCategory.tag(meta.source.book, meta.source.category, meta.source.section, o.category))
        };

        return Mustache.render(`<body>
        <div id="root">
            <div class="all">
                <div class="row h-100 w-100 m-0 p-0 my-auto d-flex">
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
                                <a href="https://twitter.com/intent/tweet?{{{url}}}&{{{hashtags}}}" target="_blank" class="text-secondary">
                                    <i class="fab fa-twitter fa-lg fa-fw"></i>
                                </a>
                                <a href="https://www.google.com/search?{{{search}}}" target="_blank" class="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fas fa-heart fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fas fa-random fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fas fa-play fa-lg fa-fw"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="col-4 mt-5 pr-5">
                        {{{sections}}}
                    </div>
                </div>
            </div>
        </div>
    </body>`, data);
    }
}