package com.onesignal.core.internal.modeling

import com.onesignal.core.internal.common.events.EventProducer
import com.onesignal.core.internal.common.events.IEventNotifier
import com.onesignal.core.internal.common.events.IEventProducer
import kotlin.collections.HashMap

internal open class Model(
    private val _changeNotifier: IEventProducer<IModelChangedHandler> = EventProducer()
) : IEventNotifier<IModelChangedHandler> by _changeNotifier {

    var id: String
        get() = get(::id.name)
        set(value) { set(::id.name, value) }

    private val _data: HashMap<String, Any?> = HashMap()

    fun <T> set(name: String, value: T, notify: Boolean = true) {
        val oldValue = _data[name]
        val newValue = value as Any?

        _data[name] = newValue

        if (notify) {
            val changeArgs = ModelChangedArgs(this, name, oldValue, newValue)
            _changeNotifier.fire { it.onChanged(changeArgs) }
        }
    }

    protected fun <T> get(name: String): T {
        return _data[name] as T
    }

    protected fun <T> get(name: String, create: () -> T): T {
        return if (_data.containsKey(name)) {
            _data[name] as T
        } else {
            val defaultValue = create()
            _data[name] = defaultValue as Any?
            defaultValue
        }
    }
}