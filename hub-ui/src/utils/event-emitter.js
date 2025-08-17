class EventEmitter {
    constructor() {
        this.subs = Object.create(null) // 对象没有 原型属性   提高性能
    }

    // 注册事件
    $on(eventType, handler) {
        this.subs[eventType] = this.subs[eventType] || []
        this.subs[eventType].push(handler)
    }
    //触发事件
    $emit(eventType) {
        if (this.subs[eventType]) {
            this.subs[eventType].forEach(handler => {
                handler()
            })
        }
    }
}

const em = new EventEmitter()

export default em
