package team.waggly.backend.security

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import team.waggly.backend.commomenum.HttpMethodEnum
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class FilterSkipMatcher() : RequestMatcher {
    private lateinit var orRequestMatcher: OrRequestMatcher
    private lateinit var processingMatcher: RequestMatcher

    constructor(pathToSkip: MutableList<Pair<HttpMethodEnum, String>>, processingPath: String) : this() {
        this.orRequestMatcher = OrRequestMatcher(
                pathToSkip
                        .stream()
                        .map(this::httpPath)
                        .collect(Collectors.toList()) as List<RequestMatcher>?
        )

        this.processingMatcher = AntPathRequestMatcher(processingPath)
    }

    private fun httpPath(skipPath: Pair<HttpMethodEnum, String>): AntPathRequestMatcher {
        return AntPathRequestMatcher(skipPath.second, skipPath.first.toString())
    }

    override fun matches(request: HttpServletRequest?): Boolean {
        return !orRequestMatcher.matches(request) && processingMatcher.matches(request)
    }
}