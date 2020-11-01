
export default class Reacts {
    static render(condition, component) {
        return condition && component;
    }
    static map(o, func) {
        if(o instanceof Array) {
            return o.map(func);
        }
        return null;
    }
}