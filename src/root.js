import React from 'react';

export default class Root extends React.Component {
    render() {
        return (
            <div className="all">
                <div className="row h-100 m-0 p-0 my-auto d-flex">
                    <div className="col-lg-8 col-md-12 px-0">
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h6 className="text-chinese text-right">
                                    論語 / 學而
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
                    </div>
                    <div className="col-4 mt-5 pr-5">
                        {/* 공자께서 말씀하시길 "배우고 때때로 익히니 또한 기쁘지 아니한가? 벗이 있어 먼 곳으로부터 찾아오면 또한 즐겁지 아니한가? 남이 알아주지 않아도 노여워하지 않는다면 또한 군자라 하지 않겠는가?" */}
                    </div>
                </div>
            </div>
        );
    }
}
