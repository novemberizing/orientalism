
export default class Card {
    constructor(type) {
        this._type = type;
    }
    
    html(){
        throw new Error();
    }
}