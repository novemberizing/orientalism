import Mustache from 'mustache';

export default class TemplateIndex {
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
                    <div class="col-4 mt-5 pr-5">
                        book
                    </div>
                </div>
            </div>
        </div>
    </body>`, data);
    }
}