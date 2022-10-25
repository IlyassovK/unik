package com.example.students.utils.exceptions

object GenericExceptions{
    class UnsuccessfulOperation(description: String? = "Unsuccessful operation"): RuntimeException(description)
}