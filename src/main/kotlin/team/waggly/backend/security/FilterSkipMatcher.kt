package team.waggly.backend.security

import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

class FilterSkipMatcher() : RequestMatcher {
    private lateinit var orRequestMatcher: OrRequestMatcher
    private lateinit var processingMatcher: RequestMatcher

    constructor(pathToSkip: MutableList<String>, processingPath: String) : this() {
        this.orRequestMatcher = OrRequestMatcher(
                pathToSkip
                        .stream()
                        .map(this::httpPath)
                        .collect(Collectors.toList()) as List<RequestMatcher>?
        )

        this.processingMatcher = AntPathRequestMatcher(processingPath)
    }

    private fun httpPath(skipPath: String): AntPathRequestMatcher {
        val splitStr = skipPath.split(",")

        return AntPathRequestMatcher(splitStr[1], splitStr[0])
    }

    override fun matches(request: HttpServletRequest?): Boolean {
        return !orRequestMatcher.matches(request) && processingMatcher.matches(request)
    }
}