package tasks

import contributors.User

fun List<User>.aggregate(): List<User> =
    groupBy { it.login }.map { (login, allContributions) ->
        User(login, allContributions.sumOf { it.contributions })
    }.sortedByDescending { it.contributions }