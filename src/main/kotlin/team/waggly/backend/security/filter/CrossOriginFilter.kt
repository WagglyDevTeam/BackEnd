package team.waggly.backend.security.filter

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@Component
//@Order(1)
//class CrossOriginFilter : Filter {
//    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
//        val response = response as HttpServletResponse
//        response.setHeader("Access-Control-Allow-Origin", "*")
//        response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS, GET, DELETE, PUT, PATCH")
//        response.setHeader("Access-Control-Max-Age", "3600")
//        response.setHeader(
//                "Access-Control-Allow-Headers",
//                "x-requested-with, origin, content-type, accept, Authorization"
//        )
//
//        val request = request as HttpServletRequest
//        if ("OPTIONS" == request.method) {
//            response.status = HttpServletResponse.SC_OK
//        } else {
//            chain?.doFilter(request, response)
//        }
//    }
//}