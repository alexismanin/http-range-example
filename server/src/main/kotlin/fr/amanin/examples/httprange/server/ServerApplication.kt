package fr.amanin.examples.httprange.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@SpringBootApplication
class ServerApplication {

	@Bean
	fun contentRouter() : RouterFunction<ServerResponse> {
		return RouterFunctions.resources("/content/**", FileSystemResource("content/"))
	}
}

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}

