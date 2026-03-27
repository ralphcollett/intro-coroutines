package tasks

import contributors.*
import kotlin.collections.fold

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service
        .getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .body() ?: emptyList()

    repos.withIndex().fold(emptyList<User>()) { acc, (index, repo) ->
        val progress =
            (acc + service.getRepoContributors(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
            ).aggregate()

        updateResults(progress, index == repos.lastIndex)
        progress
    }
}
