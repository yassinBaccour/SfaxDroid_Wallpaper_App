package com.sfaxdroid.data.call

sealed class Result<out T> {
  data class Success<out T>(val data: T) : Result<T>()
  data class Failure(val error: Error) : Result<Nothing>()
}

inline fun <R, T : R> Result<T>.getOrElses(onFailure: (Error) -> R): R = when (this) {
  is Result.Success -> data
  is Result.Failure -> onFailure(error)
}

inline fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R = when (this) {
  is Result.Success -> data
  is Result.Failure -> defaultValue
}

inline fun <T> Result<T>.getOrNull(): T? = when (this) {
  is Result.Success -> data
  is Result.Failure -> null
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result <T> = also {
  if (this is Result.Success) action(data)
}
