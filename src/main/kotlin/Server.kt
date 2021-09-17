import com.auth0.jwt.JWT
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kotlinx.html.*

fun HTML.index() {
    head {
        title("Hello from Ktor!")
    }
    body {
        div {
            +"Hello from Ktor"
        }
    }
}

const val AUTH_KEYCLOAK = "auth-keycloak"
private val dotenv = Dotenv.load()

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(Sessions) {
            cookie<UserSession>("user_session")
        }
        val httpClient = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
        install(Authentication) {
            oauth(AUTH_KEYCLOAK) {
                urlProvider = { "http://localhost:8080/callback" }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "keycloak",
                        authorizeUrl = dotenv["KEYCLOAK_AUTHORIZE_URL"],
                        accessTokenUrl = dotenv["KEYCLOAK_ACCESS_TOKEN_URL"],
                        requestMethod = HttpMethod.Post,
                        clientId = dotenv["KEYCLOAK_CLIENT_ID"],
                        clientSecret = dotenv["KEYCLOAK_CLIENT_SECRET"],
                        defaultScopes = listOf("profile", "roles")
                    )
                }
                client = httpClient
            }
        }

        routing {
            authenticate(AUTH_KEYCLOAK) {
                get("/login") {
                    // Redirects to 'authorizeUrl' automatically
                }

                get("/callback") {
                    val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                    call.sessions.set(UserSession.fromJwtToken(principal?.accessToken.toString()))
                    call.respondRedirect("/hello")
                }
            }
            get("/") {
                call.respondHtml {
                    body {
                        p {
                            a("/login") { +"Login with Keycloak" }
                        }
                    }
                }
            }
            get("/hello") {
                val userSession: UserSession? = call.sessions.get<UserSession>()
                if (userSession != null) {
                    call.respondText("Hello, ${userSession.name}! email=${userSession.email}")
                } else {
                    call.respondRedirect("/")
                }
            }
        }
    }.start(wait = true)
}

data class UserSession(val name: String, val email: String) {
    companion object {
        fun fromJwtToken(token: String): UserSession {
            val decoded = JWT.decode(token)
            val name = decoded.getClaim("name").toString()
            val email = decoded.getClaim("email").toString()
            return UserSession(name, email)
        }
    }
}
