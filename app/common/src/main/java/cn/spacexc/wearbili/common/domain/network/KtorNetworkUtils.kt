package cn.spacexc.wearbili.common.domain.network

import cn.spacexc.wearbili.common.domain.log.logd
import cn.spacexc.wearbili.common.domain.network.cookie.KtorCookiesManager
import cn.spacexc.wearbili.common.domain.network.dto.BasicResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.cookies.cookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.userAgent
import io.ktor.serialization.gson.gson
import javax.inject.Inject

/**
 * Created by XC-Qan on 2023/3/22.
 * I'm very cute so please be nice to my code!
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 * 给！爷！写！注！释！
 */

const val USER_AGENT =
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36"
const val BASE_URL = "https://www.bilibili.com" //Referer必须符合^https?://(\S+).bilibili.com

class KtorNetworkUtils @Inject constructor(private val cookiesManager: KtorCookiesManager) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson {
                serializeNulls()
                enableComplexMapKeySerialization()
            }
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = cookiesManager
        }
        expectSuccess = true
    }


    suspend inline fun <reified T> get(url: String): NetworkResponse<T> {
        return try {
            val response = client.get(url) {
                userAgent(USER_AGENT)
                header("Referer", BASE_URL)
            }
            response.request.headers.logd("request headers for $url")
            return if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                val body = response.body<BasicResponseDto>()
                NetworkResponse.Failed(code = body.code, message = body.message, null)
            }
        } /*catch (e: SocketException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonSyntaxException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonConvertException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: UnresolvedAddressException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } */ catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }
    }

    suspend inline fun <reified T> get(
        url: String,
        builder: HttpRequestBuilder.() -> Unit
    ): NetworkResponse<T> {
        return try {
            val response = client.get(url) {
                userAgent(USER_AGENT)
                header("Referer", BASE_URL)
                builder()
            }
            response.request.headers.logd("request headers for $url")
            return if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                val body = response.body<BasicResponseDto>()
                NetworkResponse.Failed(code = body.code, message = body.message, null)
            }
        } /*catch (e: SocketException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonSyntaxException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonConvertException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: UnresolvedAddressException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } */ catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }
    }

    suspend inline fun <reified T> post(
        url: String,
        form: Map<String, String>
    ): NetworkResponse<T> {
        val params = Parameters.build {
            form.forEach {
                append(it.key, it.value)
            }
        }
        return try {
            val response = client.submitForm(formParameters = params, url = url) {
                userAgent(USER_AGENT)
                header("Referer", BASE_URL)
            }
            return if (response.status == HttpStatusCode.OK) {
                NetworkResponse.Success(response.body())
            } else {
                val body = response.body<BasicResponseDto>()
                NetworkResponse.Failed(code = body.code, message = body.message, null)
            }
        } /*catch (e: SocketException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonSyntaxException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: JsonConvertException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        } catch (e: UnresolvedAddressException) {
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }*/ catch (e: Exception) {
            e.printStackTrace()
            NetworkResponse.Failed(code = -1, message = e.message ?: "Unknown error", null)
        }
    }

    suspend fun getCookie(name: String): String? {
        return client.cookies(BASE_URL).find { it.name == name }?.value
    }

    suspend fun getCookies(): Map<String, String> {
        return client.cookies(BASE_URL).associate {
            Pair(first = it.name, second = it.value)
        }
    }
}