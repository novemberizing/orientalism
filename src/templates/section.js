import fs from 'fs';
import path from 'path';
import qs from 'querystring';
import urlencode from 'urlencode';
import Mustache from 'mustache';

import Website from '../website';

export default class TemplateSection {
    static gen(o , output) {
        const html = TemplateSection.html(o);

        fs.mkdirSync(path.dirname(path.resolve(output)), {recursive: true});
        fs.writeFileSync(output + '.html', html);

        if(path.basename(output) !== 'index') {
            fs.writeFileSync(output + '.json', JSON.stringify(o.source));
        } else {
            fs.writeFileSync(output + '.json', JSON.stringify(o.sections));
        }

        return html;
    }

    static html(o) {
        const input = Object.assign(JSON.parse(fs.readFileSync("./.conf/global.json")), o.source, {sections: o.sections});
        const twitter = JSON.parse(fs.readFileSync("./.conf/twitter.json"));
        const opengraph = JSON.parse(fs.readFileSync("./.conf/opengraph.json"));
        const scripts = JSON.parse(fs.readFileSync("./.conf/scripts.json"));
        const styles = JSON.parse(fs.readFileSync("./.conf/styles.json"));

        if(input.publicPath) {
            Website.publicPath = input.publicPath;
        }

        const body = TemplateSection.body(Object.assign(o, input, twitter, opengraph, scripts, styles));
        
        return Website.gen(Website.meta(input, twitter, opengraph), styles, scripts, body);
    }

    static tag(book, category, section, o) {
        const badge = section === o ? "btn btn-outline-success" : "btn btn-outline-secondary";
        return `<a href="/orientalism/${book}/${category}/${o}.html" class="${badge} p0 mt-1"><p class="font-weight-bold m0 p0 d-inline">${o}</p></a>`;
    }

    static body(meta) {
        const data = {
            book: meta.source.book,
            category: meta.source.category,
            section: meta.source.section,
            prefix: meta.source.prefix,
            content: meta.source.content,
            description: meta.source.description,
            url: qs.stringify({url: meta.publicPath + '/' + urlencode(meta.source.book) + '/' + urlencode(meta.source.category) + '/' + urlencode(meta.source.section) + '.html'}),
            hashtags: qs.stringify({hashtags: meta.source.section}),
            search: qs.stringify({q: meta.source.content}),
            translate: qs.stringify({sl: "ko", tl: "en", text: meta.source.description, op: "translate"}),
            sections: meta.sections.map(o => TemplateSection.tag(meta.source.book, meta.source.category, meta.source.section, o.section)).join(' ')
        };

        return Mustache.render(`<body>
        <div id="root">
            <div class="all">
                <div class="row h-100 w-100 m-0 p-0 my-auto d-flex">
                    <div class="col-lg-8 col-md-12 px-0">
                        <div class="row">
                            <div class="col-lg-2 col-md-12 pr-0">
                                <h6 class="text-chinese text-chinese-book">
                                    {{book}} / {{category}}
                                </h6>
                            </div>
                            <div class="col-lg-10">
                            </div>
                        </div>
                        <hr class="row mt-0 mr-1" />
                        <div class="row">
                            <div class="col-lg-2 col-md-12">
                                <h4 class="text-chinese text-chinese-prefix">
                                    {{prefix}}
                                </h4>
                            </div>
                            <div class="col-lg-10 col-md-12">
                                <div class="row">
                                    <div class="col-lg-12 col-md-12">
                                        <h1 class="text-chinese text-chinese-content">
                                            {{content}}
                                        </h1>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-12 col-md-12 text-chinese text-chinese-description p-5">
                                        {{description}}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <hr class="row mb-2 mr-1" />
                        <div class="row text-right">
                            <div class="col-md-12 col-lg-12 text-right pr-4">
                                <a href="#" class="text-secondary"><i class="far fa-question-circle fa-lg fa-fw"></i></a>
                                <a href="https://twitter.com/intent/tweet?{{{url}}}&{{{hashtags}}}" target="_blank" class="text-secondary">
                                    <i class="fab fa-twitter fa-lg fa-fw"></i>
                                </a>
                                <a href="https://www.google.com/search?{{{search}}}" target="_blank" class="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                                <a href="https://translate.google.co.kr/?{{{translate}}}" target="_blank" class="text-secondary">
                                    <i class="fas fa-language fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fas fa-heart fa-lg fa-fw"></i>
                                </a>
                                <a href="#" class="text-secondary">
                                    <i class="fas fa-random fa-lg fa-fw"></i>
                                </a>
                                <a id="orientalism-play-btn" href="javascript:void(0);" class="text-secondary">
                                    <i class="fas fa-play fa-lg fa-fw"></i>
                                </a>
                                <script>
                                    if(status.play === 'on') {
                                        document.getElementById('orientalism-play-btn').className = 'text-success';
                                        document.getElementById('orientalism-play-btn').addEventListener('click', orientalismStop);
                                    } else {
                                        document.getElementById('orientalism-play-btn').className = 'text-secondary';
                                        document.getElementById('orientalism-play-btn').addEventListener('click', orientalismPlay);
                                    }
                                </script>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-4 col-md-12 p-2 book-panel">
                        {{{sections}}}
                    </div>
                </div>
            </div>
        </div>
    </body>`, data);
    }
}