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
        const input = Object.assign(JSON.parse(fs.readFileSync("./global.json")), o);
        const twitter = JSON.parse(fs.readFileSync("./twitter.json"));
        const opengraph = JSON.parse(fs.readFileSync("./opengraph.json"));
        const scripts = JSON.parse(fs.readFileSync("./scripts.json"));
        const styles = JSON.parse(fs.readFileSync("./styles.json"));
        
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
                <div className="row h-100 m-0 p-0 my-auto d-flex">
                    <div className="col-lg-8 col-md-12 px-0">
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h6 className="text-chinese text-right">
                                    {{book}} / {{category}} / {{section}}
                                </h6>
                            </div>
                            <div className="col-10">
                            </div>
                        </div>
                        <hr className="row mt-0 mr-1" />
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h4 className="text-chinese text-right">
                                    {{prefix}}
                                </h4>
                            </div>
                            <div className="col-10">
                                <h1 className="text-chinese">
                                    {{content}}
                                </h1>
                            </div>
                        </div>
                        <hr className="row mb-2 mr-1" />
                        <div className="row text-right">
                            <div className="col-12 text-right">
                                <a href="/orientalism" className="text-secondary">
                                    <i class="fab fa-twitter fa-lg fa-fw"></i>
                                </a>
                                <a href="#" className="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                                <a href="#" className="text-secondary">
                                    <i class="fab fa-google fa-lg fa-fw"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div className="col-4 mt-5 pr-5"></div>
                </div>
            </div>
        </div>
    </body>`, data);
    }
}

const source = './example.json';
const destination = './docs/論語/學而/學而時習之.html';

console.log(Orientalism.gen(source, destination));

// console.log(Orientalism.html({
//     content: '學而時習之 不亦說乎 有朋自遠方來 不亦樂乎 人不知而不慍 不亦君子乎',
//     prefix: '子曰',
//     description: '공자가 말씀하시기를, 이미 배운 것을 때때로 반복하여 복습하면 이 또한 기쁜 일이 아니겠는가? 뜻을 같이 하는 친구가 있어 멀리서 찾아온다면 이 또한 즐거운 일이 아니겠는가? 다른 사람이 나의 학문과 능력을 알아주지 않아도 화를 내지 않으면 이 또한 수양을 쌓은 군자라 하지 않겠는냐?',
//     book: '論語',
//     category: '學而',
//     section: '學而時習之'
// }));
