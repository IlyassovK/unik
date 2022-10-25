package com.example.students.utils.exceptions

import java.io.IOException

object NetworkExceptions {

    class BadRequest(description: String = "Something went wrong") : RuntimeException(description)

    class EmptyBody(description: String = "Server returned an empty body") : RuntimeException(description)

    class ConnectivityError(description: String = "No internet connection") : IOException(description)

    class SessionOverException(description: String = "Access denied") : RuntimeException(description)

    class ErrorMessage(description: String = "Something went wrong") : IOException(description)

    class ServiceException(description: String? = "Service exception") : RuntimeException(description)

    object Login {
        class PassCodeDisabled(description: String):RuntimeException(description)
        class TouchIdDisabled(description: String):RuntimeException(description)
        class WrongCredentials(description: String = "Wrong credentials. Can not login") :
            RuntimeException(description)
        class AccountBlocked(description: String = "Too many login attempt") :
            RuntimeException(description)
        class InvalidPassCode(description: String = "Invalid pass code") :
            RuntimeException(description)
        class PassCodeBlocked(description: String = "Pass code blocked") :
            RuntimeException(description)
        class AutoOtpFailure(description: String = "Отправка СМС-кода...") : RuntimeException(description)
        class WrongIIN(description: String = "ИИН введен некорректно") : RuntimeException(description)
        class RecoverIINNotAllowed() : RuntimeException()
    }

    object Payment {
        class UnsufficientFunds(description: String = "Недостаточно средств") : RuntimeException(description)
    }
    object OTP {
        class OtpCodeInvalid(description: String = "OTP code invalid") :
            RuntimeException(description)
        class OtpCodeCountExceeded(description: String = "OTP code count exceeded") :
            RuntimeException(description)
    }
}