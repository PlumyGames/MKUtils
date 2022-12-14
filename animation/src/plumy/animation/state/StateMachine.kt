package plumy.animation.state

import arc.graphics.g2d.Draw
import plumy.animation.ContextDraw.RESET_CONTEXT
import plumy.animation.ITimer

interface IStateful<T> {
    val stateMachine: StateMachine<T>
}

class StateMachine<T>(
    val config: StateConfig<T>,
    val entity: T,
) : ITimer {
    var curState: State<T> = config.defaultState
        ?: throw IllegalArgumentException("StateConfig's defaultState is null.")
    var lastState: State<T>? = null
    var lastSwitchTime = 0f
    var curTime = 0f
    override fun spend(time: Float) {
        curTime += time
    }

    fun draw() {
        val delta = curTime - lastSwitchTime
        val progress = (delta / config.transitionDuration).coerceIn(0f, 1f)
        config.transition(progress, {
            lastState?.draw(entity)
        }, {
            curState.draw(entity)
        })
        Draw.reset()
        RESET_CONTEXT()
    }

    fun updateState() {
        for (to in config.getAllEntrances(curState)) {
            val canEnter = config.getEntranceCondition(curState, to)
            if (canEnter != null && canEnter(entity)) {
                config.onSwitchState?.invoke(this, curState, to)
                lastState = curState
                curState = to
                lastSwitchTime = curTime
                return
            }
        }
    }
}