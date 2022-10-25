package com.example.students.utils

class Resource<T> private constructor(
    val state: State,
    val data: T?,
    val error: Throwable?,
    val source: Source = Source.SERVER
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    enum class Source {
        CACHE,
        SERVER
    }

    companion object {
        fun <T> loading(source: Source = Source.SERVER): Resource<T> =
            Resource(
                state = State.LOADING,
                data = null,
                error = null,
                source = source
            )

        fun <T> success(data: T, source: Source = Source.SERVER): Resource<T> =
            Resource(
                state = State.SUCCESS,
                data = data,
                error = null,
                source = source
            )

        fun <T> error(error: Throwable?, source: Source = Source.SERVER, data: T? = null): Resource<T> =
            Resource(
                state = State.ERROR,
                data = data,
                error = error,
                source = source
            )

        fun <T> custom(state: State, data: T?, error: Throwable?, source: Source): Resource<T> =
            Resource(
                state = state,
                data = data,
                error = error,
                source = source
            )
    }

    override fun toString(): String {
        return "Resource(state=$state, data=$data, error=$error, source=$source)"
    }
}

fun <T> Resource<T>?.isSuccessful(): Boolean = this?.state == Resource.State.SUCCESS
fun <T> Resource<T>?.isCached(): Boolean = this?.source == Resource.Source.CACHE
