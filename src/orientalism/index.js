import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import qs from 'query-string';
import Opengraph from '../util/opengraph';

export default class OrientalismView extends React.Component {
    render() {
        console.log(this.props);
        console.log(this.props.location.search);
        console.log(qs.parse(this.props.location.search));

        const query = qs.parse(this.props.location.search);

        return (
            <div className="row h-100 m-0 p-0 my-auto d-flex">
                <Opengraph title={query.book}
                           type="website"
                           url={`https://novemberizing.github.io/${this.props.location.search}`}
                           image={`https://novemberizing.github.io/example.png`}>
                </Opengraph>
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
        );
    }
}