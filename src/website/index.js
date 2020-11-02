/**
 * 
 * @author  novemberizing <novemberizing@gmail.com>
 * @version 0.0.1
 */

export default class Website {
    static gen(option) {
        return `<!DOCTYPE html>
<html lang="${option.lang}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="${option.description}">
        <meta name="author" content="${option.author}">
        <meta name="generator" content="${option.generator}">
        <meta name="docsearch:language" content="${option.docsearch.language}">
        <meta name="docsearch:version" content="${option.docsearch.version}">
        <meta name="twitter:card" content="${option.twitter.card}">
        <meta name="twitter:site" content="${option.twitter.site}">
        <meta name="twitter:creator" content="${option.twitter.creator}">
        <meta name="twitter:title" content="${option.twitter.title}">
        <meta name="twitter:description" content="${option.twitter.description}">
        <meta name="twitter:image" content="${option.twitter.image}">

        <meta property="og:url" content="${option.og.url}">
        <meta property="og:title" content="${option.og.title}">
        <meta property="og:description" content="${option.og.description}">
        <meta property="og:type" content="website">
        <meta property="og:image" content="${option.og.image.url}">
        <meta property="og:image:type" content="${option.og.image.type}">
        <meta property="og:image:width" content="${option.og.image.width}">
        <meta property="og:image:height" content="${option.og.image.height}">

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    </head>
    <body>
        <div id="root">
            <div class="all">
                <div className="row h-100 m-0 p-0 my-auto d-flex">
                    <div className="col-lg-8 col-md-12 px-0">
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h6 className="text-chinese text-right">
                                    {query.book} / {query.category}
                                </h6>
                            </div>
                            <div className="col-10">
                            </div>
                        </div>
                        <hr className="row mt-0 mr-1" />
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h4 className="text-chinese text-right">
                                    子曰
                                </h4>
                            </div>
                            <div className="col-10">
                                <h1 className="text-chinese">
                                    學而時習之 不亦說乎 有朋自遠方來 不亦樂乎 人不知而不慍 不亦君子乎
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
                            {/* <div className="col-10 text">
                                
                            </div> */}
                        </div>
                    </div>
                    <div className="col-4 mt-5 pr-5">
                        {/* 공자께서 말씀하시길 "배우고 때때로 익히니 또한 기쁘지 아니한가? 벗이 있어 먼 곳으로부터 찾아오면 또한 즐겁지 아니한가? 남이 알아주지 않아도 노여워하지 않는다면 또한 군자라 하지 않겠는가?" */}
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
`;
    }
}