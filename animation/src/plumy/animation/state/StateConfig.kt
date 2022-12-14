package plumy.animation.state

typealias SwitchStateEvent<T> = (m: StateMachine<T>, from: State<T>, to: State<T>) -> Unit

/**
 * The configuration of an Animation State Machine
 */
class StateConfig<T> {
    /**
     * Key --to--> When can go to the next State
     */
    val entranceConditions = HashMap<Any, T.() -> Boolean>()
    /**
     * Current State --to--> The next all possible State
     */
    val allEntrances = HashMap<State<T>, ArrayList<State<T>>>()
    /**
     * Call when [StateMachine] changed its [StateMachine.curState]
     */
    var onSwitchState: SwitchStateEvent<T>? = null
    /**
     * [NotNull,lateinit] The default and initial State.
     * @throws IllegalStateException thrown when this configuration has already built
     */
    var defaultState: State<T>? = null
        set(value) {
            checkBuilt()
            field = value
        }
    /**
     * Whether this configuration has built
     */
    var built = false
        private set
    /**
     * @throws IllegalStateException thrown when this configuration has already built
     */
    var transition: TransitionEffect = SmoothFade
        set(value) {
            checkBuilt()
            field = value
        }
    /**
     * @throws IllegalStateException thrown when this configuration has already built
     */
    var transitionDuration: Float = 30f
        set(value) {
            checkBuilt()
            field = value
        }
    /**
     * Check whether this Animation Config has been built.
     * @throws IllegalStateException thrown when this configuration has already built
     */
    private fun checkBuilt() {
        if (built) {
            throw IllegalStateException("$this has already built.")
        }
    }
    /**
     * Creates an entry
     *
     * @param from     the current State
     * @param to       the next State
     * @param canEnter When the State Machine can go from current State to next State
     * @return this
     * @throws IllegalStateException    thrown when this configuration has already built
     * @throws IllegalArgumentException thrown when `from` equals to `to`
     */
    fun entry(
        from: State<T>, to: State<T>,
        canEnter: T.() -> Boolean,
    ): StateConfig<T> {
        checkBuilt()
        if (from == to || from.stateName == to.stateName) {
            throw IllegalArgumentException("from[$from] is equal to to[$to].")
        }
        val key = genKey(from, to)
        entranceConditions[key] = canEnter
        val entrances = allEntrances.getOrPut(from) { ArrayList() }
        entrances.add(to)
        return this
    }
    /**
     * Builds the configuration. And this cannot be modified anymore.
     *
     * @return this
     * @throws IllegalStateException thrown when the default State hasn't been set yet
     */
    fun build(): StateConfig<T> {
        if (defaultState == null) {
            throw IllegalStateException("$this has no default state.")
        }
        built = true
        return this
    }
    /**
     * Instantiate the [StateMachine]
     *
     * @param build which has the State Machine
     * @return an Animation State Machine
     * @throws IllegalStateException thrown when this hasn't built yet
     */
    fun instantiate(build: T): StateMachine<T> {
        if (!built) {
            throw IllegalStateException("$this has not built.")
        }
        return StateMachine(this, build)
    }
    /**
     * Gets the condition for entering the `to` State from `from`
     *
     * @param from the current State
     * @param to   the next State
     * @return if the key of `form`->`to` exists, return the condition. Otherwise, return null.
     */
    fun getEntranceCondition(from: State<T>, to: State<T>): (T.() -> Boolean)? {
        return entranceConditions[genKey(from, to)]
    }
    /**
     * Gets all possible States that `from` State can enter
     *
     * @param from the current State
     * @return a collection of States
     */
    fun getAllEntrances(from: State<T>): List<State<T>> {
        return allEntrances.getOrPut(from) { ArrayList() }
    }

    companion object {
        /**
         * Calculates the key of two ordered States
         *
         * @param from the current State
         * @param to   the next State
         * @return the key
         */
        fun genKey(from: State<*>, to: State<*>): Any {
            return from.hashCode() xor to.hashCode() * 2
        }
    }
}