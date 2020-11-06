import Mustache from 'mustache';

export default class Orientalism {
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
                                    <FontAwesomeIcon icon={["fab", "twitter"]} fixedWidth size="lg" />
                                </a>
                                <a href="#" className="text-secondary">
                                    <FontAwesomeIcon icon={["fab", "google"]} fixedWidth size="lg" />
                                </a>
                                <a href="#" className="text-secondary">
                                    <FontAwesomeIcon icon={["fas", "heart"]} fixedWidth size="lg" />
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