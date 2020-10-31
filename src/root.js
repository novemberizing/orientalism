import React from 'react';

export default class Root extends React.Component {
    render() {
        return (
            <div className="all">
                <div className="row h-100 m-0 p-0 my-auto">
                    <div className="col-9 px-0">
                        <div className="row">
                            <div className="col-2 pr-0">
                                <h6 className="text-chinese text-right">
                                    論語 / 學而
                                </h6>
                            </div>
                            <div className="col-10">
                            </div>
                        </div>
                        <hr className="row mt-0" />
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
                    <div className="col-3">
                    </div>
                    {/* <div className="col-1 text-right font-weight-bold px-0">

                    </div>
                    <div className="col-8 text-left font-weight-bold my-auth">
                        <h1 className="text-chinese">
                            學而時習之 不亦說乎 有朋自遠方來 不亦樂乎 人不知而不慍 不亦君子乎
                        </h1>
                    </div>
                    <div className="col-3 text-left my-auth">
                        h
                    </div> */}
                </div>
            </div>
        );
    }
}
