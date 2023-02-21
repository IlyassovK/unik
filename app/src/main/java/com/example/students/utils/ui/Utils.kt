package com.example.students.utils.ui

object Utils {
    object Validation {
        /** Регулярное выражение полного незамаскированного номера карты. */
        private val CARD_NUMBER_REGEX = Regex("\\d{16}")

        /** Номер карты может начинаться с этих цифр (соответствуют платежной системе). */
        private val ALLOWED_FIRST_DIGITS = listOf(2, 4, 5, 6)

        fun isPhoneNumberValid(number: String): Boolean {
            return android.util.Patterns.PHONE.matcher(number).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            password.let {
                val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{6,}$"
                val passwordMatcher = Regex(passwordPattern)

                return passwordMatcher.find(password) != null
            }
        }

        fun isPasswordLengthValid(password: String): Boolean {
            return password.length >= 8
        }

        fun isValidOtp(otpText: String, length: Int): Boolean {
            return otpText.length == length && otpText.toIntOrNull() != null
        }

        fun isIinValid(iin: String): Boolean {
            return iin.length == 12
        }

        fun isCardValid(cardNumber: String): Boolean {
            val cn = cardNumber.replace(" ", "")

            return cn.length >= 13 && cn.matches(CARD_NUMBER_REGEX)
                    && cn.substring(0, 1).toInt() in ALLOWED_FIRST_DIGITS
        }
    }
}